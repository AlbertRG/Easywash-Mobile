package com.example.easywash;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.setEmailText);
        password = findViewById(R.id.setPasswordText);

        TextView forgot = findViewById(R.id.txtForgotPass);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), RecoverPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView back = findViewById(R.id.imgBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
    }//onCreate

    public void Login(View view) {
        /*
        //Get list of internal files
        String[] fileList = fileList();
        String accountFile = email.getText().toString().trim() + ".txt";
        //Validate file
        if(checkFiles(fileList, accountFile)){
            try{
                //Associate file to instance
                InputStreamReader internalFile = new InputStreamReader(openFileInput(accountFile));
                //Instance to read file
                BufferedReader FileReader = new BufferedReader(internalFile);
                //Read the content of the file and put it in a variable
                String line = FileReader.readLine();
                String email = line;
                line = FileReader.readLine();
                String password = line;
                line = FileReader.readLine();
                String username = line;
                //Compare file info vs components info
                if(email.equals(this.email.getText().toString().trim()) && password.equals(this.password.getText().toString().trim())){
                    //Toast.makeText(getApplicationContext(), "Login",Toast.LENGTH_SHORT).show();
                    savePreferencesNeeded(email, username);
                    //Save preferences if user selects it
                    if(remember.isChecked()){
                        savePreferences(password);
                    }
                    */
                    intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivity(intent);
                    finish();
                    /*
                } else {
                    Toast.makeText(getApplicationContext(), "Email or Password incorrect",Toast.LENGTH_LONG).show();
                }
                FileReader.close();
                internalFile.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "ERROR login",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "ERROR the account does not exist",Toast.LENGTH_LONG).show();
        }*/
    }//Login

    public void SignUp(View view) {
        intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }//SignUp

    private void savePreferences (String name, String lastname, String email, String password){
        //Create object to store username and password information
        SharedPreferences metadata = getSharedPreferences("user.dat",MODE_PRIVATE);
        SharedPreferences.Editor edit = metadata.edit();
        edit.putString("name", name);
        edit.putString("lastname", lastname);
        edit.putString("email", email);
        edit.putString("password", password);
        edit.putBoolean("register", true);
        edit.apply();
    }//savePreferences

}