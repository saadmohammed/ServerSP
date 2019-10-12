package com.jmc.serversp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jmc.serversp.Common.Common;
import com.jmc.serversp.Model.Department;
import com.jmc.serversp.Model.NTStaff;
import com.jmc.serversp.Model.Staff;
import com.jmc.serversp.ViewHolder.DepartmentViewHolder;
import com.jmc.serversp.ViewHolder.NTStaffViewHolder;
import com.jmc.serversp.ViewHolder.StaffViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

public class NTAidedActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_ntaided);

        relativeLayout = findViewById(R.id.UnAidedRelativeLayout);

        Toolbar toolbar = findViewById(R.id.toolbarNTAided);
        toolbar.setTitle("NTAided Staff");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        db = FirebaseDatabase.getInstance();
        staffList = db.getReference("NTAided");

        //Init
        recyclerView = findViewById(R.id.recyclerViewNTAided);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Fab
        fab = findViewById(R.id.fabNTAidedStaff);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddStaffDialog();
            }
        });

        loadNTAidedList();


    }

    private void loadNTAidedList() {
        Query query = FirebaseDatabase.getInstance().getReference().child("NTAided");
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(NTAidedActivity.this);
        alertDialog.setTitle("Add new NTStaff");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = getLayoutInflater();
        View add_staff_layout = inflater.inflate(R.layout.add_new_ntstaff_layout,null);

        editName = add_staff_layout.findViewById(R.id.edtName);
        editPhone = add_staff_layout.findViewById(R.id.edtPhone);
        editDesignation = add_staff_layout.findViewById(R.id.edtDesignation);

        buttonUpload = add_staff_layout.findViewById(R.id.btnNTStaffUpload);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDetails();
            }
        });

        alertDialog.setView(add_staff_layout);
        alertDialog.setIcon(R.drawable.ic_playlist_add_black_24dp);

        //Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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

    private void uploadDetails() {
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

        /*staffList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                newNTStaff = new NTStaff();
                newNTStaff.setName(editName.getText().toString());
                newNTStaff.setDesignation(editDesignation.getText().toString());
                newNTStaff.setPhone(Long.valueOf(editPhone.getText().toString()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }




}
