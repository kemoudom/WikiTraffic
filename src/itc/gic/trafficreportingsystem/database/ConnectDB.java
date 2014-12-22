package itc.gic.trafficreportingsystem.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConnectDB extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "trafficsystem.db" ;
	private static final int DATABASE_VERSION = 1;
	// database handler
	public static ConnectDB dbHnalder;
		
	public ConnectDB(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		db.execSQL("CREATE TABLE itinerary ( id INTEGER PRIMARY KEY autoincrement, name varchar(100), description VARCHAR(300));");
		db.execSQL("CREATE TABLE contain_spot (idItinerary int, idSpot int);");
/*		db.execSQL("CREATE TABLE place ( idPlace int PRIMARY KEY, name varchar(100), address VARCHAR(100), distance double, latitude double, longitude double, url varchar(100), callingName varchar(100), tel1 varchar(100), tel2 varchar(100), tel3 varchar(100), email varchar(100), website varchar(100)  );");
		db.execSQL("CREATE TABLE object ( idObject int PRIMARY KEY, name varchar(100), degree int, status varchar(50), idPlace int, idProblem int, url varchar(100), numberComment int, agentResponsible varchar(100)  );");
		//db.execSQL("CREATE TABLE lastUpdate ( idDate INTEGER PRIMARY KEY autoincrement, date varchar(100) );");
		db.execSQL("CREATE TABLE lastUpdate ( idDate int PRIMARY KEY , date varchar(100) );");
		db.execSQL("CREATE TABLE myTask ( idObject int, objectName varchar(100), idPlace int, idProblem int, dateOccur varchar(100), timeOccur varchar(100), reason varchar(500), degree int, status varchar(50), numberComment int, objectURL varchar(100), checkInDate varchar(100)  );");
*/		

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS itinerary;");
		db.execSQL("DROP TABLE IF EXISTS contain_spot;");
		onCreate(db);
		
	}

}
