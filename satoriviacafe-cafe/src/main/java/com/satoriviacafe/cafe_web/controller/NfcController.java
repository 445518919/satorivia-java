package com.satoriviacafe.cafe_web.controller;

import com.satoriviacafe.cafe.domain.CafeProductNfc;
import com.satoriviacafe.cafe_web.domain.NfcImportDTO;
import com.satoriviacafe.cafe_web.service.IProductNfcService;
import com.satoriviacafe.common.annotation.Log;
import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.AjaxResult;
import com.satoriviacafe.common.enums.BusinessType;
import com.satoriviacafe.common.utils.poi.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 产品NFC管理
 *
 * @author shy
 * @since 2026年06月28日
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/cafe/nfc")
public class NfcController extends BaseController {

    private final IProductNfcService cafeProductNfcService;

    @PreAuthorize("@ss.hasPermi('cafe:nfc:import')")
    @Log(title = "产品NFC", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<CafeProductNfc> util = new ExcelUtil<>(CafeProductNfc.class);
        List<CafeProductNfc> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = cafeProductNfcService.importCafeProductNfc(userList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<NfcImportDTO> util = new ExcelUtil<>(NfcImportDTO.class);
        util.importTemplateExcel(response, "产品NFC数据");
    }
}
