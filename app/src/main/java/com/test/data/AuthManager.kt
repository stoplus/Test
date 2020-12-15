package com.test.data

import io.reactivex.subjects.BehaviorSubject

class AuthManager{
    var isLoggedSubject = BehaviorSubject.create<Boolean>()
}