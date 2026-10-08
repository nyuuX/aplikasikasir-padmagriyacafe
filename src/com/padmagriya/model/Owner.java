package com.padmagriya.model;

public class Owner extends Admin {
    public Owner(int idPengguna, String nama, String username, String password, String levelAkses) {
        super(idPengguna, nama, username, password, levelAkses);
        this.role = "OWNER";
    }

    public String auditKeuangan() {
        return "Owner memantau laporan keuangan dan operasional cafe";
    }
}
