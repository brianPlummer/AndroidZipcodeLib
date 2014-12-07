package codemonkeylabs.androidzipcodelib.library;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by brianplummer on 2/17/14.
 */
public class ZipcodeDataSource {

    public static ZipResult getValue(String zip, SQLiteDatabase database) {

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
