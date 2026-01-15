package com.example.reelscounter.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UsageDao_Impl implements UsageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UsageStats> __insertionAdapterOfUsageStats;

  private final SharedSQLiteStatement __preparedStmtOfIncrementReels;

  private final SharedSQLiteStatement __preparedStmtOfIncrementShorts;

  public UsageDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUsageStats = new EntityInsertionAdapter<UsageStats>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `usage_stats` (`date`,`reelsCount`,`shortsCount`,`totalTimeMillis`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UsageStats entity) {
        statement.bindString(1, entity.getDate());
        statement.bindLong(2, entity.getReelsCount());
        statement.bindLong(3, entity.getShortsCount());
        statement.bindLong(4, entity.getTotalTimeMillis());
      }
    };
    this.__preparedStmtOfIncrementReels = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE usage_stats SET reelsCount = reelsCount + 1 WHERE date = ?";
        return _query;
      }
    };
    this.__preparedStmtOfIncrementShorts = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE usage_stats SET shortsCount = shortsCount + 1 WHERE date = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertOrUpdate(final UsageStats stats,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUsageStats.insert(stats);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementReels(final String date, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementReels.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, date);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfIncrementReels.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object incrementShorts(final String date, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfIncrementShorts.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, date);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfIncrementShorts.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getUsageForDate(final String date,
      final Continuation<? super UsageStats> $completion) {
    final String _sql = "SELECT * FROM usage_stats WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UsageStats>() {
      @Override
      @Nullable
      public UsageStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfReelsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "reelsCount");
          final int _cursorIndexOfShortsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "shortsCount");
          final int _cursorIndexOfTotalTimeMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTimeMillis");
          final UsageStats _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpReelsCount;
            _tmpReelsCount = _cursor.getInt(_cursorIndexOfReelsCount);
            final int _tmpShortsCount;
            _tmpShortsCount = _cursor.getInt(_cursorIndexOfShortsCount);
            final long _tmpTotalTimeMillis;
            _tmpTotalTimeMillis = _cursor.getLong(_cursorIndexOfTotalTimeMillis);
            _result = new UsageStats(_tmpDate,_tmpReelsCount,_tmpShortsCount,_tmpTotalTimeMillis);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<UsageStats> getUsageFlowForDate(final String date) {
    final String _sql = "SELECT * FROM usage_stats WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"usage_stats"}, new Callable<UsageStats>() {
      @Override
      @Nullable
      public UsageStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfReelsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "reelsCount");
          final int _cursorIndexOfShortsCount = CursorUtil.getColumnIndexOrThrow(_cursor, "shortsCount");
          final int _cursorIndexOfTotalTimeMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "totalTimeMillis");
          final UsageStats _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final int _tmpReelsCount;
            _tmpReelsCount = _cursor.getInt(_cursorIndexOfReelsCount);
            final int _tmpShortsCount;
            _tmpShortsCount = _cursor.getInt(_cursorIndexOfShortsCount);
            final long _tmpTotalTimeMillis;
            _tmpTotalTimeMillis = _cursor.getLong(_cursorIndexOfTotalTimeMillis);
            _result = new UsageStats(_tmpDate,_tmpReelsCount,_tmpShortsCount,_tmpTotalTimeMillis);
          } else {
            _result = null;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
