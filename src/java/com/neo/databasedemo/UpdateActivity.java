package com.neo.databasedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateActivity extends AppCompatActivity {

    EditText unameEditText;
    EditText uwhatEditText;
    Button submitUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        final String id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String what = getIntent().getStringExtra("what");

        unameEditText = (EditText) findViewById(R.id.uname_editText);
        uwhatEditText = (EditText) findViewById(R.id.uwhat_editText);

        unameEditText.setText(name);
        uwhatEditText.setText(what);
        submitUpButton = (Button) findViewById(R.id.submitup_button);



        submitUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = unameEditText.getText().toString();
                String what = uwhatEditText.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                intent.putExtra("what",what);

                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }
}
