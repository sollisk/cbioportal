package org.cbioportal.domain.clinical_data.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.cbioportal.domain.clinical_attributes.util.ClinicalAttributeUtil;
import org.cbioportal.legacy.model.ClinicalAttribute;
import org.junit.jupiter.api.Test;

public class ClinicalAttributeUtilTest {

  @Test
  public void testCategorizeClinicalAttributes() {
    // Create test clinical attributes using existing data
    List<ClinicalAttribute> clinicalAttributes =
        Arrays.asList(
            // Sample-only attributes
            createClinicalAttribute("mutation_count", false), // sample-level only
            createClinicalAttribute("days_to_collection", false), // sample-level only

            // Patient-only attributes
            createClinicalAttribute("age", true), // patient-level only
            createClinicalAttribute("center", true), // patient-level only
            createClinicalAttribute("dead", true), // patient-level only

            // Conflicting attributes (same ID, different patient_attribute values)
            createClinicalAttribute("subtype", false), // sample-level subtype
            createClinicalAttribute("subtype", true) // patient-level subtype
            );

    var result = ClinicalAttributeUtil.categorizeClinicalAttributes(clinicalAttributes);

    // Verify sample attributes
    assertEquals(2, result.sampleAttributeIds().size(),"Should have 2 sample attributes");
    assertTrue(result.sampleAttributeIds().contains("mutation_count"),"Should contain mutation_count");
    assertTrue(result.sampleAttributeIds().contains("days_to_collection"),"Should contain days_to_collection");

    // Verify patient attributes
    assertEquals(3, result.patientAttributeIds().size(),"Should have 3 patient attributes");
    assertTrue(result.patientAttributeIds().contains("age"),"Should contain age");
    assertTrue(result.patientAttributeIds().contains("center"), "Should contain center");
    assertTrue(result.patientAttributeIds().contains("dead"),"Should contain dead");

    // Verify conflicting attributes
    assertEquals(1, result.conflictingAttributeIds().size(),"Should have 1 conflicting attribute");
    assertTrue(result.conflictingAttributeIds().contains("subtype"),"Should contain subtype");
  }

  @Test
  public void testCategorizeClinicalAttributesEmptyList() {
    // Test with empty input
    var result = ClinicalAttributeUtil.categorizeClinicalAttributes(List.of());

    assertTrue(result.sampleAttributeIds().isEmpty(),"Sample attributes should be empty");
    assertTrue(result.patientAttributeIds().isEmpty(),"Patient attributes should be empty");
    assertTrue(result.conflictingAttributeIds().isEmpty(),"Conflicting attributes should be empty");
  }

  @Test
  public void testCategorizeClinicalAttributesOnlySampleAttributes() {
    // Test with only sample attributes
    List<ClinicalAttribute> clinicalAttributes =
        Arrays.asList(
            createClinicalAttribute("mutation_count", false),
            createClinicalAttribute("sample_type", false),
            createClinicalAttribute("is_ffpe", false));

    var result = ClinicalAttributeUtil.categorizeClinicalAttributes(clinicalAttributes);

    assertEquals(3, result.sampleAttributeIds().size(),"Should have 3 sample attributes");
    assertTrue(result.patientAttributeIds().isEmpty(),"Patient attributes should be empty");
    assertTrue(result.conflictingAttributeIds().isEmpty(),"Conflicting attributes should be empty");
  }

  @Test
  public void testCategorizeClinicalAttributesOnlyPatientAttributes() {
    // Test with only patient attributes
    List<ClinicalAttribute> clinicalAttributes =
        Arrays.asList(
            createClinicalAttribute("age", true),
            createClinicalAttribute("os_months", true),
            createClinicalAttribute("dfs_status", true));

    var result = ClinicalAttributeUtil.categorizeClinicalAttributes(clinicalAttributes);

    assertTrue(result.sampleAttributeIds().isEmpty(),"Sample attributes should be empty");
    assertEquals(3, result.patientAttributeIds().size(),"Should have 3 patient attributes");
    assertTrue(result.conflictingAttributeIds().isEmpty(),"Conflicting attributes should be empty");
  }

  @Test
  public void testCategorizeClinicalAttributesMultipleConflictingAttributes() {
    // Test with multiple conflicting attributes
    List<ClinicalAttribute> clinicalAttributes =
        Arrays.asList(
            // First conflicting attribute
            createClinicalAttribute("subtype", false),
            createClinicalAttribute("subtype", true),

            // Second conflicting attribute
            createClinicalAttribute("grade", false),
            createClinicalAttribute("grade", true),

            // Regular attributes
            createClinicalAttribute("mutation_count", false),
            createClinicalAttribute("age", true));

    var result = ClinicalAttributeUtil.categorizeClinicalAttributes(clinicalAttributes);

    assertEquals(1, result.sampleAttributeIds().size(),"Should have 1 sample attribute");
    assertEquals(1, result.patientAttributeIds().size(),"Should have 1 patient attribute");
    assertEquals(2, result.conflictingAttributeIds().size(),"Should have 2 conflicting attributes");

    assertTrue(result.conflictingAttributeIds().contains("subtype"),"Should contain subtype in conflicting");
    assertTrue(result.conflictingAttributeIds().contains("grade"),"Should contain grade in conflicting");
  }

  @Test
  public void testCategorizeClinicalAttributesWithNullPatientAttribute() {
    // Test edge case where patientAttribute is null (should be treated as false)
    List<ClinicalAttribute> clinicalAttributes =
        Arrays.asList(
            createClinicalAttributeWithNullPatientAttribute("sample_attr"),
            createClinicalAttribute("age", true));

    var result = ClinicalAttributeUtil.categorizeClinicalAttributes(clinicalAttributes);

    assertEquals(1, result.sampleAttributeIds().size(),"Should have 1 sample attribute");
    assertEquals(1, result.patientAttributeIds().size(),"Should have 1 patient attribute");
    assertTrue(result.conflictingAttributeIds().isEmpty(),"Conflicting attributes should be empty");
  }

  // Helper method to create ClinicalAttribute for testing
  private ClinicalAttribute createClinicalAttribute(
      String attributeId, boolean isPatientAttribute) {
    ClinicalAttribute attribute = new ClinicalAttribute();
    attribute.setAttrId(attributeId);
    attribute.setPatientAttribute(isPatientAttribute);
    return attribute;
  }

  // Helper method to create ClinicalAttribute with null patientAttribute
  private ClinicalAttribute createClinicalAttributeWithNullPatientAttribute(String attributeId) {
    ClinicalAttribute attribute = new ClinicalAttribute();
    attribute.setAttrId(attributeId);
    attribute.setPatientAttribute(null);
    return attribute;
  }
}
