package com.example.rydeldcosta.dressmeup;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private int PICK_IMAGE=1;
    Bitmap returndecoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void gallery(View v)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

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
            Uri PhotoUri = data.getData();
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
