package uuj.diet.diary;

import java.util.ArrayList;
import uuj.diet.diary.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
//this class is employed to restrict output to the user and is utilised in diary_output
public class OutputAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> date;
	private ArrayList<String> time;
	private ArrayList<String> food;
	private ArrayList<String> portion;
	private ArrayList<String> notes;
	
	public OutputAdapter(Context c, ArrayList<String> date,ArrayList<String> time, ArrayList<String> food, ArrayList<String> portion,ArrayList<String> notes) {
		
		this.mContext = c;
		//constructs  the output adapter. Sets the context and array lists equal to the ones utilised in diary_output
		this.date = date;
		this.time = time;
		this.food = food;
		this.portion = portion;
		this.notes = notes;
	}

	public int getCount() {
		//gets the max size of the array lists by checking the size of an array always utilised(in this case date)
		return date.size();
	}

	public Object getItem(int position) {
		
		return null;
	}

	public long getItemId(int position) {
		
		return 0;
	}

	public View getView(int pos, View child, ViewGroup parent) {
		Holder mHolder;
		LayoutInflater layoutInflater;
		if (child == null) {
			//if the view is empty instantiate it and use layout inflator to pull the template xml file and place it into the view
			layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			child = layoutInflater.inflate(R.layout.display_all_template, null);
			mHolder = new Holder();
			mHolder.txt_id = (TextView) child.findViewById(R.id.txt_id);
			mHolder.txt_time = (TextView) child.findViewById(R.id.txt_time);
			mHolder.txt_food = (TextView) child.findViewById(R.id.txt_foodTemplate);
			mHolder.txt_portion = (TextView) child.findViewById(R.id.txt_portionTemplate);
			mHolder.txt_notes = (TextView) child.findViewById(R.id.txt_notes);
			child.setTag(mHolder);
		} else {
			//if the view isnt empty simply link the holder item to it
			mHolder = (Holder) child.getTag();
		}
		//returns values present in each array list relative to the position passed in via the users selection
		mHolder.txt_id.setText(date.get(pos));
		mHolder.txt_time.setText(time.get(pos));
		mHolder.txt_food.setText(food.get(pos));
		mHolder.txt_portion.setText(portion.get(pos));
		mHolder.txt_notes.setText(notes.get(pos));
		return child;
	}

	public class Holder {
		//'class' within the adapter instantiates the textviews
		TextView txt_notes;
		TextView txt_portion;
		TextView txt_id;
		TextView txt_food;
		TextView txt_time;
	}

}
