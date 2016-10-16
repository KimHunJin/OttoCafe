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
import sungkyul.ac.kr.ottocafe.items.CartItem;
import sungkyul.ac.kr.ottocafe.utils.EndString;

/**
 * Created by HunJin on 2016-09-13.
 * MenuListAdapter와 동일한 폼으로 통일 필요
 */
public class CartListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<CartItem> item;
    private int layout;

    public CartListAdapter(Context context, int layout, ArrayList<CartItem> item) {
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

            viewHolder.imgItemCart = (ImageView)convertView.findViewById(R.id.imgItemCart);
            viewHolder.txtItemCartName = (TextView)convertView.findViewById(R.id.txtItemCartName);
            viewHolder.txtItemCartCost = (TextView)convertView.findViewById(R.id.txtItemCartTotalCost);
            viewHolder.txtItemCartCount = (TextView)convertView.findViewById(R.id.txtItemCartCount);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CartItem listItem = item.get(position);

        Picasso.with(inflater.getContext()).load(listItem.getcUrl()).resize(1020, 492).centerCrop().into(viewHolder.imgItemCart);
        viewHolder.txtItemCartName.setText(EndString.endString(listItem.getcName().toString(), 15));
        viewHolder.txtItemCartCost.setText(listItem.getcTotalCost());
        viewHolder.txtItemCartCount.setText(EndString.endString(listItem.getcCount().toString(), 25));
        return convertView;
    }

    class ViewHolder {
        private ImageView imgItemCart;
        private TextView txtItemCartName;
        private TextView txtItemCartCost;
        private TextView txtItemCartCount;
    }
}
