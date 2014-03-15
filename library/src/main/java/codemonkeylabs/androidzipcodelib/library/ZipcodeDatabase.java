package codemonkeylabs.androidzipcodelib.library;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by brianplummer on 2/17/14.
 */
public class ZipcodeDatabase extends SQLiteOpenHelper {
    protected static final String TABLE_ZIPCODES = "zipcodes";
    protected static final String COLUMN_ZIP = "zipcode", COLUMN_STATE = "state", COLUMN_CITY = "city";
    protected static final String TABLE_CITIES = "cities", PARENT_ID = "parent_id", CITY = "CITY";

    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String zip_create = "create table "
            + TABLE_ZIPCODES + "(" + COLUMN_ZIP
            + " text not null, " + COLUMN_STATE + " text not null,"+ COLUMN_CITY + " text not null); ";

    private static final String city_create = " create table " + TABLE_CITIES + "(" +
            PARENT_ID + " text not null, "
            + CITY + " text not null);";

    protected ZipcodeDatabase(Context context) {
        super(context, ZipcodeLib.db, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(zip_create);
        database.execSQL(city_create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ZipcodeDatabase.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZIPCODES);
        onCreate(db);
    }
}
