
package com.example.dailyexpensenote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.dailyexpensenote.R;
import com.example.dailyexpensenote.fragment.AddExpenseFragment;
import com.example.dailyexpensenote.fragment.DashboardFragment;
import com.example.dailyexpensenote.fragment.ExpenseFragment;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private int updateID = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.dashboard_navigation:
                    fragmentReplace(new DashboardFragment());
                    return true;
                case R.id.expense_navigation:
                    fragmentReplace(new ExpenseFragment());
                    return true;
                case R.id.add_navigation:
                    fragmentReplace(new AddExpenseFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        updateID = intent.getIntExtra("Update",0);
        if(updateID==1){
            fragmentReplace(new ExpenseFragment());
        }else{
            fragmentReplace(new DashboardFragment());
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void fragmentReplace(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.demoFL,fragment);
        fragmentTransaction.commit();
    }

}
