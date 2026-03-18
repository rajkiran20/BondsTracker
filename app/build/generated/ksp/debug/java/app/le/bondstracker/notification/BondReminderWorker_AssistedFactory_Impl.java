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
public final class BondReminderWorker_AssistedFactory_Impl implements BondReminderWorker_AssistedFactory {
  private final BondReminderWorker_Factory delegateFactory;

  BondReminderWorker_AssistedFactory_Impl(BondReminderWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public BondReminderWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<BondReminderWorker_AssistedFactory> create(
      BondReminderWorker_Factory delegateFactory) {
    return InstanceFactory.create(new BondReminderWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<BondReminderWorker_AssistedFactory> createFactoryProvider(
      BondReminderWorker_Factory delegateFactory) {
    return InstanceFactory.create(new BondReminderWorker_AssistedFactory_Impl(delegateFactory));
  }
}
