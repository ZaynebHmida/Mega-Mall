package com.anypli.megamall.contracts;

import android.net.Uri;

import com.anypli.megamall.models.AccountData;

public class AccountContract {
    public  interface AccountViewItf {
        void showAccountData(AccountData data);
        void showMessage(String message);
    }
    public interface AccountPresenterItf{
        void getAccountData();
        void UpdatePassword (String oldpassword , String newpassword, String conformpassword);
        void UpdatePersonalInfo(String FirstName,String Lastname,String Adresse,String phonenumber );
        void NotifyView(String Message);
        void UpdateUserProfilPic(Uri uri);
    }
}
