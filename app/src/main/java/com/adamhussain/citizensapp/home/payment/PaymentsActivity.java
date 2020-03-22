package com.adamhussain.citizensapp.home.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.adamhussain.citizensapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaymentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        recyclerView = findViewById(R.id.pay_items);
        List<PayItem> payItems = Arrays.asList(
                new PayItem("Water Bill", R.drawable.water_bill),
                new PayItem("Electricity Bill", R.drawable.electricity_bill),
                new PayItem("Gas Bill", R.drawable.gas_bills),
                new PayItem("Car road taxes", R.drawable.car_taxes));
        adapter = new PayItemAdapter(payItems,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.HORIZONTAL));
        recyclerView.setAdapter(adapter);

    }
}
