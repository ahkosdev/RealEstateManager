package fr.kosdev.realestatemanager.Provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fr.kosdev.realestatemanager.Database.RealEstateDatabase;
import fr.kosdev.realestatemanager.Models.Property;

public class PropertyContentProvider extends ContentProvider {

    public static final String AUTHORITY = "fr.kosdev.realstatemanager.Provider";
    public static final String TABLE_NAME = Property.class.getSimpleName();
    public static final Uri PROPERTY_uRI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        if (getContext() != null){
            long propertyId = ContentUris.parseId(uri);
            final Cursor cursor = RealEstateDatabase.getInstance(getContext()).propertyDao().getPropertyWithCursor(propertyId);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);

            return cursor;
        }
        throw new IllegalArgumentException("Failed to query row for uri" + uri);

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.property/" + AUTHORITY + "." + TABLE_NAME;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
