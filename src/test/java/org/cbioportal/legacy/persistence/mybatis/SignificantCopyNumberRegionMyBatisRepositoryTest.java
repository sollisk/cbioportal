package org.cbioportal.legacy.persistence.mybatis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.cbioportal.legacy.model.Gistic;
import org.cbioportal.legacy.model.GisticToGene;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.mybatis.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {SignificantCopyNumberRegionMyBatisRepository.class, TestConfig.class})
public class SignificantCopyNumberRegionMyBatisRepositoryTest {

  @Autowired
  private SignificantCopyNumberRegionMyBatisRepository significantCopyNumberRegionMyBatisRepository;

  @Test
  public void getSignificantCopyNumberRegionsIdProjection() throws Exception {

    List<Gistic> result =
        significantCopyNumberRegionMyBatisRepository.getSignificantCopyNumberRegions(
            "study_tcga_pub", "ID", null, null, null, null);

    Assertions.assertEquals(2, result.size());
    Gistic gistic = result.get(0);
    Assertions.assertEquals((Long) 1L, gistic.getGisticRoiId());
    Assertions.assertEquals("study_tcga_pub", gistic.getCancerStudyId());
    Assertions.assertEquals((Integer) 1, gistic.getChromosome());
    Assertions.assertEquals("1q32.32", gistic.getCytoband());
  }

  @Test
  public void getSignificantCopyNumberRegionsSummaryProjection() throws Exception {

    List<Gistic> result =
        significantCopyNumberRegionMyBatisRepository.getSignificantCopyNumberRegions(
            "study_tcga_pub", "SUMMARY", null, null, null, null);

    Assertions.assertEquals(2, result.size());
    Gistic gistic = result.get(0);
    Assertions.assertEquals((Long) 1L, gistic.getGisticRoiId());
    Assertions.assertEquals("study_tcga_pub", gistic.getCancerStudyId());
    Assertions.assertEquals((Integer) 1, gistic.getChromosome());
    Assertions.assertEquals("1q32.32", gistic.getCytoband());
    Assertions.assertEquals((Integer) 123, gistic.getWidePeakStart());
    Assertions.assertEquals((Integer) 136, gistic.getWidePeakEnd());
    Assertions.assertEquals(new BigDecimal("0.0208839997649193"), gistic.getqValue());
    Assertions.assertEquals(false, gistic.getAmp());
  }

  @Test
  public void getSignificantCopyNumberRegionsDetailedProjection() throws Exception {

    List<Gistic> result =
        significantCopyNumberRegionMyBatisRepository.getSignificantCopyNumberRegions(
            "study_tcga_pub", "DETAILED", null, null, null, null);

    Assertions.assertEquals(2, result.size());
    Gistic gistic = result.get(0);
    Assertions.assertEquals((Long) 1L, gistic.getGisticRoiId());
    Assertions.assertEquals("study_tcga_pub", gistic.getCancerStudyId());
    Assertions.assertEquals((Integer) 1, gistic.getChromosome());
    Assertions.assertEquals("1q32.32", gistic.getCytoband());
    Assertions.assertEquals((Integer) 123, gistic.getWidePeakStart());
    Assertions.assertEquals((Integer) 136, gistic.getWidePeakEnd());
    Assertions.assertEquals(new BigDecimal("0.0208839997649193"), gistic.getqValue());
    Assertions.assertEquals(false, gistic.getAmp());
  }

  @Test
  public void getSignificantCopyNumberRegionsSummaryProjection1PageSize() throws Exception {

    List<Gistic> result =
        significantCopyNumberRegionMyBatisRepository.getSignificantCopyNumberRegions(
            "study_tcga_pub", "SUMMARY", 1, 0, null, null);

    Assertions.assertEquals(1, result.size());
  }

  @Test
  public void getSignificantCopyNumberRegionsSummaryProjectionQValueSort() throws Exception {

    List<Gistic> result =
        significantCopyNumberRegionMyBatisRepository.getSignificantCopyNumberRegions(
            "study_tcga_pub", "SUMMARY", null, null, "qValue", "ASC");

    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(new BigDecimal("0.000323799991747364"), result.get(0).getqValue());
    Assertions.assertEquals(new BigDecimal("0.0208839997649193"), result.get(1).getqValue());
  }

  @Test
  public void getMetaSignificantCopyNumberRegions() throws Exception {

    BaseMeta result =
        significantCopyNumberRegionMyBatisRepository.getMetaSignificantCopyNumberRegions(
            "study_tcga_pub");

    Assertions.assertEquals((Integer) 2, result.getTotalCount());
  }

  @Test
  public void getGenesOfRegions() throws Exception {

    List<Long> gisticRoiIds = new ArrayList<>();
    gisticRoiIds.add(1L);
    gisticRoiIds.add(2L);
    List<GisticToGene> result =
        significantCopyNumberRegionMyBatisRepository.getGenesOfRegions(gisticRoiIds);

    Assertions.assertEquals(3, result.size());
    GisticToGene gisticToGene1 = result.get(0);
    Assertions.assertEquals((Integer) 207, gisticToGene1.getEntrezGeneId());
    Assertions.assertEquals("AKT1", gisticToGene1.getHugoGeneSymbol());
    GisticToGene gisticToGene2 = result.get(1);
    Assertions.assertEquals((Integer) 208, gisticToGene2.getEntrezGeneId());
    Assertions.assertEquals("AKT2", gisticToGene2.getHugoGeneSymbol());
    GisticToGene gisticToGene3 = result.get(2);
    Assertions.assertEquals((Integer) 207, gisticToGene3.getEntrezGeneId());
    Assertions.assertEquals("AKT1", gisticToGene3.getHugoGeneSymbol());
  }
}
