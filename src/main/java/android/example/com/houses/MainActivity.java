package android.example.com.houses;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private FlatViewModel mFlatViewModel;
    FlatListAdapter adapter;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_ACTIVITY_REQUEST_CODE = 2;
    private ImageView search;
    private EditText input;

    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        search=findViewById(R.id.search);
        input=findViewById(R.id.input_text);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FindFlatsByAddress(input.getText().toString().split("\\s"));
                                        }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, new_flat_activity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new FlatListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFlatViewModel = ViewModelProviders.of(this).get(FlatViewModel.class);
        mFlatViewModel.getAllWords().observe(this, new Observer<List<Flat>>() {
            @Override
            public void onChanged(@Nullable final List<Flat> flats) {
                // Update the cached copy of the words in the adapter.
                adapter.setFlats(flats);
            }
        });


    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            deleteCheckedFlats();
            return true;
    }
        if(id==R.id.action_map){
            if(isServicesOK()) {

                List<Flat> mFlats=adapter.getAllFlats();
                Intent mapIntent = new Intent(MainActivity.this, MapsActivity.class);
                mapIntent.putExtra("flats",(Serializable)mFlats);
                startActivity(mapIntent);
            }
            return true;

        }
        return super.onOptionsItemSelected(item);
}

    public void FindFlatsByAddress(String[] address)
    {

        if(address.length==1)
        mFlatViewModel.findFlatByAddress("%" + address[0] + "%" ).observe(this, new Observer<List<Flat>>() {
            @Override
            public void onChanged(@Nullable List<Flat> flats) {
                adapter.setFlats(flats);
            }
        });
        if(address.length==2)
            mFlatViewModel.findFlatByAddress("%" + address[0] + "%" +address[1]+"%").observe(this, new Observer<List<Flat>>() {
                @Override
                public void onChanged(@Nullable List<Flat> flats) {
                    adapter.setFlats(flats);
                }
            });
        if(address.length==3)
            mFlatViewModel.findFlatByAddress("%" + address[0] + "%" +address[1]+"%"+address[2]+"%").observe(this, new Observer<List<Flat>>() {
                @Override
                public void onChanged(@Nullable List<Flat> flats) {
                    adapter.setFlats(flats);
                }
            });

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                Flat flat = new Flat(data.getStringExtra(new_flat_activity.EXTRA_CITY),data.getStringExtra(new_flat_activity.EXTRA_STREET),data.getStringExtra(new_flat_activity.EXTRA_CODE),data.getByteArrayExtra(new_flat_activity.EXTRA_IMAGE));
//            mFlatViewModel.insert(flat);
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        R.string.empty_not_saved,
                        Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == UPDATE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
            Flat flat = new Flat(data.getStringExtra(EditFlatActivity.EXTRA_CITY),data.getStringExtra(EditFlatActivity.EXTRA_STREET),data.getStringExtra(EditFlatActivity.EXTRA_CODE),data.getIntExtra(EditFlatActivity.EXTRA_ID,1),data.getByteArrayExtra(EditFlatActivity.EXTRA_IMAGE), data.getStringExtra(EditFlatActivity.EXTRA_OPINIONS));
            mFlatViewModel.update(flat);
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        R.string.empty_not_saved,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

        void deleteCheckedFlats()
        {
            for (int i = 0; i < adapter.getItemCount(); i++) {
                if (adapter.isCheckedFlat(i)) {
                    mFlatViewModel.delete(adapter.getItem(i));
                    adapter.setChecked(i, false);
                }
            }
        }
    }
