@echo off
echo Menjalankan Padmagriya Cafe... (pertama kali agak lama, tunggu sampai selesai)
docker compose up -d --build
echo.
echo Selesai. Buka browser: http://localhost:8080
echo Login: admin/admin123, kasir/kasir123, owner/owner123
start http://localhost:8080
pause
