package com.dicoding.mysimplelogin

import org.koin.dsl.module

val storageModule = module { // make new module for `Koin`
    factory { // `factory` to make a new object
        SessionManager(get()) // `get()` to provide object that has been made inside module
    }
    factory {
        UserRepository(get())
    }
}