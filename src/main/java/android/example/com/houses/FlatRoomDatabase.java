package android.example.com.houses;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Database(entities = {Flat.class}, version = 1)
public abstract class FlatRoomDatabase extends RoomDatabase {
    public abstract FlatDao flatDao();

    private static volatile FlatRoomDatabase INSTANCE;
    private static Context mContext;
    static FlatRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FlatRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FlatRoomDatabase.class, "flat_database").addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
            mContext=context;
        }
        return INSTANCE;

    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final FlatDao mDao;

        PopulateDbAsync(FlatRoomDatabase db) {
            mDao = db.flatDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
           // mDao.deleteAll();
//            Flat word = new Flat("Białystok","Zwierzyniecka 12","15-335",(Bitmap)BitmapFactory.decodeResource(mContext.getResources(),R.drawable.mieszkanie_2));
//            mDao.insert(word);
//            word = new Flat("Białystok","Zwierzyniecka 10","15-667",null);
//            mDao.insert(word);
            return null;
        }
    }
}
