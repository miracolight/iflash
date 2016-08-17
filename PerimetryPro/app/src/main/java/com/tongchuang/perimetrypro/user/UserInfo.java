package com.tongchuang.perimetrypro.user;

/**
 * Created by qingdi on 8/5/16.
 */
public class UserInfo {
    public static enum Role {ADMIN, DOCTOR, PATIENT};

    private Integer     userId;
    private Role        role;
    private Integer     subjectId;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDoctorId() {
        if (role == Role.DOCTOR) {
            return subjectId;
        }
        return null;
    }

    public Integer getPatientId() {
        if (role == Role.PATIENT) {
            return subjectId;
        }
        return null;
    }
}
