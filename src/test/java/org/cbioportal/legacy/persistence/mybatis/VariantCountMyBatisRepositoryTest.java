package org.cbioportal.legacy.persistence.mybatis;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.cbioportal.legacy.model.VariantCount;
import org.cbioportal.legacy.persistence.mybatis.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {VariantCountMyBatisRepository.class, TestConfig.class})
public class VariantCountMyBatisRepositoryTest {

  @Autowired private VariantCountMyBatisRepository variantCountMyBatisRepository;

  @Test
  public void fetchVariantCounts() throws Exception {

    List<VariantCount> result =
        variantCountMyBatisRepository.fetchVariantCounts(
            "study_tcga_pub_mutations",
            Arrays.asList(207, 207, 369),
            Arrays.asList("AKT1 truncating", null, "ARAF G1513 missense"));

    Assertions.assertEquals(3, result.size());

    Optional<VariantCount> variantCountOptional =
        result.stream().filter(r -> r.getKeyword() == null).findAny();
    Assertions.assertTrue(variantCountOptional.isPresent());
    VariantCount variantCount1 = variantCountOptional.get();

    Assertions.assertEquals("study_tcga_pub_mutations", variantCount1.getMolecularProfileId());
    Assertions.assertEquals((Integer) 207, variantCount1.getEntrezGeneId());
    Assertions.assertEquals((Integer) 21, variantCount1.getNumberOfSamplesWithKeyword());
    Assertions.assertEquals((Integer) 22, variantCount1.getNumberOfSamplesWithMutationInGene());

    variantCountOptional =
        result.stream().filter(r -> r.getKeyword().equals("AKT1 truncating")).findAny();
    Assertions.assertTrue(variantCountOptional.isPresent());
    VariantCount variantCount2 = variantCountOptional.get();

    Assertions.assertEquals("study_tcga_pub_mutations", variantCount2.getMolecularProfileId());
    Assertions.assertEquals((Integer) 207, variantCount2.getEntrezGeneId());
    Assertions.assertEquals((Integer) 54, variantCount2.getNumberOfSamplesWithKeyword());
    Assertions.assertEquals((Integer) 64, variantCount2.getNumberOfSamplesWithMutationInGene());
  }
}
