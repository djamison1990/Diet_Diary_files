package uuj.diet.diary;


import java.util.Calendar;
import uuj.diet.diary.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class Reminders extends Activity  {
	


	
	Spinner spinner;
	
	Button bt1,bt2;
	NotificationManager nm;
	TimePicker tp;
	AlarmManager am;
	PendingIntent pendingIntent;
	 AlarmManager alarmManager;
	 boolean alarmSet;
	 Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
   
        // get action bar   
        ActionBar actionBar = getActionBar();
 
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        
         bt1 = (Button) findViewById(R.id.btnNotify);
         bt2 = (Button) findViewById(R.id.btnCancelNotifications);
         nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
         am = (AlarmManager) getSystemService(ALARM_SERVICE);
         tp = (TimePicker) findViewById(R.id.timePicker1);
        		 addListeners();
        		 tp.setIs24HourView(DateFormat.is24HourFormat(this));
         intent = new Intent(getBaseContext(), Alarm_Notification.class);
         pendingIntent = PendingIntent.getBroadcast( getBaseContext(),0, intent, 0);
         alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        		 
 	}


    public void addListeners(){
   	 addButtonClickListener(bt1);
   	 addButtonClickListener(bt2);
   }
        
    public void addButtonClickListener(final Button x){
    	
    	x.setOnClickListener(new View.OnClickListener() {
          
    		public void onClick(View v) {
    		if(x==bt1){
    			
    			
    			  //pulls the current hour and minute values from the time picker, sets the calendar values for minute and time  and fires the alarm	    	
    	    	 Calendar cal = Calendar.getInstance();
    	    	 //calculations present due to difference in 24 hour and 12 hour clock systems causing an error during testing
    	    	 int minute = (int) ( tp.getCurrentMinute() - cal.get(Calendar.MINUTE));
    	    	 int hour= (int) ( tp.getCurrentHour()- cal.get(Calendar.HOUR_OF_DAY));
    	    	 cal.add(Calendar.MINUTE, minute);
    	    	 cal.add(Calendar.HOUR, hour);
    	         
    	         setAlarm(cal);
    	    	
    	    	
    	    	
    	    	
    			
    		}else if(x==bt2){
    			
    				cancelAlarm();
    				
    				
    			
    			
    		}
    			
    		
    			
    				
    		        
        			
    			}    
    	}) ;
    	}


	
    
    private void setAlarm(Calendar targetCal) {

        
        Toast.makeText(Reminders.this, "Alarm is set for " + targetCal.getTime() + " daily",
                Toast.LENGTH_LONG).show();
        
        
       //sets the alarm to repeat every 24 hours, firing the pendingIntent intent.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), 1000*60*60*24 ,pendingIntent);
        
    }

  
    private void cancelAlarm(){
    	 //cancels the larm which will activate the pendingIntent variable
    	alarmManager.cancel(pendingIntent);
    }
  
	

}
