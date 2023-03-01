package com.example.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    //create new variable
    private static final String mSharedPrefFile = "com..example.android.widget";
    //untuk menyimpan dan mengakses
    private static final String COUNT_KEY = "count";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        //nampilin id, id dah di ada di atas tinggal di set buat nampilin, me refer ke content yg di appwidget atas
        views.setTextViewText(R.id.app_widget_id, String.valueOf(appWidgetId));

        //counter baca dari sharedpreference
        SharedPreferences preferences = context.getSharedPreferences(mSharedPrefFile, 0);
        //sebagai aditifier dgn widget yg sesuai dgn id widget nya
        int count = preferences.getInt(COUNT_KEY+appWidgetId, 0);
        count++;
        views.setTextViewText(R.id.appwidget_count,String.valueOf(count));

        //sp buat editornya
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(COUNT_KEY+appWidgetId, count);
        editor.apply();

        //nampilin jam
        String dateString = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
        views.setTextViewText(R.id.appwidget_update, dateString);

        //intent yg dibungkus dlm pending intent untuk update
        //ktk melakukan update dia akan melakukan bc, dan yg menerima
        Intent intentUpdate = new Intent(context, NewAppWidget.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] idArray = new int[]{appWidgetId};
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        views.setOnClickPendingIntent(R.id.button_update, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}