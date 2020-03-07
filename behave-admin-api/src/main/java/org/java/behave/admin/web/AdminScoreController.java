package org.java.behave.admin.web;

import io.swagger.models.auth.In;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.java.behave.db.domain.BehaveCourse;
import org.java.behave.db.domain.BehaveUser;
import org.java.behave.db.domain.BehaveUserScore;
import org.java.behave.db.service.BehaveCourseService;
import org.java.behave.db.service.BehaveUserScoreService;
import org.java.behave.db.service.BehaveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/admin/score")
public class AdminScoreController {
    @Autowired
    private BehaveUserScoreService scoreService;
    @Autowired
    private BehaveUserService userService;
    @Autowired
    private BehaveCourseService courseService;
    @PostMapping("export")
    public void exportExcel(Integer teacherid,Integer courseid,HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (teacherid==null||courseid==null)
            return;
        Map<Integer,List<BehaveUserScore>> map=scoreService.queryStudents(teacherid,courseid);
        BehaveUser user=userService.findById(teacherid);
        BehaveCourse course=courseService.findById(courseid);
        HSSFWorkbook wb =export(map,user,course);
        response.setContentType("application/vnd.ms-excel");
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
        String fileName = df.format(new Date());// new Date()为获取当前系统时间
        response.setHeader("Content-disposition", "attachment;fileName=" + "学生平时成绩" + fileName + ".xls");
        response.setContentType("application/octet-stream;charset=utf-8");
        OutputStream ouputStream = response.getOutputStream();
        wb.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }
    private HSSFWorkbook export(Map<Integer,List<BehaveUserScore>> map,BehaveUser user,BehaveCourse course) {
        int maxLength=0;
        Iterator<Map.Entry<Integer,List<BehaveUserScore>>> entries = map.entrySet().iterator();
        while(entries.hasNext()){
            Map.Entry<Integer,List<BehaveUserScore>> key = entries.next();
            if (key.getValue().size()>maxLength)
                maxLength=key.getValue().size();
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        initFistSheet(wb,map,maxLength,user,course);
        return wb;
    }
    private HSSFWorkbook initFistSheet(HSSFWorkbook wb ,Map<Integer,List<BehaveUserScore>> map,int maxLength,BehaveUser user,BehaveCourse course){
        HSSFSheet sheet = wb.createSheet("学生表现");
        HSSFRow row = sheet.createRow((int) 0);
        CellStyle style = styleOne(wb);

        HSSFCell cellFrist = row.createCell(0);
        cellFrist.setCellValue("学生");
        cellFrist.setCellStyle(style);
        sheet.autoSizeColumn(0);
        for (int i = 1,j=0; j < maxLength; i++,j++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue("表现分"+i);
            cell.setCellStyle(style);
            sheet.autoSizeColumn(i);
        }
        Iterator<Map.Entry<Integer,List<BehaveUserScore>>> entries = map.entrySet().iterator();
        int index=1;
        List<List<String>> allValues=new ArrayList<>();
        while(entries.hasNext()){
            //总分
            Integer total=0;
            List<String> stringList=new ArrayList<>();

            Map.Entry<Integer,List<BehaveUserScore>> entry = entries.next();
            List<BehaveUserScore> userScores=entry.getValue();//学生所有表现分
            HSSFRow sheetRow = sheet.createRow(index);
            //每行第一个格子放名字
            HSSFCell cellNm=sheetRow.createCell(0);
            cellNm.setCellValue(userScores.get(0).getUserName());
            for (int i=0,j=1;i<userScores.size();i++,j++) {
                total+=userScores.get(i).getValue();
                HSSFCell cellVl=sheetRow.createCell(j);
                cellVl.setCellValue(userScores.get(i).getValue());
            }
            stringList.add(userScores.get(0).getUserName());//名字
            stringList.add(total+"");//总分
            stringList.add(userScores.size()+"");//次数
            stringList.add((total/userScores.size())+"");//平均分
            allValues.add(stringList);
            index++;
        }
        initSecondSheet(wb,user,course,allValues);
        return wb;
    }
    private HSSFWorkbook initSecondSheet(HSSFWorkbook wb,BehaveUser user,BehaveCourse course ,List<List<String>> allValues){
        HSSFSheet sheet = wb.createSheet("表现汇总");
        HSSFRow fristRow = sheet.createRow((int) 0);
        CellStyle style = styleOne(wb);

        sheet.setColumnWidth(0, 100 * 256);
        sheet.setColumnWidth(1, 100 * 256);

        HSSFCell cellName = fristRow.createCell(0);
        cellName.setCellValue(user.getUsername()+":");
        cellName.setCellStyle(style);
        sheet.autoSizeColumn(0);
        HSSFCell cellCourse = fristRow.createCell(1);
        cellCourse.setCellValue(course.getName());
        cellCourse.setCellStyle(style);
        sheet.autoSizeColumn(1);
        HSSFRow secondRow = sheet.createRow((int)1);
        HSSFCell one=secondRow.createCell(0);
        one.setCellValue("学生");
        one.setCellStyle(style);
        HSSFCell two=secondRow.createCell(1);
        two.setCellValue("总分");
        two.setCellStyle(style);
        HSSFCell three=secondRow.createCell(2);
        three.setCellValue("评论次数");
        three.setCellStyle(style);
        HSSFCell four=secondRow.createCell(3);
        four.setCellValue("平均分");
        four.setCellStyle(style);
        for (int i=0,j=2;i<allValues.size();i++,j++){
            HSSFRow sheetRow = sheet.createRow(j);
            List<String> stringList=allValues.get(i);
            for (int index=0;index<stringList.size();index++){
                sheetRow.createCell(index).setCellValue(stringList.get(index));
            }
        }
        return wb;
    }
    private CellStyle styleOne(HSSFWorkbook wb){
        CellStyle style = wb.createCellStyle();
        Font fontStyle = wb.createFont(); // 字体样式
        fontStyle.setBold(true); // 加粗
        fontStyle.setFontName("黑体"); // 字体
        fontStyle.setFontHeightInPoints((short) 10); // 大小
        style.setFont(fontStyle);
        // 设置样式
//        style.setFillForegroundColor(IndexedColors.AQUA.getIndex());//背景颜色
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        return style;
    }
    private CellStyle styleTwo(HSSFWorkbook wb){
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setWrapText(true);//文本换行
        return style2;
    }
}
