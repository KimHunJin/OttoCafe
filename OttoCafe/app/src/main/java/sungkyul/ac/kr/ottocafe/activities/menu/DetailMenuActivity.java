package sungkyul.ac.kr.ottocafe.activities.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sungkyul.ac.kr.ottocafe.R;
import sungkyul.ac.kr.ottocafe.activities.credit.NicePayDemoActivity;
import sungkyul.ac.kr.ottocafe.sql.SQLite;
import sungkyul.ac.kr.ottocafe.repo.ConnectService;
import sungkyul.ac.kr.ottocafe.repo.RepoItem;
import sungkyul.ac.kr.ottocafe.utils.StaticUrl;

/**
 * Created by HunJin on 2016-09-17.
 * <p>
 * detail form of menuu
 */
public class DetailMenuActivity extends AppCompatActivity {

    final String TAG = "DetailMenuActivity";

    private FloatingActionButton fbtPay, fbtAdd;
    private TextView txtName, txtDescription;
    private ImageView imgMenu;
    private Spinner spnSize, spnNumber;

    private int price, upPrice;
    private String size;
    private int cost;
    private int number;
    private String imgPath;

    String[] sizes = new String[]{"Small", "Middle", "Large"};
    String[] numbers = new String[]{"1", "2", "3", "4", "5", "6"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);
        initialization();
        settingDetailPage();
        btnListener();
        spnClickListener();
    }

    /**
     * 버튼 클릭 리스너
     */
    void btnListener() {
        fbtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite sqlite = new SQLite(getApplicationContext(), "menu.db", null, 1);
                cost = upPrice * number;
                sqlite.insert(txtName.getText().toString(), number, cost, imgPath);
                sqlite.close();
                // 장바구니 추가
            }
        });

        fbtPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 결제
                // 결제 했을 때 서버에 재고 수량 반영
                setAutoStockManagement(txtName.getText().toString().trim());
                startActivity(new Intent(getApplicationContext(), NicePayDemoActivity.class));

            }
        });
    }


    /**
     * 초기화
     */
    void initialization() {
        txtName = (TextView) findViewById(R.id.txtDetailMenuName);
        txtDescription = (TextView) findViewById(R.id.txtDetailMenuComment);
        fbtPay = (FloatingActionButton) findViewById(R.id.fbtMenuPay);
        fbtAdd = (FloatingActionButton) findViewById(R.id.fbtMenuAdd);
        imgMenu = (ImageView) findViewById(R.id.imgDetailMenu);
        spnSize = (Spinner) findViewById(R.id.spnSIze);
        spnNumber = (Spinner) findViewById(R.id.spnNumber);
        spinnerSetting();

    }

    /**
     * 스피너 세팅 - 클릭 시 변수 size와 변수 number에 값 저장.
     * spnSize default value = small
     * spnNumber default value = number
     */
    void spinnerSetting() {
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sizes);
        spnSize.setAdapter(sizeAdapter);

        ArrayAdapter<String> numberAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, numbers);
        spnNumber.setAdapter(numberAdapter);
    }

    /**
     * 스피너 클릭 리스너
     */
    void spnClickListener() {
        spnSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                size = sizes[position];
                switch (position) {
                    case 0: {
                        upPrice = price;
                        break;
                    }
                    case 1: {
                        upPrice = price + 500;
                        break;
                    }
                    case 2: {
                        upPrice = price + 1000;
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                number = Integer.parseInt(numbers[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * 음료 이름을 파라미터로 받아 재고 관리를 하는 메서드
     *
     * @param name
     */
    void setAutoStockManagement(String name) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(StaticUrl.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Map map = new HashMap();
        map.put("MERCHANDISE_NAME", name);

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

    /**
     * CoffeeKey
     * CoffeeName
     * CoffeeImage
     */
    void settingDetailPage() {
        Intent it = getIntent();
//        long key = it.getExtras().getLong("CoffeeKey");
        price = Integer.parseInt(it.getExtras().getString("CoffeePrice"));
        imgPath = it.getExtras().getString("CoffeeImage");
        // key를 이용하여 정보를 가져온다. (retrofit 사용하 예정)
        txtName.setText(it.getExtras().getString("CoffeeName"));
        Picasso.with(getApplicationContext()).load(it.getExtras().getString("CoffeeImage")).into(imgMenu);
    }
}
