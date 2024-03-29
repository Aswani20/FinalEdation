package com.example.collegeassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class qrattendance extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA=1;
    private ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                Toast.makeText(qrattendance.this, "Permission is granted",Toast.LENGTH_LONG).show();
            }
            else{
                requestPermission();
            }
        }
    }



    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(qrattendance.this, CAMERA)== PackageManager.PERMISSION_GRANTED);
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permission[], @NonNull int grantResults[])
    {
        switch (requestCode)
        {
            case REQUEST_CAMERA:
                if (grantResults.length>0){
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(qrattendance.this,"Permission Granted",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(qrattendance.this,"Permission Denied",Toast.LENGTH_LONG).show();
                        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
                            if (shouldShowRequestPermissionRationale(CAMERA)){
                                displyAlterMessage("you need to allow for both permission",
                                        new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                requestPermissions(new String[]{CAMERA},REQUEST_CAMERA);
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
                break;
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission())
            {
                if(scannerView == null){
                    scannerView =new ZXingScannerView(this);
                    setContentView(scannerView);

                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else {
                requestPermission();

            }
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        scannerView.startCamera();
    }

    public void displyAlterMessage(String message, DialogInterface.OnCancelListener listener){
        new AlertDialog.Builder(qrattendance.this)
                .setMessage(message)
                .setPositiveButton("OK", (DialogInterface.OnClickListener) listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();


    }
    @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText();
        android.support.v7.app.AlertDialog.Builder builder= new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(qrattendance.this);
            }
        });
        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse(scanResult));
                startActivity(intent);
            }
        });
        builder.setMessage(scanResult);
        android.support.v7.app.AlertDialog alert=builder.create();
        alert.show();


    }
}
