package com.satoriviacafe.cafe.controller;

import com.satoriviacafe.cafe.domain.CafeBeansNote;
import com.satoriviacafe.cafe.service.ICafeBeansNoteService;
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
 * 豆子笔记Controller
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/cafe/note")
@RequiredArgsConstructor
public class CafeBeansNoteController extends BaseController {

    private final ICafeBeansNoteService cafeBeansNoteService;

    /**
     * 查询豆子笔记列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:note:list')")
    @GetMapping("/list")
    public TableDataInfo list(CafeBeansNote cafeBeansNote) {
        startPage();
        List<CafeBeansNote> list = cafeBeansNoteService.selectCafeBeansNoteList(cafeBeansNote);
        return getDataTable(list);
    }

    /**
     * 导出豆子笔记列表
     */
    @PreAuthorize("@ss.hasPermi('cafe:note:export')")
    @Log(title = "豆子笔记", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CafeBeansNote cafeBeansNote) {
        List<CafeBeansNote> list = cafeBeansNoteService.selectCafeBeansNoteList(cafeBeansNote);
        ExcelUtil<CafeBeansNote> util = new ExcelUtil<>(CafeBeansNote.class);
        util.exportExcel(response, list, "豆子笔记数据");
    }

    /**
     * 获取豆子笔记详细信息
     */
    @PreAuthorize("@ss.hasPermi('cafe:note:query')")
    @GetMapping(value = "/{noteId}")
    public AjaxResult getInfo(@PathVariable("noteId") Long noteId) {
        return success(cafeBeansNoteService.selectCafeBeansNoteByNoteId(noteId));
    }

    /**
     * 新增豆子笔记
     */
    @PreAuthorize("@ss.hasPermi('cafe:note:add')")
    @Log(title = "豆子笔记", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CafeBeansNote cafeBeansNote) {
        return toAjax(cafeBeansNoteService.insertCafeBeansNote(cafeBeansNote));
    }


    /**
     * 新增豆子笔记
     */
    @PreAuthorize("@ss.hasPermi('cafe:note:addBatch')")
    @Log(title = "豆子笔记", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public AjaxResult addBatch(@RequestBody List<CafeBeansNote> cafeBeansNotes) {
        return toAjax(cafeBeansNoteService.insertBatchCafeBeansNote(cafeBeansNotes));
    }


    /**
     * 修改豆子笔记
     */
    @PreAuthorize("@ss.hasPermi('cafe:note:edit')")
    @Log(title = "豆子笔记", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CafeBeansNote cafeBeansNote) {
        return toAjax(cafeBeansNoteService.updateCafeBeansNote(cafeBeansNote));
    }

    /**
     * 修改豆子笔记
     */
    @PreAuthorize("@ss.hasPermi('cafe:note:editBatch')")
    @Log(title = "豆子笔记", businessType = BusinessType.UPDATE)
    @PutMapping("/batch")
    public AjaxResult editBatch(@RequestBody List<CafeBeansNote> cafeBeansNotes) {
        return toAjax(cafeBeansNoteService.updateBatchCafeBeansNote(cafeBeansNotes));
    }

    /**
     * 删除豆子笔记
     */
    @PreAuthorize("@ss.hasPermi('cafe:note:remove')")
    @Log(title = "豆子笔记", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noteIds}")
    public AjaxResult remove(@PathVariable Long[] noteIds) {
        return toAjax(cafeBeansNoteService.deleteCafeBeansNoteByNoteIds(noteIds));
    }

    /**
     * 删除豆子笔记根據id
     */
    @PreAuthorize("@ss.hasPermi('cafe:note:remove')")
    @Log(title = "豆子笔记", businessType = BusinessType.DELETE)
    @DeleteMapping("/byId/{noteId}")
    public AjaxResult remove(@PathVariable Long noteId) {
        return toAjax(cafeBeansNoteService.deleteCafeBeansNoteByNoteId(noteId));
    }
}
