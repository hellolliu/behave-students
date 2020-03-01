var api = require('../../../config/api.js');
var check = require('../../../utils/check.js');
var util = require("../../../utils/util.js");
var app = getApp();
Page({
  data: {
    username: '',
    class:'班级',
    classId:'',
    password: '',
    confirmPassword: '',
    mobile: '',
    classArray: [],
    user:{}
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
    // 页面渲染完成
   
    this.setData({
      user:wx.getStorageSync('user')
    })
    
  },
  initClass(){
    let that=this;
    util.request(api.AllClass,{},'GET').then(res=>{
      if (res.errno === 0) {
        let user=that.data.user
        let classArray=res.data
        let className='班级'
        let classId='' 
        for(var i=0;i<classArray.length;i++){
          if(classArray[i].id==user.classId){
            className=classArray[i].name
            classId=classArray[i].id
          }
        }
        console.log("user",user)
        that.setData({
          classArray:classArray,
          class:className,
          classId:classId,
        })
      }
    });
  },
  onReady: function() {

  },
  onShow: function() {
    // 页面显示
    this.initClass();
  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭

  },
  startRegister: function() {
    var that = this;


    if (this.data.password != this.data.confirmPassword) {
      wx.showModal({
        title: '错误信息',
        content: '确认密码不一致',
        showCancel: false
      });
      return false;
    }

    if (!check.isValidPhone(this.data.user.mobile)) {
      wx.showModal({
        title: '错误信息',
        content: '手机号输入不正确',
        showCancel: false
      });
      return false;
    }
    util.request(api.StudentUpdate, that.data.user, 'POST').then(function(res) {
      if (res.errno === 0) {
        wx.showToast({
          title: '修改信息成功',
          icon: 'success',
          duration: 2000
        });
        wx.removeStorageSync('user')
        wx.setStorageSync('user', that.data.user)
        wx.navigateBack({
          delta: 1
        })
      }
    });
  },
  bindUsernameInput: function(e) {
    let user=this.data.user
    user.username=e.detail.value
    this.setData({
      user:user
    });
  },
  bindPickerChange:function(e){
    let user=this.data.user
    user.classId=this.data.classArray[e.detail.value].id
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      class: this.data.classArray[e.detail.value].name,
      classId:this.data.classArray[e.detail.value].id,
      user:user
    })
  },
  bindPasswordInput: function(e) {
    let user=this.data.user
    user.password=e.detail.value
    this.setData({
      user: user,
      password:e.detail.value,
    });
  },
  bindConfirmPasswordInput: function(e) {

    this.setData({
      confirmPassword: e.detail.value
    });
  },
  bindMobileInput: function(e) {
    let user=this.data.user
    user.mobile=e.detail.value
    this.setData({
      user: user
    });
  },

  clearInput: function(e) {
    let user=this.data.user
    switch (e.currentTarget.id) {
      case 'clear-username':
       user.username=''
        this.setData({
          username: '',
          user:user
        });
        break;
      case 'clear-class':
        user.classId=''

        this.setData({
          class:'班级',
          user:user
        })
          break;
  
      case 'clear-mobile':
        user.mobile=''
        this.setData({
          mobile: '',
          user:user
        });
        break;
    }
  }
})