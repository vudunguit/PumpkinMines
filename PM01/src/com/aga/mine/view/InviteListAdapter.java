package com.aga.mine.view;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aga.mine.pumpkin.FacebookData;
import com.aga.mine.pumpkin.GameScore;
import com.aga.mine.pumpkin.MainActivity;
import com.aga.mine.pumpkin.MainApplication;
import com.aga.mine.pumpkin.R;
import com.aga.mine.util.Util;
import com.androidquery.AQuery;
import com.sromku.simple.fb.entities.Profile;

public class InviteListAdapter extends BaseAdapter {
	private Context mContext;
	private Profile user;
	private List<Profile> notAPlayers;
	private AQuery mAq;
	boolean isLocaleKo = false;
	
	public InviteListAdapter(Context context) {
		mContext = context;
		user = FacebookData.getinstance().getUserInfo();
		if (Locale.getDefault().getLanguage().toString().equals("ko"))
			isLocaleKo = true;
		List<Profile> friends = FacebookData.getinstance().getFriendsInfo();
		List<GameScore> gameScore = FacebookData.getinstance().getGameScore();
		notAPlayers = friends;
		
		for (GameScore player : gameScore) {
			for (int i = 0; i < friends.size(); i++) {
				if (friends.get(i).getId().equals(player.id)) {
					notAPlayers.remove(i);
					break;
				}
			}
		}
		Log.d("LDK", "adpater, notAPlayers size:" + notAPlayers.size());
		mAq = new AQuery(mContext);
	}

	@Override
	public int getCount() {
		return notAPlayers.size();
	}

	@Override
	public String getItem(int position) {
		return notAPlayers.get(position).getId();
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
			convertView = View.inflate(mContext, R.layout.list_invite, null);
			holder.imgProfile = (ImageView) convertView.findViewById(R.id.inviteProfile);
			holder.imgInviteBtn = (ImageView) convertView.findViewById(R.id.inviteButton); // 다른 이미지로 변경
			holder.tvFBId = (TextView) convertView.findViewById(R.id.inviteId);
			
			convertView.setTag(holder);
		} else {
			holder = (Viewholder) convertView.getTag();
		}
		
		holder.tvFBId.setText(notAPlayers.get(position).getName());
		
		//초대버튼 disable 처리
		Log.d("LDK", "id: " + notAPlayers.get(position).getId() + ",초대상태:" + Util.canInvite(notAPlayers.get(position).getId())) ;
		if(!Util.canInvite(notAPlayers.get(position).getId())) {
			if (isLocaleKo)
				holder.imgInviteBtn.setImageResource(R.drawable.invite_button2ko);
			else
				holder.imgInviteBtn.setImageResource(R.drawable.invite_button2en);
			holder.imgInviteBtn.setEnabled(false);
		} else {
			if (isLocaleKo)
				holder.imgInviteBtn.setImageResource(R.drawable.invite_button1ko);
			else
				holder.imgInviteBtn.setImageResource(R.drawable.invite_button1en);
			holder.imgInviteBtn.setEnabled(true);
		}
		
		//초대버튼 클릭 이벤트 처리
		holder.imgInviteBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    MainActivity mMainActivity = MainApplication.getInstance().getActivity();
			    mMainActivity.click();
				String friend = notAPlayers.get(position).getId();
				// 메시지가 정확하게 전달 되는지는 모르겠네요.
				
				// 로그인 안되어 있을 경우 '취소'처리(이런경우가 없는데...) 
				Log.e("InviteListAapter", "Callback_3 - imgInviteBtn.setOnClickListener()");
				mMainActivity.sendInvite(friend, user.getName() + "님이 귀하를 초대합니다.", null);
			}
		});
		
		AQuery aq = mAq.recycle(convertView);
		
		String url = "https://graph.facebook.com/" + notAPlayers.get(position).getId() +"/picture";
		
		if(aq.shouldDelay(position, convertView, parent, url)){
		}else{
			aq.id(holder.imgProfile).image(url, true, true);
		}
		
		return convertView;
	}
	
	class Viewholder {
		ImageView imgProfile;
		ImageView imgInviteBtn;
		TextView tvFBId;
	}

}
