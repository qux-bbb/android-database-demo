package com.neo.databasedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText whatEditText;

    Button submitaddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        nameEditText = (EditText)findViewById(R.id.name_editText);
        whatEditText = (EditText) findViewById(R.id.what_editText);
        submitaddButton = (Button) findViewById(R.id.submitadd_button);

        submitaddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String what = whatEditText.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("name",name);
                intent.putExtra("what",what);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }
}
