package android.example.com.houses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Rating;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class FlatListAdapter extends RecyclerView.Adapter<FlatListAdapter.FlatViewHolder> {
    private Context mContext;
    private Bitmap bm;
    SparseBooleanArray itemStateArray= new SparseBooleanArray();

    FlatListAdapter(Context context) { mInflater = LayoutInflater.from(context); mContext=context; }

    class FlatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView flatNameView;
    private final ImageView flatImageView;
    private final TextView flatStreetView;
    private CheckBox checkBox;
    private TextView edit;
        private LinearLayout parentLayout;
        private RatingBar ratingBar;


        private FlatViewHolder(View itemView) {
            super(itemView);
            flatNameView = itemView.findViewById(R.id.city);
            flatImageView=(ImageView) itemView.findViewById(R.id.photo);
            flatStreetView=itemView.findViewById(R.id.street);
            parentLayout=itemView.findViewById(R.id.parent_layout);
            checkBox=itemView.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(this);
            edit=itemView.findViewById(R.id.edit);
            ratingBar=itemView.findViewById(R.id.ratingBar3);

        }

        void bind(int position) {
            // use the sparse boolean array to check
            if (!itemStateArray.get(position, false)) {
                checkBox.setChecked(false);}
            else {
                checkBox.setChecked(true);
            }

        }
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
           makeText();
            if (!itemStateArray.get(adapterPosition, false)) {
                checkBox.setChecked(true);
                itemStateArray.put(adapterPosition, true);
            }
            else  {
                checkBox.setChecked(false);
                itemStateArray.put(adapterPosition, false);
            }
        }

        public void setlisteners() {
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Flat current=mFlats.get(getAdapterPosition());
                    Intent intent=new Intent(mContext,EditFlatActivity.class);
                    intent.putExtra("flat_id",current.getId());
                    intent.putExtra("city",current.getCity());
                    intent.putExtra("street",current.getStreet());
                    intent.putExtra("code",current.getPostalCode());
                    intent.putExtra("image",current.getImage());
                    intent.putExtra("opinions",current.getOpinions());
                    ((Activity)mContext).startActivityForResult(intent,MainActivity.UPDATE_ACTIVITY_REQUEST_CODE);
                }
            });


        }
    }

    private final LayoutInflater mInflater;
    private List<Flat> mFlats; // Cached copy of flats



    @Override
    public FlatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new FlatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FlatViewHolder holder, final int position) {
        holder.bind(position);
        if (mFlats != null) {
           final Flat current = mFlats.get(position);
            holder.flatNameView.setText(current.getCity());
           bm = BitmapFactory.decodeByteArray(current.getImage() , 0, current.getImage() .length);
            holder.setlisteners();
            holder.flatImageView.setImageBitmap(bm);
            holder.flatStreetView.setText(current.getStreet());
            float avgStars=0;
            ArrayList<Opinion> opinions=new ArrayList<>();
            Gson gson = new Gson();
            opinions= gson.fromJson(mFlats.get(position).getOpinions(),new TypeToken<ArrayList<Opinion>>(){}.getType());
            if(opinions!=null) {
                for (Opinion opinion : opinions)
                    avgStars += opinion.getCheckedStars();
                avgStars/=opinions.size();
            }
            holder.ratingBar.setRating(avgStars);
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(mContext, flat_activity.class);
                    intent.putExtra("opinions", current.getOpinions());
                    intent.putExtra("street",current.getStreet());
                    intent.putExtra("city",current.getCity());
                    intent.putExtra("postalCode",current.getPostalCode());
                    intent.putExtra("photo",current.getImage());
                    intent.putExtra("flat_id",current.getId());
                    mContext.startActivity(intent);

                }
            });

        } else {
            // Covers the case of data not being ready yet.
            holder.flatNameView.setText(mContext.getString(R.string.no_flats));
        }


    }

    void setFlats(List<Flat> flats){
        mFlats = flats;
        notifyDataSetChanged();
    }

    public List<Flat> getAllFlats()
    {
        return mFlats;
    }
    public Flat getItem(int position) {
        return mFlats.get(position);
    }
    void makeText()
    {
        Toast.makeText(mContext, "kliknieto", Toast.LENGTH_SHORT).show();
    }

    boolean isCheckedFlat(int position)
    {
        return itemStateArray.get(position, false);
    }
    void setChecked(int pos,boolean value)
    {
        itemStateArray.put(pos,value);
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mFlats != null)
            return mFlats.size();
        else return 0;
    }
}
