package com.netsdl.android.main.view;

import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netsdl.android.common.Util;
import com.netsdl.android.common.db.StoreMaster;

public class Login {
	public static final int LAYOUT_COMMON33 = R.layout.common33;
	final LayoutInflater inflater;
	final View view;
	final LinearLayout linearLayoutLogin;
	final FrameLayout coreLayout;
	MainActivity parent;
	Status status;
	Object[] storeObjs;

	public Login(MainActivity parent) {
		this.parent = parent;
		status = Status.operaterID;

		inflater = LayoutInflater.from(parent);
		view = inflater.inflate(LAYOUT_COMMON33, null);
		linearLayoutLogin = (LinearLayout) inflater.inflate(R.layout.login,
				null);

		LinearLayout row1 = (LinearLayout) view.findViewById(R.id.row1);
		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) row1
				.getLayoutParams();
		params.weight = 3;
		row1.setLayoutParams(params);

		
		LinearLayout row2 = (LinearLayout) view.findViewById(R.id.row2);
		params = (android.widget.LinearLayout.LayoutParams) row2
				.getLayoutParams();
		params.weight = 0.5f;
		row2.setLayoutParams(params);

		LinearLayout row3 = (LinearLayout) view.findViewById(R.id.row3);
		params = (android.widget.LinearLayout.LayoutParams) row3
				.getLayoutParams();
		params.weight = 1;
		row3.setLayoutParams(params);
		
		LinearLayout column1 = (LinearLayout) view.findViewById(R.id.column1);
		params = (android.widget.LinearLayout.LayoutParams) column1
				.getLayoutParams();
		params.weight = 3;
		column1.setLayoutParams(params);

		
		LinearLayout column2 = (LinearLayout) view.findViewById(R.id.column2);
		column2.getLayoutParams();
		params = (android.widget.LinearLayout.LayoutParams) column2
				.getLayoutParams();
		params.weight = 1;
		column2.setLayoutParams(params);

		LinearLayout column3 = (LinearLayout) view.findViewById(R.id.column3);
		params = (android.widget.LinearLayout.LayoutParams) column3
				.getLayoutParams();
		params.weight = 3;
		column3.setLayoutParams(params);
		
		coreLayout = (FrameLayout) view.findViewById(R.id.core);
	}

	public void init() {

		parent.setContentView(view);
		coreLayout.removeAllViews();
		coreLayout.addView(linearLayoutLogin);

		final EditText editText = (EditText) parent.findViewById(R.id.editText);

		int[] buttons = new int[] { R.id.button0, R.id.button1, R.id.button2,
				R.id.button3, R.id.button4, R.id.button5, R.id.button6,
				R.id.button7, R.id.button8, R.id.button9 };

		for (int i : buttons) {
			final Button button = (Button) parent.findViewById(i);

			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					editText.setText(editText.getText().toString()
							+ Integer.parseInt(button.getText().toString()));
				}
			});
		}

		final Button buttonBack = (Button) parent.findViewById(R.id.buttonBack);

		buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (editText.getText().length() <= 0) {
					if (status == Status.operaterID)
						return;
					status = Status.operaterID;
					((TextView) parent.findViewById(R.id.status))
							.setText(R.string.username);
					((TextView) parent.findViewById(R.id.textViewUsername))
							.setText("");
					((TextView) parent.findViewById(R.id.textViewUsernameFixed))
							.setText("");

				} else {
					editText.setText(editText.getText().delete(
							editText.getText().length() - 1,
							editText.getText().length()));
				}
			}
		});

		final Button buttonClear = (Button) parent
				.findViewById(R.id.buttonClear);
		buttonClear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (editText.getText().length() <= 0)
					return;
				editText.setText("");
			}
		});

		final Button buttonReturn = (Button) parent
				.findViewById(R.id.buttonReturn);
		buttonReturn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (status == Status.operaterID) {
					try {
						int iTemp = Integer.parseInt(editText.getText()
								.toString());
						storeObjs = parent.storeMaster
								.getSingleColumn(new Object[] { iTemp });
						if (storeObjs == null) {

							Toast.makeText(parent, R.string.msg_no_id,
									Toast.LENGTH_SHORT).show();
							editText.setText("");
						} else {

							status = Status.password;
							String name = (String) parent.storeMaster
									.getColumnValue(storeObjs,
											StoreMaster.COLUMN_NAME);

							((TextView) parent
									.findViewById(R.id.textViewUsername))
									.setText(name);

							((TextView) parent
									.findViewById(R.id.textViewUsernameFixed))
									.setText(R.string.username);

							((TextView) parent.findViewById(R.id.status))
									.setText(R.string.password);

							editText.setText("");
							editText.setTransformationMethod(new PasswordTransformationMethod());

						}

					} catch (NumberFormatException nfe) {
						Toast.makeText(parent, R.string.msg_id_must_be_number,
								Toast.LENGTH_SHORT).show();
						editText.setText("");
					}

				} else {
					if (editText.getText().toString().length() > 0) {
						String str1 = Util.getMD5(editText.getText()
								.toString());
						String str2 = (String) parent.storeMaster
								.getColumnValue(storeObjs,
										StoreMaster.COLUMN_MD5);
						if (str1.equals(str2)) {
							// parent.main.init();
							//parent.type.init();
							parent.function.init();
						} else {
							Toast.makeText(parent, R.string.msg_password_wrong,
									Toast.LENGTH_SHORT).show();
							editText.setText("");
						}

					}

				}
			}
		});

	}

	public enum Status {
		operaterID, password
	}

}
