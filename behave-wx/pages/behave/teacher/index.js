// pages/behave/teacher/index.js
var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    lonadding:true,//开始提问
    hiddenmodalput:true,//打分
    inputFocus:false,//打分输入框
    items:[],
  itemIndex:null,
  classObject:{},
  scoreValue:'',
  questionList:[],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var str = options.str//课程表id
    var courseid=options.courseid
    this.setData({
      courseid:courseid
    })
    console.log(options)
    if(str!=null){
      this.initClass(str);
    }
    this.initQuestionList();
  },
  initClass(scheduleId){
    var that=this;
    util.request(api.ScheduleIdToclass, {
      scheudleId: scheduleId,
    }, 'GET').then(function(res) {
      if (res.errno === 0) {
        that.data.classObject=res.data
        that.setData({
          classObject:res.data
        })
        that.initStudent(that.data.classObject.id);
      }
    });
    
  },
  initStudent(classId){
    var that=this;
    util.request(api.StudentsByClass, {
      classId: classId,
    }, 'GET').then(function(res) {
      if (res.errno === 0) {
        let student=res.data;
        for(var i=0;i<student.length;i++){
          student[i].select=false;
        }
        that.setData({
          items:student
        })
      }
    });
  },
  initQuestionList(){
    var that=this
    if(app.globalData.hasLogin){
      util.request(api.TeacherQuestionEnable, {
        userId: wx.getStorageSync('user').id
      }, 'GET').then(function(res) {
        if (res.errno === 0) {
          that.setData({
            questionList:res.data
          })
        }
      });
    }else{
      util.showErrorToast("请登录")
    }
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
   
  },
 
  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  radomSelect(){
    if(this.data.lonadding){
      var that=this;
      var items=that.data.items;
      var length=items.length-1;
      var index=Math.floor(Math.random() * (length + 1) );
      that.changeSelect(index)
    }
  //   wx.showToast({
  //     title: 'ok',//提示文字
  //     duration:2000,//显示时长
  //     mask:true,//是否显示透明蒙层，防止触摸穿透，默认：false  
  //     icon:'success', //图标，支持"success"、"loading"  
  //     success:function(){ 
        
  //     },//接口调用成功
  //     fail: function () { },  //接口调用失败的回调函数  
  //     complete: function () { } //接口调用结束的回调函数  
  //  })
  },
  questionStart(){
    if(this.data.itemIndex==null){
      util.showErrorToast('请先选学生')
      return
    }
    this.setData({
      lonadding:false,//取消隐藏
      
    })
  },
  questionEnd(){
    this.setData({
      lonadding:true,
      hiddenmodalput:false,
      inputFocus:true,
      scoreValue:''
    })
  },
  releaseContact(){
    let that=this
    if(this.data.lonadding){
      let questionList=this.data.questionList
      var questions=[];
      for(var i=0;i<questionList.length;i++){
        questions[i]=questionList[i].title
      }
      let user=wx.getStorageSync('user')
      wx.showActionSheet({
        itemList:questions,//显示的列表项
        success: function (res) {//res.tapIndex点击的列表项
           util.request(api.TeacherQuestionQC, {
            classId: that.data.classObject.id,
            questionId:questionList[res.tapIndex].id,
            userId:user.id
          }, 'POST').then(function(res) {
            if (res.errno === 0) {
              wx.showToast({
                title: '发布成功',
              })
            }
          });
        },
        fail: function (res) { },
        complete:function(res){ }
     })
    }
  },
  cancelM:function(e){
    this.setData({
       hiddenmodalput: true,
    })
 },
 confirmM: function (e) {
  var that=this
    
    util.request(api.AddStudentScore, {
      userId: that.data.items[ that.data.itemIndex].id,
      userName:that.data.items[ that.data.itemIndex].username,
      teacherId:wx.getStorageSync('user').id,
      value:that.data.scoreValue ,
      courseId:that.data.courseid
    }, 'POST').then(function(res) {
      if (res.errno === 0) {
        that.setData({
          hiddenmodalput: true,
          inputFocus:false
        })
      }
    });
 },
 performance: function (e) {
    this.setData({
      scoreValue:e.detail.value
    })
 },
 itemSelect(e){
   if(this.data.lonadding){
    var index=e.currentTarget.dataset.index;
    this.changeSelect(index)
   }
 },
 //用来改变选中的select
 changeSelect(index){
   var items=this.data.items;
   for(var i=0;i<items.length;i++){
     items[i].select=false;
    }
    items[index].select=true;
    this.setData({
      items:items,
      itemIndex:index
    })
 }
})