package cn.cbg.testwebview;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.net.*;
import android.net.http.*;
import android.os.*;
import android.provider.*;
import android.view.*;
import android.webkit.*;
import android.widget.*;
import android.view.ContextMenu.*;
import android.view.View.*;
import java.security.acl.*;
import android.view.ViewGroup.*;
import android.widget.FrameLayout.*;

public class MainActivity extends Activity
{

    private WebView webView;
    private FrameLayout fullVideo;
    private View customView = null;
    private ProgressBar progressBar;

    private String VURL;
    private String VVURL1;
    private String VVURL;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout mLayout=(FrameLayout) findViewById(R.id.fraview1);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //FrameLayout mLayout = new FrameLayout(getApplicationContext());
		//mLayout.setLayoutParams(params);
        //requestWindowFeature(Window.FEATURE_OPTIONS_PANEL|Window.FEATURE_RIGHT_ICON|Window.FEATURE_CONTEXT_MENU|Window.FEATURE_ACTION_BAR|Window.FEATURE_OPTIONS_PANEL);
		//setContentView(mLayout);

        /*WebView*/
        FrameLayout.LayoutParams paWv = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView(getApplicationContext());
		webView.setLayoutParams(paWv);
		//FrameLayout mLayout=(FrameLayout)findViewById ( R.id.fraview );
		mLayout.addView(webView);




        ImageButton mBt=(ImageButton) findViewById(R.id.mButton);

        /*
         Button mBt = new Button(getApplicationContext());
         mBt.setLayoutParams(params);
         FrameLayout.LayoutParams setBt=(FrameLayout.LayoutParams) mBt.getLayoutParams();
         setBt.height=30;
         setBt.width=50;


         mBt.setLayoutParams(setBt);


         mLayout.addView(mBt);

         */


        mBt.setOnClickListener(new MyListener());



        fullVideo = new FrameLayout(getApplicationContext());
		fullVideo.setLayoutParams(params);
		FrameLayout fLayout=(FrameLayout) findViewById(R.id.fraview);
		fLayout.addView(fullVideo);


        /*
         progressBar = new ProgressBar(getApplicationContext());
         progressBar.setLayoutParams(params);
         FrameLayout.LayoutParams paPr = (FrameLayout.LayoutParams) progressBar.getLayoutParams();
         paPr.width = 100;
         paPr.height = 100;
         progressBar.setLayoutParams(paPr);
         //FrameLayout fLayout=(FrameLayout)findViewById ( R.id.fraview );
         fLayout.addView(progressBar);

         */
        //mWebView=new WebView(this);
		//webView = (WebView) findViewById ( R.id.webView );
        //fullVideo = (FrameLayout) findViewById ( R.id.full_video );
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
		VURL = "https://www.360kan.com";
		VVURL = "http://tv.wandhi.com/go.html?url=";
        VVURL1 = "http://q.z.vip.totv.72du.com/?url=";
        webView.loadUrl(VURL);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
		webView.setDownloadListener(new FileDownLoadListener());


        setWebView();
    }


    private boolean isDestroyed = false;
    private void destroy()
    {
        if (isDestroyed)
        {
            return;
        }
        // 回收资源
        isDestroyed = true;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (isFinishing())
        {
            //onDestroy();
            destroy();
        }
    }
    /*
     @Override
     public void onDestroy() {
     destroy();//需要在onDestroy方法中进一步检测是否回收资源等。
     }
     */ 

	@Override
	protected void onDestroy()
	{
        if (webView != null)
		{
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);

			// 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
			// destory()
			((ViewGroup) fullVideo.getParent()).removeView(fullVideo);

			ViewParent parent = webView.getParent();
			if (parent != null)
			{
                ((ViewGroup) parent).removeView(webView);
            }
			((ViewGroup) progressBar.getParent()).removeView(progressBar);

			webView.stopLoading();
			// 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
			webView.getSettings().setJavaScriptEnabled(false);
			webView.clearHistory();
			webView.clearView();
			webView.removeAllViews();
			webView.destroy();
            
        }
		super.onDestroy();
    }



    // 在 Actvity 中监听返回键按钮
    @Override
    public void onBackPressed()
	{
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();

    }



    private void setWebView()
	{
        WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
		webSettings.setAppCacheEnabled(true); //启用应用缓存
		webSettings.setDomStorageEnabled(true); //启用或禁用DOM缓存。
		webSettings.setDatabaseEnabled(true); //启用或禁用DOM缓存。
		//if (.isNetworkConnected()) { //判断是否联网
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); //默认的缓存使用模式

		//webSettings.setCacheMode(WebSettings.LOAD_CACHE_ONLY); //不从网络加载数据，只从缓存加载数据。

        //开启DOM storage API功能（HTML// 提供的一种标准的接口，主要将键值对存储在本地，在页面加载完毕后可以通过 JavaScript 来操作这些数据。）
		//webSettings.setDomStorageEnabled(true);
        //设置数据库缓存路径
		//	webSettings.setDatabasePath(cacheDirPath);
        //设置Application Caches缓存目录
		//webSettings.setAppCachePath(cacheDirPath);
        //设置默认编码
		webSettings.setDefaultTextEncodingName("utf-8");
        //将图片调整到适合webview的大小
		webSettings.setUseWideViewPort(false);
        //支持缩放
		webSettings.setSupportZoom(true);
        //支持内容重新布局
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //多窗口
		webSettings.supportMultipleWindows();
        //设置可以访问文件
		webSettings.setAllowFileAccess(true);
        //当webview调用requestFocus时为webview设置节点
		webSettings.setNeedInitialFocus(true);
        //设置支持缩放
		webSettings.setBuiltInZoomControls(true);
		//不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        //支持通过JS打开新窗口
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //缩放至屏幕的大小
		webSettings.setLoadWithOverviewMode(true);
        //支持自动加载图片
		webSettings.setLoadsImagesAutomatically(true);


    }

    private class MyWebChromeClient extends WebChromeClient
	{

        @Override
        public void onHideCustomView()
		{

            if (customView == null)
			{
                return;
            }
            fullVideo.removeView(customView);
            fullVideo.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//清除全屏

        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback)
		{
            try
            {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
            }
            catch (Exception o)
            {}
            customView = view;
            fullVideo.setVisibility(View.VISIBLE);
            fullVideo.addView(customView);
            fullVideo.bringToFront();
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        }

    }


    private class MyWebViewClient extends WebViewClient
	{

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }
		@Override 
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
		{ 
            handler.proceed(); //表示等待证书响应
            // handler.cancel(); //表示挂起连接，为默认方式
// handler.handleMessage(null); //可做其他处理

        }

        @Override
        public void onPageFinished(WebView view, String url)
		{
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

	/**
     * 当下载文件时打开系统自带的浏览器进行下载，当然也可以对捕获到的 url 进行处理在应用内下载。
	 **/
    private class FileDownLoadListener implements DownloadListener
	{
        @Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength)
		{
            Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
        }
    }




	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu)
	{
        // TODO: Implement this method
		menu.add(1, 1, 1, "玩的嗨");
        menu.add(5, 5, 5, "Via");
        menu.add(6, 6, 6, "vip");
		menu.add(2, 2, 2, "刷新");
		menu.add(3, 3, 3, "主页");
		menu.add(4, 4, 4, "退出");
		return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
        // TODO: Implement this method
		if (item.getItemId() == 1)
		{
            String url=VVURL + webView.getUrl();
            webView.loadUrl(url);

            /*
             Intent intent = new Intent ( Intent.ACTION_VIEW );  
             String type = "video/mp4";  


             Uri name = Uri.parse ( url );  
             intent.setDataAndType ( name, type );  
             intent.setClassName ( "mark.via.gp", "mark.via.ui.activity.BrowserActivity" );  
             intent.putExtra ( MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );  
             startActivity ( intent ); 
             */
        }

        if (item.getItemId() == 5)
        {
            String url=VVURL + webView.getUrl();

            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        }


        if (item.getItemId() == 6)
        {
            String url=VVURL1 + webView.getUrl();
            webView.loadUrl(url);
        }

		if (item.getItemId() == 2)
		{
            webView.loadUrl(webView.getUrl());
        }
		if (item.getItemId() == 3)
        {webView.loadUrl(VURL);}
		if (item.getItemId() == 4)
		{
            webView = null;
			onDestroy();
            destroy();
			finish();

        }
		return false;
    }
    /*
     private void sendKeyCode1(int keyCode)
     {
     try
     {
     String keyCommand = "input keyevent " + keyCode;
     // 调用Runtime模拟按键操作
     Runtime.getRuntime().exec(keyCommand);
     }
     catch (Exception e)
     {
     e.printStackTrace();
     }
     }*/

    /**
     * 使用Instrumentation接口：对于非自行编译的安卓系统，无法获取系统签名，只能在前台模拟按键，不能后台模拟
     * 注意:调用Instrumentation的sendKeyDownUpSync方法必须另起一个线程，否则无效
     * @param keyCode 按键事件(KeyEvent)的按键值
     */
    private void sendKeyCode2(final int keyCode)
    {
        new Thread(new Runnable() {
                @Override
                public void run()
                {
                    try
                    {
                        // 创建一个Instrumentation对象
                        Instrumentation inst = new Instrumentation();
                        // 调用inst对象的按键模拟方法
                        inst.sendKeyDownUpSync(keyCode);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }).start();
    }
    private class MyListener implements OnClickListener
    {

        @Override
        public void onClick(View p1)
        {
            // TODO: Implement this method


            sendKeyCode2(82);


        }

    }
}
