# PennyWise - Personal Expense Tracker

Aplikasi mobile Android modern untuk pelacakan dan pencatatan pengeluaran pribadi dengan antarmuka yang bersih dan intuitif menggunakan Jetpack Compose dan Material Design 3.

## 🎨 Fitur Utama

### 1. **Dashboard/Home**
- Tampilan ringkasan saldo saat ini
- Total pemasukan dan pengeluaran bulan ini
- Card ringkasan pengeluaran per kategori
- Grafik pie chart untuk distribusi pengeluaran
- Daftar transaksi terbaru

### 2. **Daftar Transaksi**
- Daftar lengkap semua transaksi dengan LazyColumn
- Filter transaksi (Semua, Pemasukan, Pengeluaran)
- Pengelompokan transaksi berdasarkan tanggal
- Floating Action Button (FAB) untuk menambah transaksi baru
- Tampilan detail setiap transaksi dengan ikon kategori

### 3. **Tambah Transaksi**
- Form input yang user-friendly
- Pemilihan jenis transaksi (Pemasukan/Pengeluaran)
- Input jumlah uang dengan validasi
- Pemilihan kategori dengan ikon visual
- Date picker untuk memilih tanggal transaksi
- Field catatan opsional
- Tombol simpan dengan validasi

### 4. **Laporan/Analytics**
- Laporan bulanan, mingguan, dan tahunan
- Grafik bar chart untuk pengeluaran 7 hari terakhir
- Statistik pengeluaran per kategori dengan progress bar
- Total pemasukan dan pengeluaran
- Rata-rata pengeluaran harian
- Kategori pengeluaran terbesar

### 5. **Pengaturan**
- Toggle dark mode
- Pengaturan mata uang
- Pengaturan bahasa
- Notifikasi dan pengingat
- Cadangkan dan pulihkan data
- Ekspor data ke CSV/Excel
- Informasi aplikasi dan kebijakan

## 🎨 Desain UI/UX

- **Material Design 3 (Material You)** dengan tema dinamis
- **Dark Mode** support penuh
- **Warna Tema**: Hijau (primary) dan Biru (secondary) untuk nuansa keuangan yang tenang
- **Bottom Navigation Bar** dengan 4 tab utama
- **Responsive** untuk berbagai ukuran layar
- **Modern** dengan rounded corners dan shadows
- **Intuitive** dengan ikon yang jelas dan navigasi yang mudah

## 🏗️ Teknologi yang Digunakan

### Core
- **Kotlin** - Bahasa pemrograman
- **Jetpack Compose** - UI toolkit modern
- **Material Design 3** - Design system

### Architecture & Navigation
- **Navigation Compose** - Navigasi antar screen
- **MVVM Pattern** (siap untuk implementasi dengan ViewModel)

### UI Components
- **Material Icons Extended** - Set ikon lengkap
- **Compose Material3** - Komponen UI Material 3
- **Vico Charts** - Library untuk grafik dan chart

### Dependencies
```gradle
// Compose BOM
implementation(platform("androidx.compose:compose-bom:2025.01.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.ui:ui-tooling-preview")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.7")

// Material Icons Extended
implementation("androidx.compose.material:material-icons-extended:1.7.6")

// Charts (Vico)
implementation("com.patrykandpatrick.vico:compose:2.0.0-alpha.28")
implementation("com.patrykandpatrick.vico:compose-m3:2.0.0-alpha.28")
implementation("com.patrykandpatrick.vico:core:2.0.0-alpha.28")

// Lifecycle
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
implementation("androidx.activity:activity-compose:1.12.2")
```

## 📁 Struktur Project

```
app/src/main/java/com/example/pennywise/
├── data/
│   ├── Category.kt           # Enum kategori transaksi dengan ikon
│   ├── Transaction.kt        # Data class untuk transaksi
│   └── TransactionType.kt    # Enum tipe transaksi (Income/Expense)
│
├── navigation/
│   ├── Screen.kt            # Sealed class untuk routes
│   ├── BottomNavItem.kt     # Data class untuk bottom nav items
│   └── NavGraph.kt          # Navigation graph
│
├── ui/
│   ├── screens/
│   │   ├── DashboardScreen.kt        # Halaman dashboard
│   │   ├── TransactionsScreen.kt    # Halaman daftar transaksi
│   │   ├── AddTransactionScreen.kt  # Halaman tambah transaksi
│   │   ├── ReportsScreen.kt         # Halaman laporan
│   │   └── SettingsScreen.kt        # Halaman pengaturan
│   │
│   └── theme/
│       ├── Color.kt         # Definisi warna tema
│       ├── Type.kt          # Definisi typography
│       └── Theme.kt         # Setup tema Material 3
│
└── MainActivity.kt          # Entry point aplikasi
```

## 🚀 Cara Menjalankan

1. **Clone atau buka project di Android Studio**
   ```bash
   cd c:\Users\szal\AndroidStudioProjects\PennyWise
   ```

2. **Sync Gradle**
   - Android Studio akan otomatis sync dependencies
   - Atau jalankan: `./gradlew build`

3. **Run aplikasi**
   - Pilih emulator atau device
   - Klik Run atau tekan `Shift + F10`

## 📱 Minimum Requirements

- **Min SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 36
- **Compile SDK**: 36

## 🎯 Kategori Transaksi

### Pengeluaran (Expense)
- 🍽️ Makanan
- 🚗 Transportasi
- 🎬 Hiburan
- 🛒 Belanja
- 🏥 Kesehatan
- 📚 Pendidikan
- 🧾 Tagihan
- ➕ Lainnya

### Pemasukan (Income)
- 💼 Gaji
- 💻 Freelance
- 📈 Investasi
- 🎁 Hadiah
- ➕ Lainnya

## 🔜 Fitur yang Akan Datang

- [ ] Integrasi Room Database untuk penyimpanan lokal
- [ ] ViewModel dan State Management
- [ ] Export data ke CSV/PDF
- [ ] Backup ke cloud (Google Drive)
- [ ] Reminder/notification untuk pencatatan
- [ ] Multi-currency support
- [ ] Budget planning dan tracking
- [ ] Recurring transactions
- [ ] Search dan filter advanced
- [ ] Biometric authentication

## 📝 Catatan Pengembangan

Aplikasi ini saat ini fokus pada **UI/UX implementation**. Data yang ditampilkan adalah sample data. Untuk implementasi production, perlu ditambahkan:

1. **ViewModel** untuk state management
2. **Repository pattern** untuk data layer
3. **Room Database** untuk local storage
4. **DataStore** untuk preferences
5. **Coroutines** untuk async operations
6. **Dependency Injection** (Hilt/Koin)

## 📄 Lisensi

Project ini dibuat untuk keperluan pembelajaran dan portfolio.

## 👨‍💻 Developer

Dibuat dengan ❤️ menggunakan Jetpack Compose dan Material Design 3

---

**PennyWise v1.0.0** - Kelola Keuangan dengan Bijak 💰
