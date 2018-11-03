package com.footballnukes.moreorlessfootballers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by moshe on 27/04/2017.
 */

public class NewSplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    StringBuilder namesBuilder = new StringBuilder();
    StringBuilder positionsBuilder = new StringBuilder();
    StringBuilder clubBuilder = new StringBuilder();
    StringBuilder ageBuilder = new StringBuilder();
    StringBuilder imageUrlBuilder = new StringBuilder();
    StringBuilder beautyImageURLBuilder = new StringBuilder();
    StringBuilder valueBuilder = new StringBuilder();
    StringBuilder goalsThisSeasonBuilder = new StringBuilder();
    StringBuilder instaIDBuilder = new StringBuilder();
    StringBuilder instaNumberBuilder = new StringBuilder();

    StringBuilder wikiImages = new StringBuilder();
    StringBuilder wikiImageAuthors = new StringBuilder();


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference all_data = mRootRef.child("moreorlessfootballers");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = this.getSharedPreferences("Download Data",0);
        editor = sharedPreferences.edit();
        editor.apply();


        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray data = obj.getJSONArray("data");
            for (int i = 0; i < data.length(); i++){
                JSONObject player = (JSONObject) data.get(i);
                namesBuilder.append(player.getString("name")+",");
                positionsBuilder.append(player.getString("position")+",");
                ageBuilder.append(player.getString("age")+",");
                //imageUrlBuilder.append(player.getString("imageURL")+",");
                //beautyImageURLBuilder.append(player.getString("beautyImageURL")+",");
                wikiImages.append(player.getString("wikipedia_image_url")+"arusak");
                wikiImageAuthors.append(player.getString("author_name")+"arusak");
                clubBuilder.append(player.getString("club")+",");
                goalsThisSeasonBuilder.append(player.getString("goals")+",");
                valueBuilder.append(player.getString("value")+",");
                instaIDBuilder.append(player.getString("instagram_id")+",");
                instaNumberBuilder.append(player.getString("instagram_follower")+",");
            }

            namesBuilder.deleteCharAt(namesBuilder.length()-1);
            editor.putString("Data Names",namesBuilder.toString());

            positionsBuilder.deleteCharAt(positionsBuilder.length()-1);
            editor.putString("Data Positions",positionsBuilder.toString());

            ageBuilder.deleteCharAt(ageBuilder.length()-1);
            editor.putString("Data Ages",ageBuilder.toString());


            clubBuilder.deleteCharAt(clubBuilder.length()-1);
            editor.putString("Data Clubs",clubBuilder.toString());


            valueBuilder.deleteCharAt(valueBuilder.length()-1);
            editor.putString("Data Values",valueBuilder.toString());



            wikiImages.deleteCharAt(wikiImages.length()-6);
            wikiImageAuthors.deleteCharAt(wikiImageAuthors.length()-6);
            editor.putString("Data WikiImages",wikiImages.toString());
            editor.putString("Data WikiImageAuthors",wikiImageAuthors.toString());


            goalsThisSeasonBuilder.deleteCharAt(goalsThisSeasonBuilder.length()-1);
            editor.putString("Data GoalsThisSeason",goalsThisSeasonBuilder.toString());


            instaIDBuilder.deleteCharAt(instaIDBuilder.length()-1);
            editor.putString("Data InstaIDs",instaIDBuilder.toString());


            instaNumberBuilder.deleteCharAt(instaNumberBuilder.length()-1);
            editor.putString("Data InstaNumbers",instaNumberBuilder.toString());

            editor.putBoolean("Data Downloaded",true);
            editor.apply();

            Intent home = new Intent(NewSplashActivity.this,HomeActivity.class);
            startActivity(home);
            NewSplashActivity.this.finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getResources().getAssets().open("all_data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
