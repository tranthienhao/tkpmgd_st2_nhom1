package com.nhom1.englishspeaking;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nhom1.englishspeaking.Model.Vocabulary;
import com.nhom1.englishspeaking.R;

import java.util.ArrayList;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class FragmentWordSpeechTest extends Fragment {
    Vocabulary vocabulary;
    Button word1, word2;
    TextView resultText1, resultText2;
    ImageButton speechBtn1, speechBtn2;
    int answer = 0;
    String firstWord, secondWord;
    TextToSpeech textToSpeech;
    int resultSpeech, phrase2 = 0;
    public boolean result = false;
    FragmentWordSpeechTest.TestWordSpeechInterface mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_word_speech_test, container, false);

        word1 = (Button)rootView.findViewById(R.id.test_compare_word1);
        word2 = (Button)rootView.findViewById(R.id.test_compare_word2);
        speechBtn1 = (ImageButton)rootView.findViewById(R.id.speech_btn);
        speechBtn2 = (ImageButton)rootView.findViewById(R.id.speech_btn2);
        resultText1 =(TextView)rootView.findViewById(R.id.result_textview);
        resultText2 =(TextView)rootView.findViewById(R.id.result_textview2);

        Bundle arguments = getArguments();
        if (arguments != null) {
            vocabulary = (Vocabulary)arguments.getSerializable("WordTest");
            if(vocabulary == null){
                vocabulary= (Vocabulary)arguments.getSerializable("WordTest2");
                phrase2 = 1;
                word1.setText(vocabulary.getWords()[0] + " - " + vocabulary.getPronounce()[0]) ;
                firstWord = vocabulary.getWords()[0];
                word2.setText(vocabulary.getWords()[1] + " - " + vocabulary.getPronounce()[1]);
                secondWord = vocabulary.getWords()[1];
              //  Toast.makeText(getContext(),"Phrase 2",Toast.LENGTH_SHORT).show();
            }
            else {
               // Toast.makeText(getContext(),"Phrase 1",Toast.LENGTH_SHORT).show();

                word1.setText(vocabulary.getWord()[0] + " - " + vocabulary.getPronounce()[0]) ;
                firstWord = vocabulary.getWord()[0];
                word2.setText(vocabulary.getWord()[1] + " - " + vocabulary.getPronounce()[1]);
                secondWord = vocabulary.getWord()[1];
            }

        }
        else {
            return rootView;
        }

        speechBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...!");
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
                    startActivityForResult(intent, 1);
                }
                catch(ActivityNotFoundException e)
                {
                    Intent your_browser_intent = new Intent(Intent.ACTION_VIEW,

                            Uri.parse("https://market.android.com/details?id=APP_PACKAGE_NAME"));
                    startActivity(your_browser_intent);
                }
            }
        });
        speechBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...!");
                    intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US);
                    startActivityForResult(intent, 2);
                }
                catch(ActivityNotFoundException e)
                {
                    Intent your_browser_intent = new Intent(Intent.ACTION_VIEW,

                            Uri.parse("https://market.android.com/details?id=APP_PACKAGE_NAME"));
                    startActivity(your_browser_intent);

                }
            }
        });

        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    this.resultText1.setText(result.get(0));

                    if((firstWord.toLowerCase()).equals(result.get(0).toLowerCase())){
                        this.resultText1.setTextColor(Color.parseColor("green"));
                        answer++;
                        if(answer >= 2) mCallback.onReturnWordSpeechAnswer(true);
                        //Toast.makeText(getContext(),"TRUE WORD 1 " + answer, Toast.LENGTH_LONG).show();
                    }
                    else {
                        this.resultText1.setTextColor(Color.parseColor("red"));
                        answer++;
                        //Toast.makeText(getContext(),"FALSE WORD 1 " + answer, Toast.LENGTH_LONG).show();
                        if(answer >= 2) mCallback.onReturnWordSpeechAnswer(false);
                    }
                }
                break;
            }
            case 2: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    this.resultText2.setText(result.get(0));

                    if((secondWord.toLowerCase()).equals(result.get(0).toLowerCase())){
                        this.resultText2.setTextColor(Color.parseColor("green"));
                        answer++;
                        //Toast.makeText(getContext(),"TRUE WORD 2 " + answer, Toast.LENGTH_LONG).show();
                        if(answer >= 2) mCallback.onReturnWordSpeechAnswer(true);
                    }
                    else {
                        this.resultText2.setTextColor(Color.parseColor("red"));
                        answer++;
                        //Toast.makeText(getContext(),"FALSE WORD 2 " + answer, Toast.LENGTH_LONG).show();
                        if(answer >= 2) mCallback.onReturnWordSpeechAnswer(false);
                    }
                }
                break;
            }
            default:
                Toast.makeText(getContext(),"Error occur!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
    // Container Activity must implement this interface
    public interface TestWordSpeechInterface {
        public boolean onReturnWordSpeechAnswer(boolean answer);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (FragmentWordSpeechTest.TestWordSpeechInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TestWordInterface");
        }
    }

}