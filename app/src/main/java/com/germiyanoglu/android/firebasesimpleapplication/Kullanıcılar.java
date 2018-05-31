package com.germiyanoglu.android.firebasesimpleapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Kullanıcılar implements Parcelable{

    private String ad;
    private String soyad;
    private int yaş;

    public Kullanıcılar(){}


    protected Kullanıcılar(Parcel in) {
        ad = in.readString();
        soyad = in.readString();
        yaş = in.readInt();
    }

    public static final Creator<Kullanıcılar> CREATOR = new Creator<Kullanıcılar>() {
        @Override
        public Kullanıcılar createFromParcel(Parcel in) {
            return new Kullanıcılar(in);
        }

        @Override
        public Kullanıcılar[] newArray(int size) {
            return new Kullanıcılar[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ad);
        dest.writeString(soyad);
        dest.writeInt(yaş);
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public int getYaş() {
        return yaş;
    }

    public void setYaş(int yaş) {
        this.yaş = yaş;
    }
}
