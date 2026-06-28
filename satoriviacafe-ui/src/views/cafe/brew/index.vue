<template>
    <div class="app-container">
        <el-form v-show="showSearch" ref="queryForm" :inline="true" :model="queryParams" label-width="68px"
                 size="small">
            <el-form-item label="商品id" prop="productId">
                <el-input
                    v-model="queryParams.productId"
                    clearable
                    placeholder="请输入商品id"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="方案名称，例如推荐参数、V60" prop="brewName">
                <el-input
                    v-model="queryParams.brewName"
                    clearable
                    placeholder="请输入方案名称，例如推荐参数、V60"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="冲煮方式/器具" prop="brewMethod">
                <el-input
                    v-model="queryParams.brewMethod"
                    clearable
                    placeholder="请输入冲煮方式/器具"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="粉量" prop="powderQuantity">
                <el-input
                    v-model="queryParams.powderQuantity"
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
            <el-form-item label="粉水比" prop="powderWaterRatio">
                <el-input
                    v-model="queryParams.powderWaterRatio"
                    clearable
                    placeholder="请输入粉水比"
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
            <el-form-item label="目标总时间" prop="targetTime">
                <el-input
                    v-model="queryParams.targetTime"
                    clearable
                    placeholder="请输入目标总时间"
                    @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="显示顺序" prop="brewSort">
                <el-input
                    v-model="queryParams.brewSort"
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
                    v-hasPermi="['cafe:brew:add']"
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
                    v-hasPermi="['cafe:brew:edit']"
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
                    v-hasPermi="['cafe:brew:remove']"
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
                    v-hasPermi="['cafe:brew:export']"
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

        <el-table v-loading="loading" :data="brewList" @selection-change="handleSelectionChange">
            <el-table-column align="center" type="selection" width="55"/>
            <el-table-column align="center" label="冲煮方案id" prop="brewId"/>
            <el-table-column align="center" label="商品id" prop="productId"/>
            <el-table-column align="center" label="方案名称，例如推荐参数、V60" prop="brewName"/>
            <el-table-column align="center" label="冲煮方式/器具" prop="brewMethod"/>
            <el-table-column align="center" label="粉量" prop="powderQuantity"/>
            <el-table-column align="center" label="水量" prop="waterVolume"/>
            <el-table-column align="center" label="水温" prop="waterTemp"/>
            <el-table-column align="center" label="研磨度" prop="grindDegree"/>
            <el-table-column align="center" label="粉水比" prop="powderWaterRatio"/>
            <el-table-column align="center" label="萃取时间" prop="extractionTime"/>
            <el-table-column align="center" label="目标总时间" prop="targetTime"/>
            <el-table-column align="center" label="准备工作" prop="preparations"/>
            <el-table-column align="center" label="冲煮步骤" prop="brewSteps"/>
            <el-table-column align="center" label="显示顺序" prop="brewSort"/>
            <el-table-column align="center" label="状态" prop="brewStatus"/>
            <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
                <template slot-scope="scope">
                    <el-button
                        v-hasPermi="['cafe:brew:edit']"
                        icon="el-icon-edit"
                        size="mini"
                        type="text"
                        @click="handleUpdate(scope.row)"
                    >修改
                    </el-button>
                    <el-button
                        v-hasPermi="['cafe:brew:remove']"
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

        <!-- 添加或修改咖啡商品冲煮方案对话框 -->
        <el-dialog :title="title" :visible.sync="open" append-to-body width="500px">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="商品id" prop="productId">
                    <el-input v-model="form.productId" placeholder="请输入商品id"/>
                </el-form-item>
                <el-form-item label="方案名称，例如推荐参数、V60" prop="brewName">
                    <el-input v-model="form.brewName" placeholder="请输入方案名称，例如推荐参数、V60"/>
                </el-form-item>
                <el-form-item label="冲煮方式/器具" prop="brewMethod">
                    <el-input v-model="form.brewMethod" placeholder="请输入冲煮方式/器具"/>
                </el-form-item>
                <el-form-item label="粉量" prop="powderQuantity">
                    <el-input v-model="form.powderQuantity" placeholder="请输入粉量"/>
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
                <el-form-item label="粉水比" prop="powderWaterRatio">
                    <el-input v-model="form.powderWaterRatio" placeholder="请输入粉水比"/>
                </el-form-item>
                <el-form-item label="萃取时间" prop="extractionTime">
                    <el-input v-model="form.extractionTime" placeholder="请输入萃取时间"/>
                </el-form-item>
                <el-form-item label="目标总时间" prop="targetTime">
                    <el-input v-model="form.targetTime" placeholder="请输入目标总时间"/>
                </el-form-item>
                <el-form-item label="准备工作" prop="preparations">
                    <el-input v-model="form.preparations" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="冲煮步骤" prop="brewSteps">
                    <el-input v-model="form.brewSteps" placeholder="请输入内容" type="textarea"/>
                </el-form-item>
                <el-form-item label="显示顺序" prop="brewSort">
                    <el-input v-model="form.brewSort" placeholder="请输入显示顺序"/>
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
import {addBrew, delBrew, getBrew, listBrew, updateBrew} from "@/api/cafe/brew"

export default {
    name: "Brew",
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
            // 咖啡商品冲煮方案表格数据
            brewList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                productId: null,
                brewName: null,
                brewMethod: null,
                powderQuantity: null,
                waterVolume: null,
                waterTemp: null,
                grindDegree: null,
                powderWaterRatio: null,
                extractionTime: null,
                targetTime: null,
                preparations: null,
                brewSteps: null,
                brewSort: null,
                brewStatus: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                productId: [
                    {required: true, message: "商品id不能为空", trigger: "blur"}
                ],
                brewName: [
                    {required: true, message: "方案名称，例如推荐参数、V60不能为空", trigger: "blur"}
                ],
            }
        }
    },
    created() {
        this.getList()
    },
    methods: {
        /** 查询咖啡商品冲煮方案列表 */
        getList() {
            this.loading = true
            listBrew(this.queryParams).then(response => {
                this.brewList = response.rows
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
                brewId: null,
                productId: null,
                brewName: null,
                brewMethod: null,
                powderQuantity: null,
                waterVolume: null,
                waterTemp: null,
                grindDegree: null,
                powderWaterRatio: null,
                extractionTime: null,
                targetTime: null,
                preparations: null,
                brewSteps: null,
                brewSort: null,
                brewStatus: null,
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
            this.ids = selection.map(item => item.brewId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset()
            this.open = true
            this.title = "添加咖啡商品冲煮方案"
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset()
            const brewId = row.brewId || this.ids
            getBrew(brewId).then(response => {
                this.form = response.data
                this.open = true
                this.title = "修改咖啡商品冲煮方案"
            })
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.brewId != null) {
                        updateBrew(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功")
                            this.open = false
                            this.getList()
                        })
                    } else {
                        addBrew(this.form).then(response => {
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
            const brewIds = row.brewId || this.ids
            this.$modal.confirm('是否确认删除咖啡商品冲煮方案编号为"' + brewIds + '"的数据项？').then(function () {
                return delBrew(brewIds)
            }).then(() => {
                this.getList()
                this.$modal.msgSuccess("删除成功")
            }).catch(() => {
            })
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('cafe/brew/export', {
                ...this.queryParams
            }, `brew_${new Date().getTime()}.xlsx`)
        }
    }
}
</script>
