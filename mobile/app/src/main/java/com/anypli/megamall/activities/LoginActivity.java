package com.anypli.megamall.activities;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.anypli.megamall.R;
import com.anypli.megamall.contracts.LoginContract.LoginPresenterItf;
import com.anypli.megamall.presenters.LoginPresenterImpl;


import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements com.anypli.megamall.contracts.LoginContract.LoginViewItf {

    private LoginPresenterItf mLoginPresenter ;
    private TextView mSignInStatusTextView ;
    private SharedPreferences mSharedPrefs ;
    private SharedPreferences.Editor mEditor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPrefs = getSharedPreferences("megamall",MODE_PRIVATE);
        mEditor= mSharedPrefs.edit() ;
        setContentView(R.layout.activity_login);
        mLoginPresenter= new LoginPresenterImpl(this);
        initLoginScreen();
        ((EditText)findViewById(R.id.usrnametextinput)).setText( mSharedPrefs.getString("email",""));
        ((EditText)findViewById(R.id.passwordtextinput)).setText( mSharedPrefs.getString("password",""));
    }

    @Override
    public void initLoginScreen() {
        //TODO : Init default values and listener ;
        mSignInStatusTextView= findViewById(R.id.signinStatustextView);
       findViewById(R.id.signinbutton).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String email =((EditText)findViewById(R.id.usrnametextinput)).getText().toString();
               String password =((EditText)findViewById(R.id.passwordtextinput)).getText().toString();

               if(email.equals("") ||password.equals("")){
                   Toast.makeText(v.getContext(),"email and password cannot be empty", Toast.LENGTH_SHORT).show();
               }else {
                   mLoginPresenter.checkLoginData(email,password);
                   v.setOnClickListener(null);
                   mSignInStatusTextView.setText("Connecting ...");
               }
           }
       });
        findViewById(R.id.signupbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegistrationActivity();
            }
        });
    }

    @Override
    public void showErrorMessage(String Message) {
        mSignInStatusTextView.setText(Message);
        findViewById(R.id.signinbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPresenter.checkLoginData(
                        ((EditText)findViewById(R.id.usrnametextinput)).getText().toString(),
                        ((EditText)findViewById(R.id.passwordtextinput)).getText().toString());
                v.setOnClickListener(null);
            }
        });
    }

    @Override
    public void showNavigationActivity() {
        mSignInStatusTextView.setText("");
        findViewById(R.id.signinbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPresenter.checkLoginData(
                        ((EditText)findViewById(R.id.usrnametextinput)).getText().toString(),
                        ((EditText)findViewById(R.id.passwordtextinput)).getText().toString());
                v.setOnClickListener(null);
            }
        });
        Intent nav_intent = new Intent("android.intent.action.NavigationScreen");
        startActivityForResult(nav_intent,0);
    }

    public void showRegistrationActivity() {
        Intent intent = new Intent("android.intent.action.SignUpScreen");
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0) {
            String email;
            String password ;
            if (resultCode == NavigationActivity.KEEPMELOGGEDIN) {
                email =((EditText)findViewById(R.id.usrnametextinput)).getText().toString();
                password=((EditText)findViewById(R.id.passwordtextinput)).getText().toString();
                mEditor.putString("email",email);
                mEditor.putString("password",password);
                mEditor.apply();
                finish();

            }else{
                mEditor.putString("email","");
                mEditor.putString("password","");
                mEditor.apply();
            }

        }else if(requestCode==1){
            if(resultCode==RESULT_OK){
                showNavigationActivity();
            }
        }

    }
}
