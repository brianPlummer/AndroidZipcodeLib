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

    protected static boolean copyDatabase(Context context, File dbdir)
    {
        GZIPInputStream gis = null;
        FileOutputStream fos = null;
        boolean success = false;
        try {
            gis = new GZIPInputStream(context.getResources().openRawResource(R.raw.zipcodesdb));

            if(!dbdir.exists()){
                Log.e(TAG, "DB dir is false");
                boolean create = dbdir.mkdir();
                if(!create)
                    return false;
            }

            File outputFile = new File(dbdir, ZipcodeLib.db);
            Log.e(TAG, "DB dir is true");

            fos = new FileOutputStream(outputFile);
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
        File dbdir = getAndroidDBDir(context);
        File dbFile = new File(dbdir, ZipcodeLib.db);
        if (dbFile.exists())
            success = dbFile.delete();
        return success;
    }

    protected static File getAndroidDBDir(Context context){
        return new File(Environment.getDataDirectory(), "//data//" + context.getPackageName() + "//databases//");
    }

}
