package com.jmc.serversp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jmc.serversp.Common.Common;
import com.jmc.serversp.Interface.ItemClickListener;
import com.jmc.serversp.Model.Department;
import com.jmc.serversp.ViewHolder.DepartmentViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebase;
    DatabaseReference database;
    FirebaseStorage storage;
    StorageReference reference;
    FirebaseRecyclerAdapter<Department, DepartmentViewHolder> adapter;
    Uri saveUri;

    // Creating RecyclerView.
    RecyclerView recyclerView;
    GridLayoutManager mlm;
    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter recyclerAdapter ;


    //Add new Menu Layout
    MaterialEditText editName;
    private Button buttonSelect,buttonUpload;

    Department newDepartment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarMain);
        toolbar.setTitle("Department");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));


        firebase = FirebaseDatabase.getInstance();
        database = firebase.getReference().child("Department");

        //Recycler view Init
        recyclerView = findViewById(R.id.recyclerViewMain);
        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);
        // Setting RecyclerView layout as LinearLayout.
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        if (Common.isConnectedToInternet(this)) {
            loadDepartment();
        }else {
            Intent intent = new Intent(getApplicationContext(), RetryActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(), "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDepartment() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Department");
        FirebaseRecyclerOptions<Department> options = new FirebaseRecyclerOptions.Builder<Department>()
                .setQuery(query, Department.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<Department, DepartmentViewHolder>(options) {
            @NonNull
            @Override
            public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.department, viewGroup, false);
                return new DepartmentViewHolder(view1);
            }

            @Override
            protected void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position, @NonNull Department model) {
                holder.departmentName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.departmentImage);
                final Department clickDepartmentItem = model;
                if (Common.isConnectedToInternet(getApplicationContext())) {
                    holder.btnAided.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(),"Aided",Toast.LENGTH_SHORT).show();
                        }
                    });
                } else
                    startActivity(new Intent(getApplicationContext(), RetryActivity.class));
                if (Common.isConnectedToInternet(getApplicationContext())){
                    holder.btnUnAided.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(),"UnAided",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add:
                showDialog();
                return(true);
            }
        return(super.onOptionsItemSelected(item));
    }
    
    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Add new Department");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_department_layout,null);

        editName = add_menu_layout.findViewById(R.id.edtName);
        buttonSelect = add_menu_layout.findViewById(R.id.btnSelect);
        buttonUpload = add_menu_layout.findViewById(R.id.btnUpload);

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user select this image and save this uri
                chooseImage();
                Toast.makeText(getApplicationContext(),"Select",Toast.LENGTH_SHORT).show();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
  //              uploadImage();
                Toast.makeText(getApplicationContext(),"Upload",Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_playlist_add_black_24dp);

        //Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //Here we create new category
                if(newDepartment != null){
                    database.push().setValue(newDepartment);
                    Toast.makeText(getApplicationContext(),"New Category "+newDepartment.getName()+" was added",Toast.LENGTH_SHORT).show();
                }
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

    private void chooseImage() {
    }


}

