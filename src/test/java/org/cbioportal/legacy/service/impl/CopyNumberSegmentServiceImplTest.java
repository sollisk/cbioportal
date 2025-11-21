package org.cbioportal.legacy.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cbioportal.legacy.model.CopyNumberSeg;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.CopyNumberSegmentRepository;
import org.cbioportal.legacy.service.SampleService;
import org.cbioportal.legacy.service.exception.SampleNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

@ExtendWith(MockitoExtension.class)
public class CopyNumberSegmentServiceImplTest extends BaseServiceImplTest {

  @InjectMocks private CopyNumberSegmentServiceImpl copyNumberSegmentService;

  @Mock private CopyNumberSegmentRepository copyNumberSegmentRepository;
  @Mock private SampleService sampleService;

  @Test
  public void getCopyNumberSegmentsInSampleInStudy() throws Exception {

    List<CopyNumberSeg> expectedCopyNumberSegList = new ArrayList<>();
    CopyNumberSeg copyNumberSeg = new CopyNumberSeg();
    expectedCopyNumberSegList.add(copyNumberSeg);

    Mockito.when(
            copyNumberSegmentRepository.getCopyNumberSegmentsInSampleInStudy(
                STUDY_ID, SAMPLE_ID1, null, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION))
        .thenReturn(expectedCopyNumberSegList);

    List<CopyNumberSeg> result =
        copyNumberSegmentService.getCopyNumberSegmentsInSampleInStudy(
            STUDY_ID, SAMPLE_ID1, null, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION);

    Assertions.assertEquals(expectedCopyNumberSegList, result);
  }

  @Test
  public void getCopyNumberSegmentsInSampleInStudySampleNotFound() throws Exception {

    Mockito.when(sampleService.getSampleInStudy(STUDY_ID, SAMPLE_ID1))
        .thenThrow(new SampleNotFoundException(STUDY_ID, SAMPLE_ID1));
    Assertions.assertThrows(SampleNotFoundException.class, () -> 
        copyNumberSegmentService.getCopyNumberSegmentsInSampleInStudy(
            STUDY_ID, SAMPLE_ID1, null, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION));
  }

  @Test
  public void getMetaCopyNumberSegmentsInSampleInStudy() throws Exception {

    BaseMeta expectedBaseMeta = new BaseMeta();

    Mockito.when(
            copyNumberSegmentRepository.getMetaCopyNumberSegmentsInSampleInStudy(
                STUDY_ID, SAMPLE_ID1, null))
        .thenReturn(expectedBaseMeta);

    BaseMeta result =
        copyNumberSegmentService.getMetaCopyNumberSegmentsInSampleInStudy(
            STUDY_ID, SAMPLE_ID1, null);

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getMetaCopyNumberSegmentsInSampleInStudySampleNotFound() throws Exception {

    Mockito.when(sampleService.getSampleInStudy(STUDY_ID, SAMPLE_ID1))
        .thenThrow(new SampleNotFoundException(STUDY_ID, SAMPLE_ID1));
    Assertions.assertThrows(SampleNotFoundException.class, () -> 
        copyNumberSegmentService.getMetaCopyNumberSegmentsInSampleInStudy(STUDY_ID, SAMPLE_ID1, null));
  }

  @Test
  public void fetchCopyNumberSegments() {

    List<CopyNumberSeg> expectedCopyNumberSegList = new ArrayList<>();
    CopyNumberSeg copyNumberSeg = new CopyNumberSeg();
    expectedCopyNumberSegList.add(copyNumberSeg);

      OngoingStubbing<List<CopyNumberSeg>> listOngoingStubbing = Mockito.when(
              copyNumberSegmentRepository.fetchCopyNumberSegments(
                  List.of(STUDY_ID), List.of(PATIENT_ID_1), null, PROJECTION))
          .thenReturn(expectedCopyNumberSegList);

      List<CopyNumberSeg> result =
        copyNumberSegmentService.fetchCopyNumberSegments(
            List.of(STUDY_ID), List.of(PATIENT_ID_1), null, PROJECTION);

    Assertions.assertEquals(expectedCopyNumberSegList, result);
  }

  @Test
  public void fetchMetaCopyNumberSegments() {

    BaseMeta expectedBaseMeta = new BaseMeta();
    Mockito.when(
            copyNumberSegmentRepository.fetchMetaCopyNumberSegments(
                List.of(STUDY_ID), List.of(PATIENT_ID_1), null))
        .thenReturn(expectedBaseMeta);
    BaseMeta result =
        copyNumberSegmentService.fetchMetaCopyNumberSegments(
            List.of(STUDY_ID), List.of(PATIENT_ID_1), null);

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getCopyNumberSegmentsBySampleListId()  {

    List<CopyNumberSeg> expectedCopyNumberSegList = new ArrayList<>();
    CopyNumberSeg copyNumberSeg = new CopyNumberSeg();
    expectedCopyNumberSegList.add(copyNumberSeg);

    Mockito.when(
            copyNumberSegmentRepository.getCopyNumberSegmentsBySampleListId(
                STUDY_ID, SAMPLE_LIST_ID, null, PROJECTION))
        .thenReturn(expectedCopyNumberSegList);

    List<CopyNumberSeg> result =
        copyNumberSegmentService.getCopyNumberSegmentsBySampleListId(
            STUDY_ID, SAMPLE_LIST_ID, null, PROJECTION);

    Assertions.assertEquals(expectedCopyNumberSegList, result);
  }
}
