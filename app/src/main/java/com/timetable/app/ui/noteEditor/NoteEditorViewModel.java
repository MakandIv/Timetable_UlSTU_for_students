package com.timetable.app.ui.noteEditor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NoteEditorViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NoteEditorViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}