package com.example.project2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;



public class Char extends Activity{
	String []jsonSCode;
	double [] jsonPrice;
	int [] jsonLot;
	int [] quantityOnHand;
	double [] totalValue;
	String []SCode;
	SQLiteDatabase db;
	Cursor cursor;
	int DBSize=0;
	String []jsonSName;
	int TotalAssest=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(new Panel(this));		
		db = SQLiteDatabase.openDatabase("/data/data/com.example.project2/eBidDB", null, SQLiteDatabase.OPEN_READWRITE);		
		cursor = db.rawQuery("select * from Stock",null);
		//Toast.makeText(this,"Read DB",1).show();
		quantityOnHand = new int[ cursor.getCount()];
		jsonSCode = new String[ cursor.getCount()];
		jsonPrice = new double[ cursor.getCount()];
		SCode = new String[ cursor.getCount()];
		totalValue = new double[ cursor.getCount()];
		jsonLot = new int[cursor.getCount()];
		jsonSName = new String[cursor.getCount()];
		
		while(cursor.moveToNext()){
			quantityOnHand[DBSize]=cursor.getInt(4);
			SCode[DBSize]=cursor.getString(1);
			
			
		
			try {
				String url="http://www.alanpo.com/itp4501/stock_quote.php?stock_no=" +SCode[DBSize];		
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
		    	jsonLot[DBSize]= jsonObject.getJSONObject("quote").getJSONObject("stock").getInt("lot");
		    	jsonPrice[DBSize]=jsonObject.getJSONObject("quote").getJSONObject("stock").getDouble("price");
		    	jsonSCode[DBSize]=jsonObject.getJSONObject("quote").getJSONObject("stock").getString("symbol");
		    	jsonSName[DBSize]=jsonObject.getJSONObject("quote").getJSONObject("stock").getJSONObject("name").getString("english");
		    	
			} catch (IOException e) {
				
			} catch (JSONException e) {
			}
			totalValue[DBSize]=jsonPrice[DBSize]*quantityOnHand[DBSize];
			
			Toast.makeText(this, "TotalValue:"+totalValue[DBSize],1).show();
			DBSize++;
		}
		for(int i=0;i<totalValue.length;i++){
			TotalAssest+=totalValue[i];
		}
		Toast.makeText(this, "Total Assest:"+TotalAssest,1).show();
	}
	
	
	int panelHeight;
	int panelWidth;

	class Panel extends View {
		public Panel(Context context) {
			super(context);
		}
		float cDegree = 0;
		int rColor[] = {Color.BLUE, Color.CYAN, Color.DKGRAY, Color.YELLOW,Color.GRAY,Color.GREEN,Color.LTGRAY,Color.MAGENTA,Color.RED,Color.WHITE};
		protected void onSizeChanged(int w, int h, int oldw,int oldh) {
			panelHeight= h;
			panelWidth= w;
		}
		
		@Override
		public void onDraw(Canvas c){
			super.onDraw(c);
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			// make the entire canvas white
			paint.setColor(Color.BLACK);
			c.drawPaint(paint);
			
			// draw pie chart
			paint.setAntiAlias(true); 
	    	paint.setStyle(Paint.Style.FILL); 
	    	float x=0;
	    	
	    	for (int i = 0; i < totalValue.length; i++) {
	        	cDegree = (float)(totalValue[i] * 360 / TotalAssest);
	            
	      	  	// Draw the arc (Radius of the pie = 400px)
	        	paint.setColor(rColor[i]);
	        	RectF rec=new RectF(getWidth()/10,getHeight()/5,getWidth()/10+400,getHeight()/5+400);
	        	c.drawArc(rec, x, cDegree, true, paint);
	        	x+=(float)cDegree; 
	        }
	    	
	    	// Draw the title
	    	paint.setColor(Color.BLUE); 
	    	paint.setStyle(Paint.Style.FILL); 
	    	paint.setTextSize(80);
	    	paint.setTypeface(Typeface.SERIF);
	    	c.drawText("Asset", getWidth()/2-80,80, paint); 
	    	
	    	int vertSpace = getHeight()-100;
	    	paint.setTextSize(20);    	
	        for (int i = jsonSName.length - 1; i >= 0; i--) {
	            // Draw the legend rect (20px SQ)
	        	paint.setColor(rColor[i]);
	        	c.drawRect(getWidth()-300, getHeight()-300+40*i, getWidth()-300+20,getHeight()-300+20+40*i , paint);
	            // Draw the label
	        	c.drawText(jsonSName[i], getWidth()-300 +40, getHeight()-300+20+40*i, paint);
	      	}  	
		}
	}
}