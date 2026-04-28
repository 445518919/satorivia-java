<template>
    <div class="app-container">
        <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px"
                 size="small">
            <el-form-item label="产品ID" prop="prodId">
                <el-input
                    v-model="queryParams.prodId"
                    clearable
                    placeholder="请输入产品ID"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="商品编码" prop="prodCode">
                <el-input
                    v-model="queryParams.prodCode"
                    clearable
                    placeholder="请输入商品编码"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="产品名称 (如: 埃塞瑰夏精品咖啡豆)" prop="prodName">
                <el-input
                    v-model="queryParams.prodName"
                    clearable
                    placeholder="请输入产品名称 (如: 埃塞瑰夏精品咖啡豆)"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="事件类型 (如: page_view)" prop="eventName">
                <el-input
                    v-model="queryParams.eventName"
                    clearable
                    placeholder="请输入事件类型 (如: page_view)"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="访问者IP地址 (支持IPv6)" prop="ip">
                <el-input
                    v-model="queryParams.ip"
                    clearable
                    placeholder="请输入访问者IP地址 (支持IPv6)"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="访问者地理位置" prop="location">
                <el-input
                    v-model="queryParams.location"
                    clearable
                    placeholder="请输入访问者地理位置"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="浏览器名称 (如: Chrome, WeChat, Safari)" prop="browser">
                <el-input
                    v-model="queryParams.browser"
                    clearable
                    placeholder="请输入浏览器名称 (如: Chrome, WeChat, Safari)"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="操作系统 (如: iOS, Android, Windows)" prop="os">
                <el-input
                    v-model="queryParams.os"
                    clearable
                    placeholder="请输入操作系统 (如: iOS, Android, Windows)"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="记录创建时间" prop="createdAt">
                <el-date-picker v-model="queryParams.createdAt"
                                clearable
                                placeholder="请选择记录创建时间"
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
                    v-hasPermi="['cafe:track_log:add']"
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
                    v-hasPermi="['cafe:track_log:edit']"
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
                    v-hasPermi="['cafe:track_log:remove']"
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
                    v-hasPermi="['cafe:track_log:export']"
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

        <el-table v-loading="loading" :data="track_logList" @selection-change="handleSelectionChange">
            <el-table-column align="center" type="selection" width="55"/>
            <el-table-column align="center" label="主键ID" prop="id"/>
            <el-table-column align="center" label="产品ID" prop="prodId"/>
            <el-table-column align="center" label="商品编码" prop="prodCode"/>
            <el-table-column align="center" label="产品名称 (如: 埃塞瑰夏精品咖啡豆)" prop="prodName"/>
            <el-table-column align="center" label="事件类型 (如: page_view)" prop="eventName"/>
            <el-table-column align="center" label="访问者IP地址 (支持IPv6)" prop="ip"/>
            <el-table-column align="center" label="访问者地理位置" prop="location"/>
            <el-table-column align="center" label="原始浏览器User-Agent字符串" prop="userAgent"/>
            <el-table-column align="center" label="设备类型 (如: Mobile, Desktop, Tablet)" prop="deviceType"/>
            <el-table-column align="center" label="浏览器名称 (如: Chrome, WeChat, Safari)" prop="browser"/>
            <el-table-column align="center" label="操作系统 (如: iOS, Android, Windows)" prop="os"/>
            <el-table-column align="center" label="当前页面URL" prop="pageUrl"/>
            <el-table-column align="center" label="来源页面URL" prop="referrerUrl"/>
            <el-table-column align="center" label="记录创建时间" prop="createdAt" width="180">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
                <template slot-scope="scope">
                    <el-button
                        v-hasPermi="['cafe:track_log:edit']"
                        icon="el-icon-edit"
                        size="mini"
                        type="text"
                        @click="handleUpdate(scope.row)"
                    >修改
                    </el-button>
                    <el-button
                        v-hasPermi="['cafe:track_log:remove']"
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

        <!-- 添加或修改咖啡产品访问跟踪日志对话框 -->
        <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="产品ID" prop="prodId">
                    <el-input v-model="form.prodId" placeholder="请输入产品ID"/>
                </el-form-item>
                <el-form-item label="商品编码" prop="prodCode">
                    <el-input v-model="form.prodCode" placeholder="请输入商品编码"/>
                </el-form-item>
                <el-form-item label="产品名称 (如: 埃塞瑰夏精品咖啡豆)" prop="prodName">
                    <el-input v-model="form.prodName" placeholder="请输入产品名称 (如: 埃塞瑰夏精品咖啡豆)"/>
                </el-form-item>
                <el-form-item label="事件类型 (如: page_view)" prop="eventName">
                    <el-input v-model="form.eventName" placeholder="请输入事件类型 (如: page_view)"/>
                </el-form-item>
                <el-form-item label="访问者IP地址 (支持IPv6)" prop="ip">
                    <el-input v-model="form.ip" placeholder="请输入访问者IP地址 (支持IPv6)"/>
                </el-form-item>
                <el-form-item label="访问者地理位置" prop="location">
                    <el-input v-model="form.location" placeholder="请输入访问者地理位置"/>
                </el-form-item>
                <el-form-item label="原始浏览器User-Agent字符串" prop="userAgent">
                    <el-input v-model="form.userAgent" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="浏览器名称 (如: Chrome, WeChat, Safari)" prop="browser">
                    <el-input v-model="form.browser" placeholder="请输入浏览器名称 (如: Chrome, WeChat, Safari)"/>
                </el-form-item>
                <el-form-item label="操作系统 (如: iOS, Android, Windows)" prop="os">
                    <el-input v-model="form.os" placeholder="请输入操作系统 (如: iOS, Android, Windows)"/>
                </el-form-item>
                <el-form-item label="当前页面URL" prop="pageUrl">
                    <el-input v-model="form.pageUrl" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="来源页面URL" prop="referrerUrl">
                    <el-input v-model="form.referrerUrl" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="记录创建时间" prop="createdAt">
                    <el-date-picker v-model="form.createdAt"
                                    clearable
                                    placeholder="请选择记录创建时间"
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
import {addTrack_log, delTrack_log, getTrack_log, listTrack_log, updateTrack_log} from "@/api/cafe/track_log"

export default {
    name: "Track_log",
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
            // 咖啡产品访问跟踪日志表格数据
            track_logList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                prodId: null,
                prodCode: null,
                prodName: null,
                eventName: null,
                ip: null,
                location: null,
                userAgent: null,
                deviceType: null,
                browser: null,
                os: null,
                pageUrl: null,
                referrerUrl: null,
                createdAt: null
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                prodId: [
                    {required: true, message: "产品ID不能为空", trigger: "blur"}
                ],
                prodCode: [
                    {required: true, message: "商品编码不能为空", trigger: "blur"}
                ],
                prodName: [
                    {required: true, message: "产品名称 (如: 埃塞瑰夏精品咖啡豆)不能为空", trigger: "blur"}
                ],
                eventName: [
                    {required: true, message: "事件类型 (如: page_view)不能为空", trigger: "blur"}
                ],
            }
        }
    },
    created() {
        this.getList()
    },
    methods: {
        /** 查询咖啡产品访问跟踪日志列表 */
        getList() {
            this.loading = true
            listTrack_log(this.queryParams).then(response => {
                this.track_logList = response.rows
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
                id: null,
                prodId: null,
                prodCode: null,
                prodName: null,
                eventName: null,
                ip: null,
                location: null,
                userAgent: null,
                deviceType: null,
                browser: null,
                os: null,
                pageUrl: null,
                referrerUrl: null,
                createdAt: null
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
            this.ids = selection.map(item => item.id)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset()
            this.open = true
            this.title = "添加咖啡产品访问跟踪日志"
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset()
            const id = row.id || this.ids
            getTrack_log(id).then(response => {
                this.form = response.data
                this.open = true
                this.title = "修改咖啡产品访问跟踪日志"
            })
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateTrack_log(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功")
                            this.open = false
                            this.getList()
                        })
                    } else {
                        addTrack_log(this.form).then(response => {
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
            const ids = row.id || this.ids
            this.$modal.confirm('是否确认删除咖啡产品访问跟踪日志编号为"' + ids + '"的数据项？').then(function () {
                return delTrack_log(ids)
            }).then(() => {
                this.getList()
                this.$modal.msgSuccess("删除成功")
            }).catch(() => {
            })
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('cafe/track_log/export', {
                ...this.queryParams
            }, `track_log_${new Date().getTime()}.xlsx`)
        }
    }
}
</script>
