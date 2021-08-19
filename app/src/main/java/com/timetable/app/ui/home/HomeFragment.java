package com.timetable.app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.timetable.app.R;
import com.timetable.app.SiteData;

public class HomeFragment extends Fragment {

    String[] data = new String[3]; //данные группы
    String[] desc = new String[8]; //описание пар на день
    String[][] all_desc; //описание пар на две недели

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final EditText editGroup = root.findViewById(R.id.editGroup);
        final Spinner dayOfWeek = root.findViewById(R.id.day_of_week);
        final Spinner week = root.findViewById(R.id.week);

        Button b = root.findViewById(R.id.button);
        b.setOnClickListener(v -> click(root, v));

        homeViewModel.getGroup().observe(getViewLifecycleOwner(), editGroup::setText);

        homeViewModel.getDay().observe(getViewLifecycleOwner(), d -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_spinner_item, new String[]{getString(R.string.monday), getString(R.string.tuesday), getString(R.string.wednesday), getString(R.string.thursday), getString(R.string.friday), getString(R.string.saturday), getString(R.string.sunday)});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dayOfWeek.setAdapter(adapter);
            dayOfWeek.setSelection(d);
        });

        homeViewModel.getWeek().observe(getViewLifecycleOwner(), w -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_spinner_item, new String[]{getString(R.string.first_week), getString(R.string.second_week)});
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            week.setAdapter(adapter);
            week.setSelection(w);
        });
        return root;

    }

    public void click(View root, View v) {
        if (v.getId() == R.id.button) {
            EditText editText = root.findViewById(R.id.editGroup);
            Spinner day_of_week = root.findViewById(R.id.day_of_week);
            Spinner week = root.findViewById(R.id.week);
            int dow = 0, w = 0;

            switch (day_of_week.getSelectedItem().toString()) {
                case "Понедельник":
                case "Monday":
                    dow = 1;
                    break;
                case "Вторник":
                case "Tuesday":
                    dow = 2;
                    break;
                case "Среда":
                case "Wednesday":
                    dow = 3;
                    break;
                case "Четверг":
                case "Thursday":
                    dow = 4;
                    break;
                case "Пятница":
                case "Friday":
                    dow = 5;
                    break;
                case "Суббота":
                case "Saturday":
                    dow = 6;
                    break;
                case "Воскресенье":
                case "Sunday":
                    dow = 7;
                    break;
            }
            switch (week.getSelectedItem().toString()) {
                case "Первая неделя":
                case "First Week":
                    w = 1;
                    break;
                case "Вторая неделя":
                case "Second Week":
                    w = 2;
                    break;
            }
            if (all_desc == null) {
                data = SiteData.groupData(root.getContext(), editText.getText().toString());
                all_desc = SiteData.allTimetable(root.getContext(), data, true);
            }
            else if (data == null || data[0] == null) {
                data = SiteData.groupData(root.getContext(), editText.getText().toString());
                all_desc = SiteData.allTimetable(root.getContext(), data, true);
            } else if (!data[0].equals(editText.getText().toString())) {
                data = SiteData.groupData(root.getContext(), editText.getText().toString());
                all_desc = SiteData.allTimetable(root.getContext(), data, true);
            } else {
                all_desc = SiteData.allTimetable(root.getContext(), data, false);
            }

            assert all_desc != null;
            desc = all_desc[(w - 1) * 7 + dow - 1];
        }
    }
}