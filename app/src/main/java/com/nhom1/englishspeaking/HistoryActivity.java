package com.nhom1.englishspeaking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nhom1.englishspeaking.Model.Vocabulary;
import com.nhom1.englishspeaking.R;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    ListView lvHistory;
    ArrayList<Vocabulary> vocabularyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        lvHistory = (ListView) findViewById(R.id.vocabulary_list);

        //open database
        DatabaseHandler db = new DatabaseHandler(getBaseContext());
        db.openDataBase();
        //get record
        vocabularyList = db.getHistory();
        db.close();

        // set adapter
        lvHistory.setAdapter(new AdapterVocabulary(this, vocabularyList));

        //event click on words
        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // open compare activity
                Vocabulary vocabulary = (Vocabulary) lvHistory.getItemAtPosition(position);
                Intent intent = new Intent(HistoryActivity.this, Compare.class);
                intent.putExtra("Vocabulary", vocabulary);
                startActivity(intent);
            }
        });

    }
}
