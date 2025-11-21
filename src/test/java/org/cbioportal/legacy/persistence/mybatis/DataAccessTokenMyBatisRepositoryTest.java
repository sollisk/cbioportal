package org.cbioportal.legacy.persistence.mybatis;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.cbioportal.legacy.model.DataAccessToken;
import org.cbioportal.legacy.persistence.mybatis.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {DataAccessTokenMyBatisRepository.class, TestConfig.class})
public class DataAccessTokenMyBatisRepositoryTest {

  @Autowired private DataAccessTokenMyBatisRepository dataAccessTokenMyBatisRepository;

  @Test
  public void getAllDataAccessTokensForUsername() {
    List<DataAccessToken> dataAccessTokensForMockEmail =
        dataAccessTokenMyBatisRepository.getAllDataAccessTokensForUsername("mockemail@email.com");
    Assertions.assertEquals(1, dataAccessTokensForMockEmail.size());
    List<DataAccessToken> dataAccessTokensForMockEmail3 =
        dataAccessTokenMyBatisRepository.getAllDataAccessTokensForUsername("mockemail3@email.com");
    Assertions.assertEquals(3, dataAccessTokensForMockEmail3.size());
    for (DataAccessToken dataAccessToken : dataAccessTokensForMockEmail3) {
      Assertions.assertEquals("mockemail3@email.com", dataAccessToken.getUsername());
    }
  }

  @Test
  public void getDataAccessToken() {
    DataAccessToken dataAccessToken =
        dataAccessTokenMyBatisRepository.getDataAccessToken("6c9a641e-9719-fake-data-f17e089b37e8");
    Assertions.assertEquals("6c9a641e-9719-fake-data-f17e089b37e8", dataAccessToken.getToken());
    Assertions.assertEquals("mockemail2@email.com", dataAccessToken.getUsername());
  }

  @Test
  @Transactional
  public void addDataAccessToken() {
    String uuid = UUID.randomUUID().toString();
    Calendar calendar = Calendar.getInstance();
    Date creationDate = calendar.getTime();
    calendar.add(Calendar.SECOND, 1000);
    Date expirationDate = calendar.getTime();

    DataAccessToken dataAccessToken =
        new DataAccessToken(uuid, "mockemail2@email.com", expirationDate, creationDate);
    dataAccessTokenMyBatisRepository.addDataAccessToken(dataAccessToken);

    DataAccessToken newDataAccessToken = dataAccessTokenMyBatisRepository.getDataAccessToken(uuid);
    Assertions.assertEquals(uuid, newDataAccessToken.getToken());
    Assertions.assertEquals("mockemail2@email.com", newDataAccessToken.getUsername());
    Assertions.assertEquals(creationDate, newDataAccessToken.getCreation());
    Assertions.assertEquals(expirationDate, newDataAccessToken.getExpiration());
  }

  @Test
  @Transactional
  public void removeDataAccessToken() {
    dataAccessTokenMyBatisRepository.removeDataAccessToken("6c9a641e-9719-fake-data-f17e089b37e8");
    List<DataAccessToken> dataAccessTokensForMockEmail2 =
        dataAccessTokenMyBatisRepository.getAllDataAccessTokensForUsername("mockemail2@email.com");
    Assertions.assertEquals(0, dataAccessTokensForMockEmail2.size());
  }

  @Test
  @Transactional
  public void removeAllDataAccessTokensForUsername() {
    List<DataAccessToken> dataAccessTokensForMockEmail4 =
        dataAccessTokenMyBatisRepository.getAllDataAccessTokensForUsername("mockemail4@email.com");
    Assertions.assertEquals(3, dataAccessTokensForMockEmail4.size());
    dataAccessTokenMyBatisRepository.removeAllDataAccessTokensForUsername("mockemail4@email.com");
    List<DataAccessToken> dataAccessTokensForMockEmail4AfterDeletion =
        dataAccessTokenMyBatisRepository.getAllDataAccessTokensForUsername("mockemail4@email.com");
    Assertions.assertEquals(0, dataAccessTokensForMockEmail4AfterDeletion.size());
  }
}
