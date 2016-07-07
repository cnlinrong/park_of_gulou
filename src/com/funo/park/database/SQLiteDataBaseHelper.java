package com.funo.park.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.funo.park.util.BaseConstant;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {

	private Context context;
	private String db_path = BaseConstant.DATA_BASE_PATH;
	private String db_name = BaseConstant.DATA_BASE_NAME;
	private SQLiteDatabase myDataBase;

	public SQLiteDataBaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);

		this.context = context;
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase database) {

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

	public SQLiteDatabase openDataBase(String db_path, String db_name) throws SQLException {
		String myPath = db_path + db_name;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		return myDataBase;
	}

	private boolean checkDataBase(String db_path, String db_name) {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = db_path + db_name;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {

			// database does't exist yet.

		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	public void createDataBase(boolean isCover, String db_path, String db_name) throws Exception {
		if (isCover) {
			this.getReadableDatabase();
			try {
				this.copyDataBase(db_path, db_name);
			} catch (IOException e) {
				throw new Error("Error copying" + db_name + " database");
			}
		} else {
			boolean dbExist = checkDataBase(db_path, db_name);
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

}
