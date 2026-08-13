package com.hardwaredeals.deal;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;

class DealEvaluationServiceTest {
    private final DealEvaluationService service = new DealEvaluationService();

    @Test
    void classifiesExcellentDealAtHistoricalLow() {
        assertThat(service.evaluate(input("80", "100", "80", "120", true, "SAVE")))
                .isEqualTo(new DealEvaluation(100, DealClassification.EXCELENTE));
    }

    @Test
    void scoresNeutralPriceAsNormal() {
        assertThat(service.evaluate(input("100", "100", "80", "120", true, null)))
                .isEqualTo(new DealEvaluation(55, DealClassification.NORMAL));
    }

    @Test
    void couponAddsOnlyFivePoints() {
        DealEvaluation withoutCoupon = service.evaluate(input("95", "100", "80", "120", true, null));
        DealEvaluation withCoupon = service.evaluate(input("95", "100", "80", "120", true, " FIVE "));
        assertThat(withCoupon.score() - withoutCoupon.score()).isEqualTo(5);
    }

    @Test
    void unavailableOfferAlwaysScoresZero() {
        assertThat(service.evaluate(input("50", "100", "50", "150", false, "SAVE")))
                .isEqualTo(new DealEvaluation(0, DealClassification.NORMAL));
    }

    @Test
    void clampsVeryBadAndVeryGoodPricesToScoreRange() {
        assertThat(service.evaluate(input("500", "100", "50", "200", true, null)).score()).isZero();
        assertThat(service.evaluate(input("1", "100", "1", "200", true, "SAVE")).score()).isEqualTo(100);
    }

    @Test
    void mapsEveryClassificationBoundary() {
        assertThat(service.classify(90)).isEqualTo(DealClassification.EXCELENTE);
        assertThat(service.classify(89)).isEqualTo(DealClassification.OTIMA);
        assertThat(service.classify(80)).isEqualTo(DealClassification.OTIMA);
        assertThat(service.classify(79)).isEqualTo(DealClassification.BOA);
        assertThat(service.classify(70)).isEqualTo(DealClassification.BOA);
        assertThat(service.classify(69)).isEqualTo(DealClassification.INTERESSANTE);
        assertThat(service.classify(60)).isEqualTo(DealClassification.INTERESSANTE);
        assertThat(service.classify(59)).isEqualTo(DealClassification.NORMAL);
    }

    @Test
    void rejectsInvalidPriceInputsAndScore() {
        assertThatIllegalArgumentException().isThrownBy(() -> service.evaluate(input("0", "100", "80", "120", true, null)));
        assertThatIllegalArgumentException().isThrownBy(() -> service.evaluate(input("100", "100", "120", "80", true, null)));
        assertThatIllegalArgumentException().isThrownBy(() -> service.classify(101));
    }

    private DealEvaluationInput input(String current, String average, String lowest, String highest,
                                      boolean available, String coupon) {
        return new DealEvaluationInput(new BigDecimal(current), new BigDecimal(average),
                new BigDecimal(lowest), new BigDecimal(highest), available, coupon);
    }
}
