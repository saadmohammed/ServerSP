package com.jmc.serversp.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.jmc.serversp.Common.Common;
import com.jmc.serversp.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NTStaffViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

    public TextView ntStaffName, ntStaffDesignation, ntStaffPhone;
    //public ImageButton ntStaffCall;

    public NTStaffViewHolder(@NonNull View itemView) {
        super(itemView);

        ntStaffName = itemView.findViewById(R.id.nt_staff_name);
        ntStaffDesignation = itemView.findViewById(R.id.nt_staffdesignation);
        ntStaffPhone = itemView.findViewById(R.id.nt_staffphone);
       // ntStaffCall = itemView.findViewById(R.id.img_nt_staff_call);
        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        menu.setHeaderTitle("Select the Action");

        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(),Common.DELETE);

    }

}
