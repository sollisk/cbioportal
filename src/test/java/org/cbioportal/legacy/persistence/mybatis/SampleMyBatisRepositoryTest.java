package org.cbioportal.legacy.persistence.mybatis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.cbioportal.legacy.model.CancerStudy;
import org.cbioportal.legacy.model.Patient;
import org.cbioportal.legacy.model.Sample;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.mybatis.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {SampleMyBatisRepository.class, TestConfig.class})
public class SampleMyBatisRepositoryTest {

  @Autowired private SampleMyBatisRepository sampleMyBatisRepository;

  @Test
  public void getAllSamplesInStudyIdProjection() throws Exception {

    List<Sample> result =
        sampleMyBatisRepository.getAllSamplesInStudy(
            "study_tcga_pub", "ID", null, null, null, null);

    Assertions.assertEquals(15, result.size());
    Sample sample = result.get(0);
    Assertions.assertEquals((Integer) 1, sample.getInternalId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", sample.getStableId());
    Assertions.assertNull(sample.getPatient());
  }

  @Test
  public void getAllSamplesInStudySummaryProjection() throws Exception {

    List<Sample> result =
        sampleMyBatisRepository.getAllSamplesInStudy(
            "study_tcga_pub", "SUMMARY", null, null, null, null);

    Assertions.assertEquals(15, result.size());
    Sample sample = result.get(0);
    Assertions.assertEquals((Integer) 1, sample.getInternalId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", sample.getStableId());
    Assertions.assertEquals(Sample.SampleType.PRIMARY_SOLID_TUMOR, sample.getSampleType());
    Assertions.assertEquals((Integer) 1, sample.getPatientId());
    Assertions.assertEquals("TCGA-A1-A0SB", sample.getPatientStableId());
    Assertions.assertNull(sample.getPatient());
  }

  @Test
  public void getAllSamplesInStudyDetailedProjection() throws Exception {

    List<Sample> result =
        sampleMyBatisRepository.getAllSamplesInStudy(
            "study_tcga_pub", "DETAILED", null, null, null, null);

    Assertions.assertEquals(15, result.size());
    Sample sample = result.get(0);
    Assertions.assertEquals((Integer) 1, sample.getInternalId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", sample.getStableId());
    Assertions.assertEquals(Sample.SampleType.PRIMARY_SOLID_TUMOR, sample.getSampleType());
    Assertions.assertEquals((Integer) 1, sample.getPatientId());
    Assertions.assertEquals("TCGA-A1-A0SB", sample.getPatientStableId());
    Patient patient = sample.getPatient();
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
  public void getAllSamplesInStudySummaryProjection1PageSize() throws Exception {

    List<Sample> result =
        sampleMyBatisRepository.getAllSamplesInStudy("study_tcga_pub", "SUMMARY", 1, 0, null, null);

    Assertions.assertEquals(1, result.size());
  }

  @Test
  public void getAllSamplesInStudySummaryProjectionStableIdSort() throws Exception {

    List<Sample> result =
        sampleMyBatisRepository.getAllSamplesInStudy(
            "study_tcga_pub", "SUMMARY", null, null, "stableId", "ASC");

    Assertions.assertEquals(15, result.size());
    Assertions.assertEquals("TCGA-A1-A0SB-01", result.get(0).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SB-02", result.get(1).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SD-01", result.get(2).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SE-01", result.get(3).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SF-01", result.get(4).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SG-01", result.get(5).getStableId());
  }

  @Test
  public void getMetaSamplesInStudy() throws Exception {

    BaseMeta result = sampleMyBatisRepository.getMetaSamplesInStudy("study_tcga_pub");

    Assertions.assertEquals((Integer) 15, result.getTotalCount());
  }

  @Test
  public void getSampleInStudyNullResult() throws Exception {

    Sample result = sampleMyBatisRepository.getSampleInStudy("study_tcga_pub", "invalid_sample");

    Assertions.assertNull(result);
  }

  @Test
  public void getSampleInStudy() throws Exception {

    Sample sample = sampleMyBatisRepository.getSampleInStudy("study_tcga_pub", "TCGA-A1-A0SI-01");

    Assertions.assertEquals((Integer) 7, sample.getInternalId());
    Assertions.assertEquals("TCGA-A1-A0SI-01", sample.getStableId());
    Assertions.assertEquals(Sample.SampleType.PRIMARY_SOLID_TUMOR, sample.getSampleType());
    Assertions.assertEquals((Integer) 7, sample.getPatientId());
    Assertions.assertEquals("TCGA-A1-A0SI", sample.getPatientStableId());
    Patient patient = sample.getPatient();
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
  public void getAllSamplesOfPatientInStudyIdProjection() throws Exception {

    List<Sample> result =
        sampleMyBatisRepository.getAllSamplesOfPatientInStudy(
            "study_tcga_pub", "TCGA-A1-A0SB", "ID", null, null, null, null);

    Assertions.assertEquals(2, result.size());
    Sample sample = result.get(0);
    Assertions.assertEquals((Integer) 1, sample.getInternalId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", sample.getStableId());
    Assertions.assertNull(sample.getPatient());
  }

  @Test
  public void getAllSamplesOfPatientInStudySummaryProjection() throws Exception {

    List<Sample> result =
        sampleMyBatisRepository.getAllSamplesOfPatientInStudy(
            "study_tcga_pub", "TCGA-A1-A0SB", "SUMMARY", null, null, null, null);

    Assertions.assertEquals(2, result.size());
    Sample sample = result.get(0);
    Assertions.assertEquals((Integer) 1, sample.getInternalId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", sample.getStableId());
    Assertions.assertEquals(Sample.SampleType.PRIMARY_SOLID_TUMOR, sample.getSampleType());
    Assertions.assertEquals((Integer) 1, sample.getPatientId());
    Assertions.assertEquals("TCGA-A1-A0SB", sample.getPatientStableId());
    Assertions.assertNull(sample.getPatient());
  }

  @Test
  public void getAllSamplesOfPatientInStudyDetailedProjection() throws Exception {

    List<Sample> result =
        sampleMyBatisRepository.getAllSamplesOfPatientInStudy(
            "study_tcga_pub", "TCGA-A1-A0SB", "DETAILED", null, null, null, null);

    Assertions.assertEquals(2, result.size());
    Sample sample = result.get(0);
    Assertions.assertEquals((Integer) 1, sample.getInternalId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", sample.getStableId());
    Assertions.assertEquals(Sample.SampleType.PRIMARY_SOLID_TUMOR, sample.getSampleType());
    Assertions.assertEquals((Integer) 1, sample.getPatientId());
    Assertions.assertEquals("TCGA-A1-A0SB", sample.getPatientStableId());
    Patient patient = sample.getPatient();
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
  public void getAllSamplesOfPatientInStudySummaryProjection1PageSize() throws Exception {

    List<Sample> result =
        sampleMyBatisRepository.getAllSamplesOfPatientInStudy(
            "study_tcga_pub", "TCGA-A1-A0SB", "SUMMARY", 1, 0, null, null);

    Assertions.assertEquals(1, result.size());
  }

  @Test
  public void getAllSamplesOfPatientInStudySummaryProjectionStableIdSort() throws Exception {

    List<Sample> result =
        sampleMyBatisRepository.getAllSamplesOfPatientInStudy(
            "study_tcga_pub", "TCGA-A1-A0SB", "SUMMARY", null, null, "stableId", "ASC");

    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("TCGA-A1-A0SB-01", result.get(0).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SB-02", result.get(1).getStableId());
  }

  @Test
  public void getMetaSamplesOfPatientInStudy() throws Exception {

    BaseMeta result =
        sampleMyBatisRepository.getMetaSamplesOfPatientInStudy("study_tcga_pub", "TCGA-A1-A0SB");

    Assertions.assertEquals((Integer) 2, result.getTotalCount());
  }

  @Test
  public void getAllSamplesOfPatientsInStudy() throws Exception {

    List<Sample> result =
        sampleMyBatisRepository.getAllSamplesOfPatientsInStudy(
            "study_tcga_pub", Arrays.asList("TCGA-A1-A0SB", "TCGA-A1-A0SE"), "SUMMARY");

    Assertions.assertEquals(3, result.size());
    Sample sample = result.get(0);
    Assertions.assertEquals((Integer) 1, sample.getInternalId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", sample.getStableId());
    Assertions.assertEquals(Sample.SampleType.PRIMARY_SOLID_TUMOR, sample.getSampleType());
    Assertions.assertEquals((Integer) 1, sample.getPatientId());
    Assertions.assertEquals("TCGA-A1-A0SB", sample.getPatientStableId());
    Assertions.assertNull(sample.getPatient());
  }

  @Test
  public void fetchSamples() throws Exception {

    List<String> studyIds = new ArrayList<>();
    studyIds.add("study_tcga_pub");
    studyIds.add("study_tcga_pub");
    List<String> sampleIds = new ArrayList<>();
    sampleIds.add("TCGA-A1-A0SB-01");
    sampleIds.add("TCGA-A1-A0SE-01");

    List<Sample> result = sampleMyBatisRepository.fetchSamples(studyIds, sampleIds, "SUMMARY");

    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("TCGA-A1-A0SB-01", result.get(0).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SE-01", result.get(1).getStableId());
  }

  @Test
  public void fetchSamplesBySampleListId() throws Exception {

    List<String> sampleListIds = new ArrayList<>();
    sampleListIds.add("study_tcga_pub_all");
    sampleListIds.add("study_tcga_pub_acgh");

    List<Sample> result =
        sampleMyBatisRepository.fetchSamplesBySampleListIds(sampleListIds, "SUMMARY");

    Assertions.assertEquals(14, result.size());
    Assertions.assertEquals("TCGA-A1-A0SB-01", result.get(0).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SD-01", result.get(1).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SE-01", result.get(2).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SF-01", result.get(3).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SG-01", result.get(4).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SH-01", result.get(5).getStableId());
  }

  @Test
  public void fetchMetaSamples() throws Exception {

    List<String> studyIds = new ArrayList<>();
    studyIds.add("study_tcga_pub");
    studyIds.add("study_tcga_pub");
    List<String> sampleIds = new ArrayList<>();
    sampleIds.add("TCGA-A1-A0SB-01");
    sampleIds.add("TCGA-A1-A0SE-01");

    BaseMeta result = sampleMyBatisRepository.fetchMetaSamples(studyIds, sampleIds);

    Assertions.assertEquals((Integer) 2, result.getTotalCount());
  }

  @Test
  public void fetchMetaSamplesBySampleListId() throws Exception {

    List<String> sampleListIds = new ArrayList<>();
    sampleListIds.add("study_tcga_pub_all");
    sampleListIds.add("study_tcga_pub_acgh");

    BaseMeta result = sampleMyBatisRepository.fetchMetaSamples(sampleListIds);

    Assertions.assertEquals((Integer) 14, result.getTotalCount());
  }

  @Test
  public void getSamplesByInternalIds() throws Exception {

    List<Sample> result = sampleMyBatisRepository.getSamplesByInternalIds(Arrays.asList(1, 2));

    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("TCGA-A1-A0SB-01", result.get(0).getStableId());
    Assertions.assertEquals("TCGA-A1-A0SD-01", result.get(1).getStableId());
  }

  @Test
  public void getSamplesByKeyword() {
    List<Sample> result =
        sampleMyBatisRepository.getAllSamples("TCGA-A1-A0SB", null, "SUMMARY", 10, 0, null, null);
    List<String> actual = result.stream().map((Sample::getStableId)).collect(Collectors.toList());
    List<String> expected = Arrays.asList("TCGA-A1-A0SB-01", "TCGA-A1-A0SB-01", "TCGA-A1-A0SB-02");

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void getSamplesByEmptyKeyword() {
    List<Sample> result =
        sampleMyBatisRepository.getAllSamples(" ", null, "SUMMARY", 10, 0, null, null);
    List<String> actual = result.stream().map((Sample::getStableId)).collect(Collectors.toList());
    List<String> expected =
        Arrays.asList(
            "TCGA-A1-A0SB-01",
            "TCGA-A1-A0SB-01",
            "TCGA-A1-A0SB-02",
            "TCGA-A1-A0SD-01",
            "TCGA-A1-A0SE-01",
            "TCGA-A1-A0SF-01",
            "TCGA-A1-A0SG-01",
            "TCGA-A1-A0SH-01",
            "TCGA-A1-A0SI-01",
            "TCGA-A1-A0SJ-01");

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void getSamplesByKeywordFilterByStudies() {
    List<Sample> result =
        sampleMyBatisRepository.getAllSamples(
            "TCGA-A1", Collections.singletonList("acc_tcga"), "SUMMARY", 10, 0, null, null);
    List<String> actual = result.stream().map(Sample::getStableId).collect(Collectors.toList());
    List<String> expected =
        Arrays.asList("TCGA-A1-A0SB-01", "TCGA-A1-B0SO-01", "TCGA-A1-B0SP-01", "TCGA-A1-B0SQ-01");

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void getSamplesByKeywordFilterByNoStudies() {
    List<Sample> result =
        sampleMyBatisRepository.getAllSamples(
            "TCGA-A1", new ArrayList<>(), "SUMMARY", 10, 0, null, null);
    List<String> actual = result.stream().map(Sample::getStableId).collect(Collectors.toList());
    List<String> expected = Collections.emptyList();

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void getMetaSamplesByKeyword() {
    BaseMeta actual = sampleMyBatisRepository.getMetaSamples("TCGA-A1-A0SB", null);
    Integer expected = 3;

    Assertions.assertEquals(expected, actual.getTotalCount());
  }
}
