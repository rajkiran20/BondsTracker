package app.le.bondstracker.notification;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class BondReminderWorker_Factory {
  public BondReminderWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams);
  }

  public static BondReminderWorker_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static BondReminderWorker newInstance(Context context, WorkerParameters workerParams) {
    return new BondReminderWorker(context, workerParams);
  }

  private static final class InstanceHolder {
    private static final BondReminderWorker_Factory INSTANCE = new BondReminderWorker_Factory();
  }
}
