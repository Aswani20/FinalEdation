package com.example.collegeassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

public class studentGuide extends AppCompatActivity {
    PDFView studentG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_guide);
        studentG=(PDFView) findViewById(R.id.pdfstudentG);
        studentG.fromAsset("Student-Guide.pdf").load();
    }
}

