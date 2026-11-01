package com.google.common.math;

import com.google.common.base.Preconditions;
import java.lang.Comparable;
import java.lang.Number;
import java.math.RoundingMode;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class ToDoubleRounder<X extends Number & Comparable<X>> {

    /* JADX INFO: renamed from: com.google.common.math.ToDoubleRounder$1 */
    static /* synthetic */ class C08661 {
        static final /* synthetic */ int[] $SwitchMap$java$math$RoundingMode = null;

        static {
            $SwitchMap$java$math$RoundingMode = new int[RoundingMode.values().length];
            $SwitchMap$java$math$RoundingMode[RoundingMode.DOWN.ordinal()] = 1;     // Catch: NoSuchFieldError -> L5
        L40:
            $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_EVEN.ordinal()] = 2;     // Catch: NoSuchFieldError -> L8
        L30:
            $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_DOWN.ordinal()] = 3;     // Catch: NoSuchFieldError -> L11
        L28:
            $SwitchMap$java$math$RoundingMode[RoundingMode.HALF_UP.ordinal()] = 4;     // Catch: NoSuchFieldError -> L14
        L32:
            $SwitchMap$java$math$RoundingMode[RoundingMode.FLOOR.ordinal()] = 5;     // Catch: NoSuchFieldError -> L17
        L36:
            $SwitchMap$java$math$RoundingMode[RoundingMode.CEILING.ordinal()] = 6;     // Catch: NoSuchFieldError -> L20
        L42:
            $SwitchMap$java$math$RoundingMode[RoundingMode.UP.ordinal()] = 7;     // Catch: NoSuchFieldError -> L23
        L38:
            $SwitchMap$java$math$RoundingMode[RoundingMode.UNNECESSARY.ordinal()] = 8;     // Catch: NoSuchFieldError -> L26
            return;
        }
    }

    ToDoubleRounder() {
    }

    abstract X minus(X r1, X r2);

    final double roundToDouble(X r21, RoundingMode r22) {
        Preconditions.checkNotNull(r21, "x");
        Preconditions.checkNotNull(r22, "mode");
        double r4 = roundToDoubleArbitrarily(r21);
        if (Double.isInfinite(r4) == true) goto L5;
    L22:
        Number r2 = toX(r4, RoundingMode.UNNECESSARY);
        int r10 = ((Comparable) r21).compareTo(r2);
        switch(C08661.$SwitchMap$java$math$RoundingMode[r22.ordinal()]) {
            case 1: goto L83;
            case 2: goto L49;
            case 3: goto L49;
            case 4: goto L49;
            case 5: goto L45;
            case 6: goto L41;
            case 7: goto L32;
            case 8: goto L26;
            default: goto L25;
        };
    L26:
        if (r10 != 0) goto L28;
        boolean r6 = true;
    L29:
        MathPreconditions.checkRoundingUnnecessary(r6);
        return r4;
    L28:
        r6 = false;
        goto L29
    L41:
        if (r10 > 0) goto L44;
        return r4;
    L44:
        return Math.nextUp(r4);
    L45:
        if (r10 < 0) goto L48;
        return r4;
    L48:
        return DoubleUtils.nextDown(r4);
    L49:
        if (r10 < 0) goto L54;
        double r7 = r4;
        Number r11 = r2;
        double r13 = Math.nextUp(r4);
        if (r13 != Double.POSITIVE_INFINITY) goto L53;
        return r7;
    L53:
        Number r8 = toX(r13, RoundingMode.CEILING);
    L58:
        int r0 = ((Comparable) minus(r21, r11)).compareTo(minus(r8, r21));
        if (r0 >= 0) goto L61;
        return r7;
    L61:
        if (r0 <= 0) goto L64;
        return r13;
    L64:
        switch(C08661.$SwitchMap$java$math$RoundingMode[r22.ordinal()]) {
            case 2: goto L78;
            case 3: goto L73;
            case 4: goto L68;
            default: goto L66;
        };
    L66:
        throw new AssertionError("impossible");
    L68:
        if (sign(r21) < 0) goto L71;
        return r13;
    L71:
        return r7;
    L73:
        if (sign(r21) < 0) goto L76;
        return r7;
    L76:
        return r13;
    L78:
        if ((Double.doubleToRawLongBits(r7) & 1) != 0) goto L81;
        return r7;
    L81:
        return r13;
    L54:
        r13 = r4;
        r8 = r2;
        r7 = DoubleUtils.nextDown(r4);
        if (r7 != Double.NEGATIVE_INFINITY) goto L57;
        return r13;
    L57:
        r11 = toX(r7, RoundingMode.FLOOR);
        goto L58
    L25:
        throw new AssertionError("impossible");
    L32:
        if (sign(r21) < 0) goto L37;
        if (r10 > 0) goto L36;
        return r4;
    L36:
        return Math.nextUp(r4);
    L37:
        if (r10 < 0) goto L40;
        return r4;
    L40:
        return DoubleUtils.nextDown(r4);
    L83:
        if (sign(r21) < 0) goto L88;
        if (r10 < 0) goto L87;
        return r4;
    L87:
        return DoubleUtils.nextDown(r4);
    L88:
        if (r10 > 0) goto L91;
        return r4;
    L91:
        return Math.nextUp(r4);
    L5:
        switch(C08661.$SwitchMap$java$math$RoundingMode[r22.ordinal()]) {
            case 1: goto L21;
            case 2: goto L21;
            case 3: goto L21;
            case 4: goto L21;
            case 5: goto L16;
            case 6: goto L11;
            case 7: goto L9;
            case 8: goto L8;
            default: goto L22;
        };
    L9:
        return r4;
    L8:
        throw new ArithmeticException(r21 + " cannot be represented precisely as a double");
    L11:
        if (r4 == Double.POSITIVE_INFINITY) goto L92;
        return -1.7976931348623157E308d;
    L92:
        return Double.POSITIVE_INFINITY;
    L16:
        if (r4 == Double.POSITIVE_INFINITY) goto L93;
        return Double.NEGATIVE_INFINITY;
    L93:
        return Double.MAX_VALUE;
    L21:
        return ((double) sign(r21)) * Double.MAX_VALUE;
    }

    abstract double roundToDoubleArbitrarily(X r1);

    abstract int sign(X r1);

    abstract X toX(double r1, RoundingMode r3);
}
