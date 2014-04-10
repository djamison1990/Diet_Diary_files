package uuj.diet.diary;


import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import uuj.diet.diary.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter extends SQLiteOpenHelper {
	
	Context dbContext;
	public static final String KEY_ROWID = "_id";
	public static final int COL_ROWID = 0;
	
	//table 1 fields
	public static final String KEY_DATE = "date";
	public static final String KEY_TIME = "time";
	public static final String KEY_FOOD = "food";
	public static final String KEY_PORTION = "portion";
	public static final String KEY_NOTES = "notes";
	//table 2 fields
	public static final String KEY_EXERCISE_DATE = "date";
	public static final String KEY_EXERCISE_TIME = "time";
	public static final String KEY_EXERCISE = "exercise";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_INTENSITY = "intensity";
	
	
	//table 3 fields
	public static final String KEY_FOOD_STORE = "food";
	public static final String KEY_CALORIES = "calories";
	public static final String KEY_PROTEIN = "protein";
	public static final String KEY_CARBS = "carbs";
	public static final String KEY_WATER = "water";
	public static final String KEY_FAT = "fat";
	public static final String KEY_SATURATED_FAT = "satFat";
	public static final String KEY_IRON = "iron";
	public static final String KEY_CALCIUM = "calcium";
	
	
	//table 1 columns
	public static final int COL_DATE = 1;
	public static final int COL_TIME = 2;
	public static final int COL_FOOD = 3;
	public static final int COL_PORTION = 4;
	public static final int COL_NOTES = 5;
	//table 2 columns
	public static final int COL_EXERCISE_DATE = 1;
	public static final int COL_EXERCISE_TIME = 2;
	public static final int COL_EXERCISE = 3;
	public static final int COL_DURATION = 4;
	public static final int COL_INTENSITY = 5;
	//table 3 columns
	public static final int COL_FOOD_STORE = 1;
	public static final int COL_WATER = 2;
	public static final int COL_PROTEIN = 3;
	public static final int COL_FAT = 4;
	public static final int COL_CARBS = 5;
	public static final int COL_CALORIES = 6;
	public static final int COL_SATURATED_FAT = 7;
	public static final int COL_CALCIUM = 8;
	public static final int COL_IRON = 9;
	
	
	
	// DB info: it's name and the tables we are using.
	public static final String DATABASE_NAME = "MyDb";
	public static final String DATABASE_TABLE_FOOD = "foodTable";
	public static final String DATABASE_TABLE_EXERCISE = "exerciseTable";
	public static final String DATABASE_TABLE_FOOD_NUTRI_DATA = "foodNutritionTable";
	public static final String DATABASE_TABLE_FOOD_NUTRITION_STORE = "foodNutritionDatabase";
	// Track DB version if a new version of your app changes the format.
	public static final int DATABASE_VERSION = 0;	
	
	//set up the sqlite creation statments for ease of reading.
	private static final String DATABASE_CREATE_FOOD_TABLE = 
			
			"create table " + DATABASE_TABLE_FOOD 
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_DATE + " string not null, "
			+ KEY_TIME + " string not null, "
			+ KEY_FOOD + " string not null, "
			+ KEY_PORTION + " string not null, "
			+ KEY_NOTES + " string "
			
			+ ");";
	
	private static final String DATABASE_CREATE_EXERCISE_TABLE = 
			
			"create table " + DATABASE_TABLE_EXERCISE
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_EXERCISE_DATE + " string not null, "
			+ KEY_EXERCISE_TIME + " string not null, "
			+ KEY_EXERCISE + " string not null, "
			+ KEY_DURATION + " string not null, "
			+ KEY_INTENSITY +" string "
			+ ");";
	
private static final String DATABASE_CREATE_NUTRITION_STORE_TABLE = 
			
			"create table " + DATABASE_TABLE_FOOD_NUTRITION_STORE
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_FOOD_STORE + " string, "
			+ KEY_CALORIES + " integer not null, "
			+ KEY_WATER + " integer not null, "
			+ KEY_CARBS + " integer not null, "
			+ KEY_FAT + " integer not null, "	
			+ KEY_SATURATED_FAT +" integer not null,"
			+ KEY_PROTEIN + " integer not null, "
			+ KEY_IRON + " integer not null, "
			+ KEY_CALCIUM + " integer not null "
			+ ");";

private static final String DATABASE_CREATE_NUTRITION_DATA_TABLE = 

			"create table " + DATABASE_TABLE_FOOD_NUTRI_DATA
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_DATE + " string not null, "
			+ KEY_TIME + " string not null, "
			+ KEY_FOOD_STORE + " string not null, "
			+ KEY_CALORIES + " integer not null, "
			+ KEY_WATER + " integer not null, "
			+ KEY_CARBS + " integer not null, "
			+ KEY_FAT + " integer not null, "	
			+ KEY_SATURATED_FAT +" integer not null,"
			+ KEY_PROTEIN + " integer not null, "
			+ KEY_IRON + " integer not null, "
			+ KEY_CALCIUM + " integer not null "
			+ ");";
//tag for testing
private static final String TAG = null;
	

	
;
	
	

	

	public DBAdapter(Context context) {
		super(context, DATABASE_NAME, null, 1);
		dbContext = context;
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		//creates the 4 tables to be used by the application, 3 for storing user data and one for containing the xml data passed from the xml file
		db.execSQL(DATABASE_CREATE_FOOD_TABLE);
	   	db.execSQL(DATABASE_CREATE_EXERCISE_TABLE);
	   	db.execSQL(DATABASE_CREATE_NUTRITION_STORE_TABLE);
	   	db.execSQL(DATABASE_CREATE_NUTRITION_DATA_TABLE);
	   	
	  //parses the xml data into the nutrition store table
	   	parseDatatoNutrition(db);
	   	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//when the database is upgraded the ables get remade

		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_FOOD_NUTRITION_STORE);
	
		onCreate(db);

	}
	public void parseDatatoNutrition(SQLiteDatabase db){
	  
		
		String _Food_Name;
        String _Kcal;
        String _Water ;
        String _Carbohydrate ;
        String _Fat ;
        String _Satfat ;
        String _Protein ;
        String _Iron ;
        String _Calcium ;
                         
        //Get xml resource file
        Resources res = dbContext.getResources();
        
        XmlResourceParser _xml = res.getXml(R.xml.nutrition_data);
        try
        {
            //Check for end of document
            int event = _xml.getEventType();
            
            while (event != XmlPullParser.END_DOCUMENT) {
            	
               
                ContentValues _Values = new ContentValues();
                //while the document isnt fully read pull the data from it based upon the tags
				if(event==XmlPullParser.START_TAG){
					 if(_xml.getName().equals("record")){
					
               		   _Food_Name = _xml.getAttributeValue(null, "Food_Name");
               		  
 						_Values.put(KEY_FOOD_STORE, _Food_Name);
 						
 						
 						
 						 _Kcal = _xml.getAttributeValue(null, "kcal");
 	                       
                         _Values.put(KEY_CALORIES, _Kcal);
                      
                         
                         _Water = _xml.getAttributeValue(null,"Water");
                         
                         _Values.put(KEY_WATER, _Water);
                        
                         
                         _Carbohydrate = _xml.getAttributeValue(null,"Carbohydrate");
                         
                         _Values.put(KEY_CARBS, _Carbohydrate);
                         
                         
                         _Fat = _xml.getAttributeValue(null,"Fat");
                         
                         _Values.put(KEY_FAT, _Fat);
                     
                         
                         _Satfat = _xml.getAttributeValue(null,"Satfat");
                         
                         _Values.put(KEY_SATURATED_FAT, _Satfat);
                      
                         
                         _Protein = _xml.getAttributeValue(null,"Protein");
                        	
                         _Values.put(KEY_PROTEIN, _Protein);
                        
                         
                         _Iron = _xml.getAttributeValue(null,"Iron");
                         
                         _Values.put(KEY_IRON, _Iron);
                      
                         
                         _Calcium = _xml.getAttributeValue(null,"Calcium");
                        	
                      	  
                         _Values.put(KEY_CALCIUM, _Calcium);
                      
                         db.insert(DATABASE_TABLE_FOOD_NUTRITION_STORE, null, _Values);
                         
                       }
					 
				}else if(event==XmlPullParser.END_TAG){
					if(_xml.getName().equals("record")){
						 //expansion room, end of data being read in
					}
				}
                	  //current event is equal to the next tag read in by the reader
                		 event = _xml.next();  
                      }		   
                	  
                		   
                	   
                	  
                      
                  
                 

                   
                	 
                	   
                	 
                      
				
                  
          
        }
        //Catch errors
        catch (XmlPullParserException e)
        {       
            Log.e(TAG, e.getMessage(), e);
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage(), e);
             
        }           
        finally
        {           
            //Close the xml reader.
            _xml.close();
        }
	}
	
	
}

