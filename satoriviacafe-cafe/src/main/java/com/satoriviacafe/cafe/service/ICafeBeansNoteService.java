package com.satoriviacafe.cafe.service;

import com.satoriviacafe.cafe.domain.CafeBeansNote;

import java.util.List;

/**
 * 豆子笔记Service接口
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
public interface ICafeBeansNoteService {

    /**
     * 查询豆子笔记
     *
     * @param noteId 豆子笔记主键
     * @return 豆子笔记
     */
    CafeBeansNote selectCafeBeansNoteByNoteId(Long noteId);

    /**
     * 查询豆子笔记列表
     *
     * @param cafeBeansNote 豆子笔记
     * @return 豆子笔记集合
     */
    List<CafeBeansNote> selectCafeBeansNoteList(CafeBeansNote cafeBeansNote);

    /**
     * 新增豆子笔记
     *
     * @param cafeBeansNote 豆子笔记
     * @return 结果
     */
    int insertCafeBeansNote(CafeBeansNote cafeBeansNote);

    /**
     * 批量新增豆子笔记
     *
     * @param cafeBeansNotes 豆子笔记
     * @param ignorePk       是否忽略主键
     * @return 结果
     */
    int insertBatchCafeBeansNote(List<CafeBeansNote> cafeBeansNotes, boolean ignorePk);

    /**
     * 批量新增豆子笔记
     *
     * @param cafeBeansNotes 豆子笔记
     * @return 结果
     */
    default int insertBatchCafeBeansNote(List<CafeBeansNote> cafeBeansNotes) {
        return insertBatchCafeBeansNote(cafeBeansNotes, false);
    }


    /**
     * 修改豆子笔记
     *
     * @param cafeBeansNote 豆子笔记
     * @return 结果
     */
    int updateCafeBeansNote(CafeBeansNote cafeBeansNote);

    /**
     * 批量修改豆子笔记
     *
     * @param cafeBeansNotes 豆子笔记
     * @return 结果
     */
    int updateBatchCafeBeansNote(List<CafeBeansNote> cafeBeansNotes);

    /**
     * 批量删除豆子笔记
     *
     * @param noteIds 需要删除的豆子笔记主键集合
     * @return 结果
     */
    int deleteCafeBeansNoteByNoteIds(Long[] noteIds);

    /**
     * 删除豆子笔记信息
     *
     * @param noteId 豆子笔记主键
     * @return 结果
     */
    int deleteCafeBeansNoteByNoteId(Long noteId);
}
