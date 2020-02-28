//index.js
var api = require('../../../config/api.js');
var util = require("../../../utils/util.js");
var app=getApp();
//更改数组 第三个参数是对象
function editArr(arr, i, editCnt) {
  let newArr = arr, editingObj = newArr[i];
  for (var x in editCnt) {
    editingObj[x] = editCnt[x];
  }
  return newArr;
}

Page({
  data: {
    loading:false,
    teacher:true,
    userInfo: {},
    curIpt: '',
    showAll: true,
    lists: [],
    curRange: [],
    curBegin: 0,
    curFinish: 1,
    remind: [],
    type:'',//学生：student;学生:teacher;不清楚：none
    courseList:[]
  },
  onLoad: function () {
    var that = this;
     //获取用户信息
     app.getUserInfo(function (userInfo) {
       that.setData({
         userInfo: userInfo
        })
    })
    //获取之前保留在缓存里的数据
    wx.getStorage({
      key: 'todolist',
      success: function (res) {
        if (res.data) {
          that.setData({
            lists: res.data
          })
        }
      }
    })
   
  },
  onShow(){  
  let dateNow=util.formatTime(new Date())
  this.setData({
    dateNow:dateNow.split(" ")[0]
  })
   var that=this;
   //获取应用实例
   var app = getApp()
   if(app.globalData.hasLogin){
     let user=wx.getStorageSync('user');
     if(user.userType==1){//学生
      that.initStudent();
     }else if(user.userType==0){//教师
      that.tScheduTd(user);
     }
     
   }else{
    that.loadingInit();
    wx.navigateTo({
      url: '/pages/auth/login/login',
    })
   }
  }, 
  switchType(type){
    switch(type){
      case 'teacher':
        this.setData({
          loading:true,
          teacher:false,//
          student:true,
          type:'teacher'
        })
        break;
      case 'student':
        this.setData({
          loading:true,
          teacher:true,//
          student:false,
          type:'student'
        })
        break;
      default:
        that.loadingInit();
        break;
    }
  },
  initStudent(){
    var that=this
    let classId=wx.getStorageSync('user').classId
    if(!classId){
      util.showErrorToast("请完善您的信息！")
      return
    }
    util.request(api.StudentAllCourse, {
      classId:classId
    }, 'GET').then(res => {
      that.switchType("student");
      if (res.errno === 0) {
        console.log("res",res)
        that.setData({
          studentCourses:res.data
        })
      }
    }).catch((err) => {
      util.showErrorToast("系统错误")
    });
  },
  tScheduTd(user){
    var that=this
    util.request(api.TeacherScheudleToday, {
      userId:user.id
    }, 'GET').then(res => {
      that.switchType("teacher");
      if (res.errno === 0) {
        console.log(res.data)
        that.setData({
          courseList:res.data,
          teacherName:user.username
        })
      }
    }).catch((err) => {
    });
  },
  loadingInit(){
    this.setData({
      loading:false,
      teacher:true,//
      student:false
    })
  },
  iptChange(e) {
    let timeArr = util.setTimeHalf();
    this.setData({
      curIpt: e.detail.value,
      curRange: timeArr
    })
  },
  formReset() {
    this.setData({
      curIpt: '',
      curRange: []
    })
  },
  formSubmit() {
    let cnt = this.data.curIpt, newLists = this.data.lists, i = newLists.length, begin = this.data.curRange[this.data.curBegin], finish = this.data.curRange[this.data.curFinish];
    if (cnt) {
      newLists.push({ id: i, content: cnt, done: false, beginTime: begin, finishTime: finish, editing: false });
      this.setData({
        lists: newLists,
        curIpt: ''
      })
    };
    this.saveData()
  },
  beginChange(e) {
    this.setData({
      curBegin: e.detail.value,
      curFinish: Number(e.detail.value) + 1
    })
  },
  finishChange(e) {
    this.setData({
      curFinish: e.detail.value
    })
  },
  //修改备忘录
  toChange(e) {
    let i = e.target.dataset.id;
    this.setData({
      lists: editArr(this.data.lists, i, { editing: true })
    })
  },
  iptEdit(e) {
    let i = e.target.dataset.id;
    this.setData({
      lists: editArr(this.data.lists, i, { curVal: e.detail.value })
    })
  },
  saveEdit(e) {
    let i = e.target.dataset.id;
    this.setData({
      lists: editArr(this.data.lists, i, { content: this.data.lists[i].curVal, editing: false })
    })
  },
  setDone(e) {
    let i = e.target.dataset.id, originalDone = this.data.lists[i].done;
    this.setData({
      lists: editArr(this.data.lists, i, { done: !originalDone })
    })
  },
  toDelete(e) {
    let i = e.target.dataset.id, newLists = this.data.lists;
    newLists.map(function (l, index) {
      if (l.id == i) {
        newLists.splice(index, 1);
      }
    })
    this.setData({
      lists: newLists
    })
  },
  doneAll() {
    let newLists = this.data.lists;
    newLists.map(function (l) {
      l.done = true;
    })
    this.setData({
      lists: newLists
    })
  },
  deleteAll() {
    this.setData({
      lists: [],
      remind: []
    });
    wx.setStorage({
      key: 'todolist',
      data: []
    })
  },
  showUnfinished() {
    this.setData({
      showAll: false
    })
  },
  showAll() {
    //显示全部事项
    this.setData({
      showAll: true
    })
  },
  saveData() {
    let listsArr = this.data.lists;
    wx.setStorage({
      key: 'todolist',
      data: listsArr
    })
  },
  startClass(e){
    var str =e.target.dataset.scheduleid;
    let courseId=e.target.dataset.courseid
    wx.navigateTo({
      url: '../teacher/index?str=' + str+"&courseid="+courseId,
    })
  },
  gotoClass(e){
    var str =e.target.dataset.index;
    let obje=this.data.studentCourses[str]
    let courserid=obje.scheduleValue.courseId
    let teacherid=obje.scheduleValue.courseUserId
    let scheduleid=obje.scheduleValue.scheduleId
    console.log(obje)
    wx.navigateTo({
      url: '../student/index?courserid=' + courserid+"&teacherid="+teacherid+"&scheduleid="+scheduleid,
    })
  }

})
