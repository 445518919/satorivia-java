<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="产品名称" prop="prodName">
        <el-input
          v-model="queryParams.prodName"
          placeholder="请输入产品名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品描述" prop="prodDesc">
        <el-input
          v-model="queryParams.prodDesc"
          placeholder="请输入产品描述"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品图片" prop="prodImg">
        <el-input
          v-model="queryParams.prodImg"
          placeholder="请输入产品图片"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品价格" prop="prodPrice">
        <el-input
          v-model="queryParams.prodPrice"
          placeholder="请输入产品价格"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="显示顺序" prop="orderNum">
        <el-input
          v-model="queryParams.orderNum"
          placeholder="请输入显示顺序"
          clearable
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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['cafe:production:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['cafe:production:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['cafe:production:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['cafe:production:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="产品id" align="center" prop="prodId" />
      <el-table-column label="产品名称" align="center" prop="prodName" />
      <el-table-column label="产品描述" align="center" prop="prodDesc" />
      <el-table-column label="产品图片" align="center" prop="prodImg" />
      <el-table-column label="产品价格" align="center" prop="prodPrice" />
      <el-table-column label="产品详情" align="center" prop="prodText" />
      <el-table-column label="显示顺序" align="center" prop="orderNum" />
      <el-table-column label="产品状态" align="center" prop="prodStatus" />
        <el-table-column align="center" label="商品编码" prop="prodCode"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['cafe:production:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['cafe:production:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改产品对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="产品名称" prop="prodName">
          <el-input v-model="form.prodName" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="产品描述" prop="prodDesc">
          <el-input v-model="form.prodDesc" placeholder="请输入产品描述" />
        </el-form-item>
        <el-form-item label="产品图片" prop="prodImg">
          <el-input v-model="form.prodImg" placeholder="请输入产品图片" />
        </el-form-item>
        <el-form-item label="产品价格" prop="prodPrice">
          <el-input v-model="form.prodPrice" placeholder="请输入产品价格" />
        </el-form-item>
        <el-form-item label="产品详情" prop="prodText">
          <el-input v-model="form.prodText" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入显示顺序" />
        </el-form-item>
        <el-form-item label="删除时间" prop="deleteAt">
          <el-date-picker clearable
            v-model="form.deleteAt"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择删除时间">
          </el-date-picker>
        </el-form-item>
          <el-form-item label="商品编码" prop="prodCode">
              <el-input v-model="form.prodCode" placeholder="请输入商品编码"/>
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
import {addProduction, delProduction, getProduction, listProduction, updateProduction} from "@/api/cafe/production"

export default {
  name: "Production",
  dicts: [[], ],
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
      // 产品表格数据
      productionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        prodName: null,
        prodDesc: null,
        prodImg: null,
        prodPrice: null,
        prodText: null,
        orderNum: null,
        prodStatus: null,
          prodCode: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询产品列表 */
    getList() {
      this.loading = true
      listProduction(this.queryParams).then(response => {
        this.productionList = response.rows
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
        prodId: null,
        prodName: null,
        prodDesc: null,
        prodImg: null,
        prodPrice: null,
        prodText: null,
        orderNum: null,
        prodStatus: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
          deleteAt: null,
          prodCode: null
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
      this.ids = selection.map(item => item.prodId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加产品"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const prodId = row.prodId || this.ids
      getProduction(prodId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改产品"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.prodId != null) {
            updateProduction(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addProduction(this.form).then(response => {
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
      const prodIds = row.prodId || this.ids
      this.$modal.confirm('是否确认删除产品编号为"' + prodIds + '"的数据项？').then(function() {
        return delProduction(prodIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cafe/production/export', {
        ...this.queryParams
      }, `production_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
