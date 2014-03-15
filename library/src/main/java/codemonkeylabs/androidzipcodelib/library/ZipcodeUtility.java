package codemonkeylabs.androidzipcodelib.library;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * Created by brianplummer on 2/17/14.
 */
public class ZipcodeUtility {

    protected static final String TAG = "ZipcodeUtility";

    protected static boolean copyDatabase(Context context)
    {
        GZIPInputStream gis = null;
        FileOutputStream fos = null;
        boolean success = false;
        try {
            gis = new GZIPInputStream(context.getResources().openRawResource(R.raw.zipcodesdb));
            File dbdir = new File(Environment.getDataDirectory(),"//data//" +context.getPackageName()+"//databases//");

            if(!dbdir.exists()){
                boolean create = dbdir.mkdir();
                if(!create)
                    return false;
            }

            fos = new FileOutputStream(new File(dbdir, ZipcodeLib.db));
            byte[] buffer = new byte[4096];
            int count = 0;
            while( (count = gis.read(buffer)) > 0)
            {
                fos.write(buffer,0,count);
            }
            fos.flush();
            success = true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }finally {
            if(gis != null)
                try {
                    gis.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            if(fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
        }
        return success;
    }


    protected static boolean destroyDatabase(Context context)
    {
        boolean success = false;
        File dbdir = new File(Environment.getDataDirectory(), "//data//" + context.getPackageName() + "//databases//");
        File dbFile = new File(dbdir, ZipcodeLib.db);
        if (dbFile.exists())
            success = dbFile.delete();
        return success;
    }

}
