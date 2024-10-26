package com.rxjava2.android.samples.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.rxjava2.android.samples.MyApplication;
import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.databinding.ActivitySelectionBinding;
import com.rxjava2.android.samples.ui.cache.CacheExampleActivity;
import com.rxjava2.android.samples.ui.compose.ComposeOperatorExampleActivity;
import com.rxjava2.android.samples.ui.networking.NetworkingActivity;
import com.rxjava2.android.samples.ui.pagination.PaginationActivity;
import com.rxjava2.android.samples.ui.rxbus.RxBusActivity;
import com.rxjava2.android.samples.ui.search.SearchActivity;

public class SelectionActivity extends AppCompatActivity {

    private Button btn;
    private ViewStub viewStub;
    Boolean isLongPress = false;

    ActivitySelectionBinding activitySelectionBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySelectionBinding = DataBindingUtil.setContentView(this, R.layout.activity_selection);
//        setContentView(R.layout.activity_selection);

        btn = (Button) findViewById(R.id.btn);

        viewStub = (ViewStub) findViewById(R.id.view_stub);
        final Boolean[] firstIn = {new Boolean(true)};

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstIn[0]) {
                    firstIn[0] = false;
                    viewStub.inflate();
                }
            }
        });

        initA11y();
    }

    // practice for A11y
    private void initA11y(){
        activitySelectionBinding.btnTestLongPress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongPress = true;
                Toast.makeText(getApplicationContext(),getString(R.string.long_press_toast),Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        activitySelectionBinding.btnTestLongPress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP && isLongPress==true){
                    isLongPress = false;
                    Toast.makeText(getApplicationContext(),getString(R.string.long_press_toast_and_up),Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    public void startOperatorsActivity(View view) {
        startActivity(new Intent(SelectionActivity.this, OperatorsActivity.class));
    }

    public void startNetworkingActivity(View view) {
        startActivity(new Intent(SelectionActivity.this, NetworkingActivity.class));
    }

    public void startCacheActivity(View view) {
        startActivity(new Intent(SelectionActivity.this, CacheExampleActivity.class));
    }

    public void startRxBusActivity(View view) {
        ((MyApplication) getApplication()).sendAutoEvent();
        startActivity(new Intent(SelectionActivity.this, RxBusActivity.class));
    }

    public void startPaginationActivity(View view) {
        startActivity(new Intent(SelectionActivity.this, PaginationActivity.class));
    }

    public void startComposeOperator(View view) {
        startActivity(new Intent(SelectionActivity.this, ComposeOperatorExampleActivity.class));
    }

    public void startSearchActivity(View view) {
        startActivity(new Intent(SelectionActivity.this, SearchActivity.class));
    }



}
