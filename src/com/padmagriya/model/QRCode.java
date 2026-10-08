package com.padmagriya.model;

import java.math.BigDecimal;

public class QRCode {
    private String kode;

    public QRCode(BigDecimal jumlahBayar) {
        this.kode = "QRIS-PADMAGRIYA-" + jumlahBayar.toPlainString().replace(".", "");
    }

    public String getKode() {
        return kode;
    }
}
