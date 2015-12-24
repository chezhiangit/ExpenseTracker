package paramtech.com.exptracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Collections;

public class LoginActivity extends AppCompatActivity {

    private EditText authCode;
    private Button goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authCode = (EditText)findViewById(R.id.auth_code);
        goButton=(Button)findViewById(R.id.goButton);

        authCode.addTextChangedListener(new TextWatcher() {
            int authCodeCount=0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 5) {
                    goButton.setVisibility(View.VISIBLE);
                } else {
                    goButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void onGoButtonClick(View v){
        int authCodeValue=0;
        try{
            authCodeValue = Integer.parseInt(authCode.getText().toString());
        }
        catch (NumberFormatException nfe){
        }

        ParamBaseClass loginParam = new LoginParam(authCodeValue,getApplicationContext(),this);
        loginParam.func="validateUser";
        loginParam.callBack="validateUserHandler";
        loginParam.context=getApplicationContext();
        loginParam.currentActivity=this;
        new ExpenseDBTaskHandler().execute(loginParam);

    }
    public void onCreateAccessCode(View v){
        LayoutInflater factory = LayoutInflater.from(this);
        final LoginActivity currentActivity=this;
        final View textEntryView = factory.inflate(R.layout.new_user_dialog, null);
        AlertDialog alertDialog=new AlertDialog.Builder(LoginActivity.this)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setTitle(R.string.new_user_dialog_title)
                .setView(textEntryView)
                .setPositiveButton(R.string.account_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        EditText newUsrNameField = (EditText) textEntryView.findViewById(R.id.newUserName);
                        String newUsrName = (String)newUsrNameField.getText().toString();
                        EditText newUsrCodeField = (EditText) textEntryView.findViewById(R.id.newAccessCode);
                        String tmpCode = (String)newUsrCodeField.getText().toString();

                        if (tmpCode.trim().isEmpty()) return;

                        int newUsrCode = Integer.parseInt(tmpCode);

                        ParamBaseClass insertUserParam = new InsertUserParam(newUsrCode,newUsrName,getApplicationContext(),currentActivity);
                        insertUserParam.func="insertUser";
                        insertUserParam.callBack="insertUserHandler";
                        insertUserParam.context=getApplicationContext();
                        insertUserParam.currentActivity=currentActivity;
                        new ExpenseDBTaskHandler().execute(insertUserParam);

                    }
                })
                .setNegativeButton(R.string.account_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked cancel so do some stuff */
                    }

                }).create();
        alertDialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        alertDialog.show();
    }

    public void insertUserHandler(ParamBaseClass p){

        if(p.successFlag==true) {
            //Utility.showAlert(this,"Information","Account Created Successfully.");
            Intent accountPage = new Intent(this, AccountsMainActivity.class);
            startActivity(accountPage);
        }
        else{
            Utility.showAlert(this,"Information","Could not create user. Pls Try again");
        }

    }

    public void validateUserHandler(ParamBaseClass p){

        LoginParam lp = (LoginParam)p;

        if(p.successFlag==true) {
            SharedPreferenceManager sharedPreferenceManager = new SharedPreferenceManager(this);
            sharedPreferenceManager.setAccessCode(lp.accessCode);
            sharedPreferenceManager=null;
            Intent accountPage = new Intent(this, AccountsMainActivity.class);
            startActivity(accountPage);
        }
        else{
            Utility.showAlert(this,"Information","Incorrect user. Pls Try again with valid access code.");
        }

    }

}
