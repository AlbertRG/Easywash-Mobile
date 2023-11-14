package com.example.easywash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.easywash.Adapters.CarListAdapter;
import com.example.easywash.rest.ApiInterface;
import com.example.easywash.rest.Car;
import com.example.easywash.rest.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarsFragment extends Fragment {
    RecyclerView carList;
    ArrayList<Car> carArrayList;
    private ActivityResultLauncher<Intent> registerVehicleLauncher;

    private View vista;
    private AppCompatButton buttontest;
    private AppCompatButton buttontestedit;
    Intent intent;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public CarsFragment() {
        // Required empty public constructor
    }

    public static CarsFragment newInstance(String param1, String param2) {
        CarsFragment fragment = new CarsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_cars, container, false);


        //buttons
        buttontest = vista.findViewById(R.id.carssignup);
        buttontest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent = new Intent(getActivity(), RegisterVehicleActivity.class);
                //startActivity(intent);

                Intent intent = new Intent(getActivity(), RegisterVehicleActivity.class);
                registerVehicleLauncher.launch(intent);
            }
        });

        buttontestedit = vista.findViewById(R.id.carsitemMyAccount);
        buttontestedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), EditVehicleActivity.class);
                startActivity(intent);
            }
        });

        return vista;
    }//OnCreateView


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        carList = vista.findViewById(R.id.recyclerCar);
        carList.setLayoutManager(new LinearLayoutManager(getActivity()));
        carList.setHasFixedSize(true);
        carArrayList = new ArrayList<>();
        //API
        //Conexion a la API
        RetrofitClient retrofit = new RetrofitClient();
        ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
        // Construye el cuerpo de la solicitud
        Call<Car[]> call = api.getCarsList("json");
        //Llamada a APIREST
        call.enqueue(new Callback<Car[]>() {
            @Override
            public void onResponse(Call<Car[]> call, Response<Car[]> response) {
                if (response.isSuccessful()) {
                    boolean login = false;
                    Car[] cars = response.body();
                    int pos = 0;
                    for(Car car1 : cars){
                        if(car1.getOwner().equals(getUserId())) {
                            carArrayList.add(car1);
                            Log.d("onViewCreated owner", car1.getOwner());
                            Log.d("onViewCreated model", car1.getModel());
                            Log.d("onViewCreated carArrayList",carArrayList.get(pos).getModel());
                            pos++;
                        }

                    }

                }
                Log.d("onViewCreated carArrayList size", String.valueOf(carArrayList.size()));
                CarListAdapter carListAdapter = new CarListAdapter(carArrayList,getActivity(),registerVehicleLauncher);
                carList.setAdapter(carListAdapter);
                carListAdapter.notifyDataSetChanged();
            }//onResponse

            @Override
            public void onFailure(Call<Car[]> call, Throwable t) {
                Log.e("ERRORRR onViewCreated",t.getMessage());
            }

        });
        //API

        registerVehicleLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            getItemscar();
                        }
                    }
                }
        );
    }//OnviewCreated

    private String getUserId(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString("id", "0");
        return idUser;
    }

    private void getItemscar(){
        int id = getId();
        Log.d("fragment ID", String.valueOf(id));
        carList = vista.findViewById(R.id.recyclerCar);
        carList.setLayoutManager(new LinearLayoutManager(getActivity()));
        carList.setHasFixedSize(true);
        carArrayList = new ArrayList<>();
        //API
        //Conexion a la API
        RetrofitClient retrofit = new RetrofitClient();
        ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
        // Construye el cuerpo de la solicitud
        Call<Car[]> call = api.getCarsList("json");
        //Llamada a APIREST
        call.enqueue(new Callback<Car[]>() {
            @Override
            public void onResponse(Call<Car[]> call, Response<Car[]> response) {
                if (response.isSuccessful()) {
                    boolean login = false;
                    Car[] cars = response.body();
                    int pos = 0;
                    for(Car car1 : cars){
                        if(car1.getOwner().equals(getUserId())) {
                            carArrayList.add(car1);
                            Log.d("onViewCreated owner", car1.getOwner());
                            Log.d("onViewCreated model", car1.getModel());
                            Log.d("onViewCreated carArrayList",carArrayList.get(pos).getModel());
                            pos++;
                        }

                    }

                }
                Log.d("onViewCreated carArrayList size", String.valueOf(carArrayList.size()));
                CarListAdapter carListAdapter = new CarListAdapter(carArrayList,getActivity(),registerVehicleLauncher);
                carList.setAdapter(carListAdapter);
                carListAdapter.notifyDataSetChanged();
            }//onResponse

            @Override
            public void onFailure(Call<Car[]> call, Throwable t) {
                Log.e("ERRORRR onViewCreated",t.getMessage());
            }

        });
        //API
    }//getItemscar
    public void updateCarList() {
        carArrayList.clear();
        getItemscar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == Activity.RESULT_OK) {
            boolean adapterUpdated = data.getBooleanExtra("adapterUpdated", false);
            if (adapterUpdated) {
                // Actualiza el adaptador
                updateCarList();
            }
        }
    }
}