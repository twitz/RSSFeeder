package ml223vz.dv606.rssfeeder.provider;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * TODO: Add comments
 *
 * This contains all the constants and important methods for accessing the tables
 */
public class FeedTables {
    private static final String TAG = "Table handler";

    private static final String TYPE_PK = "integer primary key autoincrement";
    private static final String TYPE_TEXT = "text not null";
    private static final String TYPE_TEXT_UNIQUE = "text unique";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    /**
     * This method will be called when updating the application with a new database version
     * defined in the SQLHelper class. This will drop all the current tables.
     *
     * TODO: If database is updated, handle copying of relevant data.
     *
     * @param database database connection
     * @param oldVersion current database version
     * @param newVersion new database version
     */
    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        Log.w(TAG, "Database will upgrade from " + oldVersion + " to " + newVersion);
        database.execSQL(DROP_TABLE + ChannelTable.TABLE_NAME);
        database.execSQL(DROP_TABLE + PostTable.TABLE_NAME);
        onCreate(database);
    }

    public static void onCreate(SQLiteDatabase database) {
        ChannelTable.createTable(database);
        PostTable.createTable(database);
    }

    /**
     * Class that handles specifics for the channels table
     */
    public static class ChannelTable {
        public static final String TABLE_NAME = "channel";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_URI = "uri";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";

        private static final String CREATE_POSTS_TABLE = "create table"
                + TABLE_NAME + "("
                + COLUMN_ID + " " + TYPE_PK + ", "
                + COLUMN_URI + " " + TYPE_TEXT + ", "
                + COLUMN_TITLE + " " + TYPE_TEXT + ", "
                + COLUMN_DESCRIPTION + " " + TYPE_TEXT + ");";

        public static void createTable(SQLiteDatabase database){
            database.execSQL(CREATE_POSTS_TABLE);
        }
    }

    /**
     * Class that handles specifics for the posts table
     */
    public static class PostTable {
        public static final String TABLE_NAME = "post";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_URI = "uri";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CHANNEL = "channel";
        public static final String COLUMN_DESCRIPTION = "description";

        private static final String CREATE_POSTS_TABLE = "create table"
                + TABLE_NAME + "("
                + COLUMN_ID + " " + TYPE_PK + ", "
                + COLUMN_URI + " " + TYPE_TEXT_UNIQUE + ", "
                + COLUMN_TITLE + " " + TYPE_TEXT + ", "
                + COLUMN_DESCRIPTION + " " + TYPE_TEXT + ", "
                + COLUMN_CHANNEL + " " + TYPE_TEXT + ");";

        public static void createTable(SQLiteDatabase database){
            database.execSQL(CREATE_POSTS_TABLE);
        }
    }
}
