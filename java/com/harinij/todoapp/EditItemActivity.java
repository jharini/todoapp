package com.harinij.todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        final Intent intent = getIntent();
        String itemValue = intent.getExtras().getString(ToDoActivity.ITEM_VALUE);
        final EditText etEditItem = (EditText) findViewById(R.id.etEditItem);
        etEditItem.setText(itemValue);
        etEditItem.requestFocus();
        Button saveBtn = (Button) findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String newItemValue = ((EditText)findViewById(R.id.etEditItem)).getText().toString();
                if(newItemValue.length()==0){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditItemActivity.this);
                    dialog.setMessage("The entered string is empty. Are you sure you want to save an empty to-do item?");
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialogYes, int arg1){
                           Intent newIntent = new Intent();
                           newIntent.putExtra(ToDoActivity.ITEM_VALUE,newItemValue);
                           newIntent.putExtra(ToDoActivity.ITEM_POSITION,intent.getExtras().getInt(ToDoActivity.ITEM_POSITION));
                           setResult(RESULT_OK,newIntent);
                           EditItemActivity.this.finish();
                       }
                    });
                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogNo, int i) {
                            etEditItem.requestFocus();
                            dialogNo.cancel();
                        }
                    });
                    AlertDialog dialogBox = dialog.create();
                    dialogBox.show();
                }
                else {
                    Intent newIntent = new Intent();
                    newIntent.putExtra(ToDoActivity.ITEM_VALUE, newItemValue);
                    newIntent.putExtra(ToDoActivity.ITEM_POSITION, intent.getExtras().getInt(ToDoActivity.ITEM_POSITION));
                    setResult(RESULT_OK, newIntent);
                    finish();
                }
            }
        });
    }
}
