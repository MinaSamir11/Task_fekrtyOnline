package com.fekrety.fekretyonline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.MaterialEditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.fekrety.fekretyonline.Model.item;
import com.fekrety.fekretyonline.ui.Fragment1;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DialogEdit extends AppCompatDialogFragment {

    private MaterialEditText ET_name,ET_descrpition,ET_address;
    private Button btn_edit,btn_cancel;
    private de.hdodenhof.circleimageview.CircleImageView circleImageView;
       private item data ;
       private AlertDialog Dialog;
       private ProgressBar progressBar ;
    public DialogEdit(item data){
            this.data=data;    // recieved data to make update
    }


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.layout_dialog_edit, null);
         ET_name = view.findViewById(R.id.ET_name_Dialog);
         ET_descrpition = view.findViewById(R.id.ET_Descrpition);
         ET_address = view.findViewById(R.id.Et_address);
         btn_edit = view.findViewById(R.id.btn_send_Dialog_ET);
         btn_cancel = view.findViewById(R.id.btn_cancel_Dialog_ET);
         circleImageView=view.findViewById(R.id.img_profile_image_Dialog_ET);
         progressBar=view.findViewById(R.id.progressBar2);
         progressBar.setVisibility(View.INVISIBLE);
         ET_name.setText(data.getName());
         ET_address.setText(data.getAddress());
         ET_descrpition.setText(data.getDescrpition());
         //load picture
        PicassoClient.dowloadImage(getActivity().getApplicationContext(),data.getPhoto(),circleImageView);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog.dismiss();
            }
        });
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                //restrict field until finsh update
                ET_name.setEnabled(false);
                ET_address.setEnabled(false);
                btn_edit.setEnabled(false);
                ET_descrpition.setEnabled(false);
                 item item = new item(data.getId(),   ET_name.getText().toString(),
                ET_descrpition.getText().toString(),
                ET_address.getText().toString(),data.getRate(),data.getPhoto());
                 updateitem(item);
            }
        });
        alertDialog.setView(view).setTitle("").setCancelable(true);
        Dialog =alertDialog.create();
        return Dialog;
    }


    private void updateitem(final item item){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/MinaSamir11/Test/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JasonPlaceHolderApi jasonPlaceHolderApi =retrofit.create(JasonPlaceHolderApi.class);

        Call<item> updateitem = jasonPlaceHolderApi.updateitem(item.getId(),item);
        updateitem.enqueue(new Callback<com.fekrety.fekretyonline.Model.item>() {
            @Override
            public void onResponse(Call<com.fekrety.fekretyonline.Model.item> call, Response<com.fekrety.fekretyonline.Model.item> response) {
                if(response.isSuccessful()){
                    item item1 = response.body();
                    for (int i = 0; i <Fragment1.items.size() ; i++) {
                        if(Fragment1.items.get(i).getId().equals( item1.getId())){
                            Fragment1.items.remove(i);
                            Fragment1.items.add(i,item1);
                        }
                    }
                    //refrech list for the new data
                    Fragment1.lstview.invalidateViews();
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "update Successful", Toast.LENGTH_SHORT).show();
                    Dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<com.fekrety.fekretyonline.Model.item> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Faild to Edit right now", Toast.LENGTH_SHORT).show();
                //release button and fields to let user try again
                ET_name.setEnabled(true);
                ET_address.setEnabled(true);
                ET_descrpition.setEnabled(true);
                btn_edit.setEnabled(true);
            }
        });
    }

}
