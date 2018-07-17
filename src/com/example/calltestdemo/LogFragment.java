package com.example.calltestdemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class LogFragment extends ListFragment {
	private static final String TAG = "LogFragment";
	private Button exportBtn;
	private Button clearBtn;
	private List<LogBean> list;
	private Context mContext;
	private MyAdapter adapter;

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Log.d(TAG, "Log Fragment hiddenchanged");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getContext();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume list:" + list);
		list = ((MainActivity) getActivity()).getListData();
		adapter = new MyAdapter(getContext(), list);
		setListAdapter(adapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.log_view, container, false);
		Log.d(TAG, "onCreateView list:" + list);
		exportBtn = (Button) view.findViewById(R.id.export_log);
		exportBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list != null) {
					File dir = new File("/storage/emulated/legacy/honeywell/calldemo");
					if (!dir.exists()) {
						dir.mkdir();
					}
					File file = new File(dir, "call_log.txt");
					try {
						FileWriter writer = new FileWriter(file);
						for (int i = 0; i < list.size(); i++) {
							LogBean bean = list.get(i);
							String s1 = bean.isIsoutingcall() ? mContext.getResources().getString(R.string.call_out)
									: mContext.getResources().getString(R.string.call_in);
							String s2 = bean.isCallStatus() ? mContext.getResources().getString(R.string.call_succeed)
									: mContext.getResources().getString(R.string.call_fail);
							String s3 = bean.getAnswerTime();
							String s4 = bean.getEndCallTime();
							String content = "Call Type:" + s1 + "" + "       Call Status:" + s2 + "\n"
									+ "Incoming Time:" + s3 + "\n" + "End Time:" + s4 + "\n";
							writer.write(content);
							writer.write(System.getProperty("line.separator"));
							Toast.makeText(mContext,
									"Log has been recorded in /文件管理/手机内存/honeywell/calldemo/call_log.txt", 5000).show();
						}
						writer.flush();
						writer.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					Toast.makeText(mContext, "List is empty!", 5000).show();
				}
			}

		});
		clearBtn = (Button) view.findViewById(R.id.clear_log);
		clearBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "clear log!");
				if (list != null) {
					list.clear();
					adapter.notifyDataSetChanged();
				}
			}
		});
		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		Log.d(TAG, "onAttach");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}
}
