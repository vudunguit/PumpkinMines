package com.aga.mine.pumpkin;

import org.cocos2d.sound.SoundEngine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class PopupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.activity_popup);
		
		//만일 영문모드라면
//		ImageView ivBuyQ = (ImageView) findViewById(R.id.ivBuyQ);
//		ImageView ivBuyOk = (ImageView) findViewById(R.id.ivBuyOK);
//		ImageView ivBuyCancle = (ImageView) findViewById(R.id.ivBuyCancle);
//		ivBuyQ.setImageResource(R.drawable.popup_msgen);
//		ivBuyOk.setImageResource(R.drawable.popup_ok1en);
//		ivBuyCancle.setImageResource(R.drawable.popup_cancel1en);
		
		final int emoticonID = getIntent().getIntExtra("emoticonID", 0);

		findViewById(R.id.ivBuy).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String emoticons = FacebookData.getinstance().getDBData("Emoticons");
				emoticons += "," + emoticonID;
				FacebookData.getinstance().modDBData("Emoticons", emoticons);
				
				String gold = FacebookData.getinstance().getDBData("Gold");
				gold = String.valueOf(Long.parseLong(gold) - 100);
				FacebookData.getinstance().modDBData("Gold", gold);
				SoundEngine.sharedEngine().playEffect(PopupActivity.this, R.raw.buy);
				
				MainApplication.getInstance().getActivity().mEmoticonAdapter.refresh(emoticonID);
				finish();
			}
		});
		
		findViewById(R.id.ivBuyCancle).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainApplication.getInstance().getActivity().click();
				Toast.makeText(PopupActivity.this, "구매 취소", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
}
