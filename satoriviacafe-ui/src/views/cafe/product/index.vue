<template>
    <div class="app-container">

        <!-- 搜索区域：只保留核心筛选字段 -->
        <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="80px"
                 size="small">
            <el-form-item label="产品线" prop="lineId">
                <el-input v-model="queryParams.lineId" clearable placeholder="请输入产品线ID"
                          @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item label="商品名称" prop="productName">
                <el-input v-model="queryParams.productName" clearable placeholder="请输入商品名称"
                          @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item label="商品编码" prop="productSlug">
                <el-input v-model="queryParams.productSlug" clearable placeholder="请输入URL编码"
                          @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item label="产地/庄园" prop="origin">
                <el-input v-model="queryParams.origin" clearable placeholder="请输入产地或庄园"
                          @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item label="咖啡品种" prop="variety">
                <el-input v-model="queryParams.variety" clearable placeholder="请输入咖啡品种"
                          @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item label="烘焙度" prop="roastDegree">
                <el-input v-model="queryParams.roastDegree" clearable placeholder="请输入烘焙度"
                          @keyup.enter.native="handleQuery"/>
            </el-form-item>
            <el-form-item label="是否推荐" prop="isFeatured">
                <el-select v-model="queryParams.isFeatured" clearable placeholder="全部">
                    <el-option label="推荐" value="1"/>
                    <el-option label="不推荐" value="0"/>
                </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="productStatus">
                <el-select v-model="queryParams.productStatus" clearable placeholder="全部">
                    <el-option label="正常" value="0"/>
                    <el-option label="停用" value="1"/>
                </el-select>
            </el-form-item>
            <el-form-item label="发布时间" prop="publishTime">
                <el-date-picker v-model="queryParams.publishTime" clearable placeholder="请选择发布时间" type="date"
                                value-format="yyyy-MM-dd"/>
            </el-form-item>
            <el-form-item>
                <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <!-- 操作按钮行 -->
        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button v-hasPermi="['cafe:product:add']" icon="el-icon-plus" plain size="mini" type="primary"
                           @click="handleAdd">新增
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button v-hasPermi="['cafe:product:edit']" :disabled="single" icon="el-icon-edit" plain size="mini"
                           type="success" @click="handleUpdate">修改
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button v-hasPermi="['cafe:product:remove']" :disabled="multiple" icon="el-icon-delete" plain
                           size="mini" type="danger" @click="handleDelete">删除
                </el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button v-hasPermi="['cafe:product:export']" icon="el-icon-download" plain size="mini" type="warning"
                           @click="handleExport">导出
                </el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 列表表格：只展示核心字段，长文本截断或改为操作按钮 -->
        <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
            <el-table-column align="center" type="selection" width="50"/>
            <el-table-column align="center" label="商品ID" prop="productId" width="80"/>
            <el-table-column align="center" label="主图" prop="primaryImage" width="80">
                <template slot-scope="scope">
                    <image-preview :height="50" :src="scope.row.primaryImage" :width="50"/>
                </template>
            </el-table-column>
            <el-table-column align="center" label="商品名称" min-width="160" prop="productName" show-overflow-tooltip/>
            <el-table-column align="center" label="短名称" prop="productShortName" show-overflow-tooltip width="120"/>
            <el-table-column align="center" label="URL编码" prop="productSlug" show-overflow-tooltip width="160"/>
            <el-table-column align="center" label="产品线ID" prop="lineId" width="90"/>
            <el-table-column align="center" label="标签" prop="productLabel" show-overflow-tooltip width="100"/>
            <el-table-column align="center" label="产地/庄园" prop="origin" show-overflow-tooltip width="130"/>
            <el-table-column align="center" label="品种" prop="variety" show-overflow-tooltip width="120"/>
            <el-table-column align="center" label="海拔" prop="altitude" show-overflow-tooltip width="100"/>
            <el-table-column align="center" label="处理方式" prop="treatment" show-overflow-tooltip width="120"/>
            <el-table-column align="center" label="烘焙度" prop="roastDegree" show-overflow-tooltip width="100"/>
            <el-table-column align="center" label="风味特点" prop="flavor" width="100">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.flavor" size="mini" type="text"
                               @click="handlePreview('风味特点', scope.row.flavor, false)">查看
                    </el-button>
                    <span v-else>-</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="商品摘要" prop="productSummary" width="90">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.productSummary" size="mini" type="text"
                               @click="handlePreview('商品摘要', scope.row.productSummary, false)">查看
                    </el-button>
                    <span v-else>-</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="冲煮概述" width="90">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.brewingMethod" size="mini" type="text"
                               @click="handlePreview('推荐冲煮方法', scope.row.brewingMethod, false)">查看
                    </el-button>
                    <span v-else>-</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="粉量" prop="dosage" show-overflow-tooltip width="100"/>
            <el-table-column align="center" label="水温" prop="waterTemp" show-overflow-tooltip width="100"/>
            <el-table-column align="center" label="萃取时间" prop="extractionTime" show-overflow-tooltip width="100"/>
            <el-table-column align="center" label="粉水比" prop="powderWaterRatio" show-overflow-tooltip width="90"/>
            <el-table-column align="center" label="规格" prop="specification" show-overflow-tooltip width="120"/>
            <el-table-column align="center" label="包装说明" width="90">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.packageDesc" size="mini" type="text"
                               @click="handlePreview('包装说明', scope.row.packageDesc, false)">查看
                    </el-button>
                    <span v-else>-</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="添加物" width="80">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.additives" size="mini" type="text"
                               @click="handlePreview('添加物说明', scope.row.additives, false)">查看
                    </el-button>
                    <span v-else>-</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="商品详情" width="90">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.productDescription" size="mini" type="text"
                               @click="handlePreview('商品详情正文', scope.row.productDescription, true)">预览
                    </el-button>
                    <span v-else>-</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="产品特色" width="90">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.productFeatures" size="mini" type="text"
                               @click="handlePreview('产品特色', scope.row.productFeatures, true)">预览
                    </el-button>
                    <span v-else>-</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="雷达图" prop="radarImage" width="80">
                <template slot-scope="scope">
                    <image-preview v-if="scope.row.radarImage" :height="50" :src="scope.row.radarImage" :width="50"/>
                    <span v-else>-</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="购买链接" width="90">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.purchaseUrl" size="mini" type="text"
                               @click="handlePreview('购买链接', scope.row.purchaseUrl, false)">查看
                    </el-button>
                    <span v-else>-</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="SEO标题" prop="seoTitle" show-overflow-tooltip width="130"/>
            <el-table-column align="center" label="SEO描述" width="90">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.seoDescription" size="mini" type="text"
                               @click="handlePreview('SEO描述', scope.row.seoDescription, true)">查看
                    </el-button>
                    <span v-else>-</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="分享图" prop="ogImage" width="80">
                <template slot-scope="scope">
                    <image-preview v-if="scope.row.ogImage" :height="50" :src="scope.row.ogImage" :width="50"/>
                    <span v-else>-</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="推荐" prop="isFeatured" width="70">
                <template slot-scope="scope">
                    <el-tag :type="scope.row.isFeatured === '1' ? 'success' : 'info'" size="mini">
                        {{ scope.row.isFeatured === '1' ? '是' : '否' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column align="center" label="排序" prop="productSort" width="70"/>
            <el-table-column align="center" label="状态" prop="productStatus" width="80">
                <template slot-scope="scope">
                    <el-tag :type="scope.row.productStatus === '0' ? 'success' : 'danger'" size="mini">
                        {{ scope.row.productStatus === '0' ? '正常' : '停用' }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column align="center" label="发布时间" prop="publishTime" width="110">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.publishTime, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" fixed="right" label="操作" width="120">
                <template slot-scope="scope">
                    <el-button v-hasPermi="['cafe:product:edit']" icon="el-icon-edit" size="mini" type="text"
                               @click="handleUpdate(scope.row)">修改
                    </el-button>
                    <el-button v-hasPermi="['cafe:product:remove']" icon="el-icon-delete" size="mini" type="text"
                               @click="handleDelete(scope.row)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页 -->
        <pagination v-show="total > 0" :limit.sync="queryParams.pageSize" :page.sync="queryParams.pageNum"
                    :total="total" @pagination="getList"/>

        <!-- 文本/HTML 内容预览弹窗 -->
        <el-dialog :title="previewTitle" :visible.sync="previewVisible" append-to-body width="700px">
            <div v-if="previewIsHtml" class="preview-html-content" v-html="previewContent"/>
            <div v-else class="preview-text-content">{{ previewContent }}</div>
            <div slot="footer">
                <el-button @click="previewVisible = false">关 闭</el-button>
            </div>
        </el-dialog>

        <!-- 新增/修改对话框 -->
        <el-dialog :title="title" :visible.sync="open" append-to-body class="product-dialog" width="680px">
            <el-form ref="form" :model="form" :rules="rules" label-width="110px">

                <el-divider content-position="left">基本信息</el-divider>
                <el-row :gutter="16">
                    <el-col :span="12">
                        <el-form-item label="产品线ID" prop="lineId">
                            <el-input v-model="form.lineId" placeholder="请输入产品线ID"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="URL编码" prop="productSlug">
                            <el-input v-model="form.productSlug" placeholder="如: ethiopia-geisha"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="16">
                    <el-col :span="16">
                        <el-form-item label="商品完整名称" prop="productName">
                            <el-input v-model="form.productName" placeholder="请输入商品完整名称"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="短名称" prop="productShortName">
                            <el-input v-model="form.productShortName" placeholder="短名称"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="16">
                    <el-col :span="12">
                        <el-form-item label="商品标签" prop="productLabel">
                            <el-input v-model="form.productLabel" placeholder="如: 精品咖啡"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="是否推荐" prop="isFeatured">
                            <el-radio-group v-model="form.isFeatured">
                                <el-radio label="1">是</el-radio>
                                <el-radio label="0">否</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="商品摘要" prop="productSummary">
                    <el-input v-model="form.productSummary" :rows="2" placeholder="请输入商品摘要" type="textarea"/>
                </el-form-item>

                <el-divider content-position="left">产品信息</el-divider>
                <el-row :gutter="16">
                    <el-col :span="12">
                        <el-form-item label="产地/庄园" prop="origin">
                            <el-input v-model="form.origin" placeholder="请输入产地或庄园"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="海拔高度" prop="altitude">
                            <el-input v-model="form.altitude" placeholder="如: 1900-2100m"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="16">
                    <el-col :span="12">
                        <el-form-item label="咖啡品种" prop="variety">
                            <el-input v-model="form.variety" placeholder="如: 瑰夏"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="烘焙度" prop="roastDegree">
                            <el-input v-model="form.roastDegree" placeholder="如: 浅烘"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="处理方式" prop="treatment">
                    <el-input v-model="form.treatment" :rows="2" placeholder="请输入处理方式" type="textarea"/>
                </el-form-item>
                <el-form-item label="风味特点" prop="flavor">
                    <el-input v-model="form.flavor" :rows="2" placeholder="请输入风味特点" type="textarea"/>
                </el-form-item>

                <el-divider content-position="left">冲煮参数</el-divider>
                <el-form-item label="冲煮方法概述" prop="brewingMethod">
                    <el-input v-model="form.brewingMethod" :rows="2" placeholder="请输入推荐冲煮方法概述"
                              type="textarea"/>
                </el-form-item>
                <el-row :gutter="16">
                    <el-col :span="8">
                        <el-form-item label="建议粉量" prop="dosage">
                            <el-input v-model="form.dosage" placeholder="如: 15g"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="建议水温" prop="waterTemp">
                            <el-input v-model="form.waterTemp" placeholder="如: 93℃"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="萃取时间" prop="extractionTime">
                            <el-input v-model="form.extractionTime" placeholder="如: 2:30"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="16">
                    <el-col :span="12">
                        <el-form-item label="粉水比" prop="powderWaterRatio">
                            <el-input v-model="form.powderWaterRatio" placeholder="如: 1:15"/>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-divider content-position="left">规格与包装</el-divider>
                <el-row :gutter="16">
                    <el-col :span="12">
                        <el-form-item label="规格概述" prop="specification">
                            <el-input v-model="form.specification" placeholder="如: 227g / 454g"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="包装说明" prop="packageDesc">
                    <el-input v-model="form.packageDesc" :rows="2" placeholder="请输入包装说明" type="textarea"/>
                </el-form-item>
                <el-form-item label="添加物说明" prop="additives">
                    <el-input v-model="form.additives" :rows="2" placeholder="请输入添加物说明" type="textarea"/>
                </el-form-item>

                <el-divider content-position="left">图片与媒体</el-divider>
                <el-row :gutter="16">
                    <el-col :span="12">
                        <el-form-item label="商品主图" prop="primaryImage">
                            <image-upload v-model="form.primaryImage"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="风味雷达图" prop="radarImage">
                            <image-upload v-model="form.radarImage"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="16">
                    <el-col :span="12">
                        <el-form-item label="社交分享图片" prop="ogImage">
                            <image-upload v-model="form.ogImage"/>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-divider content-position="left">详情内容</el-divider>
                <el-form-item label="商品详情正文">
                    <editor v-model="form.productDescription" :min-height="192"/>
                </el-form-item>
                <el-form-item label="产品特色" prop="productFeatures">
                    <el-input v-model="form.productFeatures" :rows="3" placeholder="建议输入JSON数组或HTML"
                              type="textarea"/>
                </el-form-item>

                <el-divider content-position="left">购买与SEO</el-divider>
                <el-form-item label="默认购买链接" prop="purchaseUrl">
                    <el-input v-model="form.purchaseUrl" :rows="2" placeholder="请输入外部购买链接" type="textarea"/>
                </el-form-item>
                <el-form-item label="SEO标题" prop="seoTitle">
                    <el-input v-model="form.seoTitle" placeholder="请输入SEO标题"/>
                </el-form-item>
                <el-form-item label="SEO描述">
                    <editor v-model="form.seoDescription" :min-height="120"/>
                </el-form-item>

                <el-divider content-position="left">发布设置</el-divider>
                <el-row :gutter="16">
                    <el-col :span="12">
                        <el-form-item label="产品线内排序" prop="productSort">
                            <el-input-number v-model="form.productSort" :min="0" controls-position="right"
                                             style="width:100%"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="发布时间" prop="publishTime">
                            <el-date-picker v-model="form.publishTime" clearable placeholder="请选择发布时间"
                                            style="width:100%" type="date" value-format="yyyy-MM-dd"/>
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
import {addProduct, delProduct, getProduct, listProduct, updateProduct} from '@/api/cafe/product'

export default {
    name: 'Product',
    dicts: [[]],
    data() {
        return {
            loading: true,
            ids: [],
            single: true,
            multiple: true,
            showSearch: true,
            total: 0,
            productList: [],
            title: '',
            open: false,
            // 预览弹窗
            previewVisible: false,
            previewTitle: '',
            previewContent: '',
            previewIsHtml: false,
            // 查询参数（只保留有意义的筛选项）
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                lineId: null,
                productSlug: null,
                productName: null,
                origin: null,
                variety: null,
                roastDegree: null,
                isFeatured: null,
                productStatus: null,
                publishTime: null,
            },
            form: {},
            rules: {
                lineId: [{required: true, message: '产品线ID不能为空', trigger: 'blur'}],
                productSlug: [{required: true, message: 'URL编码不能为空', trigger: 'blur'}],
                productName: [{required: true, message: '商品完整名称不能为空', trigger: 'blur'}],
                flavor: [{required: true, message: '风味特点不能为空', trigger: 'blur'}],
                origin: [{required: true, message: '产地/庄园不能为空', trigger: 'blur'}],
                altitude: [{required: true, message: '海拔高度不能为空', trigger: 'blur'}],
                variety: [{required: true, message: '咖啡品种不能为空', trigger: 'blur'}],
                treatment: [{required: true, message: '处理方式不能为空', trigger: 'blur'}],
                primaryImage: [{required: true, message: '商品主图不能为空', trigger: 'blur'}],
            }
        }
    },
    created() {
        this.getList()
    },
    methods: {
        getList() {
            this.loading = true
            listProduct(this.queryParams).then(response => {
                this.productList = response.rows
                this.total = response.total
                this.loading = false
            })
        },
        // 打开内容预览弹窗
        handlePreview(title, content, isHtml) {
            this.previewTitle = title
            this.previewContent = content
            this.previewIsHtml = isHtml
            this.previewVisible = true
        },
        cancel() {
            this.open = false
            this.reset()
        },
        reset() {
            this.form = {
                productId: null,
                lineId: null,
                productSlug: null,
                productName: null,
                productShortName: null,
                productLabel: null,
                productSummary: null,
                flavor: null,
                origin: null,
                altitude: null,
                variety: null,
                treatment: null,
                roastDegree: null,
                brewingMethod: null,
                dosage: null,
                extractionTime: null,
                waterTemp: null,
                powderWaterRatio: null,
                specification: null,
                packageDesc: null,
                additives: null,
                productDescription: null,
                productFeatures: null,
                primaryImage: null,
                radarImage: null,
                purchaseUrl: null,
                seoTitle: null,
                seoDescription: null,
                ogImage: null,
                isFeatured: '0',
                productSort: 0,
                productStatus: null,
                publishTime: null,
            }
            this.resetForm('form')
        },
        handleQuery() {
            this.queryParams.pageNum = 1
            this.getList()
        },
        resetQuery() {
            this.resetForm('queryForm')
            this.handleQuery()
        },
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.productId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        handleAdd() {
            this.reset()
            this.open = true
            this.title = '添加咖啡商品'
        },
        handleUpdate(row) {
            this.reset()
            const productId = row.productId || this.ids
            getProduct(productId).then(response => {
                this.form = response.data
                this.open = true
                this.title = '修改咖啡商品'
            })
        },
        submitForm() {
            this.$refs['form'].validate(valid => {
                if (valid) {
                    if (this.form.productId != null) {
                        updateProduct(this.form).then(() => {
                            this.$modal.msgSuccess('修改成功')
                            this.open = false
                            this.getList()
                        })
                    } else {
                        addProduct(this.form).then(() => {
                            this.$modal.msgSuccess('新增成功')
                            this.open = false
                            this.getList()
                        })
                    }
                }
            })
        },
        handleDelete(row) {
            const productIds = row.productId || this.ids
            this.$modal.confirm('是否确认删除编号为"' + productIds + '"的商品？').then(() => {
                return delProduct(productIds)
            }).then(() => {
                this.getList()
                this.$modal.msgSuccess('删除成功')
            }).catch(() => {
            })
        },
        handleExport() {
            this.download('cafe/product/export', {...this.queryParams}, `product_${new Date().getTime()}.xlsx`)
        }
    }
}
</script>

<style scoped>
/* 预览弹窗：纯文本 */
.preview-text-content {
    white-space: pre-wrap;
    word-break: break-all;
    line-height: 1.7;
    max-height: 500px;
    overflow-y: auto;
    padding: 8px 4px;
    font-size: 14px;
    color: #333;
}

/* 预览弹窗：HTML渲染 */
.preview-html-content {
    max-height: 500px;
    overflow-y: auto;
    padding: 8px 4px;
    font-size: 14px;
    line-height: 1.7;
    color: #333;
}

/* 编辑弹窗内分区标题 */
.product-dialog .el-divider__text {
    font-size: 13px;
    color: #666;
    font-weight: 600;
}
</style>
