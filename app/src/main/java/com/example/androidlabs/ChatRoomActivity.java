package com.example.androidlabs;

        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.TextView;
        import java.lang.String;
        import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    // messages
    ArrayList<Message> list = new ArrayList<>(); // private  elements
    BaseAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        // get reference to ListView
        ListView theList = findViewById(R.id.lv_chat_room);
        theList.setAdapter(myAdapter = new MyListAdapter());
        myAdapter.notifyDataSetChanged();


        theList.setOnItemLongClickListener((parent, view, pos, id) -> {
            View message_view = getLayoutInflater().inflate(R.layout.view_alert, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Do you want to delete this?\nThe selected row is: " )

                    .setMessage("The slected row is: " + (pos+1) + "\nThe database id: " + id)
                    .setView(message_view)

                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            // What to do on delete and update
                           // dialog.dismiss();
                            list.remove(pos);
                            myAdapter.notifyDataSetChanged(); // update
                        }

                    })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            // What to do on "No"
                            dialog.dismiss();
                        }

                    }).setView(message_view);

            builder.create().show();
            return false;
        });

        TextView textView = findViewById(R.id.et_chat_text);
        Button sendButton = findViewById(R.id.btn_send);
        sendButton.setOnClickListener( click -> {
            Message message =
                    new Message(textView.getText().toString(), true, false);
            list.add(message);
            myAdapter.notifyDataSetChanged(); // update
            textView.setText("");
        });

        Button receiveButton = findViewById(R.id.btn_receive);
        receiveButton.setOnClickListener(click -> {
            Message message =
                    new Message(textView.getText().toString(), false, true);
            list.add(message);
            myAdapter.notifyDataSetChanged(); // update
            textView.setText("");
        });
    }

    public class Message {
        public String message;
        public boolean isSend;
        public boolean isReceived;

        public Message(String message, boolean isSend, boolean isReceived){
            this.message = message;
            this.isSend = isSend;
            this.isReceived = isReceived;
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
            return (long)position; // normally returns database ID; for now it is (long)position
        }

        @Override
        public View getView(int position, View newView, ViewGroup parent) {
            Message msg = (Message) getItem(position);
            int layout = 0;
            newView = null;
            if (msg.isSend) {layout = R.layout.send_row_layout;}
            else {
                if (msg.isReceived)
                    layout = R.layout.receive_row_layout;
            }
            if (newView == null){
                newView = getLayoutInflater().inflate(layout, parent, false);

            }
            else
            {

            }
            TextView textView = newView.findViewById(R.id.message);
            textView.setText(msg.message);

            return newView;
        }
    }
}
