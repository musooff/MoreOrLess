package com.footballnukes.moreorlessfootballers;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String category;

    String[] names;
    String[] subNames;
    String[] imageUrls;
    //String[] beautyImageUrls;
    String[] credits;
    String[] whatToCompare;

    int prev = 0;
    String prev_str = null;
    ImageView imageViewCard;
    GameAdapter2 gameAdapter;

    FontTextView tv_number_2, tv_score_main,tv_high_score;
    int num_1,num_2, score =0,highScore =0;

    RelativeLayout rl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sharedPreferences = this.getSharedPreferences("Download Data",0);
        editor = sharedPreferences.edit();

        rl = (RelativeLayout)findViewById(R.id.rl_root);


        category = sharedPreferences.getString("last","None");

        names = sharedPreferences.getString("Data Names",null).split(",");

        imageUrls = sharedPreferences.getString("Data WikiImages",null).split("arusak");
        //beautyImageUrls = sharedPreferences.getString("Data BeautyImageUrls",null).split(",");
        credits = sharedPreferences.getString("Data WikiImageAuthors",null).split("arusak");


        switch (category){
            case "Market Value":
                whatToCompare = sharedPreferences.getString("Data Values",null).split(",");
                subNames = sharedPreferences.getString("Data Clubs",null).split(",");
                break;
            case "Age":
                whatToCompare = sharedPreferences.getString("Data Ages",null).split(",");
                subNames = sharedPreferences.getString("Data Clubs",null).split(",");
                break;
            case "Goals This Season":
                whatToCompare = sharedPreferences.getString("Data GoalsThisSeason",null).split(",");
                subNames = sharedPreferences.getString("Data Clubs",null).split(",");
                break;
            case "Instagram":
                whatToCompare = sharedPreferences.getString("Data InstaNumbers",null).split(",");
                subNames = sharedPreferences.getString("Data InstaIDs","null").split(",");
                break;

        }


        vsView = (VSView)findViewById(R.id.vs_view);
        tv_high_score = (FontTextView)findViewById(R.id.tv_high_score);
        tv_score_main = (FontTextView)findViewById(R.id.tv_score_main);

        recyclerView = (RecyclerView) findViewById(R.id.rl_game_item);
        gameItems= new ArrayList<GameItem>();

        Random rr = new Random();
        int randomNumber = rr.nextInt(names.length);
        while (subNames[randomNumber].equals("null")){
            randomNumber = rr.nextInt(names.length);
        }
        if (category.equals("Instagram")){
            while (whatToCompare[randomNumber].equals("0")){
                randomNumber = rr.nextInt(names.length);
            }
        }
        gameItems.add(new GameItem(names[randomNumber],subNames[randomNumber],Integer.valueOf(whatToCompare[randomNumber]),0,false,null,imageUrls[randomNumber],credits[randomNumber]));
        num_1 = Integer.valueOf(whatToCompare[randomNumber]);
        prev = randomNumber;
        prev_str = names[randomNumber];

        gameItems.add(nextItem());

        num_2 = Integer.valueOf(whatToCompare[prev]);
        //gameItems.add(new GameItem("Man","@man",20,0,false,null,imageUrls[0],"some"));
        gameAdapter = new GameAdapter2(this,gameItems);
        recyclerView.setAdapter(gameAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setItemAnimator(new ScaleInTopAnimator());

        highScore = sharedPreferences.getInt(category,0);
        tv_high_score.setText("Highscore: "+ highScore);
    }

    public class GameAdapter2  extends RecyclerView.Adapter<GameAdapter2.ViewHolder> {

        private ArrayList<GameItem> mGameItems;
        private Context mContext;


        public GameAdapter2(Context context, ArrayList<GameItem> gameItems) {
            mGameItems = gameItems;
            mContext = context;
        }

        // Easy access to the context object in the recyclerview
        private Context getContext() {
            return mContext;
        }

        @Override
        public GameAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);


            int measuredHeight = parent.getMeasuredHeight();
            int measuredWidth = parent.getMeasuredWidth();
            if (this.getContext().getResources().getConfiguration().orientation == 2) {
                measuredWidth /= 2;
            } else {
                measuredHeight /= 2;
            }
            // Inflate the custom layout
            View gameView = inflater.inflate(R.layout.game_item, parent, false);
            gameView.setLayoutParams(new RecyclerView.LayoutParams(measuredWidth,measuredHeight));

            // Return a new holder instance
            GameAdapter2.ViewHolder viewHolder = new GameAdapter2.ViewHolder(gameView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(GameAdapter2.ViewHolder holder, int position) {
            // Get the data model based on position
            final GameItem gameItem = mGameItems.get(position);

            // Set item views based on your views and data model
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

            //Picasso.with(GameActivity.this).load(gameItem.getImage_url()).into(card_image);
            Picasso.with(GameActivity.this).load(gameItem.getImage_url()).fit().into(imageView);

            //Uri imageUri = Uri.parse(gameItem.getImage_url());
            //imageView.setImageURI(imageUri);


            if (gameItem.getImage_url().equals("None")){
                Log.e("Doesn't have image",gameItem.getName());
            }


            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewSwitcher.showNext();
                    onButtonClick(true);
                }
            });

            less.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    viewSwitcher.showNext();
                    onButtonClick(false);
                }
            });
            name.setText(gameItem.getName());

            if (category.equals("Instagram")){
                username.setText("@"+gameItem.getUsername());
            }

            else {
                username.setText(gameItem.getUsername());
            }


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

            highScore = sharedPreferences.getInt(category,0);
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


        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        public class ViewHolder extends RecyclerView.ViewHolder {
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
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



            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                name = (FontTextView)itemView.findViewById(R.id.tv_keyword);
                username = (FontTextView)itemView.findViewById(R.id.tv_username);
                number = (FontTextView)itemView.findViewById(R.id.tv_volume);
                ll_searches_view = (LinearLayout) itemView.findViewById(R.id.ll_searches_view);
                ll_buttons = (LinearLayout) itemView.findViewById(R.id.ll_buttons);
                searches_more = (FontTextView) itemView.findViewById(R.id.tv_searches_more);
                viewSwitcher = (ViewSwitcher)itemView.findViewById(R.id.vs_game);
                more = (GameButton)itemView.findViewById(R.id.bt_more);
                less = (GameButton)itemView.findViewById(R.id.bt_less);
                background = (ImageView) itemView.findViewById(R.id.iv_image);
                tv_category = (FontTextView)itemView.findViewById(R.id.tv_category);
                cardImage = (ImageView)itemView.findViewById(R.id.card_image);
                tv_attribution = (FontTextView)itemView.findViewById(R.id.tv_attribution);
            }
        }

        public void onButtonClick(final boolean more){
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
                                //GameActivity.this.finish();
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
                    editor.putInt(category,highScore);
                    editor.apply();

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

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                if (category.equals("Market Value")){
                    textview.setText(Spacer(valueAnimator.getAnimatedValue().toString())+" £");

                }
                else {
                    textview.setText(Spacer(valueAnimator.getAnimatedValue().toString()));

                }

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
    }// end Spacer()
/*
    @Override
    public void onBackPressed() {
        CustomDialogClass cdd=new CustomDialogClass(GameActivity.this);

        cdd.show();
    }
*/
    public GameItem nextItem(){
        Random random = new Random();
        int next = random.nextInt(names.length);
        //Log.w("subNames",subNames[next]);
        while (next==prev){
            next = random.nextInt(names.length);
        }
        while (subNames[next].equals("null")){
            next = random.nextInt(names.length);
        }
        if (category.equals("Instagram")){
            while (whatToCompare[next].equals("0")){
                next = random.nextInt(names.length);
            }
        }
        GameItem gameItem = new GameItem(names[next],subNames[next],Integer.valueOf(whatToCompare[next]),1,true,prev_str,imageUrls[next],credits[next]);
        prev = next;
        return gameItem;
    };

    public class GetImageFromWikipedia extends AsyncTask<String,Void,String> {

        String name;
        @Override
        protected String doInBackground(String... params) {
            try {
                name = params[0];
                String url_str = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=original&titles="+spaceConverter(params[0]);
                URL url = new URL(url_str);
                HttpURLConnection connection  = (HttpURLConnection)url.openConnection();
                connection.connect();
                InputStream in = new BufferedInputStream(connection.getInputStream());

                JSONObject jsonObject = new JSONObject(convertStreamToString(in));
                JSONObject query = jsonObject.getJSONObject("query");
                JSONObject pages = query.getJSONObject("pages");
                Iterator iterator = pages.keys();
                if (iterator.hasNext()){
                    String key = (String)iterator.next();
                    JSONObject page = pages.getJSONObject(key);
                    String source = page.getJSONObject("original").getString("source");
                    return source;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //imageViewCard.setImageDrawable(null);
            Picasso.with(GameActivity.this).load(s).into(imageViewCard);
            Log.e("image updated for",name);
            //recyclerView.getAdapter().notifyItemChanged(0);
            //imageViewCard.notify();
        }

        public String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return sb.toString();
        }

        public String spaceConverter(String string){
            String[] some = new String[StringUtils.countMatches(" ",string)+1];
            some = string.split(" ");
            String result = StringUtils.join(some,"%20");
            return result;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sharedPreferences.getBoolean("watched",false)){
            editor.putBoolean("watched",false);
            editor.apply();
            score+=1;
            if (highScore<score)
                highScore+=1;

            tv_score_main.setText("Score: "+score);
            tv_high_score.setText("Highscore: "+highScore);
            editor.putInt(category,highScore);
            editor.apply();

            gameItems.remove(0);
            gameAdapter.notifyItemRemoved(0);
            gameItems.get(0).setLocked();
            num_1 = gameItems.get(0).getNumber();
            gameItems.add(nextItem());
            gameAdapter.notifyItemInserted(1);
            vsView.bring_vs();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1123){
            if (resultCode == 58){
                this.finish();
            }
        }
    }
}
