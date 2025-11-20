package org.cbioportal.legacy.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.cbioportal.legacy.model.Mutation;
import org.cbioportal.legacy.model.MutationSpectrum;
import org.cbioportal.legacy.service.MutationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MutationSpectrumServiceImplTest extends BaseServiceImplTest {

  @InjectMocks private MutationSpectrumServiceImpl mutationSpectrumService;

  @Mock private MutationService mutationService;

  @Test
  public void getMutationSpectrums() throws Exception {

    List<Mutation> mutationList = createMutationList();

    Mockito.when(
            mutationService.getMutationsInMolecularProfileBySampleListId(
                MOLECULAR_PROFILE_ID,
                SAMPLE_LIST_ID,
                null,
                true,
                "SUMMARY",
                null,
                null,
                null,
                null))
        .thenReturn(mutationList);

    List<MutationSpectrum> result =
        mutationSpectrumService.getMutationSpectrums(MOLECULAR_PROFILE_ID, SAMPLE_LIST_ID);

    Assertions.assertEquals(2, result.size());
    MutationSpectrum mutationSpectrum1 = result.get(0);
    Assertions.assertEquals(MOLECULAR_PROFILE_ID, mutationSpectrum1.getMolecularProfileId());
    Assertions.assertEquals(SAMPLE_ID1, mutationSpectrum1.getSampleId());
    Assertions.assertEquals((Integer) 2, mutationSpectrum1.getCtoA());
    Assertions.assertEquals((Integer) 0, mutationSpectrum1.getCtoG());
    Assertions.assertEquals((Integer) 0, mutationSpectrum1.getCtoT());
    Assertions.assertEquals((Integer) 0, mutationSpectrum1.getTtoA());
    Assertions.assertEquals((Integer) 0, mutationSpectrum1.getTtoC());
    Assertions.assertEquals((Integer) 0, mutationSpectrum1.getTtoG());
    MutationSpectrum mutationSpectrum2 = result.get(1);
    Assertions.assertEquals(MOLECULAR_PROFILE_ID, mutationSpectrum2.getMolecularProfileId());
    Assertions.assertEquals(SAMPLE_ID2, mutationSpectrum2.getSampleId());
    Assertions.assertEquals((Integer) 0, mutationSpectrum2.getCtoA());
    Assertions.assertEquals((Integer) 0, mutationSpectrum2.getCtoG());
    Assertions.assertEquals((Integer) 1, mutationSpectrum2.getCtoT());
    Assertions.assertEquals((Integer) 1, mutationSpectrum2.getTtoA());
    Assertions.assertEquals((Integer) 0, mutationSpectrum2.getTtoC());
    Assertions.assertEquals((Integer) 0, mutationSpectrum2.getTtoG());
  }

  @Test
  public void fetchMutationSpectrums() throws Exception {

    List<Mutation> mutationList = createMutationList();

    Mockito.when(
            mutationService.fetchMutationsInMolecularProfile(
                MOLECULAR_PROFILE_ID,
                Arrays.asList(SAMPLE_ID1, SAMPLE_ID2),
                null,
                true,
                "SUMMARY",
                null,
                null,
                null,
                null))
        .thenReturn(mutationList);

    List<MutationSpectrum> result =
        mutationSpectrumService.fetchMutationSpectrums(
            MOLECULAR_PROFILE_ID, Arrays.asList(SAMPLE_ID1, SAMPLE_ID2));

    Assertions.assertEquals(2, result.size());
    MutationSpectrum mutationSpectrum1 = result.get(0);
    Assertions.assertEquals(MOLECULAR_PROFILE_ID, mutationSpectrum1.getMolecularProfileId());
    Assertions.assertEquals(SAMPLE_ID1, mutationSpectrum1.getSampleId());
    Assertions.assertEquals((Integer) 2, mutationSpectrum1.getCtoA());
    Assertions.assertEquals((Integer) 0, mutationSpectrum1.getCtoG());
    Assertions.assertEquals((Integer) 0, mutationSpectrum1.getCtoT());
    Assertions.assertEquals((Integer) 0, mutationSpectrum1.getTtoA());
    Assertions.assertEquals((Integer) 0, mutationSpectrum1.getTtoC());
    Assertions.assertEquals((Integer) 0, mutationSpectrum1.getTtoG());
    MutationSpectrum mutationSpectrum2 = result.get(1);
    Assertions.assertEquals(MOLECULAR_PROFILE_ID, mutationSpectrum2.getMolecularProfileId());
    Assertions.assertEquals(SAMPLE_ID2, mutationSpectrum2.getSampleId());
    Assertions.assertEquals((Integer) 0, mutationSpectrum2.getCtoA());
    Assertions.assertEquals((Integer) 0, mutationSpectrum2.getCtoG());
    Assertions.assertEquals((Integer) 1, mutationSpectrum2.getCtoT());
    Assertions.assertEquals((Integer) 1, mutationSpectrum2.getTtoA());
    Assertions.assertEquals((Integer) 0, mutationSpectrum2.getTtoC());
    Assertions.assertEquals((Integer) 0, mutationSpectrum2.getTtoG());
  }

  private List<Mutation> createMutationList() {

    List<Mutation> mutationList = new ArrayList<>();
    Mutation mutation1 = new Mutation();
    mutation1.setSampleId(SAMPLE_ID1);
    mutation1.setReferenceAllele("C");
    mutation1.setTumorSeqAllele("A");
    mutationList.add(mutation1);
    Mutation mutation2 = new Mutation();
    mutation2.setSampleId(SAMPLE_ID1);
    mutation2.setReferenceAllele("G");
    mutation2.setTumorSeqAllele("T");
    mutationList.add(mutation2);
    Mutation mutation3 = new Mutation();
    mutation3.setSampleId(SAMPLE_ID2);
    mutation3.setReferenceAllele("T");
    mutation3.setTumorSeqAllele("A");
    mutationList.add(mutation3);
    Mutation mutation4 = new Mutation();
    mutation4.setSampleId(SAMPLE_ID2);
    mutation4.setReferenceAllele("C");
    mutation4.setTumorSeqAllele("T");
    mutationList.add(mutation4);
    return mutationList;
  }
}
