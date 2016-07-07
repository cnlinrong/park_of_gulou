package com.funo.park.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {

	private Context context;
	private String db_path = FunoConst.DATA_BASE_PATH;
	private String db_name = FunoConst.DATA_BASE_NAME;
	private SQLiteDatabase bicycleDataBase;

	public SQLiteDataBaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);

		this.context = context;
	}

	@Override
	public synchronized void close() {
		if (bicycleDataBase != null)
			bicycleDataBase.close();

		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS pub_bicycle"
				+ "( id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, addr VARCHAR, lat VARCHAR,lon VARCHAR,collectStatus VARCHAR,collectTime VARCHAR,num VARCHAR)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

	}

	private void copyDataBase(String db_path, String db_name) throws IOException {

		InputStream myInput = context.getAssets().open(db_name);
		String outFileName = db_path + db_name;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public SQLiteDatabase openDataBase(String path) throws SQLException {
		bicycleDataBase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
		return bicycleDataBase;
	}

	public boolean checkDataBase(String path) {
		SQLiteDatabase checkDB = null;
		try {
			checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	public void createDataBase(boolean isCover, String path) throws Exception {
		if (isCover) {
			this.getReadableDatabase();
			try {
				this.copyDataBase(db_path, db_name);
			} catch (IOException e) {
				throw new Error("Error copying" + db_name + " database");
			}
		} else {
			boolean dbExist = checkDataBase(path);
			if (dbExist) {

			} else {
				this.getReadableDatabase();
				try {
					this.copyDataBase(db_path, db_name);
				} catch (IOException e) {
					throw new Error("Error copying" + db_name + " database");
				}
			}
		}
	}

	public void createDataBase() {
		DBManager dbManager = new DBManager(context);
	}
	/*
	 * public void createSpecialDataBase(String db_path, String db_name) throws
	 * Exception {
	 * 
	 * boolean dbExist = checkDataBase(db_path, db_name); if (dbExist) {
	 * openDataBase(db_path, db_name); Cursor cur =
	 * bicycleDataBase.query(BaseConstant.TABLE_NAME, new String[] { "city" },
	 * null, null, null, null, "mark asc"); if(cur.getCount() == 161){
	 * 
	 * }else{ this.copyDataBase(db_path, db_name); } cur.close(); } else {
	 * this.getReadableDatabase(); try { this.copyDataBase(db_path, db_name); }
	 * catch (IOException e) { throw new Error("Error copying" + db_name +
	 * " database"); } } }
	 */
}
