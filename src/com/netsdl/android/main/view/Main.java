package com.netsdl.android.main.view;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.netsdl.android.common.Structs;
import com.netsdl.android.common.Util;
import com.netsdl.android.common.Structs.Item;
import com.netsdl.android.common.db.DatabaseHelper;
import com.netsdl.android.common.db.PaymentMaster;
import com.netsdl.android.common.db.PosTable;
import com.netsdl.android.common.db.SkuMaster;
import com.netsdl.android.common.db.StoreMaster;
import com.netsdl.android.main.R;
import com.netsdl.android.main.view.list.ItemList;
import com.netsdl.android.main.view.list.PayList;

public class Main {
	public final LayoutInflater inflater;
	public static final int LAYOUT_MAIN = R.layout.main;
	public MainActivity parent;
	ItemList itemList;
	PayList payList;

	Status status = Status.barcode;
	final View view;
	final FrameLayout underInputLayout;
	final LinearLayout linearLayoutBarcodeSearch;
	final LinearLayout linearLayoutPayMethod;
	final EditText editSearch;

	final Button buttonPay;
	final Button buttonConfirm;
	final Button buttonReturn;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Main(MainActivity parent) {
		this.parent = parent;
		itemList = new ItemList(this);
		payList = new PayList(this);

		inflater = LayoutInflater.from(parent);
		view = inflater.inflate(LAYOUT_MAIN, null);

		underInputLayout = (FrameLayout) view
				.findViewById(R.id.underInputLayout);

		linearLayoutBarcodeSearch = (LinearLayout) inflater.inflate(
				R.layout.barcode_search, null);
		linearLayoutPayMethod = (LinearLayout) inflater.inflate(
				R.layout.paymethod, null);

		editSearch = (EditText) view.findViewById(R.id.editSearch);

		buttonPay = (Button) view.findViewById(R.id.buttonPay);
		buttonConfirm = (Button) view.findViewById(R.id.buttonConfirm);
		buttonReturn = (Button) view.findViewById(R.id.buttonReturn);

	}

	public void init() {

		parent.setContentView(view);
		// parent.setContentView(LAYOUT_MAIN);

		underInputLayout.removeAllViews();
		underInputLayout.addView(linearLayoutBarcodeSearch);

		EditSearchTextWatcher editSearchTextWatcher = new EditSearchTextWatcher(
				parent);
		editSearch.addTextChangedListener(editSearchTextWatcher);

		initEditSearch();

		initNumberKey();

		initButtonPay();

		initLinearLayoutPayMethod();

		itemList.init();
		payList.init();

	}

	private void initNumberKey() {
		int[] buttons = new int[] { R.id.button0, R.id.button1, R.id.button2,
				R.id.button3, R.id.button4, R.id.button5, R.id.button6,
				R.id.button7, R.id.button8, R.id.button9 };
		for (int i : buttons) {
			final Button button = (Button) parent.findViewById(i);

			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					editSearch.setText(editSearch.getText().toString()
							+ Integer.parseInt(button.getText().toString()));
				}
			});
		}

		((Button) parent.findViewById(R.id.buttonConfirm))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if (status == Status.barcode) {
							procBarCodeSearch();
						} else {
							underInputLayout.removeAllViews();
							underInputLayout.addView(linearLayoutBarcodeSearch);
							status = Status.barcode;
							editSearch.setText("");
						}

						setButtonPay();

					}

				});

		((Button) parent.findViewById(R.id.buttonReturn))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						parent.preMain.init();
					}
				});

		final Button buttonBack = (Button) parent.findViewById(R.id.buttonBack);

		buttonBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (editSearch.getText().length() <= 0) {

				} else {
					editSearch.setText(editSearch.getText().delete(
							editSearch.getText().length() - 1,
							editSearch.getText().length()));
				}
				setButtonPay();
			}
		});

		final Button buttonClear = (Button) parent
				.findViewById(R.id.buttonClear);
		buttonClear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (editSearch.getText().length() <= 0)
					return;
				editSearch.setText("");
			}
		});
	}

	private void initButtonPay() {

		buttonPay.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (status == Status.barcode) {
					underInputLayout.removeAllViews();
					underInputLayout.addView(linearLayoutPayMethod);
					status = Status.payment;
					editSearch.setText("");

				} else {
					closeThis();
					// init();
					parent.mapSkuMaster = new HashMap<Integer, Object[]>();
					// parent.mapPaymentMaster = new HashMap<Integer,
					// Object[]>();
					parent.mapItem = new HashMap<Integer, Item>();
					parent.mapPay = new HashMap<Integer, BigDecimal>();

					underInputLayout.removeAllViews();
					underInputLayout.addView(linearLayoutBarcodeSearch);
					status = Status.barcode;

				}
				setTotal();
				setButtonPay();

			}
		});
	}

	private void initEditSearch() {
		editSearch.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_UP) {
					procBarCodeSearch();
					return true;
				}
				return false;
			}
		});

	}

	public BigDecimal getTotalPay() {
		BigDecimal bigDecimal = new BigDecimal(0);
		for (Entry<Integer, BigDecimal> entry : parent.mapPay.entrySet()) {
			// Integer id = entry.getKey();
			BigDecimal pay = entry.getValue();
			bigDecimal = bigDecimal.add(pay);
		}

		return bigDecimal;
	}

	public BigDecimal getTotalItemStdPrice() {
		BigDecimal bigDecimal = new BigDecimal(0);

		for (Entry<Integer, Item> entry : parent.mapItem.entrySet()) {
			Item item = entry.getValue();

			// bigDecimal = bigDecimal.add(((BigDecimal) parent.mapSkuMaster
			// .get(skuID)[parent.skuMaster
			// .getColumnIndex(SkuMaster.COLUMN_ITEM_STD_PRICE)])
			// .multiply(new BigDecimal(item.count)));

			bigDecimal = bigDecimal.add(item.lumpSum);

		}

		return bigDecimal;
	}

	private void procBarCodeSearch() {
		String str = editSearch.getText().toString();
		if (str.length() == 0)
			return;
		try {
			Object[] objs = DatabaseHelper
					.getSingleColumn(parent.getContentResolver(),
							new Object[] { str },
							new String[] { SkuMaster.COLUMN_BAR_CODE },
							SkuMaster.class);

			// Object[] objs = parent.skuMaster.getSingleColumn(
			// new Object[] { str },
			// new String[] { SkuMaster.COLUMN_BAR_CODE });

			if (objs != null) {

				Integer skuId = (Integer) DatabaseHelper.getColumnValue(objs,
						SkuMaster.COLUMN_SKU_ID, SkuMaster.COLUMNS);

				if (!parent.mapSkuMaster.containsKey(skuId)) {
					parent.mapSkuMaster.put(skuId, objs);
				}
				if (!parent.mapItem.containsKey(skuId)) {
					Item item = new Structs().new Item();
					item.count = 1;

					item.price = (BigDecimal) DatabaseHelper.getColumnValue(
							objs, SkuMaster.COLUMN_ITEM_STD_PRICE,
							SkuMaster.COLUMNS);
					item.price.multiply(new BigDecimal(item.count));
					item.lumpSum = item.price.multiply(new BigDecimal(
							item.count));
					parent.mapItem.put(skuId, item);
				} else {
					Item item = parent.mapItem.get(skuId);
					item.count += 1;

					item.price = (BigDecimal) DatabaseHelper.getColumnValue(
							objs, SkuMaster.COLUMN_ITEM_STD_PRICE,
							SkuMaster.COLUMNS);
					item.lumpSum = item.price.multiply(new BigDecimal(
							item.count));

					parent.mapItem.put(skuId, item);
				}

				listViewNotifyDataSetChanged(R.id.listViewItem);

				setTotal();
				editSearch.setText("");

				setButtonPay();

				// Log.d("textView.getText()", textView.getText().toString());
			} else {
				Toast.makeText(parent, R.string.msg_no_sku, Toast.LENGTH_SHORT)
						.show();
				editSearch.setText("");
			}
		} catch (NumberFormatException nfe) {
		} catch (IllegalArgumentException e) {
		} catch (SecurityException e) {
		} catch (IllegalAccessException e) {
		} catch (NoSuchFieldException e) {
		}
	}

	public void setButtonPay() {
		if (status == Status.barcode) {
			buttonPay.setText(R.string.Pay);
			buttonPay.setEnabled(parent.mapItem.size() > 0);
			buttonConfirm.setText(R.string.Confirm);
			buttonConfirm
					.setEnabled(editSearch.getText().toString().length() > 0);
		} else {
			buttonPay.setText(R.string.Close);
			buttonPay.setEnabled(parent.mapPay.size() > 0);
			buttonConfirm.setText(R.string.Return);
			buttonConfirm.setEnabled(true);
		}

	}

	private void initLinearLayoutPayMethod() {
		int[] buttonPays = new int[] { R.id.buttonPay1, R.id.buttonPay2,
				R.id.buttonPay3, R.id.buttonPay4, R.id.buttonPay5,
				R.id.buttonPay6, R.id.buttonPay7, R.id.buttonPay8 };

		// final Object[][] objss = parent.paymentMaster.getMultiColumn(
		// new String[] {}, new String[] {}, null, null,
		// new String[] { PaymentMaster.COLUMN_SORT }, null, true);

		try {
			final Object[][] objss = DatabaseHelper.getMultiColumn(
					parent.getContentResolver(), new String[] {},
					new String[] {}, null, null,
					new String[] { PaymentMaster.COLUMN_SORT }, null, true,
					PaymentMaster.class);
			for (int i = 0; i < buttonPays.length; i++) {
				Button buttonPay = (Button) linearLayoutPayMethod
						.findViewById(buttonPays[i]);
				if (i < objss.length) {

					buttonPay.setText((String) DatabaseHelper.getColumnValue(
							objss[i], PaymentMaster.COLUMN_NAME,
							PaymentMaster.COLUMNS));

					final int id = (Integer) DatabaseHelper.getColumnValue(
							objss[i], PaymentMaster.COLUMN_ID,
							PaymentMaster.COLUMNS);

					parent.mapPaymentMaster.put(id, objss[i]);

					buttonPay.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							if (editSearch.getText().toString().trim().length() <= 0)
								return;
							parent.mapPay.put(id, new BigDecimal(editSearch
									.getText().toString()));
							editSearch.setText("");

							listViewNotifyDataSetChanged(R.id.listViewPay);

							setTotal();

							setButtonPay();
						}
					});
				} else {
					buttonPay.setText("");
					buttonPay.setEnabled(false);
				}

			}

		} catch (IllegalArgumentException e) {
		} catch (SecurityException e) {
		} catch (IllegalAccessException e) {
		} catch (NoSuchFieldException e) {
		}

	}

	private void closeThis() {
		String strUUID = Util.getUUID();
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(System.currentTimeMillis());
		String timestamp = sdf.format(now.getTime());

		for (Entry<Integer, Item> entry : parent.mapItem.entrySet()) {
			Integer id = entry.getKey();
			Item item = entry.getValue();

			try {
				DatabaseHelper.insert(parent.getContentResolver(),
						new String[] { strUUID, timestamp, timestamp,
								parent.type.toDocumentType().toString(),
								parent.type.toRtnType().toString(), "wh_no",
								"wh_name", "cust_no", "cust_name", "user_no",
								"user_name", "SKU", "sku_cd", "item_name",
								"2680.00", "2078.00", "2078.00", "1",
								"2680.00", "2078.00" }, PosTable.class);
			} catch (IllegalArgumentException e) {
			} catch (SecurityException e) {
			} catch (IllegalAccessException e) {
			} catch (NoSuchFieldException e) {
			}

		}
		for (Entry<Integer, BigDecimal> entry : parent.mapPay.entrySet()) {
			Integer id = entry.getKey();
			BigDecimal count = entry.getValue();

			// try {
			// DatabaseHelper.insert(parent.getContentResolver(), new String[] {
			// strUUID, PosTable.FLG_P,
			// id.toString(), count.toString(), timestamp }, PosTable.class);
			// } catch (IllegalArgumentException e) {
			// } catch (SecurityException e) {
			// } catch (IllegalAccessException e) {
			// } catch (NoSuchFieldException e) {
			// }

			// parent.posTable.insert(new String[] { strUUID, PosTable.FLG_P,
			// id.toString(), count.toString(), timestamp });
		}

	}

	class EditSearchTextWatcher implements TextWatcher {
		Context context;

		public EditSearchTextWatcher(Context context) {
			this.context = context;
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			setButtonPay();

		}

		public void afterTextChanged(Editable s) {

		}

	}

	public void listViewNotifyDataSetChanged(int id) {
		ListView listView = (ListView) parent.findViewById(id);
		((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
	}

	public void setTotal() {
		((TextView) parent.findViewById(R.id.all_item_fee))
				.setText(getTotalItemStdPrice().toString());

		((TextView) parent.findViewById(R.id.all_pay_fee))
				.setText(getTotalPay().toString());

		((TextView) parent.findViewById(R.id.textViewQty))
				.setText(getTotalPay().subtract(getTotalItemStdPrice())
						.toString());

	}

	public enum Status {
		barcode, payment
	}

}
