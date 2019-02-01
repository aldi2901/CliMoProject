package com.climo.al.climoproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.climo.al.climoproject.Models.Postingan;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    public List<Postingan> blogList;
    RequestOptions options;
    private CardView cardView;

    public RecyclerViewAdapter(List<Postingan> blogList, Context aContext){
        this.mContext = aContext;
        this.blogList = blogList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardviewpost_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        final String namaGunung = blogList.get(position).getTujuanGunung();
        final String tglMendaki = blogList.get(position).getTglMendaki();
        String namaUser = blogList.get(position).getUserName();
        final String lokasi = blogList.get(position).getMeetingPoint();
        final String lat = blogList.get(position).getLatitude();
        final String lon = blogList.get(position).getLongitude();
        final String deskripsi = blogList.get(position).getDeskripsi();
        viewHolder.setGunungText(namaGunung);
        viewHolder.setTglText(tglMendaki);
        viewHolder.setnamaText(namaUser);
        viewHolder.setLatitudeText(lat);
        viewHolder.setLongitude(lon);
        viewHolder.setMeepoText(lokasi);

        viewHolder.setCardView(cardView);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailPost.class);
                intent.putExtra("Title", namaGunung);
                intent.putExtra("Tgl", tglMendaki);
                intent.putExtra("meepo", lokasi);
                intent.putExtra("latitude", lat);
                intent.putExtra("longitude", lon);
                intent.putExtra("Deskripsi", deskripsi);

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView namaGunungView, tglMendakiView,namaView, meepo, latitudeView, longitudeView;
        private View nView;
        private CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nView = itemView;
            cardView = (CardView) itemView.findViewById(R.id.cardView_id);
            //namaGunungView = (TextView) itemView.findViewById(R.id.nama_gunung_id);
        }

        public void setLatitudeText(String latitude) {
            latitudeView = nView.findViewById(R.id.latitude);
            latitudeView.setText(latitude);
        }

        public void setLongitude(String longitude) {
            longitudeView = nView.findViewById(R.id.longitude);
            longitudeView.setText(longitude);
        }

        public void setMeepoText(String lokasi) {
            meepo = nView.findViewById(R.id.meepo);
            meepo.setText(lokasi);
        }

        public void setCardView(CardView cardView) {
            cardView = nView.findViewById(R.id.cardView_id);

        }

        public void setGunungText(String gunungText){
            namaGunungView = nView.findViewById(R.id.nama_gunung_id);
            namaGunungView.setText(gunungText);

        }
        public void setTglText(String tglText){
            tglMendakiView = nView.findViewById(R.id.tgl_id);
            tglMendakiView.setText(tglText);

        }
        public void setnamaText(String namaText){
            namaView = nView.findViewById(R.id.nama_user_id);
            namaView.setText(namaText);

        }
    }
}
