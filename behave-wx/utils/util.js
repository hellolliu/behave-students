var api = require('../config/api.js');
var app = getApp();

function formatTime(date) {
  var year = date.getFullYear()
  var month = date.getMonth() + 1
  var day = date.getDate()

  var hour = date.getHours()
  var minute = date.getMinutes()
  var second = date.getSeconds()


  return [year, month, day].map(formatNumber).join('-') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

function formatNumber(n) {
  n = n.toString()
  return n[1] ? n : '0' + n
}

/**
 * 封封微信的的request
 */
function request(url, data = {}, method = "GET") {
  return new Promise(function(resolve, reject) {
    wx.request({
      url: url,
      data: data,
      method: method,
      header: {
        'Content-Type': 'application/json',
        'X-Litemall-Token': wx.getStorageSync('token')
      },
      success: function(res) {

        if (res.statusCode == 200) {

          if (res.data.errno == 501) {
            // 清除登录相关内容
            try {
              wx.removeStorageSync('userInfo');
              wx.removeStorageSync('token');
            } catch (e) {
              // Do something when catch error
            }
            // 切换到登录页面
            wx.navigateTo({
              url: '/pages/auth/login/login'
            });
          } else {
            resolve(res.data);
          }
        } else {
          reject(res.errMsg);
        }

      },
      fail: function(err) {
        reject(err)
      }
    })
  });
}

function redirect(url) {

  //判断页面是否需要登录
  if (false) {
    wx.redirectTo({
      url: '/pages/auth/login/login'
    });
    return false;
  } else {
    wx.redirectTo({
      url: url
    });
  }
}

function showErrorToast(msg) {
  wx.showToast({
    title: msg,
    image: '/static/images/icon_error.png'
  })
}
let timeArr = halfHour();
function halfHour(){
  let timeArr = [];
  for (let i =0; i<=48; i++){
    if(i % 2==0){
      timeArr.push(formatNumber(i/2) +':00');
    }else{
      timeArr.push(formatNumber(Math.floor(i/2))+":30")
    }
  }
  return timeArr;
}
function setTimeHalf(){
  var thisDate = new Date(), thisTime = formatTime(thisDate),lastTimeArr = [],index = 0;
  
 timeArr.map(function(t,i){
    let tArr = t.split(":");
    if (thisTime[0] >= Number(tArr[0])){
      index = thisTime[1]<=30?i:i+1;
    }
 })
 lastTimeArr = timeArr.slice(index);
 if (thisTime[1] !== 0 && thisTime[1]!==30){
    lastTimeArr.unshift(thisTime[0]+":"+thisTime[1]);
  }
  return lastTimeArr;
}
module.exports = {
  formatTime,
  request,
  redirect,
  showErrorToast,
  setTimeHalf
}