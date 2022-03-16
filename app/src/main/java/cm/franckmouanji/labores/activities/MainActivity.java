package cm.franckmouanji.labores.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


import cm.franckmouanji.labores.R;
import cm.franckmouanji.labores.systeme.Controller;

public class MainActivity extends AppCompatActivity {

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Controller.haveStoragePermission(MainActivity.this);
                Controller.getPlage();
                if(verifConnexion()){

                    if(Controller.file_not_empty(MainActivity.this)){
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("connexion", "true");
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.putExtra("connexion", "true");
                        startActivity(intent);
                        finish();
                    }
                }else{

                    if(Controller.file_not_empty(MainActivity.this)){
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("connexion", "false");
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.putExtra("connexion", "false");
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, 2000);
    }


    private boolean verifConnexion(){
        String url ="https://www.google.com";


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.d("pass", response.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("error_connect", e.getMessage());
            return false;
        }
    }
}




