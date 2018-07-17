package com.example.calltestdemo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private List<LogBean> list;
	private LayoutInflater mInflater = null;
	private Context mContext;

	public MyAdapter(Context context, List<LogBean> list) {
		super();
		mInflater = LayoutInflater.from(context);
		mContext = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public LogBean getItem(int position) {
		if (list == null || position < 0 || position >= getCount()) {
			return null;
		} else {
			return list.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LogBean bean = getItem(position);
		View view = convertView;
		if (view == null) {
			view = mInflater.inflate(R.layout.log_item, null);
		}
		bindView(view, mContext, position, bean);
		return view;
	}

	private void bindView(View view, Context context, int position, LogBean data) {
		LogHolder holder;
		if (view.getTag() == null) {
			holder = new LogHolder(view);
			view.setTag(holder);
		} else {
			holder = (LogHolder) view.getTag();
		}
		bindLogData(holder, position, data);
	}

	private void bindLogData(LogHolder holder, int position, LogBean data) {
		if (data.isIsoutingcall()) {
			holder.callType.setText(mContext.getResources().getString(R.string.call_out));
		} else {
			holder.callType.setText(mContext.getResources().getString(R.string.call_in));
		}
		if (data.isCallStatus()) {
			holder.callStatus.setText(mContext.getResources().getString(R.string.call_succeed));
			holder.callStatus.setTextColor(mContext.getResources().getColor(R.color.call_successful));
		} else {
			holder.callStatus.setText(mContext.getResources().getString(R.string.call_fail));
			holder.callStatus.setTextColor(mContext.getResources().getColor(R.color.call_failed));
		}
		holder.incomingTime.setText(data.getAnswerTime());
		holder.endTime.setText(data.getEndCallTime());

	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	static class LogHolder {

		public LogHolder(View view) {

			callType = (TextView) view.findViewById(R.id.call_type);
			callStatus = (TextView) view.findViewById(R.id.call_status);
			incomingTime = (TextView) view.findViewById(R.id.income_time);
			endTime = (TextView) view.findViewById(R.id.end_time);
		}

		TextView callType;
		TextView callStatus;
		TextView incomingTime;
		TextView endTime;
	}
}
