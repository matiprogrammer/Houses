package android.example.com.houses;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditFlatActivity extends AppCompatActivity {

    public static final String EXTRA_CITY = "city";
    public static final String EXTRA_STREET="street";
    public static final String EXTRA_CODE="code";
    public static final String EXTRA_ID="flat";
    public static final String EXTRA_IMAGE="image";
    public static final String EXTRA_OPINIONS="opinions";
    public static final int PICK_IMAGE = 1;
    private EditText mEditFlatView;
    private EditText mStreetFlatView;
    private EditText mPostalCodeFlatView;
    private ImageView mImageFlat;
    public ByteArrayOutputStream bos;
    public Bitmap bm;
    public byte[] bitmapdata;
    private LiveData<Flat>flat;
private int flatId;
private Bundle bundle;
    FlatViewModel flatModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flat_activity);

        mEditFlatView = findViewById(R.id.edit_city);
        mStreetFlatView = findViewById(R.id.edit_street);
        mPostalCodeFlatView = findViewById(R.id.edit_postal_code);
        mImageFlat=findViewById(R.id.flat_image);
        flatModel=ViewModelProviders.of(this).get(FlatViewModel.class);

        bm = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("image") , 0, getIntent().getByteArrayExtra("image") .length);
        mImageFlat.setImageBitmap(bm);
        mEditFlatView.setText(getIntent().getStringExtra("city"));
        mStreetFlatView.setText(getIntent().getStringExtra("street"));
        mPostalCodeFlatView.setText(getIntent().getStringExtra("code"));
        bundle=getIntent().getExtras();
        if(bundle!=null)
            flatId=bundle.getInt("flat_id");
        flat=flatModel.getFlat(flatId);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent resutlIntent=new Intent();
                String updatedCity=mEditFlatView.getText().toString();
                String updatedStreet=mStreetFlatView.getText().toString();
                String updatedCode=mPostalCodeFlatView.getText().toString();

                if(bos!=null){
                    bitmapdata = bos.toByteArray();
                }
                String regex = "^[0-9]{2}(?:-[0-9]{3})?$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher=pattern.matcher(mPostalCodeFlatView.getText().toString());
                if(matcher.matches()) {
                    if (TextUtils.isEmpty(mEditFlatView.getText()) || TextUtils.isEmpty(mStreetFlatView.getText()) || TextUtils.isEmpty(mPostalCodeFlatView.getText())) {
                        setResult(RESULT_CANCELED, resutlIntent);
                    } else {
                        resutlIntent.putExtra(EXTRA_CITY, updatedCity);
                        resutlIntent.putExtra(EXTRA_STREET, updatedStreet);
                        resutlIntent.putExtra(EXTRA_CODE, updatedCode);
                        resutlIntent.putExtra(EXTRA_ID, flatId);
                        if(bitmapdata!=null)
                        resutlIntent.putExtra(EXTRA_IMAGE, bitmapdata);
                        else
                            resutlIntent.putExtra(EXTRA_IMAGE, getIntent().getByteArrayExtra("image"));
                        resutlIntent.putExtra(EXTRA_OPINIONS,getIntent().getStringExtra(EXTRA_OPINIONS));
                        setResult(RESULT_OK, resutlIntent);
                    }

                    finish();
                }
                else make();

            }
        });

        final Button addButton=findViewById(R.id.button_add);
        addButton.setText(getString(R.string.edit_photo));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
            }
        });


    }
    private void make(){Toast.makeText(this, "Nieprawidłowy kod pocztowy", Toast.LENGTH_LONG).show();}
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            InputStream inputStream = null;
            bm=null;

            try {
                inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(inputStream!=null) {
                bm = BitmapFactory.decodeStream(inputStream);
                mImageFlat.setImageBitmap(bm);
                bos = new ByteArrayOutputStream();
                if(bm.compress(Bitmap.CompressFormat.JPEG, 40, bos))
                    Toast.makeText(this, "udalo sie", Toast.LENGTH_SHORT).show();


                else
                    Toast.makeText(this, "nie udalo sie", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "nie ma sciezki", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
