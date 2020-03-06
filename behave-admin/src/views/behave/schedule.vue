<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-cascader expand-trigger="hover"
                   :options="gradeUnitData"
                   v-model="positionArr"
                   :props="defaultProps"
                   @change="gradeUnitChange"
                   placeholder="班级" class="filter-item" style="width: 200px;">
      </el-cascader>
      <el-button @click="onSearch" v-permission="['GET /admin/schedule/list']" class="filter-item" type="primary" icon="el-icon-search">查询</el-button>
      <el-button @click="onEdit"  class="filter-item" type="primary" icon="el-icon-edit">编辑</el-button>
      <el-button @click="timeShow=true" v-show="!timeShow" class="filter-item"   type="primary" icon="el-icon-close">退出编辑</el-button>
    </div>

    <!--表格-->
    <el-table
      :data="timeData"
      stripe
      style="width: 100%">

      <el-table-column min-width="30px" label="周" fixed="left" prop="label" align="center"></el-table-column>

      <el-table-column label="上午" align="center">
        <el-table-column
          min-width="50px"
          v-for="(v,i) in titleData" :key="i"
          v-if="v.label===0" align="center">
          <template slot="header" slot-scope="scope">
            <div class="tabletitle-timeline">
              第{{v.sortFie}}节 <br/>
              {{v.timeSlot}}
            </div>
          </template>
          <template slot-scope="scope">
            <div >
              <div v-if="timeShow">
                {{scope.row[sbjectKey[i]]}}<br/>
                {{scope.row[teacherKey[i]]}}
              </div>
              <div v-else>
                <el-input :disabled="true"
                  size="mini"
                  placeholder="科目"
                  suffix-icon="el-icon-date"
                  v-model="scope.row[sbjectKey[i]]">
                </el-input>
                <el-select
                  clearable
                  v-model="scope.row[teacherKey[i]]"
                  size="mini"
                  placeholder="任课老师ya"@change="teacherSelectChange(scope,i)">
                  <el-option
                    v-for="(val,ind) in courseList"
                    :key="ind"
                    :label="val.userName+'('+val.name+')'"
                    :value="ind"></el-option>
                </el-select>
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table-column>

      <el-table-column label="下午" align="center">
        <el-table-column
          min-width="50px"
          v-for="(v,i) in titleData" :key="i"
          v-if="v.label===1" align="center">
          <template slot="header" slot-scope="scope">
            <div class="tabletitle-timeline">
              第{{v.sortFie}}节 <br/>
              {{v.timeSlot}}
            </div>
          </template>
          <template slot-scope="scope">
            <div >
              <div v-if="timeShow">
                {{scope.row[sbjectKey[i]]}}<br/>
                {{scope.row[teacherKey[i]]}}
              </div>
              <div v-else>
                <el-input
                  size="mini" :disabled="true"
                  placeholder="科目"
                  suffix-icon="el-icon-date"
                  v-model="scope.row[sbjectKey[i]]">
                </el-input>
                <el-select
                  clearable
                  v-model="scope.row[teacherKey[i]]"
                  size="mini"
                  placeholder="任课老师" @change="teacherSelectChange(scope,i)">
                  <el-option
                    v-for="(val,ind) in courseList"
                    :key="ind"
                    :label="val.userName+'('+val.name+')'"
                    :value="ind"></el-option>
                </el-select>
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table-column>

      <el-table-column label="晚上" align="center">
        <el-table-column
          min-width="50px"
          v-for="(v,i) in titleData" :key="i"
          v-if="v.label===2"
          align="center">
          <template slot="header" slot-scope="scope">
            <div class="tabletitle-timeline">
              第{{v.sortFie}}节 <br/>
              {{v.timeSlot}}
            </div>
          </template>
          <template slot-scope="scope">
            <div >
              <div v-if="timeShow">
                {{scope.row[sbjectKey[i]]}}<br/>
                {{scope.row[teacherKey[i]]}}
              </div>
              <div v-else>
                <el-input
                  size="mini" :disabled="true"
                  placeholder="科目"
                  suffix-icon="el-icon-date"
                  v-model="scope.row[sbjectKey[i]]">
                </el-input>
                <el-select
                  clearable
                  v-model="scope.row[teacherKey[i]]"
                  size="mini"
                  placeholder="任课老师" @change="teacherSelectChange(scope,i)">
                  <el-option
                    v-for="(val,ind) in courseList"
                    :key="ind"
                    :label="val.userName+'('+val.name+')'"
                    :value="ind"></el-option>
                </el-select>
              </div>
            </div>
          </template>
        </el-table-column>

      </el-table-column>


    </el-table>
  </div>
</template>

<script>
  import { querySoltAll ,queryClassAll,queryCourseAll,deletedSchedule,updateSchedule,listScheduleValue } from '@/api/schedule'
  import { getToken } from '@/utils/auth'
  import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

  export default {
    data() {
      return {
        pageButton:{},//权限按钮
        //查询
        gradeUnitData:[{
          id:"1",name:"2"
        }],//年级班级数据
        defaultProps: {
          value: 'id',
          label: 'name'
         },//默认节点名与数据绑定
        positionArr:[],//级联选择值

        timeShow:true,//编辑表与展示表
        courseList:[],//全部课程

        sbjectKey:['oneS','twoS','threeS','fourS','fiveS','sixS','sevenS','eightS','nineS','tenS','elevenS','twelveS'],//科目key值
        teacherKey:['oneT','twoT','threeT','fourT','fiveT','sixT','sevenT','eightT','nineT','tenS','elevenS','twelveS'],//老师key值
        //每天的课表
        timeData:[],
        timeDataOld:[],
        belongId:"",//班级id
        belongType:"803",//803表示班级，教室804
        timeId:"",
        //课节数据---标题
        titleData:[],
      }
    },
    mounted(){
      this.timetableLoad();
    },
    created() {
      //获取时间段
      querySoltAll().then(response=>{
        this.titleData=response.data.data.list
      }).catch(()=>{
        this.titleData=[]
      })
      //获取班级信息
      queryClassAll().then(response=>{
//          gradeUnitData
        this.gradeUnitData=response.data.data.list
      }).catch(()=>{
        this.gradeUnitData=[]
      })
      //
      queryCourseAll().then(response=>{
        this.courseList=response.data.data.list
      }).catch(()=>{
        this.courseList=[]
      })
      this.getList()
    },
    methods:{
      getList(){
        if(this.belongId&&this.belongType){
          console.log("this.belongId",this.belongId)
          listScheduleValue({
            classId:this.belongId
          }).then(response=>{
            this.timeData=response.data.data
            console.log(this.timeData)
          }).catch(response=>{
            this.$message.warning(response)
          })
        }
      },
      timetableLoad(){
        //根据path来找页面权限，按钮根据code来找对应的按钮权限
        let self = this;
      },
      //年级班级
      gradeUnitChange(val){
        this.belongId=val[val.length-1];
        this.belongType='803'
      },
      //条件查询
      onSearch(){
        let self = this;
        if(this.belongId&&this.belongType){
          self.timeShow=true;
          self.getList();
        }else{
          this.$message.warning('请先选择班级')
        }
      },
      onEdit(){
        if(this.belongId&&this.belongType){
          this.timeShow=false;
        }else{
          this.$message.warning('请先选择班级')
        }
      },
      teacherSelectChange(scope,i){

        console.log(scope)
        console.log(i)
        let courseIndex=scope.row[this.teacherKey[i]];
        let course=this.courseList[courseIndex];
        var row=scope.row;
        let rowNum=scope.$index;

        if (course!=undefined){//课程不是空的
          //执行更新(星期，时间段，班级，课程)
          this.updateScheduleValue(scope,i)
          row[this.teacherKey[i]]=course.userName
          row[this.sbjectKey[i]]=course.name

        }else {
          //执行删除(星期，时间段，班级)
          this.deleteScheduleVel(scope,i);
          row[this.teacherKey[i]]=""
          row[this.sbjectKey[i]]=""
        }
        this.timeData[rowNum]=row;
      },
      deleteScheduleVel(scope,i){
        console.log(this.titleData[i])
        let rowNum=scope.$index;
        deletedSchedule({
          classId:this.belongId,
          week:rowNum,
          slotFd:this.titleData[i].sortFie
        }).then(response=>{
        }).catch(response=>{
          this.$message.warning(response)
        })
      },
      updateScheduleValue(scope,i){
        let courseIndex=scope.row[this.teacherKey[i]];
        let course=this.courseList[courseIndex];
        var row=scope.row;
        let rowNum=scope.$index;
        let slot=this.titleData[i]
        updateSchedule({
          scheduleId:this.belongId,
          week:rowNum,
          courseId:course.id,
          courseName:course.name,
          courseUser:course.userName,
          courseUserId:course.userId,
          courseAddress:course.address,
          slotOrderFie:this.titleData[i].sortFie,
          slotId:this.titleData[i].sortFie
        }).then(response=>{
        }).catch(response=>{
          this.$message.warning(response)
        })
      }
    }
  }
</script>
<style>
  .tabletitle-timeline{
    line-height: 18px!important;
  }
</style>
