package com.aga.mine.pumpkin;

import java.util.ArrayList;
import java.util.Locale;

import org.cocos2d.actions.base.CCAction;
import org.cocos2d.actions.base.CCFiniteTimeAction;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.instant.CCInstantAction;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCFadeIn;
import org.cocos2d.actions.interval.CCFadeOut;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCSpriteFrameCache;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.opengl.CCTexture2D;
import org.cocos2d.particlesystem.CCParticleSystem;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;

import android.util.Log;


//싱글톤 같은데 뭔가 살짝 다르다???
// winSizeInPixels;
// alloc]		// initWithFile:@]		// autorelease];
//= floor(256 * opacity); 뭐지?
//CCanimation  String 모름
// RemoveNode.start() 맞을랑가 몰라.
public class Utility extends CCLayer {
	
	/*************** Utility.m ***************/
	/** Remove the node from parent and cleanup **/
    public class RemoveNode extends CCAction{
    	// 맞을랑가 몰라.
    	public void start(Object aTarget) {
    		super.setOriginalTarget((CCNode)aTarget);
    		super.setTarget((CCNode)aTarget);
    		start(aTarget);
    		getTarget().removeFromParentAndCleanup(true);
    	}
    	
    	@Override
    	public CCAction copy() {
    		return null;
    	}

    	@Override
    	public void step(float arg0) {
    	}
    	
    	@Override
    	public void update(float arg0) {
    	}
    }
    /*************************************/

	public enum UtilityDirection {
		kUtilityDirectionUp, kUtilityDirectionDown, kUtilityDirectionLeft, kUtilityDirectionRight
	};

	CCParticleSystem emitter;

	//------------------------------ Singleton ---------------------------------//
    //외부에서 접근하지 못하도록 private, 한프로그램에서 모두 사용할수있도록 static	
    private static Utility singleton = new Utility();

	ArrayList<CCSpriteFrame> frames = null;
	CCTexture2D t2d = null;
	CCSprite sprite = null;
	
    //외부에서 생성하지 못하게 생성자의 접근제한자를 private으로 선언
    private Utility(){ 
        System.out.println("Utility 인스턴스가 생성되었습니다.");
    }
    
    //생성된 Singleton인스턴스를 반환.
    public static Utility getInstance(){
        return singleton;
    }
	//---------------------------------------------------------------//

    
	/*
	 * private void dimScreen(CCNode parentNode, CGFloat opacity) { }
	 * 
	 * // private void undimScreen(CCNode parentNode) {}
	 * 
	 * private void moveInHoldAndOut(CCSprite sprite ) { } private void
	 * moveInHoldAndOut(CCSprite sprite, ccTime startDelayTime, ccTime inTime,
	 * ccTime returnDelay, ccTime returnDelay,ccTime returnTime, ccTime
	 * holdTime, ccTime outTime) { } private void moveInHoldAndOut(CCSprite
	 * sprite, ccTime startDelayTime, ccTime inTime , CGFloat distance,ccTime
	 * returnDelay, ccTime returnTime,ccTime holdTime, ccTime outTime) { }
	 * 
	 * private void fadeIn (CCSprite sprite,ccTime startDelayTime,ccTime time){
	 * }
	 * 
	 * private void fadeOut ( CCSprite sprite,ccTime time){ }
	 * 
	 * private void flashOut (CCSprite sprite,ccTime startDelay ,ccTCCSprite
	 * delay){ }
	 * 
	 * private void move (CCSprite sprite,CGFloat distance ,ccTime moveTime
	 * ,UtilityDirection direction,boolean isRemoveAfter){ }
	 * 
	 * private void move (CCSprite sprite,CGFloat distance ,UtilityDirection
	 * direction ,boolean isRemoveAfter){ }
	 * 
	 * private void move (CCSprite sprit,ccTime startDelayTime,ccTime returnTime
	 * ,ccTime returnDelay ,ccTime movetime ,CGFloat distance , UtilityDirection
	 * direction ,boolean isRemoveAfter){ }
	 * 
	 * private void rotate (CCSprite sprite,ccTime duration){ }
	 * 
	 * private void zoomOutFit (CCSprite sprite,float initialScale ,ccTime
	 * time){ }
	 * 
	 * private void moveBezier (CCSprite sprite,CGPoint endPosition ,ccTime
	 * moveTime ,ccTime lastDelayTime){ }
	 * 
	 * private void pulse (CCSprite sprite,ccTime delayTime){ }
	 * 
	 * private void fadeInOut (CCSprite sprite,ccTime time){ }
	 *
	
	*
	 * 
	 * - (CGPoint)getStartPosition:(CCSprite)sprite
	 * directiion:(UtilityDirection)direction;
	 * 
	 * - (CCRenderTexture *) createStroke: (CCLabelTTF *) label size:(float)size
	 * color:(ccColor3B)cor;
	 * 
	 * private void animationO:(CCNode *)parentNode; private void
	 * animationO:(CCNode *)parentNode z:(int)z; private void animationX:(CCNode
	 * *)parentNode; private void animationX:(CCNode *)parentNode z:(int)z;
	 * private void animationFoot:(CCNode *)parentNode; private void
	 * animationFoot:(CCNode *)parentNode z:(int)z; private void
	 * animationComboCharacter:(CCSprite)sprite delay:ccTime delayTime
	 * numberOfCombo:(int)numberOfCombo; private void
	 * animationComboCharacter:(CCSprite)sprite delay:ccTime delayTime
	 * direction:(UtilityDirection)direction; 
	 */
	private void animationHint(CCNode parentNode){
		
	} 
	 
	 
	 /*
	  - boolean isIPhone5;
	   * 
	 * -(void) createBurst:(CCNode *)parentNode position:(CGPoint)position
	 * withScale:(float)theScale;
	 * 
	 * @end
	 */

	
	int kTagDimLayer = 555;
	/*
	private static Utility utility; // c는 static이 지역변수인듯
		
	public static synchronized Utility initUtility(){
	if (utility == null) {
	    utility = new Utility();
	    initUtility(); // [utility initUtility];
	}
	return utility;
}
//싱글톤 같은데 뭔가 살짝 다르다???	
	private Object initUtility(){
		//return this;
		return null;
	}
	*/

	// #pragma mark - dim screen
	public void dimScreen(CCNode parentNode) {
		this.dimScreen(parentNode, 0.5f);
	}

	public void dimScreen(CCNode parentNode, float opacity) {
		// winSizeInPixels;
		CGSize wins = CCDirector.sharedDirector().winSize();
		CCSprite dim = CCSprite.sprite("singleblackpixel.png");
		dim.setScaleX(wins.width);
		dim.setScaleY(wins.height);
		dim.setAnchorPoint(0, 0);
		dim.setPosition(0, 0);
		// = floor(256 * opacity); 뭐지?
		dim.setOpacity((int)(256 * opacity));
		parentNode.addChild(dim);
		dim.setTag(kTagDimLayer);
	}
	
	public void undimScreen(CCNode parentNode) {
		parentNode.removeChildByTag(kTagDimLayer, false);
	}
	
	
/*
	// #pragma mark - animations
	private void scaleInOut(CCSprite sprite, ccTime time) {
		CGSize size = sprite.getContentSize();
		id scaleIn = CCScaleTo
				.action(time, size.width * 1.1, size.height * 1.1);
		id scaleOut = CCScaleTo.action(time, size.width * 0.9,
				size.height * 0.9);
		id scaleRestore = CCScaleTo.action(time, size.width, size.height);

		CCSequence sequence = CCSequence.actions(scaleIn, scaleOut, scaleRestore);
		sprite.runAction(sequence);
	}

	private void moveInHoldAndOut(CCSprite sprite) {
		this.moveInHoldAndOut(sprite, 0.0f, 0.2f, 0.05f, 0.1f, 0.8f, 0.2f);
	}

	private void moveInHoldAndOut(CCSprite sprite, ccTime startDelayTime,
			ccTime inTime, ccTime returnDelay, ccTime returnTime,
			ccTime holdTime, ccTime outTime) {
		CGFloat distance = CCDirector.sharedDirector().winSize().height;
		this.moveInHoldAndOut(sprite, startDelayTime, inTime, distance,
				returnDelay, returnTime, holdTime, outTime);
	}

	private void moveInHoldAndOut(
		CCSprite sprite, ccTime startDelayTime,	ccTime inTime, CGFloat distance,
		ccTime returnDelay, ccTime returnTime, ccTime holdTime, ccTime outTime){
    CGSize winSize = CCDirector.sharedDirector().winSize();
    float returnOffset = winSize.height/20;
    
    CGPoint inPosition = sprite.getPosition();
    //inPosition.y -= winSize.height;
    inPosition.y -= distance;
    inPosition.y -= returnOffset;
    
    CGPoint outPosition = inPosition;
    outPosition.y -= winSize.height;
    
    id startDelay = CCDelayTime.action(startDelayTime);
    id moveIn = CCMoveTo.action(inTime,inPosition);
    id inAfterDelay = CCDelayTime.action(returnDelay);
    id inReturn = CCMoveTo.action(returnTime, CGPoint.make(inPosition.x, inPosition.y+returnOffset));
    id holdDelay = CCDelayTime.action(holdTime);
    id outReturn = CCMoveTo.action(returnTime, CGPoint.make(inPosition.x, inPosition.y+returnOffset+returnOffset));
    id outBeforeDelay = CCDelayTime.action(returnDelay);
    id moveOut = CCMoveTo.action(outTime, outPosition);
    
    
    CCSequence sequence = CCSequence.actions(startDelay, moveIn, inAfterDelay, inReturn, holdDelay, outReturn, outBeforeDelay, moveOut);
    sprite.runAction(sequence);
}

	private void fadeOut(CCSprite sprite, ccTime time) {
		id fadeOut = CCFadeOut.action(time);
		CCSequence sequence = CCSequence.actions(fadeOut, action);
		sprite.runAction(sequence);
	}
*/
	public void flashOut(CCSprite sprite, float startDelay, float delay) {
		//
		// 일정시간(delay)이후 사라진다.
		CCFadeIn fadeIn = CCFadeIn.action(0);
		CCFadeOut fadeOut = CCFadeOut.action(0);
		CCDelayTime startHold = CCDelayTime.action(startDelay); 
		CCDelayTime hold = CCDelayTime.action(delay); 
		// 안됨 ㅠㅠ
		//CCSequence sequence = CCSequence.actions(startHold, fadeIn, hold,fadeOut, RemoveNode.action());
//		Log.e("Utility / flashOut", "sequence");
		CCSequence sequence = CCSequence.actions(startHold, fadeIn, hold,fadeOut
				//, CCCallFunc.action(this, "removeCap")
				);
		sprite.runAction(sequence);
		//sprite.removeSelf();
	}
/*
	private void fadeIn(CCSprite sprite, ccTime startDelayTime, ccTime time) {
		id startDelay = CCDelayTime.action(startDelayTime);
		id fadeIn = CCFadeIn.action(time);

		sprite.opacity = 0.0f;
		CCSequence sequence = CCSequence.actions(startDelay, fadeIn);
		sprite.runAction(sequence);
	}

	private void move(CCSprite sprite, CGFloat distance, ccTime moveTime,
			UtilityDirection direction, boolean isRemoveAfter) {
		this.move(sprite, 0.0f, 0.0f, 0.0f, moveTime, distance, direction,
				isRemoveAfter);
	}

	private void move(CCSprite sprite, CGFloat distance,
			UtilityDirection direction, boolean isRemoveAfter) {
		this.move(sprite, 0.0f, 0.08f, 0.02f, 0.08f, distance, direction,
				isRemoveAfter);
	}

	private void move(CCSprite sprite, ccTime startDelayTime,
			ccTime returnTime, ccTime returnDelay, ccTime movetime,
			CGFloat distance, UtilityDirection direction, boolean isRemoveAfter) {
		CGPoint toPosition = sprite.getPosition();
		CGPoint returnPosition = sprite.getPosition();
		switch (direction) {
		case kUtilityDirectionUp:
			toPosition.y += distance;
			returnPosition.y -= distance / 10;
			break;

		case kUtilityDirectionDown:
			toPosition.y -= distance;
			returnPosition.y += distance / 10;
			break;

		case kUtilityDirectionLeft:
			toPosition.x -= distance;
			returnPosition.x += distance / 10;
			break;

		case kUtilityDirectionRight:
			toPosition.x += distance;
			returnPosition.x -= distance / 10;
			break;
		}

		//
		// no return if return time is 0
		if (returnTime == 0)
			returnPosition = sprite.getPosition();

		id startDelay = CCDelayTime.action(startDelayTime);
		id startReturn = CCMoveTo.action(returnTime, returnPosition);
		id startReturnDelay = CCDelayTime.action(returnDelay);
		id moveTo = CCMoveTo.action(movetime, toPosition);

		CCSequence sequence = null;
		if (isRemoveAfter == true)
			sequence = CCSequence.actions(startDelay, startReturn,
					startReturnDelay, moveTo, action);
		else
			sequence = CCSequence.actions(startDelay, startReturn,	startReturnDelay, moveTo);
		sprite.runAction(sequence);
	}

	private void rotate(CCSprite sprite , ccTime duration){
    CCRotateBy rotate = CCRotateBy.action(duration,360.0f);
    
    CCSequence sequence = null;
    CCSequence.actions(rotate);
    CCRepeatForever repeat = CCRepeatForever.action(sequence);
    sprite.runAction(repeat);
}

	private void zoomOutFit(CCSprite sprite,float initialScale	 , ccTime time)
	  { float targetScale = sprite.getScale();
	  sprite.setScale( initialScale);
	  
	  id zoomIn = CCScaleTo.action(time,targetScale);
	  sprite.runAction(zoomIn);
	  }

	private void moveBezier(CCSprite sprite, CGPoint endPosition	 , ccTime moveTime , ccTime lastDelayTime) { 
		CGPoint	  startPosition = sprite.getPosition();
		CCBezierConfig bezier;
		bezier.controlPoint_1 = CGPoint.make(startPosition.x+20.f,	  startPosition.y);
		bezier.controlPoint_2 = CGPoint.make(endPosition.x+20,	 endPosition.y);
		bezier.endPosition = endPosition;
	  
	  id moveBezier = CCBezierTo.action(moveTime ,bezier);
	  id lastDelay = CCDelayTime.action(lastDelayTime);
	  
	  CCSequence sequence = CCSequence.actions(moveBezier, lastDelay,	 action);
	  sprite.runAction(sequence);
	  }

	private void pulse(CCSprite sprite, ccTime delayTime)	  {
	  sprite.setOpacity((int) 1.0f);
	  
	  CCFadeIn flashIn = CCFadeIn.action(0.0f);
	  CCFadeOut flashOut = CCFadeOut.action(0.0f);
	  id delay = CCDelayTime.action(delayTime);
	  
	  CCSequence sequence = CCSequence.actions(flashOut, delay, flashIn,delay); 
	  CCRepeatForever repeat = CCRepeatForever.action(sequence);
	  sprite.runAction(repeat);  
	  }
	
	  
	  private void fadeInOut(CCSprite sprite , ccTime time {
		  sprite.setopacity((int)1.0f);
	  
	  id fadeIn = CCFadeIn.action(time);
	  id fadeOut = CCFadeOut.action(time);
	  
	  CCSequence sequence = CCSequence.actions(fadeOut, fadeIn);
	  CCRepeatForever repeat = CCRepeatForever.action(sequence);
	  sprite.runAction(repeat); 
	  }
	  
	  //#pragma mark - sprite frames animation private void
	  private void animationComboCharacter(CCSprite sprite ,ccTime delayTime,	int numberOfCombo)	{
		  String frameName = null;
	  
	  // // 둘 중 하나의 캐릭터를 구매 한 경우
		  if (UserDataHelper.initUserDataHelper().isPurchased(kSkillItemHint) == true) {
			  frameName = "o_c_"; 
			  } else if  (UserDataHelper.initUserDataHelper().isPurchased(kSkillItemGold) == true) {
				  frameName = @"r_c_";
				  } else { 
					  frameName = @"d_c_";
					  }
	  
	  // // 두개 모두 구매 한 경우에는 두 캐릭터가 번갈아 나온다. 130529 @김상태, 픽스테이션
		  if (UserDataHelper initUserDataHelper.isPurchased(kSkillItemHint) == true
				  && UserDataHelper initUserDataHelper.isPurchased(kSkillItemGold) ==	  true) { 
			  frameName = arc4random_uniform(2) == 0 ? @"o_c_" : @"r_c_";
			  }
	  
	  // // set direction 
		  UtilityDirection theDirection = arc4random_uniform(2)  == 0 ? 
				  kUtilityDirectionLeft :
					  kUtilityDirectionRight;
		  
		  switch	  (theDirection) { 
		  case kUtilityDirectionLeft:
			  frameName = frameName +"r.png"; 
			  break;
		  
		  case kUtilityDirectionRight: 
			  frameName = frameName	+"l.png"; 
		  break;
	  
	  default: 
		  break;
	  }
	  
	  // // character change here 
		  CCSpriteFrameCache.sharedSpriteFrameCache().addSpriteFrames("comboCharacters.plist"); 
		  sprite.setDisplayFrame(CCSpriteFrameCache.sharedSpriteFrameCache().spriteFrameByName(frameName));
	  
	  
	  // // combo number add 
		  CCLabel comboNumber = CCLabel.makeLabel(""+numberOfCombo,"kFontNameDefault", 20f);
	  
	  comboNumber.setColor(ccColor3B.ccRED);
	  sprite.removeAllChildren(true);
	  sprite.addChild(comboNumber);
	  comboNumber.setPosition(
			  theDirection == kUtilityDirectionLeft ?
					  sprite.getContentSize().width*0.32,  sprite.getContentSize().height*0.55) :
						  sprite.getContentSize().width*0.68,  sprite.getContentSize().height*0.55));
	  this.animationComboCharacter(sprite,delayTime,theDirection);
	  }
	  */
	  /*
	 * private void animationComboCharacter:(CCSprite *)sprite delay: ccTime
	 * delayTime direction:(UtilityDirection)direction { sprite.opacity = 0.0f;
	 * 
	 * CGPoint toPosition = CGPointZero; CGPoint restorePosition =
	 * CGPointMake(160.0f, 313.846f); CGFloat height = this.isIPhone5] == YES ?
	 * 420. : 356.; if (direction == kUtilityDirectionLeft) { toPosition =
	 * CGPointMake(73.9f, height); } else if (direction ==
	 * kUtilityDirectionRight) { toPosition = CGPointMake(246.1f, height); } id
	 * flashIn = [CCFadeIn actionWithDuration:0.0f]; id moveUp = [CCMoveTo
	 * actionWithDuration:0.1f position:toPosition]; id delay = [CCDelayTime
	 * actionWithDuration:delayTime]; id moveRestore = [CCMoveTo
	 * actionWithDuration:0.1f position:restorePosition]; id flashOut =
	 * [CCFadeOut actionWithDuration:0.0f];
	 * 
	 * CCSequence *sequence = [CCSequence actions:flashIn, moveUp, delay,
	 * moveRestore, flashOut, null]; [sprite runAction:sequence]; }
	 * 
	 * #pragma mark - move sprite to start position -
	 * (CGPoint)getStartPosition:(CCSprite *)sprite
	 * directiion:(UtilityDirection)direction { CGSize winSize = [CCDirector
	 * sharedDirector].winSize; CGPoint startPosition = sprite.position;
	 * 
	 * switch (direction) { case kUtilityDirectionUp: startPosition.y +=
	 * winSize.height; break;
	 * 
	 * case kUtilityDirectionDown: startPosition.y -= winSize.height; break;
	 * 
	 * case kUtilityDirectionLeft: startPosition.x -= winSize.width; break;
	 * 
	 * case kUtilityDirectionRight: startPosition.x += winSize.width;
	 * 
	 * default: break; }
	 * 
	 * return startPosition; }
	 * 
	 * #pragma mark - font - (CCRenderTexture *)createStroke:(CCLabelTTF *)label
	 * size:(float)size color:(ccColor3B)cor { // //
	 * http://www.cocos2d-iphone.org/forum/topic/12126 // CCRenderTexture* rt =
	 * [CCRenderTexture
	 * renderTextureWithWidth:label.texture.contentSize.width+size*2
	 * height:label.texture.contentSize.height+size*2]; CGPoint originalPos =
	 * [label position]; ccColor3B originalColor = [label color]; BOOL
	 * originalVisibility = [label visible];
	 * 
	 * [label setColor:cor]; [label setVisible:YES]; ccBlendFunc originalBlend =
	 * [label blendFunc]; [label setBlendFunc:(ccBlendFunc) { GL_SRC_ALPHA,
	 * GL_ONE }]; //[label setBlendFunc:(ccBlendFunc) { GL_SRC_ALPHA,
	 * GL_ONE_MINUS_SRC_COLOR }]; CGPoint center =
	 * ccp(label.texture.contentSize.width/2+size,
	 * label.texture.contentSize.height/2+size);
	 * 
	 * [rt begin]; for (int i=0; i<360; i+=15) { [label setPosition:ccp(center.x
	 * + sin(CC_DEGREES_TO_RADIANS(i))*size, center.y +
	 * cos(CC_DEGREES_TO_RADIANS(i))*size)]; [label visit]; } [rt end];
	 * 
	 * [label setPosition:originalPos]; [label setColor:originalColor]; [label
	 * setBlendFunc:originalBlend]; [label setVisible:originalVisibility]; [rt
	 * setPosition:originalPos];
	 * 
	 * return rt; }
	 * 
	 * #pragma mark - frame animations private void animationO:(CCNode
	 * *)parentNode { this.animationO:parentNode z:0]; }
	 * 
	 * private void animationO:(CCNode *)parentNode z:(int)z {
	 * [[CCSpriteFrameCache sharedSpriteFrameCache]
	 * addSpriteFramesWithFile:@"animationOXs.plist"]; NSMutableArray *frames =
	 * [[NSMutableArray alloc] initWithObjects: [[CCSpriteFrameCache
	 * sharedSpriteFrameCache] spriteFrameByName:@"O01.png"],
	 * [[CCSpriteFrameCache sharedSpriteFrameCache]
	 * spriteFrameByName:@"O02.png"], [[CCSpriteFrameCache
	 * sharedSpriteFrameCache] spriteFrameByName:@"O03.png"], null];
	 * 
	 * CCAnimation *animation = [CCAnimation animationWithFrames:frames
	 * delay:0.1f]; CCAnimate *animate = [CCAnimate
	 * actionWithAnimation:animation]; id lastDelay = [CCDelayTime
	 * actionWithDuration:0.1f]; CCSprite *lastFrame = [CCSprite
	 * spriteWithSpriteFrameName:@"O03.png"]; CCSequence *sequence = [CCSequence
	 * actions:animate, lastDelay, [RemoveNode action], null]; [lastFrame
	 * runAction:sequence]; [parentNode addChild:lastFrame z:z];
	 * lastFrame.position = CGPointMake(parentNode.contentSize.width/2,
	 * parentNode.contentSize.height*0.3); }
	 * 
	 * private void animationX:(CCNode *)parentNode { this.animationX:parentNode
	 * z:0]; }
	 * 
	 * private void animationX:(CCNode *)parentNode z:(int)z {
	 * 
	 * [[CCSpriteFrameCache sharedSpriteFrameCache]
	 * addSpriteFramesWithFile:@"animationOXs.plist"]; NSMutableArray *frames =
	 * [[NSMutableArray alloc] initWithObjects: [[CCSpriteFrameCache
	 * sharedSpriteFrameCache] spriteFrameByName:@"X01.png"],
	 * [[CCSpriteFrameCache sharedSpriteFrameCache]
	 * spriteFrameByName:@"X02.png"], [[CCSpriteFrameCache
	 * sharedSpriteFrameCache] spriteFrameByName:@"X03.png"],
	 * [[CCSpriteFrameCache sharedSpriteFrameCache]
	 * spriteFrameByName:@"X04.png"], null];
	 * 
	 * CCAnimation *animation = [CCAnimation animationWithFrames:frames
	 * delay:0.1f]; CCAnimate *animate = [CCAnimate
	 * actionWithAnimation:animation]; id lastDelay = [CCDelayTime
	 * actionWithDuration:0.1f]; CCSprite *lastFrame = [CCSprite
	 * spriteWithSpriteFrameName:@"X04.png"]; CCSequence *sequence = [CCSequence
	 * actions:animate, lastDelay, [RemoveNode action], null]; [lastFrame
	 * runAction:sequence]; [parentNode addChild:lastFrame z:z];
	 * lastFrame.position = CGPointMake(parentNode.contentSize.width/2,
	 * parentNode.contentSize.height*0.3); NSLog(@"%g, %g",
	 * lastFrame.position.x, lastFrame.position.y); }
	 * 
	 * private void animationFoot:(CCNode *)parentNode {
	 * this.animationFoot:parentNode z:0]; }
	 * 
	 * private void animationFoot:(CCNode *)parentNode z:(int)z {
	 * 
	 * [[CCSpriteFrameCache sharedSpriteFrameCache]
	 * addSpriteFramesWithFile:@"characterFeet.plist"]; NSMutableArray *frames =
	 * [[NSMutableArray alloc] initWithObjects: [[CCSpriteFrameCache
	 * sharedSpriteFrameCache] spriteFrameByName:@"h_f02.png"],
	 * [[CCSpriteFrameCache sharedSpriteFrameCache]
	 * spriteFrameByName:@"h_f.png"], [[CCSpriteFrameCache
	 * sharedSpriteFrameCache] spriteFrameByName:@"h_f01.png"], null];
	 * 
	 * CCAnimation *animation = [CCAnimation animationWithFrames:frames
	 * delay:0.1f]; CCAnimate *animate = [CCAnimate
	 * actionWithAnimation:animation]; CCSprite *sprite = [CCSprite
	 * spriteWithSpriteFrameName:@"h_f.png"]; CCSequence *sequence = [CCSequence
	 * actions:animate, null]; CCRepeatForever *repeat = [CCRepeatForever
	 * actionWithAction:sequence]; [sprite runAction:repeat]; //[sprite
	 * runAction:sequence]; [parentNode addChild:sprite z:z]; CGSize winSize =
	 * [CCDirector sharedDirector].winSize; sprite.position =
	 * CGPointMake(winSize.width/2, winSize.height/2+winSize.height*(1./6.5));
	 * //defaultFrame.position = CGPointMake(parentNode.contentSize.width/2,
	 * parentNode.contentSize.height*0.3); //NSLog(@"%g, %g",
	 * lastFrame.position.x, lastFrame.position.y); }
	 * 
	 * 
	 * private void animationHint:(CCNode *)parentNode { [[CCSpriteFrameCache
	 * sharedSpriteFrameCache] addSpriteFramesWithFile:@"hintFingers.plist"];
	 * NSMutableArray *frames = [[NSMutableArray alloc] initWithObjects:
	 * [[CCSpriteFrameCache sharedSpriteFrameCache]
	 * spriteFrameByName:@"h01.png"], [[CCSpriteFrameCache
	 * sharedSpriteFrameCache] spriteFrameByName:@"h02.png"],
	 * [[CCSpriteFrameCache sharedSpriteFrameCache]
	 * spriteFrameByName:@"h03.png"], null];
	 * 
	 * CCAnimation *animation = [CCAnimation animationWithFrames:frames
	 * delay:0.1f]; CCAnimate *animate = [CCAnimate
	 * actionWithAnimation:animation]; CCSprite *sprite = [CCSprite
	 * spriteWithSpriteFrameName:@"h01.png"]; CCSequence *sequence = [CCSequence
	 * actions:animate, null]; CCRepeatForever *repeat = [CCRepeatForever
	 * actionWithAction:sequence]; [sprite runAction:repeat]; [parentNode
	 * addChild:sprite]; sprite.position =
	 * CGPointMake(parentNode.contentSize.width/2,
	 * parentNode.contentSize.height/2); }
	 */ 
	
//	public void animationMagicianAction(CCNode parentNode) {
//		String path = "60game/";
//		CGSize winSize = CCDirector.sharedDirector().winSize();
//        String rWizard1 =  path + "R-WizardAction-2.png";
//        String rWizard2 = path + "R-WizardAction-1.png";
//        
//			sprite = CCSprite.sprite(rWizard2);
//			sprite.setPosition(winSize.width * 0.57f, winSize.height * 0.3f);
//			parentNode.addChild(sprite, 1, 888);
//			
//			frames = new ArrayList<CCSpriteFrame>();
//			t2d = CCTextureCache.sharedTextureCache().addImage(rWizard1);
//			CCSpriteFrame frame = CCSpriteFrame.frame(
//					t2d,CGRect.make(0,0,t2d.getContentSize().width,t2d.getContentSize().height),CGPoint.ccp(0,0));
//			frames.add(frame);
//			frames.add(frame);
//			frames.add(frame);
//	        t2d = CCTextureCache.sharedTextureCache().addImage(rWizard2); 
//			frame = CCSpriteFrame.frame(
//					t2d,CGRect.make(0,0,t2d.getContentSize().width,t2d.getContentSize().height),CGPoint.ccp(0,0));
//			frames.add(frame);
//		
//		
//		CCAnimation animation = CCAnimation.animation("", 0.1f, frames);
//		CCAnimate animate = CCAnimate.action(animation);
////		Log.e("Utility / animationMagicianAction", "sequence");
//		CCSequence sequence = CCSequence.actions(
//				//animate,CCCallFunc.action(this, "removeCap"));
//				animate);
//		sprite.runAction(sequence);
//	}
	
CCNode papa;
	public void removeCap(){
		sprite.removeFromParentAndCleanup(true);
	}

		  
// #pragma mark - general
//  return device set language code 
//  - kIsoCodeKo 		: korean 
//  - kIsoCodeEn 		: english 
//  - kIsoCodeZh 		: simplified chinese 
//  - -1 							: none
	
	// 파일명 뒤에 언어별 코드명 붙여 주기. 예) 123.png --> 123Ko.png 또는 123En.png 
		// ** 2글자 붙이기
		public String getNameWithIsoCodeSuffix(String filename) {
	
	//		Log.e("Utility / getNameWithIsoCodeSuffix", "in Utility");
	//		Log.e("Utility / getNameWithIsoCodeSuffix", "입력받은 파일명 : " + filename);
	
			String suffix = Locale.getDefault().getLanguage().toString();
	//		Log.e("Utility / getNameWithIsoCodeSuffix", "지역명1 : " + suffix);
	
			if (!suffix.equals("ko") && !suffix.equals("en") && !suffix.equals("zh"))
				suffix = "en";
	//		Log.e("Utility / getNameWithIsoCodeSuffix", "지역명2 : " + suffix);
	
			suffix = suffix.substring(0, 1).toUpperCase() + suffix.substring(1).toLowerCase();
	//		Log.e("Utility / getNameWithIsoCodeSuffix", "지역명3: " + suffix);
	
			// return filename.substring(0,(filename.lastIndexOf("."))) + suffix + filename.substring(filename.lastIndexOf("."));
			
			// 테스트로 인해 주석처리
			// 테스트후 아래는 지우고 주석처리된것으로 사용
			 String value = filename.substring(0,(filename.lastIndexOf("."))) + suffix + filename.substring(filename.lastIndexOf("."));
			return value;
		}

//	// 파일명 뒤에 언어별 코드명 붙여 주기. 예) 123.png --> 123Ko.png 또는 123En.png  -->  최종 123En-hd.png 
//			// ** 2글자 붙이기
//			public String getNameWithIsoCodeSuffixHD(String filename) {
//		
//		//		Log.e("Utility / getNameWithIsoCodeSuffix", "in Utility");
//		//		Log.e("Utility / getNameWithIsoCodeSuffix", "입력받은 파일명 : " + filename);
//		
//				String suffix = Locale.getDefault().getLanguage().toString();
//		//		Log.e("Utility / getNameWithIsoCodeSuffix", "지역명1 : " + suffix);
//		
//				if (!suffix.equals("ko") && !suffix.equals("en") && !suffix.equals("zh"))
//					suffix = "en";
//		//		Log.e("Utility / getNameWithIsoCodeSuffix", "지역명2 : " + suffix);
//		
//				suffix = suffix.substring(0, 1).toUpperCase() + suffix.substring(1).toLowerCase();
//		//		Log.e("Utility / getNameWithIsoCodeSuffix", "지역명3: " + suffix);
//		
//				// return filename.substring(0,(filename.lastIndexOf("."))) + suffix + filename.substring(filename.lastIndexOf("."));
//				
//				// 테스트로 인해 주석처리
//				// 테스트후 아래는 지우고 주석처리된것으로 사용
//				// String value = filename.substring(0,(filename.lastIndexOf("."))) + suffix + filename.substring(filename.lastIndexOf("."));
//				String value = filename.substring(0, (filename.lastIndexOf(".")))
//						+ suffix + "-hd"
//						+ filename.substring(filename.lastIndexOf("."));
//		//		Log.e("Utility / getNameWithIsoCodeSuffix", "최종 파일명 : " + value);
//				return value;
//			}

	// 전래동화용
	// ** 3글자용은 나중에 따로 수정하기
	public String getNameWithIsoCodeSuffix2(String filename) {
//		Log.e("Utility : ", "getNameWithIsoCodeSuffix2 실행");
		// 값 받아서 다시 if문으로 돌릴것(-kor) getNameWithIsoCodeSuffix(filename);
		return getNameWithIsoCodeSuffix(filename);
	}
	
	// 파일명 뒤에 언어별 코드명 붙여 주기. 예) 123 --> 123ko 또는 123en 
	// ** 2글자 붙이기
	public String getNameWithIsoCodeSuffix3(String filename) {

		String suffix = Locale.getDefault().getLanguage().toString();

		if (!suffix.equals("ko") && !suffix.equals("en") && !suffix.equals("zh"))
			suffix = "en";

		suffix = suffix.substring(0, 1).toUpperCase() + suffix.substring(1).toLowerCase();

		// return filename.substring(0,(filename.lastIndexOf("."))) + suffix + filename.substring(filename.lastIndexOf("."));
		
		// 테스트로 인해 주석처리
		// 테스트후 아래는 지우고 주석처리된것으로 사용
		 String value = filename + suffix;
		return value;
	}
	 /* 
	 * - (NSString *)documentPath { return
	 * [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,
	 * NSUserDomainMask, YES) lastObject]; }
	 * 
	 * private void writeToFile:(NSString *)filename array:(NSArray *)array {
	 * NSString *documentPath = this.documentPath]; [array
	 * writeToFile:[documentPath stringByAppendingPathComponent:filename]
	 * atomically:YES]; }
	 * 
	 * - (double)deltaTime:(NSDate *)previousDate currentDate:(NSDate
	 * *)currentDate { //return [currentDate
	 * timeIntervalSinceDate:previousDate];
	 * 
	 * double start = [previousDate timeIntervalSince1970]; double end =
	 * [currentDate timeIntervalSince1970]; double difference = end - start;
	 * 
	 * NSLog(@"difference : %g", difference); return difference; }
	 * 
	 * - (NSString *)commaSeperatedNumberString:(int)number { NSNumberFormatter
	 * *numberFormatter = [[NSNumberFormatter alloc] init]; [numberFormatter
	 * setNumberStyle:NSNumberFormatterDecimalStyle]; [numberFormatter
	 * setGroupingSeparator:@","];
	 * 
	 * NSString *result = [numberFormatter stringForObjectValue:[NSNumber
	 * numberWithInt:number]]; [numberFormatter release];
	 * 
	 * return result; }
	 * 
	 * #pragma mark - Device
	 * 
	 * - boolean isIPhone5 { if([[UIScreen mainScreen] bounds].size.height ==
	 * 568) return YES;
	 * 
	 * return NO; }
	 * 
	 * #pragma mark - Particle -(void) createBurst:(CCNode *)parentNode
	 * position:(CGPoint)position withScale:(float)theScale { [_emitter
	 * resetSystem]; _emitter = [[CCParticleSystemQuad alloc]
	 * initWithTotalParticles:30]; // 30 //_emitter.texture = [[CCTextureCache
	 * sharedTextureCache] addImage:@"Particles_fire.png"]; _emitter.texture =
	 * [[CCTextureCache sharedTextureCache] addImage:@"flagPart.png"];
	 * 
	 * // duration // _emitter.duration = -1; //continuous effect
	 * _emitter.duration = 0.3;
	 * 
	 * // gravity _emitter.gravity = CGPointMake(0,-200);
	 * 
	 * // angle _emitter.angle = 90; _emitter.angleVar = 360;
	 * 
	 * // speed of particles _emitter.speed = 160; _emitter.speedVar = 20;
	 * 
	 * // radial _emitter.radialAccel = -120; _emitter.radialAccelVar = 0;
	 * 
	 * // tagential _emitter.tangentialAccel = 30; _emitter.tangentialAccelVar =
	 * 0;
	 * 
	 * // life of particles _emitter.life = 1; _emitter.lifeVar = 1;
	 * 
	 * // spin of particles _emitter.startSpin = 0; _emitter.startSpinVar = 180;
	 * _emitter.endSpin = 360; _emitter.endSpinVar = 180;
	 * 
	 * // color of particles ccColor4F startColor = {0.5f, 0.5f, 0.5f, 0.5f};
	 * _emitter.startColor = startColor; ccColor4F startColorVar = {0.5f, 0.5f,
	 * 0.5f, 0.2f}; _emitter.startColorVar = startColorVar; ccColor4F endColor =
	 * {0.1f, 0.1f, 0.1f, 0.2f}; _emitter.endColor = endColor; ccColor4F
	 * endColorVar = {0.1f, 0.1f, 0.1f, 0.2f}; _emitter.endColorVar =
	 * endColorVar;
	 * 
	 * // size, in pixels _emitter.startSize = 12.; //20.0f;
	 * _emitter.startSizeVar = _emitter.startSize/2; //10.0f; _emitter.endSize =
	 * kParticleStartSizeEqualToEndSize; // emits per second
	 * _emitter.emissionRate = _emitter.totalParticles/_emitter.life; //
	 * additive _emitter.blendAdditive = NO; //YES; _emitter.position =
	 * position; [_emitter setScale:theScale]; [parentNode addChild: _emitter];
	 * // adding the _emitter _emitter.autoRemoveOnFinish = YES; // this
	 * removes/deallocs the _emitter after its animation }
	 */
}
// end

