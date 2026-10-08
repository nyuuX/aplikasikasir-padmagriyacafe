package com.padmagriya.model;

import java.math.BigDecimal;

public class Menu {
    private int idMenu;
    private String namaMenu;
    private String kategori;
    private BigDecimal harga;
    private boolean tersedia;
    private String fotoPath;
    private int stok;

    public Menu(int idMenu, String namaMenu, String kategori, BigDecimal harga, boolean tersedia) {
        this(idMenu, namaMenu, kategori, harga, tersedia, null, 0);
    }

    public Menu(int idMenu, String namaMenu, String kategori, BigDecimal harga, boolean tersedia, String fotoPath) {
        this(idMenu, namaMenu, kategori, harga, tersedia, fotoPath, 0);
    }

    public Menu(int idMenu, String namaMenu, String kategori, BigDecimal harga, boolean tersedia, String fotoPath, int stok) {
        this.idMenu = idMenu;
        this.namaMenu = namaMenu;
        this.kategori = kategori;
        this.harga = harga;
        this.tersedia = tersedia;
        this.fotoPath = fotoPath;
        this.stok = stok;
    }

    public Menu(String namaMenu, String kategori, BigDecimal harga, boolean tersedia) {
        this(0, namaMenu, kategori, harga, tersedia);
    }

    public Menu(String namaMenu, String kategori, BigDecimal harga, boolean tersedia, String fotoPath) {
        this(0, namaMenu, kategori, harga, tersedia, fotoPath);
    }

    public int getIdMenu() {
        return idMenu;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public boolean isTersedia() {
        return tersedia;
    }

    public void setTersedia(boolean tersedia) {
        this.tersedia = tersedia;
    }

    public String getFotoPath() {
        return fotoPath == null || fotoPath.trim().isEmpty() ? "images/menu/menu-01.jpg" : fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }
}
