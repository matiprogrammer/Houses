package android.example.com.houses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class AddOpinion extends AppCompatActivity {

    public static final String EXTRA_AUTHOR="author";
    public final static String EXTRA_CONTENT="content";
    public final static String EXTRA_STARS="stars";
    private Button applyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_opinion);
        final EditText mAutor= findViewById(R.id.autor_textview);
        final EditText mContent=findViewById(R.id.content_textview);
        final RatingBar mRatingBar=findViewById(R.id.ratingBar);
        mRatingBar.setStepSize(1);
         applyButton= findViewById(R.id.apply_button);
        applyButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent resultIntent=new Intent();
                String author=mAutor.getText().toString();
                String content=mContent.getText().toString();
                int filledStar=(int)mRatingBar.getRating();


                if (TextUtils.isEmpty(mAutor.getText()) ||TextUtils.isEmpty(mContent.getText()) ) {
                    setResult(RESULT_CANCELED, resultIntent);
                }
                else {
                    resultIntent.putExtra(EXTRA_AUTHOR, author);
                    resultIntent.putExtra(EXTRA_CONTENT, content);
                    resultIntent.putExtra(EXTRA_STARS, filledStar);
                    setResult(RESULT_OK, resultIntent);

                }
                finish();
            }

        });
    }
}
