package com.example.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
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
	private EditText UrlEdit;//获取一个输入url的编辑框对象   
    private Button button;//声明一个"发送POST请求"按钮对象  
    private Handler handler;//声明一个Handler对象  
    private boolean flag=false;//标记是否成功的变量
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

	
	/**
	 * 点击书签按钮，跳转到书签界面
	 */
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
		LayoutInflater inflater=LayoutInflater.from(MainActivity.this);
		final View textEntryView=inflater.inflate(R.layout.alert_dialog_text_entry, null);
		
		final EditText downloadUrl=(EditText)textEntryView.findViewById(R.id.download_url_value);
		
		new AlertDialog.Builder(MainActivity.this)
		.setIcon(R.drawable.add_bookmarks)
		.setTitle("下载")
		.setView(textEntryView)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				 new Thread(new Runnable(){  
	                    @Override  
	                    public void run() {  
	                        try {  
	                            String sourceUrl=downloadUrl.getText().toString();//获取下载地址  
	                            URL url=new URL(sourceUrl);//创建下载地址对应的URL对象  
	                            //创建一个连接  
	                            HttpURLConnection conn=(HttpURLConnection)url.openConnection();  
	                            InputStream is=conn.getInputStream();//获取输入流对象  
	                            if(is!=null){  
	                                String expandName=sourceUrl.substring(sourceUrl.lastIndexOf(".")+1,  
	                                        sourceUrl.length()).toLowerCase();//获取文件的拓展名  
	                                String fileName=sourceUrl.substring(sourceUrl.lastIndexOf("/")+1,  
	                                        sourceUrl.lastIndexOf("."));//获取文件名  
	                                //在SD卡上创建文件  
	                                Log.e("myerr", fileName);
	                                File file=new File("/sdcard/pic/"+fileName+"."+expandName);  
	                                FileOutputStream fos=new FileOutputStream(file);//创建一个文件输出流对象  
	                                byte buf[]=new byte[1024];//创建一个字节数组  
	                                //读取文件到输入流对象中  
	                                while(true){  
	                                    int numread=is.read(buf);  
	                                    if(numread<=0){  
	                                        break;  
	                                    }else{  
	                                        fos.write(buf, 0, numread);  
	                                    }  
	                                }  
	                            }  
	                            is.close();//关闭输入流对象  
	                            conn.disconnect();//关闭连接  
	                            flag=true;  
	                        } catch (MalformedURLException e) {  
	                            // TODO Auto-generated catch block  
	                            e.printStackTrace();  
	                            flag=false;  
	                        } catch (IOException e) {  
	                            // TODO Auto-generated catch block  
	                            e.printStackTrace();  
	                            flag=false;  
	                        }  
	                        Message m=handler.obtainMessage();//获取一个Message  
	                        handler.sendMessage(m);//发送消息  
	                    }  
	                      
	                }).start();//开启线程  
				Toast.makeText(MainActivity.this, "下载链接="+downloadUrl.getText().toString(), Toast.LENGTH_LONG).show();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, "取消下载", Toast.LENGTH_SHORT).show();
			}
		})
		.show();
		 //重写Handler的handleMessage()方法，根据flag标记变量flag的值不同显示不同的提示  
        handler=new Handler(){  
  
  
            @Override  
            public void handleMessage(Message msg) {  
                if(flag){  
                    Toast.makeText(MainActivity.this, "文件下载完成！",   
                            Toast.LENGTH_SHORT).show();  
                }else{  
                    Toast.makeText(MainActivity.this, "文件下载失败！",   
                            Toast.LENGTH_SHORT).show();  
                }  
                super.handleMessage(msg);  
            }  
              
        };
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
		Toast.makeText(MainActivity.this, "添加书签成功", Toast.LENGTH_SHORT).show();

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