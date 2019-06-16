package com.example.collegeassistant.announcement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.collegeassistant.R;
import com.example.collegeassistant.api.announcement_helper.AnnouncementHelper;
import com.example.collegeassistant.api.user_helper.UserHelper;
import com.example.collegeassistant.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnnouncementSenderActivity extends AppCompatActivity {

    //--------------------popup activity------------------------------
    @BindView(R.id.year_id) Spinner yearID;
    @BindView(R.id.department_id) Spinner departmentID;
    @BindView(R.id.announcement_message) EditText announcementMessage;
    @BindView(R.id.announcement_sender) Button sender;
    @BindView(R.id.announcement_dismiss) Button cancel;


    private String year, department;
    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_sender);
        ButterKnife.bind(this);
        populateSpinner();


        getUser();

        //-----------------in popup button config---------------------------------
        sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = announcementMessage.getText().toString();
                if(!message.isEmpty()&& valid()){
                    //execute database set
                    AnnouncementHelper.createAnnouncement(user, message, year, department);
                    announcementMessage.setText("");
                    Intent intent = new Intent(AnnouncementSenderActivity.this,AnnouncementActivity.class);
                    intent.putExtra("User",user);
                    startActivity(intent);

                    AnnouncementSenderActivity.this.finish();
                }else{
                    announcementMessage.setError("This field can't be empty!");
                }
            }
        });

        //-------------------in popup cancel--------------------------------------
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AnnouncementSenderActivity.this,AnnouncementActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);

                AnnouncementSenderActivity.this.finish();

            }
        });
    }

    //-------------------------popup config-----------------------------
    //spinner population method
    private void populateSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.year, android.R.layout.simple_spinner_item);        //  Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        yearID.setAdapter(adapter);
        yearID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = (String)adapterView.getItemAtPosition(i);
                populateSpinner2(year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void populateSpinner2(String year){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sectionC, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        departmentID.setAdapter(adapter);
        switch (year){
            case "First Year":
                populateOnSelection(R.array.first);
                break;

            case "Second Year":
                populateOnSelection(R.array.second);
                break;

            case "Third Year":
                populateOnSelection(R.array.third);
                break;

            case "Fourth Year":
                populateOnSelection(R.array.third);
                break;

            default:
                break;
        }
    }

    private void populateOnSelection(int ID){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                ID, android.R.layout.simple_spinner_item);
        //  Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        departmentID.setAdapter(adapter);
        departmentID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                department = (String)adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    //---------------------------popup config end-------------------------------------------------

    private boolean valid() {
        boolean valid = true;
        //todo:--------------->removed view error checking disabled
        if (year.equals("Select Enrollment year!")) {
            Toast.makeText(this, "Select Enrollment year!", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (department.equals("Select department!")) {
            Toast.makeText(this, "Select department!", Toast.LENGTH_LONG).show();
            valid = false;
        }

        return valid;

    }//end  valid

    private void getUser(){
        user = (User) getIntent().getSerializableExtra("User");

        if(user == null){
            final ProgressDialog progressDialog =new ProgressDialog(AnnouncementSenderActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

            UserHelper.getUser(mUser.getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        user = task.getResult().toObject(User.class);
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AnnouncementSenderActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        //your code when back button pressed

        Intent intent = new Intent(AnnouncementSenderActivity.this,AnnouncementActivity.class);
        intent.putExtra("User",user);
        startActivity(intent);

        AnnouncementSenderActivity.this.finish();
    }

}
