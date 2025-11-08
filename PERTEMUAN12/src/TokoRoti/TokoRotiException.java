/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TokoRoti;

/**
 *
 * @author HP
 */

// Exception khusus jika stok roti tidak cukup
class StokTidakCukupException extends Exception {
    public StokTidakCukupException(String pesan) {
        super(pesan);
    }
}

// Class utama untuk simulasi stok roti
public class TokoRotiException {
    private int stokRoti;

 
    public TokoRotiException(int stokAwal) {
        this.stokRoti = stokAwal;
    }

    // Method untuk membeli roti
    public void beliRoti(int jumlah) throws StokTidakCukupException {
        if (jumlah > stokRoti) {
        
            throw new StokTidakCukupException(
                "Stok roti tidak cukup! Kamu mencoba membeli " + jumlah +
                " tapi stok hanya tersedia " + stokRoti
            );
        }
        // Jika cukup, kurangi stok
        stokRoti -= jumlah;
        System.out.println("Pembelian roti sebanyak " + jumlah +
                           " berhasil. Sisa stok: " + stokRoti);
    }

    public int getStokRoti() {
        return stokRoti;
    }

    public static void main(String[] args) {
        TokoRotiException toko = new TokoRotiException(50); 
        try {
            toko.beliRoti(20);  // pembelian pertama (berhasil, stok jadi 30)
            toko.beliRoti(40);  // pembelian kedua (gagal, stok tidak cukup)
        } catch (StokTidakCukupException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Stok roti tersisa: " + toko.getStokRoti());
    }
}