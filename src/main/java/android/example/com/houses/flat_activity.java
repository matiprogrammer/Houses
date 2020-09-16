package android.example.com.houses;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class flat_activity extends AppCompatActivity {

    private FlatViewModel mFlatViewModel;
    private TextView mStreetFlatView,mCityFlatView,mPostalCodeFlatView;
    private ImageView mImageFlatView;
    private Bitmap bm;
    private OpinionListAdapter opinionsAdapter;
    private RecyclerView opinionsRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    public static final int ADD_ACTIVITY_REQUEST_CODE=1;
    private  ArrayList<Opinion> opinions= new ArrayList<>();



    private int flatId;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_activity);

        mFlatViewModel = ViewModelProviders.of(this).get(FlatViewModel.class);
        ArrayList<String> autors=new ArrayList<>();
        ArrayList<String> contents=new ArrayList<>();
        autors.add("Mateusz");
        autors.add("Tadeusz");
        autors.add("Marek");
        autors.add("Darek");
        autors.add("Tadeusz");
        autors.add("Marek");
        autors.add("Darek");

        contents.add("Bardzo fajne mieszkanie, łazienka troche strara, a z sedesu smierdzi, ale jest ok");
        contents.add("Bardzo fajne mieszkanie, łazienka troche strara, a z sedesu smierdzi, ale jest ok");
        contents.add("Bardzo fajne mieszkanie, łazienka troche strara, a z sedesu smierdzi, ale jest ok");
        contents.add("Własciciel wkurzajacy");
        contents.add("Bardzo fajne mieszkanie, łazienka troche strara, a z sedesu smierdzi, ale jest ok");
        contents.add("Bardzo fajne mieszkanie, łazienka troche strara, a z sedesu smierdzi, ale jest ok");
        contents.add("Własciciel wkurzajacy");

        Gson gson = new Gson();
        opinions= gson.fromJson(getIntent().getStringExtra("opinions"),new TypeToken<ArrayList<Opinion>>(){}.getType());
        //if(opinions.get(0)!=null)
        //Log.d("flatOpinion",opinions.get(0).getAutor());
        opinionsRecycleView=findViewById(R.id.opinions_recycleview);
        opinionsAdapter=new OpinionListAdapter(this,opinions);
        opinionsRecycleView.setAdapter(opinionsAdapter);
        opinionsRecycleView.setLayoutManager(new LinearLayoutManager(this));


        mStreetFlatView=findViewById(R.id.street);
        mCityFlatView=findViewById(R.id.city);
        mPostalCodeFlatView=findViewById(R.id.postal_code);
        mImageFlatView=findViewById(R.id.photo);
        bm = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("photo") , 0, getIntent().getByteArrayExtra("photo") .length);

        mStreetFlatView.setText(getIntent().getStringExtra("street"));
        mCityFlatView.setText(getIntent().getStringExtra("city"));
        mPostalCodeFlatView.setText(getIntent().getStringExtra("postalCode"));
        mImageFlatView.setImageBitmap(bm);

        bundle=getIntent().getExtras();
        if(bundle!=null)
            flatId=bundle.getInt("flat_id");
        LiveData<Flat> flat=mFlatViewModel.getFlat(flatId);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent addOpinionIntent=new Intent(flat_activity.this,AddOpinion.class);
            startActivityForResult(addOpinionIntent,ADD_ACTIVITY_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Gson gson= new Gson();
                Opinion opinion = new Opinion(data.getStringExtra(AddOpinion.EXTRA_AUTHOR), data.getStringExtra(AddOpinion.EXTRA_CONTENT), data.getIntExtra(AddOpinion.EXTRA_STARS,0));
                if(opinions==null)
                    opinions=new ArrayList<>();
                opinions.add(opinion);
                opinionsAdapter.setOpinions(opinions);
                String jsonOpinion=gson.toJson(opinions);

                Flat updateFlat= new Flat(getIntent().getStringExtra("city"),getIntent().getStringExtra("street"),getIntent().getStringExtra("postalCode"),getIntent().getIntExtra("flat_id",1),getIntent().getByteArrayExtra("photo"), jsonOpinion);
                mFlatViewModel.update(updateFlat);
            } else
                Toast.makeText(
                        getApplicationContext(),
                        R.string.empty_not_saved,
                        Toast.LENGTH_LONG).show();
            }
        }
    }



