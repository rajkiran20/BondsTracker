package app.le.bondstracker.ui.addbond;

import app.le.bondstracker.data.repository.BondRepository;
import app.le.bondstracker.notification.NotificationScheduler;
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

  public AddBondViewModel_Factory(Provider<BondRepository> repositoryProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.notificationSchedulerProvider = notificationSchedulerProvider;
  }

  @Override
  public AddBondViewModel get() {
    return newInstance(repositoryProvider.get(), notificationSchedulerProvider.get());
  }

  public static AddBondViewModel_Factory create(Provider<BondRepository> repositoryProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    return new AddBondViewModel_Factory(repositoryProvider, notificationSchedulerProvider);
  }

  public static AddBondViewModel newInstance(BondRepository repository,
      NotificationScheduler notificationScheduler) {
    return new AddBondViewModel(repository, notificationScheduler);
  }
}
