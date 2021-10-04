package com.example.whatsapp.Adapters;

import static com.example.whatsapp.R.drawable.avatar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp.ChatActivity;
import com.example.whatsapp.Fragments.ChatsFragment;
import com.example.whatsapp.Model.Messages;
import com.example.whatsapp.Model.Users;
import com.example.whatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends  RecyclerView.Adapter<UsersAdapter.viewHolder> {

    ArrayList<Users> userslist;
    Context context;
     Users users ;
     String lastmsg;



    public UsersAdapter(ArrayList<Users> userslist, Context context ) {
        this.userslist = userslist;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_user_item,parent,false);


        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)

    {

        int pos = position;
        //uploading user name and user profile pic in chats Fragment

         users = userslist.get(pos);
        Log.d("reci1",users.getUserId());

        Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.avatar).into(holder.userimage);

        holder.username.setText(users.getUserName());

        lastmsg(users.getUserId(),holder.lastmessage);

        Boolean state = users.getState();
////
//        FirebaseDatabase.getInstance().getReference().child("Chats")
//                .child(FirebaseAuth.getInstance().getUid() + users.getUserId())
//                .orderByChild("timestamp")
//                .limitToLast(1)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                       users = userslist.get(pos);
//                        if (snapshot.hasChildren()){
//                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
//                                holder.lastmessage.setText(snapshot.child("message")
//                                        .getValue().toString());
//
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });



//on clicklistener for the user item in recyclerview
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                users = userslist.get(pos);
                Log.d("reci3", users.getUserId());


                Intent intent = new Intent(context , ChatActivity.class);

                intent.putExtra("userId", users.getUserId());
                intent.putExtra("profilePic", users.getProfilePic());
                intent.putExtra("userName",users.getUserName());
                context.startActivity(intent);


            }
        });

    }


    @Override
    public int getItemCount() {
        return userslist.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView userimage,state;
        TextView username,lastmessage;

        public viewHolder(@NonNull View itemView ) {
            super(itemView);
            userimage = itemView.findViewById(R.id.profile_pic);
            username = itemView.findViewById(R.id.usernametxt);
            lastmessage = itemView.findViewById(R.id.lastmessagetxt);
            state = itemView.findViewById(R.id.state);
        }


    }

    public void  lastmsg (String userid,TextView lastmsgTV){
        lastmsg = "default";
        final FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference()
                .child("Chats")
                .child(FirebaseAuth.getInstance().getUid()+users.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Messages message = snapshot1.getValue(Messages.class);
                            if (message.getRecieverId().equals(currentuser.getUid()) &&
                                    message.getuID().equals(userid))
                            {
                                lastmsg = users.getUserName() +" :  " + message.getMessage();


                            }else if (message.getRecieverId().equals(userid) &&
                                    message.getuID().equals(currentuser.getUid())){

                                lastmsg = "You :  " + message.getMessage();

                            }


                        }
                        switch (lastmsg){
                            case "default":
                                lastmsgTV.setText("No messages yet");
                                break;
                            default:
                                lastmsgTV.setText(lastmsg);

                        }
                        lastmsg="default";

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






    }


}
