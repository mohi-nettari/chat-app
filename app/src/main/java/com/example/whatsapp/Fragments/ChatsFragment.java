package com.example.whatsapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.whatsapp.Adapters.UsersAdapter;
import com.example.whatsapp.ChatActivity;
import com.example.whatsapp.Model.Messages;
import com.example.whatsapp.Model.Users;
import com.example.whatsapp.R;
import com.example.whatsapp.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment{
    public ChatsFragment() {

    }

    FragmentChatsBinding binding;
    FirebaseDatabase database;
    final ArrayList<Users> userslist = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ProgressDialog progressDialog;

        //Intailazing the progressDialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Brigin your contacts list");
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();


        final UsersAdapter usersAdapter;
        usersAdapter = new UsersAdapter(userslist,getContext());

        binding.rcvusers.setAdapter(usersAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        binding.rcvusers.setLayoutManager(linearLayoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userslist.clear();
                for (DataSnapshot dataSnapshot :snapshot.getChildren() ){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if(!(users.getUserId().equals(FirebaseAuth.getInstance().getUid()))){
                        userslist.add(users);
                    }
                }
                progressDialog.dismiss();
                usersAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        return binding.getRoot();

    }
}
