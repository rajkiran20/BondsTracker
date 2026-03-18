package app.le.bondstracker.ui.addbond;

import app.le.bondstracker.data.repository.BondRepository;
import app.le.bondstracker.notification.NotificationScheduler;
import com.google.gson.Gson;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AddBondViewModel_Factory implements Factory<AddBondViewModel> {
  private final Provider<BondRepository> repositoryProvider;

  private final Provider<NotificationScheduler> notificationSchedulerProvider;

  private final Provider<Gson> gsonProvider;

  public AddBondViewModel_Factory(Provider<BondRepository> repositoryProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider, Provider<Gson> gsonProvider) {
    this.repositoryProvider = repositoryProvider;
    this.notificationSchedulerProvider = notificationSchedulerProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public AddBondViewModel get() {
    return newInstance(repositoryProvider.get(), notificationSchedulerProvider.get(), gsonProvider.get());
  }

  public static AddBondViewModel_Factory create(Provider<BondRepository> repositoryProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider, Provider<Gson> gsonProvider) {
    return new AddBondViewModel_Factory(repositoryProvider, notificationSchedulerProvider, gsonProvider);
  }

  public static AddBondViewModel newInstance(BondRepository repository,
      NotificationScheduler notificationScheduler, Gson gson) {
    return new AddBondViewModel(repository, notificationScheduler, gson);
  }
}
