package com.tongchuang.perimetrypro.user;

/**
 * Created by qingdi on 8/5/16.
 */
public class UserInfo {
    public static enum Role {ADMIN, DOCTOR, PATIENT};

    private Integer     userId;
    private Role        role;
    private String      subjectId;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDoctorId() {
        if (role == Role.DOCTOR) {
            return subjectId;
        }
        return null;
    }

    public String getPatientId() {
        if (role == Role.PATIENT) {
            return subjectId;
        }
        return null;
    }
}
