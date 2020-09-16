package android.example.com.houses;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FlatRepository {

    private FlatDao mFlatDao;
    private LiveData<List<Flat> > mAllFlats;
   static private LiveData<List<Flat>> mFlats=null ;

    FlatRepository(Application application) {
        FlatRoomDatabase db = FlatRoomDatabase.getDatabase(application);
        mFlatDao = db.flatDao();
        mAllFlats = mFlatDao.getAllFlats();
    }

    LiveData<List<Flat>> getAllFlats() {
        return mAllFlats;
    }
    LiveData<Flat>getFlat(int flatId){return mFlatDao.getFlat(flatId);}
    LiveData<List<Flat>> findCityByName(String city) {return mFlatDao.findCityByName(city);}
    LiveData<List<Flat>> findFlatByAddress(String address) {return mFlatDao.findFlatByAddress(address);}



    public void insert (Flat flat) {
        new insertAsyncTask(mFlatDao).execute(flat);
    }
    public void delete (Flat flat) { new deleteAsyncTask(mFlatDao).execute(flat);}
    public void update(Flat flat){new updateAsyncTask(mFlatDao).execute(flat);}


    private static class findAsyncTask extends AsyncTask<String,Void, LiveData<List<Flat>>> {

        private FlatDao mAsyncTaskDao;

        findAsyncTask(FlatDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected LiveData<List<Flat>> doInBackground(final String... params) {
          return  mAsyncTaskDao.findCityByName(params[0]);
        }


        protected void onPostExecute( LiveData<List<Flat>> result)
        {
        mFlats=result;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Flat, Void, Void> {

        private FlatDao mAsyncTaskDao;

        insertAsyncTask(FlatDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Flat... params) {
            mAsyncTaskDao.insert(params[0]);

            return null;
        }
    }

    private static class deleteAsyncTask extends android.os.AsyncTask<Flat, Void, Void> {

        private FlatDao mAsyncTaskDao;

        deleteAsyncTask(FlatDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Flat... params) {
            mAsyncTaskDao.delete(params[0]);

            return null;
        }
    }
    private static class updateAsyncTask extends android.os.AsyncTask<Flat, Void, Void> {

        private FlatDao mAsyncTaskDao;

        updateAsyncTask(FlatDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Flat... params) {
            mAsyncTaskDao.update(params[0]);

            return null;
        }
    }


    }
