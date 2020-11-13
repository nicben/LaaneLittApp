package com.laanelitt.laanelittapp.objects

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.util.*

@JsonClass(generateAdapter = true)
data class Loan(
    val id: Int?,
    val assetId: Int?,
    val users: User?,
    val startDate: Date?,
    val endDate: Date?,
    val statusLoan: RequestStatus?
): Serializable