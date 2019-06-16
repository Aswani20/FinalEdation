package com.example.collegeassistant.authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.collegeassistant.DrawerActivity;
import com.example.collegeassistant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    //TODO:VIEWS RELATED THE ORIGINAL SIGNUP FORM ENABLE IF NEEDED
    private static final String TAG = "LoginActivity";

    @BindView(R.id.mail_address) EditText mailView;
    @BindView(R.id.log_password) EditText password;

    ProgressDialog progressDialog;

    String mail;
    String pass;

    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
    }

    public void toUp(View view){
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        this.finish();
    }

    public void login(View view){
        if(valide()){
            //---------------ProgressBar-----------------------------
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Logging In...");
            progressDialog.show();
            //-----------------Authentication-----------------------------------
            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                updateUI();
                                Log.e(TAG,"Logging in",task.getException());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            handleFail(e);
                        }
                    });
        }
    }

    private boolean valide(){
        boolean valide = true;
        mail = mailView.getText().toString();
        pass = password.getText().toString();

        if (mail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            mailView.setError("enter a valid email address");
            valide = false;
        } else {
            this.mailView.setError(null);
        }


        if (pass.isEmpty() || pass.length() < 4) {
            password.setError("between 4 and 10 alphanumeric characters");
            valide = false;
        } else {
            password.setError(null);
        }

        return valide;
    }

    private void updateUI(){
        progressDialog.dismiss();
            // Sign in success, update UI with the signed-in user's information
            Log.d(TAG, "signInWithEmail:success");
            mUser = mAuth.getCurrentUser();
            startActivity(new Intent(this, DrawerActivity.class));
            finish();
    }

    //handle logging failure
    private void handleFail(@NonNull Exception e){
        // If sign in fails, display a message to the user.
        progressDialog.dismiss();
        Toast.makeText(LoginActivity.this, e.getLocalizedMessage(),
                Toast.LENGTH_LONG).show();
        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(LoginActivity.this, "Invalid password",
                    Toast.LENGTH_LONG).show();
        } else if (e instanceof FirebaseAuthInvalidUserException) {

            String errorCode =
                    ((FirebaseAuthInvalidUserException) e).getErrorCode();

            if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                Toast.makeText(LoginActivity.this, "No matching account found",
                        Toast.LENGTH_LONG).show();
            } else if (errorCode.equals("ERROR_USER_DISABLED")) {
                Toast.makeText(LoginActivity.this, "User account has been disabled",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginActivity.this, e.getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }
    }


}
