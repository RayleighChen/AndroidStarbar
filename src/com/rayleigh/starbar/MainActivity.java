package com.rayleigh.starbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity {

	private EditText usernameEdit;
	private EditText passwordEdit;
	private Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		usernameEdit = (EditText) findViewById(R.id.usernameEdit);
		passwordEdit = (EditText) findViewById(R.id.pwdEdit);
		loginButton = (Button) findViewById(R.id.loginButton);
		
	    loginButton.setOnClickListener(loginListener);
	}
	
	Handler handler = new Handler(){
	    public void handleMessage(Message msg) {
	        super.handleMessage(msg);
	        Bundle data = msg.getData();
	        String val = data.getString("value");
	        Log.i("mylog","请求结果-->" + val);
	    }
	};

	Runnable runnable = new Runnable(){
	    @Override
	    public void run() {
	    	System.out.println("Login Button clicked!");
			String httpUrl = "http://10.96.63.65:8080/starbar/servlet/UserServlet";
			System.out.println(httpUrl);
			HttpPost request = new HttpPost(httpUrl);
			HttpClient httpClient = new DefaultHttpClient();
			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", usernameEdit
					.getText().toString()));
			params.add(new BasicNameValuePair("p", passwordEdit
					.getText().toString()));

			HttpResponse response;
			try {
				HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
				request.setEntity(entity);
				response = httpClient.execute(request);

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String str = EntityUtils.toString(response.getEntity());
					System.out.println("response:" + str);
					if (str.trim().equals("success")) {
						System.out.println("登录成功！");
						Intent intent = new Intent(MainActivity.this,
								MainActivity.class);
						startActivity(intent);
					} else {
						System.out.println("登录失败！");
					}
				} else {
					System.out.println("连接失败！");
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	
	        Message msg = new Message();
	        Bundle data = new Bundle();
	        data.putString("value","传递！");
	        msg.setData(data);
	        handler.sendMessage(msg);
	    }
	};
	

	OnClickListener loginListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
		    new Thread(runnable).start();
			
		}
	};

}
