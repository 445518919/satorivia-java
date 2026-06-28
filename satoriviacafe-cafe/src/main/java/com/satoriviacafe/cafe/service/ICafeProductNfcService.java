package com.satoriviacafe.cafe.service;

import com.satoriviacafe.cafe.domain.CafeProductNfc;

import java.util.List;

/**
 * 产品NFCService接口
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
public interface ICafeProductNfcService {

    /**
     * 查询产品NFC
     *
     * @param nfcId 产品NFC主键
     * @return 产品NFC
     */
    CafeProductNfc selectCafeProductNfcByNfcId(Long nfcId);

    /**
     * 查询产品NFC列表
     *
     * @param cafeProductNfc 产品NFC
     * @return 产品NFC集合
     */
    List<CafeProductNfc> selectCafeProductNfcList(CafeProductNfc cafeProductNfc);

    /**
     * 新增产品NFC
     *
     * @param cafeProductNfc 产品NFC
     * @return 结果
     */
    int insertCafeProductNfc(CafeProductNfc cafeProductNfc);

    /**
     * 批量新增产品NFC
     *
     * @param cafeProductNfcs 产品NFC
     * @param ignorePk        是否忽略主键
     * @return 结果
     */
    int insertBatchCafeProductNfc(List<CafeProductNfc> cafeProductNfcs, boolean ignorePk);

    /**
     * 批量新增产品NFC
     *
     * @param cafeProductNfcs 产品NFC
     * @return 结果
     */
    default int insertBatchCafeProductNfc(List<CafeProductNfc> cafeProductNfcs) {
        return insertBatchCafeProductNfc(cafeProductNfcs, false);
    }


    /**
     * 修改产品NFC
     *
     * @param cafeProductNfc 产品NFC
     * @return 结果
     */
    int updateCafeProductNfc(CafeProductNfc cafeProductNfc);

    /**
     * 批量修改产品NFC
     *
     * @param cafeProductNfcs 产品NFC
     * @return 结果
     */
    int updateBatchCafeProductNfc(List<CafeProductNfc> cafeProductNfcs);

    /**
     * 批量删除产品NFC
     *
     * @param nfcIds 需要删除的产品NFC主键集合
     * @return 结果
     */
    int deleteCafeProductNfcByNfcIds(Long[] nfcIds);

    /**
     * 删除产品NFC信息
     *
     * @param nfcId 产品NFC主键
     * @return 结果
     */
    int deleteCafeProductNfcByNfcId(Long nfcId);
}
