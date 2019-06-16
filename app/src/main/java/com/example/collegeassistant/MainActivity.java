package com.example.collegeassistant;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.collegeassistant.api.user_helper.UserHelper;
import com.example.collegeassistant.authentication.LoginActivity;
import com.example.collegeassistant.DrawerActivity;
import com.example.collegeassistant.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;


    private FirebaseUser mUser;

    ProgressBar bar;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        bar = findViewById(R.id.progress_bar);
        bar.setVisibility(View.VISIBLE);



        mUser = FirebaseAuth.getInstance().getCurrentUser();

        getUserOBJ();

        /*new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity.
                if(mUser!=null){
                    Intent mainIntent = new Intent(MainActivity.this, HomeActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }else{
                    Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);*/


    }

    private void getUserOBJ(){
        if(mUser != null){
            UserHelper.getUser(mUser.getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        user = task.getResult().toObject(User.class);
                        Intent mainIntent = new Intent(MainActivity.this, DrawerActivity.class);
                        mainIntent.putExtra("User",user);
                        MainActivity.this.startActivity(mainIntent);
                        MainActivity.this.finish();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(mainIntent);
            MainActivity.this.finish();
        }
    }

}
