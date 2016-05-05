package com.example.rydeldcosta.dressmeup;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private int PICK_IMAGE=1;
    Bitmap returndecoded;
    CallbackManager callbackManager;
    Uri glob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        //System.out.println("Oncreate");
        setupDetails();



    }

    private void setupDetails() {
        TextView t = (TextView) findViewById(R.id.profile_name);
        Intent i = getIntent();
        Bundle b = i.getBundleExtra("BUNDLE");
        String id = b.getString("id");
        String name = b.getString("name");
        Profile p = Profile.getCurrentProfile();
        t.setText("Hi, "+ name + " Your id is : " + p.getId());
    }

    public void gallery(View v)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }
    public  void takescreenshot(View v)
    {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "test" + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            shareScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }

    }

    private void shareScreenshot(File imageFile) {
        Intent whatsappIntent = new Intent();
        whatsappIntent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        whatsappIntent.setType("image/*");
        whatsappIntent.setPackage("com.whatsapp");
        //whatsappIntent.putExtra(Intent.EXTRA_TEXT,"Check this out!");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM,uri);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(),"Whatsapp not installed",Toast.LENGTH_LONG).show();

        }
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, Base64.URL_SAFE);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
    public int setScaleFactor(int width, int height) {
        if (width > 3500 || height > 3500) return 9;
        if (width >= 3000 || height >= 3000) return 8;
        if (width >= 2500 || height >= 2500) return 7;
        if (width >= 2000 || height >= 2000) return 6;
        if (width >= 1500 || height >= 1500) return 4;
        if (width >= 1000 || height >= 1000) return 3;
        if (width >= 500 || height >= 500) return 2;
        return 1;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String encoded = "";
            Uri PhotoUri = data.getData();          //store this data as a key to each uri
            glob = data.getData();
            try {
                InputStream is = getContentResolver().openInputStream(PhotoUri);
                Bitmap yourBitmap = BitmapFactory.decodeStream(is);

                int scale = setScaleFactor(yourBitmap.getWidth(), yourBitmap.getHeight());

                final Bitmap resized = Bitmap.createScaledBitmap(yourBitmap, (yourBitmap.getWidth() / scale), (yourBitmap.getHeight() / scale), true);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                resized.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encoded = Base64.encodeToString(byteArray, Base64.URL_SAFE);
                System.out.println("Encoded the string");

                ImageView img = (ImageView) findViewById(R.id.display_pic);
                img.setImageBitmap(resized);
                            //headerImageView.setImageBitmap(resized1);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);



    }



}
