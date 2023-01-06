package com.madeiteasy.dangtorisum;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity  {
    private EditText username, reg_email, reg_password, reg_confirm_password;
    private EditText lgn_email, lgn_password;
    private Button register_btn, login_btn;
    private int currentApiVersion;
private CheckBox show_pword_lgn,show_pword_rgt;

    private FirebaseAuth reg_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //hide actionbar and notification bar
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        FullScreencall();

        TextView bottom_register = findViewById(R.id.Register_btn1);
        TextView already_have_an_account_Login_btn = findViewById(R.id.already_have_an_account_Login_btn);
        TextView login_txt = findViewById(R.id.login_txt);
        TextView forgot_password = findViewById(R.id.forget_password);

        LinearLayout login_layout = findViewById(R.id.for_login);
        LinearLayout register_layout = findViewById(R.id.for_register);

        LinearLayout already_have_an_account_layout = findViewById(R.id.already_have_an_account_layout);



        reg_auth = FirebaseAuth.getInstance();

        //for register
        username = findViewById(R.id.username_input);
        reg_email = findViewById(R.id.email_input_for_reg);
        reg_password = findViewById(R.id.password_input_for_reg);
        reg_confirm_password = findViewById(R.id.confirm_password_input_for_reg);
        register_btn = findViewById(R.id.Register_btn);
        show_pword_rgt = findViewById(R.id.show_pword_rgt);

        //for Login
        login_btn = findViewById(R.id.login_btn);
        lgn_email = findViewById(R.id.email_input);
        lgn_password = findViewById(R.id.password_input);
        show_pword_lgn = findViewById(R.id.show_pword_lgn);

        show_pword_rgt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    reg_confirm_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else{
                    reg_confirm_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        show_pword_lgn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    lgn_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else{
                    lgn_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_reg_email = reg_email.getText().toString();
                String txt_reg_password = reg_password.getText().toString();

                FullScreencall();

                if (!register_username_validation() | !register_email_validation() | !register_password_validation()) {
                } else {
                    register_user(txt_username, txt_reg_email, txt_reg_password);
                }


            }



            private void register_user(String username, String email, String password) {
                reg_auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);

                        } else {
                            Toast.makeText(LoginActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            private boolean register_username_validation() {
                String txt_username = username.getText().toString();
                String noWhiteSpace = "\\A\\w{4,20}\\z";

                if (txt_username.isEmpty()) {
                    username.setError("Field cannot be empty");
                    return false;
                } else if (txt_username.length() >= 15) {
                    username.setError("Username too long");
                    return false;
                } else if (!txt_username.matches(noWhiteSpace)) {
                    username.setError("White Spaces are not allowed");
                    return false;
                } else {
                    username.setError(null);
                    return true;
                }
            }

            private boolean register_email_validation() {
                String val = reg_email.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (val.isEmpty()) {
                    reg_email.setError("Field cannot be empty");
                    return false;
                } else if (!val.matches(emailPattern)) {
                    reg_email.setError("Invalid email address");
                    return false;
                } else {
                    reg_email.setError(null);
                    return true;
                }
            }

            private boolean register_password_validation() {
                String val = reg_password.getText().toString();
                String val2 = reg_confirm_password.getText().toString();

                String passwordVal = "^" +
                        //"(?=.*[0-9])" +         //at least 1 digit
                        //"(?=.*[a-z])" +         //at least 1 lower case letter
                        //"(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=.*[a-zA-Z])" +      //any letter
                        "(?=.*[@#$%^&+=])" +    //at least 1 special character
                        "(?=\\S+$)" +           //no white spaces
                        ".{4,}" +               //at least 4 characters
                        "$";

                if (val.isEmpty()) {
                    reg_password.setError("Field cannot be empty");
                    return false;
                } else if (!val.matches(passwordVal)) {
                    reg_password.setError("Password is too weak");
                    return false;
                } else if (!val.matches(val2)) {
                    reg_confirm_password.setError("Password do not Match");
                    return false;
                } else {
                    reg_password.setError(null);
                    return true;
                }

            }

        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = lgn_email.getText().toString();
                String txt_password = lgn_password.getText().toString();
                if (!login_email_validation() | !login_password_validation()) {
                } else {
                    try {
                        login_btn.setVisibility(View.INVISIBLE);
                        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                            login_user(txt_email, txt_password);

                        }
                        else
                        {
                            login_btn.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Offline!", Toast.LENGTH_SHORT).show();

                        }


                    } catch (Exception e) {
                        login_btn.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            private boolean login_email_validation() {
                String val = lgn_email.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (val.isEmpty()) {
                    lgn_email.setError("Field cannot be empty");
                    return false;
                } else if (!val.matches(emailPattern)) {
                    lgn_email.setError("Invalid email address");
                    return false;
                } else {
                    lgn_email.setError(null);
                    return true;
                }
            }

            private boolean login_password_validation() {
                String val = lgn_password.getText().toString();

                if (val.isEmpty()) {
                    lgn_password.setError("Field cannot be empty");
                    return false;
                }  else {
                    lgn_password.setError(null);
                    return true;
                }

            }

            private void login_user(String email, String password) {
                reg_auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
                reg_auth.signInWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                        login_btn.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = lgn_email.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                login_btn.setVisibility(View.INVISIBLE);
                reg_auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Please check your Email!", Toast.LENGTH_SHORT).show();
                        }
                        login_btn.setVisibility(View.VISIBLE);

                    }
                });


            }
        });



        bottom_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login_layout.setVisibility(View.GONE);
                register_layout.setVisibility(View.VISIBLE);

                login_txt.setText("Register");
                already_have_an_account_layout.setVisibility(View.VISIBLE);


            }
        });
        already_have_an_account_Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_layout.setVisibility(View.VISIBLE);
                register_layout.setVisibility(View.GONE);

                login_txt.setText("Login");
                already_have_an_account_layout.setVisibility(View.GONE);

            }
        });
    }

    private void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}