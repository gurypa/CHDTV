package androidx.media3.common;

import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import androidx.media3.common.util.GlUtil;

/* JADX INFO: loaded from: classes.dex */
public interface GlObjectsProvider {
    GlTextureInfo createBuffersForTexture(int r1, int r2, int r3) throws GlUtil.GlException;

    EGLContext createEglContext(EGLDisplay r1, int r2, int[] r3) throws GlUtil.GlException;

    EGLSurface createEglSurface(EGLDisplay r1, Object r2, int r3, boolean r4) throws GlUtil.GlException;

    EGLSurface createFocusedPlaceholderEglSurface(EGLContext r1, EGLDisplay r2) throws GlUtil.GlException;

    void release(EGLDisplay r1) throws GlUtil.GlException;
}
