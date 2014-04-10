package uuj.diet.diary;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import uuj.diet.diary.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabSpec;

public class Diet_Diary extends Activity implements OnClickListener {
	
//TODO : onchanged event for spinMeasure to output integer variable to complete calculation

	
	Spinner spinner, spinMeasure;
	TextView tv4, tv2, tv3,tv1,tv5;
	EditText et1,et2,et3,et4,et5,et6;
	Button bt1,bt2,bt3;
	int bSpin = 0, id=0;
	private DBAdapter mHelper;
	private SQLiteDatabase dataBase;
	String strTime, strDate, strFood, strPortion, strNote, strID;
	ListView foodList, dbList;
	TabHost tabHost;
	private boolean isUpdate;
	TabSpec specs;
	private ArrayList<String> food = new ArrayList<String>();
	private ArrayList<String> portion = new ArrayList<String>();
	private ArrayList<String> notes = new ArrayList<String>();
	private ArrayList<Integer> ids = new ArrayList<Integer>();
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_input);
        mHelper=new DBAdapter(this);
        
        // get action bar   
        ActionBar actionBar = getActionBar();
 
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        
     	setupWidgits();
        setupTabs();
        clearUserText();
        resetPromptText();
        activateControls(true);
      
        isUpdate=getIntent().getExtras().getBoolean("update");
        if(isUpdate)
        {
        	pullUpdateData();
        }
        applyListeners();
         
         
       
 	}
        
    
    
    public void activateControls(boolean tf){
    	spinner.setEnabled(tf);
        bt2.setEnabled(tf);
    	
    }

    public void updateMeal(){
    	tv1.setText("Edit Time");
    	tv2.setText("Edit Food/Drink");
    	tv3.setText("Edit Portion");
    	tv4.setText("Edit Note");
    	
    }
    
    public void updateExercise(){
    	tv1.setText("Edit Time");
    	tv2.setText("Edit Exercise");
    	tv3.setText("Edit Duration");
    	tv4.setText("Edit Intensity");
    	
    }
    
    public void resetPromptText(){
    	tv1.setText("Time");
    	tv2.setText("Food/Drinks");
    	tv3.setText("Portion");
    	tv4.setText("Note");
    }
    public void clearUserText(){
    	et1.setText("");
    	et2.setText("");
    	et3.setText("");
    	et4.setText("");
    	
    }
    public void pullUpdateData(){
    	
    	int db = getIntent().getExtras().getInt("db");
    	spinner.setSelection(db);
    	strDate = getIntent().getExtras().getString("Date");
    	strID=getIntent().getExtras().getString("ID");
    	strTime=getIntent().getExtras().getString("Time");
    	strFood=getIntent().getExtras().getString("Food");
    	strPortion=getIntent().getExtras().getString("Portion");
    	strNote=getIntent().getExtras().getString("Notes");
    	int tabId = getIntent().getExtras().getInt("tab");
    	et1.setText(strTime);
    	et2.setText(strFood);
    	et3.setText(strPortion);
    	et4.setText(strNote);
    	tabHost.setCurrentTab(tabId);
    	activateControls(false);
    	Toast.makeText(getApplicationContext(), "While editing certain features are disabled", Toast.LENGTH_LONG).show();
    	bSpin = spinner.getSelectedItemPosition();
    	if(bSpin==0){
    		updateMeal();
    	}else{
    		updateExercise();
    	}
    }
    public void applyListeners(){
    	 addListenerOnSpinnerItemSelection();
         bt1.setOnClickListener(onClickListener);
         bt2.setOnClickListener(onClickListener);
         bt3.setOnClickListener(onClickListener);
         et5.addTextChangedListener(new TextWatcher() {
        	 
        	   public void afterTextChanged(Editable s) {
        		   String str = et5.getText().toString();
        		   displaySearchData(str);
        		   
        	   }

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
				
			}
        	 
        	  

			
        	  });
         addListListener();
         addDbListListener();
         tabListener();
    }
    public void setupTabs(){
    	tabHost = (TabHost)findViewById(android.R.id.tabhost);
        tabHost.setup();
        specs = tabHost.newTabSpec("tag1");
        specs.setContent(R.id.tab1);
        specs.setIndicator("Search foods");
        tabHost.addTab(specs);
        specs = tabHost.newTabSpec("tag2");
        specs.setContent(R.id.tab2);
        specs.setIndicator("Recent manual meals");
        tabHost.addTab(specs);
        specs = tabHost.newTabSpec("tag3");
        specs.setContent(R.id.tab3);
        specs.setIndicator("Manual entry");
        tabHost.addTab(specs);
        specs = tabHost.newTabSpec("tag4");
        specs.setContent(R.id.tab4);
        specs.setIndicator("Weighed entry");
       
        tabHost.addTab(specs);
    }
    public void setupWidgits(){
    	spinner = (Spinner) findViewById(R.id.spinInput);
        tv1 = (TextView) findViewById(R.id.tvName);
        tv2 = (TextView) findViewById(R.id.tvHeight);
        tv3 = (TextView) findViewById(R.id.tvDOB);
        tv4 = (TextView) findViewById(R.id.tvNotes);
        
        tv5= (TextView) findViewById(R.id.tvDataEntryItem);
        et6 = (EditText) findViewById(R.id.etEntryWeight);
        bt3 =(Button) findViewById(R.id.btnEnterNutritionData);
        spinMeasure = (Spinner) findViewById(R.id.spinMeasurements);
       
        et1 = (EditText) findViewById(R.id.etInput1);
        et2 = (EditText) findViewById(R.id.etInput2);
        et3 = (EditText) findViewById(R.id.etInput3);
        et4 = (EditText) findViewById(R.id.etInput4);
        bt1 = (Button) findViewById(R.id.btnSubmitView);
        bt2 = (Button) findViewById(R.id.btnSubmitReturn);
        foodList = (ListView) findViewById(R.id.lvDisplayFood);
        
        et5 = (EditText) findViewById(R.id.etSearchDb);
        dbList =(ListView) findViewById(R.id.lvDisplaySearch);
    }
   public void addListListener(){
	   foodList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				tabHost.setCurrentTab(2);
				et2.setText(food.get(arg2));
				et3.setText(portion.get(arg2));
				et4.setText(notes.get(arg2));
			}
		});
   }
   
   public void addDbListListener(){
	   dbList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				
				tabHost.setCurrentTab(3);
				tv5.setText(food.get(arg2));
				id = ids.get(arg2);
				et5.setText("");
				
				
			}
		});
   }
  public void addListenerOnSpinnerItemSelection() {
    	
    	spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int position = spinner.getSelectedItemPosition();
				switch(position){
				case 0:
					tv2.setText("Food/Drink");
					tv3.setText("Portion Size");
					tv4.setText("Notes");
					bSpin = 0;
					break;
				case 1:
					tv2.setText("Exercise");
					tv3.setText("Duration");
					tv4.setText("Intensity");
					bSpin = 1;
					break;
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
      }
  
    // saveButton click event 
	public void onClick(View v) {
	

	    
		
	}
	private OnClickListener onClickListener = new OnClickListener() {
	     @Override
	     public void onClick(View v) {
	    	 String date;
	    	 SimpleDateFormat df;
	    	 switch(v.getId()){
	         
	        
	             case R.id.btnSubmitView:
	            	 if(isUpdate==false){
	            		df = new SimpleDateFormat("dd-MM-yyyy");
	         	    date = df.format(Calendar.getInstance().getTime());
	         	    strDate = date;
	            	 }
	         	   
	         		strTime=et1.getText().toString().trim();
	         		strFood=et2.getText().toString().trim();
	         		strPortion=et3.getText().toString().trim();
	         		strNote=et4.getText().toString().trim();
	         		if(strTime.length()>0 && strFood.length()>0 && strPortion.length()>0)
	         		{
	         			saveData(1);
	         			clearUserText();
	         		}
	         		else
	         		{
	         			AlertDialog.Builder alertBuilder=new AlertDialog.Builder(Diet_Diary.this);
	         			alertBuilder.setTitle("Invalid Data");
	         			alertBuilder.setMessage("Please, Enter valid data");
	         			alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	         				
	         				public void onClick(DialogInterface dialog, int which) {
	         				dialog.cancel();
	         					
	         				}
	         			});
	         			alertBuilder.create().show();
	         		}
	             break;
	             case R.id.btnSubmitReturn:
	            	 
	            	 if(isUpdate==false){
	            		 df = new SimpleDateFormat("dd-MM-yyyy");
	         	    date = df.format(Calendar.getInstance().getTime());
	         	    strDate = date;
	            	 }
	         	    
	         		strTime=et1.getText().toString().trim();
	         		strFood=et2.getText().toString().trim();
	         		strPortion=et3.getText().toString().trim();
	         		strNote=et4.getText().toString().trim();
	         		if(strTime.length()>0 && strFood.length()>0 && strPortion.length()>0)
	         		{
	         			saveData(2);
	         			clearUserText();
	         		}
	         		else
	         		{
	         			AlertDialog.Builder alertBuilder=new AlertDialog.Builder(Diet_Diary.this);
	         			alertBuilder.setTitle("Invalid Data");
	         			alertBuilder.setMessage("Please, Enter valid data");
	         			alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	         				
	         				public void onClick(DialogInterface dialog, int which) {
	         				dialog.cancel();
	         					
	         				}
	         			});
	         			alertBuilder.create().show();
	         		}
	             break;
	             case R.id.btnEnterNutritionData:
	            	 
	            	 
	            		
	            		
	            		dataBase = mHelper.getWritableDatabase();
	            		Cursor mCursor;
	            		String foodEntry = null;
	            		double[] nutritionalInformation = new double[8];
	            		
	            			
	            			
	            			mCursor = dataBase.rawQuery("SELECT * FROM "
	            					+ DBAdapter.DATABASE_TABLE_FOOD_NUTRITION_STORE +" WHERE " + DBAdapter.KEY_ROWID + " LIKE " + id, null);
	            			
	            			if (mCursor.moveToFirst()) {
	            				do {
	            					//adds food data to the arrays to enable display until there are no more entries
	            					
	            					foodEntry =(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_FOOD)));
	            					nutritionalInformation[0] =  (mCursor.getDouble(mCursor.getColumnIndex(DBAdapter.KEY_CALORIES)));
	            					
	            					nutritionalInformation[1] = (mCursor.getDouble(mCursor.getColumnIndex(DBAdapter.KEY_WATER)));
	            					nutritionalInformation[2] = (mCursor.getDouble(mCursor.getColumnIndex(DBAdapter.KEY_CARBS)));
	            					nutritionalInformation[3] = (mCursor.getDouble(mCursor.getColumnIndex(DBAdapter.KEY_FAT)));
	            					nutritionalInformation[4] = (mCursor.getDouble(mCursor.getColumnIndex(DBAdapter.KEY_SATURATED_FAT)));
	            					nutritionalInformation[5] = (mCursor.getDouble(mCursor.getColumnIndex(DBAdapter.KEY_PROTEIN)));
	            					nutritionalInformation[6] = (mCursor.getDouble(mCursor.getColumnIndex(DBAdapter.KEY_IRON)));
	            					nutritionalInformation[7] = (mCursor.getDouble(mCursor.getColumnIndex(DBAdapter.KEY_CALCIUM)));
	            				} while (mCursor.moveToNext());
	            			}
	            			ContentValues values=new ContentValues();
	            			double multiplicationValue = 0;
	            			String measurement = "grams";
	            			//implement additional requirement that id is not null
	            			if((et6.getText().toString().trim().length() >0)&&(id!=0)){
	            				switch(spinMeasure.getSelectedItemPosition()){
	            				
	            				
	            				case 0:
	            					//grams
	            					multiplicationValue =Double.parseDouble(et6.getText().toString().trim())/100 ;
	            					 measurement = "gram(s)";
	            				break;
	            				case 1:
	            					//mls
	            					multiplicationValue =Double.parseDouble(et6.getText().toString().trim())/100 ;
	            					 measurement = "ml(s)";
	            				break;
	            				case 2:
	            					//pints
	            					multiplicationValue =(Double.parseDouble(et6.getText().toString().trim())*568)/100 ;
	            					 measurement = "pint(s)";
	            					
	            				break;
	            				case 3:
	            					//Litres
	            					multiplicationValue =(Double.parseDouble(et6.getText().toString().trim())*1000)/100 ;
	            					 measurement = "litre(s)";
	            				break;
	            				case 4:
	            					//teaspoon
	            					multiplicationValue =(Double.parseDouble(et6.getText().toString().trim())*5)/100 ;
	            					 measurement = "teaspoon(s)";
	            				break;
	            				case 5:
	            					//tablespoon
	            					multiplicationValue =(Double.parseDouble(et6.getText().toString().trim())*15)/100 ;
	            					 measurement = "tablespoon(s)";
	            				break;
	            				case 6:
	            				
	            				break;
	            				}
	            				
	            				for(int i=0;i<nutritionalInformation.length;i++){
	            					nutritionalInformation[i] = nutritionalInformation[i]*multiplicationValue;
	            					
	            				}
	            				
	            				Calendar cal = Calendar.getInstance(); 
	            				 df = new SimpleDateFormat("dd-MM-yyyy");
	         	         	    date = df.format(Calendar.getInstance().getTime());
	         	         	    strDate = date;
	            				  
	            				  int minute = cal.get(Calendar.MINUTE);
	            				        //12 hour format
	              				  int hourofday = cal.get(Calendar.HOUR_OF_DAY);
	              				  String now = hourofday + ":" +minute ;
	            				
	              				values.put(DBAdapter.KEY_DATE,strDate );
	            				values.put(DBAdapter.KEY_TIME,now );
	            				values.put(DBAdapter.KEY_FOOD_STORE,foodEntry);
	            				values.put(DBAdapter.KEY_CALORIES,nutritionalInformation[0]);
	            				values.put(DBAdapter.KEY_WATER,nutritionalInformation[1]);
	            				values.put(DBAdapter.KEY_CARBS,nutritionalInformation[2]);
	            				values.put(DBAdapter.KEY_FAT,nutritionalInformation[3]);
	            				values.put(DBAdapter.KEY_SATURATED_FAT,nutritionalInformation[4]);
	            				values.put(DBAdapter.KEY_PROTEIN,nutritionalInformation[5]);
	            				values.put(DBAdapter.KEY_IRON,nutritionalInformation[6]);
	            				values.put(DBAdapter.KEY_CALCIUM,nutritionalInformation[7]);
	            				
								Toast.makeText(getApplicationContext(), "Logged "+Integer.parseInt(et6.getText().toString().trim())+" "+measurement+ " of " + foodEntry + " totalling " + nutritionalInformation[0] + " calories!", Toast.LENGTH_LONG).show();
	            				dataBase.insert(DBAdapter.DATABASE_TABLE_FOOD_NUTRI_DATA, null, values);
	            				id=0;
		            			tv5.setText(" ");
		            		et6.setText(" ");
		            		tabHost.setCurrentTab(0);
	            			}else if(id==0){
	            				Toast.makeText(getApplicationContext(), "Please select an item from the search foods tab", Toast.LENGTH_LONG).show();
	            			}else{
	            				Toast.makeText(getApplicationContext(), "Please input a value entry for the weight of your meal", Toast.LENGTH_LONG).show();
	            			}
	            			//clear id
	            			
	            	 break;
	             	         }

	   }
	};

	
	private void saveData(int b){
		dataBase=mHelper.getWritableDatabase();
		
		ContentValues values=new ContentValues();
		if (bSpin == 0){
			
			values.put(DBAdapter.KEY_DATE,strDate);
			values.put(DBAdapter.KEY_TIME,strTime);
			values.put(DBAdapter.KEY_FOOD,strFood );
			values.put(DBAdapter.KEY_PORTION,strPortion);
			values.put(DBAdapter.KEY_NOTES,strNote);
		
		
		}else{
			
			values.put(DBAdapter.KEY_EXERCISE_DATE,strDate);
			values.put(DBAdapter.KEY_EXERCISE_TIME,strTime);
			values.put(DBAdapter.KEY_EXERCISE,strFood );
			values.put(DBAdapter.KEY_DURATION,strPortion);
			values.put(DBAdapter.KEY_INTENSITY,strNote);
			
		}
		
		
		System.out.println("");
		if(isUpdate)
		{    
			
			
			
			//update database with new data 
			if(bSpin==0){
				dataBase.update(DBAdapter.DATABASE_TABLE_FOOD, values, DBAdapter.KEY_ROWID+"="+strID, null);
				Toast.makeText(getApplicationContext(), strTime +" Entry updated!", Toast.LENGTH_LONG).show();
				
			}else{
				dataBase.update(DBAdapter.DATABASE_TABLE_EXERCISE, values, DBAdapter.KEY_ROWID+"="+strID, null);
				Toast.makeText(getApplicationContext(),strTime +" Entry updated!", Toast.LENGTH_LONG).show();
				
				
			}
			finish();
			
				
		}
		else
		{Intent i = new Intent(getApplicationContext(),
					Diary_Output.class);
			//insert data into database
			if(bSpin==0){
				dataBase.insert(DBAdapter.DATABASE_TABLE_FOOD, null, values);
				Toast.makeText(getApplicationContext(), strTime+ " "+ strFood +" logged", Toast.LENGTH_LONG).show();
			}else{
				dataBase.insert(DBAdapter.DATABASE_TABLE_EXERCISE, null, values);
				Toast.makeText(getApplicationContext(), strTime+ " "+ strFood +" logged", Toast.LENGTH_LONG).show();
			}
			i.putExtra("db", spinner.getSelectedItemPosition());
			if(b==1){
			finish();
			startActivity(i);
			}else if(b==2){
			clearUserText();
			}
			
			
		}
		//close database
		dataBase.close();
		
		
		
	}
	private void tabListener(){
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			public void onTabChanged(String tabId) {

			int i = tabHost.getCurrentTab();
			 

			    if (i == 0) {
			            

			    }
			    else if (i ==1) {
			    	Toast.makeText(getApplicationContext(), "Tap an item for fast input", Toast.LENGTH_LONG).show(); 
			    	displayData();
			    }

			  }
			});
	}
	
	private void displayData() {
		dataBase = mHelper.getWritableDatabase();
		Cursor mCursor;
		
			//if the spinner value is 0
			
			mCursor = dataBase.rawQuery("SELECT * FROM "
					+ DBAdapter.DATABASE_TABLE_FOOD, null);
			//create a database query on the food table
			
			food.clear();
			
			
			//empty the arraylists being used to hold the data
			
			if (mCursor.moveToFirst()) {
				do {
					//adds food data to the arrays to enable display until there are no more entries
					
					food.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_FOOD)));
					String spacing;
					spacing = "  " + mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_PORTION)) + "  ";
					portion.add(spacing);
					notes.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_NOTES)));
					
				} while (mCursor.moveToNext());
			}//while the cursor can move to a new item loop the data to its respective arraylist
			
		
		//sets the display adapter to utilise the arrays to provide a fixed, reliable display.
		RecentItemsAdapter disadpt = new RecentItemsAdapter(Diet_Diary.this,food,portion);
		//using the display adapter created allocate values to be output through it
		foodList.setAdapter(disadpt);
		//set the listview to utilise the display adapter created above
		mCursor.close();
		//shut down the cursor to avoid any memory leaks
	}
	
	public void displaySearchData(String search){
		
		dataBase = mHelper.getWritableDatabase();
		Cursor fCursor;
		
			//if the spinner value is 0
			
			fCursor = dataBase.rawQuery("SELECT * FROM "
					+ DBAdapter.DATABASE_TABLE_FOOD_NUTRITION_STORE +" WHERE " + DBAdapter.KEY_FOOD_STORE + " LIKE '%" + search+"%'", null);
			 
			//create a database query on the food table
			
			
			food.clear();
			portion.clear();
			ids.clear();
			//empty the arraylists being used to hold the data
			
			if (fCursor.moveToFirst()) {
				do {
					//adds food data to the arrays to enable display until there are no more entries
					
					food.add(fCursor.getString(fCursor.getColumnIndex(DBAdapter.KEY_FOOD_STORE)));
					String spacing;
					spacing = "  " +   "  ";
					portion.add(spacing);
					ids.add(fCursor.getInt(fCursor.getColumnIndex(DBAdapter.KEY_ROWID)));
					
					
					
				} while (fCursor.moveToNext());
			}//while the cursor can move to a new item loop the data to its respective arraylist
			
		
		//sets the display adapter to utilise the arrays to provide a fixed, reliable display.
		RecentItemsAdapter disadpt = new RecentItemsAdapter(Diet_Diary.this,food,portion);
		//using the display adapter created allocate values to be output through it
		dbList.setAdapter(disadpt);
		//set the listview to utilise the display adapter created above
		fCursor.close();
		//shut down the cursor to avoid any memory leaks
	}

}
