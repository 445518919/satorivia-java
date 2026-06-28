<template>
    <div class="app-container">
        <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px"
                 size="small">
            <el-form-item label="NFC编码" prop="nfcCode">
                <el-input
                    v-model="queryParams.nfcCode"
                    clearable
                    placeholder="请输入NFC编码"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="SKU编码" prop="skuCode">
                <el-input
                    v-model="queryParams.skuCode"
                    clearable
                    placeholder="请输入SKU编码"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="产品名称" prop="productName">
                <el-input
                    v-model="queryParams.productName"
                    clearable
                    placeholder="请输入产品名称"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="批次名称" prop="batchName">
                <el-input
                    v-model="queryParams.batchName"
                    clearable
                    placeholder="请输入批次名称"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="NFC哈希值" prop="nfcHash">
                <el-input
                    v-model="queryParams.nfcHash"
                    clearable
                    placeholder="请输入NFC哈希值"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="NFC链接" prop="nfcUrl">
                <el-input
                    v-model="queryParams.nfcUrl"
                    clearable
                    placeholder="请输入NFC链接"
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
                    v-hasPermi="['cafe:nfc:add']"
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
                    v-hasPermi="['cafe:nfc:edit']"
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
                    v-hasPermi="['cafe:nfc:remove']"
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
                <el-button v-hasPermi="['cafe:nfc:import']" icon="el-icon-upload2" plain size="mini" type="info"
                           @click="handleImport">导入
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                    v-hasPermi="['cafe:nfc:export']"
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

        <el-table v-loading="loading" :data="nfcList" @selection-change="handleSelectionChange">
            <el-table-column align="center" type="selection" width="55"/>
            <el-table-column align="center" label="产品NFCid" prop="nfcId"/>
            <el-table-column align="center" label="NFC编码" prop="nfcCode"/>
            <el-table-column align="center" label="SKU编码" prop="skuCode"/>
            <el-table-column align="center" label="产品名称" prop="productName"/>
            <el-table-column align="center" label="批次名称" prop="batchName"/>
            <el-table-column align="center" label="NFC哈希值" prop="nfcHash"/>
            <el-table-column align="center" label="NFC链接" prop="nfcUrl"/>
            <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
                <template slot-scope="scope">
                    <el-button
                        v-hasPermi="['cafe:nfc:edit']"
                        icon="el-icon-edit"
                        size="mini"
                        type="text"
                        @click="handleUpdate(scope.row)"
                    >修改
                    </el-button>
                    <el-button
                        v-hasPermi="['cafe:nfc:remove']"
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

        <!-- 添加或修改产品NFC对话框 -->
        <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="NFC编码" prop="nfcCode">
                    <el-input v-model="form.nfcCode" placeholder="请输入NFC编码"/>
                </el-form-item>
                <el-form-item label="SKU编码" prop="skuCode">
                    <el-input v-model="form.skuCode" placeholder="请输入SKU编码"/>
                </el-form-item>
                <el-form-item label="产品名称" prop="productName">
                    <el-input v-model="form.productName" placeholder="请输入产品名称"/>
                </el-form-item>
                <el-form-item label="批次名称" prop="batchName">
                    <el-input v-model="form.batchName" placeholder="请输入批次名称"/>
                </el-form-item>
                <el-form-item label="删除时间" prop="deleteAt">
                    <el-date-picker v-model="form.deleteAt"
                                    clearable
                                    placeholder="请选择删除时间"
                                    type="date"
                                    value-format="yyyy-MM-dd">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="NFC哈希值" prop="nfcHash">
                    <el-input v-model="form.nfcHash" placeholder="请输入NFC哈希值"/>
                </el-form-item>
                <el-form-item label="NFC链接" prop="nfcUrl">
                    <el-input v-model="form.nfcUrl" placeholder="请输入NFC链接"/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>

        <!-- NFC导入对话框 -->
        <el-dialog :title="upload.title" :visible.sync="upload.open" append-to-body width="400px">
            <el-upload ref="upload" :action="upload.url + '?updateSupport=' + upload.updateSupport" :auto-upload="false"
                       :disabled="upload.isUploading"
                       :headers="upload.headers" :limit="1"
                       :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" accept=".xlsx, .xls"
                       drag>
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <div slot="tip" class="el-upload__tip text-center">
                    <div slot="tip" class="el-upload__tip">
                        <el-checkbox v-model="upload.updateSupport"/>
                        是否更新已经存在的NFC数据
                    </div>
                    <span>仅允许导入xls、xlsx格式文件。</span>
                    <el-link :underline="false" style="font-size: 12px; vertical-align: baseline" type="primary"
                             @click="importTemplate">下载模板
                    </el-link>
                </div>
            </el-upload>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitFileForm">确 定</el-button>
                <el-button @click="upload.open = false">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {addNfc, delNfc, getNfc, listNfc, updateNfc} from "@/api/cafe/nfc"
import {getToken} from "@/utils/auth";

export default {
    name: "Nfc",
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
            // 产品NFC表格数据
            nfcList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                nfcCode: null,
                skuCode: null,
                productName: null,
                batchName: null,
                nfcHash: null,
                nfcUrl: null
            },
            // NFC导入参数
            upload: {
                // 是否显示弹出层（NFC导入）
                open: false,
                // 弹出层标题（NFC导入）
                title: "",
                // 是否禁用上传
                isUploading: false,
                // 是否更新已经存在的NFC数据
                updateSupport: 0,
                // 设置上传的请求头部
                headers: {Authorization: "Bearer " + getToken()},
                // 上传的地址
                url: process.env.VUE_APP_BASE_API + "/cafe/nfc/importData"
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                nfcCode: [
                    {required: true, message: "NFC编码不能为空", trigger: "blur"}
                ],
                skuCode: [
                    {required: true, message: "SKU编码不能为空", trigger: "blur"}
                ],
                productName: [
                    {required: true, message: "产品名称不能为空", trigger: "blur"}
                ],
                batchName: [
                    {required: true, message: "批次名称不能为空", trigger: "blur"}
                ],
            }
        }
    },
    created() {
        this.getList()
    },
    methods: {
        /** 查询产品NFC列表 */
        getList() {
            this.loading = true
            listNfc(this.queryParams).then(response => {
                this.nfcList = response.rows
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
                nfcId: null,
                nfcCode: null,
                skuCode: null,
                productName: null,
                batchName: null,
                createBy: null,
                createTime: null,
                updateBy: null,
                updateTime: null,
                deleteAt: null,
                nfcHash: null,
                nfcUrl: null
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
            this.ids = selection.map(item => item.nfcId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset()
            this.open = true
            this.title = "添加产品NFC"
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset()
            const nfcId = row.nfcId || this.ids
            getNfc(nfcId).then(response => {
                this.form = response.data
                this.open = true
                this.title = "修改产品NFC"
            })
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.nfcId != null) {
                        updateNfc(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功")
                            this.open = false
                            this.getList()
                        })
                    } else {
                        addNfc(this.form).then(response => {
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
            const nfcIds = row.nfcId || this.ids
            this.$modal.confirm('是否确认删除产品NFC编号为"' + nfcIds + '"的数据项？').then(function () {
                return delNfc(nfcIds)
            }).then(() => {
                this.getList()
                this.$modal.msgSuccess("删除成功")
            }).catch(() => {
            })
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('cafe/nfc/export', {
                ...this.queryParams
            }, `nfc_${new Date().getTime()}.xlsx`)
        },
        /** 导入按钮操作 */
        handleImport() {
            this.upload.title = "用户导入";
            this.upload.open = true;
        },
        /** 下载模板操作 */
        importTemplate() {
            this.download('cafe/nfc/importTemplate', {}, `nfc_template_${new Date().getTime()}.xlsx`)
        },
        // 文件上传中处理
        handleFileUploadProgress(event, file, fileList) {
            this.upload.isUploading = true;
        },
        // 文件上传成功处理
        handleFileSuccess(response, file, fileList) {
            this.upload.open = false;
            this.upload.isUploading = false;
            this.$refs.upload.clearFiles();
            this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", {dangerouslyUseHTMLString: true});
            this.getList();
        },
        // 提交上传文件
        submitFileForm() {
            this.$refs.upload.submit();
        }
    }
}
</script>
