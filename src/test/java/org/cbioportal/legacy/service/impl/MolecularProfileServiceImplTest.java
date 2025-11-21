package org.cbioportal.legacy.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.cbioportal.legacy.model.MolecularProfile;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.MolecularProfileRepository;
import org.cbioportal.legacy.service.StudyService;
import org.cbioportal.legacy.service.exception.MolecularProfileNotFoundException;
import org.cbioportal.legacy.service.exception.StudyNotFoundException;
import org.cbioportal.legacy.service.util.MolecularProfileUtil;
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
public class MolecularProfileServiceImplTest extends BaseServiceImplTest {

  @InjectMocks private MolecularProfileServiceImpl molecularProfileService;

  @Mock private MolecularProfileRepository molecularProfileRepository;
  @Mock private StudyService studyService;
  @Mock private MolecularProfileUtil molecularProfileUtil;

  @Before
  public void setup() {
    ReflectionTestUtils.setField(molecularProfileService, "AUTHENTICATE", "false");
  }

  @Test
  public void getAllMolecularProfiles() {

    List<MolecularProfile> expectedMolecularProfileList = new ArrayList<>();
    MolecularProfile molecularProfile = new MolecularProfile();
    expectedMolecularProfileList.add(molecularProfile);

    Mockito.when(
            molecularProfileRepository.getAllMolecularProfiles(
                PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION))
        .thenReturn(expectedMolecularProfileList);

    List<MolecularProfile> result =
        molecularProfileService.getAllMolecularProfiles(
            PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION);

    Assertions.assertEquals(expectedMolecularProfileList, result);
  }

  @Test
  public void getMetaMolecularProfiles() {

    BaseMeta expectedBaseMeta = new BaseMeta();

    Mockito.when(molecularProfileRepository.getMetaMolecularProfiles())
        .thenReturn(expectedBaseMeta);

    BaseMeta result = molecularProfileService.getMetaMolecularProfiles();

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getMolecularProfileNotFound() {

    Mockito.when(molecularProfileRepository.getMolecularProfile(MOLECULAR_PROFILE_ID))
        .thenReturn(null);

    Assertions.assertThrows(MolecularProfileNotFoundException.class, () -> 
        molecularProfileService.getMolecularProfile(MOLECULAR_PROFILE_ID));
  }

  @Test
  public void getMolecularProfile() throws Exception {

    MolecularProfile expectedMolecularProfile = new MolecularProfile();

    Mockito.when(molecularProfileRepository.getMolecularProfile(MOLECULAR_PROFILE_ID))
        .thenReturn(expectedMolecularProfile);

    MolecularProfile result = molecularProfileService.getMolecularProfile(MOLECULAR_PROFILE_ID);

    Assertions.assertEquals(expectedMolecularProfile, result);
  }

  @Test
  public void getMolecularProfiles() {

    List<MolecularProfile> expectedMolecularProfiles = new ArrayList<>();

    Mockito.when(
            molecularProfileRepository.getMolecularProfiles(
                Collections.singleton(MOLECULAR_PROFILE_ID), PROJECTION))
        .thenReturn(expectedMolecularProfiles);

    List<MolecularProfile> result =
        molecularProfileService.getMolecularProfiles(
            Collections.singleton(MOLECULAR_PROFILE_ID), PROJECTION);

    Assertions.assertEquals(expectedMolecularProfiles, result);
  }

  @Test
  public void getMetaMolecularProfilesById() {

    BaseMeta expectedBaseMeta = new BaseMeta();

    Mockito.when(
            molecularProfileRepository.getMetaMolecularProfiles(
                Collections.singleton(MOLECULAR_PROFILE_ID)))
        .thenReturn(expectedBaseMeta);

    BaseMeta result =
        molecularProfileService.getMetaMolecularProfiles(
            Collections.singleton(MOLECULAR_PROFILE_ID));

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getAllMolecularProfilesInStudy() throws Exception {

    List<MolecularProfile> expectedMolecularProfileList = new ArrayList<>();
    MolecularProfile molecularProfile = new MolecularProfile();
    expectedMolecularProfileList.add(molecularProfile);

    Mockito.when(
            molecularProfileRepository.getAllMolecularProfilesInStudy(
                STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION))
        .thenReturn(expectedMolecularProfileList);

    List<MolecularProfile> result =
        molecularProfileService.getAllMolecularProfilesInStudy(
            STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION);

    Assertions.assertEquals(expectedMolecularProfileList, result);
  }

  @Test
  public void getAllMolecularProfilesInStudyNotFound() throws Exception {

    Mockito.when(studyService.getStudy(STUDY_ID)).thenThrow(new StudyNotFoundException(STUDY_ID));
    Assertions.assertThrows(StudyNotFoundException.class, () -> 
        molecularProfileService.getAllMolecularProfilesInStudy(
        STUDY_ID, PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION));
  }

  @Test
  public void getMetaMolecularProfilesInStudy() throws Exception {

    BaseMeta expectedBaseMeta = new BaseMeta();

    Mockito.when(molecularProfileRepository.getMetaMolecularProfilesInStudy(STUDY_ID))
        .thenReturn(expectedBaseMeta);

    BaseMeta result = molecularProfileService.getMetaMolecularProfilesInStudy(STUDY_ID);

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getMetaMolecularProfilesInStudyNotFound() throws Exception {

    Mockito.when(studyService.getStudy(STUDY_ID)).thenThrow(new StudyNotFoundException(STUDY_ID));
    Assertions.assertThrows(StudyNotFoundException.class, () ->
        molecularProfileService.getMetaMolecularProfilesInStudy(STUDY_ID));
  }

  @Test
  public void getMolecularProfilesInStudies() {

    List<MolecularProfile> expectedMolecularProfileList = new ArrayList<>();
    MolecularProfile molecularProfile = new MolecularProfile();
    expectedMolecularProfileList.add(molecularProfile);

    Mockito.when(
            molecularProfileRepository.getMolecularProfilesInStudies(
                List.of(STUDY_ID), PROJECTION))
        .thenReturn(expectedMolecularProfileList);

    List<MolecularProfile> result =
        molecularProfileService.getMolecularProfilesInStudies(List.of(STUDY_ID), PROJECTION);

    Assertions.assertEquals(expectedMolecularProfileList, result);
  }

  @Test
  public void getMetaMolecularProfilesInStudies() {

    BaseMeta expectedBaseMeta = new BaseMeta();

    Mockito.when(
            molecularProfileRepository.getMetaMolecularProfilesInStudies(List.of(STUDY_ID)))
        .thenReturn(expectedBaseMeta);

    BaseMeta result =
        molecularProfileService.getMetaMolecularProfilesInStudies(List.of(STUDY_ID));

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getMolecularProfilesReferredBy() throws Exception {
    List<MolecularProfile> expectedMolecularProfileList = new ArrayList<>();
    MolecularProfile molecularProfile = new MolecularProfile();
    expectedMolecularProfileList.add(molecularProfile);

    Mockito.when(molecularProfileRepository.getMolecularProfile(MOLECULAR_PROFILE_ID))
        .thenReturn(molecularProfile);
    Mockito.when(molecularProfileRepository.getMolecularProfilesReferredBy(MOLECULAR_PROFILE_ID))
        .thenReturn(expectedMolecularProfileList);

    List<MolecularProfile> result =
        molecularProfileService.getMolecularProfilesReferredBy(MOLECULAR_PROFILE_ID);

    Assertions.assertEquals(expectedMolecularProfileList, result);
  }

  @Test
  public void getMolecularProfilesReferringTo() throws Exception {
    List<MolecularProfile> expectedMolecularProfileList = new ArrayList<>();
    MolecularProfile molecularProfile = new MolecularProfile();
    expectedMolecularProfileList.add(molecularProfile);

    Mockito.when(molecularProfileRepository.getMolecularProfile(MOLECULAR_PROFILE_ID))
        .thenReturn(molecularProfile);
    Mockito.when(molecularProfileRepository.getMolecularProfilesReferringTo(MOLECULAR_PROFILE_ID))
        .thenReturn(expectedMolecularProfileList);

    List<MolecularProfile> result =
        molecularProfileService.getMolecularProfilesReferringTo(MOLECULAR_PROFILE_ID);

    Assertions.assertEquals(expectedMolecularProfileList, result);
  }
}
