package com.satoriviacafe.cafe.mapper;

import com.satoriviacafe.cafe.domain.CafeProductNfc;

import java.util.List;

/**
 * 产品NFCMapper接口
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
public interface CafeProductNfcMapper {
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
     * @return 结果
     */
    int insertBatchCafeProductNfc(List<CafeProductNfc> cafeProductNfcs);


    /**
     * 忽略主键批量新增产品NFC
     *
     * @param cafeProductNfcs 产品NFC
     * @return 结果
     */
    int insertBatchIgnoreCafeProductNfc(List<CafeProductNfc> cafeProductNfcs);

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
     * 删除产品NFC
     *
     * @param nfcId 产品NFC主键
     * @return 结果
     */
    int deleteCafeProductNfcByNfcId(Long nfcId);

    /**
     * 批量删除产品NFC
     *
     * @param nfcIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteCafeProductNfcByNfcIds(Long[] nfcIds);
}
