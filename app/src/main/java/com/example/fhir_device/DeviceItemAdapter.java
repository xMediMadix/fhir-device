package com.example.fhir_device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeviceItemAdapter extends RecyclerView.Adapter<DeviceItemAdapter.ViewHolder>{
    private ArrayList<Device> deviceItemsData;
    private ArrayList<Device> deviceItemsDataAll;
    private Context context;
    private int lastPosition = -1;

    DeviceItemAdapter(Context context, ArrayList<Device> itemsData){
        this.deviceItemsData = itemsData;
        this.deviceItemsDataAll = itemsData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // sema bindolasa az adapterhez
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return deviceItemsData.size();
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceItemAdapter.ViewHolder holder, int position) {
        Device currentItem = deviceItemsData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition){
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            holder.itemView.startAnimation(anim);
            lastPosition = holder.getAdapterPosition();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameText;
        private TextView manufacturerText;
        private TextView statusText;
        private TextView typeText;
        private TextView serialNumberText;
        private TextView itemDateText;

        public ViewHolder(View itemView){
            super(itemView);

            nameText = itemView.findViewById(R.id.item_name);
            manufacturerText = itemView.findViewById(R.id.item_manufacturer);
            statusText = itemView.findViewById(R.id.item_status);
            typeText = itemView.findViewById(R.id.item_type);
            serialNumberText = itemView.findViewById(R.id.item_serial_number);
            itemDateText = itemView.findViewById(R.id.item_date);

            itemView.findViewById(R.id.edit_item_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: modositas
                }
            });

            itemView.findViewById(R.id.delete_item_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: torles
                }
            });
        }

        public void bindTo(Device currentItem) {
            nameText.setText(currentItem.getDeviceName()[0]);
            manufacturerText.setText(currentItem.getManufacturer());
            statusText.setText(currentItem.getStatus());
            typeText.setText(currentItem.getType().getText());
            serialNumberText.setText(currentItem.getSerialNumber());
            itemDateText.setText(currentItem.getManufacturerDate().toString());
        }
    }
}
