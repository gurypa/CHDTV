package kotlin.random;

import java.io.Serializable;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* JADX INFO: compiled from: XorWowRandom.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000e\b\u0000\u0018\u0000 \u00122\u00020\u00012\u00060\u0002j\u0002`\u0003:\u0001\u0012B\u0017\b\u0010\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005¢\u0006\u0002\u0010\u0007B7\b\u0000\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\u0005\u0012\u0006\u0010\n\u001a\u00020\u0005\u0012\u0006\u0010\u000b\u001a\u00020\u0005\u0012\u0006\u0010\f\u001a\u00020\u0005\u0012\u0006\u0010\r\u001a\u00020\u0005¢\u0006\u0002\u0010\u000eJ\u0010\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u0005H\u0016J\b\u0010\u0011\u001a\u00020\u0005H\u0016R\u000e\u0010\r\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0013"}, m421d2 = {"Lkotlin/random/XorWowRandom;", "Lkotlin/random/Random;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "seed1", "", "seed2", "(II)V", "x", "y", "z", "w", "v", "addend", "(IIIIII)V", "nextBits", "bitCount", "nextInt", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class XorWowRandom extends Random implements Serializable {
    private static final Companion Companion = null;
    private static final long serialVersionUID = 0;
    private int addend;

    /* JADX INFO: renamed from: v */
    private int f196v;

    /* JADX INFO: renamed from: w */
    private int f197w;

    /* JADX INFO: renamed from: x */
    private int f198x;

    /* JADX INFO: renamed from: y */
    private int f199y;

    /* JADX INFO: renamed from: z */
    private int f200z;

    /* JADX INFO: compiled from: XorWowRandom.kt */
    @Metadata(m420d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, m421d2 = {"Lkotlin/random/XorWowRandom$Companion;", "", "()V", "serialVersionUID", "", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    private static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }
    }

    static {
        Companion = new Companion(null);
    }

    public XorWowRandom(int r8, int r9) {
        this(r8, r9, 0, 0, ~r8, (r8 << 10) ^ (r9 >>> 4));
    }

    public XorWowRandom(int r4, int r5, int r6, int r7, int r8, int r9) {
        this.f198x = r4;
        this.f199y = r5;
        this.f200z = r6;
        this.f197w = r7;
        this.f196v = r8;
        this.addend = r9;
        int r1 = 0;
        if (((((this.f198x | this.f199y) | this.f200z) | this.f197w) | this.f196v) == 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L13;
    L8:
        if (r1 >= 64) goto L11;
        nextInt();
        r1 = r1 + 1;
        goto L8
    L11:
        return;
    L13:
        throw new IllegalArgumentException("Initial state must have at least one non-zero element.".toString());
    L5:
        r0 = false;
        goto L6
    }

    @Override // kotlin.random.Random
    public int nextBits(int r2) {
        return RandomKt.takeUpperBits(nextInt(), r2);
    }

    @Override // kotlin.random.Random
    public int nextInt() {
        int r0 = this.f198x;
        int r1 = r0 ^ (r0 >>> 2);
        this.f198x = this.f199y;
        this.f199y = this.f200z;
        this.f200z = this.f197w;
        int r2 = this.f196v;
        this.f197w = r2;
        int r3 = (((r1 << 1) ^ r1) ^ r2) ^ (r2 << 4);
        this.f196v = r3;
        this.addend += 362437;
        return this.addend + r3;
    }
}
