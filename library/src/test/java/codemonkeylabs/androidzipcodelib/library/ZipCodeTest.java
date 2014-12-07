package codemonkeylabs.androidzipcodelib.library;

import android.database.sqlite.SQLiteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.io.File;

import static junit.framework.Assert.assertTrue;

/**
 * Created by brianplummer on 10/6/14.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class ZipCodeTest
{

    private SQLiteDatabase zipCodeDatabase;
    private File dbFile;

    @Before
    public void setUp() throws Exception
    {
        ShadowLog.stream = System.out;

        File dbDir = new File("src/test/resources");
        ZipcodeUtility.copyDatabase(Robolectric.application, dbDir);

        this.dbFile = new File(dbDir, ZipcodeLib.db);
        assertTrue(dbFile.exists());

        this.zipCodeDatabase = SQLiteDatabase.openDatabase(this.dbFile.getPath(),
                null,
                SQLiteDatabase.OPEN_READONLY);

        assertTrue(zipCodeDatabase.isOpen());
    }

    @After
    public void tearDown()
    {
        if(this.zipCodeDatabase != null && this.zipCodeDatabase.isOpen())
        {
            this.zipCodeDatabase.close();
        }

        if(this.dbFile.exists())
        {
            this.dbFile.delete();
        }
    }

    @Test
    public void basicTest()
    {
        ZipResult zipResult = ZipcodeDataSource.getValue("30120", this.zipCodeDatabase);
        assertTrue(zipResult.state.equalsIgnoreCase("GA"));
    }

}
