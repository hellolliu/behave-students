package org.java.behave.admin.vo;

import org.apache.commons.lang3.ArrayUtils;
import org.java.behave.db.domain.BehaveScheduleValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleVo {
    private String id,label,
            oneS,twoS,threeS,fourS,fiveS,sixS,sevenS,eightS,nineS,tenS,elevenS,twelveS,thirteenS,
            oneT,twoT,threeT,fourT,fiveT,sixT,sevenT,eightT,nineT,tenT,elevenT,twelveT,thirteenT;

    public ScheduleVo(String id, String label, List<BehaveScheduleValue> scheduleValues){
        this.id=id;
        this.label=label;
        if (scheduleValues!=null&&scheduleValues.size()!=0){
            for (int i=0;i<scheduleValues.size();i++){
                switch (scheduleValues.get(i).getSlotOrderFie()){
                    case 1:
                        this.oneS=scheduleValues.get(i).getCourseName();
                        this.oneT=scheduleValues.get(i).getCourseUser();
                        break;
                    case 2:
                        this.twoS=scheduleValues.get(i).getCourseName();
                        this.twoT=scheduleValues.get(i).getCourseUser();
                        break;
                    case 3:
                        this.threeS=scheduleValues.get(i).getCourseName();
                        this.threeT=scheduleValues.get(i).getCourseUser();
                        break;
                    case 4:
                        this.fourS=scheduleValues.get(i).getCourseName();
                        this.fourT=scheduleValues.get(i).getCourseUser();
                        break;
                    case 5:
                        this.fiveS=scheduleValues.get(i).getCourseName();
                        this.fiveT=scheduleValues.get(i).getCourseUser();
                        break;
                    case 6:
                        this.sixS=scheduleValues.get(i).getCourseName();
                        this.sixT=scheduleValues.get(i).getCourseUser();
                        break;
                    case 7:
                        this.sevenS=scheduleValues.get(i).getCourseName();
                        this.sevenT=scheduleValues.get(i).getCourseUser();
                        break;
                    case 8:
                        this.eightS=scheduleValues.get(i).getCourseName();
                        this.eightT=scheduleValues.get(i).getCourseUser();
                        break;
                    case 9:
                        this.nineS=scheduleValues.get(i).getCourseName();
                        this.nineT=scheduleValues.get(i).getCourseUser();
                        break;
                    case 10: this.tenS=scheduleValues.get(i).getCourseName();
                        this.tenT=scheduleValues.get(i).getCourseUser();
                        break;
                    case 11:
                        this.elevenS=scheduleValues.get(i).getCourseName();
                        this.elevenT=scheduleValues.get(i).getCourseUser();
                        break;
                    case 12:
                        this.twelveS=scheduleValues.get(i).getCourseName();
                        this.twelveT=scheduleValues.get(i).getCourseUser();
                        break;
                    case 13:
                        this.thirteenS=scheduleValues.get(i).getCourseName();
                        this.thirteenT=scheduleValues.get(i).getCourseUser();
                        break;
                }
            }
//            if(scheduleValues.size()>0&&scheduleValues.get(0)!=null&&scheduleValues.get(0).getSlotOrderFie()==(1)){
//                this.oneS=scheduleValues.get(0).getCourseName();
//                this.oneT=scheduleValues.get(0).getCourseUser();
//            }
//            if(scheduleValues.size()>1&&scheduleValues.get(1)!=null&&scheduleValues.get(1).getSlotOrderFie()==(2)){
//                this.twoS=scheduleValues.get(1).getCourseName();
//                this.twoT=scheduleValues.get(1).getCourseUser();
//            }
//            if(scheduleValues.size()>2&&scheduleValues.get(2)!=null&&scheduleValues.get(2).getSlotOrderFie()==(3)){
//                this.threeS=scheduleValues.get(2).getCourseName();
//                this.threeT=scheduleValues.get(2).getCourseUser();
//            }
//            if(scheduleValues.size()>3&&scheduleValues.get(3)!=null&&scheduleValues.get(3).getSlotOrderFie()==(4)){
//                this.fourS=scheduleValues.get(3).getCourseName();
//                this.fourT=scheduleValues.get(3).getCourseUser();
//            }
//            if(scheduleValues.size()>4&&scheduleValues.get(4)!=null&&scheduleValues.get(4).getSlotOrderFie()==(5)){
//                this.fiveS=scheduleValues.get(4).getCourseName();
//                this.fiveT=scheduleValues.get(4).getCourseUser();
//            }
//            if(scheduleValues.size()>5&&scheduleValues.get(5)!=null&&scheduleValues.get(5).getSlotOrderFie()==(6)){
//                this.sixS=scheduleValues.get(5).getCourseName();
//                this.sixT=scheduleValues.get(5).getCourseUser();
//            }
//            if(scheduleValues.size()>6&&scheduleValues.get(6)!=null&&scheduleValues.get(6).getSlotOrderFie()==(7)){
//                this.sevenS=scheduleValues.get(6).getCourseName();
//                this.sevenT=scheduleValues.get(6).getCourseUser();
//            }
//            if(scheduleValues.size()>7&&scheduleValues.get(7)!=null&&scheduleValues.get(7).getSlotOrderFie()==(8)){
//                this.eightS=scheduleValues.get(7).getCourseName();
//                this.eightT=scheduleValues.get(7).getCourseUser();
//            }
//            if(scheduleValues.size()>8&&scheduleValues.get(8)!=null&&scheduleValues.get(8).getSlotOrderFie()==(9)){
//                this.nineS=scheduleValues.get(8).getCourseName();
//                this.nineT=scheduleValues.get(8).getCourseUser();
//            }
//            if(scheduleValues.size()>9&&scheduleValues.get(9)!=null&&scheduleValues.get(9).getSlotOrderFie()==(10)){
//                this.tenS=scheduleValues.get(9).getCourseName();
//                this.tenT=scheduleValues.get(9).getCourseUser();
//            }
//            if(scheduleValues.size()>10&&scheduleValues.get(10)!=null&&scheduleValues.get(10).getSlotOrderFie()==(11)){
//                this.elevenS=scheduleValues.get(10).getCourseName();
//                this.elevenT=scheduleValues.get(10).getCourseUser();
//            }
//            if(scheduleValues.size()>11&&scheduleValues.get(11)!=null&&scheduleValues.get(11).getSlotOrderFie()==(12)){
//                this.twelveS=scheduleValues.get(11).getCourseName();
//                this.twelveT=scheduleValues.get(11).getCourseUser();
//            }
//            if(scheduleValues.size()>12&&scheduleValues.get(12)!=null&&scheduleValues.get(11).getSlotOrderFie()==13){
//                this.thirteenS=scheduleValues.get(12).getCourseName();
//                this.thirteenT=scheduleValues.get(12).getCourseUser();
//            }
        }
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getOneS() {
        return oneS;
    }

    public void setOneS(String oneS) {
        this.oneS = oneS;
    }

    public String getTwoS() {
        return twoS;
    }

    public void setTwoS(String twoS) {
        this.twoS = twoS;
    }

    public String getThreeS() {
        return threeS;
    }

    public void setThreeS(String threeS) {
        this.threeS = threeS;
    }

    public String getFourS() {
        return fourS;
    }

    public void setFourS(String fourS) {
        this.fourS = fourS;
    }

    public String getFiveS() {
        return fiveS;
    }

    public void setFiveS(String fiveS) {
        this.fiveS = fiveS;
    }

    public String getSixS() {
        return sixS;
    }

    public void setSixS(String sixS) {
        this.sixS = sixS;
    }

    public String getSevenS() {
        return sevenS;
    }

    public void setSevenS(String sevenS) {
        this.sevenS = sevenS;
    }

    public String getEightS() {
        return eightS;
    }

    public void setEightS(String eightS) {
        this.eightS = eightS;
    }

    public String getNineS() {
        return nineS;
    }

    public void setNineS(String nineS) {
        this.nineS = nineS;
    }

    public String getTenS() {
        return tenS;
    }

    public void setTenS(String tenS) {
        this.tenS = tenS;
    }

    public String getElevenS() {
        return elevenS;
    }

    public void setElevenS(String elevenS) {
        this.elevenS = elevenS;
    }

    public String getTwelveS() {
        return twelveS;
    }

    public void setTwelveS(String twelveS) {
        this.twelveS = twelveS;
    }

    public String getOneT() {
        return oneT;
    }

    public void setOneT(String oneT) {
        this.oneT = oneT;
    }

    public String getTwoT() {
        return twoT;
    }

    public void setTwoT(String twoT) {
        this.twoT = twoT;
    }

    public String getThreeT() {
        return threeT;
    }

    public void setThreeT(String threeT) {
        this.threeT = threeT;
    }

    public String getFourT() {
        return fourT;
    }

    public void setFourT(String fourT) {
        this.fourT = fourT;
    }

    public String getFiveT() {
        return fiveT;
    }

    public void setFiveT(String fiveT) {
        this.fiveT = fiveT;
    }

    public String getSixT() {
        return sixT;
    }

    public void setSixT(String sixT) {
        this.sixT = sixT;
    }

    public String getSevenT() {
        return sevenT;
    }

    public void setSevenT(String sevenT) {
        this.sevenT = sevenT;
    }

    public String getEightT() {
        return eightT;
    }

    public void setEightT(String eightT) {
        this.eightT = eightT;
    }

    public String getNineT() {
        return nineT;
    }

    public void setNineT(String nineT) {
        this.nineT = nineT;
    }

    public String getTenT() {
        return tenT;
    }

    public void setTenT(String tenT) {
        this.tenT = tenT;
    }

    public String getElevenT() {
        return elevenT;
    }

    public void setElevenT(String elevenT) {
        this.elevenT = elevenT;
    }

    public String getTwelveT() {
        return twelveT;
    }

    public void setTwelveT(String twelveT) {
        this.twelveT = twelveT;
    }

    public String getThirteenS() {
        return thirteenS;
    }

    public void setThirteenS(String thirteenS) {
        this.thirteenS = thirteenS;
    }

    public String getThirteenT() {
        return thirteenT;
    }

    public void setThirteenT(String thirteenT) {
        this.thirteenT = thirteenT;
    }
}
