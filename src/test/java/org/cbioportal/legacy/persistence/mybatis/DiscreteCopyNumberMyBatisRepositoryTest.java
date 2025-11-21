package org.cbioportal.legacy.persistence.mybatis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.cbioportal.legacy.model.CNA;
import org.cbioportal.legacy.model.CopyNumberCountByGene;
import org.cbioportal.legacy.model.DiscreteCopyNumberData;
import org.cbioportal.legacy.model.Gene;
import org.cbioportal.legacy.model.GeneFilterQuery;
import org.cbioportal.legacy.model.ReferenceGenomeGene;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.model.util.Select;
import org.cbioportal.legacy.persistence.mybatis.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(
    classes = {
      DiscreteCopyNumberMyBatisRepository.class,
      ReferenceGenomeGeneMyBatisRepository.class,
      TestConfig.class
    })
public class DiscreteCopyNumberMyBatisRepositoryTest {

  //    mutation, cna and struct var events in testSql.sql
  //        SAMPLE_ID, ENTREZ_GENE_ID, HUGO_GENE_SYMBOL, GENETIC_PROFILE_ID, TYPE, MUTATION_TYPE,
  // DRIVER_FILTER, DRIVER_TIERS_FILTER, PATIENT_ID, MUTATION_TYPE
  //        1	    207	AKT1	2	CNA         -2	                Putative_Driver	    Tier 1  TCGA-A1-A0SB
  //    germline
  //        2	    207	AKT1	2	CNA         2	                Putative_Passenger	Tier 2  TCGA-A1-A0SD
  //  germline
  //        1	    207	AKT1	6	MUTATION    Nonsense_Mutation	Putative_Driver	    Tier 1  TCGA-A1-A0SB
  //   germline
  //        2	    207	AKT1	6	MUTATION    Missense_Mutation	Putative_Passenger	Tier 2  TCGA-A1-A0SD
  //  germline
  //        1	    208	AKT2	2	CNA         2		            <null>              <null>  TCGA-A1-A0SB
  // germline
  //        3	    208	AKT2	6	MUTATION    Splice_Site	        Putative_Passenger	Tier 1  TCGA-A1-A0SE
  //    germline
  //        6	    672	BRCA1	6	MUTATION    Missense_Mutation	Putative_Passenger	Tier 2  TCGA-A1-A0SH
  //   germline
  //        6	    672	BRCA1	6	MUTATION    Nonsense_Mutation	Putative_Driver	    Tier 1  TCGA-A1-A0SH
  //    NA
  //        7	    672	BRCA1	6	MUTATION    Nonsense_Mutation	Putative_Driver	    Tier 2  TCGA-A1-A0SI
  //    germline
  //        12	672	BRCA1	6	MUTATION    Splice_Site	        Putative_Passenger	Tier 1  TCGA-A1-A0SO
  //  germline
  //        13	672	BRCA1	6	MUTATION    Splice_Site	        Putative_Driver	    Tier 1  TCGA-A1-A0SP
  //   germline
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
  //        1     7113-2078   TMPRSS2-ERG 7   SV          Fusion              <noi>
  // <noi>   TCGA-A1-A0SB    somatic
  //       15     8031-NULL   NCOA4-      13  SV          Fusion              <noi>
  // <noi>   TCGA-A1-A0SB    somatic

  @Autowired private DiscreteCopyNumberMyBatisRepository discreteCopyNumberMyBatisRepository;

  @Autowired private ReferenceGenomeGeneMyBatisRepository refGeneMyBatisRepository;

  @Before
  public void init() {
    molecularProfileIds = new ArrayList<>();
    molecularProfileIds.add("study_tcga_pub_gistic");
    molecularProfileIds.add("study_tcga_pub_gistic");
    sampleIds = new ArrayList<>();
    sampleIds.add("TCGA-A1-A0SB-01");
    sampleIds.add("TCGA-A1-A0SD-01");
    tiers = Select.all();
    includeUnknownTier = true;
    includeDriver = true;
    includeVUS = true;
    includeUnknownOncogenicity = true;
    cnas = Arrays.asList(CNA.AMP, CNA.HOMDEL);
  }

  List<String> molecularProfileIds = new ArrayList<>();
  List<String> sampleIds = new ArrayList<>();
  Select<String> tiers;
  boolean includeUnknownTier;
  boolean includeDriver;
  boolean includeVUS;
  boolean includeUnknownOncogenicity;
  List<CNA> cnas;

  @Test
  public void getDiscreteCopyNumbersInMolecularProfileBySampleListIdSummaryProjection()
      throws Exception {

    List<Integer> alterations = new ArrayList<>();
    alterations.add(-2);
    alterations.add(2);

    List<Integer> entrezGeneIds = new ArrayList<>();
    entrezGeneIds.add(207);
    entrezGeneIds.add(208);

    List<DiscreteCopyNumberData> result =
        discreteCopyNumberMyBatisRepository.getDiscreteCopyNumbersInMolecularProfileBySampleListId(
            "study_tcga_pub_gistic", "study_tcga_pub_all", entrezGeneIds, alterations, "SUMMARY");

    Assertions.assertEquals(3, result.size());
    DiscreteCopyNumberData discreteCopyNumberData = result.get(0);
    Assertions.assertEquals("study_tcga_pub_gistic", discreteCopyNumberData.getMolecularProfileId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", discreteCopyNumberData.getSampleId());
    Assertions.assertEquals((Integer) 207, discreteCopyNumberData.getEntrezGeneId());
    Assertions.assertEquals((Integer) (-2), discreteCopyNumberData.getAlteration());
    Assertions.assertNull(discreteCopyNumberData.getGene());
  }

  @Test
  public void getDiscreteCopyNumbersInMolecularProfileBySampleListIdDetailedProjection()
      throws Exception {

    List<Integer> alterations = new ArrayList<>();
    alterations.add(-2);
    alterations.add(2);

    List<Integer> entrezGeneIds = new ArrayList<>();
    entrezGeneIds.add(207);
    entrezGeneIds.add(208);

    List<DiscreteCopyNumberData> result =
        discreteCopyNumberMyBatisRepository.getDiscreteCopyNumbersInMolecularProfileBySampleListId(
            "study_tcga_pub_gistic", "study_tcga_pub_all", entrezGeneIds, alterations, "DETAILED");

    Assertions.assertEquals(3, result.size());
    // We do not test order here
    result.sort(
        new Comparator<DiscreteCopyNumberData>() {
          @Override
          public int compare(DiscreteCopyNumberData o1, DiscreteCopyNumberData o2) {
            int sampleIdCompare = o1.getSampleId().compareTo(o2.getSampleId());
            if (sampleIdCompare == 0) {
              return o1.getEntrezGeneId().compareTo(o2.getEntrezGeneId());
            }
            return sampleIdCompare;
          }
        });
    DiscreteCopyNumberData discreteCopyNumberDataB207 = result.get(0);
    Assertions.assertEquals(
        "study_tcga_pub_gistic", discreteCopyNumberDataB207.getMolecularProfileId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", discreteCopyNumberDataB207.getSampleId());
    Assertions.assertEquals((Integer) 207, discreteCopyNumberDataB207.getEntrezGeneId());
    Assertions.assertEquals((Integer) (-2), discreteCopyNumberDataB207.getAlteration());
    Assertions.assertEquals("Putative_Driver", discreteCopyNumberDataB207.getDriverFilter());
    Assertions.assertEquals("Pathogenic", discreteCopyNumberDataB207.getDriverFilterAnnotation());
    Assertions.assertEquals("Tier 1", discreteCopyNumberDataB207.getDriverTiersFilter());
    Assertions.assertEquals(
        "Highly Actionable", discreteCopyNumberDataB207.getDriverTiersFilterAnnotation());
    Gene gene = discreteCopyNumberDataB207.getGene();
    Assertions.assertEquals((Integer) 207, gene.getEntrezGeneId());
    Assertions.assertEquals("AKT1", gene.getHugoGeneSymbol());
    ReferenceGenomeGene refGene =
        refGeneMyBatisRepository.getReferenceGenomeGene(gene.getEntrezGeneId(), "hg19");
    Assertions.assertEquals("14q32.33", refGene.getCytoband());

    DiscreteCopyNumberData discreteCopyNumberDataB208 = result.get(1);
    Assertions.assertEquals(
        "study_tcga_pub_gistic", discreteCopyNumberDataB208.getMolecularProfileId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", discreteCopyNumberDataB208.getSampleId());
    Assertions.assertEquals((Integer) 208, discreteCopyNumberDataB208.getEntrezGeneId());
    Assertions.assertEquals((Integer) (2), discreteCopyNumberDataB208.getAlteration());
    Assertions.assertNull(discreteCopyNumberDataB208.getDriverFilter());
    Assertions.assertNull(discreteCopyNumberDataB208.getDriverFilterAnnotation());
    Assertions.assertNull(discreteCopyNumberDataB208.getDriverTiersFilter());
    Assertions.assertNull(discreteCopyNumberDataB208.getDriverTiersFilterAnnotation());

    DiscreteCopyNumberData discreteCopyNumberDataD207 = result.get(2);
    Assertions.assertEquals(
        "study_tcga_pub_gistic", discreteCopyNumberDataD207.getMolecularProfileId());
    Assertions.assertEquals("TCGA-A1-A0SD-01", discreteCopyNumberDataD207.getSampleId());
    Assertions.assertEquals((Integer) 207, discreteCopyNumberDataD207.getEntrezGeneId());
    Assertions.assertEquals((Integer) (2), discreteCopyNumberDataD207.getAlteration());
    Assertions.assertEquals("Putative_Passenger", discreteCopyNumberDataD207.getDriverFilter());
    Assertions.assertEquals("Pathogenic", discreteCopyNumberDataD207.getDriverFilterAnnotation());
    Assertions.assertEquals("Tier 2", discreteCopyNumberDataD207.getDriverTiersFilter());
    Assertions.assertEquals(
        "Potentially Actionable", discreteCopyNumberDataD207.getDriverTiersFilterAnnotation());
  }

  @Test
  public void getMetaDiscreteCopyNumbersInMolecularProfileBySampleListId() throws Exception {

    List<Integer> alterations = new ArrayList<>();
    alterations.add(-2);
    alterations.add(2);

    List<Integer> entrezGeneIds = new ArrayList<>();
    entrezGeneIds.add(207);
    entrezGeneIds.add(208);

    BaseMeta result =
        discreteCopyNumberMyBatisRepository
            .getMetaDiscreteCopyNumbersInMolecularProfileBySampleListId(
                "study_tcga_pub_gistic", "study_tcga_pub_all", entrezGeneIds, alterations);

    Assertions.assertEquals((Integer) 3, result.getTotalCount());
  }

  @Test
  public void fetchDiscreteCopyNumbersInMolecularProfile() throws Exception {

    List<String> sampleIds = new ArrayList<>();
    sampleIds.add("TCGA-A1-A0SB-01");
    sampleIds.add("TCGA-A1-A0SD-01");

    List<Integer> alterations = new ArrayList<>();
    alterations.add(-2);
    alterations.add(2);

    List<Integer> entrezGeneIds = new ArrayList<>();
    entrezGeneIds.add(207);
    entrezGeneIds.add(208);

    List<DiscreteCopyNumberData> result =
        discreteCopyNumberMyBatisRepository.fetchDiscreteCopyNumbersInMolecularProfile(
            "study_tcga_pub_gistic", sampleIds, entrezGeneIds, alterations, "SUMMARY");

    Assertions.assertEquals(3, result.size());
    Assertions.assertEquals("study_tcga_pub_gistic", result.get(0).getMolecularProfileId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", result.get(0).getSampleId());
    Assertions.assertEquals("study_tcga_pub_gistic", result.get(1).getMolecularProfileId());
    Assertions.assertEquals("TCGA-A1-A0SD-01", result.get(1).getSampleId());
    Assertions.assertEquals("study_tcga_pub_gistic", result.get(2).getMolecularProfileId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", result.get(2).getSampleId());
  }

  @Test
  public void fetchMetaDiscreteCopyNumbersInMolecularProfile() throws Exception {

    List<String> sampleIds = new ArrayList<>();
    sampleIds.add("TCGA-A1-A0SB-01");
    sampleIds.add("TCGA-A1-A0SD-01");

    List<Integer> alterations = new ArrayList<>();
    alterations.add(-2);
    alterations.add(2);

    List<Integer> entrezGeneIds = new ArrayList<>();
    entrezGeneIds.add(207);
    entrezGeneIds.add(208);

    BaseMeta result =
        discreteCopyNumberMyBatisRepository.fetchMetaDiscreteCopyNumbersInMolecularProfile(
            "study_tcga_pub_gistic", sampleIds, entrezGeneIds, alterations);

    Assertions.assertEquals((Integer) 3, result.getTotalCount());
  }

  @Test
  public void getSampleCountByGeneAndAlterationAndSampleIds() throws Exception {

    List<CopyNumberCountByGene> result =
        discreteCopyNumberMyBatisRepository.getSampleCountByGeneAndAlterationAndSampleIds(
            "study_tcga_pub_gistic", null, Arrays.asList(207, 208), Arrays.asList(-2, 2));

    Assertions.assertEquals(2, result.size());
    CopyNumberCountByGene copyNumberSampleCountByGene1 = result.get(0);
    Assertions.assertEquals((Integer) 207, copyNumberSampleCountByGene1.getEntrezGeneId());
    Assertions.assertEquals((Integer) (-2), copyNumberSampleCountByGene1.getAlteration());
    Assertions.assertEquals((Integer) 1, copyNumberSampleCountByGene1.getNumberOfAlteredCases());
    CopyNumberCountByGene copyNumberSampleCountByGene2 = result.get(1);
    Assertions.assertEquals((Integer) 208, copyNumberSampleCountByGene2.getEntrezGeneId());
    Assertions.assertEquals((Integer) (2), copyNumberSampleCountByGene2.getAlteration());
    Assertions.assertEquals((Integer) 1, copyNumberSampleCountByGene2.getNumberOfAlteredCases());
  }

  @Test
  public void getDiscreteCopyNumbersInMultipleMolecularProfiles() throws Exception {

    List<String> molecularProfileIds = new ArrayList<>();
    molecularProfileIds.add("study_tcga_pub_gistic");

    List<String> sampleIds = new ArrayList<>();
    sampleIds.add("TCGA-A1-A0SB-01");
    sampleIds.add("TCGA-A1-A0SD-01");

    List<Integer> alterationTypes = Arrays.asList(new Integer[] {2, -2});

    List<DiscreteCopyNumberData> result =
        discreteCopyNumberMyBatisRepository.getDiscreteCopyNumbersInMultipleMolecularProfiles(
            molecularProfileIds, sampleIds, null, alterationTypes, "SUMMARY");

    Assertions.assertEquals(3, result.size());
    DiscreteCopyNumberData cna1 = result.get(0);
    Assertions.assertEquals("study_tcga_pub_gistic", cna1.getMolecularProfileId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", cna1.getSampleId());
    DiscreteCopyNumberData cna2 = result.get(1);
    Assertions.assertEquals("study_tcga_pub_gistic", cna2.getMolecularProfileId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", cna2.getSampleId());
    DiscreteCopyNumberData cna3 = result.get(2);
    Assertions.assertEquals("study_tcga_pub_gistic", cna3.getMolecularProfileId());
    Assertions.assertEquals("TCGA-A1-A0SD-01", cna3.getSampleId());
  }

  @Test
  public void getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueries() throws Exception {

    GeneFilterQuery geneFilterQuery1 =
        new GeneFilterQuery(
            "AKT1",
            207,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    GeneFilterQuery geneFilterQuery2 =
        new GeneFilterQuery(
            "AKT2",
            208,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    List<GeneFilterQuery> geneQueries = Arrays.asList(geneFilterQuery1, geneFilterQuery2);

    List<DiscreteCopyNumberData> result =
        discreteCopyNumberMyBatisRepository
            .getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueries(
                molecularProfileIds, sampleIds, geneQueries, "SUMMARY");

    Assertions.assertEquals(3, result.size());
    assert (result.stream()
        .allMatch(r -> r.getMolecularProfileId().equals("study_tcga_pub_gistic")));
    List<String> expectedSampleIds = Arrays.asList("TCGA-A1-A0SB-01", "TCGA-A1-A0SD-01");
    assert (result.stream().allMatch(r -> expectedSampleIds.contains(r.getSampleId())));
  }

  @Test
  public void getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueriesInlcudeOnlyDriver()
      throws Exception {

    includeVUS = false;
    includeUnknownOncogenicity = false;

    GeneFilterQuery geneFilterQuery1 =
        new GeneFilterQuery(
            "AKT1",
            207,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    GeneFilterQuery geneFilterQuery2 =
        new GeneFilterQuery(
            "AKT2",
            208,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    List<GeneFilterQuery> geneQueries = Arrays.asList(geneFilterQuery1, geneFilterQuery2);

    List<DiscreteCopyNumberData> result =
        discreteCopyNumberMyBatisRepository
            .getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueries(
                molecularProfileIds, sampleIds, geneQueries, "SUMMARY");

    Assertions.assertEquals(1, result.size());
    assert (result.stream()
        .allMatch(r -> r.getMolecularProfileId().equals("study_tcga_pub_gistic")));
    List<String> expectedSampleIds = Arrays.asList("TCGA-A1-A0SB-01");
    assert (result.stream().allMatch(r -> expectedSampleIds.contains(r.getSampleId())));
  }

  @Test
  public void getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueriesInlcudeOnlyVus()
      throws Exception {

    includeDriver = false;
    includeUnknownOncogenicity = false;

    GeneFilterQuery geneFilterQuery1 =
        new GeneFilterQuery(
            "AKT1",
            207,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    GeneFilterQuery geneFilterQuery2 =
        new GeneFilterQuery(
            "AKT2",
            208,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    List<GeneFilterQuery> geneQueries = Arrays.asList(geneFilterQuery1, geneFilterQuery2);

    List<DiscreteCopyNumberData> result =
        discreteCopyNumberMyBatisRepository
            .getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueries(
                molecularProfileIds, sampleIds, geneQueries, "SUMMARY");

    Assertions.assertEquals(1, result.size());
    assert (result.stream()
        .allMatch(r -> r.getMolecularProfileId().equals("study_tcga_pub_gistic")));
    List<String> expectedSampleIds = Arrays.asList("TCGA-A1-A0SD-01");
    assert (result.stream().allMatch(r -> expectedSampleIds.contains(r.getSampleId())));
  }

  @Test
  public void
      getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueriesInlcudeOnlyUnknownOncogenicity()
          throws Exception {

    includeDriver = false;
    includeVUS = false;

    GeneFilterQuery geneFilterQuery1 =
        new GeneFilterQuery(
            "AKT1",
            207,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    GeneFilterQuery geneFilterQuery2 =
        new GeneFilterQuery(
            "AKT2",
            208,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    List<GeneFilterQuery> geneQueries = Arrays.asList(geneFilterQuery1, geneFilterQuery2);

    List<DiscreteCopyNumberData> result =
        discreteCopyNumberMyBatisRepository
            .getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueries(
                molecularProfileIds, sampleIds, geneQueries, "SUMMARY");

    Assertions.assertEquals(1, result.size());
    assert (result.stream()
        .allMatch(r -> r.getMolecularProfileId().equals("study_tcga_pub_gistic")));
    List<String> expectedSampleIds = Arrays.asList("TCGA-A1-A0SB-01");
    assert (result.stream().allMatch(r -> expectedSampleIds.contains(r.getSampleId())));
  }

  @Test
  public void getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueriesFilterTiers()
      throws Exception {

    Select<String> tiers = Select.byValues(Arrays.asList("Tier 2"));
    includeUnknownTier = false;

    GeneFilterQuery geneFilterQuery1 =
        new GeneFilterQuery(
            "AKT1",
            207,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    GeneFilterQuery geneFilterQuery2 =
        new GeneFilterQuery(
            "AKT2",
            208,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    List<GeneFilterQuery> geneQueries = Arrays.asList(geneFilterQuery1, geneFilterQuery2);

    List<DiscreteCopyNumberData> result =
        discreteCopyNumberMyBatisRepository
            .getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueries(
                molecularProfileIds, sampleIds, geneQueries, "SUMMARY");

    Assertions.assertEquals(1, result.size());
    assert (result.stream()
        .allMatch(r -> r.getMolecularProfileId().equals("study_tcga_pub_gistic")));
    List<String> expectedSampleIds = Arrays.asList("TCGA-A1-A0SD-01");
    assert (result.stream().allMatch(r -> expectedSampleIds.contains(r.getSampleId())));
  }

  @Test
  public void getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueriesFilterUnknownTier()
      throws Exception {

    tiers = Select.none();

    GeneFilterQuery geneFilterQuery1 =
        new GeneFilterQuery(
            "BRCA1",
            672,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    GeneFilterQuery geneFilterQuery2 =
        new GeneFilterQuery(
            "AKT1",
            207,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    GeneFilterQuery geneFilterQuery3 =
        new GeneFilterQuery(
            "AKT2",
            208,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    List<GeneFilterQuery> geneQueries =
        Arrays.asList(geneFilterQuery1, geneFilterQuery2, geneFilterQuery3);

    List<DiscreteCopyNumberData> result =
        discreteCopyNumberMyBatisRepository
            .getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueries(
                molecularProfileIds, sampleIds, geneQueries, "SUMMARY");

    Assertions.assertEquals(1, result.size());
    List<String> expectedSampleIds = Arrays.asList("TCGA-A1-A0SB-01");
    assert (result.stream().allMatch(r -> expectedSampleIds.contains(r.getSampleId())));
  }

  @Test
  public void getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueriesMixed()
      throws Exception {

    Select<String> tiers = Select.byValues(Arrays.asList("Tier 2"));

    GeneFilterQuery geneFilterQuery1 =
        new GeneFilterQuery("AKT1", 207, cnas, true, true, true, tiers, false, false, false, false);
    GeneFilterQuery geneFilterQuery2 =
        new GeneFilterQuery(
            "AKT2", 208, cnas, false, false, true, Select.all(), true, false, false, false);
    List<GeneFilterQuery> geneQueries = Arrays.asList(geneFilterQuery1, geneFilterQuery2);

    List<DiscreteCopyNumberData> result =
        discreteCopyNumberMyBatisRepository
            .getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueries(
                molecularProfileIds, sampleIds, geneQueries, "SUMMARY");

    Assertions.assertEquals(2, result.size());
    assert (result.stream()
        .allMatch(r -> r.getMolecularProfileId().equals("study_tcga_pub_gistic")));
    List<String> expectedSampleIds = Arrays.asList("TCGA-A1-A0SB-01", "TCGA-A1-A0SD-01");
    assert (result.stream().allMatch(r -> expectedSampleIds.contains(r.getSampleId())));
  }

  @Test
  public void getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueriesAmpOnly()
      throws Exception {

    List<CNA> cnas = Arrays.asList(CNA.AMP);

    GeneFilterQuery geneFilterQuery1 =
        new GeneFilterQuery(
            "AKT1",
            207,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    GeneFilterQuery geneFilterQuery2 =
        new GeneFilterQuery(
            "AKT2",
            208,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    List<GeneFilterQuery> geneQueries = Arrays.asList(geneFilterQuery1, geneFilterQuery2);

    List<DiscreteCopyNumberData> result =
        discreteCopyNumberMyBatisRepository
            .getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueries(
                molecularProfileIds, sampleIds, geneQueries, "SUMMARY");

    Assertions.assertEquals(2, result.size());
    assert (result.stream()
        .allMatch(r -> r.getMolecularProfileId().equals("study_tcga_pub_gistic")));
    List<String> expectedSampleIds = Arrays.asList("TCGA-A1-A0SB-01", "TCGA-A1-A0SD-01");
    assert (result.stream().allMatch(r -> expectedSampleIds.contains(r.getSampleId())));
  }

  @Test
  public void getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueriesDeepDelOnly()
      throws Exception {

    List<CNA> cnas = Arrays.asList(CNA.HOMDEL);

    GeneFilterQuery geneFilterQuery1 =
        new GeneFilterQuery(
            "AKT1",
            207,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    GeneFilterQuery geneFilterQuery2 =
        new GeneFilterQuery(
            "AKT2",
            208,
            cnas,
            includeDriver,
            includeVUS,
            includeUnknownOncogenicity,
            tiers,
            includeUnknownTier,
            false,
            false,
            false);
    List<GeneFilterQuery> geneQueries = Arrays.asList(geneFilterQuery1, geneFilterQuery2);

    List<DiscreteCopyNumberData> result =
        discreteCopyNumberMyBatisRepository
            .getDiscreteCopyNumbersInMultipleMolecularProfilesByGeneQueries(
                molecularProfileIds, sampleIds, geneQueries, "SUMMARY");

    Assertions.assertEquals(1, result.size());
    assert (result.stream()
        .allMatch(r -> r.getMolecularProfileId().equals("study_tcga_pub_gistic")));
    List<String> expectedSampleIds = Arrays.asList("TCGA-A1-A0SB-01");
    assert (result.stream().allMatch(r -> expectedSampleIds.contains(r.getSampleId())));
  }
}
