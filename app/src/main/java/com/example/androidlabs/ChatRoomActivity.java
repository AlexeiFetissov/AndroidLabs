package com.example.androidlabs;

        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.ContentValues;
        import android.content.DialogInterface;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.TextView;
        import java.lang.String;
        import java.util.ArrayList;
        import java.util.Arrays;

        import android.database.Cursor;

public class ChatRoomActivity extends AppCompatActivity {

    // messages
    ArrayList<Message> list = new ArrayList<>(); // private  elements
    BaseAdapter myAdapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();

        //loadDataFromDatabase();
        // We want to get all of the columns
        String [] columns = {
                MyOpener.COL_ID, MyOpener.COL_MESSAGE, MyOpener.COL_SENT, MyOpener.COL_RECEIVED};
        //query all the results from the database:
        Cursor results = db.query(
                false, MyOpener.TABLE_NAME, columns,
                null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int msgColumnIndex = results.getColumnIndex(MyOpener.COL_MESSAGE);
        int sentColIndex = results.getColumnIndex(MyOpener.COL_SENT);
        int receivedColIndex = results.getColumnIndex(MyOpener.COL_RECEIVED);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        while(results.moveToNext())
        {
            String msg = results.getString(msgColumnIndex);
            String msgSent = results.getString(sentColIndex);
            String msgReceived = results.getString(receivedColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            if (msgSent.equals("1"))
                list.add(new Message(id, msg, true, false));
            else
                list.add(new Message(id, msg, false, true));
        }


        // get reference to ListView
        ListView theList = findViewById(R.id.lv_chat_room);
        theList.setAdapter(myAdapter = new MyListAdapter());
        myAdapter.notifyDataSetChanged();


        theList.setOnItemLongClickListener((parent, view, pos, id) -> {
            View message_view = getLayoutInflater().inflate(R.layout.view_alert, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Do you want to delete this?" )

                    .setMessage("The slected row is: " + (pos+1) + "\nThe database id: " + id)
                    .setView(message_view)

                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialogInf, int dlg) {
                            // What to do on delete and update
                            deleteContact(id);
                            list.remove(pos);
                            myAdapter.notifyDataSetChanged(); // update
                        }

                    })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialogInf, int dlg) {
                            // What to do on "No"
                            dialogInf.dismiss();
                        }

                    }).setView(message_view);

            builder.create().show();
            return false;
        });

        TextView textView = findViewById(R.id.et_chat_text);
        Button sendButton = findViewById(R.id.btn_send);
        sendButton.setOnClickListener( click -> {
            String mess2db = textView.getText().toString();
            String isSent = "1";
            String isReceived = "0";
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_MESSAGE, mess2db);
            newRowValues.put(MyOpener.COL_SENT, isSent);
            newRowValues.put(MyOpener.COL_RECEIVED, isReceived);
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            Message message =
                    new Message(newId, mess2db, true, false);
            list.add(message);
            myAdapter.notifyDataSetChanged(); // update
            textView.setText("");
        });

        Button receiveButton = findViewById(R.id.btn_receive);
        receiveButton.setOnClickListener(click -> {
            String mess2db = textView.getText().toString();
            String isSent = "0";
            String isReceived = "1";
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_MESSAGE, mess2db);
            newRowValues.put(MyOpener.COL_SENT, isSent);
            newRowValues.put(MyOpener.COL_RECEIVED, isReceived);
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            Message message =
                    new Message(newId, mess2db, false, true);
            list.add(message);
            myAdapter.notifyDataSetChanged(); // update
            textView.setText("");
        });

        printCursor(results);
    }

    public void printCursor(Cursor c) {

        int v = MyOpener.VERSION_NUM;

        Log.i("Database Version #: ", String.valueOf(v));
        Log.i("Numb of clmn in curs: ", String.valueOf(c.getColumnCount()));
        Log.i("Columns names in curs: ", Arrays.toString(c.getColumnNames()));
        Log.i("Numb results in curs: ", String.valueOf(c.getCount()));

        c.moveToFirst();

        do{
            String data = c.getString(c.getColumnIndex("MESSAGE"));
            Log.i("Row result: ", data);
            c.moveToNext();
        }while (!c.isAfterLast());
    }


    protected void deleteContact(long index)
    {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(index)});
    }

    public class Message {
        long id;
        public String message;
        public boolean isSent;
        public boolean isReceived;

        public Message(long i, String message, boolean isSent, boolean isReceived){  // long i,
            this.id = i;
            this.message = message;
            this.isSent = isSent;
            this.isReceived = isReceived;
        }
        public long getId() {
            return id;
        }
    }

    private class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        } // number of rows in the elements list

        @Override
        public Object getItem(int position) {
            return list.get(position);
        } // returns an elements item found at the "position"

        @Override
        public long getItemId(int position) {
            return list.get(position).getId(); // normally returns database ID; for now it is (long)position
        }

        @Override
        public View getView(int position, View newView, ViewGroup parent) {
            Message msg = (Message) getItem(position);
            int layout = 0;
            newView = null;
            if (msg.isSent) {layout = R.layout.send_row_layout;}
            else if (msg.isReceived)
                    layout = R.layout.receive_row_layout;

            if (newView == null)
                newView = getLayoutInflater().inflate(layout, parent, false);

            TextView textView = newView.findViewById(R.id.message);
            textView.setText(msg.message);

            return newView;
        }
    }
}
