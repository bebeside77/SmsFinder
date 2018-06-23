package com.may.smsfinder;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.may.smsfinder.data.DataHelper;
import com.may.smsfinder.model.Section;
import com.may.smsfinder.model.Sms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SectionActivity extends AppCompatActivity {
    private Section section;
    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    private List<Sms> smsList;
    private int pageSize = 10;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String sectionName = intent.getStringExtra("sectionName");
        setTitle(sectionName + " 분류 메시지");

        listView = findViewById(R.id.smsList);
        listAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.section_list_item);
        listView.setAdapter(listAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(false);
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean isReachLastItem = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && isReachLastItem) {
                    // load new data
                    if (listAdapter.getCount() >= smsList.size()) {
                        return;
                    }

                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss", Locale.KOREA);
                            int lastIndex = Math.min(page * pageSize, smsList.size());
                            for (int i = (page - 1) * pageSize; i < lastIndex; i++) {
                                Sms sms = smsList.get(i);
                                Date date = new Date();
                                date.setTime(sms.getDate());
                                listAdapter.add(getSmsPrintString(sdf, sms, date));
                            }
                            page++;
                        }
                    });
                    executor.shutdown();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isReachLastItem = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });

        DataHelper dataHelper = DataHelper.getInstance();
        smsList = dataHelper.selectSmsList(sectionName);

        Log.i("GDAY", "smsList: " + smsList);

        listAdapter.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss", Locale.KOREA);

        int lastIndex = Math.min(pageSize, smsList.size());
        for (int i = 0; i < lastIndex; i++) {
            Sms sms = smsList.get(i);

            Date date = new Date();
            date.setTime(sms.getDate());

            listAdapter.add(getSmsPrintString(sdf, sms, date));
        }

        page++;
    }

    @NonNull
    private String getSmsPrintString(SimpleDateFormat sdf, Sms sms, Date date) {
        return sms.getBody() + "\n\n" + sms.getAddress() + "  " + sdf.format(date);
    }

}
