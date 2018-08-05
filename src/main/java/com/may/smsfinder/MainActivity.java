package com.may.smsfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.may.smsfinder.data.DataHelper;
import com.may.smsfinder.model.Section;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> listAdapter;
    private SmsLoader smsLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        smsLoader = new SmsLoader(getContentResolver(), getSharedPreferences("appData", MODE_PRIVATE));
        InputStream is = getResources().openRawResource(R.raw.schema);

        DataHelper dataHelper = new DataHelper(this, is);
        DataHelper.setInstance(dataHelper);

        List<Section> sectionList = dataHelper.selectSections();
        Log.i("GDAY", sectionList.toString());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.listview);
        listAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.section_list_item);

        for (Section section : sectionList) {
            listAdapter.add(section.getName());
        }

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(onClickListItem);

        registerForContextMenu(listView);

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.listview) {
            menu.add(Menu.NONE, 1, Menu.NONE, "수정");
            menu.add(Menu.NONE, 2, Menu.NONE,"삭제");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String sectionName = listAdapter.getItem(acmi.position);

        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(getThis(), SectionModifyActivity.class);
                intent.putExtra("sectionName", sectionName);
                startActivity(intent);

                break;
            case 2:
                listAdapter.remove(sectionName);
                DataHelper.getInstance().deleteSection(sectionName);
                DataHelper.getInstance().deleteSms(sectionName);

                break;
            default:
                super.onContextItemSelected(item);
        }

        return false;
    }

    private AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Intent intent = new Intent(getThis(), SectionActivity.class);
            intent.putExtra("sectionName", listAdapter.getItem(arg2));
            startActivity(intent);
        }
    };

    private MainActivity getThis() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        DataHelper dataHelper = DataHelper.getInstance();
        List<Section> sectionList = dataHelper.selectSections();

        listAdapter.clear();

        for (Section section : sectionList) {
            listAdapter.add(section.getName());
        }

        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_reload_sms) {
            smsLoader.refreshSms();
            Log.d("GDAY", "refresh sms list!");
        } else if (itemId == R.id.action_section_add) {
            startActivity(new Intent(getThis(), InsertSectionActivity.class));
        } else if (itemId == R.id.action_guide) {
            startActivity(new Intent(getThis(), GuideActivity.class));
        } else if (itemId == R.id.action_about) {
            startActivity(new Intent(getThis(), AboutActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
