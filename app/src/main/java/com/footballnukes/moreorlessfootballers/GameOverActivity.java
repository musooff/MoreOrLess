package com.footballnukes.moreorlessfootballers;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.footballnukes.moreorlessfootballers.model.AppPreference;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;


import com.footballnukes.moreorlessfootballers.beautifiers.FontTextView;
import com.footballnukes.moreorlessfootballers.beautifiers.GameButton;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by moshe on 20/04/2017.
 */

public class GameOverActivity extends Activity {

    public static String FACEBOOK_URL = "https://www.facebook.com/ballboytv";
    public static String FACEBOOK_PAGE_ID = "634136046769198";

    GameButton go_playAgain,go_keepGoing;
    LinearLayout go_share,go_tweet,go_other;
    FontTextView go_score;
    int go_int_score;
    InterstitialAd mInterstitialAd;
    LinearLayout ll_youtube,ll_facebook;
    private RewardedVideoAd mRewardedVideoAd;

    public static String GameURL = "https://play.google.com/store/apps/details?id=com.footballnukes.moreorlessfootballers";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        manageAds();

        go_playAgain = findViewById(R.id.bt_restart);
        go_keepGoing = findViewById(R.id.keep_going);
        go_share = findViewById(R.id.ll_share);
        go_tweet = findViewById(R.id.ll_tweet);
        go_other = findViewById(R.id.ll_other);
        go_score = findViewById(R.id.tv_score);

        Bundle extras = getIntent().getExtras();
        go_int_score = extras.getInt("score");
        go_score.setText(go_int_score+"");

        go_playAgain.setOnClickListener(v -> {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                beginPlayingGame("playAgain");
            }
        });

        go_keepGoing.setOnClickListener(v -> {
            if (mRewardedVideoAd.isLoaded()) {
                mRewardedVideoAd.show();
            }else {
                Toast.makeText(getApplicationContext(), "Ad hasn't been loaded, Try again later", Toast.LENGTH_SHORT).show();
            }
        });

        go_tweet.setOnClickListener(v -> share(getApplicationContext(),go_int_score,"com.twitter.android"));

        go_share.setOnClickListener(v -> share(getApplicationContext(),go_int_score,"com.facebook.katana"));

        go_other.setOnClickListener(v -> share(getApplicationContext(),go_int_score,null));

        ll_facebook = findViewById(R.id.ll_facebook);
        ll_facebook.setOnClickListener(v -> {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookPageURL(getApplicationContext());
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
        });
        ll_youtube = findViewById(R.id.ll_youtube);
        ll_youtube.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/c/theballboy"))));

    }

    private void manageAds(){
        // video ads
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                beginPlayingGame("videoAdWatched");

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
        mRewardedVideoAd.loadAd(getString(R.string.video_ad_id), new AdRequest.Builder().build());


        // inter ads
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.inter_ad_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                beginPlayingGame("interAdClosed");
            }
        });

        // banner ads
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.app_ads_id));
        AdView mAdView = findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    private void beginPlayingGame(String source) {
        Intent restart = new Intent();
        restart.putExtra("source", source);
        setResult(58, restart);
        GameOverActivity.this.finish();
    }


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


    @Override
    public void onBackPressed() {
        beginPlayingGame("onBackPressed");
    }
}
