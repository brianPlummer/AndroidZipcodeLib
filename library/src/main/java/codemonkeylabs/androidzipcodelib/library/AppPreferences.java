package codemonkeylabs.androidzipcodelib.library;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

/**
 * Created by brianplummer on 2/17/14.
 */
public class AppPreferences {

    private static AppPreferences INSTANCE;
    private final SharedPreferences mSp;

    protected static synchronized AppPreferences getInstance(Context context) {
        if(INSTANCE == null)
        {
            INSTANCE = new AppPreferences(context);
        }
        return INSTANCE;
    }

    private AppPreferences(Context context)
    {
        mSp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @SuppressLint("CommitPrefEdits")
    private void applyPreference(String key, float value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putFloat(key, value);
        editorApply(editor);
    }

    @SuppressLint("CommitPrefEdits")
    private void applyPreference(String key, String value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(key, value);
        editorApply(editor);
    }

    @SuppressLint("CommitPrefEdits")
    private void applyPreference(String key, boolean value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putBoolean(key, value);
        editorApply(editor);
    }

    @SuppressLint("CommitPrefEdits")
    private void applyPreference(String key, int value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putInt(key, value);
        editorApply(editor);
    }

    @SuppressLint("CommitPrefEdits")
    private void applyPreference(String key, long value) {
        SharedPreferences.Editor editor = mSp.edit();
        editor.putLong(key, value);
        editorApply(editor);
    }

    protected static final String IS_LOADED = "is_loaded";

    protected boolean isLoaded(){
        return mSp.getBoolean(IS_LOADED, false);
    }
    protected void setIsLoaded(){
        applyPreference(IS_LOADED,true);
    }

    protected void setIsLoadedFalse(){
        applyPreference(IS_LOADED,false);
    }

    private static final boolean USE_APPLY = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;

    /**
     * {@link android.content.SharedPreferences.Editor#apply()} is quicker than
     * {@link android.content.SharedPreferences.Editor#commit()} which wouldn't matter except that writing to
     * preferences is IO which we want to minimize as much as possible
     * @param editor
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static void editorApply(SharedPreferences.Editor editor)
    {
        if(USE_APPLY) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    protected SharedPreferences getPrefs() {
        return mSp;
    }
}
