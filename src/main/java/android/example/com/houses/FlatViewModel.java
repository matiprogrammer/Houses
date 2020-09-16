package android.example.com.houses;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FlatViewModel extends AndroidViewModel {
    private FlatRepository mRepository;
    private LiveData<List<Flat>> mAllFlats;

    public FlatViewModel (Application application) {
        super(application);
        mRepository = new FlatRepository(application);
        mAllFlats = mRepository.getAllFlats();
    }

    LiveData<List<Flat>> getAllWords() { return mAllFlats; }
    LiveData<List<Flat>> findCityByName(String city) {return mRepository.findCityByName(city);}
    public void insert(Flat flat) { mRepository.insert(flat); }
    public void delete(Flat flat) { mRepository.delete(flat); }
    public void update(Flat flat){mRepository.update(flat);}
    public LiveData<Flat> getFlat(int flatId){return mRepository.getFlat(flatId); }
    public LiveData<List<Flat>>findFlatByAddress(String address){return mRepository.findFlatByAddress(address);}
}
