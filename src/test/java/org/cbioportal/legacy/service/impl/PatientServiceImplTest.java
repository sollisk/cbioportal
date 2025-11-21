package org.cbioportal.legacy.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cbioportal.legacy.model.Patient;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.PatientRepository;
import org.cbioportal.legacy.service.StudyService;
import org.cbioportal.legacy.service.exception.PatientNotFoundException;
import org.cbioportal.legacy.service.exception.StudyNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class PatientServiceImplTest extends BaseServiceImplTest {

  @InjectMocks private PatientServiceImpl patientService;

  @Mock private PatientRepository patientRepository;
  @Mock private StudyService studyService;

  @Before
  public void setup() {
    ReflectionTestUtils.setField(patientService, "AUTHENTICATE", "false");
  }

  @Test
  public void getAllPatients() throws Exception {

    List<Patient> expectedPatientList = new ArrayList<>();
    Patient patient = new Patient();
    expectedPatientList.add(patient);

    Mockito.when(
            patientRepository.getAllPatients(
                KEYWORD, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION))
        .thenReturn(expectedPatientList);

    List<Patient> result =
        patientService.getAllPatients(KEYWORD, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION);

    Assertions.assertEquals(expectedPatientList, result);
  }

  @Test
  public void getMetaPatients() throws Exception {

    BaseMeta expectedBaseMeta = new BaseMeta();

    Mockito.when(patientRepository.getMetaPatients(KEYWORD)).thenReturn(expectedBaseMeta);

    BaseMeta result = patientService.getMetaPatients(KEYWORD);

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getAllPatientsInStudy() throws Exception {

    List<Patient> expectedPatientList = new ArrayList<>();
    Patient patient = new Patient();
    expectedPatientList.add(patient);

    Mockito.when(
            patientRepository.getAllPatientsInStudy(
                STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION))
        .thenReturn(expectedPatientList);

    List<Patient> result =
        patientService.getAllPatientsInStudy(
            STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION);

    Assertions.assertEquals(expectedPatientList, result);
  }

  @Test
  public void getAllPatientsInStudyNotFound() throws Exception {

    Mockito.when(studyService.getStudy(STUDY_ID)).thenThrow(new StudyNotFoundException(STUDY_ID));
    Assertions.assertThrows(StudyNotFoundException.class, () ->
        patientService.getAllPatientsInStudy(
            STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION));
  }

  @Test
  public void getMetaPatientsInStudy() throws Exception {

    BaseMeta expectedBaseMeta = new BaseMeta();
    Mockito.when(patientRepository.getMetaPatientsInStudy(STUDY_ID)).thenReturn(expectedBaseMeta);
    BaseMeta result = patientService.getMetaPatientsInStudy(STUDY_ID);

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getMetaPatientsInStudyNotFound() throws Exception {

    Mockito.when(studyService.getStudy(STUDY_ID)).thenThrow(new StudyNotFoundException(STUDY_ID));
    Assertions.assertThrows(StudyNotFoundException.class, () -> 
        patientService.getMetaPatientsInStudy(STUDY_ID));
  }

  @Test
  public void getPatientInStudyPatientNotFound() {

    Mockito.when(patientRepository.getPatientInStudy(STUDY_ID, PATIENT_ID_1)).thenReturn(null);
    Assertions.assertThrows(PatientNotFoundException.class, () -> patientService.getPatientInStudy(STUDY_ID, PATIENT_ID_1));
  }

  @Test
  public void getPatientInStudyNotFound() throws Exception {

    Mockito.when(studyService.getStudy(STUDY_ID)).thenThrow(new StudyNotFoundException(STUDY_ID));
    Assertions.assertThrows(StudyNotFoundException.class, () -> 
        patientService.getPatientInStudy(STUDY_ID, PATIENT_ID_1));
  }

  @Test
  public void getPatientInStudy() throws Exception {

    Patient expectedPatient = new Patient();
    Mockito.when(patientRepository.getPatientInStudy(STUDY_ID, PATIENT_ID_1))
        .thenReturn(expectedPatient);
    Patient result = patientService.getPatientInStudy(STUDY_ID, PATIENT_ID_1);

    Assertions.assertEquals(expectedPatient, result);
  }

  @Test
  public void fetchPatients() {

    List<Patient> expectedPatientList = new ArrayList<>();
    Patient patient = new Patient();
    expectedPatientList.add(patient);

    Mockito.when(
            patientRepository.fetchPatients(
                List.of(STUDY_ID), List.of(PATIENT_ID_1), PROJECTION))
        .thenReturn(expectedPatientList);

    List<Patient> result =
        patientService.fetchPatients(
            List.of(STUDY_ID), List.of(PATIENT_ID_1), PROJECTION);

    Assertions.assertEquals(expectedPatientList, result);
  }

  @Test
  public void fetchMetaPatients() {

    BaseMeta expectedBaseMeta = new BaseMeta();
    Mockito.when( 
            patientRepository.fetchMetaPatients(
                List.of(STUDY_ID), List.of(PATIENT_ID_1)))
        .thenReturn(expectedBaseMeta);
    BaseMeta result =
        patientService.fetchMetaPatients(List.of(STUDY_ID), List.of(PATIENT_ID_1));

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getPatientsOfSamples() {

    List<Patient> patients = new ArrayList<>();
    Patient patient = new Patient();
    patient.setStableId(PATIENT_ID_1);
    patient.setCancerStudyIdentifier(STUDY_ID);
    patients.add(patient);

    Mockito.when(
            patientRepository.getPatientsOfSamples(
                List.of(STUDY_ID), List.of(SAMPLE_ID1)))
        .thenReturn(patients);

    List<Patient> result =
        patientService.getPatientsOfSamples(List.of(STUDY_ID), List.of(SAMPLE_ID1));

    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(PATIENT_ID_1, result.getFirst().getStableId());
  }
}
