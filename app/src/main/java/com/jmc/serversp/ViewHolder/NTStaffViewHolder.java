package com.jmc.serversp.ViewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jmc.serversp.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NTStaffViewHolder extends RecyclerView.ViewHolder {

    public TextView ntStaffName, ntStaffDesignation, ntStaffPhone;
    public ImageButton ntStaffCall;

    public NTStaffViewHolder(@NonNull View itemView) {
        super(itemView);

        ntStaffName = itemView.findViewById(R.id.nt_staff_name);
        ntStaffDesignation = itemView.findViewById(R.id.nt_staffdesignation);
        ntStaffPhone = itemView.findViewById(R.id.nt_staffphone);
        ntStaffCall = itemView.findViewById(R.id.img_nt_staff_call);
    }


  }
