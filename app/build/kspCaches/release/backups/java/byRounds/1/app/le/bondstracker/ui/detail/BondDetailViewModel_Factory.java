package app.le.bondstracker.ui.detail;

import androidx.lifecycle.SavedStateHandle;
import app.le.bondstracker.data.repository.BondRepository;
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
public final class BondDetailViewModel_Factory implements Factory<BondDetailViewModel> {
  private final Provider<BondRepository> repositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public BondDetailViewModel_Factory(Provider<BondRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public BondDetailViewModel get() {
    return newInstance(repositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static BondDetailViewModel_Factory create(Provider<BondRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new BondDetailViewModel_Factory(repositoryProvider, savedStateHandleProvider);
  }

  public static BondDetailViewModel newInstance(BondRepository repository,
      SavedStateHandle savedStateHandle) {
    return new BondDetailViewModel(repository, savedStateHandle);
  }
}
