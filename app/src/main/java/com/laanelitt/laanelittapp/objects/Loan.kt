package com.laanelitt.laanelittapp.objects

import java.util.*

data class Loan(
    val id: Int?,
    val assetId: Int?,
    val users: AssetOwner?,
    val startDate: String,
    val endDate: String,
    val statusLoan: RequestStatus
)