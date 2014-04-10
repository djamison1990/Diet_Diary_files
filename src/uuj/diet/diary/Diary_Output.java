package uuj.diet.diary;
import java.util.ArrayList;
import uuj.diet.diary.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class Diary_Output extends Activity {

	private DBAdapter mHelper;
	private SQLiteDatabase dataBase;
//these array lists are used by the output adapter to display the data
	private ArrayList<String> userId = new ArrayList<String>();
	private ArrayList<String> date = new ArrayList<String>();
	private ArrayList<String> time = new ArrayList<String>();
	private ArrayList<String> food = new ArrayList<String>();
	private ArrayList<String> portion = new ArrayList<String>();
	private ArrayList<String> notes = new ArrayList<String>();
	private ListView userList;
	private AlertDialog.Builder build;
	Spinner spinner;
	Button btClose;
	Integer bSpin=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diary_output);
		
		 //set up back navigation via top bar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //sets up the buttons, spinner and listeners
		setupButtons();
		setupSpinner();		
		applyListeners();
		//toasts user instructions for editing/deleting
		Toast.makeText(getApplicationContext(), "Tap to edit, hold to delete.", Toast.LENGTH_LONG).show();
		
	
	}
	public void setupSpinner(){
		//gets the input from the extra values relating to which database had data entered into
		int db = getIntent().getExtras().getInt("db");
		//sets the spinner value which in turn will set the displayed data
    	spinner.setSelection(db);
	}
	public void setupButtons(){
		btClose = (Button) findViewById(R.id.btnClose);
		userList = (ListView) findViewById(R.id.lvOutput);
		spinner = (Spinner) findViewById(R.id.spTables);
		mHelper = new DBAdapter(this);
	}
	public void applyListeners(){
		addListenerOnSpinnerItemSelection();
		addButtonClickListener();
		//long click listener is the 'delete' listener
		addListLongListener();
		//short click listener is the 'edit' listener
		addListShortListener();
		}
	
	public void addListShortListener(){
		userList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2!=0) {
					if(bSpin==2){
						//if the table being viewed is the nuatirion data table it cannot be edited
						Toast.makeText(getApplicationContext(), "Items from this database cannot be edited, only deleted.", Toast.LENGTH_LONG).show();
					}else{
						//starts the intent to the edit tab in the input activity, inputs the values to be edited as extras
						Intent i = new Intent(getApplicationContext(),
						Diet_Diary.class);
						//data being edited
				i.putExtra("Date", date.get(arg2));
				i.putExtra("Time", time.get(arg2));
				i.putExtra("Food", food.get(arg2));
				i.putExtra("Portion", portion.get(arg2));
				i.putExtra("Notes", notes.get(arg2));
				//as the id isnt actually displayed the item is pulled from the database directly meaning it doesnt have the extra value
				//which is gained from the title values which is why the value is arg2-1
				i.putExtra("ID", userId.get(arg2-1));
				//sets the 'update' to true, this is used in the data entry activity to determine how it will handle the data
				i.putExtra("update", true);
				//dictates which database will be being edited
				i.putExtra("db", spinner.getSelectedItemPosition());
				//starts the activity at tab 2, the manual input tab
				i.putExtra("tab", 2);
				startActivity(i);
					}
					
				}
				

			}
		});
		
		//long click to delete data
		
	}
	public void addListLongListener(){
		userList.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				//arg2==0 is the titles of the page, obviously not something we want to be edited
				if(arg2>0){
					//builds a confirm dialog to ensure deletion doesnt happen by accident
					build = new AlertDialog.Builder(Diary_Output.this);
					build.setTitle("Delete " + time.get(arg2) + " "
							+ food.get(arg2));
					build.setMessage("Do you want to delete ?");
					build.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
						//onclick for the dialog 'yes'
								public void onClick(DialogInterface dialog,
										int which) {

									Toast.makeText(
											getApplicationContext(),
											time.get(arg2) + " "
													+ food.get(arg2)
													+ " is deleted.", Toast.LENGTH_LONG).show();
									//decides which data will be deleted based upon the current spinner value
									//again arg2-1 due to the titles
									if (bSpin==0){
										dataBase.delete(
											DBAdapter.DATABASE_TABLE_FOOD,
											DBAdapter.KEY_ROWID + "="
													+ userId.get(arg2-1), null);
									}else if (bSpin==1){
										dataBase.delete(
										DBAdapter.DATABASE_TABLE_EXERCISE,
										DBAdapter.KEY_ROWID + "="
												+ userId.get(arg2-1), null);
									}else if (bSpin==2){
										dataBase.delete(
												DBAdapter.DATABASE_TABLE_FOOD_NUTRI_DATA,
												DBAdapter.KEY_ROWID + "="
														+ userId.get(arg2-1), null);
									}
									//rebuilds the data display
									displayData();
									//clears the dialog
									dialog.cancel();
								}
							});

					build.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
							//onclick for dialog no
								public void onClick(DialogInterface dialog,
										int which) {
									//cancel dialog when clicked
									dialog.cancel();
								}
							});
					AlertDialog alert = build.create();
					alert.show();
				}
				

				return true;
			}
		});
	}
	@Override
	protected void onResume() {
		//when resumed display the data
		displayData();
		super.onResume();
	}

	
	private void displayData() {
		dataBase = mHelper.getWritableDatabase();
		Cursor mCursor = null;
		if (bSpin==0){
			
			//if the spinner value is 0
			mCursor = dataBase.rawQuery("SELECT * FROM "
					+ DBAdapter.DATABASE_TABLE_FOOD, null);
			//create a database query on the food table
			date.clear();
			userId.clear();
			time.clear();
			food.clear();
			portion.clear();
			notes.clear();
			//empty the arraylists being used to hold the data
			
			date.add("Date");
			time.add("Time");
			food.add("Food/Drink");
			portion.add("Portion");
			notes.add("Notes");
			if (mCursor.moveToFirst()) {
				do {
					//adds food data to the arrays to enable display until there are no more entries
					userId.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_ROWID)));
					date.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_DATE)));
					time.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_TIME)));
					food.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_FOOD)));
					portion.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_PORTION)));
					notes.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_NOTES)));
				} while (mCursor.moveToNext());
			}//while the cursor can move to a new item loop the data to its respective arraylist
			
		}else if (bSpin==1){
			
			mCursor = dataBase.rawQuery("SELECT * FROM "
					+ DBAdapter.DATABASE_TABLE_EXERCISE, null);
			//query the exercise database
			date.clear();
			userId.clear();
			time.clear();
			food.clear();
			portion.clear();
			notes.clear();
			
			date.add("Date");
			time.add("Time");
			food.add("Exercise");
			portion.add("Duration");
			notes.add("Intensity");
			//clear the arrays
			if (mCursor.moveToFirst()) {
				do {
					//adds exercise data to the arrays to enable display until there are no more entries
					userId.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_ROWID)));
					date.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_EXERCISE_DATE)));
					time.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_EXERCISE_TIME)));
					food.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_EXERCISE)));
					portion.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_DURATION)));
					notes.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_INTENSITY)));
				} while (mCursor.moveToNext());
			}//append to the arrays
			
		}else{
			
			
			mCursor = dataBase.rawQuery("SELECT * FROM "
					+ DBAdapter.DATABASE_TABLE_FOOD_NUTRI_DATA, null);
			//query the exercise database
			date.clear();
			userId.clear();
			time.clear();
			food.clear();
			portion.clear();
			notes.clear();
			
			date.add("Date");
			time.add("Time");
			food.add("Food/Drink");
			portion.add("Calories (Kcal)");
			notes.add("Fat(g)");
			//clear the arrays
			if (mCursor.moveToFirst()) {
				do {
					//adds nutrition data to the arrays to enable display until there are no more entries
					userId.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_ROWID)));
					date.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_DATE)));
					time.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_TIME)));
					food.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_FOOD)));
					portion.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_CALORIES)));
					notes.add(mCursor.getString(mCursor.getColumnIndex(DBAdapter.KEY_FAT)));
				} while (mCursor.moveToNext());
			}
		}
		//sets the display adapter to utilise the arrays to provide a fixed, reliable display.
		OutputAdapter disadpt = new OutputAdapter(Diary_Output.this,date, time, food,portion,notes);
		//using the display adapter created allocate values to be output through it
		userList.setAdapter(disadpt); 	
		//set the listview to utilise the display adapter created above
		mCursor.close();
		//shut down the cursor to avoid any memory leaks
	}

	  public void addListenerOnSpinnerItemSelection() {
	    	//creates and sets the listener on the spinner to check if it has been changed.
	    	spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					int position = spinner.getSelectedItemPosition();
					switch(position){
					case 0:
						//outputs food data into the arrays for display
						bSpin = 0;
						break;
					case 1:
						
						bSpin = 1;
						break;
					
					case 2:
						bSpin = 2;
						break;
					}
					
					displayData();
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
	      }

	  public void addButtonClickListener(){
	    	btClose.setOnClickListener(new View.OnClickListener() {
	          
	    		public void onClick(View v) {
	    		
	    			
	    			
	    			finish();
	    			
	    			}    
	    	
	    	});  
	    }
	  
}