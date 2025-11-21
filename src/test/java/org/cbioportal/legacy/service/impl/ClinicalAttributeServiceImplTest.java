package org.cbioportal.legacy.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.cbioportal.legacy.model.ClinicalAttribute;
import org.cbioportal.legacy.model.ClinicalAttributeCount;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.ClinicalAttributeRepository;
import org.cbioportal.legacy.service.StudyService;
import org.cbioportal.legacy.service.exception.ClinicalAttributeNotFoundException;
import org.cbioportal.legacy.service.exception.StudyNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ClinicalAttributeServiceImplTest extends BaseServiceImplTest {

  @InjectMocks private ClinicalAttributeServiceImpl clinicalAttributeService;

  @Mock private ClinicalAttributeRepository clinicalAttributeRepository;
  @Mock private StudyService studyService;

  @BeforeEach
  public void setup() {
    ReflectionTestUtils.setField(clinicalAttributeService, "AUTHENTICATE", "false");
  }

  @Test
  public void getAllClinicalAttributes() {

    List<ClinicalAttribute> expectedClinicalAttributeList = new ArrayList<>();
    ClinicalAttribute clinicalAttribute = new ClinicalAttribute();
    expectedClinicalAttributeList.add(clinicalAttribute);

    Mockito.when(
            clinicalAttributeRepository.getAllClinicalAttributes(
                PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION))
        .thenReturn(expectedClinicalAttributeList);

    List<ClinicalAttribute> result =
        clinicalAttributeService.getAllClinicalAttributes(
            PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION);

    Assertions.assertEquals(expectedClinicalAttributeList, result);
  }

  @Test
  public void getMetaClinicalAttributes() {

    BaseMeta expectedBaseMeta = new BaseMeta();
    Mockito.when(clinicalAttributeRepository.getMetaClinicalAttributes())
        .thenReturn(expectedBaseMeta);
    BaseMeta result = clinicalAttributeService.getMetaClinicalAttributes();

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getClinicalAttributeNotFound() {

    Mockito.when(
            clinicalAttributeRepository.getClinicalAttribute(STUDY_ID, CLINICAL_ATTRIBUTE_ID_1))
        .thenReturn(null);
    Assertions.assertThrows(ClinicalAttributeNotFoundException.class, () -> 
        clinicalAttributeService.getClinicalAttribute(STUDY_ID, CLINICAL_ATTRIBUTE_ID_1));
  }

  @Test
  public void getClinicalAttributeStudyNotFound() throws Exception {

    Mockito.when(studyService.getStudy(STUDY_ID)).thenThrow(new StudyNotFoundException(STUDY_ID));
    Assertions.assertThrows(StudyNotFoundException.class, () -> 
        clinicalAttributeService.getClinicalAttribute(STUDY_ID, CLINICAL_ATTRIBUTE_ID_1));
  }

  @Test
  public void getClinicalAttribute() throws Exception {

    ClinicalAttribute expectedClinicalAttribute = new ClinicalAttribute();

    Mockito.when(
            clinicalAttributeRepository.getClinicalAttribute(STUDY_ID, CLINICAL_ATTRIBUTE_ID_1))
        .thenReturn(expectedClinicalAttribute);

    ClinicalAttribute result =
        clinicalAttributeService.getClinicalAttribute(STUDY_ID, CLINICAL_ATTRIBUTE_ID_1);

    Assertions.assertEquals(expectedClinicalAttribute, result);
  }

  @Test
  public void getAllClinicalAttributesInStudy() throws Exception {

    List<ClinicalAttribute> expectedClinicalAttributeList = new ArrayList<>();
    ClinicalAttribute clinicalAttribute = new ClinicalAttribute();
    expectedClinicalAttributeList.add(clinicalAttribute);

    Mockito.when(
            clinicalAttributeRepository.getAllClinicalAttributesInStudy(
                STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION))
        .thenReturn(expectedClinicalAttributeList);

    List<ClinicalAttribute> result =
        clinicalAttributeService.getAllClinicalAttributesInStudy(
            STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION);

    Assertions.assertEquals(expectedClinicalAttributeList, result);
  }

  @Test
  public void getAllClinicalAttributesInStudyNotFound() throws Exception {

    Mockito.when(studyService.getStudy(STUDY_ID)).thenThrow(new StudyNotFoundException(STUDY_ID));
    Assertions.assertThrows(StudyNotFoundException.class, () -> 
        clinicalAttributeService.getAllClinicalAttributesInStudy(
            STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION));
  }

  @Test
  public void getMetaClinicalAttributesInStudy() throws Exception {

    BaseMeta expectedBaseMeta = new BaseMeta();
    Mockito.when(clinicalAttributeRepository.getMetaClinicalAttributesInStudy(STUDY_ID))
        .thenReturn(expectedBaseMeta);
    BaseMeta result = clinicalAttributeService.getMetaClinicalAttributesInStudy(STUDY_ID);

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getMetaClinicalAttributesInStudyNotFound() throws Exception {

    Mockito.when(studyService.getStudy(STUDY_ID)).thenThrow(new StudyNotFoundException(STUDY_ID));
    Assertions.assertThrows(StudyNotFoundException.class, () -> 
        clinicalAttributeService.getMetaClinicalAttributesInStudy(STUDY_ID));
  }

  @Test
  public void fetchClinicalAttributes() {

    List<ClinicalAttribute> expectedClinicalAttributeList = new ArrayList<>();
    ClinicalAttribute clinicalAttribute = new ClinicalAttribute();
    expectedClinicalAttributeList.add(clinicalAttribute);

    Mockito.when(
            clinicalAttributeRepository.fetchClinicalAttributes(
                List.of(STUDY_ID), PROJECTION))
        .thenReturn(expectedClinicalAttributeList);

    List<ClinicalAttribute> result =
        clinicalAttributeService.fetchClinicalAttributes(List.of(STUDY_ID), PROJECTION);

    Assertions.assertEquals(expectedClinicalAttributeList, result);
  }

  @Test
  public void fetchMetaClinicalAttributes() {

    BaseMeta expectedBaseMeta = new BaseMeta();
    Mockito.when(clinicalAttributeRepository.fetchMetaClinicalAttributes(List.of(STUDY_ID)))
        .thenReturn(expectedBaseMeta);
    BaseMeta result = clinicalAttributeService.fetchMetaClinicalAttributes(List.of(STUDY_ID));

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getClinicalAttributeCountsBySampleIds() {

    List<String> sampleIds = new ArrayList<>();
    List<String> studyIds = new ArrayList<>();
    sampleIds.add(SAMPLE_ID1);
    studyIds.add(STUDY_ID);

    List<ClinicalAttributeCount> expectedClinicalAttributeCounts = new ArrayList<>();
    ClinicalAttributeCount clinicalAttributeCount = new ClinicalAttributeCount();
    expectedClinicalAttributeCounts.add(clinicalAttributeCount);

    Mockito.when(
            clinicalAttributeRepository.getClinicalAttributeCountsBySampleIds(sampleIds, studyIds))
        .thenReturn(expectedClinicalAttributeCounts);

    List<ClinicalAttributeCount> result =
        clinicalAttributeService.getClinicalAttributeCountsBySampleIds(sampleIds, studyIds);

    Assertions.assertEquals(expectedClinicalAttributeCounts, result);
  }

  @Test
  public void getClinicalAttributeCountsBySampleListId() {

    List<ClinicalAttributeCount> expectedClinicalAttributeList = new ArrayList<>();
    ClinicalAttributeCount clinicalAttributeCount = new ClinicalAttributeCount();
    expectedClinicalAttributeList.add(clinicalAttributeCount);

    Mockito.when(
            clinicalAttributeRepository.getClinicalAttributeCountsBySampleListId(SAMPLE_LIST_ID))
        .thenReturn(expectedClinicalAttributeList);

    List<ClinicalAttributeCount> result =
        clinicalAttributeService.getClinicalAttributeCountsBySampleListId(SAMPLE_LIST_ID);

    Assertions.assertEquals(expectedClinicalAttributeList, result);
  }
}
