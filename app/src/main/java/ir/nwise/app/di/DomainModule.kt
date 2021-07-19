package ir.nwise.app.di

import android.content.Context
import ir.nwise.app.common.NetworkManagerImp
import ir.nwise.app.data.DefaultDispatcherProvider
import ir.nwise.app.data.repository.AppRepositoryImp
import ir.nwise.app.domain.AppRepository
import ir.nwise.app.domain.NetworkManager
import ir.nwise.app.domain.usecase.GetPhotoResultUseCase
import ir.nwise.app.networking.ApiService
import org.koin.android.ext.koin.androidContext
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

    fun provideNetworkManger(context: Context): NetworkManager {
        return NetworkManagerImp(context)
    }

    single { provideNetworkManger(androidContext()) }

    factory { provideRepository(get()) }
}