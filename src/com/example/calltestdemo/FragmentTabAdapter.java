package com.example.calltestdemo;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class FragmentTabAdapter implements OnCheckedChangeListener {
	private List<Fragment> fragments; // һ��tabҳ���Ӧһ��Fragment
	private RadioGroup rgs; // �����л�tab
	private FragmentActivity fragmentActivity; // Fragment������Activity
	private int fragmentContentId; // Activity����Ҫ���滻�������id

	private int currentTab; // ��ǰTabҳ������

	private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener;

	public FragmentTabAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId,
			RadioGroup rgs) {
		super();
		this.fragments = fragments;
		this.rgs = rgs;
		this.fragmentActivity = fragmentActivity;
		this.fragmentContentId = fragmentContentId;

		FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
		transaction.add(fragmentContentId, fragments.get(0));
		transaction.commit();

		rgs.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		for (int i = 0; i < rgs.getChildCount(); i++) {
			if (rgs.getChildAt(i).getId() == checkedId) {
				Fragment fragment = fragments.get(i);
				FragmentTransaction ft = obtainFragmentTransaction(i);

				getCurrentFragment().onPause(); // ��ͣ��ǰtab
				// getCurrentFragment().onStop(); // ��ͣ��ǰtab

				if (fragment.isAdded()) {
					// fragment.onStart(); // ����Ŀ��tab��onStart()
					fragment.onResume(); // ����Ŀ��tab��onResume()
				} else {
					ft.add(fragmentContentId, fragment);
				}
				showTab(i); // ��ʾĿ��tab
				ft.commit();

				// ����������л�tab���⹦�ܹ��ܽӿ�
				if (null != onRgsExtraCheckedChangedListener) {
					onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(group, checkedId, i);
				}

			}
		}
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public Fragment getCurrentFragment() {
		return fragments.get(currentTab);
	}

	private void showTab(int idx) {
		for (int i = 0; i < fragments.size(); i++) {
			Fragment fragment = fragments.get(i);
			FragmentTransaction ft = obtainFragmentTransaction(idx);

			if (idx == i) {
				ft.show(fragment);
			} else {
				ft.hide(fragment);
			}
			ft.commit();
		}
		currentTab = idx; // ����Ŀ��tabΪ��ǰtab
	}

	/**
	 * ��ȡһ����������FragmentTransaction
	 * 
	 * @param index
	 * @return
	 */
	private FragmentTransaction obtainFragmentTransaction(int index) {
		FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
		// �����л�����
		if (index > currentTab) {
			ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
			
		} else {
			ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
		}
		return ft;
	}

	public void setOnRgsExtraCheckedChangedListener(OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
		this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
	}

	static class OnRgsExtraCheckedChangedListener {
		public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {

		}
	}
}
