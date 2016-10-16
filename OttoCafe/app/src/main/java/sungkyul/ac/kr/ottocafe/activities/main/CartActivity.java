package sungkyul.ac.kr.ottocafe.activities.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import sungkyul.ac.kr.ottocafe.R;
import sungkyul.ac.kr.ottocafe.activities.credit.NicePayDemoActivity;
import sungkyul.ac.kr.ottocafe.adapter.CartListAdapter;
import sungkyul.ac.kr.ottocafe.adapter.MenuListAdapter;
import sungkyul.ac.kr.ottocafe.items.CartItem;
import sungkyul.ac.kr.ottocafe.items.MenuItem;

/**
 * Created by HunJin on 2016-09-17.
 *
 * cart activity
 */
public class CartActivity extends AppCompatActivity {

    private ListView lst;
    private CartListAdapter cartListAdapter;
    private ArrayList<CartItem> cartItemArrayList;
    private Button btnCartCencel, btnCartPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initialization();

        cartListAdapter = new CartListAdapter(getApplicationContext(), R.layout.item_menu_cart, cartItemArrayList);
        lst.setAdapter(cartListAdapter);

        addCart();
        cartListAdapter.notifyDataSetChanged();

        listener();
    }

    /**
     * add your cart list
     */
    void addCart() {
        // get list using sqlite

        cartItemArrayList.add(new CartItem(0, "블랙커피", "8,000", "2", "http://14.63.196.255/020cafe_image/blackcoffee.jpg"));
        cartItemArrayList.add(new CartItem(1, "카페라떼", "4,500", "1", "http://14.63.196.255/020cafe_image/latte.jpg"));
    }

    /**
     * component initialization
     */
    void initialization() {
        lst = (ListView) findViewById(R.id.lst_cart);
        btnCartCencel = (Button)findViewById(R.id.btnCartCencel);
        btnCartPayment = (Button)findViewById(R.id.btnCartPayment);

        cartItemArrayList = new ArrayList<>();
    }

    /**
     * list listener
     */
    void listener() {
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // show detail information
            }
        });

        lst.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // alert dialog - delete or cancel
                return false;
            }
        });

        btnCartCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCartPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NicePayDemoActivity.class));
            }
        });
    }
}
