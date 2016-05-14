package com.testapp.hairsimulator;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by bharath.simha on 12/01/16.
 */
public class HairStyleImagesAdapter extends RecyclerView.Adapter<HairStyleImagesAdapter.ContactViewHolder> {

    private int itemSize;
    private int[] imagesArray;
    private ArrayList<ContactViewHolder> mContactView = new ArrayList<>();

    public HairStyleImagesAdapter() {

    }

    public void imagesArray(int[] imagesArray){
        this.imagesArray=imagesArray;
        this.itemSize=imagesArray.length;
    }

    @Override
    public int getItemCount() {
        return itemSize;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, final int i) {
        contactViewHolder.imageView.setImageResource(imagesArray[i]);

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.hair_styels_item, viewGroup, false);

        mContactView.add(new ContactViewHolder(itemView));

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imageView;
        protected LinearLayout card_view;


        public ContactViewHolder(View v) {
            super(v);
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.card_view);
            layout.setLayoutParams(setCustomLinearLayoutParams(Utility.dpToPx(118), Utility.dpToPx(152)));

            imageView = (ImageView) v.findViewById(R.id.hairStyleImageItem);
            card_view = (LinearLayout) v.findViewById(R.id.card_view);

            GradientDrawable gradientDrawable = (GradientDrawable) card_view.getBackground();
            gradientDrawable.setStroke(2, Color.parseColor("#FFFFFF"));

        }

        public static LinearLayout.LayoutParams setCustomLinearLayoutParams(int weight, int height) {
            LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(weight, height);
            linearLayout.setMargins(Utility.dpToPx(4), Utility.dpToPx(4), Utility.dpToPx(4), Utility.dpToPx(4));
            return linearLayout;
        }

    }


}
