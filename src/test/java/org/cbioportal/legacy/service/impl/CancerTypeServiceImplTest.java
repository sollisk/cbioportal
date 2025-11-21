package org.cbioportal.legacy.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.cbioportal.legacy.model.TypeOfCancer;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.CancerTypeRepository;
import org.cbioportal.legacy.service.exception.CancerTypeNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CancerTypeServiceImplTest extends BaseServiceImplTest {

  @InjectMocks private CancerTypeServiceImpl cancerTypeService;

  @Mock private CancerTypeRepository cancerTypeRepository;

  @Test
  public void getAllCancerTypes() {

    List<TypeOfCancer> expectedTypeOfCancerList = new ArrayList<>();
    TypeOfCancer typeOfCancer = new TypeOfCancer();
    expectedTypeOfCancerList.add(typeOfCancer);

    Mockito.when(
            cancerTypeRepository.getAllCancerTypes(
                PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION))
        .thenReturn(expectedTypeOfCancerList);

    List<TypeOfCancer> result =
        cancerTypeService.getAllCancerTypes(PROJECTION, PAGE_SIZE, PAGE_NUMBER, SORT, DIRECTION);

    Assertions.assertEquals(expectedTypeOfCancerList, result);
  }

  @Test
  public void getMetaCancerTypes() {

    BaseMeta expectedBaseMeta = new BaseMeta();

    Mockito.when(cancerTypeRepository.getMetaCancerTypes()).thenReturn(expectedBaseMeta);

    BaseMeta result = cancerTypeService.getMetaCancerTypes();

    Assertions.assertEquals(expectedBaseMeta, result);
  }

  @Test
  public void getCancerTypeNotFound() {

    Mockito.when(cancerTypeRepository.getCancerType(CANCER_TYPE_ID)).thenReturn(null);

    Assertions.assertThrows(CancerTypeNotFoundException.class, () -> 
        cancerTypeService.getCancerType(CANCER_TYPE_ID));
  }

  @Test
  public void getCancerType() throws Exception {

    TypeOfCancer expectedTypeOfCancer = new TypeOfCancer();

    Mockito.when(cancerTypeRepository.getCancerType(CANCER_TYPE_ID))
        .thenReturn(expectedTypeOfCancer);

    TypeOfCancer result = cancerTypeService.getCancerType(CANCER_TYPE_ID);

    Assertions.assertEquals(expectedTypeOfCancer, result);
  }
}
