package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.Adapters.ChateAdapter;
import com.example.whatsapp.Model.Messages;
import com.example.whatsapp.Model.Users;
import com.example.whatsapp.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
     String senderId;
     String userPic;
     String userName;
     String recieverId;
     ValueEventListener isSeenListener;
        String senderRoom;
    String recieverRoom;
Boolean state;

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
        recieverId = getIntent().getExtras().get("userId").toString();
        Log.d("done2", "done");
        userPic = getIntent().getStringExtra("profilePic");
        //userPic = getIntent().getExtras().get("profilePic").toString();
        Log.d("done3", "done");
        userName = getIntent().getStringExtra("userName");
        //  userName = getIntent().getExtras().get("userName").toString();
        Log.d("done4", "done");


        //updating the views in chat lyout
        binding.userename.setText(userName);
        Picasso.get().load(userPic).placeholder(R.drawable.avatar).into(binding.profilePic);


        //Back arrow events
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<Messages> messagesList = new ArrayList<>();
        final ChateAdapter chateAdapter = new ChateAdapter(messagesList, ChatActivity.this, recieverId);
        binding.rcvmessages.setAdapter(chateAdapter);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        binding.rcvmessages.setLayoutManager(LayoutManager);

        final String senderRoom = senderId + recieverId;
        final String recieverRoom = recieverId + senderId;

        //message isSeen (status of the message)
        isSeenListener = database.getReference()
                .child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Messages message = snapshot1.getValue(Messages.class);
//                                    assert message != null;
                            if (message.getRecieverId().equals(senderId) &&
                                    message.getuID().equals(ChatActivity.this.recieverId)){

                                HashMap<String, Object> seenMap = new HashMap<>();
                                seenMap.put("seen",true);
                                snapshot1.getRef().updateChildren(seenMap);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference()
                .child("Chats")
                .child(recieverRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Messages message = snapshot1.getValue(Messages.class);
//                                    assert message != null;
                            if (message.getRecieverId().equals(senderId) &&
                                    message.getuID().equals(ChatActivity.this.recieverId)){

                                HashMap<String, Object> seen1Map = new HashMap<>();
                                seen1Map.put("seen",true);
                                snapshot1.getRef().updateChildren(seen1Map);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //send arrow events (sending message)
        binding.sendarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = binding.sendmsget.getText().toString();
                if (message.equals("")) {
                    Toast.makeText(ChatActivity.this, "Pleas type your message", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Messages messages = new Messages(senderId,recieverId, message );
                messages.setTimestamp(new Date().getTime());
                messages.setSeen(false);
                binding.sendmsget.setText("");
                database.getReference().child("Chats")
                        .child(senderRoom)
                        .push()
                        .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChatActivity.this, "sended", Toast.LENGTH_SHORT).show();
                        database.getReference().child("Chats")
                                .child(recieverRoom)
                                .push()
                                .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        });

                    }
                });
            }
        });

//
        database.getReference()
                .child(senderId)
                .child("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Users users = snapshot.getValue(Users.class);
//                                    assert message != null;

                                HashMap<String, Object> stateHash = new HashMap<>();
                                 stateHash.put("state",state);

                        }
                    //}

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        //Show the messages in the adapter

  database.getReference()
          .child("Chats")
              .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                messagesList.clear();
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    Messages message = snapshot1.getValue(Messages.class);
//                                    assert message != null;
                           if (message.getRecieverId().equals(senderId) &&
                                 message.getuID().equals(ChatActivity.this.recieverId) ||
                              message.getRecieverId().equals(ChatActivity.this.recieverId) &&
                                  message.getuID().equals(senderId)) {
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


    }

    @Override
    protected void onResume() {
        super.onResume();
       state = true;
        binding.state.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green));





    }

    @Override
    protected void onPause() {
        super.onPause();
        state = false;
        binding.state.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.grey));

    }
}


