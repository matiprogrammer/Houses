package android.example.com.houses;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class new_flat_activity extends AppCompatActivity {
    private FlatViewModel mFlatViewModel;

    public static final String EXTRA_CITY = "com.example.android.wordlistsql.REPLY";
    public static final String EXTRA_STREET="street";
    public static final String EXTRA_CODE="code";
    public static final String EXTRA_IMAGE="image";
    public static final int PICK_IMAGE = 1;
    private EditText mEditFlatView;
    private EditText mStreetFlatView;
    private EditText mPostalCodeFlatView;
    private ImageView mImageFlat;
    public ByteArrayOutputStream bos;
    public Bitmap bm;
    public byte[] bitmapdata;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flat_activity);
        mEditFlatView = findViewById(R.id.edit_city);
        mStreetFlatView=findViewById(R.id.edit_street);
        mImageFlat=findViewById(R.id.flat_image);
        mPostalCodeFlatView=findViewById(R.id.edit_postal_code);
        mFlatViewModel = ViewModelProviders.of(this).get(FlatViewModel.class);


        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                Intent replyIntent = new Intent();
                if(bos!=null){
                    bitmapdata = bos.toByteArray();
                }
                String regex = "^[0-9]{2}(?:-[0-9]{3})?$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher=pattern.matcher(mPostalCodeFlatView.getText().toString());
                if(matcher.matches()) {
                    if (TextUtils.isEmpty(mEditFlatView.getText()) || TextUtils.isEmpty(mStreetFlatView.getText()) || TextUtils.isEmpty(mPostalCodeFlatView.getText())) {
                        setResult(RESULT_CANCELED, replyIntent);
                        errorMessage2();
                    } else {
                        String city = mEditFlatView.getText().toString();
                        String street = mStreetFlatView.getText().toString();
                        String postalCode = mPostalCodeFlatView.getText().toString();
                        replyIntent.putExtra(EXTRA_CITY, city);
                        replyIntent.putExtra(EXTRA_STREET, street);
                        replyIntent.putExtra(EXTRA_CODE, postalCode);
                        replyIntent.putExtra(EXTRA_IMAGE, bitmapdata);
                        setResult(RESULT_OK, replyIntent);
                        Flat flat = new Flat(mEditFlatView.getText().toString(), mStreetFlatView.getText().toString(), mPostalCodeFlatView.getText().toString(), -1, bitmapdata, null);
                        mFlatViewModel.insert(flat);
                    }

                    finish();
                }
                else make();
            }
        });

        final Button addButton=findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });



    }

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
    private void make()
    {
        Toast.makeText(this, "Nieprawidłowy kod pocztowy", Toast.LENGTH_LONG).show();
    }
    private void errorMessage2(){Toast.makeText(this, "Nie wypełniono wszystkich danych", Toast.LENGTH_LONG).show();}
}
