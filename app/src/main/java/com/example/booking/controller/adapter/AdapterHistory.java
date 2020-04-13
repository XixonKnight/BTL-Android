package com.example.booking.controller.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booking.R;
import com.example.booking.model.ItemHistory;

import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {
    private Context context;
    private List<ItemHistory> historyList;

    public AdapterHistory(Context context, List<ItemHistory> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_history, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemHistory itemHistory = historyList.get(position);
        holder.lblNameHotel.setText(itemHistory.getLblNameHotel());
        holder.nameTypeRoom.setText(itemHistory.getNameTypeRoom());
        holder.codeRoom.setText(itemHistory.getCodeRoom());
        holder.timeRegister.setText(itemHistory.getTimeRegister());
        holder.timeGoRoom.setText(itemHistory.getTimeGoRoom());
        holder.countRoom.setText(String.valueOf(itemHistory.getCountRoom()));
        holder.countPerson.setText(String.valueOf(itemHistory.getCountPerson()));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView lblNameHotel,nameTypeRoom,codeRoom,timeRegister,timeGoRoom,countRoom,countPerson;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lblNameHotel = itemView.findViewById(R.id.lblHotel);
            nameTypeRoom = itemView.findViewById(R.id.nameTypeRoom);
            codeRoom = itemView.findViewById(R.id.codeRoom);
            timeRegister = itemView.findViewById(R.id.timeRegister);
            timeGoRoom = itemView.findViewById(R.id.timeGoRoom);
            countRoom = itemView.findViewById(R.id.countRoom);
            countPerson = itemView.findViewById(R.id.countPerson);
        }
    }
}
