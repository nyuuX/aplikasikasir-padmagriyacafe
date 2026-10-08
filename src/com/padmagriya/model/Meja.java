package com.padmagriya.model;

public class Meja {
    private int idMeja;
    private int nomorMeja;
    private String statusMeja;

    public Meja(int idMeja, int nomorMeja, String statusMeja) {
        this.idMeja = idMeja;
        this.nomorMeja = nomorMeja;
        this.statusMeja = statusMeja;
    }

    public int getIdMeja() {
        return idMeja;
    }

    public int getNomorMeja() {
        return nomorMeja;
    }

    public String getStatusMeja() {
        return statusMeja;
    }
}
