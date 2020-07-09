package isilacar.kacyadayakala;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class SkorEkrani extends AppCompatActivity {

    private TextView tvIsim, tvPuan, tvEnyuksekskor;
    private Button btnTekrar;

    private AdView banner;
    private InterstitialAd gecis;
    private AdRequest gecisReklam;
    String tamEkranAd = "ca-app-pub-5037089565212879/4055844325";

    //banner -- ca-app-pub-5037089565212879/3728541260

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skor_ekrani);

        banner = findViewById(R.id.bannerSkor);
        AdRequest bannerReklam = new AdRequest.Builder().build();
        banner.loadAd(bannerReklam);

      /*  gecis = new InterstitialAd(context);
        gecis.setAdUnitId(tamEkranAd);
        gecisReklam = new AdRequest.Builder().build();
        gecis.loadAd(gecisReklam);
*/
        tvIsim = findViewById(R.id.skorIsim);
        tvPuan = findViewById(R.id.puan);
        tvEnyuksekskor = findViewById(R.id.enyuksekskor);
        btnTekrar = findViewById(R.id.skorTekrarOynaButon);

        //en yüksek skoru kayıt edelim
        SharedPreferences sp = getSharedPreferences("Kayıtlar", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        String isim = sp.getString("isim", null);
        tvIsim.setText(isim.toUpperCase());

        int skor = getIntent().getIntExtra("skor", 0);
        tvPuan.setText(String.valueOf(skor));


        int enYuksekSkor = sp.getInt("enYuksekSkor", 0);

        if (skor > enYuksekSkor) {

            editor.putInt("enYuksekSkor", skor);
            editor.commit();

            tvEnyuksekskor.setText(String.valueOf(skor));
        } else {
            tvEnyuksekskor.setText(String.valueOf(enYuksekSkor));
        }


        btnTekrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SkorEkrani.this, OyunEkrani.class));
                finish();
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
