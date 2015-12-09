package lin.com.imagelibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

/**
 * 异步图片模糊 采用线程池
 *
 * @author Lin
 *
 */
public class AsyncImageBlur {
	ExecutorService mExecutorService;
	/**
	 * 默认开启线程数
	 */
	static int DEFAULT_THREAD_COUNT = 5;
	/**
	 * 记录已经加载图片的Imageview
	 */
	Map<ImageView, Integer> mImageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, Integer>());

	/**
	 * 记录正在加载图片的url
	 */
	List<BlurPhotoTask> mTaskQueue = new ArrayList<BlurPhotoTask>();

	public AsyncImageBlur() {
		mExecutorService = Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT);
	}

	public void blurBitmap(Bitmap src, ImageView imageview, int radius) {
		mImageViews.put(imageview, new Integer(radius));
		if (isTaskExisted(radius)) {
			return;
		}
		BlurPhotoTask task = new BlurPhotoTask(radius, imageview, src);
		synchronized (mTaskQueue) {
			mTaskQueue.add(task);
		}
		mExecutorService.execute(task);
	}

	/**
	 * 判断线程池是否存在url这个图片下载任务
	 *
	 * @param radius
	 * @return
	 */
	private boolean isTaskExisted(int radius) {
		if (radius < 0) {
			return false;
		}
		synchronized (mTaskQueue) {
			int size = mTaskQueue.size();
			for (int i = 0; i < size; i++) {
				BlurPhotoTask task = mTaskQueue.get(i);
				if (task != null && task.getRadius() == radius) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean imageViwReused(ImageView imageview, int radius) {
		int tag = mImageViews.get(imageview);
		if (tag >= 0 && tag != radius) {
			return true;
		}
		return false;
	}

	private void removeTask(BlurPhotoTask task) {
		synchronized (mTaskQueue) {
			mTaskQueue.remove(task);
		}
	}

	/**
	 * 释放所有内存资源
	 */
	public void destroy() {
		mImageViews.clear();
		mImageViews = null;
		mTaskQueue.clear();
		mTaskQueue = null;
		mExecutorService.shutdown();
		mExecutorService = null;
	}

	class BlurPhotoTask implements Runnable {
		int blur_radius;
		ImageView imageview;
		Bitmap srcBit;

		/**
		 * 图片加载，需要根据url的末尾是否有模糊字段进行处理
		 * @param radius
		 * @param imageview
		 * @param bitmap
		 */
		public BlurPhotoTask(int radius, ImageView imageview, Bitmap bitmap) {
			this.blur_radius = radius;
			this.imageview = imageview;
			this.srcBit = bitmap;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (imageViwReused(imageview, blur_radius)) {
				removeTask(this);
				return;
			}
			if (blur_radius < 0) {
				return;
			}
			if (srcBit != null) {
				float scaleFactor = AsyncImageLoader.BLUR_TAG_SCALEFACTOR;
				Bitmap overlay= Bitmap.createBitmap(
						(int) (srcBit.getWidth() / scaleFactor),
						(int) (srcBit.getHeight() / scaleFactor),
						Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(overlay);
				canvas.scale(1 / scaleFactor, 1 / scaleFactor);
				Paint paint = new Paint();
				paint.setFlags(Paint.FILTER_BITMAP_FLAG);
				canvas.drawBitmap(srcBit, 0, 0, paint);
				overlay = FastBlur.doBlur(overlay, (int) blur_radius, true);
//				Bitmap mDrawable = BlurFilter.blurFilter(srcBit, blur_radius,
//						blur_radius);
				if (!imageViwReused(imageview, blur_radius)) {
					BimapBlurDisplayer bd = new BimapBlurDisplayer(overlay,
							imageview, blur_radius);
					Activity a = (Activity) imageview.getContext();
					a.runOnUiThread(bd);
				}
				removeTask(this);
			}
		}

		public int getRadius() {
			return blur_radius;
		}

	}

	/**
	 * UI线程执行
	 *
	 * @author Lin
	 *
	 */
	class BimapBlurDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageview;
		int radius;

		public BimapBlurDisplayer(Bitmap b, ImageView imageview, int radius) {
			this.bitmap = b;
			this.imageview = imageview;
			this.radius = radius;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (imageViwReused(imageview, radius)) {
				return;
			}
			if (bitmap != null) {
				imageview.setImageBitmap(bitmap);
			}
		}

	}

}
