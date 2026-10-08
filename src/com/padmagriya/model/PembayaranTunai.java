package com.padmagriya.model;

import java.math.BigDecimal;

public class PembayaranTunai extends Pembayaran {
    private BigDecimal uangDiterima;
    private BigDecimal kembalian;

    public PembayaranTunai(BigDecimal jumlahBayar, BigDecimal uangDiterima) {
        super(jumlahBayar);
        this.uangDiterima = uangDiterima;
        this.kembalian = hitungKembalian();
    }

    public BigDecimal hitungKembalian() {
        return uangDiterima.subtract(jumlahBayar);
    }

    public BigDecimal getKembalian() {
        return kembalian;
    }

    @Override
    public boolean prosesPembayaran() {
        boolean lunas = uangDiterima.compareTo(jumlahBayar) >= 0;
        statusBayar = lunas ? "LUNAS" : "BELUM BAYAR";
        return lunas;
    }
}
