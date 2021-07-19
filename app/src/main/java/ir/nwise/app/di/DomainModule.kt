package ir.nwise.app.di

import ir.nwise.app.data.DefaultDispatcherProvider
import ir.nwise.app.data.repository.AppRepositoryImp
import ir.nwise.app.domain.AppRepository
import ir.nwise.app.domain.usecase.GetPhotoResultUseCase
import ir.nwise.app.networking.ApiService
import org.koin.dsl.module

val domainModule = module {
    fun provideRepository(api: ApiService): AppRepository {
        return AppRepositoryImp(api)
    }

    factory {
        GetPhotoResultUseCase(
            appRepository = get(),
            dispatchers = DefaultDispatcherProvider()
        )
    }

    single { provideRepository(get()) }
}