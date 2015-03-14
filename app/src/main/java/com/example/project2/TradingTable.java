package com.example.project2;

import java.util.Calendar;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.TextView;

public class TradingTable extends Activity{
	SQLiteDatabase db;
	Cursor cursor=null;
	TextView tvTID;
	TextView tvDate;
	TextView tvTime;
	TextView tvSCode;
	TextView tvSName;
	TextView tvQTY;
	TextView tvPrice;
	TextView tvBuySell;
	
public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tradingtable);
		
		tvTID=(TextView)findViewById(R.id.tvTID);
		tvDate=(TextView)findViewById(R.id.tvDate);
		tvTime=(TextView)findViewById(R.id.tvTime);		
		tvSCode=(TextView)findViewById(R.id.tvSCode);
		tvSName=(TextView)findViewById(R.id.tvSName);
		tvQTY=(TextView)findViewById(R.id.tvQTY);
		tvPrice=(TextView)findViewById(R.id.tvPrice);
		tvBuySell=(TextView)findViewById(R.id.tvBuySell);
		refreshDataView();
}
public void refreshDataView(){
	db = SQLiteDatabase.openDatabase("/data/data/com.example.project2/eBidDB", null, SQLiteDatabase.OPEN_READWRITE);
	cursor = db.rawQuery("select * from Trading",null);	
	String dataTID="";
	String dataDate="";
	String dataTime="";
	String dataSCode="";
	String dataSName="";
	String dataQTY="";
	String dataPrice="";
	String dataBuySell="";
	while(cursor.moveToNext()){
		String TID=cursor.getString(0);
		String Date=cursor.getString(1);
		String Time=cursor.getString(2);
		String SCode=cursor.getString(3);
		String SName=cursor.getString(4);
		String QTY=cursor.getString(5);
		String Price=cursor.getString(6);
		String BuySell=cursor.getString(7);
		dataTID+=String.format("%s \n",TID);
		dataDate+=String.format("%s \n",Date);
		dataTime+=String.format("%s \n",Time);
		dataSCode+=String.format("%s \n",SCode);
		dataSName+=String.format("%s \n",SName);
		dataQTY+=String.format("%s \n",QTY);
		dataPrice+=String.format("%s \n",Price);
		dataBuySell+=String.format("%s \n",BuySell);
	}
	tvTID.setText(dataTID);
	tvDate.setText(dataDate);
	tvTime.setText(dataTime);
	tvSCode.setText(dataSCode);
	tvSName.setText(dataSName);
	tvQTY.setText(dataQTY);
	tvPrice.setText(dataPrice);
	tvBuySell.setText(dataBuySell);
	
	
}

}