package com.jmc.serversp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jmc.serversp.Model.Staff;
import com.jmc.serversp.ViewHolder.StaffViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

public class AidedStaffActivity extends AppCompatActivity {


    RelativeLayout relativeLayout ;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase db;
    DatabaseReference deptList;
    FirebaseStorage storage;
    StorageReference reference;
    Uri saveUri;
    String DeptId = "";

    FirebaseRecyclerAdapter<Staff, StaffViewHolder> adapter;

    MaterialEditText editName,editPost,editDegree,editPhone,editEmail,editAddress;
    Button buttonSelect,buttonUpload;
    Staff newStaff;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        relativeLayout = findViewById(R.id.aidedRelativeLayout);

        Toolbar toolbar = findViewById(R.id.toolbarAided);
        toolbar.setTitle("Aided Staff");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        db = FirebaseDatabase.getInstance();
        deptList = db.getReference().child("Aided");
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();

        //Init
        recyclerView = findViewById(R.id.recyclerViewAided);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(getIntent() != null)
            DeptId = getIntent().getStringExtra("DeptId");
        if(!DeptId.isEmpty())
            loadItemList(DeptId);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter == null){
            adapter.startListening();
        }
    }

    private void loadItemList(String deptId) {
        Query query =deptList.orderByChild("DeptId").equalTo(deptId);
        FirebaseRecyclerOptions<Staff> options = new FirebaseRecyclerOptions.Builder<Staff>().setQuery(query, Staff.class).build();
        adapter = new FirebaseRecyclerAdapter<Staff, StaffViewHolder>(options){
            @NonNull
            @Override
            public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.staff_list, viewGroup, false);
                return  new StaffViewHolder(view1);
            }

            @Override
            protected void onBindViewHolder(@NonNull StaffViewHolder holder, int position, @NonNull Staff model) {
                holder.staffName.setText(model.getName());
                holder.staffAddress.setText(model.getAddress());
                holder.staffEmail.setText(model.getEmail());
                holder.staffPhone.setText(model.getPhone().toString());
                holder.staffDegree.setText(model.getDegree());
                holder.staffPost.setText(model.getPost());
                Picasso.get().load(model.getImage()).into(holder.staffImage);


            }
        } ;
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Toast.makeText(getApplicationContext(),"Working ", Toast.LENGTH_SHORT).show();
                return (true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
