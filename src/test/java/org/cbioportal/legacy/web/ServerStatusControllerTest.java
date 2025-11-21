package org.cbioportal.legacy.web;

import org.cbioportal.legacy.service.ServerStatusService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Ignore
@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@ContextConfiguration("/applicationContext-web-test.xml")
@TestConfiguration
public class ServerStatusControllerTest {

  @Autowired private WebApplicationContext wac;

  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }

  @Bean
  public static ServerStatusService serverStatusService() {
    ServerStatusService serverStatusServiceMock = Mockito.mock(ServerStatusService.class);
    return serverStatusServiceMock;
  }
}
