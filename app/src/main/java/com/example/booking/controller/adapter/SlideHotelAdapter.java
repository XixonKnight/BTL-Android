package com.example.booking.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.booking.R;
import com.example.booking.model.dbo.ImageRoom;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SlideHotelAdapter extends PagerAdapter {
    private Context context;
    private List<ImageRoom> imageRoomList;

    public SlideHotelAdapter(Context context, List<ImageRoom> imageRoomList) {
        this.context = context;
        this.imageRoomList = imageRoomList;
    }

    @Override
    public int getCount() {
        return imageRoomList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_item, container, false);
        ImageView imageView = view.findViewById(R.id.img_item_slide);
        Picasso.get().load(imageRoomList.get(position).getImage()).into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
