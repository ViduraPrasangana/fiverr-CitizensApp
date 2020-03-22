package com.adamhussain.citizensapp.home.account_operation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import com.adamhussain.citizensapp.R;
import com.adamhussain.citizensapp.home.account_operation.fragment.EditDetailsFragment;
import com.adamhussain.citizensapp.home.account_operation.fragment.UserDetailsFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class AccountSettingsActivity extends AppCompatActivity {
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        viewPager = findViewById(R.id.viewpager);
        smartTabLayout = findViewById(R.id.tabLayout);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Account details", UserDetailsFragment.class)
                .add("Edit Details", EditDetailsFragment.class)
                .create());
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
    }
}
