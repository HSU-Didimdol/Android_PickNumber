package com.example.picknumberproject.data.di.annotation

import javax.inject.Qualifier

/**
 * 레트로핏을 하나의 인스턴스로 정의하여 어노테이션으로 하였습니다. 어노테이션에 대해서는 저도 잘 아직은 모르지만,
 * 이렇게 하였을 때 모듈 파일에 침투하여 하나의 객체로 표기할 수 있습니다.
 */

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class Directions5RetrofitInstance
