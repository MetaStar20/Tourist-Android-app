package com.tourism.attraction.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tourism.attraction.R;
import com.tourism.attraction.database.DBHelper;
import com.tourism.attraction.preferences.PKey;
import com.tourism.attraction.preferences.Prefs;

import java.io.IOException;

public class Login extends AppCompatActivity {

    private Context mContext;
    private EditText etuserName, etPassword;
    private Button login;
    private TextView tvRegister;
    private CheckBox cbShow;
    private CheckBox cbRemember;
    private DBHelper db;
    private Boolean isRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initObject();
        gotoHomeIfRemebered();
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(etuserName.getText().toString().equals("")|| etPassword.getText().toString().equals(""))  {
                Toast.makeText(getApplicationContext(), "Username and Password can't be empty", Toast.LENGTH_LONG).show();
                return;
            }
            saveUserInfo();
            gotoHome(etuserName.getText().toString(),etPassword.getText().toString());

            }
        });

    }

    public void initObject()  {

        mContext = getBaseContext();
        db = DBHelper.getInstance(this);
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.openDataBase();

        tvRegister = (TextView) findViewById(R.id.regis);
        etuserName = (EditText) findViewById(R.id.luser);
        etPassword = (EditText) findViewById(R.id.lpass);
        cbShow = (CheckBox) findViewById(R.id.showPass);
        cbRemember = (CheckBox) findViewById(R.id.rememberMe);
        login = (Button)findViewById(R.id.login);

        showPass();
        setRemember();
    }

    public void gotoHomeIfRemebered(){

        if(isRemember){
            gotoHome(Prefs.getStringPref(mContext,PKey.CUR_USER_NAME,"test"),
                     Prefs.getStringPref(mContext,PKey.CUR_PASSWORD,"12345"));
        }

    }

    public void gotoHome(String userName, String passWord){
        if (db.logIn(userName,passWord)) {
            Intent intent = new Intent(Login.this, HomeScreen.class);
            startActivity(intent);
            finish();
        }
    }


    public void showPass(){
        cbShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    public void setRemember() {
        isRemember = Prefs.getBooleanPref(mContext, PKey.REMEMBER_ME, false);
        cbRemember.setChecked(isRemember);
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                Prefs.putBooleanPref(mContext,PKey.REMEMBER_ME,true);
            } else {
                Prefs.putBooleanPref(mContext,PKey.REMEMBER_ME,false);
            }
            }
        });
    }

    public void saveUserInfo(){
        Prefs.putStringPref(mContext,PKey.CUR_USER_NAME,etuserName.getText().toString());
        Prefs.putStringPref(mContext,PKey.CUR_PASSWORD,etPassword.getText().toString());
    }


    public void onBackPressed()
    {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);
                    }
                }).setNegativeButton("No", null).show();
    }
}
