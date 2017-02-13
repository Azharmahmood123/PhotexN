package com.example.ninesole.photexpro;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.keyboard:
                Toast.makeText(context, "Open KeyBoard", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sticker:
                Toast.makeText(context, "Sticker Dialog", Toast.LENGTH_SHORT).show();
                break;
            case R.id.background:
                Toast.makeText(context, "Backgroung Dialog", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;


    }
}
