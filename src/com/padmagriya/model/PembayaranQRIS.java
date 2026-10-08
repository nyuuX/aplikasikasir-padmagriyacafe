package com.padmagriya.model;

import java.math.BigDecimal;

public class PembayaranQRIS extends Pembayaran {
    private QRCode qrCode;

    public PembayaranQRIS(BigDecimal jumlahBayar) {
        super(jumlahBayar);
    }

    public void generateQR() {
        this.qrCode = new QRCode(jumlahBayar);
    }

    public String getQrCode() {
        if (qrCode == null) {
            generateQR();
        }
        return qrCode.getKode();
    }

    @Override
    public boolean prosesPembayaran() {
        generateQR();
        statusBayar = "LUNAS";
        return true;
    }
}
