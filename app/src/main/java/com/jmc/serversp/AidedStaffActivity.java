package com.jmc.serversp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jmc.serversp.Common.Common;
import com.jmc.serversp.Model.Staff;
import com.jmc.serversp.ViewHolder.StaffViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class AidedStaffActivity extends AppCompatActivity {


    RelativeLayout relativeLayout ;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase db;
    DatabaseReference staffList;
    FirebaseStorage storage;
    StorageReference reference;
    Uri saveUri;
    String DeptId = "";

    FirebaseRecyclerAdapter<Staff, StaffViewHolder> adapter;

    MaterialEditText editName,editPost,editDegree,editPhone,editEmail,editAddress;
    private Button buttonSelect,buttonUpload;
    Staff newStaff;

    StorageReference imageFolder;

    FloatingActionButton fab;
    
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
        staffList = db.getReference("Aided");
        storage = FirebaseStorage.getInstance();
        reference = storage.getReference();

        //Init
        recyclerView = findViewById(R.id.recyclerViewAided);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Fab
        fab = findViewById(R.id.fabStaff);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddStaffDialog();
            }
        });
        
        if(getIntent() != null)
            DeptId = getIntent().getStringExtra("DeptId");
        if(!DeptId.isEmpty())
            loadItemList(DeptId);

    }

    private void loadItemList(String deptId) {
        Query query =staffList.orderByChild("deptId").equalTo(deptId);
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

    private void showAddStaffDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AidedStaffActivity.this);
        alertDialog.setTitle("Add new Staff");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = getLayoutInflater();
        View add_staff_layout = inflater.inflate(R.layout.add_new_staff_layout,null);

        editName = add_staff_layout.findViewById(R.id.edtName);
        editPost = add_staff_layout.findViewById(R.id.edtPost);
        editDegree = add_staff_layout.findViewById(R.id.edtDegree);
        editPhone = add_staff_layout.findViewById(R.id.edtPhone);
        editEmail = add_staff_layout.findViewById(R.id.edtEmail);
        editAddress = add_staff_layout.findViewById(R.id.edtAddress);


        buttonSelect = add_staff_layout.findViewById(R.id.btnStaffSelect);
        buttonUpload = add_staff_layout.findViewById(R.id.btnStaffUpload);

        buttonSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //user select this image and save this uri
                chooseImage();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        alertDialog.setView(add_staff_layout);
        alertDialog.setIcon(R.drawable.ic_playlist_add_black_24dp);

        //Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //Here we create new staff
                if(newStaff != null){
                    staffList.push().setValue(newStaff);
                    Snackbar.make(relativeLayout,"New Staff "+newStaff.getName()+" was added",Snackbar.LENGTH_SHORT).show();
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

    private void uploadImage() {
        if(saveUri != null){

            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading ...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
                 imageFolder = reference.child("staff/" +imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(AidedStaffActivity.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for newCategory if image upload and we can get download link
                                    newStaff = new Staff();
                                    newStaff.setName(editName.getText().toString());
                                    newStaff.setAddress(editAddress.getText().toString());
                                    newStaff.setDegree(editDegree.getText().toString());
                                    newStaff.setEmail(editEmail.getText().toString());
                                    newStaff.setPhone(Long.valueOf(editPhone.getText().toString()));
                                    newStaff.setPost(editPost.getText().toString());
                                    newStaff.setDeptId(DeptId);
                                    newStaff.setImage(uri.toString());


                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(AidedStaffActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //Dont woory about this error
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Uploaded "+progress+"%");

                }
            });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null
        ) {
            saveUri = data.getData();
            buttonSelect.setText("Image Selected");
        }


    }



}
