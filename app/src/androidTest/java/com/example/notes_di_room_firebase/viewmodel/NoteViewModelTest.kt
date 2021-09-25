package com.example.notes_di_room_firebase.viewmodel

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.platform.app.ActivityLifecycleTimeout
import androidx.test.rule.ActivityTestRule
import com.example.notes_di_room_firebase.R
import com.example.notes_di_room_firebase.di.appModule
import com.example.notes_di_room_firebase.di.mainModule
import com.example.notes_di_room_firebase.di.noteModule
import com.example.notes_di_room_firebase.di.splashModule
import com.example.notes_di_room_firebase.model.Note
import com.example.notes_di_room_firebase.model.Repository
import com.example.notes_di_room_firebase.view.NoteActivity
import com.example.notes_di_room_firebase.view.NoteViewState
import io.mockk.*
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class NoteViewModelTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(NoteActivity::class.java, true, false)

    private val viewModel: NoteViewModel = spyk(NoteViewModel(mockk<Repository>()))
    private val viewStateLiveData = MutableLiveData<NoteViewState>()
    private val testNote = Note("228", "title", "body")

    @Before
    fun setUp() {
        startKoin {
            modules()
        }

        loadKoinModules(module { viewModel { viewModel } })

        every { viewModel.getViewState() } returns viewStateLiveData
        every { viewModel.loadNote(any()) } just runs
        every { viewModel.saveChanges(any()) } just runs
        every { viewModel.deleteNote() } just runs

        activityTestRule.launchActivity(Intent().apply {
            putExtra("NoteActivity.extra.NOTE", testNote.id)
        })
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    // doesn`t work on API 30
    @Test
    fun should_show_color_picker() {
        onView(withId(R.id.palette)).perform(click())

        onView(withId(R.menu.menu_color)).check(matches(isCompletelyDisplayed()))
    }
}