package org.cbioportal.legacy.persistence.mybatis;

import java.util.Arrays;
import java.util.List;
import org.cbioportal.legacy.model.CancerStudy;
import org.cbioportal.legacy.model.SampleList;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.mybatis.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {SampleListMyBatisRepository.class, TestConfig.class})
public class SampleListMyBatisRepositoryTest {

  @Autowired private SampleListMyBatisRepository sampleListMyBatisRepository;

  @Test
  public void getAllSampleListsIdProjection() throws Exception {

    List<SampleList> result =
        sampleListMyBatisRepository.getAllSampleLists("ID", null, null, null, null);

    Assertions.assertEquals(14, result.size());
    SampleList sampleList = result.get(0);
    Assertions.assertEquals((Integer) 14, sampleList.getListId());
    Assertions.assertEquals("acc_tcga_all", sampleList.getStableId());
    Assertions.assertNull(sampleList.getCancerStudy());
  }

  @Test
  public void getAllSampleListsSummaryProjection() throws Exception {

    List<SampleList> result =
        sampleListMyBatisRepository.getAllSampleLists("SUMMARY", null, null, null, null);

    Assertions.assertEquals(14, result.size());
    SampleList sampleList = result.get(0);
    Assertions.assertEquals((Integer) 1, sampleList.getListId());
    Assertions.assertEquals("study_tcga_pub_all", sampleList.getStableId());
    Assertions.assertEquals((Integer) 1, sampleList.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", sampleList.getCancerStudyIdentifier());
    Assertions.assertEquals("other", sampleList.getCategory());
    Assertions.assertEquals("All Tumors", sampleList.getName());
    Assertions.assertEquals("All tumor samples", sampleList.getDescription());
    Assertions.assertNull(sampleList.getCancerStudy());
  }

  @Test
  public void getAllSampleListsDetailedProjection() throws Exception {

    List<SampleList> result =
        sampleListMyBatisRepository.getAllSampleLists("DETAILED", null, null, null, null);

    Assertions.assertEquals(14, result.size());
    SampleList sampleList = result.get(0);
    Assertions.assertEquals((Integer) 1, sampleList.getListId());
    Assertions.assertEquals("study_tcga_pub_all", sampleList.getStableId());
    Assertions.assertEquals((Integer) 1, sampleList.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", sampleList.getCancerStudyIdentifier());
    Assertions.assertEquals("other", sampleList.getCategory());
    Assertions.assertEquals("All Tumors", sampleList.getName());
    Assertions.assertEquals("All tumor samples", sampleList.getDescription());
    CancerStudy cancerStudy = sampleList.getCancerStudy();
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
  public void getAllSampleListsSummaryProjection1PageSize() throws Exception {

    List<SampleList> result =
        sampleListMyBatisRepository.getAllSampleLists("SUMMARY", 1, 0, null, null);

    Assertions.assertEquals(1, result.size());
  }

  @Test
  public void getAllSampleListsSummaryProjectionStableIdSort() throws Exception {

    List<SampleList> result =
        sampleListMyBatisRepository.getAllSampleLists("SUMMARY", null, null, "stableId", "ASC");

    Assertions.assertEquals(14, result.size());
    Assertions.assertEquals("acc_tcga_all", result.get(0).getStableId());
    Assertions.assertEquals("study_tcga_pub_3way_complete", result.get(1).getStableId());
    Assertions.assertEquals("study_tcga_pub_acgh", result.get(2).getStableId());
    Assertions.assertEquals("study_tcga_pub_all", result.get(3).getStableId());
    Assertions.assertEquals("study_tcga_pub_cna", result.get(4).getStableId());
    Assertions.assertEquals("study_tcga_pub_cnaseq", result.get(5).getStableId());
  }

  @Test
  public void getMetaSampleLists() throws Exception {

    BaseMeta result = sampleListMyBatisRepository.getMetaSampleLists();

    Assertions.assertEquals((Integer) 14, result.getTotalCount());
  }

  @Test
  public void getSampleListNullResult() throws Exception {

    SampleList result = sampleListMyBatisRepository.getSampleList("invalid_sample_list");

    Assertions.assertNull(result);
  }

  @Test
  public void getSampleList() throws Exception {

    SampleList sampleList = sampleListMyBatisRepository.getSampleList("study_tcga_pub_all");

    Assertions.assertEquals((Integer) 1, sampleList.getListId());
    Assertions.assertEquals("study_tcga_pub_all", sampleList.getStableId());
    Assertions.assertEquals((Integer) 1, sampleList.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", sampleList.getCancerStudyIdentifier());
    Assertions.assertEquals("other", sampleList.getCategory());
    Assertions.assertEquals("All Tumors", sampleList.getName());
    Assertions.assertEquals("All tumor samples", sampleList.getDescription());
    CancerStudy cancerStudy = sampleList.getCancerStudy();
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
  public void getSampleLists() throws Exception {

    List<SampleList> result =
        sampleListMyBatisRepository.getSampleLists(
            Arrays.asList("study_tcga_pub_all", "study_tcga_pub_acgh"), "SUMMARY");

    Assertions.assertEquals(2, result.size());
    SampleList sampleList = result.get(0);
    Assertions.assertEquals((Integer) 2, sampleList.getListId());
    Assertions.assertEquals("study_tcga_pub_acgh", sampleList.getStableId());
    Assertions.assertEquals((Integer) 1, sampleList.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", sampleList.getCancerStudyIdentifier());
    Assertions.assertEquals("other", sampleList.getCategory());
    Assertions.assertEquals("Tumors aCGH", sampleList.getName());
    Assertions.assertEquals("All tumors with aCGH data", sampleList.getDescription());
    Assertions.assertNull(sampleList.getCancerStudy());
  }

  @Test
  public void getAllSampleListsInStudySummaryProjection() throws Exception {

    List<SampleList> result =
        sampleListMyBatisRepository.getAllSampleListsInStudies(
            Arrays.asList("study_tcga_pub"), "SUMMARY", null, null, null, null);

    Assertions.assertEquals(13, result.size());
    SampleList sampleList = result.get(0);
    Assertions.assertEquals((Integer) 1, sampleList.getListId());
    Assertions.assertEquals("study_tcga_pub_all", sampleList.getStableId());
    Assertions.assertEquals((Integer) 1, sampleList.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", sampleList.getCancerStudyIdentifier());
    Assertions.assertEquals("other", sampleList.getCategory());
    Assertions.assertEquals("All Tumors", sampleList.getName());
    Assertions.assertEquals("All tumor samples", sampleList.getDescription());
    Assertions.assertNull(sampleList.getCancerStudy());
  }

  @Test
  public void getAllSampleListsInStudyDetailedProjection() throws Exception {

    List<SampleList> result =
        sampleListMyBatisRepository.getAllSampleListsInStudies(
            Arrays.asList("study_tcga_pub"), "DETAILED", null, null, null, null);

    Assertions.assertEquals(13, result.size());
    SampleList sampleList = result.get(0);
    Assertions.assertEquals((Integer) 1, sampleList.getListId());
    Assertions.assertEquals("study_tcga_pub_all", sampleList.getStableId());
    Assertions.assertEquals((Integer) 1, sampleList.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", sampleList.getCancerStudyIdentifier());
    Assertions.assertEquals("other", sampleList.getCategory());
    Assertions.assertEquals("All Tumors", sampleList.getName());
    Assertions.assertEquals("All tumor samples", sampleList.getDescription());
    CancerStudy cancerStudy = sampleList.getCancerStudy();
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
  public void getMetaSampleListsInStudy() throws Exception {

    BaseMeta result = sampleListMyBatisRepository.getMetaSampleListsInStudy("study_tcga_pub");

    Assertions.assertEquals((Integer) 13, result.getTotalCount());
  }

  @Test
  public void getAllSampleIdsInSampleList() throws Exception {

    List<String> result =
        sampleListMyBatisRepository.getAllSampleIdsInSampleList("study_tcga_pub_all");

    Assertions.assertEquals(14, result.size());
    Assertions.assertEquals("TCGA-A1-A0SB-01", result.get(0));
    Assertions.assertEquals("TCGA-A1-A0SD-01", result.get(1));
    Assertions.assertEquals("TCGA-A1-A0SE-01", result.get(2));
    Assertions.assertEquals("TCGA-A1-A0SF-01", result.get(3));
    Assertions.assertEquals("TCGA-A1-A0SG-01", result.get(4));
    Assertions.assertEquals("TCGA-A1-A0SQ-01", result.get(13));
  }
}
