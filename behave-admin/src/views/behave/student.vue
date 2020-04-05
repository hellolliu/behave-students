<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.username" clearable class="filter-item" style="width: 200px;" placeholder="请输入学生名称"/>
      <el-input v-model="listQuery.mobile" clearable class="filter-item" style="width: 200px;" placeholder="请输入学生手机号"/>
      <el-button v-permission="['GET /admin/user/list']" class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column align="center" label="学生ID" prop="id" sortable/>

      <el-table-column align="center" label="真实姓名" prop="username"/>

      <el-table-column align="center" label="性别" prop="gender">
        <template slot-scope="scope">
          <el-tag >{{ genderDic[scope.row.gender] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="网络名称" prop="nickname"/>
      <el-table-column align="center" label="头像" prop="avatar">
        <template slot-scope="scope">
          <img v-if="scope.row.avatar" :src="scope.row.avatar" width="40">
        </template>
      </el-table-column>
      <el-table-column align="center" label="手机号码" prop="mobile"/>
      <!--<el-table-column align="center" label="所属班级" prop="classId"/>-->

      <el-table-column align="center" label="操作" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-permission="['GET /admin/student/read']" type="primary" size="mini" @click="handleQuery(scope.row)">更新</el-button>
          <el-button v-permission="['GET /admin/student/read']" size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>

    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 对话框 -->
    <el-dialog :visible.sync="queryDialogVisible" title="其他信息">
      <el-form ref="dataForm" :model="dataForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="班级" prop="name">
          <el-cascader expand-trigger="hover"
                       :options="gradeUnitData"
                       v-model="dataForm.class"
                       :props="defaultProps"
                       @change="gradeUnitChange"
                       placeholder="班级" class="filter-item" style="width: 200px;">
          </el-cascader>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="queryDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateData">确定</el-button>
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
  import { listStudent,readStudent,deleteStudent ,updateStudent} from '@/api/student'
  import { getToken } from '@/utils/auth'
  import Pagination from '@/components/Pagination' // Secondary package based on el-paginatio
  import { queryClassAll} from '@/api/schedule'
  export default {
    name: 'student',
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
          sort: 'add_time',
          order: 'desc'
        },
        defaultProps: {
          value: 'id',
          label: 'name'
        },//默认节点名与数据绑定
        dataForm: {
          id:undefined,
          studentId: undefined,
          classId:undefined,
          className: ''
        },
        dialogFormVisible: false,
        dialogStatus: '',
        downloadLoading: false,
        queryDialogVisible:false,
        genderDic: ['未知', '男', '女'],
        gradeUnitData:[]
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
      this.getList();
      //获取班级信息
      queryClassAll().then(response=>{
//          gradeUnitData
        this.gradeUnitData=response.data.data.list
      }).catch(()=>{
        this.gradeUnitData=[]
      })
    },
    methods: {

      getList() {
        this.listLoading = true
        listStudent(this.listQuery)
          .then(response => {
            this.list = response.data.data.list
            this.total = response.data.data.total
            this.listLoading = false
          })
          .catch(() => {
            this.list = []
            this.total = 0
            this.listLoading = false
          })
      },
      handleFilter() {
        this.listQuery.page = 1
        this.getList()
      },
      handleQuery(row) {
        let id=Object.assign({},row).id;
        console.log(id)
        if (id){
          readStudent({
            id:id
          }).then(response=>{
            console.log(response.data.data)
            let student=response.data.data.student;
            let studentClass=response.data.data.studentClass;
            if(studentClass==undefined){
              this.$notify.error({title: '失败', message: "学生班级信息丢失"})
              studentClass={name: ""}
            }
            this.dataForm= student
            this.dataForm.class=[studentClass.id]
          }).catch(()=>{
            this.$notify.error({
              title: '失败',
              message: response.data.errmsg
            })
          })
        }

        this.queryDialogVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      handleDelete(row) {
        deleteStudent(row)
          .then(response => {
            this.$notify.success({
              title: '成功',
              message: '删除学生成功'
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
        console.log(row)
      },
      updateData() {
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            updateStudent(this.dataForm)
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
                  message: '更新成功'
                })
              })
              .catch(response => {
                this.$notify.error({
                  title: '失败',
                  message: response.data.errmsg
                })
              })
            this.queryDialogVisible = false
          }
        })
      },
      //班级
      gradeUnitChange(val){
        this.dataForm.classId=val[val.length-1];
        console.log("this.dataForm.classId",this.dataForm.classId)
      },
    }
  }
</script>
