package itc.gic.trafficreportingsystem.database;


import itc.gic.model.Itinerary;

import java.util.ArrayList;
import java.util.Collections;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;


public class DBFunction{
	
	public static void addSpotToItinerary(ConnectDB events, int idItinerary, int idSpot){
		
    	SQLiteDatabase db = events.getWritableDatabase();
    	
    	db.execSQL("delete from contain_spot where idItinerary="+idItinerary+" and idSpot="+idSpot+";");
    	
    	String query = "replace into contain_spot values ( '"+idItinerary+"', '"+idSpot+"');";
    	
    	db.execSQL(query);
    	
    	db.close();

	}
	
	public static void deleteSpotFromItinerary(ConnectDB events, int idItinerary, int idSpot){
		
    	SQLiteDatabase db = events.getWritableDatabase();
    	
    	db.execSQL("delete from contain_spot where idItinerary="+idItinerary+" and idSpot="+idSpot+";");
    	
    	
    	db.close();

	}
	
	public static String getIdSpots(ConnectDB dbHandler, int id) {
        
        // Select All Query
        String	selectQuery = "SELECT idSpot from contain_spot where idItinerary='"+id+"';";
        
               
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = null;
        
        String ids = "";
        try{
        	
        
        	cursor = db.rawQuery(selectQuery, null);
 
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            	 
	           do{
	        	   
	        	   ids+=cursor.getInt(0)+"-";
	        	   
	            } while (cursor.moveToNext());
	            
	           if(ids.charAt(ids.length()-1)=='-'){
	        	   ids = ids.substring(0, ids.length()-1);
	           }
	        }
	        
	        
	    
	        
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	
        	if(cursor!=null){
        		cursor.close();
        	}
        	db.close();
            
        }
        
        
        // return contact list
        return ids;
    }
	
	public static ArrayList<Itinerary> getItinerary(ConnectDB dbHandler) {
        ArrayList<Itinerary> itineraryList = new ArrayList<Itinerary>();
       
        // Select All Query
        String	selectQuery = "SELECT id, name, description from itinerary;";
        
               
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = null;
        
        try{
        	
        
        	cursor = db.rawQuery(selectQuery, null);
 
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst()) {
	            	 
	           do{
	            	// Add Place to listPlace
	                itineraryList.add(new Itinerary(
	                		Integer.parseInt(cursor.getString(0)), 
	                		cursor.getString(1), 
	                		cursor.getString(2) 
	                		));
	      
	            } while (cursor.moveToNext());
	            
	        }
	    
	        
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	
        	if(cursor!=null){
        		cursor.close();
        	}
        	db.close();
            
        }
        
        
        // return contact list
        return itineraryList;
    }
	
	public static void addItinerary( String name, String description, ConnectDB events) {
    	
    	SQLiteDatabase db = events.getWritableDatabase();
    	
    	String query = "insert into itinerary values (NULL, '"+name+"', '"+description+"');";
    	
    	db.execSQL(query);
    	
    	db.close();
    	
	}
/*	public static void addUser(int id, String name, String username, String password, ConnectDB events) {
    	
    	SQLiteDatabase db = events.getWritableDatabase();
    	
    	String query = "replace into user values ("+id+", '"+name+"', '"+username+"', '"+password+"');";
    	
    	db.execSQL(query);
    	
    	db.close();
    	
	}
		
	 public static boolean verifyLoginUser() {
			
			String from[] = {"id","name"};
	    	SQLiteDatabase db = MyApplication.dbHnalder.getReadableDatabase();
	    	Cursor cursor = db.query("user", from, null, null, null,null, null);
	    	//startManagingCursor(cursor);
			
			
		   	 while (cursor.moveToNext()) {
		   		 	 // Assign id and name to local variable
			    	 MyApplication.userId = cursor.getInt(0);
			    	 MyApplication.userName = cursor.getString(1);
			    	 MyApplication.loggedIn = true;
			    	 
			    	 
			    	 Log.e("===> COmmentor: ",MyApplication.userId+"   "+MyApplication.userName);
			    	 
			    	 // close
			    	 cursor.close();
			    	 db.close();
			    	 return true;
			 }
		   	 
		   	 // close
		   	 cursor.close();
		   	 db.close();

		   	 return false;
		}
	
    public static void logoutUser(SQLiteDatabase db, int id){
		
		
    	String query = "delete from user where id="+id;
    	
    	db.execSQL(query);
    	
	}
    
*/
    public static String escapeQuote(String str){
    	
    	str = str.replaceAll("'" , "''");
    	return str;
    }
	
	
}
