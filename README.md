<div align="center">

# PennyWise - Aplikasi Pencatat Pengeluaran

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp" alt="PennyWise Icon" width="120"/>
</p>

Aplikasi mobile Android modern untuk pencatatan dan manajemen pengeluaran pribadi dengan antarmuka yang bersih dan intuitif menggunakan Jetpack Compose, Material Design 3, dan integrasi API cPanel.

[![Platform](https://img.shields.io/badge/platform-Android-green.svg)](https://www.android.com/) [![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24) [![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg)](https://kotlinlang.org) [![License](https://img.shields.io/badge/License-Educational-orange.svg)](LICENSE)

[Fitur](#fitur-utama) • [Teknologi](#teknologi--library) • [Arsitektur](#arsitektur) • [API Integration](#api-integration) • [Cara Menjalankan](#cara-menjalankan) • [Dokumentasi Kode](#dokumentasi-kode) • [Design Pattern](#design-patterns) • [Testing](#testing) • [Security & Best Practices](#security--best-practices) • [TODO & Roadmap](#todo--roadmap) • [Contributing](#contributing)


</div>

## Fitur Utama

### Dashboard
- **Overview Pengeluaran**: Total pengeluaran dengan statistik real-time
- **Statistik Cepat**: Jumlah transaksi dan total item
- **Recent Transactions**: 5 transaksi terakhir dengan quick access
- **Quick Actions**: Tombol cepat untuk menambah transaksi baru
- **Pull to Refresh**: Sinkronisasi data dengan server

### Manajemen Transaksi
- **List View Terorganisir**: Pengelompokan transaksi per tanggal
- **Detail Dialog**: Tampilan detail lengkap dengan opsi edit/delete
- **Real-time Updates**: Otomatis refresh setelah CRUD operation
- **Empty State**: Ilustrasi informatif saat belum ada data
- **Loading States**: Indikator loading yang smooth

### Tambah/Edit Transaksi
- **Form Dinamis**: Tambah multiple items dalam satu transaksi
- **Smart Input**: Otomatis format angka tanpa desimal .0
- **Date & Time Picker**: Material Design 3 picker
- **Inline Actions**: Tombol + untuk tambah item, - untuk hapus
- **Validation**: Form validation sebelum submit
- **Edit Mode**: Load data existing untuk update

### Laporan & Analytics
- **Period Filter**: Minggu Ini, Bulan Ini, Tahun Ini
- **Dynamic Charts**: Bar chart yang adaptive per periode
  - Minggu: 7 hari (Mon-Sun)
  - Bulan: 4 minggu
  - Tahun: 12 bulan
- **Smart Labels**: Label otomatis sesuai periode (hari/tanggal/bulan)
- **Statistics Cards**: Total pengeluaran, jumlah transaksi, rata-rata
- **Period-based Averages**: Rata-rata harian atau bulanan

### Pengaturan
- **Theme Switcher**: Light/Dark mode
- **Data Management**: Backup & restore options
- **About**: Informasi aplikasi dan versi

## Teknologi & Library

### Core Technologies
- **Language**: Kotlin 2.0.0
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 14+)
- **Build**: Gradle 8.7 with Kotlin DSL

### Jetpack & Android
```kotlin
// Compose & UI
androidx.compose.ui:ui:1.7.6
androidx.compose.material3:material3:1.3.1
androidx.navigation:navigation-compose:2.8.7
androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7

// Core
androidx.core:core-ktx:1.15.0
androidx.lifecycle:lifecycle-runtime-ktx:2.8.7
androidx.activity:activity-compose:1.9.3
```

### Networking & API
```kotlin
// Retrofit & OkHttp
com.squareup.retrofit2:retrofit:2.9.0
com.squareup.retrofit2:converter-gson:2.9.0
com.squareup.okhttp3:okhttp:4.12.0
com.squareup.okhttp3:logging-interceptor:4.12.0

// JSON Parsing
com.google.code.gson:gson:2.10.1
```

### Async & Coroutines
```kotlin
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0
org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0
```

## Arsitektur

### MVVM (Model-View-ViewModel)
```
app/
├── data/
│   ├── Transaction.kt              # Data model
│   ├── TransactionMapper.kt        # Mapper between API & Domain
│   ├── remote/
│   │   ├── ApiModels.kt           # API request/response models
│   │   ├── ApiService.kt          # Retrofit interface
│   │   └── RetrofitClient.kt      # Retrofit configuration
│   └── repository/
│       └── TransactionRepository.kt # Data repository layer
├── viewmodel/
│   └── TransactionViewModel.kt     # ViewModel with StateFlow
├── ui/
│   ├── screens/
│   │   ├── AddTransactionScreen.kt
│   │   ├── DashboardScreen.kt
│   │   ├── TransactionsScreen.kt
│   │   ├── ReportsScreen.kt
│   │   └── SettingsScreen.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── navigation/
│   ├── Screen.kt                   # Navigation routes
│   ├── NavGraph.kt                 # Navigation graph
│   └── BottomNavItem.kt            # Bottom nav config
└── utils/
    └── Utils.kt                    # Shared utilities
```

### State Management
- **UiState Sealed Class**: Idle, Loading, Success<T>, Error
- **StateFlow**: Reactive state untuk transactions dan operations
- **LaunchedEffect**: Handle state changes di Composable

## API Integration

### Backend Configuration
- **Base URL**: `https://appocalypse.my.id/api_pennywise.php`
- **Format Data**: JSON
- **Date Format**: `DD-MM-YYYY`
- **Time Format**: `HH:mm:ss`

### API Endpoints

#### GET - Fetch All Transactions
```http
GET /api_pennywise.php?action=read
Response: List<TransactionResponse>
```

#### POST - Create Transaction
```http
POST /api_pennywise.php?action=create
Body: CreateTransactionRequest
Response: { "status": "success", "message": "..." }
```

#### PUT - Update Transaction
```http
PUT /api_pennywise.php?action=update
Body: UpdateTransactionRequest
Response: { "status": "success", "message": "..." }
```

#### DELETE - Delete Transaction
```http
DELETE /api_pennywise.php?action=delete
Body: { "id": "transaction_id" }
Response: { "status": "success", "message": "..." }
```

### Error Handling
- Network errors dengan try-catch di Repository
- Result<T> wrapper untuk success/failure
- User-friendly error messages di UI
- Retry mechanism dengan refresh button

## Cara Menjalankan

### Prerequisites
- Android Studio Koala | 2024.1.1+
- JDK 17 atau lebih tinggi
- Android SDK API 24+
- Koneksi internet untuk sinkronisasi data

### Installation Steps

1. **Clone Repository**
   ```bash
   git clone https://github.com/your-username/PennyWise.git
   cd PennyWise
   ```

2. **Open in Android Studio**
   - Buka Android Studio
   - File → Open → Pilih folder PennyWise
   - Wait for Gradle sync

3. **Configure API (Optional)**
   - Jika ingin menggunakan API sendiri, update base URL di:
   ```kotlin
   // app/src/main/java/com/example/pennywise/data/remote/RetrofitClient.kt
   private const val BASE_URL = "YOUR_API_URL"
   ```

4. **Build & Run**
   - Connect device atau start emulator
   - Run → Run 'app' atau tekan Shift+F10
   - Aplikasi akan terinstall dan berjalan

### Build APK
```bash
# Debug APK
./gradlew assembleDebug

# Release APK (signed)
./gradlew assembleRelease
```

## Dokumentasi Kode

### Data Models

#### Transaction
```kotlin
data class Transaction(
    val id: String,
    val title: String,
    val items: List<ExpenseItem>,
    val date: LocalDate,
    val time: LocalTime,
    val note: String = ""
) {
    val totalAmount: Double get() = items.sumOf { it.amount }
}

data class ExpenseItem(
    val name: String,
    val amount: Double
)
```

### ViewModel Pattern

```kotlin
class TransactionViewModel : ViewModel() {
    private val _transactions = MutableStateFlow<UiState<List<Transaction>>>(UiState.Idle)
    val transactions: StateFlow<UiState<List<Transaction>>> = _transactions.asStateFlow()
    
    fun loadTransactions() {
        viewModelScope.launch {
            _transactions.value = UiState.Loading
            when (val result = repository.getAllTransactions()) {
                is Result.Success -> _transactions.value = UiState.Success(result.data)
                is Result.Error -> _transactions.value = UiState.Error(result.message)
            }
        }
    }
}
```

### Navigation

```kotlin
sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Transactions : Screen("transactions")
    object AddTransaction : Screen("add_transaction?id={transactionId}") {
        fun createRoute(transactionId: String? = null): String {
            return if (transactionId != null) {
                "add_transaction?id=$transactionId"
            } else {
                "add_transaction"
            }
        }
    }
    object Reports : Screen("reports")
    object Settings : Screen("settings")
}
```

## Design Patterns

### 1. Repository Pattern
Abstraksi layer data untuk memisahkan logic bisnis dari sumber data:
```kotlin
class TransactionRepository(private val apiService: ApiService) {
    suspend fun getAllTransactions(): Result<List<Transaction>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllTransactions()
                if (response.isSuccessful && response.body() != null) {
                    Result.Success(response.body()!!.map { it.toTransaction() })
                } else {
                    Result.Error("Failed to fetch transactions")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Unknown error")
            }
        }
    }
}
```

### 2. Sealed Class untuk State
```kotlin
sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

### 3. Mapper Pattern
Konversi antara API models dan domain models:
```kotlin
fun TransactionResponse.toTransaction(): Transaction {
    return Transaction(
        id = this.id,
        title = this.title,
        items = this.items.map { it.toExpenseItem() },
        date = LocalDate.parse(this.date, DateTimeFormatter.ofPattern("dd-MM-yyyy")),
        time = LocalTime.parse(this.time, DateTimeFormatter.ofPattern("HH:mm:ss")),
        note = this.note
    )
}
```

## Testing

### Unit Tests
```kotlin
class TransactionViewModelTest {
    @Test
    fun `load transactions success`() = runTest {
        // Given
        val mockRepository = mock<TransactionRepository>()
        val viewModel = TransactionViewModel(mockRepository)
        
        // When
        viewModel.loadTransactions()
        
        // Then
        assertTrue(viewModel.transactions.value is UiState.Success)
    }
}
```

## Security & Best Practices

- HTTPS untuk semua API calls
- ProGuard rules untuk obfuscation (release build)
- No hardcoded credentials
- Input validation di semua form
- Error handling di setiap network call
- Coroutines untuk async operations
- Memory leak prevention (ViewModel lifecycle)

## TODO & Roadmap

### Version 1.1
- [ ] Offline mode dengan Room Database
- [ ] Category management
- [ ] Budget limits & notifications
- [ ] Export data (CSV/PDF)

### Version 1.2
- [ ] Multiple accounts support
- [ ] Recurring transactions
- [ ] Search & advanced filters
- [ ] Backup to cloud (Google Drive)

### Version 2.0
- [ ] Income tracking
- [ ] Multi-currency support
- [ ] Shared expenses (split bill)
- [ ] Financial insights dengan AI

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

### Coding Standards
- Follow Kotlin coding conventions
- Use meaningful variable/function names
- Add comments for complex logic
- Write unit tests for new features
- Update documentation

```
Educational Use License
Copyright (c) 2025 Zeni Ramadan

Permission is granted to use, copy, modify, and distribute this software
for educational and non-commercial purposes only.
```

---

## Author & Contributors
<div align="center">
<table>
   <tr>
      <td align="center">
         <a href="https://github.com/zeniramadan">
               <img src="https://github.com/zeniramadan.png" width="100px;" alt="Zeni Ramadan"/><br />
               <sub><b>Zeni Ramadan</b></sub>
         </a><br />
         <sub>Team Leader & Ideation</sub><br />
         <sub>
			<a href="https://github.com/zeniramadan" target="_blank">
			<img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" alt="GitHub"/>
			</a>
         </sub>
      </td>
      <td align="center">
         <a href="https://github.com/famss-star">
               <img src="https://github.com/famss-star.png" width="100px;" alt="Rifa Maulana"/><br />
               <sub><b>Rifa Maulana</b></sub>
         </a><br />
         <sub>Bug Fixes & Improvements</sub><br />
         <sub>
			<a href="https://github.com/famss-star" target="_blank">
			<img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" alt="GitHub"/>
			</a>
         </sub>
      </td>
      <td align="center">
         <a href="https://github.com/ocktaniasalma">
               <img src="https://github.com/ocktaniasalma.png" width="100px;" alt="Salma Oktania"/><br />
               <sub><b>Salma Oktania</b></sub>
         </a><br />
         <sub>UI/UX Designer</sub><br />
         <sub>
			<a href="https://github.com/ocktaniasalma" target="_blank">
			<img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" alt="GitHub"/>
			</a>
         </sub>
      </td>
      <td align="center">
         <a href="https://github.com/Raiena">
               <img src="https://github.com/Raienna.png" width="100px;" alt="Salma Oktania"/><br />
               <sub><b>Raina Radiatussiva</b></sub>
         </a><br />
         <sub>Project Idea</sub><br/>
         <sub>
			<a href="https://github.com/Raienna" target="_blank">
			<img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" alt="GitHub"/>
			</a>
         </sub>
      </td>
   </tr>
</table>
</div>

### Roles & Contributions

<div align="center">

| Contributor | Role | Responsibilities |
|-------------|------|------------------|
| **Zeni Ramadan** | Backend Developer | Logic Database, tabel MySQL |
| **Rifa Maulana** | Bug Fixer & Developer | Debugging, code optimization, bug fixes, improvements |
| **Salma Oktania** | UI/UX Designer | Interface design, layout, theming, user experience |
| **Raina Radiatushiva** | Ideation | Konsep aplikasi |


---
</div>

## Support & Contact

### Get Help

Jika Anda memiliki pertanyaan, saran, atau menemukan bug:

1. Baca dokumentasi ini terlebih dahulu
2. Cek [Existing Issues](https://github.com/zeniramadan/PennyWise/issues)
3. [Create New Issue](https://github.com/zeniramadan/PennyWise/issues/new) jika belum ada

## Acknowledgments

- Material Design 3 guidelines
- Jetpack Compose documentation
- Android Developers community
- Open source libraries contributors

## Support

Jika Anda menemukan bug atau memiliki saran:
- Open an [issue](https://github.com/your-username/PennyWise/issues)
- Email: support@pennywise.app

---

<p align="center">
  Made with ❤️ using Jetpack Compose
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" />
  <img src="https://img.shields.io/badge/Material%20Design%203-757575?style=for-the-badge&logo=material-design&logoColor=white" />
</p>

---

<p align="center">
**PennyWise v1.0.0** - Kelola Keuangan dengan Bijak 💰
</p>
