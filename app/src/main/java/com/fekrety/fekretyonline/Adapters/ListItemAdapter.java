package com.fekrety.fekretyonline.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fekrety.fekretyonline.PicassoClient;
import com.fekrety.fekretyonline.R;
import com.fekrety.fekretyonline.Model.item;
import java.util.ArrayList;
import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListAdapterHolder> {
    private List<item> items = new ArrayList<>();
    private Activity  activity;
    private OnItemClickListener listener ;

    public ListItemAdapter(Activity activity){
        this.activity=activity;
    }
    @NonNull
    @Override
    public ListAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listcartproduct, viewGroup, false);
        return new ListAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterHolder noteHolder, int position) {
        item currentItem = items.get(position);
            //set data
        noteHolder.textViewname.setText(currentItem.getName());
        noteHolder.textViewaddress.setText(currentItem.getAddress());
        noteHolder.descrpition.setText(currentItem.getDescrpition());
        noteHolder.rate.setText(String.valueOf(currentItem.getRate()));

        PicassoClient.dowloadImage(activity.getApplicationContext(),currentItem.getPhoto(),noteHolder.circleImageView);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItem(List<item> items) {
        this.items = items;
        notifyDataSetChanged();   // refrech list
    }

    public item getItemAt(int position) {
        return items.get(position);
    }

    class ListAdapterHolder extends RecyclerView.ViewHolder {
        private TextView textViewname;
        private TextView textViewaddress;
        private TextView descrpition;
        private TextView rate;
        private de.hdodenhof.circleimageview.CircleImageView circleImageView;

        private ListAdapterHolder(View itemView) {
            super(itemView);
            textViewname = itemView.findViewById(R.id.txt_name);
            textViewaddress = itemView.findViewById(R.id.txt_address);
            descrpition = itemView.findViewById(R.id.txt_desc);
            rate = itemView.findViewById(R.id.txt_rate);
            circleImageView = itemView.findViewById(R.id.img_profile_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(items.get(position));
                    }
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
