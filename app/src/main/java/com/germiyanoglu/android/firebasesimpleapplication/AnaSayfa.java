package com.germiyanoglu.android.firebasesimpleapplication;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnaSayfa extends AppCompatActivity {


    @BindView(R.id.anasayfa_profil_image)
    ImageView profil_resim;


    @BindView(R.id.anasayfa_email)
    TextView anasayfa_email;


    @BindView(R.id.anasayfa_ad_soyad)
    TextView anasayfa_ad_soyad;


    @BindView(R.id.anasayfa_sifre)
    TextView anasayfa_sifre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_sayfa);
        ButterKnife.bind(this);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        if(auth.getCurrentUser() == null){

            // Geri tuşuna basıldığında bir önceki activitye gitmemek için .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
            // ve o acitivty gittiğnde onu belirlemek için
            startActivity(new Intent(this,MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK));
        }else{

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference()
                    .child("kullanicilar")
                    .child(auth.getCurrentUser().getUid());

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Map map = (Map) dataSnapshot.getValue();

                    Picasso.with(getApplicationContext())
                            .load(map.get("resim").toString())
                            .into(profil_resim);


                    anasayfa_email.setText(map.get("email").toString());
                    anasayfa_ad_soyad.setText(map.get("adSoyad").toString());
                    anasayfa_sifre.setText(map.get("sifre").toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


    }
}
