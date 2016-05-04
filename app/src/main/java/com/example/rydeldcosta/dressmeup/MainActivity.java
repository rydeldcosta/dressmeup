package com.example.rydeldcosta.dressmeup;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class MainActivity extends AppCompatActivity {

    private int PICK_IMAGE=1;
    Bitmap returndecoded;
    CallbackManager callbackManager;
    Uri glob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        System.out.println("Oncreate");
        LoginButton lg  = (LoginButton) findViewById(R.id.login_button);

        lg.setReadPermissions("public_profile","email");
        lg.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                /*Profile p = Profile.getCurrentProfile();
                if(p!=null) {
                    System.out.print("Onsuccess");
                    TextView t = (TextView) findViewById(R.id.profile_name);
                    System.out.print(p.getName());
                    t.setText("Welcome" + p.getName());
                }
                else
                    System.out.println("Null profile");*/


            }

            @Override
            public void onCancel() {
                System.out.print("Oncancel");

            }

            @Override
            public void onError(FacebookException e) {
                System.out.print("Onerror");
                TextView t = (TextView)findViewById(R.id.profile_name);
                t.setText("error");
            }
        });


    }
    public void gallery(View v)
    {
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);*/
        Profile p = Profile.getCurrentProfile();
        if(p!=null) {
            System.out.print("Onsuccess");
            TextView t = (TextView) findViewById(R.id.profile_name);
            System.out.print(p.getName());
            t.setText("Welcome" + p.getName());
        }
        else
            System.out.println("Null profile3");

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
        /*if (requestCode == 1 && resultCode == RESULT_OK) {
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
        else*/
        super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        Profile p = Profile.getCurrentProfile();
        if(p!=null) {
            System.out.print("Onsuccess");
            TextView t = (TextView) findViewById(R.id.profile_name);
            System.out.print(p.getName());
            t.setText("Welcome" + p.getName());
        }
        else
            System.out.println("Null profile2");
        System.out.println("OnActRes");

    }
    public void sendWA(View v)
    {
        ImageView img= (ImageView) findViewById(R.id.display_pic);
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        Uri uri=glob;
        whatsappIntent.setType("image/*");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM,uri);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {

        }
    }


}
