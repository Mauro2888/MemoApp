package app.memo.com.memoapp.UI;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import app.memo.com.memoapp.R;
import app.memo.com.memoapp.UI.MainActivityMemo;


public class NoteAppWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String title = preferences.getString("title", null);
        String note = preferences.getString("note", null);


        Log.d("TAG", title + note);


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.note_app_widget);
        views.setTextViewText(R.id.noteTitle, title);
        views.setTextViewText(R.id.noteText, note);
        views.setInt(R.id.colorBarMain, "setBackgroundColor", preferences.getInt("color", 0));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, MainActivityMemo.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String title = preferences.getString("title", null);
            String note = preferences.getString("note", null);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.note_app_widget);
            views.setOnClickPendingIntent(R.id.btnWidget, pendingIntent);

            views.setTextViewText(R.id.noteTitle, title);
            views.setTextViewText(R.id.noteText, note);
            views.setInt(R.id.colorBarMain, "setBackgroundColor", preferences.getInt("color", 0));

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        super.onReceive(context, intent);
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

