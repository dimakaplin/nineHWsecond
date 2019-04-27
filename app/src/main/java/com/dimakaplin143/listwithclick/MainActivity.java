package com.dimakaplin143.listwithclick;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class MainActivity extends AppCompatActivity {
    private int count = 0;
    private final String REMOVE_INDEXES = "remove_indexes";
    private final String COLOR_COUNT = "color_count";
    private final String LIST_TEXT = "strings";
    private final String LARGE_TEXT = "large_text";
    private List<Map<String, String>> simpleAdapterContent = new ArrayList<>();
    private SharedPreferences mySharedPref;
    private String[] wooText;
    private SwipeRefreshLayout swipeLayout;
    private ArrayList<Integer> removeIndexes = new ArrayList<>();
    private ListView list;
    TypedArray colors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySharedPref = getSharedPreferences(LIST_TEXT, MODE_PRIVATE);
        saveText();




        wooText = getResources().getStringArray(R.array.woo);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        colors = getResources().obtainTypedArray(R.array.colors);
        list = findViewById(R.id.list);
        prepareContent();
        SimpleAdapter listContentAdapter = createAdapter(simpleAdapterContent);
        list.setAdapter(listContentAdapter);

        swipeLayout = findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(()-> {
                removeIndexes.clear();

                prepareContent();

                listContentAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);

        });

        list.setOnItemClickListener((parent, view, position, id) -> {
            if(count == 10) {
                count = 0;
            } else {
                count++;
            }

            list.setBackgroundColor(colors.getColor(count,0));
            Toast.makeText(MainActivity.this, wooText[getRandom(wooText.length - 1)], Toast.LENGTH_SHORT).show();

            simpleAdapterContent.remove(position);
            listContentAdapter.notifyDataSetChanged();
            removeIndexes.add(position);

            if(simpleAdapterContent.size() == 0){
                Toast.makeText(MainActivity.this, "Препарируемый закончился", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putIntegerArrayList(REMOVE_INDEXES, removeIndexes);
        outState.putInt(COLOR_COUNT, count);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        removeIndexes = savedInstanceState.getIntegerArrayList(REMOVE_INDEXES);
        try {
            for(Integer i:removeIndexes) {
                simpleAdapterContent.remove(i.intValue());
            }

        } catch (Exception e) {

        }

        list = findViewById(R.id.list);
        colors = getResources().obtainTypedArray(R.array.colors);
        count = savedInstanceState.getInt(COLOR_COUNT);

        list.setBackgroundColor(colors.getColor(count,0));


    }

    @NonNull
    private SimpleAdapter createAdapter(List<Map<String, String>> data) {
        return new SimpleAdapter(this, data, R.layout.simple_adapte_text, new String[] {"title", "sub-title"}, new int[] {R.id.title, R.id.sub_title});
    }

    @NonNull
    private void prepareContent() {
        String saved = mySharedPref.getString(LARGE_TEXT,"");
        String target = getString(R.string.large_text);
        if(!saved.equals(target)) {
            String[] largeStrings = saved.split("\n\n");
            simpleAdapterContent.clear();
            for(String str:largeStrings) {
                Map<String, String> elem = new HashMap<>();
                elem.put("title", str);
                elem.put("sub-title", Integer.toString(str.length()) + " очков гриффиндору");
                simpleAdapterContent.add(elem);

            }
        }

    }

    private void saveText() {

        if(!mySharedPref.contains(LARGE_TEXT)) {
            SharedPreferences.Editor myEditor = mySharedPref.edit();
            String largeText = getString(R.string.large_text);
            myEditor.putString(LARGE_TEXT, largeText);
            myEditor.apply();

        }
    }
    private int getRandom(int max) {
        return (int) (Math.random() * ++max);
    }
}
