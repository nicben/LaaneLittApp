package com.laanelitt.laanelittapp.objects

import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.util.*

data class Loan(
    val id: Int?,
    val assetId: Int?,
    val users: User?,
    val startDate: String,
    val endDate: String,
    val statusLoan: RequestStatus
)