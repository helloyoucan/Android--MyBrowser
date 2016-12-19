package com.example.activity;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.database.MyOpenHelper;
import com.example.fragment.EditUrlFragment;
import com.example.fragment.EditUrlFragment.Fbtn_goClickListener;
import com.example.fragment.MainFragment;
import com.example.fragment.MainFragment.FMainClickListener;
import com.example.fragment.MenuFragment;
import com.example.fragment.MenuFragment.FMenuClickListener;
import com.example.mybrowser.R;

public class MainActivity extends Activity implements Fbtn_goClickListener,
		FMainClickListener, FMenuClickListener {
	private MainFragment mainF;
	private EditUrlFragment editF;
	private MenuFragment menuF;
	private String url;
	private SQLiteDatabase db;
	private MyOpenHelper dbHelper;
	private WebView webView;
	final int CODE = 0x717;// 定义一个请求码常量

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = new MyOpenHelper(MainActivity.this, null, null, 1);
		db = dbHelper.getWritableDatabase();
		
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			mainF = new MainFragment();
			FragmentManager fm = getFragmentManager();
			FragmentTransaction tx = fm.beginTransaction();
			tx.add(R.id.id_content, mainF, "mainF");
			tx.commit();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CODE && resultCode == CODE) {// 点击历史记录的链接后跳到该页面
			Bundle bundle = data.getExtras();
			// Toast.makeText(MainActivity.this, bundle.getString("historyUrl"),
			// Toast.LENGTH_LONG).show();
			mainF.startReadUrl(bundle.getString("historyUrl"));
		}
	};

	public void onFed_showClick() {
		// 切换到Editfragment
		// if (editF == null) {
		editF = new EditUrlFragment();
		editF.setfBtn_goClickListener(this);// 回调函数
		// }

		FragmentManager fm = getFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		// tx.replace(R.id.id_content, editF, "editF");
		tx.add(R.id.id_content, editF, "editF");
		tx.addToBackStack(null);
		tx.commit();
	}

	public void onFsortClick() {
		// TODO Auto-generated method stub
		// 切换到Menufragment
		if (menuF == null) {
			menuF = new MenuFragment();
			menuF.setfBtn_closeClickListener(this);
		}
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		// tx.replace(R.id.id_content, menuF, "menuF");
		tx.add(R.id.id_content, menuF, "menuF");
		tx.addToBackStack(null);
		tx.commit();
	}

	public void onFallwinClick() {
		// TODO Auto-generated method stub
		// 点击所有窗口
		Toast.makeText(MainActivity.this, "所有窗口", Toast.LENGTH_SHORT).show();
	}

	public void onFbtn_goClick() {
		// 从editF切换mainfragment
		if (mainF == null) {
			mainF = new MainFragment();
		}
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		// url = getUrl();
		if (url != null && url != "") {
			if (url.indexOf("http://") < 0 && url.indexOf("https://") < 0) {
				url = "http://" + url;
			}
			url = url.replace(" ", "");// 去掉空格
			mainF.startReadUrl(url);
		}
		tx.remove(editF);
		// tx.add(R.id.id_content, mainF, "mainF");
		// tx.addToBackStack(null);
		tx.commit();

	}

	@Override
	public void onFinput_bgClick() {
		// TODO Auto-generated method stub
		// 在输入url时点击空白事件
		if (mainF == null) {
			mainF = new MainFragment();
		}
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		tx.remove(editF);
		tx.commit();
	}

	@Override
	public void onFbtn_closeClick() {
		// TODO Auto-generated method stub
		// 点击关闭
		this.finish();
	}

	@Override
	public void onFbtn_foldClick() {
		// TODO Auto-generated method stub
		if (mainF == null) {
			mainF = new MainFragment();
		}
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		// tx.add(R.id.id_content, mFOne, "ONE");
		tx.remove(menuF);
		// tx.replace(R.id.id_content, mainF, "mainF");
		// tx.addToBackStack(null);
		tx.commit();
	}

	@Override
	public void onFbtn_bookmarksClick() {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, BookMarksActivity.class);
		// startActivity(intent);
		startActivityForResult(intent, CODE);
		if (mainF == null) {
			mainF = new MainFragment();
		}
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		tx.remove(menuF);
		tx.addToBackStack(null);
		tx.commit();
		// TODO Auto-generated method stub
		Toast.makeText(MainActivity.this, "书签", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onFbtn_tracelessClick() {
		// TODO Auto-generated method stub
		// 无痕模式
		mainF.setCanTraceless(!mainF.isCanTraceless());
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		tx.remove(menuF);
		tx.commit();
	}

	@Override
	public void onFbtn_settingClick() {
		// TODO Auto-generated method stub
		//Toast.makeText(MainActivity.this, "设置", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, SettingActivity.class);
		startActivity(intent);
		if (mainF == null) {
			mainF = new MainFragment();
		}
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		tx.remove(menuF);
		tx.addToBackStack(null);
		tx.commit();
	}

	@Override
	public void onFbtn_historyClick() {
		// TODO Auto-generated method stub
		// Toast.makeText(MainActivity.this, "历史记录", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, HistoryActivity.class);
		// startActivity(intent);
		startActivityForResult(intent, CODE);
		if (mainF == null) {
			mainF = new MainFragment();
		}
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		tx.remove(menuF);
		tx.addToBackStack(null);
		tx.commit();
	}

	@Override
	public void onFbtn_downloadClick() {
		// TODO Auto-generated method stub
		Toast.makeText(MainActivity.this, "下载", Toast.LENGTH_SHORT).show();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void onFbtn_addBookmarksClick() {
		// TODO Auto-generated method stub
		// 添加书签
		webView = (WebView)this.findViewById(R.id.webView);
		ContentValues cValue = new ContentValues();
		if (webView.getTitle() != null && webView.getTitle() != ""
				&& webView.getUrl() != null && webView.getUrl() != "") {
			cValue.put("url", webView.getUrl());
			cValue.put("title", webView.getTitle());
			cValue.put("time",
					DateFormat.getDateInstance().format(new Date().getTime()));
			db.insert(MyOpenHelper.TABLE_BM, null, cValue);// 插入数据
		}
		Toast.makeText(MainActivity.this, "添加书签", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onFbtn_HasImg() {// 智能无图
		// TODO Auto-generated method stub
		mainF.setHasImg(!mainF.isHasImg());
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		tx.remove(menuF);
		tx.commit();
	}

}