package org.cbioportal.legacy.persistence.mybatis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cbioportal.legacy.model.GenePanel;
import org.cbioportal.legacy.model.GenePanelData;
import org.cbioportal.legacy.model.GenePanelToGene;
import org.cbioportal.legacy.model.MolecularProfileCaseIdentifier;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.mybatis.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {GenePanelMyBatisRepository.class, TestConfig.class})
public class GenePanelMyBatisRepositoryTest {

  @Autowired private GenePanelMyBatisRepository genePanelMyBatisRepository;

  @Test
  public void getAllGenePanelsIdProjection() {

    List<GenePanel> result =
        genePanelMyBatisRepository.getAllGenePanels("ID", null, null, null, null);

    Assertions.assertEquals(2, result.size());
    GenePanel genePanel = result.getFirst();
    Assertions.assertEquals((Integer) 1, genePanel.getInternalId());
    Assertions.assertEquals("TESTPANEL1", genePanel.getStableId());
  }

  @Test
  public void getAllGenePanelsSummaryProjection() {

    List<GenePanel> result =
        genePanelMyBatisRepository.getAllGenePanels("SUMMARY", null, null, null, null);

    Assertions.assertEquals(2, result.size());
    GenePanel genePanel = result.getFirst();
    Assertions.assertEquals((Integer) 1, genePanel.getInternalId());
    Assertions.assertEquals("TESTPANEL1", genePanel.getStableId());
    Assertions.assertEquals("A test panel consisting of a few genes", genePanel.getDescription());
  }

  @Test
  public void getMetaGenePanels() {

    BaseMeta result = genePanelMyBatisRepository.getMetaGenePanels();

    Assertions.assertEquals((Integer) 2, result.getTotalCount());
  }

  @Test
  public void getGenePanelNullResult() {

    GenePanel result = genePanelMyBatisRepository.getGenePanel("invalid_gene_panel");

    Assertions.assertNull(result);
  }

  @Test
  public void getGenePanel() {

    GenePanel result = genePanelMyBatisRepository.getGenePanel("TESTPANEL1");

    Assertions.assertEquals((Integer) 1, result.getInternalId());
    Assertions.assertEquals("TESTPANEL1", result.getStableId());
    Assertions.assertEquals("A test panel consisting of a few genes", result.getDescription());
  }

  @Test
  public void getGenePanelData() {

    List<GenePanelData> result =
        genePanelMyBatisRepository.getGenePanelDataBySampleListId(
            "study_tcga_pub_mrna", "study_tcga_pub_all");

    Assertions.assertEquals(14, result.size());
    GenePanelData genePanelData = result.getFirst();
    Assertions.assertEquals("study_tcga_pub_mrna", genePanelData.getMolecularProfileId());
    Assertions.assertEquals("TESTPANEL1", genePanelData.getGenePanelId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", genePanelData.getSampleId());
  }

  @Test
  public void fetchGenePanelData() {

    List<GenePanelData> result =
        genePanelMyBatisRepository.fetchGenePanelData(
            "study_tcga_pub_mrna", Arrays.asList("TCGA-A1-A0SB-01", "TCGA-A1-A0SD-01"));

    Assertions.assertEquals(2, result.size());
    GenePanelData genePanelData = result.getFirst();
    Assertions.assertEquals("study_tcga_pub_mrna", genePanelData.getMolecularProfileId());
    Assertions.assertEquals("TESTPANEL1", genePanelData.getGenePanelId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", genePanelData.getSampleId());
  }

  @Test
  public void fetchGenePanelDataInMultipleMolecularProfiles() {

    List<MolecularProfileCaseIdentifier> molecularProfileSampleIdentifiers = new ArrayList<>();
    MolecularProfileCaseIdentifier profileCaseIdentifier = new MolecularProfileCaseIdentifier();
    profileCaseIdentifier.setMolecularProfileId("study_tcga_pub_mrna");
    profileCaseIdentifier.setCaseId("TCGA-A1-A0SB-01");
    molecularProfileSampleIdentifiers.add(profileCaseIdentifier);

    MolecularProfileCaseIdentifier profileCaseIdentifier2 = new MolecularProfileCaseIdentifier();
    profileCaseIdentifier2.setMolecularProfileId("study_tcga_pub_log2CNA");
    profileCaseIdentifier2.setCaseId("TCGA-A1-A0SD-01");
    molecularProfileSampleIdentifiers.add(profileCaseIdentifier2);

    List<GenePanelData> result =
        genePanelMyBatisRepository.fetchGenePanelDataInMultipleMolecularProfiles(
            molecularProfileSampleIdentifiers);

    Assertions.assertEquals(2, result.size());
    GenePanelData genePanelData = result.getFirst();
    Assertions.assertEquals("study_tcga_pub_mrna", genePanelData.getMolecularProfileId());
    Assertions.assertEquals("TESTPANEL1", genePanelData.getGenePanelId());
    Assertions.assertEquals("TCGA-A1-A0SB-01", genePanelData.getSampleId());
  }

  @Test
  public void getGenesOfPanels() {

    List<GenePanelToGene> result =
        genePanelMyBatisRepository.getGenesOfPanels(List.of("TESTPANEL1"));

    Assertions.assertEquals(3, result.size());
    GenePanelToGene genePanelToGene = result.getFirst();
    Assertions.assertEquals("TESTPANEL1", genePanelToGene.getGenePanelId());
    Assertions.assertEquals((Integer) 207, genePanelToGene.getEntrezGeneId());
    Assertions.assertEquals("AKT1", genePanelToGene.getHugoGeneSymbol());
  }
}
