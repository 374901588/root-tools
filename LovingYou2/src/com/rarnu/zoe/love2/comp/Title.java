package com.rarnu.zoe.love2.comp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.rarnu.zoe.love2.R;
import com.rarnu.zoe.loving.utils.UIUtils;

public class Title extends RelativeLayout {

	public static final int BARITEM_LEFT = 1;
	public static final int BARITEM_RIGHT = 2;
	public static final int BARITEM_CENTER = 3;

	BarItem barLeft, barRight, barCenter;

	public Title(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public Title(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Title(Context context) {
		super(context);
		init();
	}

	private void init() {
		setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, UIUtils.dipToPx(52)));
		View v = inflate(getContext(), R.layout.comp_title, null);
		v.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, UIUtils.dipToPx(52)));
		addView(v);

		barLeft = (BarItem) findViewById(R.id.barLeft);
		barRight = (BarItem) findViewById(R.id.barRight);
		barCenter = (BarItem) findViewById(R.id.barCenter);
	}

	public BarItem getBarItem(int index) {
		switch (index) {
		case BARITEM_LEFT:
			return barLeft;
		case BARITEM_RIGHT:
			return barRight;
		case BARITEM_CENTER:
			return barCenter;
		default:
			return null;
		}
	}

}
