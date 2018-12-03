package com.spaz.spaz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import java.util.Calendar;

/**
 * Created by asyaky on 26/11/2016.
 */

public class AlarmSetter extends BroadcastReceiver {
    @Override
    // once phone reboot complete, set back all alarms
    public void onReceive(Context context, Intent intent) {
        Database database = new Database(context);
        Cursor cursor = database.getAllItems();
        try {
            while (cursor.moveToNext()) {

                String type = cursor.getString(cursor.getColumnIndex(Database.DB_COLUMN_TIPE));
                long time = cursor.getLong(cursor.getColumnIndex(Database.DB_COLUMN_TIME));

                if (type.equals("alert") && time > Calendar.getInstance().getTimeInMillis()) {
                    Intent service = new Intent(context, AlarmService.class);
                    service.setAction(AlarmService.CREATE);
                    service.putExtra("id",
                            cursor.getInt(cursor.getColumnIndex(Database.DB_COLUMN_ID)));
                    context.startService(service);
                }
            }
        } finally {
            cursor.close();
        }

    }
}