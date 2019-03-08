package com.example.androidlabs;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.support.design.widget.Snackbar;

public class TestToolbar extends AppCompatActivity {
    String initMessage, goBack, yes, clkOverflow, send, cancel, noChgsMade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tool_bar);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        initMessage = getString(R.string.initialMessage);
        goBack = getString(R.string.goback);
        yes = getString(R.string.yes);
        clkOverflow = getString(R.string.clickedonoverflow);
        send = getString(R.string.send);
        cancel = getString(R.string.cancel);
        noChgsMade = getString(R.string.nochanges);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.item1:
                Toast.makeText(this, initMessage, Toast.LENGTH_SHORT).show();
                break;
            case R.id.item2:
                alertDialogue();
                break;
            case R.id.item3:
                Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
                Snackbar sb = Snackbar.make(tBar, goBack, Snackbar.LENGTH_LONG)
                        .setAction(yes, e -> finish());
                sb.show();
                break;
            case R.id.item4:
                Toast.makeText(this, clkOverflow, Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    public void alertDialogue()
    {
        View dialogueView = getLayoutInflater().inflate(R.layout.alert_dialogue, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.activity_chat_room);
        builder.setPositiveButton(send, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        EditText et = dialogueView.findViewById(R.id.view_edit_text);
                        String stringToDisplay = et.getText().toString();
                        ActionMenuItemView item2 = findViewById(R.id.item1);
                        item2.setOnClickListener(e->{
                            Toast.makeText(TestToolbar.this, stringToDisplay, Toast.LENGTH_SHORT).show();
                        }
                       );

                    }
                })
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(TestToolbar.this, noChgsMade, Toast.LENGTH_SHORT).show();
                    }
                }).setView(dialogueView);

        builder.create().show();

    }


}
