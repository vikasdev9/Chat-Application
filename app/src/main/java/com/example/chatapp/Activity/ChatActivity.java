package com.example.chatapp.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.chatapp.Adaptors.MessageAdaptors;
import com.example.chatapp.Models.MessageModel;
import com.example.chatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
String ReceiverImg,ReceivName,ReceiverUid;
CircleImageView profile_image;
TextView ReceiverName;
private Toolbar toolbar;

 public static String Senderimg;
 public static String Receiverimgss;

private FirebaseDatabase database;
private FirebaseAuth firebaseAuth;

EditText EnterMsg;
CardView SendMsgBtn;
String SenderUid;

String SenderRoom,ReceiverRoom;

RecyclerView messageRecycler;

MessageAdaptors messageAdaptors;

ArrayList<MessageModel> messageModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        profile_image=findViewById(R.id.receiverimg);
        ReceiverName=findViewById(R.id.ReceiverName);
        messageRecycler=findViewById(R.id.MessageAdatporrecycler);

        EnterMsg=findViewById(R.id.entermsg);
        SendMsgBtn=findViewById(R.id.sendmsg);
        messageModelArrayList=new ArrayList<>();

        // Initialize Toolbar
       toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        status bar
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.primary_purple));

//        Firebase
        database = FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        ReceiverImg=getIntent().getStringExtra("Recievedimage");
        ReceivName=getIntent().getStringExtra("name");
        ReceiverUid=getIntent().getStringExtra("uid");
        Log.i("msg", "Receiver Uid: "+ReceiverUid);

        Picasso.get().load(ReceiverImg).into(profile_image);
        ReceiverName.setText(""+ReceivName);

        SenderUid=firebaseAuth.getUid();

        SenderRoom=SenderUid+ReceiverUid;
        ReceiverRoom=ReceiverUid+SenderUid;

       DatabaseReference reference= database.getReference().child("users").child(firebaseAuth.getUid());
       DatabaseReference Chatreference= database.getReference().child("chats").child(SenderRoom).child("messages");

       LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
       linearLayoutManager.setStackFromEnd(true);


       messageAdaptors=new MessageAdaptors(this, messageModelArrayList);
       messageRecycler.setLayoutManager(linearLayoutManager);
       messageRecycler.setAdapter(messageAdaptors);

       Chatreference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               messageModelArrayList.clear();
               for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                   MessageModel messageModel2=dataSnapshot.getValue(MessageModel.class);
                   messageModelArrayList.add(messageModel2);
                   messageAdaptors.notifyDataSetChanged();
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
              Senderimg= snapshot.child("imageuris").getValue().toString();
               Receiverimgss=ReceiverImg;
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

       SendMsgBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String message=EnterMsg.getText().toString();
               if (message.isEmpty()){
                   Toast.makeText(ChatActivity.this, "Enter Message", Toast.LENGTH_SHORT).show();
                   return;
               }
               EnterMsg.setText("");
               Date date=new Date();

               MessageModel messageModel=new MessageModel(message,SenderUid,date.getTime());

               database.getReference().child("chats")
                       .child(SenderRoom)
                       .child("messages")
                       .push()
                       .setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               database.getReference().child("chats")
                                       .child(ReceiverRoom)
                                       .child("messages")
                                       .push()
                                       .setValue(messageModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {

                                           }
                                       });
                           }
                       });
           }
       });


    }

}