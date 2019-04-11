package com.dimakaplin143.listwithclick;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class MainActivity extends AppCompatActivity {
    private int count = 0;
    private final String LIST_TEXT = "strings";
    private final String LARGE_TEXT = "large_text";
    List<Map<String, String>> simpleAdapterContent = new ArrayList<>();
    private SharedPreferences mySharedPref;
    String[] wooText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySharedPref = getSharedPreferences(LIST_TEXT, MODE_PRIVATE);
        saveText();
        wooText = getResources().getStringArray(R.array.woo);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        TypedArray colors = getResources().obtainTypedArray(R.array.colors);
        ListView list = findViewById(R.id.list);
        prepareContent();
        SimpleAdapter listContentAdapter = createAdapter(simpleAdapterContent);
        list.setAdapter(listContentAdapter);
        list.setOnItemClickListener((parent, view, position, id) -> {

            count++;
            list.setBackgroundColor(colors.getColor(count,0));
            Toast.makeText(MainActivity.this, wooText[getRandom(wooText.length - 1)], Toast.LENGTH_SHORT).show();

            simpleAdapterContent.remove(position);
            listContentAdapter.notifyDataSetChanged();

            if(simpleAdapterContent.size() == 0){
                Toast.makeText(MainActivity.this, "Препарируемый закончился", Toast.LENGTH_LONG).show();
            }
        });
    }

    @NonNull
    private SimpleAdapter createAdapter(List<Map<String, String>> data) {
        return new SimpleAdapter(this, data, R.layout.simple_adapte_text, new String[] {"title", "sub-title"}, new int[] {R.id.title, R.id.sub_title});
    }

    @NonNull
    private void prepareContent() {
        String[] largeStrings = mySharedPref.getString(LARGE_TEXT,"").split("\n\n");
        for(String str:largeStrings) {
            Map<String, String> elem = new HashMap<>();
            elem.put("title", str);
            elem.put("sub-title", Integer.toString(str.length()) + " очков гриффиндору");
            simpleAdapterContent.add(elem);
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
