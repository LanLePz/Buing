package com.ahyapo.buing.widget;

import android.content.Context;
import android.widget.Toast;

/**
 * 按两次返回键退出
 * @author VanishMagic
 * 备注：如有特殊退出需求，可修改退出方式
 */
public class TwiceBackOut {
	public static long firstTimeBack = (long) 0;
	/**
	 * 
	 * @param context 当前Activity
	 * @param outTipStr 退出提示
	 * @param twiceBackIntervalTime 能够退出的两次按返回键的时间间隔
	 * @return
	 */
	public static boolean twiceBackOut(Context context,String outTipStr,int twiceBackIntervalTime){
		long secondTimeBack = System.currentTimeMillis();
		if (secondTimeBack - firstTimeBack > twiceBackIntervalTime) {// 如果两次按键时间间隔大于twiceBackIntervalTime毫秒，则不退出
			Toast.makeText(context, outTipStr,
					Toast.LENGTH_SHORT).show();
			firstTimeBack = secondTimeBack;// 更新firstTimeBack
			return true;
		} else {
			System.exit(0);// 否则退出程序，可以修改退出方式
			return false;
		}
	}
	
}