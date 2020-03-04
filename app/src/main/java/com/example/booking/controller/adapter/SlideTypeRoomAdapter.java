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
import com.example.booking.model.TypeRoom;

import java.util.List;

public class SlideTypeRoomAdapter extends RecyclerView.Adapter<SlideTypeRoomAdapter.ViewHolder> {
    private Context context;
    private List<TypeRoom> list;

    public SlideTypeRoomAdapter(Context context, List<TypeRoom> list) {
        this.context = context;
        this.list = list;
    }

    //    private List<>
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_type_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TypeRoom typeRoom = list.get(position);
        holder.lblPrice.setText(String.valueOf(typeRoom.getPrice()));
        holder.lblNameTypeRoom.setText(typeRoom.getLblName());
        if (typeRoom.getLblName().equalsIgnoreCase("Phòng đơn")) {
            holder.imageView.setImageResource(R.drawable.image_room_single);
        } else if (typeRoom.getLblName().equalsIgnoreCase("Phòng đôi")) {
            holder.imageView.setImageResource(R.drawable.image_room_double);
        } else if (typeRoom.getLblName().equalsIgnoreCase("Phòng gia đình")) {
            holder.imageView.setImageResource(R.drawable.image_room_family);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView lblNameTypeRoom, lblPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_type_room);
            lblNameTypeRoom = itemView.findViewById(R.id.lbl_name_type_room);
            lblPrice = itemView.findViewById(R.id.lbl_price);
        }
    }
}
