package com.satoriviacafe.cafe.domain

import com.alipay.mychain.taas.api.response.h5.TpCodeInfoResp


data class ProductDetail(
    private val displayData: CafeProduction,
    private var blockchainData: TpCodeInfoResp,
)
