package com.timetable.app.ui.noteEditor;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.timetable.app.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.reflect.TypeToken;

public class NoteEditorFragment extends Fragment {
    RadioGroup onceOrMultiple;
    RadioButton selectOnce, selectMultiple;
    Spinner dayOfWeek, week, couple;
    EditText noteText;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NoteEditorViewModel noteEditorViewModel = new ViewModelProvider(this).get(NoteEditorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_note_editor, container, false);

        onceOrMultiple = root.findViewById(R.id.once_or_multiple);
        selectOnce = root.findViewById(R.id.select_once);
        selectMultiple = root.findViewById(R.id.select_multiple);
        dayOfWeek = root.findViewById(R.id.day_of_week);
        week = root.findViewById(R.id.week);
        couple = root.findViewById(R.id.couple);
        noteText = root.findViewById(R.id.note_text);

        selectOnce.setChecked(true);

        ArrayAdapter<String> a = new ArrayAdapter<>(root.getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{
                        getString(R.string.couple1),
                        getString(R.string.couple2),
                        getString(R.string.couple3),
                        getString(R.string.couple4),
                        getString(R.string.couple5),
                        getString(R.string.couple6),
                        getString(R.string.couple7),
                        getString(R.string.couple8)
        });
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        couple.setAdapter(a);
        couple.setSelection(0);

        noteEditorViewModel.getDay().observe(getViewLifecycleOwner(), d -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(root.getContext(),
                    android.R.layout.simple_spinner_item,
                    new String[]{
                            getString(R.string.monday),
                            getString(R.string.tuesday),
                            getString(R.string.wednesday),
                            getString(R.string.thursday),
                            getString(R.string.friday),
                            getString(R.string.saturday),
                            getString(R.string.sunday)});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dayOfWeek.setAdapter(adapter);
            dayOfWeek.setSelection(d);
        });

        noteEditorViewModel.getWeek().observe(getViewLifecycleOwner(), w -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(root.getContext(),
                    android.R.layout.simple_spinner_item,
                    new String[]{
                            getString(R.string.first_week),
                            getString(R.string.second_week)
            });
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            week.setAdapter(adapter);
            week.setSelection(w);
        });

        Button b = root.findViewById(R.id.save);
        b.setOnClickListener(v -> saveClick(root, v));

        return root;
    }

    private void saveClick(View root, View v) {
        final Type type = new TypeToken<ArrayList<HashMap<String, String>>>() {
        }.getType();

        ArrayList<HashMap<String, String>> arrayList;
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
            arrayList = new ArrayList<>();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("dayOfWeek", String.valueOf(dayOfWeek.getSelectedItemPosition()));
        map.put("week", String.valueOf(week.getSelectedItemPosition()));

        map.put("couple", String.valueOf(couple.getSelectedItemPosition()));
        map.put("noteText", String.valueOf(noteText.getText()));

        map.put("active", String.valueOf(true));

        arrayList.add(map);
        Gson gson = new Gson();
        FileOutputStream fos;
        try {
            fos = root.getContext().openFileOutput("NoteData.json", Context.MODE_PRIVATE);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.write(gson.toJson(arrayList, type).getBytes(StandardCharsets.UTF_8));
            oos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        NavController navController = Navigation.findNavController(root);
        navController.navigateUp();
    }
}