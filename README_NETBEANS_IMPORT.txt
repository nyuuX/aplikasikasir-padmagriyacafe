PadmagriyaCafe - Import NetBeans
=================================

Cara import:
1. NetBeans: File > Import Project > From ZIP.
2. Pilih file PadmagriyaCafe-NetBeans.zip.
3. Setelah project terbuka, gunakan server Tomcat 9 atau GlassFish 5.

Catatan penting:
- Source servlet project ini memakai javax.servlet, jadi jangan gunakan Tomcat 10 kecuali semua import diubah ke jakarta.servlet.
- Library compile ada di folder lib:
  - javax.servlet-api-3.1.0.jar untuk compile servlet.
  - mysql-connector-j-8.0.33.jar untuk koneksi database.
- Aplikasi membuat database MySQL otomatis dengan nama padmagriya_cafe jika user MySQL root tanpa password tersedia.
- Jika MySQL berbeda, set JVM property atau environment variable:
  DB_URL, DB_USER, DB_PASS.

Login awal:
- admin / admin123
- kasir / kasir123
- owner / owner123
