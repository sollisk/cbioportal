package org.cbioportal.legacy.service.impl;

import static org.mockito.Mockito.when;

import java.util.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.cbioportal.legacy.model.ClinicalAttribute;
import org.cbioportal.legacy.model.ClinicalData;
import org.cbioportal.legacy.model.ClinicalDataCount;
import org.cbioportal.legacy.model.ClinicalDataCountItem;
import org.cbioportal.legacy.model.Patient;
import org.cbioportal.legacy.model.SampleClinicalDataCollection;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.ClinicalDataRepository;
import org.cbioportal.legacy.service.ClinicalAttributeService;
import org.cbioportal.legacy.service.PatientService;
import org.cbioportal.legacy.service.SampleService;
import org.cbioportal.legacy.service.StudyService;
import org.cbioportal.legacy.service.exception.PatientNotFoundException;
import org.cbioportal.legacy.service.exception.SampleNotFoundException;
import org.cbioportal.legacy.service.exception.StudyNotFoundException;
import org.cbioportal.legacy.utils.Encoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClinicalDataServiceImplTest extends BaseServiceImplTest {

  @InjectMocks private ClinicalDataServiceImpl clinicalDataService;

  @Mock private ClinicalDataRepository clinicalDataRepository;
  @Mock private StudyService studyService;
  @Mock private PatientService patientService;
  @Mock private SampleService sampleService;
  @Mock private ClinicalAttributeService clinicalAttributeService;

  ClinicalData datum1 = new ClinicalData();
  ClinicalData datum2 = new ClinicalData();
  String uniqueKeySample1;
  String uniqueKeySample2;
  List<String> sampleStudyIds = Arrays.asList(STUDY_ID, STUDY_ID, STUDY_ID);
  List<String> sampleIds = Arrays.asList(SAMPLE_ID1, SAMPLE_ID2, SAMPLE_ID3);

  Integer pageSize = 2;
  Integer pageNumber = 0;
  String searchTerm = "mySearch";
  String sortBy = "column name";
  String direction = "ASC";
  List<Integer> sampleInternalIds = Arrays.asList(0, 1);
  List<Integer> sampleInternalIdsAll = Arrays.asList(0, 1, 2, 3);

  @BeforeEach
  public void init() {

    datum1.setSampleId("SampleA");
    datum1.setStudyId("Study1");
    uniqueKeySample1 = Encoder.calculateBase64("SampleA", "Study1");

    datum2.setSampleId("SampleA");
    datum2.setStudyId("Study2");
    uniqueKeySample2 = Encoder.calculateBase64("SampleA", "Study2");
  }

  @Test
  public void getAllClinicalDataOfSampleInStudy() throws Exception {

    List<ClinicalData> expectedSampleClinicalDataList = new ArrayList<>();
    ClinicalData sampleClinicalData = new ClinicalData();
    expectedSampleClinicalDataList.add(sampleClinicalData);

    when(clinicalDataRepository.getAllClinicalDataOfSampleInStudy(
            STUDY_ID,
            SAMPLE_ID1,
            CLINICAL_ATTRIBUTE_ID_1,
            PROJECTION,
            PAGE_SIZE,
            PAGE_NUMBER,
            SORT,
            DIRECTION))
        .thenReturn(expectedSampleClinicalDataList);

    List<ClinicalData> result =
        clinicalDataService.getAllClinicalDataOfSampleInStudy(
            STUDY_ID,
            SAMPLE_ID1,
            CLINICAL_ATTRIBUTE_ID_1,
            PROJECTION,
            PAGE_SIZE,
            PAGE_NUMBER,
            SORT,
            DIRECTION);

    Assertions.assertEquals(expectedSampleClinicalDataList, result);
  }

  @Test
  public void getAllClinicalDataOfSampleInStudySampleNotFound() throws Exception {

    when(sampleService.getSampleInStudy(STUDY_ID, SAMPLE_ID1))
        .thenThrow(new SampleNotFoundException(STUDY_ID, SAMPLE_ID1));
    Assertions.assertThrows(SampleNotFoundException.class, () -> 
        clinicalDataService.getAllClinicalDataOfSampleInStudy(
        STUDY_ID,
        SAMPLE_ID1,
        CLINICAL_ATTRIBUTE_ID_1,
        PROJECTION,
        PAGE_SIZE,
        PAGE_NUMBER,
        SORT,
        DIRECTION));
  }

  @Test
  public void getMetaSampleClinicalData() throws Exception {

    BaseMeta expectedBaseMeta = new BaseMeta();

    when(clinicalDataRepository.getMetaSampleClinicalData(
            STUDY_ID, SAMPLE_ID1, CLINICAL_ATTRIBUTE_ID_1))
        .thenReturn(expectedBaseMeta);

    BaseMeta result =
        clinicalDataService.getMetaSampleClinicalData(
            STUDY_ID, SAMPLE_ID1, CLINICAL_ATTRIBUTE_ID_1);

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getMetaSampleClinicalDataSampleNotFound() throws Exception {

    when(sampleService.getSampleInStudy(STUDY_ID, SAMPLE_ID1))
        .thenThrow(new SampleNotFoundException(STUDY_ID, SAMPLE_ID1));
    Assertions.assertThrows(SampleNotFoundException.class, () -> 
        clinicalDataService.getMetaSampleClinicalData(STUDY_ID, SAMPLE_ID1, CLINICAL_ATTRIBUTE_ID_1));
  }

  @Test
  public void getAllClinicalDataOfPatientInStudy() throws Exception {

    List<ClinicalData> expectedPatientClinicalDataList = new ArrayList<>();
    ClinicalData patientClinicalData = new ClinicalData();
    expectedPatientClinicalDataList.add(patientClinicalData);

    when(clinicalDataRepository.getAllClinicalDataOfPatientInStudy(
            STUDY_ID,
            PATIENT_ID_1,
            CLINICAL_ATTRIBUTE_ID_1,
            PROJECTION,
            PAGE_SIZE,
            PAGE_NUMBER,
            SORT,
            DIRECTION))
        .thenReturn(expectedPatientClinicalDataList);

    List<ClinicalData> result =
        clinicalDataService.getAllClinicalDataOfPatientInStudy(
            STUDY_ID,
            PATIENT_ID_1,
            CLINICAL_ATTRIBUTE_ID_1,
            PROJECTION,
            PAGE_SIZE,
            PAGE_NUMBER,
            SORT,
            DIRECTION);

    Assertions.assertEquals(expectedPatientClinicalDataList, result);
  }

  @Test
  public void getAllClinicalDataOfPatientInStudyPatientNotFound() throws Exception {

    when(patientService.getPatientInStudy(STUDY_ID, PATIENT_ID_1))
        .thenThrow(new PatientNotFoundException(STUDY_ID, PATIENT_ID_1));
    Assertions.assertThrows(PatientNotFoundException.class, () -> 
        clinicalDataService.getAllClinicalDataOfPatientInStudy(
        STUDY_ID,
        PATIENT_ID_1,
        CLINICAL_ATTRIBUTE_ID_1,
        PROJECTION,
        PAGE_SIZE,
        PAGE_NUMBER,
        SORT,
        DIRECTION));
  }

  @Test
  public void getMetaPatientClinicalData() throws Exception {

    BaseMeta expectedBaseMeta = new BaseMeta();

    when(clinicalDataRepository.getMetaPatientClinicalData(
            STUDY_ID, PATIENT_ID_1, CLINICAL_ATTRIBUTE_ID_1))
        .thenReturn(expectedBaseMeta);

    BaseMeta result =
        clinicalDataService.getMetaPatientClinicalData(
            STUDY_ID, PATIENT_ID_1, CLINICAL_ATTRIBUTE_ID_1);

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getMetaPatientClinicalDataPatientNotFound() throws Exception {

    when(patientService.getPatientInStudy(STUDY_ID, PATIENT_ID_1))
        .thenThrow(new PatientNotFoundException(STUDY_ID, PATIENT_ID_1));
    Assertions.assertThrows(PatientNotFoundException.class, () -> 
        clinicalDataService.getMetaPatientClinicalData(STUDY_ID, PATIENT_ID_1, CLINICAL_ATTRIBUTE_ID_1));
  }

  @Test
  public void getAllClinicalDataInStudy() throws Exception {

    List<ClinicalData> expectedSampleClinicalDataList = new ArrayList<>();
    ClinicalData sampleClinicalData = new ClinicalData();
    expectedSampleClinicalDataList.add(sampleClinicalData);

    when(clinicalDataRepository.getAllClinicalDataInStudy(
            STUDY_ID,
            CLINICAL_ATTRIBUTE_ID_1,
            CLINICAL_DATA_TYPE,
            PROJECTION,
            PAGE_SIZE,
            PAGE_NUMBER,
            SORT,
            DIRECTION))
        .thenReturn(expectedSampleClinicalDataList);

    List<ClinicalData> result =
        clinicalDataService.getAllClinicalDataInStudy(
            STUDY_ID,
            CLINICAL_ATTRIBUTE_ID_1,
            CLINICAL_DATA_TYPE,
            PROJECTION,
            PAGE_SIZE,
            PAGE_NUMBER,
            SORT,
            DIRECTION);

    Assertions.assertEquals(expectedSampleClinicalDataList, result);
  }

  @Test
  public void getAllClinicalDataInStudyNotFound() throws Exception {

    when(studyService.getStudy(STUDY_ID)).thenThrow(new StudyNotFoundException(STUDY_ID));
    Assertions.assertThrows(StudyNotFoundException.class, () -> 
        clinicalDataService.getAllClinicalDataInStudy(
        STUDY_ID,
        CLINICAL_ATTRIBUTE_ID_1,
        CLINICAL_DATA_TYPE,
        PROJECTION,
        PAGE_SIZE,
        PAGE_NUMBER,
        SORT,
        DIRECTION));
  }

  @Test
  public void getMetaAllClinicalData() throws Exception {

    BaseMeta expectedBaseMeta = new BaseMeta();
    expectedBaseMeta.setTotalCount(5);

    when(clinicalDataRepository.getMetaAllClinicalData(
            STUDY_ID, CLINICAL_ATTRIBUTE_ID_1, CLINICAL_DATA_TYPE))
        .thenReturn(expectedBaseMeta);

    BaseMeta result =
        clinicalDataService.getMetaAllClinicalData(
            STUDY_ID, CLINICAL_ATTRIBUTE_ID_1, CLINICAL_DATA_TYPE);

    Assertions.assertEquals((Integer) 5, result.getTotalCount());
  }

  @Test
  public void getMetaAllClinicalDataStudyNotFound() throws Exception {

    when(studyService.getStudy(STUDY_ID)).thenThrow(new StudyNotFoundException(STUDY_ID));
    Assertions.assertThrows(StudyNotFoundException.class, () -> 
        clinicalDataService.getMetaAllClinicalData(
        STUDY_ID, CLINICAL_ATTRIBUTE_ID_1, CLINICAL_DATA_TYPE));
  }

  @Test
  public void fetchClinicalDataPatientClinicalDataType() {

    List<String> studyIds = new ArrayList<>();
    studyIds.add(STUDY_ID);
    List<String> patientIds = new ArrayList<>();
    patientIds.add(PATIENT_ID_1);

    List<ClinicalData> expectedPatientClinicalDataList = new ArrayList<>();
    ClinicalData patientClinicalData = new ClinicalData();
    expectedPatientClinicalDataList.add(patientClinicalData);

    when(clinicalDataRepository.fetchClinicalData(
            studyIds,
            patientIds,
        List.of(CLINICAL_ATTRIBUTE_ID_1),
            CLINICAL_DATA_TYPE,
            PROJECTION))
        .thenReturn(expectedPatientClinicalDataList);

    List<ClinicalData> result =
        clinicalDataService.fetchClinicalData(
            studyIds,
            patientIds,
            List.of(CLINICAL_ATTRIBUTE_ID_1),
            CLINICAL_DATA_TYPE,
            PROJECTION);

    Assertions.assertEquals(expectedPatientClinicalDataList, result);
  }

  @Test
  public void fetchMetaClinicalDataPatientClinicalDataType() {

    List<String> studyIds = new ArrayList<>();
    studyIds.add(STUDY_ID);
    List<String> patientIds = new ArrayList<>();
    patientIds.add(PATIENT_ID_1);

    BaseMeta expectedBaseMeta = new BaseMeta();
    expectedBaseMeta.setTotalCount(5);

    when(clinicalDataRepository.fetchMetaClinicalData(
            studyIds, patientIds, List.of(CLINICAL_ATTRIBUTE_ID_1), CLINICAL_DATA_TYPE))
        .thenReturn(expectedBaseMeta);

    BaseMeta result =
        clinicalDataService.fetchMetaClinicalData(
            studyIds, patientIds, List.of(CLINICAL_ATTRIBUTE_ID_1), CLINICAL_DATA_TYPE);

    Assertions.assertEquals((Integer) 5, result.getTotalCount());
  }

  @Test
  public void fetchClinicalDataCounts() {

    ClinicalAttribute clinicalAttribute1 = new ClinicalAttribute();
    clinicalAttribute1.setAttrId(CLINICAL_ATTRIBUTE_ID_1);
    clinicalAttribute1.setPatientAttribute(true);
    ClinicalAttribute clinicalAttribute2 = new ClinicalAttribute();
    clinicalAttribute2.setAttrId(CLINICAL_ATTRIBUTE_ID_2);
    clinicalAttribute2.setPatientAttribute(true);
    ClinicalAttribute clinicalAttribute3 = new ClinicalAttribute();
    clinicalAttribute3.setAttrId(CLINICAL_ATTRIBUTE_ID_3);
    clinicalAttribute3.setPatientAttribute(false);

    ClinicalAttribute clinicalAttribute4 = new ClinicalAttribute();
    clinicalAttribute4.setAttrId(CLINICAL_ATTRIBUTE_ID_1);
    clinicalAttribute4.setPatientAttribute(false);

    when(clinicalAttributeService.getClinicalAttributesByStudyIdsAndAttributeIds(
            Arrays.asList(STUDY_ID, STUDY_ID, STUDY_ID),
            Arrays.asList(
                CLINICAL_ATTRIBUTE_ID_1, CLINICAL_ATTRIBUTE_ID_2, CLINICAL_ATTRIBUTE_ID_3)))
        .thenReturn(
            Arrays.asList(
                clinicalAttribute1, clinicalAttribute2, clinicalAttribute3, clinicalAttribute4));

    ClinicalDataCount clinicalDataCount2 = new ClinicalDataCount();
    clinicalDataCount2.setAttributeId(CLINICAL_ATTRIBUTE_ID_1);
    clinicalDataCount2.setValue("NA");
    clinicalDataCount2.setCount(1);
    ClinicalDataCount clinicalDataCount5 = new ClinicalDataCount();
    clinicalDataCount5.setAttributeId(CLINICAL_ATTRIBUTE_ID_3);
    clinicalDataCount5.setValue("N/A");
    clinicalDataCount5.setCount(3);
    ClinicalDataCount clinicalDataCount6 = new ClinicalDataCount();
    clinicalDataCount6.setAttributeId(CLINICAL_ATTRIBUTE_ID_1);
    clinicalDataCount6.setValue("value1");
    clinicalDataCount6.setCount(2);

    when(clinicalDataRepository.fetchClinicalDataCounts(
            Arrays.asList(STUDY_ID, STUDY_ID, STUDY_ID),
            Arrays.asList(SAMPLE_ID1, SAMPLE_ID2, SAMPLE_ID3),
            Arrays.asList(CLINICAL_ATTRIBUTE_ID_3, CLINICAL_ATTRIBUTE_ID_1),
            "SAMPLE",
            "SUMMARY"))
        .thenReturn(Arrays.asList(clinicalDataCount2, clinicalDataCount5, clinicalDataCount6));

    ClinicalDataCount clinicalDataCount3 = new ClinicalDataCount();
    clinicalDataCount3.setAttributeId(CLINICAL_ATTRIBUTE_ID_2);
    clinicalDataCount3.setValue("value2");
    clinicalDataCount3.setCount(1);
    ClinicalDataCount clinicalDataCount4 = new ClinicalDataCount();
    clinicalDataCount4.setAttributeId(CLINICAL_ATTRIBUTE_ID_2);
    clinicalDataCount4.setValue("value3");
    clinicalDataCount4.setCount(1);

    when(clinicalDataRepository.fetchClinicalDataCounts(
            List.of(STUDY_ID, STUDY_ID, STUDY_ID),
            List.of(SAMPLE_ID1, SAMPLE_ID2, SAMPLE_ID3),
            List.of(CLINICAL_ATTRIBUTE_ID_2),
            "PATIENT",
            "SUMMARY"))
        .thenReturn(Arrays.asList(clinicalDataCount3, clinicalDataCount4));

    ClinicalDataCount clinicalDataCount1 = new ClinicalDataCount();
    clinicalDataCount1.setAttributeId(CLINICAL_ATTRIBUTE_ID_1);
    clinicalDataCount1.setValue("value1");
    clinicalDataCount1.setCount(2);

    when(clinicalDataRepository.fetchClinicalDataCounts(
            List.of(STUDY_ID, STUDY_ID, STUDY_ID),
            List.of(SAMPLE_ID1, SAMPLE_ID2, SAMPLE_ID3),
            List.of(CLINICAL_ATTRIBUTE_ID_1),
            "PATIENT",
            "DETAILED"))
        .thenReturn(List.of(clinicalDataCount1));

    List<Patient> patients = new ArrayList<>();
    Patient patient1 = new Patient();
    patient1.setStableId(PATIENT_ID_1);
    patient1.setCancerStudyIdentifier(STUDY_ID);
    patients.add(patient1);
    Patient patient2 = new Patient();
    patient2.setStableId(PATIENT_ID_2);
    patient2.setCancerStudyIdentifier(STUDY_ID);
    patients.add(patient2);
    Patient patient3 = new Patient();
    patient3.setStableId(PATIENT_ID_3);
    patient3.setCancerStudyIdentifier(STUDY_ID);
    patients.add(patient3);

    when(patientService.getPatientsOfSamples(
            Arrays.asList(STUDY_ID, STUDY_ID, STUDY_ID),
            Arrays.asList(SAMPLE_ID1, SAMPLE_ID2, SAMPLE_ID3)))
        .thenReturn(patients);

    List<ClinicalDataCountItem> result =
        clinicalDataService.fetchClinicalDataCounts(
            Arrays.asList(STUDY_ID, STUDY_ID, STUDY_ID),
            Arrays.asList(SAMPLE_ID1, SAMPLE_ID2, SAMPLE_ID3),
            Arrays.asList(
                CLINICAL_ATTRIBUTE_ID_1, CLINICAL_ATTRIBUTE_ID_2, CLINICAL_ATTRIBUTE_ID_3));

    Assertions.assertEquals(3, result.size());

    ClinicalDataCountItem counts3 = result.getFirst();
    Assertions.assertEquals(CLINICAL_ATTRIBUTE_ID_3, counts3.getAttributeId());
    List<ClinicalDataCount> clinicalDataCounts3 = counts3.getCounts();
    Assertions.assertEquals(1, clinicalDataCounts3.size());
    ClinicalDataCount count5 = clinicalDataCounts3.getFirst();
    Assertions.assertEquals(CLINICAL_ATTRIBUTE_ID_3, count5.getAttributeId());
    Assertions.assertEquals("NA", count5.getValue());
    Assertions.assertEquals((Integer) 3, count5.getCount());

    ClinicalDataCountItem counts2 = result.get(1);
    Assertions.assertEquals(CLINICAL_ATTRIBUTE_ID_2, counts2.getAttributeId());
    List<ClinicalDataCount> clinicalDataCounts2 = counts2.getCounts();
    Assertions.assertEquals(3, clinicalDataCounts2.size());
    ClinicalDataCount count3 = clinicalDataCounts2.get(0);
    Assertions.assertEquals(CLINICAL_ATTRIBUTE_ID_2, count3.getAttributeId());
    Assertions.assertEquals("value2", count3.getValue());
    Assertions.assertEquals((Integer) 1, count3.getCount());
    ClinicalDataCount count4 = clinicalDataCounts2.get(1);
    Assertions.assertEquals(CLINICAL_ATTRIBUTE_ID_2, count4.getAttributeId());
    Assertions.assertEquals("value3", count4.getValue());
    Assertions.assertEquals((Integer) 1, count4.getCount());

    ClinicalDataCountItem counts1 = result.get(2);
    Assertions.assertEquals(CLINICAL_ATTRIBUTE_ID_1, counts1.getAttributeId());
    List<ClinicalDataCount> clinicalDataCounts1 = counts1.getCounts();
    Assertions.assertEquals(1, clinicalDataCounts1.size());
    ClinicalDataCount count1 = clinicalDataCounts1.getFirst();
    Assertions.assertEquals(CLINICAL_ATTRIBUTE_ID_1, count1.getAttributeId());
    Assertions.assertEquals("value1", count1.getValue());
    Assertions.assertEquals((Integer) 4, count1.getCount());
  }

  @Test
  public void fetchSampleClinicalTableHappyCase() {

    when(clinicalDataRepository.getVisibleSampleInternalIdsForClinicalTable(
            sampleStudyIds, sampleIds, null, null, searchTerm, sortBy, direction))
        .thenReturn(sampleInternalIdsAll);

    when(clinicalDataRepository.getSampleClinicalDataBySampleInternalIds(sampleInternalIds))
        .thenReturn(List.of(datum1, datum2));
    when(clinicalDataRepository.getPatientClinicalDataBySampleInternalIds(sampleInternalIds))
        .thenReturn(List.of(datum1, datum2));

    ImmutablePair<SampleClinicalDataCollection, Integer> result =
        clinicalDataService.fetchSampleClinicalTable(
            sampleStudyIds, sampleIds, pageSize, pageNumber, searchTerm, sortBy, direction);
    SampleClinicalDataCollection clinicalDataCollection = result.getLeft();
    Integer itemCount = result.getRight();

    Assertions.assertEquals(4, (int) itemCount);
    Assertions.assertEquals(2, clinicalDataCollection.getByUniqueSampleKey().size());
    Assertions.assertTrue(clinicalDataCollection.getByUniqueSampleKey().containsKey(uniqueKeySample1));
    Assertions.assertTrue(clinicalDataCollection.getByUniqueSampleKey().containsKey(uniqueKeySample2));
    Assertions.assertEquals(
        2, clinicalDataCollection.getByUniqueSampleKey().get(uniqueKeySample1).size());
    Assertions.assertEquals(
        2, clinicalDataCollection.getByUniqueSampleKey().get(uniqueKeySample2).size());
    Assertions.assertEquals(
        "SampleA",
        clinicalDataCollection.getByUniqueSampleKey().get(uniqueKeySample1).getFirst().getSampleId());
    Assertions.assertEquals(
        "Study1",
        clinicalDataCollection.getByUniqueSampleKey().get(uniqueKeySample1).getFirst().getStudyId());
    Assertions.assertEquals(
        "SampleA",
        clinicalDataCollection.getByUniqueSampleKey().get(uniqueKeySample2).getFirst().getSampleId());
    Assertions.assertEquals(
        "Study2",
        clinicalDataCollection.getByUniqueSampleKey().get(uniqueKeySample2).getFirst().getStudyId());
  }

  @Test
  public void fetchSampleClinicalTableEmptyIdLists() {
    Assertions.assertEquals(
        0,
        clinicalDataService
            .fetchSampleClinicalTable(
                null, sampleIds, pageSize, pageNumber, searchTerm, sortBy, direction)
            .getLeft()
            .getByUniqueSampleKey()
            .size());
    Assertions.assertEquals(
        0,
        clinicalDataService
            .fetchSampleClinicalTable(
                sampleStudyIds, null, pageSize, pageNumber, searchTerm, sortBy, direction)
            .getLeft()
            .getByUniqueSampleKey()
            .size());
    Assertions.assertEquals(
        0,
        clinicalDataService
            .fetchSampleClinicalTable(
                new ArrayList<>(), sampleIds, pageSize, pageNumber, searchTerm, sortBy, direction)
            .getLeft()
            .getByUniqueSampleKey()
            .size());
    Assertions.assertEquals(
        0,
        clinicalDataService
            .fetchSampleClinicalTable(
                sampleStudyIds,
                new ArrayList<>(),
                pageSize,
                pageNumber,
                searchTerm,
                sortBy,
                direction)
            .getLeft()
            .getByUniqueSampleKey()
            .size());
  }
}
