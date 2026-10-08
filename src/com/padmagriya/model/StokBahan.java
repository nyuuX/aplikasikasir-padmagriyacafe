package com.padmagriya.model;

import java.math.BigDecimal;

public class StokBahan implements Validatable {
    private int idBahan;
    private String namaBahan;
    private BigDecimal jumlahStok;
    private String satuan;
    private BigDecimal batasMinimum;

    public StokBahan(int idBahan, String namaBahan, BigDecimal jumlahStok, String satuan, BigDecimal batasMinimum) {
        this.idBahan = idBahan;
        this.namaBahan = namaBahan;
        this.jumlahStok = jumlahStok;
        this.satuan = satuan;
        this.batasMinimum = batasMinimum;
    }

    public StokBahan(String namaBahan, BigDecimal jumlahStok, String satuan, BigDecimal batasMinimum) {
        this(0, namaBahan, jumlahStok, satuan, batasMinimum);
    }

    public void tambahStok(BigDecimal jumlah) {
        jumlahStok = jumlahStok.add(jumlah);
    }

    public void kurangiStok(BigDecimal jumlah) {
        jumlahStok = jumlahStok.subtract(jumlah);
    }

    public boolean cekStokRendah() {
        return jumlahStok.compareTo(batasMinimum) < 0;
    }

    @Override
    public boolean validasiInput() {
        return namaBahan != null && !namaBahan.trim().isEmpty()
                && jumlahStok.compareTo(BigDecimal.ZERO) >= 0
                && batasMinimum.compareTo(BigDecimal.ZERO) >= 0;
    }

    public int getIdBahan() {
        return idBahan;
    }

    public String getNamaBahan() {
        return namaBahan;
    }

    public BigDecimal getJumlahStok() {
        return jumlahStok;
    }

    public String getSatuan() {
        return satuan;
    }

    public BigDecimal getBatasMinimum() {
        return batasMinimum;
    }
}
