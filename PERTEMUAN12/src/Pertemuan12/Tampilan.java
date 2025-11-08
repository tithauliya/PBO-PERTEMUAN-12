/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Pertemuan12;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author HP
 */
public class Tampilan extends javax.swing.JFrame {

    private EntityManagerFactory emf;
    private EntityManager em;

    String idrotiLama, namarotiLama, stokLama, hargaLama, idpelangganLama;
    String namaLama, alamatLama, nohpLama;

    public void connect() {
        try {
            emf = Persistence.createEntityManagerFactory("PERTEMUAN12PU");
            em = emf.createEntityManager();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Koneksi gagal: " + e.getMessage());
        }
    }

    public Tampilan() {
        initComponents();
        connect();
        showTable();
        showTablePelanggan();

        tabeldataroti.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = tabeldataroti.getSelectedRow();

                idrotiLama = tabeldataroti.getValueAt(row, 0).toString();
                namarotiLama = tabeldataroti.getValueAt(row, 1).toString();
                hargaLama = tabeldataroti.getValueAt(row, 2).toString();
                stokLama = tabeldataroti.getValueAt(row, 3).toString();
                idpelangganLama = tabeldataroti.getValueAt(row, 4).toString();

            }
        });
    }

    public void showTable() {
        try {
            em.clear();

            // Ambil semua data roti
            List<DataRoti> hasil = em.createNamedQuery("DataRoti.findAll", DataRoti.class)
                    .getResultList();

            // Render agar isi tabel rata tengah
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            // Buat model tabel baru sesuai kolom di database
            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"ID Roti", "Nama Roti", "Stok", "Harga", "ID Pelanggan"}, 0
            );

            // Masukkan data dari database ke tabel GUI
            for (DataRoti p : hasil) {
                model.addRow(new Object[]{
                    p.getIdRoti(),
                    p.getNamaRoti(),
                    p.getStok(),
                    p.getHarga(),
                    p.getIdPelanggan()
                });
            }

            tabeldataroti.setModel(model);

            // Rata tengah semua kolom
            for (int i = 0; i < tabeldataroti.getColumnCount(); i++) {
                tabeldataroti.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tampil data: " + e.getMessage());
        }

    }

    private void imporCsvKeDatabaseDataRoti() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih File CSV");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fileName = file.getName();

            // Pastikan file CSV
            if (!fileName.toLowerCase().endsWith(".csv")) {
                JOptionPane.showMessageDialog(this,
                        "File yang dipilih bukan file CSV!\nSilakan pilih file dengan ekstensi .csv",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                String line;
                br.readLine(); // lewati baris header

                em.getTransaction().begin();

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(";");

                    // Format CSV: id_roti;nama_roti;stok;harga;id_pelanggan
                    if (data.length == 5) {
                        DataRoti roti = new DataRoti();
                        roti.setIdRoti(data[0].trim());
                        roti.setNamaRoti(data[1].trim());
                        roti.setStok(Integer.parseInt(data[2].trim()));
                        roti.setHarga(Integer.parseInt(data[3].trim()));

                        String idPelanggan = data[4].trim();
                        Pelanggan pelanggan = em.find(Pelanggan.class, idPelanggan);

                        if (pelanggan != null) {
                            roti.setIdPelanggan(pelanggan);
                            em.persist(roti);
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Pelanggan dengan ID '" + idPelanggan + "' tidak ditemukan.\nBaris ini dilewati.",
                                    "Kesalahan Data Pelanggan",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Format CSV tidak sesuai di baris: " + line,
                                "Kesalahan Format CSV",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }

                em.getTransaction().commit();
                JOptionPane.showMessageDialog(this, "Data roti berhasil diimpor dari CSV!");
                showTable();          // refresh tabel roti
                showTablePelanggan(); // refresh tabel pelanggan

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal impor: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void showTablePelanggan() {
        try {

            // Ambil semua data pelanggan dari database
            List<Pelanggan> hasil = em.createNamedQuery("Pelanggan.findAll", Pelanggan.class)
                    .getResultList();

            // Biar rata tengah
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            // Buat model tabel baru
            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"ID Pelanggan", "Nama Pelanggan", "Alamat", "No Telepon"}, 0
            );

            // Masukkan data ke tabel GUI
            for (Pelanggan p : hasil) {
                model.addRow(new Object[]{
                    p.getIdPelanggan(),
                    p.getNamaPelanggan(),
                    p.getAlamat(),
                    p.getNoTelepon(),});
            }

            tabelpelanggan.setModel(model);

            // Rata tengah kolom
            for (int i = 0; i < tabelpelanggan.getColumnCount(); i++) {
                tabelpelanggan.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tampil data: " + e.getMessage());
        }
    }

    private void imporCsvKeDatabasePelanggan() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih File CSV");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fileName = file.getName();

            // Pastikan file berformat .csv
            if (!fileName.toLowerCase().endsWith(".csv")) {
                JOptionPane.showMessageDialog(this,
                        "File yang dipilih bukan file CSV!\nSilakan pilih file dengan ekstensi .csv",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                String line;
                br.readLine();

                em.getTransaction().begin();

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(";");

                    if (data.length == 4) {
                        Pelanggan p = new Pelanggan();
                        p.setIdPelanggan(data[0].trim());
                        p.setNamaPelanggan(data[1].trim());
                        p.setAlamat(data[2].trim());
                        p.setNoTelepon(data[3].trim());

                        em.persist(p);
                    } else {
                        // Kalau jumlah kolom tidak sesuai
                        JOptionPane.showMessageDialog(this,
                                "Format CSV tidak sesuai di baris: " + line,
                                "Kesalahan Format CSV",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }

                em.getTransaction().commit();
                JOptionPane.showMessageDialog(this, "Data pelanggan berhasil diimpor dari CSV!");
                showTablePelanggan(); // tampilkan data terbaru

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal impor: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelpelanggan = new javax.swing.JTable();
        jButtonTAMBAH1 = new javax.swing.JButton();
        jButtonHAPUS1 = new javax.swing.JButton();
        jButtonPERBARUI1 = new javax.swing.JButton();
        jButtonCETAKPELANGGAN = new javax.swing.JButton();
        jButtonUPLOAD1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButtonTAMBAH = new javax.swing.JButton();
        jButtonHAPUS = new javax.swing.JButton();
        jButtonPERBARUI = new javax.swing.JButton();
        jButtonCETAKDATAROTI = new javax.swing.JButton();
        jButtonUPLOAD = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabeldataroti = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Constantia", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("TOKO ROTI");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, -1, -1));

        jPanel4.setBackground(new java.awt.Color(255, 204, 102));

        tabelpelanggan.setBackground(new java.awt.Color(255, 204, 102));
        tabelpelanggan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tabelpelanggan.setForeground(new java.awt.Color(0, 0, 0));
        tabelpelanggan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tabelpelanggan);

        jButtonTAMBAH1.setBackground(new java.awt.Color(255, 255, 204));
        jButtonTAMBAH1.setFont(new java.awt.Font("Microsoft PhagsPa", 1, 10)); // NOI18N
        jButtonTAMBAH1.setForeground(new java.awt.Color(0, 0, 0));
        jButtonTAMBAH1.setText("TAMBAH");
        jButtonTAMBAH1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTAMBAH1ActionPerformed(evt);
            }
        });

        jButtonHAPUS1.setBackground(new java.awt.Color(255, 255, 204));
        jButtonHAPUS1.setFont(new java.awt.Font("Microsoft PhagsPa", 1, 10)); // NOI18N
        jButtonHAPUS1.setForeground(new java.awt.Color(0, 0, 0));
        jButtonHAPUS1.setText("HAPUS");
        jButtonHAPUS1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHAPUS1ActionPerformed(evt);
            }
        });

        jButtonPERBARUI1.setBackground(new java.awt.Color(255, 255, 204));
        jButtonPERBARUI1.setFont(new java.awt.Font("Microsoft PhagsPa", 1, 10)); // NOI18N
        jButtonPERBARUI1.setForeground(new java.awt.Color(0, 0, 0));
        jButtonPERBARUI1.setText("PERBARUI");
        jButtonPERBARUI1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPERBARUI1ActionPerformed(evt);
            }
        });

        jButtonCETAKPELANGGAN.setBackground(new java.awt.Color(255, 255, 204));
        jButtonCETAKPELANGGAN.setFont(new java.awt.Font("Microsoft PhagsPa", 1, 10)); // NOI18N
        jButtonCETAKPELANGGAN.setForeground(new java.awt.Color(0, 0, 0));
        jButtonCETAKPELANGGAN.setText("CETAK");
        jButtonCETAKPELANGGAN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCETAKPELANGGANActionPerformed(evt);
            }
        });

        jButtonUPLOAD1.setBackground(new java.awt.Color(255, 255, 204));
        jButtonUPLOAD1.setFont(new java.awt.Font("Microsoft PhagsPa", 1, 10)); // NOI18N
        jButtonUPLOAD1.setForeground(new java.awt.Color(0, 0, 0));
        jButtonUPLOAD1.setText("MENGUNGGAH");
        jButtonUPLOAD1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUPLOAD1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jButtonTAMBAH1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonHAPUS1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonPERBARUI1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(164, 164, 164)
                .addComponent(jButtonCETAKPELANGGAN, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonUPLOAD1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonUPLOAD1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonHAPUS1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonTAMBAH1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonPERBARUI1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCETAKPELANGGAN, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24))
        );

        jTabbedPane1.addTab("PELANGGAN", jPanel4);

        jPanel1.setBackground(new java.awt.Color(255, 153, 102));

        jButtonTAMBAH.setBackground(new java.awt.Color(255, 102, 51));
        jButtonTAMBAH.setFont(new java.awt.Font("Microsoft PhagsPa", 1, 10)); // NOI18N
        jButtonTAMBAH.setForeground(new java.awt.Color(0, 0, 0));
        jButtonTAMBAH.setText("TAMBAH");
        jButtonTAMBAH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTAMBAHActionPerformed(evt);
            }
        });

        jButtonHAPUS.setBackground(new java.awt.Color(255, 102, 51));
        jButtonHAPUS.setFont(new java.awt.Font("Microsoft PhagsPa", 1, 10)); // NOI18N
        jButtonHAPUS.setForeground(new java.awt.Color(0, 0, 0));
        jButtonHAPUS.setText("HAPUS");
        jButtonHAPUS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonHAPUSActionPerformed(evt);
            }
        });

        jButtonPERBARUI.setBackground(new java.awt.Color(255, 102, 51));
        jButtonPERBARUI.setFont(new java.awt.Font("Microsoft PhagsPa", 1, 10)); // NOI18N
        jButtonPERBARUI.setForeground(new java.awt.Color(0, 0, 0));
        jButtonPERBARUI.setText("PERBARUI");
        jButtonPERBARUI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPERBARUIActionPerformed(evt);
            }
        });

        jButtonCETAKDATAROTI.setBackground(new java.awt.Color(255, 102, 51));
        jButtonCETAKDATAROTI.setFont(new java.awt.Font("Microsoft PhagsPa", 1, 10)); // NOI18N
        jButtonCETAKDATAROTI.setForeground(new java.awt.Color(0, 0, 0));
        jButtonCETAKDATAROTI.setText("CETAK");
        jButtonCETAKDATAROTI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCETAKDATAROTIActionPerformed(evt);
            }
        });

        jButtonUPLOAD.setBackground(new java.awt.Color(255, 102, 51));
        jButtonUPLOAD.setFont(new java.awt.Font("Microsoft PhagsPa", 1, 10)); // NOI18N
        jButtonUPLOAD.setForeground(new java.awt.Color(0, 0, 0));
        jButtonUPLOAD.setText("MENGUNGGAH");
        jButtonUPLOAD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUPLOADActionPerformed(evt);
            }
        });

        tabeldataroti.setBackground(new java.awt.Color(255, 153, 102));
        tabeldataroti.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tabeldataroti.setForeground(new java.awt.Color(0, 0, 0));
        tabeldataroti.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "id_roti", "nama_roti", "harga", "stock", "id_pelanggan"
            }
        ));
        jScrollPane1.setViewportView(tabeldataroti);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jButtonTAMBAH, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonHAPUS, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonPERBARUI, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonCETAKDATAROTI, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonUPLOAD, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonUPLOAD, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonHAPUS, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonTAMBAH, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonPERBARUI, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCETAKDATAROTI, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jTabbedPane1.addTab("DATA ROTI", jPanel1);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 710, 320));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/foto/A rustic, artisanal bakery display, featuring freshly baked bread and pastries, celebrat.jpeg"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 370));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonUPLOADActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUPLOADActionPerformed
        imporCsvKeDatabaseDataRoti();
    }//GEN-LAST:event_jButtonUPLOADActionPerformed

    private void jButtonCETAKDATAROTIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCETAKDATAROTIActionPerformed
        try {
            String path = "src/pertemuan12/REPORTDATAROTI.jasper";
            HashMap<String, Object> parameters = new HashMap<>();

            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/TUGASPBO12", "postgres", "BISMILLAH");

            JasperPrint jprint = JasperFillManager.fillReport(path, parameters, conn);
            JasperViewer jviewer = new JasperViewer(jprint, false);
            jviewer.setSize(800, 600);
            jviewer.setLocationRelativeTo(this);
            jviewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jviewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonCETAKDATAROTIActionPerformed

    private void jButtonPERBARUIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPERBARUIActionPerformed
        int row = tabeldataroti.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu di tabel!");
            return;
        }

        idrotiLama = tabeldataroti.getValueAt(row, 0).toString();
        namarotiLama = tabeldataroti.getValueAt(row, 1).toString();
        hargaLama = tabeldataroti.getValueAt(row, 2).toString();
        stokLama = tabeldataroti.getValueAt(row, 3).toString();
        idpelangganLama = tabeldataroti.getValueAt(row, 4).toString();

        UpdateDATAROTI dialog = new UpdateDATAROTI(this, true, idrotiLama, namarotiLama, stokLama, hargaLama, idpelangganLama);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        showTable();
        showTablePelanggan();
    }//GEN-LAST:event_jButtonPERBARUIActionPerformed

    private void jButtonHAPUSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHAPUSActionPerformed
        int[] rows = tabeldataroti.getSelectedRows(); // ambil semua baris yang dipilih
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu di tabel!");
            return;
        }

        String[] idrotiLama = new String[rows.length];
        String[] namarotiLama = new String[rows.length];
        String[] hargaLama = new String[rows.length];
        String[] stokLama = new String[rows.length];
        String[] idpelangganLama = new String[rows.length];

        for (int i = 0; i < rows.length; i++) {
            idrotiLama[i] = tabeldataroti.getValueAt(rows[i], 0).toString();
            namarotiLama[i] = tabeldataroti.getValueAt(rows[i], 1).toString();
            stokLama[i] = tabeldataroti.getValueAt(rows[i], 2).toString();
            hargaLama[i] = tabeldataroti.getValueAt(rows[i], 3).toString();
            idpelangganLama[i] = tabeldataroti.getValueAt(rows[i], 4).toString();
        }

        DeleteDATAROTI dialog = new DeleteDATAROTI(this, true, idrotiLama, namarotiLama, hargaLama, stokLama, idpelangganLama);
        dialog.setLocationRelativeTo(this); // tampil di tengah layar
        dialog.setVisible(true);

        showTable();
        showTablePelanggan();

    }//GEN-LAST:event_jButtonHAPUSActionPerformed

    private void jButtonTAMBAHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTAMBAHActionPerformed
        InsertDATAROTI dialog = new InsertDATAROTI(this, true); // true = modal
        dialog.setLocationRelativeTo(this); // supaya muncul di tengah
        dialog.setVisible(true);

        showTablePelanggan();
        showTable();
    }//GEN-LAST:event_jButtonTAMBAHActionPerformed

    private void jButtonTAMBAH1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTAMBAH1ActionPerformed
        InsertPELANGGAN dialog = new InsertPELANGGAN(this, true); // 
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        showTablePelanggan();
    }//GEN-LAST:event_jButtonTAMBAH1ActionPerformed

    private void jButtonHAPUS1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonHAPUS1ActionPerformed
        int[] rows = tabelpelanggan.getSelectedRows(); // ambil semua baris yang dipilih
        if (rows.length == 0) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu di tabel!");
            return;
        }

        String[] idpelangganLama = new String[rows.length];
        String[] namaLama = new String[rows.length];
        String[] alamatLama = new String[rows.length];
        String[] nohpLama = new String[rows.length];

        for (int i = 0; i < rows.length; i++) {
            idpelangganLama[i] = tabelpelanggan.getValueAt(rows[i], 0).toString();
            namaLama[i] = tabelpelanggan.getValueAt(rows[i], 1).toString();
            alamatLama[i] = tabelpelanggan.getValueAt(rows[i], 2).toString();
            nohpLama[i] = tabelpelanggan.getValueAt(rows[i], 3).toString();

        }

        DeletePELANGGAN dialog = new DeletePELANGGAN(this, true, idpelangganLama, namaLama, alamatLama, nohpLama);
        dialog.setLocationRelativeTo(this); // tampil di tengah layar
        dialog.setVisible(true);

        showTable();
        showTablePelanggan();


    }//GEN-LAST:event_jButtonHAPUS1ActionPerformed

    private void jButtonPERBARUI1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPERBARUI1ActionPerformed
        int row = tabelpelanggan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu di tabel!");
            return;
        }

        idpelangganLama = tabelpelanggan.getValueAt(row, 0).toString();
        namaLama = tabelpelanggan.getValueAt(row, 1).toString();
        alamatLama = tabelpelanggan.getValueAt(row, 2).toString();
        nohpLama = tabelpelanggan.getValueAt(row, 3).toString();

        UpdatePELANGGAN dialog = new UpdatePELANGGAN(this, true, idpelangganLama, namaLama, alamatLama, nohpLama);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        showTable();
        showTablePelanggan();

    }//GEN-LAST:event_jButtonPERBARUI1ActionPerformed

    private void jButtonCETAKPELANGGANActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCETAKPELANGGANActionPerformed
        try {
            String path = "src/pertemuan12/DATAPELANGGAN.jasper";
            HashMap<String, Object> parameters = new HashMap<>();

            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/TUGASPBO12", "postgres", "BISMILLAH");

            JasperPrint jprint = JasperFillManager.fillReport(path, parameters, conn);
            JasperViewer jviewer = new JasperViewer(jprint, false);
            jviewer.setSize(800, 600);
            jviewer.setLocationRelativeTo(this);
            jviewer.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jviewer.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonCETAKPELANGGANActionPerformed

    private void jButtonUPLOAD1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUPLOAD1ActionPerformed
        imporCsvKeDatabasePelanggan();
    }//GEN-LAST:event_jButtonUPLOAD1ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Tampilan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tampilan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tampilan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tampilan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tampilan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCETAKDATAROTI;
    private javax.swing.JButton jButtonCETAKPELANGGAN;
    private javax.swing.JButton jButtonHAPUS;
    private javax.swing.JButton jButtonHAPUS1;
    private javax.swing.JButton jButtonPERBARUI;
    private javax.swing.JButton jButtonPERBARUI1;
    private javax.swing.JButton jButtonTAMBAH;
    private javax.swing.JButton jButtonTAMBAH1;
    private javax.swing.JButton jButtonUPLOAD;
    private javax.swing.JButton jButtonUPLOAD1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tabeldataroti;
    private javax.swing.JTable tabelpelanggan;
    // End of variables declaration//GEN-END:variables

    void refreshTable() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
