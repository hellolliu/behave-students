// pages/behave/student/exercise/answer.js
var api = require('../../../../config/api.js');
var util = require("../../../../utils/util.js");
var app=getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    tureOrFalse:[{index:'0',label:'正确'},{index:'1',label:'错误'}],
    answerOptions:[{value:'0',label:"选项1"},{value:'1',label:"选项2"},{value:'2',label:"选项3"},{value:'3',label:"选项4"}],
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
    this.initQuestions();
  },
  initObj(arr){
    for(let i=0;i<arr.length;i++){
      let option=arr[i].option
      arr[i].option=JSON.parse(option)
    }
    console.log("arr",arr)
    return arr
  },
  initQuestions(){
    let that=this
    let answerObj=that.data.answerObj
    util.request(api.studentQuestions,{questionId:answerObj.teacherQuestion.id}, 'GET').then(function(res) {
      if (res.errno === 0) {
        
        console.log(res.data[1])
        that.setData({
          panduan:res.data[0],
          duoxuan:that.initObj(res.data[1]),
          danxuan:that.initObj(res.data[2])
        })
        console.log("duoxuan",that.data.duoxuan)
      }else{
        util.showErrorToast(err.errmsg)
      }
    });
  },
  answer(){
    let that=this
    let answerObj=that.data.answerObj
    wx.showModal({
      title: '提示',
      content: '提交后不能更改',
      confirmText: "确定",
      cancelText: "取消",
      success: function (res) {
          if (res.confirm) {
              console.log('用户点击主操作')
              let panduan =that.data.panduan
              let duoxuan=that.data.duoxuan
              let danxuan =that.data.danxuan
              let zongfen=0,cuoti=0,duiti=0;
              console.log("panduan",panduan)     
              console.log("duoxuan",duoxuan)     
              console.log("danxuan",danxuan)
              for(let i=0;i<panduan.length;i++){
                let pd=panduan[i]
                if(pd.answer==pd.student){
                  console.log(pd)
                  zongfen=zongfen+pd.score
                  duiti++
                }else{
                  cuoti++
                }

              }
              for(let i=0;i<duoxuan.length;i++){
                let dx= duoxuan[i]
                let dxanswer=JSON.parse(dx.answer)
                let student=dx.student
                if(dxanswer.length==student.length){
                  if(that.isSame(dxanswer,student)){
                    console.log(dx)
                    zongfen=zongfen+dx.score
                    duiti++
                  }else{
                    cuoti++
                  }
                }else{
                  cuoti++
                }
              }
              for(let i=0;i<danxuan.length;i++){
                let dx=danxuan[i]
                if(dx.student==dx.answer){
                  console.log(dx)
                  zongfen=zongfen+dx.score
                  duiti++
                }else{
                  cuoti++
                }
              }
              let value="做错题："+cuoti+"道;"+"做对题："+duiti+"道;得分："+zongfen+"分."
              answerObj.userAnswer.value=value
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
          }else{
              console.log('用户点击辅助操作')
          }
      }
  });
  },
  isSame(one,two){
    let max=0;
    for(let i=0;i<one.length;i++){
      for(let j=0;j<two.length;j++){
        if(one[i]==two[j]){
          max++
        }
      }
    }
    if(max==one.length){
      return true
    }else{
      return false
    }
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

  },
  panduanChange: function (e) {
    console.log('radio发生change事件，携带value值为：', e);
    let index=e.target.dataset.index
    let value=e.detail.value
    let panduan=this.data.panduan
    panduan[index].student=value
    this.setData({
      panduan: panduan
    });
},
danxuanChange: function (e) {
  console.log('radio发生change事件，携带value值为：', e);
  let index=e.target.dataset.index
  let value=e.detail.value
  let danxuan=this.data.danxuan
  danxuan[index].student=value
  this.setData({
    danxuan: danxuan
  });
},
duoxuanChange:function(e){;
        var duoxuans = this.data.duoxuan, values = e.detail.value;
        let index=e.target.dataset.index
        let option=duoxuans[index].option
        for (var i = 0, lenI = option.length; i < lenI; ++i) {
          option[i].checked = false;

            for (var j = 0, lenJ = values.length; j < lenJ; ++j) {
                if(option[i].index == values[j]){
                  option[i].checked = true;
                    break;
                }
            }
        }
        duoxuans[index].student=values
        this.setData({
          duoxuan: duoxuans
        });
        console.log(this.data.duoxuan)
}
})