package org.cbioportal.legacy.persistence.mybatis;

import java.math.BigDecimal;
import java.util.List;
import org.cbioportal.legacy.model.MutSig;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.mybatis.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {SignificantlyMutatedGeneMyBatisRepository.class, TestConfig.class})
public class SignificantlyMutatedGeneMyBatisRepositoryTest {

  @Autowired
  private SignificantlyMutatedGeneMyBatisRepository significantlyMutatedGeneMyBatisRepository;

  @Test
  public void getSignificantlyMutatedGenesIdProjection() {

    List<MutSig> result =
        significantlyMutatedGeneMyBatisRepository.getSignificantlyMutatedGenes(
            "study_tcga_pub", "ID", null, null, null, null);

    Assertions.assertEquals(2, result.size());
    MutSig mutSig = result.getFirst();
    Assertions.assertEquals((Integer) 207, mutSig.getEntrezGeneId());
  }

  @Test
  public void getSignificantlyMutatedGenesSummaryProjection() {

    List<MutSig> result =
        significantlyMutatedGeneMyBatisRepository.getSignificantlyMutatedGenes(
            "study_tcga_pub", "SUMMARY", null, null, null, null);

    Assertions.assertEquals(2, result.size());
    MutSig mutSig = result.getFirst();
    Assertions.assertEquals((Integer) 207, mutSig.getEntrezGeneId());
    Assertions.assertEquals((Integer) 1, mutSig.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", mutSig.getCancerStudyIdentifier());
    Assertions.assertEquals("AKT1", mutSig.getHugoGeneSymbol());
    Assertions.assertEquals((Integer) 998421, mutSig.getNumbasescovered());
    Assertions.assertEquals((Integer) 17, mutSig.getNummutations());
    Assertions.assertEquals((Integer) 1, mutSig.getRank());
    Assertions.assertEquals(new BigDecimal("0.00000315"), mutSig.getpValue());
    Assertions.assertEquals(new BigDecimal("0.00233"), mutSig.getqValue());
  }

  @Test
  public void getSignificantlyMutatedGenesDetailedProjection() {

    List<MutSig> result =
        significantlyMutatedGeneMyBatisRepository.getSignificantlyMutatedGenes(
            "study_tcga_pub", "DETAILED", null, null, null, null);

    Assertions.assertEquals(2, result.size());
    MutSig mutSig = result.getFirst();
    Assertions.assertEquals((Integer) 207, mutSig.getEntrezGeneId());
    Assertions.assertEquals((Integer) 1, mutSig.getCancerStudyId());
    Assertions.assertEquals("study_tcga_pub", mutSig.getCancerStudyIdentifier());
    Assertions.assertEquals("AKT1", mutSig.getHugoGeneSymbol());
    Assertions.assertEquals((Integer) 998421, mutSig.getNumbasescovered());
    Assertions.assertEquals((Integer) 17, mutSig.getNummutations());
    Assertions.assertEquals((Integer) 1, mutSig.getRank());
    Assertions.assertEquals(new BigDecimal("0.00000315"), mutSig.getpValue());
    Assertions.assertEquals(new BigDecimal("0.00233"), mutSig.getqValue());
  }

  @Test
  public void getSignificantlyMutatedGenesSummaryProjection1PageSize() {

    List<MutSig> result =
        significantlyMutatedGeneMyBatisRepository.getSignificantlyMutatedGenes(
            "study_tcga_pub", "SUMMARY", 1, 0, null, null);

    Assertions.assertEquals(1, result.size());
  }

  @Test
  public void getSignificantlyMutatedGenesSummaryProjectionPValueSort() {

    List<MutSig> result =
        significantlyMutatedGeneMyBatisRepository.getSignificantlyMutatedGenes(
            "study_tcga_pub", "SUMMARY", null, null, "pValue", "ASC");

    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals(new BigDecimal("0.000000012"), result.get(0).getpValue());
    Assertions.assertEquals(new BigDecimal("0.00000315"), result.get(1).getpValue());
  }

  @Test
  public void getMetaSignificantlyMutatedGenes() {

    BaseMeta result =
        significantlyMutatedGeneMyBatisRepository.getMetaSignificantlyMutatedGenes(
            "study_tcga_pub");

    Assertions.assertEquals((Integer) 2, result.getTotalCount());
  }
}
