package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.ninesole.photexpro.R;

public class FontsListAdapter extends BaseAdapter {
    // references to our images
//    public static Integer[] mThumbIds = {
//
//            R.drawable.frimg, R.drawable.ic_launcher, R.drawable.ic_launcher,
//            R.drawable.ic_launcher, R.drawable.frimg, R.drawable.ic_launcher,
//            R.drawable.frimg, R.drawable.ic_launcher, R.drawable.frimg,
//            R.drawable.frimg, R.drawable.ic_launcher, R.drawable.frimg,
//            R.drawable.ic_launcher, R.drawable.frimg, R.drawable.ic_launcher,
//            R.drawable.frimg, R.drawable.ic_launcher, R.drawable.frimg
//    };
    private Context mContext;
    private LayoutInflater mInflater;
    private String[] categoryContent = {"font1", "font2", "font3", "font4", "font5", "font6"};

    public FontsListAdapter(Context c) {
        mInflater = LayoutInflater.from(c);
        mContext = c;
    }

    public int getCount() {
        return categoryContent.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {  // if it's not recycled,
            convertView = mInflater.inflate(R.layout.custom, null);
            convertView.setLayoutParams(new GridView.LayoutParams(90, 90));
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tv_font_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(categoryContent[position]);

        return convertView;
    }

    class ViewHolder {
        public TextView title;

    }


}