package com.satoriviacafe.cafe_web.controller;

import com.satoriviacafe.cafe.domain.CafeBeansNote;
import com.satoriviacafe.cafe.service.ICafeBeansNoteService;
import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.R;
import com.satoriviacafe.common.core.page.RT;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 豆子笔记端点
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@RestController
@RequestMapping("/v1/cafe/note")
@RequiredArgsConstructor
public class BeansNoteController extends BaseController {

    private final ICafeBeansNoteService cafeBeansNoteService;

    /**
     * 查询豆子笔记列表
     */
    @GetMapping("/list")
    public RT<CafeBeansNote> list(CafeBeansNote cafeBeansNote) {
        startPage();
        List<CafeBeansNote> list = cafeBeansNoteService.selectCafeBeansNoteList(cafeBeansNote);
        return RT.success(list);
    }

    /**
     * 获取豆子笔记详细信息
     */
    @GetMapping(value = "/{noteId}")
    public R<CafeBeansNote> getInfo(@PathVariable("noteId") Long noteId) {
        return R.ok(cafeBeansNoteService.selectCafeBeansNoteByNoteId(noteId));
    }
}
