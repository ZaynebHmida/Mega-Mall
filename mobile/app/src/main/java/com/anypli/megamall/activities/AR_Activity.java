package com.anypli.megamall.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.location.LocationManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.anypli.megamall.R;
import com.anypli.megamall.ar.core.helpers.*;
import com.anypli.megamall.ar.core.rendering.BackgroundRenderer;
import com.anypli.megamall.ar.core.rendering.MarkerRenderer;
import com.anypli.megamall.contracts.ARContract;
import com.anypli.megamall.models.BoutiqueLocation;
import com.anypli.megamall.presenters.ARPresenterImpl;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Camera;
import com.google.ar.core.CameraConfig;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingFailureReason;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableDeviceNotCompatibleException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AR_Activity extends AppCompatActivity implements GLSurfaceView.Renderer ,ARContract.ViewItf {

    private static final String TAG = AR_Activity.class.getSimpleName();
    private static final String INSUFFICIENT_FEATURES_MESSAGE =
            "Can't find anything. Aim device at a surface with more texture or color.";
    private static final String EXCESSIVE_MOTION_MESSAGE = "Moving too fast. Slow down.";
    private static final String INSUFFICIENT_LIGHT_MESSAGE =
            "Too dark. Try moving to a well-lit area.";
    private static final String BAD_STATE_MESSAGE =
            "Tracking lost due to bad internal state. Please try restarting the AR experience.";


    // Rendering. The Renderers are created here, and initialized when the GL surface is created.
    private GLSurfaceView surfaceView;

    private boolean installRequested;

    private Session session;
    private final SnackbarHelper messageSnackbarHelper = new SnackbarHelper();
    private DisplayRotationHelper mDisplayRotationHelper;
    private LocalisationHelpers mLocalisationHelper ;

    float[] projmtx = new float[16];
    float[] viewmtx = new float[16];
    final float[] colorCorrectionRgba = new float[4];

    private final BackgroundRenderer backgroundRenderer = new BackgroundRenderer();
    private MarkerRenderer MarkerObject =new MarkerRenderer() ;

    private ARContract.PresenterItf mPresenter = new ARPresenterImpl(this);
    private ArrayList<BoutiqueLocation> mLocations = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_layout);
        surfaceView = findViewById(R.id.surfaceview);
        mDisplayRotationHelper = new DisplayRotationHelper(this);
        mLocalisationHelper = new LocalisationHelpers(this);
        mLocalisationHelper.LocalisationUpdateConfig( 100, 0);

        // Set up renderer.
        surfaceView.setPreserveEGLContextOnPause(true);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Alpha used for plane blending.
        surfaceView.setRenderer(this);
        surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        surfaceView.setWillNotDraw(false);
        installRequested = false;
        mPresenter.getAllBoutiques();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (session == null) {
            Exception exception = null;
            String message = null;
            try {
                switch (ArCoreApk.getInstance().requestInstall(this, !installRequested)) {
                    case INSTALL_REQUESTED:
                        installRequested = true;
                        return;
                    case INSTALLED:
                        break;
                }

                // ARCore requires camera permissions to operate. If we did not yet obtain runtime
                // permission on Android M and above, now is a good time to ask the user for it.
                if (!CameraPermissionHelper.hasCameraPermission(this)) {
                    CameraPermissionHelper.requestCameraPermission(this);
                    return;
                }

                if (!LocalisationPermissionHelper.hasLocalisationPermission(this)) {
                    LocalisationPermissionHelper.requestLocalisationPermission(this);
                    return;
                }
                // Create the session.
                session = new Session(/* context= */ this);


            } catch (UnavailableArcoreNotInstalledException
                    | UnavailableUserDeclinedInstallationException e) {
                message = "Please install ARCore";
                exception = e;
            } catch (UnavailableApkTooOldException e) {
                message = "Please update ARCore";
                exception = e;
            } catch (UnavailableSdkTooOldException e) {
                message = "Please update this app";
                exception = e;
            } catch (UnavailableDeviceNotCompatibleException e) {
                message = "This device does not support AR";
                exception = e;
            } catch (Exception e) {
                message = "Failed to create AR session";
                exception = e;
            }

            if (message != null) {
                messageSnackbarHelper.showError(this, message);
                Log.e(TAG, "Exception creating session", exception);
                return;
            }
        }

        // Note that order matters - see the note in onPause(), the reverse applies here.
        try {
            session.resume();
        } catch (CameraNotAvailableException e) {
            // In some cases (such as another camera app launching) the camera may be given to
            // a different app instead. Handle this properly by showing a message and recreate the
            // session at the next iteration.
            messageSnackbarHelper.showError(this, "Camera not available. Please restart the app.");
            session = null;
            return;
        }

        surfaceView.onResume();
        mDisplayRotationHelper.onResume();
        mLocalisationHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (session != null) {
            // Note that the order matters - GLSurfaceView is paused first so that it does not try
            // to query the session. If Session is paused before GLSurfaceView, GLSurfaceView may
            // still call session.update() and get a SessionPausedException.
            mDisplayRotationHelper.onPause();
            surfaceView.onPause();
            session.pause();
        }
        mLocalisationHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalisationHelper.onDestroy();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if(requestCode==1) {
            if (!CameraPermissionHelper.hasCameraPermission(this)) {
                Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                        .show();
                if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                    // Permission denied with checking "Do not ask again".
                    CameraPermissionHelper.launchPermissionSettings(this);
                }
                finish();
            }
        }else if(requestCode==0){
            if(!LocalisationPermissionHelper.hasLocalisationPermission(this)){
                Toast.makeText(this, "Localisation permission is needed to run this application", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

        // Prepare the rendering objects. This involves reading shaders, so may throw an IOException.
        try {
            // Create the texture and pass it to ARCore session to be filled during update().
            backgroundRenderer.createOnGlThread(/*context=*/ this);
            //TODO: Create marker renderable Object here
            MarkerObject.createOnGlThread(/*context=*/ this, "models/marker.obj");
            MarkerObject.setMaterialProperties(0.0f, 2.0f, 0.5f, 6.0f);


        } catch (IOException e) {
            Log.e(TAG, "Failed to read an asset file", e);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mDisplayRotationHelper.onSurfaceChanged(width, height);
        GLES20.glViewport(0, 0, width, height);
        MatrixHelper.getProjectionMatrix(projmtx, 45.0f,((float)width)/height,1.0f,100.0f);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear screen to notify driver it should not load any pixels from previous frame.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if (session == null) {
            return;
        }
        // Notify ARCore session that the view size changed so that the perspective matrix and
        // the video background can be properly adjusted.
        mDisplayRotationHelper.updateSessionIfNeeded(session);

        try {
            session.setCameraTextureName(backgroundRenderer.getTextureId());

            // Obtain the current frame from ARSession. When the configuration is set to
            // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
            // camera framerate.
            Frame frame = session.update();
            Camera camera = frame.getCamera();


            // If frame is ready, render camera preview image to the GL surface.
            backgroundRenderer.draw(frame);
            messageSnackbarHelper.hide(this);

            frame.getLightEstimate().getColorCorrection(colorCorrectionRgba, 0);
            //camera.getProjectionMatrix(projmtx,0,1.0f,100.0f);
            if(mLocalisationHelper.isLocationChanged()){
                MatrixHelper.getViewMatrix(viewmtx, mLocalisationHelper.getRotation(),
                        (float)mLocalisationHelper.getLastKnownLocation().getLatitude(),0.0f,(float)mLocalisationHelper.getLastKnownLocation().getLongitude());
            ((TextView)findViewById(R.id.latview)).setText(String.valueOf(mLocalisationHelper.getLastKnownLocation().getLatitude()));
            ((TextView)findViewById(R.id.longview)).setText(String.valueOf(mLocalisationHelper.getLastKnownLocation().getLongitude()));

            }
            //TODO: insert Marker Render Calls here
            for(BoutiqueLocation location: mLocations){
                MarkerObject.setPosition(location.getPositionX(),0.0f,location.getPositionZ());
                MarkerObject.draw(viewmtx, projmtx, colorCorrectionRgba);
            }



        } catch (Throwable t) {
            // Avoid crashing the application due to unhandled exceptions.
            Log.e(TAG, "Exception on the OpenGL thread", t);
        }
    }

    public static String getTrackingFailureReasonString(Camera camera) {
        TrackingFailureReason reason = camera.getTrackingFailureReason();
        switch (reason) {
            case NONE:
                return "";
            case BAD_STATE:
                return BAD_STATE_MESSAGE;
            case INSUFFICIENT_LIGHT:
                return INSUFFICIENT_LIGHT_MESSAGE;
            case EXCESSIVE_MOTION:
                return EXCESSIVE_MOTION_MESSAGE;
            case INSUFFICIENT_FEATURES:
                return INSUFFICIENT_FEATURES_MESSAGE;
        }
        return "Unknown tracking failure reason: " + reason;
    }

    @Override
    public void updateBoutiquesMaker(Bundle[] allBoutiques) {
        for(int i=0 ;i< allBoutiques.length;++i){
            mLocations.add(
                    new BoutiqueLocation(allBoutiques[i].getString("title"),
                            allBoutiques[i].getDouble("lat",0.0),
                            allBoutiques[i].getDouble("long",0.0))
            );
        }
        Toast.makeText(this, "bouotiques : "+allBoutiques.length, Toast.LENGTH_LONG);
    }
}
