package com.jmc.serversp.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmc.serversp.Common.Common;
import com.jmc.serversp.Interface.ItemClickListener;
import com.jmc.serversp.R;

import androidx.recyclerview.widget.RecyclerView;


public class DepartmentViewHolder extends RecyclerView.ViewHolder  implements View.OnCreateContextMenuListener {


    public TextView departmentName;
    public ImageView departmentImage;
    public Button btnAided,btnUnAided;

    public DepartmentViewHolder( View itemView) {
        super(itemView);

        departmentName = itemView.findViewById(R.id.department_name);
        departmentImage = itemView.findViewById(R.id.department_image);
        btnAided = itemView.findViewById(R.id.btn_aided);
        btnUnAided = itemView.findViewById(R.id.btn_unaided);

        itemView.setOnCreateContextMenuListener(this);

    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        menu.setHeaderTitle("Select the Action");

        menu.add(0,0,getAdapterPosition(),Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DELETE);
    }
}
