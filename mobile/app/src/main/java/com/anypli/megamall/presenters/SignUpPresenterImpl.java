package com.anypli.megamall.presenters;

import androidx.annotation.NonNull;

import com.anypli.megamall.contracts.SignUpContract.SignUpPresenterItf;
import com.anypli.megamall.contracts.SignUpContract.SignUpViewItf;
import com.anypli.megamall.models.AccountData;
import com.anypli.megamall.models.BackEndInteractor;

public class SignUpPresenterImpl implements SignUpPresenterItf {
    private SignUpViewItf mViewItf ;

    public SignUpPresenterImpl(@NonNull  SignUpViewItf viewItf){
        this.mViewItf=viewItf ;
    }


    @Override
    public void RequestSignUp(String Username, String email, String password) {
        BackEndInteractor.getInstance().SignUp(this,Username,email,password);
    }

    @Override
    public void setResultMessage(String message) {
        mViewItf.resultMessage(message);
    }
}
