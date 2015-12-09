package lin.com.imagelibrary;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.graphics.Bitmap;

/**
 * 图片加载，一级缓存
 * @author Lin
 *
 */
public class MemoryCache {
	/*最大缓存数*/
	static final int MAX_CACHE_CAPACITY=30;
	/*软引用，方便回收*/
	HashMap<String,SoftReference<Bitmap>> mCacheMap=new LinkedHashMap<String,SoftReference<Bitmap>>();

	//是否清除旧的缓存
	public boolean removeEldestEntry(){
		if(mCacheMap.size()>MAX_CACHE_CAPACITY){
			return true;
		}
		return false;
	}

	//取图片
	public Bitmap get(String id){
		if(!mCacheMap.containsKey(id))  return null;
		SoftReference<Bitmap>  ref=mCacheMap.get(id);
		return ref.get();
	}

	//添加图片进缓存
	public void put(String id,Bitmap bmp){
		mCacheMap.put(id, new SoftReference<Bitmap>(bmp));
	}

	//清除所有缓存
	public void clear(){
		try{
			for(Map.Entry<String,SoftReference<Bitmap>> entry:mCacheMap.entrySet()){
				SoftReference<Bitmap>  sr=entry.getValue();
				if(null!=sr){
					Bitmap bmp=sr.get();
					if(bmp!=null){
						bmp.recycle();
						bmp=null;
					}
				}
			}
			mCacheMap.clear();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
