package com.example.activity;

import com.example.mybrowser.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity implements OnClickListener {
	private Button btn_backSetting;
	private Button btn_sure_settings;
	private EditText edit_homepage;
	private SharedPreferences sp;
	private String homepage;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activeity_settings);
		btn_backSetting = (Button) findViewById(R.id.btn_backSetting);
		btn_sure_settings = (Button) findViewById(R.id.btn_sure_settings);
		edit_homepage = (EditText) findViewById(R.id.edit_homepage);
		btn_backSetting.setOnClickListener(this);
		btn_sure_settings.setOnClickListener(this);
		sp = getSharedPreferences("settings", MODE_APPEND);
		homepage = sp.getString("homepage", "http://www.baidu.com");
		edit_homepage.setText(homepage);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_backSetting:
			finish();
			break;

		case R.id.btn_sure_settings:
			Editor editor = sp.edit();
			editor.putString("homepage", edit_homepage.getText().toString());
			editor.commit();
			Toast.makeText(SettingActivity.this, "±£´æ³É¹¦",Toast.LENGTH_SHORT).show();
			finish();
			break;
		}
	}
}
