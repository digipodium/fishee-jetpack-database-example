package com.example.fishee.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val password: String = "",
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Fish(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val weight: Float = 0f,
    val height: Float = 0f,
    val length: Float = 0f,
    val image: String = "",
    val description: String = "",
    val river: String = "",
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "user_id")
    val userId: Int
)