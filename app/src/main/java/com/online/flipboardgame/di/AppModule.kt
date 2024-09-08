package com.online.flipboardgame.di

import com.online.flipboardgame.usecase.LargestRectangleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLargestRectangleUseCase(): LargestRectangleUseCase {
        return LargestRectangleUseCase()
    }
}
