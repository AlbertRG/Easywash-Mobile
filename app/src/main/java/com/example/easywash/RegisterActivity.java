package com.example.easywash;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.NoSuchPaddingException;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, lastname,phone, email, password, repeatPassword;
    private CheckBox termsOfService;
    private Intent intent;
    private DatabaseHelper databaseHelper;
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        databaseHelper = new DatabaseHelper(this);
        name = findViewById(R.id.setNameSignText);
        lastname = findViewById(R.id.setLastNameSignText);
        email = findViewById(R.id.setEmailSignText);
        phone = findViewById(R.id.setPhoneSignText);
        password = findViewById(R.id.setPasswordSignText);
        repeatPassword = findViewById(R.id.setRepeatPasswordSignText);
        termsOfService = findViewById(R.id.cbxTermOfServices);
        termsOfService.setMovementMethod(LinkMovementMethod.getInstance());
    }//onCreate

    public void signUp(View view) throws NoSuchPaddingException, NoSuchAlgorithmException {
        String nombre, apellido, correo, contrasena, confContra;
        nombre = name.getText().toString();
        apellido = lastname.getText().toString();
        correo = email.getText().toString().trim();
        contrasena = password.getText().toString().trim();
        confContra = repeatPassword.getText().toString().trim();
        if(!nombre.equals("")&&!apellido.equals("")&&!correo.equals("")&&!contrasena.equals("")&&!confContra.equals("")){
            if(!termsOfService.isChecked()) {
                Toast.makeText(getApplicationContext(), "Es necesario aceptar los terminos de uso del servicio", Toast.LENGTH_LONG).show();

            }else if(createAccount()){
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Llena todos los campos", Toast.LENGTH_LONG).show();

        }
    }//signUp

    private boolean createAccount() throws NoSuchPaddingException, NoSuchAlgorithmException {
        if(password.getText().toString().trim().equals(repeatPassword.getText().toString().trim())){
            if(!esContrasenaValida(password.getText().toString())){
                Toast.makeText(getApplicationContext(), "Contraseña no válida", Toast.LENGTH_LONG).show();

            }else{
                if(!esNumeroDeTelefonoValido(phone.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Telefono no válido", Toast.LENGTH_LONG).show();
                }else{
                    Boolean checkUserEmail = databaseHelper.checkEmail(email.getText().toString());
                    if(!checkUserEmail){
                        Boolean insert = databaseHelper.insertData(name.getText().toString(),lastname.getText().toString(),email.getText().toString().trim(),password.getText().toString().trim());
                        if(insert){
                            Toast.makeText(getApplicationContext(), "Cuenta registrada", Toast.LENGTH_LONG).show();
                            return true;
                        }else{
                            Toast.makeText(getApplicationContext(), "Error al registrar cuenta", Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Correo ya registrado", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
            }
        }else{
            Toast.makeText(getApplicationContext(), "Las contraseñas no estan iguales", Toast.LENGTH_LONG).show();
            Log.d("PASSSSSWQOOOORD",password.getText().toString());
            Log.d("CONFIIIIIIRRRRRMMMM",repeatPassword.getText().toString());
            Log.d(password.getText().toString(),"PASSSSSWQOOOORD");
            Log.d(repeatPassword.getText().toString(),"CONFIIIIIIRRRRRMMMM");


        }
        return false;
    }//createAccount

    public void login(View view) {
        intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }//Login

    public static boolean esContrasenaValida(String contrasena) {
        // Al menos 8 caracteres
        if (contrasena.length() < 8) {
            return false;
        }

        // Al menos una letra mayúscula
        if (!contrasena.matches(".*[A-Z].*")) {
            return false;
        }

        // Al menos un número
        if (!contrasena.matches(".*\\d.*")) {
            return false;
        }

        // Al menos un carácter especial (puedes ajustar la expresión regular según tus necesidades)
        Pattern pattern = Pattern.compile("[!@#$%^&*()_+{}.?~]");
        Matcher matcher = pattern.matcher(contrasena);
        return matcher.find();
    }
    public static boolean esNumeroDeTelefonoValido(String telefono) {
        // Patrón para un número de teléfono con exactamente 10 dígitos
        String patron = "^\\d{10}$";

        // Compilar la expresión regular
        Pattern pattern = Pattern.compile(patron);

        // Crear un objeto Matcher
        Matcher matcher = pattern.matcher(telefono);

        // Verificar si el número coincide con el patrón
        return matcher.matches();
    }
}