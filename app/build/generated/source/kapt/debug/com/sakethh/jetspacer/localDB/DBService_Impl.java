package com.sakethh.jetspacer.localDB;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DBService_Impl implements DBService {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<APOD_DB_DTO> __insertionAdapterOfAPOD_DB_DTO;

  private final EntityInsertionAdapter<MarsRoversDBDTO> __insertionAdapterOfMarsRoversDBDTO;

  private final EntityInsertionAdapter<BookMarkScreenGridNames> __insertionAdapterOfBookMarkScreenGridNames;

  private final BookMarkDataConverterForListOfCustomBookMarks __bookMarkDataConverterForListOfCustomBookMarks = new BookMarkDataConverterForListOfCustomBookMarks();

  private final EntityInsertionAdapter<NewsDB> __insertionAdapterOfNewsDB;

  private final EntityInsertionAdapter<APIKeysDB> __insertionAdapterOfAPIKeysDB;

  private final SharedSQLiteStatement __preparedStmtOfDeleteFromAPODDB;

  private final SharedSQLiteStatement __preparedStmtOfAddDataInAnExistingBookmarkTopic;

  private final BookMarkDataConverterForCustomBookMarks __bookMarkDataConverterForCustomBookMarks = new BookMarkDataConverterForCustomBookMarks();

  private final SharedSQLiteStatement __preparedStmtOfDeleteFromRoverDB;

  private final SharedSQLiteStatement __preparedStmtOfDeleteFromNewsDB;

  private final SharedSQLiteStatement __preparedStmtOfDeleteFromCustomBookmarksDB;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllDataFromMarsDB;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllDataFromAPODDB;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllDataFromNewsDB;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllDataFromCustomBookMarkDB;

  private final SharedSQLiteStatement __preparedStmtOfDeleteACollectionFromCustomBookMarksDB;

  private final SharedSQLiteStatement __preparedStmtOfRenameACollectionFromCustomBookMarksDB;

  public DBService_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAPOD_DB_DTO = new EntityInsertionAdapter<APOD_DB_DTO>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `apod_db` (`title`,`datePublished`,`description`,`imageURL`,`hdImageURL`,`mediaType`,`isBookMarked`,`category`,`addedToLocalDBOn`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, APOD_DB_DTO value) {
        if (value.getTitle() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getTitle());
        }
        if (value.getDatePublished() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDatePublished());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescription());
        }
        if (value.getImageURL() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getImageURL());
        }
        if (value.getHdImageURL() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getHdImageURL());
        }
        if (value.getMediaType() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getMediaType());
        }
        final int _tmp = value.isBookMarked() ? 1 : 0;
        stmt.bindLong(7, _tmp);
        if (value.getCategory() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getCategory());
        }
        if (value.getAddedToLocalDBOn() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getAddedToLocalDBOn());
        }
      }
    };
    this.__insertionAdapterOfMarsRoversDBDTO = new EntityInsertionAdapter<MarsRoversDBDTO>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `marsRovers_db` (`imageURL`,`capturedBy`,`sol`,`earthDate`,`roverName`,`roverStatus`,`launchingDate`,`landingDate`,`isBookMarked`,`category`,`addedToLocalDBOn`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, MarsRoversDBDTO value) {
        if (value.getImageURL() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getImageURL());
        }
        if (value.getCapturedBy() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getCapturedBy());
        }
        if (value.getSol() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSol());
        }
        if (value.getEarthDate() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getEarthDate());
        }
        if (value.getRoverName() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getRoverName());
        }
        if (value.getRoverStatus() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getRoverStatus());
        }
        if (value.getLaunchingDate() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getLaunchingDate());
        }
        if (value.getLandingDate() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getLandingDate());
        }
        final int _tmp = value.isBookMarked() ? 1 : 0;
        stmt.bindLong(9, _tmp);
        if (value.getCategory() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getCategory());
        }
        if (value.getAddedToLocalDBOn() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getAddedToLocalDBOn());
        }
      }
    };
    this.__insertionAdapterOfBookMarkScreenGridNames = new EntityInsertionAdapter<BookMarkScreenGridNames>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `bookMarkScreen_GridNames` (`name`,`imgUrlForGrid`,`data`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BookMarkScreenGridNames value) {
        if (value.getName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getName());
        }
        if (value.getImgUrlForGrid() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getImgUrlForGrid());
        }
        final String _tmp = __bookMarkDataConverterForListOfCustomBookMarks.convertToString(value.getData());
        if (_tmp == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, _tmp);
        }
      }
    };
    this.__insertionAdapterOfNewsDB = new EntityInsertionAdapter<NewsDB>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `newsDB` (`title`,`imageURL`,`sourceURL`,`sourceOfNews`,`publishedTime`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, NewsDB value) {
        if (value.getTitle() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getTitle());
        }
        if (value.getImageURL() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getImageURL());
        }
        if (value.getSourceURL() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSourceURL());
        }
        if (value.getSourceOfNews() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getSourceOfNews());
        }
        if (value.getPublishedTime() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getPublishedTime());
        }
      }
    };
    this.__insertionAdapterOfAPIKeysDB = new EntityInsertionAdapter<APIKeysDB>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `apiKeys` (`currentNASAAPIKey`,`currentNewsAPIKey`,`id`) VALUES (?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, APIKeysDB value) {
        if (value.getCurrentNASAAPIKey() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getCurrentNASAAPIKey());
        }
        if (value.getCurrentNewsAPIKey() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getCurrentNewsAPIKey());
        }
        if (value.getId() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteFromAPODDB = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE from apod_db WHERE imageURL = ?";
        return _query;
      }
    };
    this.__preparedStmtOfAddDataInAnExistingBookmarkTopic = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE bookMarkScreen_GridNames SET data = json_insert(data, '$[#]', json(?)) WHERE name = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteFromRoverDB = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE from marsRovers_db WHERE imageURL = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteFromNewsDB = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE from newsDB WHERE sourceURL = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteFromCustomBookmarksDB = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE from bookMarkScreen_GridNames WHERE name = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllDataFromMarsDB = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM marsRovers_db";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllDataFromAPODDB = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM apod_db";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllDataFromNewsDB = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM newsDB";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllDataFromCustomBookMarkDB = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM bookMarkScreen_GridNames";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteACollectionFromCustomBookMarksDB = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE from bookMarkScreen_GridNames WHERE name = ?";
        return _query;
      }
    };
    this.__preparedStmtOfRenameACollectionFromCustomBookMarksDB = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE bookMarkScreen_GridNames SET name = ? WHERE name = ?";
        return _query;
      }
    };
  }

  @Override
  public Object addNewBookMarkToAPODDB(final APOD_DB_DTO apodDbDto,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAPOD_DB_DTO.insert(apodDbDto);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object addNewBookMarkToRoverDB(final MarsRoversDBDTO marsRoverDbDto,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMarsRoversDBDTO.insert(marsRoverDbDto);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object addCustomBookMarkTopic(final BookMarkScreenGridNames bookMarkScreenGridNames,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBookMarkScreenGridNames.insert(bookMarkScreenGridNames);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object addNewBookMarkToNewsDB(final NewsDB newsDB,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfNewsDB.insert(newsDB);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object addAPIKeys(final APIKeysDB apiKeysDB,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAPIKeysDB.insert(apiKeysDB);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteFromAPODDB(final String imageURL,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteFromAPODDB.acquire();
        int _argIndex = 1;
        if (imageURL == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, imageURL);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteFromAPODDB.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object addDataInAnExistingBookmarkTopic(final String tableName,
      final CustomBookMarkData newData, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAddDataInAnExistingBookmarkTopic.acquire();
        int _argIndex = 1;
        final String _tmp = __bookMarkDataConverterForCustomBookMarks.convertToString(newData);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _tmp);
        }
        _argIndex = 2;
        if (tableName == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, tableName);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfAddDataInAnExistingBookmarkTopic.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteFromRoverDB(final String imageURL,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteFromRoverDB.acquire();
        int _argIndex = 1;
        if (imageURL == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, imageURL);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteFromRoverDB.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteFromNewsDB(final String sourceURL,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteFromNewsDB.acquire();
        int _argIndex = 1;
        if (sourceURL == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, sourceURL);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteFromNewsDB.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteFromCustomBookmarksDB(final String name,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteFromCustomBookmarksDB.acquire();
        int _argIndex = 1;
        if (name == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, name);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteFromCustomBookmarksDB.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteAllDataFromMarsDB(final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllDataFromMarsDB.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteAllDataFromMarsDB.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteAllDataFromAPODDB(final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllDataFromAPODDB.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteAllDataFromAPODDB.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteAllDataFromNewsDB(final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllDataFromNewsDB.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteAllDataFromNewsDB.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteAllDataFromCustomBookMarkDB(final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllDataFromCustomBookMarkDB.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteAllDataFromCustomBookMarkDB.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteACollectionFromCustomBookMarksDB(final String nameOfTheCollection,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteACollectionFromCustomBookMarksDB.acquire();
        int _argIndex = 1;
        if (nameOfTheCollection == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, nameOfTheCollection);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteACollectionFromCustomBookMarksDB.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object renameACollectionFromCustomBookMarksDB(final String actualNameOfTheCollection,
      final String updatedNameOfTheCollection, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRenameACollectionFromCustomBookMarksDB.acquire();
        int _argIndex = 1;
        if (updatedNameOfTheCollection == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, updatedNameOfTheCollection);
        }
        _argIndex = 2;
        if (actualNameOfTheCollection == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, actualNameOfTheCollection);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfRenameACollectionFromCustomBookMarksDB.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<APOD_DB_DTO>> getBookMarkedAPODDBDATA() {
    final String _sql = "SELECT * FROM apod_db ORDER BY imageURL ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"apod_db"}, new Callable<List<APOD_DB_DTO>>() {
      @Override
      public List<APOD_DB_DTO> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDatePublished = CursorUtil.getColumnIndexOrThrow(_cursor, "datePublished");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfImageURL = CursorUtil.getColumnIndexOrThrow(_cursor, "imageURL");
          final int _cursorIndexOfHdImageURL = CursorUtil.getColumnIndexOrThrow(_cursor, "hdImageURL");
          final int _cursorIndexOfMediaType = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaType");
          final int _cursorIndexOfIsBookMarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookMarked");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfAddedToLocalDBOn = CursorUtil.getColumnIndexOrThrow(_cursor, "addedToLocalDBOn");
          final List<APOD_DB_DTO> _result = new ArrayList<APOD_DB_DTO>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final APOD_DB_DTO _item;
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpDatePublished;
            if (_cursor.isNull(_cursorIndexOfDatePublished)) {
              _tmpDatePublished = null;
            } else {
              _tmpDatePublished = _cursor.getString(_cursorIndexOfDatePublished);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpImageURL;
            if (_cursor.isNull(_cursorIndexOfImageURL)) {
              _tmpImageURL = null;
            } else {
              _tmpImageURL = _cursor.getString(_cursorIndexOfImageURL);
            }
            final String _tmpHdImageURL;
            if (_cursor.isNull(_cursorIndexOfHdImageURL)) {
              _tmpHdImageURL = null;
            } else {
              _tmpHdImageURL = _cursor.getString(_cursorIndexOfHdImageURL);
            }
            final String _tmpMediaType;
            if (_cursor.isNull(_cursorIndexOfMediaType)) {
              _tmpMediaType = null;
            } else {
              _tmpMediaType = _cursor.getString(_cursorIndexOfMediaType);
            }
            final boolean _tmpIsBookMarked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBookMarked);
            _tmpIsBookMarked = _tmp != 0;
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpAddedToLocalDBOn;
            if (_cursor.isNull(_cursorIndexOfAddedToLocalDBOn)) {
              _tmpAddedToLocalDBOn = null;
            } else {
              _tmpAddedToLocalDBOn = _cursor.getString(_cursorIndexOfAddedToLocalDBOn);
            }
            _item = new APOD_DB_DTO(_tmpTitle,_tmpDatePublished,_tmpDescription,_tmpImageURL,_tmpHdImageURL,_tmpMediaType,_tmpIsBookMarked,_tmpCategory,_tmpAddedToLocalDBOn);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MarsRoversDBDTO>> getBookMarkedRoverDBDATA() {
    final String _sql = "SELECT * FROM marsRovers_db ORDER BY imageURL ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"marsRovers_db"}, new Callable<List<MarsRoversDBDTO>>() {
      @Override
      public List<MarsRoversDBDTO> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfImageURL = CursorUtil.getColumnIndexOrThrow(_cursor, "imageURL");
          final int _cursorIndexOfCapturedBy = CursorUtil.getColumnIndexOrThrow(_cursor, "capturedBy");
          final int _cursorIndexOfSol = CursorUtil.getColumnIndexOrThrow(_cursor, "sol");
          final int _cursorIndexOfEarthDate = CursorUtil.getColumnIndexOrThrow(_cursor, "earthDate");
          final int _cursorIndexOfRoverName = CursorUtil.getColumnIndexOrThrow(_cursor, "roverName");
          final int _cursorIndexOfRoverStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "roverStatus");
          final int _cursorIndexOfLaunchingDate = CursorUtil.getColumnIndexOrThrow(_cursor, "launchingDate");
          final int _cursorIndexOfLandingDate = CursorUtil.getColumnIndexOrThrow(_cursor, "landingDate");
          final int _cursorIndexOfIsBookMarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookMarked");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfAddedToLocalDBOn = CursorUtil.getColumnIndexOrThrow(_cursor, "addedToLocalDBOn");
          final List<MarsRoversDBDTO> _result = new ArrayList<MarsRoversDBDTO>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final MarsRoversDBDTO _item;
            final String _tmpImageURL;
            if (_cursor.isNull(_cursorIndexOfImageURL)) {
              _tmpImageURL = null;
            } else {
              _tmpImageURL = _cursor.getString(_cursorIndexOfImageURL);
            }
            final String _tmpCapturedBy;
            if (_cursor.isNull(_cursorIndexOfCapturedBy)) {
              _tmpCapturedBy = null;
            } else {
              _tmpCapturedBy = _cursor.getString(_cursorIndexOfCapturedBy);
            }
            final String _tmpSol;
            if (_cursor.isNull(_cursorIndexOfSol)) {
              _tmpSol = null;
            } else {
              _tmpSol = _cursor.getString(_cursorIndexOfSol);
            }
            final String _tmpEarthDate;
            if (_cursor.isNull(_cursorIndexOfEarthDate)) {
              _tmpEarthDate = null;
            } else {
              _tmpEarthDate = _cursor.getString(_cursorIndexOfEarthDate);
            }
            final String _tmpRoverName;
            if (_cursor.isNull(_cursorIndexOfRoverName)) {
              _tmpRoverName = null;
            } else {
              _tmpRoverName = _cursor.getString(_cursorIndexOfRoverName);
            }
            final String _tmpRoverStatus;
            if (_cursor.isNull(_cursorIndexOfRoverStatus)) {
              _tmpRoverStatus = null;
            } else {
              _tmpRoverStatus = _cursor.getString(_cursorIndexOfRoverStatus);
            }
            final String _tmpLaunchingDate;
            if (_cursor.isNull(_cursorIndexOfLaunchingDate)) {
              _tmpLaunchingDate = null;
            } else {
              _tmpLaunchingDate = _cursor.getString(_cursorIndexOfLaunchingDate);
            }
            final String _tmpLandingDate;
            if (_cursor.isNull(_cursorIndexOfLandingDate)) {
              _tmpLandingDate = null;
            } else {
              _tmpLandingDate = _cursor.getString(_cursorIndexOfLandingDate);
            }
            final boolean _tmpIsBookMarked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBookMarked);
            _tmpIsBookMarked = _tmp != 0;
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpAddedToLocalDBOn;
            if (_cursor.isNull(_cursorIndexOfAddedToLocalDBOn)) {
              _tmpAddedToLocalDBOn = null;
            } else {
              _tmpAddedToLocalDBOn = _cursor.getString(_cursorIndexOfAddedToLocalDBOn);
            }
            _item = new MarsRoversDBDTO(_tmpImageURL,_tmpCapturedBy,_tmpSol,_tmpEarthDate,_tmpRoverName,_tmpRoverStatus,_tmpLaunchingDate,_tmpLandingDate,_tmpIsBookMarked,_tmpCategory,_tmpAddedToLocalDBOn);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<NewsDB>> getBookMarkedNewsDATA() {
    final String _sql = "SELECT * FROM newsDB ORDER BY imageURL ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"newsDB"}, new Callable<List<NewsDB>>() {
      @Override
      public List<NewsDB> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfImageURL = CursorUtil.getColumnIndexOrThrow(_cursor, "imageURL");
          final int _cursorIndexOfSourceURL = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceURL");
          final int _cursorIndexOfSourceOfNews = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceOfNews");
          final int _cursorIndexOfPublishedTime = CursorUtil.getColumnIndexOrThrow(_cursor, "publishedTime");
          final List<NewsDB> _result = new ArrayList<NewsDB>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final NewsDB _item;
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpImageURL;
            if (_cursor.isNull(_cursorIndexOfImageURL)) {
              _tmpImageURL = null;
            } else {
              _tmpImageURL = _cursor.getString(_cursorIndexOfImageURL);
            }
            final String _tmpSourceURL;
            if (_cursor.isNull(_cursorIndexOfSourceURL)) {
              _tmpSourceURL = null;
            } else {
              _tmpSourceURL = _cursor.getString(_cursorIndexOfSourceURL);
            }
            final String _tmpSourceOfNews;
            if (_cursor.isNull(_cursorIndexOfSourceOfNews)) {
              _tmpSourceOfNews = null;
            } else {
              _tmpSourceOfNews = _cursor.getString(_cursorIndexOfSourceOfNews);
            }
            final String _tmpPublishedTime;
            if (_cursor.isNull(_cursorIndexOfPublishedTime)) {
              _tmpPublishedTime = null;
            } else {
              _tmpPublishedTime = _cursor.getString(_cursorIndexOfPublishedTime);
            }
            _item = new NewsDB(_tmpTitle,_tmpImageURL,_tmpSourceURL,_tmpSourceOfNews,_tmpPublishedTime);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<BookMarkScreenGridNames>> getCustomBookMarkTopicData() {
    final String _sql = "SELECT * FROM bookMarkScreen_GridNames ORDER BY name ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"bookMarkScreen_GridNames"}, new Callable<List<BookMarkScreenGridNames>>() {
      @Override
      public List<BookMarkScreenGridNames> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfImgUrlForGrid = CursorUtil.getColumnIndexOrThrow(_cursor, "imgUrlForGrid");
          final int _cursorIndexOfData = CursorUtil.getColumnIndexOrThrow(_cursor, "data");
          final List<BookMarkScreenGridNames> _result = new ArrayList<BookMarkScreenGridNames>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final BookMarkScreenGridNames _item;
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpImgUrlForGrid;
            if (_cursor.isNull(_cursorIndexOfImgUrlForGrid)) {
              _tmpImgUrlForGrid = null;
            } else {
              _tmpImgUrlForGrid = _cursor.getString(_cursorIndexOfImgUrlForGrid);
            }
            final List<CustomBookMarkData> _tmpData;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfData)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfData);
            }
            _tmpData = __bookMarkDataConverterForListOfCustomBookMarks.convertToList(_tmp);
            _item = new BookMarkScreenGridNames(_tmpName,_tmpImgUrlForGrid,_tmpData);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object doesThisExistsInAPODDB(final String imageURL,
      final Continuation<? super Boolean> continuation) {
    final String _sql = "SELECT EXISTS(SELECT * FROM apod_db WHERE imageURL = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (imageURL == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, imageURL);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if(_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object doesThisExistsInRoversDB(final String imageURL,
      final Continuation<? super Boolean> continuation) {
    final String _sql = "SELECT EXISTS(SELECT * FROM marsRovers_db WHERE imageURL = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (imageURL == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, imageURL);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if(_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object doesThisExistsInNewsDB(final String sourceURL,
      final Continuation<? super Boolean> continuation) {
    final String _sql = "SELECT EXISTS(SELECT * FROM newsDB WHERE sourceURL = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (sourceURL == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, sourceURL);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if(_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object getAPIKeys(final Continuation<? super List<APIKeysDB>> continuation) {
    final String _sql = "SELECT * FROM apiKeys";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<APIKeysDB>>() {
      @Override
      public List<APIKeysDB> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCurrentNASAAPIKey = CursorUtil.getColumnIndexOrThrow(_cursor, "currentNASAAPIKey");
          final int _cursorIndexOfCurrentNewsAPIKey = CursorUtil.getColumnIndexOrThrow(_cursor, "currentNewsAPIKey");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<APIKeysDB> _result = new ArrayList<APIKeysDB>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final APIKeysDB _item;
            final String _tmpCurrentNASAAPIKey;
            if (_cursor.isNull(_cursorIndexOfCurrentNASAAPIKey)) {
              _tmpCurrentNASAAPIKey = null;
            } else {
              _tmpCurrentNASAAPIKey = _cursor.getString(_cursorIndexOfCurrentNASAAPIKey);
            }
            final String _tmpCurrentNewsAPIKey;
            if (_cursor.isNull(_cursorIndexOfCurrentNewsAPIKey)) {
              _tmpCurrentNewsAPIKey = null;
            } else {
              _tmpCurrentNewsAPIKey = _cursor.getString(_cursorIndexOfCurrentNewsAPIKey);
            }
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            _item = new APIKeysDB(_tmpCurrentNASAAPIKey,_tmpCurrentNewsAPIKey,_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object didThisNasaApiKeyGotUpdated(final String apiKey,
      final Continuation<? super Boolean> continuation) {
    final String _sql = "SELECT EXISTS(SELECT * FROM apiKeys WHERE currentNASAAPIKey = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (apiKey == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, apiKey);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if(_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object didThisNewsApiKeyGotUpdated(final String apiKey,
      final Continuation<? super Boolean> continuation) {
    final String _sql = "SELECT EXISTS(SELECT * FROM apiKeys WHERE currentNewsAPIKey = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (apiKey == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, apiKey);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if(_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  @Override
  public Object doesThisTableExistsInCustomBookMarkDB(final String name,
      final Continuation<? super Boolean> continuation) {
    final String _sql = "SELECT EXISTS(SELECT 1 FROM bookMarkScreen_GridNames WHERE name = ?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if(_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
