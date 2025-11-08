package Pertemuan12;

import Pertemuan12.Pelanggan;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-11-08T08:49:57", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(DataRoti.class)
public class DataRoti_ { 

    public static volatile SingularAttribute<DataRoti, String> namaRoti;
    public static volatile SingularAttribute<DataRoti, Integer> harga;
    public static volatile SingularAttribute<DataRoti, String> idRoti;
    public static volatile SingularAttribute<DataRoti, Pelanggan> idPelanggan;
    public static volatile SingularAttribute<DataRoti, Integer> stok;

}