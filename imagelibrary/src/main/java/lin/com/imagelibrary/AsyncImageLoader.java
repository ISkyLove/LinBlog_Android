package lin.com.imagelibrary;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 异步加载图片，采用线程池
 *
 * @author Lin
 *
 */
public class AsyncImageLoader {
	MemoryCache memoryCache;
	FileCache fileCache;
	ExecutorService mExecutorService;
	/**
	 * 默认开启线程数
	 */
	static int DEFAULT_THREAD_COUNT = 5;
	/**
	 * 记录已经加载图片的Imageview
	 */
	Map<ImageView, String> mImageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());

	/**
	 * 记录正在加载图片的url
	 */
	List<LoadPhotoTask> mTaskQueue = new ArrayList<LoadPhotoTask>();

	/**
	 * 模糊字段，需要模糊的图片以blur_tag结尾名字保存
	 */
	public static final String BLUR_TAG = ".blur_tag";
	/**
	 * 缩放因子
	 */
	public static final float BLUR_TAG_SCALEFACTOR = 1;
	/**
	 * 模糊参数
	 */
	public static final float BLUR_TAG_RADIUS = 60;

	public AsyncImageLoader(Context context, MemoryCache memoryCache,
							FileCache fileCache) {
		this.memoryCache = memoryCache;
		this.fileCache = fileCache;
		mExecutorService = Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT);
	}

	/**
	 * 从一级缓存获取图片
	 *
	 * @param imageview
	 * @param url
	 * @return
	 */
	public Bitmap loadBitmap(ImageView imageview, String url) {
		mImageViews.put(imageview, url);
		Bitmap bitmap;
		bitmap = memoryCache.get(url);
		if (bitmap == null) {
			enqueLoadPhoto(url, imageview);
		}
		return bitmap;
	}

	/**
	 * 从一级缓存获取图片 是否模糊处理，若模糊处理，请求地址默认添加模糊字段在地址末端
	 *
	 * @param imageview
	 * @param url
	 * @param isblur
	 * @return
	 */
	public Bitmap loadBitmap(ImageView imageview, String url, boolean isblur) {
		if (isblur) {
			StringBuffer mBuffer = new StringBuffer();
			mBuffer.append(url);
			mBuffer.append(BLUR_TAG);
			url = mBuffer.toString();
		}
		mImageViews.put(imageview, url);
		Bitmap bitmap;
		bitmap = memoryCache.get(url);
		if (bitmap == null) {
			enqueLoadPhoto(url, imageview);
		}
		return bitmap;
	}

	/**
	 * 从二级缓存获取图片或者直接下载图片
	 *
	 * @param url
	 * @param imageview
	 */
	public void enqueLoadPhoto(String url, ImageView imageview) {
		if (isTaskExisted(url)) {
			return;
		}
		LoadPhotoTask task = new LoadPhotoTask(url, imageview);
		synchronized (mTaskQueue) {
			mTaskQueue.add(task);
		}
		mExecutorService.execute(task);
	}

	/**
	 * 判断线程池是否存在url这个图片下载任务
	 *
	 * @param url
	 * @return
	 */
	private boolean isTaskExisted(String url) {
		if (url == null) {
			return false;
		}
		synchronized (mTaskQueue) {
			int size = mTaskQueue.size();
			for (int i = 0; i < size; i++) {
				LoadPhotoTask task = mTaskQueue.get(i);
				if (task != null && task.getUrl().equals(url)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 从二级缓存或者网络获取图片
	 *
	 * @param url
	 * @return
	 */
	private Bitmap getBitmapByUrl(String url) {

		File f = fileCache.getFile(url);
		Bitmap b = null;
		if (f != null && f.exists()) {
			b = ImageUtil.decodeFile(f);
		}
		if (b != null) {
			return b;
		}
		return ImageUtil.loadBitmapFromWeb(url, f);
	}

	private boolean imageViwReused(ImageView imageview, String url) {
		String tag = mImageViews.get(imageview);
		if (tag == null || !tag.equals(url)) {
			return true;
		}
		return false;
	}

	private void removeTask(LoadPhotoTask task) {
		synchronized (mTaskQueue) {
			mTaskQueue.remove(task);
		}
	}

	/**
	 * 释放所有内存资源
	 */
	public void destroy() {
		memoryCache.clear();
		memoryCache = null;
		mImageViews.clear();
		mImageViews = null;
		mTaskQueue.clear();
		mTaskQueue = null;
		mExecutorService.shutdown();
		mExecutorService = null;
	}

	class LoadPhotoTask implements Runnable {
		String url;
		ImageView imageview;

		/**
		 * 图片加载，需要根据url的末尾是否有模糊字段进行处理
		 *
		 * @param url
		 * @param imageview
		 */
		public LoadPhotoTask(String url, ImageView imageview) {
			this.url = url;
			this.imageview = imageview;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (imageViwReused(imageview, url)) {
				removeTask(this);
				return;
			}
			Bitmap bmp = getBitmapByUrl(url);

			memoryCache.put(url, bmp);

			if (!imageViwReused(imageview, url)) {
				BimapDisplayer bd = new BimapDisplayer(bmp, imageview, url);
				Activity a = (Activity) imageview.getContext();
				a.runOnUiThread(bd);
			}
			removeTask(this);
		}

		public String getUrl() {
			return url;
		}

	}

	/**
	 * UI线程执行
	 *
	 * @author Lin
	 *
	 */
	class BimapDisplayer implements Runnable {
		Bitmap bitmap;
		ImageView imageview;
		String url;

		public BimapDisplayer(Bitmap b, ImageView imageview, String url) {
			this.bitmap = b;
			this.imageview = imageview;
			this.url = url;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (imageViwReused(imageview, url)) {
				return;
			}
			if (bitmap != null) {
				imageview.setImageBitmap(bitmap);
			}
		}

	}
}
