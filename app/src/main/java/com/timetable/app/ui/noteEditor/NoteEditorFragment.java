package com.timetable.app.ui.noteEditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.timetable.app.R;

public class NoteEditorFragment extends Fragment {

    private NoteEditorViewModel noteEditorViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        noteEditorViewModel =
                new ViewModelProvider(this).get(NoteEditorViewModel.class);
        View root = inflater.inflate(R.layout.fragment_note_editor, container, false);




        return root;
    }
}