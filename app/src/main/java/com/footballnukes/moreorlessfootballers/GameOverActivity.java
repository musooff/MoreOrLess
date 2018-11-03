package com.footballnukes.moreorlessfootballers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;


import com.footballnukes.moreorlessfootballers.beautifiers.FontTextView;
import com.footballnukes.moreorlessfootballers.beautifiers.GameButton;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by moshe on 20/04/2017.
 */

public class GameOverActivity extends GameActivity {

    final public UnityAdsListener unityAdsListener = new UnityAdsListener();

    GameButton go_playAgain,go_keepGoing;
    LinearLayout go_share,go_tweet,go_other;
    FontTextView go_score;
    int go_last_id, go_int_score;
    //Integer[] win_gifs = {R.drawable.win_1,R.drawable.win_2,R.drawable.win_3,R.drawable.win_4,R.drawable.win_5, R.drawable.win_6, R.drawable.win_7};
    //Integer[] lose_gifs = {R.drawable.fail_1,R.drawable.fail_2,R.drawable.fail_3,R.drawable.fail_4,R.drawable.fail_5};
    GifImageView gifImageView;
    InterstitialAd mInterstitialAd;


    LinearLayout ll_main;
    LinearLayout ll_youtube,ll_facebook;

    public static String GameURL = "https://play.google.com/store/apps/details?id=com.footballnukes.moreorlessfootballers";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        ll_main = (LinearLayout)findViewById(R.id.ll_main);
        CountDownTimer countDownTimer  = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                ll_main.setVisibility(View.VISIBLE);
            }
        };
        //countDownTimer.start();

        UnityAds.initialize(this, "1397555", unityAdsListener);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                beginPlayingGame();
            }
        });



        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.app_ads_id));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        if (sharedPreferences.getBoolean("paid",false)){
            mAdView.setVisibility(View.GONE);
        }
        else {
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            requestNewInterstitial();
        }

        go_playAgain = (GameButton)findViewById(R.id.bt_restart);
        go_keepGoing = (GameButton)findViewById(R.id.keep_going);

        go_share = (LinearLayout) findViewById(R.id.ll_share);
        go_tweet = (LinearLayout)findViewById(R.id.ll_tweet);
        go_other = (LinearLayout)findViewById(R.id.ll_other);

        gifImageView = (GifImageView)findViewById(R.id.gif);

        go_score = (FontTextView)findViewById(R.id.tv_score);

        Bundle extras = getIntent().getExtras();
        go_last_id = extras.getInt("previous");
        go_int_score = extras.getInt("score");
        go_score.setText(go_int_score+"");


/*
        Random random = new Random();
        if (go_int_score >= 5){
            gifImageView.setImageResource(win_gifs[random.nextInt(7)]);
        }
        else {
            gifImageView.setImageResource(lose_gifs[random.nextInt(5)]);

        }
        */

        go_playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    beginPlayingGame();
                }
            }
        });

        go_keepGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UnityAds.isReady()) {
                    UnityAds.show(GameOverActivity.this);
                    onBackPressed();

                }
            }
        });

        go_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(getApplicationContext(),score,"com.twitter.android");
            }
        });

        go_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(getApplicationContext(),score,"com.facebook.katana");
            }
        });

        go_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(getApplicationContext(),score,null);

            }
        });

        ll_facebook = (LinearLayout)findViewById(R.id.ll_facebook);
        ll_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(getApplicationContext());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
        ll_youtube = (LinearLayout)findViewById(R.id.ll_youtube);
        ll_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/c/footballnukes")));
            }
        });


    }

    private void beginPlayingGame() {
        Intent restart = new Intent(GameOverActivity.this,GameActivity.class);
        startActivity(restart);
        setResult(58);
        GameOverActivity.this.finish();
    }

    private class UnityAdsListener implements IUnityAdsListener{

        @Override
        public void onUnityAdsReady(String s) {

        }

        @Override
        public void onUnityAdsStart(String s) {

        }

        @Override
        public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
            watched();

        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {

        }
    }

    public void watched(){
        editor.putBoolean("watched",true);
        editor.apply();

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("91EA9768AB496C541B00CBD148B25849")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    public static String FACEBOOK_URL = "https://www.facebook.com/footballnukes";
    public static String FACEBOOK_PAGE_ID = "634136046769198";

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    public static void share(Context context,int level, String pack)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String text = "I scored "+ level + ". What's your score?";
        sendIntent.putExtra(Intent.EXTRA_TEXT, text + GameURL);
        sendIntent.setType("text/plain");
        try {
            int versionCode =  context.getPackageManager().getPackageInfo(pack, 0).versionCode;
            if (versionCode !=0){
                if (pack != null){
                    sendIntent.setPackage(pack);

                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        context.startActivity(sendIntent);
    }

}
