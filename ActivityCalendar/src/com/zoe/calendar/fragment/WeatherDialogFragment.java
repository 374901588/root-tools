package com.zoe.calendar.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.rarnu.devlib.base.BaseDialogFragment;
import com.zoe.calendar.R;
import com.zoe.calendar.classes.WeatherInfo;

public class WeatherDialogFragment extends BaseDialogFragment implements
		OnClickListener {

	Button btnOK;
	TextView tvWeatherText;

	// update
	public WeatherDialogFragment(String tag) {
		super(tag);
	}

	@Override
	public int getBarTitle() {
		return 0;
	}

	@Override
	public int getBarTitleWithPath() {
		return 0;
	}

	@Override
	public String getCustomTitle() {
		return null;
	}

	@Override
	public void initComponents() {
		btnOK = (Button) innerView.findViewById(R.id.btnOK);
		tvWeatherText = (TextView) innerView.findViewById(R.id.tvWeatherText);
	}

	@Override
	public void initEvents() {
		btnOK.setOnClickListener(this);
	}

	@Override
	public void initLogic() {
		WeatherInfo weather = (WeatherInfo) getActivity().getIntent().getSerializableExtra("weather");
		// TODO: show weather info
		tvWeatherText.setText(weather.index_d);
		
	}

	@Override
	public int getFragmentLayoutResId() {
		return R.layout.dialog_weather;
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

	}

	@Override
	public Bundle getFragmentState() {
		return null;
	}

	@Override
	public void onClick(View v) {
		getActivity().finish();

	}
}
