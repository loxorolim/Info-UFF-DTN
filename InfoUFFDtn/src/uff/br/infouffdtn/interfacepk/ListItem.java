package uff.br.infouffdtn.interfacepk;



import uff.br.infouffdtn.R;
import uff.br.infouffdtn.db.Content;
import uff.br.infouffdtn.interfacepk.TextArrayAdapter.RowType;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ListItem implements Item {
   // private final String         type;
   // private final String         date;
   //private final String filepath;
    private final Content content;
    public ListItem(Content c) {
        //this.type = text1;
        //this.date = text2;
        //this.filepath = filepath;
        content = c;
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
        ImageView img = (ImageView) view.findViewById(R.id.imageView1);
  //      cb.setChecked(FileManager.getCheckOptionFromFile(str2,ctx));
        text1.setText(content.getName());
        text2.setText(content.getDate());
        if(content.isCommSource())
        	img.setImageResource(R.drawable.wifipacket);
        else
        	img.setImageResource(R.drawable.dtnpacket);
        	
        
        
        

        return view;
    }
    public Content getContent()
    {
    	return content;
    }
    @Override
    public String toString()
    {
    	return content.getName() +" "+ content.getDate()+" "+ content.getFilepath();
    }

}