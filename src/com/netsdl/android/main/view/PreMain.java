package com.netsdl.android.main.view;

import com.netsdl.android.common.db.DatabaseHelper;
import com.netsdl.android.common.db.DeviceMaster;
import com.netsdl.android.common.db.StoreMaster;
import com.netsdl.android.common.Util;
import com.netsdl.android.main.R;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
			initType1();
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

		final Button buttonBack = (Button) parent.findViewById(R.id.buttonBack);
		buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				parent.function.init();
			}
		});

		final Button buttonEnter = (Button) parent
				.findViewById(R.id.buttonEnter);
		buttonEnter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				parent.main.init();
			}
		});

		((EditText) parent.findViewById(R.id.editDocumentDate))
				.setInputType(InputType.TYPE_NULL);

	}

	private void initType1() {
		linearLayoutType = (LinearLayout) inflater
				.inflate(R.layout.type1, null);
		coreLayout.addView(linearLayoutType);

		String strDeviceId = Util.getLocalDeviceId(parent);
		if (strDeviceId == null || strDeviceId.trim().length() == 0)
			strDeviceId = Util.DEFAULT_LOCAL_DEVICE_ID;
		try {
			Object[] deviceMasterObjs = DatabaseHelper.getSingleColumn(
					parent.getContentResolver(), new Object[] { "1",
							strDeviceId }, DeviceMaster.class);
			if (deviceMasterObjs == null) {
				strDeviceId = Util.DEFAULT_LOCAL_DEVICE_ID;
				deviceMasterObjs = DatabaseHelper.getSingleColumn(
						parent.getContentResolver(), new Object[] { "1",
								strDeviceId }, DeviceMaster.class);
			}

			if (deviceMasterObjs != null) {
				String strShop = (String) DatabaseHelper.getColumnValue(
						deviceMasterObjs, DeviceMaster.COLUMN_FIELD_01,
						DeviceMaster.COLUMNS);
				((EditText) parent.findViewById(R.id.editShop))
						.setText(strShop);

				String strCustName = (String) DatabaseHelper.getColumnValue(
						deviceMasterObjs, DeviceMaster.COLUMN_FIELD_02,
						DeviceMaster.COLUMNS);
				((EditText) parent.findViewById(R.id.editCustomer))
						.setText(strCustName);
				
				String strSalesType = (String) DatabaseHelper.getColumnValue(
						deviceMasterObjs, DeviceMaster.COLUMN_FIELD_03,
						DeviceMaster.COLUMNS);
				((EditText) parent.findViewById(R.id.editSalesType))
						.setText(strSalesType);
				

			}

		} catch (IllegalArgumentException e1) {
		} catch (SecurityException e1) {
		} catch (IllegalAccessException e1) {
		} catch (NoSuchFieldException e1) {
		}

	}

}
