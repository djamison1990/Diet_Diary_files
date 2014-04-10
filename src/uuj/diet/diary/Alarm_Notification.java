package uuj.diet.diary;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import uuj.diet.diary.R;

public class Alarm_Notification extends BroadcastReceiver {
	NotificationManager nm;
     @SuppressWarnings("deprecation")
	@Override
    public void onReceive(Context context, Intent intent) {
        
    	 context.getApplicationContext();
		nm = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
         Intent i = new Intent(context.getApplicationContext(),Home.class);
       //creates the notification
         Notification n = new Notification(R.drawable.uuj_launcher,"Remember to update your diet diary!", System.currentTimeMillis());
       
         //creates the pending intent, an intent held in waiting until the notification is clicked.
         PendingIntent pi = PendingIntent.getActivity( context.getApplicationContext(),0, i,0);
         //sets the flag to remove itself when clicked
         n.flags |= Notification.FLAG_AUTO_CANCEL;
         //sets the notification to utilise the default methods to notify the user, things such as vibration or sound
         n.defaults = Notification.DEFAULT_ALL;
         //sets the notification title, text and the pending intent which it is to call on click.
         n.setLatestEventInfo(context.getApplicationContext(), "UUJ Diet Diary", "Remember to update your diet diary!!", pi);
         //fires the notification, 6390 is a number to identify the notification
         nm.notify(6390, n);
       }
}
