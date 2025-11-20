package org.cbioportal.legacy.web;

import java.util.HashMap;
import org.cbioportal.legacy.service.StaticDataTimestampService;
import org.cbioportal.legacy.web.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
@ContextConfiguration(classes = {StaticDataTimestampController.class, TestConfig.class})
public class StaticDataTimestampControllerTest {

  @MockBean private StaticDataTimestampService service;

  @Autowired private MockMvc mockMvc;

  @Test
  @WithMockUser
  public void getAllTimestamps() throws Exception {
    HashMap<String, String> pairs = new HashMap<>();
    pairs.put("gene", "1997-08-13 22:00:00");
    Mockito.when(service.getTimestamps(Mockito.anyList())).thenReturn(pairs);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/api/timestamps").accept(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.gene").value("1997-08-13 22:00:00"));
  }
}
