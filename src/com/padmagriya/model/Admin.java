package com.padmagriya.model;

public class Admin extends Pengguna {
    private String levelAkses;

    public Admin(int idPengguna, String nama, String username, String password, String levelAkses) {
        super(idPengguna, nama, username, password);
        this.levelAkses = levelAkses;
        this.role = "ADMIN";
    }

    public String kelolaKaryawan() {
        return "Admin mengelola data karyawan";
    }

    public String lihatLaporan() {
        return "Admin melihat laporan penjualan";
    }

    public String kelolaMenu() {
        return "Admin mengelola menu";
    }

    public String getLevelAkses() {
        return levelAkses;
    }

    @Override
    public String getRole() {
        return role;
    }
}
