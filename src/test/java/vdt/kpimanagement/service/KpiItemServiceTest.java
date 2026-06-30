package vdt.kpimanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import vdt.kpimanagement.constant.enums.AggregationType;
import vdt.kpimanagement.constant.enums.KpiItemType;
import vdt.kpimanagement.constant.enums.TargetType;
import vdt.kpimanagement.entity.KpiDocument;
import vdt.kpimanagement.entity.KpiItem;
import vdt.kpimanagement.exception.BadRequestException;
import vdt.kpimanagement.repository.KpiDocumentRepo;
import vdt.kpimanagement.repository.KpiItemRepo;
import vdt.kpimanagement.repository.KpiTemplateRepo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KpiItemServiceTest {

    private KpiItemRepo kpiItemRepo;
    private KpiDocumentRepo kpiDocumentRepo;
    private KpiTemplateRepo kpiTemplateRepo;
    private KpiProgressCalculator progressCalculator;
    private KpiItemService kpiItemService;

    @BeforeEach
    void setUp() {
        kpiItemRepo = mock(KpiItemRepo.class);
        kpiDocumentRepo = mock(KpiDocumentRepo.class);
        kpiTemplateRepo = mock(KpiTemplateRepo.class);
        progressCalculator = new KpiProgressCalculator();
        kpiItemService = new KpiItemService(kpiItemRepo, kpiDocumentRepo, kpiTemplateRepo, progressCalculator);
    }

    @Test
    void testCalculateLeafProgress_HigherBetter() {
        BigDecimal progress = progressCalculator.calculateLeafProgress(
                TargetType.HIGHER_BETTER,
                BigDecimal.valueOf(80),
                BigDecimal.valueOf(100)
        );
        assertEquals(new BigDecimal("80.00"), progress);
    }

    @Test
    void testCalculateLeafProgress_LowerBetter_Success() {
        BigDecimal progress = progressCalculator.calculateLeafProgress(
                TargetType.LOWER_BETTER,
                BigDecimal.valueOf(3),
                BigDecimal.valueOf(5)
        );
        assertEquals(new BigDecimal("100.00"), progress);
    }

    @Test
    void testCalculateLeafProgress_LowerBetter_Deviation() {
        BigDecimal progress = progressCalculator.calculateLeafProgress(
                TargetType.LOWER_BETTER,
                BigDecimal.valueOf(8),
                BigDecimal.valueOf(5)
        );
        assertEquals(new BigDecimal("40.00"), progress);
    }

    @Test
    void testCalculateLeafProgress_Exact() {
        BigDecimal progress = progressCalculator.calculateLeafProgress(
                TargetType.EXACT,
                BigDecimal.valueOf(11),
                BigDecimal.valueOf(10)
        );
        assertEquals(new BigDecimal("90.00"), progress);
    }

    @Test
    void testCalculateGroupProgress_WeightedAverage() {
        KpiItem child1 = new KpiItem();
        child1.setProgress(BigDecimal.valueOf(80));
        child1.setParentWeight(BigDecimal.valueOf(40));

        KpiItem child2 = new KpiItem();
        child2.setProgress(BigDecimal.valueOf(90));
        child2.setParentWeight(BigDecimal.valueOf(60));

        BigDecimal progress = progressCalculator.calculateGroupProgress(
                AggregationType.WEIGHTED_AVERAGE,
                Arrays.asList(child1, child2)
        );
        assertEquals(new BigDecimal("86.00"), progress);
    }

    @Test
    void testCalculateGroupProgress_Sum() {
        KpiItem child1 = new KpiItem();
        child1.setProgress(BigDecimal.valueOf(30));

        KpiItem child2 = new KpiItem();
        child2.setProgress(BigDecimal.valueOf(45));

        BigDecimal progress = progressCalculator.calculateGroupProgress(
                AggregationType.SUM,
                Arrays.asList(child1, child2)
        );
        assertEquals(new BigDecimal("75.00"), progress);
    }

    @Test
    void testUpdateItemValue_ThrowsOnGroup() {
        KpiItem groupItem = new KpiItem();
        groupItem.setId(1L);
        groupItem.setItemType(KpiItemType.GROUP);

        when(kpiItemRepo.findById(1L)).thenReturn(Optional.of(groupItem));

        assertThrows(BadRequestException.class, () ->
                kpiItemService.updateItemValue(1L, BigDecimal.valueOf(100))
        );
    }

    @Test
    void testRecursiveRollupAndDocumentProgressUpdate() {
        // Setup Document
        KpiDocument doc = new KpiDocument();
        doc.setId(10L);

        // Setup Parent Group Item
        KpiItem parentGroup = new KpiItem();
        parentGroup.setId(2L);
        parentGroup.setDocument(doc);
        parentGroup.setItemType(KpiItemType.GROUP);
        parentGroup.setAggregationType(AggregationType.WEIGHTED_AVERAGE);
        parentGroup.setDocumentWeight(BigDecimal.valueOf(100));

        // Setup Leaf Item
        KpiItem leafItem = new KpiItem();
        leafItem.setId(1L);
        leafItem.setDocument(doc);
        leafItem.setItemType(KpiItemType.PERCENTAGE);
        leafItem.setTargetType(TargetType.HIGHER_BETTER);
        leafItem.setTargetValue(BigDecimal.valueOf(100));
        leafItem.setParentWeight(BigDecimal.valueOf(100));
        leafItem.setParent(parentGroup);

        parentGroup.setChildren(Collections.singletonList(leafItem));

        // Mock repo calls
        when(kpiItemRepo.findById(1L)).thenReturn(Optional.of(leafItem));
        when(kpiItemRepo.findByParent_IdAndIsDeletedFalse(2L)).thenReturn(Collections.singletonList(leafItem));
        when(kpiItemRepo.findByDocument_IdAndIsDeletedFalseAndParentIsNull(10L)).thenReturn(Collections.singletonList(parentGroup));

        // Act
        kpiItemService.updateItemValue(1L, BigDecimal.valueOf(85));

        // Assert Leaf progress updated to 85%
        assertEquals(new BigDecimal("85.00"), leafItem.getProgress());
        assertEquals(BigDecimal.valueOf(85), leafItem.getCurrentValue());

        // Assert Parent group progress updated to 85%
        assertEquals(new BigDecimal("85.00"), parentGroup.getProgress());

        // Assert Document overall progress updated to 85%
        ArgumentCaptor<KpiDocument> docCaptor = ArgumentCaptor.forClass(KpiDocument.class);
        verify(kpiDocumentRepo, times(1)).save(docCaptor.capture());
        assertEquals(new BigDecimal("85.00"), docCaptor.getValue().getTotalProgress());
    }
}
