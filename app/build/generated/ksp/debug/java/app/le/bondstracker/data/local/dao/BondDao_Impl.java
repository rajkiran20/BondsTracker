package app.le.bondstracker.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import app.le.bondstracker.data.local.entity.BondEntity;
import app.le.bondstracker.data.local.entity.BondWithPayouts;
import app.le.bondstracker.data.local.entity.PayoutEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BondDao_Impl implements BondDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BondEntity> __insertionAdapterOfBondEntity;

  private final EntityInsertionAdapter<PayoutEntity> __insertionAdapterOfPayoutEntity;

  private final EntityDeletionOrUpdateAdapter<BondEntity> __deletionAdapterOfBondEntity;

  public BondDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBondEntity = new EntityInsertionAdapter<BondEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `bonds` (`investmentId`,`createdAt`,`platform`,`investor`,`companyName`,`bondCategory`,`bondTypeJson`,`status`,`currency`,`investmentAmount`,`faceValuePerUnit`,`units`,`currentValue`,`outstandingPrincipal`,`returnsReceived`,`gains`,`totalPrincipalRepaid`,`interestRate`,`couponRate`,`payoutFrequency`,`startDate`,`orderDate`,`maturityDate`,`tenureMonths`,`interestPaid`,`nextPayoutDate`,`notes`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BondEntity entity) {
        statement.bindString(1, entity.getInvestmentId());
        statement.bindString(2, entity.getCreatedAt());
        statement.bindString(3, entity.getPlatform());
        statement.bindString(4, entity.getInvestor());
        statement.bindString(5, entity.getCompanyName());
        statement.bindString(6, entity.getBondCategory());
        statement.bindString(7, entity.getBondTypeJson());
        statement.bindString(8, entity.getStatus());
        statement.bindString(9, entity.getCurrency());
        statement.bindDouble(10, entity.getInvestmentAmount());
        statement.bindDouble(11, entity.getFaceValuePerUnit());
        statement.bindLong(12, entity.getUnits());
        if (entity.getCurrentValue() == null) {
          statement.bindNull(13);
        } else {
          statement.bindDouble(13, entity.getCurrentValue());
        }
        statement.bindDouble(14, entity.getOutstandingPrincipal());
        statement.bindDouble(15, entity.getReturnsReceived());
        if (entity.getGains() == null) {
          statement.bindNull(16);
        } else {
          statement.bindDouble(16, entity.getGains());
        }
        statement.bindDouble(17, entity.getTotalPrincipalRepaid());
        statement.bindDouble(18, entity.getInterestRate());
        statement.bindDouble(19, entity.getCouponRate());
        statement.bindString(20, entity.getPayoutFrequency());
        statement.bindString(21, entity.getStartDate());
        statement.bindString(22, entity.getOrderDate());
        statement.bindString(23, entity.getMaturityDate());
        statement.bindLong(24, entity.getTenureMonths());
        statement.bindDouble(25, entity.getInterestPaid());
        if (entity.getNextPayoutDate() == null) {
          statement.bindNull(26);
        } else {
          statement.bindString(26, entity.getNextPayoutDate());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(27);
        } else {
          statement.bindString(27, entity.getNotes());
        }
      }
    };
    this.__insertionAdapterOfPayoutEntity = new EntityInsertionAdapter<PayoutEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `payouts` (`payoutId`,`bondId`,`date`,`payoutType`,`amount`,`principalComponent`,`interestComponent`,`status`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PayoutEntity entity) {
        statement.bindString(1, entity.getPayoutId());
        statement.bindString(2, entity.getBondId());
        statement.bindString(3, entity.getDate());
        statement.bindString(4, entity.getPayoutType());
        statement.bindDouble(5, entity.getAmount());
        statement.bindDouble(6, entity.getPrincipalComponent());
        statement.bindDouble(7, entity.getInterestComponent());
        statement.bindString(8, entity.getStatus());
      }
    };
    this.__deletionAdapterOfBondEntity = new EntityDeletionOrUpdateAdapter<BondEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `bonds` WHERE `investmentId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BondEntity entity) {
        statement.bindString(1, entity.getInvestmentId());
      }
    };
  }

  @Override
  public Object insertBond(final BondEntity bond, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBondEntity.insert(bond);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBonds(final List<BondEntity> bonds,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBondEntity.insert(bonds);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertPayouts(final List<PayoutEntity> payouts,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPayoutEntity.insert(payouts);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBond(final BondEntity bond, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBondEntity.handle(bond);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBondWithPayouts(final BondEntity bond, final List<PayoutEntity> payouts,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> BondDao.DefaultImpls.insertBondWithPayouts(BondDao_Impl.this, bond, payouts, __cont), $completion);
  }

  @Override
  public Object insertBondsWithPayouts(final List<BondEntity> bonds,
      final List<PayoutEntity> payouts, final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> BondDao.DefaultImpls.insertBondsWithPayouts(BondDao_Impl.this, bonds, payouts, __cont), $completion);
  }

  @Override
  public Flow<List<BondWithPayouts>> getAllBondsWithPayouts() {
    final String _sql = "SELECT * FROM bonds ORDER BY maturityDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"payouts",
        "bonds"}, new Callable<List<BondWithPayouts>>() {
      @Override
      @NonNull
      public List<BondWithPayouts> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfInvestmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "investmentId");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
            final int _cursorIndexOfInvestor = CursorUtil.getColumnIndexOrThrow(_cursor, "investor");
            final int _cursorIndexOfCompanyName = CursorUtil.getColumnIndexOrThrow(_cursor, "companyName");
            final int _cursorIndexOfBondCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "bondCategory");
            final int _cursorIndexOfBondTypeJson = CursorUtil.getColumnIndexOrThrow(_cursor, "bondTypeJson");
            final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
            final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
            final int _cursorIndexOfInvestmentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "investmentAmount");
            final int _cursorIndexOfFaceValuePerUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "faceValuePerUnit");
            final int _cursorIndexOfUnits = CursorUtil.getColumnIndexOrThrow(_cursor, "units");
            final int _cursorIndexOfCurrentValue = CursorUtil.getColumnIndexOrThrow(_cursor, "currentValue");
            final int _cursorIndexOfOutstandingPrincipal = CursorUtil.getColumnIndexOrThrow(_cursor, "outstandingPrincipal");
            final int _cursorIndexOfReturnsReceived = CursorUtil.getColumnIndexOrThrow(_cursor, "returnsReceived");
            final int _cursorIndexOfGains = CursorUtil.getColumnIndexOrThrow(_cursor, "gains");
            final int _cursorIndexOfTotalPrincipalRepaid = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPrincipalRepaid");
            final int _cursorIndexOfInterestRate = CursorUtil.getColumnIndexOrThrow(_cursor, "interestRate");
            final int _cursorIndexOfCouponRate = CursorUtil.getColumnIndexOrThrow(_cursor, "couponRate");
            final int _cursorIndexOfPayoutFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "payoutFrequency");
            final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
            final int _cursorIndexOfOrderDate = CursorUtil.getColumnIndexOrThrow(_cursor, "orderDate");
            final int _cursorIndexOfMaturityDate = CursorUtil.getColumnIndexOrThrow(_cursor, "maturityDate");
            final int _cursorIndexOfTenureMonths = CursorUtil.getColumnIndexOrThrow(_cursor, "tenureMonths");
            final int _cursorIndexOfInterestPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "interestPaid");
            final int _cursorIndexOfNextPayoutDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextPayoutDate");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final ArrayMap<String, ArrayList<PayoutEntity>> _collectionPayouts = new ArrayMap<String, ArrayList<PayoutEntity>>();
            while (_cursor.moveToNext()) {
              final String _tmpKey;
              _tmpKey = _cursor.getString(_cursorIndexOfInvestmentId);
              if (!_collectionPayouts.containsKey(_tmpKey)) {
                _collectionPayouts.put(_tmpKey, new ArrayList<PayoutEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshippayoutsAsappLeBondstrackerDataLocalEntityPayoutEntity(_collectionPayouts);
            final List<BondWithPayouts> _result = new ArrayList<BondWithPayouts>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final BondWithPayouts _item;
              final BondEntity _tmpBond;
              final String _tmpInvestmentId;
              _tmpInvestmentId = _cursor.getString(_cursorIndexOfInvestmentId);
              final String _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
              final String _tmpPlatform;
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
              final String _tmpInvestor;
              _tmpInvestor = _cursor.getString(_cursorIndexOfInvestor);
              final String _tmpCompanyName;
              _tmpCompanyName = _cursor.getString(_cursorIndexOfCompanyName);
              final String _tmpBondCategory;
              _tmpBondCategory = _cursor.getString(_cursorIndexOfBondCategory);
              final String _tmpBondTypeJson;
              _tmpBondTypeJson = _cursor.getString(_cursorIndexOfBondTypeJson);
              final String _tmpStatus;
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
              final String _tmpCurrency;
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
              final double _tmpInvestmentAmount;
              _tmpInvestmentAmount = _cursor.getDouble(_cursorIndexOfInvestmentAmount);
              final double _tmpFaceValuePerUnit;
              _tmpFaceValuePerUnit = _cursor.getDouble(_cursorIndexOfFaceValuePerUnit);
              final int _tmpUnits;
              _tmpUnits = _cursor.getInt(_cursorIndexOfUnits);
              final Double _tmpCurrentValue;
              if (_cursor.isNull(_cursorIndexOfCurrentValue)) {
                _tmpCurrentValue = null;
              } else {
                _tmpCurrentValue = _cursor.getDouble(_cursorIndexOfCurrentValue);
              }
              final double _tmpOutstandingPrincipal;
              _tmpOutstandingPrincipal = _cursor.getDouble(_cursorIndexOfOutstandingPrincipal);
              final double _tmpReturnsReceived;
              _tmpReturnsReceived = _cursor.getDouble(_cursorIndexOfReturnsReceived);
              final Double _tmpGains;
              if (_cursor.isNull(_cursorIndexOfGains)) {
                _tmpGains = null;
              } else {
                _tmpGains = _cursor.getDouble(_cursorIndexOfGains);
              }
              final double _tmpTotalPrincipalRepaid;
              _tmpTotalPrincipalRepaid = _cursor.getDouble(_cursorIndexOfTotalPrincipalRepaid);
              final double _tmpInterestRate;
              _tmpInterestRate = _cursor.getDouble(_cursorIndexOfInterestRate);
              final double _tmpCouponRate;
              _tmpCouponRate = _cursor.getDouble(_cursorIndexOfCouponRate);
              final String _tmpPayoutFrequency;
              _tmpPayoutFrequency = _cursor.getString(_cursorIndexOfPayoutFrequency);
              final String _tmpStartDate;
              _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
              final String _tmpOrderDate;
              _tmpOrderDate = _cursor.getString(_cursorIndexOfOrderDate);
              final String _tmpMaturityDate;
              _tmpMaturityDate = _cursor.getString(_cursorIndexOfMaturityDate);
              final int _tmpTenureMonths;
              _tmpTenureMonths = _cursor.getInt(_cursorIndexOfTenureMonths);
              final double _tmpInterestPaid;
              _tmpInterestPaid = _cursor.getDouble(_cursorIndexOfInterestPaid);
              final String _tmpNextPayoutDate;
              if (_cursor.isNull(_cursorIndexOfNextPayoutDate)) {
                _tmpNextPayoutDate = null;
              } else {
                _tmpNextPayoutDate = _cursor.getString(_cursorIndexOfNextPayoutDate);
              }
              final String _tmpNotes;
              if (_cursor.isNull(_cursorIndexOfNotes)) {
                _tmpNotes = null;
              } else {
                _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              }
              _tmpBond = new BondEntity(_tmpInvestmentId,_tmpCreatedAt,_tmpPlatform,_tmpInvestor,_tmpCompanyName,_tmpBondCategory,_tmpBondTypeJson,_tmpStatus,_tmpCurrency,_tmpInvestmentAmount,_tmpFaceValuePerUnit,_tmpUnits,_tmpCurrentValue,_tmpOutstandingPrincipal,_tmpReturnsReceived,_tmpGains,_tmpTotalPrincipalRepaid,_tmpInterestRate,_tmpCouponRate,_tmpPayoutFrequency,_tmpStartDate,_tmpOrderDate,_tmpMaturityDate,_tmpTenureMonths,_tmpInterestPaid,_tmpNextPayoutDate,_tmpNotes);
              final ArrayList<PayoutEntity> _tmpPayoutsCollection;
              final String _tmpKey_1;
              _tmpKey_1 = _cursor.getString(_cursorIndexOfInvestmentId);
              _tmpPayoutsCollection = _collectionPayouts.get(_tmpKey_1);
              _item = new BondWithPayouts(_tmpBond,_tmpPayoutsCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getBondWithPayoutsById(final String id,
      final Continuation<? super BondWithPayouts> $completion) {
    final String _sql = "SELECT * FROM bonds WHERE investmentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<BondWithPayouts>() {
      @Override
      @Nullable
      public BondWithPayouts call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfInvestmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "investmentId");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
            final int _cursorIndexOfInvestor = CursorUtil.getColumnIndexOrThrow(_cursor, "investor");
            final int _cursorIndexOfCompanyName = CursorUtil.getColumnIndexOrThrow(_cursor, "companyName");
            final int _cursorIndexOfBondCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "bondCategory");
            final int _cursorIndexOfBondTypeJson = CursorUtil.getColumnIndexOrThrow(_cursor, "bondTypeJson");
            final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
            final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
            final int _cursorIndexOfInvestmentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "investmentAmount");
            final int _cursorIndexOfFaceValuePerUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "faceValuePerUnit");
            final int _cursorIndexOfUnits = CursorUtil.getColumnIndexOrThrow(_cursor, "units");
            final int _cursorIndexOfCurrentValue = CursorUtil.getColumnIndexOrThrow(_cursor, "currentValue");
            final int _cursorIndexOfOutstandingPrincipal = CursorUtil.getColumnIndexOrThrow(_cursor, "outstandingPrincipal");
            final int _cursorIndexOfReturnsReceived = CursorUtil.getColumnIndexOrThrow(_cursor, "returnsReceived");
            final int _cursorIndexOfGains = CursorUtil.getColumnIndexOrThrow(_cursor, "gains");
            final int _cursorIndexOfTotalPrincipalRepaid = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPrincipalRepaid");
            final int _cursorIndexOfInterestRate = CursorUtil.getColumnIndexOrThrow(_cursor, "interestRate");
            final int _cursorIndexOfCouponRate = CursorUtil.getColumnIndexOrThrow(_cursor, "couponRate");
            final int _cursorIndexOfPayoutFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "payoutFrequency");
            final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
            final int _cursorIndexOfOrderDate = CursorUtil.getColumnIndexOrThrow(_cursor, "orderDate");
            final int _cursorIndexOfMaturityDate = CursorUtil.getColumnIndexOrThrow(_cursor, "maturityDate");
            final int _cursorIndexOfTenureMonths = CursorUtil.getColumnIndexOrThrow(_cursor, "tenureMonths");
            final int _cursorIndexOfInterestPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "interestPaid");
            final int _cursorIndexOfNextPayoutDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextPayoutDate");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final ArrayMap<String, ArrayList<PayoutEntity>> _collectionPayouts = new ArrayMap<String, ArrayList<PayoutEntity>>();
            while (_cursor.moveToNext()) {
              final String _tmpKey;
              _tmpKey = _cursor.getString(_cursorIndexOfInvestmentId);
              if (!_collectionPayouts.containsKey(_tmpKey)) {
                _collectionPayouts.put(_tmpKey, new ArrayList<PayoutEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshippayoutsAsappLeBondstrackerDataLocalEntityPayoutEntity(_collectionPayouts);
            final BondWithPayouts _result;
            if (_cursor.moveToFirst()) {
              final BondEntity _tmpBond;
              final String _tmpInvestmentId;
              _tmpInvestmentId = _cursor.getString(_cursorIndexOfInvestmentId);
              final String _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
              final String _tmpPlatform;
              _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
              final String _tmpInvestor;
              _tmpInvestor = _cursor.getString(_cursorIndexOfInvestor);
              final String _tmpCompanyName;
              _tmpCompanyName = _cursor.getString(_cursorIndexOfCompanyName);
              final String _tmpBondCategory;
              _tmpBondCategory = _cursor.getString(_cursorIndexOfBondCategory);
              final String _tmpBondTypeJson;
              _tmpBondTypeJson = _cursor.getString(_cursorIndexOfBondTypeJson);
              final String _tmpStatus;
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
              final String _tmpCurrency;
              _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
              final double _tmpInvestmentAmount;
              _tmpInvestmentAmount = _cursor.getDouble(_cursorIndexOfInvestmentAmount);
              final double _tmpFaceValuePerUnit;
              _tmpFaceValuePerUnit = _cursor.getDouble(_cursorIndexOfFaceValuePerUnit);
              final int _tmpUnits;
              _tmpUnits = _cursor.getInt(_cursorIndexOfUnits);
              final Double _tmpCurrentValue;
              if (_cursor.isNull(_cursorIndexOfCurrentValue)) {
                _tmpCurrentValue = null;
              } else {
                _tmpCurrentValue = _cursor.getDouble(_cursorIndexOfCurrentValue);
              }
              final double _tmpOutstandingPrincipal;
              _tmpOutstandingPrincipal = _cursor.getDouble(_cursorIndexOfOutstandingPrincipal);
              final double _tmpReturnsReceived;
              _tmpReturnsReceived = _cursor.getDouble(_cursorIndexOfReturnsReceived);
              final Double _tmpGains;
              if (_cursor.isNull(_cursorIndexOfGains)) {
                _tmpGains = null;
              } else {
                _tmpGains = _cursor.getDouble(_cursorIndexOfGains);
              }
              final double _tmpTotalPrincipalRepaid;
              _tmpTotalPrincipalRepaid = _cursor.getDouble(_cursorIndexOfTotalPrincipalRepaid);
              final double _tmpInterestRate;
              _tmpInterestRate = _cursor.getDouble(_cursorIndexOfInterestRate);
              final double _tmpCouponRate;
              _tmpCouponRate = _cursor.getDouble(_cursorIndexOfCouponRate);
              final String _tmpPayoutFrequency;
              _tmpPayoutFrequency = _cursor.getString(_cursorIndexOfPayoutFrequency);
              final String _tmpStartDate;
              _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
              final String _tmpOrderDate;
              _tmpOrderDate = _cursor.getString(_cursorIndexOfOrderDate);
              final String _tmpMaturityDate;
              _tmpMaturityDate = _cursor.getString(_cursorIndexOfMaturityDate);
              final int _tmpTenureMonths;
              _tmpTenureMonths = _cursor.getInt(_cursorIndexOfTenureMonths);
              final double _tmpInterestPaid;
              _tmpInterestPaid = _cursor.getDouble(_cursorIndexOfInterestPaid);
              final String _tmpNextPayoutDate;
              if (_cursor.isNull(_cursorIndexOfNextPayoutDate)) {
                _tmpNextPayoutDate = null;
              } else {
                _tmpNextPayoutDate = _cursor.getString(_cursorIndexOfNextPayoutDate);
              }
              final String _tmpNotes;
              if (_cursor.isNull(_cursorIndexOfNotes)) {
                _tmpNotes = null;
              } else {
                _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              }
              _tmpBond = new BondEntity(_tmpInvestmentId,_tmpCreatedAt,_tmpPlatform,_tmpInvestor,_tmpCompanyName,_tmpBondCategory,_tmpBondTypeJson,_tmpStatus,_tmpCurrency,_tmpInvestmentAmount,_tmpFaceValuePerUnit,_tmpUnits,_tmpCurrentValue,_tmpOutstandingPrincipal,_tmpReturnsReceived,_tmpGains,_tmpTotalPrincipalRepaid,_tmpInterestRate,_tmpCouponRate,_tmpPayoutFrequency,_tmpStartDate,_tmpOrderDate,_tmpMaturityDate,_tmpTenureMonths,_tmpInterestPaid,_tmpNextPayoutDate,_tmpNotes);
              final ArrayList<PayoutEntity> _tmpPayoutsCollection;
              final String _tmpKey_1;
              _tmpKey_1 = _cursor.getString(_cursorIndexOfInvestmentId);
              _tmpPayoutsCollection = _collectionPayouts.get(_tmpKey_1);
              _result = new BondWithPayouts(_tmpBond,_tmpPayoutsCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getBondById(final String id, final Continuation<? super BondEntity> $completion) {
    final String _sql = "SELECT * FROM bonds WHERE investmentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BondEntity>() {
      @Override
      @Nullable
      public BondEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfInvestmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "investmentId");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfPlatform = CursorUtil.getColumnIndexOrThrow(_cursor, "platform");
          final int _cursorIndexOfInvestor = CursorUtil.getColumnIndexOrThrow(_cursor, "investor");
          final int _cursorIndexOfCompanyName = CursorUtil.getColumnIndexOrThrow(_cursor, "companyName");
          final int _cursorIndexOfBondCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "bondCategory");
          final int _cursorIndexOfBondTypeJson = CursorUtil.getColumnIndexOrThrow(_cursor, "bondTypeJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfInvestmentAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "investmentAmount");
          final int _cursorIndexOfFaceValuePerUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "faceValuePerUnit");
          final int _cursorIndexOfUnits = CursorUtil.getColumnIndexOrThrow(_cursor, "units");
          final int _cursorIndexOfCurrentValue = CursorUtil.getColumnIndexOrThrow(_cursor, "currentValue");
          final int _cursorIndexOfOutstandingPrincipal = CursorUtil.getColumnIndexOrThrow(_cursor, "outstandingPrincipal");
          final int _cursorIndexOfReturnsReceived = CursorUtil.getColumnIndexOrThrow(_cursor, "returnsReceived");
          final int _cursorIndexOfGains = CursorUtil.getColumnIndexOrThrow(_cursor, "gains");
          final int _cursorIndexOfTotalPrincipalRepaid = CursorUtil.getColumnIndexOrThrow(_cursor, "totalPrincipalRepaid");
          final int _cursorIndexOfInterestRate = CursorUtil.getColumnIndexOrThrow(_cursor, "interestRate");
          final int _cursorIndexOfCouponRate = CursorUtil.getColumnIndexOrThrow(_cursor, "couponRate");
          final int _cursorIndexOfPayoutFrequency = CursorUtil.getColumnIndexOrThrow(_cursor, "payoutFrequency");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfOrderDate = CursorUtil.getColumnIndexOrThrow(_cursor, "orderDate");
          final int _cursorIndexOfMaturityDate = CursorUtil.getColumnIndexOrThrow(_cursor, "maturityDate");
          final int _cursorIndexOfTenureMonths = CursorUtil.getColumnIndexOrThrow(_cursor, "tenureMonths");
          final int _cursorIndexOfInterestPaid = CursorUtil.getColumnIndexOrThrow(_cursor, "interestPaid");
          final int _cursorIndexOfNextPayoutDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextPayoutDate");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final BondEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpInvestmentId;
            _tmpInvestmentId = _cursor.getString(_cursorIndexOfInvestmentId);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpPlatform;
            _tmpPlatform = _cursor.getString(_cursorIndexOfPlatform);
            final String _tmpInvestor;
            _tmpInvestor = _cursor.getString(_cursorIndexOfInvestor);
            final String _tmpCompanyName;
            _tmpCompanyName = _cursor.getString(_cursorIndexOfCompanyName);
            final String _tmpBondCategory;
            _tmpBondCategory = _cursor.getString(_cursorIndexOfBondCategory);
            final String _tmpBondTypeJson;
            _tmpBondTypeJson = _cursor.getString(_cursorIndexOfBondTypeJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final double _tmpInvestmentAmount;
            _tmpInvestmentAmount = _cursor.getDouble(_cursorIndexOfInvestmentAmount);
            final double _tmpFaceValuePerUnit;
            _tmpFaceValuePerUnit = _cursor.getDouble(_cursorIndexOfFaceValuePerUnit);
            final int _tmpUnits;
            _tmpUnits = _cursor.getInt(_cursorIndexOfUnits);
            final Double _tmpCurrentValue;
            if (_cursor.isNull(_cursorIndexOfCurrentValue)) {
              _tmpCurrentValue = null;
            } else {
              _tmpCurrentValue = _cursor.getDouble(_cursorIndexOfCurrentValue);
            }
            final double _tmpOutstandingPrincipal;
            _tmpOutstandingPrincipal = _cursor.getDouble(_cursorIndexOfOutstandingPrincipal);
            final double _tmpReturnsReceived;
            _tmpReturnsReceived = _cursor.getDouble(_cursorIndexOfReturnsReceived);
            final Double _tmpGains;
            if (_cursor.isNull(_cursorIndexOfGains)) {
              _tmpGains = null;
            } else {
              _tmpGains = _cursor.getDouble(_cursorIndexOfGains);
            }
            final double _tmpTotalPrincipalRepaid;
            _tmpTotalPrincipalRepaid = _cursor.getDouble(_cursorIndexOfTotalPrincipalRepaid);
            final double _tmpInterestRate;
            _tmpInterestRate = _cursor.getDouble(_cursorIndexOfInterestRate);
            final double _tmpCouponRate;
            _tmpCouponRate = _cursor.getDouble(_cursorIndexOfCouponRate);
            final String _tmpPayoutFrequency;
            _tmpPayoutFrequency = _cursor.getString(_cursorIndexOfPayoutFrequency);
            final String _tmpStartDate;
            _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            final String _tmpOrderDate;
            _tmpOrderDate = _cursor.getString(_cursorIndexOfOrderDate);
            final String _tmpMaturityDate;
            _tmpMaturityDate = _cursor.getString(_cursorIndexOfMaturityDate);
            final int _tmpTenureMonths;
            _tmpTenureMonths = _cursor.getInt(_cursorIndexOfTenureMonths);
            final double _tmpInterestPaid;
            _tmpInterestPaid = _cursor.getDouble(_cursorIndexOfInterestPaid);
            final String _tmpNextPayoutDate;
            if (_cursor.isNull(_cursorIndexOfNextPayoutDate)) {
              _tmpNextPayoutDate = null;
            } else {
              _tmpNextPayoutDate = _cursor.getString(_cursorIndexOfNextPayoutDate);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _result = new BondEntity(_tmpInvestmentId,_tmpCreatedAt,_tmpPlatform,_tmpInvestor,_tmpCompanyName,_tmpBondCategory,_tmpBondTypeJson,_tmpStatus,_tmpCurrency,_tmpInvestmentAmount,_tmpFaceValuePerUnit,_tmpUnits,_tmpCurrentValue,_tmpOutstandingPrincipal,_tmpReturnsReceived,_tmpGains,_tmpTotalPrincipalRepaid,_tmpInterestRate,_tmpCouponRate,_tmpPayoutFrequency,_tmpStartDate,_tmpOrderDate,_tmpMaturityDate,_tmpTenureMonths,_tmpInterestPaid,_tmpNextPayoutDate,_tmpNotes);
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

  private void __fetchRelationshippayoutsAsappLeBondstrackerDataLocalEntityPayoutEntity(
      @NonNull final ArrayMap<String, ArrayList<PayoutEntity>> _map) {
    final Set<String> __mapKeySet = _map.keySet();
    if (__mapKeySet.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchArrayMap(_map, true, (map) -> {
        __fetchRelationshippayoutsAsappLeBondstrackerDataLocalEntityPayoutEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `payoutId`,`bondId`,`date`,`payoutType`,`amount`,`principalComponent`,`interestComponent`,`status` FROM `payouts` WHERE `bondId` IN (");
    final int _inputSize = __mapKeySet.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (String _item : __mapKeySet) {
      _stmt.bindString(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "bondId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfPayoutId = 0;
      final int _cursorIndexOfBondId = 1;
      final int _cursorIndexOfDate = 2;
      final int _cursorIndexOfPayoutType = 3;
      final int _cursorIndexOfAmount = 4;
      final int _cursorIndexOfPrincipalComponent = 5;
      final int _cursorIndexOfInterestComponent = 6;
      final int _cursorIndexOfStatus = 7;
      while (_cursor.moveToNext()) {
        final String _tmpKey;
        _tmpKey = _cursor.getString(_itemKeyIndex);
        final ArrayList<PayoutEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final PayoutEntity _item_1;
          final String _tmpPayoutId;
          _tmpPayoutId = _cursor.getString(_cursorIndexOfPayoutId);
          final String _tmpBondId;
          _tmpBondId = _cursor.getString(_cursorIndexOfBondId);
          final String _tmpDate;
          _tmpDate = _cursor.getString(_cursorIndexOfDate);
          final String _tmpPayoutType;
          _tmpPayoutType = _cursor.getString(_cursorIndexOfPayoutType);
          final double _tmpAmount;
          _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
          final double _tmpPrincipalComponent;
          _tmpPrincipalComponent = _cursor.getDouble(_cursorIndexOfPrincipalComponent);
          final double _tmpInterestComponent;
          _tmpInterestComponent = _cursor.getDouble(_cursorIndexOfInterestComponent);
          final String _tmpStatus;
          _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
          _item_1 = new PayoutEntity(_tmpPayoutId,_tmpBondId,_tmpDate,_tmpPayoutType,_tmpAmount,_tmpPrincipalComponent,_tmpInterestComponent,_tmpStatus);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
