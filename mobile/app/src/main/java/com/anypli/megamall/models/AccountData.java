package com.anypli.megamall.models;

import androidx.annotation.NonNull;

public class AccountData {
    private String mId ;
    private String mEmail ;
    private String mUsername ;
    private String mPassword ;
    private String mFirstname ;
    private String mLastName ;
    private String mAdresse ;
    private String mIconUrl ;
    private String mPhoneNumber ;


    public AccountData(String Id , String Username, String Password, String Email, String Firstname, String LastName, String Adresse, String IconUrl, String phonenumber ) {
        this.mId = Id;
        this.mUsername = Username;
        this.mPassword = Password;
        this.mEmail = Email;
        this.mFirstname = Firstname;
        this.mLastName = LastName;
        this.mAdresse = Adresse;
        this.mIconUrl = IconUrl;
        this.mPhoneNumber = phonenumber;

    }

    public AccountData(@NonNull AccountData clone) {
        this.mId = clone.mId ;
        this.mUsername = clone.mUsername;
        this.mEmail = clone.mEmail ;
        this.mPassword = clone.mPassword ;
            this.mFirstname = clone.mFirstname;
            this.mLastName = clone.mLastName;
            this.mAdresse = clone.mAdresse;
            this.mIconUrl = clone.mIconUrl;
            this.mPhoneNumber = clone.mPhoneNumber;
    }

    public String getId() {
        return mId;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getFirstname() {
        return mFirstname;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getAdresse() {
        return mAdresse;
    }

    public String getIconUrl() {
        return mIconUrl;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }


    public void setFirstname(String firstname) {
        this.mFirstname = firstname;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public void setAdresse(String adresse) {
        this.mAdresse = adresse;
    }


    public void setIconUrl(String mIconUrl) {
        this.mIconUrl = mIconUrl;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }
}
