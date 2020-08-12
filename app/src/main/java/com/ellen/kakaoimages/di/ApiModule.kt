package com.ellen.kakaoimages.di

import com.ellen.kakaoimages.network.ImageApi
import org.koin.dsl.module
import retrofit2.Retrofit

// 안드 대표적인 DI에는 koin과 dagger가있는데 둘의 차이점은 dagger는 generated code를 생성해준다는거지!
// 하지만 koin은 런타임때 리플렉션?? 기반으로 된다고 기억하고있어.
// 그 말은 혹여 세팅을 잘못해뒀으면 Dagger일 경우 빌드타임때 에러가 뜨고 말겠지만
// Koin은 빌드타임때는 잘 돌아가다가 실행시키면 죽는.. 어떻게보면 더 불안전한 특징을 가지고있어.
// 우리 팀에서는 처음에 Koin 써보려다가 이런 불안정성때문에 Dagger로 갈아탐!
val apiModule = module {

    fun provideImageApi(retrofit: Retrofit): ImageApi {
        return retrofit.create(ImageApi::class.java)
    }
    single { provideImageApi(get()) }

}