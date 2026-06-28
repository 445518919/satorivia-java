<template>
    <div class="app-container">
        <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px"
                 size="small">
            <el-form-item label="轮播图标题" prop="bannerTitle">
                <el-input
                    v-model="queryParams.bannerTitle"
                    clearable
                    placeholder="请输入轮播图标题"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="轮播图链接" prop="bannerLink">
                <el-input
                    v-model="queryParams.bannerLink"
                    clearable
                    placeholder="请输入轮播图链接"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="轮播图描述" prop="bannerDesc">
                <el-input
                    v-model="queryParams.bannerDesc"
                    clearable
                    placeholder="请输入轮播图描述"
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
                    v-hasPermi="['cafe:banner:add']"
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
                    v-hasPermi="['cafe:banner:edit']"
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
                    v-hasPermi="['cafe:banner:remove']"
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
                    v-hasPermi="['cafe:banner:export']"
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

        <el-table v-loading="loading" :data="bannerList" @selection-change="handleSelectionChange">
            <el-table-column align="center" type="selection" width="55"/>
            <el-table-column align="center" label="轮播图id" prop="bannerId"/>
            <el-table-column align="center" label="轮播图标题" prop="bannerTitle"/>
            <el-table-column align="center" label="轮播图图片" prop="bannerImage" width="100">
                <template slot-scope="scope">
                    <image-preview :height="50" :src="scope.row.bannerImage" :width="50"/>
                </template>
            </el-table-column>
            <el-table-column align="center" label="轮播图链接" prop="bannerLink"/>
            <el-table-column align="center" label="轮播图描述" prop="bannerDesc"/>
            <el-table-column align="center" label="轮播图状态" prop="bannerStatus"/>
            <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
                <template slot-scope="scope">
                    <el-button
                        v-hasPermi="['cafe:banner:edit']"
                        icon="el-icon-edit"
                        size="mini"
                        type="text"
                        @click="handleUpdate(scope.row)"
                    >修改
                    </el-button>
                    <el-button
                        v-hasPermi="['cafe:banner:remove']"
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

        <!-- 添加或修改轮播图对话框 -->
        <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="轮播图标题" prop="bannerTitle">
                    <el-input v-model="form.bannerTitle" placeholder="请输入轮播图标题"/>
                </el-form-item>
                <el-form-item label="轮播图图片" prop="bannerImage">
                    <image-upload v-model="form.bannerImage"/>
                </el-form-item>
                <el-form-item label="轮播图链接" prop="bannerLink">
                    <el-input v-model="form.bannerLink" placeholder="请输入轮播图链接"/>
                </el-form-item>
                <el-form-item label="轮播图描述" prop="bannerDesc">
                    <el-input v-model="form.bannerDesc" placeholder="请输入轮播图描述"/>
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
import {addBanner, delBanner, getBanner, listBanner, updateBanner} from "@/api/cafe/banner"

export default {
    name: "Banner",
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
            // 轮播图表格数据
            bannerList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                bannerTitle: null,
                bannerImage: null,
                bannerLink: null,
                bannerDesc: null,
                bannerStatus: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                bannerTitle: [
                    {required: true, message: "轮播图标题不能为空", trigger: "blur"}
                ],
                bannerImage: [
                    {required: true, message: "轮播图图片不能为空", trigger: "blur"}
                ],
                bannerLink: [
                    {required: true, message: "轮播图链接不能为空", trigger: "blur"}
                ],
            }
        }
    },
    created() {
        this.getList()
    },
    methods: {
        /** 查询轮播图列表 */
        getList() {
            this.loading = true
            listBanner(this.queryParams).then(response => {
                this.bannerList = response.rows
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
                bannerId: null,
                bannerTitle: null,
                bannerImage: null,
                bannerLink: null,
                bannerDesc: null,
                bannerStatus: null,
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
            this.ids = selection.map(item => item.bannerId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset()
            this.open = true
            this.title = "添加轮播图"
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset()
            const bannerId = row.bannerId || this.ids
            getBanner(bannerId).then(response => {
                this.form = response.data
                this.open = true
                this.title = "修改轮播图"
            })
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.bannerId != null) {
                        updateBanner(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功")
                            this.open = false
                            this.getList()
                        })
                    } else {
                        addBanner(this.form).then(response => {
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
            const bannerIds = row.bannerId || this.ids
            this.$modal.confirm('是否确认删除轮播图编号为"' + bannerIds + '"的数据项？').then(function () {
                return delBanner(bannerIds)
            }).then(() => {
                this.getList()
                this.$modal.msgSuccess("删除成功")
            }).catch(() => {
            })
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('cafe/banner/export', {
                ...this.queryParams
            }, `banner_${new Date().getTime()}.xlsx`)
        }
    }
}
</script>
