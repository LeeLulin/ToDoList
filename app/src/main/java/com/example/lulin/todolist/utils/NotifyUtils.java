package com.example.lulin.todolist.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lulin.todolist.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class NotifyUtils {

	private static MediaPlayer player;
	private static Timer timer_play;
	private static int curPosition = 0;

	/**
	 * 有人提醒任务时弹出的卡片
	 * @param context
	 * @param card
	 */
	public static void showZixiAlertToast(final Context context, final Card card) {
		final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		final View view = View.inflate(context, R.layout.toast_alert_notify, null);
		ImageView iv_bell = (ImageView) view.findViewById(R.id.iv_bell);
		ImageView iv_photo = (ImageView) view.findViewById(R.id.iv_photo);
		TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
		TextView tv_from = (TextView) view.findViewById(R.id.tv_from);
		TextView tv_zixitime = (TextView) view.findViewById(R.id.tv_zixitime);
		TextView tv_zixiname = (TextView) view.findViewById(R.id.tv_zixiname);
		TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
		ViewGroup ll_audio = (ViewGroup) view.findViewById(R.id.ll_audio);
		final ImageButton ib_play = (ImageButton) view.findViewById(R.id.ib_play);
		final ProgressBar pb = (ProgressBar) view.findViewById(R.id.pb);
		tv_type.setText("任务提醒");
		tv_from.setText("来自：" + card.getFnick());
		tv_zixitime.setText("");
		tv_zixiname.setText(card.getZixiName());
		tv_content.setText(card.getContent());
		ll_audio.setVisibility(card.getAudioUrl() != null ? View.VISIBLE : View.GONE);
		// 设置任务已提醒，不需要本地系统提醒了


		// 铃铛动画
		iv_bell.setBackgroundResource(R.drawable.alert_bell_anim);
		AnimationDrawable draw = (AnimationDrawable) iv_bell.getBackground();
		draw.start();

		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		params.height = WindowManager.LayoutParams.MATCH_PARENT;
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.gravity = Gravity.BOTTOM;
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		// params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
		// WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
		// | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.windowAnimations = android.R.style.Animation_InputMethod;
		wm.addView(view, params);

		// 知道了按钮
		view.findViewById(R.id.ll_save).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeMyToast(wm, view);
			}
		});
	}




	/**
	 * 移除卡片
	 * @param wm
	 * @param view
	 */
	private static void removeMyToast(final WindowManager wm, final View view) {
		// params.windowAnimations = android.R.style.Animation_Toast;
		// wm.updateViewLayout(view, params);
		if (view != null) wm.removeView(view);
	}


}
