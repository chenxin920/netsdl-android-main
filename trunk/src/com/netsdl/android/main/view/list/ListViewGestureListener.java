package com.netsdl.android.main.view.list;

import com.netsdl.android.common.view.list.Currentable;
import com.netsdl.android.main.R;

import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Button;

public class ListViewGestureListener extends SimpleOnGestureListener {
	Currentable current;

	public ListViewGestureListener(Currentable current) {
		this.current = current;
	}

	public boolean onDown(MotionEvent e) {
		if (current.getPosition() >= 0 && current.getView() != null) {
			Button buttonDelete = (Button) current.getView().findViewById(
					R.id.buttonDelete);
			buttonDelete.setVisibility(View.GONE);
		}
		return false;
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		final int X = 20;
		final int Y = 10;
		if (X + distanceX < 0 && Math.abs(distanceY) < Y)
			if (current.getPosition() >= 0 && current.getView() != null) {
				Button buttonDelete = (Button) current.getView().findViewById(
						R.id.buttonDelete);
				buttonDelete.setVisibility(View.VISIBLE);
			}
		return false;
	}

}