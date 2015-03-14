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
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RealTime extends Activity implements OnClickListener{
    TextView tvStockName;
    TextView tvPrice;
    TextView tvHigh;
    TextView tvLow;
    TextView tvOpen;
    TextView tvYear_high;
    TextView tvYear_low;
    TextView tvDate;
    TextView tvLot;
    TextView tvVol;
    TextView tvStockNameCode;
    Button btnSearch;
    EditText etSearch;
    public void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realtime);
        tvStockName=(TextView)findViewById(R.id.tvStockName);
        tvPrice=(TextView)findViewById(R.id.tvPrice);
        tvHigh=(TextView)findViewById(R.id.tvHigh);
        tvLow=(TextView)findViewById(R.id.tvLow);
        tvOpen=(TextView)findViewById(R.id.tvOpen);
        tvYear_high=(TextView)findViewById(R.id.tvYear_high);
        tvYear_low=(TextView)findViewById(R.id.tvYear_low);
        tvDate = (TextView)findViewById(R.id.tvDate);
        tvLot = (TextView)findViewById(R.id.tvLot);
        tvVol= (TextView)findViewById(R.id.tvVol);
        tvStockNameCode=(TextView)findViewById(R.id.tvStockNameCode);
        btnSearch=(Button)findViewById(R.id.btnSearch);
        etSearch=(EditText)findViewById(R.id.etSearch);
        btnSearch.setOnClickListener(this);
    }
    public void onClick(View v) {
        try {
            String url="http://www.alanpo.com/itp4501/stock_quote.php?stock_no=" + etSearch.getText().toString();
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            String line = "";
            String reply = "";
            while ((line = reader.readLine()) != null) {
                reply += line + "\n";
            }
            JSONObject jsonObject = new JSONObject(reply);
            String name=jsonObject.getJSONObject("quote").getJSONObject("stock").getJSONObject("name").getString("english");
            String symbol=jsonObject.getJSONObject("quote").getJSONObject("stock").getString("symbol");
            String price=jsonObject.getJSONObject("quote").getJSONObject("stock").getString("price");
            double high=jsonObject.getJSONObject("quote").getJSONObject("stock").getDouble("high");
            double low=jsonObject.getJSONObject("quote").getJSONObject("stock").getDouble("low");
            String open=jsonObject.getJSONObject("quote").getJSONObject("stock").getString("open");
            double year_high=jsonObject.getJSONObject("quote").getJSONObject("stock").getDouble("year_high");
            double year_low=jsonObject.getJSONObject("quote").getJSONObject("stock").getDouble("year_low");
            String date=jsonObject.getJSONObject("quote").getJSONObject("stock").getString("date");
            double lot = jsonObject.getJSONObject("quote").getJSONObject("stock").getDouble("lot");
            double volume= jsonObject.getJSONObject("quote").getJSONObject("stock").getDouble("volume");
            tvStockName.setText(name);
            tvStockNameCode.setText(symbol);
            tvPrice.setText(""+price);
            tvHigh.setText(""+high);
            tvLow.setText(""+low);
           tvOpen.setText(""+open);
            tvYear_high.setText(""+ year_high);
            tvYear_low.setText(""+ year_low);
            tvDate.setText("" +date);
            tvLot.setText(""+lot);
            tvVol.setText(""+volume);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();

        }
    }


}