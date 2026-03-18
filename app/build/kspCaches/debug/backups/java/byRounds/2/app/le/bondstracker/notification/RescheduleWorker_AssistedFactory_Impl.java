package app.le.bondstracker.notification;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class RescheduleWorker_AssistedFactory_Impl implements RescheduleWorker_AssistedFactory {
  private final RescheduleWorker_Factory delegateFactory;

  RescheduleWorker_AssistedFactory_Impl(RescheduleWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public RescheduleWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<RescheduleWorker_AssistedFactory> create(
      RescheduleWorker_Factory delegateFactory) {
    return InstanceFactory.create(new RescheduleWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<RescheduleWorker_AssistedFactory> createFactoryProvider(
      RescheduleWorker_Factory delegateFactory) {
    return InstanceFactory.create(new RescheduleWorker_AssistedFactory_Impl(delegateFactory));
  }
}
