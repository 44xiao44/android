import java.util.concurrent.atomic.AtomicInteger;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author lixiaoqing
 */
public class DBManager {

	private AtomicInteger databaseCounter = new AtomicInteger();
	private static DBManager instance;
	private static SQLiteOpenHelper helper;
	private SQLiteDatabase writableDatabase;
	private SQLiteDatabase readableDatabase;

	public static synchronized DBManager getInstance(SQLiteOpenHelper helper) {

		
		if (instance == null) {
			DBManager.helper = helper;
			instance = new DBManager();
		}
		
		return instance;
	}

	public SQLiteDatabase getWritableDatabase() {
		return closeOrOpenDatabase(true,true);
	}

	public void closeWritableDatabase() {
		closeOrOpenDatabase(false,true);
	}

	public SQLiteDatabase getReadableDatabase() {
		return closeOrOpenDatabase(true, false);
	}

	public void closeReadableDatabase() {
		closeOrOpenDatabase(false, false);
	}

	/**
	 * close or open writable database
	 * @param isOpen
	 *            true:open, false:close
	 * @param isWrite
	 *            true:write, false:read
	 * @return
	 */
	private synchronized SQLiteDatabase closeOrOpenDatabase(boolean isOpen, boolean isWrite) {

		if (true == isOpen) {

			databaseCounter.incrementAndGet();
			
			if(true == isWrite){
				
				if(null == writableDatabase){
					writableDatabase = helper.getWritableDatabase();
				}
				return writableDatabase;
				
			} else {
			
				if(null == readableDatabase){
					readableDatabase = helper.getReadableDatabase();
				}
				return readableDatabase;
			}

		} else {

			if (databaseCounter.decrementAndGet() == 0) {
				
				if(null != writableDatabase){
					writableDatabase.close();
				} else if(null != readableDatabase){
					readableDatabase.close();
				}
				
				writableDatabase = null;
				readableDatabase = null;
			}
			
			return null;
		}
	}

}
