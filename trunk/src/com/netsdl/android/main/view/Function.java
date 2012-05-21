package com.netsdl.android.main.view;

import com.netsdl.android.main.view.Type.DocumentType;
import com.netsdl.android.main.view.Type.InnerType;
import com.netsdl.android.main.view.Type.RtnType;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class Function {
	public static final int LAYOUT_COMMON33 = R.layout.common33;
	final View view;
	final LayoutInflater inflater;
	final LinearLayout linearLayoutType;
	final FrameLayout coreLayout;
	public InnerFunction function = InnerFunction.function1;
	MainActivity parent;

	public Function(MainActivity parent) {
		this.parent = parent;
		inflater = LayoutInflater.from(parent);
		view = inflater.inflate(LAYOUT_COMMON33, null);
		linearLayoutType = (LinearLayout) inflater.inflate(R.layout.function,
				null);
		coreLayout = (FrameLayout) view.findViewById(R.id.core);

	}

	public void init() {
		parent.setContentView(view);
		coreLayout.removeAllViews();
		coreLayout.addView(linearLayoutType);

		setFunction();

		initFunction();

	}

	private void setFunction() {
		int[] ids = new int[] { R.id.buttonFunction1, R.id.buttonFunction2 };
		for (int id : ids) {
			((Button) parent.findViewById(id))
					.setBackgroundColor(Color.TRANSPARENT);
		}
		((Button) parent.findViewById(ids[InnerFunction.getIndex(function)]))
				.setBackgroundColor(Color.CYAN);

	}

	private void initFunction() {
		int[] ids = new int[] { R.id.buttonFunction1, R.id.buttonFunction2 };
		for (final int id : ids) {

			((Button) parent.findViewById(id))
					.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							function = InnerFunction.getType(id);
							setFunction();
							parent.type.init();

						}
					});
		}

	}

	public enum InnerFunction {
		function1, function2;
		static InnerFunction[] functions = new InnerFunction[] { function1,
				function2 };
		static int[] ids = new int[] { R.id.buttonFunction1,
				R.id.buttonFunction2 };

		public static InnerFunction getType(int id) {
			return functions[getIndex(id)];
		}

		public static int getIndex(int id) {
			for (int i = 0; i < functions.length; i++)
				if (ids[i] == id)
					return i;
			return -1;
		}

		public static int getIndex(InnerFunction function) {
			for (int i = 0; i < functions.length; i++)
				if (functions[i] == function)
					return i;
			return -1;
		}
	}
}
