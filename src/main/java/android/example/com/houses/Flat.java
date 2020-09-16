package android.example.com.houses;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "flat_table")
public class Flat implements Serializable {
    private String street, city,postalCode;

    @PrimaryKey(autoGenerate = true)
    private int Id;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    private String opinions;
//    public Flat(@NonNull String city,@NonNull String street,  @NonNull String postalCode, byte[] image)
//    {
//        this.street=street;
//        this.city=city;
//        this.postalCode=postalCode;
//        this.image=image;
//    }
    public Flat(@NonNull String city, @NonNull String street, @NonNull String postalCode, int Id,byte[] image,String opinions )
    {
        this.street=street;
        this.city=city;
        this.postalCode=postalCode;
        this.opinions=opinions;
        this.image=image;
        if(Id!=-1)
        setId(Id);
    }
//    public Flat(@NonNull String city,@NonNull String street,  @NonNull String postalCode, String imageURL)
//    {
//        this.street=street;
//        this.city=city;
//        this.postalCode=postalCode;
//         ByteArrayOutputStream bos;
//         Bitmap bm;
//         byte[] bitmapdata;
//
//         if(imageURL!=null){
//        bm = BitmapFactory.decodeFile(imageURL);
//        bos = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.JPEG, 70 , bos);
//        bitmapdata = bos.toByteArray();
//        this.image=bitmapdata;}
//    }
    public String getOpinions(){return opinions;}
    public String getStreet(){return this.street;}
    public String getCity(){return this.city;}
    public String getPostalCode(){return this.postalCode;}
    public byte[] getImage(){return image;}
    public int getId(){return this.Id;}
    public void setId(int id){this.Id=id;}
    public void setOpinions(String opnions){this.opinions=opinions;}

}
