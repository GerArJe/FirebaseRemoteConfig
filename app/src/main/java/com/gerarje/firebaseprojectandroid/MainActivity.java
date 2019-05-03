package com.gerarje.firebaseprojectandroid;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {

    private FirebaseRemoteConfig firebaseRemoteConfig;

    private LinearLayout linearLayout;
    private ImageView imvLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = findViewById(R.id.activity_main);
        imvLogo = findViewById(R.id.imvLogo);

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings
                = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();

        firebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings);
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        setConfigurationView();

    }

    private void setConfigurationView(){
        linearLayout.setBackgroundColor(Color.parseColor(firebaseRemoteConfig.getString("color_background")));
        int imageResource= getResources().getIdentifier(firebaseRemoteConfig.getString("image_background"), "drawable", getPackageName());
        imvLogo.setImageResource(imageResource);
    }

    public void syncronizeData(View view){
        long cacheExpiration = 3600;

        if (firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
            cacheExpiration = 0;
        }

        firebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Syncronize Done", Toast.LENGTH_SHORT).show();
                    firebaseRemoteConfig.activateFetched();
                }else {
                    Toast.makeText(MainActivity.this, "Syncronize Fail", Toast.LENGTH_SHORT).show();
                }

                setConfigurationView();
            }
        });
    }
}
