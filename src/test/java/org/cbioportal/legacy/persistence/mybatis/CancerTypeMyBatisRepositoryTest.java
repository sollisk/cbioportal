package org.cbioportal.legacy.persistence.mybatis;

import java.util.List;
import org.cbioportal.legacy.model.TypeOfCancer;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.mybatis.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {CancerTypeMyBatisRepository.class, TestConfig.class})
public class CancerTypeMyBatisRepositoryTest {

  @Autowired private CancerTypeMyBatisRepository cancerTypeMyBatisRepository;

  @Test
  public void getAllCancerTypesIdProjection() throws Exception {

    List<TypeOfCancer> result =
        cancerTypeMyBatisRepository.getAllCancerTypes("ID", null, null, null, null);

    Assertions.assertEquals(2, result.size());
    TypeOfCancer typeOfCancer = result.get(0);
    Assertions.assertEquals("acc", typeOfCancer.getTypeOfCancerId());
  }

  @Test
  public void getAllCancerTypesSummaryProjection() throws Exception {

    List<TypeOfCancer> result =
        cancerTypeMyBatisRepository.getAllCancerTypes("SUMMARY", null, null, null, null);

    Assertions.assertEquals(2, result.size());
    TypeOfCancer typeOfCancer = result.get(0);
    Assertions.assertEquals("brca", typeOfCancer.getTypeOfCancerId());
    Assertions.assertEquals("Breast Invasive Carcinoma", typeOfCancer.getName());
    Assertions.assertEquals("HotPink", typeOfCancer.getDedicatedColor());
    Assertions.assertEquals("Breast", typeOfCancer.getShortName());
    Assertions.assertEquals("tissue", typeOfCancer.getParent());
  }

  @Test
  public void getAllCancerTypesDetailedProjection() throws Exception {

    List<TypeOfCancer> result =
        cancerTypeMyBatisRepository.getAllCancerTypes("DETAILED", null, null, null, null);

    Assertions.assertEquals(2, result.size());
    TypeOfCancer typeOfCancer = result.get(0);
    Assertions.assertEquals("brca", typeOfCancer.getTypeOfCancerId());
    Assertions.assertEquals("Breast Invasive Carcinoma", typeOfCancer.getName());
    Assertions.assertEquals("HotPink", typeOfCancer.getDedicatedColor());
    Assertions.assertEquals("Breast", typeOfCancer.getShortName());
    Assertions.assertEquals("tissue", typeOfCancer.getParent());
  }

  @Test
  public void getAllCancerTypesSummaryProjection1PageSize() throws Exception {

    List<TypeOfCancer> result =
        cancerTypeMyBatisRepository.getAllCancerTypes("SUMMARY", 1, 0, null, null);

    Assertions.assertEquals(1, result.size());
  }

  @Test
  public void getAllCancerTypesSummaryProjectionNameSort() throws Exception {

    List<TypeOfCancer> result =
        cancerTypeMyBatisRepository.getAllCancerTypes("SUMMARY", null, null, "name", "ASC");

    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("Adrenocortical Carcinoma", result.get(0).getName());
    Assertions.assertEquals("Breast Invasive Carcinoma", result.get(1).getName());
  }

  @Test
  public void getMetaCancerTypes() throws Exception {

    BaseMeta result = cancerTypeMyBatisRepository.getMetaCancerTypes();

    Assertions.assertEquals((Integer) 2, result.getTotalCount());
  }

  @Test
  public void getCancerTypeNullResult() throws Exception {

    TypeOfCancer result = cancerTypeMyBatisRepository.getCancerType("invalid_cancer_type");

    Assertions.assertNull(result);
  }

  @Test
  public void getCancerType() throws Exception {

    TypeOfCancer result = cancerTypeMyBatisRepository.getCancerType("acc");

    Assertions.assertEquals("acc", result.getTypeOfCancerId());
    Assertions.assertEquals("Adrenocortical Carcinoma", result.getName());
    Assertions.assertEquals("Purple", result.getDedicatedColor());
    Assertions.assertEquals("ACC", result.getShortName());
    Assertions.assertEquals("adrenal_gland", result.getParent());
  }
}
