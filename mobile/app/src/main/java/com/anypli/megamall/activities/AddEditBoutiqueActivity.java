package com.anypli.megamall.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anypli.megamall.R;
import com.anypli.megamall.contracts.AddEditBoutiqueContract.AddEditBoutiquePresenterItf;
import com.anypli.megamall.contracts.AddEditBoutiqueContract.AddEditBoutiqueViewItf;
import com.anypli.megamall.contracts.FileNetContract;
import com.anypli.megamall.presenters.AddEditBoutiquePresenterImpl;
import com.anypli.megamall.presenters.FileNetPresenterImpl;
import com.google.firebase.firestore.GeoPoint;
import com.google.type.LatLng;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddEditBoutiqueActivity extends AppCompatActivity implements AddEditBoutiqueViewItf, FileNetContract.ViewItf,DialogInterface.OnClickListener{

    private static final int STOPPED =0 ;
    private static final int STARTED =1 ;
    private static final int COMPLETED =2 ;

    private AddEditBoutiquePresenterItf mPresenter;
    private FileNetContract.PresenterItf    mImageUploadPresenter ;
    private String mBoutiqueID ;
    private Bundle mBundle ;
    private char mIndicator ;// indicate icon or bgimage is been chosen
    private double mLatitude ;
    private double mLongitude ;
    private boolean mNewLocation=false ;
    private Uri     mNewIcon ;
    private Uri     mBgImage ;
    private int     mNumImagestoUpload ;
    private int     mUploadStatus ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_boutique);
        Toolbar toolbar = findViewById(R.id.addeditboutiquetoolbar);
        setSupportActionBar(toolbar);
        mPresenter=new AddEditBoutiquePresenterImpl(this);
        mImageUploadPresenter = new FileNetPresenterImpl(this);
        mBoutiqueID=this.getIntent().getStringExtra("boutiqueid");
        if(mBoutiqueID !=null && !mBoutiqueID.equals("")) {
            mPresenter.GetBoutiqueData(mBoutiqueID);
        }else
            this.SetPageData(null);

        mUploadStatus=COMPLETED;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void SetPageData(Bundle boutique) {
        Button deleteboutiquebutton ;
        mNewLocation=false ;
        if(boutique!=null) {
            ImageView background = findViewById(R.id.boutiquebackgroundimage);
            if (boutique.get("bgimage") != null && !boutique.get("bgimage").equals("")) {
                String image= (String) boutique.get("bgimage");
                Picasso.get()
                        .load(image).into(background);
            }
            CircleImageView icon = findViewById(R.id.boutiqueiconedit);
            if (boutique.getString("iconurl") != null && !boutique.getString("iconurl").equals(""))
                Picasso.get().load(boutique.getString("iconurl")).into(icon);
            EditText name = findViewById(R.id.boutiquenameedittextinput);
            name.setText(boutique.getString("title"));
            EditText description = findViewById(R.id.boutiquedescriptionedittextinput);
            description.setText(boutique.getString("description"));
            EditText adresse = findViewById(R.id.boutiqueadresseedittextinput);
            adresse.setText(boutique.getString("adress"));

            if(boutique.getBoolean("haveLocation",false)){
                double lat = boutique.getDouble("lat",0.0);
                double lon = boutique.getDouble("long",0.0);
                ((TextView)findViewById(R.id.latitudeTextView)).setText("Lat "+String.valueOf(lat));
                ((TextView)findViewById(R.id.longitudeTextView)).setText("Lng: "+String.valueOf(lon));
            }


            Button deleteButton = findViewById(R.id.boutiqueEditDeleteButton);
            deleteButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("the effect of this operation is permenant. Are you sure? ");
                    builder.setPositiveButton("Yes",(DialogInterface.OnClickListener)(v.getContext()));
                    builder.setNegativeButton("No",null);
                    builder.show();

                    return true;

                }
            });

            mBundle=boutique;
        }else{
            //remove the delete Button because we are treating the new boutique case
            deleteboutiquebutton=findViewById(R.id.boutiqueEditDeleteButton);
            deleteboutiquebutton.setVisibility(View.GONE);
            mBundle =new Bundle();


        }


        ImageView returnbutton = findViewById(R.id.boutiqueaddeditreturnbutton);
        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        ImageView confirmbutton = findViewById(R.id.boutiqueaddeditconfirmbutton);
        confirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNumImagestoUpload!=0){
                    if(mUploadStatus==STOPPED) {
                        mUploadStatus=STARTED ;
                        if (mNewIcon != null)
                            mImageUploadPresenter.uploadImage(mNewIcon, mBoutiqueID, "icon");
                        if (mBgImage != null)
                            mImageUploadPresenter.uploadImage(mBgImage, mBoutiqueID, "bgimage");
                    }
                }

                if(mUploadStatus!=STARTED) {
                    Toast.makeText(v.getContext(),"wait until upload complete",Toast.LENGTH_SHORT).show();
                }

                if(mUploadStatus==COMPLETED){

                    if (mBoutiqueID != null && !mBoutiqueID.equals(""))
                        mBundle.putString("boutiqueid", mBoutiqueID);
                    mBundle.putString("title", ((EditText) findViewById(R.id.boutiquenameedittextinput)).getText().toString());//Title
                    mBundle.putString("adress", ((EditText) findViewById(R.id.boutiqueadresseedittextinput)).getText().toString());//adress
                    mBundle.putString("description", ((EditText) findViewById(R.id.boutiquedescriptionedittextinput)).getText().toString());//Description
                    mPresenter.SetBoutiqueData(mBundle);
                }
            }
        });
        //TODO listener to change images
        findViewById(R.id.boutiqueiconimageeditbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageChoose((char) 1);
            }
        });
        findViewById(R.id.boutiquebackgroundimageeditbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageChoose((char) 2);
            }
        });

        findViewById(R.id.selectLocationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(v.getContext(),GeoLocationSelectionActivity.class);
                startActivityForResult(intent,0);
            }
        });

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mPresenter.requestDeleteBoutique(mBoutiqueID);
    }

    @Override
    public void dealWithIt(Bundle input) {
        this.SetPageData(input);
    }

    @Override
    public void dealWithIt(Object input) {
        if(input instanceof String){
            this.showMessage((String) input);
            if(input.equals("delete")){
                setResult(1);
                finish();
            }
            if(input.equals("success")){
                setResult(RESULT_OK);
                finish();
            }else if(input.equals("failed")){
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK) {
            if (requestCode == 2) {
                Uri selectedImage = data.getData();
                if (mIndicator == 1) {

                    Picasso.get().load(selectedImage).into((ImageView) findViewById(R.id.boutiqueiconedit));
                    if(mNewIcon==null)
                        mNumImagestoUpload ++;
                    mNewIcon=selectedImage ;
                } else if (mIndicator == 2) {
                    Picasso.get().load(selectedImage).into((ImageView) findViewById(R.id.boutiquebackgroundimage));
                    if(mBgImage==null)
                        mNumImagestoUpload ++;
                    mBgImage=selectedImage ;

                }
                mUploadStatus=STOPPED;
            }else if(requestCode==0){
                double lat = data.getDoubleExtra("lat",0.0);
                double lon = data.getDoubleExtra("long",0.0);
                ((TextView)findViewById(R.id.latitudeTextView)).setText("Lat: "+String.valueOf(lat));
                ((TextView)findViewById(R.id.longitudeTextView)).setText("Lng: "+String.valueOf(lon));
                mBundle.putDouble("lat",lat);
                mBundle.putDouble("long",lon);
                mBundle.putBoolean("haveLocation",true);
            }
        }
    }

    protected void ImageChoose(char indicator){
        Intent imagechooser = new Intent(Intent.ACTION_PICK);
        imagechooser.setType("image/");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        imagechooser.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        mIndicator= indicator;
        // Launching the Intent
        startActivityForResult(imagechooser,2);//pick from gallery
    }

    @Override
    public void onUploadCompleteNotification(String link) {
        if(link.contains("icon")){
            mBundle.putString("iconurl",link);
        }else {
            mBundle.putString("bgimage",link);
        }
        mNumImagestoUpload--;
        if(mNumImagestoUpload==0){
            mUploadStatus=COMPLETED;
        }
    }

    @Override
    public void onUploadFailedNotification(Uri link, String message) {
        Toast.makeText(this,"Error uploading file ",Toast.LENGTH_LONG).show();
        mNumImagestoUpload--;
        if(mNumImagestoUpload==0){
            mUploadStatus=COMPLETED;
        }
    }

    @Override
    public void onDeleteCompleteNotification(Uri link) {

    }

    @Override
    public void onDeleteFailedNotification(Uri link, String message) {

    }
}
