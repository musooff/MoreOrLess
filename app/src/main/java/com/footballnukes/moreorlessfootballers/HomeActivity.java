package com.footballnukes.moreorlessfootballers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.footballnukes.moreorlessfootballers.beautifiers.FontTextView;
import com.footballnukes.moreorlessfootballers.beautifiers.PlayButton;
import com.footballnukes.moreorlessfootballers.model.AppPreference;

/**
 * Created by moshe on 18/04/2017.
 */

public class HomeActivity extends AppCompatActivity {

    public static String FACEBOOK_URL = "https://www.facebook.com/ballboytv";
    public static String FACEBOOK_PAGE_ID = "634136046769198";

    List<Category> locked_cat = new ArrayList<>();
    List<Category> unlocked_cat = new ArrayList<>();

    RecyclerView rv_unlocked;
    RecyclerView rv_locked;

    LinearLayout ll_youtube,ll_facebook;
    CategoryAdapter categoryAdapterUnlocked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setCategories();
        socialButtons();

    }

    private void setCategories(){
        rv_unlocked = findViewById(R.id.rv_unlocked);
        rv_locked = findViewById(R.id.rv_locked);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Category> categories = objectMapper.readValue(getResources().getAssets().open("categories.json"), new TypeReference<List<Category>>(){});
            for (Category category: categories){
                if (category.isPurchased()) unlocked_cat.add(category);
                else locked_cat.add(category);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        categoryAdapterUnlocked = new CategoryAdapter(unlocked_cat);
        rv_unlocked.setAdapter(categoryAdapterUnlocked);
        rv_unlocked.setLayoutManager(new LinearLayoutManager(this));
        rv_unlocked.setNestedScrollingEnabled(false);

        CategoryAdapter categoryAdapterLocked = new CategoryAdapter(locked_cat);
        rv_locked.setAdapter(categoryAdapterLocked);
        rv_locked.setLayoutManager(new LinearLayoutManager(this));
        rv_locked.setNestedScrollingEnabled(false);

    }

    private void socialButtons(){
        ll_facebook = findViewById(R.id.ll_facebook);
        ll_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(getApplicationContext());
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
            }
        });
        ll_youtube = findViewById(R.id.ll_youtube);
        ll_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/c/theballboy")));
            }
        });
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

        private List<Category> mCategory;

        CategoryAdapter(List<Category> category) {
            mCategory = category;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View categoryView = inflater.inflate(R.layout.category_card, parent, false);
            return new ViewHolder(categoryView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Category category = mCategory.get(position);

            FontTextView name = holder.name;
            FontTextView description = holder.description;
            FontTextView score = holder.score;
            ViewSwitcher viewSwitcher = holder.viewSwitcher;
            PlayButton playButton = holder.playButton;
            FrameLayout lock_frame = holder.lock_frame;
            ImageView iv_background = holder.iv_background;
            ImageView iv_border = holder.iv_border;

            name.setText(category.getName());
            if (category.getName().contains("Goals")){
                name.setText("Goals");
            }
            description.setText(category.getDescription());
            score.setText(category.getScore()+"");


            playButton.setOnClickListener(v -> {
                if (!category.isPurchased()){
                    Toast.makeText(HomeActivity.this,"Coming Soon",Toast.LENGTH_SHORT).show();

                }
                else {
                    Intent game = new Intent(HomeActivity.this, GameActivity.class);
                    getAppPref().setLastGame(category.getName());
                    startActivity(game);
                }
            });

            if (!category.isPurchased()){
                viewSwitcher.showNext();
                lock_frame.setVisibility(View.VISIBLE);
                iv_border.setImageResource(R.drawable.white_border);
                playButton.setText("Get");
                playButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_getplus,0,0,0);
            }
            iv_background.setImageResource(getImageId(getApplicationContext(),category.getImageUrl()));


        }

        int getImageId(Context context, String imageName) {
            return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
        }

        void setHighScores(){
            for (Category category: mCategory){
                if (category.isPurchased()) category.setScore(getAppPref().getHighScores(category.getName()));
            }
            notifyDataSetChanged();
        }
        @Override
        public int getItemCount() {
            return mCategory.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            private FontTextView name;
            private FontTextView description;
            private FontTextView score;
            private ViewSwitcher viewSwitcher;
            private PlayButton playButton;
            private FrameLayout lock_frame;
            private ImageView iv_background;
            private ImageView iv_border;

            ViewHolder(View itemView) {
                super(itemView);

                name = itemView.findViewById(R.id.tv_category_name);
                description = itemView.findViewById(R.id.tv_category_description);
                score = itemView.findViewById(R.id.tv_score);
                viewSwitcher = itemView.findViewById(R.id.vs_score);
                playButton = itemView.findViewById(R.id.bt_play);
                lock_frame = itemView.findViewById(R.id.fl_padlock);
                iv_background = itemView.findViewById(R.id.iv_category_background);;
                iv_border = itemView.findViewById(R.id.iv_category_border);
            }
        }
    }

    public AppPreference getAppPref(){
        return new AppPreference(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        categoryAdapterUnlocked.setHighScores();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //CustomDialogClass cdd=new CustomDialogClass(HomeActivity.this);

        //cdd.show();
    }

}
