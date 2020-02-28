// pages/behave/teacher/studentwork/update.js
var util = require('../../../../utils/util.js');
var api = require('../../../../config/api.js');
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    answer:{},
    inputValue:null,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log("answer",options.answer)
    let answer=JSON.parse(options.answer)
    this.setData({
      answer:answer
    })
  },
  inputChange(res){
    console.log(res)
    let value=res.detail.value
    this.setData({
      inputValue:value
    })

  },
  addScore(){
    let that=this
    let answer=that.data.answer
    util.request(api.TeacherAddScore,{
      userAnswer:answer,
      score:that.data.inputValue,
      teacherId:wx.getStorageSync('user').id
    },'POST').then(res=>{
      console.log(res)
      if (res.errno === 0) {
        var pages = getCurrentPages();
        var beforePage = pages[pages.length - 2];
        beforePage.queryStudentAnswer();
        wx.navigateBack({
          delta: 1
        })
        
      }
    });
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