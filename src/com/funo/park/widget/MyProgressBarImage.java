package com.funo.park.widget;

import com.funo.park.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class MyProgressBarImage extends ProgressBar {

	public MyProgressBarImage(Context context, AttributeSet attribute) {
		super(context, attribute);

		this.setIndeterminateDrawable(getResources().getDrawable(R.drawable.anim_loading_image));
		this.setIndeterminate(false);
	}

}
