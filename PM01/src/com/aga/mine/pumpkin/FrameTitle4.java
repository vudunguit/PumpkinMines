package com.aga.mine.pumpkin;

import org.cocos2d.nodes.CCSprite;

import com.aga.mine.pumpkin.Utility;

public class FrameTitle4 {

	final static String fileExtension = ".png";
	
	public FrameTitle4() {
	}
	
	public static void setTitle(CCSprite parentSprite, String imageFolder) {
		// 타이틀
		CCSprite frameTitle = CCSprite.sprite(
		Utility.getInstance().getNameWithIsoCodeSuffix(
				imageFolder + imageFolder.substring(2, imageFolder.length()-1) + "-title" + fileExtension));
		parentSprite.addChild(frameTitle);
		frameTitle.setAnchorPoint(0.5f, 0.5f);
		frameTitle.setPosition(parentSprite.getContentSize().width / 2, parentSprite.getContentSize().height / 2);
	}
	
}
