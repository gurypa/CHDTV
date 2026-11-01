package androidx.media3.exoplayer;

import android.content.Context;
import androidx.media3.common.Metadata;
import androidx.media3.common.text.CueGroup;
import androidx.media3.common.util.SystemClock;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.analytics.PlayerId;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultRendererCapabilitiesList implements RendererCapabilitiesList {
    private final Renderer[] renderers;

    /* JADX INFO: renamed from: androidx.media3.exoplayer.DefaultRendererCapabilitiesList$1 */
    static /* synthetic */ class C02241 {
    }

    public static final class Factory implements RendererCapabilitiesList.Factory {
        private final RenderersFactory renderersFactory;

        public Factory(Context r2) {
            this.renderersFactory = new DefaultRenderersFactory(r2);
        }

        public Factory(RenderersFactory r1) {
            this.renderersFactory = r1;
        }

        static /* synthetic */ void lambda$createRendererCapabilitiesList$0(CueGroup r0) {
        }

        static /* synthetic */ void lambda$createRendererCapabilitiesList$1(Metadata r0) {
        }

        @Override // androidx.media3.exoplayer.RendererCapabilitiesList.Factory
        public DefaultRendererCapabilitiesList createRendererCapabilitiesList() {
            return new DefaultRendererCapabilitiesList(this.renderersFactory.createRenderers(Util.createHandlerForCurrentOrMainLooper(), new C02271(this), new C02282(this), new C0225x7908b72d(), new C0226x7908b72e()), null);
        }

        @Override // androidx.media3.exoplayer.RendererCapabilitiesList.Factory
        public /* bridge */ /* synthetic */ RendererCapabilitiesList createRendererCapabilitiesList() {
            return createRendererCapabilitiesList();
        }
    }

    private DefaultRendererCapabilitiesList(Renderer[] r5) {
        this.renderers = (Renderer[]) Arrays.copyOf(r5, r5.length);
        int r0 = 0;
    L4:
        if (r0 >= r5.length) goto L6;
        this.renderers[r0].init(r0, PlayerId.UNSET, SystemClock.DEFAULT);
        r0 = r0 + 1;
        goto L4
    }

    /* synthetic */ DefaultRendererCapabilitiesList(Renderer[] r1, C02241 r2) {
        this(r1);
    }

    @Override // androidx.media3.exoplayer.RendererCapabilitiesList
    public RendererCapabilities[] getRendererCapabilities() {
        RendererCapabilities[] r0 = new RendererCapabilities[this.renderers.length];
        int r1 = 0;
    L4:
        if (r1 >= this.renderers.length) goto L6;
        r0[r1] = this.renderers[r1].getCapabilities();
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    @Override // androidx.media3.exoplayer.RendererCapabilitiesList
    public void release() {
        Renderer[] r0 = this.renderers;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L5;
        r0[r2].release();
        r2 = r2 + 1;
        goto L3
    }

    @Override // androidx.media3.exoplayer.RendererCapabilitiesList
    public int size() {
        return this.renderers.length;
    }
}
