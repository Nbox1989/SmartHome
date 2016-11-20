package com.test.UI;

import com.test.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MyButton extends Button implements View.OnTouchListener{

	public MyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundResource(R.drawable.roundedrectangle);
		setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == MotionEvent.ACTION_DOWN) 
    	{ 
			setBackgroundResource(R.drawable.roundedrectangle_pressed); 
	    }
    	else if(event.getAction() == MotionEvent.ACTION_UP) 
    	{ 
    		setBackgroundResource(R.drawable.roundedrectangle);
	    } 
		return false;
	}
}
