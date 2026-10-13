# 📱 Buku Panduan Lengkap: Mobile Test Automation dengan Katalon Studio & Appium
**Target Aplikasi**: SauceLabs Mobile Sample App (`SauceLabs.apk`)  
**Metode Eksekusi**: Real Android Device (Samsung Galaxy via USB-C)  
**Tools**: Katalon Studio Enterprise, Node.js, Appium 2.x, UiAutomator2 Driver, Android Platform-Tools (ADB)

---

## 📑 Daftar Isi
1. [Pendahuluan & Konsep Arsitektur](#1-pendahuluan--konsep-arsitektur)
2. [Prasyarat & Matriks Kompatibilitas Versi (Sangat Kritis)](#2-prasyarat--matriks-kompatibilitas-versi-sangat-kritis)
3. [Panduan Instalasi & Konfigurasi dari Nol](#3-panduan-instalasi--konfigurasi-dari-nol)
   - [A. Setup Node.js & Appium](#a-setup-nodejs--appium)
   - [B. Setup Driver UiAutomator2 yang Kompatibel](#b-setup-driver-uiautomator2-yang-kompatibel)
   - [C. Setup Perangkat Fisik (Real Android Device)](#c-setup-perangkat-fisik-real-android-device)
   - [D. Konfigurasi Katalon Studio Preferences](#d-konfigurasi-katalon-studio-preferences)
   - [E. Konfigurasi Desired Capabilities (Anti-Splash Crash)](#e-konfigurasi-desired-capabilities-anti-splash-crash)
4. [Struktur Proyek & Kepatuhan Rubrik Pengujian](#4-struktur-proyek--kepatuhan-rubrik-pengujian)
5. [Strategi Menangkap Elemen (Mobile Object Spy) & Best Practices Locator](#5-strategi-menangkap-elemen-mobile-object-spy--best-practices-locator)
6. [Implementasi Lengkap Script Pengujian (Groovy)](#6-implementasi-lengkap-script-pengujian-groovy)
   - [TC_01_Login_Success (Skenario Positif)](#tc_01_login_success-skenario-positif)
   - [TC_02_Login_Failed (Skenario Negatif)](#tc_02_login_failed-skenario-negatif)
   - [TC_03_Add_Product_To_Cart (Skenario Positif E2E)](#tc_03_add_product_to_cart-skenario-positif-e2e)
7. [Penggabungan ke Test Suite & Analisis Hasil Laporan](#7-penggabungan-ke-test-suite--analisis-hasil-laporan)
8. [Kamus Troubleshooting & Hal-Hal Kritis yang Wajib Diperhatikan](#8-kamus-troubleshooting--hal-hal-kritis-yang-wajib-diperhatikan)

---

## 1. Pendahuluan & Konsep Arsitektur

Pengujian otomasi mobile pada proyek ini menggunakan pendekatan **Lightweight Real Device Architecture**, yaitu menggunakan perangkat fisik Android sungguhan yang dihubungkan melalui kabel data USB.

### Mengapa Real Device, Bukan Emulator Android Studio?
* **Penggunaan Sumber Daya (RAM & CPU)**: Emulator Android Studio (AVD) membutuhkan minimal RAM 8GB–12GB tersendiri dan membebani prosesor (CPU virtualisasi). Sedangkan pengujian via real device menyerahkan proses komputasi rendering aplikasi langsung ke hardware ponsel.
* **Perilaku Riil**: Menjamin kestabilan gesture, rendering layar riil, serta konsumsi memori aktual yang sesuai dengan pengguna akhir.

### Diagram Alur Komunikasi Sistem:
```
[ Katalon Studio ]
       │
       ▼ (Perintah Keyword: startApplication, setText, tap, verify)
[ Appium Server 2.x (Node.js) ] (Port Dinamis / 4723)
       │
       ▼ (Protokol W3C WebDriver)
[ UiAutomator2 Server ] (Berjalan di dalam Android)
       │
       ▼ (Aksesibilitas Android UI)
[ Aplikasi Target (SauceLabs.apk) di HP Samsung ]
```

---

## 2. Prasyarat & Matriks Kompatibilitas Versi (Sangat Kritis)

> [!CAUTION]
> **Jebakan Versi**: Masalah nomor 1 pada otomasi mobile adalah salah memasang versi driver yang tidak cocok dengan versi Appium. Jangan pernah menginstal driver tanpa memperhatikan versi induknya!

| Komponen | Versi Teruji & Stabil | Catatan Penting |
|---|---|---|
| **Sistem Operasi PC** | Windows 11 64-bit | - |
| **Node.js** | `v20.18.0` (LTS) | Wajib ditambahkan ke System Environment `PATH`. |
| **NPM** | `10.8.2` | Bawaan instalasi Node.js. |
| **Appium Server** | `2.19.0` | Dipasang secara global (`npm install -g appium`). |
| **UiAutomator2 Driver** | **`uiautomator2@3.8.0`** | **WAJIB versi 3.8.0** untuk Appium 2.x. *(Driver versi 4.x/5.x membutuhkan Appium 3 RC dan akan gagal)*. |
| **Android ADB** | Android SDK Platform-Tools | Menggunakan binary bawaan Katalon di `%USERPROFILE%\.katalon\tools\android_sdk\platform-tools\adb.exe`. |
| **Katalon Studio** | Enterprise v10.3.2 | - |
| **HP Android Target** | Samsung Galaxy SM-A135F (Android 13) | USB Debugging aktif & Authorized. |

---

## 3. Panduan Instalasi & Konfigurasi dari Nol

### A. Setup Node.js & Appium
1. Download dan pasang Node.js LTS (versi 20.x) dari [nodejs.org](https://nodejs.org/).
2. Buka PowerShell atau Command Prompt, pastikan versi terdeteksi:
   ```powershell
   node -v
   npm -v
   ```
3. Pasang Appium versi 2 secara global:
   ```powershell
   npm install -g appium
   ```
4. Verifikasi instalasi Appium:
   ```powershell
   appium -v
   # Output: 2.19.0
   ```

---

### B. Setup Driver UiAutomator2 yang Kompatibel
Driver adalah jembatan antara Appium dengan framework UIAutomator di dalam ponsel Android.
Jalankan perintah ini di PowerShell:
```powershell
appium driver install uiautomator2@3.8.0
```
Untuk memeriksa daftar driver yang terpasang dan statusnya:
```powershell
appium driver list
# Output: uiautomator2@3.8.0 [installed (npm)]
```

---

### C. Setup Perangkat Fisik (Real Android Device)
1. **Aktifkan Opsi Pengembang (Developer Options)**:
   - Buka **Pengaturan (Settings)** di HP Android ➡️ **Tentang Ponsel (About Phone)** ➡️ **Informasi Perangkat Lunak (Software Information)**.
   - Ketuk (tap) pada **Nomor Versi (Build Number)** sebanyak **7 kali berturut-turut** sampai muncul notifikasi *"Mode pengembang telah diaktifkan"*.
2. **Aktifkan USB Debugging**:
   - Kembali ke menu utama Pengaturan ➡️ Buka **Pilihan Pengembang (Developer Options)**.
   - Aktifkan toggle **Debugging USB (USB Debugging)**.
   - Aktifkan juga toggle **Tetap Terjaga (Stay Awake)** agar layar tidak mati saat terhubung ke charger/USB PC.
3. **Hubungkan HP ke Komputer via Kabel USB**:
   - Gunakan kabel data USB yang berkualitas baik (bukan hanya kabel charger).
   - Di layar HP akan muncul pop-up: *"Izinkan debugging USB dari komputer ini?"*.
   - Centang opsi **Selalu izinkan dari komputer ini (Always allow)** lalu tap **Izinkan (OK)**.
4. **Verifikasi Koneksi ADB via Terminal**:
   ```powershell
   & "$env:USERPROFILE\.katalon\tools\android_sdk\platform-tools\adb.exe" devices
   ```
   *Output yang benar:*
   ```text
   List of devices attached
   RR8TB01WHXY    device
   ```
   *(Jika statusnya `unauthorized`, cabut kabel lalu pasang lagi dan setujui pop-up izin di HP)*.

---

### D. Konfigurasi Katalon Studio Preferences
Agar Katalon Studio tahu lokasi instalasi Appium di laptop kamu:
1. Buka Katalon Studio.
2. Klik menu **Window** ➡️ **Preferences** (atau pada Mac: *Katalon Studio > Preferences*).
3. Di panel sebelah kiri, navigasikan ke: **Katalon** ➡️ **Mobile**.
4. Pada kolom **Appium Directory**, masukkan path instalasi modul Appium:
   ```text
   C:\Users\<username>\AppData\Roaming\npm\node_modules\appium
   ```
5. Klik **Apply and Close**.

---

### E. Konfigurasi Desired Capabilities (Anti-Splash Crash)

> [!WARNING]
> **Masalah SplashActivity**: Aplikasi komersial seperti Swag Labs / SauceLabs memiliki aktivitas pembuka sementara (`SplashActivity`) yang langsung beralih sangat cepat ke aktivitas login (`MainActivity`). Jika tidak dikonfigurasi, Appium akan melempar error: `com.swaglabsmobileapp.SplashActivity never started`.

**Solusi Konfigurasi di Katalon Studio**:
1. Buka menu **Project** ➡️ **Settings** ➡️ **Desired Capabilities** ➡️ **Mobile** ➡️ **Android**.
2. Tambahkan properti berikut pada tabel:
   * **Name**: `appWaitActivity`
   * **Type**: `String`
   * **Value**: `*`  *(Tanda bintang artinya Appium akan menerima aktivitas apapun yang terbuka setelah splash screen)*
3. Klik **Apply and Close**.  
*(Setting ini otomatis disimpan ke file `settings/internal/com.kms.katalon.core.mobile.android.properties`)*.

---

## 4. Struktur Proyek & Kepatuhan Rubrik Pengujian

Sesuai instruksi tugas, pengujian wajib mematuhi standar berikut:
1. **Tidak Boleh Hardcode Path APK**: Path file APK tidak boleh berupa absolute path statis (misal `C:/Users/.../SauceLabs.apk`), karena akan langsung error jika dijalankan oleh dosen/asisten lab di laptop berbeda.
   * **Solusi**: Letakkan file APK di dalam folder proyek: `androidapp/SauceLabs.apk`.
   * **Panggil secara dinamis di script**:
     ```groovy
     String appPath = RunConfiguration.getProjectDir() + '/androidapp/SauceLabs.apk'
     Mobile.startApplication(appPath, false)
     ```
2. **Tanpa Record Mobile**: Dilarang menggunakan tombol Record Mobile bawaan yang menghasilkan script kaku. Gunakan **Mobile Object Spy** untuk mengidentifikasi elemen, lalu rangkai script secara modular menggunakan Page Object Model (POM).
3. **Cakupan Pengujian**: Minimal 3 Test Cases dengan perpaduan skenario Positif & Negatif.

---

## 5. Strategi Menangkap Elemen (Mobile Object Spy) & Best Practices Locator

### Cara Menggunakan Mobile Object Spy:
1. Klik icon **Spy Mobile** (icon smartphone dengan kaca pembesar) di toolbar atas Katalon Studio ➡️ pilih **Android Devices**.
2. Pilih device kamu (`RR8TB01WHXY`) dan pilih file APK di `androidapp/SauceLabs.apk`.
3. Klik **Start**. Aplikasi akan terbuka di HP dan layar HP akan dicerminkan (*mirrored*) ke jendela Katalon.
4. Klik elemen yang ingin diuji di tampilan cermin, lalu klik **Add to Object Repository**.
5. **PENTING**: Setelah selesai menangkap elemen, selalu klik **Save** lalu **TUTUP JENDELA MOBILE OBJECT SPY** agar sesi Appium tidak menggantung di latar belakang.

### Hirarki Prioritas Locator di Android:
1. **Accessibility ID (`content-desc`)** - *Prioritas #1 (Paling Stabil)*:  
   Pada aplikasi SauceLabs, developer menyediakan atribut `test-` khusus untuk otomasi:
   * Username input: `test-Username`
   * Password input: `test-Password`
   * Tombol Add to Cart: `test-ADD TO CART`
   * Icon Cart: `test-Cart`
   * Error container: `test-Error message`
2. **XPath berbasis Atribut Teks / Class** - *Prioritas #2*:
   * Contoh: `//android.widget.TextView[@text='PRODUCTS']`
   * Contoh: `//*[@class='android.widget.TextView' and @text='LOGIN']`
3. **XPath Hirarki Absolut** - *DILARANG DIGUNAKAN*:
   * Hindari: `//hierarchy/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/...`  
   *(Sangat rapuh! Begitu layout aplikasi sedikit berubah, test case langsung gagal)*.

---

## 6. Implementasi Lengkap Script Pengujian (Groovy)

### TC_01_Login_Success (Skenario Positif)
* **File**: `Scripts/TC_01_Login_Success/Script1791859498791.groovy`
```groovy
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import internal.GlobalVariable as GlobalVariable

// 1. Dynamic Path untuk APK (Sesuai Rubrik: Bebas Hardcode)
String appPath = RunConfiguration.getProjectDir() + '/androidapp/SauceLabs.apk'

KeywordUtil.logInfo("=== START TC_01_LOGIN_SUCCESS ===")
KeywordUtil.logInfo("Membuka aplikasi dari path: " + appPath)

// 2. Buka Aplikasi di HP Samsung (false = jangan uninstall jika sudah terpasang)
Mobile.startApplication(appPath, false)

// 3. Masukkan Kredensial Valid dari GlobalVariable
Mobile.waitForElementPresent(findTestObject('page_Login/input_Username'), 20)
Mobile.setText(findTestObject('page_Login/input_Username'), GlobalVariable.valid_username, 10)
Mobile.setText(findTestObject('page_Login/input_Password'), GlobalVariable.valid_password, 10)

// 4. Tap Tombol LOGIN
Mobile.tap(findTestObject('page_Login/btn_Login'), 10)

// 5. Assertion: Pastikan Header Katalog 'PRODUCTS' Muncul
Mobile.waitForElementPresent(findTestObject('page_Products/text_Header_Products'), 20)
Mobile.verifyElementExist(findTestObject('page_Products/text_Header_Products'), 10)

KeywordUtil.logInfo(">> Assertion Berhasil: Katalog produk berhasil ditampilkan.")

// 6. Tutup Aplikasi
Mobile.closeApplication()
KeywordUtil.logInfo("=== TC_01_LOGIN_SUCCESS PASSED ===")
```

---

### TC_02_Login_Failed (Skenario Negatif)
* **File**: `Scripts/TC_02_Login_Failed/Script1791862650000.groovy`
```groovy
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import internal.GlobalVariable as GlobalVariable

// 1. Dynamic Path untuk APK
String appPath = RunConfiguration.getProjectDir() + '/androidapp/SauceLabs.apk'

KeywordUtil.logInfo("=== START TC_02_LOGIN_FAILED ===")

// 2. Buka Aplikasi
Mobile.startApplication(appPath, false)

// 3. Masukkan User Terkunci (invalid_username = 'locked_out_user')
Mobile.waitForElementPresent(findTestObject('page_Login/input_Username'), 20)
Mobile.setText(findTestObject('page_Login/input_Username'), GlobalVariable.invalid_username, 10)
Mobile.setText(findTestObject('page_Login/input_Password'), GlobalVariable.valid_password, 10)

// 4. Tap Tombol LOGIN
Mobile.tap(findTestObject('page_Login/btn_Login'), 10)

// 5. Assertion: Pastikan Container Pesan Error Muncul
Mobile.waitForElementPresent(findTestObject('page_Login/text_Error_Message'), 15)
Mobile.verifyElementExist(findTestObject('page_Login/text_Error_Message'), 10)

KeywordUtil.logInfo(">> Assertion Berhasil: Pesan error login tertangkap.")

// 6. Tutup Aplikasi
Mobile.closeApplication()
KeywordUtil.logInfo("=== TC_02_LOGIN_FAILED PASSED ===")
```

---

### TC_03_Add_Product_To_Cart (Skenario Positif E2E)
* **File**: `Scripts/TC_03_Add_Product_To_Cart/Script1791862700000.groovy`
```groovy
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import internal.GlobalVariable as GlobalVariable

// 1. Dynamic Path untuk APK
String appPath = RunConfiguration.getProjectDir() + '/androidapp/SauceLabs.apk'

KeywordUtil.logInfo("=== START TC_03_ADD_PRODUCT_TO_CART ===")

// 2. Buka Aplikasi
Mobile.startApplication(appPath, false)

// 3. Login dengan Akun Valid
Mobile.waitForElementPresent(findTestObject('page_Login/input_Username'), 20)
Mobile.setText(findTestObject('page_Login/input_Username'), GlobalVariable.valid_username, 10)
Mobile.setText(findTestObject('page_Login/input_Password'), GlobalVariable.valid_password, 10)
Mobile.tap(findTestObject('page_Login/btn_Login'), 10)

// 4. Verifikasi Berhasil Masuk ke Katalog
Mobile.waitForElementPresent(findTestObject('page_Products/text_Header_Products'), 20)

// 5. Tap 'ADD TO CART' pada Produk Pertama
Mobile.waitForElementPresent(findTestObject('page_Products/btn_Add_To_Cart'), 15)
Mobile.tap(findTestObject('page_Products/btn_Add_To_Cart'), 10)

// 6. Tap Icon Keranjang di Kanan Atas
Mobile.waitForElementPresent(findTestObject('page_Products/btn_Cart'), 10)
Mobile.tap(findTestObject('page_Products/btn_Cart'), 10)

// 7. Assertion: Verifikasi Halaman Keranjang Terbuka
Mobile.waitForElementPresent(findTestObject('page_Products/text_Header_Cart'), 15)
Mobile.verifyElementExist(findTestObject('page_Products/text_Header_Cart'), 10)

KeywordUtil.logInfo(">> Assertion Berhasil: Produk berhasil dimasukkan ke keranjang belanja.")

// 8. Tutup Aplikasi
Mobile.closeApplication()
KeywordUtil.logInfo("=== TC_03_ADD_PRODUCT_TO_CART PASSED ===")
```

---

## 7. Penggabungan ke Test Suite & Analisis Hasil Laporan

### Mengapa Harus Dijalankan via Test Suite?
Menjalankan Test Case satu per satu hanya menguji fungsionalitas sementara di layar. Rubrik Final Project mewajibkan hasil eksekusi resmi dalam bentuk file report di folder `Reports/`. Test Suite bertugas membungkus semua Test Case dan men-generate laporan ini secara otomatis.

### Cara Menjalankan Test Suite:
1. Di Tests Explorer, buka **`Test Suites/TS_Mobile_Regression`**.
2. Klik tombol panah drop-down di samping icon **Run** (panah hijau) ➡️ pilih **Android**.
3. Pilih perangkat Samsung kamu (`RR8TB01WHXY`) ➡️ klik **OK**.
4. Tunggu seluruh 3 Test Case dieksekusi secara otomatis satu per satu.

### Lokasi File Laporan Eksekusi:
Setelah eksekusi selesai dengan status **PASSED**, laporan otomatis tersimpan di folder:
```text
C:\Users\djeha\Katalon Studio\FinalProject_Mobile_AfterOffice\Reports\<timestamp>\TS_Mobile_Regression\
├── <timestamp>.html        # Laporan visual lengkap (bisa dibuka di browser)
├── <timestamp>.csv         # Laporan tabular metrik step & durasi
├── JUnit_Report.xml        # Laporan format standar CI/CD
└── appium.log              # Log transaksi server Appium
```

---

## 8. Kamus Troubleshooting & Hal-Hal Kritis yang Wajib Diperhatikan

Bagian ini merangkum seluruh kendala nyata yang sempat kita temukan dan solusi tepatnya:

### ⚠️ Kendala 1: `Fail to start Appium server in 30 seconds`
* **Penyebab**: Ada proses `node.exe` (Appium) dari sesi sebelumnya yang masih menyala di latar belakang dan mengunci port/file log.
* **Solusi Cepat**:
  1. Pastikan jendela **Mobile Object Spy** sudah ditutup.
  2. Buka PowerShell dan jalankan perintah *kill* proses Node:
     ```powershell
     Stop-Process -Name "node" -Force
     ```

---

### ⚠️ Kendala 2: HP Tidak Merespon / Status Langsung `<Done>` (0/1 Runs)
* **Penyebab**: Script memiliki garis bawah merah (*squiggly error*) di editor (misal: `GlobalVariable` belum dikenali), sehingga sistem Eclipse Katalon menolak menjalankan script.
* **Solusi**:
  1. Tutup tab `default` profile jika sedang terbuka di editor.
  2. Klik menu **Project** ➡️ **Refresh** (atau tekan **F5**).
  3. Klik menu **Project** ➡️ **Clean...** lalu klik **Clean**.

---

### ⚠️ Kendala 3: `MissingPropertyException: No such property for class: internal.GlobalVariable`
* **Penyebab**: File `Profiles/default.glbl` baru ditambahkan variabel, namun file class internal di folder `Libs/` belum di-generate ulang oleh Katalon.
* **Solusi**: Tutup dan buka kembali (*reopen*) project Katalon Studio, atau simpan ulang profile di GUI.

---

### ⚠️ Kendala 4: HP Samsung Tiba-Tiba Diam / Timeout saat Mengetik
* **Penyebab**: Layar HP masuk ke mode **Dozing (Screen Off/Terkunci)**. Saat layar mati, UI Automator tidak bisa berinteraksi dengan elemen layar.
* **Solusi**:
  1. Pastikan layar HP selalu dalam keadaan **Unlocked** (tidak berada di pola/PIN pengunci).
  2. Masuk ke **Developer Options** di HP ➡️ aktifkan opsi **Tetap Terjaga (Stay Awake)** agar layar menyala terus selama kabel USB tertancap.

---

### ⚠️ Kendala 5: `Driver uiautomator2 is not compatible with Appium version 2.x`
* **Penyebab**: Menjalankan `appium driver install uiautomator2` tanpa menyertakan tag versi, sehingga NPM mengunduh versi 4.x/5.x yang khusus untuk Appium 3.0-RC.
* **Solusi**:
  ```powershell
  appium driver uninstall uiautomator2
  appium driver install uiautomator2@3.8.0
  ```

---

*Dokumen ini dibuat sebagai referensi lengkap pengujian Mobile Automation Final Project AfterOffice Bootcamp.*  
*Status Eksekusi Terakhir: **TS_Mobile_Regression PASSED (3/3 Test Cases Green)***.
