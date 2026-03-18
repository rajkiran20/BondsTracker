package app.le.bondstracker.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import app.le.bondstracker.data.local.dao.BondDao;
import app.le.bondstracker.data.local.dao.BondDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile BondDao _bondDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `bonds` (`investmentId` TEXT NOT NULL, `createdAt` TEXT NOT NULL, `platform` TEXT NOT NULL, `investor` TEXT NOT NULL, `companyName` TEXT NOT NULL, `bondCategory` TEXT NOT NULL, `bondTypeJson` TEXT NOT NULL, `status` TEXT NOT NULL, `currency` TEXT NOT NULL, `investmentAmount` REAL NOT NULL, `faceValuePerUnit` REAL NOT NULL, `units` INTEGER NOT NULL, `currentValue` REAL, `outstandingPrincipal` REAL NOT NULL, `returnsReceived` REAL NOT NULL, `gains` REAL, `totalPrincipalRepaid` REAL NOT NULL, `interestRate` REAL NOT NULL, `couponRate` REAL NOT NULL, `payoutFrequency` TEXT NOT NULL, `startDate` TEXT NOT NULL, `orderDate` TEXT NOT NULL, `maturityDate` TEXT NOT NULL, `tenureMonths` INTEGER NOT NULL, `interestPaid` REAL NOT NULL, `nextPayoutDate` TEXT, `notes` TEXT, PRIMARY KEY(`investmentId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `payouts` (`payoutId` TEXT NOT NULL, `bondId` TEXT NOT NULL, `date` TEXT NOT NULL, `payoutType` TEXT NOT NULL, `amount` REAL NOT NULL, `principalComponent` REAL NOT NULL, `interestComponent` REAL NOT NULL, `status` TEXT NOT NULL, PRIMARY KEY(`payoutId`), FOREIGN KEY(`bondId`) REFERENCES `bonds`(`investmentId`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_payouts_bondId` ON `payouts` (`bondId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'e94546fdfb9475aa0773e9d0808b0702')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `bonds`");
        db.execSQL("DROP TABLE IF EXISTS `payouts`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsBonds = new HashMap<String, TableInfo.Column>(27);
        _columnsBonds.put("investmentId", new TableInfo.Column("investmentId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("platform", new TableInfo.Column("platform", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("investor", new TableInfo.Column("investor", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("companyName", new TableInfo.Column("companyName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("bondCategory", new TableInfo.Column("bondCategory", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("bondTypeJson", new TableInfo.Column("bondTypeJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("currency", new TableInfo.Column("currency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("investmentAmount", new TableInfo.Column("investmentAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("faceValuePerUnit", new TableInfo.Column("faceValuePerUnit", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("units", new TableInfo.Column("units", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("currentValue", new TableInfo.Column("currentValue", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("outstandingPrincipal", new TableInfo.Column("outstandingPrincipal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("returnsReceived", new TableInfo.Column("returnsReceived", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("gains", new TableInfo.Column("gains", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("totalPrincipalRepaid", new TableInfo.Column("totalPrincipalRepaid", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("interestRate", new TableInfo.Column("interestRate", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("couponRate", new TableInfo.Column("couponRate", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("payoutFrequency", new TableInfo.Column("payoutFrequency", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("startDate", new TableInfo.Column("startDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("orderDate", new TableInfo.Column("orderDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("maturityDate", new TableInfo.Column("maturityDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("tenureMonths", new TableInfo.Column("tenureMonths", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("interestPaid", new TableInfo.Column("interestPaid", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("nextPayoutDate", new TableInfo.Column("nextPayoutDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBonds.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBonds = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBonds = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBonds = new TableInfo("bonds", _columnsBonds, _foreignKeysBonds, _indicesBonds);
        final TableInfo _existingBonds = TableInfo.read(db, "bonds");
        if (!_infoBonds.equals(_existingBonds)) {
          return new RoomOpenHelper.ValidationResult(false, "bonds(app.le.bondstracker.data.local.entity.BondEntity).\n"
                  + " Expected:\n" + _infoBonds + "\n"
                  + " Found:\n" + _existingBonds);
        }
        final HashMap<String, TableInfo.Column> _columnsPayouts = new HashMap<String, TableInfo.Column>(8);
        _columnsPayouts.put("payoutId", new TableInfo.Column("payoutId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayouts.put("bondId", new TableInfo.Column("bondId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayouts.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayouts.put("payoutType", new TableInfo.Column("payoutType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayouts.put("amount", new TableInfo.Column("amount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayouts.put("principalComponent", new TableInfo.Column("principalComponent", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayouts.put("interestComponent", new TableInfo.Column("interestComponent", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPayouts.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPayouts = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysPayouts.add(new TableInfo.ForeignKey("bonds", "CASCADE", "NO ACTION", Arrays.asList("bondId"), Arrays.asList("investmentId")));
        final HashSet<TableInfo.Index> _indicesPayouts = new HashSet<TableInfo.Index>(1);
        _indicesPayouts.add(new TableInfo.Index("index_payouts_bondId", false, Arrays.asList("bondId"), Arrays.asList("ASC")));
        final TableInfo _infoPayouts = new TableInfo("payouts", _columnsPayouts, _foreignKeysPayouts, _indicesPayouts);
        final TableInfo _existingPayouts = TableInfo.read(db, "payouts");
        if (!_infoPayouts.equals(_existingPayouts)) {
          return new RoomOpenHelper.ValidationResult(false, "payouts(app.le.bondstracker.data.local.entity.PayoutEntity).\n"
                  + " Expected:\n" + _infoPayouts + "\n"
                  + " Found:\n" + _existingPayouts);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "e94546fdfb9475aa0773e9d0808b0702", "c86deb70160a960a69fd7173c8c95af2");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "bonds","payouts");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `bonds`");
      _db.execSQL("DELETE FROM `payouts`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(BondDao.class, BondDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public BondDao bondDao() {
    if (_bondDao != null) {
      return _bondDao;
    } else {
      synchronized(this) {
        if(_bondDao == null) {
          _bondDao = new BondDao_Impl(this);
        }
        return _bondDao;
      }
    }
  }
}
