package app.le.bondstracker.notification;

import androidx.hilt.work.WorkerAssistedFactory;
import androidx.work.ListenableWorker;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.annotation.processing.Generated;

@Generated("androidx.hilt.AndroidXHiltProcessor")
@Module
@InstallIn(SingletonComponent.class)
@OriginatingElement(
    topLevelClass = RescheduleWorker.class
)
public interface RescheduleWorker_HiltModule {
  @Binds
  @IntoMap
  @StringKey("app.le.bondstracker.notification.RescheduleWorker")
  WorkerAssistedFactory<? extends ListenableWorker> bind(RescheduleWorker_AssistedFactory factory);
}
