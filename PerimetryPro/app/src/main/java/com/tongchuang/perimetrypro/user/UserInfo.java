package com.tongchuang.perimetrypro.user;

/**
 * Created by qingdi on 8/5/16.
 */
public class UserInfo {
    public static enum Role {ADMIN, DOCTOR, PATIENT};

    private int     userId;
    private Role    userRole;

    private Integer patientId;
    private Integer doctorId;

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }
}
