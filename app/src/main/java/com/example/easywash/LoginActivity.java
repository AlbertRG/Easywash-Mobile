package com.example.easywash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easywash.rest.ApiInterface;
import com.example.easywash.rest.RetrofitClient;
import com.example.easywash.rest.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import okhttp3.MediaType;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                Connection connection = null;
                try {
                    // Configura la URL de conexión a tu base de datos PostgreSQL
                    String url = "jdbc:postgresql://54.152.214.237:5432/myproject";
                    String user = "myprojectuser";
                    String password = "password";

                    // Carga el controlador JDBC de PostgreSQL
                    Class.forName("org.postgresql.Driver");

                    // Conecta a la base de datos
                    connection = DriverManager.getConnection(url, user, password);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Log.d("Soy gabo","hola");
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
            //Conexion a la API
            RetrofitClient retrofit = new RetrofitClient();
            ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
            User user = new User();
            // Construye el cuerpo de la solicitud
            Call<User[]> call = api.getClientsList("json");
            //Llamada a APIREST
            call.enqueue(new Callback<User[]>() {
                @Override
                public void onResponse(Call<User[]> call, Response<User[]> response) {
                    if (response.isSuccessful()) {
                        boolean login = false;
                        User[] users = response.body();
                        Log.d("email",email.getText().toString());
                        Log.d("pass",password.getText().toString());

                        for(User user1 : users){
                            Log.d("for",user1.getEmail());
                            Log.d("id",user1.getId());
                            if(email.getText().toString().equals(user1.getEmail()) && password.getText().toString().equals(user1.getPassword())){
                                login = true;
                                user.setId(user1.getId());
                                user.setfirst_name(user1.getfirst_name());
                                user.setLast_name(user1.getLast_name());
                                user.setEmail(user1.getEmail());
                                user.setPhone(user1.getPhone());
                                Log.d("id",user1.getId());
                                Log.d("login email",user.getEmail());
                                Log.d("login last",user.getLast_name());
                                Log.d("login name user",user1.getLast_name());
                                Log.d("login firt",user.getfirst_name());
                                Log.d("login phone",user.getPhone());
                                SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
                                String idUser = user.getId();
                                String firstName = user.getfirst_name();
                                String lastName = user.getLast_name();
                                String phone = user.getPhone();
                                String email = user.getEmail();
                                String pass = user1.getPassword();

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("id",idUser);
                                editor.putString("name",firstName);
                                editor.putString("lastname",lastName);
                                editor.putString("phone",phone);
                                editor.putString("email",email);
                                editor.putString("pass",pass);
                                editor.commit();

                                break;
                            }

                        }
                        if(login) {
                            Toast.makeText(getApplicationContext(), "Bienvenido "+user.getfirst_name(), Toast.LENGTH_LONG).show();
                            intent = new Intent(getApplicationContext(), MenuActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Crendenciales Incorrectas", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject errorResponse = new JSONObject(errorBody);
                            if (errorResponse.has("phone")) {
                                String phoneError = errorResponse.getJSONArray("phone").getString(0);
                                Toast.makeText(getApplicationContext(), "Teléfono ya registrado", Toast.LENGTH_LONG).show();


                            }
                            if (errorResponse.has("email")) {
                                String emailError = errorResponse.getJSONArray("email").getString(0);
                                Toast.makeText(getApplicationContext(), "Email ya registrado", Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<User[]> call, Throwable t) {
                    Log.e("ERRORRR",t.getMessage());
                }

            });
        }
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