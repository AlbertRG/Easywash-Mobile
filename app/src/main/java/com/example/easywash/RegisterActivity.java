package com.example.easywash;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, lastname, email, password, repeatPassword;
    private CheckBox termsOfService;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.setNameSignText);
        lastname = findViewById(R.id.setLastNameSignText);
        email = findViewById(R.id.setEmailSignText);
        password = findViewById(R.id.setPasswordSignText);
        repeatPassword = findViewById(R.id.setRepeatPasswordSignText);
        termsOfService = findViewById(R.id.cbxTermOfServices);
        termsOfService.setMovementMethod(LinkMovementMethod.getInstance());
    }//onCreate

    public void signUp(View view){
        if(termsOfService.isChecked()){
            if(createAccount()){
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Es necesario aceptar los terminos de uso del servicio", Toast.LENGTH_LONG).show();
        }
    }//signUp

    private boolean createAccount() {
        if(true){
            return true;
        }else{
            Toast.makeText(getApplicationContext(), "El correo ya se encuentra registrado", Toast.LENGTH_LONG).show();
        }
        return false;
    }//createAccount

    public void login(View view) {
        intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }//Login
}