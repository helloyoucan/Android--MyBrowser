package com.example.fragment;

import java.text.DateFormat;
import java.util.Date;

import com.example.activity.HistoryActivity;
import com.example.activity.MainActivity;
import com.example.database.MyOpenHelper;
import com.example.fragment.MenuFragment.FMenuClickListener;
import com.example.mybrowser.R;

import android.R.bool;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainFragment extends Fragment implements OnClickListener,
		FMenuClickListener {
	private WebView webView;
	private TextView Text_show_url;
	private Button btn_refresh;
	private LinearLayout btn_back;
	private LinearLayout btn_next;
	private LinearLayout btn_sort;
	private LinearLayout btn_home;
	private LinearLayout btn_allwin;
	private ProgressBar bar;// 进度条
	private String homepage;
	private MyOpenHelper dbHelper;
	private SQLiteDatabase db;
	private boolean canTraceless;// 无痕模式
	private boolean hasImg;// 智能无图
	private SharedPreferences sp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		getId(view);
		// 对五个按钮和地址栏添加点击监听事件
		btn_refresh.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_sort.setOnClickListener(this);
		btn_home.setOnClickListener(this);
		Text_show_url.setOnClickListener(this);
		btn_allwin.setOnClickListener(this);
		webView.setOnClickListener(this);
		sp = ((MainActivity) getActivity()).getSharedPreferences("settings",
				((MainActivity) getActivity()).MODE_APPEND);
		homepage = sp.getString("homepage", "http://www.baidu.com");
		startReadUrl(homepage);
		dbHelper = new MyOpenHelper((MainActivity) getActivity(), null, null, 1);
		db = dbHelper.getWritableDatabase();
		SharedPreferences sp = ((MainActivity) getActivity())
				.getSharedPreferences("browser",
						((MainActivity) getActivity()).MODE_APPEND);
		canTraceless = sp.getBoolean("canTraceless", false);
		hasImg = sp.getBoolean("hasImg", false);
		return view;
	}

	private void addHistory() {
		ContentValues cValue = new ContentValues();
		if (webView.getTitle() != null && webView.getTitle() != ""
				&& webView.getUrl() != null && webView.getUrl() != "") {
			cValue.put("url", webView.getUrl());
			cValue.put("title", webView.getTitle());
			cValue.put("time",
					DateFormat.getDateInstance().format(new Date().getTime()));
			db.insert("history", null, cValue);// 插入数据
		}
	}

	private void getId(View view) {
		webView = (WebView) view.findViewById(R.id.webView);
		Text_show_url = (TextView) view.findViewById(R.id.Text_show_url);
		btn_refresh = (Button) view.findViewById(R.id.btn_refresh);
		btn_next = (LinearLayout) view.findViewById(R.id.btn_next);
		btn_back = (LinearLayout) view.findViewById(R.id.btn_back);
		btn_sort = (LinearLayout) view.findViewById(R.id.btn_sort);
		btn_home = (LinearLayout) view.findViewById(R.id.btn_home);
		btn_allwin = (LinearLayout) view.findViewById(R.id.btn_allwin);
		bar = (ProgressBar) view.findViewById(R.id.progressBar);
	}

	public void startReadUrl(String url) {
		// TODO Auto-generated method stub
		// 先停止加载当前页面
		webView.stopLoading();
		// webView加载web资源
		webView.loadUrl(url);
		// 覆盖webView默认通过系统或者第三方浏览器打开网页的行为
		// 如果为false调用系统或者第三方浏览器打开网页的行为
		WebSettings settings = webView.getSettings();
		// 启用支持javascript
		settings.setJavaScriptEnabled(true);
		// web加载页面优先使用缓存加载
		//settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
				if (hasImg && !hasLinkWifi()) {// 设置了智能无图并且没有无线
					webView.getSettings().setBlockNetworkImage(true);// 无图
				} else {
					webView.getSettings().setBlockNetworkImage(false);
				}
				view.loadUrl(url);
				return true;
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(((MainActivity) getActivity()), "加载失败，请稍候再试",
						Toast.LENGTH_SHORT).show();
			}
		});
		// 当打开页面时 显示进度
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				if (newProgress == 100) {
					// 加载完毕
					bar.setVisibility(View.INVISIBLE);
					((MainActivity) getActivity()).setUrl(webView.getUrl());
					if (!canTraceless) {// 非无痕模式
						addHistory();
					}
				} else {
					if (View.INVISIBLE == bar.getVisibility()) {
						bar.setVisibility(View.VISIBLE);
					}
					bar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				Text_show_url.setText(title);
			}
		});
	}
	// 设置点击网页标题回调接口
	public interface FMainClickListener {
		void onFed_showClick();

		void onFallwinClick();

		void onFsortClick();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_refresh:// 刷新
			webView.reload();
			break;
		case R.id.btn_back:// 返回
			if (webView.canGoBack()) {
				webView.goBack();
				Text_show_url.setText(webView.getTitle());
			} else {
				// 交给宿主Activity处理，如果它希望处理
				/*
				 * if (getActivity() instanceof FexitClickListener) {
				 * ((FexitClickListener) getActivity()).onFexitClick(); }
				 */
			}
			break;
		case R.id.btn_next:// 前进
			if (webView.canGoForward()) {
				webView.goForward();
				Text_show_url.setText(webView.getTitle());
			}
			break;
		case R.id.btn_home:
			startReadUrl(homepage);
			break;
		case R.id.btn_allwin:
			// 交给点击所有窗口的是事件宿主Activity处理
			if (getActivity() instanceof FMainClickListener) {
				((FMainClickListener) getActivity()).onFallwinClick();
			}
			break;
		case R.id.Text_show_url:
			// ((MainActivity) getActivity()).setUrl(webView.getUrl());
			// ((MainActivity) getActivity()).setBrowse(browse);
			// 交给点击网页标题宿主Activity处理
			if (getActivity() instanceof FMainClickListener) {
				((FMainClickListener) getActivity()).onFed_showClick();
			}
			break;
		case R.id.btn_sort:
			// 交给点击菜单宿主Activity处理
			// ((MainActivity) getActivity()).setBrowse(browse);
			if (getActivity() instanceof FMainClickListener) {
				((FMainClickListener) getActivity()).onFsortClick();
			}
			break;
		case R.id.webView:
			break;
		}
	}

	@Override
	public void onFbtn_closeClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFbtn_foldClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFbtn_bookmarksClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFbtn_tracelessClick() {
		// TODO Auto-generated method stub
		// 无痕模式
	}

	@Override
	public void onFbtn_settingClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFbtn_historyClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFbtn_downloadClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFbtn_addBookmarksClick() {
		// TODO Auto-generated method stub
		// 添加到书签
	}

	public boolean isCanTraceless() {
		return canTraceless;
	}

	public void setCanTraceless(boolean canTraceless) {
		this.canTraceless = canTraceless;
	}

	@Override
	public void onFbtn_HasImg() {
		// TODO Auto-generated method stub

	}

	public boolean isHasImg() {
		return hasImg;
	}

	public void setHasImg(boolean hasImg) {
		this.hasImg = hasImg;
	}

	public boolean hasLinkWifi() {
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) ((MainActivity) getActivity())
				.getSystemService(((MainActivity) getActivity()).WIFI_SERVICE);
		// 判断wifi是否开启
		if (wifiManager.isWifiEnabled()) {
			return true;
		} else {
			return false;
		}
	}
}
