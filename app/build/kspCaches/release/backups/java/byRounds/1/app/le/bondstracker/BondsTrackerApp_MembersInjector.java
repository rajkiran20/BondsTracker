package app.le.bondstracker;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class BondsTrackerApp_MembersInjector implements MembersInjector<BondsTrackerApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public BondsTrackerApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<BondsTrackerApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new BondsTrackerApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(BondsTrackerApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("app.le.bondstracker.BondsTrackerApp.workerFactory")
  public static void injectWorkerFactory(BondsTrackerApp instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
