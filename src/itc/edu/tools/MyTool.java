package itc.edu.tools;

import itc.gic.model.Spot;
import itc.gic.trafficreportingsystem.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MyTool {
	
	public static final String FONT_NAME = "CamMob.ttf";
	
	private static boolean isDebuggable;
	private static boolean isAlertShow;
	private static Context context;
	private static ProgressDialog pgd;
	public static ImageSwitcherLoader imgSwitcherLoader;
	//public static ImageLoader imageLoader;
	//public static KhmerRender kr;
	//public static DisplayImageOptions options;
	
	
	public static long getTimeStamp(){
		return System.currentTimeMillis();
	}
	public static void sendGmail(String to,String subject,String text,Context context){
		Intent send = new Intent(Intent.ACTION_SENDTO);
		String uriText;

		uriText = "mailto:" + to + 
		          "?subject=" + subject + 
		          "&body=" + text;
		uriText = uriText.replace(" ", "%20");
		Uri uri = Uri.parse(uriText);

		send.setData(uri);
		context.startActivity(Intent.createChooser(send, "Send mail..."));
	}
	public static String formatDecimal2Place(int val){
		//"##00"
		DecimalFormat df = new DecimalFormat("##00");
		return df.format(val);
	}
	public static String getErrorLineInfo(Exception info){
		return "Error at: "+ getLineInfo(info);
	}
	public static String getLineInfo(Exception info){
		String tag = info.getStackTrace()[0].getClassName()+"."+info.getStackTrace()[0].getMethodName()+":"+info.getStackTrace()[0].getLineNumber();
		return tag;
	}
	public static int getLastDate(){
	    Calendar calendar = Calendar.getInstance();
	    int lastDate = calendar.getActualMaximum(Calendar.DATE);
	    return lastDate;
	}
	public static void hideKeyboard(Activity activity){
		InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS); 
	}
	public static String FormatDate(String formatIn,String formatOut,String date){
		Date d = null;
		SimpleDateFormat outputFormat = new SimpleDateFormat(formatIn);
		SimpleDateFormat formater = new SimpleDateFormat(formatOut);
		
		
		try {
			d = outputFormat.parse(date);
			return formater.format(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	public static String FormatDate(Date date,String formatOut){
		SimpleDateFormat formater = new SimpleDateFormat(formatOut);
		return formater.format(date);
	}	
	public static Date FormatDate(String formatIn,String date){
		Date d = null;
		SimpleDateFormat outputFormat = new SimpleDateFormat(formatIn);
		try {
			d = outputFormat.parse(date);
			return d;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static long getTimeStam(String dataStr,String formatData){
		Date d = null;
		SimpleDateFormat outputFormat = new SimpleDateFormat(formatData);
		try {
			d = outputFormat.parse(dataStr);
			return d.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1; 
		}
	}
	public static void hideKeyBoard(Activity activity){
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	public static void init(Context cnt){
		 
		context = cnt;		
		isDebuggable = (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
		isAlertShow = false;
		imgSwitcherLoader = new ImageSwitcherLoader(context);
		
		// Creates display image options for custom display task (all options are optional)
		/*options = new DisplayImageOptions.Builder()
												.showStubImage(R.drawable.loading)
												.showImageForEmptyUri(R.drawable.no_image)
												.cacheInMemory()
												.cacheOnDisc()		                                       
												.build();
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "MPMAG/Cache");
		ImageLoaderConfiguration configH = new ImageLoaderConfiguration.Builder(context)
		        .memoryCacheExtraOptions(480, 800) // max width, max height
		        .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75) // Can slow ImageLoader, use it carefully (Better don't use it)
		        .threadPoolSize(3)
		        .threadPriority(Thread.NORM_PRIORITY - 1)
		        .denyCacheImageMultipleSizesInMemory()
		        .offOutOfMemoryHandling()
		        .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation
		        .discCache(new UnlimitedDiscCache(cacheDir)) // You can pass your own disc cache implementation
		        .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
		        .imageDownloader(new URLConnectionImageDownloader(5 * 1000, 20 * 1000)) // connectTimeout (5 s), readTimeout (20 s)
		        .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
		        .enableLogging()
		        .build();
		// Get singletone instance of ImageLoader
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//	        .memoryCacheExtraOptions(480, 800) // max width, max height
//	        .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75) // Can slow ImageLoader, use it carefully (Better don't use it)
//	        .threadPoolSize(3)
//	        .threadPriority(Thread.NORM_PRIORITY - 1)
//	        .denyCacheImageMultipleSizesInMemory()
//	        .offOutOfMemoryHandling()
//	        //.memoryCache(new WeakMemoryCache()) // You can pass your own memory cache implementation
//	        .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
//	        .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
//	        .imageDownloader(new URLConnectionImageDownloader(5 * 1000, 20 * 1000)) // connectTimeout (5 s), readTimeout (20 s)
//	        .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
//	        .enableLogging()
//	        .build();
		imageLoader = ImageLoader.getInstance();
		// Initialize ImageLoader with configuration. Do it once.
//		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		imageLoader.init(configH);*/
	}
	
	public static String getHashKey(String str) {
		return str.hashCode()+"";
	}
	
	public static void log(Exception info){		
		log(info,"");
	}	
	public static void log(Exception info,String msg){
		
		if(isDebuggable==true){
			String tag = info.getStackTrace()[0].getClassName()+"."+info.getStackTrace()[0].getMethodName()+":"+info.getStackTrace()[0].getLineNumber();
			Log.i(tag,"??? "+msg);
		}
	}
	public static void alert(String message,Context cnt){
		alert(cnt,"",message);
	}
	
	public static void alert(Context cnt,String title,String message){
		if (isAlertShow==false){
			isAlertShow = true;
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(cnt);
			alt_bld.setTitle(title);
			alt_bld.setMessage(message)
			.setCancelable(true)
			.setPositiveButton(cnt.getString(R.string.app_name), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				isAlertShow = false;
			}
			});
			
			try {
				AlertDialog alert = alt_bld.create();
				alert.show();			

			} catch (Exception e) {
				// TODO: handle exception
				isAlertShow = false;
			}
		}
		
	}
	
    public static boolean showProgressDialog(String text,Context cnt){
    	if (pgd==null){
    		pgd = ProgressDialog.show(cnt, "", text,true,false);
    		return true;
    	}
    	else{
	    	if(pgd.isShowing()==false){
	    		
	    			pgd = ProgressDialog.show(cnt, "", text,true,false);
				
	    		
	    		return true;
	    	}
	    	else{
	    		return false;
	    	}
    	}
    }
	public static void alert(Context cnt,String message,DialogInterface.OnClickListener onClick_btnOk,String captionOk,DialogInterface.OnClickListener onClick_btnCancel,String captionCancel){
		alert(cnt,"",message,onClick_btnOk,captionOk,onClick_btnCancel,captionCancel);
	}

    public static void alert(Context cnt,String message,DialogInterface.OnClickListener onClick_btnOk,String captionOk){
		alert(cnt,"",message,onClick_btnOk,captionOk);
	}
	public static void alert(Context cnt,String title,String message,DialogInterface.OnClickListener onClick_btnOk,String captionOk){

		alert(cnt,title,message,onClick_btnOk,captionOk,null,null);
	}
	public static void alert(Context cnt,String title,String message,DialogInterface.OnClickListener onClick_btnOk,String captionOk,DialogInterface.OnClickListener onClick_btnCancel,String captionCancel){
//		if (isAlertShow==false){
//			isAlertShow = true;
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(cnt);
			alt_bld.setTitle(title);
			alt_bld.setMessage(Html.fromHtml(message))
			.setCancelable(true)
			.setPositiveButton(captionOk, onClick_btnOk);
			if(onClick_btnCancel!=null){
				alt_bld.setNegativeButton(captionCancel, onClick_btnCancel);
			}
			
			try {
				AlertDialog alert = alt_bld.create();
				alert.show();

			} catch (Exception e) {
				// TODO: handle exception
			}						
//		}
		
	}
	
	public static void dismissProgressDialog(){
		if(pgd!=null){
			if(pgd.isShowing()){
				try {
					pgd.dismiss();					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
	}
	
	public static void appendLog(String text)
	{
	    File logFile = new File("sdcard/log.txt");
	    if (!logFile.exists())
	    {
	        try
	        {
	            logFile.createNewFile();
	        } catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	    }
	    try
	    {
	        // BufferedWriter for performance, true to set append to file flag
	        BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
	        buf.append(text);
	        buf.newLine();
	        buf.close();
	    } catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	}
	
	public static void writeTextToFile(String filename, Context cnt, String text){
		try {
			FileOutputStream fos = cnt.openFileOutput(filename, Context.MODE_PRIVATE);
			fos.write(text.getBytes());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String readTextFromFile(String filename, Context cnt){
		String datax = "" ;
		
        try {
            FileInputStream fIn = cnt.openFileInput ( filename ) ;
            InputStreamReader isr = new InputStreamReader ( fIn ) ;
            BufferedReader buffreader = new BufferedReader ( isr ) ;

            String readString = buffreader.readLine ( ) ;
            while ( readString != null ) {
                datax = datax + readString ;
                readString = buffreader.readLine ( ) ;
            }

            isr.close ( ) ;
        } catch ( IOException ioe ) {
            ioe.printStackTrace ( ) ;
        }
        return datax ;
	}
	
	public static boolean FileExist(String filename, Context cnt){
		File file = context.getFileStreamPath(filename);
		if(file.exists()) return true;
		return false;
	}
	
	public static String readTextFromUrl_(String urlStr){
		try {
			
			URI _url;
			_url = new URI(urlStr.replaceAll(" ", "%20"));

		    HttpGet httpget = new HttpGet(_url);
		    DefaultHttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response;
			response = httpclient.execute(httpget);		    
		    if (response.getStatusLine().getStatusCode()==200) {
		        String s =  new String(EntityUtils.toString(response.getEntity()));
		        return s.trim();
		    } else {
//		      Log.d("TESTING", "error receiving content");
		    }
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		    
		return "";
	}
	public static String encodeTextForPasseInUrl_UTF8(String text){
		try {
			text = URLEncoder.encode(text,"UTF-8");
//			text = URLEncoder.encode(text,"ISO-8859-1");			
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		} 
		return text;
	}
	public static String encodeTextForPasseInUrl(String text){
		try {
//			text = URLEncoder.encode(text,"UTF-8");
			text = URLEncoder.encode(text,"ISO-8859-1");
			
			
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		} 
		return text;
	}
	public static String readTextTileInAssets(Context context,String FileName){
		String result = "";
		AssetManager assetManager = context.getAssets();
		InputStream inputStream = null;
		try {
			inputStream = assetManager.open(FileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		result = readTextFile(inputStream);
		return result;
	}
	public static String readTextFile(InputStream inputStream) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int len;
		try {
			while ((len = inputStream.read(buf)) != -1) {
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
		}
		return outputStream.toString();
	}
	
	public static String getSystemLanguage(){
		return Locale.getDefault().getDisplayLanguage();
	}
	
	public static boolean isOnline(Context cnt) {
	    ConnectivityManager connMgr = (ConnectivityManager) 
	            cnt.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    return (networkInfo != null && networkInfo.isConnected());
	}
	
	public static String convertDate(String dateFromDB){
		String[] split_date = dateFromDB.split(" ");
		String date1 = split_date[0];
		String[] spl_date1 = date1.split("-");
		String date = spl_date1[2] +"-"+ spl_date1[1]+ "-" +spl_date1[0];
		
		return date;
	}
	
	public static void openPlaystore(Context context, String appPackageName){
		Intent marketIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appPackageName));
		marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		context.startActivity(marketIntent);
	}
	
	public static void phoneCall(Context context, String number){
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:"+number));
		context.startActivity(callIntent);
	}
	
	public static void phoneDial(Context context, String number){
		Intent callIntent = new Intent(Intent.ACTION_DIAL);
		callIntent.setData(Uri.parse("tel:"+number));
		context.startActivity(callIntent);
	}
	
	public static void openAssetsImage(Context context, String assetFilePath, ImageView image){
		InputStream path = null;
		try {
			path = context.getAssets().open(assetFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Bitmap bm = BitmapFactory.decodeStream(path);
		image.setImageBitmap(bm);
	} 
	
	public static void email(Context context, String mail){
		/* Create the Intent */
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		/* Fill it with Data */
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mail});
//		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
//		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

		context.startActivity(emailIntent);
	}
	
	public static void openWebBrowser(Context context, String url){
		if (!(url.indexOf("http://") > -1)){
			url = "http://"+url;
		}
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		context.startActivity(browserIntent);
	}
	
	public static String getKhmerDate(String date){
		String res = "default";
		
		String day = date.substring(8);
		if(day.length() > 10)
			day = date.substring(8, 10);
		//MyTool.log(new Exception(), MyApplication.kr.renderKhmer("ពង្រីក  បង្រួម ជំរើសនៃការចែករំលែក")); 
		SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			Date dateObj = curFormater.parse(date);
			MyTool.log(new Exception(), dateObj.toString());
			//MyTool.log(new Exception(), "Month"+Integer.toString(dateObj.getMonth()));
			//res = String.format(MyApplication.strPublishedDateArticleFormat, day, MyApplication.khmer_month_data[dateObj.getMonth()], date.substring(0, 4));  
			
			//MyTool.log(new Exception(), MyApplication.kr.renderKhmer("យល់ព្រម"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
				
		return res;
	}
	
	public static String getKhmerDateNokora(String date){
		String res = "default";
		
		String day = date.substring(8);
		if(day.length() > 10)
			day = date.substring(8, 10);
		
		SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			Date dateObj = curFormater.parse(date);
			//res = String.format(MyApplication.strPublishedDateFormatNokora, day, MyApplication.khmer_nokora_month_data[dateObj.getMonth()], date.substring(0, 4));  
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
				 
		return res;
	}
	
	public static String getLastUpdateTime(long mili){
		String res = "default";
		
		SimpleDateFormat curFormater = new SimpleDateFormat("dd-MM-yyyy HH:mm"); 
		
		Date resultdate = new Date(mili);
		
		String date = curFormater.format(resultdate).toString();
		//res = String.format(MyApplication.strLastUpdateArticleFormat, date.substring(0, 2), MyApplication.khmer_month_data[resultdate.getMonth()], date.substring(6, 10), date.substring(11, 13), date.substring(14, 16));  

		return res;
	}
	
	public static JSONArray imageResize(JSONArray photos){
		int size = photos.length();
		for (int i = 0; i < size; i++) {
			/*try {
				//photos.put(i, photos.getString(i)+MyApplication.image_detail_setting);
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		return photos;
	}
	
	//helper method for clearCache() , recursive
	//returns number of deleted files
	static int clearCacheFolder(final File dir, final int numDays) {

	    int deletedFiles = 0;
	    if (dir!= null && dir.isDirectory()) {
	        try {
	            for (File child:dir.listFiles()) {

	                //first delete subdirectories recursively
	                if (child.isDirectory()) {
	                    deletedFiles += clearCacheFolder(child, numDays);
	                }

	                //then delete the files and subdirectories in this dir
	                //only empty directories can be deleted, so subdirs have been done first
	                if (child.lastModified() < new Date().getTime() - numDays * DateUtils.DAY_IN_MILLIS) {
	                    if (child.delete()) {
	                        deletedFiles++;
	                    }
	                }
	            }
	        }
	        catch(Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return deletedFiles;
	}

	/*
	 * Delete the files older than numDays days from the application cache
	 * 0 means all files.
	 */
	public static void clearCache(final Context context, final int numDays) {
	    int numDeletedFiles = clearCacheFolder(context.getCacheDir(), numDays);
	}
	
	public static void getDegreeStatus(Context context, double degree, ImageView degree_image, TextView degree_status) {
		Log.d("degree = ", degree+"");
		Log.d("Math.round(degree) = ", Math.round(degree)+"" );
		if(Math.round(degree) == 0){
			degree_image.setImageResource(R.drawable.blue);
			degree_status.setText(context.getString(R.string.spot_degree_0));
		}
		else if(Math.round(degree) == 1){
			degree_image.setImageResource(R.drawable.yellow);
			degree_status.setText(context.getString(R.string.spot_degree_1));
		}
		else if(Math.round(degree) == 2){
			degree_image.setImageResource(R.drawable.orange);
			degree_status.setText(context.getString(R.string.spot_degree_2));
		}
		else if(Math.round(degree) == 3){
			degree_image.setImageResource(R.drawable.red);
			degree_status.setText(context.getString(R.string.spot_degree_3));
		}
		else 	
			degree_status.setText(context.getString(R.string.spot_degree_unknown));
	}
	
	public static ArrayList<Spot> filter_spot(ArrayList<Spot> list_sport, String filter_option){
		ArrayList<Spot> filtered_spot = new ArrayList<Spot>();
		
		for (int i = 0; i < list_sport.size(); i++) {
			if (filter_option.equals("no traffic jam")) {
				if (Math.round(list_sport.get(i).getDegree())==0) {
					filtered_spot.add(list_sport.get(i));
				}
			}
			else if (filter_option.equals("traffic jam")) {
				if ((Math.round(list_sport.get(i).getDegree())==1) || (Math.round(list_sport.get(i).getDegree())==2) || (Math.round(list_sport.get(i).getDegree())==3)) {
					filtered_spot.add(list_sport.get(i));
				}
			}
/*			else if (filter_option.equals("medium")) {
				if (Math.round(list_sport.get(i).getDegree())==2) {
					filtered_spot.add(list_sport.get(i));
				}
			}
			else if (filter_option.equals("serious")) {
				if (Math.round(list_sport.get(i).getDegree())==3) {
					filtered_spot.add(list_sport.get(i));
				}
			}
*/		}
		
		return filtered_spot;
	}	
}