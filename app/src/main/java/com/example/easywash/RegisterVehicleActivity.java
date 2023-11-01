package com.example.easywash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.easywash.rest.ApiInterface;
import com.example.easywash.rest.Car;
import com.example.easywash.rest.RetrofitClient;
import com.example.easywash.rest.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterVehicleActivity extends AppCompatActivity {

    ImageView back;
    Button btnSend;
    EditText plate, model, brand, color, year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_vehicle);
        plate = findViewById(R.id.setPlateText);
        model = findViewById(R.id.setModelText);
        brand = findViewById(R.id.setBrandText);
        color = findViewById(R.id.setColorText);
        year = findViewById(R.id.setYearText);


        back = findViewById(R.id.imgBackCars);
        btnSend = findViewById(R.id.btnSignUpSU);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }//Onclick
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(plate.getText().toString().equals("")||model.getText().toString().equals("")||brand.getText().toString().equals("")||color.getText().toString().equals("")||year.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"Llena todos los campos", Toast.LENGTH_SHORT).show();
                else{
                    if(!checkPlate(plate.getText().toString()))
                        Toast.makeText(getApplicationContext(),"Placa no válida", Toast.LENGTH_SHORT).show();
                    else{
                        //Instancia de retrofit para invocar a la api
                        RetrofitClient retrofit = new RetrofitClient();
                        //Instancia de api
                        ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
                        //Instancia de carro
                        Car car = new Car(getUserId(),plate.getText().toString(),model.getText().toString(),year.getText().toString(),color.getText().toString());

                        // Construye el cuerpo de la solicitud
                        Call<User> call = api.sendCar(car);
                        //Llamada a APIREST
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Auto registrado", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    try {
                                        //En caso de que la placa ya haya sido registrada
                                        String errorBody = response.errorBody().string();
                                        JSONObject errorResponse = new JSONObject(errorBody);
                                        if (errorResponse.has("plate")) {
                                            String phoneError = errorResponse.getJSONArray("plate").getString(0);
                                            Toast.makeText(getApplicationContext(), "Placa ya registrada", Toast.LENGTH_LONG).show();


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
                                Log.e("ERRORRR",t.getMessage());
                            }

                        });
                    }

                }
            }
        });

    }//onCreate


    private boolean checkPlate(String plate){
        // Expresión regular que busca tres letras seguidas de tres o cuatro números
        String regex = "^[A-Za-z]{3}[0-9]{3,4}$";

        // Compilar la expresión regular
        Pattern pattern = Pattern.compile(regex);

        // Crear un objeto Matcher
        Matcher matcher = pattern.matcher(plate);

        // Verificar si el texto coincide con la expresión regular
        return matcher.matches();
    }

    private String getUserId(){
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("id", "0");
        return idUser;
    }
}