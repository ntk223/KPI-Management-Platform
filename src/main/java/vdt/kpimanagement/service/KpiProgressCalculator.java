package vdt.kpimanagement.service;

import org.springframework.stereotype.Component;
import vdt.kpimanagement.constant.enums.AggregationType;
import vdt.kpimanagement.constant.enums.TargetType;
import vdt.kpimanagement.entity.KpiItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class KpiProgressCalculator {

    /**
     * Tính tiến độ cho lá (Leaf Item: NUMERIC hoặc PERCENTAGE)
     */
    public BigDecimal calculateLeafProgress(TargetType targetType, BigDecimal currentValue, BigDecimal targetValue) {
        if (currentValue == null) currentValue = BigDecimal.ZERO;
        if (targetValue == null) targetValue = BigDecimal.ZERO;

        if (targetValue.compareTo(BigDecimal.ZERO) == 0) {
            return currentValue.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO;
        }

        switch (targetType) {
            case HIGHER_BETTER:
                return currentValue.divide(targetValue, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);

            case LOWER_BETTER:
                if (currentValue.compareTo(targetValue) <= 0) {
                    return BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP);
                }
                BigDecimal ratio = currentValue.divide(targetValue, 4, RoundingMode.HALF_UP);
                BigDecimal lowerBetterProgress = BigDecimal.valueOf(2).subtract(ratio).multiply(BigDecimal.valueOf(100));
                if (lowerBetterProgress.compareTo(BigDecimal.ZERO) < 0) {
                    return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                }
                return lowerBetterProgress.setScale(2, RoundingMode.HALF_UP);

            case EXACT:
                BigDecimal diff = currentValue.subtract(targetValue).abs();
                BigDecimal deviationRatio = diff.divide(targetValue, 4, RoundingMode.HALF_UP);
                BigDecimal exactProgress = BigDecimal.valueOf(100).subtract(deviationRatio.multiply(BigDecimal.valueOf(100)));
                if (exactProgress.compareTo(BigDecimal.ZERO) < 0) {
                    return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                }
                return exactProgress.setScale(2, RoundingMode.HALF_UP);

            default:
                return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
    }

    /**
     * Tính tiến độ cho nút nhóm (Group Item) dựa trên danh sách các item con trực tiếp
     */
    public BigDecimal calculateGroupProgress(AggregationType aggregationType, List<KpiItem> activeChildren) {
        if (activeChildren == null || activeChildren.isEmpty()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        if (aggregationType == null) {
            aggregationType = AggregationType.WEIGHTED_AVERAGE;
        }

        switch (aggregationType) {
            case SUM:
                BigDecimal sum = BigDecimal.ZERO;
                for (KpiItem child : activeChildren) {
                    BigDecimal progress = child.getProgress() != null ? child.getProgress() : BigDecimal.ZERO;
                    sum = sum.add(progress);
                }
                return sum.setScale(2, RoundingMode.HALF_UP);

            case WEIGHTED_AVERAGE:
                BigDecimal weightedSum = BigDecimal.ZERO;
                BigDecimal totalWeight = BigDecimal.ZERO;
                for (KpiItem child : activeChildren) {
                    BigDecimal progress = child.getProgress() != null ? child.getProgress() : BigDecimal.ZERO;
                    BigDecimal weight = child.getParentWeight() != null ? child.getParentWeight() : BigDecimal.ZERO;
                    weightedSum = weightedSum.add(progress.multiply(weight));
                    totalWeight = totalWeight.add(weight);
                }
                if (totalWeight.compareTo(BigDecimal.ZERO) == 0) {
                    return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                }
                return weightedSum.divide(totalWeight, 2, RoundingMode.HALF_UP);

            default:
                return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
    }

    /**
     * Tính overallScore (total_progress) cho KpiDocument từ các item gốc (parent == null)
     */
    public BigDecimal calculateOverallScore(List<KpiItem> rootItems) {
        if (rootItems == null || rootItems.isEmpty()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal weightedSum = BigDecimal.ZERO;
        BigDecimal totalWeight = BigDecimal.ZERO;

        for (KpiItem item : rootItems) {
            if (item.getParent() == null) {
                BigDecimal progress = item.getProgress() != null ? item.getProgress() : BigDecimal.ZERO;
                BigDecimal weight = item.getDocumentWeight() != null ? item.getDocumentWeight() : BigDecimal.ZERO;
                weightedSum = weightedSum.add(progress.multiply(weight));
                totalWeight = totalWeight.add(weight);
            }
        }

        if (totalWeight.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return weightedSum.divide(totalWeight, 2, RoundingMode.HALF_UP);
    }
}
