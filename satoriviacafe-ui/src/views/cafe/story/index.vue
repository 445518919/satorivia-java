<template>
    <div class="app-container">
        <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px"
                 size="small">
            <el-form-item label="唯一编码" prop="storyCode">
                <el-input
                    v-model="queryParams.storyCode"
                    clearable
                    placeholder="请输入唯一编码"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="英文栏目名" prop="storyLabel">
                <el-input
                    v-model="queryParams.storyLabel"
                    clearable
                    placeholder="请输入英文栏目名"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="标题" prop="storyTitle">
                <el-input
                    v-model="queryParams.storyTitle"
                    clearable
                    placeholder="请输入标题"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="显示顺序" prop="storySort">
                <el-input
                    v-model="queryParams.storySort"
                    clearable
                    placeholder="请输入显示顺序"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="发布时间" prop="publishTime">
                <el-date-picker v-model="queryParams.publishTime"
                                clearable
                                placeholder="请选择发布时间"
                                type="date"
                                value-format="yyyy-MM-dd">
                </el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                    v-hasPermi="['cafe:story:add']"
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
                    v-hasPermi="['cafe:story:edit']"
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
                    v-hasPermi="['cafe:story:remove']"
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
                    v-hasPermi="['cafe:story:export']"
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

        <el-table v-loading="loading" :data="storyList" @selection-change="handleSelectionChange">
            <el-table-column align="center" type="selection" width="55"/>
            <el-table-column align="center" label="品牌故事id" prop="storyId"/>
            <el-table-column align="center" label="唯一编码" prop="storyCode"/>
            <el-table-column align="center" label="英文栏目名" prop="storyLabel"/>
            <el-table-column align="center" label="标题" prop="storyTitle"/>
            <el-table-column align="center" label="副标题" prop="storySubTitle"/>
            <el-table-column align="center" label="品牌引言" prop="storyQuote"/>
            <el-table-column align="center" label="品牌摘要" prop="storySummary"/>
            <el-table-column align="center" label="品牌故事封面" prop="coverImage" width="100">
                <template slot-scope="scope">
                    <image-preview :height="50" :src="scope.row.coverImage" :width="50"/>
                </template>
            </el-table-column>
            <el-table-column align="center" label="显示顺序" prop="storySort"/>
            <el-table-column align="center" label="发布状态" prop="publishStatus"/>
            <el-table-column align="center" label="状态" prop="storyStatus"/>
            <el-table-column align="center" label="发布时间" prop="publishTime" width="180">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.publishTime, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
                <template slot-scope="scope">
                    <el-button
                        v-hasPermi="['cafe:story:edit']"
                        icon="el-icon-edit"
                        size="mini"
                        type="text"
                        @click="handleUpdate(scope.row)"
                    >修改
                    </el-button>
                    <el-button
                        v-hasPermi="['cafe:story:remove']"
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

        <!-- 添加或修改品牌故事主对话框 -->
        <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="唯一编码" prop="storyCode">
                    <el-input v-model="form.storyCode" placeholder="请输入唯一编码"/>
                </el-form-item>
                <el-form-item label="英文栏目名" prop="storyLabel">
                    <el-input v-model="form.storyLabel" placeholder="请输入英文栏目名"/>
                </el-form-item>
                <el-form-item label="标题" prop="storyTitle">
                    <el-input v-model="form.storyTitle" placeholder="请输入标题"/>
                </el-form-item>
                <el-form-item label="副标题" prop="storySubTitle">
                    <el-input v-model="form.storySubTitle" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="品牌引言" prop="storyQuote">
                    <el-input v-model="form.storyQuote" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="品牌摘要" prop="storySummary">
                    <el-input v-model="form.storySummary" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="品牌故事封面" prop="coverImage">
                    <image-upload v-model="form.coverImage"/>
                </el-form-item>
                <el-form-item label="显示顺序" prop="storySort">
                    <el-input v-model="form.storySort" placeholder="请输入显示顺序"/>
                </el-form-item>
                <el-form-item label="发布时间" prop="publishTime">
                    <el-date-picker v-model="form.publishTime"
                                    clearable
                                    placeholder="请选择发布时间"
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
import {addStory, delStory, getStory, listStory, updateStory} from "@/api/cafe/story"

export default {
    name: "Story",
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
            // 品牌故事主表格数据
            storyList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                storyCode: null,
                storyLabel: null,
                storyTitle: null,
                storySubTitle: null,
                storyQuote: null,
                storySummary: null,
                coverImage: null,
                storySort: null,
                publishStatus: null,
                storyStatus: null,
                publishTime: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                storyCode: [
                    {required: true, message: "唯一编码不能为空", trigger: "blur"}
                ],
                storyTitle: [
                    {required: true, message: "标题不能为空", trigger: "blur"}
                ],
            }
        }
    },
    created() {
        this.getList()
    },
    methods: {
        /** 查询品牌故事主列表 */
        getList() {
            this.loading = true
            listStory(this.queryParams).then(response => {
                this.storyList = response.rows
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
                storyId: null,
                storyCode: null,
                storyLabel: null,
                storyTitle: null,
                storySubTitle: null,
                storyQuote: null,
                storySummary: null,
                coverImage: null,
                storySort: null,
                publishStatus: null,
                storyStatus: null,
                publishTime: null,
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
            this.ids = selection.map(item => item.storyId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset()
            this.open = true
            this.title = "添加品牌故事主"
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset()
            const storyId = row.storyId || this.ids
            getStory(storyId).then(response => {
                this.form = response.data
                this.open = true
                this.title = "修改品牌故事主"
            })
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.storyId != null) {
                        updateStory(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功")
                            this.open = false
                            this.getList()
                        })
                    } else {
                        addStory(this.form).then(response => {
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
            const storyIds = row.storyId || this.ids
            this.$modal.confirm('是否确认删除品牌故事主编号为"' + storyIds + '"的数据项？').then(function () {
                return delStory(storyIds)
            }).then(() => {
                this.getList()
                this.$modal.msgSuccess("删除成功")
            }).catch(() => {
            })
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('cafe/story/export', {
                ...this.queryParams
            }, `story_${new Date().getTime()}.xlsx`)
        }
    }
}
</script>
