package com.netsdl.android.main.view;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.netsdl.android.common.Structs;
import com.netsdl.android.common.Structs.Item;
import com.netsdl.android.common.Structs.Type;
import com.netsdl.android.common.Structs.DeviceItem;
import com.netsdl.android.common.db.DatabaseHelper;
import com.netsdl.android.common.db.DbMaster;
import com.netsdl.android.common.db.PaymentMaster;
import com.netsdl.android.common.db.PosTable;
import com.netsdl.android.common.db.SkuMaster;
import com.netsdl.android.common.db.StoreMaster;
import com.netsdl.android.common.view.dialog.Dialogable;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

	public Map<Integer, Dialogable> mapDialogable;

	public Map<Integer, Object[]> mapSkuMaster;

	public Map<Integer, Object[]> mapStoreMaster;

	public Map<Integer, Object[]> mapPaymentMaster;

	public Map<Integer, Item> mapItem;

	public Map<Integer, BigDecimal> mapPay;

	public PosTable posTable = null;

	public Login login = null;

	public Function function = null;

	public PreMain preMain = null;

	public Type type = null;

	public DeviceItem deviceItem;

	public Main main = null;

	public MainActivity() {

		mapDialogable = new HashMap<Integer, Dialogable>();
		mapStoreMaster = new HashMap<Integer, Object[]>();
		mapSkuMaster = new HashMap<Integer, Object[]>();
		mapPaymentMaster = new HashMap<Integer, Object[]>();
		mapItem = new HashMap<Integer, Item>();
		//mapPay = new HashMap<Integer, BigDecimal>();
		mapPay = new TreeMap<Integer, BigDecimal>(new Comparator<Integer>() {
			public int compare(Integer lhs, Integer rhs) {
				try {
					Object[] objs = DatabaseHelper.getSingleColumn(
							getContentResolver(), new Object[] { lhs },
							new String[] { PaymentMaster.COLUMN_ID },
							PaymentMaster.class);
					Integer lhsSort = (Integer) DatabaseHelper.getColumnValue(
							objs, PaymentMaster.COLUMN_SORT,
							PaymentMaster.COLUMNS);

					objs = DatabaseHelper.getSingleColumn(getContentResolver(),
							new Object[] { rhs },
							new String[] { PaymentMaster.COLUMN_ID },
							PaymentMaster.class);
					Integer rhsSort = (Integer) DatabaseHelper.getColumnValue(
							objs, PaymentMaster.COLUMN_SORT,
							PaymentMaster.COLUMNS);
					return lhsSort - rhsSort;

				} catch (IllegalArgumentException e) {
				} catch (SecurityException e) {
				} catch (IllegalAccessException e) {
				} catch (NoSuchFieldException e) {
				}
				return 0;
			}
		});

		// posTable = new PosTable(this);

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		login = new Login(this);
		function = new Function(this);
		preMain = new PreMain(this);
		type = Structs.Type.type1;
		deviceItem = new Structs().new DeviceItem();
		main = new Main(this);

		login.init();

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialogable dialog = mapDialogable.get(id);
		if (dialog == null)
			return super.onCreateDialog(id);
		return dialog.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		if (mapDialogable.get(id) == null) {
			super.onPrepareDialog(id, dialog);
		} else {
			((Dialogable) mapDialogable.get(id)).onPrepareDialog(id, dialog);
		}
	}

}