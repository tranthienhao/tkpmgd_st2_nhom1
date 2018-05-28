package com.nhom1.englishspeaking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhom1.englishspeaking.Model.Vocabulary;
import com.nhom1.englishspeaking.R;

import java.util.List;


public class AdapterVocabulary  extends BaseAdapter {
    private List<Vocabulary> listData;
    private LayoutInflater layoutInflater;
    private Context context;


    public AdapterVocabulary(Context aContext, List<Vocabulary> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listData.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            // get the view
            convertView = layoutInflater.inflate(R.layout.list_item_vocabulary, null);
            holder = new ViewHolder();
            holder.word_textview1 = (TextView) convertView.findViewById(R.id.vocabulary_list_textview1);
            holder.word_textview2 = (TextView) convertView.findViewById(R.id.vocabulary_list_textview2);
            holder.ivChecked = (ImageView) convertView.findViewById(R.id.iv_checked);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //set content
        Vocabulary vocabulary = this.listData.get(position);

        holder.word_textview1.setText(vocabulary.getWord()[0]);
        holder.word_textview2.setText(vocabulary.getWord()[1]);

        // check if this voca was learned
        if(vocabulary.isLearned())
            holder.ivChecked.setVisibility(View.VISIBLE);

        return convertView;
    }
    static class ViewHolder {
        TextView word_textview1;
        TextView word_textview2;
        ImageView ivChecked;
    }
}