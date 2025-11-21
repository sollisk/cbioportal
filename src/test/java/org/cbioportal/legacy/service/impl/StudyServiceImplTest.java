package org.cbioportal.legacy.service.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.cbioportal.legacy.model.CancerStudy;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.StudyRepository;
import org.cbioportal.legacy.service.CancerTypeService;
import org.cbioportal.legacy.service.ReadPermissionService;
import org.cbioportal.legacy.service.exception.StudyNotFoundException;
import org.cbioportal.legacy.utils.security.AccessLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StudyServiceImplTest extends BaseServiceImplTest {

  @InjectMocks private StudyServiceImpl studyService;

  @Mock private ReadPermissionService readPermissionService;

  @Mock private StudyRepository studyRepository;
  @Mock private CancerTypeService cancerTypeService;

  @Test
  public void getAllStudies() {

    List<CancerStudy> expectedCancerStudyList = new ArrayList<>();
    CancerStudy cancerStudy = new CancerStudy();
    cancerStudy.setReadPermission(false);
    expectedCancerStudyList.add(cancerStudy);

    when(studyRepository.getAllStudies(
            KEYWORD, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION))
        .thenReturn(expectedCancerStudyList);
    when(cancerTypeService.getPrimarySiteMap()).thenReturn(new HashMap<>());

    List<CancerStudy> result =
        studyService.getAllStudies(
            KEYWORD, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION, null, AccessLevel.READ);

    Assertions.assertEquals(expectedCancerStudyList.getFirst(), result.getFirst());
  }

  @Test
  public void getMetaStudies() {

    BaseMeta expectedBaseMeta = new BaseMeta();
    when(studyRepository.getMetaStudies(null)).thenReturn(expectedBaseMeta);

    BaseMeta result = studyService.getMetaStudies(null);

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getStudyNotFound() {

    when(studyRepository.getStudy(STUDY_ID, "DETAILED")).thenReturn(null);

    Assertions.assertThrows(StudyNotFoundException.class, () -> studyService.getStudy(STUDY_ID));
  }

  @Test
  public void getStudy() throws Exception {

    CancerStudy expectedCancerStudy = new CancerStudy();

    when(studyRepository.getStudy(STUDY_ID, "DETAILED")).thenReturn(expectedCancerStudy);

    CancerStudy result = studyService.getStudy(STUDY_ID);

    Assertions.assertEquals(expectedCancerStudy, result);
  }

  @Test
  public void fetchStudies() {

    List<CancerStudy> expectedCancerStudyList = new ArrayList<>();
    CancerStudy cancerStudy = new CancerStudy();
    expectedCancerStudyList.add(cancerStudy);

    when(studyRepository.fetchStudies(List.of(STUDY_ID), PROJECTION))
        .thenReturn(expectedCancerStudyList);

    List<CancerStudy> result = studyService.fetchStudies(List.of(STUDY_ID), PROJECTION);

    Assertions.assertEquals(expectedCancerStudyList, result);
  }

  @Test
  public void fetchMetaStudies() {

    BaseMeta expectedBaseMeta = new BaseMeta();

    when(studyRepository.fetchMetaStudies(List.of(STUDY_ID))).thenReturn(expectedBaseMeta);

    BaseMeta result = studyService.fetchMetaStudies(List.of(STUDY_ID));

    Assertions.assertEquals(expectedBaseMeta, result);
  }
}
