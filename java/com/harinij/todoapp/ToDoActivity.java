package com.harinij.todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ToDoActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    private static final int EDIT_ITEM_REQUEST = 100;
    public static final String ITEM_VALUE = "item_value";
    public static final String ITEM_POSITION = "item_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v){
        final String newItem = ((EditText) findViewById(R.id.etNewItem)).getText().toString();
        if(newItem.length()==0){
            AlertDialog.Builder dialog = new AlertDialog.Builder(ToDoActivity.this);
            dialog.setMessage("The entered string is empty. Are you sure you want to save an empty to-do item?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogYes, int arg1){
                    itemsAdapter.add(newItem);
                    ((EditText) findViewById(R.id.etNewItem)).setText("");
                    writeItems();
                    return;
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogNo, int i) {
                    (findViewById(R.id.etNewItem)).requestFocus();
                    dialogNo.cancel();
                }
            });
            AlertDialog dialogBox = dialog.create();
            dialogBox.show();
        }
        else {
            itemsAdapter.add(newItem);
            ((EditText) findViewById(R.id.etNewItem)).setText("");
            writeItems();
        }
    }

    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id){
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id){
                Intent intent = new Intent(ToDoActivity.this,EditItemActivity.class);
                intent.putExtra(ITEM_VALUE,items.get(pos).toString());
                intent.putExtra(ITEM_POSITION,pos);
                startActivityForResult(intent,EDIT_ITEM_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(resultCode==RESULT_OK && requestCode==EDIT_ITEM_REQUEST){
            String itemValue = intent.getExtras().getString(ITEM_VALUE,"");
            int position = intent.getExtras().getInt(ITEM_POSITION);
            items.remove(position);
            items.add(position,itemValue);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e){
            items = new ArrayList<String>();
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile,items);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
