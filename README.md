# 📱 SauceLabs Mobile Automation - Katalon Studio & Appium

![Katalon Studio](https://img.shields.io/badge/Katalon%20Studio-v10.3.2-blue?style=flat&logo=katalon)
![Appium](https://img.shields.io/badge/Appium-v2.19.0-purple?style=flat&logo=appium)
![UiAutomator2](https://img.shields.io/badge/Driver-UiAutomator2%20v3.8.0-red?style=flat)
![Platform](https://img.shields.io/badge/Platform-Android%2013%20(Real%20Device)-green?style=flat&logo=android)
![Status](https://img.shields.io/badge/Build-PASSED-success?style=flat)

Proyek ini adalah implementasi **Mobile Test Automation** pada aplikasi Android **SauceLabs Sample App (`SauceLabs.apk`)** menggunakan **Katalon Studio Enterprise** dan **Appium 2.x**. Pengujian dijalankan langsung pada perangkat fisik (**Samsung Galaxy SM-A135F via USB-C**) tanpa menggunakan emulator Android Studio yang berat. Proyek ini dibuat untuk memenuhi tugas **Final Project Bootcamp AfterOffice**.

---

## 🎯 Target Aplikasi & Lingkungan Pengujian
* **Aplikasi**: SauceLabs Sample Mobile App (`com.swaglabsmobileapp`)
* **File APK**: Disimpan di dalam folder proyek (`androidapp/SauceLabs.apk` v2.7.1)
* **Perangkat Fisik**: Samsung Galaxy SM-A135F (Android 13)
* **Appium Server**: Versi `2.19.0`
* **Driver Otomasi**: `uiautomator2@3.8.0`

---

## 📋 Cakupan Pengujian (Test Scope & Scenarios)

Proyek ini mencakup minimal 3 Test Cases yang dirancang modular menggunakan **Page Object Model (POM)** dan ditangkap menggunakan **Mobile Object Spy** (tanpa fitur *Record Mobile* kaku):

| Test Case ID | Nama Test Case | Tipe | Skenario & Validasi |
|---|---|---|---|
| **TC_01** | `TC_01_Login_Success` | **Positif** | Membuka APK via dynamic path ➡️ Login dengan kredensial valid (`standard_user` & `secret_sauce`) ➡️ Memvalidasi header katalog `PRODUCTS` berhasil muncul ➡️ Menutup aplikasi. |
| **TC_02** | `TC_02_Login_Failed` | **Negatif** | Membuka APK via dynamic path ➡️ Login dengan user terkunci (`locked_out_user`) ➡️ Memvalidasi container pesan error muncul (`test-Error message` / `Sorry, this user has been locked out.`) ➡️ Menutup aplikasi. |
| **TC_03** | `TC_03_Add_Product_To_Cart` | **Positif (E2E)** | Membuka APK ➡️ Login valid ➡️ Menekan tombol `ADD TO CART` pada produk pertama ➡️ Menekan icon `Cart` di kanan atas ➡️ Memvalidasi halaman keranjang belanja terbuka (`YOUR CART`) ➡️ Menutup aplikasi. |

---

## 🚀 Fitur Utama & Kepatuhan Rubrik

1. **Bebas Hardcode (Dynamic Project-Relative Path)**:
   - Path file APK tidak di-hardcode ke path absolut komputer tertentu, melainkan dipanggil secara dinamis:
     ```groovy
     String appPath = RunConfiguration.getProjectDir() + '/androidapp/SauceLabs.apk'
     Mobile.startApplication(appPath, false)
     ```
2. **Tanpa Record Mobile**:
   - Seluruh elemen ditangkap menggunakan *Mobile Object Spy*, dengan prioritas atribut **Accessibility ID (`content-desc`)** yang stabil terhadap perubahan layout.
3. **Konfigurasi Anti-Crash SplashActivity**:
   - Menambahkan Desired Capabilities `appWaitActivity = *` pada setting project untuk mencegah race condition `SplashActivity never started`.
4. **Eksekusi Test Suite Terverifikasi**:
   - Ketiga Test Case digabungkan dalam `TS_Mobile_Regression` dan berhasil lulus 100% dengan laporan pengujian resmi.
5. **Buku Panduan Lengkap**:
   - Dilengkapi file dokumentasi mendalam: [PANDUAN_MOBILE_AUTOMATION.md](PANDUAN_MOBILE_AUTOMATION.md) dan versi web [PANDUAN_MOBILE_AUTOMATION.html](PANDUAN_MOBILE_AUTOMATION.html).

---

## 📁 Struktur Folder Proyek

```text
FinalProject_Mobile_AfterOffice/
├── androidapp/
│   └── SauceLabs.apk             # File installer APK target pengujian
├── Object Repository/            # Page Object Model (POM)
│   ├── page_Login/               # Elemen input username, password, btn login, error text
│   └── page_Products/            # Elemen header products, btn add to cart, icon cart, header cart
├── Profiles/
│   └── default.glbl              # Global Variables (valid/invalid username & password)
├── Reports/                      # Laporan hasil eksekusi pengujian (HTML, CSV, JUnit)
├── Scripts/                      # Source code script Groovy pengujian
├── Test Cases/                   # Definisi test cases
├── Test Suites/
│   └── TS_Mobile_Regression.ts   # Suite gabungan regresi mobile
├── PANDUAN_MOBILE_AUTOMATION.md  # Buku panduan lengkap instalasi, setup & troubleshooting
└── PANDUAN_MOBILE_AUTOMATION.html# Buku panduan versi HTML (siap cetak PDF)
```

---

## 🛠️ Prasyarat & Cara Menjalankan Pengujian

### Prasyarat Environment:
* Node.js v20 LTS & Appium v2.19.0 dipasang global: `npm install -g appium`
* Driver UiAutomator2 versi kompatibel: `appium driver install uiautomator2@3.8.0`
* HP Android dengan **USB Debugging** aktif, terhubung ke laptop via kabel data USB.
* Setting Katalon: `Window ➔ Preferences ➔ Katalon ➔ Mobile ➔ Appium Directory` mengarah ke folder npm Appium.

### Langkah Eksekusi:
1. **Clone Repository**:
   ```bash
   git clone https://github.com/Jehanwar/katalon-saucelabs-mobile.git
   ```
2. **Buka di Katalon Studio**:
   - Buka Katalon Studio ➡️ **File** ➡️ **Open Project** ➡️ Pilih file `FinalProject_Mobile_AfterOffice.prj`.
3. **Jalankan Test Suite**:
   - Buka folder `Test Suites` ➡️ Klik ganda `TS_Mobile_Regression`.
   - Klik tombol **Run** ➡️ Pilih **Android** ➡️ Pilih device Samsung kamu.
4. **Lihat Laporan**:
   - Buka folder `Reports/` untuk melihat laporan interaktif `.html` dan data tabular `.csv`.

---

## 📊 Hasil Eksekusi & Laporan Pengujian
Laporan resmi tersimpan di folder `Reports/` proyek ini:
* **HTML Report**: [Reports/TS_Mobile_Regression/](Reports/)
* **Status**: **PASSED (100% Green)**
  * Total Test Cases: 3
  * Passed: 3
  * Failed / Error: 0
  * Total Execution Time: ~1 menit 39 detik

---
*Author: Jehan Khairul Anwar (Final Project AfterOffice Bootcamp)*
