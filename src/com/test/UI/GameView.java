package com.test.UI;

import com.test.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Callback{

	private Thread flushThread= null;
    private Bitmap sourceBitmap = null;// 扑克图片来源  
    private Bitmap backgroundDesk = null;// 牌桌背景  
    private Bitmap backgroundPuke = null;// 扑克背面  
  
    private int pukeWidth = 0;// 扑克的宽  
    private int pukeHeight = 0;// 扑克的高  
    private int deskWidth = 0;// 牌桌的宽  
    private int deskHeight = 0;// 牌桌的高  
    private int left = 0;// 我自己首张牌左距离  
	
	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		getHolder().addCallback(this);
		  
        this.flushThread = new FlushThread(getHolder(), this);// 实例化线程  
        initBitmap();
	}

	private void initBitmap() {
		// TODO Auto-generated method stub
		sourceBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.poke_fuwa);
		pukeWidth = sourceBitmap.getWidth() / 13;// 每张扑克的宽高  
        pukeHeight = sourceBitmap.getHeight() / 5;  
  
//        backgroundDesk = BitmapFactory.decodeResource(getResources(),  
//                R.drawable.gameback2);  
//  
//        deskWidth = backgroundDesk.getWidth();// 牌桌的宽高  
//        deskHeight = backgroundDesk.getHeight();  
//  
//        backgroundPuke = BitmapFactory.decodeResource(getResources(),  
//                R.drawable.cardback);  
	}
	
	/** 绘制每个玩家手里的牌 */  
    public void personPaint(Canvas c, int pukeWidth, int pukeHeight) {  
        Rect src = new Rect();  
        Rect dst = new Rect();  
  
        // 遍历数组  
        for (int i = 0; i < 13; i++) 
        {  
                src.set(i*pukeWidth,0,(i+1)*pukeWidth,pukeHeight); 
                dst.set((int)(i/2f*pukeWidth),0,(int)((i/2f+1)*pukeWidth),pukeHeight);  
                c.drawBitmap(sourceBitmap, src, dst, null);
        }  
    }  

	@Override
	protected void onDraw(Canvas canvas) 
	{
		// TODO Auto-generated method stub
		drawDeck(canvas);
	}
	
	private void drawDeck(Canvas canvas)
	{
		personPaint(canvas,pukeWidth,pukeHeight);
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		this.flushThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
    class FlushThread extends Thread
    {
    	public final GameView gameView;
    	public final SurfaceHolder holder;
    	
    	public FlushThread(SurfaceHolder holder,GameView gameView) {
			// TODO Auto-generated constructor stub
    		this.gameView=gameView;
    		this.holder=holder;
		}	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Canvas canvas;  
            while (true) {  
                canvas = null;  
                try {  
                    canvas = this.holder.lockCanvas(null);  
                    synchronized (this.holder) {  
                        this.gameView.drawDeck(canvas);  
                    }  
                } finally {  
                    if (canvas != null) {  
                        this.holder.unlockCanvasAndPost(canvas);  
                    }  
                }  
  
                try {  
                    Thread.sleep(1000);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
            }  	
		}
    	
    }
}
