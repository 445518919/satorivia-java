package com.satoriviacafe.cafe.service.impl;

import com.satoriviacafe.cafe.domain.CafeProductNfc;
import com.satoriviacafe.cafe.mapper.CafeProductNfcMapper;
import com.satoriviacafe.cafe.service.ICafeProductNfcService;
import com.satoriviacafe.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品NFCService业务层处理
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@Service
@RequiredArgsConstructor
public class CafeProductNfcServiceImpl implements ICafeProductNfcService {

    private final CafeProductNfcMapper cafeProductNfcMapper;

    /**
     * 查询产品NFC
     *
     * @param nfcId 产品NFC主键
     * @return 产品NFC
     */
    @Override
    public CafeProductNfc selectCafeProductNfcByNfcId(Long nfcId) {
        return cafeProductNfcMapper.selectCafeProductNfcByNfcId(nfcId);
    }

    /**
     * 查询产品NFC列表
     *
     * @param cafeProductNfc 产品NFC
     * @return 产品NFC
     */
    @Override
    public List<CafeProductNfc> selectCafeProductNfcList(CafeProductNfc cafeProductNfc) {
        return cafeProductNfcMapper.selectCafeProductNfcList(cafeProductNfc);
    }

    /**
     * 新增产品NFC
     *
     * @param cafeProductNfc 产品NFC
     * @return 结果
     */
    @Override
    public int insertCafeProductNfc(CafeProductNfc cafeProductNfc) {
        cafeProductNfc.setCreateTime(DateUtils.getNowDate());
        return cafeProductNfcMapper.insertCafeProductNfc(cafeProductNfc);
    }

    /**
     * 批量新增产品NFC
     *
     * @param cafeProductNfcs 产品NFC
     * @param ignorePk        是否忽略主键
     * @return 结果
     */
    @Override
    public int insertBatchCafeProductNfc(List<CafeProductNfc> cafeProductNfcs, boolean ignorePk) {
        for (CafeProductNfc cafeProductNfc : cafeProductNfcs) {
            cafeProductNfc.setCreateTime(DateUtils.getNowDate());
        }
        if (ignorePk) {
            return cafeProductNfcMapper.insertBatchIgnoreCafeProductNfc(cafeProductNfcs);
        }
        return cafeProductNfcMapper.insertBatchCafeProductNfc(cafeProductNfcs);
    }

    /**
     * 修改产品NFC
     *
     * @param cafeProductNfc 产品NFC
     * @return 结果
     */
    @Override
    public int updateCafeProductNfc(CafeProductNfc cafeProductNfc) {
        cafeProductNfc.setUpdateTime(DateUtils.getNowDate());
        return cafeProductNfcMapper.updateCafeProductNfc(cafeProductNfc);
    }

    /**
     * 批量修改产品NFC
     *
     * @param cafeProductNfcs 产品NFC
     * @return 结果
     */
    @Override
    public int updateBatchCafeProductNfc(List<CafeProductNfc> cafeProductNfcs) {
        for (CafeProductNfc cafeProductNfc : cafeProductNfcs) {
            cafeProductNfc.setUpdateTime(DateUtils.getNowDate());
        }
        return cafeProductNfcMapper.updateBatchCafeProductNfc(cafeProductNfcs);
    }

    /**
     * 批量删除产品NFC
     *
     * @param nfcIds 需要删除的产品NFC主键
     * @return 结果
     */
    @Override
    public int deleteCafeProductNfcByNfcIds(Long[] nfcIds) {
        return cafeProductNfcMapper.deleteCafeProductNfcByNfcIds(nfcIds);
    }

    /**
     * 删除产品NFC信息
     *
     * @param nfcId 产品NFC主键
     * @return 结果
     */
    @Override
    public int deleteCafeProductNfcByNfcId(Long nfcId) {
        return cafeProductNfcMapper.deleteCafeProductNfcByNfcId(nfcId);
    }
}
