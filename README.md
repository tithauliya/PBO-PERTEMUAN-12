# Tugas Pertemuan 12 – Penerapan GUI Database Roti Berelasi

## Deskripsi Proyek
Repository ini berisi aplikasi **GUI berbasis Java** yang terhubung dengan **database relasional** melalui **Java Persistence API (JPA)**.  
Aplikasi menampilkan data dari tabel-tabel yang memiliki relasi dan memungkinkan operasi **CRUD**.  
GUI menggunakan **Swing** dan **JTabbedPane** untuk memisahkan fungsi/modul agar pengguna dapat berpindah antar tab dengan mudah.  

---

## Fitur Utama
- **Relasi antar tabel:**  
  Contoh: Tabel `Pelanggan` memiliki relasi One → Many ke tabel `Roti`.  
  Satu pelanggan bisa membeli banyak jenis roti, tapi satu roti hanya dimiliki satu pelanggan.

- **GUI dengan tabbed interface:**  
  Pengguna dapat berpindah antar tab untuk melihat atau mengelola entitas berbeda.  
  Contoh: Tab “Pelanggan” untuk mengelola data pelanggan, Tab “Roti” untuk daftar roti.

- **Operasi CRUD:**  
  Tambah, ubah, hapus, tampilkan data.  
  Sistem memeriksa relasi terlebih dahulu sebelum mengizinkan penghapusan, menjaga integritas data.

- **Validasi relasi:**  
  Jika suatu entitas (misalnya pelanggan) masih memiliki data terkait (roti), penghapusan akan ditolak.  

---

 **GUI menggunakan JTabbedPane** supaya pengguna bisa:  
- Berpindah antar tab “Pelanggan” dan “Roti”.  
- Masing-masing tab menampilkan panel sendiri dengan **JTable**, tombol CRUD, dan form input.  
Referensi: tutorial Oracle tentang penggunaan **JTabbedPane**.

---
##  Catatan Penting
- Jika tab “Pelanggan” dipilih dan pengguna mencoba menghapus pelanggan yang masih memiliki roti, aplikasi akan menampilkan **peringatan** dan **tidak menghapus data**.  
- Pastikan tabel database membuat **foreign key** yang relevan, misal:

```sql
CREATE TABLE Pelanggan (
    id_pelanggan CHAR(4) PRIMARY KEY,
    nama_pelanggan VARCHAR(100),
    alamat VARCHAR(255),
    no_telp VARCHAR(20)
);

CREATE TABLE Roti (
    id_roti CHAR(4) PRIMARY KEY,
    nama_roti VARCHAR(100),
    harga INT,
    stock INT,
    id_pelanggan CHAR(4),
    FOREIGN KEY (id_pelanggan) REFERENCES Pelanggan(id_pelanggan)
);
```

---

## ✍️ Penulis
**Titha Auliya Khotim**  
Mahasiswa Sistem Informasi  
Semester 3  
Universitas Islam Negeri Sunan Ampel Surabaya
