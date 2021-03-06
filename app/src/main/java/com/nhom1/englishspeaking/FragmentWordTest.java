package com.nhom1.englishspeaking;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nhom1.englishspeaking.Model.Vocabulary;
import com.nhom1.englishspeaking.R;

import java.util.Locale;
import java.util.Random;

public class FragmentWordTest extends Fragment {
    Vocabulary vocabulary;
    Button word1, word2, word3, word4;
    ImageButton listenBtn;
    String answer;
    TextToSpeech textToSpeech;
    int resultSpeech;
    public boolean result = false;
    TestWordInterface mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_trac_nghiem_word, container, false);
        word1 = (Button)rootView.findViewById(R.id.test_compare_word1);
        word2 = (Button)rootView.findViewById(R.id.test_compare_word2);
        word3 = (Button)rootView.findViewById(R.id.test_compare_word3);
        word4 = (Button)rootView.findViewById(R.id.test_compare_word4);

        listenBtn = (ImageButton)rootView.findViewById(R.id.tracnghiem_word_Btn);
        Bundle arguments = getArguments();
        if (arguments != null) {
            vocabulary = (Vocabulary)arguments.getSerializable("WordTest");
            //Toast.makeText(getContext(),vocabulary.getPhrase()[0],Toast.LENGTH_SHORT).show();
            word1.setText(vocabulary.getWord()[0]);
            word2.setText(vocabulary.getWord()[1]);
            if(!vocabulary.isOneWord()){
                word3.setText(vocabulary.getWords()[0]);
                word4.setText(vocabulary.getWords()[1]);
            }
            else{
                word3.setVisibility(View.GONE);
                word4.setVisibility(View.GONE);
            }

        }
        else {
            return rootView;
        }
        Random r = new Random();
        int wordNum = r.nextInt(2);

       // Toast.makeText(getContext(),""+wordNum, Toast.LENGTH_SHORT).show();

        if(wordNum == 0) answer = vocabulary.getWord()[0];
        else answer = vocabulary.getWord()[1];

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    resultSpeech = textToSpeech.setLanguage(Locale.US);
                }
                else{
                    Toast.makeText(getContext(),"Your device cannot read this word!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTextToVoiceTest(listenBtn);
            }
        });
        word1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word1.setBackgroundColor(Color.parseColor("#00cd00"));
                word2.setBackgroundResource(android.R.drawable.btn_default);
                if(!vocabulary.isOneWord()){
                    word3.setBackgroundResource(android.R.drawable.btn_default);
                    word4.setBackgroundResource(android.R.drawable.btn_default);
                }

                onClickAnswer(word1);
            }
        });
        word2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word1.setBackgroundResource(android.R.drawable.btn_default);
                word2.setBackgroundColor(Color.parseColor("#00cd00"));
                if(!vocabulary.isOneWord()) {
                    word3.setBackgroundResource(android.R.drawable.btn_default);
                    word4.setBackgroundResource(android.R.drawable.btn_default);
                }
                onClickAnswer(word2);
            }
        });
        if(!vocabulary.isOneWord()){
            word3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    word3.setBackgroundColor(Color.parseColor("#00cd00"));
                    word2.setBackgroundResource(android.R.drawable.btn_default);
                    word1.setBackgroundResource(android.R.drawable.btn_default);
                    word4.setBackgroundResource(android.R.drawable.btn_default);
                    onClickAnswer(word1);
                }
            });
            word4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    word1.setBackgroundResource(android.R.drawable.btn_default);
                    word4.setBackgroundColor(Color.parseColor("#00cd00"));
                    word3.setBackgroundResource(android.R.drawable.btn_default);
                    word2.setBackgroundResource(android.R.drawable.btn_default);
                    onClickAnswer(word2);
                }
            });
        }
        return rootView;
    }


    public void doTextToVoiceTest(View v){
        switch (v.getId()){
            case R.id.tracnghiem_word_Btn:
                if(resultSpeech == TextToSpeech.LANG_NOT_SUPPORTED || resultSpeech == TextToSpeech.LANG_MISSING_DATA){
                    Toast.makeText(getContext(),
                            "You device is not support feature!", Toast.LENGTH_SHORT).show();
                }
                else {
                    textToSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH, null);
                 //   Toast.makeText(getContext(), answer, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(getContext(),
                        "Error! You device cant speak this word!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    public void onClickAnswer(View v) {
        switch (v.getId()) {
            case R.id.test_compare_word1:
                if((vocabulary.getWord()[0].toLowerCase()).equals(answer.toLowerCase())){
               //     Toast.makeText(getContext(),"1 TRUE!", Toast.LENGTH_LONG).show();
                    mCallback.onReturnAnswer(true);
                }
                else {
                 //   Toast.makeText(getContext(), "1 FALSE!", Toast.LENGTH_LONG).show();
                    mCallback.onReturnAnswer(false);
                }
                break;
            case R.id.test_compare_word2:
                if((vocabulary.getWord()[1].toLowerCase()).equals(answer.toLowerCase())){
                  //  Toast.makeText(getContext(), "2 TRUE!", Toast.LENGTH_LONG).show();
                    mCallback.onReturnAnswer(true);
                }
                else{
                 //   Toast.makeText(getContext(), "2 FALSE!", Toast.LENGTH_LONG).show();
                    mCallback.onReturnAnswer(false);
                }
                break;
            default:
                throw new RuntimeException("Unknow button ID");
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
    public interface TestWordInterface {
        public boolean onReturnAnswer(boolean answer);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (TestWordInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TestWordInterface");
        }
    }

}
