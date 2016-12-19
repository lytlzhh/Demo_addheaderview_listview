package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xerdp.demo_addheaderview_listview.Bean;
import com.example.xerdp.demo_addheaderview_listview.MainActivity;
import com.example.xerdp.demo_addheaderview_listview.R;

import java.util.List;

/**
 * Created by xerdp on 2016/12/13.
 */

public class List_adapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater = null;
    private List<Bean> list = null;


    public List_adapter(List<Bean> list, MainActivity mainActivity) {
        this.list = list;
        this.context = mainActivity;
        layoutInflater = LayoutInflater.from(context);
    }

    public void onDatachange(List<Bean> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override

    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.myitem, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.mytextview_id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.textView.setText(list.get(position).getSrt());

        return convertView;
    }


    public class ViewHolder {
        private TextView textView = null;
    }


}
