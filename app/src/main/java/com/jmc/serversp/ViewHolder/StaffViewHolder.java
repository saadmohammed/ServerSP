package com.jmc.serversp.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmc.serversp.Common.Common;
import com.jmc.serversp.Interface.ItemClickListener;
import com.jmc.serversp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StaffViewHolder extends RecyclerView.ViewHolder implements  View.OnCreateContextMenuListener {

    public TextView staffName, staffPost, staffDegree, staffPhone, staffEmail, staffAddress;
    public ImageView staffImage;



    public StaffViewHolder(@NonNull View itemView) {
        super(itemView);

        staffName = itemView.findViewById(R.id.staff_name);
        staffPost = itemView.findViewById(R.id.staff_post);
        staffImage = itemView.findViewById(R.id.staff_image);
        staffDegree = itemView.findViewById(R.id.staff_degree);
        staffPhone = itemView.findViewById(R.id.staff_phone);
        staffEmail = itemView.findViewById(R.id.staff_email);
        staffAddress = itemView.findViewById(R.id.staff_address);

        itemView.setOnCreateContextMenuListener(this);

    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        menu.setHeaderTitle("Select the Action");

        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(),Common.DELETE);

    }
}
