package com.funo.park.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.funo.park.mode.BicycleStation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

	private SQLiteDataBaseHelper helper;
	private SQLiteDatabase db;
	private SQLiteDatabase readDb;
	private String dbPath;
	
	public SQLiteDataBaseHelper getHelper() {
		return helper;
	}

	public void setHelper(SQLiteDataBaseHelper helper) {
		this.helper = helper;
	}

	public String getDbPath() {
		return dbPath;
	}

	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}

	public SQLiteDatabase getReadDb() {
		return readDb;
	}

	public void setReadDb(SQLiteDatabase readDb) {
		this.readDb = readDb;
	}
	
	public DBManager(Context context) {
		helper = new SQLiteDataBaseHelper(context, FunoConst.DATA_BASE_NAME, null, FunoConst.dbVersion);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
		readDb = helper.getReadableDatabase();
		dbPath = db.getPath();
		System.out.println("db.getPath()-->" + db.getPath());
	}

	/**
	 * add persons
	 * 
	 * @param persons
	 */
	public void add(List<BicycleStation> bsList) {
		db.beginTransaction(); // 开始事务
		try {
			for (BicycleStation bs : bsList) {
				db.execSQL("INSERT INTO pub_bicycle VALUES(null,?,?,?,?,?,?,?)",
						new Object[] { bs.getStationName(), bs.getStationAddr(), bs.getLat(), bs.getLon(),
								bs.getCollectStatus(), bs.getCollectTime(), bs.getStationNum() });
				System.out.println("add");
				System.out.println("bs.getStationNum()-->" + bs.getStationNum());
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction();// 结束事务
		}
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}

	/**
	 * update person's age
	 * 
	 * @param person
	 */
	public void update(BicycleStation bs) {
		ContentValues cv = new ContentValues();
		cv.put("collectStatus", bs.getCollectStatus());
		cv.put("collectTime", formatCurrentTime());
		db.update("pub_bicycle", cv, "name=?", new String[] { bs.getStationName() });
	}

	/**
	 * delete old person
	 * 
	 * @param person
	 */
	public void deleteBicycleStation(BicycleStation bs) {
		db.delete("pub_bicycle", "name=?", new String[] { bs.getStationName() });
	}

	/**
	 * query all persons, return list
	 * 
	 * @return List<Person>
	 */
	public List<BicycleStation> query(String str, int flag) {
		ArrayList<BicycleStation> bicycleStations = new ArrayList<BicycleStation>();
		Cursor c = null;
		System.out.println("flag->" + flag);
		if (flag == 0) {
			c = queryTheCursor(str);// 查名称
		} else if (flag == 1) {
			System.out.println("str-->" + str);
			c = queryTheCursorByStatus(str);// 查状态
		}

		BicycleStation bicycleStation = null;
		while (c.moveToNext()) {
			bicycleStation = new BicycleStation();
			bicycleStation.setStationName(c.getString(c.getColumnIndex("name")));
			System.out.println("c.getString(c.getColumnIndex('name'))-->" + c.getString(c.getColumnIndex("name")));
			bicycleStation.setStationAddr(c.getString(c.getColumnIndex("addr")));
			bicycleStation.setLat(c.getString(c.getColumnIndex("lat")));
			bicycleStation.setLon(c.getString(c.getColumnIndex("lon")));
			bicycleStation.setStationNum(c.getString(c.getColumnIndex("num")));
			bicycleStation.setCollectStatus(c.getString(c.getColumnIndex("collectStatus")));
			bicycleStation.setCollectTime(c.getString(c.getColumnIndex("collectTime")));
			bicycleStations.add(bicycleStation);
		}
		if (c != null) {
			c.close();
			c = null;
		}

		return bicycleStations;
	}

	/**
	 * query all persons, return cursor
	 * 
	 * @return Cursor
	 */
	public Cursor queryTheCursor(String name) {
		System.out.println("name-->" + name);
		Cursor c = readDb.rawQuery("SELECT * FROM pub_bicycle where name like '%" + name + "%'", null);
		return c;
	}

	public Cursor queryTheCursorByStatus(String status) {
		System.out.println(" status-->" + status);
		Cursor c = readDb.rawQuery("SELECT * FROM pub_bicycle where collectStatus='" + status + "'", null);
		return c;
	}

	/**
	 * close database
	 */
	public void closeDB(SQLiteDatabase db) {
		if (db != null) {
			db.close();
			db = null;
		}
	}

	public String formatCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss");
		return df.format(new Date());
	}
	
}
