package ecocupfirebase.ecocup;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StoreViewholder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView, textView1,t1,t2,t3,t4,textview10;


    public StoreViewholder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.rimage);
        textView = itemView.findViewById(R.id.bstats);
        textview10 = itemView.findViewById(R.id.textView10);
        textView1 = itemView.findViewById(R.id.shopname);
        t1 = itemView.findViewById(R.id.masss);
        t2 = itemView.findViewById(R.id.treess);
        t3 = itemView.findViewById(R.id.waters);
        t4 = itemView.findViewById(R.id.cos);
    }
}
