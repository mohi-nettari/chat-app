package com.example.whatsapp.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.ChatActivity;
import com.example.whatsapp.Model.Users;
import com.example.whatsapp.Model.Messages;
import com.example.whatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChateAdapter extends RecyclerView.Adapter  {

    ArrayList<Messages> messagesList;
    Context context;
    String recId;

    int SENDER_VIEW_TYPE = 1;
    int RECIRVER_VIEW_TYPE = 2;


    public ChateAdapter(ArrayList<Messages> messagesList, Context context) {
        this.messagesList = messagesList;
        this.context = context;
    }

    public ChateAdapter(ArrayList<Messages> messagesList, Context context, String recId) {
        this.messagesList = messagesList;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new senderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever,parent,false);
            return new recieverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages messages = messagesList.get(position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure want to delete this message")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                                database.getReference().child("Chats").child(senderRoom)
                                        .child(messages.getMessageId())
                                        .setValue(null);


                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                    }
                }).show();


                return false;
            }
        });

        if (holder.getClass() == senderViewHolder.class){

            ((senderViewHolder)holder).sendermsg.setText(messages.getMessage());
           // ((senderViewHolder)holder).sendertime.setText(messages.getTimestamp().toString());
            if(position == messagesList.size()-1){
                if(messages.getSeen()){
                    ((senderViewHolder)holder).senderseen.setVisibility(View.VISIBLE);
                    ((senderViewHolder)holder).senderseen.setText("Seen");
                }else {
                    ((senderViewHolder)holder).senderseen.setVisibility(View.VISIBLE);

                    ((senderViewHolder) holder).senderseen.setText("Delivered");

                }

            }

            }
        else {
            ((recieverViewHolder)holder).recievermsg.setText(messages.getMessage());
           // ((recieverViewHolder)holder).recievertime.setText(messages.getTimestamp().toString());


        }




    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messagesList.get(position).getuID().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }else {
            return RECIRVER_VIEW_TYPE;
        }


        //return super.getItemViewType(position);
    }

    public class recieverViewHolder extends RecyclerView.ViewHolder{

        TextView recievermsg,recievertime,recieverseen;

        public recieverViewHolder (@NonNull View itemView) {
            super(itemView);
            recievermsg = itemView.findViewById(R.id.reciever_txt);
            recievertime = itemView.findViewById(R.id.reciever_time);
            recieverseen = itemView.findViewById(R.id.txt_seen);
        }
    }

    public class senderViewHolder extends RecyclerView.ViewHolder{

        TextView sendermsg,sendertime,senderseen;

        public senderViewHolder (@NonNull View itemView) {
            super(itemView);
            sendermsg = itemView.findViewById(R.id.sender_txt);
            sendertime = itemView.findViewById(R.id.sender_time);

            senderseen = itemView.findViewById(R.id.txt_seen1);
        }
    }

}