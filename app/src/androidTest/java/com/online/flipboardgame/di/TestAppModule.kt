package com.online.flipboardgame.di

import com.online.flipboardgame.usecase.LargestRectangleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class] // This is the original prod Dagger module we're replacing
)
object TestAppModule {
    @Provides
    @Singleton
    fun provideLargestRectangleUseCase(): LargestRectangleUseCase {
        return LargestRectangleUseCase()
    }
}