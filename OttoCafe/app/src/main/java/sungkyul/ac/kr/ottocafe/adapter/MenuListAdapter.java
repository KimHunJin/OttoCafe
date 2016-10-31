package sungkyul.ac.kr.ottocafe.adapter;

import android.content.Context;
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
import sungkyul.ac.kr.ottocafe.utils.CostChange;
import sungkyul.ac.kr.ottocafe.utils.EndString;

/**
 * Created by HunJin on 2016-09-15.
 * 리사이클러뷰를 활용하여 만든 정말 쓰기에 좋아보이는 어댑터 클래스
 */
public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.GenericViewHolder> {

    static final String TAG = "MenuListAdapter";

    private static final int ITEM_VIEW_TYPE_STRS = 0;
    private static final int ITEM_VIEW_TYPE_IMGS = 1;
    private static final int ITEM_VIEW_TYPE_MAX = 2;

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
        items = new ArrayList<>();

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
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public GenericViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.e(TAG, viewType + "");

        switch (viewType) {
            case ITEM_VIEW_TYPE_STRS: {
                view = inflater.inflate(R.layout.item_menu_list_1, viewGroup, false);
                return new ViewHolder(view);
            }
            default: {
                view = inflater.inflate(R.layout.item_menu_list_2, viewGroup, false);
                return new ViewHolder2(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(GenericViewHolder viewHolder, int position) {
        viewHolder.setDataOnView(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    abstract class GenericViewHolder extends RecyclerView.ViewHolder {
        public GenericViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void setDataOnView(int position);
    }

    class ViewHolder extends GenericViewHolder {
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

        @Override
        public void setDataOnView(int position) {
            MenuItem listItem = items.get(position);
            Picasso.with(inflater.getContext()).load(listItem.getmImageUrl()).resize(170, 120).centerCrop().into(this.imgItemMenu);
            this.txtItemMenuName.setText(EndString.endString(listItem.getmName().toString(), 15));
            this.txtItemMenuCost.setText(CostChange.changCost(listItem.getmCost()));
            this.txtItemMenuExplain.setText(EndString.endString(listItem.getmExplain().toString(), 25));
        }
    }

    class ViewHolder2 extends GenericViewHolder {
        private ImageView imgItemMenu;
        private TextView txtItemMenuName;
        private TextView txtItemMenuCost;
        private TextView txtItemMenuExplain;

        public ViewHolder2(View v) {
            super(v);
            imgItemMenu = (ImageView) v.findViewById(R.id.imgItemMenu2);
            txtItemMenuCost = (TextView) v.findViewById(R.id.txtItemMenuPrice2);
            txtItemMenuName = (TextView) v.findViewById(R.id.txtItemMenuName2);
            txtItemMenuExplain = (TextView) v.findViewById(R.id.txtItemMenuExplain2);

        }

        @Override
        public void setDataOnView(int position) {
            MenuItem listItem = items.get(position);
            Picasso.with(inflater.getContext()).load(listItem.getmImageUrl()).resize(170, 120).centerCrop().into(this.imgItemMenu);
            this.txtItemMenuName.setText(EndString.endString(listItem.getmName().toString(), 15));
            this.txtItemMenuCost.setText(CostChange.changCost(listItem.getmCost()));
            this.txtItemMenuExplain.setText(EndString.endString(listItem.getmExplain().toString(), 25));
        }
    }
}

