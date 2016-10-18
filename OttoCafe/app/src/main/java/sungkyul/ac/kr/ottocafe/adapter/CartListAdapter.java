package sungkyul.ac.kr.ottocafe.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import sungkyul.ac.kr.ottocafe.R;
import sungkyul.ac.kr.ottocafe.items.CartItem;
import sungkyul.ac.kr.ottocafe.items.MenuItem;
import sungkyul.ac.kr.ottocafe.utils.EndString;

/**
 * Created by HunJin on 2016-09-13.
 * MenuListAdapter와 동일한 폼으로 통일 필요
 */
public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    static final String TAG = "CartListAdapter";

    private View view;
    private LayoutInflater inflater;
    private Context context;
    List<CartItem> items;

    public CartListAdapter(Context context) {
        super();
        this.context = context;
        items = new ArrayList<>();
    }

    public void addData(CartItem item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_menu_cart, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        CartItem listItem = items.get(position);

        Picasso.with(inflater.getContext()).load(listItem.getcUrl()).resize(1020, 492).centerCrop().into(viewHolder.imgItemCart);
        viewHolder.txtItemCartName.setText(EndString.endString(listItem.getcName().toString(), 15));
        viewHolder.txtItemCartCost.setText(listItem.getcTotalCost());
        viewHolder.txtItemCartCount.setText(EndString.endString(listItem.getcCount().toString(), 25));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgItemCart;
        private TextView txtItemCartName;
        private TextView txtItemCartCost;
        private TextView txtItemCartCount;

        public ViewHolder(View v) {
            super(v);
            imgItemCart = (ImageView) v.findViewById(R.id.imgItemCart);
            txtItemCartName = (TextView) v.findViewById(R.id.txtItemCartName);
            txtItemCartCost = (TextView) v.findViewById(R.id.txtItemCartTotalCost);
            txtItemCartCount = (TextView) v.findViewById(R.id.txtItemCartCount);
        }
    }
}
