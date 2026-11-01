package androidx.media3.common;

/* JADX INFO: loaded from: classes.dex */
public final class AuxEffectInfo {
    public static final int NO_AUX_EFFECT_ID = 0;
    public final int effectId;
    public final float sendLevel;

    public AuxEffectInfo(int r1, float r2) {
        this.effectId = r1;
        this.sendLevel = r2;
    }

    public boolean equals(Object r6) {
        if (this != r6) goto L6;
        return true;
    L6:
        if (r6 != null) goto L8;
    L17:
        return false;
    L8:
        if (getClass() != r6.getClass()) goto L17;
        AuxEffectInfo r2 = (AuxEffectInfo) r6;
        if (this.effectId != r2.effectId) goto L16;
        if (Float.compare(r2.sendLevel, this.sendLevel) != 0) goto L16;
        return true;
    L16:
        return false;
    }

    public int hashCode() {
        return (((17 * 31) + this.effectId) * 31) + Float.floatToIntBits(this.sendLevel);
    }
}
