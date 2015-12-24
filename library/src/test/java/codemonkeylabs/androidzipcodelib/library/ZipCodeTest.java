package codemonkeylabs.androidzipcodelib.library;

import android.database.sqlite.SQLiteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import java.io.File;

import static junit.framework.Assert.assertTrue;

/**
 * Created by brianplummer on 10/6/14.
 */
@RunWith(AndroidJUnit4.class)
public class ZipCodeTest
{

    private SQLiteDatabase zipCodeDatabase;
    private File dbFile;

    @Before
    public void setUp() throws Exception
    {

        File dbDir = ZipcodeUtility.getAndroidDBDir(InstrumentationRegistry.getInstrumentation().getTargetContext());
        ZipcodeUtility.copyDatabase(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                dbDir);

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
