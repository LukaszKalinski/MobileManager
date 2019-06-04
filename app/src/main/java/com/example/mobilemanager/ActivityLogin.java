package com.example.mobilemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ActivityLogin extends AppCompatActivity {

    Button loginActivityLoginBtn;
    Button loginActivityCreateAccountBtn;
    EditText loginActivityUserName;
    EditText loginActivityPassword;
    ImageView loginActivityDrawing;
    String userName;
    String password;
    DatabaseUsers userDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        loginActivityLoginBtn = (Button) findViewById(R.id.loginActivityLoginBtn);
        loginActivityCreateAccountBtn = (Button) findViewById(R.id.loginActivityCreateAccountBtn);
        loginActivityUserName = (EditText) findViewById(R.id.loginActivityUserName);
        loginActivityPassword = (EditText) findViewById(R.id.loginActivityPassword);
        loginActivityDrawing = (ImageView) findViewById(R.id.loginActivityDrawing);

        userDb = new DatabaseUsers(ActivityLogin.this);

        loginActivityLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = loginActivityUserName.getText().toString();
                password = loginActivityPassword.getText().toString();
                userDb.open();
                if (userName.length()>0 && password.length()>0){
                    if (userDb.Login(userName, password)){
                        Toast.makeText(ActivityLogin.this,"Going to log as: " + userName, Toast.LENGTH_LONG).show();
                        shareLogin();
                        userDb.close();

                        Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(ActivityLogin.this,"Wrong username/password", Toast.LENGTH_LONG).show();
                        userDb.close();
                    }
                } else {
                    Toast.makeText(ActivityLogin.this,"Enter both (username & password)", Toast.LENGTH_LONG).show();
                }
            }
        });

        loginActivityCreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = loginActivityUserName.getText().toString();
                password = loginActivityPassword.getText().toString();
                userDb.open();
                if (userName.length()>0 && password.length()>0){
                    if (userDb.checkUserName(userName)){
                        Toast.makeText(ActivityLogin.this,"Username already exists", Toast.LENGTH_LONG).show();
                        userDb.close();
                    } else {
                        userDb.AddUser(userName, password);
                        Toast.makeText(ActivityLogin.this,"Account created: " + userName + "/" + password, Toast.LENGTH_LONG).show();
                        userDb.close();
                    }
                } else {
                    Toast.makeText(ActivityLogin.this,"Enter both (username & password)", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void shareLogin(){
        SharedPreferences sp = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("Username", userName);
        ed.commit();
    }
}
