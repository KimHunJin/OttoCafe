package sungkyul.ac.kr.ottocafe.activities.menu;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sungkyul.ac.kr.ottocafe.R;
import sungkyul.ac.kr.ottocafe.adapter.CartListAdapter;
import sungkyul.ac.kr.ottocafe.items.CartItem;
import sungkyul.ac.kr.ottocafe.repo.ConnectService;
import sungkyul.ac.kr.ottocafe.repo.RepoItem;
import sungkyul.ac.kr.ottocafe.sql.SQLite;
import sungkyul.ac.kr.ottocafe.utils.CostChange;
import sungkyul.ac.kr.ottocafe.utils.RecyclerViewOnItemClickListener;
import sungkyul.ac.kr.ottocafe.utils.SaveDataSession;
import sungkyul.ac.kr.ottocafe.utils.StaticUrl;

/**
 * Created by HunJin on 2016-09-17.
 * <p>
 * cart activity
 */
public class CartActivity extends AppCompatActivity {

    static final String TAG = "CartActivity";

    private RecyclerView rcvDrink, rcvSide;
    private CartListAdapter cartListAdapter, cartListSideAdapter;
    private ArrayList<CartItem> cartItemArrayList;
    private Button btnCartClear, btnCartPayment;
    private TextView txtDrinkCount, txtSideCount, txtTotalPrice;
    private ImageView imgToolbarBack;

    private SQLite mSQLite;
    private AlertDialog ald;
    private AlertDialog.Builder aldB;
    private Toolbar toolbar;
    private int totalCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initialization();

        cartListAdapter = new CartListAdapter(getApplicationContext());
        cartListSideAdapter = new CartListAdapter(getApplicationContext());
        addCart();
        rcvDrink.setAdapter(cartListAdapter);
        rcvSide.setAdapter(cartListSideAdapter);

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
        int count = 0;

        while (cursor.moveToNext()) {
            mNumber = cursor.getInt(0);
            mName = cursor.getString(1);
            mCost = cursor.getInt(2);
            mCount = cursor.getInt(3);
            mImgUrl = cursor.getString(4);

            cartListAdapter.addData(new CartItem(mNumber, mName, CostChange.changCost(mCount), CostChange.changCost(mCost), mImgUrl));
            totalCost += mCount;
            count++;
        }

        txtDrinkCount.setText("음료 " + count);

        Cursor cursorSIde = mSQLite.selectSide();
        cartListSideAdapter.clear();

        count = 0;
        while (cursorSIde.moveToNext()) {
            mNumber = cursor.getInt(0);
            mName = cursor.getString(1);
            mCost = cursor.getInt(2);
            mCount = cursor.getInt(3);
            mImgUrl = cursor.getString(4);

            cartListSideAdapter.addData(new CartItem(mNumber, mName, CostChange.changCost(mCount), CostChange.changCost(mCost), mImgUrl));
            totalCost += mCount;
            count++;
        }

        txtSideCount.setText("사이드메뉴 " + count);
        txtTotalPrice.setText(CostChange.changCost(totalCost));
    }

    /**
     * component initialization
     */
    void initialization() {
        rcvDrink = (RecyclerView) findViewById(R.id.rcv_cart_drink);
        rcvDrink.setHasFixedSize(true);
        rcvDrink.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        rcvSide = (RecyclerView) findViewById(R.id.rcv_cart_side);
        rcvSide.setHasFixedSize(true);
        rcvSide.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        btnCartClear = (Button) findViewById(R.id.btnCartCencel);
        btnCartPayment = (Button) findViewById(R.id.btnCartPayment);

        txtDrinkCount = (TextView) findViewById(R.id.txtCartDrinkCount);
        txtSideCount = (TextView) findViewById(R.id.txtCartSideCount);
        txtTotalPrice = (TextView) findViewById(R.id.txtCartTotalCost);

        imgToolbarBack = (ImageView) findViewById(R.id.imgToolbarBackBack);

        toolbar = (Toolbar) findViewById(R.id.toolbarBack);
        toolbar.setContentInsetsAbsolute(0, 0);

        cartItemArrayList = new ArrayList<>();
        mSQLite = new SQLite(getApplicationContext(), "menu.db", null, 1);
    }

    /**
     * recycler view listener
     */
    void listener() {

        // drink
        rcvDrink.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getApplicationContext(), rcvDrink, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "click");
                // intent detail menu
            }

            @Override
            public void onItemLongClick(View v, final int position) {
                Log.d(TAG, "long click");
                dialog(position, 0);
            }
        }));

        // side menu
        rcvSide.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getApplicationContext(), rcvSide, new RecyclerViewOnItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "click");
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "long click");
                dialog(position, 1);
            }
        }));

        /**
         * 클리어
         */
        btnCartClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        /**
         * 장바구니 결제
         */
        btnCartPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 장바구니에 담은 것들을 한번에 결제 (서버처리)

                String menu = "";
                String count = "";

                // 사이드 메뉴 처리
                for (int i = 0; i < cartListSideAdapter.getItemCount(); i++) {
                    for (int j = 0; j < Integer.parseInt(cartListSideAdapter.getItems().get(i).getcCount().trim()); j++) {
                        setAutoStockManagement(cartListSideAdapter.getItems().get(i).getcName());
                    }
                    menu += cartListSideAdapter.getItems().get(i).getcName() + "|";
                    count += cartListSideAdapter.getItems().get(i).getcCount() + "|";
                }

                // 음료 리스트 처리
                for (int i = 0; i < cartListAdapter.getItemCount(); i++) {
                    for (int j = 0; j < Integer.parseInt(cartListAdapter.getItems().get(i).getcCount().trim()); j++) {
                        setAutoStockManagement(cartListAdapter.getItems().get(i).getcName());
                    }
                    menu += cartListAdapter.getItems().get(i).getcName() + "|";
                    count += cartListAdapter.getItems().get(i).getcCount() + "|";
                }
                clear();

                setOrderList(SaveDataSession.getAppPreferences(getApplicationContext(), "UserId"), menu, count, totalCost/100+"");
                Toast.makeText(getApplicationContext(), "결제되었습니다.", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(), NicePayDemoActivity.class));
            }
        });

        imgToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * show alert dialog
     * separate : 0 -> drink
     * separate : 1 -> side
     *
     * @param position
     * @param separate
     */
    void dialog(final int position, final int separate) {
        aldB = new AlertDialog.Builder(CartActivity.this);
        aldB.setMessage("삭제하시겠습니까?");
        aldB.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (separate == 0) {
                    mSQLite.delete(cartListAdapter.getItems().get(position).getcName());
                    cartListAdapter.removeData(position);
                    txtDrinkCount.setText((Integer.parseInt(txtDrinkCount.getText().toString().trim()) - 1) + "");
                } else {
                    mSQLite.deleteSide(cartListSideAdapter.getItems().get(position).getcName());
                    cartListSideAdapter.removeData(position);
                    txtSideCount.setText((Integer.parseInt(txtSideCount.getText().toString().trim()) - 1) + "");
                }
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

    void clear() {
        mSQLite.deleteAll();
        cartListAdapter.clear();
        txtTotalPrice.setText("0");
        txtDrinkCount.setText("0");
        txtSideCount.setText("0");
    }

    /**
     * 음료 이름을 파라미터로 받아 재고 관리를 하는 메서드
     *
     * @param name
     */
    void setAutoStockManagement(String name) {

        Map map = new HashMap();
        map.put("MERCHANDISE_NAME", name);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticUrl.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ConnectService connectService = retrofit.create(ConnectService.class);
        Call<RepoItem> call = connectService.setStockReflect(map);
        call.enqueue(new Callback<RepoItem>() {
            @Override
            public void onResponse(Call<RepoItem> call, Response<RepoItem> response) {

            }

            @Override
            public void onFailure(Call<RepoItem> call, Throwable t) {

            }
        });
    }

    void setOrderList(String key, String menu, String count, String totalCost) {
        Map map = new HashMap();
        map.put("id", key);
        map.put("menus", menu);
        map.put("counts", count);
        map.put("cost", totalCost);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticUrl.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ConnectService connectService = retrofit.create(ConnectService.class);
        Call<RepoItem> call = connectService.setOrder(map);
        call.enqueue(new Callback<RepoItem>() {
            @Override
            public void onResponse(Call<RepoItem> call, Response<RepoItem> response) {

            }

            @Override
            public void onFailure(Call<RepoItem> call, Throwable t) {

            }
        });
    }
}
