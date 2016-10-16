package sungkyul.ac.kr.ottocafe.adapter;

import android.content.Context;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import sungkyul.ac.kr.ottocafe.R;
import sungkyul.ac.kr.ottocafe.items.MenuItem;
import sungkyul.ac.kr.ottocafe.utils.EndString;

/**
 * Created by HunJin on 2016-09-15.
 */
public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder> {

    static final String TAG = "MenuListAdapter";

    private View view;
    private LayoutInflater inflater;
    private Context context;
    List<MenuItem> items;

    public List<MenuItem> getItems() {
        return items;
    }

    public MenuListAdapter(Context context) {
        super();
        this.context = context;
        items = new ArrayList<MenuItem>();

    }

    public void addData(MenuItem item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_menu_list, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        MenuItem listItem = items.get(position);
        Log.e(TAG,inflater.getContext()+"");
        Picasso.with(inflater.getContext()).load(listItem.getmImageUrl()).resize(1020, 492).centerCrop().into(viewHolder.imgItemMenu);
        viewHolder.txtItemMenuName.setText(EndString.endString(listItem.getmName().toString(), 15));
        viewHolder.txtItemMenuCost.setText(listItem.getmCost());
        viewHolder.txtItemMenuExplain.setText(EndString.endString(listItem.getmExplain().toString(), 25));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgItemMenu;
        private TextView txtItemMenuName;
        private TextView txtItemMenuCost;
        private TextView txtItemMenuExplain;

        public ViewHolder(View v) {
            super(v);
            imgItemMenu = (ImageView) v.findViewById(R.id.imgItemMenu);
            txtItemMenuCost = (TextView) v.findViewById(R.id.txtItemMenuPrice);
            txtItemMenuName = (TextView) v.findViewById(R.id.txtItemMenuName);
            txtItemMenuExplain = (TextView) v.findViewById(R.id.txtItemMenuExplain);
        }
    }
}
