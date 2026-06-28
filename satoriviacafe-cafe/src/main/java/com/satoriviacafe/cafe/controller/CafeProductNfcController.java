package com.satoriviacafe.cafe.controller;

import com.satoriviacafe.cafe.domain.CafeProductNfc;
import com.satoriviacafe.cafe.service.ICafeProductNfcService;
import com.satoriviacafe.common.annotation.Log;
import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.AjaxResult;
import com.satoriviacafe.common.core.page.TableDataInfo;
import com.satoriviacafe.common.enums.BusinessType;
import com.satoriviacafe.common.utils.poi.ExcelUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品NFCController
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/cafe/nfc")
@RequiredArgsConstructor
public class CafeProductNfcController extends BaseController {

    private final ICafeProductNfcService cafeProductNfcService;

    /**
     * 查询产品NFC列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:nfc:list')")
    @GetMapping("/list")
    public TableDataInfo list(CafeProductNfc cafeProductNfc) {
        startPage();
        List<CafeProductNfc> list = cafeProductNfcService.selectCafeProductNfcList(cafeProductNfc);
        return getDataTable(list);
    }

    /**
     * 导出产品NFC列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:nfc:export')")
    @Log(title = "产品NFC", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CafeProductNfc cafeProductNfc) {
        List<CafeProductNfc> list = cafeProductNfcService.selectCafeProductNfcList(cafeProductNfc);
        ExcelUtil<CafeProductNfc> util = new ExcelUtil<>(CafeProductNfc.class);
        util.exportExcel(response, list, "产品NFC数据");
    }

    /**
     * 获取产品NFC详细信息
     */
    @PreAuthorize("@ss.hasPermi('cafe:nfc:query')")
    @GetMapping(value = "/{nfcId}")
    public AjaxResult getInfo(@PathVariable("nfcId") Long nfcId) {
        return success(cafeProductNfcService.selectCafeProductNfcByNfcId(nfcId));
    }

    /**
     * 新增产品NFC
     */
    @PreAuthorize("@ss.hasPermi('cafe:nfc:add')")
    @Log(title = "产品NFC", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CafeProductNfc cafeProductNfc) {
        return toAjax(cafeProductNfcService.insertCafeProductNfc(cafeProductNfc));
    }


    /**
     * 新增产品NFC
     */
    @PreAuthorize("@ss.hasPermi('cafe:nfc:addBatch')")
    @Log(title = "产品NFC", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult addBatch(@RequestBody List<CafeProductNfc> cafeProductNfcs) {
        return toAjax(cafeProductNfcService.insertBatchCafeProductNfc(cafeProductNfcs));
    }


    /**
     * 修改产品NFC
     */
    @PreAuthorize("@ss.hasPermi('cafe:nfc:edit')")
    @Log(title = "产品NFC", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CafeProductNfc cafeProductNfc) {
        return toAjax(cafeProductNfcService.updateCafeProductNfc(cafeProductNfc));
    }

    /**
     * 修改产品NFC
     */
    @PreAuthorize("@ss.hasPermi('cafe:nfc:editBatch')")
    @Log(title = "产品NFC", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public AjaxResult editBatch(@RequestBody List<CafeProductNfc> cafeProductNfcs) {
        return toAjax(cafeProductNfcService.updateBatchCafeProductNfc(cafeProductNfcs));
    }

    /**
     * 删除产品NFC
     */
    @PreAuthorize("@ss.hasPermi('cafe:nfc:remove')")
    @Log(title = "产品NFC", businessType = BusinessType.DELETE)
    @DeleteMapping("/{nfcIds}")
    public AjaxResult remove(@PathVariable Long[] nfcIds) {
        return toAjax(cafeProductNfcService.deleteCafeProductNfcByNfcIds(nfcIds));
    }

    /**
     * 删除产品NFC根據id
     */
    @PreAuthorize("@ss.hasPermi('cafe:nfc:remove')")
    @Log(title = "产品NFC", businessType = BusinessType.DELETE)
    @DeleteMapping("/byId/{nfcId}")
    public AjaxResult remove(@PathVariable Long nfcId) {
        return toAjax(cafeProductNfcService.deleteCafeProductNfcByNfcId(nfcId));
    }
}
