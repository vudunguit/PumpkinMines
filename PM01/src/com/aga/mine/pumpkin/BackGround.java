package com.aga.mine.pumpkin;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

import com.aga.mine.util.UtilResource;

public class BackGround {

	public BackGround() {
	}
	
	private static CGSize winsize() {
		return CCDirector.sharedDirector().winSize();
	}

	public static CCSprite setBackground(CCNode nodeThis, String backGroundImage) {
		return setBackground(nodeThis, CGPoint.make(0.5f, 0.5f), backGroundImage); 
	}
	
	public static CCSprite setBackground(CCNode nodeThis, CGPoint anchor, String backGroundImage) {
		CCTexture2D texture = CCTextureCache.sharedTextureCache().addImageExternal(UtilResource.RESOURCE_PATH + backGroundImage);
		CCSprite bg = CCSprite.sprite(texture);
		nodeThis.addChild(bg);
		bg.setScale(winsize().width / bg.getBoundingBox().size.width);
		bg.setPosition(anchor.x * winsize().width, anchor.y * winsize().height);
		bg.setAnchorPoint(anchor);		
		return bg;
	}
}
