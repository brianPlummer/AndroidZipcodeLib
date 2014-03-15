package codemonkeylabs.androidzipcodelib.library;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by brianplummer on 2/17/14.
 */
public class ZipcodeDataSource {

    private SQLiteDatabase database;
    private ZipcodeDatabase dbHelper;

    private String[] zipColumns = {
            ZipcodeDatabase.COLUMN_ZIP,
            ZipcodeDatabase.COLUMN_STATE, ZipcodeDatabase.COLUMN_CITY};

    private String[] cityColumns = {ZipcodeDatabase.PARENT_ID, ZipcodeDatabase.CITY};

    protected ZipcodeDataSource(Context context) {
        this.dbHelper = new ZipcodeDatabase(context);
    }

    protected void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    protected void close() {
        dbHelper.close();
    }

    protected ZipResult getValue(String zip) {

        Cursor cursor = database.rawQuery("select * from zipcodes where zipcode = \'" + zip + "\' LIMIT 1", null);
        boolean result = cursor.moveToFirst();

        if (!result) {
            cursor.close();
            return null;
        }

        String zipDB = cursor.getString(0);
        String state = cursor.getString(1);
        String primaryCity = cursor.getString(2);

        Cursor cities = database.rawQuery("select * from cities where parent_id = \'" + zipDB + "\'", null);

        ArrayList<String> holder = new ArrayList<String>();
        boolean resultCities = cities.moveToFirst();

        if (resultCities) {
            String cit = cities.getString(1);
            holder.add(cit);
            while (cities.moveToNext()) {
                cit = cities.getString(1);
                holder.add(cit);
            }
        }

        ZipResult ret = new ZipResult();
        ret.state = state;
        ret.zip = zip;
        ret.cities.add(primaryCity);
        for (int r = 0; r < holder.size(); r++) {
            ret.cities.add(holder.get(r));
        }
        cursor.close();
        cities.close();

        return ret;
    }

}
