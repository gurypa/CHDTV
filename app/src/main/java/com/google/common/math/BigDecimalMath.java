package com.google.common.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class BigDecimalMath {

    private static class BigDecimalToDoubleRounder extends ToDoubleRounder<BigDecimal> {
        static final BigDecimalToDoubleRounder INSTANCE = null;

        static {
            INSTANCE = new BigDecimalToDoubleRounder();
        }

        private BigDecimalToDoubleRounder() {
        }

        @Override // com.google.common.math.ToDoubleRounder
        /* bridge */ /* synthetic */ Number minus(Number r1, Number r2) {
            return minus((BigDecimal) r1, (BigDecimal) r2);
        }

        BigDecimal minus(BigDecimal r2, BigDecimal r3) {
            return r2.subtract(r3);
        }

        @Override // com.google.common.math.ToDoubleRounder
        /* bridge */ /* synthetic */ double roundToDoubleArbitrarily(Number r3) {
            return roundToDoubleArbitrarily((BigDecimal) r3);
        }

        double roundToDoubleArbitrarily(BigDecimal r3) {
            return r3.doubleValue();
        }

        @Override // com.google.common.math.ToDoubleRounder
        /* bridge */ /* synthetic */ int sign(Number r1) {
            return sign((BigDecimal) r1);
        }

        int sign(BigDecimal r2) {
            return r2.signum();
        }

        @Override // com.google.common.math.ToDoubleRounder
        /* bridge */ /* synthetic */ Number toX(double r1, RoundingMode r3) {
            return toX(r1, r3);
        }

        @Override // com.google.common.math.ToDoubleRounder
        BigDecimal toX(double r2, RoundingMode r4) {
            return new BigDecimal(r2);
        }
    }

    private BigDecimalMath() {
    }

    public static double roundToDouble(BigDecimal r2, RoundingMode r3) {
        return BigDecimalToDoubleRounder.INSTANCE.roundToDouble(r2, r3);
    }
}
