package inspirational.designs.fighttheunknown;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public class DBHelperObject {
		public int id;
		public int type;
		public int x;
		public int y;
		public int dx;
		public int dy;
	};

	public DBHelper(Context context) {
		super(context, "ftu.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table objstore "
				+ "(id integer primary key, type integer, x integer,y integer, dx integer, dy integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS objstore");
		onCreate(db);
	}

	public long insertObject(int type, int x, int y, int dx, int dy) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();

		contentValues.put("type", type);
		contentValues.put("x", x);
		contentValues.put("y", y);
		contentValues.put("dx", dx);
		contentValues.put("dy", dy);

		long id = db.insert("objstore", null, contentValues);
		return id;
	}

	public Cursor getData(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from objstore where id=" + id + "",
				null);
		return res;
	}

	public int numberOfRows() {
		SQLiteDatabase db = this.getReadableDatabase();
		int numRows = (int) DatabaseUtils.queryNumEntries(db,
				"objstore");
		return numRows;
	}

	public boolean updateObject(Integer id, int type, int x,
			int y, int dx, int dy) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("type", type);
		contentValues.put("x", x);
		contentValues.put("y", y);
		contentValues.put("dx", dx);
		contentValues.put("dy", dy);
		db.update("objstore", contentValues, "id = ? ",
				new String[] { Integer.toString(id) });
		return true;
	}

	public Integer deleteObject(Integer id) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete("objstore", "id = ? ",
				new String[] { Integer.toString(id) });
	}

	public ArrayList<DBHelperObject> getAllObjects() {
		ArrayList<DBHelperObject> array_list = new ArrayList<DBHelperObject>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from objstore", null);
		res.moveToFirst();
		
		while (res.isAfterLast() == false) {
			
			DBHelperObject object = new DBHelperObject();
			
			object.id = res.getInt(res.getColumnIndex("id"));
			object.type = res.getInt(res.getColumnIndex("type"));
			object.x = res.getInt(res.getColumnIndex("x"));
			object.y = res.getInt(res.getColumnIndex("y"));
			object.dx = res.getInt(res.getColumnIndex("dx"));
			object.dy = res.getInt(res.getColumnIndex("dy"));
			
			array_list.add(object);
			
			res.moveToNext();
		}
		return array_list;
	}

}
