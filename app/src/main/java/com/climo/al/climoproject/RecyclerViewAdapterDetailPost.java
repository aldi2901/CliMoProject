package com.climo.al.climoproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.climo.al.climoproject.Models.Postingan;

import java.util.List;

public class RecyclerViewAdapterDetailPost extends RecyclerView.Adapter<RecyclerViewAdapterDetailPost.ViewHolder> {
    private Context mContext;
    public List<Postingan> blogList2;
    RequestOptions options;
    private ConstraintLayout constraintLayout;

    public RecyclerViewAdapterDetailPost(Context aContext,List<Postingan> blogList2){
        this.mContext = aContext;
        this.blogList2 = blogList2;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.view_design_detail_post, constraintLayout, false);

        ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.cons);

        return new RecyclerViewAdapterDetailPost.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        String namaGunung = blogList2.get(position).getTujuanGunung();
        String tglMendaki = blogList2.get(position).getTglMendaki();
        String namaUser = blogList2.get(position).getUserName();
        String lokasi = blogList2.get(position).getMeetingPoint();
        String latitude = blogList2.get(position).getLatitude();
        String longitude = blogList2.get(position).getLongitude();
        String deskripsi = blogList2.get(position).getDeskripsi();
        viewHolder.setGunungText(namaGunung);
        viewHolder.setTglText(tglMendaki);
        viewHolder.setMeepoText(lokasi);
        viewHolder.setLatLngText(latitude+","+longitude);
        viewHolder.setDeskText(deskripsi);



    }

    @Override
    public int getItemCount() {
        return blogList2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView namaGunungView, tglMendakiView,namaView, meepoView, latlngView, deskView;
        private View nView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nView = itemView;
        }
        public void setGunungText(String gunungText){
            namaGunungView = nView.findViewById(R.id.sp_gunung_id);
            namaGunungView.setText(gunungText);

        }
        public void setTglText(String tglText){
            tglMendakiView = nView.findViewById(R.id.txt_tgl);
            tglMendakiView.setText(tglText);

        }

        public void setMeepoText(String meepoText) {
            meepoView = nView.findViewById(R.id.tvPickUpFrom);
            meepoView.setText(meepoText);
        }

        public void setLatLngText(String latlngText) {
            latlngView = nView.findViewById(R.id.tvPickUpLatLng);
            latlngView.setText(latlngText);
        }

        public void setDeskText(String deskText){
            deskView = nView.findViewById(R.id.deskripsi);
            deskView.setText(deskText);
        }
    }
}
