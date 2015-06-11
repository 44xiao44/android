package com.appacts.plugin.DB;

import java.util.concurrent.atomic.AtomicInteger;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author lixiaoqing
 */
public class DBManager {

	private static AtomicInteger writableCounter = new AtomicInteger();
	private static AtomicInteger readableCounter = new AtomicInteger();
	private static DBManager instance;
	private static SQLiteOpenHelper myHelper;
	private SQLiteDatabase writableDatabase;
	private SQLiteDatabase readableDatabase;

	public static synchronized DBManager getInstance(SQLiteOpenHelper helper) {

		if (instance == null) {
			instance = new DBManager();
			myHelper = helper;
		}
		return instance;
	}

	public SQLiteDatabase getWritableDatabase() {
		return closeOrOpenWritableDatabase(true);
	}

	public void closeWritableDatabase() {
		closeOrOpenWritableDatabase(false);
	}

	public SQLiteDatabase getReadableDatabase() {
		return closeOrOpenReadableDatabase(true);
	}

	public void closeReadableDatabase() {
		closeOrOpenReadableDatabase(false);
	}

	/**
	 * close or open writable database
	 * 
	 * @param operateType
	 *            true:open, false:close
	 * @return
	 */
	private synchronized SQLiteDatabase closeOrOpenWritableDatabase(boolean operateType) {

		if (true == operateType) {

			if (writableCounter.incrementAndGet() == 1) {
				writableDatabase = myHelper.getWritableDatabase();
			}
			return writableDatabase;

		} else {

			if (writableCounter.decrementAndGet() == 0) {
				writableDatabase.close();
			}
			return null;
		}
	}

	/**
	 * close or open readable database
	 * 
	 * @param operateType
	 *            true:open, false:close
	 * @return
	 */
	private synchronized SQLiteDatabase closeOrOpenReadableDatabase(boolean operateType) {

		if (true == operateType) {

			if (readableCounter.incrementAndGet() == 1) {
				readableDatabase = myHelper.getReadableDatabase();
			}
			return readableDatabase;

		} else {

			if (readableCounter.decrementAndGet() == 0) {
				readableDatabase.close();
			}
			return null;
		}
	}
}
