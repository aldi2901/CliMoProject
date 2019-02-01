package com.climo.al.climoproject.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.Exclude;

public class Postingan {
    public String deskripsi;
    public String tujuanGunung;
    public String userName;
    public String tglMendaki;
    public String imgGunung;
    public String meetingPoint;
    public String latitude;
    public String longitude;
    public String post_id;
    public LatLng latlng;

    public Postingan(){

    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTujuanGunung() {
        return tujuanGunung;
    }

    public void setTujuanGunung(String tujuanGunung) {
        this.tujuanGunung = tujuanGunung;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTglMendaki() {
        return tglMendaki;
    }

    public void setTglMendaki(String tglMendaki) {
        this.tglMendaki = tglMendaki;
    }

    public String getImgGunung() {
        return imgGunung;
    }

    public void setImgGunung(String imgGunung) {
        this.imgGunung = imgGunung;
    }

    public String getMeetingPoint() {
        return meetingPoint;
    }

    public void setMeetingPoint(String meetingPoint) {
        this.meetingPoint = meetingPoint;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Exclude
    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    @Override
    public String toString() {
        return " " + tujuanGunung + "\n" +
                " " + tglMendaki + "\n" +
                " " + meetingPoint + "\n" +
                " " + latitude + "\n" +
                " " + longitude + "\n" +
                " " + deskripsi;
    }

    public Postingan(String tujuanGunung, String tglMendaki, String meetingPoint, String latitude, String longitude, String deskripsi, String userName) {
        this.deskripsi = deskripsi;
        this.tujuanGunung = tujuanGunung;
        this.userName = userName;
        this.tglMendaki = tglMendaki;
        //this.imgGunung = imgGunung;
        this.meetingPoint = meetingPoint;
        this.latitude = latitude;
        this.longitude = longitude;
        //this.post_id = post_id;
    }

}
