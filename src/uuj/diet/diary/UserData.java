package uuj.diet.diary;






import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import uuj.diet.diary.R;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class UserData extends Activity {

	Button bt1;
	
	EditText et1,et2,et3,et4;
	Spinner sp1;
	String strWeight, strHeight, strDOB, strActivity;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_input);
        
        // get action bar   
        ActionBar actionBar = getActionBar();
 
        // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        createItems();
       
       
        bt1.setOnClickListener(new OnClickListener() {
        	   @Override
        	   public void onClick(View v) {
        	      saveData();
        	  	
        	   }
        	  });
       
    }
	
    
    public void createItems(){
    	bt1 = (Button) findViewById(R.id.btnSubmitUserData);
        et1 = (EditText) findViewById(R.id.etUserInput1);
        et2 = (EditText) findViewById(R.id.etUserInput2);
        et3 = (EditText) findViewById(R.id.etUserInput3);
        sp1 = (Spinner) findViewById(R.id.spActivity);
    }
	private void saveData(){
	//nested ifs to verify data entry is valid
		if(et1.getText().toString().trim().length()>0){
			if(et2.getText().toString().trim().length()>0){
				if(et3.getText().toString().trim().length()>0){
					//if all boxes are not empty then we create a stringbuilder to enter the data into the text file created
					StringBuilder ret = new StringBuilder("Saved User Data:\n\r\n");
					String pathName= Environment.getExternalStorageDirectory().getAbsolutePath() + "/uujDietDiary";
					File myFile;
					myFile = new File(pathName, "User_Data.txt");
					//data appended to the stringbuilder
					strWeight = et1.getText().toString().trim();
					ret.append("User Weight :" +strWeight +"\n\r\n");
					strHeight = et2.getText().toString().trim();
					ret.append("User Height :" +strHeight+"\n\r\n");
					strDOB = et3.getText().toString().trim();
					ret.append("User Date of Birth :" +strDOB+"\n\r\n");
					strActivity = sp1.getSelectedItem().toString().trim();
					ret.append("User Activity Level :" +strActivity+"\n\r\n");
						
					
			        try {
			        	//creates the file if it doesnt exist
						myFile.createNewFile();
					//creates a file output stream and streamwriter to write the file and write data into it
			        FileOutputStream fOut = new FileOutputStream(myFile);
			        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			        //append the stringbuilder contents to the output writer
			        myOutWriter.append(ret.toString());
			        myOutWriter.close();
			        fOut.close();
			        //close down the writers to avoid memory leaks
			        
			        } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				//toasts for invalid data entry prompts
						Toast.makeText(getApplicationContext(), "Thank you for logging your user data", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getApplicationContext(), "Please enter a valid date of birth", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(getApplicationContext(), "Please enter a valid height", Toast.LENGTH_LONG).show();
			}
		}else{
			Toast.makeText(getApplicationContext(), "Please enter a valid weight", Toast.LENGTH_LONG).show();
		}
		
		
			
		
				
		}
		
			
			
		
		
		
				
		
		
		
		
		
		
		
	}
    
    

			
		
      
   
 
  
    


