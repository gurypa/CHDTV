package androidx.media3.common;

import androidx.media3.common.util.GlUtil;

/* JADX INFO: loaded from: classes.dex */
public final class GlTextureInfo {
    public static final GlTextureInfo UNSET = null;
    public final int fboId;
    public final int height;
    public final int rboId;
    public final int texId;
    public final int width;

    static {
        UNSET = new GlTextureInfo(-1, -1, -1, -1, -1);
    }

    public GlTextureInfo(int r1, int r2, int r3, int r4, int r5) {
        this.texId = r1;
        this.fboId = r2;
        this.rboId = r3;
        this.width = r4;
        this.height = r5;
    }

    public void release() throws GlUtil.GlException {
        if (this.texId == (-1)) goto L6;
        GlUtil.deleteTexture(this.texId);
    L6:
        if (this.fboId == (-1)) goto L9;
        GlUtil.deleteFbo(this.fboId);
    L9:
        if (this.rboId == (-1)) goto L12;
        GlUtil.deleteRbo(this.rboId);
        return;
    }
}
