package com.netsdl.android.main.view.list;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.netsdl.android.common.db.DatabaseHelper;
import com.netsdl.android.common.db.PaymentMaster;
import com.netsdl.android.common.db.SkuMaster;
import com.netsdl.android.common.view.list.Currentable;
import com.netsdl.android.main.R;
import com.netsdl.android.main.view.Main;
import com.netsdl.android.main.view.MainActivity;

public class ItemList {
	MainActivity grandpa;
	Main parent;
	GestureDetector gestureDetector;
	ItemAdapter adapter;

	public ItemList(Main parent) {
		this.parent = parent;
		grandpa = parent.parent;
	}

	public void init() {
		ListView listView = (ListView) grandpa.findViewById(R.id.listViewItem);

		adapter = new ItemAdapter(grandpa);
		listView.setAdapter(adapter);

		gestureDetector = new GestureDetector(new ListViewGestureListener(
				adapter));

		listView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> adapterView,
					View view, int position, long id) {
				// showItemDialog(position);
				Integer[] skuIDs = grandpa.mapItem.keySet().toArray(
						new Integer[] {});
				ItemDialog itemDialog = new ItemDialog(grandpa, grandpa.mapItem
						.get(skuIDs[position]), position);
				grandpa.mapDialogable.put(itemDialog.hashCode(), itemDialog);
				grandpa.showDialog(itemDialog.hashCode());

				return false;
			}
		});

	}

	public class ItemAdapter extends BaseAdapter implements Currentable {
		private LayoutInflater mInflater;
		public int positionCurrent;
		public View viewCurrent;

		public ItemAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return grandpa.mapSkuMaster.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(final int position, View convertView,
				ViewGroup viewGroup) {
			ViewHolder holder = null;

			if (convertView == null) {

				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.item, null);
				holder.itemCatName = (TextView) convertView
						.findViewById(R.id.itemCatName);
				holder.skuPropName = (TextView) convertView
						.findViewById(R.id.skuPropName);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.count = (TextView) convertView.findViewById(R.id.count);
				holder.lumpSum = (TextView) convertView
						.findViewById(R.id.lumpSum);
				holder.buttonDelete = (Button) convertView
						.findViewById(R.id.buttonDelete);

				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			final Integer[] skuIDs = grandpa.mapItem.keySet().toArray(
					new Integer[] {});
			Object[] objs = grandpa.mapSkuMaster.get(skuIDs[position]);

			holder.itemCatName.setText((String) objs[DatabaseHelper
					.getColumnIndex(SkuMaster.COLUMN_ITEM_CAT_NAME,
							SkuMaster.COLUMNS)]);

			holder.skuPropName.setText((String) objs[DatabaseHelper
					.getColumnIndex(SkuMaster.COLUMN_SKU_PROP_1_NAME,
							SkuMaster.COLUMNS)]
					+ (String) objs[DatabaseHelper
							.getColumnIndex(SkuMaster.COLUMN_SKU_PROP_2_NAME,
									SkuMaster.COLUMNS)]);

			holder.price.setText(grandpa.mapItem.get(skuIDs[position]).price
					.toString());

			holder.count.setText(grandpa.mapItem.get(skuIDs[position]).count
					.toString());

			holder.lumpSum
					.setText(grandpa.mapItem.get(skuIDs[position]).lumpSum
							.toString());

			holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					grandpa.mapItem.remove(skuIDs[position]);
					grandpa.mapSkuMaster.remove(skuIDs[position]);
					v.setVisibility(View.GONE);
					parent.listViewNotifyDataSetChanged(R.id.listViewItem);
					parent.setTotal();
					parent.setButtonPay();
				}
			});

			convertView.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						positionCurrent = -1;
						viewCurrent = null;
						break;
					case MotionEvent.ACTION_DOWN:
						positionCurrent = position;
						viewCurrent = v;
						break;
					}

					return false;
				}
			});

			return convertView;
		}

		public int getPosition() {
			return positionCurrent;
		}

		public View getView() {
			return viewCurrent;
		}
	}

	public final class ViewHolder {
		public TextView itemCatName;
		public TextView skuPropName;
		public TextView price;
		public TextView count;
		public TextView lumpSum;
		public Button buttonDelete;

	}

}
