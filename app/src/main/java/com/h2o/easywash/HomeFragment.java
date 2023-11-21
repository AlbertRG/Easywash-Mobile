package com.h2o.easywash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.h2o.easywash.Adapters.ServiceListAdapter;
import com.h2o.easywash.rest.ApiInterface;
import com.h2o.easywash.rest.RetrofitClient;
import com.h2o.easywash.rest.Service;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {


    RecyclerView serviceRecycler;
    ArrayList<Service> serviceArrayList;
    private ActivityResultLauncher<Intent> serviceLauncher;

    private View vista;
    private AppCompatButton buttontest;
    Intent intent;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        vista = inflater.inflate(R.layout.fragment_home, container, false);
        buttontest = vista.findViewById(R.id.carsitem);
        buttontest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getServiceList();
            }
        });

        return vista;
    }//onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        serviceRecycler = vista.findViewById(R.id.recyclerService);
        serviceRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        serviceRecycler.setHasFixedSize(true);
        serviceArrayList = new ArrayList<>();

        //API
        //Conexion a la API
        RetrofitClient retrofit = new RetrofitClient();
        ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
        // Construye el cuerpo de la solicitud
        Call<Service[]> call = api.getServiceList("json");
        //Llamada a APIREST
        call.enqueue(new Callback<Service[]>() {
            @Override
            public void onResponse(Call<Service[]> call, Response<Service[]> response) {
                if (response.isSuccessful()) {
                    boolean login = false;
                    Service[] services = response.body();
                    int pos = 0;
                    for(Service service : services){
                        if(service.getClient().equals(getUserId())) {
                            serviceArrayList.add(service);
                            Log.d("onViewCreated owner", service.getClient());
                            Log.d("onViewCreated price", service.getService());
                            Log.d("onViewCreated carArrayList",serviceArrayList.get(pos).getService());
                            pos++;
                        }

                    }

                }
                ArrayList<Service> servicenewArray = new ArrayList<>();
                Log.d("AANNTESS Terminado","HOLA");
                int lastIndex = serviceArrayList.size() - 1;
                for (int i = lastIndex; i >= 0 && servicenewArray.size() < 5; i--) {
                    Service item = serviceArrayList.get(i);
                    if ("Terminado".equals(item.getStatus())) {
                        servicenewArray.add(item);
                        Log.d("Terminado",item.getService());

                    }
                    Log.d("Terminado IF DESOUES",item.getService());

                }
                Log.d("servicenew",String.valueOf(servicenewArray.size()));
                serviceArrayList = new ArrayList<>();
                serviceArrayList.addAll(servicenewArray);
                if(serviceArrayList.size()==5)
                    showDiscountDialog();
                Log.d("onViewCreated carArrayList size", String.valueOf(serviceArrayList.size()));
                ServiceListAdapter serviceListAdapter = new ServiceListAdapter(serviceArrayList,getActivity(),serviceLauncher);
                serviceRecycler.setAdapter(serviceListAdapter);
                serviceListAdapter.notifyDataSetChanged();
            }//onResponse

            @Override
            public void onFailure(Call<Service[]> call, Throwable t) {
                Log.e("ERRORRR onViewCreated",t.getMessage());
            }

        });
        //API
        serviceLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            getServiceList();
                        }
                    }
                }
        );
    }//onViewCreated
    private String getUserId(){
        // Verificar que el fragmento esté asociado a una actividad
        if (isAdded() && getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
            if (sharedPreferences != null) {
                // Verificar que las preferencias compartidas no sean nulas
                return sharedPreferences.getString("id", "0");
            } else {
                // Manejar el caso en que sharedPreferences sea nulo
                return "0";
            }
        } else {
            // Manejar el caso en que el fragmento no está asociado a una actividad
            return "0";
        }
    }

    private void getServiceList(){
        //API
        //Conexion a la API
        serviceArrayList = new ArrayList<>();
        RetrofitClient retrofit = new RetrofitClient();
        ApiInterface api = retrofit.getRetrofitInstance().create(ApiInterface.class);
        // Construye el cuerpo de la solicitud
        Call<Service[]> call = api.getServiceList("json");
        //Llamada a APIREST
        call.enqueue(new Callback<Service[]>() {
            @Override
            public void onResponse(Call<Service[]> call, Response<Service[]> response) {
                if (response.isSuccessful()) {
                    boolean login = false;
                    Service[] services = response.body();
                    int pos = 0;
                    for(Service service : services){
                        if(service.getClient().equals(getUserId())) {
                            serviceArrayList.add(service);
                            Log.d("onViewCreated owner", service.getClient());
                            Log.d("onViewCreated price", service.getService());
                            Log.d("onViewCreated carArrayList",serviceArrayList.get(pos).getService());
                            pos++;
                        }

                    }

                }
                ArrayList<Service> servicenewArray = new ArrayList<>();
                Log.d("AANNTESS Terminado","HOLA");
                int lastIndex = serviceArrayList.size() - 1;
                for (int i = lastIndex; i >= 0 && servicenewArray.size() < 5; i--) {
                    Service item = serviceArrayList.get(i);
                    if ("Terminado".equals(item.getStatus())) {
                        servicenewArray.add(item);
                        Log.d("Terminado",item.getService());

                    }
                    Log.d("Terminado IF DESOUES",item.getService());

                }
                Log.d("servicenew",String.valueOf(servicenewArray.size()));
                serviceArrayList = new ArrayList<>();
                serviceArrayList.addAll(servicenewArray);
                if(serviceArrayList.size()==5)
                    showDiscountDialog();
                Log.d("onViewCreated carArrayList size", String.valueOf(serviceArrayList.size()));
                ServiceListAdapter serviceListAdapter = new ServiceListAdapter(serviceArrayList,getActivity(),serviceLauncher);
                serviceRecycler.setAdapter(serviceListAdapter);
                serviceListAdapter.notifyDataSetChanged();
            }//onResponse

            @Override
            public void onFailure(Call<Service[]> call, Throwable t) {
                Log.e("ERRORRR onViewCreated",t.getMessage());
            }

        });
        //API
    }//getItemscar
    private void showDiscountDialog() {
        // Implementa la lógica para mostrar un diálogo que informa al usuario que ganó un descuento.
        // Puedes utilizar AlertDialog u otra clase de diálogo según tus necesidades.
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("¡Felicidades!");
        builder.setMessage("Has ganado un descuento del 25% en tu próximo lavado.");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Acciones al hacer clic en "Ok" en el diálogo
                dialog.dismiss();
            }
        });
        builder.show();
    }
}