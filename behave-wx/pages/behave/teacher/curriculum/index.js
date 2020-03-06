//index.js
import util from '../../../../utils/util';
var api = require('../../../../config/api.js');
var app = getApp()
Page({
  data: {
    currentIndex:0,
    cardRightIn: false,
    cardLeftIn: false,
    month:(new Date()).getMonth()+1,
    year:(new Date()).getFullYear(),
    times: [],
    week_kecheng: [{
      index: 1,
      week_day: [{
        week: '一',
        k: (new Date()).getDay()==1?'top-text2':''
      }, {
        week: '二',
        k: (new Date()).getDay()==2?'top-text2':''
      }, {
        week: '三',
        k: (new Date()).getDay()==3?'top-text2':''
      }, {
        week: '四',
        k: (new Date()).getDay()==4?'top-text2':''
      }, {
        week: '五',
        k: (new Date()).getDay()==5?'top-text2':''
      }, {
        week: '六',
        k: (new Date()).getDay()==6?'top-text2':''
      }, {
        week: '日',
        k: (new Date()).getDay()==0?'top-text2':''
      }]
    }],
  },
  onLoad: function() {
   this.initValues()
  },
  initValues(){
    var that = this;
    util.request(api.TeacherScheduleAll, {
      teacherId:wx.getStorageSync('user').id
    }, 'GET').then(res => {
      if (res.errno === 0) {
        console.log(res.data)
        that.setData(res.data)
        that.initTimes();
      }
    }).catch((err) => {
    });
  },
  initTimes(){
    let slotM=this.data.slotMorning;
    let slotN=this.data.slotNoon;
    let slotNit=this.data.slotNight;
    let all=[];
    var i=1
    all[0]={
      timeSlot: '上学',
      class2: 'left2'
    }
    for(var j=0;j<slotM.length;i++){
      let obj=slotM[j]
      obj.timeStart=obj.timeSlot.split("~")[0]
      obj.timeEnd=obj.timeSlot.split("~")[1]
      all[i]=obj
      j++
    }
    all[i++]={
      timeSlot: '午休',
      class2: 'left2'
    }
    let len=slotN.length+i;
    for(var j=0;i<len;i++){
      let obj=slotN[j]
      obj.timeStart=obj.timeSlot.split("~")[0]
      obj.timeEnd=obj.timeSlot.split("~")[1]
      all[i]=obj
      j++
    }
    all[i++]= {
      timeSlot: '放学',
      class2: 'left2'
    }
    let lent=slotNit.length+i;
    for(var j=0;i<lent;i++){
      let obj=slotNit[j]
      obj.timeStart=obj.timeSlot.split("~")[0]
      obj.timeEnd=obj.timeSlot.split("~")[1]
      all[i]=obj
      j++
    }
    this.setData({
      times:all,
      moningTh:slotM.length,
      noonTh:slotN.length
    })
  }
})