package com.padmagriya.model;

import java.math.BigDecimal;

public class ItemPesanan {
    private int idItem;
    private Menu menu;
    private int jumlah;
    private BigDecimal subtotal;

    public ItemPesanan(int idItem, Menu menu, int jumlah) {
        this.idItem = idItem;
        this.menu = menu;
        this.jumlah = jumlah;
        this.subtotal = menu.getHarga().multiply(new BigDecimal(jumlah));
    }

    public ItemPesanan(Menu menu, int jumlah) {
        this(0, menu, jumlah);
    }

    public int getIdItem() {
        return idItem;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public Menu getMenu() {
        return menu;
    }

    public int getJumlah() {
        return jumlah;
    }
}
