package org.cbioportal.legacy.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.cbioportal.legacy.model.TypeOfCancer;
import org.cbioportal.legacy.persistence.CancerTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServerStatusServiceImplTest extends BaseServiceImplTest {

  @InjectMocks private ServerStatusServiceImpl serverStatusService;

  @Mock private CancerTypeRepository cancerTypeRepository;

  @Test
  public void getServerStatusSuccess() throws Exception {

    List<TypeOfCancer> cancerList = new ArrayList<>();
    TypeOfCancer typeOfCancer = new TypeOfCancer();
    cancerList.add(typeOfCancer);

    Mockito.when(cancerTypeRepository.getAllCancerTypes("SUMMARY", null, null, null, null))
        .thenReturn(cancerList);

    Assertions.assertEquals(
        ServerStatusServiceImpl.MESSAGE_RUNNING, serverStatusService.getServerStatus().status);
  }

  @Test
  public void getServerStatusFailure() throws Exception {

    List<TypeOfCancer> cancerList = new ArrayList<>();

    Mockito.when(cancerTypeRepository.getAllCancerTypes("SUMMARY", null, null, null, null))
        .thenReturn(cancerList);

    Assertions.assertEquals(
        ServerStatusServiceImpl.MESSAGE_DOWN, serverStatusService.getServerStatus().status);
  }
}
