package com.padmagriya.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class Pembayaran implements MetodePembayaran {
    protected BigDecimal jumlahBayar;
    protected String statusBayar;
    protected LocalDateTime waktuBayar;

    public Pembayaran(BigDecimal jumlahBayar) {
        this.jumlahBayar = jumlahBayar;
        this.statusBayar = "BELUM BAYAR";
        this.waktuBayar = LocalDateTime.now();
    }

    public abstract boolean prosesPembayaran();

    public String getStatusBayar() {
        return statusBayar;
    }

    public BigDecimal getJumlahBayar() {
        return jumlahBayar;
    }

    public LocalDateTime getWaktuBayar() {
        return waktuBayar;
    }
}
