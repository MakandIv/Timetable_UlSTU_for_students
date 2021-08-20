package com.timetable.app.ui.notes;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.timetable.app.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class NotesFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotesViewModel notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notes, container, false);
        final int USER_ID = 60000;

        Button b = root.findViewById(R.id.add_notes);
        b.setOnClickListener(v -> click(root, v));

        final Type type = new TypeToken<ArrayList<Map<String, String>>>() {}.getType();
        ArrayList<Map<String, String>> arrayList = null;
        FileInputStream fis;
        try {
            fis = root.getContext().openFileInput("NoteData.json");
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Gson gson = new Gson();
            arrayList = gson.fromJson(ois.readLine(), type);
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int countID = 1;

        if (arrayList != null) {
            TableLayout tableLayout = root.findViewById(R.id.table_notes);
            for (Map<String, String> map : arrayList) {
                TableRow tableRow = new TableRow(tableLayout.getContext());
                tableRow.setLayoutParams(new TableLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
                tableRow.setBackgroundColor(Color.parseColor("#C8C8C8"));
                tableRow.setOrientation(TableRow.HORIZONTAL);
                tableRow.setId(USER_ID + countID - 1550);
                tableLayout.addView(tableRow);
                LinearLayout linearLayout = new LinearLayout(tableRow.getContext());
                linearLayout.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (float) 0.2
                ));
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setBackgroundColor(getResources().getColor(R.color.purple_500));
                linearLayout.setId(USER_ID + countID - 1450);
                tableRow.addView(linearLayout);
                TextView dayOfWeekText = new TextView(linearLayout.getContext());
                dayOfWeekText.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                switch (map.get("dayOfWeek")) {
                    case "0":
                        dayOfWeekText.setText("Пн");
                        break;
                    case "1":
                        dayOfWeekText.setText("Вт");
                        break;
                    case "2":
                        dayOfWeekText.setText("Ср");
                        break;
                    case "3":
                        dayOfWeekText.setText("Чт");
                        break;
                    case "4":
                        dayOfWeekText.setText("Пт");
                        break;
                    case "5":
                        dayOfWeekText.setText("Сб");
                        break;
                    case "6":
                        dayOfWeekText.setText("Вс");
                        break;
                }
                dayOfWeekText.setTextColor(getResources().getColor(R.color.white));
                dayOfWeekText.setId(USER_ID + countID - 6050);
                dayOfWeekText.setGravity(Gravity.CENTER_HORIZONTAL);
                linearLayout.addView(dayOfWeekText);
                TextView weekText = new TextView(linearLayout.getContext());
                weekText.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                switch (map.get("week")) {
                    case "0":
                        weekText.setText(getString(R.string.first_week_alt));
                        break;
                    case "1":
                        weekText.setText(getString(R.string.second_week_alt));
                        break;
                }
                weekText.setTextColor(getResources().getColor(R.color.white));
                weekText.setGravity(Gravity.CENTER_HORIZONTAL);
                weekText.setId(USER_ID + countID - 5151);
                linearLayout.addView(weekText);
                TextView coupleText = new TextView(tableRow.getContext());
                coupleText.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (float) 0.1
                ));
                coupleText.setBackgroundColor(getResources().getColor(R.color.purple_500));
                coupleText.setTextColor(getResources().getColor(R.color.white));
                coupleText.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                switch (map.get("couple")) {
                    case "0":
                        coupleText.setText(getString(R.string.couple1));
                        break;
                    case "1":
                        coupleText.setText(getString(R.string.couple2));
                        break;
                    case "2":
                        coupleText.setText(getString(R.string.couple3));
                        break;
                    case "3":
                        coupleText.setText(getString(R.string.couple4));
                        break;
                    case "4":
                        coupleText.setText(getString(R.string.couple5));
                        break;
                    case "5":
                        coupleText.setText(getString(R.string.couple6));
                        break;
                    case "6":
                        coupleText.setText(getString(R.string.couple7));
                        break;
                    case "7":
                        coupleText.setText(getString(R.string.couple8));
                        break;
                }
                tableRow.addView(coupleText);
                TextView noteText = new TextView(tableRow.getContext());
                noteText.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1
                ));
                noteText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.FILL_VERTICAL);
                noteText.setText(new String(map.get("noteText").getBytes(), StandardCharsets.UTF_8));
                tableRow.addView(noteText);

                Switch switchEdit = new Switch(tableRow.getContext());
                switchEdit.setBackgroundColor(getResources().getColor(R.color.purple_500));
                switchEdit.setChecked(Boolean.parseBoolean(Objects.requireNonNull(map.get("active"))));
                switchEdit.setId(USER_ID + countID - 4321);
                tableRow.addView(switchEdit);
                countID++;
            }

        };

        return root;
    }

    public void click(View root, View v) {
        if (v.getId() == R.id.add_notes) {
            NavController navController = Navigation.findNavController(root);
            navController.navigate(R.id.nav_note_editor);
        }
    }
}