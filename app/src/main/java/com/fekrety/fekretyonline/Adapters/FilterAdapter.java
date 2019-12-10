package com.fekrety.fekretyonline.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.fekrety.fekretyonline.Dialogs.DialogEdit;
import com.fekrety.fekretyonline.Model.JasonPlaceHolderApi;
import com.fekrety.fekretyonline.PicassoClient;
import com.fekrety.fekretyonline.R;
import com.fekrety.fekretyonline.Model.item;
import com.fekrety.fekretyonline.ui.Fragment1;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@SuppressWarnings("unchecked")
public class FilterAdapter extends ArrayAdapter<item> implements Filterable {
    private List<item> allchildModels ;
    private List<item> filtersListallchildModels ;
    private customFilter customFilter;
    private Context context;
    private FragmentManager fragmentmanger;
    private Activity activity;
    public FilterAdapter(Context context , List<item> child_data, FragmentManager fragmentmanger,Activity activity){
        super (context, R.layout.list_cart_items,child_data);
        this.fragmentmanger=fragmentmanger;
        this.allchildModels=child_data;
        this.filtersListallchildModels=child_data;
        this.context=context;
        this.activity=activity;
    }
    public  class Holder {
        TextView txt_name,txt_address ,txt_desc,txt_rate;
        de.hdodenhof.circleimageview.CircleImageView profile,option_menu ;
    }

    @Override
    public int getCount() {
        return allchildModels.size();
    }

    @Nullable
    @Override
    public item getItem(int position) {
        return allchildModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return allchildModels.indexOf(getItem(position));
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final item data =getItem(position);
        final Holder viewHolder;
        viewHolder =new Holder();
        LayoutInflater inflater =LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.list_cart_items,parent,false);
        if(data!=null ) {
            viewHolder.txt_name = convertView.findViewById(R.id.txt_name_H);
            viewHolder.txt_address = convertView.findViewById(R.id.txt_address_H);
            viewHolder.txt_desc = convertView.findViewById(R.id.txt_desc_H);
             viewHolder.txt_rate = convertView.findViewById(R.id.txt_rate_H);
             viewHolder.profile = convertView.findViewById(R.id.img_profile_image_H);
             viewHolder.option_menu = convertView.findViewById(R.id.dots_edit);

             viewHolder.option_menu.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     final PopupMenu  popupMenu = new PopupMenu(context,viewHolder.option_menu);
                     popupMenu.inflate(R.menu.option_item);   // inflate menu
                     popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                         @Override
                         public boolean onMenuItemClick(MenuItem item) {
                             switch (item.getItemId()){
                                 case R.id.edit_option :{
                                     DialogEdit dialogaboutprogram = new DialogEdit(data);
                                     dialogaboutprogram.setCancelable(false);                // not cancel dialog
                                     dialogaboutprogram.show(fragmentmanger, "App");
                                     break;
                                 }
                                 case R.id.delete_option:{
                                     AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                     builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {  // yes delete item
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             DeleteItem(data.getId());     // delete item from server according to id
                                         }
                                     }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();
                                         }
                                     }).setMessage("Do you want to delete this item");

                                     AlertDialog alertDialog =   builder.create();
                                     alertDialog.show();
                                     break;
                                 }
                             }
                             return false;
                         }
                     });
                     popupMenu.show();   // show menu
                 }
             });
             // set data
            viewHolder.txt_name.setText(data.getName());
            viewHolder.txt_address.setText(data.getAddress());
            viewHolder.txt_desc.setText(data.getDescrpition());
           viewHolder.txt_rate.setText((String.valueOf( data.getRate())));
            PicassoClient.dowloadImage(context,data.getPhoto(),viewHolder.profile);
        }
        return convertView;

    }

    private  void  DeleteItem(final int id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/MinaSamir11/Test/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JasonPlaceHolderApi jasonPlaceHolderApi =retrofit.create(JasonPlaceHolderApi.class);
        Call<Void> call = jasonPlaceHolderApi.deleteitem(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(activity, "Response" + response.code(), Toast.LENGTH_SHORT).show();
                    if (response.code() == 200) {   // response 200 successuf delete then delete from list of item
                        for (int i = 0; i < Fragment1.items.size(); i++) {
                            if (Fragment1.items.get(i).getId().equals(id)) {
                                Fragment1.items.remove(i);
                            }
                            Fragment1.lstview.invalidateViews();  // refresh after delete from list item
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity, "Failed to delete item", Toast.LENGTH_SHORT).show();
            }
        });
    }

// filter class
    class customFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if(constraint!=null&&constraint.length()>0) {
                    filterResults = Filter(constraint.toString().toUpperCase());

            }else{
                filterResults.count=filtersListallchildModels.size();
                filterResults.values=filtersListallchildModels;
            }
            return filterResults;
        }

        private FilterResults Filter(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<item> allchildModelsFiltered = new ArrayList<>();
            for (int i = 0; i <filtersListallchildModels.size() ; i++) {
                if(filtersListallchildModels.get(i).getName().toUpperCase().contains(constraint)     // filter according to name and address and descrpition
                || filtersListallchildModels.get(i).getAddress().toUpperCase().contains(constraint)
                        || filtersListallchildModels.get(i).getDescrpition().toUpperCase().contains(constraint)){
                    item allchildModel = new item(filtersListallchildModels.get(i).getId(),
                            filtersListallchildModels.get(i).getName()
                            ,filtersListallchildModels.get(i).getDescrpition(),
                            filtersListallchildModels.get(i).getAddress(),filtersListallchildModels.get(i).getRate(),
                            filtersListallchildModels.get(i).getPhoto());
                    allchildModelsFiltered.add(allchildModel);    // add new  filtered item to new list
                }
            }
            filterResults.count=allchildModelsFiltered.size();
            filterResults.values=allchildModelsFiltered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            allchildModels = (ArrayList<item>) results.values;
            notifyDataSetChanged();
            notifyDataSetInvalidated();
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(customFilter == null){
            customFilter = new customFilter();
        }
        return customFilter;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }
}

