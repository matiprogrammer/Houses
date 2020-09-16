package android.example.com.houses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class OpinionListAdapter extends RecyclerView.Adapter<OpinionListAdapter.ViewHolder>  {

    private ArrayList<Opinion> opinions= new ArrayList<>();
    private Context context;

    public OpinionListAdapter(Context context,ArrayList<Opinion> opinions)
    {
        this.opinions=opinions;
        this.context=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.opinionslist_item,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    Log.d("debugging","onBindViewHolder: called");
    viewHolder.autor.setText(opinions.get(i).getAutor());
        viewHolder.content.setText((opinions.get(i).getContent()));
        viewHolder.ratingBar.setIsIndicator(true);
        viewHolder.ratingBar.setRating(opinions.get(i).getCheckedStars());
    }

    @Override
    public int getItemCount() {
        if(opinions==null)
            return 0;
        else
        return opinions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView autor;
        TextView content;
        RatingBar ratingBar;
        public ViewHolder(View itemView){
            super(itemView);
            autor= itemView.findViewById(R.id.autor_name);
            content=itemView.findViewById(R.id.content);
            ratingBar=itemView.findViewById(R.id.ratingBar);
        }
    }

    void setOpinions(ArrayList<Opinion> opinions){
        this.opinions=opinions;
        notifyDataSetChanged();
    }
}
