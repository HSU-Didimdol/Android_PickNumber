package com.example.picknumberproject.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.picknumberproject.domain.model.CompanyEntity

/**
 * company_table에서 담기는 Dao를 담고있는 interface입니다.
 */

@Dao
interface CompanyDao {
    @Query("SELECT * FROM company_table")
    fun getAll(): List<CompanyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 같은 것이 충돌시에 새로운 것으로 대체한다.
    suspend fun insertAll(companys: List<CompanyEntity>)

    @Query("delete from company_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM company_table WHERE searchQuery LIKE :query")
    suspend fun searchQuery(query: String): List<CompanyEntity>

    @Query("SELECT * FROM company_table WHERE companyID LIKE :query")
    suspend fun searchValidCode(query: String): CompanyEntity
}