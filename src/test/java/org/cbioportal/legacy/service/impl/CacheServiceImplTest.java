package org.cbioportal.legacy.service.impl;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.cbioportal.legacy.model.CancerStudy;
import org.cbioportal.legacy.persistence.StudyRepository;
import org.cbioportal.legacy.persistence.cachemaputil.StaticRefCacheMapUtil;
import org.cbioportal.legacy.persistence.util.CacheUtils;
import org.cbioportal.legacy.service.exception.CacheOperationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class CacheServiceImplTest {

  @InjectMocks private CacheServiceImpl cachingService;

  @Mock private CacheManager cacheManager;

  @Mock private StaticRefCacheMapUtil cacheMapUtil;

  @Mock private CacheUtils cacheUtils;

  private Cache mockCache;
  private String clearAllKeysRegex = ".*";

  @Mock private StudyRepository studyRepository;

  @BeforeEach
  public void init() {
    when(cacheManager.getCacheNames()).thenReturn(Arrays.asList("name_1", "name_2"));
    CancerStudy cancerStudy1 = mock(CancerStudy.class);
    when(cancerStudy1.getCancerStudyIdentifier()).thenReturn("study1");
    CancerStudy cancerStudy2 = mock(CancerStudy.class);
    when(cancerStudy2.getCancerStudyIdentifier()).thenReturn("study2");
    List<CancerStudy> studies = Arrays.asList(cancerStudy1, cancerStudy2);
    when(studyRepository.getAllStudies(
            nullable(String.class),
            nullable(String.class),
            nullable(Integer.class),
            nullable(Integer.class),
            nullable(String.class),
            nullable(String.class)))
        .thenReturn(studies);
  }

  @Test
  public void evictAllCachesSuccess() throws Exception {
    cachingService.clearCaches(true);
    verify(cacheUtils, times(2)).evictByPattern(anyString(), eq(clearAllKeysRegex));
    verify(cacheMapUtil, times(1)).initializeCacheMemory();
  }

  @Test
  public void evictAllCachesNullManager() throws Exception {
    ReflectionTestUtils.setField(cachingService, "cacheManager", null);
    cachingService.clearCaches(true);
    verify(cacheUtils, never()).evictByPattern(anyString(), anyString());
    verify(cacheMapUtil, times(1)).initializeCacheMemory();
    ReflectionTestUtils.setField(cachingService, "cacheManager", cacheManager);
  }

  @Test
  public void evictAllCachesSkipSpringManagedCache() throws Exception {
    cachingService.clearCaches(false);
    verify(cacheUtils, never()).evictByPattern(anyString(), anyString());
    verify(cacheMapUtil, times(1)).initializeCacheMemory();
  }

  @Test
  public void evictAllCachesThrowsException() {
    doThrow(RuntimeException.class).when(cacheUtils).evictByPattern(anyString(), anyString());
    Assertions.assertThrows(CacheOperationException.class, () -> 
        cachingService.clearCaches(true));
  }

  @Test
  public void evictCacheForStudySuccess() throws Exception {
    cachingService.clearCachesForStudy("study3", true);
    verify(cacheUtils, times(2))
        .evictByPattern(anyString(), eq("^(?=.*study3).*|^(?!.*study3)(?!.*study1)(?!.*study2).*"));
    verify(cacheMapUtil, times(1)).initializeCacheMemory();
  }

  @Test
  public void evictCacheForStudyNullManager() throws Exception {
    ReflectionTestUtils.setField(cachingService, "cacheManager", null);
    cachingService.clearCachesForStudy("study3", true);
    verify(cacheUtils, never()).evictByPattern(anyString(), anyString());
    verify(cacheMapUtil, times(1)).initializeCacheMemory();
    ReflectionTestUtils.setField(cachingService, "cacheManager", cacheManager);
  }

  @Test
  public void evictCacheForStudySkipSpringManagedCache() throws Exception {
    cachingService.clearCachesForStudy("study3", false);
    verify(cacheUtils, never()).evictByPattern(anyString(), anyString());
    verify(cacheMapUtil, times(1)).initializeCacheMemory();
  }

  @Test
  public void evictCacheForStudyThrowsException() {
    doThrow(RuntimeException.class).when(cacheUtils).evictByPattern(anyString(), anyString());
    Assertions.assertThrows(CacheOperationException.class, () -> 
        cachingService.clearCachesForStudy("study3", true));
  }
}
