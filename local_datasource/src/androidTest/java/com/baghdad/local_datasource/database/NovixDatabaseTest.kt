package com.baghdad.local_datasource.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.baghdad.local_datasource.roomDB.database.NovixDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class NovixDatabaseTest {
    private lateinit var db: NovixDatabase

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NovixDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun initializeAllDAOs_Successfully() {
        Assertions.assertNotNull(db.movieDao())
        Assertions.assertNotNull(db.tvShowDao())
        Assertions.assertNotNull(db.recentViewedDao())
        Assertions.assertNotNull(db.recentSearchDao())
        Assertions.assertNotNull(db.actorDao())
        Assertions.assertNotNull(db.genreDao())
        Assertions.assertNotNull(db.favoriteGenreDao())
        Assertions.assertNotNull(db.searchQueryDao())
        Assertions.assertNotNull(db.continueWatchingDao())
    }
}