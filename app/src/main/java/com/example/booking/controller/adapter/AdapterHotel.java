package com.example.booking.controller.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking.R;
import com.example.booking.activity.ActivityCustomer;
import com.example.booking.model.dbo.Hotel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterHotel extends RecyclerView.Adapter<AdapterHotel.ViewHolder> {
    private Context context;
    private List<Hotel> hotels;

    public AdapterHotel(Context context, List<Hotel> hotels) {
        this.context = context;
        this.hotels = hotels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_list_hotel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hotel hotel = hotels.get(position);
        holder.lbl_hotel.setText(hotel.getName());
        holder.lbl_address.setText(hotel.getAddress());
        Picasso.get().load(hotel.getImage()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView lbl_hotel, lbl_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_hotel);
            lbl_address = itemView.findViewById(R.id.lbl_address);
            lbl_hotel = itemView.findViewById(R.id.lbl_hotel);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Hotel hotel = hotels.get(this.getAdapterPosition());
            ((ActivityCustomer) context).setClickRecyclerView(hotel);
        }
    }
}
