package com.vvavy.visiondemo.object;

import android.graphics.Point;

import com.vvavy.visiondemo.service.PerimetryTestService;
import com.vvavy.visiondemo.service.impl.DefaultIntensityServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingdi on 4/15/16.
 */
public class ExamResult {
    public static final String DUMMY_PATIENT_ID="dummyPatient";
    public static final int AMPLIFICATION=3;
    private int     id;
    private String  patientId;
    private String  result;
    private long    examDate;
    private String  uploaded;
    private Integer     serverId;
    private String  testDeviceId;

    public ExamResult() {
    }

    public ExamResult(String patientId, long examDate, String result, String uploaded) {
        super();
        this.patientId = patientId;
        this.examDate = examDate;
        this.result = result;
        this.uploaded = uploaded;
    }

    public ExamResult(PerimetryTestService exam) {
        this.patientId = DUMMY_PATIENT_ID;
        this.examDate = System.currentTimeMillis();
        this.uploaded = "N";

        StringBuilder strBlder = new StringBuilder();
        strBlder.append(exam.getExamType()== PerimetryTestService.ExamType.LEFT? exam.getCenterLeftX():exam.getCenterRightX())
                .append(":").append(exam.getCenterY()).append(";");
        for (PerimetryStimulus p : exam.getExamResult()) {
            strBlder.append(p.getIntensity().getDb()).append(":").append(p.getPoint().x).append(":").append(p.getPoint().y).append(",");
        }

        this.result = strBlder.toString();
    }

    public List<PerimetryStimulus> getPerimetryStimulus() {
        List<PerimetryStimulus> r = new ArrayList<PerimetryStimulus>();
        String[] data = this.result.split(";");
        String[] center = data[0].split(":");
        int centerX = Integer.parseInt(center[0]);
        int centerY = Integer.parseInt(center[1]);

        for(String ps : data[1].split(",")) {
            String[] v = ps.split(":");
            PerimetryStimulus p = new PerimetryStimulus(new Point(centerX+Integer.parseInt(v[1])*AMPLIFICATION,
                                                                  centerY+Integer.parseInt(v[2])*AMPLIFICATION),
                                DefaultIntensityServiceImpl.ALL_INTENSITIES[Integer.parseInt(v[0])]);
            r.add(p);
        }
        return r;
    }

    //getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public long getExamDate() {
        return examDate;
    }

    public void setExamDate(long examDate) {
        this.examDate = examDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUploaded() {
        return uploaded;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }

    public String getTestDeviceId() {
        return testDeviceId;
    }

    public void setTestDeviceId(String testDeviceId) {
        this.testDeviceId = testDeviceId;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    @Override
    public String toString() {
        return "ExamResult{" +
                "id=" + id +
                ", patientId='" + patientId + '\'' +
                ", result='" + result + '\'' +
                ", examDate=" + examDate +
                ", uploaded='" + uploaded + '\'' +
                ", serverId=" + serverId +
                ", testDeviceId='" + testDeviceId + '\'' +
                '}';
    }
}
