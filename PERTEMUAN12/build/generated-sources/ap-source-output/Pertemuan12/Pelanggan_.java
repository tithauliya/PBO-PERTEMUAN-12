package Pertemuan12;

import Pertemuan12.DataRoti;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-11-08T08:49:57", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Pelanggan.class)
public class Pelanggan_ { 

    public static volatile SingularAttribute<Pelanggan, String> noTelepon;
    public static volatile SingularAttribute<Pelanggan, String> namaPelanggan;
    public static volatile CollectionAttribute<Pelanggan, DataRoti> dataRotiCollection;
    public static volatile SingularAttribute<Pelanggan, String> idPelanggan;
    public static volatile SingularAttribute<Pelanggan, String> alamat;

}