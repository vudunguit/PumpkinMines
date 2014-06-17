package com.aga.mine.pumpkin;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.types.CGSize;

import com.aga.mine.util.UtilResource;

public class BottomImage {

	final static String commonfolder = "00common/";
	final static String fileExtension = ".png";
	
	public BottomImage() {
	}
	
	private static CGSize winsize() {
		return CCDirector.sharedDirector().winSize();
	}
	
	public static void setBottomImage(CCNode nodeThis) {
		setBottomImage(nodeThis, 999);
	}
	
	// 하단 이미지
	public static void setBottomImage(CCNode nodeThis, int tag) {
		CCSprite pumpkinL = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + commonfolder + "pumpkinL" + fileExtension));
		nodeThis.addChild(pumpkinL, tag, tag);
		pumpkinL.setAnchorPoint(0, 0);
		pumpkinL.setPosition(0, 0);
		
		CCSprite pumpkinR = CCSprite.sprite(CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + commonfolder + "pumpkinR" + fileExtension));
		nodeThis.addChild(pumpkinR);
		pumpkinR.setAnchorPoint(1f, 0);
		pumpkinR.setPosition(winsize().width, 0);
	}
}
