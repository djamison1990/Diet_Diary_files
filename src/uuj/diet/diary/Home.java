package uuj.diet.diary;



//one change to test github


import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import uuj.diet.diary.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Home extends Activity {

	
	
	Button bt1,bt2,bt3,bt4,bt5,bt6;
	private DBAdapter mHelper;
	private SQLiteDatabase dataBase;
	FileWriter writer;
	String emailAddress, pathName, rawFoodFile, exerciseFile,userFile,nutritionInfoFile;
	File myFile, file1, file2,file3,file4;
	ArrayList<Uri> uriFiles;
	boolean fileExists = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        initialVariables();     
        addListeners();
        
    }

    
  
  



	public void addListeners(){
    	 addButtonClickListener(bt1);
         addButtonClickListener(bt2);
         addButtonClickListener(bt3);
         addButtonClickListener(bt4);
         addButtonClickListener(bt5);
         addButtonClickListener(bt6);
    }
    
    public void initialVariables(){
    	 emailAddress = "Jamison-d1@email.ulster.ac.uk";
         pathName= Environment.getExternalStorageDirectory().getAbsolutePath() + "/uujDietDiary";
         File dirCheck=  new File(pathName);
         if(!dirCheck.isDirectory()){
        	 dirCheck.mkdirs();
         }
         rawFoodFile="/Raw_Food_Data.txt";
 		exerciseFile="/Exercise_Data.txt";
 		userFile="/User_Data.txt";
 		nutritionInfoFile ="/Processed_Food_Data.txt";
 		
 		file1=new File(pathName, rawFoodFile);
 		file2=new File(pathName, exerciseFile);
 		file3=new File(pathName, userFile);
 		file4 = new File(pathName, nutritionInfoFile);
 		
         mHelper = new DBAdapter(this);
         bt1 = (Button) findViewById(R.id.btnInput);
         bt2 = (Button) findViewById(R.id.btnLog);
         bt3 = (Button) findViewById(R.id.btnUserData);
         bt4 = (Button) findViewById(R.id.btnEmail);
         bt5 = (Button) findViewById(R.id.btnBluetooth);
         bt6 = (Button) findViewById(R.id.btnSettings);
    }
			
	@Override
	protected void onResume() {
		fileCheck();
		super.onResume();
	}
      
	public void fileCheck(){
		//this test is necessary because the user data file is not written from here and thus we need to ensure it exists to avoid errors
		if(file3.exists()){
 			fileExists= true;
 			
 		}else{
 			fileExists = false;
 			
 		}
	}
   
    public void addButtonClickListener(final Button x){
    	x.setOnClickListener(new View.OnClickListener() {
          
    		public void onClick(View v) {
    		if(x==bt1){
    			Intent dataInput = new Intent(Home.this, Diet_Diary.class);
				//lets the activity know not to run in 'update' mode
				dataInput.putExtra("update", false);
				
		        startActivity(dataInput);
    			
    		}else if(x==bt2){
    			Intent dataDisplay = new Intent(Home.this, Diary_Output.class);
				//sets the initial database being displayed to the value at 0, currently the food database.
				dataDisplay.putExtra("db", "0");
				
		        startActivity(dataDisplay);
    		}else if(x==bt3){
    			Intent userData = new Intent(Home.this, UserData.class);
				//sets the initial database being displayed to the value at 0, currently the food database.
				
				
		        startActivity(userData);
    		}else if(x==bt4){
    			try {
					writeData(1);
					writeData(2);
					writeData(3);
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
				if(fileExists==true){
					createEmailIntent();
				}else{
					Toast.makeText(getBaseContext(),"Please enter user data before transmitting",
			                Toast.LENGTH_LONG).show();
				}
    		}else if(x==bt5){
				try {
					writeData(1);
					writeData(2);
					writeData(3);
					
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				
			
				
				
				if(fileExists==true){
					createBluetoothIntent();
				}else{
					Toast.makeText(getBaseContext(),"Please enter user data before transmitting",
			                Toast.LENGTH_LONG).show();
				}
    		}else if(x==bt6){
    			
    			Intent settings = new Intent(Home.this, Reminders.class);
    			
    			startActivity(settings);
    			
    		}
    			
    		
    			
    				
    		        
        			
    			}    
    	}) ;
    	}  
  
    public void createEmailIntent(){
    	Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		//title should be meaningful, client name or something
		createUriData();
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {emailAddress});
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Diet Diary submission - YOUR NAME HERE");
		emailIntent.putExtra(Intent.EXTRA_TEXT, "Please be sure to identify yourself and include any additional information required");
		//TODO add Andrea's email address.
		
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriFiles);
		emailIntent.setType("plain/text");
		startActivity(Intent.createChooser(emailIntent, "Please select an email application"));
    }
    public void createBluetoothIntent(){
    	Intent bluetoothIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		//title should be meaningful, client name or something
		bluetoothIntent.putExtra(Intent.EXTRA_SUBJECT, "Title");
		
		createUriData();
        bluetoothIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriFiles);
        bluetoothIntent.setType("image/*");
        bluetoothIntent.setPackage("com.android.bluetooth");
		startActivity(Intent.createChooser(bluetoothIntent, "Please select a bluetooth application"));
    }
    public void createUriData(){
    	
		
    	if(fileExists = true){
    	uriFiles = new ArrayList<Uri>();
        Uri u1 = Uri.fromFile(file1);
        uriFiles.add(u1);
        u1= Uri.fromFile(file2);
   	    uriFiles.add(u1);	   	    	
   	    u1= Uri.fromFile(file3);
	    uriFiles.add(u1);
	    u1= Uri.fromFile(file4);
	    uriFiles.add(u1);
    	 }
   	 
    }
    public void writeData(Integer tableNo) throws IOException{
    	dataBase = mHelper.getWritableDatabase();
		Cursor mCursor = null;
		String fileData = null;
		StringBuilder ret = null;
		String dateChanged = "empty";

		//set the size array widths to 0
		int[] intSizeArray = new int[5];
		String[] headers, dataArray;
		if(tableNo==1){
			for(int i=0;i<intSizeArray.length;i++){
				intSizeArray[i]=0;
			}
			//instantiate the header and data arrays
			headers = new String[]{"Date","Time","Food/Drink","Portion","Notes"};
			dataArray = new String[5];
					fileData = "Raw_Food_Data.txt";
			
			 ret = new StringBuilder("Saved Food Data:\n\r\n");
			   	      
	   
			mCursor = dataBase.rawQuery("SELECT * FROM "
					+ DBAdapter.DATABASE_TABLE_FOOD, null);
			
			fillMaxSizeArray(mCursor,dataArray,intSizeArray);
			

			mCursor = dataBase.rawQuery("SELECT * FROM "
					+ DBAdapter.DATABASE_TABLE_FOOD, null);
			 while(mCursor.moveToNext()){

	    		  //pull the data out of the database
	    		   //if the data pulled is shorter than the max length then pad it to the same size
				 fillDataArrays(dataArray, headers, mCursor,intSizeArray);
	     	     	 	    
	    		 dateChanged =populateStringBuilder(mCursor,dataArray,headers, ret, dateChanged,false);
	    		 
	    		
	    	     
	    	      
	   }   
		}else if (tableNo==2){
			dateChanged = "empty";
			int[] exIntSizeArray = new int[5];
			for(int i=0;i<intSizeArray.length;i++){
				exIntSizeArray[i]=0;
			}
			//instantiate the header and data arrays
			String[] exerciseHeaders = new String[]{"Date","Time","Exercise","Duration","Intensity"};
			
			String[] exerciseDataArray = new String[5];
			fileData = "Exercise_Data.txt";
			
			 ret = new StringBuilder("Saved Exercise Data:\n\r\n");
			   	      
	   
			mCursor = dataBase.rawQuery("SELECT * FROM "
					+ DBAdapter.DATABASE_TABLE_EXERCISE, null);
			
			fillMaxSizeArray(mCursor,exerciseDataArray,exIntSizeArray);
			

			mCursor = dataBase.rawQuery("SELECT * FROM "
					+ DBAdapter.DATABASE_TABLE_EXERCISE, null);
			 while(mCursor.moveToNext()){

	    		  //pull the data out of the database
	    		   //if the data pulled is shorter than the max length then pad it to the same size
				 fillDataArrays(exerciseDataArray, exerciseHeaders, mCursor,exIntSizeArray);
	     	     	 	    
	    		 dateChanged =populateStringBuilder(mCursor,exerciseDataArray,exerciseHeaders, ret, dateChanged,false);
	    		 
	    		
	    	     
	    	
	     	     
	    	      
	    	      
	    	     
	   }   
		}else if(tableNo==3){
			
			
			dateChanged = "empty";
			int[] proFoodIntSizeArray = new int[11];
			String[] proFoodDataArray = new String[11];
			for(int i=0;i<intSizeArray.length;i++){
				proFoodIntSizeArray[i]=0;
				proFoodDataArray[i] = "";
			}
			//instantiate the header and data arrays
			String[] proFoodHeaders = new String[]{"Date","Time","Food/Drink","Calories","Water","Carbs","Fat ","Saturated Fat","Protein","Iron ","Calcium"};
			
			fileData = "/Processed_Food_Data.txt";
			
			 ret = new StringBuilder("Saved Processed Food Data:\n\r\n");
			   	      
			
			mCursor = dataBase.rawQuery("SELECT * FROM "
					+ DBAdapter.DATABASE_TABLE_FOOD_NUTRI_DATA, null);
			
			 //pull the data out of the database
	    		   //if the data pulled is shorter than the max length then pad it to the same size
			fillMaxSizeArray(mCursor,proFoodDataArray,proFoodIntSizeArray);
			
			
			mCursor = dataBase.rawQuery("SELECT * FROM "
					+ DBAdapter.DATABASE_TABLE_FOOD_NUTRI_DATA, null);
			
			 while(mCursor.moveToNext()){

	    		 
				
				 fillDataArrays(proFoodDataArray, proFoodHeaders, mCursor,proFoodIntSizeArray);
				
	    		 dateChanged =populateStringBuilder(mCursor,proFoodDataArray,proFoodHeaders, ret, dateChanged,true);
	    		
	    	     
	    	
	     	     
	    	      
			 }      
	    	     
	   }  else{
			
		}
		
		
    	
         
    	  try {

  			String fileName = fileData;
    		  	
    		  	myFile = new File(pathName, fileName);
    	        myFile.createNewFile();
    	        FileOutputStream fOut = new FileOutputStream(myFile);
    	        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
    	        myOutWriter.append(ret.toString());
    	        myOutWriter.close();
    	        fOut.close();
    	        
    	    } catch (Exception e) {
    	        Toast.makeText(getBaseContext(), e.getMessage(),
    	                Toast.LENGTH_SHORT).show();
    	    }
    	  mCursor.close();
    	  
    }
    private String populateStringBuilder(Cursor c, String[] data,String[] headings,
			StringBuilder sb,String dateLog,boolean lineTotals) {
    	
    	if(dateLog.equals(data[0])){
	    	  //if the date is the same as the previous entry carry on outputting data
    		for(int i=1;i<data.length;i++){
    			sb.append(data[i]+"|");
    			
    			if(i==data.length-1){
    				sb.append("\n\r\n");
    			}
    		}
	    	
	      }else{
	    	
	    		  
	    		  
	    	  
	    	  //if the date changes then output new headers, which are also padded to fit the max size constraints
	    	  //set the dateChanged variable to the new date
	    	 dateLog= data[0];
	    	 //display the new day as a sub heading
	    	  sb.append("\n\r\n" +"| Data for " + data[0] + " |" + "\n\r\n");
	    	  if(lineTotals == true){
	    		
	    		  
	    		  
	    		  sb.append("\n\r\n" +"| Totals for " + data[0] + " |" + "\n\r\n");
	    		  for(int i=1;i<headings.length;i++){
		    			sb.append(headings[i]+"|");
		    			if(i==headings.length-1){
		    				sb.append("\n\r\n");
		    			
		    			}
		    		}
	    		  
	    		  for (int i=2;i<=data.length;i++){
	    			  String buffer =padText(data[i-1].length(),calculateTotals(dateLog)[i]);
	    			  if (i<4){
	    				  buffer =(padText(data[i-1].length(),"N/A"));
	    			  }
	    			  
    				   sb.append(buffer);
    				   sb.append("|");
    				   if(i==headings.length){
		    				sb.append("\n\r\n");
		    				
		    			}
    			  }
    			 
	    		  sb.append("\n\r\n" +"| Item breakdown for " + data[0] + " |" + "\n\r\n");
    			
    		  }
	    	  
	    	//header pop here
	    	  for(int i=1;i<headings.length;i++){
	    			sb.append(headings[i]+"|");
	    			if(i==headings.length-1){
	    				sb.append("\n\r\n");
	    			}
	    		}
	    	  
	    	  for(int i=1;i<data.length;i++){
	    			sb.append(data[i]+"|");
	    			if(i==data.length-1){
	    				sb.append("\n\r\n");
	    			}
	    		}
	    	  
	    	
	      }
		return dateLog;
		
	}


	public String padText(int padAmount, String stringToPad){
    	return String.format("%1$-" + padAmount + "s", stringToPad);
    }
	public String[] calculateTotals(String date){
		  
		Cursor c = dataBase.rawQuery("SELECT * FROM "
				+ DBAdapter.DATABASE_TABLE_FOOD_NUTRI_DATA +" WHERE " + DBAdapter.KEY_DATE + " LIKE '%" + date+"%'"  , null);
		
		double[] totals = new double[12];
		for(int i=0; i<totals.length;i++){
			totals[i] = 0.0;
		}
		
		if (c.moveToFirst()) {
			  
			do {
				
				
				for(int i=2;i<totals.length;i++){
					      
					
					totals[i] += c.getDouble(i);
					  
				
				}
				
				
				
			} while (c.moveToNext());
    	
    }
		String[] values = new String[12];
		for(int i=0; i<totals.length;i++){
			
			values[i] = String.format("%.2f", totals[i]);
			
			
							
			
		}
		
		
		return values;
		
		
	}
    public void fillMaxSizeArray(Cursor c,String[] data,int[] maxSize){
    	//method loops through the data set and finds the maximum size of each output in order to standardise the outputs
    	if (c.moveToFirst()) {
			do {
				
				
				for(int i=0;i<maxSize.length;i++){
					data[i] = c.getString(i+1);
					if(data[i].length() > maxSize[i]){
						maxSize[i] = data[i].length();
					}
				}
				
				
				
			} while (c.moveToNext());
    	
    }
    	
    }
    	
    
    public void fillDataArrays(String[] data,String[]headings, Cursor c,int[] intArray){
    
    	//for display continuity this method buffers the output so the columns remain equally sized
    	//if the headings are the longest string pad the data array to the maximum size
    	for(int i=0;i<headings.length;i++){
			if(intArray[i]<headings[i].length()){
				intArray[i]= headings[i].length();
			}
		}
    	//if the input from the user in data array is the longest string pad the headers and data to the maximum size
    	for(int i=0;i<data.length;i++){
    		data[i] = c.getString(i+1);
        	if(data[i].length()<intArray[i]){
     			//pad the data
       		  data[i]=padText(intArray[i],data[i]);
       		
    	}
        	 if(headings[i].length()<intArray[i]){
    			 //pad the headers
    			
    		  headings[i]=padText(intArray[i],headings[i]);
    	  }
    	 
   	  }
		 
    }
    
    
    
    
  }
    


