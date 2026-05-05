package com.moodly.app.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.moodly.app.data.model.Converters;
import com.moodly.app.data.model.EntradaDiaria;
import com.moodly.app.data.model.Etiqueta;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class EntradaDao_Impl implements EntradaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EntradaDiaria> __insertionAdapterOfEntradaDiaria;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<EntradaDiaria> __deletionAdapterOfEntradaDiaria;

  public EntradaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEntradaDiaria = new EntityInsertionAdapter<EntradaDiaria>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `entradas` (`fecha`,`estadoNivel`,`nota`,`etiquetas`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EntradaDiaria entity) {
        final String _tmp = __converters.toLocalDate(entity.getFecha());
        if (_tmp == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, _tmp);
        }
        statement.bindLong(2, entity.getEstadoNivel());
        statement.bindString(3, entity.getNota());
        final String _tmp_1 = __converters.toEtiquetas(entity.getEtiquetas());
        statement.bindString(4, _tmp_1);
      }
    };
    this.__deletionAdapterOfEntradaDiaria = new EntityDeletionOrUpdateAdapter<EntradaDiaria>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `entradas` WHERE `fecha` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EntradaDiaria entity) {
        final String _tmp = __converters.toLocalDate(entity.getFecha());
        if (_tmp == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, _tmp);
        }
      }
    };
  }

  @Override
  public Object insertar(final EntradaDiaria entrada,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfEntradaDiaria.insert(entrada);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object eliminar(final EntradaDiaria entrada,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfEntradaDiaria.handle(entrada);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<EntradaDiaria>> observarTodas() {
    final String _sql = "SELECT * FROM entradas ORDER BY fecha DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"entradas"}, new Callable<List<EntradaDiaria>>() {
      @Override
      @NonNull
      public List<EntradaDiaria> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfEstadoNivel = CursorUtil.getColumnIndexOrThrow(_cursor, "estadoNivel");
          final int _cursorIndexOfNota = CursorUtil.getColumnIndexOrThrow(_cursor, "nota");
          final int _cursorIndexOfEtiquetas = CursorUtil.getColumnIndexOrThrow(_cursor, "etiquetas");
          final List<EntradaDiaria> _result = new ArrayList<EntradaDiaria>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EntradaDiaria _item;
            final LocalDate _tmpFecha;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfFecha)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfFecha);
            }
            final LocalDate _tmp_1 = __converters.fromLocalDate(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpFecha = _tmp_1;
            }
            final int _tmpEstadoNivel;
            _tmpEstadoNivel = _cursor.getInt(_cursorIndexOfEstadoNivel);
            final String _tmpNota;
            _tmpNota = _cursor.getString(_cursorIndexOfNota);
            final List<Etiqueta> _tmpEtiquetas;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfEtiquetas);
            _tmpEtiquetas = __converters.fromEtiquetas(_tmp_2);
            _item = new EntradaDiaria(_tmpFecha,_tmpEstadoNivel,_tmpNota,_tmpEtiquetas);
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
  public Flow<List<EntradaDiaria>> observarSemana(final LocalDate inicio, final LocalDate fin) {
    final String _sql = "\n"
            + "        SELECT * FROM entradas\n"
            + "        WHERE fecha >= ? AND fecha <= ?\n"
            + "        ORDER BY fecha DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __converters.toLocalDate(inicio);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    final String _tmp_1 = __converters.toLocalDate(fin);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp_1);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"entradas"}, new Callable<List<EntradaDiaria>>() {
      @Override
      @NonNull
      public List<EntradaDiaria> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfEstadoNivel = CursorUtil.getColumnIndexOrThrow(_cursor, "estadoNivel");
          final int _cursorIndexOfNota = CursorUtil.getColumnIndexOrThrow(_cursor, "nota");
          final int _cursorIndexOfEtiquetas = CursorUtil.getColumnIndexOrThrow(_cursor, "etiquetas");
          final List<EntradaDiaria> _result = new ArrayList<EntradaDiaria>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EntradaDiaria _item;
            final LocalDate _tmpFecha;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfFecha)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfFecha);
            }
            final LocalDate _tmp_3 = __converters.fromLocalDate(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpFecha = _tmp_3;
            }
            final int _tmpEstadoNivel;
            _tmpEstadoNivel = _cursor.getInt(_cursorIndexOfEstadoNivel);
            final String _tmpNota;
            _tmpNota = _cursor.getString(_cursorIndexOfNota);
            final List<Etiqueta> _tmpEtiquetas;
            final String _tmp_4;
            _tmp_4 = _cursor.getString(_cursorIndexOfEtiquetas);
            _tmpEtiquetas = __converters.fromEtiquetas(_tmp_4);
            _item = new EntradaDiaria(_tmpFecha,_tmpEstadoNivel,_tmpNota,_tmpEtiquetas);
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
  public Object porFecha(final LocalDate fecha,
      final Continuation<? super EntradaDiaria> $completion) {
    final String _sql = "SELECT * FROM entradas WHERE fecha = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.toLocalDate(fecha);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<EntradaDiaria>() {
      @Override
      @Nullable
      public EntradaDiaria call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfEstadoNivel = CursorUtil.getColumnIndexOrThrow(_cursor, "estadoNivel");
          final int _cursorIndexOfNota = CursorUtil.getColumnIndexOrThrow(_cursor, "nota");
          final int _cursorIndexOfEtiquetas = CursorUtil.getColumnIndexOrThrow(_cursor, "etiquetas");
          final EntradaDiaria _result;
          if (_cursor.moveToFirst()) {
            final LocalDate _tmpFecha;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfFecha)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfFecha);
            }
            final LocalDate _tmp_2 = __converters.fromLocalDate(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDate', but it was NULL.");
            } else {
              _tmpFecha = _tmp_2;
            }
            final int _tmpEstadoNivel;
            _tmpEstadoNivel = _cursor.getInt(_cursorIndexOfEstadoNivel);
            final String _tmpNota;
            _tmpNota = _cursor.getString(_cursorIndexOfNota);
            final List<Etiqueta> _tmpEtiquetas;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfEtiquetas);
            _tmpEtiquetas = __converters.fromEtiquetas(_tmp_3);
            _result = new EntradaDiaria(_tmpFecha,_tmpEstadoNivel,_tmpNota,_tmpEtiquetas);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
