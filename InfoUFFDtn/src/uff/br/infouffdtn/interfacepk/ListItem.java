package uff.br.infouffdtn.interfacepk;



import uff.br.infouffdtn.R;
import uff.br.infouffdtn.interfacepk.TwoTextArrayAdapter.RowType;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ListItem implements Item {
    private final String         str1;
    private final String         str2;
    public ListItem( String text1, String text2) {
        this.str1 = text1;
        this.str2 = text2;
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
        text1.setText(str1);
        text2.setText(str2);

        return view;
    }
    @Override
    public String toString()
    {
    	return str1+str2;
    }
    
}