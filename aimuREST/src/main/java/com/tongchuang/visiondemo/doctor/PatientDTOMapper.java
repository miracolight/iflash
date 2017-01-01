package com.tongchuang.visiondemo.doctor;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.tongchuang.visiondemo.ApplicationConstants.EntityStatus;
import com.tongchuang.visiondemo.patient.PatientConstants.Gender;
import com.tongchuang.visiondemo.patient.dto.PatientDTO;
import com.tongchuang.visiondemo.patient.entity.Patient;

public class PatientDTOMapper implements RowMapper {

	//private Logger logger;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private Gson gson = new Gson();

	@Override
	public PatientDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		Patient patient = new Patient();
		patient.setAddress(rs.getString("address"));
		patient.setBirthdate(rs.getDate("birthdate"));
		patient.setEmail(rs.getString("email"));
		patient.setGender(Gender.valueOf(rs.getString("gender")));
		patient.setName(rs.getString("name"));
		patient.setPatientId(rs.getString("patient_id"));
		patient.setLastUpdateDate(rs.getDate("last_update_date")); // added by Ming to enable query patients by doctor_id to expose last_update_date
		patient.setPhone(rs.getString("phone"));
		patient.setStatus(EntityStatus.valueOf(rs.getString("status")));

//		logger.info("ming@ PatientDTOMapper = " + gson.toJson(patient, Patient.class));
		
		
		PatientDTO result = new PatientDTO(patient, null);
		result.setRelationshipId(rs.getInt("relationship_id"));
		
//		logger.info("ming@ PatientDTOMapper... result = " + result.getLastUpdateDate());	
//		logger.info("ming@ PatientDTOMapper... result = " + gson.toJson(result, PatientDTO.class));
//		
		return result;
	}

}
