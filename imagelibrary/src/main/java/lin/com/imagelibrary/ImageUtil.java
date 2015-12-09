package lin.com.imagelibrary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * 图片帮助类
 *
 * @author Lin
 *
 */
public class ImageUtil {
	public static final int CONN_TIMEOUT = 10000;
	public static final int CONN_READ_TIMEOUT = 10000;

	/**
	 * 从网络下载图片
	 *
	 * @param url
	 * @param file
	 * @return
	 */
	public static Bitmap loadBitmapFromWeb(String url, File file) {
		HttpURLConnection conn = null;
		InputStream is = null;
		OutputStream os = null;
		try {
			Bitmap bitmap = null;
			String newurl = url;
			if (url.endsWith(AsyncImageLoader.BLUR_TAG)) {
				int mask = url.lastIndexOf(AsyncImageLoader.BLUR_TAG);
				newurl = url.substring(0, mask);
			}
			URL imageUrl = new URL(newurl);
			conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(CONN_TIMEOUT);
			conn.setReadTimeout(CONN_READ_TIMEOUT);
			conn.setInstanceFollowRedirects(true);
			is = conn.getInputStream();
			os = new FileOutputStream(file);
			Bitmap urlbitmap = BitmapFactory.decodeStream(is);
			if (url.endsWith(AsyncImageLoader.BLUR_TAG)) {
				float scaleFactor = AsyncImageLoader.BLUR_TAG_SCALEFACTOR;
				float radius = AsyncImageLoader.BLUR_TAG_RADIUS;
				Bitmap overlay = Bitmap.createBitmap(
						(int) (urlbitmap.getWidth() / scaleFactor),
						(int) (urlbitmap.getHeight() / scaleFactor),
						Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(overlay);
				canvas.scale(1 / scaleFactor, 1 / scaleFactor);
				Paint paint = new Paint();
				paint.setFlags(Paint.FILTER_BITMAP_FLAG);
				canvas.drawBitmap(urlbitmap, 0, 0, paint);
				overlay = FastBlur.doBlur(overlay, (int) radius, true);
				overlay.compress(Bitmap.CompressFormat.JPEG, 100, os);
			} else {
				urlbitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
			}
			try {
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bitmap = decodeFile(file);

			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 从二级缓存读取文件并转换成bitmap
	 *
	 * @param f
	 * @return
	 */
	public static Bitmap decodeFile(File f) {
		try {
			return BitmapFactory.decodeStream(new FileInputStream(f), null,
					null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			int count = -1;
			do {
				count = is.read(bytes, 0, buffer_size);
				if (count > 0) {
					os.write(bytes, 0, count);
				}
			} while (count > 0);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap, float rx,
												float ry) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight()));
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, rx, ry, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			final Rect src = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());

			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}

}
