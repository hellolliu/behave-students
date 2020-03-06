// pages/behave/teacher/studentwork/index.js
var util = require('../../../../utils/util.js');
var api = require('../../../../config/api.js');
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    taskArray: [],
    taskIndex:null,
    classArray: [],
    calssIndex:null,
    userAnswers:[],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.initQuestion();
    this.initClass();
  },
  initQuestion(){
    let that=this
    util.request(api.TeacherQuestionEnable, {
      userId: wx.getStorageSync('user').id
    }, 'GET').then(function(res) {
      if (res.errno === 0) {
        console.log(res.data)
        that.setData({
          taskArray:res.data
        })
      }
    });
  },
  initClass(){
    let that=this
    util.request(api.AllClass,{},'GET').then(res=>{
      console.log(res)
      if (res.errno === 0) {
        that.setData({
          classArray:res.data
        })
      }
    });
  },
  queryStudentAnswer(){
    let user=wx.getStorageSync('user')
    if(this.data.classIndex==null) return;
    if(this.data.taskIndex==null) return;  
    let clasId=this.data.classArray[this.data.classIndex].id
    let questionId=this.data.taskArray[this.data.taskIndex].id
    let that=this
    util.request(api.StudentAnswer,{
      userId:user.id,classId:clasId,questionId:questionId
    },'GET').then(res=>{
      console.log(res)
      if(res.errno==0){
        that.setData({
          userAnswers:res.data
        })
      }else{
        that.setData({
          userAnswers:{}
        })
      }
     
    });
  },
  teacherCorrect(options){
    console.log(options)
    let index=options.target.dataset.index
    let answer=this.data.userAnswers[index]
    let answerString=JSON.stringify(answer)
    if(answer.status==1){
      wx.navigateTo({
        url: './update?answer='+answerString,
      })
    }else{
      return
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
  bindTaskChange: function(e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      taskIndex: e.detail.value
    })
  },
  bindClassChange: function(e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      classIndex: e.detail.value
    })
  }
})