package com.h2o.easywash;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.h2o.easywash.rest.ApiInterface;
import com.h2o.easywash.rest.RetrofitClient;
import com.h2o.easywash.rest.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountActivity extends AppCompatActivity {
    private String idUser;
    private EditText name, lastname, phone, email, password, newpassword;
    private String pass;
    ImageView back;
    Button edit, delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        idUser = getUserId();
        back = findViewById(R.id.imgBackCarsEdit);
        name = findViewById(R.id.setNameSignText);
        lastname = findViewById(R.id.setLastNameSignText);
        phone = findViewById(R.id.setPhoneSignText);
        email = findViewById(R.id.setEmailSignText);
        password = findViewById(R.id.setPasswordSignText);
        newpassword = findViewById(R.id.setRepeatPasswordSignText);

        //API
        //Conexion a la API
        RetrofitClient retrofit = new RetrofitClient();
        ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
        // Construye el cuerpo de la solicitud
        Call<User> call = api.getUsertById(idUser);
        //Llamada a APIREST
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    boolean login = false;
                    User user = response.body();
                    Log.d("user id", user.getId());
                    Log.d("user name", user.getfirst_name());
                    name.setText(user.getfirst_name());
                    lastname.setText(user.getLast_name());;
                    phone.setText(user.getPhone());
                    email.setText(user.getEmail());




                }


            }//onResponse

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ERRORRR onViewCreated",t.getMessage());
            }

        });
        //API
        //Edit
        edit = findViewById(R.id.btnEditVehicle);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData()){
                    if(esContrasenaValida(password.getText().toString())&&password.getText().toString().equals(newpassword.getText().toString())){
                        User updatedUser = new User(name.getText().toString(),lastname.getText().toString(),phone.getText().toString(),email.getText().toString(),password.getText().toString());
                        showPasswordDialogEdit(updatedUser);
                    }else if (password.getText().toString().equals("")&&newpassword.getText().toString().equals("")){
                        User updatedUser = new User(name.getText().toString(),lastname.getText().toString(),phone.getText().toString(),email.getText().toString());
                        showPasswordDialogEdit(updatedUser);
                    } else if(!esContrasenaValida(password.getText().toString())&&password.getText().toString().equals(newpassword.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Contraseña no válida", Toast.LENGTH_SHORT).show();

                    }
                    else if (password.getText().toString() != "" || newpassword.getText().toString() != "") {

                        Toast.makeText(getApplicationContext(),"Nuevas contraseñas no coinciden", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
        delete = findViewById(R.id.btnDeleteVehicle);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountActivity.this);
                    builder.setMessage("¿Desea eliminar su cuenta?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showPasswordDialog();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Conéctate a internet", Toast.LENGTH_LONG).show();

                }
            }//onClick
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private String getUserId(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("id", "0");
        return idUser;
    }
    private boolean validateData(){
        String emailT = email.getText().toString().trim();
        String phoneT = phone.getText().toString().trim();

        String regexEmail = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
        String regexPhone = "^[0-9]{10}$";
        Boolean passed = true;
        if (!emailT.matches(regexEmail)) {
            Toast.makeText(getApplicationContext(),"Email no válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!phoneT.matches(regexPhone)) {
            Toast.makeText(getApplicationContext(),"Teléfono no válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(name.getText().toString().equals("")||lastname.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),"LLena los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return passed;
    }

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
    }//escontrasenaValida

    private void showPasswordDialog() {
        AlertDialog.Builder passwordDialogBuilder = new AlertDialog.Builder(this);
        passwordDialogBuilder.setTitle("Ingrese su contraseña actual");

        // Agrega un EditText para ingresar la contraseña
        final EditText editTextPassword = new EditText(this);
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordDialogBuilder.setView(editTextPassword);

        passwordDialogBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String enteredPassword = editTextPassword.getText().toString().trim();
                // Verifica la contraseña ingresada aquí
                // Si es correcta, realiza el cambio; de lo contrario, muestra un mensaje de error
                if(checkPassword(enteredPassword)){
                    RetrofitClient retrofit = new RetrofitClient();
                    ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
                    Call<Void> call = api.deleteUser(idUser);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // La solicitud DELETE se realizó con éxito y el automóvil se eliminó
                                // Realiza cualquier acción necesaria después de la eliminación.
                                Toast.makeText(getApplicationContext(),"Cuenta eliminada", Toast.LENGTH_SHORT).show();
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                finishAffinity(); // Cierra todas las actividades
                                // Aquí puedes iniciar la actividad de inicio de sesión
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);



                                finish();
                            } else {
                                // Maneja el caso en el que la solicitud DELETE no fue exitosa
                                Toast.makeText(getApplicationContext(),"Error al eliminar", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // Maneja errores de red o de la API
                            Toast.makeText(getApplicationContext(),"Error al eliminar", Toast.LENGTH_SHORT).show();

                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"Contraseña incorrecta", Toast.LENGTH_SHORT).show();

                }


            }//onClick
        });

        passwordDialogBuilder.setNegativeButton("Cancelar", null);
        passwordDialogBuilder.show();
    }//showPasswordDialog

    private String getPass(){
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("pass", "0");
        return idUser;
    }//getPass

    private void showPasswordDialogEdit(User updatedUser){
        AlertDialog.Builder passwordDialogBuilder = new AlertDialog.Builder(this);
        passwordDialogBuilder.setTitle("Ingrese su contraseña actual");

        // Agrega un EditText para ingresar la contraseña
        final EditText editTextPassword = new EditText(this);
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordDialogBuilder.setView(editTextPassword);

        passwordDialogBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String enteredPassword = editTextPassword.getText().toString().trim();
                // Verifica la contraseña ingresada aquí
                // Si es correcta, realiza el cambio; de lo contrario, muestra un mensaje de error
                Log.d("pass",enteredPassword);
                Log.d("savepass",getPass());
                if(isNetworkAvailable()) {
//API
                    //Conexion a la API
                    RetrofitClient retrofit = new RetrofitClient();
                    ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
                    // Construye el cuerpo de la solicitud
                    Call<User> call = api.getUsertById(idUser);
                    //Llamada a APIREST
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                boolean login = false;
                                User user = response.body();
                                pass = user.getPassword();
                                if (enteredPassword.equals(pass)) {
                                    // La contraseña es correcta
                                    // Realizar acciones necesarias o llamar a un método, por ejemplo:
                                    fillFileds(updatedUser);
                                } else {
                                    // La contraseña no es correcta
                                    Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                }



                            }


                        }//onResponse

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("ERRORRR onViewCreated",t.getMessage());
                        }

                    });
                    //API

                }else{
                    Toast.makeText(getApplicationContext(), "Conéctate a internet", Toast.LENGTH_LONG).show();

                }


            }//onClick
        });

        passwordDialogBuilder.setNegativeButton("Cancelar", null);
        passwordDialogBuilder.show();
    }//showPasswordDialog

    private boolean checkPassword(String password){
        //API
        //Conexion a la API
        RetrofitClient retrofit = new RetrofitClient();
        ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
        // Construye el cuerpo de la solicitud
        Call<User> call = api.getUsertById(idUser);
        //Llamada a APIREST
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    boolean login = false;
                    User user = response.body();
                    pass = user.getPassword();




                }


            }//onResponse

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ERRORRR onViewCreated",t.getMessage());
            }

        });
        //API
        if(password.equals(pass))
            return true;
        Log.d("No paso pass",pass);
        Log.d("No paso password",password);
        return false;
    }//checkPassword
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }//isNetworkAvailable
    private void fillFileds(User updatedUser){
        // Crea una instancia de Retrofit y ApiInterface (esto varía según cómo lo configures en tu aplicación)
        RetrofitClient retrofit = new RetrofitClient();
        ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);

        // Realiza la solicitud PATCH
        Call<User> call = api.updateUser(idUser, updatedUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // El carro ha sido actualizado con éxito
                    User user = response.body();
                    Log.d("update name", user.getfirst_name());
                    Log.d("update lastname", user.getLast_name());
                    Log.d("update phone", user.getPhone());
                    Log.d("update email", user.getEmail());
                    Log.d("pass", user.getPassword());
                    Toast.makeText(getApplicationContext(), "Cuenta actualizada", Toast.LENGTH_SHORT).show();
                    finish();


                } else {
                    // Maneja el caso en el que la solicitud PATCH no fue exitosa
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
            public void onFailure(Call<User> call, Throwable t) {
                // Maneja errores de red o de la API
            }
        });
    }
}