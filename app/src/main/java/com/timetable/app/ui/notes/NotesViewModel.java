package com.timetable.app.ui.notes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class NotesViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<String[]>> mText;

    public NotesViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<ArrayList<String[]>> getData() {
        return mText;
    }
}