package sungkyul.ac.kr.ottocafe.activities.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import sungkyul.ac.kr.ottocafe.R;
import sungkyul.ac.kr.ottocafe.activities.credit.NicePayDemoActivity;
import sungkyul.ac.kr.ottocafe.activities.menu.DetailMenuActivity;
import sungkyul.ac.kr.ottocafe.adapter.CartListAdapter;
import sungkyul.ac.kr.ottocafe.items.CartItem;
import sungkyul.ac.kr.ottocafe.utils.RecyclerViewOnItemClickListener;

/**
 * Created by HunJin on 2016-09-17.
 * <p>
 * cart activity
 */
public class CartActivity extends AppCompatActivity {

    static final String TAG = "CartActivity";

    private RecyclerView rcv;
    private CartListAdapter cartListAdapter;
    private ArrayList<CartItem> cartItemArrayList;
    private Button btnCartCencel, btnCartPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initialization();

        cartListAdapter = new CartListAdapter(getApplicationContext());
        rcv.setAdapter(cartListAdapter);

        addCart();
        cartListAdapter.notifyDataSetChanged();

        listener();
    }

    /**
     * add your cart list
     */
    void addCart() {
        // get list using sqlite
        // SQLite를 활용해서 장바구니 리스트를 가져와야 함

        cartItemArrayList.add(new CartItem(0, "블랙커피", "8,000", "2", "http://14.63.196.255/020cafe_image/blackcoffee.jpg"));
        cartItemArrayList.add(new CartItem(1, "카페라떼", "4,500", "1", "http://14.63.196.255/020cafe_image/latte.jpg"));
    }

    /**
     * component initialization
     */
    void initialization() {
        rcv = (RecyclerView) findViewById(R.id.rcv_cart);
        btnCartCencel = (Button) findViewById(R.id.btnCartCencel);
        btnCartPayment = (Button) findViewById(R.id.btnCartPayment);

        cartItemArrayList = new ArrayList<>();
    }

    /**
     * recycler view listener
     */
    void listener() {
        rcv.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getApplicationContext(), rcv, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "click");
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "long click");
            }
        }));

        btnCartCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCartPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 장바구니에 담은 것들을 한번에 결제 (서버처리)
                startActivity(new Intent(getApplicationContext(), NicePayDemoActivity.class));
            }
        });
    }
}
