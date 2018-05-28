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
import android.widget.TextView;
import android.widget.Toast;

import com.nhom1.englishspeaking.Model.Vocabulary;
import com.nhom1.englishspeaking.R;

import java.util.Locale;
import java.util.Random;


public class FragmentPhraseTest extends Fragment {
    Vocabulary vocabulary;
    Button word1, word2, word3, word4;
    ImageButton listenBtn;
    String answer, firstPhrase, secondPhrase;
    TextToSpeech textToSpeech;
    TextView header;
    int resultSpeech;
    int phrase2 = 0;
    public boolean result = false;
    TestPhraseInterface mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_fragment_phares_test, container, false);
        word1 = (Button)rootView.findViewById(R.id.test_compare_word1);
        word2 = (Button)rootView.findViewById(R.id.test_compare_word2);
        word3 = (Button)rootView.findViewById(R.id.test_compare_word3);
        word4 = (Button)rootView.findViewById(R.id.test_compare_word4);

        listenBtn = (ImageButton)rootView.findViewById(R.id.tracnghiem_Btn);
        header = (TextView)rootView.findViewById(R.id.header_textview);

        Bundle arguments = getArguments();
        if (arguments != null) {
            vocabulary = (Vocabulary)arguments.getSerializable("WordTest");
            if(vocabulary == null){
                vocabulary= (Vocabulary)arguments.getSerializable("WordTest2");
                phrase2 = 1;
                header.setText("Which word is contained in the sentence below?");
                word1.setText(vocabulary.getWord()[0]);
                firstPhrase = vocabulary.getPhrase1()[1];
                word2.setText(vocabulary.getWord()[1]);
                secondPhrase = vocabulary.getPhrase2()[1];
              //  word3.setText(vocabulary.getWords()[0]);
                word3.setVisibility(View.GONE);
                //word4.setText(vocabulary.getWords()[1]);
                word4.setVisibility(View.GONE);
                //Toast.makeText(getContext(),"Phrase 2",Toast.LENGTH_SHORT).show();
            }
            else {
                //Toast.makeText(getContext(),"Phrase 1",Toast.LENGTH_SHORT).show();

                word1.setText(vocabulary.getPhrase1()[0]);
                word2.setText(vocabulary.getPhrase2()[0]);
                word3.setText(vocabulary.getPhrase1()[1]);
                word4.setText(vocabulary.getPhrase2()[1]);
                firstPhrase = vocabulary.getPhrase1()[0];
                secondPhrase = vocabulary.getPhrase2()[0];

            }

        }
        else {
            return rootView;
        }
        Random r = new Random();
        int wordNum = r.nextInt(2);

       // Toast.makeText(getContext(),""+wordNum, Toast.LENGTH_SHORT).show();
        if(phrase2 == 0){
            if(wordNum == 0) answer = vocabulary.getPhrase1()[0];
            else answer = vocabulary.getPhrase2()[0];
        }
        else {
            if(wordNum == 0) answer = vocabulary.getPhrase1()[1];
            else answer = vocabulary.getPhrase2()[1];
        }

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
                word2.setBackgroundResource(android.R.drawable.btn_default);
                word1.setBackgroundColor(Color.parseColor("#00cd00"));
                word3.setBackgroundResource(android.R.drawable.btn_default);
                word4.setBackgroundResource(android.R.drawable.btn_default);
                onClickAnswer(word1);
            }
        });
        word2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word1.setBackgroundResource(android.R.drawable.btn_default);
                word2.setBackgroundColor(Color.parseColor("#00cd00"));
                word3.setBackgroundResource(android.R.drawable.btn_default);
                word4.setBackgroundResource(android.R.drawable.btn_default);
                onClickAnswer(word2);
            }
        });
        word3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word2.setBackgroundResource(android.R.drawable.btn_default);
                word4.setBackgroundColor(Color.parseColor("#00cd00"));
                word3.setBackgroundResource(android.R.drawable.btn_default);
                word1.setBackgroundResource(android.R.drawable.btn_default);
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
        return rootView;
    }

    public void doTextToVoiceTest(View v){
        switch (v.getId()){
            case R.id.tracnghiem_Btn:
                if(resultSpeech == TextToSpeech.LANG_NOT_SUPPORTED || resultSpeech == TextToSpeech.LANG_MISSING_DATA){
                    Toast.makeText(getContext(),
                            "You device is not support feature!", Toast.LENGTH_SHORT).show();
                }
                else {
                    textToSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH, null);
//                    Toast.makeText(getContext(),
//                            answer, Toast.LENGTH_SHORT).show();
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
                if((firstPhrase.toLowerCase()).equals(answer.toLowerCase())){
                   // Toast.makeText(getContext(),"1 TRUE!", Toast.LENGTH_LONG).show();
                    mCallback.onReturnAnswerPhrase(true);
                }
                else {
                   // Toast.makeText(getContext(), "1 FALSE!", Toast.LENGTH_LONG).show();
                    mCallback.onReturnAnswerPhrase(false);
                }
                break;
            case R.id.test_compare_word2:
                if((secondPhrase.toLowerCase()).equals(answer.toLowerCase())){
              //      Toast.makeText(getContext(), "2 TRUE!", Toast.LENGTH_LONG).show();
                    mCallback.onReturnAnswerPhrase(true);
                }
                else{
                  //  Toast.makeText(getContext(),"2 FALSE!", Toast.LENGTH_LONG).show();
                    mCallback.onReturnAnswerPhrase(false);
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
    public interface TestPhraseInterface {
        public boolean onReturnAnswerPhrase(boolean answer);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (TestPhraseInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TestWordInterface");
        }
    }

}