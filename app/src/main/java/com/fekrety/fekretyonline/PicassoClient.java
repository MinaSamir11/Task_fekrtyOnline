package com.fekrety.fekretyonline;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PicassoClient {
    public static void dowloadImage(Context c, String url,de.hdodenhof.circleimageview.CircleImageView  img){
        if(url !=null &&url.length()>0)
        {
            Picasso.with(c).load(url).placeholder(R.drawable.ic_add_a_photo_black).into(img);
        }else{
            Picasso.with(c).load(R.drawable.ic_add_a_photo_black).into(img);
        }
    }
}