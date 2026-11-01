package androidx.media3.exoplayer;

/* JADX INFO: loaded from: classes.dex */
public final class RendererConfiguration {
    public static final RendererConfiguration DEFAULT = null;
    public final int offloadModePreferred;
    public final boolean tunneling;

    static {
        DEFAULT = new RendererConfiguration(0, false);
    }

    public RendererConfiguration(int r1, boolean r2) {
        this.offloadModePreferred = r1;
        this.tunneling = r2;
    }

    public RendererConfiguration(boolean r2) {
        this.offloadModePreferred = 0;
        this.tunneling = r2;
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
        RendererConfiguration r2 = (RendererConfiguration) r6;
        if (this.offloadModePreferred != r2.offloadModePreferred) goto L16;
        if (this.tunneling != r2.tunneling) goto L16;
        return true;
    L16:
        return false;
    }

    public int hashCode() {
        return (this.offloadModePreferred << 1) + (this.tunneling ? 1 : 0);
    }
}
