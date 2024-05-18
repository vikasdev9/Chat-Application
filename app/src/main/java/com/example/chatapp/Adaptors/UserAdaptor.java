package com.example.chatapp.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Activity.ChatActivity;
import com.example.chatapp.Models.Users;
import com.example.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdaptor extends RecyclerView.Adapter<UserAdaptor.MyViewHolder> {

    private List<Users> alluserlist;
    private Context context;

    public UserAdaptor(List<Users> alluserlist, Context context) {
        this.alluserlist = alluserlist;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      Users users=alluserlist.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(users.getUid())){
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.height = 0;
            holder.itemView.setLayoutParams(params);
            holder.itemView.setVisibility(View.GONE);
        } else {
            // Reset the height and visibility for other items
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT; // Set to appropriate height
            holder.itemView.setLayoutParams(params);
            holder.itemView.setVisibility(View.VISIBLE);
        }


      holder.Name.setText(users.getName());
      holder.Status.setText(users.getStatus());
        Picasso.get().load(users.getImageuris()).into(holder.profileuserimg);
      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(context, ChatActivity.class);
              intent.putExtra("name",users.getName());
              intent.putExtra("Recievedimage",users.getImageuris());
              intent.putExtra("uid",users.getUid());
              context.startActivity(intent);
          }
      });
    }

    @Override
    public int getItemCount() {
        return alluserlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Name,Status;
        CircleImageView profileuserimg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.UserNameRecycler);
            Status = itemView.findViewById(R.id.UserStatusRecycler);
            profileuserimg = itemView.findViewById(R.id.userimagerecycler);
        }
    }
}
