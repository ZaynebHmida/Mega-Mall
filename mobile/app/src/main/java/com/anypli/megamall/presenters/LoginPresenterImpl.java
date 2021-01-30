package com.anypli.megamall.presenters;

import androidx.annotation.NonNull;

import com.anypli.megamall.contracts.LoginContract.LoginViewItf;
import com.anypli.megamall.models.AccountData;
import com.anypli.megamall.models.BackEndInteractor;
import com.anypli.megamall.contracts.LoginContract.LoginPresenterItf;

public class LoginPresenterImpl implements LoginPresenterItf {

    private LoginViewItf mViewItf ;

    public LoginPresenterImpl(@NonNull LoginViewItf viewItf){
        this.mViewItf=viewItf;
    }

    @Override
    public void checkLoginData(String username, String password) {
        BackEndInteractor.getInstance().SignIn(this,username,password);
    }

    @Override
    public void showErrorMessage(String Message) {
        mViewItf.showErrorMessage(Message);
    }

    @Override
    public void Login(AccountData data) {
        mViewItf.showNavigationActivity();
    }
}
