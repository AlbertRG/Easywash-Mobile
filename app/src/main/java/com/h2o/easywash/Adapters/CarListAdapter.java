package com.h2o.easywash.Adapters;

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

import com.h2o.easywash.EditVehicleActivity;
import com.h2o.easywash.R;
import com.h2o.easywash.rest.Car;

import java.util.ArrayList;

public class CarListAdapter extends RecyclerView.Adapter<CarListAdapter.CarViewHolder> {
    private Context context;
    private ArrayList<Car> carArrayList;
    private ActivityResultLauncher<Intent> editVehicleLauncher;


    public CarListAdapter(ArrayList<Car> carArrayList, Context context,  ActivityResultLauncher<Intent> launcher){
        this.carArrayList = carArrayList;
        this.context = context;
        this.editVehicleLauncher = launcher;
        Log.d("CarListAdapter constructor", String.valueOf(carArrayList.size()));
        for(Car car1 : carArrayList){
                Log.d("plate constructor", car1.getOwner());
                Log.d("model constructor", car1.getModel());

        }

    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_car,parent, false);
        Log.d("onCreateViewHolder","holaaaa");

        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        holder.btnPlate.setText(carArrayList.get(position).getPlate());
        Log.d("onBindViewHolder Plate",carArrayList.get(position).getPlate());


    }


    @Override
    public int getItemCount() {
        return carArrayList.size();
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {
        TextView btnPlate;
        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPlate = itemView.findViewById(R.id.list_car_button);
            btnPlate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Context context = view.getContext();
                    //Intent intent = new Intent(context, EditVehicleActivity.class);
                    //intent.putExtra("ID", carArrayList.get(getAbsoluteAdapterPosition()).getId());
                    //context.startActivity(intent);

                    Intent intent = new Intent( context, EditVehicleActivity.class);
                    intent.putExtra("ID", carArrayList.get(getAbsoluteAdapterPosition()).getId());
                    editVehicleLauncher.launch(intent);

                }
            });

            Log.d("CarViewHolder Plate","hola");

        }

    }
}
