package com.anypli.megamall.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.anypli.megamall.R;
import com.anypli.megamall.contracts.SignUpContract;
import com.anypli.megamall.contracts.SignUpContract.SignUpViewItf;
import com.anypli.megamall.presenters.SignUpPresenterImpl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SignUpActivity extends AppCompatActivity implements SignUpViewItf ,View.OnClickListener {
    private SignUpContract.SignUpPresenterItf mPresenter ;
    private Button mSignUpButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.signuptoolbar);
        setSupportActionBar(toolbar);
        mPresenter= new SignUpPresenterImpl(this);
        mSignUpButton = findViewById(R.id.SignUpButton);
        mSignUpButton.setOnClickListener(this);


    }

    @Override
    public void resultMessage(String message) {
        if(message.equals("success")){
            Toast.makeText(this,"Account created successfully",Toast.LENGTH_LONG).show();
            finish();
        }else{
            Toast.makeText(this,"cannot create account",Toast.LENGTH_LONG).show();
        }
        mSignUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

            String username=  ((EditText)findViewById(R.id.UsernameSignUpInput)).getText().toString();
            String email=  ((EditText)findViewById(R.id.EmailSignUpInput)).getText().toString();
            String password=  ((EditText)findViewById(R.id.PasswordSingUpInput)).getText().toString();
            String confirmpassword= ((EditText)findViewById(R.id.ConfirmPasswordSignUpInput)).getText().toString();
            if(password.equals(confirmpassword)) {
                mPresenter.RequestSignUp(username, email, password);
                v.setOnClickListener(null);
            }else{
                Toast.makeText(v.getContext(),"Passwords are different", Toast.LENGTH_LONG).show();
            }

    }
}
