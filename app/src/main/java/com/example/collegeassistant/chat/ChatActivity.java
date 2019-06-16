
package com.example.collegeassistant.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.bumptech.glide.Glide;



import com.example.collegeassistant.api.user_helper.UserHelper;
import com.example.collegeassistant.DrawerActivity;
import com.example.collegeassistant.models.Message;
import com.example.collegeassistant.api.message_helper.MessageHelper;
import com.example.collegeassistant.R;
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

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.reyclerview_message_list) RecyclerView mRecycler;
    @BindView(R.id.departmentButton)ImageButton  departmentButton;
    @BindView(R.id.messageEditText) EditText mMessageEditText;
    @BindView(R.id.sendButton) Button mSendButton;
    @BindView(R.id.apply) Button apply;
    @BindView(R.id.departmentChat) Spinner departmentChat;
    @BindView(R.id.yearChat) Spinner yearChat;
    @BindView(R.id.section) LinearLayout section;
    @BindView(R.id.layout_chatbox) LinearLayout chatBox;



    private MessageAdapter mAdapter;

    private final String TAG = "ChatActivity";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;



    FirebaseAuth mAuth;
    FirebaseUser mUser;
    User user;
    ProgressDialog progressDialog;

    private String year;
    private String department;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        //---------------ProgressBar-----------------------------
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        setObserver();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        getUser();

        setConfigOptions();//configures the layout onStartUp

        // ImagePickerButton shows an image picker to upload a image for a message
        departmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setConfigOptions();//configures the layout onStartUp
            }
        });

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MessageHelper.createMessageForChat(user,  mMessageEditText.getText().toString(),year ,department)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG,e.getLocalizedMessage());
                            }
                        });
                // Clear input box
                mMessageEditText.setText("");
            }
        });
    }//------------------------------------end onCreat



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


    private void configureRecyclerView(String year, String department){


        this.mAdapter = new MessageAdapter(generateOptionsForAdapter(MessageHelper.getAllMessageForChat(year,department)), Glide.with(this), this.mUser.getUid());
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mRecycler.smoothScrollToPosition(mAdapter.getItemCount()); // Scroll to bottom on new messages
            }
        });
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(this.mAdapter);
    }

    //Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)//controls the listening process
                .build();
    }


    private void getUser(){
        user = (User) getIntent().getSerializableExtra("User");

        if(user == null){
            final ProgressDialog progressDialog =new ProgressDialog(ChatActivity.this, R.style.AppTheme_Dark_Dialog);
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
                    Toast.makeText(ChatActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        //your code when back button pressed

        Intent intent = new Intent(ChatActivity.this, DrawerActivity.class);
        intent.putExtra("User",user);
        startActivity(intent);

        ChatActivity.this.finish();
    }


    //-------------------------popup config-----------------------------
    //spinner population method
    private void populateSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.year, android.R.layout.simple_spinner_item);        //  Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        yearChat.setAdapter(adapter);
        yearChat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        departmentChat.setAdapter(adapter);
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
        departmentChat.setAdapter(adapter);
        departmentChat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            chatBox.setVisibility(View.GONE);
            section.setVisibility(View.VISIBLE);

            populateSpinner();

            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (valid()) {

                        mRecycler.setVisibility(View.VISIBLE);
                        chatBox.setVisibility(View.VISIBLE);
                        departmentButton.setVisibility(View.VISIBLE);
                        section.setVisibility(View.GONE);

                        applyConfig();
                    }
                }
            });
        }else{//in case the user is student

            year = user.getEduYear();
            department = user.getDepartment();

            mRecycler.setVisibility(View.VISIBLE);
            chatBox.setVisibility(View.VISIBLE);
            section.setVisibility(View.GONE);
            departmentButton.setVisibility(View.GONE);

            applyConfig();
        }
    }

    private void applyConfig(){
        try {
            //RecyclerView
            configureRecyclerView(year, department);
        } catch (NullPointerException e) {
            MessageHelper.createMessageForChat(user, "", year, department)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.getLocalizedMessage());
                        }
                    });

            //after creating an empty message
            //RecyclerView
            configureRecyclerView(year, department);

        }
    }


}