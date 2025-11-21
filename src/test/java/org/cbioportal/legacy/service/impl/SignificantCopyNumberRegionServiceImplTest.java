package org.cbioportal.legacy.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cbioportal.legacy.model.Gistic;
import org.cbioportal.legacy.model.GisticToGene;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.SignificantCopyNumberRegionRepository;
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
public class SignificantCopyNumberRegionServiceImplTest extends BaseServiceImplTest {

  @InjectMocks private SignificantCopyNumberRegionServiceImpl significantCopyNumberRegionService;

  @Mock private SignificantCopyNumberRegionRepository significantCopyNumberRegionRepository;
  @Mock private StudyService studyService;

  @Test
  public void getSignificantCopyNumberRegions() throws Exception {

    List<Gistic> expectedGisticList = new ArrayList<>();
    Gistic gistic = new Gistic();
    gistic.setGisticRoiId(GISTIC_ROI_ID);
    expectedGisticList.add(gistic);

    Mockito.when(
            significantCopyNumberRegionRepository.getSignificantCopyNumberRegions(
                STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION))
        .thenReturn(expectedGisticList);

    List<GisticToGene> expectedGisticToGeneList = new ArrayList<>();
    GisticToGene gisticToGene = new GisticToGene();
    gisticToGene.setGisticRoiId(GISTIC_ROI_ID);
    expectedGisticToGeneList.add(gisticToGene);

    Mockito.when(
            significantCopyNumberRegionRepository.getGenesOfRegions(Arrays.asList(GISTIC_ROI_ID)))
        .thenReturn(expectedGisticToGeneList);

    List<Gistic> result =
        significantCopyNumberRegionService.getSignificantCopyNumberRegions(
            STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION);

    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(gistic, result.get(0));
    Assertions.assertEquals(1, result.get(0).getGenes().size());
    Assertions.assertEquals(gisticToGene, result.get(0).getGenes().get(0));
  }

  @Test
  public void getSignificantCopyNumberRegionsStudyNotFound() throws Exception {

    Mockito.when(studyService.getStudy(STUDY_ID)).thenThrow(new StudyNotFoundException(STUDY_ID));
    
    Assertions.assertThrows(StudyNotFoundException.class, () ->
        significantCopyNumberRegionService.getSignificantCopyNumberRegions(
            STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION));
  }

  @Test
  public void getMetaSignificantCopyNumberRegions() throws Exception {

    BaseMeta expectedBaseMeta = new BaseMeta();
    Mockito.when(
            significantCopyNumberRegionRepository.getMetaSignificantCopyNumberRegions(STUDY_ID))
        .thenReturn(expectedBaseMeta);
    BaseMeta result =
        significantCopyNumberRegionService.getMetaSignificantCopyNumberRegions(STUDY_ID);

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getMetaSignificantCopyNumberRegionsStudyNotFound() throws Exception {

    Mockito.when(studyService.getStudy(STUDY_ID)).thenThrow(new StudyNotFoundException(STUDY_ID));
    Assertions.assertThrows(StudyNotFoundException.class, () -> 
        significantCopyNumberRegionService.getMetaSignificantCopyNumberRegions(STUDY_ID));
  }
}
