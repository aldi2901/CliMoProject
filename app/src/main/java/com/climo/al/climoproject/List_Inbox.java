package com.climo.al.climoproject;

public class List_Inbox {
    private String Isi_Inbox;
    private String Nama_User;
    private int Thumbnail_User;

    public List_Inbox(String isi_Inbox, String nama_User, int thumbnail_User) {
        Isi_Inbox = isi_Inbox;
        Nama_User = nama_User;
        Thumbnail_User = thumbnail_User;
    }

    public String getIsi_Inbox() {
        return Isi_Inbox;
    }

    public void setIsi_Inbox(String isi_Inbox) {
        Isi_Inbox = isi_Inbox;
    }

    public String getNama_User() {
        return Nama_User;
    }

    public void setNama_User(String nama_User) {
        Nama_User = nama_User;
    }

    public int getThumbnail_User() {
        return Thumbnail_User;
    }

    public void setThumbnail_User(int thumbnail_User) {
        Thumbnail_User = thumbnail_User;
    }
}
