package com.satoriviacafe.cafe.service.impl;

import com.satoriviacafe.cafe.domain.CafeBeansNote;
import com.satoriviacafe.cafe.mapper.CafeBeansNoteMapper;
import com.satoriviacafe.cafe.service.ICafeBeansNoteService;
import com.satoriviacafe.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 豆子笔记Service业务层处理
 *
 * @author satoriviacafe
 * @since 2026-06-28
 */
@Service
@RequiredArgsConstructor
public class CafeBeansNoteServiceImpl implements ICafeBeansNoteService {

    private final CafeBeansNoteMapper cafeBeansNoteMapper;

    /**
     * 查询豆子笔记
     *
     * @param noteId 豆子笔记主键
     * @return 豆子笔记
     */
    @Override
    public CafeBeansNote selectCafeBeansNoteByNoteId(Long noteId) {
        return cafeBeansNoteMapper.selectCafeBeansNoteByNoteId(noteId);
    }

    /**
     * 查询豆子笔记列表
     *
     * @param cafeBeansNote 豆子笔记
     * @return 豆子笔记
     */
    @Override
    public List<CafeBeansNote> selectCafeBeansNoteList(CafeBeansNote cafeBeansNote) {
        return cafeBeansNoteMapper.selectCafeBeansNoteList(cafeBeansNote);
    }

    /**
     * 新增豆子笔记
     *
     * @param cafeBeansNote 豆子笔记
     * @return 结果
     */
    @Override
    public int insertCafeBeansNote(CafeBeansNote cafeBeansNote) {
        cafeBeansNote.setCreateTime(DateUtils.getNowDate());
        return cafeBeansNoteMapper.insertCafeBeansNote(cafeBeansNote);
    }

    /**
     * 批量新增豆子笔记
     *
     * @param cafeBeansNotes 豆子笔记
     * @param ignorePk       是否忽略主键
     * @return 结果
     */
    @Override
    public int insertBatchCafeBeansNote(List<CafeBeansNote> cafeBeansNotes, boolean ignorePk) {
        for (CafeBeansNote cafeBeansNote : cafeBeansNotes) {
            cafeBeansNote.setCreateTime(DateUtils.getNowDate());
        }
        if (ignorePk) {
            return cafeBeansNoteMapper.insertBatchIgnoreCafeBeansNote(cafeBeansNotes);
        }
        return cafeBeansNoteMapper.insertBatchCafeBeansNote(cafeBeansNotes);
    }

    /**
     * 修改豆子笔记
     *
     * @param cafeBeansNote 豆子笔记
     * @return 结果
     */
    @Override
    public int updateCafeBeansNote(CafeBeansNote cafeBeansNote) {
        cafeBeansNote.setUpdateTime(DateUtils.getNowDate());
        return cafeBeansNoteMapper.updateCafeBeansNote(cafeBeansNote);
    }

    /**
     * 批量修改豆子笔记
     *
     * @param cafeBeansNotes 豆子笔记
     * @return 结果
     */
    @Override
    public int updateBatchCafeBeansNote(List<CafeBeansNote> cafeBeansNotes) {
        for (CafeBeansNote cafeBeansNote : cafeBeansNotes) {
            cafeBeansNote.setUpdateTime(DateUtils.getNowDate());
        }
        return cafeBeansNoteMapper.updateBatchCafeBeansNote(cafeBeansNotes);
    }

    /**
     * 批量删除豆子笔记
     *
     * @param noteIds 需要删除的豆子笔记主键
     * @return 结果
     */
    @Override
    public int deleteCafeBeansNoteByNoteIds(Long[] noteIds) {
        return cafeBeansNoteMapper.deleteCafeBeansNoteByNoteIds(noteIds);
    }

    /**
     * 删除豆子笔记信息
     *
     * @param noteId 豆子笔记主键
     * @return 结果
     */
    @Override
    public int deleteCafeBeansNoteByNoteId(Long noteId) {
        return cafeBeansNoteMapper.deleteCafeBeansNoteByNoteId(noteId);
    }
}
