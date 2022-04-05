package com.example.chkforupdate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {
    FirebaseRemoteConfig remoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        int currentversioncode;
        currentversioncode = getcurrentVersioncode();
        Log.d("myApp", String.valueOf(currentversioncode));

        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);


        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    final String new_version_code = remoteConfig.getString("new_version_code");
                    if (Integer.parseInt(new_version_code) > getcurrentVersioncode()) {
                        showUpdateDialog();
                    }
                }
            }
        });
    }

    private void showUpdateDialog() {


        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New Update available")
                .setMessage("Update Now")
                .setPositiveButton("UPdate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/drive/u/0/folders/1j9FaBFz7l4Xa8kai3BQ1mItKZBIaCnZQ")));
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Something Went wrong Try again  ", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
        dialog.setCancelable(false);
    }

    private int getcurrentVersioncode() {

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

        } catch (Exception e) {

        }
        return packageInfo.versionCode;
    }
}