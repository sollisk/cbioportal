package org.cbioportal.legacy.persistence.mybatis;

import java.util.Arrays;
import java.util.List;
import org.cbioportal.legacy.model.GenericAssayAdditionalProperty;
import org.cbioportal.legacy.model.meta.GenericAssayMeta;
import org.cbioportal.legacy.persistence.mybatis.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {GenericAssayMyBatisRepository.class, TestConfig.class})
public class GenericAssayMyBatisRepositoryTest {

  @Autowired private GenericAssayMyBatisRepository genericAssayMyBatisRepository;

  @Test
  public void getGenericAssayMeta() {
    List<String> stableIds = Arrays.asList("mean_1", "mean_2");
    List<GenericAssayMeta> result = genericAssayMyBatisRepository.getGenericAssayMeta(stableIds);
    Assertions.assertEquals(2, result.size());
  }

  @Test
  public void getGenericAssayAdditionalproperties() {
    List<String> stableIds = Arrays.asList("mean_1", "mean_2");
    List<GenericAssayAdditionalProperty> result =
        genericAssayMyBatisRepository.getGenericAssayAdditionalproperties(stableIds);
    Assertions.assertNotNull(result);
    Assertions.assertEquals(4, result.size());

    for (GenericAssayAdditionalProperty additionalProperty : result) {
      if (additionalProperty.getStableId().equals("mean_1")) {
        if (additionalProperty.getName().equals("name")) {
          Assertions.assertEquals("mean_1", additionalProperty.getValue());
        } else {
          Assertions.assertEquals("description of mean_1", additionalProperty.getValue());
        }
      } else if (additionalProperty.getStableId().equals("mean_2")) {
        if (additionalProperty.getName().equals("name")) {
          Assertions.assertEquals("mean_2", additionalProperty.getValue());
        } else {
          Assertions.assertEquals("description of mean_2", additionalProperty.getValue());
        }
      }
    }
  }
}
