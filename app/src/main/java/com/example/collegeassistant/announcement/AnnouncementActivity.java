package com.example.collegeassistant.announcement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.collegeassistant.R;
import com.example.collegeassistant.api.announcement_helper.AnnouncementHelper;
import com.example.collegeassistant.api.user_helper.UserHelper;
import com.example.collegeassistant.DrawerActivity;
import com.example.collegeassistant.models.Announcement;
import com.example.collegeassistant.models.User;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnnouncementActivity extends AppCompatActivity {
    //-------------------main announcement activity------------------
    @BindView(R.id.recylerview_announcement) RecyclerView mRecycler;
    @BindView(R.id.sendButton) Button toSend;
    @BindView(R.id.department_id_button) Button id;
    @BindView(R.id.applyAnnouncement) Button apply;
    @BindView(R.id.departmentAnnouncement) Spinner departmentA;
    @BindView(R.id.yearAnnouncement) Spinner yearA;
    @BindView(R.id.sectionA) LinearLayout sectionA;
    @BindView(R.id.layout_A) LinearLayout layoutA;



    private AnnouncementAdapter mAdapter;
    private User user;

    private FirebaseUser mUser;



    private ProgressDialog progressDialog;
    private static final String TAG = "Announcement Actvity";

    private String year;
    private String department;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        ButterKnife.bind(this);


        mUser = FirebaseAuth.getInstance().getCurrentUser();

        getUser();

        setObserver();




        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        //---------------ProgressBar-----------------------------
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        setConfigOptions();//configures the data path


        id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setConfigOptions();//configures the data path
            }
        });

        //------------------popup activator----------------------------
        toSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnnouncementActivity.this, AnnouncementSenderActivity.class);
                intent.putExtra("User",user);
                startActivity(intent);
            }
        });
    }




    //observes the population of the recycler view
    private void setObserver(){
        mRecycler.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //At this point the layout is complete and the
                        //dimensions of recyclerView and any child views are known.
                        //Remove listener after changed RecyclerView's height to prevent infinite loop
                        progressDialog.dismiss();
                        mRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

   private void configureRecyclerView(){
        //Configure Adapter & RecyclerView
        //todo:update announcement DB path

        this.mAdapter = new AnnouncementAdapter(generateOptionsForAdapter(AnnouncementHelper.getAllAnnouncement(year,department)));
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecycler.smoothScrollToPosition(mAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // it will reverse the stack (bottom to top)
        layoutManager.setReverseLayout(true); // When set to true, If RecyclerView is LTR, than it will layout from RTL
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(this.mAdapter);
    }

    //Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Announcement> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Announcement>()
                .setQuery(query, Announcement.class)
                .setLifecycleOwner(this)//controls the listening process
                .build();
    }

    private void getUser(){
        user = (User) getIntent().getSerializableExtra("User");

        if(user == null){
            final ProgressDialog progressDialog =new ProgressDialog(AnnouncementActivity.this, R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

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
                    Toast.makeText(AnnouncementActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        //your code when back button pressed

        Intent intent = new Intent(AnnouncementActivity.this, DrawerActivity.class);
        intent.putExtra("User",user);
        startActivity(intent);

        AnnouncementActivity.this.finish();
    }


    //-------------------------popup config-----------------------------
    //spinner population method
    private void populateSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.year, android.R.layout.simple_spinner_item);        //  Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        yearA.setAdapter(adapter);
        yearA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        departmentA.setAdapter(adapter);
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
        departmentA.setAdapter(adapter);
        departmentA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void setConfigOptions() {
        if (user.getIsProfessor()) {//if user is professor show filter
            mRecycler.setVisibility(View.GONE);
            layoutA.setVisibility(View.GONE);
            sectionA.setVisibility(View.VISIBLE);

            populateSpinner();

            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (valid()) {

                        mRecycler.setVisibility(View.VISIBLE);
                        layoutA.setVisibility(View.VISIBLE);
                        sectionA.setVisibility(View.GONE);

                        applyConfig();
                    }
                }
            });
        }else{//in case the user is student

            year = user.getEduYear();
            department = user.getDepartment();

            mRecycler.setVisibility(View.VISIBLE);
            layoutA.setVisibility(View.GONE);
            sectionA.setVisibility(View.GONE);


            applyConfig();
        }
    }

    private void applyConfig(){
        try{
            //RecyclerView
            this.configureRecyclerView();
        }catch(NullPointerException e){
            AnnouncementHelper.createAnnouncement(user,  "",year,department)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG,e.getLocalizedMessage());
                        }
                    });

            //after creating an empty message
            //RecyclerView
            this.configureRecyclerView();

        }
    }





}
