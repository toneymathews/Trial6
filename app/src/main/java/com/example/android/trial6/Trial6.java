package com.example.android.trial6;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Trial6 extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial6);
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

        ListView listView = (ListView) findViewById(R.id.listView);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.android.trial6", Context.MODE_PRIVATE);

        set = sharedPreferences.getStringSet("notes", null); //getting all the notes from the array

        notes.clear();
        //retrieve notes from shared preferences if possible. if note, we add Example
        if(set != null){

            notes.addAll(set);
        }
        else{
            notes.add("Example");
            set = new HashSet<String>();
            set.addAll(notes);
            sharedPreferences.edit().putStringSet("notes",set).apply();
        }


        //link between the array and the list view is the array adapter
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);
        //connecting the list view to the adapter
        listView.setAdapter(arrayAdapter);
        //what happens on clicking an item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent i = new Intent(getApplicationContext(), EditYourNote.class);
                i.putExtra("noteId", position);
                startActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int position, long l) { //error if final is missing

                new AlertDialog.Builder(Trial6.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(position);

                                SharedPreferences sharedPreferences = Trial6.this.getSharedPreferences("com.example.android.trial6", Context.MODE_PRIVATE);

                                if(set == null){
                                    set = new HashSet<String>();

                                }
                                else {
                                    set.clear();

                                }
                                set.addAll(Trial6.notes);
                                sharedPreferences.edit().remove("notes").apply();
                                sharedPreferences.edit().putStringSet("notes",set).apply();
                                arrayAdapter.notifyDataSetChanged();


                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trial6, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {

            notes.add("");
            SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.android.trial6", Context.MODE_PRIVATE);

            if(set == null){
                set = new HashSet<String>();

            }
            else {
                set.clear();

            }
            set.addAll(Trial6.notes);
            sharedPreferences.edit().remove("notes").apply();
            sharedPreferences.edit().putStringSet("notes",set).apply();
            arrayAdapter.notifyDataSetChanged();

            Intent i = new Intent(getApplicationContext(), EditYourNote.class);
            i.putExtra("noteId", notes.size() -1);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
