package ecocupfirebase.ecocup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
Context context;
ArrayList<Model> models;
    private static final String TAG = "MyActivity";

public MyAdapter(Context c, ArrayList<Model> p)
{
    context = c;
    models = p;
}
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.mainlayout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.Friend.setText(models.get(position).getFriend());

    }

    @Override
    public int getItemCount() {

    return models.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView Friend;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           Friend =itemView.findViewById(R.id.Friend);
        }
    }
}
