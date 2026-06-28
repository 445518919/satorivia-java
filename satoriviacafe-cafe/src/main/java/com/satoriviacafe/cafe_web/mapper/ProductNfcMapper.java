package com.satoriviacafe.cafe_web.mapper;

import com.satoriviacafe.cafe.domain.CafeProductNfc;
import com.satoriviacafe.cafe.mapper.CafeProductNfcMapper;
import org.springframework.context.annotation.Primary;

/**
 * 产品NFCMapper接口
 *
 * @author shy
 * @since 2026年06月28日
 */
@Primary
public interface ProductNfcMapper extends CafeProductNfcMapper {

    CafeProductNfc selectByNfcCode(String nfcCode);

}
