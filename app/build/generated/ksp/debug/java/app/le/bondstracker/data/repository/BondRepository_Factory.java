package app.le.bondstracker.data.repository;

import app.le.bondstracker.data.local.dao.BondDao;
import com.google.gson.Gson;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class BondRepository_Factory implements Factory<BondRepository> {
  private final Provider<BondDao> bondDaoProvider;

  private final Provider<Gson> gsonProvider;

  public BondRepository_Factory(Provider<BondDao> bondDaoProvider, Provider<Gson> gsonProvider) {
    this.bondDaoProvider = bondDaoProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public BondRepository get() {
    return newInstance(bondDaoProvider.get(), gsonProvider.get());
  }

  public static BondRepository_Factory create(Provider<BondDao> bondDaoProvider,
      Provider<Gson> gsonProvider) {
    return new BondRepository_Factory(bondDaoProvider, gsonProvider);
  }

  public static BondRepository newInstance(BondDao bondDao, Gson gson) {
    return new BondRepository(bondDao, gson);
  }
}
