package isilacar.kacyadayakala;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.*;
import com.google.android.material.textfield.TextInputEditText;

public class AnaEkran extends AppCompatActivity {

    private TextInputEditText tiet;
    private Button btnBasla, btnBilgi;
    private AdView banner;
    private InterstitialAd gecis;
    String tamEkranAd = "ca-app-pub-5037089565212879/4055844325";


    private AdRequest gecisReklam;
    Context context = this;


    private Animation upToDown,downToUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SharedPreferences sp = getSharedPreferences("Kayıtlar", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();

        MobileAds.initialize(context,
                "ca-app-pub-5037089565212879~8980867949");


        upToDown= AnimationUtils.loadAnimation(context,R.anim.yukardanasagiya);
        downToUp=AnimationUtils.loadAnimation(context,R.anim.asagidanyukari);




        banner = findViewById(R.id.bannerAnaEkran);
        AdRequest bannerReklam = new AdRequest.Builder().build();
        banner.loadAd(bannerReklam);

        gecis = new InterstitialAd(context);
        gecis.setAdUnitId(tamEkranAd);
        gecisReklam = new AdRequest.Builder().build();
        gecis.loadAd(gecisReklam);

        tiet = findViewById(R.id.isim);
        btnBasla = findViewById(R.id.oyunBasla);
        btnBilgi = findViewById(R.id.bilgi);

        tiet.setText(sp.getString("isim", null));
        tiet.setAnimation(upToDown);
        btnBasla.setAnimation(downToUp);
        btnBilgi.setAnimation(downToUp);
        btnBasla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(tiet.getText().toString())) {
                    LayoutInflater li=getLayoutInflater();
                    View view=li.inflate(R.layout.isimalani_toast,(ViewGroup)findViewById(R.id.ll));
                    Toast toast=new Toast(context);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP,20,30);
                    toast.setView(view);
                    toast.show();

                   // Toast.makeText(AnaEkran.this, "Lütfen bir karakter ismi belirleyin..", Toast.LENGTH_SHORT).show();
                } else {

                    editor.putString("isim", tiet.getText().toString());
                    editor.commit();

                    Intent intent = new Intent(AnaEkran.this, OyunEkrani.class);
                    intent.putExtra("isim", String.valueOf(tiet));
                    startActivity(intent);
                }

            }
        });


        btnBilgi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent i = new Intent(AnaEkran.this, Bilgi.class);

                if (gecis.isLoaded()) {
                    gecis.show();
                } else {
                    startActivity(i);
                }
                gecis.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        //super.onAdClosed();
                        gecis.loadAd(gecisReklam);
                        startActivity(i);
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        customExitPlan();
    }

    public void customExitPlan() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.cikisdialog);

        Button evet = dialog.findViewById(R.id.dialogEvet);
        Button hayir = dialog.findViewById(R.id.dialogHayir);

        evet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        });

        hayir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
