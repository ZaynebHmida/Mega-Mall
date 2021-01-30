package com.anypli.megamall.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anypli.megamall.R;
import com.anypli.megamall.contracts.AccountContract.AccountPresenterItf;
import com.anypli.megamall.contracts.AccountContract.AccountViewItf;
import com.anypli.megamall.models.AccountData;
import com.anypli.megamall.presenters.AccountPresenterImpl;
import com.squareup.picasso.Picasso;

public class AccountActivity extends AppCompatActivity implements AccountViewItf {
    private AccountPresenterItf mPresenter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = findViewById(R.id.myaccounttoolbar);
        setSupportActionBar(toolbar);
        mPresenter= new AccountPresenterImpl(this);
        mPresenter.getAccountData();
        Button savechanges = findViewById(R.id.savemyaccountdatachanges);
        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentPasswordinput =
                        ((EditText)findViewById(R.id.myaccountcurrentpasswordtextfield)).getText().toString() ;
                String newpasswordinput =
                        ((EditText)findViewById(R.id.myaccountnewpasswordtextfield)).getText().toString() ;
                String confirmnewPassword =
                        ((EditText)findViewById(R.id.myaccountconfirmpasswordtextfield)).getText().toString() ;
                if(!currentPasswordinput.equals("")){
                    mPresenter.UpdatePassword(currentPasswordinput,newpasswordinput,confirmnewPassword);
                }
                //TODO
                mPresenter.UpdatePersonalInfo(
                        ((EditText)findViewById(R.id.myaccountfirstnametextfield)).getText().toString(),//Firstname
                        ((EditText)findViewById(R.id.myaccountlastnametextfield)).getText().toString(),//Lastname
                        ((EditText)findViewById(R.id.myaccountadressetextfield)).getText().toString(),//Adresse
                        ((EditText)findViewById(R.id.myaccountphonenumbertextfield)).getText().toString()//Phone number
                );

            }
        });
    }

    @Override
    public void showAccountData(AccountData data) {
        //TODO : provide all needed data
        if(data.getIconUrl()!=null && !data.getIconUrl().equals(""))
            Picasso.get().load(data.getIconUrl()).into((ImageView)findViewById(R.id.myaccountprofilpic));
        ((TextView)findViewById(R.id.myaccountusernametextview)).setText(data.getUsername());
        ((TextView)findViewById(R.id.myaccountemailtextview)).setText(data.getEmail());

        ((EditText)findViewById(R.id.myaccountfirstnametextfield)).setText(data.getFirstname());
        ((EditText)findViewById(R.id.myaccountlastnametextfield)).setText(data.getLastName());
        ((EditText)findViewById(R.id.myaccountadressetextfield)).setText(data.getAdresse());
        

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
