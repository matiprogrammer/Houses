package android.example.com.houses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
private final View mWindow;
private Context context;
private HashMap<Marker,Flat> markerMap;
public CustomInfoWindowAdapter(Context context, HashMap<Marker,Flat> markerMap)
{this.context=context;
this.markerMap=markerMap;
    this.mWindow= LayoutInflater.from(context).inflate(R.layout.marker_view,null);


}

private void renderWindowText(Marker marker, View view){
    String street =markerMap.get(marker).getStreet();
    TextView streetMarker=view.findViewById(R.id.marker_street);
    RatingBar ratingBarMarer=view.findViewById(R.id.marker_ratingBar);
    TextView votersMarker=view.findViewById(R.id.marker_voters);



    if(street!="")
        streetMarker.setText(street);
    float avgStars=0;
    int countVoters=0;
    ArrayList<Opinion> opinions=new ArrayList<>();
    Gson gson = new Gson();
    opinions= gson.fromJson(markerMap.get(marker).getOpinions(),new TypeToken<ArrayList<Opinion>>(){}.getType());
    if(opinions!=null) {
        for (Opinion opinion : opinions) {
            avgStars += opinion.getCheckedStars();
            countVoters++;
        }
        avgStars/=opinions.size();
    }
    ratingBarMarer.setRating(avgStars);
    votersMarker.setText(context.getResources().getString(R.string.opinionsCount)+countVoters);


}


    @Override
    public View getInfoWindow(final Marker marker) {
    renderWindowText(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return mWindow;
    }
}
