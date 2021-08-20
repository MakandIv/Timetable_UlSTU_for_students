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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class NotesFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotesViewModel notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notes, container, false);

        Button b = root.findViewById(R.id.add_notes);
        b.setOnClickListener(v -> click(root, v));

        final Type type = new TypeToken<ArrayList<Map<String, Object>>>() {
        }.getType();
        ArrayList<Map<String, Object>> arrayList = null;
        try {
            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(new FileReader("NoteData.json"));
            arrayList = gson.fromJson(jsonReader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (arrayList != null) {
            TableLayout tableLayout = root.findViewById(R.id.table_notes);
            for (Map<String, Object> map : arrayList) {
                TableRow tableRow = new TableRow(tableLayout.getContext());
                tableRow.setLayoutParams(new TableRow.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
                tableRow.setBackgroundColor(Color.parseColor("#C8C8C8"));
                tableRow.setOrientation(TableRow.VERTICAL);
                LinearLayout linearLayout = new LinearLayout(tableRow.getContext());
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (float) 0.2
                ));
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setBackgroundColor(getResources().getColor(R.color.purple_500));
                TextView dayOfWeekText = new TextView(linearLayout.getContext());
                dayOfWeekText.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                switch ((int)map.get("dayOfWeek")) {
                    case 0:
                        dayOfWeekText.setText("Пн");
                        break;
                    case 1:
                        dayOfWeekText.setText("Вт");
                        break;
                    case 2:
                        dayOfWeekText.setText("Ср");
                        break;
                    case 3:
                        dayOfWeekText.setText("Чт");
                        break;
                    case 4:
                        dayOfWeekText.setText("Пт");
                        break;
                    case 5:
                        dayOfWeekText.setText("Сб");
                        break;
                    case 6:
                        dayOfWeekText.setText("Вс");
                        break;
                }
                dayOfWeekText.setTextColor(getResources().getColor(R.color.white));
                TextView weekText = new TextView(linearLayout.getContext());
                weekText.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                switch ((int)map.get("week")) {
                    case 0:
                        weekText.setText(getString(R.string.first_week_alt));
                        break;
                    case 1:
                        weekText.setText(getString(R.string.second_week_alt));
                        break;
                }
                weekText.setTextColor(getResources().getColor(R.color.white));
                TextView noteText = new TextView(tableRow.getContext());
                noteText.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        (float) 0.1
                ));
                noteText.setGravity(Gravity.CENTER|Gravity.CENTER_VERTICAL);
                noteText.setText((String)map.get("noteText"));

                Switch switchEdit = new Switch(tableRow.getContext());
                switchEdit.setBackgroundColor(getResources().getColor(R.color.purple_500));
                switchEdit.setChecked((boolean)map.get("active"));
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