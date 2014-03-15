package codemonkeylabs.androidzipcodelib.sample;

import android.app.Application;
import codemonkeylabs.androidzipcodelib.library.ZipcodeLib;


/**
 * Created by brianplummer on 2/17/14.
 */
public class ZipcodeSampleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ZipcodeLib.init(getApplicationContext());
    }
}
