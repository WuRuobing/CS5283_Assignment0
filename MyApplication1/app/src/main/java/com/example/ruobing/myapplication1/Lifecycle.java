package com.example.ruobing.myapplication1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Lifecycle extends AppCompatActivity {
    public static final String TAG = Lifecycle.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_lifecycle);
        Toast.makeText(Lifecycle.this, "onCreate", Toast.LENGTH_SHORT).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lifecycle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
        Toast.makeText(Lifecycle.this, "onStart", Toast.LENGTH_SHORT).show();
    }

    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
        Toast.makeText(Lifecycle.this, "onResume", Toast.LENGTH_SHORT).show();
    }

    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
        Toast.makeText(Lifecycle.this, "onPause", Toast.LENGTH_SHORT).show();
    }

    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop");
        Toast.makeText(Lifecycle.this, "onStop", Toast.LENGTH_SHORT).show();
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        Toast.makeText(Lifecycle.this, "onDestroy", Toast.LENGTH_SHORT).show();
    }

    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart");
        Toast.makeText(Lifecycle.this, "onRestart", Toast.LENGTH_SHORT).show();
    }
}