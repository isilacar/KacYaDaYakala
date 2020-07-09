package isilacar.kacyadayakala;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Timer;
import java.util.TimerTask;

public class OyunEkrani extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private TextView tvPuan, tvAltyazi;
    private ImageView kirmiziDaire, maviDortgen, pembeKalp, sariYildiz, siyahKare, yesilUcgen, karakter;

    private int puan = 0;

    //karakterimizim  X Y pozisyonlarını belirleyelim.
    private int karakterX, karakterY;


    //oyunumuz sırasında ekrana dokunma oldu mu olmadı mı
    private boolean ekranaDokunma = false;

    //oyun ekranındaki ilk ekrana dokunmayı alıcaz.
    private boolean oyunaBaslama = false;


    private Timer timer = new Timer();

    private Handler handler = new Handler();

    private int ekranGenisligi, ekranYuksekligi, karakterYuksekligi, karakterGenisligi;

    private int kirmiziDaireX, kirmiziDaireY, maviDortgenX, maviDortgenY, pembeKalpX, pembeKalpY, sariYildizX, sariYildizY, siyahKareX,
            siyahKareY, yesilUcgenX, yesilUcgenY;

    private AdRequest gecisReklam;
    private InterstitialAd gecis;
    String tamEkranAd = "ca-app-pub-5037089565212879/4055844325";

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oyun_ekrani);

        gecis = new InterstitialAd(context);
        gecis.setAdUnitId(tamEkranAd);
        gecisReklam = new AdRequest.Builder().build();
        gecis.loadAd(gecisReklam);

        constraintLayout = findViewById(R.id.cl);

        tvPuan = findViewById(R.id.oyunekraniPuan);
        tvAltyazi = findViewById(R.id.altyazi);

        karakter = findViewById(R.id.anakarakter);

        kirmiziDaire = findViewById(R.id.kirmizi);
        maviDortgen = findViewById(R.id.mavi);
        pembeKalp = findViewById(R.id.pembe);
        sariYildiz = findViewById(R.id.sari);
        siyahKare = findViewById(R.id.siyah);
        yesilUcgen = findViewById(R.id.yesil);

        //başlangıçta cisimleri ekranın dışına çıkarıyoruz
        kirmiziDaire.setX(-80);
        maviDortgen.setX(-80);
        pembeKalp.setX(-80);
        sariYildiz.setX(-80);
        siyahKare.setX(-80);
        yesilUcgen.setX(-80);

        kirmiziDaire.setY(-80);
        maviDortgen.setY(-80);
        pembeKalp.setY(-80);
        sariYildiz.setY(-80);
        siyahKare.setY(-80);
        yesilUcgen.setY(-80);


        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                /*ekrana ilk dokunma oyuna başlamak için olacağından bu ilk tıklamayla hareketleri pasaif yapıyoruz.
                 Oyun ekranı ilk açıldığında oyunaBaslama false olarak geliyor. */

                if (oyunaBaslama) {
                    //Ekrana dokunulduğunda yapılcaklar

                    tvAltyazi.setVisibility(View.INVISIBLE);
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Log.i("dokundu", "EKRANA DOKUNULDU");
                        ekranaDokunma = true;


                    }
                    //dokunmayı bıraktığında yapılacaklar
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        Log.i("cekti", "EKRANDAN ELİNİ ÇEKTİ");
                        ekranaDokunma = false;
                        karakterY += 20;

                    }

                } else {
                    //ekrana ilk kez burada dokunuyoruz ve oyuna başlıyoruz, Timerı da burada  çalıştırıyoruz

                    oyunaBaslama = true;

                    //karakterimizin X ve Y koordinatlarını alıyoruz
                    karakterX = (int) karakter.getX();
                    karakterY = (int) karakter.getY();


                    ekranGenisligi = constraintLayout.getWidth();
                    ekranYuksekligi = constraintLayout.getHeight();

                    karakterGenisligi = karakter.getWidth();
                    karakterYuksekligi = karakter.getHeight();

                    //20 ms de bir handler içindekileri yap diyoruz
                    timer.schedule(new TimerTask() {


                        @Override
                        public void run() {

                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    karakterHareketEttir();
                                    cisimleriHareketEttir();
                                    cisimlerinCarpmasi();

                                }
                            });

                        }
                    }, 0, 20);//20ms aralıklarla çalış diyoruz,0 saniye gecikmeyle

                }
                return true;
            }
        });


    }

    public void karakterHareketEttir() {
        if (ekranaDokunma) {

            karakterY -= 20;
        } else {
            karakterY += 20;
        }

        //karakterimiz yukarı giderken ekrandan çıkmaması için bu kontrolü yapıyoruz
        if (karakterY <= 0) {
            karakterY = 0;
        }

        //aşağıya giderken de ekrandan çıkmaması için,ekranın uzunluğundan,cismin uzunluğunu çıkarıp
        // karakterimizin Y sini buna güncelliyoruz
        if (karakterY >= ekranYuksekligi - karakterYuksekligi) {
            karakterY = ekranYuksekligi - karakterYuksekligi;
        }

        //karakterimizin güncel değerini belirtiyoruz
        karakter.setY(karakterY);
    }

    public void cisimleriHareketEttir() {
        //her 20ms de bir X ekseninde -30 olarak(sağdan sola doğru) hareket edecek.
        siyahKareX -= 15;

        if (siyahKareX < 0) {
            siyahKareX = ekranGenisligi + 20;
            siyahKareY = (int) Math.floor(Math.random() * ekranYuksekligi);//ekran yüksekliğimiz kadar bize rastgele bir sayı üret
            //ve Y koordinatını ordan belirle

        }
        siyahKare.setX(siyahKareX);
        siyahKare.setY(siyahKareY);

        kirmiziDaireX -= 18;

        if (kirmiziDaireX < 0) {
            kirmiziDaireX = ekranGenisligi + 20;
            kirmiziDaireY = (int) Math.floor(Math.random() * ekranYuksekligi);//ekran yüksekliğimiz kadar bize rastgele bir sayı üret
            //ve Y koordinatını ordan belirle

        }
        kirmiziDaire.setX(kirmiziDaireX);
        kirmiziDaire.setY(kirmiziDaireY);

        yesilUcgenX -= 12;

        if (yesilUcgenX < 0) {
            yesilUcgenX = ekranGenisligi + 20;
            yesilUcgenY = (int) Math.floor(Math.random() * ekranYuksekligi);//ekran yüksekliğimiz kadar bize rastgele bir sayı üret
            //ve Y koordinatını ordan belirle

        }
        yesilUcgen.setX(yesilUcgenX);
        yesilUcgen.setY(yesilUcgenY);

        pembeKalpX -= 5;

        if (pembeKalpX < 0) {
            pembeKalpX = ekranGenisligi + 20;
            pembeKalpY = (int) Math.floor(Math.random() * ekranYuksekligi);//ekran yüksekliğimiz kadar bize rastgele bir sayı üret
            //ve Y koordinatını ordan belirle

        }
        pembeKalp.setX(pembeKalpX);
        pembeKalp.setY(pembeKalpY);

        sariYildizX -= 10;

        if (sariYildizX < 0) {
            sariYildizX = ekranGenisligi + 20;
            sariYildizY = (int) Math.floor(Math.random() * ekranYuksekligi);//ekran yüksekliğimiz kadar bize rastgele bir sayı üret
            //ve Y koordinatını ordan belirle

        }
        sariYildiz.setX(sariYildizX);
        sariYildiz.setY(sariYildizY);

        maviDortgenX -= 25;

        if (maviDortgenX < 0) {
            maviDortgenX = ekranGenisligi + 20;
            maviDortgenY = (int) Math.floor(Math.random() * ekranYuksekligi);//ekran yüksekliğimiz kadar bize rastgele bir sayı üret
            //ve Y koordinatını ordan belirle

        }
        maviDortgen.setX(maviDortgenX);
        maviDortgen.setY(maviDortgenY);

    }

    public void cisimlerinCarpmasi() {

        //tüm cisimlerin merkezX ve merkezY lerini bulalım
        int siyahKareMerkezX = siyahKareX + siyahKare.getWidth() / 2;
        int siyahKareMerkezY = siyahKareY + siyahKare.getHeight() / 2;

        int kirmiziDaireMerkezX = kirmiziDaireX + kirmiziDaire.getWidth() / 2;
        int kirmiziDaireMerkezY = kirmiziDaireY + kirmiziDaire.getHeight() / 2;

        int maviDortgenMerkezX = maviDortgenX + maviDortgen.getWidth() / 2;
        int maviDortgenMerkezY = maviDortgenY + maviDortgen.getHeight() / 2;

        int sariYildizMerkezX = sariYildizX + sariYildiz.getWidth() / 2;
        int sariYildizMerkezY = sariYildizY + sariYildiz.getHeight() / 2;

        int yesilUcgenMerkezX = yesilUcgenX + yesilUcgen.getWidth() / 2;
        int yesilUcgenMerkezY = yesilUcgenY + yesilUcgen.getHeight() / 2;

        int pembeKalpMerkezX = pembeKalpX + pembeKalp.getWidth() / 2;
        int pembeKalpMerkezY = pembeKalpY + pembeKalp.getHeight() / 2;


        if (maviDortgenMerkezX >= 0 && maviDortgenMerkezX <= karakterGenisligi
                && karakterY <= maviDortgenMerkezY && maviDortgenMerkezY <= karakterY + karakterYuksekligi) {

            puan += 40;
            maviDortgenX = -20;


        }
        if (siyahKareMerkezX >= 0 && siyahKareMerkezX <= karakterGenisligi
                && karakterY <= siyahKareMerkezY && siyahKareMerkezY <= karakterY + karakterYuksekligi) {


            siyahKareX = -20;
            timer.cancel();
            timer = null;

            final Intent intent = new Intent(OyunEkrani.this, SkorEkrani.class);
            intent.putExtra("skor", puan);
            if (gecis.isLoaded()) {
                gecis.show();
            } else {
                startActivity(intent);
                finish();
            }

            gecis.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    //super.onAdClosed();
                    gecis.loadAd(gecisReklam);
                    startActivity(intent);
                    finish();
                }
            });



        }
        if (kirmiziDaireMerkezX >= 0 && kirmiziDaireMerkezX <= karakterGenisligi
                && karakterY <= kirmiziDaireMerkezY && kirmiziDaireMerkezY <= karakterY + karakterYuksekligi) {

            puan = 0;
            kirmiziDaireX = -20;


        }
        if (sariYildizMerkezX >= 0 && sariYildizMerkezX <= karakterGenisligi
                && karakterY <= sariYildizMerkezY && sariYildizMerkezY <= karakterY + karakterYuksekligi) {

            puan += 20;
            sariYildizX = -20;


        }
        if (yesilUcgenMerkezX >= 0 && yesilUcgenMerkezX <= karakterGenisligi
                && karakterY <= yesilUcgenMerkezY && yesilUcgenMerkezY <= karakterY + karakterYuksekligi) {

            puan += 25;
            yesilUcgenX = -20;


        }
        if (pembeKalpMerkezX >= 0 && pembeKalpMerkezX <= karakterGenisligi
                && karakterY <= pembeKalpMerkezY && pembeKalpMerkezY <= karakterY + karakterYuksekligi) {

            puan += 10;
            pembeKalpX = -20;


        }
        tvPuan.setText(String.valueOf(puan));

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
