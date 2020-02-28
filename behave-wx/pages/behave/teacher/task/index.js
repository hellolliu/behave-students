// pages/behave/teacher/task/index.js
var api = require('../../../../config/api.js');
var util = require("../../../../utils/util.js");
var app=getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    startTime:util.formatTime(new Date).split(" ")[0],
    endTime:util.formatTime(new Date).split(" ")[0],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.initTime();
  },
  initTime(){
    let nowTime=util.formatTime(new Date);
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
    this.initQustions();
  },
  initQustions(){
    let user=wx.getStorageSync('user')
    let that=this;
    util.request(api.TeacherQuestionAll, {
      userId:user.id,start:that.data.startTime,end:that.data.endTime
    }, 'GET').then(function(res) {
      if (res.errno === 0) {
        that.setData({
          question:res.data
        })
      }
    });
  },
  queryQuestioin(){
    this.initQustions();
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
  bindTimeStartChange: function(e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      startTime: e.detail.value
    })
  },
  bindTimeEndChange: function(e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      endTime: e.detail.value
    })
  },
  toAdd(){
    wx.navigateTo({
      url: '/pages/behave/teacher/task/add?type=0',
    })
  },
  toUpdate(res){
    console.log(res)
    let index=res.target.dataset.id
    let object=this.data.question[index];
    let question=JSON.stringify(object)
    wx.navigateTo({
      url: '/pages/behave/teacher/task/add?type=1&question='+question,
    })
  }
})