package com.sumit.ibox.ui.student.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;
import com.sumit.ibox.ui.chat.ChatActivity;
import com.sumit.ibox.ui.articles_for_you.ExpandedArticlesForYou;
import com.sumit.ibox.controller.ArticlesForYouAdapter;
import com.sumit.ibox.R;
import com.sumit.ibox.ui.chat.ChatListActivity;
import com.sumit.ibox.ui.gallery.Gallery;
import com.sumit.ibox.model.ArticlesForYouData;
import com.sumit.ibox.ui.notification.Notification;
import com.sumit.ibox.ui.student.library.LibraryActivity;
import com.sumit.ibox.ui.student.TrackBus;
import com.sumit.ibox.ui.student.fees.FeesActivity;
import com.sumit.ibox.ui.student.homework.HomeWorkViewStudentMain;
import com.sumit.ibox.ui.student.result.ResultList;
import com.sumit.ibox.ui.student.syllabus.Syllabus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private RecyclerView articlesForYouRecyclerView;
    private List<ArticlesForYouData> articles = new ArrayList<>();
    private ArticlesForYouAdapter articlesForYouAdapter;

    Toolbar toolbar;

    private Context fContext;
    private ViewPager sliderPager;
    private TabLayout indicator;
    private List<PagerData> pagerData;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 1000; //delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.
    final static long NUM_PAGES = 6;

    private LinearLayout homeWorkClick, resultClick, feesClick, syllabusLayout,galleryClick,trackBus,libraryLayout,chatLayout;//faq_layout,
    private TextView viewAllBlogData;
    private ImageView notificationClick;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        init(root);

        galleryClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Gallery.class));
            }
        });
        homeWorkClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HomeWorkViewStudentMain.class));
            }
        });

        resultClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ResultList.class));
            }
        });

        feesClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FeesActivity.class));
            }
        });

        syllabusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Syllabus.class));
            }
        });
//        faq_layout = root.findViewById(R.id.faq_layoutClick);
//        faq_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), FaqActivity.class));
//            }
//        });
        libraryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LibraryActivity.class));
            }
        });

        chatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent (getContext(), ChatListActivity.class);
                chatIntent.putExtra(Constant.KEY_USER_TYPE, Constant.TYPE_PARENT);
                startActivity(chatIntent);
            }
        });

        viewAllBlogData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ExpandedArticlesForYou.class));
            }
        });

        trackBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TrackBus.class));
            }
        });
        notificationClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationIntent = new Intent(getContext(), Notification.class);
                notificationIntent.putExtra(Constant.KEY_USER_TYPE, Constant.TYPE_PARENT);
                startActivity(notificationIntent);
            }
        });

        setupViewPager(root);
        setUpRecyclerView(root);
        blogData();
        return root;
    }

    private void init(View root) {
        fContext = getContext();
        toolbar = root.findViewById(R.id.home_toolbar);
        galleryClick = root.findViewById(R.id.galleryClick);
        notificationClick = root.findViewById(R.id.notification_click);
        trackBus = root.findViewById(R.id.track_bus);
        homeWorkClick = root.findViewById(R.id.homework);
        resultClick = root.findViewById(R.id.result_layout_click);
        feesClick = root.findViewById(R.id.fees_click);
        syllabusLayout = root.findViewById(R.id.syllabus_layout);
        libraryLayout = root.findViewById(R.id.library_layoutClick);
        chatLayout = root.findViewById(R.id.chat_layout_click);
        viewAllBlogData = root.findViewById(R.id.viewAllBlogData);
    }

    private void setUpRecyclerView(View root){
        articlesForYouRecyclerView = root.findViewById(R.id.articlesForYouRecyclerView);
        articles = new ArrayList<>();
        articlesForYouAdapter = new ArticlesForYouAdapter(getContext(),articles);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        articlesForYouRecyclerView.setLayoutManager(mLayoutManager);
        articlesForYouRecyclerView.setAdapter(articlesForYouAdapter);
    }

    private void setupViewPager(View root){
        sliderPager = root.findViewById(R.id.viewPagerNotice);
        indicator = root.findViewById(R.id.indicator);
        ViewPagerDataAdapter adapter = new ViewPagerDataAdapter(fContext, getViewPagerData());
        sliderPager.setAdapter(adapter);
        sliderPager.setPageTransformer(true, new DepthPageTransformer());
        indicator.setupWithViewPager(sliderPager, true);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES-1) {
                    currentPage = 0;
                }
                sliderPager.setCurrentItem(currentPage++, true);
            }
        };
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    private List<PagerData> getViewPagerData() {
            ArrayList<PagerData> viewFlipperData = new ArrayList<>();
            viewFlipperData.add(new PagerData(R.drawable.notice, "notice1"));
            viewFlipperData.add(new PagerData(R.drawable.school, "notice2"));
            viewFlipperData.add(new PagerData(R.drawable.students, "students"));
            viewFlipperData.add(new PagerData(R.drawable.notice9, "teacher"));
            return viewFlipperData;
    }

    public class ViewPagerDataAdapter extends PagerAdapter {

        public Context mContext;
        private List<PagerData> viewPagerData;


        public ViewPagerDataAdapter(Context context, List<PagerData> viewPagerData) {
            this.mContext = context;
            this.viewPagerData = viewPagerData;
        }

        @Override
        public int getCount() {
            return  viewPagerData.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View slideLayout = inflater.inflate(R.layout.slide_item_home,null);

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slide_item_home, container, false);

            ImageView slideImg = view.findViewById(R.id.slide_img);
            TextView slideText = view.findViewById(R.id.slide_title);
            slideImg.setImageResource(viewPagerData.get(position).getImage());
            slideText.setText(viewPagerData.get(position).getTitle());
            ViewPager viewPager = (ViewPager) container;
            viewPager.addView(view, 0);
            return view;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ViewPager viewPager = (ViewPager) container;
            View view = (View) object;
            viewPager.removeView(view);
        }

    }
    /** view pager animation*/
    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }



    public class PagerData {
        private int Image ;
        private String Title;

        public PagerData(int image, String title) {
            Image = image;
            Title = title;
        }

        public int getImage() {
            return Image;
        }

        public String getTitle() {
            return Title;
        }

    }

    VolleyCallback articlesCallback = new VolleyCallback() {
        @Override
        public void onSuccess(Object result) {
            try {
                ArticlesForYouData article;
                JSONObject jsonObject = (JSONObject) result;
                int success = jsonObject.getInt("success");
                if(success == 1){//details":[{"title":"School is closed for coronavirus.","image_link":"https:\/\/externas-six.jpg&f=1&nofb=1","date":"03 MAR 2020"}]}
                    JSONArray jsonArray = jsonObject.getJSONArray("details");
                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String title = jsonObject1.getString("title");
                        String date = jsonObject1.getString("date");
                        String url = jsonObject1.getString("image_link");
                        article = new ArticlesForYouData(title, getString(R.string.ibox), R.drawable.logo_demo, url, date);
                        articles.add(article);
                    }
                }
                articlesForYouAdapter.notifyDataSetChanged();
            } catch (Exception ignored) {}
        }

        @Override
        public void noDataFound() {}

        @Override
        public void onCatch(JSONException e) {}

        @Override
        public void onError(VolleyError e) {}
    };

    private void blogData(){
        Perform.fetchArticles(fContext, articlesCallback);
    }
}