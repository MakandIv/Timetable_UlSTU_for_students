package com.timetable.app.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.timetable.app.SiteData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Integer> dayOfWeek;
    private final MutableLiveData<Integer> week;
    private final MutableLiveData<String> group;
    String[] data;

    public HomeViewModel() {
        dayOfWeek = new MutableLiveData<>();
        week = new MutableLiveData<>();
        group = new MutableLiveData<>();
        dayOfWeek.setValue(SiteData.currentDayOfWeek() - 1);
        week.setValue(SiteData.currentWeek() - 1);

        FileInputStream fis2;
        try {
            fis2 = new FileInputStream("GroupData.txt");
            ObjectInputStream ois = new ObjectInputStream(fis2);
            data = (String[]) ois.readObject();
            fis2.close();
        } catch (FileNotFoundException e) {
            data = null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (data != null) {
            group.setValue(data[0]);
        }

    }

    public LiveData<String> getGroup() {
        return group;
    }

    public LiveData<Integer> getDay() {
        return dayOfWeek;
    }

    public LiveData<Integer> getWeek() {
        return week;
    }
}