package com.example.lijiang.bookandmovie.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cnrobin on 17-8-15.
 * Just Enjoy It!!!
 */

public class BookHelper implements Parcelable {
    private String img;
    private String bookName;
    private String author;
    private String publishing;
    private double rating;
    private String words;
    private String authorInfo;
    private String pubData;
    private String catalog;
    private int manNum;

    public int getManNum() {
        return manNum;
    }

    public void setManNum(int manNum) {
        this.manNum = manNum;
    }

    public String getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(String authorInfo) {
        this.authorInfo = authorInfo;
    }

    public String getPubData() {
        return pubData;
    }

    public void setPubData(String pubData) {
        this.pubData = pubData;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishing() {
        return publishing;
    }

    public void setPublishing(String publishing) {
        this.publishing = publishing;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(img);
        parcel.writeString(bookName);
        parcel.writeString(author);
        parcel.writeString(publishing);
        parcel.writeDouble(rating);
        parcel.writeString(words);
        parcel.writeString(authorInfo);
        parcel.writeString(pubData);
        parcel.writeString(catalog);
        parcel.writeInt(manNum);

    }

    public static final Creator<BookHelper> CREATOR = new Creator<BookHelper>() {

        @Override
        public BookHelper createFromParcel(Parcel parcel) {
            BookHelper helper = new BookHelper();
            helper.setImg(parcel.readString());
            helper.setBookName(parcel.readString());
            helper.setAuthor(parcel.readString());
            helper.setPublishing(parcel.readString());
            helper.setRating(parcel.readDouble());
            helper.setWords(parcel.readString());
            helper.setAuthorInfo(parcel.readString());
            helper.setPubData(parcel.readString());
            helper.setCatalog(parcel.readString());
            helper.setManNum(parcel.readInt());
            return helper;
        }

        @Override
        public BookHelper[] newArray(int i) {
            return new BookHelper[i];
        }
    };
}

