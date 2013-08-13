package uff.br.infouffdtn;


import android.content.Context;
import android.graphics.Color;
//import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayAdapter extends ArrayAdapter<String> {
  private final Context context;
  private final String[] values;

  public DisplayAdapter(Context context, String[] values) {
    super(context, R.layout.displayactivitymenu, values);
    this.context = context;
    this.values = values;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.displayactivitymenu, parent, false);
    TextView textView = (TextView) rowView.findViewById(R.id.label);
    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
    textView.setText(values[position]);
    // Change the icon for Windows and iPhone


    
    imageView.setImageResource(R.drawable.square);
    imageView.setAlpha(1);

    float percent = (float)position/(float)values.length;
    imageView.setBackgroundColor((int)interpolateColor(Color.GREEN,Color.RED,percent));
    //imageView.setBackgroundColor(color.BLACK);

    return rowView;
  }
  private float interpolate(float a, float b, float proportion) {
	    return (a + ((b - a) * proportion));
	  }

	  /** Returns an interpoloated color, between <code>a</code> and <code>b</code> */
	  private int interpolateColor(int a, int b, float proportion) {
	    float[] hsva = new float[3];
	    float[] hsvb = new float[3];
	    Color.colorToHSV(a, hsva);
	    Color.colorToHSV(b, hsvb);
	    for (int i = 0; i < 3; i++) {
	      hsvb[i] = interpolate(hsva[i], hsvb[i], proportion);
	    }
	    return Color.HSVToColor(hsvb);
	  }
} 