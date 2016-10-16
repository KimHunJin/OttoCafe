package sungkyul.ac.kr.ottocafe.activities.credit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import sungkyul.ac.kr.ottocafe.R;

/**
 * Created by HunJin on 2016-09-19.
 *
 * nicepay demo activity
 */
public class NicePayDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nice_pay_demo);

        String url = "https://web.nicepay.co.kr/smart/mainPay.jsp";

        WebView webView = (WebView) findViewById(R.id.webViewCreditPage);
        webView.setWebViewClient(new WebViewClient()); // 이걸 안해주면 새창이 뜸
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setBlockNetworkLoads(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.loadUrl(url);
    }
}
