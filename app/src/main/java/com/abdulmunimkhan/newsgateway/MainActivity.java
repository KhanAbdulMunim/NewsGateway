package com.abdulmunimkhan.newsgateway;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
//API Key 26367432808e457b9ab6e9b32f4e9606

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MainActivity mainActivity;
    private Menu menu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<Fragment> fragments;
    private MyPageAdapter pageAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private ViewPager pager;
    public static int screenWidth, screenHeight;
    private String[] items;
    private ArrayList<String> sourcesMenuList = new ArrayList<>();
    private ArrayList<NewsSource> sourcesList = new ArrayList<>();
    private String selectedID;
    private String selectedSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MyPageAdapter mFragmentAdapter = new MyPageAdapter(getSupportFragmentManager());
//        ViewPager mViewPager = findViewById(R.id.viewpager);
//        mViewPager.setAdapter(mFragmentAdapter);

//        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
//        pager = findViewById(R.id.viewpager);
//        pager.setAdapter(pageAdapter);

        SourceRunnable myRunnable = new SourceRunnable(this);
        new Thread(myRunnable).start();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.drawer_list);

        mDrawerList.setAdapter(new ArrayAdapter<>(this,   // <== Important!
                R.layout.drawer_item, sourcesMenuList));

        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Log.d(TAG, sourcesMenuList.get(position));
                        for(int i = 0; i < sourcesList.size(); i++){
                            if (sourcesMenuList.get(position).equals(sourcesList.get(i).getName())){
                                selectedID = sourcesList.get(i).getId();
                                selectedSource = sourcesList.get(i).getName();
//                                Log.d(TAG, selectedID);
                            }
                        }

                        articleRunner();

                        mDrawerLayout.closeDrawer(mDrawerList);
                    }
                }
        );

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        fragments = new ArrayList<>();

        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

        if (getSupportActionBar() != null) {  // <== Important!
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    public void articleRunner(){
        ArticleRunnable myArticleRunnable = new ArticleRunnable(this, selectedID);
        new Thread(myArticleRunnable).start();
    }

    public void setArticles(ArrayList<Article> articleList){
        setTitle(selectedSource);

//        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
//        pager = findViewById(R.id.viewpager);
//        pager.setAdapter(pageAdapter);

        for (int i = 0; i < pageAdapter.getCount(); i++)
            pageAdapter.notifyChangeInPosition(i);

        fragments.clear();

        for (int i = 0; i < articleList.size(); i++) {
//            Log.d(TAG, "setting article "+ i);
            Log.d(TAG, articleList.get(i).getAuthor());
            fragments.add(
                    ArticleFragment.newInstance(articleList.get(i), i+1, articleList.size()));
//            pageAdapter.notifyChangeInPosition(i);
        }

        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState(); // <== IMPORTANT
    } //complete

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig); // <== IMPORTANT
    } //complete

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

        String selectedCategory = item.getTitle().toString();

        sourcesMenuList.clear();
        for (int i = 0; i < sourcesList.size(); i++){
            if (selectedCategory.equals(sourcesList.get(i).getCategory())){
                sourcesMenuList.add(sourcesList.get(i).getName());
            }
        }

        arrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_item, sourcesMenuList);
        mDrawerList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        return super.onOptionsItemSelected(item);
    } //complete

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    } //complete

    public void addSources(ArrayList<NewsSource> sources){
        int length = sources.size();
        for (int i = 0; i < length; i++){
            sourcesList.add(sources.get(i));
            menu.add(sources.get(i).getCategory());
        }
    }

//    ______________________________________

    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseID = 0;

        MyPageAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            return baseID + position;
        }

        void notifyChangeInPosition(int n) {
            baseID += getCount() + n;
        }
    }
}