package ml223vz.dv606.rssfeeder.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This is our database helper. Currently this just calls the correct functions in FeedTables.
 *
 * TODO: Write comments
 */
public class FeedDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "rssfeedr.db";
    private static final int DATABASE_VERSION = 1;

    public FeedDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        FeedTables.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        FeedTables.onUpgrade(database, oldVersion, newVersion);
    }
}
