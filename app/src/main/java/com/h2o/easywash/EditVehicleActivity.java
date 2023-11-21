package com.h2o.easywash;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.h2o.easywash.rest.ApiInterface;
import com.h2o.easywash.rest.Car;
import com.h2o.easywash.rest.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditVehicleActivity extends AppCompatActivity {

    ImageView back;
    EditText plate, model, brand, color, year;
    String id = "";
    Button edit, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back = findViewById(R.id.imgBackCarsEdit);
        setContentView(R.layout.activity_edit_vehicle);
        plate = findViewById(R.id.editPlateText);
        model = findViewById(R.id.editModelText);
        brand = findViewById(R.id.editBrandText);
        color = findViewById(R.id.editColorText);
        year = findViewById(R.id.editYearText);
        //Fill the fields
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                id = null;
            } else {
                id = extras.getString("ID");
            }
        } else {
            id = (String) savedInstanceState.getSerializable("ID");

        }
        //API
        //Conexion a la API
        RetrofitClient retrofit = new RetrofitClient();
        ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
        // Construye el cuerpo de la solicitud
        Log.d("CAR ID ",id);
        Call<Car> call = api.getCarById(id);
        //Llamada a APIREST
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                if (response.isSuccessful()) {
                    boolean login = false;
                    Car car = response.body();
                    Log.d("car id", car.getId());
                    Log.d("car id", car.getModel());
                    plate.setText(car.getPlate());
                    model.setText(car.getModel());
                    brand.setText(car.getBrand());;
                    color.setText(car.getColor());
                    year.setText(car.getYear());




                }


            }//onResponse

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Log.e("ERRORRR onViewCreated",t.getMessage());
            }

        });
        //API
        back = findViewById(R.id.imgBackCarsEdit);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Edit Car
        edit = findViewById(R.id.btnEditVehicle);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable()) {
                    // Crea un objeto "updatedCar" con los campos que deseas actualizar
                    if (plate.getText().toString().equals("") || model.getText().toString().equals("") || brand.getText().toString().equals("") || color.getText().toString().equals("") || year.getText().toString().equals(""))
                        Toast.makeText(getApplicationContext(), "Llena todos los campos", Toast.LENGTH_SHORT).show();
                    else {
                        if (!checkPlate(plate.getText().toString().trim())) {
                            Toast.makeText(getApplicationContext(), "Placa no válida", Toast.LENGTH_SHORT).show();
                        } else if (verifyYear(year.getText().toString().trim())) {
                            //Instancia de retrofit para invocar a la api
                            RetrofitClient retrofit = new RetrofitClient();
                            //Instancia de api
                            ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
                            //Instancia de carro
                            Car car = new Car(getUserId(), plate.getText().toString().trim(), brand.getText().toString().trim(), model.getText().toString().trim(), year.getText().toString().trim(), color.getText().toString().trim());
                            // Construye el cuerpo de la solicitud
                            Call<Car> call = api.updateCar(id, car);
                            //Llamada a APIREST
                            call.enqueue(new Callback<Car>() {
                                @Override
                                public void onResponse(Call<Car> call, Response<Car> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Auto actualizado", Toast.LENGTH_LONG).show();
                                        Intent resultIntent = new Intent();
                                        setResult(RESULT_OK, resultIntent);
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
                                public void onFailure(Call<Car> call, Throwable t) {
                                    Log.e("ERRORRR", t.getMessage());

                                }

                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Año no válido", Toast.LENGTH_LONG).show();

                        }

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Conéctate a internet", Toast.LENGTH_LONG).show();

                }

            }//editonClick
        });//edit

        //delete car
        delete = findViewById(R.id.btnDeleteVehicle);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditVehicleActivity.this);
                    builder.setMessage("¿Desea eliminar este auto?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            RetrofitClient retrofit = new RetrofitClient();
                            ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
                            Call<Void> call = api.deleteCar(id);

                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        // La solicitud DELETE se realizó con éxito y el automóvil se eliminó
                                        // Realiza cualquier acción necesaria después de la eliminación.
                                        Toast.makeText(getApplicationContext(), "Auto eliminado", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.putExtra("adapterUpdated", true); // Puedes pasar cualquier valor o bandera que desees
                                        setResult(RESULT_OK, intent);


                                        finish();
                                    } else {
                                        // Maneja el caso en el que la solicitud DELETE no fue exitosa
                                        Toast.makeText(getApplicationContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    // Maneja errores de red o de la API
                                    Toast.makeText(getApplicationContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();

                                }
                            });
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

    }//onCreate

    private String getUserId(){
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("id", "0");
        return idUser;
    }
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
    private boolean verifyYear(String year){
        Calendar calendar = Calendar.getInstance();
        int añoActual = calendar.get(Calendar.YEAR);
        int añoIngresado = Integer.parseInt(year);

        if (añoIngresado <= añoActual) {
            // El año ingresado es inferior o igual al año actual
            return true;
        } else {
            // El año ingresado es superior al año actual
           return false;
        }
    }//Verifyyear
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }//isNetworkAvailable



}