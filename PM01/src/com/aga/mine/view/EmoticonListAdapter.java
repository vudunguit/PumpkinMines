package com.aga.mine.view;

import java.util.ArrayList;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.aga.mine.pumpkin.Constant;
import com.aga.mine.pumpkin.FacebookData;
import com.aga.mine.pumpkin.MainApplication;
import com.aga.mine.pumpkin.R;
import com.aga.mine.pumpkin.ShopEmoticon;

public class EmoticonListAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<Item> mItemList = new ArrayList<Item>();
	
	public EmoticonListAdapter(Context context) {
		mContext = context;
		// 이모티콘 ","로 구분된 String을 잘라서 String배열에 저장
		String[] emoticons = FacebookData.getinstance().getDBData("Emoticons").split(",");
		ArrayList<String> emoticonList = new ArrayList<String>();
		for(String emoticonData : emoticons) {
			emoticonList.add(emoticonData);
			Log.d("LDK", "checked emoticon: " + emoticonData);
		}
		
		for(int i=0; i<48; i++) {
			Item item = new Item();
			item.emoticon = R.drawable.emoticon_01 + i;
			item.checked = emoticonList.contains(String.valueOf(i+1)) ? true : false;
			mItemList.add(item);
		}
	}
	
	public void refresh(int id) {
		mItemList.get(id-1).checked = true;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Viewholder holder;
		if(convertView == null) {
			holder = new Viewholder();
			convertView = View.inflate(mContext, R.layout.grid_emoticon, null);
			holder.emoticon = (ImageView) convertView.findViewById(R.id.imgEmoticon);
			holder.checked = (ImageView) convertView.findViewById(R.id.imgChecked);
			holder.selected = (ImageView) convertView.findViewById(R.id.imgSelected);
			convertView.setTag(holder);
		} else {
			holder = (Viewholder) convertView.getTag();
		}

		holder.emoticon.setImageResource(mItemList.get(position).emoticon);
		
		if(mItemList.get(position).checked) {
			holder.checked.setVisibility(View.VISIBLE);
			holder.emoticon.setEnabled(false);
		} else {
			holder.checked.setVisibility(View.INVISIBLE);
			holder.emoticon.setEnabled(true);
		}
		
		holder.emoticon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 
				MainApplication.getInstance().getActivity().click();
				
				Message msg = MainApplication.getInstance().getActivity().mHandler.obtainMessage();
				msg.what = Constant.MSG_DISPLAY_POPUP;
				msg.arg1 = position+1;
				MainApplication.getInstance().getActivity().mHandler.sendMessage(msg);
				
				//new ShopEmoticon().popup(position+1);
//				String emoticons = FacebookData.getinstance().getDBData("Emoticons");
//				Log.e("EmoticonAdapter", "emoticons [" +emoticons + "] + " + (position+1));
//				emoticons += "," + (position+1);
//				Log.e("EmoticonAdapter", "emoticons : " + emoticons);
//				FacebookData.getinstance().modDBData("Emoticons", emoticons);
				
				//상태변경
//				mItemList.get(position).checked = true;
//				EmoticonListAdapter.this.notifyDataSetChanged();
			}
		});
		
		return convertView;
	}
	
	class Item {
		int emoticon;
		boolean checked;
	}
	
	class Viewholder {
		ImageView emoticon;
		ImageView checked;
		ImageView selected;
	}
}