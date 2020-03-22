package com.adamhussain.citizensapp.home.emergency_service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import com.adamhussain.citizensapp.R;
import com.adamhussain.citizensapp.home.account_operation.fragment.EditDetailsFragment;
import com.adamhussain.citizensapp.home.account_operation.fragment.UserDetailsFragment;
import com.adamhussain.citizensapp.home.emergency_service.fragment.AmbulanceServiceFragment;
import com.adamhussain.citizensapp.home.emergency_service.fragment.FireServiceFragment;
import com.adamhussain.citizensapp.home.emergency_service.fragment.PoliceServiceFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class EmergencyActivity extends AppCompatActivity {

    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        smartTabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Police Service", PoliceServiceFragment.class)
                .add("Fire Service", FireServiceFragment.class)
                .add("Ambulance Service", AmbulanceServiceFragment.class)
                .create());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        smartTabLayout.setViewPager(viewPager);
    }
}
