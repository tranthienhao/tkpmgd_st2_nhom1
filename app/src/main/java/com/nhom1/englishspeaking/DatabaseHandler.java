package com.nhom1.englishspeaking;

/**
 * Created by Hao Tran Thien on 5/5/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nhom1.englishspeaking.Model.Vocabulary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "EnglishSpeaking.sqlite";
    // Table name
    private static final String TABLE_WORD = "WORD";
    private static final String TABLE_WORDS = "WORDS";
    private static final String TABLE_HISTORY = "HISTORY";

    private static final String DB_PATH_SUFFIX = "/databases/";

    static Context ctx;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    // select * from words
    public ArrayList<Vocabulary> getAllVocabularies() {
        ArrayList<Vocabulary> vocaList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_WORDS + " ORDER BY isLearned";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Vocabulary vocabulary = new Vocabulary();
                vocabulary.setId(Integer.parseInt(cursor.getString(0)));
                vocabulary.setOneWord(cursor.getInt(1) > 0);
                vocabulary.setYoutube(cursor.getString(4));
                vocabulary.setLearned(cursor.getInt(5) > 0);

                //SELECT FROM WORD BY ID
                List<String> mean = new ArrayList<>();
                List<String> word = new ArrayList<>();
                List<String> words = new ArrayList<>();
                List<String> phrase1 = new ArrayList<>();
                List<String> phrase2 = new ArrayList<>();
                List<String> pronounce = new ArrayList<>();
                List<String> type = new ArrayList<>();
                String wordQuery = "SELECT * FROM " + TABLE_WORD + " WHERE id ="
                        + cursor.getInt(2) + " OR id =" + cursor.getInt(3); // query word in words
                Cursor wordCursor = db.rawQuery(wordQuery, null);
                if (wordCursor.moveToFirst()) {
                    do {
                        mean.add(wordCursor.getString(1));
                        word.add(wordCursor.getString(2));
                        words.add(wordCursor.getString(3));
                        phrase1.add(wordCursor.getString(4));
                        phrase2.add(wordCursor.getString(5));
                        pronounce.add(wordCursor.getString(6));
                        type.add(wordCursor.getString(7));
                    } while (wordCursor.moveToNext());
                }
                while (wordCursor.moveToNext()) ;
                // Adding word to voca
                vocabulary.setMean(mean.toArray(new String[2]));
                vocabulary.setWord(word.toArray(new String[2]));
                vocabulary.setWords(words.toArray(new String[2]));
                vocabulary.setPhrase1(phrase1.toArray(new String[2]));
                vocabulary.setPhrase2(phrase2.toArray(new String[2]));
                vocabulary.setPronounce(pronounce.toArray(new String[2]));
                vocabulary.setType(type.toArray(new String[2]));
                // Adding item to list
                vocaList.add(vocabulary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return vocaList;
    }

    // select * from words
    public Vocabulary getOneVocabulary(int wordsID) {
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_WORDS + " WHERE id = " + wordsID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        Vocabulary vocabulary = new Vocabulary();
        if (cursor.moveToFirst()) {

            vocabulary.setId(Integer.parseInt(cursor.getString(0)));
            vocabulary.setOneWord(cursor.getInt(1) > 0);
            vocabulary.setYoutube(cursor.getString(4));
            vocabulary.setLearned(cursor.getInt(5) > 0);

            //SELECT FROM WORD BY ID
            List<String> mean = new ArrayList<>();
            List<String> word = new ArrayList<>();
            List<String> words = new ArrayList<>();
            List<String> phrase1 = new ArrayList<>();
            List<String> phrase2 = new ArrayList<>();
            List<String> pronounce = new ArrayList<>();
            List<String> type = new ArrayList<>();
            String wordQuery = "SELECT * FROM " + TABLE_WORD + " WHERE id ="
                    + cursor.getInt(2) + " OR id =" + cursor.getInt(3); // query word in words
            Cursor wordCursor = db.rawQuery(wordQuery, null);
            if (wordCursor.moveToFirst()) {
                do {
                    mean.add(wordCursor.getString(1));
                    word.add(wordCursor.getString(2));
                    words.add(wordCursor.getString(3));
                    phrase1.add(wordCursor.getString(4));
                    phrase2.add(wordCursor.getString(5));
                    pronounce.add(wordCursor.getString(6));
                    type.add(wordCursor.getString(7));
                } while (wordCursor.moveToNext());
            }
            while (wordCursor.moveToNext()) ;
            // Adding word to voca
            vocabulary.setMean(mean.toArray(new String[2]));
            vocabulary.setWord(word.toArray(new String[2]));
            vocabulary.setWords(words.toArray(new String[2]));
            vocabulary.setPhrase1(phrase1.toArray(new String[2]));
            vocabulary.setPhrase2(phrase2.toArray(new String[2]));
            vocabulary.setPronounce(pronounce.toArray(new String[2]));
            vocabulary.setType(type.toArray(new String[2]));
        }
        cursor.close();
        return vocabulary;
    }

    // search words
    public ArrayList<Vocabulary> searchWords(String content) {
        ArrayList<Vocabulary> vocaList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_WORD + " WHERE word LIKE '%" + content + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
                String wordsQuery = "SELECT * FROM " + TABLE_WORDS + " WHERE word_id1 ="
                        + cursor.getInt(0) + " OR word_id2 =" + cursor.getInt(0);
                Cursor wordsCursor = db.rawQuery(wordsQuery, null);
                if (wordsCursor.moveToFirst()) {
                   Vocabulary vocabulary = getOneVocabulary(wordsCursor.getInt(0));
                   vocaList.add(vocabulary);
                }
        }
        cursor.close();
        return vocaList;
    }

    // insert when click in words
    public void insertToHistory(int wordsID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("words_id", wordsID);
        db.insert(TABLE_HISTORY, null, value);
    }

    // update learned words
    public void updateLearnedWord(int wordsID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("isLearned", 1);
        db.update(TABLE_WORDS, value,"id = "+ wordsID, null);
    }

    //get * from history
    public ArrayList<Vocabulary> getHistory() {
        ArrayList<Vocabulary> vocaList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_HISTORY +
                ", " + TABLE_WORDS + " WHERE words_id = words.id ORDER BY isLearned";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Vocabulary vocabulary = new Vocabulary();
                vocabulary.setId(Integer.parseInt(cursor.getString(0)));
                vocabulary.setOneWord(cursor.getInt(2) > 0);
                vocabulary.setYoutube(cursor.getString(5));
                vocabulary.setLearned(cursor.getInt(6) > 0);

                //SELECT FROM WORD BY ID
                List<String> mean = new ArrayList<>();
                List<String> word = new ArrayList<>();
                List<String> words = new ArrayList<>();
                List<String> phrase1 = new ArrayList<>();
                List<String> phrase2 = new ArrayList<>();
                List<String> pronounce = new ArrayList<>();
                List<String> type = new ArrayList<>();
                String wordQuery = "SELECT * FROM " + TABLE_WORD + " WHERE id ="
                        + cursor.getInt(3) + " OR id =" + cursor.getInt(4); // query word in words
                Cursor wordCursor = db.rawQuery(wordQuery, null);
                if (wordCursor.moveToFirst()) {
                    do {
                        mean.add(wordCursor.getString(1));
                        word.add(wordCursor.getString(2));
                        words.add(wordCursor.getString(3));
                        phrase1.add(wordCursor.getString(4));
                        phrase2.add(wordCursor.getString(5));
                        pronounce.add(wordCursor.getString(6));
                        type.add(wordCursor.getString(7));
                    } while (wordCursor.moveToNext());
                }
                while (wordCursor.moveToNext()) ;
                // Adding word to voca
                vocabulary.setMean(mean.toArray(new String[2]));
                vocabulary.setWord(word.toArray(new String[2]));
                vocabulary.setWords(words.toArray(new String[2]));
                vocabulary.setPhrase1(phrase1.toArray(new String[2]));
                vocabulary.setPhrase2(phrase2.toArray(new String[2]));
                vocabulary.setPronounce(pronounce.toArray(new String[2]));
                vocabulary.setType(type.toArray(new String[2]));
                // Adding item to list
                vocaList.add(vocabulary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return vocaList;
    }

    public void CopyDataBaseFromAsset() throws IOException {

        InputStream myInput = ctx.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = getDatabasePath();

        // if the path doesn't exist first, create it
        File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    private static String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        File dbFile = ctx.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}