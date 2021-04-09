package com.example.firebasechat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity{

    private TextView mTextView;

    private ChatAdapter adapter;
    private ArrayList<ChatMessage> arrayList =new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            Intent i = new Intent(this, LoginActivity.class );
            startActivity(i);
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Bienvenido " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

            // Load chat room contents
            displayChatMessages();
        }

        mTextView = (TextView) findViewById(R.id.text);

        Button fab = (Button) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

//                Log.i("mensaje",input.getText().toString());

                if(input.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Debe ingresar un mensaje",
                            Toast.LENGTH_LONG)
                            .show();
                } else {
                    // Read the input field and push a new instance
                    // of ChatMessage to the Firebase database
                    FirebaseDatabase.getInstance().getReference().push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName())
                            );

                    // Clear the input
                    input.setText("");
                }
            }
        });


    }

    protected void displayChatMessages() {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.i("usuario",user.getEmail());


        DatabaseReference db =
                FirebaseDatabase.getInstance().getReference();

//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
//
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);
//        listOfMessages.setAdapter(adapter);

        adapter = new ChatAdapter(this,arrayList);
        listOfMessages.setAdapter(adapter);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                ChatMessage mensaje = dataSnapshot.getValue(ChatMessage.class);
                arrayList.add(mensaje);
                adapter.notifyDataSetChanged();
//                mensaje.setMessageText();

                int taglog = Log.d("TAGLOG", mensaje.toString() + "");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("TAGLOG","onChildChanged: {" + dataSnapshot.getKey() + ": " + dataSnapshot.getValue() + "}");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("TAGLOG", "onChildRemoved: {" + dataSnapshot.getKey() + ": " + dataSnapshot.getValue() + "}");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("TAGLOG", "onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAGLOG","Error!", databaseError.toException());
            }
        };

        db.addChildEventListener(childEventListener);
    }

    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(this, LoginActivity.class );
        startActivity(i);
        // Sign Out Google...
    }
}