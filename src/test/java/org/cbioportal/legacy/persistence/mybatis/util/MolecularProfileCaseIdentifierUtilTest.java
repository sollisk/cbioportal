package org.cbioportal.legacy.persistence.mybatis.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MolecularProfileCaseIdentifierUtilTest {
  @InjectMocks private MolecularProfileCaseIdentifierUtil molecularProfileCaseIdentifierUtil;

  @Test
  public void getGroupedCasesByMolecularProfileId() {

    final String MOLECULAR_PROFILE_ID_1 = "molecular_profile_id_1";
    final String MOLECULAR_PROFILE_ID_2 = "molecular_profile_id_2";
    final String SAMPLE_ID_1 = "sample_id_1";
    final String SAMPLE_ID_2 = "sample_id_2";
    final String SAMPLE_ID_3 = "sample_id_3";

    Map<String, Set<String>> result =
        molecularProfileCaseIdentifierUtil.getGroupedCasesByMolecularProfileId(
            new ArrayList<>(), new ArrayList<>());
    Assertions.assertEquals(0, result.size(),"empty request");

    result =
        molecularProfileCaseIdentifierUtil.getGroupedCasesByMolecularProfileId(
            Arrays.asList(MOLECULAR_PROFILE_ID_1), new ArrayList<>());
    Assertions.assertEquals(1, result.size(),"empty sample ids");
    Assertions.assertEquals(0, result.get(MOLECULAR_PROFILE_ID_1).size());

    result =
        molecularProfileCaseIdentifierUtil.getGroupedCasesByMolecularProfileId(
            Arrays.asList(MOLECULAR_PROFILE_ID_1, MOLECULAR_PROFILE_ID_1, MOLECULAR_PROFILE_ID_1),
            Arrays.asList(SAMPLE_ID_1, SAMPLE_ID_2, SAMPLE_ID_3));

    Assertions.assertEquals(1, result.size(),"valid - single profile");

    result =
        molecularProfileCaseIdentifierUtil.getGroupedCasesByMolecularProfileId(
            Arrays.asList(MOLECULAR_PROFILE_ID_1, MOLECULAR_PROFILE_ID_1, MOLECULAR_PROFILE_ID_2),
            Arrays.asList(SAMPLE_ID_1, SAMPLE_ID_2, SAMPLE_ID_3));

    Assertions.assertEquals(2, result.size(),"valid - multiple profiles");
    Assertions.assertEquals(2, result.get(MOLECULAR_PROFILE_ID_1).size());
    Assertions.assertEquals(1, result.get(MOLECULAR_PROFILE_ID_2).size());
  }
}
