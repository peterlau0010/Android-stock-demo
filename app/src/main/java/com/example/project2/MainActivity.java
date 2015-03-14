package com.example.project2;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	TextView tvPassword;
	TextView tvUser;
	EditText etUser;
	EditText etPassword;
	Button btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvPassword=(TextView)findViewById(R.id.tvPassword);
		tvUser=(TextView)findViewById(R.id.tvUser);
		etUser=(EditText)findViewById(R.id.etUser);
		etPassword=(EditText)findViewById(R.id.etPassword);
		btnLogin=(Button)findViewById(R.id.btnLogin);
		etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
	}
	public void onClick(View view){
	//	Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_LONG).show();
		if(login()){
			Toast.makeText(getApplicationContext(),"Login success",Toast.LENGTH_LONG).show();
			
			Intent i = new Intent();
			i.setClass(MainActivity.this, Home.class);
			startActivity(i);
			
			}
		else
			Toast.makeText(getApplicationContext(),"Login fail",Toast.LENGTH_LONG).show();
	}
	
	public boolean login(){
		//return (etUser.getText().toString().equals("User"))&&(etPassword.getText().toString().equals("User"));
		return true;
	}
}