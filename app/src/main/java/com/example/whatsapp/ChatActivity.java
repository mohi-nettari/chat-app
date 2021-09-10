package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.Adapters.ChateAdapter;
import com.example.whatsapp.Model.Messages;
import com.example.whatsapp.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
     String senderId;
    String recieverId;
    String userName;
    String userPic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //intializing FireBase
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        //sender Id
          senderId = auth.getCurrentUser().getUid();

        //Getting reciever Data From users adapter
         recieverId = getIntent().getStringExtra("userID");
          userName = getIntent().getStringExtra("userName");
          userPic = getIntent().getStringExtra("profilePic");

        //updating the views in chat lyout
        binding.userename.setText(userName);
        Picasso.get().load(userPic).placeholder(R.drawable.avatar).into(binding.profilePic);

        //Back arrow events
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<Messages> messagesList = new ArrayList<>();
        final ChateAdapter chateAdapter = new ChateAdapter(messagesList,this,recieverId);
        binding.rcvmessages.setAdapter(chateAdapter);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        binding.rcvmessages.setLayoutManager(LayoutManager);

        final String senderRoom= senderId + recieverId;
        final String recieverRoom= recieverId +senderId ;


        //send arrow events (sending message)
        binding.sendarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = binding.sendmsget.getText().toString();
                if ( message == "" ){
                    Toast.makeText(ChatActivity.this, "Pleas type your message", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Messages messages = new Messages(senderId,recieverId, message);
                messages.setTimestamp(new Date().getTime());
                binding.sendmsget.setText("");

                database.getReference().child("Chats")
                        .child(senderRoom)
                        .push()
                        .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("Chats")
                                .child(recieverRoom)
                                .push()
                                .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });

                    }
                });
            }
        });

        //Show the messages in the adapter

        database.getReference().child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Messages message = snapshot1.getValue(Messages.class);
                    assert message != null;
                 if (message.getRecieverId().equals(senderId) && message.getuID().equals(recieverId) ||
                      message.getRecieverId().equals(recieverId) && message.getuID().equals(senderId)) {
                          message.setMessageId(snapshot1.getKey());
                           messagesList.add(message);
                  }
                }
                chateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       // reference = FirebaseDatabase.getInstance().getReference().child("Chats");
//
//          FirebaseDatabase.getInstance().getReference().child("Chats").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                messagesList.clear();
//                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//                    Messages message = snapshot1.getValue(Messages.class);
//                    // assert message != null;
//                    if (message.getRecieverId().equals(senderId) && message.getuID().equals(recieverId) ||
//                            message.getRecieverId().equals(recieverId) && message.getuID().equals(senderId)) {
//                        messagesList.add(message);
//                        //}
//                    }
//                    chateAdapter = new ChateAdapter(messagesList, ChatActivity.this);
//                    binding.rcvmessages.setAdapter(chateAdapter);
//                    //chateAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

}
