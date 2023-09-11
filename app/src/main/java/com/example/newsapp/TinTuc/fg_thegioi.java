package com.example.newsapp.TinTuc;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.Card.CardTrangChu_Adapter;
import com.example.newsapp.Card.ClickItem;
import com.example.newsapp.Card.NoiDungModel;
import com.example.newsapp.DocBao.docbao;
import com.example.newsapp.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class fg_thegioi extends Fragment {
    private View view;

    private RecyclerView recyclerView;
    private CardTrangChu_Adapter cardTrangChu_adapter;
    private List<NoiDungModel> noiDungModelList = new ArrayList<>();
    Elements data;
    Document document;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_fg_thegioi, container, false);
        recyclerView = view.findViewById(R.id.recycler_fg_thegioi);

        Content content = new Content();
        content.execute();

        return view;
    }

    private class Content extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            cardTrangChu_adapter = new CardTrangChu_Adapter((ArrayList<NoiDungModel>) noiDungModelList, new ClickItem() {
                @Override
                public void onClickItem(NoiDungModel noiDungModel) {
                    Intent intentgiavang = new Intent(getActivity(), docbao.class);
                    startActivity(intentgiavang);
                }
            });
            recyclerView.setAdapter(cardTrangChu_adapter);
            cardTrangChu_adapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "https://tuoitre.vn/the-gioi.htm";
                document = Jsoup.connect(url).get();
                data = document.select("div.list__listing").select("div.box-category-middle").select("div.box-category-item");
                int size = data.size();
                for (int i = 0; i < size; i++) {
                    String tieude = data.select("h3.box-title-text").select("a.box-category-link-title").eq(i).text();
                    String thoigian = data.select("p.box-category-sapo").eq(i).text();
                    String anhbao = data.select("a.box-category-link-with-avatar.img-resize").eq(i).select("img").attr("src");
                    String linkbao = "https://tuoitre.vn" + data.select("a.box-category-link-with-avatar.img-resize").eq(i).attr("href");
                    Log.e("fg_moi", linkbao);
                    noiDungModelList.add(new NoiDungModel(tieude,thoigian,anhbao,linkbao));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
