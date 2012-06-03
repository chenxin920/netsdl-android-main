package com.netsdl.android.main.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.netsdl.android.common.db.DatabaseHelper;
import com.netsdl.android.common.db.DeviceMaster;
import com.netsdl.android.common.db.StoreMaster;
import com.netsdl.android.common.Util;
import com.netsdl.android.main.R;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class PreMain {
	public static final int LAYOUT_COMMON33 = R.layout.common33;
	final View view;
	final LayoutInflater inflater;
	LinearLayout linearLayoutType;
	final FrameLayout coreLayout;
	MainActivity parent;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

		((EditText) parent.findViewById(R.id.editDocumentDate))
				.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {

						try {
							if (event.getAction() == MotionEvent.ACTION_DOWN) {
								Date date = sdf
										.parse(parent.deviceItem.field_04);
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(date);

								new DatePickerDialog(
										parent,
										new OnDateSetListener() {

											@Override
											public void onDateSet(
													DatePicker view, int year,
													int monthOfYear,
													int dayOfMonth) {
												Calendar calendar = Calendar
														.getInstance();
												calendar.set(Calendar.YEAR,
														year);
												calendar.set(Calendar.MONTH,
														monthOfYear);
												calendar.set(
														Calendar.DAY_OF_MONTH,
														dayOfMonth);
												String strDocumentDate = sdf
														.format(calendar
																.getTime());

												parent.deviceItem.field_04 = strDocumentDate;
												((EditText) parent
														.findViewById(R.id.editDocumentDate))
														.setText(strDocumentDate);

											}
										}, calendar.get(Calendar.YEAR),
										calendar.get(Calendar.MONTH), calendar
												.get(Calendar.DAY_OF_MONTH))
										.show();
							}
							return true;

						} catch (ParseException e) {
						}

						return false;
					}
				});

		((EditText) parent.findViewById(R.id.editDocumentDate))
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
					}
				});

		((Spinner) parent.findViewById(R.id.spinnerOperator))
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> adapterView,
							View view, int position, long id) {
						parent.deviceItem.field_05 = adapterView
								.getItemAtPosition(position).toString();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

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
				parent.deviceItem.deviceID = strDeviceId;
			}

			if (deviceMasterObjs != null) {
				String strShop = (String) DatabaseHelper.getColumnValue(
						deviceMasterObjs, DeviceMaster.COLUMN_FIELD_01,
						DeviceMaster.COLUMNS);
				((EditText) parent.findViewById(R.id.editShop))
						.setText(strShop);
				parent.deviceItem.field_01 = strShop;

				String strCustName = (String) DatabaseHelper.getColumnValue(
						deviceMasterObjs, DeviceMaster.COLUMN_FIELD_02,
						DeviceMaster.COLUMNS);
				((EditText) parent.findViewById(R.id.editCustomer))
						.setText(strCustName);
				parent.deviceItem.field_02 = strCustName;

				String strSalesType = (String) DatabaseHelper.getColumnValue(
						deviceMasterObjs, DeviceMaster.COLUMN_FIELD_03,
						DeviceMaster.COLUMNS);
				((EditText) parent.findViewById(R.id.editSalesType))
						.setText(strSalesType);
				parent.deviceItem.field_03 = strSalesType;

				String strDocumentDate = (String) DatabaseHelper
						.getColumnValue(deviceMasterObjs,
								DeviceMaster.COLUMN_FIELD_04,
								DeviceMaster.COLUMNS);
				if (strDocumentDate == null
						|| strDocumentDate.trim().length() == 0) {
					Calendar now = Calendar.getInstance();
					now.setTimeInMillis(System.currentTimeMillis());
					strDocumentDate = sdf.format(now.getTime());
				}
				parent.deviceItem.field_04 = strDocumentDate;
				((EditText) parent.findViewById(R.id.editDocumentDate))
						.setText(strDocumentDate);

				String strOperator = (String) DatabaseHelper.getColumnValue(
						deviceMasterObjs, DeviceMaster.COLUMN_FIELD_05,
						DeviceMaster.COLUMNS);
				Spinner spinnerOperator = (Spinner) parent
						.findViewById(R.id.spinnerOperator);
				String[] spinnerOperatorItems = strOperator.split(":");
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,
						android.R.layout.simple_spinner_item,
						spinnerOperatorItems);
				spinnerOperator.setAdapter(adapter);
				parent.deviceItem.field_05 = spinnerOperatorItems[0];

				String strRemarks = (String) DatabaseHelper.getColumnValue(
						deviceMasterObjs, DeviceMaster.COLUMN_FIELD_06,
						DeviceMaster.COLUMNS);
				((EditText) parent.findViewById(R.id.editRemarks))
						.setText(strRemarks);
				parent.deviceItem.field_06 = strRemarks;
			}

		} catch (IllegalArgumentException e1) {
		} catch (SecurityException e1) {
		} catch (IllegalAccessException e1) {
		} catch (NoSuchFieldException e1) {
		}

	}

}
