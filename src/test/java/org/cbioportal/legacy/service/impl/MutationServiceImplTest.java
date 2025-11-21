package org.cbioportal.legacy.service.impl;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.cbioportal.legacy.model.Gene;
import org.cbioportal.legacy.model.GeneFilterQuery;
import org.cbioportal.legacy.model.GenomicDataCount;
import org.cbioportal.legacy.model.GenomicDataCountItem;
import org.cbioportal.legacy.model.MolecularProfile;
import org.cbioportal.legacy.model.Mutation;
import org.cbioportal.legacy.model.MutationCountByPosition;
import org.cbioportal.legacy.model.MutationEventType;
import org.cbioportal.legacy.model.meta.MutationMeta;
import org.cbioportal.legacy.persistence.MutationRepository;
import org.cbioportal.legacy.service.MolecularProfileService;
import org.cbioportal.legacy.service.exception.MolecularProfileNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MutationServiceImplTest extends BaseServiceImplTest {

  @InjectMocks private MutationServiceImpl mutationService;

  @Mock private MutationRepository mutationRepository;
  @Mock private MolecularProfileService molecularProfileService;

  @Test
  public void getMutationsInMolecularProfileBySampleListId() throws Exception {

    MolecularProfile molecularProfile = new MolecularProfile();
    molecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.MUTATION_EXTENDED);
    Mockito.when(molecularProfileService.getMolecularProfile(MOLECULAR_PROFILE_ID))
        .thenReturn(molecularProfile);

    List<Mutation> expectedMutationList = new ArrayList<>();
    Mutation mutation = new Mutation();
    Gene gene = new Gene();
    mutation.setGene(gene);
    mutation.setChr("19");
    expectedMutationList.add(mutation);

    Mockito.when(
            mutationRepository.getMutationsInMolecularProfileBySampleListId(
                MOLECULAR_PROFILE_ID,
                SAMPLE_LIST_ID,
                List.of(ENTREZ_GENE_ID_1),
                false,
                PROJECTION,
                PAGE_SIZE,
                PAGE_NUMBER,
                SORT,
                DIRECTION))
        .thenReturn(expectedMutationList);

    List<Mutation> result =
        mutationService.getMutationsInMolecularProfileBySampleListId(
            MOLECULAR_PROFILE_ID,
            SAMPLE_LIST_ID,
            List.of(ENTREZ_GENE_ID_1),
            false,
            PROJECTION,
            PAGE_SIZE,
            PAGE_NUMBER,
            SORT,
            DIRECTION);

    Assertions.assertEquals(expectedMutationList, result);
    Assertions.assertEquals("19", result.getFirst().getChr());
  }

  @Test
  public void getMutationsInMolecularProfileBySampleListIdMolecularProfileNotFound()
      throws Exception {

    Mockito.when(molecularProfileService.getMolecularProfile(MOLECULAR_PROFILE_ID))
        .thenThrow(new MolecularProfileNotFoundException(MOLECULAR_PROFILE_ID));
    Assertions.assertThrows(MolecularProfileNotFoundException.class, () -> 
        mutationService.getMutationsInMolecularProfileBySampleListId(
            MOLECULAR_PROFILE_ID,
            SAMPLE_LIST_ID,
            List.of(ENTREZ_GENE_ID_1),
            false,
            PROJECTION,
            PAGE_SIZE,
            PAGE_NUMBER,
            SORT,
            DIRECTION));
  }

  @Test
  public void getMetaMutationsInMolecularProfileBySampleListId() throws Exception {

    MolecularProfile molecularProfile = new MolecularProfile();
    molecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.MUTATION_EXTENDED);
    Mockito.when(molecularProfileService.getMolecularProfile(MOLECULAR_PROFILE_ID))
        .thenReturn(molecularProfile);

    MutationMeta expectedMutationMeta = new MutationMeta();
    Mockito.when(
            mutationRepository.getMetaMutationsInMolecularProfileBySampleListId(
                MOLECULAR_PROFILE_ID, SAMPLE_LIST_ID, List.of(ENTREZ_GENE_ID_1)))
        .thenReturn(expectedMutationMeta);
    MutationMeta result =
        mutationService.getMetaMutationsInMolecularProfileBySampleListId(
            MOLECULAR_PROFILE_ID, SAMPLE_LIST_ID, List.of(ENTREZ_GENE_ID_1));

    Assertions.assertEquals(expectedMutationMeta, result);
  }

  @Test
  public void getMetaMutationsInMolecularProfileBySampleListIdMolecularProfileNotFound()
      throws Exception {

    Mockito.when(molecularProfileService.getMolecularProfile(MOLECULAR_PROFILE_ID))
        .thenThrow(new MolecularProfileNotFoundException(MOLECULAR_PROFILE_ID));
    Assertions.assertThrows(MolecularProfileNotFoundException.class, () -> 
        mutationService.getMetaMutationsInMolecularProfileBySampleListId(
            MOLECULAR_PROFILE_ID, SAMPLE_LIST_ID, List.of(ENTREZ_GENE_ID_1)));
  }

  @Test
  public void getMutationsInMultipleMolecularProfiles() {

    List<Mutation> expectedMutationList = new ArrayList<>();
    Mutation mutation = new Mutation();
    Gene gene = new Gene();
    mutation.setGene(gene);
    mutation.setChr("19");
    expectedMutationList.add(mutation);

    Mockito.when(
            mutationRepository.getMutationsInMultipleMolecularProfiles(
                anyList(),
                anyList(),
                anyList(),
                eq(PROJECTION),
                eq(PAGE_SIZE),
                eq(PAGE_NUMBER),
                eq(SORT),
                eq(DIRECTION)))
        .thenReturn(expectedMutationList);

    List<Mutation> result =
        mutationService.getMutationsInMultipleMolecularProfiles(
            List.of(MOLECULAR_PROFILE_ID),
            List.of(SAMPLE_ID1),
            List.of(ENTREZ_GENE_ID_1),
            PROJECTION,
            PAGE_SIZE,
            PAGE_NUMBER,
            SORT,
            DIRECTION);

    Assertions.assertEquals(expectedMutationList, result);
    Assertions.assertEquals("19", result.getFirst().getChr());
  }

  @Test
  public void getMutationsInMultipleMolecularProfilesByGeneQueries() {

    List<Mutation> expectedMutationList = new ArrayList<>();
    Mutation mutation = new Mutation();
    Gene gene = new Gene();
    mutation.setGene(gene);
    mutation.setChr("19");
    expectedMutationList.add(mutation);

    GeneFilterQuery geneFilterQuery = mock(GeneFilterQuery.class);

    Mockito.when(
            mutationRepository.getMutationsInMultipleMolecularProfilesByGeneQueries(
                anyList(),
                anyList(),
                anyList(),
                eq(PROJECTION),
                eq(PAGE_SIZE),
                eq(PAGE_NUMBER),
                eq(SORT),
                eq(DIRECTION)))
        .thenReturn(expectedMutationList);

    List<Mutation> result =
        mutationService.getMutationsInMultipleMolecularProfilesByGeneQueries(
            List.of(MOLECULAR_PROFILE_ID),
            List.of(SAMPLE_ID1),
            Collections.singletonList(geneFilterQuery),
            PROJECTION,
            PAGE_SIZE,
            PAGE_NUMBER,
            SORT,
            DIRECTION);

    Assertions.assertEquals(expectedMutationList, result);
    Assertions.assertEquals("19", result.get(0).getChr());
  }

  @Test
  public void getMetaMutationsInMultipleMolecularProfiles() {

    MutationMeta expectedMutationMeta = new MutationMeta();
    Mockito.when(
            mutationRepository.getMetaMutationsInMultipleMolecularProfiles(
                List.of(MOLECULAR_PROFILE_ID),
                List.of(SAMPLE_ID1),
                List.of(ENTREZ_GENE_ID_1)))
        .thenReturn(expectedMutationMeta);
    MutationMeta result =
        mutationService.getMetaMutationsInMultipleMolecularProfiles(
            List.of(MOLECULAR_PROFILE_ID),
            List.of(SAMPLE_ID1),
            List.of(ENTREZ_GENE_ID_1));

    Assertions.assertEquals(expectedMutationMeta, result);
  }

  @Test
  public void fetchMutationsInMolecularProfile() throws Exception {

    MolecularProfile molecularProfile = new MolecularProfile();
    molecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.MUTATION_EXTENDED);
    Mockito.when(molecularProfileService.getMolecularProfile(MOLECULAR_PROFILE_ID))
        .thenReturn(molecularProfile);

    List<Mutation> expectedMutationList = new ArrayList<>();
    Mutation mutation = new Mutation();
    Gene gene = new Gene();
    mutation.setGene(gene);
    mutation.setChr("19");
    expectedMutationList.add(mutation);

    Mockito.when(
            mutationRepository.fetchMutationsInMolecularProfile(
                MOLECULAR_PROFILE_ID,
                List.of(SAMPLE_ID1),
                List.of(ENTREZ_GENE_ID_1),
                false,
                PROJECTION,
                PAGE_SIZE,
                PAGE_NUMBER,
                SORT,
                DIRECTION))
        .thenReturn(expectedMutationList);

    List<Mutation> result =
        mutationService.fetchMutationsInMolecularProfile(
            MOLECULAR_PROFILE_ID,
            List.of(SAMPLE_ID1),
            List.of(ENTREZ_GENE_ID_1),
            false,
            PROJECTION,
            PAGE_SIZE,
            PAGE_NUMBER,
            SORT,
            DIRECTION);

    Assertions.assertEquals(expectedMutationList, result);
    Assertions.assertEquals("19", result.get(0).getChr());
  }

  @Test
  public void fetchMutationsInMolecularProfileNotFound() throws Exception {

    Mockito.when(molecularProfileService.getMolecularProfile(MOLECULAR_PROFILE_ID))
        .thenThrow(new MolecularProfileNotFoundException(MOLECULAR_PROFILE_ID));
    Assertions.assertThrows(MolecularProfileNotFoundException.class, () ->
        mutationService.fetchMutationsInMolecularProfile(
            MOLECULAR_PROFILE_ID,
            List.of(SAMPLE_ID1),
            List.of(ENTREZ_GENE_ID_1),
            false,
            PROJECTION,
            PAGE_SIZE,
            PAGE_NUMBER,
            SORT,
            DIRECTION));
  }

  @Test
  public void fetchMetaMutationsInMolecularProfile() throws Exception {

    MolecularProfile molecularProfile = new MolecularProfile();
    molecularProfile.setMolecularAlterationType(
        MolecularProfile.MolecularAlterationType.MUTATION_EXTENDED);
    Mockito.when(molecularProfileService.getMolecularProfile(MOLECULAR_PROFILE_ID))
        .thenReturn(molecularProfile);

    MutationMeta expectedMutationMeta = new MutationMeta();
    Mockito.when(
            mutationRepository.fetchMetaMutationsInMolecularProfile(
                MOLECULAR_PROFILE_ID, List.of(SAMPLE_ID1), List.of(ENTREZ_GENE_ID_1)))
        .thenReturn(expectedMutationMeta);
    MutationMeta result =
        mutationService.fetchMetaMutationsInMolecularProfile(
            MOLECULAR_PROFILE_ID, List.of(SAMPLE_ID1), List.of(ENTREZ_GENE_ID_1));

    Assertions.assertEquals(expectedMutationMeta, result);
  }

  @Test
  public void fetchMetaMutationsInMolecularProfileNotFound() throws Exception {

    Mockito.when(molecularProfileService.getMolecularProfile(MOLECULAR_PROFILE_ID))
        .thenThrow(new MolecularProfileNotFoundException(MOLECULAR_PROFILE_ID));
    Assertions.assertThrows(MolecularProfileNotFoundException.class, () -> 
        mutationService.fetchMetaMutationsInMolecularProfile(
            MOLECULAR_PROFILE_ID, List.of(SAMPLE_ID1), List.of(ENTREZ_GENE_ID_1)));
  }

  @Test
  public void fetchMutationCountsByPosition() {

    MutationCountByPosition expectedMutationCountByPosition = new MutationCountByPosition();
    Mockito.when(
            mutationRepository.getMutationCountByPosition(
                ENTREZ_GENE_ID_1, PROTEIN_POS_START, PROTEIN_POS_END))
        .thenReturn(expectedMutationCountByPosition);

    List<MutationCountByPosition> result =
        mutationService.fetchMutationCountsByPosition(
            List.of(ENTREZ_GENE_ID_1),
            List.of(PROTEIN_POS_START),
            List.of(PROTEIN_POS_END));

    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(expectedMutationCountByPosition, result.getFirst());
  }

  @Test
  public void getMutationCountsByType() {
    GenomicDataCountItem expectedGenomicDataCountItem = new GenomicDataCountItem();
    expectedGenomicDataCountItem.setProfileType(PROFILE_TYPE_1);
    expectedGenomicDataCountItem.setHugoGeneSymbol(HUGO_GENE_SYMBOL_1);
    GenomicDataCount expectedGenomicDataCount = new GenomicDataCount();
    expectedGenomicDataCount.setLabel(MutationEventType.missense_mutation.getMutationType());
    expectedGenomicDataCount.setValue(MutationEventType.missense_mutation.getMutationType());
    expectedGenomicDataCount.setCount(2);
    expectedGenomicDataCount.setUniqueCount(1);
    expectedGenomicDataCountItem.setCounts(Collections.singletonList(expectedGenomicDataCount));

    Mockito.when(
            mutationRepository.getMutationCountsByType(
                Collections.singletonList(MOLECULAR_PROFILE_ID),
                Collections.singletonList(SAMPLE_ID1),
                Collections.singletonList(ENTREZ_GENE_ID_1),
                PROFILE_TYPE_1))
        .thenReturn(expectedGenomicDataCountItem);

    GenomicDataCountItem result =
        mutationService.getMutationCountsByType(
            Collections.singletonList(MOLECULAR_PROFILE_ID),
            Collections.singletonList(SAMPLE_ID1),
            Collections.singletonList(ENTREZ_GENE_ID_1),
            PROFILE_TYPE_1);

    Assertions.assertEquals(expectedGenomicDataCountItem, result);
    Assertions.assertEquals(1, result.getCounts().size());
  }
}
