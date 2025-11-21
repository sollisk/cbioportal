/*
 * Copyright (c) 2016 Memorial Sloan-Kettering Cancer Center.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS
 * FOR A PARTICULAR PURPOSE. The software and documentation provided hereunder
 * is on an "as is" basis, and Memorial Sloan-Kettering Cancer Center has no
 * obligations to provide maintenance, support, updates, enhancements or
 * modifications. In no event shall Memorial Sloan-Kettering Cancer Center be
 * liable to any party for direct, indirect, special, incidental or
 * consequential damages, including lost profits, arising out of the use of this
 * software and its documentation, even if Memorial Sloan-Kettering Cancer
 * Center has been advised of the possibility of such damage.
 */

/*
 * This file is part of cBioPortal.
 *
 * cBioPortal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cbioportal.legacy.persistence.mybatis;

import java.util.ArrayList;
import java.util.List;
import org.cbioportal.legacy.model.Gene;
import org.cbioportal.legacy.model.meta.BaseMeta;
import org.cbioportal.legacy.persistence.mybatis.config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {GeneMyBatisRepository.class, TestConfig.class})
public class GeneMyBatisRepositoryTest {

  @Autowired private GeneMyBatisRepository geneMyBatisRepository;

  @Test
  public void getAllGenesIdProjection() {

    List<Gene> result = geneMyBatisRepository.getAllGenes(null, null, "ID", null, null, null, null);

    Assertions.assertEquals(23, result.size());
    Gene gene = result.getFirst();
    Assertions.assertEquals((Integer) 207, gene.getEntrezGeneId());
    Assertions.assertEquals("AKT1", gene.getHugoGeneSymbol());
  }

  @Test
  public void getAllGenesSummaryProjection() {

    List<Gene> result =
        geneMyBatisRepository.getAllGenes(null, null, "SUMMARY", null, null, null, null);

    Assertions.assertEquals(23, result.size());
    Gene gene = result.getFirst();
    Assertions.assertEquals((Integer) 207, gene.getEntrezGeneId());
    Assertions.assertEquals("AKT1", gene.getHugoGeneSymbol());
    Assertions.assertEquals("protein-coding", gene.getType());
  }

  @Test
  public void getAllGenesDetailedProjection() {

    List<Gene> result =
        geneMyBatisRepository.getAllGenes(null, null, "DETAILED", null, null, null, null);

    Assertions.assertEquals(23, result.size());
    Gene gene = result.getFirst();
    Assertions.assertEquals((Integer) 207, gene.getEntrezGeneId());
    Assertions.assertEquals("AKT1", gene.getHugoGeneSymbol());
    Assertions.assertEquals("protein-coding", gene.getType());
  }

  @Test
  public void getAllGenesSummaryProjection1PageSize() {

    List<Gene> result = geneMyBatisRepository.getAllGenes(null, null, "SUMMARY", 1, 0, null, null);

    Assertions.assertEquals(1, result.size());
  }

  @Test
  public void getAllGenesSummaryProjectionHugoGeneSymbolSort() {

    List<Gene> result =
        geneMyBatisRepository.getAllGenes(
            null, null, "SUMMARY", null, null, "hugoGeneSymbol", "ASC");

    Assertions.assertEquals(23, result.size());
    Assertions.assertEquals("AKT1", result.get(0).getHugoGeneSymbol());
    Assertions.assertEquals("AKT2", result.get(1).getHugoGeneSymbol());
    Assertions.assertEquals("AKT3", result.get(2).getHugoGeneSymbol());
    Assertions.assertEquals("ALK", result.get(3).getHugoGeneSymbol());
    Assertions.assertEquals("ARAF", result.get(4).getHugoGeneSymbol());
    Assertions.assertEquals("ATM", result.get(5).getHugoGeneSymbol());
    Assertions.assertEquals("BRAF", result.get(6).getHugoGeneSymbol());
    Assertions.assertEquals("SAMD11", result.get(21).getHugoGeneSymbol());
  }

  @Test
  public void getMetaGenes() {

    BaseMeta result = geneMyBatisRepository.getMetaGenes(null, null);

    Assertions.assertEquals((Integer) 23, result.getTotalCount());
  }

  @Test
  public void getGeneByEntrezGeneIdNullResult() {

    Gene result = geneMyBatisRepository.getGeneByEntrezGeneId(999);

    Assertions.assertNull(result);
  }

  @Test
  public void getGeneByEntrezGeneId() throws Exception {

    Gene result = geneMyBatisRepository.getGeneByEntrezGeneId(207);

    Assertions.assertEquals((Integer) 207, result.getEntrezGeneId());
    Assertions.assertEquals("AKT1", result.getHugoGeneSymbol());
    Assertions.assertEquals("protein-coding", result.getType());
  }

  @Test
  public void getGeneByHugoGeneSymbolNullResult() {

    Gene result = geneMyBatisRepository.getGeneByHugoGeneSymbol("invalid_gene");

    Assertions.assertNull(result);
  }

  @Test
  public void getGeneByHugoGeneSymbol() throws Exception {

    Gene result = geneMyBatisRepository.getGeneByHugoGeneSymbol("AKT1");

    Assertions.assertEquals((Integer) 207, result.getEntrezGeneId());
    Assertions.assertEquals("AKT1", result.getHugoGeneSymbol());
    Assertions.assertEquals("protein-coding", result.getType());
  }

  @Test
  public void getAliasesOfGeneByEntrezGeneIdEmptyList() {

    List<String> result = geneMyBatisRepository.getAliasesOfGeneByEntrezGeneId(208);

    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getAliasesOfGeneByEntrezGeneId() {

    List<String> result = geneMyBatisRepository.getAliasesOfGeneByEntrezGeneId(207);

    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("AKT alias", result.get(0));
    Assertions.assertEquals("AKT alias2", result.get(1));
  }

  @Test
  public void getAliasesOfGeneByHugoGeneSymbolEmptyList() {

    List<String> result = geneMyBatisRepository.getAliasesOfGeneByHugoGeneSymbol("AKT2");

    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void getAliasesOfGeneByHugoGeneSymbol() {

    List<String> result = geneMyBatisRepository.getAliasesOfGeneByHugoGeneSymbol("AKT1");

    Assertions.assertEquals(2, result.size());
    Assertions.assertEquals("AKT alias", result.get(0));
    Assertions.assertEquals("AKT alias2", result.get(1));
  }

  @Test
  public void fetchGenesByEntrezGeneIds() {

    List<Integer> entrezGeneIds = new ArrayList<>();
    entrezGeneIds.add(207);
    entrezGeneIds.add(208);

    List<Gene> result = geneMyBatisRepository.fetchGenesByEntrezGeneIds(entrezGeneIds, "SUMMARY");

    Assertions.assertEquals(2, result.size());
    Gene gene;
      gene = result.get(0);
      Assertions.assertEquals((Integer) 207, gene.getEntrezGeneId());
    Assertions.assertEquals("AKT1", gene.getHugoGeneSymbol());
    Assertions.assertEquals("protein-coding", gene.getType());
  }

  @Test
  public void fetchGenesByHugoGeneSymbols() {

    List<String> hugoGeneSymbols = new ArrayList<>();
    hugoGeneSymbols.add("AKT1");
    hugoGeneSymbols.add("AKT2");

    List<Gene> result =
        geneMyBatisRepository.fetchGenesByHugoGeneSymbols(hugoGeneSymbols, "SUMMARY");

    Assertions.assertEquals(2, result.size());
    Gene gene;
    gene = result.getFirst();
    
    Assertions.assertEquals((Integer) 207, gene.getEntrezGeneId());
    Assertions.assertEquals("AKT1", gene.getHugoGeneSymbol());
    Assertions.assertEquals("protein-coding", gene.getType());
  }

  @Test
  public void fetchMetaGenesByEntrezGeneIds() {

    List<Integer> entrezGeneIds = new ArrayList<>();
    entrezGeneIds.add(207);
    entrezGeneIds.add(208);

    BaseMeta result = geneMyBatisRepository.fetchMetaGenesByEntrezGeneIds(entrezGeneIds);

    Assertions.assertEquals((Integer) 2, result.getTotalCount());
  }

  @Test
  public void fetchMetaGenesByHugoGeneSymbol() {

    List<String> hugoGeneSymbols = new ArrayList<>();
    hugoGeneSymbols.add("AKT1");
    hugoGeneSymbols.add("AKT2");

    BaseMeta result = geneMyBatisRepository.fetchMetaGenesByHugoGeneSymbols(hugoGeneSymbols);

    Assertions.assertEquals((Integer) 2, result.getTotalCount());
  }
}
