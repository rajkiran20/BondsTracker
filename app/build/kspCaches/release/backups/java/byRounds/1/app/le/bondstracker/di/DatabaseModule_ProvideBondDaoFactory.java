package app.le.bondstracker.di;

import app.le.bondstracker.data.local.AppDatabase;
import app.le.bondstracker.data.local.dao.BondDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class DatabaseModule_ProvideBondDaoFactory implements Factory<BondDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideBondDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public BondDao get() {
    return provideBondDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideBondDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideBondDaoFactory(dbProvider);
  }

  public static BondDao provideBondDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideBondDao(db));
  }
}
