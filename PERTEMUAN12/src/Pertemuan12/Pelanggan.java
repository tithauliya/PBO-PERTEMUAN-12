/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pertemuan12;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author HP
 */
@Entity
@Table(name = "pelanggan")
@NamedQueries({
    @NamedQuery(name = "Pelanggan.findAll", query = "SELECT p FROM Pelanggan p"),
    @NamedQuery(name = "Pelanggan.findByIdPelanggan", query = "SELECT p FROM Pelanggan p WHERE p.idPelanggan = :idPelanggan"),
    @NamedQuery(name = "Pelanggan.findByNamaPelanggan", query = "SELECT p FROM Pelanggan p WHERE p.namaPelanggan = :namaPelanggan"),
    @NamedQuery(name = "Pelanggan.findByAlamat", query = "SELECT p FROM Pelanggan p WHERE p.alamat = :alamat"),
    @NamedQuery(name = "Pelanggan.findByNoTelepon", query = "SELECT p FROM Pelanggan p WHERE p.noTelepon = :noTelepon")})
public class Pelanggan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_pelanggan")
    private String idPelanggan;
    @Column(name = "nama_pelanggan")
    private String namaPelanggan;
    @Column(name = "alamat")
    private String alamat;
    @Column(name = "no_telepon")
    private String noTelepon;
    @OneToMany(mappedBy = "idPelanggan")
    private Collection<DataRoti> dataRotiCollection;

    public Pelanggan() {
    }

    public Pelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public Collection<DataRoti> getDataRotiCollection() {
        return dataRotiCollection;
    }

    public void setDataRotiCollection(Collection<DataRoti> dataRotiCollection) {
        this.dataRotiCollection = dataRotiCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPelanggan != null ? idPelanggan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pelanggan)) {
            return false;
        }
        Pelanggan other = (Pelanggan) object;
        if ((this.idPelanggan == null && other.idPelanggan != null) || (this.idPelanggan != null && !this.idPelanggan.equals(other.idPelanggan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return idPelanggan;
    }
    
}
