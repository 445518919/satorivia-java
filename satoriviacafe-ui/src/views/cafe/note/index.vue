<template>
    <div class="app-container">
        <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px"
                 size="small">
            <el-form-item label="豆子笔记标题" prop="noteTitle">
                <el-input
                    v-model="queryParams.noteTitle"
                    clearable
                    placeholder="请输入豆子笔记标题"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="豆子笔记副标题" prop="noteSubTitle">
                <el-input
                    v-model="queryParams.noteSubTitle"
                    clearable
                    placeholder="请输入豆子笔记副标题"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="冲泡时间" prop="brewTime">
                <el-input
                    v-model="queryParams.brewTime"
                    clearable
                    placeholder="请输入冲泡时间"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="粉量" prop="poderQuantity">
                <el-input
                    v-model="queryParams.poderQuantity"
                    clearable
                    placeholder="请输入粉量"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="水量" prop="waterVolume">
                <el-input
                    v-model="queryParams.waterVolume"
                    clearable
                    placeholder="请输入水量"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="水温" prop="waterTemp">
                <el-input
                    v-model="queryParams.waterTemp"
                    clearable
                    placeholder="请输入水温"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="研磨度" prop="grindDegree">
                <el-input
                    v-model="queryParams.grindDegree"
                    clearable
                    placeholder="请输入研磨度"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="目标总时间" prop="targetTime">
                <el-input
                    v-model="queryParams.targetTime"
                    clearable
                    placeholder="请输入目标总时间"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item>
                <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                    v-hasPermi="['cafe:note:add']"
                    icon="el-icon-plus"
                    plain
                    size="mini"
                    type="primary"
                    @click="handleAdd"
                >新增
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    v-hasPermi="['cafe:note:edit']"
                    :disabled="single"
                    icon="el-icon-edit"
                    plain
                    size="mini"
                    type="success"
                    @click="handleUpdate"
                >修改
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    v-hasPermi="['cafe:note:remove']"
                    :disabled="multiple"
                    icon="el-icon-delete"
                    plain
                    size="mini"
                    type="danger"
                    @click="handleDelete"
                >删除
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    v-hasPermi="['cafe:note:export']"
                    icon="el-icon-download"
                    plain
                    size="mini"
                    type="warning"
                    @click="handleExport"
                >导出
                </el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="noteList" @selection-change="handleSelectionChange">
            <el-table-column align="center" type="selection" width="55"/>
            <el-table-column align="center" label="豆子笔记id" prop="noteId"/>
            <el-table-column align="center" label="豆子笔记标题" prop="noteTitle"/>
            <el-table-column align="center" label="豆子笔记副标题" prop="noteSubTitle"/>
            <el-table-column align="center" label="豆子笔记内容" prop="noteContent"/>
            <el-table-column align="center" label="冲泡时间" prop="brewTime"/>
            <el-table-column align="center" label="粉量" prop="poderQuantity"/>
            <el-table-column align="center" label="水量" prop="waterVolume"/>
            <el-table-column align="center" label="水温" prop="waterTemp"/>
            <el-table-column align="center" label="研磨度" prop="grindDegree"/>
            <el-table-column align="center" label="目标总时间" prop="targetTime"/>
            <el-table-column align="center" label="准备工作" prop="preparations"/>
            <el-table-column align="center" label="冲泡步骤" prop="brewSteps"/>
            <el-table-column align="center" label="豆子笔记状态" prop="noteStatus"/>
            <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
                <template slot-scope="scope">
                    <el-button
                        v-hasPermi="['cafe:note:edit']"
                        icon="el-icon-edit"
                        size="mini"
                        type="text"
                        @click="handleUpdate(scope.row)"
                    >修改
                    </el-button>
                    <el-button
                        v-hasPermi="['cafe:note:remove']"
                        icon="el-icon-delete"
                        size="mini"
                        type="text"
                        @click="handleDelete(scope.row)"
                    >删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination
            v-show="total>0"
            :limit.sync="queryParams.pageSize"
            :page.sync="queryParams.pageNum"
            :total="total"
            @pagination="getList"
        />

        <!-- 添加或修改豆子笔记对话框 -->
        <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="豆子笔记标题" prop="noteTitle">
                    <el-input v-model="form.noteTitle" placeholder="请输入豆子笔记标题"/>
                </el-form-item>
                <el-form-item label="豆子笔记副标题" prop="noteSubTitle">
                    <el-input v-model="form.noteSubTitle" placeholder="请输入豆子笔记副标题"/>
                </el-form-item>
                <el-form-item label="豆子笔记内容">
                    <editor v-model="form.noteContent" :min-height="192"/>
                </el-form-item>
                <el-form-item label="冲泡时间" prop="brewTime">
                    <el-input v-model="form.brewTime" placeholder="请输入冲泡时间"/>
                </el-form-item>
                <el-form-item label="粉量" prop="poderQuantity">
                    <el-input v-model="form.poderQuantity" placeholder="请输入粉量"/>
                </el-form-item>
                <el-form-item label="水量" prop="waterVolume">
                    <el-input v-model="form.waterVolume" placeholder="请输入水量"/>
                </el-form-item>
                <el-form-item label="水温" prop="waterTemp">
                    <el-input v-model="form.waterTemp" placeholder="请输入水温"/>
                </el-form-item>
                <el-form-item label="研磨度" prop="grindDegree">
                    <el-input v-model="form.grindDegree" placeholder="请输入研磨度"/>
                </el-form-item>
                <el-form-item label="目标总时间" prop="targetTime">
                    <el-input v-model="form.targetTime" placeholder="请输入目标总时间"/>
                </el-form-item>
                <el-form-item label="准备工作" prop="preparations">
                    <el-input v-model="form.preparations" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="冲泡步骤" prop="brewSteps">
                    <el-input v-model="form.brewSteps" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="删除时间" prop="deleteAt">
                    <el-date-picker v-model="form.deleteAt"
                                    clearable
                                    placeholder="请选择删除时间"
                                    type="date"
                                    value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {addNote, delNote, getNote, listNote, updateNote} from "@/api/cafe/note"

export default {
    name: "Note",
    dicts: [[],],
    data() {
        return {
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 非单个禁用
            single: true,
            // 非多个禁用
            multiple: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 豆子笔记表格数据
            noteList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                noteTitle: null,
                noteSubTitle: null,
                noteContent: null,
                brewTime: null,
                poderQuantity: null,
                waterVolume: null,
                waterTemp: null,
                grindDegree: null,
                targetTime: null,
                preparations: null,
                brewSteps: null,
                noteStatus: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                noteTitle: [
                    {required: true, message: "豆子笔记标题不能为空", trigger: "blur"}
                ],
                noteSubTitle: [
                    {required: true, message: "豆子笔记副标题不能为空", trigger: "blur"}
                ],
                noteContent: [
                    {required: true, message: "豆子笔记内容不能为空", trigger: "blur"}
                ],
                brewTime: [
                    {required: true, message: "冲泡时间不能为空", trigger: "blur"}
                ],
                poderQuantity: [
                    {required: true, message: "粉量不能为空", trigger: "blur"}
                ],
                waterVolume: [
                    {required: true, message: "水量不能为空", trigger: "blur"}
                ],
                waterTemp: [
                    {required: true, message: "水温不能为空", trigger: "blur"}
                ],
                grindDegree: [
                    {required: true, message: "研磨度不能为空", trigger: "blur"}
                ],
                targetTime: [
                    {required: true, message: "目标总时间不能为空", trigger: "blur"}
                ],
                preparations: [
                    {required: true, message: "准备工作不能为空", trigger: "blur"}
                ],
                brewSteps: [
                    {required: true, message: "冲泡步骤不能为空", trigger: "blur"}
                ],
            }
        }
    },
    created() {
        this.getList()
    },
    methods: {
        /** 查询豆子笔记列表 */
        getList() {
            this.loading = true
            listNote(this.queryParams).then(response => {
                this.noteList = response.rows
                this.total = response.total
                this.loading = false
            })
        },
        // 取消按钮
        cancel() {
            this.open = false
            this.reset()
        },
        // 表单重置
        reset() {
            this.form = {
                noteId: null,
                noteTitle: null,
                noteSubTitle: null,
                noteContent: null,
                brewTime: null,
                poderQuantity: null,
                waterVolume: null,
                waterTemp: null,
                grindDegree: null,
                targetTime: null,
                preparations: null,
                brewSteps: null,
                noteStatus: null,
                createBy: null,
                createTime: null,
                updateBy: null,
                updateTime: null,
                deleteAt: null
            }
            this.resetForm("form")
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1
            this.getList()
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm")
            this.handleQuery()
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.noteId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset()
            this.open = true
            this.title = "添加豆子笔记"
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset()
            const noteId = row.noteId || this.ids
            getNote(noteId).then(response => {
                this.form = response.data
                this.open = true
                this.title = "修改豆子笔记"
            })
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.noteId != null) {
                        updateNote(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功")
                            this.open = false
                            this.getList()
                        })
                    } else {
                        addNote(this.form).then(response => {
                            this.$modal.msgSuccess("新增成功")
                            this.open = false
                            this.getList()
                        })
                    }
                }
            })
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const noteIds = row.noteId || this.ids
            this.$modal.confirm('是否确认删除豆子笔记编号为"' + noteIds + '"的数据项？').then(function () {
                return delNote(noteIds)
            }).then(() => {
                this.getList()
                this.$modal.msgSuccess("删除成功")
            }).catch(() => {
            })
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('cafe/note/export', {
                ...this.queryParams
            }, `note_${new Date().getTime()}.xlsx`)
        }
    }
}
</script>
