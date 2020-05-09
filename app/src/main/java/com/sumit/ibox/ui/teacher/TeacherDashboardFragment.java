package com.sumit.ibox.ui.teacher;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import com.sumit.ibox.controller.ArticlesForYouAdapter;
import com.sumit.ibox.R;
import com.sumit.ibox.remote.Perform;
import com.sumit.ibox.remote.VolleyCallback;
import com.sumit.ibox.ui.articles_for_you.ExpandedArticlesForYou;
import com.sumit.ibox.ui.chat.ChatListActivity;
import com.sumit.ibox.ui.gallery.*;
import com.sumit.ibox.model.ArticlesForYouData;
import com.sumit.ibox.ui.notification.Notification;
import com.sumit.ibox.ui.student.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherDashboardFragment extends Fragment {

    private RecyclerView articlesForYouRecyclerView;
    private List<ArticlesForYouData> articles = new ArrayList<>();
    ArticlesForYouAdapter articlesForYouAdapter;

//    options teacher dashboard
    LinearLayout attandanceClick, leaveClick, studentsClick, chatLayoutClick, galleryClick;
    ImageView notificationClick;
    TextView viewMore;
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

    LinearLayout homework_layout;

    public TeacherDashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.teacher_dashboard, container, false);
        init(inflate);
//        toolbar = inflate.findViewById(R.id.teacher_toolbar);
//        toolbar.setTitle(R.string.title_home);
        articlesForYouRecyclerView = inflate.findViewById(R.id.articlesForYouRecyclerView);
        articles = new ArrayList<>();
        articlesForYouAdapter = new ArticlesForYouAdapter(getContext(), articles);


        /**options dashboard*/
        attandanceClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AttendanceActivity.class));
            }
        });
        leaveClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TeacherLeaveActivity.class));
            }
        });
        homework_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AcademicsUpload.class));
            }
        });
        studentsClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),StudentsActivity.class));
            }
        });
        chatLayoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent (getContext(), ChatListActivity.class);
                chatIntent.putExtra(Constant.KEY_USER_TYPE, Constant.TYPE_TEACHER);
                startActivity(chatIntent);
            }
        });
        galleryClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getContext(),Gallery.class));
            }
        });
        notificationClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationIntent = new Intent(getContext(), Notification.class);
                notificationIntent.putExtra(Constant.KEY_USER_TYPE, Constant.TYPE_TEACHER);
                startActivity(notificationIntent);
            }
        });
        viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getContext(), ExpandedArticlesForYou.class));
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        articlesForYouRecyclerView.setLayoutManager(mLayoutManager);
        articlesForYouRecyclerView.setAdapter(articlesForYouAdapter);
        blogData();

        /**viewPager setup*/
        sliderPager = inflate.findViewById(R.id.viewPagerNoticeTeacher);
        indicator = inflate.findViewById(R.id.indicator);
        ViewPagerDataAdapter adapter = new ViewPagerDataAdapter(fContext, getViewPagerData());
        sliderPager.setAdapter(adapter);
        sliderPager.setPageTransformer(true, new TeacherDashboardFragment.DepthPageTransformer());
        indicator.setupWithViewPager(sliderPager, true);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES - 1) {
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

        return inflate;
    }

    private void init(View inflate) {
        fContext = getContext();
        notificationClick = inflate.findViewById(R.id.notification_click);
        attandanceClick = inflate.findViewById(R.id.attandance_click);
        leaveClick = inflate.findViewById(R.id.leave_layout);
        homework_layout =  inflate.findViewById(R.id.upload_homework_click);
        studentsClick = inflate.findViewById(R.id.studentsClick);
        chatLayoutClick= inflate.findViewById(R.id.chatLayoutClick);
        galleryClick = inflate.findViewById(R.id.galleryLayout);
        viewMore = inflate.findViewById(R.id.teacher_view_more);
    }

    private List<TeacherDashboardFragment.PagerData> getViewPagerData() {
        ArrayList<TeacherDashboardFragment.PagerData> viewFlipperData = new ArrayList<>();
        viewFlipperData.add(new TeacherDashboardFragment.PagerData(R.drawable.notice, "notice"));
        viewFlipperData.add(new TeacherDashboardFragment.PagerData(R.drawable.school, "notice"));
        viewFlipperData.add(new TeacherDashboardFragment.PagerData(R.drawable.notice9, ""));
        viewFlipperData.add(new TeacherDashboardFragment.PagerData(R.drawable.notice, ""));
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
            return viewPagerData.size();
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

    /**
     * view pager animation
     */
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
        private int Image;
        private String Title;

        PagerData(int image, String title) {
            Image = image;
            Title = title;
        }

        int getImage() {
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
