package com.climo.al.climoproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class EditAkun extends AppCompatActivity {
    FirebaseUser currentUSer;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    ImageView imgUser;
    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri pickedImgUri;
    private ProgressBar pBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_akun);

        //init
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        pBar = (ProgressBar) findViewById(R.id.pBar);
        currentUSer = mAuth.getCurrentUser();
        Button btnCancel = (Button) findViewById(R.id.button);
        final Button btnSimpan = (Button) findViewById(R.id.Simpan);
        final TextView userName = (TextView) findViewById(R.id.txt_userName);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditAkun.this, com.climo.al.climoproject.Activities.MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        userData();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = userName.getText().toString();





                if(name.isEmpty())
                {
                    //error message
                    showMessage("Mohon Cek Kembali Data Anda");

                }
                else if(pickedImgUri == null){
                    //update user account
                    pBar.setVisibility(View.VISIBLE);
                    btnSimpan.setVisibility(View.INVISIBLE);
                    updateUserInfoName(name, mAuth.getCurrentUser());

                }
                else{
                    //update user account
                    pBar.setVisibility(View.VISIBLE);
                    btnSimpan.setVisibility(View.INVISIBLE);
                    updateUserInfo(name,pickedImgUri, mAuth.getCurrentUser());
                }
            }
        });

        imgUser = findViewById(R.id.img_user);

        imgUser = findViewById(R.id.img_user);
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }
                else
                {
                    openGallery();
                }

            }


        });
    }

    //update user info
    private void updateUserInfo(final String name, Uri pickedImgUri, final FirebaseUser currentUser) {
        pBar = (ProgressBar) findViewById(R.id.pBar);
        final Map<String, String> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("image", pickedImgUri.toString());
        final Button btnSimpan = (Button) findViewById(R.id.Simpan);
        final String userId = mAuth.getCurrentUser().getUid();
        //upload user photo to firebase storage and get url
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photo").child(userId + ".jpg");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //image upload success
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){



                                            //user info update

                                            firebaseFirestore.collection("Users").document(userId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()){
                                                        pBar.setVisibility(View.INVISIBLE);
                                                        btnSimpan.setVisibility(View.VISIBLE);
                                                        updateUI();
                                                        showMessage("Update Data Complete");

                                                    }else
                                                    {
                                                        String error = task.getException().getMessage();
                                                    }


                                                }
                                            });


                                        }
                                    }
                                });
                    }
                });
            }
        });



    }
    //update user info
    private void updateUserInfoName(final String name, final FirebaseUser currentUser) {
        pBar = (ProgressBar) findViewById(R.id.pBar);
        final Button btnSimpan = (Button) findViewById(R.id.Simpan);
        final Map<String, String> userMap = new HashMap<>();
        userMap.put("name", name);

        final String userId = mAuth.getCurrentUser().getUid();

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //user info update

                                            firebaseFirestore.collection("Users").document(userId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()){
                                                        pBar.setVisibility(View.INVISIBLE);
                                                        btnSimpan.setVisibility(View.VISIBLE);
                                                        updateUI();
                                                        showMessage("Update Data Complete");
                                                    }else
                                                    {
                                                        String error = task.getException().getMessage();
                                                    }


                                                }
                                            });

                                        }
                                    }
                                });

    }

    private void updateUI() {
        Intent intent = new Intent(getApplicationContext(), EditAkun.class);
        startActivity(intent);
        finish();
    }

    //toast message
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();

    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(EditAkun.this, android.Manifest.permission.READ_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(EditAkun.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){

                Toast.makeText(EditAkun.this, "Please accept for required permission",Toast.LENGTH_SHORT).show();

            }
            else
            {
                ActivityCompat.requestPermissions(EditAkun.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
        {
            openGallery();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null){

            //user has successfully picked an image
            pickedImgUri = data.getData();
            imgUser.setImageURI(pickedImgUri);
        }

    }

    private void openGallery() {
        //TODO : open gallery intent and wait for user to pick an image
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESTCODE);
    }

    public void userData() {
        TextView navUsername = (TextView) findViewById(R.id.txt_userName);
        TextView navMail = (TextView) findViewById(R.id.txt_userEmail);
        ImageView navImg = (ImageView) findViewById(R.id.img_user);

        navUsername.setText(currentUSer.getDisplayName());
        navMail.setText(currentUSer.getEmail());
        Glide.with(this).load(currentUSer.getPhotoUrl()).into(navImg);


    }


}
