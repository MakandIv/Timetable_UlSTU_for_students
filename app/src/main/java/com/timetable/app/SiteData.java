package com.timetable.app;

import android.content.Context;
import android.widget.Toast;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import java.lang.Math;

public class SiteData {
    private static final String url = "https://old.ulstu.ru/schedule/students";

    public static String[] groupData(Context context, String group) {
        String[] data = new String[3];

        Document doc;

        FileInputStream fis;
        try {
            fis = context.openFileInput("GroupData.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            data = (String[])ois.readObject();
            if (!data[0].equals(group)) {
                if (!MainActivity.hasConnection(context)) {
                    Toast.makeText(context, context.getString(R.string.no_connected_first), Toast.LENGTH_LONG).show();
                    return data;
                } else {
                    boolean f = false;
                    for (int i = 1; i <= 3; i++) {
                        if (f) {
                            break;
                        }
                        try {
                            doc = new LoadPage().execute(url + "/part" + i + "/raspisan.html").get();
                            if (doc == null) {
                                data[0] = group;
                                return data;
                            }
                            Element elements = doc.getElementsByTag("table").get(0).child(0);
                            int len = elements.childrenSize();
                            for (int l = 0; l < len - 1; l++) {
                                if (f) {
                                    break;
                                }
                                for (int j = 0; j < 6; j++) {
                                    if (elements.child(l + 1).child(j).child(0).child(0).child(0).text().contains(group)) {
                                        data[0] = group;
                                        data[1] = String.valueOf(i);
                                        data[2] = elements.child(l + 1).child(j).child(0).child(0).attr("href");
                                        f = true;
                                        break;
                                    }
                                }
                            }
                            if (f) {
                                FileOutputStream fos = context.openFileOutput("GroupData.txt", MainActivity.MODE_PRIVATE);
                                ObjectOutputStream oos = new ObjectOutputStream(fos);
                                oos.writeObject(data);
                                oos.close();
                                fos.close();
                            }
                        } catch (ExecutionException | InterruptedException | IOException ex) {
                            Toast.makeText(context, context.getString(R.string.no_access), Toast.LENGTH_LONG).show();
                            return data;
                        }
                    }
                    if (!f) {
                        Toast.makeText(context, context.getString(R.string.group_not_found), Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            if (!MainActivity.hasConnection(context)) {
                Toast.makeText(context, context.getString(R.string.no_connected_first), Toast.LENGTH_LONG).show();
                data[0] = group;
                return data;
            } else {
                boolean f = false;
                for (int i = 1; i <= 3; i++) {
                    if (f) {break;}
                    try {
                        doc = new LoadPage().execute(url + "/part" + i + "/raspisan.html").get();
                        if (doc == null) {
                            data[0] = group;
                            return data;
                        }
                        Element elements = doc.getElementsByTag("table").get(0).child(0);
                        for (int l = 0; l < elements.childrenSize(); l++) {
                            if (f) {break;}
                            for (int j = 0; j < 6; j++) {
                                if (elements.child(l + 1).child(j).child(0).child(0).child(0).text().equals(group)) {
                                    data[0] = group;
                                    data[1] = String.valueOf(i);
                                    data[2] = elements.child(l + 1).child(j).child(0).child(0).attr("href");
                                    f = true;
                                    break;
                                }
                            }
                        }
                        if (f) {
                            FileOutputStream fos = context.openFileOutput("GroupData.txt", MainActivity.MODE_PRIVATE);
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            oos.writeObject(data);
                            oos.close();
                            fos.close();
                        }
                    } catch (ExecutionException | InterruptedException | IOException ex) {
                        Toast.makeText(context, context.getString(R.string.no_connected_first), Toast.LENGTH_LONG).show();
                        data[0] = group;
                        return data;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            data[0] = group;
            return data;
        }
        return data;
    }

    public static int currentDayOfWeek() {
        int day;
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("u");
        day = Integer.parseInt(f.format(date));
        return day;
    }

    public static int currentWeek() {
        Date date = new Date();
        SimpleDateFormat y = new SimpleDateFormat("y");
        //String df = "01.09." + y.format(date);
        String df = "01.02." + y.format(date);
        SimpleDateFormat fd = new SimpleDateFormat ("dd.MM.yyyy");
        Date firstday = null;
        try {
            firstday = fd.parse(df);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat f = new SimpleDateFormat("w");

        assert firstday != null;
        int week = Integer.parseInt(f.format(date)) - Integer.parseInt(f.format(firstday));
        week = Math.abs(week);
        week %= 2;
        week += 1;

        return week;
    }

    public static String[] oneDay(int dow, int w, Document document) {
        String[] desc = new String[8];
        Element elements;
        try {
            elements = document.getElementsByTag("table").get(w - 1).child(0).child(dow + 1);
            for (int i = 0; i < 8; i++) {
                desc[i] = elements.child(i + 1).child(1).child(0).html();
                while (desc[i].contains("<br>")) {
                    desc[i] = desc[i].replace("<br>", "\n");
                }
            }
        } catch (IndexOutOfBoundsException e) {
            for (int i = 0; i < 8; i++) {
                desc[i] = "";
            }
        }
        return desc;
    }

    public static String[][] allTimetable(Context context, String[] groupData, boolean refresh) {
        String[][] allData = new String[14][8];
        if (!refresh) {
            FileInputStream fis;
            try {
                fis = context.openFileInput("data.txt");
                ObjectInputStream ois = new ObjectInputStream(fis);
                allData = (String[][]) ois.readObject();
                fis.close();
            } catch (IOException | ClassNotFoundException e) {
                refresh = true;
            }
            if (allData == null)
                refresh = true;
        }
        if (refresh) {
            if (!MainActivity.hasConnection(context)) {
                Toast.makeText(context, context.getString(R.string.no_connected_first), Toast.LENGTH_LONG).show();
                return null;
            }
            Document doc;
            try {
                doc = new LoadPage().execute(url + "/part" + groupData[1] + "/" + groupData[2]).get();
                if (doc == null) {
                    Toast.makeText(context, context.getString(R.string.no_access), Toast.LENGTH_LONG).show();
                    return allData;
                }
                String week = doc.getElementsByTag("p").get(0).child(1).childNode(2).toString();
                int i = 0;
                if (week.equals(" Неделя 2-я")) {
                    i = 7;
                }
                while (i < 14) {
                    assert allData != null;
                    allData[i] = oneDay((i % 7) + 1, (i / 7) + 1, doc);
                    i++;
                }
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(context, context.getString(R.string.no_access), Toast.LENGTH_LONG).show();
            }
            FileOutputStream fos;
            try {
                fos = context.openFileOutput("data.txt", Context.MODE_PRIVATE);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(allData);
                fos.close();
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return allData;
    }

}