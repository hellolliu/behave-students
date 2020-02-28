// pages/behave/teacher/task/index.js
// pages/behave/teacher/score/index.js
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
    this.initValues();
  },
  initValues(){
    let that=this
    util.request(api.StudentAnswerAll, {
      studentId:wx.getStorageSync('user').id,
      startTime:that.data.startTime,
      endTime:that.data.endTime
    }, 'GET').then(function(res) {
      console.log(res)
      if (res.errno === 0) {
        that.setData({
          anserList:res.data
        })
      }
    });
  },
  toAnswer(e){
   
    let that=this
    let answerObj=that.data.anserList[e.target.dataset.index]
    console.log(answerObj)
    if(answerObj.userAnswer.status!=0){
      return;
    }
    let answer=JSON.stringify(answerObj)
    wx.navigateTo({
      url: '../exercise/answer?obj='+answer,
    })
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
  }
})