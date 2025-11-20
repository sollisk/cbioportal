package org.cbioportal.legacy.persistence.mybatis;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.cbioportal.legacy.model.CancerStudy;
import org.cbioportal.legacy.model.MolecularProfile;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.mybatis.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {MolecularProfileMyBatisRepository.class, TestConfig.class})
public class MolecularProfileMyBatisRepositoryTest {

  @Autowired private MolecularProfileMyBatisRepository molecularProfileMyBatisRepository;

  @Test
  public void getAllMolecularProfilesIdProjection() throws Exception {

    List<MolecularProfile> result =
        molecularProfileMyBatisRepository.getAllMolecularProfiles("ID", null, null, null, null);

    Assertions.assertEquals(12, result.size());
    MolecularProfile molecularProfile = result.get(0);
    Assertions.assertEquals((Integer) 8, molecularProfile.getMolecularProfileId());
    Assertions.assertEquals("acc_tcga_mutations", molecularProfile.getStableId());
    Assertions.assertNull(molecularProfile.getCancerStudy());
  }

  @Test
  public void getAllMolecularProfilesSummaryProjection() throws Exception {

    List<MolecularProfile> result =
        molecularProfileMyBatisRepository.getAllMolecularProfiles(
            "SUMMARY", null, null, null, null);

    Assertions.assertEquals(12, result.size());
    MolecularProfile molecularProfile = result.get(0);
    Assertions.assertEquals((Integer) 2, molecularProfile.getMolecularProfileId());
    Assertions.assertEquals("study_tcga_pub_gistic", molecularProfile.getStableId());
    Assertions.assertEquals((Integer) 1, molecularProfile.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", molecularProfile.getCancerStudyIdentifier());
    Assertions.assertEquals(
        MolecularProfile.MolecularAlterationType.COPY_NUMBER_ALTERATION,
        molecularProfile.getMolecularAlterationType());
    Assertions.assertEquals("DISCRETE", molecularProfile.getDatatype());
    Assertions.assertEquals("Putative copy-number alterations from GISTIC", molecularProfile.getName());
    Assertions.assertEquals(
        "Putative copy-number from GISTIC 2.0. Values: -2 = homozygous deletion; -1 = hemizygous "
            + "deletion; 0 = neutral / no change; 1 = gain; 2 = high level amplification.",
        molecularProfile.getDescription());
    Assertions.assertEquals(true, molecularProfile.getShowProfileInAnalysisTab());
    Assertions.assertNull(molecularProfile.getCancerStudy());
  }

  @Test
  public void getAllMolecularProfilesDetailedProjection() throws Exception {

    List<MolecularProfile> result =
        molecularProfileMyBatisRepository.getAllMolecularProfiles(
            "DETAILED", null, null, null, null);

    Assertions.assertEquals(12, result.size());
    MolecularProfile molecularProfile = result.get(0);
    Assertions.assertEquals((Integer) 2, molecularProfile.getMolecularProfileId());
    Assertions.assertEquals("study_tcga_pub_gistic", molecularProfile.getStableId());
    Assertions.assertEquals((Integer) 1, molecularProfile.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", molecularProfile.getCancerStudyIdentifier());
    Assertions.assertEquals(
        MolecularProfile.MolecularAlterationType.COPY_NUMBER_ALTERATION,
        molecularProfile.getMolecularAlterationType());
    Assertions.assertEquals("DISCRETE", molecularProfile.getDatatype());
    Assertions.assertEquals("Putative copy-number alterations from GISTIC", molecularProfile.getName());
    Assertions.assertEquals(
        "Putative copy-number from GISTIC 2.0. Values: -2 = homozygous deletion; -1 = hemizygous "
            + "deletion; 0 = neutral / no change; 1 = gain; 2 = high level amplification.",
        molecularProfile.getDescription());
    Assertions.assertEquals(true, molecularProfile.getShowProfileInAnalysisTab());
    CancerStudy cancerStudy = molecularProfile.getCancerStudy();
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
  public void getAllMolecularProfilesSummaryProjection1PageSize() throws Exception {

    List<MolecularProfile> result =
        molecularProfileMyBatisRepository.getAllMolecularProfiles("SUMMARY", 1, 0, null, null);

    Assertions.assertEquals(1, result.size());
  }

  @Test
  public void getAllMolecularProfilesSummaryProjectionStableIdSort() throws Exception {

    List<MolecularProfile> result =
        molecularProfileMyBatisRepository.getAllMolecularProfiles(
            "SUMMARY", null, null, "stableId", "ASC");

    final List<String> expected =
        Arrays.asList(
            "acc_tcga_mutations",
            "acc_tcga_sv",
            "study_tcga_pub_gistic",
            "study_tcga_pub_gsva_scores",
            "study_tcga_pub_log2CNA",
            "study_tcga_pub_m_na",
            "study_tcga_pub_methylation_hm27",
            "study_tcga_pub_mrna",
            "study_tcga_pub_mutational_signature",
            "study_tcga_pub_mutations",
            "study_tcga_pub_sv",
            "study_tcga_pub_treatment_ic50");
    assertArrayEquals(
        expected.stream().toArray(), result.stream().map(m -> m.getStableId()).toArray());
  }

  @Test
  public void getMetaMolecularProfiles() throws Exception {

    BaseMeta result = molecularProfileMyBatisRepository.getMetaMolecularProfiles();

    Assertions.assertEquals((Integer) 12, result.getTotalCount());
  }

  @Test
  public void getMolecularProfileNullResult() throws Exception {

    MolecularProfile result =
        molecularProfileMyBatisRepository.getMolecularProfile("invalid_molecular_profile");

    Assertions.assertNull(result);
  }

  @Test
  public void getMolecularProfile() throws Exception {

    MolecularProfile molecularProfile =
        molecularProfileMyBatisRepository.getMolecularProfile("study_tcga_pub_gistic");

    Assertions.assertEquals((Integer) 2, molecularProfile.getMolecularProfileId());
    Assertions.assertEquals("study_tcga_pub_gistic", molecularProfile.getStableId());
    Assertions.assertEquals((Integer) 1, molecularProfile.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", molecularProfile.getCancerStudyIdentifier());
    Assertions.assertEquals(
        MolecularProfile.MolecularAlterationType.COPY_NUMBER_ALTERATION,
        molecularProfile.getMolecularAlterationType());
    Assertions.assertEquals("DISCRETE", molecularProfile.getDatatype());
    Assertions.assertEquals("Putative copy-number alterations from GISTIC", molecularProfile.getName());
    Assertions.assertEquals(
        "Putative copy-number from GISTIC 2.0. Values: -2 = homozygous deletion; -1 = hemizygous "
            + "deletion; 0 = neutral / no change; 1 = gain; 2 = high level amplification.",
        molecularProfile.getDescription());
    Assertions.assertEquals(true, molecularProfile.getShowProfileInAnalysisTab());
    CancerStudy cancerStudy = molecularProfile.getCancerStudy();
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
  public void getMolecularProfiles() throws Exception {

    List<MolecularProfile> result =
        molecularProfileMyBatisRepository.getMolecularProfiles(
            Stream.of("study_tcga_pub_gistic", "study_tcga_pub_mrna").collect(Collectors.toSet()),
            "SUMMARY");

    final List<String> expected = Arrays.asList("study_tcga_pub_gistic", "study_tcga_pub_mrna");
    assertArrayEquals(
        expected.stream().toArray(), result.stream().map(m -> m.getStableId()).toArray());
  }

  @Test
  public void getMetaMolecularProfilesById() throws Exception {

    BaseMeta result =
        molecularProfileMyBatisRepository.getMetaMolecularProfiles(
            Stream.of("study_tcga_pub_gistic", "study_tcga_pub_mrna").collect(Collectors.toSet()));

    Assertions.assertEquals((Integer) 2, result.getTotalCount());
  }

  @Test
  public void getAllMolecularProfilesInStudySummaryProjection() throws Exception {

    List<MolecularProfile> result =
        molecularProfileMyBatisRepository.getAllMolecularProfilesInStudy(
            "study_tcga_pub", "SUMMARY", null, null, null, null);

    Assertions.assertEquals(10, result.size());
    MolecularProfile molecularProfile = result.get(0);
    Assertions.assertEquals((Integer) 2, molecularProfile.getMolecularProfileId());
    Assertions.assertEquals("study_tcga_pub_gistic", molecularProfile.getStableId());
    Assertions.assertEquals((Integer) 1, molecularProfile.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", molecularProfile.getCancerStudyIdentifier());
    Assertions.assertEquals(
        MolecularProfile.MolecularAlterationType.COPY_NUMBER_ALTERATION,
        molecularProfile.getMolecularAlterationType());
    Assertions.assertEquals("DISCRETE", molecularProfile.getDatatype());
    Assertions.assertEquals("Putative copy-number alterations from GISTIC", molecularProfile.getName());
    Assertions.assertEquals(
        "Putative copy-number from GISTIC 2.0. Values: -2 = homozygous deletion; -1 = hemizygous "
            + "deletion; 0 = neutral / no change; 1 = gain; 2 = high level amplification.",
        molecularProfile.getDescription());
    Assertions.assertEquals(true, molecularProfile.getShowProfileInAnalysisTab());
    Assertions.assertNull(molecularProfile.getCancerStudy());
  }

  @Test
  public void getMetaMolecularProfilesInStudy() throws Exception {

    BaseMeta result =
        molecularProfileMyBatisRepository.getMetaMolecularProfilesInStudy("study_tcga_pub");

    Assertions.assertEquals((Integer) 10, result.getTotalCount());
  }

  @Test
  public void getMolecularProfilesInStudies() throws Exception {

    List<MolecularProfile> result =
        molecularProfileMyBatisRepository.getMolecularProfilesInStudies(
            Arrays.asList("study_tcga_pub", "acc_tcga"), "SUMMARY");

    Assertions.assertEquals(12, result.size());
    MolecularProfile molecularProfile = result.get(0);
    Assertions.assertEquals((Integer) 8, molecularProfile.getMolecularProfileId());
    Assertions.assertEquals("acc_tcga_mutations", molecularProfile.getStableId());
    Assertions.assertEquals((Integer) 2, molecularProfile.getCancerStudyId());
    Assertions.assertEquals("acc_tcga", molecularProfile.getCancerStudyIdentifier());
    Assertions.assertEquals(
        MolecularProfile.MolecularAlterationType.MUTATION_EXTENDED,
        molecularProfile.getMolecularAlterationType());
    Assertions.assertEquals("MAF", molecularProfile.getDatatype());
    Assertions.assertEquals("Mutations", molecularProfile.getName());
    Assertions.assertEquals(
        "Mutation data from whole exome sequencing.", molecularProfile.getDescription());
    Assertions.assertEquals(true, molecularProfile.getShowProfileInAnalysisTab());
    Assertions.assertNull(molecularProfile.getCancerStudy());
  }

  @Test
  public void getMetaMolecularProfilesInStudies() throws Exception {

    BaseMeta result =
        molecularProfileMyBatisRepository.getMetaMolecularProfilesInStudies(
            Arrays.asList("study_tcga_pub", "acc_tcga"));

    Assertions.assertEquals((Integer) 12, result.getTotalCount());
  }
}
