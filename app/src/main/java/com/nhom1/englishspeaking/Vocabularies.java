package com.nhom1.englishspeaking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.nhom1.englishspeaking.Model.Vocabulary;
import com.nhom1.englishspeaking.Retrofit2.ApiClient;
import com.nhom1.englishspeaking.Retrofit2.ApiInterface;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Vocabularies extends AppCompatActivity {
    EditText etSearch;
    ListView listView;
    ArrayList<Vocabulary> vocabularyList;
    String deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabularies);

        //get extra
        deviceName = getIntent().getStringExtra("DeviceName");

        //set the view
        listView = (ListView) findViewById(R.id.vocabulary_list);
        etSearch = (EditText) findViewById(R.id.et_search);

        //open database
        DatabaseHandler db = new DatabaseHandler(getBaseContext());
        db.openDataBase();
        //get record
        vocabularyList = db.getAllVocabularies();
        db.close();

        //set adapter
        listView.setAdapter(new AdapterVocabulary(this, vocabularyList));

        //event click on word
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // add record to History
                DatabaseHandler db = new DatabaseHandler(getBaseContext());
                db.openDataBase();
                db.insertToHistory((int)(id));
                db.close();

                // open compare activity
                Vocabulary vocabulary = (Vocabulary) listView.getItemAtPosition(position);
                Intent intent = new Intent(Vocabularies.this, Compare.class);
                intent.putExtra("Vocabulary", vocabulary);
                startActivity(intent);
            }
        });


        //event search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //open database
                DatabaseHandler db = new DatabaseHandler(getBaseContext());
                db.openDataBase();
                if (etSearch.getText().toString().equals("")) {
                    //get record
                    vocabularyList = db.getAllVocabularies();
                } else {
                    //get record
                    vocabularyList = db.searchWords(etSearch.getText().toString());
                }
                db.close();
                //set adapter
                listView.setAdapter(new AdapterVocabulary(getBaseContext(), vocabularyList));
            }
        });
    }
    private void sendLearningLog(String status){
        // get api service
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        // setting the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateFormat.format(Calendar.getInstance().getTime());
        Call<String> call = apiService.sendLog(deviceName, status, currentTime);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("SEND LOG SUCCESS", "Đã send log tới server");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                Log.e("SEND LOG ERROR", t.toString());
                Toast.makeText(getBaseContext(),t.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendLearningLog("Kết thúc học");
    }

    @Override
    protected void onStart() {
        super.onStart();
        sendLearningLog("Bắt đầu học");
    }
}
