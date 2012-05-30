package com.netsdl.android.main.view;

import com.netsdl.android.common.Structs.Type;
import com.netsdl.android.main.R;
import com.netsdl.android.main.view.Login.Status;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PreMain {
	public static final int LAYOUT_COMMON33 = R.layout.common33;
	final View view;
	final LayoutInflater inflater;
	LinearLayout linearLayoutType;
	final FrameLayout coreLayout;
	MainActivity parent;
	
	public PreMain(MainActivity parent) {
		this.parent = parent;
		inflater = LayoutInflater.from(parent);
		view = inflater.inflate(LAYOUT_COMMON33, null);
		
		coreLayout = (FrameLayout) view.findViewById(R.id.core);

	}

	public void init() {
		parent.setContentView(view);
		coreLayout.removeAllViews();
		
		switch (parent.type) {
		case type1:
			linearLayoutType = (LinearLayout) inflater.inflate(R.layout.type1,
					null);
			break;
		case type2:
			linearLayoutType = (LinearLayout) inflater.inflate(R.layout.type2,
					null);
			break;
		case type3:
			linearLayoutType = (LinearLayout) inflater.inflate(R.layout.type3,
					null);
			break;
		default:
			linearLayoutType = (LinearLayout) inflater.inflate(R.layout.type1,
					null);
		}
		
		
		coreLayout.addView(linearLayoutType);

		final Button buttonBack = (Button) parent.findViewById(R.id.buttonBack);
		buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				parent.function.init();
			}
		});
		
		final Button buttonEnter = (Button) parent.findViewById(R.id.buttonEnter);
		buttonEnter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				parent.main.init();
			}
		});

	}



}
