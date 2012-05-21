package com.netsdl.android.main.view;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.netsdl.android.common.Structs.Item;
import com.netsdl.android.common.db.DbMaster;
import com.netsdl.android.common.db.PaymentMaster;
import com.netsdl.android.common.db.PosTable;
import com.netsdl.android.common.db.SkuMaster;
import com.netsdl.android.common.db.StoreMaster;
import com.netsdl.android.common.view.dialog.Dialogable;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

public class MainActivity extends Activity {

	public Map<Integer, Dialogable> mapDialogable;

	public Map<Integer, Object[]> mapSkuMaster;

	public Map<Integer, Object[]> mapStoreMaster;

	public Map<Integer, Object[]> mapPaymentMaster;

	public Map<Integer, Item> mapItem;

	public Map<Integer, BigDecimal> mapPay;

	public DbMaster dbMaster = null;

	public SkuMaster skuMaster = null;

	public StoreMaster storeMaster = null;

	public PaymentMaster paymentMaster = null;

	public PosTable posTable = null;

	public Login login = null;

	public Function function = null;

	public Type type = null;

	public Main main = null;

	public MainActivity() {
		
		mapDialogable = new HashMap<Integer, Dialogable>();
		mapStoreMaster = new HashMap<Integer, Object[]>();
		mapSkuMaster = new HashMap<Integer, Object[]>();
		mapPaymentMaster = new HashMap<Integer, Object[]>();
		mapItem = new HashMap<Integer, Item>();
		mapPay = new HashMap<Integer, BigDecimal>();
		dbMaster = new DbMaster(this);
		skuMaster = new SkuMaster(this);
		storeMaster = new StoreMaster(this);
		paymentMaster = new PaymentMaster(this);
		posTable = new PosTable(this);

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		login = new Login(this);
		function = new Function(this);
		type = new Type(this);
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