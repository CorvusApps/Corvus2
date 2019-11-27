package com.pelotheban.corvus;

public class ZZZjcCoins {

    private String personage;
    private String denomination;
    private String ricvar;
    private String weight;
    private String diameter;
    private String imageLink;
    private String coinuid;
    private int id;
    private String mint;
    private String obvdesc;
    private String obvleg;
    private String revdesc;
    private String revleg;
    private String provenance;
    private int value;
    private String notes;
    private int sortric;






    public ZZZjcCoins(String personage, String denomination, String imageLink, String ricvar, String coinuid,
                            String weight, String diameter, int id, String mint, String obvdesc, String obvleg,
                      String revdesc, String revleg, String provenance, int value, String notes, int sortric) {
        this.personage = personage;
        this.denomination = denomination;
        this.imageLink = imageLink;
        this.ricvar = ricvar;
        this.coinuid = coinuid;
        this.weight = weight;
        this.diameter = diameter;
        this.id = id;
        this.mint = mint;
        this.obvdesc = obvdesc;
        this.obvleg = obvleg;
        this.revdesc = revdesc;
        this.revleg = revleg;
        this.provenance = provenance;
        this.value = value;
        this.notes = notes;
        this.sortric = sortric;


    }

    public String getPersonage() {
        return personage;

    }

    public void setPersonage(String personage) {
        this.personage = personage;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getRicvar() {
        return ricvar;    }

    public void setRicvar(String ricvar) {
        this.ricvar = ricvar;

    }

    public String getCoinuid() {
        return coinuid;    }

    public void setCoinuid(String coinuid) {
        this.coinuid = coinuid;

    }
    public String getWeight() {
        return weight;    }

    public void setWeight(String weight) {
        this.weight = weight;

    }
    public String getDiameter() {
        return diameter;    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;

    }

    public int getId() {

        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getMint() {
        return mint;    }

    public void setMint(String mint) {
        this.mint = mint;

    }

    public String getObvdesc() {
        return obvdesc;    }

    public void setObvdesc(String obvdesc) {
        this.obvdesc = obvdesc;

    }

    public String getObvleg() {
        return obvleg;    }

    public void setObvleg(String obvleg) {
        this.obvleg = obvleg;

    }

    public String getRevdesc() {
        return revdesc;    }

    public void setRevdesc(String revdesc) {
        this.revdesc = revdesc;

    }

    public String getRevleg() {
        return revleg;    }

    public void setRevleg(String revleg) {
        this.revleg = revleg;

    }

    public String getProvenance() {
        return provenance;    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;

    }

    public int getValue() {
        return value;    }

    public void setValue(int value) {
        this.value = value;

    }

    public String getNotes() {
        return notes;    }

    public void setNotes(String notes) {
        this.notes = notes;

    }

    public int getSortric() {
        return sortric;    }

    public void setSortric(int sortric) {
        this.sortric = sortric;

    }


    public ZZZjcCoins(){

    }

}
