package com.germiyanoglu.android.firebasesimpleapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

public class KullaniciKayit extends AppCompatActivity {


    @BindView(R.id.kayit_baslik)
    TextView baslik;


    @BindView(R.id.kayit_resim)
    ImageView resim;


    @BindView(R.id.kayit_email)
    EditText email;


    @BindView(R.id.kayit_ad_soyad)
    EditText adSoyad;


    @BindView(R.id.kayit_sifre)
    EditText sifre;


    @BindView(R.id.kayit)
    Button kayit;


    @BindView(R.id.giris)
    Button giris;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_kayit);
        ButterKnife.bind(this);


        resim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*"); // hangi türde veriler
                intent.setAction(Intent.ACTION_PICK); // Intent.ACTION_PICK GALARİYİ AÇMA
                startActivityForResult(Intent.createChooser(intent, "Resim Seç"), 1);


            }
        });


        kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uri != null) {
                    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference myRef = database.getReference();
                    final FirebaseStorage storage = FirebaseStorage.getInstance();
                    final StorageReference storageRef = storage.getReference();

                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), sifre.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        // Email alanı
                                        UploadTask uploadTask = storageRef.child(email.getText().toString()).putFile(uri);

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

                                                    Map<String, String> map = new HashMap<>();
                                                    map.put("email", email.getText().toString());
                                                    map.put("sifre", sifre.getText().toString());
                                                    map.put("adSoyad", adSoyad.getText().toString());
                                                    map.put("resim", downloadUri);

                                                    myRef.child("kullanicilar").child(mAuth.getCurrentUser().getUid()).setValue(map);

                                                    Toast.makeText(getApplicationContext(),"Register Completed",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    } else {

                                    }

                                }
                            });
                }

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1) {

            // Seçtiğimiz resmin bilgileri
            uri = data.getData();

            try {
                resim.setImageURI(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
