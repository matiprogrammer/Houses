package android.example.com.houses;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FlatDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(Flat flat);

    @Query("DELETE from flat_table")
    void deleteAll();

    @Delete
    int delete(Flat flat);

    @Query("Select * FROM flat_table")
    LiveData<List<Flat>> getAllFlats();

    @Query("SELECT * FROM flat_table WHERE Id=:flatId")
    LiveData<Flat> getFlat(int flatId);

    @Query("SELECT * from flat_table WHERE street LIKE :address OR city LIKE :address OR postalCode LIKE :address")
    LiveData<List<Flat>> findFlatByAddress(String address);

    @Query("SELECT * from flat_table WHERE city LIKE :city ")
    LiveData<List<Flat>> findCityByName(String city);

    @Update
    void update(Flat flat);
}
