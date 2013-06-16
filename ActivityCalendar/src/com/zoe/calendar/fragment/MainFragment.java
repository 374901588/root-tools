package com.zoe.calendar.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rarnu.devlib.base.BaseFragment;
import com.rarnu.devlib.component.DragListView;
import com.rarnu.devlib.component.intf.RemoveListener;
import com.rarnu.devlib.component.tools.DragController;
import com.rarnu.utils.UIUtils;
import com.zoe.calendar.DetailActivity;
import com.zoe.calendar.Global;
import com.zoe.calendar.R;
import com.zoe.calendar.RestoreActivity;
import com.zoe.calendar.adapter.ActivityAdapter;
import com.zoe.calendar.classes.ActivityItem;
import com.zoe.calendar.classes.CalendarItem;
import com.zoe.calendar.classes.CityCodeItem;
import com.zoe.calendar.classes.WeatherInfo;
import com.zoe.calendar.common.Actions;
import com.zoe.calendar.common.Config;
import com.zoe.calendar.component.CalendarDays;
import com.zoe.calendar.component.CalendarView;
import com.zoe.calendar.component.CalendarView.OnCalendarChange;
import com.zoe.calendar.component.Day;
import com.zoe.calendar.component.DayClickListener;
import com.zoe.calendar.database.QueryUtils;
import com.zoe.calendar.dialog.WeatherDialog;
import com.zoe.calendar.utils.APIUtils;
import com.zoe.calendar.utils.APIUtils.WeatherCallback;
import com.zoe.calendar.utils.AnimateUtils;
import com.zoe.calendar.utils.CityUtils;
import com.zoe.calendar.utils.ResourceUtils;
import com.zoe.calendar.utils.WeatherUtils;

public class MainFragment extends BaseFragment implements OnCalendarChange,
		DayClickListener, RemoveListener, OnItemClickListener, OnClickListener,
		WeatherCallback {

	CalendarView vpCalendar;
	DragListView lvCalender;
	ImageView ivActivityHint;
	DragController mController;
	ActivityAdapter adapterActivity;
	List<ActivityItem> listActivity;
	List<CalendarItem> listCalendar;
	ImageView ivTrash;

	List<CalendarDays> listDays;
	TextView tvDate;
	private int currentDayLines;

	Day pointedDay;

	ActionBar bar;
	View actionBarView;
	RelativeLayout layWeather;
	ImageView ivWeather;
	TextView tvTemp;
	Button btnSync;
	RotateAnimation animSync;

	WeatherInfo weather;
	boolean isDownloading = false;

	int selectedMonth = 0;
	int selectedDay = 0;

	public MainFragment() {
		super();
		tagText = ResourceUtils.getString(R.tag.fragment_main);
	}

	public MainFragment(String tag) {
		super(tag, "");
	}

	@Override
	public int getBarTitle() {
		return R.string.app_name;
	}

	@Override
	public int getBarTitleWithPath() {
		return R.string.app_name;
	}

	@Override
	public String getCustomTitle() {
		return null;
	}

	@Override
	public void initComponents() {
		vpCalendar = (CalendarView) innerView.findViewById(R.id.vpCalendar);
		lvCalender = (DragListView) innerView.findViewById(R.id.lvCalendar);
		ivActivityHint = (ImageView) innerView
				.findViewById(R.id.ivActivityHint);
		tvDate = (TextView) innerView.findViewById(R.id.tvDate);
		ivTrash = (ImageView) innerView.findViewById(R.id.ivTrash);

		mController = new DragController(lvCalender);
		mController.setRemoveEnabled(true);
		mController.setSortEnabled(false);
		mController.setDragInitMode(DragController.ON_DRAG);
		mController.setRemoveMode(DragController.FLING_REMOVE);
		mController.setBackgroundColor(0x110099CC);
		mController.setTouchSlop(150);
		mController.setFlingSpeed(500f);

		lvCalender.setFloatViewManager(mController);
		lvCalender.setDragEnabled(true);
		listActivity = new ArrayList<ActivityItem>();
		adapterActivity = new ActivityAdapter(getActivity(), listActivity);
		lvCalender.setAdapter(adapterActivity);
		initActionBar();
	}

	private void initActionBar() {
		bar = getActivity().getActionBar();
		actionBarView = LayoutInflater.from(getActivity()).inflate(
				R.layout.actionbar_custom, null);
		ActionBar.LayoutParams alp = new ActionBar.LayoutParams(
				UIUtils.dipToPx(120), UIUtils.getActionBarHeight());
		alp.gravity = Gravity.END;
		int flags = ActionBar.DISPLAY_SHOW_CUSTOM;
		int change = bar.getDisplayOptions() ^ flags;
		bar.setCustomView(actionBarView, alp);
		bar.setDisplayOptions(change, flags);

		layWeather = (RelativeLayout) actionBarView
				.findViewById(R.id.layWeather);
		ivWeather = (ImageView) actionBarView.findViewById(R.id.ivWeather);
		tvTemp = (TextView) actionBarView.findViewById(R.id.tvTemp);
		btnSync = (Button) actionBarView.findViewById(R.id.btnSync);
		btnSync.setBackgroundResource(R.drawable.btn_sync_style);
		animSync = AnimateUtils.getRotateAnimation();
		btnSync.setAnimation(animSync);

		layWeather.setOnClickListener(this);
		btnSync.setOnClickListener(this);

	}

	private void initWeather() {
		if (Global.city == null || Global.city.equals("")) {
			noWeatherInfo();
			return;
		}

		final Handler hWeather = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					if (msg.arg1 != 0) {
						APIUtils.getWeather(msg.arg1, MainFragment.this);
					} else {
						noWeatherInfo();
					}
				}
				super.handleMessage(msg);
			}
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				CityUtils.loadCityCode(getActivity());
				CityCodeItem item = CityUtils.findCityCode(Global.city);
				Message msg = new Message();
				msg.what = 1;
				if (item != null) {
					msg.arg1 = item.code;
				} else {
					msg.arg1 = 0;
				}

				hWeather.sendMessage(msg);
			}
		}).start();

	}

	@Override
	public void initEvents() {
		vpCalendar.SetOnCalendarChange(this);
		vpCalendar.setDayClickListener(this);
		lvCalender.setOnTouchListener(mController);
		lvCalender.setRemoveListener(this);
		lvCalender.setOnItemClickListener(this);
		ivTrash.setOnClickListener(this);
	}

	@Override
	public void initLogic() {
		initCalendar();
		initActivity();
	}

	@Override
	public void onResume() {
		super.onResume();

		if ((Global.city != null) && (!Global.city.equals(""))) {
			bar.setTitle(Global.city);
		}
		if (!Global.synced) {
			Global.synced = true;
			downloadNewDataT();
			downloadCalendarItemT();

		}

		initPointedDay(pointedDay);

	}

	private void initActivity() {
		Day currentDay = new Day();
		Calendar cToday = Calendar.getInstance();
		currentDay.year = cToday.get(Calendar.YEAR);
		currentDay.month = cToday.get(Calendar.MONTH);
		currentDay.day = cToday.get(Calendar.DAY_OF_MONTH);
		onDayClick(0, 0, currentDay);
	}

	private void initPointedDay(Day day) {
		if (day != null) {
			onDayClick(selectedMonth, selectedDay, day);
		}
	}

	private void initCalendar() {

		int itemWidth = UIUtils.getWidth() / 7;
		int itemHeight = itemWidth * 3 / 4;

		listDays = new ArrayList<CalendarDays>();
		CalendarDays day = new CalendarDays(getActivity());
		calendarChanged(day);
		for (int i = 0; i < 3; i++) {
			listDays.add(day);
			day = day.getNextMonth();
		}
		vpCalendar.setDate(listDays, itemHeight);
		vpCalendar.setToScreen(0);
	}

	private void resetCalendarHeight(int lines) {
		currentDayLines = lines;
		int itemWidth = UIUtils.getWidth() / 7;
		int itemHeight = itemWidth * 3 / 4;

		RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) vpCalendar
				.getLayoutParams();
		rlp.height = itemHeight * lines;
		vpCalendar.setLayoutParams(rlp);
	}

	@Override
	public int getFragmentLayoutResId() {
		return R.layout.fragment_main;
	}

	@Override
	public String getMainActivityName() {
		return "";
	}

	@Override
	public void initMenu(Menu menu) {

	}

	@Override
	public void onGetNewArguments(Bundle bn) {
		initActivity();
	}

	@Override
	public Bundle getFragmentState() {
		return null;
	}

	@Override
	public void calendarChanged(CalendarDays days) {
		tvDate.setText(String.format("%d 年 %d 月", days.year, days.month + 1));
		if (currentDayLines != days.lines) {
			resetCalendarHeight(days.lines);
		}
	}

	@Override
	public void onDayClick(int monthIndex, int position, Day day) {
		selectedMonth = monthIndex;
		selectedDay = position;
		pointedDay = day;
		List<ActivityItem> list = null;
		try {
			list = QueryUtils.queryActivity(getActivity(), Global.city_pinyin,
					day.year, day.month + 1, day.day, 1);
		} catch (Exception e) {

		}
		listActivity.clear();
		if (list != null) {
			listActivity.addAll(list);
		}
		adapterActivity.setNewList(listActivity);
		List<ActivityItem> listRemoved = null;
		try {
			listRemoved = QueryUtils.queryActivity(getActivity(),
					Global.city_pinyin, day.year, day.month + 1, day.day, 0);
		} catch (Exception e) {

		}
		ivTrash.setVisibility((listRemoved == null || listRemoved.size() == 0) ? View.GONE
				: View.VISIBLE);
		vpCalendar.setSelection(monthIndex);

		changeAtivityHintStatus(day);

	}

	private void changeAtivityHintStatus(Day day) {
		if (day.isBeforeToday()) {
			ivActivityHint.setImageResource(R.drawable.activity_1);
			ivActivityHint.setVisibility(View.VISIBLE);
			lvCalender.setVisibility(View.GONE);
			ivTrash.setVisibility(View.GONE);
		} else if (day.isAfter60Days()) {
			ivActivityHint.setImageResource(R.drawable.activity_2);
			ivActivityHint.setVisibility(View.VISIBLE);
			lvCalender.setVisibility(View.GONE);
		} else {
			if (listActivity.size() == 0) {
				ivActivityHint.setImageResource(R.drawable.activity_0);
				ivActivityHint.setVisibility(View.VISIBLE);
				lvCalender.setVisibility(View.GONE);
			} else {
				ivActivityHint.setVisibility(View.GONE);
				lvCalender.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void remove(int which) {
		ActivityItem item = listActivity.get(which);
		try {
			QueryUtils.deleteActivity(getActivity(), item._id);
			listActivity.remove(which);
			adapterActivity.notifyDataSetChanged();
			ivTrash.setVisibility(View.VISIBLE);
			changeAtivityHintStatus(pointedDay);
		} catch (Exception e) {

		}
	}

	class DatabaseMsgReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			initActivity();
		}
	}

	private DatabaseMsgReceiver receiverDatabase = new DatabaseMsgReceiver();
	private IntentFilter filterDatabase = new IntentFilter(
			Actions.ACTION_LOAD_DATABASE_FINISH);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().registerReceiver(receiverDatabase, filterDatabase);
	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(receiverDatabase);
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		ActivityItem item = listActivity.get(position);
		Intent inDetail = new Intent(getActivity(), DetailActivity.class);
		inDetail.putExtra("item", item);
		startActivity(inDetail);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivTrash:
			startActivity(new Intent(getActivity(), RestoreActivity.class)
					.putExtra("year", pointedDay.year)
					.putExtra("month", pointedDay.month)
					.putExtra("day", pointedDay.day));
			break;
		case R.id.btnSync:
			downloadNewDataT();
			downloadCalendarItemT();
			break;
		case R.id.layWeather:
			// show weather info
			if (weather != null) {
				if ((weather.index_d != null) && (!weather.index_d.equals(""))) {
					startActivity(new Intent(getActivity(), WeatherDialog.class)
							.putExtra("weather", weather));
				}
			}
			break;
		}

	}

	private void downloadCalendarItemT() {
		// download calendar item
		final Handler hCalendarItem = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					if (listCalendar != null) {
						vpCalendar.setCalendarItems(listCalendar);
					}
				}
				super.handleMessage(msg);
			}
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				listCalendar = APIUtils.downloadCalendarItem(getActivity());
				hCalendarItem.sendEmptyMessage(1);
			}
		}).start();
	}

	private void downloadNewDataT() {
		if (Global.city_pinyin == null || Global.city_pinyin.equals("")) {
			return;
		}
		if (isDownloading) {
			return;
		}
		isDownloading = true;
		if (btnSync != null) {
			btnSync.setBackgroundResource(R.drawable.btn_syncing_style);
			animSync.start();
		}

		initWeather();

		final Handler hFinishDownload = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					isDownloading = false;
					if (btnSync != null) {
						animSync.cancel();
						btnSync.setBackgroundResource(R.drawable.btn_sync_style);
						animSync.cancel();
					}
					initPointedDay(pointedDay);
				}
				super.handleMessage(msg);
			}
		};

		new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 1;
				List<ActivityItem> newList = APIUtils.downloadData(
						getActivity(), Global.city_pinyin);
				List<ActivityItem> newAllList = APIUtils.downloadData(
						getActivity(), "all");
				if (newList != null && newAllList != null) {
					newList.addAll(newAllList);
				}
				if (newList != null) {
					try {
						QueryUtils.mergeData(getActivity(), newList);
						Config.setLastTimestamp(getActivity(),
								Global.city_pinyin, Global.newTimestamp);
						Config.setLastTimestamp(getActivity(), "all",
								Global.newAllTimestamp);
						Global.newTimestamp = 0L;
						Global.newAllTimestamp = 0L;
					} catch (Exception e) {

					}
					msg.arg1 = 1;
				} else {
					msg.arg1 = 0;
				}
				hFinishDownload.sendMessage(msg);
			}
		}).start();
	}

	private void noWeatherInfo() {
		tvTemp.setText(R.string.weather_cannot_get);
		ivWeather.setImageResource(R.drawable.weather_0);
	}

	@Override
	public void onGetWeather(WeatherInfo weather) {
		this.weather = weather;
		if (weather == null) {
			weather = WeatherUtils.loadLocalWeather(getActivity());
			this.weather = weather;
			if (weather == null) {
				noWeatherInfo();
			} else {
				if (tvTemp != null) {
					tvTemp.setText(weather.temp);
				}
				showWeatherImage();
			}
			return;
		}

		WeatherUtils.saveWeather(getActivity(), weather,
				System.currentTimeMillis());

		if (tvTemp != null) {
			tvTemp.setText(weather.temp);
		}
		// convert weather to image
		if (isAdded()) {
			showWeatherImage();
		} else {

			final Handler hShowWeather = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.what == 1) {
						showWeatherImage();
					}
					super.handleMessage(msg);
				}
			};
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (!isAdded()) {
						try {
							Thread.sleep(500);
						} catch (Exception e) {

						}
					}
					hShowWeather.sendEmptyMessage(1);
				}
			}).start();
		}

	}

	private void showWeatherImage() {
		if (isAdded()) {
			if (ivWeather != null) {
				String w = weather.weather;
				if (w.contains(getString(R.string.weather_snow))) {
					ivWeather.setImageResource(R.drawable.weather_4);
				} else if (w.contains(getString(R.string.weather_rain))) {
					ivWeather.setImageResource(R.drawable.weather_3);
				} else if (w.contains(getString(R.string.weather_cloud))) {
					ivWeather.setImageResource(R.drawable.weather_2);
				} else {
					ivWeather.setImageResource(R.drawable.weather_1);
				}

			}
		}
	}

}
