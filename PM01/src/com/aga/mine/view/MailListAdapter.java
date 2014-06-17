package com.aga.mine.view;

import java.util.ArrayList;
import java.util.Locale;

import org.cocos2d.nodes.CCDirector;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aga.mine.pumpkin.Constant;
import com.aga.mine.pumpkin.DataFilter;
import com.aga.mine.pumpkin.FacebookData;
import com.aga.mine.pumpkin.HomeTop;
import com.aga.mine.pumpkin.MailBox;
import com.aga.mine.pumpkin.MainApplication;
import com.aga.mine.pumpkin.R;
import com.androidquery.AQuery;

public class MailListAdapter extends BaseAdapter {
	private ArrayList<MailItem> mMailItemList;
	private AQuery mAq;
	private Context mContext;
	private int mTab;
	boolean isLocaleKo = false;
	
	public MailListAdapter(Context context, ArrayList<MailItem> mailItemList, int tab) {
		if (Locale.getDefault().getLanguage().toString().equals("ko"))
			isLocaleKo = true;
		mContext = context;
		mAq = new AQuery(mContext);
		mTab = tab;

		//tab => 1:broomstick, 2:gold
		mMailItemList = mailItemList;
	}

	@Override
	public int getCount() {
		return mMailItemList.size();
	}

	@Override
	public MailItem getItem(int position) {
		return mMailItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Viewholder holder;
		if(convertView == null) {
			holder = new Viewholder();
			convertView = View.inflate(mContext, R.layout.list_mail, null);
			holder.profile = (ImageView) convertView.findViewById(R.id.mailProfile);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
			holder.text = (TextView) convertView.findViewById(R.id.mailMessage);
			holder.image = (ImageView) convertView.findViewById(R.id.mailImage);
			convertView.setTag(holder);
		} else {
			holder = (Viewholder) convertView.getTag();
		}
		
		//아이템 받기 클릭 이벤트 처리
		holder.image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainApplication.getInstance().getActivity().click();
				// 일반 버튼같이 down상태에서는 이미지 변경이 가능할까요?
				String serialNumber = mMailItemList.get(position).serial_number;

				if (!serialNumber.equals("")) {
					long value;
					boolean goldLimit = true;
					final int GOLD_MAXIMUM = 16777215;
					if (mMailItemList.get(position).category.equals("Gold")) {
						value = Long.parseLong(FacebookData.getinstance().getDBData("Gold")) + Long.parseLong(mMailItemList.get(position).quantity);
						if (value > GOLD_MAXIMUM) {
							goldLimit = false;
							CCDirector.sharedDirector().getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(MainApplication.getInstance().getApplicationContext(), "골드가 넘칩니다.", Toast.LENGTH_LONG).show();
								}
							});
						} else {
							FacebookData.getinstance().modDBData("Gold", String.valueOf(value));
						}
					} else {
						value = Integer.parseInt(FacebookData.getinstance().getDBData("ReceivedBroomstick")) + Integer.parseInt(mMailItemList.get(position).quantity);
						if (value >= 6) {
							
							//빗자루의 총합이 6개 이상일시 남은시간 초기화 시키는 코드 넣어주시기 바랍니다.
							
						}
						FacebookData.getinstance().modDBData("ReceivedBroomstick", "" + value);

					}
					if (goldLimit) {
						DataFilter.deleteMail(serialNumber);
						if (mTab == Constant.MAIL_TAB_BROOM) {
							// 빗자루 화면 갱신  
							holder.image.setImageResource(R.drawable.mail_broomstickbutton2);
							mMailItemList.remove(position);
							MailBox.postNumber.setString(String.valueOf(mMailItemList.size()));
							MailListAdapter.this.notifyDataSetChanged();
							HomeTop.getDB();
						} else {
							// 골드 화면 갱신
							holder.image.setImageResource(R.drawable.mail_giftbutton2);
							mMailItemList.remove(position);
							MailBox.postNumber.setString(String.valueOf(mMailItemList.size()));
							MailListAdapter.this.notifyDataSetChanged();
							HomeTop.getDB();
						}
					}
					
				}
			}
		});
		
		String senderID = mMailItemList.get(position).sender_id;
		final int pumpkinMine = 1; 

		holder.tvDate.setText(mMailItemList.get(position).date);
		
		//set facebook profile image
		AQuery aq = mAq.recycle(convertView);
		if (Long.parseLong(senderID) < 2) {
			aq.id(holder.profile).image(R.drawable.mail_pumkin);
		} else {
			String url = "https://graph.facebook.com/" + senderID +"/picture";
			if(aq.shouldDelay(position, convertView, parent, url)){
				
			}else{
				aq.id(holder.profile).image(url, true, true);
			}
		}
		
		// id대신 name을 얻어야 되네요.(친구가 아닐 수 있습니다.)
		holder.tvName.setText(mMailItemList.get(position).sender_name);
		
		//set text and icon
		if(mTab == 1) {
			if (isLocaleKo)
				holder.text.setText("빗자루 " + mMailItemList.get(position).quantity + "개를 받았습니다.");
			else
				holder.text.setText("you have received the broomstick");
			holder.image.setImageResource(R.drawable.mail_broomstickbutton1);
		} else {
			if (isLocaleKo)
				holder.text.setText("" + mMailItemList.get(position).quantity + "골드를 받았습니다.");
			else
				holder.text.setText("you have received a gift of "  + mMailItemList.get(position).quantity + " gold");
			holder.image.setImageResource(R.drawable.mail_giftbutton1);
		}
		
		return convertView;
	}
	
	class Viewholder {
		ImageView profile;
		TextView tvName;
		TextView tvDate;
		TextView text;
		ImageView image;
	}

}
