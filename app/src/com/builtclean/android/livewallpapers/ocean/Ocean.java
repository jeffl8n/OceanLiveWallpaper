package com.builtclean.android.livewallpapers.ocean;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class Ocean extends WallpaperService {

	private final Handler mHandler = new Handler();

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new OceanEngine();
	}

	class OceanEngine extends Engine {

		public int offsetX = 0;
		public int offsetY = 0;
		public int height;
		public int width;
		public int visibleWidth;

		public int nextImage;
		public int currentImage = 0;

		public String[] images = { "ocean_01", "ocean_02", "ocean_03", "ocean_04",
				"ocean_05", "ocean_06", "ocean_07", "ocean_08", "ocean_09",
				"ocean_10", "ocean_11", "ocean_12", "ocean_13", "ocean_14",
				"ocean_15", "ocean_16", "ocean_17", "ocean_18", "ocean_19",
				"ocean_20", "ocean_21", "ocean_22", "ocean_23", "ocean_24",
				"ocean_25", "ocean_26", "ocean_27", "ocean_28", "ocean_29",
				"ocean_30", "ocean_31", "ocean_32", "ocean_33", "ocean_34",
				"ocean_35", "ocean_36", "ocean_37", "ocean_38", "ocean_39",
				"ocean_40", "ocean_41", "ocean_42", "ocean_43", "ocean_44",
				"ocean_45", "ocean_46", "ocean_47", "ocean_48" };

		private final Runnable mDrawOcean = new Runnable() {
			public void run() {
				drawFrame();
			}
		};
		private boolean mVisible;

		private MediaPlayer oceanPlayer;

		OceanEngine() {
			Resources res = getResources();
			nextImage = res.getIdentifier("bacon_s01", "drawable",
					"com.builtclean.android.livewallpapers.ocean");
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);

			setTouchEventsEnabled(true);

			oceanPlayer = MediaPlayer.create(getApplicationContext(),
					R.raw.ocean);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			mHandler.removeCallbacks(mDrawOcean);

			if(oceanPlayer != null) {
				oceanPlayer.release();
			}
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			mVisible = visible;
			if (visible) {
				drawFrame();
			} else {
				mHandler.removeCallbacks(mDrawOcean);
			}
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			mVisible = false;
			mHandler.removeCallbacks(mDrawOcean);
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {

			this.height = height;
			if (this.isPreview()) {
				this.width = width;
			} else {
				this.width = 4 * width;
			}
			this.visibleWidth = width;

			drawFrame();

			super.onSurfaceChanged(holder, format, width, height);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset,
				float xOffsetStep, float yOffsetStep, int xPixelOffset,
				int yPixelOffset) {

			this.offsetX = xPixelOffset;
			this.offsetY = yPixelOffset;

			drawFrame();

			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep,
					xPixelOffset, yPixelOffset);
		}

		@Override
		public Bundle onCommand(String action, int x, int y, int z,
				Bundle extras, boolean resultRequested) {

			Bundle bundle = super.onCommand(action, x, y, z, extras,
					resultRequested);

			if (action.equals("android.wallpaper.tap")) {
				playOceanSound();
			}

			return bundle;
		}

		void drawFrame() {
			final SurfaceHolder holder = getSurfaceHolder();

			Canvas c = null;
			try {
				c = holder.lockCanvas();
				if (c != null) {
					drawOcean(c);
				}
			} finally {
				if (c != null)
					holder.unlockCanvasAndPost(c);
			}

			mHandler.removeCallbacks(mDrawOcean);
			if (mVisible) {
				mHandler.postDelayed(mDrawOcean, 1000 / 30);
			}
		}

		void drawOcean(Canvas c) {

			Resources res = getResources();

			if (++currentImage == images.length) {
				currentImage = 0;
			}

			nextImage = res.getIdentifier(images[currentImage], "drawable",
					"com.builtclean.android.livewallpapers.ocean");

			c.drawBitmap(BitmapFactory.decodeResource(res, nextImage),
					this.offsetX, this.offsetY, null);
		}

		void playOceanSound() {
			oceanPlayer.seekTo(0);
			oceanPlayer.start();
		}
	}
}