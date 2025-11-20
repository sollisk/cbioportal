package org.cbioportal.application.file.export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.cbioportal.application.file.model.MafRecord;
import org.junit.jupiter.api.Test;

public class ToRowTests {
  @Test
  public void testMafRowToRow() {
    var mafRecord = new MafRecord();
    assertNotNull(mafRecord.toRow());
    assertEquals(37, mafRecord.toRow().size());
  }
}
