package com.capstone.reseepe.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface RecentlyViewedRecipeDao {
    @get:Query("SELECT * FROM recently_viewed_recipes ORDER BY timestamp DESC LIMIT 5")
    val getRecentlyViewedRecipes: LiveData<List<RecentlyViewedRecipe?>?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: RecentlyViewedRecipe?)

    @Query("DELETE FROM recently_viewed_recipes WHERE id NOT IN (SELECT id FROM recently_viewed_recipes ORDER BY timestamp DESC LIMIT 5)")
    fun deleteOldRecipes()

}


