package com.padmagriya.model;

public class Kasir extends Pengguna {
    private String idKasir;

    public Kasir(int idPengguna, String nama, String username, String password, String idKasir) {
        super(idPengguna, nama, username, password);
        this.idKasir = idKasir;
        this.role = "KASIR";
    }

    public String prosesPembayaran() {
        return "Kasir memproses pembayaran";
    }

    public String cetakStruk() {
        return "Kasir mencetak struk";
    }

    public String lihatPesanan() {
        return "Kasir melihat pesanan aktif";
    }

    public String getIdKasir() {
        return idKasir;
    }

    @Override
    public String getRole() {
        return role;
    }
}
