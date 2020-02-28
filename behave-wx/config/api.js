// 以下是业务服务器API地址
// 本机开发时使用
var WxApiRoot = 'http://localhost:8080/wx/';
// 局域网测试使用
// var WxApiRoot = 'http://192.168.0.101:8080/wx/';
// 云平台部署时使用
// var WxApiRoot = 'http://122.51.199.160:8080/wx/';
// 云平台上线时使用
// var WxApiRoot = 'https://www.menethil.com.cn/wx/';

module.exports = {
  AuthLoginByWeixin: WxApiRoot + 'auth/login_by_weixin', //微信登录
  AuthLoginByAccount: WxApiRoot + 'auth/login', //账号登录
  AuthLogout: WxApiRoot + 'auth/logout', //账号登出
  AuthRegister: WxApiRoot + 'auth/register', //账号注册
  AuthReset: WxApiRoot + 'auth/reset', //账号密码重置
  AuthBindPhone: WxApiRoot + 'auth/bindPhone', //绑定微信手机号
  AllClass:WxApiRoot+'class/all',//查询所有班级
  StudentUpdate: WxApiRoot + 'auth/studentUpdate', //学生信息更新
  

  TeacherScheudleToday:WxApiRoot+'schedule/schTeaToday',//查询教师今天课程
  ScheduleIdToclass:WxApiRoot+'schedule/toClass',//根据课程id查询班级信息
  AddStudentScore:WxApiRoot+'score/add',//添加学生提问评分
  TeacherQuestionEnable:WxApiRoot+'teacher/enableAllQuestion',//可用练习列表
  TeacherQuestionAll:WxApiRoot+'teacher/allQuestion',//按日期查询所有
  TeacherQuestionAdd:WxApiRoot+'teacher/addQuestion',//可用练习添加
  TeacherQuestionUpdate:WxApiRoot+'teacher/updateQuestion',//可用练习添加
  TeacherQuestionDelete:WxApiRoot+'teacher/deleteQuestion',//可用练习delete
  TeacherQuestionQC:WxApiRoot+'teacher/addOrUpdateQC',//发布练习
  TeacherAddScore:WxApiRoot+'teacher/teacherAddScore',//教师批改作业
  TeacherQueryScore:WxApiRoot+'teacher/teacherQueryScore',//教师查课堂评分
  TeacherScheduleAll:WxApiRoot+'schedule/teacherSchedule',//教师的所有课程
  
  StudentsByClass:WxApiRoot+'schedule/studentsByClass',//根据班级id查询学生列表
  StudentAnswer:WxApiRoot+'teacher/studentAnswers',//学生作业
  StudentAllCourse:WxApiRoot+'schedule/allCourse',//查询学生本人今日课程
  StudentIntoClass:WxApiRoot+'student/intoClassValue',//查询学生本人今日课程
  StudentQueryScore:WxApiRoot+'student/studentQueryScore',//学生查课堂评分
  StudentAnswerAll:WxApiRoot+'student/studentAnswerAll',//学生查课堂作业
  StudentAnswerQuestion:WxApiRoot+'student/studentAnswer',//学生回答问题
  StudentScheduleAll:WxApiRoot+'schedule/studentSchedule',//学生的所有课程
  
};