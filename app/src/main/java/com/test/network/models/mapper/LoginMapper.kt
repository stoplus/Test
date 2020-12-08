package com.test.network.models.mapper

import com.test.network.models.domain.LoginResult
import com.test.network.models.domain.RegisterResult
import com.test.network.models.data.response.LoginResponse
import com.test.network.models.data.response.RegisterResponse

fun LoginResponse?.toDomain(): LoginResult = this?.let {
    LoginResult(
        success = it.success ?: false,
        token = it.token ?: "",
        message = it.message ?: ""
    )
} ?: LoginResult.emptyModel()

fun RegisterResponse?.toDomain(): RegisterResult = this?.let {
    RegisterResult(
        success = it.success ?: false,
        token = it.token ?: "",
        message = it.message ?: ""
    )
} ?: RegisterResult.emptyModel()