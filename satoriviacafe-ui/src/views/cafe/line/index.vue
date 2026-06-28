<template>
    <div class="app-container">
        <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px"
                 size="small">
            <el-form-item label="产品线编码" prop="lineCode">
                <el-input
                    v-model="queryParams.lineCode"
                    clearable
                    placeholder="请输入产品线编码"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="系列编码" prop="seriesCode">
                <el-input
                    v-model="queryParams.seriesCode"
                    clearable
                    placeholder="请输入系列编码"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="产品线名称" prop="lineName">
                <el-input
                    v-model="queryParams.lineName"
                    clearable
                    placeholder="请输入产品线名称"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="产品线副标题" prop="lineSubTitle">
                <el-input
                    v-model="queryParams.lineSubTitle"
                    clearable
                    placeholder="请输入产品线副标题"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="SEO标题" prop="seoTitle">
                <el-input
                    v-model="queryParams.seoTitle"
                    clearable
                    placeholder="请输入SEO标题"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="显示顺序" prop="lineSort">
                <el-input
                    v-model="queryParams.lineSort"
                    clearable
                    placeholder="请输入显示顺序"
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
                    v-hasPermi="['cafe:line:add']"
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
                    v-hasPermi="['cafe:line:edit']"
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
                    v-hasPermi="['cafe:line:remove']"
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
                    v-hasPermi="['cafe:line:export']"
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

        <el-table v-loading="loading" :data="lineList" @selection-change="handleSelectionChange">
            <el-table-column align="center" type="selection" width="55"/>
            <el-table-column align="center" label="产品线id" prop="lineId"/>
            <el-table-column align="center" label="产品线编码" prop="lineCode"/>
            <el-table-column align="center" label="系列编码" prop="seriesCode"/>
            <el-table-column align="center" label="产品线名称" prop="lineName"/>
            <el-table-column align="center" label="产品线副标题" prop="lineSubTitle"/>
            <el-table-column align="center" label="产品线说明" prop="lineDescription"/>
            <el-table-column align="center" label="产品线桌面端封面" prop="coverImage" width="100">
                <template slot-scope="scope">
                    <image-preview :height="50" :src="scope.row.coverImage" :width="50"/>
                </template>
            </el-table-column>
            <el-table-column align="center" label="产品线手机端封面" prop="mobileCoverImage" width="100">
                <template slot-scope="scope">
                    <image-preview :height="50" :src="scope.row.mobileCoverImage" :width="50"/>
                </template>
            </el-table-column>
            <el-table-column align="center" label="SEO标题" prop="seoTitle"/>
            <el-table-column align="center" label="SEO描述" prop="seoDescription"/>
            <el-table-column align="center" label="显示顺序" prop="lineSort"/>
            <el-table-column align="center" label="状态" prop="lineStatus"/>
            <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
                <template slot-scope="scope">
                    <el-button
                        v-hasPermi="['cafe:line:edit']"
                        icon="el-icon-edit"
                        size="mini"
                        type="text"
                        @click="handleUpdate(scope.row)"
                    >修改
                    </el-button>
                    <el-button
                        v-hasPermi="['cafe:line:remove']"
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

        <!-- 添加或修改咖啡产品线对话框 -->
        <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="产品线编码" prop="lineCode">
                    <el-input v-model="form.lineCode" placeholder="请输入产品线编码"/>
                </el-form-item>
                <el-form-item label="系列编码" prop="seriesCode">
                    <el-input v-model="form.seriesCode" placeholder="请输入系列编码"/>
                </el-form-item>
                <el-form-item label="产品线名称" prop="lineName">
                    <el-input v-model="form.lineName" placeholder="请输入产品线名称"/>
                </el-form-item>
                <el-form-item label="产品线副标题" prop="lineSubTitle">
                    <el-input v-model="form.lineSubTitle" placeholder="请输入产品线副标题"/>
                </el-form-item>
                <el-form-item label="产品线说明">
                    <editor v-model="form.lineDescription" :min-height="192"/>
                </el-form-item>
                <el-form-item label="产品线桌面端封面" prop="coverImage">
                    <image-upload v-model="form.coverImage"/>
                </el-form-item>
                <el-form-item label="产品线手机端封面" prop="mobileCoverImage">
                    <image-upload v-model="form.mobileCoverImage"/>
                </el-form-item>
                <el-form-item label="SEO标题" prop="seoTitle">
                    <el-input v-model="form.seoTitle" placeholder="请输入SEO标题"/>
                </el-form-item>
                <el-form-item label="SEO描述">
                    <editor v-model="form.seoDescription" :min-height="192"/>
                </el-form-item>
                <el-form-item label="显示顺序" prop="lineSort">
                    <el-input v-model="form.lineSort" placeholder="请输入显示顺序"/>
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
import {addLine, delLine, getLine, listLine, updateLine} from "@/api/cafe/line"

export default {
    name: "Line",
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
            // 咖啡产品线表格数据
            lineList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                lineCode: null,
                seriesCode: null,
                lineName: null,
                lineSubTitle: null,
                lineDescription: null,
                coverImage: null,
                mobileCoverImage: null,
                seoTitle: null,
                seoDescription: null,
                lineSort: null,
                lineStatus: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                lineCode: [
                    {required: true, message: "产品线编码不能为空", trigger: "blur"}
                ],
                seriesCode: [
                    {required: true, message: "系列编码不能为空", trigger: "blur"}
                ],
                lineName: [
                    {required: true, message: "产品线名称不能为空", trigger: "blur"}
                ],
                coverImage: [
                    {required: true, message: "产品线桌面端封面不能为空", trigger: "blur"}
                ],
            }
        }
    },
    created() {
        this.getList()
    },
    methods: {
        /** 查询咖啡产品线列表 */
        getList() {
            this.loading = true
            listLine(this.queryParams).then(response => {
                this.lineList = response.rows
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
                lineId: null,
                lineCode: null,
                seriesCode: null,
                lineName: null,
                lineSubTitle: null,
                lineDescription: null,
                coverImage: null,
                mobileCoverImage: null,
                seoTitle: null,
                seoDescription: null,
                lineSort: null,
                lineStatus: null,
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
            this.ids = selection.map(item => item.lineId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset()
            this.open = true
            this.title = "添加咖啡产品线"
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset()
            const lineId = row.lineId || this.ids
            getLine(lineId).then(response => {
                this.form = response.data
                this.open = true
                this.title = "修改咖啡产品线"
            })
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.lineId != null) {
                        updateLine(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功")
                            this.open = false
                            this.getList()
                        })
                    } else {
                        addLine(this.form).then(response => {
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
            const lineIds = row.lineId || this.ids
            this.$modal.confirm('是否确认删除咖啡产品线编号为"' + lineIds + '"的数据项？').then(function () {
                return delLine(lineIds)
            }).then(() => {
                this.getList()
                this.$modal.msgSuccess("删除成功")
            }).catch(() => {
            })
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('cafe/line/export', {
                ...this.queryParams
            }, `line_${new Date().getTime()}.xlsx`)
        }
    }
}
</script>
