package com.anypli.megamall.contracts;

import android.net.Uri;

public class FileNetContract {
    public interface ViewItf {
        void onUploadCompleteNotification(String link);
        void onUploadFailedNotification(Uri link , String message);
        void onDeleteCompleteNotification(Uri link);
        void onDeleteFailedNotification(Uri link , String message);
    }
    public interface PresenterItf {
        void deleteImage(Uri ServerFiles);
        void notifyViewDeleteComplete(Uri link);//notification for each file uploaded
        void notifyViewDeleteFailed(Uri link , String message);
        void uploadImage(Uri localfiles , String folder, String newname);
        void notifyViewUploadComplete(String link);//notification for each file uploaded
        void notifyViewUploadFailed(Uri link ,String message);//notification for each file uploaded
    }
}
