# MyDataApp

MyDataApp adalah aplikasi Android sederhana yang dibuat untuk memenuhi tugas UTS Praktikum Pemrograman Mobile.

- Bhadriko Theo Pramudya
- IF 9
- 10123375
- 
## Fitur Aplikasi

### Login Screen
- Input username dan password
- Validasi login menggunakan data hardcode
- Tombol Login
- Tombol Cancel untuk menghapus input
- Menampilkan Toast jika data kosong atau salah
- Menyimpan status login menggunakan SharedPreferences

### Dashboard & Data Entry
- Menampilkan pesan selamat datang
- Form input data mahasiswa:
  - NIM
  - Nama Lengkap
  - Program Studi
  - Kelas
  - Alamat
  - E-Mail
- Menambahkan data ke ListView
- Logout dan kembali ke halaman login

## Teknologi yang Digunakan
- Java
- Android Studio
- SharedPreferences
- ListView
- Intent
- ArrayList

## Login Default

```text
Username : admin
Password : admin123

MyDataApp/
├── app/
├── gradle/
├── build.gradle
├── settings.gradle
└── gradlew
