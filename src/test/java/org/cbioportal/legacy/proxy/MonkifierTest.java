package org.cbioportal.legacy.proxy;

import java.util.HashMap;
import java.util.Map;
import org.cbioportal.application.proxy.Monkifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MonkifierTest {
  private Monkifier monkifier = new Monkifier();

  @Test
  public void decodeBase64() {
    String encoded = "aW5jbHVkZUV2aWRlbmNl";
    String decoded = this.monkifier.decodeBase64(encoded);
    Assertions.assertEquals("includeEvidence", decoded);
  }

  @Test
  public void encodeBase64() {
    String plain = "13:g.32914438del";
    String encoded = this.monkifier.encodeBase64(plain);
    Assertions.assertEquals("MTM6Zy4zMjkxNDQzOGRlbA==", encoded);
  }

  @Test
  public void decodeQueryString() {
    // no param
    Map<String, String[]> encodedQueryParamsEmpty = new HashMap<>();
    String decodedQueryString1 = monkifier.decodeQueryString(encodedQueryParamsEmpty);
    Assertions.assertEquals("", decodedQueryString1);

    // single param
    Map<String, String[]> encodedQueryParamsSingleParam = new HashMap<>();
    encodedQueryParamsSingleParam.put("aW5jbHVkZUV2aWRlbmNl", new String[] {"ZmFsc2U="});
    String decodedQueryString2 = monkifier.decodeQueryString(encodedQueryParamsSingleParam);
    Assertions.assertEquals("includeEvidence=false", decodedQueryString2);

    // multiple param
    Map<String, String[]> encodedQueryParamsMultiParam = new HashMap<>();
    encodedQueryParamsMultiParam.put("aW5jbHVkZUV2aWRlbmNl", new String[] {"ZmFsc2U="});
    encodedQueryParamsMultiParam.put("aGd2c2c=", new String[] {"MTM6Zy4zMjkxNDQzOGRlbA=="});

    String decodedQueryString3 = monkifier.decodeQueryString(encodedQueryParamsMultiParam);
    Assertions.assertEquals("hgvsg=13:g.32914438del&includeEvidence=false", decodedQueryString3);

    // multiple param with a problematic character '>'
    Map<String, String[]> encodedQueryParamsMultiParamUrlBreaker = new HashMap<>();
    encodedQueryParamsMultiParamUrlBreaker.put("cmVmZXJlbmNlR2Vub21l", new String[] {"R1JDaDM3"});
    encodedQueryParamsMultiParamUrlBreaker.put(
        "aGd2c2c=", new String[] {"NzpnLjE0MDQ1MzEzNkE+VA=="});

    String decodedQueryString4 =
        monkifier.decodeQueryString(encodedQueryParamsMultiParamUrlBreaker);
    Assertions.assertEquals("hgvsg=7:g.140453136A%3ET&referenceGenome=GRCh37", decodedQueryString4);
  }
}
