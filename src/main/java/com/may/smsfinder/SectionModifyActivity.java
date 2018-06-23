package com.may.smsfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.may.smsfinder.data.DataHelper;
import com.may.smsfinder.model.Section;

public class SectionModifyActivity extends AppCompatActivity {
    private EditText sectionNameEditText;
    private EditText patternNameEditText;
    private Button modifyButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_modify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sectionNameEditText = findViewById(R.id.sectionName);
        patternNameEditText = findViewById(R.id.sectionPattern);
        modifyButton = findViewById(R.id.modifyBtn);
        cancelButton = findViewById(R.id.cancelBtn);

        Intent intent = getIntent();
        final String sectionName = intent.getStringExtra("sectionName");
        Section section = DataHelper.getInstance().selectSection(sectionName);
        sectionNameEditText.setText(section.getName());
        patternNameEditText.setText(section.getPattern());

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newSectionName = sectionNameEditText.getText().toString().trim();
                if (alreadyExistsSameName(sectionName, newSectionName)) {
                    Toast.makeText(getApplicationContext(), "동일한 분류이름이 이미 존재합니다.\n다른 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String newPattern = patternNameEditText.getText().toString().trim();
                DataHelper.getInstance().updateSection(sectionName, newSectionName, newPattern);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean alreadyExistsSameName(String sectionName, String newSectionName) {
        if (sectionName.equals(newSectionName)) {
            return false;
        }

        DataHelper dataHelper = DataHelper.getInstance();
        Section section = dataHelper.selectSection(newSectionName);

        return section != null;
    }

}
