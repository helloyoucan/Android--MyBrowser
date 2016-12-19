package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.database.MyOpenHelper;
import com.example.mybrowser.R;

public class HistoryActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private ListView listview;
	private MyOpenHelper dbHelper;
	private SQLiteDatabase db;
	private Button btn_back;
	private Button btn_clearAll;
	private Cursor cr;
	private String url;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		listview = (ListView) findViewById(R.id.listView_his);
		btn_back = (Button) findViewById(R.id.btn_backMain);
		btn_clearAll = (Button) findViewById(R.id.btn_clear);
		btn_back.setOnClickListener(this);
		btn_clearAll.setOnClickListener(this);
		listview.setOnItemClickListener(this);
		dbHelper = new MyOpenHelper(HistoryActivity.this, null, null, 1);
		db = dbHelper.getWritableDatabase();
		cr = db.query("history", null, null, null, null, null, null);
		inflateList(cr);
	}

	private void inflateList(Cursor cursor) {
		SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
				HistoryActivity.this, R.layout.line, cursor, new String[] {
						"title", "url", "time" }, new int[] { R.id.URLtitle,
						R.id.URL, R.id.URLTime },
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		listview.setAdapter(simpleCursorAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_backMain:
			db.close();
			finish();
			break;

		case R.id.btn_clear:
			db.execSQL("DELETE FROM history");
			listview.setAdapter(null);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		url = cr.getString(cr.getColumnIndex("url"));
		// Toast.makeText(HistoryActivity.this,
		// listItem.get(arg2).get("ItemTitle").toString(),
		// Toast.LENGTH_LONG).show();
		Intent intent = getIntent();
		Bundle bundle = new Bundle();
		bundle.putString("historyUrl", url);
		intent.putExtras(bundle);
		setResult(0x717, intent);
		//Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
		db.close();
		finish();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
