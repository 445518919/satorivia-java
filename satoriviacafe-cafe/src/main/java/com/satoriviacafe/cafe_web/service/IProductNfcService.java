package com.satoriviacafe.cafe_web.service;

import com.satoriviacafe.cafe.domain.CafeProductNfc;
import com.satoriviacafe.cafe.service.ICafeProductNfcService;

import java.util.List;

public interface IProductNfcService extends ICafeProductNfcService {

    default String importCafeProductNfc(List<CafeProductNfc> userList, boolean updateSupport, String operName) {
        return "NOT IMPLEMENTED";
    }

}
