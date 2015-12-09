package lin.com.imagelibrary;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;

/**
 * 图片加载，二级缓存
 *
 * @author Lin
 *
 */
public class FileCache {
	File mCacheDir;

	/**
	 * 创建缓存目录，首先考虑SD卡 没有SD卡就暂时使用系统自带的缓存目录
	 *
	 * @param context
	 * @param cacheDir
	 * @param name
	 */
	public FileCache(Context context, File cacheDir, String name) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			mCacheDir = new File(cacheDir, name);
		} else {
			mCacheDir = context.getCacheDir();
		}
		if (!mCacheDir.exists()) {
			mCacheDir.mkdirs();
		}
	}

	/**
	 * url编译，避免中文乱码
	 *
	 * @param url
	 * @return
	 */
	public File getFile(String url) {
		File f = null;
		try {
			String filename = URLEncoder.encode(url, "utf-8");
			f = new File(mCacheDir, filename);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return f;
	}

	/**
	 * 清空二级缓存
	 */
	public void clear() {
		File[] files = mCacheDir.listFiles();
		for (File f : files) {
			f.delete();
		}
	}
}
