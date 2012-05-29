package com.netsdl.android.main.view;

import com.netsdl.android.main.R;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class Type {
	public static final int LAYOUT_COMMON33 = R.layout.common33;
	final View view;
	final LayoutInflater inflater;
	final LinearLayout linearLayoutType;
	final FrameLayout coreLayout;

	public InnerType type = InnerType.type1;
	DocumentType dt = DocumentType.D0;
	RtnType rt = RtnType.P1;

	MainActivity parent;

	public Type(MainActivity parent) {
		this.parent = parent;
		inflater = LayoutInflater.from(parent);
		view = inflater.inflate(LAYOUT_COMMON33, null);
		linearLayoutType = (LinearLayout) inflater.inflate(R.layout.type, null);
		coreLayout = (FrameLayout) view.findViewById(R.id.core);

	}

	public void init() {
		parent.setContentView(view);
		coreLayout.removeAllViews();
		coreLayout.addView(linearLayoutType);

		setType();

		initType();

	}

	private void setType() {
		int[] ids = new int[] { R.id.buttonType1, R.id.buttonType2,
				R.id.buttonType3 };
		for (int id : ids) {
			((Button) parent.findViewById(id))
					.setBackgroundColor(Color.TRANSPARENT);
		}
		((Button) parent.findViewById(ids[InnerType.getIndex(type)]))
				.setBackgroundColor(Color.CYAN);
	}

	private void initType() {
		int[] ids = new int[] { R.id.buttonType1, R.id.buttonType2,
				R.id.buttonType3 };
		for (final int id : ids) {
			((Button) parent.findViewById(id))
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							type = InnerType.getType(id);
							setType();
							switch (type) {
							case type1:
								dt = DocumentType.D0;
								rt = RtnType.P1;
								break;
							case type2:
								dt = DocumentType.D0;
								rt = RtnType.M1;
								break;

							case type3:
								dt = DocumentType.R0;
								rt = RtnType.M1;
								break;
							// case type4:
							// dt = DocumentType.R0;
							// rt = RtnType.P1;
							// break;
							}
							parent.main.init();

						}
					});
		}

	}

	public enum InnerType {
		type1, type2, type3;
		static InnerType[] types = new InnerType[] { type1, type2, type3 };
		static int[] ids = new int[] { R.id.buttonType1, R.id.buttonType2,
				R.id.buttonType3 };

		public static InnerType getType(int id) {
			return types[getIndex(id)];
		}

		public static int getIndex(int id) {
			for (int i = 0; i < types.length; i++)
				if (ids[i] == id)
					return i;
			return -1;
		}

		public static int getIndex(InnerType type) {
			for (int i = 0; i < types.length; i++)
				if (types[i] == type)
					return i;
			return -1;
		}
	}

	public enum DocumentType {
		D0 {
			public String toString() {
				return "DO";
			}
		},
		R0 {
			public String toString() {
				return "RO";
			}
		};

	}

	public enum RtnType {
		P1 {
			public String toString() {
				return "1";
			}
		},
		M1 {
			public String toString() {
				return "-1";
			}
		}
	}
}
