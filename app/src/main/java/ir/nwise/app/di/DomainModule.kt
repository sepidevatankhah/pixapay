package ir.nwise.app.di

import android.content.Context
import ir.nwise.app.data.DefaultDispatcherProvider
import ir.nwise.app.data.repository.AppRepositoryImp
import ir.nwise.app.database.PhotoDao
import ir.nwise.app.domain.AppRepository
import ir.nwise.app.domain.usecase.GetAllCachedPhotoUseCase
import ir.nwise.app.domain.usecase.GetPhotoResultUseCase
import ir.nwise.app.domain.usecase.InsertPhotoUseCase
import ir.nwise.app.networking.ApiService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val domainModule = module {
    fun provideRepository(api: ApiService, dao: PhotoDao, context: Context): AppRepository {
        return AppRepositoryImp(api, dao, context)
    }

    factory {
        GetPhotoResultUseCase(
            appRepository = get(),
            dispatchers = DefaultDispatcherProvider()
        )
    }
    factory {
        GetAllCachedPhotoUseCase(
            appRepository = get(),
            dispatchers = DefaultDispatcherProvider()
        )
    }
    factory {
        InsertPhotoUseCase(
            appRepository = get(),
            dispatchers = DefaultDispatcherProvider()
        )
    }

    single { provideRepository(get(), get(), androidContext()) }
}