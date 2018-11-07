package com.footballnukes.moreorlessfootballers.game;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.footballnukes.moreorlessfootballers.R;
import com.footballnukes.moreorlessfootballers.model.AppPreference;
import com.footballnukes.moreorlessfootballers.room.AppDatabase;
import com.footballnukes.moreorlessfootballers.room.dao.PlayerDao;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

import com.footballnukes.moreorlessfootballers.beautifiers.FontTextView;
import com.footballnukes.moreorlessfootballers.beautifiers.GameButton;
import com.footballnukes.moreorlessfootballers.beautifiers.VSView;

/**
 * Created by moshe on 19/04/2017.
 */

public class GameActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    VSView vsView;
    ArrayList<GameItem> gameItems;
    String category;

    List<String> names = new ArrayList<>();
    List<String> subNames = new ArrayList<>();
    List<String> imageUrls = new ArrayList<>();
    List<String> credits = new ArrayList<>();
    List<Integer> whatToCompare = new ArrayList<>();

    int prev = 0;
    String prev_str = null;
    GameAdapter gameAdapter;

    FontTextView tv_number_2, tv_score_main,tv_high_score;
    int num_1,num_2;
    int score = 0,highScore = 0;

    RelativeLayout rl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        
        

        rl = findViewById(R.id.rl_root);
        category = getAppPref().getLastGame();

        getGameData()
                .subscribe(
                        players -> { 
                            for (Player player: players){
                                names.add(player.getName());
                                imageUrls.add(player.getWikipediaImageUrl());
                                credits.add(player.getAuthorName());
                                switch (category){
                                    case "Market Value":
                                        whatToCompare.add((int) player.getValue());
                                        subNames.add(player.getClub());
                                        break;
                                    case "Age":
                                        whatToCompare.add(player.getAge());
                                        subNames.add(player.getClub());
                                        break;
                                    case "Goals This Season":
                                        whatToCompare.add(player.getGoals());
                                        subNames.add(player.getClub());
                                        break;
                                    case "Instagram":
                                        whatToCompare.add((int) player.getInstagramFollowers());
                                        subNames.add("@"+player.getInstagramId());
                                        break;

                                }
                            }
                            startGame();
                            },
                        throwable -> Log.e("GAME", throwable.getMessage()));


        vsView = findViewById(R.id.vs_view);
        tv_high_score = findViewById(R.id.tv_high_score);
        tv_score_main = findViewById(R.id.tv_score_main);
        recyclerView = findViewById(R.id.rl_game_item);

    }

    private void defaults(){
        score = 0;
        vsView.bring_vs();
        tv_score_main.setText("Score: "+score);
    }

    private void startGame(){
        defaults();
        gameItems= new ArrayList<>();
        Random rr = new Random();
        int randomNumber = rr.nextInt(names.size());
        while (subNames.get(randomNumber).equals("null")){
            randomNumber = rr.nextInt(names.size());
        }
        if (category.equals("Instagram")){
            while (whatToCompare.get(randomNumber) == 0){
                randomNumber = rr.nextInt(names.size());
            }
        }
        gameItems.add(new GameItem(names.get(randomNumber),subNames.get(randomNumber),whatToCompare.get(randomNumber),0,false,null,imageUrls.get(randomNumber),credits.get(randomNumber)));
        num_1 = whatToCompare.get(randomNumber);
        prev = randomNumber;
        prev_str = names.get(randomNumber);

        gameItems.add(nextItem());

        num_2 = whatToCompare.get(prev);
        gameAdapter = new GameAdapter(this,gameItems);
        recyclerView.setAdapter(gameAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        highScore = getAppPref().getHighScores(category);
        tv_high_score.setText("Highscore: "+ highScore);
    }
    
    private Single<List<Player>> getGameData(){
        PlayerDao playerDao = AppDatabase.Companion.getInstance(getApplicationContext()).playerDao();
        return playerDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public AppPreference getAppPref(){
        return new AppPreference(getApplicationContext());
    }

    public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

        private ArrayList<GameItem> mGameItems;
        private Context mContext;


        GameAdapter(Context context, ArrayList<GameItem> gameItems) {
            mGameItems = gameItems;
            mContext = context;
        }

        private Context getContext() {
            return mContext;
        }

        @Override
        public GameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);


            int measuredHeight = parent.getMeasuredHeight();
            int measuredWidth = parent.getMeasuredWidth();
            if (getContext().getResources().getConfiguration().orientation == 2) {
                measuredWidth /= 2;
            } else {
                measuredHeight /= 2;
            }
            View gameView = inflater.inflate(R.layout.game_item, parent, false);
            gameView.setLayoutParams(new RecyclerView.LayoutParams(measuredWidth,measuredHeight));
            return new ViewHolder(gameView);
        }

        @Override
        public void onBindViewHolder(GameAdapter.ViewHolder holder, int position) {
            final GameItem gameItem = mGameItems.get(position);

            FontTextView name = holder.name;
            FontTextView username = holder.username;
            final FontTextView number = holder.number;
            FontTextView tv_category = holder.tv_category;
            tv_number_2 = holder.number;
            LinearLayout ll_searches_view = holder.ll_searches_view;
            LinearLayout ll_buttons = holder.ll_buttons;
            FontTextView searches_more = holder.searches_more;
            final ViewSwitcher viewSwitcher = holder.viewSwitcher;
            GameButton more = holder.more;
            GameButton less = holder.less;
            ImageView imageView = holder.background;
            ImageView card_image = holder.cardImage;
            FontTextView tv_attribution = holder.tv_attribution;
            tv_attribution.setText(gameItem.getAuthorName());

            Picasso.with(GameActivity.this).load(gameItem.getImage_url()).fit().into(imageView);

            if (gameItem.getImage_url() == null){
                Log.e("Doesn't have image",gameItem.getName());
            }


            more.setOnClickListener(v -> {
                viewSwitcher.showNext();
                onButtonClick(true);
            });

            less.setOnClickListener(v -> {
                viewSwitcher.showNext();
                onButtonClick(false);
            });
            name.setText(gameItem.getName());

            username.setText(gameItem.getUsername());


            String text = Spacer(gameItem.getNumber()+"");
            if (category.equals("Market Value")){
                text = text+" €";
            }
            number.setText(text);


            String info;
            switch (category){
                case "Instagram":
                    info = "Instagram followers";
                    break;
                case "Age":
                    info = "years old";
                    break;
                default:
                    info = category;
                    break;
            }

            tv_category.setText(info);
            searches_more.setText(info+" than " + gameItem.getPrevName());
            prev_str = gameItem.getName();

            highScore = getAppPref().getHighScores(category);
            tv_high_score.setText("Highscore: "+ highScore);

            if (gameItem.isLocked()){
                viewSwitcher.showNext();
                num_2 = gameItem.getNumber();
            }
            else {
                num_1 = gameItem.getNumber();
            }

        }

        @Override
        public int getItemCount() {
            return mGameItems.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            private FontTextView name;
            private FontTextView username;
            private FontTextView number;
            private FontTextView tv_category;
            private LinearLayout ll_searches_view;
            private LinearLayout ll_buttons;
            private FontTextView searches_more;
            private ViewSwitcher viewSwitcher;
            private GameButton more;
            private GameButton less;
            private ImageView background;
            private ImageView cardImage;
            private FontTextView tv_attribution;

            
            ViewHolder(View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.tv_keyword);
                username = itemView.findViewById(R.id.tv_username);
                number = itemView.findViewById(R.id.tv_volume);
                ll_searches_view = itemView.findViewById(R.id.ll_searches_view);
                ll_buttons = itemView.findViewById(R.id.ll_buttons);
                searches_more = itemView.findViewById(R.id.tv_searches_more);
                viewSwitcher = itemView.findViewById(R.id.vs_game);
                more = itemView.findViewById(R.id.bt_more);
                less = itemView.findViewById(R.id.bt_less);
                background = itemView.findViewById(R.id.iv_image);
                tv_category = itemView.findViewById(R.id.tv_category);
                cardImage = itemView.findViewById(R.id.card_image);
                tv_attribution = itemView.findViewById(R.id.tv_attribution);
            }
        }

        void onButtonClick(final boolean more){
            animateTextView(0,num_2,tv_number_2);
            CountDownTimer countDownTimer = new CountDownTimer(700,700) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    if (more && (num_2>=num_1)){
                        vsView.correct();
                        vsView.bring_answer();
                        next();
                    }
                    else if (!more && (num_2<=num_1)){
                        vsView.correct();
                        vsView.bring_answer();
                        next();
                    }
                    else {
                        vsView.wrong();
                        vsView.bring_answer();
                        vsView.bring_answer();
                        CountDownTimer countDownTimer1 = new CountDownTimer(1000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                                Intent gameOver = new Intent(getApplicationContext(),GameOverActivity.class);
                                gameOver.putExtra("score",score);
                                gameOver.putExtra("previous",prev);
                                startActivityForResult(gameOver,1123);
                            }
                        };

                        countDownTimer1.start();

                    }

                }
            };
            countDownTimer.start();


        }

        private void next() {
            CountDownTimer countDownTimer2 = new CountDownTimer(1000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    score+=1;
                    if (highScore<score)
                        highScore+=1;
                    tv_score_main.setText("Score: "+score);
                    tv_high_score.setText("Highscore: "+highScore);
                    getAppPref().setHighScores(category, highScore);

                    gameItems.remove(0);
                    notifyItemRemoved(0);
                    gameItems.get(0).setLocked();
                    num_1 = gameItems.get(0).getNumber();
                    gameItems.add(nextItem());
                    notifyItemInserted(1);
                    vsView.bring_vs();

                }
            };
            countDownTimer2.start();
        }

    }




    public void animateTextView(int initialValue, int finalValue, final FontTextView textview) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(700);

        valueAnimator.addUpdateListener(valueAnimator1 -> {

            if (category.equals("Market Value")){
                textview.setText(Spacer(valueAnimator1.getAnimatedValue().toString())+" €");

            }
            else {
                textview.setText(Spacer(valueAnimator1.getAnimatedValue().toString()));

            }

        });
        valueAnimator.start();



    }

    private String Spacer(String number){
        StringBuilder strB = new StringBuilder();
        strB.append(number);
        int Three = 0;

        for(int i=number.length();i>0;i--){
            Three++;
            if(Three == 3){
                strB.insert(i-1, ",");
                Three = 0;
            }
        }
        if (strB.charAt(0)==','){
            strB.deleteCharAt(0);
        }
        return strB.toString();
    }

    public GameItem nextItem(){
        Random random = new Random();
        int next = random.nextInt(names.size());
        while (next==prev){
            next = random.nextInt(names.size());
        }
        while (subNames.get(next).equals("null")){
            next = random.nextInt(names.size());
        }
        if (category.equals("Instagram")){
            while (whatToCompare.get(next) == 0){
                next = random.nextInt(names.size());
            }
        }
        GameItem gameItem = new GameItem(names.get(next),subNames.get(next), whatToCompare.get(next),1,true,prev_str,imageUrls.get(next),credits.get(next));
        prev = next;
        return gameItem;
    }

    protected void continuePlaying() {
        score+=1;
        if (highScore<score)
            highScore+=1;

        tv_score_main.setText("Score: "+score);
        tv_high_score.setText("Highscore: "+highScore);
        getAppPref().setHighScores(category, highScore);

        gameItems.remove(0);
        gameAdapter.notifyItemRemoved(0);
        gameItems.get(0).setLocked();
        num_1 = gameItems.get(0).getNumber();
        gameItems.add(nextItem());
        gameAdapter.notifyItemInserted(1);
        vsView.bring_vs();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1123){
            if (resultCode == 58){
                Bundle bundle = data.getExtras();
                switch (bundle.getString("source")){
                    case "playAgain":
                        startGame();
                        break;
                    case "interAdClosed":
                        startGame();
                        break;
                    case "videoAdWatched":
                        continuePlaying();
                        break;
                    case "onBackPressed":
                        finish();
                        break;
                }
            }
        }
    }
}
