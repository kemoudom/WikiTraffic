package itc.edu.tools;

import itc.gic.trafficreportingsystem.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageSwitcher;


public class ImageSwitcherLoader {
    
    //the simplest in-memory cache implementation. This should be replaced with something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
    private HashMap<String, Bitmap> cache=new HashMap<String, Bitmap>();
    int no_image_id;
    //final int loading_image_id = R.drawable.loading;    
    final int loading_image_id = R.drawable.ic_launcher;    
    private File cacheDir;
    
    public File getCachDir(){
    	return cacheDir;
    }
    public ImageSwitcherLoader(Context context){
        //Make the background thead low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
        
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"LRV");
        else{
        	cacheDir=context.getCacheDir();
        }
        if(!cacheDir.exists()){
        	cacheDir.mkdirs();
        }
            
    }
    public void DisplayImage(String url, Activity activity, ImageSwitcher imageView,int noImgId)
    {
    	no_image_id = noImgId;

    	MyTool.log(new Exception(), imageView+"");
    	imageView.setImageResource(loading_image_id);
    	//imageView.setImageBitmap(null);
    	imageView.setTag(url);
    	
    	//url = MyTool.encodeTextForPasseInUrl(url);
    	
    	//imageView.setVisibility(View.GONE);    	    
        if(cache.containsKey(url)){
        	if (cache.get(url)!=null) {
        		//imageView.clearAnimation();
        		Drawable drawable = new BitmapDrawable(activity.getResources(), cache.get(url));
                imageView.setImageDrawable(drawable);//(cache.get(url));	
                MyTool.log(new Exception(), "in cach");
        	}
        	else{
        		cache.remove(url);
        		queuePhoto(url, activity, imageView);
        	}
        }
        else
        {        
        	
	        
            queuePhoto(url, activity, imageView);
            
        }    
    }
    
    public void DisplayImage(String url, Activity activity, ImageSwitcher imageView,int noImgId, int ImgLoading)
    {
    	no_image_id = noImgId;
    	MyTool.log(new Exception(), imageView+"");
    	imageView.setImageResource(ImgLoading);
    	//imageView.setImageBitmap(null);
    	imageView.setTag(url);
    	
//    	url = MyTool.encodeTextForPasseInUrl(url);
    	
    	//imageView.setVisibility(View.GONE);    	    
        if(cache.containsKey(url)){
        	if (cache.get(url)!=null) {
            	//imageView.setVisibility(View.VISIBLE);
        		Drawable drawable = new BitmapDrawable(activity.getResources(), cache.get(url));
                imageView.setImageDrawable(drawable);//(cache.get(url));	
                
        	}
        	else{
        		cache.remove(url);
        		queuePhoto(url, activity, imageView);
        	}
        }
        else
        {        	
            queuePhoto(url, activity, imageView);
            
        }    
    }
        
    private void queuePhoto(String url, Activity activity, ImageSwitcher imageView)
    {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them.
        photosQueue.Clean(imageView);
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        synchronized(photosQueue.photosToLoad){
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }
        //start thread if it's not started yet
        if(photoLoaderThread.getState()==Thread.State.NEW){
            photoLoaderThread.start();
        }
    }
    
    private Bitmap getBitmap(String url) 
    {
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        File f=new File(cacheDir, filename);
        
        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null)
            return b;
        
        //from web
        try {
            Bitmap bitmap=null;
            
            InputStream is=new URL(url).openStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            MyTool.log(new Exception());
            return bitmap;
        } catch (Exception ex){
           ex.printStackTrace();
           MyTool.log(new Exception());
           return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=1;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
    
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageSwitcher imageView;
     /*   public Bitmap imageLoading;
        public Bitmap imageLoadFail;*/
        
        public PhotoToLoad(String u, ImageSwitcher i){
            url=u; 
            imageView=i;
        }
        
       /* public PhotoToLoad(String u, ImageSwitcher i, Bitmap imageLoading, Bitmap imageLoadFail){
        	url=u; 
            imageView=i;
        	this.imageLoading = imageLoading;
        	this.imageLoadFail = imageLoadFail;
        }*/
    }
    
    PhotosQueue photosQueue=new PhotosQueue();
    
    public void stopThread()
    {
        photoLoaderThread.interrupt();
    }
    
    //stores list of photos to download
    class PhotosQueue
    {
        private Stack<PhotoToLoad> photosToLoad=new Stack<PhotoToLoad>();
        
        //removes all instances of this ImageView
        public void Clean(ImageSwitcher image)
        {
            for(int j=0 ;j<photosToLoad.size();){
                if(photosToLoad.get(j).imageView==image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
    }
    
    class PhotosLoader extends Thread {
        public void run() {
            try {
                while(true)
                {
                    //thread waits until there are any images to load in the queue
                    if(photosQueue.photosToLoad.size()==0)
                        synchronized(photosQueue.photosToLoad){
                            photosQueue.photosToLoad.wait();
                        }
                    if(photosQueue.photosToLoad.size()!=0)
                    {
                        PhotoToLoad photoToLoad;
                        synchronized(photosQueue.photosToLoad){
                            photoToLoad=photosQueue.photosToLoad.pop();
                        }
                        Bitmap bmp=getBitmap(photoToLoad.url);
                        cache.put(photoToLoad.url, bmp);
                        Object tag=photoToLoad.imageView.getTag();
                        if(tag!=null && ((String)tag).equals(photoToLoad.url)){
                            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad.imageView);
                            Activity a=(Activity)photoToLoad.imageView.getContext();
                            a.runOnUiThread(bd);
                        }
                    }
                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
    }
    
    PhotosLoader photoLoaderThread=new PhotosLoader();
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        ImageSwitcher imageView;
        public BitmapDisplayer(Bitmap b, ImageSwitcher i){bitmap=b;imageView=i;}
        public void run()
        {
            if(bitmap!=null){
            	//imageView.setVisibility(View.VISIBLE);
//                imageView.setImageBitmap(bitmap);
        		Drawable drawable = new BitmapDrawable(imageView.getContext().getResources(), bitmap);
                imageView.setImageDrawable(drawable);//(cache.get(url));	
            }
            else{
//            	imageView.setImageBitmap(null);
            	imageView.setImageResource(no_image_id);
            	//imageView.setVisibility(View.GONE);
            }
        }
    }
    public String getFilePath(String fileName){
    	return cacheDir.getAbsoluteFile()+"/"+String.valueOf(fileName.hashCode());
    }
    public void clearCache(String fileName){

    	cache.remove(fileName);
        File[] files=cacheDir.listFiles();
        fileName=String.valueOf(fileName.hashCode());
        for(File f:files){
        	if (f.getName().compareToIgnoreCase(fileName)==0){
        		f.delete();
        		return ;
        	}
        }
    }
    public void clearCache() {
        //clear memory cache
        cache.clear();
        
        //clear SD cache
        File[] files=cacheDir.listFiles();
        for(File f:files)
            f.delete();
    }

}
