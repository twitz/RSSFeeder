package ml223vz.dv606.rssfeeder.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.sql.SQLException;

/**
 * TODO: Check all variable names, so that they match Android documentation rules
 * TODO: Comment this bitch
 */
public class FeedContentProvider extends ContentProvider {
    private static final String TAG = "Content provider";

    private static final String AUTHORITY = "ml223vz.dv606.rssfeeder.provider";
    private static final String PATH_FEEDS = "feeds";
    private static final String PATH_CHANNELS = "channels";

    private static final String TABLE_POSTS = FeedTables.PostTable.TABLE_NAME;
    private static final String TABLE_CHANNELS = FeedTables.ChannelTable.TABLE_NAME;

    private static final int URI_FEED      =   20;
    private static final int URI_POST      =   30;
    private static final int URI_CHANNEL   =   40;
    private static final int URI_CHANNELS  =   50;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        URI_MATCHER.addURI(AUTHORITY, PATH_FEEDS, URI_FEED);
        URI_MATCHER.addURI(AUTHORITY, PATH_FEEDS + "/#", URI_POST);
        URI_MATCHER.addURI(AUTHORITY, PATH_CHANNELS, URI_CHANNELS);
        URI_MATCHER.addURI(AUTHORITY, PATH_CHANNELS + "/#", URI_CHANNEL);
    }

    private FeedDatabaseHelper mDatabaseHelper;

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new FeedDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int uriType = URI_MATCHER.match(uri);

        switch (uriType){
            case URI_FEED:
                queryBuilder.setTables(TABLE_POSTS);
                break;
            case URI_POST:
                queryBuilder.setTables(TABLE_POSTS);
                queryBuilder.appendWhere(FeedTables.PostTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case URI_CHANNELS:
                queryBuilder.setTables(TABLE_CHANNELS);
                break;
            case URI_CHANNEL:
                queryBuilder.setTables(TABLE_CHANNELS);
                queryBuilder.appendWhere(FeedTables.ChannelTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                // Debugging
                throw new IllegalArgumentException("Unknown URI for query: " + uri);
        }

        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs,
                null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = -1;
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();

        switch (uriType){
            case URI_POST:
                id = database.insert(TABLE_POSTS, null, values);
                break;
            case URI_CHANNEL:
                id = database.insert(TABLE_CHANNELS, null, values);
                break;
            default:
                // Debugging
                throw new IllegalArgumentException("Unknown URI for insert: " + uri);
        }
        if(id != -1){
            getContext().getContentResolver().notifyChange(uri, null);
            return ContentUris.withAppendedId(uri, id);
        } else {
            Log.e(TAG, "Could not insert row into " + uri);
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        StringBuilder selectionBuilder = new StringBuilder();
        String table = null;

        switch (uriType){
            case URI_POST:
                table = TABLE_POSTS;
                selectionBuilder.append(FeedTables.PostTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case URI_FEED:
                table = TABLE_POSTS;
                break;
            case URI_CHANNEL:
                table = TABLE_CHANNELS;
                selectionBuilder.append(FeedTables.ChannelTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case URI_CHANNELS:
                table = TABLE_CHANNELS;
                break;
        }

        if(!TextUtils.isEmpty(selection)){
            if(selectionBuilder.length() > 0) {
                selectionBuilder.append(" and ");
            }
            selectionBuilder.append(selection);
        }

        int rowsDeleted = database.delete(table, selectionBuilder.toString(), selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase database = mDatabaseHelper.getWritableDatabase();
        StringBuilder selectionBuilder = new StringBuilder();
        String table = null;

        switch (uriType){
            case URI_POST:
                table = TABLE_POSTS;
                selectionBuilder.append(FeedTables.PostTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case URI_FEED:
                table = TABLE_POSTS;
                break;
            case URI_CHANNEL:
                table = TABLE_CHANNELS;
                selectionBuilder.append(FeedTables.ChannelTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case URI_CHANNELS:
                table = TABLE_CHANNELS;
                break;
        }

        if(!TextUtils.isEmpty(selection)){
            if(selectionBuilder.length() > 0) {
                selectionBuilder.append(" and ");
            }
            selectionBuilder.append(selection);
        }

        int rowsUpdated = database.update(table, values, selectionBuilder.toString(), selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
