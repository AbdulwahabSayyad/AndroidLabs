package com.example.androidlabs;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    public ArrayList<Message> chatContainer;
    Button sendButton;
    Button receiveButton;
    EditText typedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        sendButton = findViewById(R.id.sendButton);
        receiveButton = findViewById(R.id.receiveButton);
        typedMessage = findViewById(R.id.textArea);
        chatContainer = new ArrayList<>();
        MyListAdapter adapter = new MyListAdapter(chatContainer);
        ListView chatLog = findViewById(R.id.chatList);

        MyDatabaseOpener dbOpener = new MyDatabaseOpener(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        String [] columns = {MyDatabaseOpener.COL_ID, MyDatabaseOpener.COL_MESSAGE, MyDatabaseOpener.COL_TYPE};
        Cursor results = db.query(false, MyDatabaseOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int messageColIndex = results.getColumnIndex(MyDatabaseOpener.COL_MESSAGE);
        int typeColIndex = results.getColumnIndex(MyDatabaseOpener.COL_TYPE);
        int idColIndex = results.getColumnIndex(MyDatabaseOpener.COL_ID);

        while(results.moveToNext())
        {
            String message = results.getString(messageColIndex);
            String type = results.getString(typeColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            chatContainer.add(new Message(message, type, id));
        }

        printCursor(results);

        sendButton.setOnClickListener(e -> {
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyDatabaseOpener.COL_MESSAGE, typedMessage.getText().toString());
            newRowValues.put(MyDatabaseOpener.COL_TYPE, "Send");
            long newId = db.insert(MyDatabaseOpener.TABLE_NAME, null, newRowValues);
                    chatContainer.add(new Message(typedMessage.getText().toString(),"Send", newId));
                    typedMessage.getText().clear();
                    adapter.notifyDataSetChanged();
                });

        receiveButton.setOnClickListener(e -> {
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyDatabaseOpener.COL_MESSAGE, typedMessage.getText().toString());
            newRowValues.put(MyDatabaseOpener.COL_TYPE, "Receive");
            long newId = db.insert(MyDatabaseOpener.TABLE_NAME, null, newRowValues);
                    chatContainer.add(new Message(typedMessage.getText().toString(),"Receive", newId));
                    typedMessage.getText().clear();
                    adapter.notifyDataSetChanged();
                });

        chatLog.setAdapter(adapter);
    }

    private static void printCursor() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class MyListAdapter extends BaseAdapter {

        private ArrayList<Message> arrayList;

        public MyListAdapter(ArrayList<Message> messages) {
            this.arrayList = messages;
        }

        @Override
        public int getCount() {
            return this.arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return this.arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View viewOfRow = inflater.inflate(R.layout.activity_chat_room, parent, false);

            if (((Message) getItem(position)).getType().equals("Send")) {
                View sendLayout = inflater.inflate(R.layout.chat_room_send, parent, false);
                TextView sendText = sendLayout.findViewById(R.id.sender);
                sendText.setText(getItem(position).toString());
                viewOfRow = sendLayout;
            }
            if (((Message) getItem(position)).getType().equals("Receive")) {
                View receiveLayout = inflater.inflate(R.layout.chat_room_receive, parent, false);
                TextView receiveText = receiveLayout.findViewById(R.id.receiver);
                receiveText.setText(getItem(position).toString());
                viewOfRow = receiveLayout;
            }
             return viewOfRow;
        }
    }
    public void printCursor(Cursor cursor){
        Log.i("Database Version: ", String.valueOf(MyDatabaseOpener.VERSION_NUM));
        Log.i("Number of Columns: ", String.valueOf(cursor.getColumnCount()));
        Log.i("Name of Columns: ", String.valueOf(cursor.getColumnName(0)) + ", " + String.valueOf(cursor.getColumnName(1)) + ", " + String.valueOf(cursor.getColumnName(2)) );
        Log.i("Number of Results: ", String.valueOf(cursor.getCount()));
        cursor.moveToFirst();
        for(int i = 1; i<=cursor.getCount(); i++){
            Log.i("Row " , String.valueOf(cursor.getString(0)) + " " + String.valueOf(cursor.getString(1))+ " " + String.valueOf(cursor.getString(2)));
            cursor.moveToNext();
        }
    }
}

