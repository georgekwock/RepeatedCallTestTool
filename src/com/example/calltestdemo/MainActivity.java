package com.example.calltestdemo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.RadioGroup;

public class MainActivity extends FragmentActivity {
	private static final String TAG = "MainActivity";
	private RadioGroup rgs;
	public List<Fragment> fragments = new ArrayList<Fragment>();
	private CallFragment callFragment;
	private LogFragment logFragment;
	private List<LogBean> listLog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		callFragment = new CallFragment();
		logFragment = new LogFragment();
		Log.d(TAG, "MainActivity listView:" + listLog);
		fragments.add(callFragment);
		fragments.add(logFragment);
		rgs = (RadioGroup) findViewById(R.id.tabs_rg);

		FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content, rgs);
		tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
			@Override
			public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
				System.out.println("Extra---- " + index + " checked!!! ");
			}
		});
	}

	public void setListData(List<LogBean> list) {
		listLog = list;
	}

	public List<LogBean> getListData() {
		return listLog;
	}

}
