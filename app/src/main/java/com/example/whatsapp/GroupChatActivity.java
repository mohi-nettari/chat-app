package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsapp.Adapters.ChateAdapter;
import com.example.whatsapp.Model.Messages;
import com.example.whatsapp.databinding.ActivityGroupChatBinding;
import com.example.whatsapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        getSupportActionBar().hide();

       // intializing FireBase
       final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final  FirebaseAuth auth = FirebaseAuth.getInstance();
//Back arrow events
        binding.backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<Messages> messagesList = new ArrayList<>();
        final ChateAdapter chateAdapter = new ChateAdapter(messagesList, this);
        binding.rcvmessages.setAdapter(chateAdapter);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        binding.rcvmessages.setLayoutManager(LayoutManager);

        final   String senderId = FirebaseAuth.getInstance().getUid();


        binding.userename.setText("Freinds Group");

        database.getReference().child("GroupChat")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                Messages message = snapshot1.getValue(Messages.class);
                    messagesList.add(message);

                }
                   chateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//sendarrow Events
        binding.sendarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = binding.sendmsget.getText().toString();
                final Messages messages = new Messages(senderId , message);
                messages.setTimestamp(new Date().getTime());
                binding.sendmsget.setText("");
                database.getReference().child("GroupChat")
                        .push()
                        .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
            }
            });

    }
}