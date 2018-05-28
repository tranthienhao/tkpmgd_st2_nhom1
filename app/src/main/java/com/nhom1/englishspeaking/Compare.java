package com.nhom1.englishspeaking;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom1.englishspeaking.Model.Vocabulary;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.nhom1.englishspeaking.R;

import java.util.ArrayList;
import java.util.Locale;

public class Compare extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    Button btnLearn, testBtn, btnSpeech, btnListen1, btnListen2;
    LinearLayout learningLayout;
    YouTubePlayerFragment youTubePlayerFragment;
    Vocabulary vocabulary;
    TextView header, tvIsLearned;
    TextView tvWord1, tvPronounce1, tvWord2, tvPronounce2, tvResult;
    Boolean isLearning = false;
    TextToSpeech textToSpeech;
    int resultSpeech;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare);

        //get the view
        btnLearn = (Button) findViewById(R.id.compare_word1_textview);
        btnListen1 = (Button) findViewById(R.id.listenBtn1);
        btnListen2 = (Button) findViewById(R.id.listenBtn2);
        btnSpeech = (Button) findViewById(R.id.speech_btn);
        tvIsLearned = (TextView) findViewById(R.id.tv_learned);
        tvResult = (TextView) findViewById(R.id.result_textview);
        tvWord1 = (TextView) findViewById(R.id.english_word_textview1);
        tvPronounce1 = (TextView) findViewById(R.id.pronounce_textview1);
        tvWord2 = (TextView) findViewById(R.id.english_word_textview2);
        tvPronounce2 = (TextView) findViewById(R.id.pronounce_textview2);
        learningLayout = (LinearLayout) findViewById(R.id.layout_learning);
        testBtn = (Button) findViewById(R.id.test_compare_Btn);
        header = (TextView) findViewById(R.id.compare_header_textview);

        youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_play_view);
        youTubePlayerFragment.initialize(PlayerConfig.API_KEY, this);

        Intent i = getIntent();
        vocabulary = (Vocabulary) i.getSerializableExtra("Vocabulary");

        if (vocabulary == null) {
            Toast.makeText(getApplicationContext(), "There is no app!", Toast.LENGTH_SHORT).show();
        } else {
            header.setText(vocabulary.getWord()[0] + " and " + vocabulary.getWord()[1]);
            btnLearn.setText("Learn: " + vocabulary.getWord()[0] + " and " + vocabulary.getWord()[1]);
            tvWord1.setText(vocabulary.getWord()[0]);
            tvWord2.setText(vocabulary.getWord()[1]);
            tvPronounce1.setText(vocabulary.getPronounce()[0]);
            tvPronounce2.setText(vocabulary.getPronounce()[1]);
            if(vocabulary.isLearned()){
                tvIsLearned.setVisibility(View.VISIBLE);
            }
        }

        btnLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLearning) {
                    learningLayout.setVisibility(View.GONE);
                    btnLearn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_plus_circle, 0);
                    isLearning = false;
                } else {
                    learningLayout.setVisibility(View.VISIBLE);
                    btnLearn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus_circle, 0);
                    isLearning = true;
                }
            }
        });
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Compare.this, TestCompare.class);
                intent.putExtra("Test", vocabulary);
                startActivity(intent);
            }
        });

        textToSpeech = new TextToSpeech(getBaseContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    resultSpeech = textToSpeech.setLanguage(Locale.US);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "You device is not support feature!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak "+ vocabulary.getWord()[0] +" or "+ vocabulary.getWord()[1] + " now...!" );
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
                    startActivityForResult(intent, 2);
                } catch (ActivityNotFoundException e) {
                    Intent your_browser_intent = new Intent(Intent.ACTION_VIEW,

                            Uri.parse("https://market.android.com/details?id=APP_PACKAGE_NAME"));
                    startActivity(your_browser_intent);
                }
            }
        });

        btnListen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doTextToVoice(vocabulary.getWord()[0]);
            }
        });
        btnListen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doTextToVoice(vocabulary.getWord()[1]);
            }
        });
    }

    public void doTextToVoice(String text) {

        if (resultSpeech == TextToSpeech.LANG_NOT_SUPPORTED || resultSpeech == TextToSpeech.LANG_MISSING_DATA) {
            Toast.makeText(getApplicationContext(),
                    "You device is not support feature!", Toast.LENGTH_SHORT).show();
        } else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        Log.e("Youtube", vocabulary.getYoutube());
        youTubePlayer.cueVideo(vocabulary.getYoutube());
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(Compare.this, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                getYoutubePlayerProvider().initialize(PlayerConfig.API_KEY, this);
            }
            case 2: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    tvResult.setText(result.get(0));

                    if (result.get(0).toLowerCase().equals(vocabulary.getWord()[0].toLowerCase())
                            || result.get(0).toLowerCase().equals(vocabulary.getWord()[1].toLowerCase())) {
                        tvResult.setTextColor(Color.parseColor("green"));
                    } else {
                        tvResult.setTextColor(Color.parseColor("red"));
                    }
                }
                break;
            }
        }
    }

    protected YouTubePlayer.Provider getYoutubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_play_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //open database
        DatabaseHandler db = new DatabaseHandler(getBaseContext());
        db.openDataBase();
        //get record
        vocabulary = db.getOneVocabulary(vocabulary.getId());
        db.close();
        if (vocabulary.isLearned()){
            tvIsLearned.setVisibility(View.VISIBLE);
        }
    }
}
