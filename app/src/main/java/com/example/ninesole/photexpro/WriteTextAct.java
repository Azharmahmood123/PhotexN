package com.example.ninesole.photexpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WriteTextAct extends AppCompatActivity {

    public static String MAIN_TEXT = "maintext";
    Button bDone, bCancel;
    EditText etMain;
    Context context;
    SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_text);
        bDone = (Button) findViewById(R.id.b_done);
        bCancel = (Button) findViewById(R.id.b_cancel);
        etMain = (EditText) findViewById(R.id.et_main);
        context = WriteTextAct.this;

        bDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed = getSharedPreferences("my", MODE_PRIVATE).edit();
                if (etMain.getText().toString().length() > 0) {
                    Intent canvas = new Intent(context, MainCanvas.class);
                    canvas.putExtra(MAIN_TEXT, etMain.getText().toString());
                    ed.putString(MAIN_TEXT, etMain.getText().toString());
                    ed.commit();
                    canvas.putExtra("add_text", 1);
//                    startActivity(canvas);
                    finish();

                } else {
                    Toast.makeText(context, "Please write something", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
