package com.example.videoplayerusingmediaplayer;

import java.io.IOException;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	Display display;
	SurfaceHolder surfaceHolder;
	SurfaceView surfaceView;
	MediaPlayer mediaPlayer;
	// 视频宽高
	int width = 0;
	int height = 0;

	boolean ready = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		display = getWindowManager().getDefaultDisplay();

		String file = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Movies/1.m4v";

		surfaceView = (SurfaceView) findViewById(R.id.sv);
		surfaceHolder = surfaceView.getHolder();

		surfaceHolder.addCallback(new SurfaceHolder.Callback() {

			// 当SurfaceView销毁时调用
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub

			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				mediaPlayer.setDisplay(holder);
			}

			// 当SurfaceView大小发生变化时调用
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// TODO Auto-generated method stub

			}
		});

		// surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mediaPlayer = new MediaPlayer();

		mediaPlayer
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub

					}
				});

		mediaPlayer
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						Log.v("MediaPlayer", "play completion");
					}
				});

		mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {

				if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
					Log.v("MediaPlayer", "MediaPlayer Server died");
				} else if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
					Log.v("MediaPlayer", "Unknown error in MediaPlayer: "
							+ extra);
				}

				// 这里返回false表明没有处理捕捉到的错误，将交由上层来处理。如果mediaPlayer注册了OnCompletionListener,系统将会调用其onCompletion方法
				return false;
			}
		});
		
		mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
			
			@Override
			public void onSeekComplete(MediaPlayer mp) {
				Log.v("MediaPlayer", "MediaPlayer seek complete");
			}
		});
		
		mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
			
			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
				Log.v("MediaPlayer", "VideoSizeChanged: " + String.valueOf(width) + "x" + String.valueOf(height));
			}
		});

		mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {

				// 获取视频大小
				width = mp.getVideoWidth();
				height = mp.getVideoHeight();

				// 对视频宽高进行缩放
				if (width > display.getWidth() || height > display.getHeight()) {
					float heightRatio = (float) height / display.getHeight();
					float widthRatio = (float) width / display.getWidth();

					if (heightRatio > 1 || widthRatio > 1) {

						if (heightRatio > widthRatio) {
							height = (int) Math.ceil(height / heightRatio);
							width = (int) Math.ceil(width / heightRatio);
						} else {
							height = (int) Math.ceil(height / widthRatio);
							width = (int) Math.ceil(width / widthRatio);
						}
					}
				}

				// 设置SurfaceView的大小
				surfaceView.setLayoutParams(new LinearLayout.LayoutParams(
						width, height));
				
				//开始播放
				mp.start();
			}
		});

		try {
			mediaPlayer.setDataSource(file);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			finish();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			finish();
		} catch (IOException e) {
			e.printStackTrace();
			finish();
		}

		try {
			mediaPlayer.prepareAsync();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			finish();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
