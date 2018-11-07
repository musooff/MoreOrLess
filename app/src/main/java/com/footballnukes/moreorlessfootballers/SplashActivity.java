package com.footballnukes.moreorlessfootballers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import com.footballnukes.moreorlessfootballers.game.Player;
import com.footballnukes.moreorlessfootballers.model.AppPreference;
import com.footballnukes.moreorlessfootballers.room.AppDatabase;
import com.footballnukes.moreorlessfootballers.room.dao.PlayerDao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by moshe on 27/04/2017.
 */

public class SplashActivity extends Activity {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference playersRef = mRootRef.child("name_players");

    private PlayerDao playerDao;

    private boolean isHomeStarted = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        playerDao = AppDatabase.Companion.getInstance(getApplicationContext()).playerDao();

        checkAndStart();

    }

    private void checkAndStart(){
        Observable.fromCallable(() -> playerDao.getCount())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> {
                    if (count > 0){
                        isHomeStarted = true;
                        startHomeActivity();
                        if (getAppPref().isConnected()) updateValues();
                    }
                    else {
                        if (getAppPref().isConnected()) updateValues();
                        else showErrorMessage();
                    }
                });
    }

    private void startHomeActivity(){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void showErrorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection!")
                .setMessage("For the first run internet access is required.")
                .setPositiveButton("Try again", (dialog, which) -> {
                    if (getAppPref().isConnected()) updateValues();
                    else showErrorMessage();
                })
                .setNegativeButton("Exit", ((dialog, which) -> finish()))
                .setCancelable(false)
                .create()
                .show();
    }

    private void updateValues(){
        playersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Player> players = new ArrayList<>();
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    players.add(child.getValue(Player.class));
                }
                updateDatabase(players).subscribe(() -> {
                    if (!isHomeStarted) {
                        isHomeStarted = true;
                        startHomeActivity();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private AppPreference getAppPref(){
        return new AppPreference(getApplicationContext());
    }

    private Completable updateDatabase(List<Player> players){
        return Completable.fromAction(
                () -> playerDao.insert(players))
                .subscribeOn(Schedulers.io());
    }
}
