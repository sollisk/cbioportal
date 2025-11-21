package org.cbioportal.legacy.persistence.mybatis;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.cbioportal.legacy.model.AlterationCountByGene;
import org.cbioportal.legacy.model.AlterationCountByStructuralVariant;
import org.cbioportal.legacy.model.AlterationFilter;
import org.cbioportal.legacy.model.CNA;
import org.cbioportal.legacy.model.CopyNumberCountByGene;
import org.cbioportal.legacy.model.MolecularProfileCaseIdentifier;
import org.cbioportal.legacy.model.MutationEventType;
import org.cbioportal.legacy.model.util.Select;
import org.cbioportal.legacy.persistence.mybatis.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(
    classes = {
      AlterationMyBatisRepository.class,
      MolecularProfileMyBatisRepository.class,
      TestConfig.class
    })
public class AlterationMyBatisRepositoryTest {

  //    mutation and cna events in testSql.sql
  //        SAMPLE_ID,    ENTREZ_GENE_ID, HUGO_GENE_SYMBOL, GENETIC_PROFILE_ID, TYPE,
  // MUTATIONio_TYPE, DRIVER_FILTER, DRIVER_TIERS_FILTER, PATIENT_ID, MUTATION_TYPE
  //        1	    207	    AKT1	2	CNA         -2	                Putative_Driver	    Tier 1
  // TCGA-A1-A0SB    germline
  //        2	    207	    AKT1	2	CNA         2	                Putative_Passenger	Tier 2
  // TCGA-A1-A0SD    germline
  //        1	    207	    AKT1	6	MUTATION    Nonsense_Mutation	Putative_Driver	    Tier 1
  // TCGA-A1-A0SB    germline
  //        2	    207	    AKT1	6	MUTATION    Missense_Mutation	Putative_Passenger	Tier 2
  // TCGA-A1-A0SD    germline
  //        1	    208	    AKT2	2	CNA         2		            <null>              <null>  TCGA-A1-A0SB
  //    germline
  //        3	    208	    AKT2	6	MUTATION    Splice_Site	        Putative_Passenger	Tier 1
  // TCGA-A1-A0SE    germline
  //        6	    672	    BRCA1	6	MUTATION    Missense_Mutation	Putative_Passenger	Tier 2
  // TCGA-A1-A0SH    germline
  //        6	    672	    BRCA1	6	MUTATION    Nonsense_Mutation	Putative_Driver	    Tier 1
  // TCGA-A1-A0SH    NA
  //        7	    672	    BRCA1	6	MUTATION    Nonsense_Mutation	Putative_Driver	    Tier 2
  // TCGA-A1-A0SI    germline
  //        12	672	    BRCA1	6	MUTATION    Splice_Site	        Putative_Passenger	Tier 1
  // TCGA-A1-A0SO    germline
  //        13	672	    BRCA1	6	MUTATION    Splice_Site	        Putative_Driver	    Tier 1
  // TCGA-A1-A0SP    germline

  //    structural_variant table in testSql.sql
  //        SAMPLE_ID,    ENTREZ_GENE_ID, HUGO_GENE_SYMBOL, GENETIC_PROFILE_ID, TYPE, MUTATION_TYPE,
  // DRIVER_FILTER, DRIVER_TIERS_FILTER, PATIENT_ID, MUTATION_TYPE
  //        1     27436-238   EML4-ALK    7   SV          Fusion              <noi>
  // <noi>   TCGA-A1-A0SB    germline
  //        2     27436-238   EML4-ALK    7   SV          Fusion              <noi>
  // <noi>   TCGA-A1-A0SD    somatic
  //        1     57670-673   KIAA..-BRAF 7   SV          Fusion              <noi>
  // <noi>   TCGA-A1-A0SB    somatic
  //        2     57670-673   KIAA..-BRAF 7   SV          Fusion              <noi>
  // <noi>   TCGA-A1-A0SD    germline
  //        2     57670-673   KIAA..-BRAF 7   SV          Fusion              <noi>
  // <noi>   TCGA-A1-A0SD    somatic
  //       15     57670-673   KIAA..-BRAF 13  SV          Fusion              <noi>
  // <noi>   TCGA-A1-A0SD    somatic
  //        1     8031-5979   NCOA4-RET   7   SV          Fusion              <noi>
  // <noi>   TCGA-A1-A0SB    somatic
  //       15     8031-5979   NCOA4-RET   13  SV          Fusion              <noi>
  // <noi>   TCGA-A1-A0SB    somatic
  //       15     7113-2078   TMPRSS2-ERG 7   SV          Fusion              <noi>
  // <noi>   TCGA-A1-A0SB    somatic
  //       15     8031-       NCOA4-      13  SV          Fusion              <noi>
  // <noi>   TCGA-A1-A0SB    somatic

  @Autowired private AlterationMyBatisRepository alterationMyBatisRepository;

  Select<MutationEventType> mutationEventTypes =
      Select.byValues(
          Arrays.asList(
              MutationEventType.splice_site,
              MutationEventType.nonsense_mutation,
              MutationEventType.missense_mutation));
  Select<CNA> cnaEventTypes = Select.byValues(Arrays.asList(CNA.AMP, CNA.HOMDEL));
  Set<MolecularProfileCaseIdentifier> sampleIdToProfileId = new TreeSet<>();
  Set<MolecularProfileCaseIdentifier> svSampleIdToProfileId = new TreeSet<>();
  Set<MolecularProfileCaseIdentifier> patientIdToProfileId = new TreeSet<>();
  Set<MolecularProfileCaseIdentifier> svPatientIdToProfileId = new TreeSet<>();
  AlterationFilter alterationFilter;

  Select<Integer> entrezGeneIds;
  Select<Integer> svEntrezGeneIds;

  @Before
  public void setup() {

    sampleIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SB-01", "study_tcga_pub_mutations"));
    sampleIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SE-01", "study_tcga_pub_mutations"));
    sampleIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SH-01", "study_tcga_pub_mutations"));
    sampleIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SI-01", "study_tcga_pub_mutations"));
    sampleIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SO-01", "study_tcga_pub_mutations"));
    sampleIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SP-01", "study_tcga_pub_mutations"));
    sampleIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SD-01", "study_tcga_pub_mutations"));
    sampleIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SB-01", "study_tcga_pub_gistic"));
    sampleIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SD-01", "study_tcga_pub_gistic"));
    svSampleIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SB-01", "study_tcga_pub_sv"));
    svSampleIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SD-01", "study_tcga_pub_sv"));
    svSampleIdToProfileId.add(new MolecularProfileCaseIdentifier("TCGA-A1-B0SO-01", "acc_tcga_sv"));

    patientIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SB", "study_tcga_pub_mutations"));
    patientIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SE", "study_tcga_pub_mutations"));
    patientIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SH", "study_tcga_pub_mutations"));
    patientIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SI", "study_tcga_pub_mutations"));
    patientIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SO", "study_tcga_pub_mutations"));
    patientIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SP", "study_tcga_pub_mutations"));
    patientIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SD", "study_tcga_pub_mutations"));
    patientIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SB", "study_tcga_pub_gistic"));
    patientIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SD", "study_tcga_pub_gistic"));
    svPatientIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SB", "study_tcga_pub_sv"));
    svPatientIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SD", "study_tcga_pub_sv"));
    svPatientIdToProfileId.add(new MolecularProfileCaseIdentifier("TCGA-A1-B0SO", "acc_tcga_sv"));

    entrezGeneIds = Select.byValues(Arrays.asList(207, 208, 672, 2064));
    svEntrezGeneIds = Select.byValues(Arrays.asList(57670, 8031, 27436, 7113));
    alterationFilter =
        new AlterationFilter(
            mutationEventTypes,
            cnaEventTypes,
            true,
            true,
            true,
            true,
            true,
            true,
            Select.all(),
            true);
  }

  @Test
  public void getSampleMutationGeneCountAllDriverAnnotationsExcluded() {
    alterationFilter.setIncludeDriver(false);
    alterationFilter.setIncludeVUS(false);
    alterationFilter.setIncludeUnknownOncogenicity(false);
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getSampleMutationGeneCountAllDriverTiersExcluded() {
    alterationFilter.setSelectedTiers(Select.none());
    alterationFilter.setIncludeUnknownTier(false);
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            new TreeSet<>(sampleIdToProfileId), entrezGeneIds, alterationFilter);
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getSampleMutationGeneCountAllDriverTiersExcludedWithNullSelect() {
    alterationFilter.setSelectedTiers(null);
    alterationFilter.setIncludeUnknownTier(false);
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getSampleMutationGeneCountAllMutationStatusExcluded() {
    alterationFilter.setIncludeSomatic(false);
    alterationFilter.setIncludeGermline(false);
    alterationFilter.setIncludeUnknownStatus(false);
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getSampleMutationGeneCount() {
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(3, result.size());
    AlterationCountByGene result672 =
        result.stream().filter(r -> r.getEntrezGeneId() == 672).findFirst().get();
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 5, result672.getTotalCount());
    Assertions.assertEquals((Integer) 4, result672.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, result207.getTotalCount());
    Assertions.assertEquals((Integer) 2, result207.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result208.getTotalCount());
    Assertions.assertEquals((Integer) 1, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleCnaGeneCount() throws Exception {
    alterationFilter.setMutationTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(2, result.size());
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 2, result207.getTotalCount());
    Assertions.assertEquals((Integer) 2, result207.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result208.getTotalCount());
    Assertions.assertEquals((Integer) 1, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleMutationAndCnaGeneCount() throws Exception {
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(3, result.size());
    AlterationCountByGene result672 =
        result.stream().filter(r -> r.getEntrezGeneId() == 672).findFirst().get();
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 5, result672.getTotalCount());
    Assertions.assertEquals((Integer) 4, result672.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 4, result207.getTotalCount());
    Assertions.assertEquals((Integer) 2, result207.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, result208.getTotalCount());
    Assertions.assertEquals((Integer) 2, result208.getNumberOfAlteredCases());
  }

  @Test
  public void whenSampleNotProfiledForCNA() throws Exception {

    Set<MolecularProfileCaseIdentifier> sampleIdToProfileId = new TreeSet<>();
    // Sample is not profiled for mutations and not cna
    sampleIdToProfileId.add(
        new MolecularProfileCaseIdentifier("TCGA-A1-A0SE-01", "study_tcga_pub_gistic"));

    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getPatientCnaGeneCountAllDriverAnnotationsExcluded() {
    alterationFilter.setIncludeDriver(false);
    alterationFilter.setIncludeVUS(false);
    alterationFilter.setIncludeUnknownOncogenicity(false);
    alterationFilter.setMutationTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getPatientCnaGeneCountAllDriverTiersExcluded() {
    alterationFilter.setSelectedTiers(Select.none());
    alterationFilter.setIncludeUnknownTier(false);
    alterationFilter.setMutationTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getPatientCnaGeneCountAllDriverTiersExcludedNullSelect() {
    alterationFilter.setSelectedTiers(null);
    alterationFilter.setIncludeUnknownTier(false);
    alterationFilter.setMutationTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getPatientCnaGeneCount() throws Exception {
    alterationFilter.setMutationTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);

    // For testSql.sql there are no more samples per patient for the investigated genes.
    // Therefore, patient level counts are the same as the sample level counts.
    Assertions.assertEquals(2, result.size());
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 2, result207.getTotalCount());
    Assertions.assertEquals((Integer) 2, result207.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result208.getTotalCount());
    Assertions.assertEquals((Integer) 1, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleCnaGeneCountLegacy() throws Exception {

    // FIXME: the CnaCountLegacy endpoint is different from the AlterationCount endpoint
    // because it returns a single additional value 'cytoband'. It would make sense to
    // harmonize these endpoints (both or none return 'cytoband') and use the AlterationCount
    // endpoint for all counts. Let's discuss...
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getSampleCnaGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(3, result.size());
    AlterationCountByGene result207up =
        result.stream()
            .filter(r -> r.getEntrezGeneId() == 207 && r.getAlteration() == 2)
            .findFirst()
            .get();
    AlterationCountByGene result207down =
        result.stream()
            .filter(r -> r.getEntrezGeneId() == 207 && r.getAlteration() == -2)
            .findFirst()
            .get();
    AlterationCountByGene result208up =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 1, result207up.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result207down.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result208up.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientCnaGeneCountLegacy() {

    // FIXME: the CnaCountLegacy endpoint is different from the AlterationCount endpoint
    // because it returns a single additional value 'cytoband'. It would make sense to
    // harmonize these endpoints (both or none return 'cytoband') and use the AlterationCount
    // endpoint for all counts. Let's discuss...
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getPatientCnaGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);

    // For testSql.sql there are no more samples per patient for the investigated genes.
    // Therefore, patient level counts are the same as the sample level counts.
    Assertions.assertEquals(3, result.size());
    AlterationCountByGene result207up =
        result.stream()
            .filter(r -> r.getEntrezGeneId() == 207 && r.getAlteration() == 2)
            .findFirst()
            .get();
    AlterationCountByGene result207down =
        result.stream()
            .filter(r -> r.getEntrezGeneId() == 207 && r.getAlteration() == -2)
            .findFirst()
            .get();
    AlterationCountByGene result208up =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 1, result207up.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result207down.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result208up.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleAlterationGeneCountsReturnsZeroForMutationsAndCnaSelectorsInNone() {
    alterationFilter.setCnaTypeSelect(Select.none());
    alterationFilter.setMutationTypeSelect(Select.none());
    alterationFilter.setStructuralVariants(false);
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getSampleAlterationGeneCountsReturnsAllForMutationsAndCnaSelectorsInAll() {
    alterationFilter.setCnaTypeSelect(Select.all());
    alterationFilter.setMutationTypeSelect(Select.all());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(3, result.size());
  }

  @Test
  public void getSampleGeneCountNullIds() throws Exception {
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            null, entrezGeneIds, new AlterationFilter());
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getPatientGeneCountNullIds() {
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            null, entrezGeneIds, new AlterationFilter());
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getSampleCnaGeneCountNullIds() {
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getSampleCnaGeneCounts(
            null, entrezGeneIds, new AlterationFilter());
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getSampleGeneCountIncludeOnlyDriver() {
    alterationFilter.setIncludeVUS(false);
    alterationFilter.setIncludeUnknownOncogenicity(false);
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(2, result.size());
    AlterationCountByGene result672 =
        result.stream().filter(r -> r.getEntrezGeneId() == 672).findFirst().get();
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    Assertions.assertEquals((Integer) 3, result672.getTotalCount());
    Assertions.assertEquals((Integer) 3, result672.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, result207.getTotalCount());
    Assertions.assertEquals((Integer) 1, result207.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleGeneCountIncludeOnlyVus() throws Exception {
    alterationFilter.setIncludeDriver(false);
    alterationFilter.setIncludeUnknownOncogenicity(false);
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(3, result.size());
    AlterationCountByGene result672 =
        result.stream().filter(r -> r.getEntrezGeneId() == 672).findFirst().get();
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 2, result672.getTotalCount());
    Assertions.assertEquals((Integer) 2, result672.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, result207.getTotalCount());
    Assertions.assertEquals((Integer) 1, result207.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result208.getTotalCount());
    Assertions.assertEquals((Integer) 1, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleGeneCountIncludeOnlyUnknownOncogenicity() {
    alterationFilter.setIncludeDriver(false);
    alterationFilter.setIncludeVUS(false);
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(1, result.size());
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 1, result208.getTotalCount());
    Assertions.assertEquals((Integer) 1, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleGeneCountIncludeOnlyTiers() {
    // All 'Tier 2' tiers are forced to be interpreted as driver events
    alterationFilter.setSelectedTiers(Select.byValues(List.of("Tier 2")));
    alterationFilter.setIncludeUnknownTier(false);
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(2, result.size());
    AlterationCountByGene result672 =
        result.stream().filter(r -> r.getEntrezGeneId() == 672).findFirst().get();
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    Assertions.assertEquals((Integer) 2, result672.getTotalCount());
    Assertions.assertEquals((Integer) 2, result672.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, result207.getTotalCount());
    Assertions.assertEquals((Integer) 1, result207.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleGeneCountIncludeUnknownTier() throws Exception {
    alterationFilter.setSelectedTiers(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(1, result.size());
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 1, result208.getTotalCount());
    Assertions.assertEquals((Integer) 1, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientMutationAndCnaGeneCount() throws Exception {
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);

    // For testSql.sql there are no more samples per patient for the investigated genes.
    // Therefore, patient level counts are the same as the sample level counts.
    Assertions.assertEquals(3, result.size());
    AlterationCountByGene result672 =
        result.stream().filter(r -> r.getEntrezGeneId() == 672).findFirst().get();
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 5, result672.getTotalCount());
    Assertions.assertEquals((Integer) 4, result672.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 4, result207.getTotalCount());
    Assertions.assertEquals((Integer) 2, result207.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, result208.getTotalCount());
    Assertions.assertEquals((Integer) 2, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientMutationGeneCountIncludeOnlyGermline() {
    alterationFilter.setIncludeSomatic(false);
    alterationFilter.setIncludeUnknownStatus(false);
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);
    // all but one mutations in testSql.sql are Germline mutations
    Assertions.assertEquals(3, result.size());
    AlterationCountByGene result672 =
        result.stream().filter(r -> r.getEntrezGeneId() == 672).findFirst().get();
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 4, result672.getTotalCount());
    Assertions.assertEquals((Integer) 4, result672.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, result207.getTotalCount());
    Assertions.assertEquals((Integer) 2, result207.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result208.getTotalCount());
    Assertions.assertEquals((Integer) 1, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientMutationGeneCountIncludeOnlySomatic() {
    alterationFilter.setIncludeGermline(false);
    alterationFilter.setIncludeUnknownStatus(false);
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);
    // all but one mutations in testSql.sql are Germline mutations
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getPatientMutationGeneCountIncludeOnlyUnknownStatus() {
    alterationFilter.setIncludeGermline(false);
    alterationFilter.setIncludeSomatic(false);
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);
    // all but one mutations in testSql.sql are Germline mutations
    Assertions.assertEquals(1, result.size());
  }

  @Test
  public void getPatientGeneCountIncludeOnlyDriver() {
    alterationFilter.setIncludeVUS(false);
    alterationFilter.setIncludeUnknownOncogenicity(false);
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(2, result.size());
    AlterationCountByGene result672 =
        result.stream().filter(r -> r.getEntrezGeneId() == 672).findFirst().get();
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    Assertions.assertEquals((Integer) 3, result672.getTotalCount());
    Assertions.assertEquals((Integer) 3, result672.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, result207.getTotalCount());
    Assertions.assertEquals((Integer) 1, result207.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientGeneCountIncludeOnlyVUS() {
    alterationFilter.setIncludeDriver(false);
    alterationFilter.setIncludeUnknownOncogenicity(false);
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(3, result.size());
    AlterationCountByGene result672 =
        result.stream().filter(r -> r.getEntrezGeneId() == 672).findFirst().get();
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 2, result672.getTotalCount());
    Assertions.assertEquals((Integer) 2, result672.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, result207.getTotalCount());
    Assertions.assertEquals((Integer) 1, result207.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result208.getTotalCount());
    Assertions.assertEquals((Integer) 1, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientGeneCountIncludeOnlyUnknownOncogenicity() {
    alterationFilter.setIncludeDriver(false);
    alterationFilter.setIncludeVUS(false);
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(1, result.size());
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 1, result208.getTotalCount());
    Assertions.assertEquals((Integer) 1, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientGeneCountIncludeOnlyTiers() {
    // All 'Tier 2' tiers are forced to be interpreted as driver events
    alterationFilter.setSelectedTiers(Select.byValues(Arrays.asList("Tier 2")));
    alterationFilter.setIncludeUnknownTier(false);
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(2, result.size());
    AlterationCountByGene result672 =
        result.stream().filter(r -> r.getEntrezGeneId() == 672).findFirst().get();
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    Assertions.assertEquals((Integer) 2, result672.getTotalCount());
    Assertions.assertEquals((Integer) 2, result672.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, result207.getTotalCount());
    Assertions.assertEquals((Integer) 1, result207.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientGeneCountIncludeUnknownTier() {
    alterationFilter.setSelectedTiers(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(1, result.size());
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 1, result208.getTotalCount());
    Assertions.assertEquals((Integer) 1, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleCnaGeneCountLegacyOnlyDriver() {
    alterationFilter.setIncludeVUS(false);
    alterationFilter.setIncludeUnknownOncogenicity(false);
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getSampleCnaGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(1, result.size());
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    Assertions.assertEquals((Integer) 1, result207.getTotalCount());
    Assertions.assertEquals((Integer) 1, result207.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleCnaGeneCountLegacyOnlyVUS() {
    alterationFilter.setIncludeDriver(false);
    alterationFilter.setIncludeUnknownOncogenicity(false);
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getSampleCnaGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(1, result.size());
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    Assertions.assertEquals((Integer) 1, result207.getTotalCount());
    Assertions.assertEquals((Integer) 1, result207.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleCnaGeneCountLegacyOnlyUnknownOncogenicity() {
    alterationFilter.setIncludeDriver(false);
    alterationFilter.setIncludeVUS(false);
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getSampleCnaGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(1, result.size());
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 1, result208.getTotalCount());
    Assertions.assertEquals((Integer) 1, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleCnaGeneCountLegacyOnlyUnknownTier() {
    alterationFilter.setSelectedTiers(Select.none());
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getSampleCnaGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(1, result.size());
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 1, result208.getTotalCount());
    Assertions.assertEquals((Integer) 1, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleCnaGeneCountLegacyOnlyTier2() {
    // All 'Tier 2' tiers are forced to be interpreted as driver events
    alterationFilter.setSelectedTiers(Select.byValues(Arrays.asList("Tier 2")));
    alterationFilter.setIncludeUnknownTier(false);
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getSampleCnaGeneCounts(
            sampleIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(1, result.size());
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    Assertions.assertEquals((Integer) 1, result207.getTotalCount());
    Assertions.assertEquals((Integer) 1, result207.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientCnaGeneCountLegacyOnlyDriver() {
    alterationFilter.setIncludeVUS(false);
    alterationFilter.setIncludeUnknownOncogenicity(false);
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getPatientCnaGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(1, result.size());
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    Assertions.assertEquals((Integer) 1, result207.getTotalCount());
    Assertions.assertEquals((Integer) 1, result207.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientCnaGeneCountLegacyOnlyVUS() {
    alterationFilter.setIncludeDriver(false);
    alterationFilter.setIncludeUnknownOncogenicity(false);
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getPatientCnaGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(1, result.size());
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    Assertions.assertEquals((Integer) 1, result207.getTotalCount());
    Assertions.assertEquals((Integer) 1, result207.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientCnaGeneCountLegacyOnlyUnknownOncogenicity() {
    alterationFilter.setIncludeDriver(false);
    alterationFilter.setIncludeVUS(false);
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getPatientCnaGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(1, result.size());
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 1, result208.getTotalCount());
    Assertions.assertEquals((Integer) 1, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientCnaGeneCountLegacyOnlyUnknownTier() {
    alterationFilter.setSelectedTiers(Select.none());
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getPatientCnaGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(1, result.size());
    AlterationCountByGene result208 =
        result.stream().filter(r -> r.getEntrezGeneId() == 208).findFirst().get();
    Assertions.assertEquals((Integer) 1, result208.getTotalCount());
    Assertions.assertEquals((Integer) 1, result208.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientCnaGeneCountLegacyOnlyTier2() {
    // All 'Tier 2' tiers are forced to be interpreted as driver events
    alterationFilter.setSelectedTiers(Select.byValues(Arrays.asList("Tier 2")));
    alterationFilter.setIncludeUnknownTier(false);
    alterationFilter.setSelectedTiers(Select.byValues(Arrays.asList("Tier 2")));
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getPatientCnaGeneCounts(
            patientIdToProfileId, entrezGeneIds, alterationFilter);

    Assertions.assertEquals(1, result.size());
    AlterationCountByGene result207 =
        result.stream().filter(r -> r.getEntrezGeneId() == 207).findFirst().get();
    Assertions.assertEquals((Integer) 1, result207.getTotalCount());
    Assertions.assertEquals((Integer) 1, result207.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientCnaGeneCountNullIds() {
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getPatientCnaGeneCounts(
            null, entrezGeneIds, new AlterationFilter());
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getSampleGeneCountNullEntrezGeneIds() {
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, null, new AlterationFilter());
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getSampleGeneCountEmptyEntrezGeneIds() {
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, Select.none(), new AlterationFilter());
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getSampleGeneCountAllEntrezGeneIds() {
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            sampleIdToProfileId, Select.all(), new AlterationFilter());
    Assertions.assertEquals(3, result.size());
  }

  @Test
  public void getPatientGeneCountNullEntrezGeneIds() {
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, null, new AlterationFilter());
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getPatientGeneCountEmptyEntrezGeneIds() {
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, Select.none(), new AlterationFilter());
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getPatientGeneCountAllEntrezGeneIds() {
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            patientIdToProfileId, Select.all(), new AlterationFilter());
    Assertions.assertEquals(3, result.size());
  }

  @Test
  public void getSampleCnaGeneCountNullEntrezGeneIds() {
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getSampleCnaGeneCounts(
            sampleIdToProfileId, null, new AlterationFilter());
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getSampleCnaGeneCountEmptyEntrezGeneIds() {
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getSampleCnaGeneCounts(
            sampleIdToProfileId, Select.none(), new AlterationFilter());
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getSampleCnaGeneCountAllEntrezGeneIds() {
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getSampleCnaGeneCounts(
            sampleIdToProfileId, Select.all(), new AlterationFilter());
    Assertions.assertEquals(3, result.size());
  }

  @Test
  public void getPatientCnaGeneCountNullEntrezGeneIds() {
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getPatientCnaGeneCounts(
            patientIdToProfileId, null, new AlterationFilter());
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getPatientCnaGeneCountEmptyEntrezGeneIds() {
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getPatientCnaGeneCounts(
            patientIdToProfileId, Select.none(), new AlterationFilter());
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getPatientCnaGeneCountAllEntrezGeneIds() {
    List<CopyNumberCountByGene> result =
        alterationMyBatisRepository.getPatientCnaGeneCounts(
            patientIdToProfileId, Select.all(), new AlterationFilter());
    Assertions.assertEquals(3, result.size());
  }

  //    StructuralVariant sample count tests
  @Test
  public void getSampleStructuralVariantCountAllStructuralVariantStatusExcluded() {
    alterationFilter.setIncludeSomatic(false);
    alterationFilter.setIncludeGermline(false);
    alterationFilter.setIncludeUnknownStatus(false);
    alterationFilter.setStructuralVariants(true);
    alterationFilter.setMutationTypeSelect(Select.none());
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            svSampleIdToProfileId, svEntrezGeneIds, alterationFilter);
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getSampleStructuralVariantCountAllStructuralVariantStatusIncluded() {
    alterationFilter.setStructuralVariants(true);
    alterationFilter.setMutationTypeSelect(Select.none());
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            svSampleIdToProfileId, svEntrezGeneIds, alterationFilter);
    Assertions.assertEquals(4, result.size());
  }

  @Test
  public void getSampleStructuralVariantCountIncludeOnlyGermline() throws Exception {
    alterationFilter.setIncludeSomatic(false);
    alterationFilter.setIncludeUnknownStatus(false);
    alterationFilter.setStructuralVariants(true);
    alterationFilter.setMutationTypeSelect(Select.none());
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            svSampleIdToProfileId, svEntrezGeneIds, alterationFilter);
    // two structural variants in testSql.sql are germline mutations

    AlterationCountByGene result57670 =
        result.stream().filter(r -> r.getEntrezGeneId() == 57670).findFirst().get();
    AlterationCountByGene result27436 =
        result.stream().filter(r -> r.getEntrezGeneId() == 27436).findFirst().get();
    AlterationCountByGene result7113 =
        result.stream().filter(r -> r.getEntrezGeneId() == 7113).findFirst().get();
    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals((Integer) 1, result57670.getTotalCount());
    Assertions.assertEquals((Integer) 1, result57670.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result27436.getTotalCount());
    Assertions.assertEquals((Integer) 1, result27436.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result7113.getTotalCount());
    Assertions.assertEquals((Integer) 1, result7113.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleStructuralVariantCountIncludeOnlySomatic() {
    alterationFilter.setIncludeGermline(false);
    alterationFilter.setIncludeUnknownStatus(false);
    alterationFilter.setStructuralVariants(true);
    alterationFilter.setMutationTypeSelect(Select.none());
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            svSampleIdToProfileId, svEntrezGeneIds, alterationFilter);
    Assertions.assertEquals(4, result.size());

    AlterationCountByGene result57670 =
        result.stream().filter(r -> r.getEntrezGeneId() == 57670).findFirst().get();
    AlterationCountByGene result8031 =
        result.stream().filter(r -> r.getEntrezGeneId() == 8031).findFirst().get();
    AlterationCountByGene result27436 =
        result.stream().filter(r -> r.getEntrezGeneId() == 27436).findFirst().get();
    AlterationCountByGene result7113 =
        result.stream().filter(r -> r.getEntrezGeneId() == 7113).findFirst().get();
    Assertions.assertEquals((Integer) 3, result57670.getTotalCount());
    Assertions.assertEquals((Integer) 3, result57670.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 3, result8031.getTotalCount());
    Assertions.assertEquals((Integer) 2, result8031.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result27436.getTotalCount());
    Assertions.assertEquals((Integer) 1, result27436.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result7113.getTotalCount());
    Assertions.assertEquals((Integer) 1, result7113.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleStructuralVariantCountIncludeOnlyUnknownStatus() {
    alterationFilter.setIncludeGermline(false);
    alterationFilter.setIncludeSomatic(false);
    alterationFilter.setStructuralVariants(true);
    alterationFilter.setMutationTypeSelect(Select.none());
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getSampleAlterationGeneCounts(
            svSampleIdToProfileId, svEntrezGeneIds, alterationFilter);
    Assertions.assertEquals(0, result.size());
  }

  //    StructuralVariant patient count tests
  @Test
  public void getPatientStructuralVariantCountAllStructuralVariantStatusExcluded()
      throws Exception {
    alterationFilter.setIncludeSomatic(false);
    alterationFilter.setIncludeGermline(false);
    alterationFilter.setIncludeUnknownStatus(false);
    alterationFilter.setStructuralVariants(true);
    alterationFilter.setMutationTypeSelect(Select.none());
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            svPatientIdToProfileId, svEntrezGeneIds, alterationFilter);
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getPatientStructuralVariantCountAllStructuralVariantStatusIncluded()
      throws Exception {
    alterationFilter.setStructuralVariants(true);
    alterationFilter.setMutationTypeSelect(Select.none());
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            svPatientIdToProfileId, svEntrezGeneIds, alterationFilter);
    Assertions.assertEquals(4, result.size());
  }

  @Test
  public void getPatientStructuralVariantCountIncludeOnlyGermline() throws Exception {
    alterationFilter.setIncludeSomatic(false);
    alterationFilter.setIncludeUnknownStatus(false);
    alterationFilter.setStructuralVariants(true);
    alterationFilter.setMutationTypeSelect(Select.none());
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            svPatientIdToProfileId, svEntrezGeneIds, alterationFilter);
    // two structural variants in testSql.sql are germline mutations
    Assertions.assertEquals(3, result.size());

    AlterationCountByGene result57670 =
        result.stream().filter(r -> r.getEntrezGeneId() == 57670).findFirst().get();
    AlterationCountByGene result27436 =
        result.stream().filter(r -> r.getEntrezGeneId() == 27436).findFirst().get();
    AlterationCountByGene result7113 =
        result.stream().filter(r -> r.getEntrezGeneId() == 7113).findFirst().get();
    Assertions.assertEquals((Integer) 1, result57670.getTotalCount());
    Assertions.assertEquals((Integer) 1, result57670.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result27436.getTotalCount());
    Assertions.assertEquals((Integer) 1, result27436.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result7113.getTotalCount());
    Assertions.assertEquals((Integer) 1, result7113.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientStructuralVariantCountIncludeOnlySomatic() {
    alterationFilter.setIncludeGermline(false);
    alterationFilter.setIncludeUnknownStatus(false);
    alterationFilter.setStructuralVariants(true);
    alterationFilter.setMutationTypeSelect(Select.none());
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            svPatientIdToProfileId, svEntrezGeneIds, alterationFilter);
    Assertions.assertEquals(4, result.size());

    AlterationCountByGene result57670 =
        result.stream().filter(r -> r.getEntrezGeneId() == 57670).findFirst().get();
    AlterationCountByGene result8031 =
        result.stream().filter(r -> r.getEntrezGeneId() == 8031).findFirst().get();
    AlterationCountByGene result27436 =
        result.stream().filter(r -> r.getEntrezGeneId() == 27436).findFirst().get();
    AlterationCountByGene result7113 =
        result.stream().filter(r -> r.getEntrezGeneId() == 7113).findFirst().get();
    Assertions.assertEquals((Integer) 3, result57670.getTotalCount());
    Assertions.assertEquals((Integer) 3, result57670.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 3, result8031.getTotalCount());
    Assertions.assertEquals((Integer) 2, result8031.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result27436.getTotalCount());
    Assertions.assertEquals((Integer) 1, result27436.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, result7113.getTotalCount());
    Assertions.assertEquals((Integer) 1, result7113.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientStructuralVariantCountIncludeOnlyUnknownStatus() {
    alterationFilter.setIncludeGermline(false);
    alterationFilter.setIncludeSomatic(false);
    alterationFilter.setStructuralVariants(true);
    alterationFilter.setMutationTypeSelect(Select.none());
    alterationFilter.setCnaTypeSelect(Select.none());
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            svPatientIdToProfileId, svEntrezGeneIds, alterationFilter);
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getPatientStructuralVariantCountIncludeCustomDriverAnnotationsIncludeUnknown()
      throws Exception {
    alterationFilter.setStructuralVariants(true);
    alterationFilter.setMutationTypeSelect(Select.none());
    alterationFilter.setCnaTypeSelect(Select.none());
    alterationFilter.setSelectedTiers(Select.byValues(Arrays.asList("Class 2")));
    alterationFilter.setIncludeUnknownTier(true);
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            svPatientIdToProfileId, svEntrezGeneIds, alterationFilter);
    Assertions.assertEquals(4, result.size());
  }

  @Test
  public void getPatientStructuralVariantCountIncludeCustomDriverAnnotationsExcludeUnknown()
      throws Exception {
    alterationFilter.setStructuralVariants(true);
    alterationFilter.setMutationTypeSelect(Select.none());
    alterationFilter.setCnaTypeSelect(Select.none());
    alterationFilter.setSelectedTiers(Select.byValues(Arrays.asList("Class 2")));
    alterationFilter.setIncludeUnknownTier(false);
    List<AlterationCountByGene> result =
        alterationMyBatisRepository.getPatientAlterationGeneCounts(
            svPatientIdToProfileId, svEntrezGeneIds, alterationFilter);
    Assertions.assertEquals(1, result.size());
  }

  @Test
  public void getSampleStructuralVariantCount() {
    List<AlterationCountByStructuralVariant> result =
        alterationMyBatisRepository.getSampleStructuralVariantCounts(
            svSampleIdToProfileId, alterationFilter);

    // Should be one KIAA154-BRAF, one NCOA4-RET and one NCOA4-null
    Assertions.assertEquals(5, result.size());
    AlterationCountByStructuralVariant resultEmlAlk = findStructVarCount("EML4", "ALK", result);
    AlterationCountByStructuralVariant resultKiaaBraf =
        findStructVarCount("KIAA1549", "BRAF", result);
    AlterationCountByStructuralVariant resultTmprsErg =
        findStructVarCount("TMPRSS2", "ERG", result);
    AlterationCountByStructuralVariant resultNcoRet = findStructVarCount("NCOA4", "RET", result);
    AlterationCountByStructuralVariant resultNcoNull = findStructVarCount("NCOA4", null, result);
    Assertions.assertEquals((Integer) 2, resultEmlAlk.getTotalCount());
    Assertions.assertEquals((Integer) 2, resultEmlAlk.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 4, resultKiaaBraf.getTotalCount());
    Assertions.assertEquals((Integer) 3, resultKiaaBraf.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, resultNcoRet.getTotalCount());
    Assertions.assertEquals((Integer) 2, resultNcoRet.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, resultNcoNull.getTotalCount());
    Assertions.assertEquals((Integer) 1, resultNcoNull.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, resultTmprsErg.getTotalCount());
    Assertions.assertEquals((Integer) 2, resultTmprsErg.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleStructuralVariantCountAllSvStatusExcluded() {

    // Note: 'NA' for SV status is not allowed as per file-formats.md
    alterationFilter.setIncludeSomatic(false);
    alterationFilter.setIncludeGermline(false);
    alterationFilter.setIncludeUnknownStatus(false);
    List<AlterationCountByStructuralVariant> result =
        alterationMyBatisRepository.getSampleStructuralVariantCounts(
            sampleIdToProfileId, alterationFilter);

    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getSampleStructuralVariantCountSomaticSvStatusExcluded() {

    // Note: 'NA' for SV status is not allowed as per file-formats.md
    alterationFilter.setIncludeSomatic(false);
    alterationFilter.setIncludeUnknownStatus(false);
    List<AlterationCountByStructuralVariant> result =
        alterationMyBatisRepository.getSampleStructuralVariantCounts(
            svSampleIdToProfileId, alterationFilter);

    Assertions.assertEquals(3, result.size());
    AlterationCountByStructuralVariant resultKiaaBraf =
        findStructVarCount("KIAA1549", "BRAF", result);
    AlterationCountByStructuralVariant resultEmlAlk = findStructVarCount("EML4", "ALK", result);
    AlterationCountByStructuralVariant resultTmprsErg =
        findStructVarCount("TMPRSS2", "ERG", result);
    Assertions.assertEquals((Integer) 1, resultKiaaBraf.getTotalCount());
    Assertions.assertEquals((Integer) 1, resultKiaaBraf.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, resultEmlAlk.getTotalCount());
    Assertions.assertEquals((Integer) 1, resultEmlAlk.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, resultTmprsErg.getTotalCount());
    Assertions.assertEquals((Integer) 1, resultTmprsErg.getNumberOfAlteredCases());
  }

  @Test
  public void getSampleStructuralVariantCountGermlineSvStatusExcluded() {
    // Note: 'NA' for SV status is not allowed as per file-formats.md
    alterationFilter.setIncludeGermline(false);
    alterationFilter.setIncludeUnknownStatus(false);
    List<AlterationCountByStructuralVariant> result =
        alterationMyBatisRepository.getSampleStructuralVariantCounts(
            svSampleIdToProfileId, alterationFilter);

    Assertions.assertEquals(5, result.size());
    AlterationCountByStructuralVariant resultEmlAlk = findStructVarCount("EML4", "ALK", result);
    AlterationCountByStructuralVariant resultKiaaBraf =
        findStructVarCount("KIAA1549", "BRAF", result);
    AlterationCountByStructuralVariant resultTmprsErg =
        findStructVarCount("TMPRSS2", "ERG", result);
    AlterationCountByStructuralVariant resultNcoRet = findStructVarCount("NCOA4", "RET", result);
    AlterationCountByStructuralVariant resultNcoNull = findStructVarCount("NCOA4", null, result);
    Assertions.assertEquals((Integer) 1, resultEmlAlk.getTotalCount());
    Assertions.assertEquals((Integer) 1, resultEmlAlk.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 3, resultKiaaBraf.getTotalCount());
    Assertions.assertEquals((Integer) 3, resultKiaaBraf.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, resultNcoRet.getTotalCount());
    Assertions.assertEquals((Integer) 2, resultNcoRet.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, resultNcoNull.getTotalCount());
    Assertions.assertEquals((Integer) 1, resultNcoNull.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 1, resultTmprsErg.getTotalCount());
    Assertions.assertEquals((Integer) 1, resultTmprsErg.getNumberOfAlteredCases());
  }

  @Test
  public void getPatientStructuralVariantCount() {
    List<AlterationCountByStructuralVariant> result =
        alterationMyBatisRepository.getPatientStructuralVariantCounts(
            svPatientIdToProfileId, alterationFilter);

    Assertions.assertEquals(4, result.size());
    AlterationCountByStructuralVariant resultEmlAlk = findStructVarCount("EML4", "ALK", result);
    AlterationCountByStructuralVariant resultKiaaBraf =
        findStructVarCount("KIAA1549", "BRAF", result);
    AlterationCountByStructuralVariant resultTmprsErg =
        findStructVarCount("TMPRSS2", "ERG", result);
    AlterationCountByStructuralVariant resultNcoRet = findStructVarCount("NCOA4", "RET", result);
    Assertions.assertEquals((Integer) 2, resultEmlAlk.getTotalCount());
    Assertions.assertEquals((Integer) 2, resultEmlAlk.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 4, resultKiaaBraf.getTotalCount());
    Assertions.assertEquals((Integer) 3, resultKiaaBraf.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, resultNcoRet.getTotalCount());
    Assertions.assertEquals((Integer) 2, resultNcoRet.getNumberOfAlteredCases());
    Assertions.assertEquals((Integer) 2, resultTmprsErg.getTotalCount());
    Assertions.assertEquals((Integer) 2, resultTmprsErg.getNumberOfAlteredCases());
  }

  private AlterationCountByStructuralVariant findStructVarCount(
      String gene1HugoSymbol,
      String gene2HugoSymbol,
      List<AlterationCountByStructuralVariant> counts) {
    return counts.stream()
        .filter(
            c ->
                ((c.getGene1HugoGeneSymbol() == null && gene1HugoSymbol == null)
                        || (c.getGene1HugoGeneSymbol() != null
                            && c.getGene1HugoGeneSymbol().equals(gene1HugoSymbol)))
                    && ((c.getGene2HugoGeneSymbol() == null && gene2HugoSymbol == null)
                        || (c.getGene2HugoGeneSymbol() != null
                            && c.getGene2HugoGeneSymbol().equals(gene2HugoSymbol))))
        .findFirst()
        .get();
  }
}
