package com.anypli.megamall.contracts;

public class SignUpContract {
    public interface SignUpViewItf{
        void resultMessage(String message) ;
    }
    public interface SignUpPresenterItf{
        void RequestSignUp(String Username, String email, String password);
        void setResultMessage(String message);
    }
}
