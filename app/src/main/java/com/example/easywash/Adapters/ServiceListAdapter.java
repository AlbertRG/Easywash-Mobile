package com.example.easywash.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easywash.EditVehicleActivity;
import com.example.easywash.R;
import com.example.easywash.ServiceActivity;
import com.example.easywash.rest.Car;
import com.example.easywash.rest.Service;

import java.util.ArrayList;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ServiceViewHolder>{
    private Context context;
    private ArrayList<Service> serviceArrayList;
    private ActivityResultLauncher<Intent> serviceLauncher;

    public ServiceListAdapter(ArrayList<Service> serviceArrayList, Context context,  ActivityResultLauncher<Intent> launcher){
        this.serviceArrayList = serviceArrayList;
        this.context = context;
        this.serviceLauncher = launcher;
        Log.d("CarListAdapter constructor", String.valueOf(serviceArrayList.size()));
        for(Service service : serviceArrayList){


        }

    }
    @NonNull
    @Override
    public ServiceListAdapter.ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_car,parent, false);

        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceListAdapter.ServiceViewHolder holder, int position) {
        holder.btnService.setText(serviceArrayList.get(position).getId());

        Log.d("onBindViewHolder Code",serviceArrayList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return serviceArrayList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView btnService;
        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            btnService = itemView.findViewById(R.id.list_car_button);
            btnService.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent( context, ServiceActivity.class);
                    intent.putExtra("ID", serviceArrayList.get(getAbsoluteAdapterPosition()).getId());
                    serviceLauncher.launch(intent);
                }
            });
        }
    }
}
