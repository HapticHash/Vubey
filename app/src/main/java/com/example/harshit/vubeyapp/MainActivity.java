package com.example.harshit.vubeyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.R.attr.description;

public class MainActivity extends Activity {
    private Context c;
    private boolean isConnected = true;
    final String offlineMessageHtml = "Please CONNECT to INTERNET";
    final String timeoutMessageHtml = "Sorry... Connection Timeout";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c = this;

        ConnectivityManager connectivityManager = (ConnectivityManager)
                c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            if (ni.getState() != NetworkInfo.State.CONNECTED) {
                // record the fact that there is not connection
                isConnected = false;
            }
        }
        WebView browser = (WebView)findViewById(R.id.webView);
        browser.setNetworkAvailable(isConnected);

        browser.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (isConnected) {
                    // return false to let the WebView handle the URL
                    return false;
                } else {
                    // show the proper "not connected" message
                    view.loadData(offlineMessageHtml, "text/html", "utf-8");
                    // return true if the host application wants to leave the current
                    // WebView and handle the url itself
                    return true;
                }
            }
            @Override
            public void onReceivedError (WebView view, int errorCode,
                                         String description, String failingUrl) {
                if (errorCode == ERROR_TIMEOUT) {
                    view.stopLoading();  // may not be needed
                    view.loadData(timeoutMessageHtml, "text/html", "utf-8");
                }
            }
        });

        WebSettings webSettings = browser.getSettings();

        webSettings.setJavaScriptEnabled(true);
        browser.loadUrl("https://vubey.yt/");
        browser.setHorizontalScrollBarEnabled(false);
        browser.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        browser.setBackgroundColor(Color.rgb(240, 128, 128));

        browser.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url) {

                //hide loading image
                findViewById(R.id.imageView1).setVisibility(View.GONE);
                findViewById(R.id.progressBar1).setVisibility(View.GONE);
                findViewById(R.id.textView2).setVisibility(View.GONE);
                //show webview
                findViewById(R.id.webView).setVisibility(View.VISIBLE);
            }
        });

        browser.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);

            }
        });

    }

}
