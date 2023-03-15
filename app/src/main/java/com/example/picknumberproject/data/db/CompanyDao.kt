package com.example.picknumberproject.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.picknumberproject.domain.model.CompanyEntity

@Dao
interface CompanyDao {
    @Query("SELECT * FROM company_table")
    fun getAll(): List<CompanyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 같은 것이 충돌시에 새로운 것으로 대체한다.
    suspend fun insertAll(companys: List<CompanyEntity>)

    @Query("delete from company_table")
    suspend fun deleteAll()
}