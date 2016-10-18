package sungkyul.ac.kr.ottocafe.activities.main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import sungkyul.ac.kr.ottocafe.R;
import sungkyul.ac.kr.ottocafe.activities.credit.NicePayDemoActivity;
import sungkyul.ac.kr.ottocafe.sql.SQLite;
import sungkyul.ac.kr.ottocafe.adapter.CartListAdapter;
import sungkyul.ac.kr.ottocafe.items.CartItem;
import sungkyul.ac.kr.ottocafe.utils.CostChange;
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

    private SQLite mSQLite;
    private AlertDialog ald;
    private AlertDialog.Builder aldB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initialization();

        cartListAdapter = new CartListAdapter(getApplicationContext());
        addCart();
        rcv.setAdapter(cartListAdapter);

        listener();
    }

    /**
     * add your cart list
     */
    void addCart() {
        // get list using sqlite
        // SQLite를 활용해서 장바구니 리스트를 가져와야 함

        Cursor cursor = mSQLite.select();
        cartListAdapter.clear();
        int mNumber;
        String mName;
        int mCost;
        int mCount;
        String mImgUrl;

        while (cursor.moveToNext()) {
            mNumber = cursor.getInt(0);
            mName = cursor.getString(1);
            mCost = cursor.getInt(2);
            mCount = cursor.getInt(3);
            mImgUrl = cursor.getString(4);

            cartListAdapter.addData(new CartItem(mNumber, mName, CostChange.changCost(mCount), CostChange.changCost(mCost), mImgUrl));
        }
    }

    /**
     * component initialization
     */
    void initialization() {
        rcv = (RecyclerView) findViewById(R.id.rcv_cart);
        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        btnCartCencel = (Button) findViewById(R.id.btnCartCencel);
        btnCartPayment = (Button) findViewById(R.id.btnCartPayment);

        cartItemArrayList = new ArrayList<>();
        mSQLite = new SQLite(getApplicationContext(), "menu.db", null, 1);
    }

    /**
     * recycler view listener
     */
    void listener() {
        rcv.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getApplicationContext(), rcv, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "click");
                // intent detail menu

            }

            @Override
            public void onItemLongClick(View v, final int position) {
                Log.d(TAG, "long click");
                dialog(position);
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

    /**
     * show alert dialog
     * @param position
     */
    void dialog(final int position) {
        aldB = new AlertDialog.Builder(CartActivity.this);
        aldB.setMessage("삭제하시겠습니까?");
        aldB.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mSQLite.delete(cartListAdapter.getItems().get(position).getcName());
                cartListAdapter.removeData(position);
            }
        });
        aldB.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        ald = aldB.create();
        ald.show();
    }

    /**
     * 뒤로가기 키를 눌렀을 때
     */
    @Override
    public void onBackPressed() {
        //핸들러 작동
        mSQLite.close();
        finish();
    }
}
