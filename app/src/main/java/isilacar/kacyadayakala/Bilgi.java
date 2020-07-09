package isilacar.kacyadayakala;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Bilgi extends AppCompatActivity {

    private Button btnAnaekran;

    private AdView banner;

    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilgi);

        banner=findViewById(R.id.bannerBilgi);
        AdRequest bannerReklam=new AdRequest.Builder().build();
        banner.loadAd(bannerReklam);

        btnAnaekran = findViewById(R.id.bilgiAnaekranButton);


        btnAnaekran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Bilgi.this, AnaEkran.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

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
