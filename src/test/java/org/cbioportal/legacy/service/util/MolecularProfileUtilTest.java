package org.cbioportal.legacy.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.cbioportal.legacy.model.MolecularProfile;
import org.cbioportal.legacy.model.MolecularProfileCaseIdentifier;
import org.cbioportal.legacy.service.impl.BaseServiceImplTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MolecularProfileUtilTest {
  @InjectMocks private MolecularProfileUtil molecularProfileUtil;

  @Test
  public void getFirstFilteredMolecularProfileCaseIdentifiers() {

    MolecularProfile mutationMolecularProfile = new MolecularProfile();
    mutationMolecularProfile.setCancerStudyIdentifier(BaseServiceImplTest.STUDY_ID);
    mutationMolecularProfile.setStableId(BaseServiceImplTest.STUDY_ID + "_mutations");
    mutationMolecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.MUTATION_EXTENDED);
    mutationMolecularProfile.setDatatype("MAF");

    MolecularProfile structuralVariantMolecularProfile = new MolecularProfile();
    structuralVariantMolecularProfile.setCancerStudyIdentifier(BaseServiceImplTest.STUDY_ID);
    structuralVariantMolecularProfile.setStableId(
        BaseServiceImplTest.STUDY_ID + "_structural_variants");
    structuralVariantMolecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.STRUCTURAL_VARIANT);
    structuralVariantMolecularProfile.setDatatype("SV");

    MolecularProfile discreteCNAMolecularProfile = new MolecularProfile();
    discreteCNAMolecularProfile.setCancerStudyIdentifier(BaseServiceImplTest.STUDY_ID);
    discreteCNAMolecularProfile.setStableId(BaseServiceImplTest.STUDY_ID + "_gistic");
    discreteCNAMolecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.COPY_NUMBER_ALTERATION);
    discreteCNAMolecularProfile.setDatatype("DISCRETE");

    MolecularProfile linearCNAMolecularProfile = new MolecularProfile();
    linearCNAMolecularProfile.setCancerStudyIdentifier(BaseServiceImplTest.STUDY_ID);
    linearCNAMolecularProfile.setStableId(BaseServiceImplTest.STUDY_ID + "_linear_CNA");
    linearCNAMolecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.COPY_NUMBER_ALTERATION);
    linearCNAMolecularProfile.setDatatype("CONTINUOUS");

    MolecularProfile continuousMRNAMolecularProfile = new MolecularProfile();
    continuousMRNAMolecularProfile.setCancerStudyIdentifier(BaseServiceImplTest.STUDY_ID);
    continuousMRNAMolecularProfile.setStableId(BaseServiceImplTest.STUDY_ID + "_rna_seq_mrna");
    continuousMRNAMolecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.MRNA_EXPRESSION);
    continuousMRNAMolecularProfile.setDatatype("CONTINUOUS");

    MolecularProfile discreteMRNAMolecularProfile = new MolecularProfile();
    discreteMRNAMolecularProfile.setCancerStudyIdentifier(BaseServiceImplTest.STUDY_ID);
    discreteMRNAMolecularProfile.setStableId(
        BaseServiceImplTest.STUDY_ID + "_rna_seq_mrna_median_Zscores");
    discreteMRNAMolecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.MRNA_EXPRESSION);
    discreteMRNAMolecularProfile.setDatatype("DISCRETE");

    List<String> studyIds =
        Arrays.asList(
            BaseServiceImplTest.STUDY_ID,
            BaseServiceImplTest.STUDY_ID,
            BaseServiceImplTest.STUDY_ID);
    List<String> sampleIds =
        Arrays.asList(
            BaseServiceImplTest.SAMPLE_ID1,
            BaseServiceImplTest.SAMPLE_ID2,
            BaseServiceImplTest.SAMPLE_ID3);

    List<MolecularProfileCaseIdentifier> result =
        molecularProfileUtil.getFirstFilteredMolecularProfileCaseIdentifiers(
            new ArrayList<>(), studyIds, sampleIds, Optional.empty());
    // no molecular profiles
    Assertions.assertEquals(0, result.size());

    List<MolecularProfile> allMolecularProfiles =
        Arrays.asList(
            mutationMolecularProfile,
            structuralVariantMolecularProfile,
            discreteCNAMolecularProfile,
            linearCNAMolecularProfile,
            continuousMRNAMolecularProfile,
            discreteMRNAMolecularProfile);
    result =
        molecularProfileUtil.getFirstFilteredMolecularProfileCaseIdentifiers(
            allMolecularProfiles, studyIds, sampleIds, Optional.empty());
    // all molecular profiles
    // return would 3(6 profiles X 3 samples)
    Assertions.assertEquals(3, result.size(),"all profiles");

    // filtered mutation profile case identifiers
    result =
        molecularProfileUtil.getFirstFilteredMolecularProfileCaseIdentifiers(
            allMolecularProfiles,
            studyIds,
            sampleIds,
            Optional.of(molecularProfileUtil.isMutationProfile));
    Assertions.assertEquals(3, result.size(),"mutation profile case identifiers");

    // filtered discrete CNA profile case identifiers
    result =
        molecularProfileUtil.getFirstFilteredMolecularProfileCaseIdentifiers(
            allMolecularProfiles,
            studyIds,
            sampleIds,
            Optional.of(molecularProfileUtil.isDiscreteCNAMolecularProfile));
    Assertions.assertEquals(3, result.size(),"discrete CNA profile case identifiers");

    // filtered structural variant profile case identifiers
    result =
        molecularProfileUtil.getFirstFilteredMolecularProfileCaseIdentifiers(
            allMolecularProfiles,
            studyIds,
            sampleIds,
            Optional.of(molecularProfileUtil.isStructuralVariantMolecularProfile));
    Assertions.assertEquals(3, result.size(),"structural variant profile case identifiers");
  }

  @Test
  public void getFilteredMolecularProfileCaseIdentifiers() {

    MolecularProfile mutationMolecularProfile = new MolecularProfile();
    mutationMolecularProfile.setCancerStudyIdentifier(BaseServiceImplTest.STUDY_ID);
    mutationMolecularProfile.setStableId(BaseServiceImplTest.STUDY_ID + "_mutations");
    mutationMolecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.MUTATION_EXTENDED);
    mutationMolecularProfile.setDatatype("MAF");

    MolecularProfile structuralVariantMolecularProfile = new MolecularProfile();
    structuralVariantMolecularProfile.setCancerStudyIdentifier(BaseServiceImplTest.STUDY_ID);
    structuralVariantMolecularProfile.setStableId(
        BaseServiceImplTest.STUDY_ID + "_structural_variants");
    structuralVariantMolecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.STRUCTURAL_VARIANT);
    structuralVariantMolecularProfile.setDatatype("SV");

    MolecularProfile discreteCNAMolecularProfile = new MolecularProfile();
    discreteCNAMolecularProfile.setCancerStudyIdentifier(BaseServiceImplTest.STUDY_ID);
    discreteCNAMolecularProfile.setStableId(BaseServiceImplTest.STUDY_ID + "_gistic");
    discreteCNAMolecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.COPY_NUMBER_ALTERATION);
    discreteCNAMolecularProfile.setDatatype("DISCRETE");

    MolecularProfile linearCNAMolecularProfile = new MolecularProfile();
    linearCNAMolecularProfile.setCancerStudyIdentifier(BaseServiceImplTest.STUDY_ID);
    linearCNAMolecularProfile.setStableId(BaseServiceImplTest.STUDY_ID + "_linear_CNA");
    linearCNAMolecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.COPY_NUMBER_ALTERATION);
    linearCNAMolecularProfile.setDatatype("CONTINUOUS");

    MolecularProfile continuousMRNAMolecularProfile = new MolecularProfile();
    continuousMRNAMolecularProfile.setCancerStudyIdentifier(BaseServiceImplTest.STUDY_ID);
    continuousMRNAMolecularProfile.setStableId(BaseServiceImplTest.STUDY_ID + "_rna_seq_mrna");
    continuousMRNAMolecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.MRNA_EXPRESSION);
    continuousMRNAMolecularProfile.setDatatype("CONTINUOUS");

    MolecularProfile discreteMRNAMolecularProfile = new MolecularProfile();
    discreteMRNAMolecularProfile.setCancerStudyIdentifier(BaseServiceImplTest.STUDY_ID);
    discreteMRNAMolecularProfile.setStableId(
        BaseServiceImplTest.STUDY_ID + "_rna_seq_mrna_median_Zscores");
    discreteMRNAMolecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.MRNA_EXPRESSION);
    discreteMRNAMolecularProfile.setDatatype("DISCRETE");

    List<String> studyIds =
        Arrays.asList(
            BaseServiceImplTest.STUDY_ID,
            BaseServiceImplTest.STUDY_ID,
            BaseServiceImplTest.STUDY_ID);
    List<String> sampleIds =
        Arrays.asList(
            BaseServiceImplTest.SAMPLE_ID1,
            BaseServiceImplTest.SAMPLE_ID2,
            BaseServiceImplTest.SAMPLE_ID3);

    List<MolecularProfileCaseIdentifier> result =
        molecularProfileUtil.getFilteredMolecularProfileCaseIdentifiers(
            new ArrayList<>(), studyIds, sampleIds, Optional.empty());
    // no molecular profiles
    Assertions.assertEquals(0, result.size());

    List<MolecularProfile> allMolecularProfiles =
        Arrays.asList(
            mutationMolecularProfile,
            structuralVariantMolecularProfile,
            discreteCNAMolecularProfile,
            linearCNAMolecularProfile,
            continuousMRNAMolecularProfile,
            discreteMRNAMolecularProfile);
    result =
        molecularProfileUtil.getFilteredMolecularProfileCaseIdentifiers(
            allMolecularProfiles, studyIds, sampleIds, Optional.empty());
    // all molecular profiles
    // return would 18(6 profiles X 3 samples) instead of 24(8 profiles X 3 samples)
    Assertions.assertEquals(18, result.size(),"all profiles");

    // filtered mutation profile case identifiers
    result =
        molecularProfileUtil.getFilteredMolecularProfileCaseIdentifiers(
            allMolecularProfiles,
            studyIds,
            sampleIds,
            Optional.of(molecularProfileUtil.isMutationProfile));
    Assertions.assertEquals(3, result.size(),"mutation profile case identifiers");

    // filtered discrete CNA profile case identifiers
    result =
        molecularProfileUtil.getFilteredMolecularProfileCaseIdentifiers(
            allMolecularProfiles,
            studyIds,
            sampleIds,
            Optional.of(molecularProfileUtil.isDiscreteCNAMolecularProfile));
    Assertions.assertEquals(3, result.size(),"discrete CNA profile case identifiers");

    // filtered structural variant profile case identifiers
    result =
        molecularProfileUtil.getFilteredMolecularProfileCaseIdentifiers(
            allMolecularProfiles,
            studyIds,
            sampleIds,
            Optional.of(molecularProfileUtil.isStructuralVariantMolecularProfile));
    Assertions.assertEquals(3, result.size(),"structural variant profile case identifiers");

    // filtered MRNA expression profile case identifiers (multiple molecular profiles)
    Predicate<MolecularProfile> isMRNAProfile =
        m ->
            m.getMolecularAlterationType()
                .equals(MolecularProfile.MolecularAlterationType.MRNA_EXPRESSION);
    result =
        molecularProfileUtil.getFilteredMolecularProfileCaseIdentifiers(
            allMolecularProfiles, studyIds, sampleIds, Optional.of(isMRNAProfile));
    Assertions.assertEquals(6, result.size(),"structural variant profile case identifiers");
  }
}
