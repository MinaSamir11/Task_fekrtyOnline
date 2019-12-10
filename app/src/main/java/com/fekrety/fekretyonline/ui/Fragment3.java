package com.fekrety.fekretyonline.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.MaterialEditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.fekrety.fekretyonline.Model.item;
import com.fekrety.fekretyonline.JasonPlaceHolderApi;
import com.fekrety.fekretyonline.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;


public class Fragment3 extends Fragment {

    private MaterialEditText et_name, et_birthday, et_email, et_address, et_phone_number;
    private Button btn_sumbit;
    private TextInputEditText textInputPassword;
    private de.hdodenhof.circleimageview.CircleImageView circleImageView;
    private Bitmap bitmap;
    private String PathFile;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    // "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment3, container, false);
        circleImageView = view.findViewById(R.id.profile_image);
        et_name = view.findViewById(R.id.et_name_RG);
        et_birthday = view.findViewById(R.id.et_birthday_RG);
        et_email = view.findViewById(R.id.et_email_RG);
        textInputPassword = view.findViewById(R.id.et_password_RG);
        et_address = view.findViewById(R.id.et_address_RG);
        et_phone_number = view.findViewById(R.id.et_phone_RG);
        btn_sumbit = view.findViewById(R.id.bt_submit_RG);
        circleImageView.setImageBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.addphoto)));
        btn_sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if field empty before register or not
                if (et_name.getText().toString().isEmpty() || et_address.getText().toString().isEmpty() || et_birthday.getText().toString().isEmpty() ||
                        et_email.getText().toString().isEmpty() || textInputPassword.getText().toString().isEmpty() || et_phone_number.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Fill field required", Toast.LENGTH_SHORT).show();

                    //if statment to check which field  is being empty
                    if (et_name.getText().toString().isEmpty()) {
                        et_name.setError("Enter full name");
                    }
                    if (et_phone_number.getText().toString().isEmpty()) {
                        et_phone_number.setError("Enter your phone number");

                    }
                    if (textInputPassword.getText().toString().isEmpty()) {
                        textInputPassword.setError("Enter password");

                    }
                    if (et_email.getText().toString().isEmpty()) {
                        et_email.setError("Enter you Email");

                    }
                    if (et_address.getText().toString().isEmpty()) {
                        et_address.setError("Enter you address");
                    }
                    if (et_birthday.getText().toString().isEmpty()) {
                        et_birthday.setError("Enter your birthday");
                    }
                    if (!PASSWORD_PATTERN.matcher(textInputPassword.getText().toString()).matches()) {
                        textInputPassword.setError("Password too weak");
                    }
                } else {
                    MainActivityTab.viewPager.setCurrentItem(0);     // Navigate to home page after making validation of process registration
                    et_name.setText("");
                    et_birthday.setText("");
                    et_email.setText(null);
                    et_email.setError(null);
                    textInputPassword.setText("");
                    et_address.setText("");
                    et_phone_number.setText(null);
                    Toast.makeText(getActivity(), "Registerd successful", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //  GETitem();
        // createitem();
        // updateitem();
        init();
        return view;
    }


    private void selectImage() {
        final CharSequence[] options = {getString(R.string.Take_photo_item),    // option menu
                getString(R.string.Choose_from_Galelry_item),
                getString(R.string.Remove_item)
                , getString(R.string.cancel_item)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getString(R.string.add_photo_title));

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.Take_photo_item))) {  // request perminssions
                    int permission_write = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    int permission_read = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                    if (permission == PackageManager.PERMISSION_GRANTED && permission_write == PackageManager.PERMISSION_GRANTED
                            && permission_read == PackageManager.PERMISSION_GRANTED) {
                        IntentCamera();   //start activity of camera
                    } else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.
                                        CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                } else if (options[item].equals(getString(R.string.Choose_from_Galelry_item))) {    // request for permissions
                    int permission_write = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    int permission_read = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (permission_write == PackageManager.PERMISSION_GRANTED
                            && permission_read == PackageManager.PERMISSION_GRANTED) {
                        IntentGallery();   // start activity of gallery
                    } else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                } else if (options[item].equals(getString(R.string.cancel_item))) {
                    dialog.dismiss();
                } else if (options[item].equals(getString(R.string.Remove_item))) {
                    bitmap = null;
                    bitmap = drawableToBitmap(getResources().getDrawable(R.drawable.addphoto)); // set image to default photo
                    circleImageView.setImageBitmap(bitmap);
                }
            }
        });
        builder.show();
    }


    private void IntentGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, 2);
    }

    private void IntentCamera() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takepic.resolveActivity(getActivity().getPackageManager()) != null) {
            File photofile;
            photofile = createPhotoFile();
            if (photofile != null) {
                PathFile = photofile.getAbsolutePath();
                takepic.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photofile));
                startActivityForResult(takepic, 1);
            }
        }
       /* File output ;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"New Picture");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        output=new File(dir, "CameraContentDemo.jpg");
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        startActivityForResult(i, 1); */
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                try {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap =null;
                    bitmap = BitmapFactory.decodeFile(PathFile, bitmapOptions);


                    circleImageView.setImageBitmap(bitmap);
                }catch (Exception  e) {
                    e.printStackTrace();
                }
            }else if (requestCode == 2 ) {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    // h=1;
                    //imgui = selectedImage;
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor c = null;
                    if (selectedImage != null) {
                        c =getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                    }
                    if (c != null) {
                        c.moveToFirst();
                    }
                    int columnIndex = 0;
                    if (c != null) {
                        columnIndex = c.getColumnIndex(filePath[0]);
                    }

                    String picturePath = null;
                    if (c != null) {
                        picturePath = c.getString(columnIndex);
                    }

                    if (c != null) {
                        c.close();
                    }
                    bitmap = null;
                    bitmap= BitmapFactory.decodeFile(picturePath);


                    Log.w("path", picturePath + "");


                    circleImageView.setImageBitmap(bitmap);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private File createPhotoFile() {
        String name = "temp";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File Image;
        Image = new File(storageDir, name + ".jpg");
        return Image;
    }

    private void init() {
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_name.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_phone_number.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_address.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_email.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches()) {
                    et_email.setError("Enter a valid email address");
                }
            }
        });
        et_birthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_birthday.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        textInputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputPassword.setError(null);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        textInputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    if (!PASSWORD_PATTERN.matcher(textInputPassword.getText().toString()).matches()) {
                        textInputPassword.setError("Password too weak");
                    }
                }
            }
        });
        et_birthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date
                    // Create the DatePickerDialog instance
                    DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                            R.style.Theme_AppCompat, datePickerListener,
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH));
                    datePicker.setCancelable(false);
                    datePicker.setTitle("Select the date");
                    datePicker.show();
                }
            }
        });
    }

    // Listener
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            String year1 = String.valueOf(selectedYear);
            String month1 = String.valueOf(selectedMonth + 1);
            String day1 = String.valueOf(selectedDay);
            et_birthday.setText(day1 + "-" + month1 + "-" + year1);
        }
    };

    private void createitem() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/MinaSamir11/Test/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final item item = new item(1, "test", "test", "test", 4.2, "test");
        JasonPlaceHolderApi jasonPlaceHolderApi = retrofit.create(JasonPlaceHolderApi.class);

        Call<item> post = jasonPlaceHolderApi.createitem(item);
        post.enqueue(new Callback<com.fekrety.fekretyonline.Model.item>() {
            @Override
            public void onResponse(Call<item> call, Response<item> response) {
                if (response.isSuccessful()) {
                    item item1 = response.body();
                    Toast.makeText(getActivity(), item1.getName(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), response.code() + "", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<com.fekrety.fekretyonline.Model.item> call, Throwable t) {

            }
        });
    }

    private void updateitem() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://my-json-server.typicode.com/MinaSamir11/Test/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JasonPlaceHolderApi jasonPlaceHolderApi = retrofit.create(JasonPlaceHolderApi.class);
        final item item = new item(2, "test", "test", "test", 4.4, "test");

        Call<item> updateitem = jasonPlaceHolderApi.updateitem(2, item);
        updateitem.enqueue(new Callback<com.fekrety.fekretyonline.Model.item>() {
            @Override
            public void onResponse(Call<com.fekrety.fekretyonline.Model.item> call, Response<com.fekrety.fekretyonline.Model.item> response) {
                if (response.isSuccessful()) {
                    item item1 = response.body();
                    Toast.makeText(getActivity(), item1.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.fekrety.fekretyonline.Model.item> call, Throwable t) {

            }
        });
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
