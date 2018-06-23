package com.may.smsfinder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.may.smsfinder.data.DataHelper;
import com.may.smsfinder.model.Section;


public class InsertSectionActivity extends AppCompatActivity {
    private EditText edtSectionName;
    private EditText edtSectionPattern;
    private Button okButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_section);

        edtSectionName = findViewById(R.id.sectionName);
        edtSectionPattern = findViewById(R.id.sectionPattern);
        okButton = findViewById(R.id.btnOk);
        cancelButton = findViewById(R.id.btnCancel);

        okButton.setOnClickListener(view -> {
            String sectionName = edtSectionName.getText().toString().trim();
            if (sectionName.length() == 0) {
                Toast.makeText(getApplicationContext(), "분류명을 입력해주세요.", Toast.LENGTH_SHORT);
                return;
            }

            String patternName = edtSectionPattern.getText().toString();
            if (patternName.length() == 0) {
                Toast.makeText(getApplicationContext(), "패턴을 입력해주세요.", Toast.LENGTH_SHORT);
                return;
            }

            Section newSection = new Section();
            newSection.setName(sectionName);
            newSection.setPattern(patternName.trim());

            DataHelper.getInstance().insertSection(newSection);
            finish();
        });

        cancelButton.setOnClickListener(v -> finish());

    }

}
