package com.netsdl.android.main.view;

import com.netsdl.android.common.Structs;
import com.netsdl.android.common.Structs.LoginViewData;
import com.netsdl.android.common.Structs.Type;
import com.netsdl.android.main.R;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class Function{
	public static final int LAYOUT_COMMON33 = R.layout.common33;
	final View view;
	final LayoutInflater inflater;
	final LinearLayout linearLayoutType;
	final FrameLayout coreLayout;
	MainActivity parent;

	int[] ids = new int[] { R.id.buttonType1, R.id.buttonType2,
			R.id.buttonType3, R.id.buttonFunction2 };
	Type[] types = new Type[] { Type.type1, Type.type2, Type.type3 };

	int currentID = R.id.buttonType1;

	public Function(MainActivity parent) {
		this.parent = parent;
		inflater = LayoutInflater.from(parent);
		view = inflater.inflate(LAYOUT_COMMON33, null);

		linearLayoutType = (LinearLayout) inflater.inflate(R.layout.function,
				null);

		coreLayout = (FrameLayout) view.findViewById(R.id.core);

	}

	public void init() {
		parent.status = MainActivity.Status.Function;
		parent.setContentView(view);
		coreLayout.removeAllViews();
		coreLayout.addView(linearLayoutType);

		setFunction();

		initFunction();
		
		final Button buttonBack = (Button) parent.findViewById(R.id.buttonBack);
		buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				parent.login.data = new Structs().new LoginViewData();
				parent.login.init();
			}
		});

	}

	private void setFunction() {
		for (int id : ids) {
			((Button) parent.findViewById(id))
					.setBackgroundColor(Color.TRANSPARENT);
		}
		((Button) parent.findViewById(currentID))
				.setBackgroundColor(Color.LTGRAY);

		parent.type = types[getIndex(currentID)];

		// ((Button) parent.findViewById(ids[getIndex(currentID)]))
		// .setBackgroundColor(Color.CYAN);

	}

	private void initFunction() {
		for (final int id : ids) {
			((Button) parent.findViewById(id))
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							currentID = id;
							setFunction();
							parent.preMain.init();
						}
					});
		}

	}

	private int getIndex(int id) {
		for (int i = 0; i < ids.length; i++)
			if (ids[i] == id)
				return i;
		return -1;
	}

}
