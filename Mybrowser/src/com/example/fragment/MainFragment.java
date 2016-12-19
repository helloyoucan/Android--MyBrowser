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
	private ProgressBar bar;// ������
	private String homepage;
	private MyOpenHelper dbHelper;
	private SQLiteDatabase db;
	private boolean canTraceless;// �޺�ģʽ
	private boolean hasImg;// ������ͼ
	private SharedPreferences sp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main, container, false);
		getId(view);
		// �������ť�͵�ַ����ӵ�������¼�
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
			db.insert("history", null, cValue);// ��������
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
		// ��ֹͣ���ص�ǰҳ��
		webView.stopLoading();
		// webView����web��Դ
		webView.loadUrl(url);
		// ����webViewĬ��ͨ��ϵͳ���ߵ��������������ҳ����Ϊ
		// ���Ϊfalse����ϵͳ���ߵ��������������ҳ����Ϊ
		WebSettings settings = webView.getSettings();
		// ����֧��javascript
		settings.setJavaScriptEnabled(true);
		// web����ҳ������ʹ�û������
		//settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// ʹ���Լ���WebView�������ӦUrl�����¼���������ʹ��Ĭ�������������ҳ��
				if (hasImg && !hasLinkWifi()) {// ������������ͼ����û������
					webView.getSettings().setBlockNetworkImage(true);// ��ͼ
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
				Toast.makeText(((MainActivity) getActivity()), "����ʧ�ܣ����Ժ�����",
						Toast.LENGTH_SHORT).show();
			}
		});
		// ����ҳ��ʱ ��ʾ����
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				if (newProgress == 100) {
					// �������
					bar.setVisibility(View.INVISIBLE);
					((MainActivity) getActivity()).setUrl(webView.getUrl());
					if (!canTraceless) {// ���޺�ģʽ
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
	// ���õ����ҳ����ص��ӿ�
	public interface FMainClickListener {
		void onFed_showClick();

		void onFallwinClick();

		void onFsortClick();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_refresh:// ˢ��
			webView.reload();
			break;
		case R.id.btn_back:// ����
			if (webView.canGoBack()) {
				webView.goBack();
				Text_show_url.setText(webView.getTitle());
			} else {
				// ��������Activity���������ϣ������
				/*
				 * if (getActivity() instanceof FexitClickListener) {
				 * ((FexitClickListener) getActivity()).onFexitClick(); }
				 */
			}
			break;
		case R.id.btn_next:// ǰ��
			if (webView.canGoForward()) {
				webView.goForward();
				Text_show_url.setText(webView.getTitle());
			}
			break;
		case R.id.btn_home:
			startReadUrl(homepage);
			break;
		case R.id.btn_allwin:
			// ����������д��ڵ����¼�����Activity����
			if (getActivity() instanceof FMainClickListener) {
				((FMainClickListener) getActivity()).onFallwinClick();
			}
			break;
		case R.id.Text_show_url:
			// ((MainActivity) getActivity()).setUrl(webView.getUrl());
			// ((MainActivity) getActivity()).setBrowse(browse);
			// ���������ҳ��������Activity����
			if (getActivity() instanceof FMainClickListener) {
				((FMainClickListener) getActivity()).onFed_showClick();
			}
			break;
		case R.id.btn_sort:
			// ��������˵�����Activity����
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
		// �޺�ģʽ
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
		// ��ӵ���ǩ
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
		// ��ȡwifi����
		WifiManager wifiManager = (WifiManager) ((MainActivity) getActivity())
				.getSystemService(((MainActivity) getActivity()).WIFI_SERVICE);
		// �ж�wifi�Ƿ���
		if (wifiManager.isWifiEnabled()) {
			return true;
		} else {
			return false;
		}
	}
}
