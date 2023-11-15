package com.example.easywash;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.easywash.rest.ApiInterface;
import com.example.easywash.rest.Car;
import com.example.easywash.rest.RetrofitClient;
import com.example.easywash.rest.Service;
import com.example.easywash.rest.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceActivity extends AppCompatActivity {

    ImageView back;
    TextView numTicket, dateTicket, timeTicket, nameTicket, plateTicket, brandTicket, modelTicket, colorTicket, serviceTicket, methodTicket, priceTicket, yearTicket;
    String idTicket = "";
    int cont = 0;
    ProgressDialog progressDialog;
    AlertDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        numTicket = findViewById(R.id.txtTicket);
        dateTicket = findViewById(R.id.txtDateTicket);
        timeTicket = findViewById(R.id.txtTimeTicket);
        nameTicket = findViewById(R.id.txtNameTicket);
        plateTicket = findViewById(R.id.txtPlateTicket);
        brandTicket = findViewById(R.id.txtBrandTicket);
        modelTicket = findViewById(R.id.txtModelTicket);
        colorTicket = findViewById(R.id.txtColorVehicleTicket);
        serviceTicket = findViewById(R.id.txtServiceTicket);
        methodTicket = findViewById(R.id.txtPaymentTicket);
        priceTicket = findViewById(R.id.txtPriceTicket);
        yearTicket = findViewById(R.id.txtYearTicket);
        //Fill the fields
        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                idTicket = null;
            } else {
                idTicket = extras.getString("ID");
            }
        } else {
            idTicket = (String) savedInstanceState.getSerializable("ID");

        }

        //API
        //Conexion a la API
        RetrofitClient retrofit = new RetrofitClient();
        ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
        // Construye el cuerpo de la solicitud
        Call<Service> call = api.getTicketById(idTicket);
        //Llamada a APIREST
        // Crear un LayoutInflater para inflar el diseño personalizado
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_main, null);

// Configurar las vistas del diseño personalizado
       // ProgressBar progressBar = dialogView.findViewById(R.id.customProgressBar);
       // TextView messageTextView = dialogView.findViewById(R.id.customMessage);

       // progressBar.setIndeterminate(true); // O personaliza la barra de progreso según tus necesidades
        //messageTextView.setText("Cargando..."); // Puedes personalizar el mensaje aquí

// Crear un AlertDialog personalizado
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Base_Theme_Easywash); // Agregar un estilo para el diálogo
        builder.setView(dialogView); // Establecer el diseño personalizado
        builder.setCancelable(false); // Evitar que el usuario cierre el diálogo tocando fuera de él

        customDialog = builder.create();

// Mostrar el diálogo a pantalla completa
        customDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Hacer que el fondo sea transparente

        customDialog.show();
        call.enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                if (response.isSuccessful()) {
                    boolean login = false;
                    Service service = response.body();
                    numTicket.setText(service.getId());
                    setTime(service.getDate());
                    setName(service.getClient());
                    setCar(service.getCar());
                    methodTicket.setText(service.getPaymethod());
                    priceTicket.setText("$"+service.getTotal());
                    serviceTicket.setText(service.getService());
                }
               // Log.d("cont", String.valueOf(cont));
                //cont++;
                //if(cont==3)
                 //   progressDialog.dismiss();


            }//onResponse

            @Override
            public void onFailure(Call<Service> call, Throwable t) {
                Log.e("ERRORRR onViewCreated",t.getMessage());
                progressDialog.dismiss();
            }

        });
        //API

        back = findViewById(R.id.imgBackService);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }//onCreate
    private void setTime(String fullDate){
        //Date and time

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        DateTimeFormatter outputTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");


        // Formatea la fecha según el nuevo patrón

        try {
            LocalDateTime dateTime = LocalDateTime.parse(fullDate, formatter);

            // Ajusta la zona horaria a "UTC"
            ZoneId utcZone = ZoneId.of("UTC");
            ZonedDateTime zonedDateTime = dateTime.atZone(utcZone);

            // Convierte a tu zona horaria local
            ZoneId localZone = ZoneId.of("America/Mexico_City");
            ZonedDateTime localDateTime = zonedDateTime.withZoneSameInstant(localZone);

            String formattedDate = dateTime.format(outputFormatter);
            String formattedTime = localDateTime.toLocalTime().format(outputTimeFormatter);


            Log.d("time", formattedTime);
            Log.d("formatted", formattedDate);
            timeTicket.setText(formattedTime);
            dateTicket.setText(formattedDate);

        } catch (DateTimeParseException e) {
            e.printStackTrace();
            Log.e("DateTimeParseException", e.getMessage());
        }

    }//setTime

    private void setName(String id){
        SharedPreferences sharedPreferences = this.getSharedPreferences("User", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name","User");
        String last = sharedPreferences.getString("lastname","");
        nameTicket.setText(name+" "+last);


    }//setName

    private void setCar(String id){
        if(id == null){
            plateTicket.setText("Auto eliminado");
            customDialog.dismiss();
            return;

        }
        //API
        //Conexion a la API
        RetrofitClient retrofit = new RetrofitClient();
        ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
        // Construye el cuerpo de la solicitud
        Call<Car> call = api.getCarById(id);
        //Llamada a APIREST
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                if (response.isSuccessful()) {
                    boolean login = false;
                    Car car = response.body();
                    plateTicket.setText(car.getPlate());
                    brandTicket.setText(car.getBrand());
                    modelTicket.setText(car.getModel());
                    yearTicket.setText(car.getYear());
                    colorTicket.setText(car.getColor());
                }
                customDialog.dismiss();


            }//onResponse

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Log.e("ERRORRR onViewCreated",t.getMessage());
            }

        });
        //API

    }//setCar



}