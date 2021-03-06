package sungkyul.ac.kr.ottocafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sungkyul.ac.kr.ottocafe.R;
import sungkyul.ac.kr.ottocafe.items.NavItem;


/**
 * Created by HunJin on 2016-09-23.
 */
public class NavCoreListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<NavItem> item;
    private int layout;

    public NavCoreListAdapter(Context context, int layout, ArrayList<NavItem> item) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.item = item;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = inflater.inflate(layout, parent, false);

            viewHolder.txtNavCoreListContent = (TextView) convertView.findViewById(R.id.txtNavListContent);
            viewHolder.imgNavCoreList = (ImageView)convertView.findViewById(R.id.imgNavItem);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NavItem listItem = item.get(position);
        viewHolder.txtNavCoreListContent.setText(listItem.getContent().toString());
        Picasso.with(inflater.getContext()).load(listItem.getImage()).into(viewHolder.imgNavCoreList);

        return convertView;
    }

    class ViewHolder {
        private TextView txtNavCoreListContent;
        private ImageView imgNavCoreList;
    }
}
