<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.mobile" clearable class="filter-item" style="width: 200px;" placeholder="请输入教师手机号"/>
      <el-input v-model="listQuery.username" type="password" clearable class="filter-item" style="width: 200px;" placeholder="请输入教师密码"/>
      <el-button v-permission="['GET /admin/course/list']" class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button v-permission="['POST /admin/course/create']" class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column align="center" label="课程ID" prop="id" sortable/>
      <el-table-column align="center" label="课程名称" prop="name"/>
      <el-table-column align="center" min-width="100px" label="上课地址" prop="address"/>
      <el-table-column align="center" min-width="200px" label="课程简介" prop="comment"/>

      <el-table-column align="center" label="操作" min-width="100px"  class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-permission="['POST /admin/course/update']" type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button v-permission="['POST /admin/course/delete']" type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
          <el-button v-permission="['POST /admin/course/delete']"  type="warning" size="mini" @click="handleDownload(scope.row)">导出</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="课程名称" prop="username">
          <el-input v-model="dataForm.name"/>
        </el-form-item>
        <el-form-item label="上课地址" prop="address">
          <el-input v-model="dataForm.address"/>
        </el-form-item>
        <el-form-item label="课程简介" prop="comment">
          <el-input :autosize="{ minRows: 6, maxRows: 18}"  v-model="dataForm.comment" type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #20a0ff;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 120px;
    height: 120px;
    line-height: 120px;
    text-align: center;
  }
  .avatar {
    width: 145px;
    height: 145px;
    display: block;
  }
</style>

<script>
  import { listCourse, createCourse, updateCourse, deleteCourse ,exportScore,exportExcel} from '@/api/course'
  import { getToken } from '@/utils/auth'
  import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
  import axios from 'axios'
  export default {
    name: 'class',
    components: { Pagination },
    data() {
      return {
        list: null,
        total: 0,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 20,
          username: undefined,
          mobile:undefined,
          sort: 'id',
          order: 'desc'
        },
        dataForm: {
          id: undefined,
          name: undefined,
          address:undefined,
          comment:undefined,
          userId:undefined,
          userName:undefined
        },
        teacher:undefined,
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '创建'
        },
        rules: {
          name: [
            { required: true, message: '名称不能为空', trigger: 'blur' }
          ]
        },
        downloadLoading: false
      }
    },
    computed: {
      headers() {
        return {
          'X-Litemall-Admin-Token': getToken()
        }
      }
    },
    created() {
      this.getList()
    },
    methods: {

      getList() {
        this.listLoading = true
        listCourse(this.listQuery)
          .then(response => {
            console.log(response)
            this.list = response.data.data.adminList.data.list
            this.total = response.data.data.adminList.data.total
            this.listLoading = false
            this.teacher=response.data.data.teacher
          })
          .catch(() => {
            this.list = []
            this.total = 0
            this.listLoading = false
            this.teacher=undefined
          })
      },
      handleFilter() {
        this.listQuery.page = 1
        this.getList()
      },
      resetForm() {
        id: undefined,
          this.dataForm = {
            id: undefined,
            name: undefined,
            address:undefined,
            comment:undefined,
            userId:undefined,
            userName:undefined
          }
      },
      uploadAvatar: function(response) {
        this.dataForm.avatar = response.data.url
      },
      handleCreate() {
        if(this.teacher==undefined){
          this.$message.warning('请先查找正确的教师信息')
          return
        }
        this.resetForm()
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      createData() {
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            this.dataForm.userId=this.teacher.id
            this.dataForm.userName=this.teacher.username
            createCourse(this.dataForm)
              .then(response => {
                this.list.unshift(response.data.data)
                this.dialogFormVisible = false
                this.$notify.success({
                  title: '成功',
                  message: '添加课程成功'
                })
              })
              .catch(response => {
                this.$notify.error({
                  title: '失败',
                  message: response.data.errmsg
                })
              })
          }
        })
      },
      handleUpdate(row) {
        this.dataForm = Object.assign({}, row)
        this.dataForm.userId=this.teacher.id
        this.dataForm.userName=this.teacher.username
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      updateData() {
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            updateCourse(this.dataForm)
              .then(() => {
                for (const v of this.list) {
                  if (v.id === this.dataForm.id) {
                    const index = this.list.indexOf(v)
                    this.list.splice(index, 1, this.dataForm)
                    break
                  }
                }
                this.dialogFormVisible = false
                this.$notify.success({
                  title: '成功',
                  message: '更新课程成功'
                })
              })
              .catch(response => {
                this.$notify.error({
                  title: '失败',
                  message: response.data.errmsg
                })
              })
          }
        })
      },
      handleDelete(row) {
        deleteCourse(row)
          .then(response => {
            this.$notify.success({
              title: '成功',
              message: '删除课程成功'
            })
            const index = this.list.indexOf(row)
            this.list.splice(index, 1)
          })
          .catch(response => {
            this.$notify.error({
              title: '失败',
              message: response.data.errmsg
            })
          })
      },


      handleDownload(row) {
        console.log(this.teacher,row)
        this.$confirm('您确定导出课程："'+row.name+'"的学生成绩吗？').then(e => {
          exportExcel({teacherid:this.teacher.id,courseid:row.id}).then((res) => {
            if (res){
              this.initExcel(res)
            }
          }).catch(res=>{
            if (res){
              this.initExcel(res)
            }
          })

        }).catch(_ => {
        })
      },
      initExcel(res){
        const link = document.createElement('a');
        let blob = new Blob([res.data], {type: 'application/vnd.ms-excel'});
        link.style.display = 'none';
        link.href = URL.createObjectURL(blob);
        let num = '';
        for (let i = 0; i < 10; i++) {
          num += Math.ceil(Math.random() * 10)
        }
        link.setAttribute('download', '表现分汇总' + num + '.xls');
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link)
      }
    }
  }
</script>
