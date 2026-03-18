package app.le.bondstracker.notification;

import android.content.Context;
import androidx.work.WorkerParameters;
import app.le.bondstracker.data.repository.BondRepository;
import dagger.internal.DaggerGenerated;
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
public final class RescheduleWorker_Factory {
  private final Provider<BondRepository> repositoryProvider;

  private final Provider<NotificationScheduler> notificationSchedulerProvider;

  public RescheduleWorker_Factory(Provider<BondRepository> repositoryProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.notificationSchedulerProvider = notificationSchedulerProvider;
  }

  public RescheduleWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, repositoryProvider.get(), notificationSchedulerProvider.get());
  }

  public static RescheduleWorker_Factory create(Provider<BondRepository> repositoryProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    return new RescheduleWorker_Factory(repositoryProvider, notificationSchedulerProvider);
  }

  public static RescheduleWorker newInstance(Context context, WorkerParameters workerParams,
      BondRepository repository, NotificationScheduler notificationScheduler) {
    return new RescheduleWorker(context, workerParams, repository, notificationScheduler);
  }
}
