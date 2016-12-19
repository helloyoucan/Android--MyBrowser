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
	private EditText UrlEdit;//��ȡһ������url�ı༭�����   
    private Button button;//����һ��"����POST����"��ť����  
    private Handler handler;//����һ��Handler����  
    private boolean flag=false;//����Ƿ�ɹ��ı���
	final int CODE = 0x717;// ����һ�������볣��

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
		if (requestCode == CODE && resultCode == CODE) {// �����ʷ��¼�����Ӻ�������ҳ��
			Bundle bundle = data.getExtras();
			// Toast.makeText(MainActivity.this, bundle.getString("historyUrl"),
			// Toast.LENGTH_LONG).show();
			mainF.startReadUrl(bundle.getString("historyUrl"));
		}
	};

	public void onFed_showClick() {
		// �л���Editfragment
		// if (editF == null) {
		editF = new EditUrlFragment();
		editF.setfBtn_goClickListener(this);// �ص�����
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
		// �л���Menufragment
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
		// ������д���
		Toast.makeText(MainActivity.this, "���д���", Toast.LENGTH_SHORT).show();
	}

	public void onFbtn_goClick() {
		// ��editF�л�mainfragment
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
			url = url.replace(" ", "");// ȥ���ո�
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
		// ������urlʱ����հ��¼�
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
		// ����ر�
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
	 * �����ǩ��ť����ת����ǩ����
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
		Toast.makeText(MainActivity.this, "��ǩ", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onFbtn_tracelessClick() {
		// TODO Auto-generated method stub
		// �޺�ģʽ
		mainF.setCanTraceless(!mainF.isCanTraceless());
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		tx.remove(menuF);
		tx.commit();
	}

	@Override
	public void onFbtn_settingClick() {
		// TODO Auto-generated method stub
		//Toast.makeText(MainActivity.this, "����", Toast.LENGTH_SHORT).show();
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
		// Toast.makeText(MainActivity.this, "��ʷ��¼", Toast.LENGTH_SHORT).show();
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
		.setTitle("����")
		.setView(textEntryView)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				 new Thread(new Runnable(){  
	                    @Override  
	                    public void run() {  
	                        try {  
	                            String sourceUrl=downloadUrl.getText().toString();//��ȡ���ص�ַ  
	                            URL url=new URL(sourceUrl);//�������ص�ַ��Ӧ��URL����  
	                            //����һ������  
	                            HttpURLConnection conn=(HttpURLConnection)url.openConnection();  
	                            InputStream is=conn.getInputStream();//��ȡ����������  
	                            if(is!=null){  
	                                String expandName=sourceUrl.substring(sourceUrl.lastIndexOf(".")+1,  
	                                        sourceUrl.length()).toLowerCase();//��ȡ�ļ�����չ��  
	                                String fileName=sourceUrl.substring(sourceUrl.lastIndexOf("/")+1,  
	                                        sourceUrl.lastIndexOf("."));//��ȡ�ļ���  
	                                //��SD���ϴ����ļ�  
	                                Log.e("myerr", fileName);
	                                File file=new File("/sdcard/pic/"+fileName+"."+expandName);  
	                                FileOutputStream fos=new FileOutputStream(file);//����һ���ļ����������  
	                                byte buf[]=new byte[1024];//����һ���ֽ�����  
	                                //��ȡ�ļ���������������  
	                                while(true){  
	                                    int numread=is.read(buf);  
	                                    if(numread<=0){  
	                                        break;  
	                                    }else{  
	                                        fos.write(buf, 0, numread);  
	                                    }  
	                                }  
	                            }  
	                            is.close();//�ر�����������  
	                            conn.disconnect();//�ر�����  
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
	                        Message m=handler.obtainMessage();//��ȡһ��Message  
	                        handler.sendMessage(m);//������Ϣ  
	                    }  
	                      
	                }).start();//�����߳�  
				Toast.makeText(MainActivity.this, "��������="+downloadUrl.getText().toString(), Toast.LENGTH_LONG).show();
			}
		})
		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, "ȡ������", Toast.LENGTH_SHORT).show();
			}
		})
		.show();
		 //��дHandler��handleMessage()����������flag��Ǳ���flag��ֵ��ͬ��ʾ��ͬ����ʾ  
        handler=new Handler(){  
  
  
            @Override  
            public void handleMessage(Message msg) {  
                if(flag){  
                    Toast.makeText(MainActivity.this, "�ļ�������ɣ�",   
                            Toast.LENGTH_SHORT).show();  
                }else{  
                    Toast.makeText(MainActivity.this, "�ļ�����ʧ�ܣ�",   
                            Toast.LENGTH_SHORT).show();  
                }  
                super.handleMessage(msg);  
            }  
              
        };
		Toast.makeText(MainActivity.this, "����", Toast.LENGTH_SHORT).show();
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
		// �����ǩ
		webView = (WebView)this.findViewById(R.id.webView);
		ContentValues cValue = new ContentValues();
		if (webView.getTitle() != null && webView.getTitle() != ""
				&& webView.getUrl() != null && webView.getUrl() != "") {
			cValue.put("url", webView.getUrl());
			cValue.put("title", webView.getTitle());
			cValue.put("time",
					DateFormat.getDateInstance().format(new Date().getTime()));
			db.insert(MyOpenHelper.TABLE_BM, null, cValue);// ��������
		}
		Toast.makeText(MainActivity.this, "�����ǩ�ɹ�", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onFbtn_HasImg() {// ������ͼ
		// TODO Auto-generated method stub
		mainF.setHasImg(!mainF.isHasImg());
		FragmentManager fm = getFragmentManager();
		FragmentTransaction tx = fm.beginTransaction();
		tx.remove(menuF);
		tx.commit();
	}

}