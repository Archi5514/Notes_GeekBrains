package com.example.notes_di_room_firebase.di

import com.example.notes_di_room_firebase.model.Repository
import com.example.notes_di_room_firebase.model.providers.FireStoreProvider
import com.example.notes_di_room_firebase.model.providers.RemoteDataProvider
import com.example.notes_di_room_firebase.viewmodel.MainViewModel
import com.example.notes_di_room_firebase.viewmodel.NoteViewModel
import com.example.notes_di_room_firebase.viewmodel.SplashViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) } bind RemoteDataProvider::class
    single { Repository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}