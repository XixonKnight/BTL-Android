package com.example.booking.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.booking.R;
import com.example.booking.model.ItemSlide;

import java.util.List;

public class SlidePagerAdapter extends PagerAdapter {
    private Context context;
    private List<ItemSlide> itemSlides;

    public SlidePagerAdapter(Context context, List<ItemSlide> itemSlides) {
        this.context = context;
        this.itemSlides = itemSlides;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.slide_item,container,false);
        ImageView img_item_slide = itemView.findViewById(R.id.img_item_slide);
        img_item_slide.setImageResource(itemSlides.get(position).getImage());
        container.addView(itemView);
        return itemView;
    }

    @Override
    public int getCount() {
        return itemSlides.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
