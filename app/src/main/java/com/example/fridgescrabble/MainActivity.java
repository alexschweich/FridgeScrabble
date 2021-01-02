package com.example.fridgescrabble;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    HashMap<String, Integer> base_letters = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doNewGame(View view) {
        File directory = getFilesDir();
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }

        base_letters.put("A", 9);
        base_letters.put("B", 2);
        base_letters.put("C", 2);
        base_letters.put("D", 4);
        base_letters.put("E", 12);
        base_letters.put("F", 2);
        base_letters.put("G", 3);
        base_letters.put("H", 2);
        base_letters.put("I", 9);
        base_letters.put("J", 1);
        base_letters.put("K", 1);
        base_letters.put("L", 4);
        base_letters.put("M", 2);
        base_letters.put("N", 6);
        base_letters.put("O", 8);
        base_letters.put("P", 2);
        base_letters.put("Q", 1);
        base_letters.put("R", 6);
        base_letters.put("S", 4);
        base_letters.put("T", 6);
        base_letters.put("U", 4);
        base_letters.put("V", 2);
        base_letters.put("W", 2);
        base_letters.put("X", 1);
        base_letters.put("Y", 2);
        base_letters.put("Z", 1);

        for (String letter : base_letters.keySet()) {
            String filename = letter + ".json";
            JSONObject object = new JSONObject();
            try {
                object.put("Letter", letter);
                object.put("Count", base_letters.get(letter));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                FileOutputStream fout = openFileOutput(filename, MODE_PRIVATE);
                OutputStreamWriter writer = new OutputStreamWriter(fout);
                writer.write(object.toString());
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(getApplicationContext(), "New game ready", Toast.LENGTH_LONG).show();
    }

    public void submitUsedLetters(View view) {
        final EditText input = (EditText) findViewById(R.id.usedLetters);
        final String[] m_text = {""};
        m_text[0] = input.getText().toString().toUpperCase();
        char[] entered = m_text[0].toCharArray();

        File directory = getFilesDir();
        File[] files = directory.listFiles();
        for (File file : files) {
            try {
                Scanner reader = new Scanner(file);
                JSONObject object = null;
                while (reader.hasNextLine()) {
                    object = new JSONObject(reader.nextLine());
                }
                String letter = object.getString("Letter").toUpperCase();
                Integer currentCount = object.getInt("Count");
                for (char c : entered) {
                    if (c == letter.charAt(0)) {
                        String filename = letter + ".json";
                        deleteFile(filename);
                        if (currentCount - 1 == 0) {
                            continue;
                        }
                        JSONObject newObject = new JSONObject();
                        try {
                            newObject.put("Letter", letter);
                            newObject.put("Count", currentCount - 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            FileOutputStream fout = openFileOutput(filename, MODE_PRIVATE);
                            OutputStreamWriter writer = new OutputStreamWriter(fout);
                            writer.write(newObject.toString());
                            writer.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        input.setText("");
        StringBuilder removed = new StringBuilder("Removed: ");
        for (char c : entered) {
            removed.append(c).append(" ");
        }
        Toast.makeText(getApplicationContext(), removed, Toast.LENGTH_LONG).show();
        TextView foundView = (TextView) findViewById(R.id.foundLetters);
        foundView.setText("");
    }

    public void getLetters(View view) {
        ArrayList<String> all_letters = new ArrayList<>();

        File directory = getFilesDir();
        File[] files = directory.listFiles();
        for (File file : files != null ? files : new File[0]) {    // foreach assert files not null
            try {
                Scanner reader = new Scanner(file);
                JSONObject object = null;
                while (reader.hasNextLine()) {
                    object = new JSONObject(reader.nextLine());
                }
                String letter = object.getString("Letter");
                Integer currentCount = object.getInt("Count");
                for (int i = 0; i < currentCount; i++) {
                    all_letters.add(letter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Random rand = new Random();
        ArrayList<Integer> usedNumbers = new ArrayList<>();
        StringBuilder selected_letters = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            Integer random = rand.nextInt(all_letters.size());
            boolean keepTrying = true;
            while (keepTrying) {
                if (!usedNumbers.contains(random)) {
                    keepTrying = false;
                    usedNumbers.add(random);
                    selected_letters.append(all_letters.get(random).toUpperCase()).append(" ");
                } else {
                    random = rand.nextInt(all_letters.size());
                }
            }
        }
        TextView textView = (TextView) findViewById(R.id.foundLetters);
        textView.setText(selected_letters);
    }

}