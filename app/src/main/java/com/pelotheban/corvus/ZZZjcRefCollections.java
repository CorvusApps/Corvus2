package com.pelotheban.corvus;

public class ZZZjcRefCollections {

    private String title;
    private String des;
    private String imageLink;
    private String notes;
    private String coluid;
    private int coincount;
    private int coincountall;



    public ZZZjcRefCollections(String title, String des, String imageLink, String notes, String coluid, int coincount, int coincountall) {
        this.title = title;
        this.des = des;
        this.imageLink = imageLink;
        this.notes = notes;
        this.coluid = coluid;
        this.coincount = coincount;
        this.coincountall = coincountall;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getNotes() {
        return notes;    }

    public void setNotes(String notes) {
        this.notes = notes;

    }

    public String getColuid() {
        return coluid;    }

    public void setColuid(String coluid) {
        this.coluid = coluid;

    }

    public int getCoincount() {

        return coincount;
    }

    public void setCoincount (int coincount) {
        this.coincount = coincount;
    }

    public int getCoincountall() {

        return coincountall;
    }

    public void setCoincountall (int coincountall) {
        this.coincountall = coincountall;
    }

    public ZZZjcRefCollections(){

    }
}

