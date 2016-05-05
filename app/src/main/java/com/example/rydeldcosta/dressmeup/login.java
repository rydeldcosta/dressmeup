package com.example.rydeldcosta.dressmeup;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class login extends AppCompatActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        String fontpath = "Fonts/makefunofmeDEMO.otf";
        TextView name1 = (TextView) findViewById(R.id.wtwt);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontpath);
        name1.setTypeface(tf);
        setupLogin();
    }

    private void setupLogin() {
        LoginButton lg  = (LoginButton) findViewById(R.id.login_button);

        lg.setReadPermissions("public_profile","email");
        lg.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();



                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                    String name = object.getString("name"), id = object.getString("id");
                                    Intent i  = new Intent(login.this, MainActivity.class);
                                    Bundle bundle = new Bundle();
                                   // bundle.putParcelable("userobject", object);
                                   // bundle.putSerializable();
                                    bundle.putString("name", name);
                                    bundle.putString("userid",id);
                                    i.putExtra("BUNDLE",bundle);
                                    startActivity(i);
                                } catch(JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                System.out.print("Oncancel");

            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(),"Check you internet connection",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
