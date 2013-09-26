package uff.br.infouffdtn.interfacepk;



import uff.br.infouffdtn.R;
import uff.br.infouffdtn.interfacepk.TwoTextArrayAdapter.RowType;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListItem implements Item {
    private final String         type;
    private final String         date;
    private final String filepath;
    public ListItem( String filepath,String text1, String text2) {
        this.type = text1;
        this.date = text2;
        this.filepath = filepath;
    }

    @Override
    public int getViewType() {
        return RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        View view;
        if (convertView == null) {
            view = (View) inflater.inflate(R.layout.my_list_item, null);
            // Do some initialization
        } else {
            view = convertView;
        }

        TextView text1 = (TextView) view.findViewById(R.id.list_content1);
        TextView text2 = (TextView) view.findViewById(R.id.list_content2);
  //      cb.setChecked(FileManager.getCheckOptionFromFile(str2,ctx));
        text1.setText(type);
        text2.setText(date);

        return view;
    }
    @Override
    public String toString()
    {
    	return type+date;
    }
    
    public String getType()
    {
    	return this.type;
    
    }
    public String getFilePath()
    {
    	return this.filepath;
    }
}