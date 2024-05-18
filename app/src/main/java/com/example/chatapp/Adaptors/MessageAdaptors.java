package com.example.chatapp.Adaptors;

import static com.example.chatapp.Activity.ChatActivity.Receiverimgss;
import static com.example.chatapp.Activity.ChatActivity.Senderimg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Models.MessageModel;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdaptors extends RecyclerView.Adapter {

    Context context;
    ArrayList<MessageModel> messageModelsAdaptor;

    public MessageAdaptors(Context context, ArrayList<MessageModel> messageModelsAdaptor) {
        this.context = context;
        this.messageModelsAdaptor = messageModelsAdaptor;
    }

    int ITEM_SEND=1;
    int ITEM_RECEIVE=2;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==ITEM_SEND){
            View view= LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new SenderViewHolder(view);
        }else {
            View view= LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false);
            return new ReceiverViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
MessageModel messageModel3=messageModelsAdaptor.get(position);
       if (holder.getClass()==SenderViewHolder.class){
           SenderViewHolder senderViewHolder= (SenderViewHolder) holder;
           senderViewHolder.sender_message.setText(messageModel3.getMessage());
           Picasso.get().load(Senderimg).into(senderViewHolder.profile_imageSender);
       }else{
           ReceiverViewHolder receiverViewHolder= (ReceiverViewHolder) holder;
           receiverViewHolder.receiver_message.setText(messageModel3.getMessage());
           Picasso.get().load(Receiverimgss).into(receiverViewHolder.profile_imageReceiver);
       }
    }

    @Override
    public int getItemCount() {
        return messageModelsAdaptor.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel=messageModelsAdaptor.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messageModel.getSenderId())){
            return ITEM_SEND;
        }else {
           return ITEM_RECEIVE;
        }
    }

    class SenderViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profile_imageSender;
        TextView sender_message;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_imageSender = itemView.findViewById(R.id.senderprofile1);
            sender_message = itemView.findViewById(R.id.sendermsg2);
        }
    }
    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile_imageReceiver;
        TextView receiver_message;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_imageReceiver = itemView.findViewById(R.id.receiverprofile1);
            receiver_message = itemView.findViewById(R.id.receivermsg2);
        }
    }
}
