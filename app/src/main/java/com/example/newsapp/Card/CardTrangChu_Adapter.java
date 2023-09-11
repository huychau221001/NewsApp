package com.example.newsapp.Card;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.R;
import com.example.newsapp.Thoitiet.Thoitiet2;
import com.example.newsapp.TruyenDuLieu;

import java.util.ArrayList;
import java.util.List;

public class CardTrangChu_Adapter extends RecyclerView.Adapter<CardTrangChu_Adapter.ViewHolder> {
    private ArrayList<NoiDungModel> noiDungModelArrayList;
    private ClickItem clickItem;

    public CardTrangChu_Adapter(ArrayList<NoiDungModel> noiDungModelArrayList, ClickItem clickItem1) {
        this.noiDungModelArrayList = noiDungModelArrayList;
        this.clickItem = clickItem1;
    }

    public void setCardTrangChu(ArrayList<NoiDungModel> noiDungModelArrayList) {
        this.noiDungModelArrayList = noiDungModelArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_trangchu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoiDungModel noiDungModel = noiDungModelArrayList.get(position);
        holder.txt_thoigian.setText(noiDungModel.getThoigian());
        holder.txt_tieude.setText(noiDungModel.getTieude());
        Glide.with(holder.img_anhbao).load(noiDungModel.getAnhbao()).into(holder.img_anhbao);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem.onClickItem(noiDungModel);
                TruyenDuLieu.Truyen_Linkbao = noiDungModel.getLinkbao();
            }
        });
    }

    @Override
    public int getItemCount() {
        return noiDungModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_anhbao;
        TextView txt_tieude, txt_thoigian;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_anhbao = itemView.findViewById(R.id.img_anhbao_trangchu);
            txt_tieude = itemView.findViewById(R.id.txt_tieude_trangchu);
            txt_thoigian = itemView.findViewById(R.id.txt_tgiandangbai_trangchu);
            cardView = itemView.findViewById(R.id.layout_card_trangchu);
        }

        @Override
        public void onClick(View view) {
        }
    }
}
