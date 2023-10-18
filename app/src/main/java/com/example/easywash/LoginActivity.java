package com.example.easywash;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Intent intent;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.setEmailText);
        password = findViewById(R.id.setPasswordText);
        databaseHelper = new DatabaseHelper(this);
        TextView forgot = findViewById(R.id.txtForgotPass);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "jdbc:postgresql://54.152.214.237:5432/myproject";
                String user = "myprojectuser";
                String password = "password";
                try {
                    // Cargar el controlador JDBC de PostgreSQL

                    // Establecer la conexión a la base de datos
                    Connection connection = DriverManager.getConnection(url, user, password);

                    // Ahora puedes utilizar 'connection' para realizar consultas a la base de datos

                    // No olvides cerrar la conexión cuando hayas terminado
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Error: No se pudo conectar a la base de datos.");
                    e.printStackTrace();
                }
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
        Log.d("INICIOOOOOOOOOOOOOO","INICIOOOOOOOOOOOOOO");
        if(email.getText().toString().equals("") || password.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Llena todos los campos", Toast.LENGTH_SHORT).show();
            Log.d("NO ENTROOOOOOO","nO ENTRO");
        }
        else {
            Boolean checkCredentials = databaseHelper.checkEmailPassword(email.getText().toString(),password.getText().toString());

            if(checkCredentials){
                Toast.makeText(getApplicationContext(),"Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(getApplicationContext(),"Credenciales inválidas", Toast.LENGTH_SHORT).show();
            }
        }
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

    public void forgotPassword(View view){
        String url = "jdbc:postgresql://54.152.214.237:5432/myproject";
        String user = "myprojectuser";
        String password = "password";
        try{
            Connection connection = DriverManager.getConnection(url,user,password);
            Toast.makeText(getApplicationContext(),"Conexión realizada", Toast.LENGTH_SHORT).show();
            connection.close();

        } catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
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