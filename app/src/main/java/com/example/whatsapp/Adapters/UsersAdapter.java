package com.example.whatsapp.Adapters;

import static com.example.whatsapp.R.drawable.avatar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsapp.ChatActivity;
import com.example.whatsapp.Model.Users;
import com.example.whatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends  RecyclerView.Adapter<UsersAdapter.viewHolder> {

    ArrayList<Users> userslist;
    Context context;

    public UsersAdapter(ArrayList<Users> userslist, Context context) {
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
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Users users = userslist.get(position);

        //uploading user name and user profile pic in chats Fragment
      
            Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.avatar).into(holder.userimage);

          holder.username.setText(users.getUserName());

        FirebaseDatabase.getInstance().getReference().child("Chats")
                .child(FirebaseAuth.getInstance().getUid() + users.getUserId())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()){
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                holder.lastmessage.setText(snapshot.child("message")
                                        .getValue().toString());



                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//on clicklistener for the user item in recyclerview
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userId",users.getUserId());
                intent.putExtra("profilePic",users.getProfilePic());
                intent.putExtra("userName",users.getUserName());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return userslist.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView userimage;
        TextView username,lastmessage;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            userimage = itemView.findViewById(R.id.profile_pic);
            username = itemView.findViewById(R.id.usernametxt);
            lastmessage = itemView.findViewById(R.id.lastmessagetxt);
        }
    }



}
