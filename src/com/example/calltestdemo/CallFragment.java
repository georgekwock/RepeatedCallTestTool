package com.example.calltestdemo;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.Call.State;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class CallFragment extends Fragment {
	private static final String TAG = "CallFragment";
	private EditText phoneEdt;
	private EditText timeEdt;
	private EditText intervalEdt;
	private Button runBtn;
	private Button pauseBtn;
	private Context context;
	private int callCount;
	private TelephonyManager mTelephonyManager;
	private String phoneNumber;
	private int callInterval;
	private CheckBox checkBox;
	private List<LogBean> listLog;
	private CallManager mCallManager;
	private final static int PHONE_STATE_CHANGED = 1;
	private final static int CALL_PHONE = 2;
	private final static int CALL_FAILD = 3;
	private LogBean bean;
	private boolean isFirstCall = true;
	private static long lastClickTime;

	private int count = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getContext();
		listLog = new ArrayList<LogBean>();
		mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		PhoneUtils.printAllInform(TelephonyManager.class);
		Phone phone = PhoneFactory.getDefaultPhone();
		mCallManager = CallManager.getInstance();
		mCallManager.registerPhone(phone);
		mCallManager.registerForPreciseCallStateChanged(mHandler, PHONE_STATE_CHANGED, null);
		mTelephonyManager.listen(new CallStateListener(), CallStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.call_view, container, false);
		initView(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		runBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "isFirstCall:" + isFirstCall);
				if (isFastClick()) {
					return;
				}
				String countStr = timeEdt.getText().toString();
				if (countStr.equals("")) {
					callCount = 0;
				} else {
					callCount = Integer.valueOf(timeEdt.getText().toString());
				}

				phoneNumber = phoneEdt.getText().toString();
				if (callCount > 10000) {
					Toast.makeText(context, "Call times no more than 10000!", 3000).show();
				} else {
					if (isAirplaneModeOn()) {
						Log.d(TAG, "air mode");
						bean = new LogBean();
						bean.setCallStatus(false);
						listLog.add(bean);
						((MainActivity) getActivity()).setListData(listLog);
					}
					Message msgCall = new Message();
					msgCall.what = CALL_PHONE;
					if (isFirstCall) {
						mHandler.sendMessage(msgCall);
						isFirstCall = false;
					} else {
						mHandler.sendMessageDelayed(msgCall, 6000);
					}
					count++;
				}

			}
		});
		pauseBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isFirstCall = true;
				mHandler.removeMessages(CALL_PHONE);
				count = 0;
			}
		});
	}

	private void initView(View view) {
		phoneEdt = (EditText) view.findViewById(R.id.phone_number);
		timeEdt = (EditText) view.findViewById(R.id.dial_times);
		intervalEdt = (EditText) view.findViewById(R.id.call_interval);
		runBtn = (Button) view.findViewById(R.id.start_btn);
		pauseBtn = (Button) view.findViewById(R.id.pause_btn);
		checkBox = (CheckBox) view.findViewById(R.id.check_auto);

	}

	private void callNumber(String number) {
		if (number.equals("")) {
			Toast.makeText(getContext(), "Empty phone number", 3000).show();
		} else {
			Intent intent = new Intent("android.intent.action.CALL_PRIVILEGED", Uri.parse("tel:" + number));
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		}

	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PHONE_STATE_CHANGED:
				updatePhoneState();
				break;
			case CALL_PHONE:
				callNumber(phoneNumber);
				break;
			case CALL_FAILD:
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							PhoneUtils.getITelephony(mTelephonyManager).endCall();
							PhoneUtils.getITelephony(mTelephonyManager).cancelMissedCallsNotification();
						} catch (RemoteException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				});
				thread.start();
				bean.setCallStatus(false);
				SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
				Date curDate3 = new Date(System.currentTimeMillis());
				String endTimeFailed = formatter3.format(curDate3);
				bean.setEndCallTime(endTimeFailed);
				break;

			}

		}
	};

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Log.d(TAG, "onHiddenChanged");

	}

	protected void updatePhoneState() {
		Call call = mCallManager.getActiveFgCall();
		if (mCallManager.hasActiveRingingCall()) {
			call = mCallManager.getFirstActiveRingingCall();
		}
		State state = call.getState();
		switch (state) {
		case IDLE:
			Log.d(TAG, "IDLE...");
			break;
		case DIALING:
			Log.d(TAG, "DIALING...");
			// 拨号中
			bean = new LogBean();
			bean.setAdded(false);
			bean.setIsoutingcall(true);
			break;
		case ACTIVE:
			Log.d(TAG, "ACTIVE...");
			mHandler.removeMessages(CALL_FAILD);
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd     HH:mm:ss");
			Date curDate1 = new Date(System.currentTimeMillis());
			String activeTime = formatter1.format(curDate1);
			final Timer timer = new Timer();
			if (TextUtils.isEmpty(intervalEdt.getText())) {
				Log.d(TAG, "is empty ");
			} else {
				callInterval = Integer.valueOf(intervalEdt.getText().toString());
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						try {
							PhoneUtils.getITelephony(mTelephonyManager).endCall();
							PhoneUtils.getITelephony(mTelephonyManager).cancelMissedCallsNotification();
							timer.cancel();
							isFirstCall = true;
							Log.d(TAG, "End Phone...");
						} catch (RemoteException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}, callInterval * 1000, callInterval * 1000);
			}
			bean.setCallStatus(true);
			bean.setAnswerTime(activeTime);
			break;
		case INCOMING:
			break;
		case ALERTING:
			Log.d(TAG, "ALERTING...");
			bean.setmAlertingTime(new Date(System.currentTimeMillis()));
			Message msgFailed = new Message();
			msgFailed.what = CALL_FAILD;
			mHandler.sendMessageDelayed(msgFailed, 20000);
			break;
		case DISCONNECTED:
			Log.d(TAG, "DISCONNECTED...");
			break;
		case DISCONNECTING:
			Log.d(TAG, "DISCONNECTING...");
			mHandler.removeMessages(CALL_FAILD);
			SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
			Date curDate2 = new Date(System.currentTimeMillis());
			String endTime = formatter2.format(curDate2);
			bean.setEndCallTime(endTime);
			bean.setmDisconnectingTime(new Date(System.currentTimeMillis()));
			if (bean.isAdded() == false) {
				listLog.add(bean);
				bean.setAdded(true);
			}
			((MainActivity) getActivity()).setListData(listLog);
			repeatCall();
			break;
		case HOLDING:
			Log.d(TAG, "HOLDING...");
			break;
		case WAITING:
			Log.d(TAG, "WAITING...");
			break;
		default:
			break;
		}

	}

	public void repeatCall() {
		Log.d(TAG, "repeat call: " + count);
		if (count < callCount) {
			mHandler.removeMessages(CALL_PHONE);
			Message msgrepeat = new Message();
			msgrepeat.what = CALL_PHONE;
			mHandler.sendMessageDelayed(msgrepeat, 8000);
			count++;
		} else {
			count = 0;
		}
	}

	public class CallStateListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if (state == TelephonyManager.CALL_STATE_IDLE) // 空闲
			{
				Log.d(TAG, "CALL_STATE_IDLE...");
			} else if (state == TelephonyManager.CALL_STATE_OFFHOOK) // 接听
			{

			} else if (state == TelephonyManager.CALL_STATE_RINGING) // 来电
			{
				Log.d(TAG, "CALL_STATE_RINGING...");
				bean = new LogBean();
				bean.setIsoutingcall(false);
				bean.setAdded(false);
				if (checkBox.isChecked()) {
					try {
						PhoneUtils.getITelephony(mTelephonyManager).silenceRinger();// 静铃
						PhoneUtils.getITelephony(mTelephonyManager).answerRingingCall();// 自动接听

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	private boolean isAirplaneModeOn() {
		return Settings.Global.getInt(getContext().getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
	}

	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 800) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

}
