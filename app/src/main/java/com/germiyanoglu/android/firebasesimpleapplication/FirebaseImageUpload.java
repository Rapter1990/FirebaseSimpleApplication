package com.germiyanoglu.android.firebasesimpleapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirebaseImageUpload {

    private static final String TAG = MainActivity.class.getName();

    /*@BindView(R.id.email)
    EditText email;

    @BindView(R.id.password)
    EditText sifre;*/

    /*@BindView(R.id.id)
    TextView key;

    @BindView(R.id.isim)
    TextView isim;

    @BindView(R.id.soyisim)
    TextView soyisim;*/
    /*
    @BindView(R.id.resim)
    ImageView imageView;

    @BindView(R.id.kaydet)
    Button kaydet;

    @BindView(R.id.yukle)
    Button yukle;

    Uri uri;

    @BindView(R.id.resim1)
    ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.upload_image);
        ButterKnife.bind(this);

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*"); // hangi türde veriler
                intent.setAction(Intent.ACTION_PICK); // Intent.ACTION_PICK GALARİYİ AÇMA
                startActivityForResult(Intent.createChooser(intent,"Select Photo"),1);



            }


        });


        yukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(uri != null){

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    final StorageReference storageRef = storage.getReference().child("image");
                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

                    UploadTask uploadTask = storageRef.putFile(uri);
                    progressDialog.show();


                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return storageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                String downloadUri = task.getResult().toString();
                                Toast.makeText(getApplicationContext(),"Uploaded",Toast.LENGTH_SHORT).show();
                                progressDialog.hide();

                                Picasso.with(getApplicationContext())
                                        .load(downloadUri)
                                        .into(imageView1);
                            }
                        }
                    });


                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {  // Yükleme Durumu
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("%" + (int)progress + " downloading.");
                        }
                    });

                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == 1){

            // Seçtiğimiz resmin bilgileri
            uri = data.getData();

            try {
                imageView.setImageURI(uri);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    /*private void DatabaseandAuthentication() {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("users");

        final String username = email.getText().toString();
        final String password = sifre.getText().toString();

        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            mAuth.signInWithEmailAndPassword(username,password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        String id = mAuth.getCurrentUser().getUid();

                                        Map<String,String> userMap = new HashMap<>();

                                        userMap.put("email",email.getText().toString());
                                        userMap.put("password",sifre.getText().toString());

                                        myRef.child(id).setValue(userMap);

                                        myRef.child(id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                                                key.setText(dataSnapshot.getKey());
                                                isim.setText(map.get("email").toString());
                                                soyisim.setText(map.get("password").toString());

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                    }else {

                                    }

                                }
                            });

                        } else {

                        }

                    }
                });
    }

    private void AuthenticationSample() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        String username = email.getText().toString();
        String password = sifre.getText().toString();

        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Başarılı", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Başarısız", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    private void VeriKaydetme() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("users").push();

        Kullanıcılar kullanıcı = new Kullanıcılar();
        kullanıcı.setAd(email.getText().toString().trim());
        kullanıcı.setSoyad(sifre.getText().toString().trim());

        myRef.setValue(kullanıcı);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                key.setText(dataSnapshot.getKey());
                isim.setText(map.get("ad").toString());
                soyisim.setText(map.get("soyad").toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

}

