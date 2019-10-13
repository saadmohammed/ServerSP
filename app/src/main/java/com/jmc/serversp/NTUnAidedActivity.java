package com.jmc.serversp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jmc.serversp.Common.Common;
import com.jmc.serversp.Model.NTStaff;
import com.jmc.serversp.ViewHolder.NTStaffViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;

public class NTUnAidedActivity extends AppCompatActivity {

    RelativeLayout relativeLayout ;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase db;
    DatabaseReference staffList;

    FirebaseRecyclerAdapter<NTStaff, NTStaffViewHolder> adapter;

    MaterialEditText editName,editDesignation,editPhone;
    private Button buttonUpload;
    NTStaff newNTStaff;

    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nt_un_aided);

        relativeLayout = findViewById(R.id.NTUnAidedRelativeLayout);

        Toolbar toolbar = findViewById(R.id.toolbarNTUnAided);
        toolbar.setTitle("NT UnAided Staff");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        db = FirebaseDatabase.getInstance();
        staffList = db.getReference("NTUnAided");

        //Init
        recyclerView = findViewById(R.id.recyclerViewNTUnAided);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Fab
        fab = findViewById(R.id.fabNTUnAidedStaff);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddStaffDialog();
            }
        });

        loadNTUnAidedList();

    }

    private void loadNTUnAidedList() {
        Query query = FirebaseDatabase.getInstance().getReference().child("NTUnAided");
        FirebaseRecyclerOptions<NTStaff> options = new FirebaseRecyclerOptions.Builder<NTStaff>()
                .setQuery(query, NTStaff.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<NTStaff, NTStaffViewHolder>(options) {
            @NonNull
            @Override
            public NTStaffViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nt_staff_list, viewGroup, false);
                return new NTStaffViewHolder(view1);
            }

            @Override
            protected void onBindViewHolder(@NonNull NTStaffViewHolder holder, final int position, @NonNull NTStaff model) {
                holder.ntStaffName.setText(model.getName());
                holder.ntStaffDesignation.setText(model.getDesignation());
                holder.ntStaffPhone.setText(model.getPhone().toString());
            }

        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void showAddStaffDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NTUnAidedActivity.this);
        alertDialog.setTitle("Add new NTStaff");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = getLayoutInflater();
        View add_staff_layout = inflater.inflate(R.layout.add_new_ntstaff_layout,null);

        editName = add_staff_layout.findViewById(R.id.edtName);
        editPhone = add_staff_layout.findViewById(R.id.edtPhone);
        editDesignation = add_staff_layout.findViewById(R.id.edtDesignation);


        alertDialog.setView(add_staff_layout);
        alertDialog.setIcon(R.drawable.ic_playlist_add_black_24dp);

        //Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editName.getText().toString();
                String designation = editDesignation.getText().toString();
                Long phone = Long.valueOf(editPhone.getText().toString());

                if (Common.isConnectedToInternet(getApplicationContext())){
                    if (name.isEmpty() || designation.isEmpty() || phone == 0){
                        Toast.makeText(getApplicationContext(),"Please all the fields",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        newNTStaff = new NTStaff(name, designation, phone);
                        staffList.push().setValue(newNTStaff).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Uploaded "+newNTStaff+" ",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Unable to upload "+newNTStaff+" ",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }


                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    @Override
    public boolean onContextItemSelected(@NonNull final MenuItem item) {

        if (item.getTitle().equals(Common.UPDATE)){
            showUpdateStaffDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }else if(item.getTitle().equals(Common.DELETE)){

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(NTUnAidedActivity.this);
            alertDialog.setTitle("Delete Item");
            alertDialog.setMessage("Are you sure?");

            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    adapter.getRef(item.getOrder()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Deleted Succesfully ",Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Unable to delete "+e.getMessage()+" ",Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDialog.show();

        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStaffDialog(final String key, final NTStaff item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NTUnAidedActivity.this);
        alertDialog.setTitle("Edit Staff");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = getLayoutInflater();
        View add_item_layout = inflater.inflate(R.layout.add_new_ntstaff_layout,null);

        editName = add_item_layout.findViewById(R.id.edtName);
        editPhone = add_item_layout.findViewById(R.id.edtPhone);
        editDesignation = add_item_layout.findViewById(R.id.edtDesignation);

        editName.setText(item.getName());
        editDesignation.setText(item.getDesignation());
        editPhone.setText(item.getPhone().toString());

        alertDialog.setView(add_item_layout);
        alertDialog.setIcon(R.drawable.ic_playlist_add_black_24dp);

        //Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //Update Information
                item.setName(editName.getText().toString());
                item.setPhone(Long.valueOf(editPhone.getText().toString()));
                item.setDesignation(editDesignation.getText().toString());

                staffList.child(key).setValue(item);
                Snackbar.make(relativeLayout,"Staff "+item.getName()+" was edited",Snackbar.LENGTH_SHORT).show();

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }



}
