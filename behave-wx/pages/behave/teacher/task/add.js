var util = require('../../../../utils/util.js');
var api = require('../../../../config/api.js');
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    question:{
      userId:wx.getStorageSync('user').id,
      status:0
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options)
    //0add,1update
    // initAdd();
    let type=options.type
    var questionObj={}
    if(type=="1"){
      let questionString=options.question
      questionObj=JSON.parse(questionString)
      
    }
    this.setData({
      question:questionObj,
      type:type
    })

  },
  addOrUpdate(){
      //0add,1update
      console.log("sdfsdfasds")
      console.log(this.data.type)
      if(this.data.type==0){
        this.addQuestion()
      }else{
        this.updateQuestion()
      }
  },
  addQuestion(){
    console.log("qutstion",this.data.question)
    let question=this.data.question
    question.userId=wx.getStorageSync('user').id
    util.request(api.TeacherQuestionAdd, question, 'POST').then(function(res) {
      if (res.errno === 0) {
        var pages = getCurrentPages();
        var beforePage = pages[pages.length - 2];
        beforePage.initQustions();
        wx.navigateBack({
          delta: 1
        })
      }
    });
  },
  updateQuestion(){
    let question=this.data.question
    console.log("update",question)
    util.request(api.TeacherQuestionUpdate,question, 'POST').then(function(res) {
      if (res.errno === 0) {
        var pages = getCurrentPages();
        var beforePage = pages[pages.length - 2];
        beforePage.initQustions();
        wx.navigateBack({
          delta: 1
        })
      }
    });
  },
  deleteQuestion(){
    let question=this.data.question
    util.request(api.TeacherQuestionDelete,{id:question.id}, 'GET').then(function(res) {
      console.log(res)
      if (res.errno === 0) {
        var pages = getCurrentPages();
        var beforePage = pages[pages.length - 2];
        beforePage.initQustions();
        wx.navigateBack({
          delta: 1
        })
      }else{
        util.showErrorToast(res.errmsg)
      }
    });
  },
  titleChange(res){
    let value=res.detail.value
    var question=this.data.question;
    question.title=value
    this.setData({
      question:question
    })
  },
  valueChange(res){
    let value=res.detail.value
    var question=this.data.question;
    question.value=value
    this.setData({
      question:question
    })
  },
  statusChange(res){
    console.log(res)
    let value=res.detail.value
    var question=this.data.question;
    if(value){
      question.status=1
    }else{
      question.status=0
    }
    this.setData({
      question:question
    })
  },
  clearTitleInput(){
    let question=this.data.question
    question.title=''
    this.setData({
      question:question
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