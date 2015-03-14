package com.example.project2;


import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends ListActivity{
	SQLiteDatabase db;	
	String sql;	
	String []items={"Portfolio","HK Quote","Sell Stock","Buy Stock","Trading Record","Pie Chart"};
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items));
		try{
			db = SQLiteDatabase.openDatabase("/data/data/com.example.project2/eBidDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
			sql="Drop table if exists Stock";
			db.execSQL(sql);
			sql="Drop table if exists Trading";			
			db.execSQL(sql);
			sql="create table Stock( PortfolioID int Primary key, stockCode text, stockName text, lotSize int, quantityOnHand int);";
			db.execSQL(sql);			
			sql="create table Trading( tradingId int Primary key, date text, time text, stockCode text, stockName text, quantity int,price double, buySell text);";
			db.execSQL(sql);			
			db.execSQL("insert into Stock(PortfolioID,stockCode,stockName,lotSize,quantityOnHand)values(1001,'00001','CHEUNG KONG',1000,2000)");
			db.execSQL("insert into Stock(PortfolioID,stockCode,stockName,lotSize,quantityOnHand)values(1002,'00002','CLP HOLDINGS',500,5000)");
			db.execSQL("insert into Stock(PortfolioID,stockCode,stockName,lotSize,quantityOnHand)values(1003,'00003','HK & CHINA GAS',1000,3000)");
			db.execSQL("insert into Stock(PortfolioID,stockCode,stockName,lotSize,quantityOnHand)values(1004,'00005','HSBC HOLDINGS',400,2000)");
			db.execSQL("insert into Stock(PortfolioID,stockCode,stockName,lotSize,quantityOnHand)values(1005,'00066','MTR COOPOARTION',500,1000)");
			Toast.makeText(this,"table created",Toast.LENGTH_LONG).show();
			db.close();
		}catch(SQLiteException e){
			Toast.makeText(this,e.getMessage(),1).show();
		}		
	}
	
	public void onListItemClick(ListView parent,View v,int position,long id){
		Intent i = new Intent();		
		switch(position){
			case 0 :i.setClass(Home.this, Database.class);
					break;
			case 1 :i.setClass(Home.this, RealTime.class);
					break;
			case 2 :i.setClass(Home.this, Sell.class);
					break;
			case 3 :i.setClass(Home.this, Buy.class);
				break;
			case 4 :i.setClass(Home.this, TradingTable.class);
				break;
			case 5 :i.setClass(Home.this, Char.class);
		}
			startActivity(i);
	}
}