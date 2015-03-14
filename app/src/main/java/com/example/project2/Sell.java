package com.example.project2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Sell extends Activity implements OnClickListener{
	EditText etScode;
	EditText etQTY;
	EditText etPrice;
	TextView tvError;
	Button btnSell;
	TextView tvSuccess;	
	
	double jsonPrice=0;
	double jsonLot=0;
	String jsonSCode="";
	String jsonSName="";
	double QTY=0;
	double Price=0;
	String SCode="";
	
	double increment =0;
	double decrement =0;
	int id=0;
	
	int quantityOnHand=0;
	boolean TradingSuccess=false;
	SQLiteDatabase db;
	Cursor cursor;
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.sell);	
		
		etScode=(EditText)findViewById(R.id.etScode);
		etQTY=(EditText)findViewById(R.id.etQTY);
		etPrice=(EditText)findViewById(R.id.etPrice);
		tvError=(TextView)findViewById(R.id.tvError);
		btnSell=(Button)findViewById(R.id.btnSell);
		tvSuccess=(TextView)findViewById(R.id.tvSuccess);
		btnSell.setOnClickListener(this);
	}	
	
	public boolean chkPrice(){
		json();	
		if((etPrice.getText().toString()).equals("")){
			tvError.setText("Price cannot be null");
			tvSuccess.setText("");
			return false;
		}
		
		Price=Double.parseDouble(etPrice.getText().toString());
		
		
		if(10<=jsonPrice &&jsonPrice<20){
			increment=jsonPrice+0.02;
			decrement=jsonPrice-0.02;
		}
		
		if(20<=jsonPrice &&jsonPrice<100){
			increment=jsonPrice+0.05;
			decrement=jsonPrice-0.05;
		}
		
		if(100<=jsonPrice &&jsonPrice<200){
			increment=jsonPrice+0.1;
			decrement=jsonPrice-0.1;
		}
		
		if(200<=jsonPrice &&jsonPrice<500){
			increment=jsonPrice+0.2;
			decrement=jsonPrice-0.2;
		}
		
		if(decrement<=Price && Price<=increment)
			return true;
		else{
			tvError.setText("Price Error");
			tvSuccess.setText("");
			return false;
		}
	}
	
	public boolean chkQTY(){
		json();		
		if((etQTY.getText().toString()).equals("")){
				tvError.setText("Quantity cannot be null");
				tvSuccess.setText("");
				return false;
		}
		QTY=Double.parseDouble(etQTY.getText().toString());
		
		if(QTY%jsonLot==0)
			return true;
		else{
			tvError.setText("Quantity Error");
			tvSuccess.setText("");
			return false;
		}
	}
	
	public void json(){
		try {
			String url="http://www.alanpo.com/itp4501/stock_quote.php?stock_no=" + etScode.getText().toString();		
			HttpClient client = new DefaultHttpClient();		
	    	HttpGet request = new HttpGet(url);    	
	    	HttpResponse response = client.execute(request);    	
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	    	String line = "";
	    	String reply = "";
	    	
	    	while ((line = reader.readLine()) != null) {
	    		reply += line + "\n";
	    	}
	    	JSONObject jsonObject = new JSONObject(reply);
	    	jsonLot= jsonObject.getJSONObject("quote").getJSONObject("stock").getDouble("lot");
	    	jsonPrice=jsonObject.getJSONObject("quote").getJSONObject("stock").getDouble("price");
	    	jsonSCode=jsonObject.getJSONObject("quote").getJSONObject("stock").getString("symbol");
	    	jsonSName=jsonObject.getJSONObject("quote").getJSONObject("stock").getJSONObject("name").getString("english");
	    	
		} catch (IOException e) {
			tvError.setText(e.getMessage());
		} catch (JSONException e) {
			tvError.setText(e.getMessage());
		}
		
	}
	
	public void onClick(View v) {	
		json();	
		if(chkQTY()==true&& chkPrice()==true){
			//update  + delete portfolio
			
			db = SQLiteDatabase.openDatabase("/data/data/com.example.project2/eBidDB", null, SQLiteDatabase.OPEN_READWRITE);
			
			cursor = db.rawQuery("select * from Stock",null);
			
			while(cursor.moveToNext()){
				SCode=cursor.getString(1);
				quantityOnHand=cursor.getInt(4);
				Log.d("***", quantityOnHand + "");
				TradingSuccess=false;
				if(SCode.equals(jsonSCode)){
					if(quantityOnHand==QTY){
						db.execSQL("DELETE FROM Stock WHERE stockCode='"+ SCode+"';");
						tvError.setText("");
						tvSuccess.setText("Sell Stock Success");
						TradingSuccess=true;
						break;
					}
					if(QTY<=quantityOnHand){
						db.execSQL("update Stock set quantityOnHand = "+ (quantityOnHand- QTY)+ " where stockCode = '" +SCode+"';");
						tvSuccess.setText("Sell Stock Success");
						tvError.setText("");
						TradingSuccess=true;
						break;
					}
					if(QTY>quantityOnHand){
						tvError.setText("Not enought Stock on Hand");
						tvSuccess.setText("");
					}
				}
			}
			//insert Trading
			if(TradingSuccess){
				cursor = db.rawQuery("select * from Trading",null);
				id=0;
				while(cursor.moveToNext()){					
					id =cursor.getInt(0);
				}
				Calendar c = Calendar.getInstance(); 
				Time time=new Time("GMT+8");
				time.setToNow();
				int hour = time.hour;
				int min =  time.minute; 
				int year = time.year; 
				int month = time.month; 
			    int day = time.monthDay; 
			    String Date=String.format("%02d/%02d", day,month);
				String Time=String.format("%02d:%02d",hour,min);
				db.execSQL("insert into Trading(tradingId,date,time,stockCode,stockName,quantity,price,buySell)values(" + String.valueOf(++id) + ",'"+ Date +"','"+  Time+"','"+ jsonSCode +"','"+jsonSName+"','" + QTY+"'," + Price+",'sell')");
			}else{
				tvError.setText("No Stock");
				tvSuccess.setText("");
			}
		}
		
			
			
		
	}
}
