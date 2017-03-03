package com.app.rakez.poscashier;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillActivity extends AppCompatActivity {

    TextView tableNo,serviceCharge,vat,grandTotal,totalTV;
    Button freeTable;
    RecyclerView recyclerBill;
    String tableNumber;
    String dVat,dServiceCharge;

    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> qty = new ArrayList<>();
    private ArrayList<String> price = new ArrayList<>();

    private BillItemAdapter billAdapter;
    private List<BillItem> billList;
    String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        Bundle b = getIntent().getExtras();
        tableNumber = b.getString("tableNo");
        SharedPreferences ipPref = getApplicationContext().getSharedPreferences("MyIP", 0);
        ipAddress = ipPref.getString("IPAddress"," ");
        inintialize();
        makeratesRequest();
        makeJsonArrayRequest();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerBill.setLayoutManager(linearLayoutManager);
        recyclerBill.setItemAnimator(new DefaultItemAnimator());
        recyclerBill.setAdapter(billAdapter);
        freeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder adb = new AlertDialog.Builder(BillActivity.this);

                adb.setTitle("Confirm");
                adb.setMessage("Are you sure bill has been paid!");
                adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       makeBillClearRequest();
                    }
                });
                adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });
                adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                    }
                });
                adb.show();

            }
        });


    }

    private void inintialize() {
        tableNo = (TextView) findViewById(R.id.tableNo);
        serviceCharge = (TextView) findViewById(R.id.serviceCharge);
        vat = (TextView) findViewById(R.id.vat);
        grandTotal = (TextView) findViewById(R.id.grandTotal);
        totalTV = (TextView) findViewById(R.id.total);
        freeTable = (Button) findViewById(R.id.freeTable);
        recyclerBill = (RecyclerView) findViewById(R.id.recyclerBill);
        billList = new ArrayList<>();
        billAdapter = new BillItemAdapter(billList,this);
    }

    private void makeJsonArrayRequest(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading");
        pDialog.show();


        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, "http://"+ipAddress+"/orderapp/requestTable.php?tablNo="+tableNumber, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                name.clear();
                qty.clear();
                price.clear();
                Log.d("size of the","Size is response "+response.length());
                for(int i = 0;i<response.length();i++){
                    try {
                        JSONObject table = (JSONObject) response.get(i);
                        //String name = table.getString("name");
                        //String qty = table.getString("qty");
                        //String price = table.getString("price");
                        //Log.d("size of the","Data is "+num + sts);

                        name.add(table.getString("name"));
                        qty.add(table.getString("qty"));
                        price.add(table.getString("price"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //Log.d("size of the","Data is "+tableNo.size());
                pDialog.hide();
                prepareData();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();

            }
        });
        AppController.getInstance().addToRequestQueue(req);


    }

    private void makeratesRequest(){

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, "http://"+ipAddress+"/orderapp/rates.php", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                name.clear();
                qty.clear();
                price.clear();
                Log.d("size of the","Size is response "+response.length());
                for(int i = 0;i<response.length();i++){
                    try {
                        JSONObject table = (JSONObject) response.get(i);
                        //String name = table.getString("name");
                        //String qty = table.getString("qty");
                        //String price = table.getString("price");
                        //Log.d("size of the","Data is "+num + sts);

                        dVat = table.getString("vat");
                        dServiceCharge = table.getString("servicecharge");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //Log.d("size of the","Data is "+tableNo.size());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(req);


    }

    private void makeBillClearRequest(){
        StringRequest sr  = new StringRequest(Request.Method.POST, "http://"+ipAddress+"/orderapp/clearBill.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("fsds","Sample post "+response);
                if(response.contains("success")){
                    Intent in = new Intent(getApplicationContext(),CashierHome.class);
                    startActivity(in);
                    finish();

                }else {
                    Toast.makeText(getApplicationContext(), "Failed! Try Again...", Toast.LENGTH_LONG);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("tableNo",tableNumber);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(sr);
    }

    private void prepareData() {
        int total = 0;
        for(int i = 0 ; i < name.size() ; i++){
            int lineTotal = Integer.parseInt(price.get(i))*Integer.parseInt(qty.get(i));
            billList.add(new BillItem(name.get(i),price.get(i),qty.get(i),String.valueOf(lineTotal)));
            total = total+lineTotal;
        }

        Log.d("dsa","Asdad "+total);
        billAdapter.notifyDataSetChanged();
        totalTV.setText("Total :-"+total);
        tableNo.setText("Table "+tableNumber);
        int sc = (Integer.parseInt(dServiceCharge)*total)/100;
        int vt = (Integer.parseInt(dVat)*total)/100;
        serviceCharge.setText("Service Charge :-"+sc);
        vat.setText("VAT :-"+vt);
        int gt = total+sc+vt;
        grandTotal.setText("Grand Total :-"+gt);


    }

}
