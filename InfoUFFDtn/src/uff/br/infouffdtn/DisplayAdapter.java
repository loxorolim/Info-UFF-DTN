package uff.br.infouffdtn;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
//import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayAdapter extends ArrayAdapter<String>
{
	private int layoutID;
	private final Context context;
	private final String[] values;

	public DisplayAdapter(int listViewId, String[] values,Context ctx)
	{
		super(ctx, listViewId, values);
		this.layoutID = listViewId;
		this.values = values;
		this.context = ctx;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View rowView = convertView;
		//LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//View rowView = inflater.inflate(R.layout.displayactivitymenu, parent, false);
		if(rowView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(context);
        	rowView = inflater.inflate(layoutID, parent, false);
		}
        
		
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		ImageView imageView2 = (ImageView) rowView.findViewById(R.id.icon2);
		textView.setText(values[position]);

	/*	if (ContentsDatabase.getSourceFromDate(values[position], context))
		{
			imageView2.setImageResource(R.drawable.wifipacket);
		}
		else
		{
			imageView2.setImageResource(R.drawable.dtnpacket);
		}
		*/
		imageView.setImageResource(R.drawable.square);
		imageView.setAlpha(1);

		float percent = (float) position / (float) values.length;
		imageView.setBackgroundColor((int) interpolateColor(Color.GREEN, Color.RED, percent));

		return rowView;
	}

	private float interpolate(float a, float b, float proportion)
	{
		return (a + ((b - a) * proportion));
	}

	/** Returns an interpoloated color, between <code>a</code> and <code>b</code> */
	private int interpolateColor(int a, int b, float proportion)
	{
		float[] hsva = new float[3];
		float[] hsvb = new float[3];
		Color.colorToHSV(a, hsva);
		Color.colorToHSV(b, hsvb);
		for (int i = 0; i < 3; i++)
		{
			hsvb[i] = interpolate(hsva[i], hsvb[i], proportion);
		}
		return Color.HSVToColor(hsvb);
	}
}