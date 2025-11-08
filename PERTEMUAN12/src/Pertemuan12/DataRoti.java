/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pertemuan12;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "data_roti")
@NamedQueries({
    @NamedQuery(name = "DataRoti.findAll", query = "SELECT d FROM DataRoti d"),
    @NamedQuery(name = "DataRoti.findByIdRoti", query = "SELECT d FROM DataRoti d WHERE d.idRoti = :idRoti"),
    @NamedQuery(name = "DataRoti.findByNamaRoti", query = "SELECT d FROM DataRoti d WHERE d.namaRoti = :namaRoti"),
    @NamedQuery(name = "DataRoti.findByStok", query = "SELECT d FROM DataRoti d WHERE d.stok = :stok"),
    @NamedQuery(name = "DataRoti.findByHarga", query = "SELECT d FROM DataRoti d WHERE d.harga = :harga")})
public class DataRoti implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_roti")
    private String idRoti;
    @Column(name = "nama_roti")
    private String namaRoti;
    @Column(name = "stok")
    private Integer stok;
    @Column(name = "harga")
    private Integer harga;
    @JoinColumn(name = "id_pelanggan", referencedColumnName = "id_pelanggan")
    @ManyToOne
    private Pelanggan idPelanggan;

    public DataRoti() {
    }

    public DataRoti(String idRoti) {
        this.idRoti = idRoti;
    }

    public String getIdRoti() {
        return idRoti;
    }

    public void setIdRoti(String idRoti) {
        this.idRoti = idRoti;
    }

    public String getNamaRoti() {
        return namaRoti;
    }

    public void setNamaRoti(String namaRoti) {
        this.namaRoti = namaRoti;
    }

    public Integer getStok() {
        return stok;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public Pelanggan getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(Pelanggan idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRoti != null ? idRoti.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DataRoti)) {
            return false;
        }
        DataRoti other = (DataRoti) object;
        if ((this.idRoti == null && other.idRoti != null) || (this.idRoti != null && !this.idRoti.equals(other.idRoti))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pertemuan12.DataRoti[ idRoti=" + idRoti + " ]";
    }
    
}
