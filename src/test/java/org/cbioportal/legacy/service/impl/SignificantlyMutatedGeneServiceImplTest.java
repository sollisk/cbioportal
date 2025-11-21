package org.cbioportal.legacy.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.cbioportal.legacy.model.MutSig;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.SignificantlyMutatedGeneRepository;
import org.cbioportal.legacy.service.StudyService;
import org.cbioportal.legacy.service.exception.StudyNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SignificantlyMutatedGeneServiceImplTest extends BaseServiceImplTest {

  @InjectMocks private SignificantlyMutatedGeneServiceImpl significantlyMutatedGeneService;

  @Mock private SignificantlyMutatedGeneRepository significantlyMutatedGeneRepository;
  @Mock private StudyService studyService;

  @Test
  public void getSignificantlyMutatedGenes() throws Exception {

    List<MutSig> expectedMutSigList = new ArrayList<>();
    MutSig mutSig = new MutSig();
    expectedMutSigList.add(mutSig);

    Mockito.when(
            significantlyMutatedGeneRepository.getSignificantlyMutatedGenes(
                STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION))
        .thenReturn(expectedMutSigList);

    List<MutSig> result =
        significantlyMutatedGeneService.getSignificantlyMutatedGenes(
            STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION);

    Assertions.assertEquals(expectedMutSigList, result);
  }

  @Test
  public void getSignificantlyMutatedGenesStudyNotFound() throws Exception {

    Mockito.when(studyService.getStudy(STUDY_ID)).thenThrow(new StudyNotFoundException(STUDY_ID));
    Assertions.assertThrows(StudyNotFoundException.class, () -> 
        significantlyMutatedGeneService.getSignificantlyMutatedGenes(
            STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION));
  }

  @Test
  public void getMetaSignificantlyMutatedGenes() throws Exception {

    BaseMeta expectedBaseMeta = new BaseMeta();
    Mockito.when(significantlyMutatedGeneRepository.getMetaSignificantlyMutatedGenes(STUDY_ID))
        .thenReturn(expectedBaseMeta);
    BaseMeta result = significantlyMutatedGeneService.getMetaSignificantlyMutatedGenes(STUDY_ID);

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getMetaSignificantlyMutatedGenesStudyNotFound() throws Exception {

    Mockito.when(studyService.getStudy(STUDY_ID)).thenThrow(new StudyNotFoundException(STUDY_ID));
    Assertions.assertThrows(StudyNotFoundException.class, () -> 
        significantlyMutatedGeneService.getMetaSignificantlyMutatedGenes(STUDY_ID));
  }
}
