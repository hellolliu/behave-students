<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.username" clearable class="filter-item" style="width: 200px;" placeholder="请输入教师名称"/>
      <el-input v-model="listQuery.mobile" clearable class="filter-item" style="width: 200px;" placeholder="请输入教师手机号"/>
      <el-button v-permission="['GET /admin/task/list']" class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button v-permission="['POST /admin/task/create']" class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column align="center" label="ID" prop="id" sortable/>

      <el-table-column align="center" label="标题" prop="title"/>
      <el-table-column align="center" label="备注" prop="value"/>
      <el-table-column align="center" label="状态" prop="status">
      <template slot-scope="scope">
        <el-tag >{{ statusDic[scope.row.status] }}</el-tag>
      </template>
      </el-table-column>


      <el-table-column align="center" label="操作" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-permission="['POST /admin/task/update']" type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button v-permission="['POST /admin/task/delete']" type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
          <el-button type="primary" size="mini" @click="selectItems(scope.row)">查询</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getItems" />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="标题" prop="title">
          <el-input v-model="dataForm.title"/>
        </el-form-item>
        <el-form-item label="备注" prop="value">
          <el-input v-model="dataForm.value"/>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="dataForm.status" clearable>
            <el-option v-for="item in statuses" :key="item.index" :label="item.label" :value="item.index" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>

    <!--测试列表-->
    <!-- 查询和其他操作 -->
    <div class="filter-container" v-show="itemstable">
      <el-button v-permission="['GET /admin/task/list']" class="filter-item" type="primary" icon="el-icon-search" @click="getItems">刷新</el-button>
      <el-button v-permission="['POST /admin/task/create']" class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreateitems('0')">判断</el-button>
      <el-button v-permission="['POST /admin/task/create']" class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreateitems('1')">多选</el-button>
      <el-button v-permission="['POST /admin/task/create']" class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreateitems('2')">单选</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-show="itemstable" v-loading="itemsLoading" :data="items" element-loading-text="正在查询中。。。" border fit highlight-current-row>
      <el-table-column align="center" label="ID" prop="id" sortable/>

      <el-table-column align="center" label="标题" prop="title"/>
      <el-table-column align="center" label="类型" prop="type">
        <template slot-scope="scope">
          <el-tag >{{ typeDic[scope.row.type] }}</el-tag>
        </template>
      </el-table-column>


      <el-table-column align="center" label="操作" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-permission="['POST /admin/task/update']" type="primary" size="mini" @click="handleUpdateitems(scope.row)">编辑</el-button>
          <el-button v-permission="['POST /admin/task/delete']" type="danger" size="mini" @click="handleDeleteitems(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="itemstotal>0&&itemstable" :total="itemstotal" :page.sync="itemsQuery.page" :limit.sync="itemsQuery.limit" @pagination="selectItems" />
    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[itemsdialogStatus]" :visible.sync="itemsdialogFormVisible">
      <el-form v-show="dataFormType==0" ref="panduanForm" :rules="itemsrules" :model="panduanForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="标题" prop="title">
          <el-input v-model="panduanForm.title"/>
        </el-form-item>
        <el-form-item label="对错">
          <el-select v-model="panduanForm.answer" clearable>
            <el-option v-for="item in tureOrFalse" :key="item.index" :label="item.label" :value="item.index" />
          </el-select>
        </el-form-item>
        <el-form-item label="分值" prop="score">
          <el-input v-model="panduanForm.score"/>
        </el-form-item>
      </el-form>
      <el-form v-show="dataFormType==1" ref="duoxuanForm" :rules="itemsrules" :model="duoxuanForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="问题" prop="title">
          <el-input v-model="duoxuanForm.title"/>
        </el-form-item>
        <el-form-item label="选项1" prop="one">
          <el-input v-model="duoxuanForm.one"/>
        </el-form-item>
        <el-form-item label="选项2" prop="two">
          <el-input v-model="duoxuanForm.two"/>
        </el-form-item>
        <el-form-item label="选项3" prop="three">
          <el-input v-model="duoxuanForm.three"/>
        </el-form-item>
        <el-form-item label="选项4" prop="four">
          <el-input v-model="duoxuanForm.four"/>
        </el-form-item>
        <el-form-item label="答案" prop="roleIds">
          <el-select v-model="duoxuanForm.answer" multiple placeholder="请选择">
            <el-option
              v-for="item in answerOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="分值" prop="score">
          <el-input v-model="duoxuanForm.score"/>
        </el-form-item>
      </el-form>

      <el-form v-show="dataFormType==2" ref="danxuanForm" :rules="itemsrules" :model="danxuanForm" status-icon label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="问题" prop="title">
          <el-input v-model="danxuanForm.title"/>
        </el-form-item>
        <el-form-item label="选项1" prop="one">
          <el-input v-model="danxuanForm.one"/>
        </el-form-item>
        <el-form-item label="选项2" prop="two">
          <el-input v-model="danxuanForm.two"/>
        </el-form-item>
        <el-form-item label="选项3" prop="three">
          <el-input v-model="danxuanForm.three"/>
        </el-form-item>
        <el-form-item label="选项4" prop="four">
          <el-input v-model="danxuanForm.four"/>
        </el-form-item>
        <el-form-item label="答案" prop="roleIds">
          <el-select v-model="danxuanForm.answer" placeholder="请选择">
            <el-option
              v-for="item in answerOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="分值" prop="score">
          <el-input v-model="danxuanForm.score"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="itemsdialogFormVisible = false">取消</el-button>
        <el-button v-if="itemsdialogStatus=='create'" type="primary" @click="createDataitems">确定</el-button>
        <el-button v-else type="primary" @click="updateDataitems">确定</el-button>
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
  import { listTask, createTask, updateTask, deleteTask } from '@/api/task'
  import { listTaskItems, createTaskItems, updateTaskItems, deleteTaskItems } from '@/api/taskitems'
  import { getToken } from '@/utils/auth'
  import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

  export default {
    name: 'task',
    components: { Pagination },
    data() {
      return {
        list: null,
        items: null,
        itemstable:false,
        total: 0,
        itemstotal: 0,
        listLoading: true,
        itemsLoading:false,
        listQuery: {
          page: 1,
          limit: 20,
          mobile: undefined,
          username:undefined,
          sort: 'add_time',
          order: 'desc'
        },
        itemsQuery: {
          page: 1,
          limit: 20,
          quetionid: undefined,
          sort: 'add_time',
          order: 'desc'
        },
        teacherid:undefined,
        quetionid:undefined,
        dataForm: {
          id: undefined,
          title: undefined,
          teacherid:undefined,
        },
        panduanForm: {
          id: undefined,
          title: undefined,
        },
        duoxuanForm:{
          id: undefined,
          title: undefined,
        },
        danxuanForm:{
          id: undefined,
          title: undefined,
        },
        dialogFormVisible: false,
        itemsdialogFormVisible: false,
        dialogStatus: '',
        itemsdialogStatus: '',
        textMap: {
          update: '编辑',
          create: '创建'
        },
        statusDic: ['禁用', '启用'],
        typeDic:['判断','多选','单选'],
        statuses:[{index:0,label:'禁用'},{index:1,label:'启用'}],
        tureOrFalse:[{index:'0',label:'正确'},{index:'1',label:'错误'}],
        answerOptions:[{value:'0',label:"选项1"},{value:'1',label:"选项2"},{value:'2',label:"选项3"},{value:'3',label:"选项4"}],
        dataFormType:undefined,
        rules: {
          title: [
            { required: true, message: '名称不能为空', trigger: 'blur' }
          ]
        },
        itemsrules: {
          title: [
            { required: true, message: '题目不能为空', trigger: 'blur' }
          ]
        },
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
        listTask(this.listQuery)
          .then(response => {
            console.log(response)
            this.list = response.data.data.teacherQuestions.data.list
            this.total = response.data.data.teacherQuestions.data.total
            this.listLoading = false
            this.itemstable=false
            let user= response.data.data.user
            console.log(user)
            if (user !=null&&user.length!=0){
              this.teacherid=user[0].id
            }
          }).catch(() => {
            this.list = []
            this.total = 0
            this.listLoading = false
          })
      },
      handleFilter() {
        this.listQuery.page = 1
        this.getList()
      },
      resetForm() {
        this.dataForm = {
          id: undefined,
          name: undefined
        }
      },
      itemsresetForm() {
        //应该把三个类别不一样
        this.panduanForm = {
          id: undefined,
          title: undefined
        }
        this.duoxuanForm = {
          id: undefined,
          title: undefined
        }
        this.danxuanForm = {
          id: undefined,
          title: undefined
        }
      },
      uploadAvatar: function(response) {
        this.dataForm.avatar = response.data.url
      },
      handleCreate() {
        if(this.teacherid==undefined){
          this.$notify.error({
            title: '错误',
            message: "请先查询教师"
          })
          return;
        }
        this.itemstable=false
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
            this.dataForm.userId=this.teacherid;
            console.log(this.dataForm)
            createTask(this.dataForm).then(response => {
                this.list.unshift(response.data.data)
                this.dialogFormVisible = false
                this.$notify.success({
                  title: '成功',
                  message: '添加成功'
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
        this.itemstable=false
        this.dataForm = Object.assign({}, row)
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].clearValidate()
        })
      },
      updateData() {
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            updateTask(this.dataForm)
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
          }
        })
      },
      handleDelete(row) {
        if (row.id==this.quetionid){
          this.itemstable=false
        }
        deleteTask(row)
          .then(response => {
            this.$notify.success({
              title: '成功',
              message: '删除班级成功'
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
      selectItems(row){
        this.itemsQuery.page = 1
        this.itemsQuery.quetionid = row.id
        this.quetionid = row.id
        this.itemstable=true
        this.getItems()
      },
      getItems(){
        this.itemsLoading = true
        listTaskItems(this.itemsQuery)
          .then(response => {
            this.items = response.data.data.list
            this.itemstotal = response.data.data.total
            this.itemsLoading = false
          })
          .catch(() => {
            this.items = []
            this.itemstotal = 0
            this.itemsLoading = false
          })
      },
      handleCreateitems(value) {
        this.dataFormType=value
        this.itemsresetForm()
        this.itemsdialogStatus = 'create'
        this.itemsdialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['panduanForm'].clearValidate()
        })
      },

      createDataitems() {
        let valuetype=this. ititFormTitle()
        this.$refs[valuetype].validate(valid => {
          if (valid) {
            let form=this.initForm()
            console.log(form)
            createTaskItems(form)
              .then(response => {
                this.items.unshift(response.data.data)
                this.itemsdialogFormVisible = false
                this.$notify.success({
                  title: '成功',
                  message: '添加成功'
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
      ititFormTitle(){
        let valuetype=""
        if(this.dataFormType==0){
          valuetype='panduanForm'
        }else if(this.dataFormType==1){
          valuetype='duoxuanForm'
        }else if (this.dataFormType==2){
          valuetype='danxuanForm'
        }
        return valuetype
      },
      initForm(){
        let form={}
        if(this.dataFormType==0){
          form={
            id:this.panduanForm.id,
            questionId:this.quetionid,
            type:0,
            title:this.panduanForm.title,
            option:'',
            answer:this.panduanForm.answer,
            score:this.panduanForm.score,
          }
        }else if(this.dataFormType==1){
          let opn=[
            {index:0,value:this.duoxuanForm.one},
            {index:1,value:this.duoxuanForm.two},
            {index:2,value:this.duoxuanForm.three},
            {index:3,value:this.duoxuanForm.four},]
          form={
            id:this.duoxuanForm.id,
            questionId:this.quetionid,
            type:1,
            title:this.duoxuanForm.title,
            option:JSON.stringify(opn),
            answer:JSON.stringify(this.duoxuanForm.answer),
            score:this.duoxuanForm.score
          }
        }else if(this.dataFormType==2){
          let opn=[
            {index:0,value:this.danxuanForm.one},
            {index:1,value:this.danxuanForm.two},
            {index:2,value:this.danxuanForm.three},
            {index:3,value:this.danxuanForm.four},]
          form={
            id:this.danxuanForm.id,
            questionId:this.quetionid,
            type:2,
            title:this.danxuanForm.title,
            option:JSON.stringify(opn),
            answer:this.danxuanForm.answer,
            score:this.danxuanForm.score
          }
        }
        return form
      },
      handleUpdateitems(row) {
        if(row.type==0){
          this.dataFormType=0
          let rowObj=Object.assign({}, row)
          this.panduanForm = {
            id:rowObj.id,
            type:0,
            title:rowObj.title,
            answer:rowObj.answer,
            score:rowObj.score
          }
          this.itemsdialogStatus = 'update'
          this.itemsdialogFormVisible = true
          this.$nextTick(() => {
            this.$refs['panduanForm'].clearValidate()
          })
        }else if(row.type==1){
          this.dataFormType=1
          let rowObj=Object.assign({}, row)
          let optionString=rowObj.option
          let option=JSON.parse(optionString)
          this.duoxuanForm = {
            id:rowObj.id,
            type:1,
            title:rowObj.title,
            one:option[0].value,
            two:option[1].value,
            three:option[2].value,
            four:option[3].value,
            answer:JSON.parse(rowObj.answer),
            score:rowObj.score
          }
          this.itemsdialogStatus = 'update'
          this.itemsdialogFormVisible = true
          this.$nextTick(() => {
            this.$refs['duoxuanForm'].clearValidate()
          })
        }else if(row.type==2){
          this.dataFormType=2
          let rowObj=Object.assign({}, row)
          let optionString=rowObj.option
          let option=JSON.parse(optionString)
          this.danxuanForm = {
            id:rowObj.id,
            type:2,
            title:rowObj.title,
            one:option[0].value,
            two:option[1].value,
            three:option[2].value,
            four:option[3].value,
            answer:rowObj.answer,
            score:rowObj.score
          }
          this.itemsdialogStatus = 'update'
          this.itemsdialogFormVisible = true
          this.$nextTick(() => {
            this.$refs['danxuanForm'].clearValidate()
          })
        }
      },
      updateDataitems() {
        let valuetype=this. ititFormTitle()
        this.$refs[valuetype].validate(valid => {
          if (valid) {
            let form=this.initForm()
            updateTaskItems(form)
              .then(() => {
                for (const v of this.items) {
                  if (v.id === form.id) {
                    const index = this.items.indexOf(v)
                    this.items.splice(index, 1, form)
                    break
                  }
                }
                this.itemsdialogFormVisible = false
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
          }
        })
      },
      handleDeleteitems(row) {
        deleteTaskItems(row)
          .then(response => {
            this.$notify.success({
              title: '成功',
              message: '删除成功'
            })
            const index = this.items.indexOf(row)
            this.items.splice(index, 1)
          })
          .catch(response => {
            this.$notify.error({
              title: '失败',
              message: response.data.errmsg
            })
          })
      },
    }
  }
</script>
