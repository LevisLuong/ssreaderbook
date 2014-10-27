package vn.seasoft.readerbook;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.holoeverywhere.app.Activity;

/**
 * User: XuanTrung
 * Date: 10/3/2014
 * Time: 10:25 AM
 */
public class actTutorial extends Activity {
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        wv = (WebView) findViewById(R.id.webview);
        wv.clearCache(true);
        wv.clearHistory();
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setDefaultTextEncodingName("utf-8");
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return true;
            }

            public void onPageFinished(WebView view, String url) {
                //Toast.makeText(myActivity.this, "Oh no!", Toast.LENGTH_SHORT).show();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                String summary = "<html><body><strong>" + getString(R.string.error_connect) + "</strong></body></html>";
                wv.loadData(summary, "text/html; charset=utf-8", "utf-8");
            }

        }); //End WebViewClient
        wv.loadUrl("http://seasoft.vn/SachCuaTui/Help.html");

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
