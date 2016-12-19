package com.example.fragment;

import com.example.activity.MainActivity;
import com.example.mybrowser.R;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MenuFragment extends Fragment implements OnClickListener {

	private FMenuClickListener fMenuClickListener;
	private LinearLayout btn_fold;
	private LinearLayout btn_close;
	private LinearLayout btn_history;
	private LinearLayout btn_setting;
	private LinearLayout btn_bookmarks;
	private LinearLayout btn_traceless;
	private LinearLayout btn_download;
	private LinearLayout bg_sort;
	private LinearLayout btn_add_bookmarks;
	private ImageView imageViewtraceless;
	private LinearLayout btn_hasImg;
	private ImageView imageViewHasImg;
	private boolean canTraceless;// 无痕模式
	private boolean hasImg;// 智能无图
	private SharedPreferences sp;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menu, container, false);
		getId(view);
		btn_fold.setOnClickListener(this);
		btn_close.setOnClickListener(this);
		btn_history.setOnClickListener(this);
		btn_setting.setOnClickListener(this);
		btn_bookmarks.setOnClickListener(this);
		btn_traceless.setOnClickListener(this);
		btn_download.setOnClickListener(this);
		bg_sort.setOnClickListener(this);
		btn_add_bookmarks.setOnClickListener(this);
		btn_hasImg.setOnClickListener(this);
		sp = ((MainActivity) getActivity()).getSharedPreferences("browser",
				((MainActivity) getActivity()).MODE_APPEND);
		canTraceless = sp.getBoolean("canTraceless", false);
		if (canTraceless) {// 开启无痕模式了
			imageViewtraceless.setBackgroundResource(R.drawable.traceless_open);
			btn_traceless.setBackgroundColor(Color.parseColor("#cccccc"));
		} else {
			imageViewtraceless
					.setBackgroundResource(R.drawable.traceless_close);
			btn_traceless.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		hasImg = sp.getBoolean("hasImg", false);
		if (hasImg) {// 开启智能无图了
			imageViewHasImg.setBackgroundResource(R.drawable.no_img);
			btn_hasImg.setBackgroundColor(Color.parseColor("#cccccc"));
		} else {
			imageViewHasImg.setBackgroundResource(R.drawable.has_img);
			btn_hasImg.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		return view;
	}

	public void getId(View view) {
		btn_fold = (LinearLayout) view.findViewById(R.id.btn_fold);
		btn_close = (LinearLayout) view.findViewById(R.id.btn_close);
		btn_history = (LinearLayout) view.findViewById(R.id.btn_history);
		btn_setting = (LinearLayout) view.findViewById(R.id.btn_settings);
		btn_bookmarks = (LinearLayout) view.findViewById(R.id.btn_bookmarks);
		btn_traceless = (LinearLayout) view.findViewById(R.id.btn_traceless);
		btn_download = (LinearLayout) view.findViewById(R.id.btn_download);
		bg_sort = (LinearLayout) view.findViewById(R.id.bg_sort);
		btn_add_bookmarks = (LinearLayout) view
				.findViewById(R.id.btn_add_bookmarks);
		imageViewtraceless = (ImageView) view
				.findViewById(R.id.imageViewtraceless);
		btn_hasImg = (LinearLayout) view.findViewById(R.id.btn_hasImg);
		imageViewHasImg = (ImageView) view.findViewById(R.id.imageViewHasImg);
	}

	public interface FMenuClickListener {
		void onFbtn_closeClick();

		void onFbtn_foldClick();

		void onFbtn_bookmarksClick();

		void onFbtn_tracelessClick();

		void onFbtn_settingClick();

		void onFbtn_historyClick();

		void onFbtn_downloadClick();

		void onFbtn_addBookmarksClick();

		void onFbtn_HasImg();
	}

	// 设置回调接口
	public void setfBtn_closeClickListener(FMenuClickListener fMenuClickListener) {
		this.fMenuClickListener = fMenuClickListener;
	}

	@Override
	public void onClick(View v) {
		Editor editor = sp.edit();
		if (fMenuClickListener != null) {
			switch (v.getId()) {
			case R.id.btn_close:
				fMenuClickListener.onFbtn_closeClick();
				break;
			case R.id.btn_fold:
				fMenuClickListener.onFbtn_foldClick();
				break;
			case R.id.bg_sort:
				fMenuClickListener.onFbtn_foldClick();
				break;
			case R.id.btn_settings:
				fMenuClickListener.onFbtn_settingClick();
				break;
			case R.id.btn_history:
				fMenuClickListener.onFbtn_historyClick();
				break;
			case R.id.btn_traceless:
				// 无痕模式
				if (!canTraceless) {// 开启无痕模式了
					imageViewtraceless
							.setBackgroundResource(R.drawable.traceless_open);
					editor.putBoolean("canTraceless", true);
					canTraceless = true;
					Toast.makeText(((MainActivity) getActivity()), "无痕模式开启",
							Toast.LENGTH_SHORT).show();
					btn_traceless.setBackgroundColor(Color
							.parseColor("#cccccc"));
					editor.commit();
				} else {
					imageViewtraceless
							.setBackgroundResource(R.drawable.traceless_close);
					editor.putBoolean("canTraceless", false);
					canTraceless = false;
					Toast.makeText(((MainActivity) getActivity()), "无痕模式关闭",
							Toast.LENGTH_SHORT).show();
					btn_traceless.setBackgroundColor(Color
							.parseColor("#ffffff"));
					editor.commit();
				}
				fMenuClickListener.onFbtn_tracelessClick();
				break;
			case R.id.btn_bookmarks:
				fMenuClickListener.onFbtn_bookmarksClick();
				break;
			case R.id.btn_download:
				fMenuClickListener.onFbtn_downloadClick();
				break;
			case R.id.btn_add_bookmarks:
				fMenuClickListener.onFbtn_addBookmarksClick();
				break;
			case R.id.btn_hasImg:// 智能无图
				if (hasImg) {// 智能无图开启
					imageViewHasImg.setBackgroundResource(R.drawable.has_img);
					editor.putBoolean("hasImg", false);
					hasImg = false;
					Toast.makeText(((MainActivity) getActivity()), "智能无图关闭",
							Toast.LENGTH_SHORT).show();
					btn_hasImg.setBackgroundColor(Color.parseColor("#ffffff"));
					editor.commit();

				} else {
					imageViewHasImg.setBackgroundResource(R.drawable.no_img);
					editor.putBoolean("hasImg", true);
					hasImg = true;
					Toast.makeText(((MainActivity) getActivity()), "智能无图开启",
							Toast.LENGTH_SHORT).show();
					btn_hasImg.setBackgroundColor(Color.parseColor("#cccccc"));
					editor.commit();
				}
				fMenuClickListener.onFbtn_HasImg();
				break;
			}

		}
	}
}
