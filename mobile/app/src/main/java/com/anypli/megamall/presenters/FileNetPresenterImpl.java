package com.anypli.megamall.presenters;

import android.net.Uri;
import androidx.annotation.NonNull;

import com.anypli.megamall.contracts.FileNetContract;
import com.anypli.megamall.models.BackEndInteractor;

public class FileNetPresenterImpl implements FileNetContract.PresenterItf {

    private FileNetContract.ViewItf mViewItf ;

    public FileNetPresenterImpl(@NonNull FileNetContract.ViewItf viewItf ){
        this.mViewItf=viewItf ;
    }

    @Override
    public void deleteImage(Uri serverFile) {
        BackEndInteractor.getInstance().deleteImage(this,serverFile);
    }

    @Override
    public void notifyViewDeleteComplete(Uri link) {
        mViewItf.onDeleteCompleteNotification(link);
    }

    @Override
    public void notifyViewDeleteFailed(Uri link, String message) {
        mViewItf.onDeleteFailedNotification(link ,message);
    }

    @Override
    public void uploadImage(Uri localfile, String folder, String newname) {
        BackEndInteractor.getInstance().uploadImage(this,localfile,folder,newname);
    }

    @Override
    public void notifyViewUploadComplete(String link) {
        mViewItf.onUploadCompleteNotification(link);
    }

    @Override
    public void notifyViewUploadFailed(Uri link,String message) {
        mViewItf.onUploadFailedNotification(link ,message);
    }
}
