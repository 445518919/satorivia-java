<template>
    <div class="app-container">
        <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px"
                 size="small">
            <el-form-item label="产品线id，关联cafe_product_line.line_id" prop="lineId">
                <el-input
                    v-model="queryParams.lineId"
                    clearable
                    placeholder="请输入产品线id，关联cafe_product_line.line_id"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="URL商品编码，对应前端id" prop="productSlug">
                <el-input
                    v-model="queryParams.productSlug"
                    clearable
                    placeholder="请输入URL商品编码，对应前端id"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="商品完整名称" prop="productName">
                <el-input
                    v-model="queryParams.productName"
                    clearable
                    placeholder="请输入商品完整名称"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="商品短名称" prop="productShortName">
                <el-input
                    v-model="queryParams.productShortName"
                    clearable
                    placeholder="请输入商品短名称"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="商品标签" prop="productLabel">
                <el-input
                    v-model="queryParams.productLabel"
                    clearable
                    placeholder="请输入商品标签"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="产地/庄园" prop="origin">
                <el-input
                    v-model="queryParams.origin"
                    clearable
                    placeholder="请输入产地/庄园"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="海拔高度" prop="altitude">
                <el-input
                    v-model="queryParams.altitude"
                    clearable
                    placeholder="请输入海拔高度"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="咖啡品种" prop="variety">
                <el-input
                    v-model="queryParams.variety"
                    clearable
                    placeholder="请输入咖啡品种"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="烘焙度" prop="roastDegree">
                <el-input
                    v-model="queryParams.roastDegree"
                    clearable
                    placeholder="请输入烘焙度"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="建议粉量/用量" prop="dosage">
                <el-input
                    v-model="queryParams.dosage"
                    clearable
                    placeholder="请输入建议粉量/用量"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="萃取时间" prop="extractionTime">
                <el-input
                    v-model="queryParams.extractionTime"
                    clearable
                    placeholder="请输入萃取时间"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="建议水温" prop="waterTemp">
                <el-input
                    v-model="queryParams.waterTemp"
                    clearable
                    placeholder="请输入建议水温"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="粉水比" prop="powderWaterRatio">
                <el-input
                    v-model="queryParams.powderWaterRatio"
                    clearable
                    placeholder="请输入粉水比"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="规格概述" prop="specification">
                <el-input
                    v-model="queryParams.specification"
                    clearable
                    placeholder="请输入规格概述"
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
            <el-form-item label="是否推荐" prop="isFeatured">
                <el-input
                    v-model="queryParams.isFeatured"
                    clearable
                    placeholder="请输入是否推荐"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="产品线内排序" prop="productSort">
                <el-input
                    v-model="queryParams.productSort"
                    clearable
                    placeholder="请输入产品线内排序"
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
                    v-hasPermi="['cafe:product:add']"
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
                    v-hasPermi="['cafe:product:edit']"
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
                    v-hasPermi="['cafe:product:remove']"
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
                    v-hasPermi="['cafe:product:export']"
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

        <el-table v-loading="loading" :data="productList" @selection-change="handleSelectionChange">
            <el-table-column align="center" type="selection" width="55"/>
            <el-table-column align="center" label="商品id" prop="productId"/>
            <el-table-column align="center" label="产品线id，关联cafe_product_line.line_id" prop="lineId"/>
            <el-table-column align="center" label="URL商品编码，对应前端id" prop="productSlug"/>
            <el-table-column align="center" label="商品完整名称" prop="productName"/>
            <el-table-column align="center" label="商品短名称" prop="productShortName"/>
            <el-table-column align="center" label="商品标签" prop="productLabel"/>
            <el-table-column align="center" label="商品摘要" prop="productSummary"/>
            <el-table-column align="center" label="风味特点" prop="flavor"/>
            <el-table-column align="center" label="产地/庄园" prop="origin"/>
            <el-table-column align="center" label="海拔高度" prop="altitude"/>
            <el-table-column align="center" label="咖啡品种" prop="variety"/>
            <el-table-column align="center" label="处理方式" prop="treatment"/>
            <el-table-column align="center" label="烘焙度" prop="roastDegree"/>
            <el-table-column align="center" label="推荐冲煮方法概述" prop="brewingMethod"/>
            <el-table-column align="center" label="建议粉量/用量" prop="dosage"/>
            <el-table-column align="center" label="萃取时间" prop="extractionTime"/>
            <el-table-column align="center" label="建议水温" prop="waterTemp"/>
            <el-table-column align="center" label="粉水比" prop="powderWaterRatio"/>
            <el-table-column align="center" label="规格概述" prop="specification"/>
            <el-table-column align="center" label="包装说明" prop="packageDesc"/>
            <el-table-column align="center" label="添加物说明" prop="additives"/>
            <el-table-column align="center" label="商品详情正文" prop="productDescription"/>
            <el-table-column align="center" label="产品特色，建议存JSON数组或HTML" prop="productFeatures"/>
            <el-table-column align="center" label="商品主图/产品线列表封面" prop="primaryImage" width="100">
                <template slot-scope="scope">
                    <image-preview :height="50" :src="scope.row.primaryImage" :width="50"/>
                </template>
            </el-table-column>
            <el-table-column align="center" label="风味雷达图" prop="radarImage" width="100">
                <template slot-scope="scope">
                    <image-preview :height="50" :src="scope.row.radarImage" :width="50"/>
                </template>
            </el-table-column>
            <el-table-column align="center" label="默认外部购买链接" prop="purchaseUrl"/>
            <el-table-column align="center" label="SEO标题" prop="seoTitle"/>
            <el-table-column align="center" label="SEO描述" prop="seoDescription"/>
            <el-table-column align="center" label="社交分享图片" prop="ogImage" width="100">
                <template slot-scope="scope">
                    <image-preview :height="50" :src="scope.row.ogImage" :width="50"/>
                </template>
            </el-table-column>
            <el-table-column align="center" label="是否推荐" prop="isFeatured"/>
            <el-table-column align="center" label="产品线内排序" prop="productSort"/>
            <el-table-column align="center" label="状态" prop="productStatus"/>
            <el-table-column align="center" label="发布时间" prop="publishTime" width="180">
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.publishTime, '{y}-{m}-{d}') }}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
                <template slot-scope="scope">
                    <el-button
                        v-hasPermi="['cafe:product:edit']"
                        icon="el-icon-edit"
                        size="mini"
                        type="text"
                        @click="handleUpdate(scope.row)"
                    >修改
                    </el-button>
                    <el-button
                        v-hasPermi="['cafe:product:remove']"
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

        <!-- 添加或修改咖啡商品主对话框 -->
        <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="产品线id，关联cafe_product_line.line_id" prop="lineId">
                    <el-input v-model="form.lineId" placeholder="请输入产品线id，关联cafe_product_line.line_id"/>
                </el-form-item>
                <el-form-item label="URL商品编码，对应前端id" prop="productSlug">
                    <el-input v-model="form.productSlug" placeholder="请输入URL商品编码，对应前端id"/>
                </el-form-item>
                <el-form-item label="商品完整名称" prop="productName">
                    <el-input v-model="form.productName" placeholder="请输入商品完整名称"/>
                </el-form-item>
                <el-form-item label="商品短名称" prop="productShortName">
                    <el-input v-model="form.productShortName" placeholder="请输入商品短名称"/>
                </el-form-item>
                <el-form-item label="商品标签" prop="productLabel">
                    <el-input v-model="form.productLabel" placeholder="请输入商品标签"/>
                </el-form-item>
                <el-form-item label="商品摘要" prop="productSummary">
                    <el-input v-model="form.productSummary" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="风味特点" prop="flavor">
                    <el-input v-model="form.flavor" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="产地/庄园" prop="origin">
                    <el-input v-model="form.origin" placeholder="请输入产地/庄园"/>
                </el-form-item>
                <el-form-item label="海拔高度" prop="altitude">
                    <el-input v-model="form.altitude" placeholder="请输入海拔高度"/>
                </el-form-item>
                <el-form-item label="咖啡品种" prop="variety">
                    <el-input v-model="form.variety" placeholder="请输入咖啡品种"/>
                </el-form-item>
                <el-form-item label="处理方式" prop="treatment">
                    <el-input v-model="form.treatment" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="烘焙度" prop="roastDegree">
                    <el-input v-model="form.roastDegree" placeholder="请输入烘焙度"/>
                </el-form-item>
                <el-form-item label="推荐冲煮方法概述" prop="brewingMethod">
                    <el-input v-model="form.brewingMethod" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="建议粉量/用量" prop="dosage">
                    <el-input v-model="form.dosage" placeholder="请输入建议粉量/用量"/>
                </el-form-item>
                <el-form-item label="萃取时间" prop="extractionTime">
                    <el-input v-model="form.extractionTime" placeholder="请输入萃取时间"/>
                </el-form-item>
                <el-form-item label="建议水温" prop="waterTemp">
                    <el-input v-model="form.waterTemp" placeholder="请输入建议水温"/>
                </el-form-item>
                <el-form-item label="粉水比" prop="powderWaterRatio">
                    <el-input v-model="form.powderWaterRatio" placeholder="请输入粉水比"/>
                </el-form-item>
                <el-form-item label="规格概述" prop="specification">
                    <el-input v-model="form.specification" placeholder="请输入规格概述"/>
                </el-form-item>
                <el-form-item label="包装说明" prop="packageDesc">
                    <el-input v-model="form.packageDesc" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="添加物说明" prop="additives">
                    <el-input v-model="form.additives" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="商品详情正文">
                    <editor v-model="form.productDescription" :min-height="192"/>
                </el-form-item>
                <el-form-item label="产品特色，建议存JSON数组或HTML" prop="productFeatures">
                    <el-input v-model="form.productFeatures" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="商品主图/产品线列表封面" prop="primaryImage">
                    <image-upload v-model="form.primaryImage"/>
                </el-form-item>
                <el-form-item label="风味雷达图" prop="radarImage">
                    <image-upload v-model="form.radarImage"/>
                </el-form-item>
                <el-form-item label="默认外部购买链接" prop="purchaseUrl">
                    <el-input v-model="form.purchaseUrl" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="SEO标题" prop="seoTitle">
                    <el-input v-model="form.seoTitle" placeholder="请输入SEO标题"/>
                </el-form-item>
                <el-form-item label="SEO描述">
                    <editor v-model="form.seoDescription" :min-height="192"/>
                </el-form-item>
                <el-form-item label="社交分享图片" prop="ogImage">
                    <image-upload v-model="form.ogImage"/>
                </el-form-item>
                <el-form-item label="是否推荐" prop="isFeatured">
                    <el-input v-model="form.isFeatured" placeholder="请输入是否推荐"/>
                </el-form-item>
                <el-form-item label="产品线内排序" prop="productSort">
                    <el-input v-model="form.productSort" placeholder="请输入产品线内排序"/>
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
import {addProduct, delProduct, getProduct, listProduct, updateProduct} from "@/api/cafe/product"

export default {
    name: "Product",
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
            // 咖啡商品主表格数据
            productList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
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
                isFeatured: null,
                productSort: null,
                productStatus: null,
                publishTime: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                lineId: [
                    {required: true, message: "产品线id，关联cafe_product_line.line_id不能为空", trigger: "blur"}
                ],
                productSlug: [
                    {required: true, message: "URL商品编码，对应前端id不能为空", trigger: "blur"}
                ],
                productName: [
                    {required: true, message: "商品完整名称不能为空", trigger: "blur"}
                ],
                flavor: [
                    {required: true, message: "风味特点不能为空", trigger: "blur"}
                ],
                origin: [
                    {required: true, message: "产地/庄园不能为空", trigger: "blur"}
                ],
                altitude: [
                    {required: true, message: "海拔高度不能为空", trigger: "blur"}
                ],
                variety: [
                    {required: true, message: "咖啡品种不能为空", trigger: "blur"}
                ],
                treatment: [
                    {required: true, message: "处理方式不能为空", trigger: "blur"}
                ],
                primaryImage: [
                    {required: true, message: "商品主图/产品线列表封面不能为空", trigger: "blur"}
                ],
            }
        }
    },
    created() {
        this.getList()
    },
    methods: {
        /** 查询咖啡商品主列表 */
        getList() {
            this.loading = true
            listProduct(this.queryParams).then(response => {
                this.productList = response.rows
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
                isFeatured: null,
                productSort: null,
                productStatus: null,
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
            this.ids = selection.map(item => item.productId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset()
            this.open = true
            this.title = "添加咖啡商品主"
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset()
            const productId = row.productId || this.ids
            getProduct(productId).then(response => {
                this.form = response.data
                this.open = true
                this.title = "修改咖啡商品主"
            })
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.productId != null) {
                        updateProduct(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功")
                            this.open = false
                            this.getList()
                        })
                    } else {
                        addProduct(this.form).then(response => {
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
            const productIds = row.productId || this.ids
            this.$modal.confirm('是否确认删除咖啡商品主编号为"' + productIds + '"的数据项？').then(function () {
                return delProduct(productIds)
            }).then(() => {
                this.getList()
                this.$modal.msgSuccess("删除成功")
            }).catch(() => {
            })
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('cafe/product/export', {
                ...this.queryParams
            }, `product_${new Date().getTime()}.xlsx`)
        }
    }
}
</script>
