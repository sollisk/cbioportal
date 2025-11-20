package org.cbioportal.legacy.persistence.mybatis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.cbioportal.legacy.model.CancerStudy;
import org.cbioportal.legacy.model.Patient;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.mybatis.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {PatientMyBatisRepository.class, TestConfig.class})
public class PatientMyBatisRepositoryTest {

  @Autowired private PatientMyBatisRepository patientMyBatisRepository;

  @Test
  public void getAllPatients() throws Exception {

    List<Patient> result =
        patientMyBatisRepository.getAllPatients(null, "ID", null, null, null, null);

    Assertions.assertEquals(18, result.size());
    Patient patient = result.get(0);
    Assertions.assertEquals((Integer) 1, patient.getInternalId());
    Assertions.assertEquals("TCGA-A1-A0SB", patient.getStableId());
    Assertions.assertNull(patient.getCancerStudy());
  }

  @Test
  public void getAllPatientsByKeywordMatchingSample() {
    // Sample TCGA-A1-A0SB-02 belongs to patient TCGA-A1-A0SB
    List<Patient> result =
        patientMyBatisRepository.getAllPatients("TCGA-A1-A0SB-02", "ID", null, null, null, null);
    List<String> actual = result.stream().map(Patient::getStableId).collect(Collectors.toList());
    List<String> expected = Collections.singletonList("TCGA-A1-A0SB");

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void getMetaPatients() throws Exception {

    BaseMeta result = patientMyBatisRepository.getMetaPatients(null);

    Assertions.assertEquals((Integer) 18, result.getTotalCount());
  }

  @Test
  public void getAllPatientsInStudyIdProjection() throws Exception {

    List<Patient> result =
        patientMyBatisRepository.getAllPatientsInStudy(
            "study_tcga_pub", "ID", null, null, null, null);

    Assertions.assertEquals(14, result.size());
    Patient patient = result.get(0);
    Assertions.assertEquals((Integer) 1, patient.getInternalId());
    Assertions.assertEquals("TCGA-A1-A0SB", patient.getStableId());
    Assertions.assertNull(patient.getCancerStudy());
  }

  @Test
  public void getAllPatientsInStudySummaryProjection() throws Exception {

    List<Patient> result =
        patientMyBatisRepository.getAllPatientsInStudy(
            "study_tcga_pub", "SUMMARY", null, null, null, null);

    Assertions.assertEquals(14, result.size());
    Patient patient = result.get(0);
    Assertions.assertEquals((Integer) 1, patient.getInternalId());
    Assertions.assertEquals("TCGA-A1-A0SB", patient.getStableId());
    Assertions.assertEquals((Integer) 1, patient.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", patient.getCancerStudyIdentifier());
    Assertions.assertNull(patient.getCancerStudy());
  }

  @Test
  public void getAllPatientsInStudyDetailedProjection() throws Exception {

    List<Patient> result =
        patientMyBatisRepository.getAllPatientsInStudy(
            "study_tcga_pub", "DETAILED", null, null, null, null);

    Assertions.assertEquals(14, result.size());
    Patient patient = result.get(0);
    Assertions.assertEquals((Integer) 1, patient.getInternalId());
    Assertions.assertEquals("TCGA-A1-A0SB", patient.getStableId());
    Assertions.assertEquals((Integer) 1, patient.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", patient.getCancerStudyIdentifier());
    CancerStudy cancerStudy = patient.getCancerStudy();
    Assertions.assertEquals((Integer) 1, cancerStudy.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", cancerStudy.getCancerStudyIdentifier());
    Assertions.assertEquals("brca", cancerStudy.getTypeOfCancerId());
    Assertions.assertEquals("Breast Invasive Carcinoma (TCGA, Nature 2012)", cancerStudy.getName());
    Assertions.assertEquals(
        "<a href=\\\"http://cancergenome.nih.gov/\\\">The Cancer Genome Atlas (TCGA)</a> Breast"
            + " Invasive Carcinoma project. 825 cases.<br><i>Nature 2012.</i> <a href=\\\"http://tcga-data.nci."
            + "nih.gov/tcga/\\\">Raw data via the TCGA Data Portal</a>.",
        cancerStudy.getDescription());
    Assertions.assertEquals(true, cancerStudy.getPublicStudy());
    Assertions.assertEquals("23000897,26451490", cancerStudy.getPmid());
    Assertions.assertEquals("TCGA, Nature 2012, ...", cancerStudy.getCitation());
    Assertions.assertEquals("SU2C-PI3K;PUBLIC;GDAC", cancerStudy.getGroups());
    Assertions.assertEquals((Integer) 0, cancerStudy.getStatus());
  }

  @Test
  public void getAllPatientsInStudySummaryProjection1PageSize() throws Exception {

    List<Patient> result =
        patientMyBatisRepository.getAllPatientsInStudy(
            "study_tcga_pub", "SUMMARY", 1, 0, null, null);

    Assertions.assertEquals(1, result.size());
  }

  @Test
  public void getAllPatientsInStudySummaryProjectionStableIdSort() throws Exception {

    List<Patient> result =
        patientMyBatisRepository.getAllPatientsInStudy(
            "study_tcga_pub", "SUMMARY", null, null, "stableId", "ASC");

    Assertions.assertEquals(14, result.size());
    Assertions.assertEquals("TCGA-A1-A0SB", result.get(0).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SD", result.get(1).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SE", result.get(2).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SF", result.get(3).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SG", result.get(4).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SH", result.get(5).getStableId());
  }

  @Test
  public void getMetaPatientsInStudy() throws Exception {

    BaseMeta result = patientMyBatisRepository.getMetaPatientsInStudy("study_tcga_pub");

    Assertions.assertEquals((Integer) 14, result.getTotalCount());
  }

  @Test
  public void getPatientInStudyNullResult() throws Exception {

    Patient result =
        patientMyBatisRepository.getPatientInStudy("study_tcga_pub", "invalid_patient");

    Assertions.assertNull(result);
  }

  @Test
  public void getPatientInStudy() throws Exception {

    Patient patient = patientMyBatisRepository.getPatientInStudy("study_tcga_pub", "TCGA-A1-A0SI");

    Assertions.assertEquals((Integer) 7, patient.getInternalId());
    Assertions.assertEquals("TCGA-A1-A0SI", patient.getStableId());
    Assertions.assertEquals((Integer) 1, patient.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", patient.getCancerStudyIdentifier());
    CancerStudy cancerStudy = patient.getCancerStudy();
    Assertions.assertEquals((Integer) 1, cancerStudy.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", cancerStudy.getCancerStudyIdentifier());
    Assertions.assertEquals("brca", cancerStudy.getTypeOfCancerId());
    Assertions.assertEquals("Breast Invasive Carcinoma (TCGA, Nature 2012)", cancerStudy.getName());
    Assertions.assertEquals(
        "<a href=\\\"http://cancergenome.nih.gov/\\\">The Cancer Genome Atlas (TCGA)</a> Breast"
            + " Invasive Carcinoma project. 825 cases.<br><i>Nature 2012.</i> <a href=\\\"http://tcga-data.nci."
            + "nih.gov/tcga/\\\">Raw data via the TCGA Data Portal</a>.",
        cancerStudy.getDescription());
    Assertions.assertEquals(true, cancerStudy.getPublicStudy());
    Assertions.assertEquals("23000897,26451490", cancerStudy.getPmid());
    Assertions.assertEquals("TCGA, Nature 2012, ...", cancerStudy.getCitation());
    Assertions.assertEquals("SU2C-PI3K;PUBLIC;GDAC", cancerStudy.getGroups());
    Assertions.assertEquals((Integer) 0, cancerStudy.getStatus());
  }

  @Test
  public void fetchPatients() throws Exception {

    List<String> studyIds = new ArrayList<>();
    studyIds.add("study_tcga_pub");
    studyIds.add("study_tcga_pub");
    List<String> patientIds = new ArrayList<>();
    patientIds.add("TCGA-A1-A0SB");
    patientIds.add("TCGA-A1-A0SE");

    List<Patient> result = patientMyBatisRepository.fetchPatients(studyIds, patientIds, "SUMMARY");

    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("TCGA-A1-A0SB", result.get(0).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SE", result.get(1).getStableId());
  }

  @Test
  public void fetchMetaPatients() throws Exception {

    List<String> studyIds = new ArrayList<>();
    studyIds.add("study_tcga_pub");
    studyIds.add("study_tcga_pub");
    List<String> patientIds = new ArrayList<>();
    patientIds.add("TCGA-A1-A0SB");
    patientIds.add("TCGA-A1-A0SE");

    BaseMeta result = patientMyBatisRepository.fetchMetaPatients(studyIds, patientIds);

    Assertions.assertEquals((Integer) 2, result.getTotalCount());
  }

  @Test
  public void getPatientIdsOfSamples() throws Exception {

    List<Patient> result =
        patientMyBatisRepository.getPatientsOfSamples(
            Arrays.asList("study_tcga_pub", "study_tcga_pub", "study_tcga_pub"),
            Arrays.asList("TCGA-A1-A0SB-01", "TCGA-A1-A0SD-01", "TCGA-A1-A0SB-02"));

    Assertions.assertEquals(2, result.size());
    List<String> stableIds = result.stream().map(r -> r.getStableId()).collect(Collectors.toList());
    Assertions.assertTrue(stableIds.contains("TCGA-A1-A0SD"));
    Assertions.assertTrue(stableIds.contains("TCGA-A1-A0SB"));
  }
}
