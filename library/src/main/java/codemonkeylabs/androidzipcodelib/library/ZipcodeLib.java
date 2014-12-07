package codemonkeylabs.androidzipcodelib.library;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import java.io.File;

/**
 * Created by brianplummer on 2/17/14.
 */
public class ZipcodeLib extends Application
{
    protected static final String TAG = "ZipcodeLib";
    protected static final String db = "zipcodes.db";

    protected enum STATES {READY,COPYING,ERROR,NOTINIT};
    protected static STATES state = STATES.NOTINIT;

    public synchronized static STATES  getState(){
        return state;
    }

    protected synchronized static void setState(STATES newstate){
        state = newstate;
    }

    public static boolean isReady(){
        return getState() == STATES.READY ? true : false;
    }

    public static void init(final Context context){

        new AsyncTask<Object,Void,Object>(){
            @Override
            protected Object doInBackground(Object... objects)
            {
                initSync(context);
                return null;
            }
        }.execute();
    }

    public static ZipResult getCitiesAndState(Context context, String zip)
    {
        if(Looper.getMainLooper().getThread() == Thread.currentThread()){
            throw new RuntimeException("Don't call ZipcodeLib.getCitiesAndState() on UI thread");
        }

        long start = System.currentTimeMillis();
        if(getState() == STATES.NOTINIT)
            initSync(context);

        if(!isReady())
            return null;

        ZipcodeDatabase zipcodeDatabase = new ZipcodeDatabase(context);
        SQLiteDatabase sqLiteDatabase = zipcodeDatabase.getReadableDatabase();

        ZipResult retVal = null;
        retVal = ZipcodeDataSource.getValue(zip, sqLiteDatabase);
        if(sqLiteDatabase != null)
            sqLiteDatabase.close();

        long end = System.currentTimeMillis();
        Log.e(TAG,"getCitiesAndState time: " + (end - start) +"ms");
        return  retVal;
    }

    public static void getCitiesAndStateAsync(final Context context,final String zip,final ZipcodeListener zipcodeListener)
    {
        new AsyncTask<Object,Void,ZipResult>(){
            @Override
            protected ZipResult doInBackground(Object... objects) {
                return getCitiesAndState(context,zip);
            }
            @Override
            protected void onPostExecute(ZipResult zipResult) {
                super.onPostExecute(zipResult);
                if(zipcodeListener != null)
                    zipcodeListener.getCitiesAndStateResult(zipResult);
            }
        }.execute();
    }

    public static void getCitiesAndStateAsyncThenDestroy(final Context context, final String zip, final ZipcodeListener zipcodeListener)
    {
        new AsyncTask<Object,Void,ZipResult>(){
            @Override
            protected ZipResult doInBackground(Object... objects) {
                return getCitiesAndState(context,zip);
            }
            @Override
            protected void onPostExecute(ZipResult zipResult) {
                super.onPostExecute(zipResult);
                destroy(context);
                if(zipcodeListener != null)
                    zipcodeListener.getCitiesAndStateResult(zipResult);
            }
        }.execute();
    }

    public static ZipResult getCitiesAndStateThenDestroy(Context context,String zip){
        ZipResult zipResult = getCitiesAndState(context,zip);
        destroy(context);
        return zipResult;
    }


    public static void destroy(final Context context) {
        new AsyncTask<Object, Void, Object>() {
            @Override
            protected Object doInBackground(Object... objects) {
                if (AppPreferences.getInstance(context).isLoaded() && (getState() == STATES.NOTINIT) || isReady())
                {
                    boolean success = ZipcodeUtility.destroyDatabase(context);
                    //if (success) {
                    AppPreferences.getInstance(context).setIsLoadedFalse();
                    setState(STATES.NOTINIT);
                    //}
                }
                return null;
            }
        }.execute();
    }

    private static void initSync(Context context){
        if(AppPreferences.getInstance(context).isLoaded())
        {
            setState(STATES.READY);
            return;
        }
        setState(STATES.COPYING);
        long start = System.currentTimeMillis();

        File dbdir = ZipcodeUtility.getAndroidDBDir(context);
        boolean success = ZipcodeUtility.copyDatabase(context, dbdir);

        if(success){
            AppPreferences.getInstance(context).setIsLoaded();
            setState(STATES.READY);
        }
        else
            setState(STATES.ERROR);
        long end = System.currentTimeMillis();
        Log.e(TAG,"DB: "+ success + " Copy time: " + (end - start) +"ms");
    }

}
