package com.example.eatzy_seller.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.eatzy_seller.data.model.User

@Dao
interface UserDao { @Insert suspend fun insertUser(user: User)
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("UPDATE users SET isVerified = 1 WHERE email = :email")
    suspend fun verifyUser(email: String)
}