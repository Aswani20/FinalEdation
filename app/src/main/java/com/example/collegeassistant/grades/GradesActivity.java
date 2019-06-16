package com.example.collegeassistant.grades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.collegeassistant.R;

public class GradesActivity extends AppCompatActivity {
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        this.populateSpinner();
    }

    //spinner data added
    //TODO: modify this function to get the spinner data from the DB
    protected void populateSpinner(){
        //spinner modification
        spinner = findViewById(R.id.subject);
        /* Create an ArrayAdapter using the string array and a default spinner layout
           -->ArrayAdapter<CharSequence> adapter
              *creating ArrayAdapter object*
           -->android.R.layout.simple_spinner_item
              *defines spinner layout*
         */
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Subject, android.R.layout.simple_spinner_item);

        //  Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }
}
