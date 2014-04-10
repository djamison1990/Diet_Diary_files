package uuj.diet.diary;

import java.util.ArrayList;
import uuj.diet.diary.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
//this class is employed to restrict output to the user and is utilised in diary_input
public class RecentItemsAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> food;
	private ArrayList<String> portion;

	
	public RecentItemsAdapter(Context c, ArrayList<String> food, ArrayList<String> portion) {
		this.mContext = c;
		//sets class variables equal to the oes passed in
		
		this.food = food;
		this.portion = portion;
		
		
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return food.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int pos, View child, ViewGroup parent) {
		Holder mHolder;
		LayoutInflater layoutInflater;
		if (child == null) {
			//if the view about to be used is empty then it is pased in a layout via the layout inflator
			layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			child = layoutInflater.inflate(R.layout.display_all_template, null);
			mHolder = new Holder();
			mHolder.txt_food_template = (TextView) child.findViewById(R.id.txt_foodTemplate);
			mHolder.txt_portion_template = (TextView) child.findViewById(R.id.txt_portionTemplate);
			
			child.setTag(mHolder);
		} else {
			//if it isnot empty then we set the holder equal to it
			mHolder = (Holder) child.getTag();
		}
		//passes back the values relative to the item the user selects via input
		mHolder.txt_food_template.setText(food.get(pos));
		mHolder.txt_portion_template.setText(portion.get(pos));
		
		
		return child;
	}

	public class Holder {
		
		//'class' to hold the values of the textviews being utilised.
		TextView txt_portion_template;
		TextView txt_food_template;
		
	}

}
