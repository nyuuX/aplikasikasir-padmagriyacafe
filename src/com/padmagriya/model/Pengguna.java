package com.padmagriya.model;

public abstract class Pengguna {
    protected int idPengguna;
    protected String nama;
    protected String username;
    protected String password;
    protected String role;

    public Pengguna(int idPengguna, String nama, String username, String password) {
        this.idPengguna = idPengguna;
        this.nama = nama;
        this.username = username;
        this.password = password;
    }

    public boolean login(String inputPassword) {
        return password != null && password.equals(inputPassword);
    }

    public void logout() {
    }

    public abstract String getRole();

    public int getIdPengguna() {
        return idPengguna;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }
}
