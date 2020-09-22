/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.laanelitt.laanelittapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents one night's sleep through start, end times, and the sleep quality.
 */
@Entity(tableName = "items_table")
data class Items(
        @PrimaryKey(autoGenerate = true)
        var itemId: Int,

        @ColumnInfo(name = "name")
        val name: String?
)

@Entity(tableName = "cat_table")
data class Category(
        @PrimaryKey(autoGenerate = true)
        var catId: Int,

        @ColumnInfo(name = "category")
        val category: String?
)