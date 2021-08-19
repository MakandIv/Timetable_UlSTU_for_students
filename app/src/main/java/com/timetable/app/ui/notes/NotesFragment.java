package com.timetable.app.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.timetable.app.R;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotesViewModel notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notes, container, false);

        Button b = root.findViewById(R.id.add_notes);
        b.setOnClickListener(this::click);



        notesViewModel.getData().observe(getViewLifecycleOwner(), new Observer<ArrayList<String[]>>() {
            @Override
            public void onChanged(ArrayList<String[]> strings) {

            }
        });

        return root;
    }

    public void click(View v) {
        if (v.getId() == R.id.add_notes) {
            NavController navController = Navigation.findNavController(this.getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_note_editor);
        }
    }
}