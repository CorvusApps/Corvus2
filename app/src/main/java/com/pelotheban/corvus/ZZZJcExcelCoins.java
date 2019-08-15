package com.pelotheban.corvus;

public class ZZZJcExcelCoins {

    private String personage;
    private String denomination;
    private String ricvar;
    private String weight;
    private String diameter;


    private String id;
    private String mint;
    private String obvdesc;
    private String obvleg;
    private String revdesc;
    private String revleg;
    private String provenance;
    private String value;
    private String notes;

    private String sortric;




    public ZZZJcExcelCoins(String denomination, String diameter, String id,
                      String mint, String notes, String obvdesc, String obvleg, String personage, String provenance,
                      String revdesc, String revleg, String ricvar, String value, String weight, String sortric) {
        this.personage = personage;
        this.denomination = denomination;

        this.ricvar = ricvar;

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

    public String getRicvar() {
        return ricvar;    }

    public void setRicvar(String ricvar) {
        this.ricvar = ricvar;

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

    public String getId() {

        return id;
    }

    public void setId (String id) {
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

    public String getValue() {
        return value;    }

    public void setValue(String value) {
        this.value = value;

    }

    public String getNotes() {
        return notes;    }

    public void setNotes(String notes) {
        this.notes = notes;

    }

    public String getSortric() {
        return sortric;    }

    public void setSortric(String sortric) {
        this.value = sortric;

    }

    public ZZZJcExcelCoins(){

    }

}
