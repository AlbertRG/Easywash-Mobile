package com.example.easywash;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.easywash.Adapters.CarListAdapter;
import com.example.easywash.rest.ApiInterface;
import com.example.easywash.rest.Car;
import com.example.easywash.rest.CarParcelable;
import com.example.easywash.rest.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

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
                    //brand.setText(car.get);;
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
                // Crea un objeto "updatedCar" con los campos que deseas actualizar
                String col = color.getText().toString();
                Log.d("color",col);
                Car updatedCar = new Car(getUserId(),plate.getText().toString(),brand.getText().toString(),model.getText().toString(),year.getText().toString(),col);
                Log.d("before update model",updatedCar.getModel());
                Log.d("before update plate",updatedCar.getPlate());
                Log.d("before update year",updatedCar.getYear());
                Log.d("before update color",updatedCar.getColor());
                // Crea una instancia de Retrofit y ApiInterface (esto varía según cómo lo configures en tu aplicación)
                RetrofitClient retrofit = new RetrofitClient();
                ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);

                // Realiza la solicitud PATCH
                Call<Car> call = api.updateCar(id, updatedCar);
                Log.d("id edit",id);
                call.enqueue(new Callback<Car>() {
                    @Override
                    public void onResponse(Call<Car> call, Response<Car> response) {
                        if (response.isSuccessful()) {
                            // El carro ha sido actualizado con éxito
                            Car car = response.body();
                            Log.d("update model",car.getModel());
                            Log.d("update plate",car.getPlate());
                            Log.d("update year",car.getYear());
                            Log.d("update color",car.getColor());
                            Toast.makeText(getApplicationContext(),"Auto editado", Toast.LENGTH_SHORT).show();
                            finish();



                        } else {
                            // Maneja el caso en el que la solicitud PATCH no fue exitosa
                            Toast.makeText(getApplicationContext(),"Auto editado", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Car> call, Throwable t) {
                        // Maneja errores de red o de la API
                    }
                });


            }//editonClick
        });//edit

        //delete car
        delete = findViewById(R.id.btnDeleteVehicle);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                    Toast.makeText(getApplicationContext(),"Auto eliminado", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("adapterUpdated", true); // Puedes pasar cualquier valor o bandera que desees
                                    setResult(RESULT_OK, intent);



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
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });

    }//onCreate

    private String getUserId(){
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("id", "0");
        return idUser;
    }



}