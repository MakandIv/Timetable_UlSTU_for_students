package com.timetable.app.ui.noteEditor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.timetable.app.SiteData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class NoteEditorViewModel extends ViewModel {

    private MutableLiveData<Integer> dayOfWeek;
    private MutableLiveData<Integer> week;

    public NoteEditorViewModel() {
        dayOfWeek = new MutableLiveData<>();
        week = new MutableLiveData<>();
        dayOfWeek.setValue(SiteData.currentDayOfWeek() - 1);
        week.setValue(SiteData.currentWeek() - 1);
    }

    public LiveData<Integer> getDay() {
        return dayOfWeek;
    }

    public LiveData<Integer> getWeek() {
        return week;
    }

}