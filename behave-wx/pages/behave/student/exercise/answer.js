// pages/behave/student/exercise/answer.js
var api = require('../../../../config/api.js');
var util = require("../../../../utils/util.js");
var app=getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    let answer=options.obj
    let answerObj=JSON.parse(answer)
    this.setData({
      answerObj:answerObj
    })
    
  },
  answer(){
    let that=this
    let answerObj=that.data.answerObj
    util.request(api.StudentAnswerQuestion,answerObj.userAnswer, 'POST').then(function(res) {
      console.log(res)
      if (res.errno === 0) {
        var pages = getCurrentPages();
        var beforePage = pages[pages.length - 2];
        beforePage.initValues();
        wx.navigateBack({
          delta: 1
        })
      }else{
        util.showErrorToast(err.errmsg)
      }
    });
  },
  valueChange(err){
    console.log(err)
    let value=err.detail.value
    let answerObj=this.data.answerObj
    answerObj.userAnswer.value=value
    this.setData({
      answerObj:answerObj
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

  }
})