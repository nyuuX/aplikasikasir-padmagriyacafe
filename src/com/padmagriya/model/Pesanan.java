package com.padmagriya.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pesanan implements Validatable {
    private int idPesanan;
    private LocalDateTime waktuPesan;
    private int nomorMeja;
    private String statusPesanan;
    private List<ItemPesanan> daftarItem;

    public Pesanan(int idPesanan, int nomorMeja, LocalDateTime waktuPesan, String statusPesanan) {
        this.idPesanan = idPesanan;
        this.nomorMeja = nomorMeja;
        this.waktuPesan = waktuPesan == null ? LocalDateTime.now() : waktuPesan;
        this.statusPesanan = statusPesanan == null ? "PENDING" : statusPesanan;
        this.daftarItem = new ArrayList<ItemPesanan>();
    }

    public Pesanan(int nomorMeja) {
        this(0, nomorMeja, LocalDateTime.now(), "PENDING");
    }

    public void tambahItem(ItemPesanan item) {
        daftarItem.add(item);
    }

    public BigDecimal hitungTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPesanan item : daftarItem) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    @Override
    public boolean validasiInput() {
        return !daftarItem.isEmpty();
    }

    public int getIdPesanan() {
        return idPesanan;
    }

    public LocalDateTime getWaktuPesan() {
        return waktuPesan;
    }

    public int getNomorMeja() {
        return nomorMeja;
    }

    public String getStatus() {
        return statusPesanan;
    }

    public void setStatus(String statusPesanan) {
        this.statusPesanan = statusPesanan;
    }

    public List<ItemPesanan> getDaftarItem() {
        return daftarItem;
    }
}
