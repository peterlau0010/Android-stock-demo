package com.example.project2;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;



public class Database extends Activity{
	
	Cursor cursor=null;
	@Override
	public void onCreate(Bundle bundle) {
	super.onCreate(bundle);
	setContentView(R.layout.database);	
	refreshDataView();	
	}	
	public void refreshDataView(){
		SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.project2/eBidDB", null, SQLiteDatabase.OPEN_READWRITE);
		cursor = db.rawQuery("select * from Stock",null);
		TextView tvID =(TextView)findViewById(R.id.tvID);
		TextView tvSCode =(TextView)findViewById(R.id.tvSCode);
		TextView tvSName =(TextView)findViewById(R.id.tvSName);
		TextView tvLot =(TextView)findViewById(R.id.tvLot);
		TextView tvQTY =(TextView)findViewById(R.id.tvQTY);
		
		String dataPid="";
		String dataSCode="";
		String dataSName="";
		String dataLot="";
		String dataQTY="";
		
		while(cursor.moveToNext()){
			String PID=cursor.getString(0);
			String stockCode=cursor.getString(1);
			String stockName=cursor.getString(2);
			String Lot=cursor.getString(3);
			String QTY=cursor.getString(4);
			dataPid+=String.format("%s \n",PID);
			dataSCode+=String.format("%s \n",stockCode);
			dataSName+=String.format("%s \n",stockName);
			dataLot+=String.format("%s \n",Lot);
			dataQTY+=String.format("%s \n",QTY);
		
		}
		tvID.setText(dataPid);
		tvSCode.setText(dataSCode);
		tvSName.setText(dataSName);
		tvLot.setText(dataLot);
		tvQTY.setText(dataQTY);
	}
}