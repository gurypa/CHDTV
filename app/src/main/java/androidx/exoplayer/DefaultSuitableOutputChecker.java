package androidx.media3.exoplayer;

import android.content.Context;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2;
import android.media.RouteDiscoveryPreference;
import android.os.Handler;
import androidx.media3.common.util.Assertions;
import com.google.common.collect.ImmutableList;
import java.util.Iterator;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
final class DefaultSuitableOutputChecker implements SuitableOutputChecker {
    private static final RouteDiscoveryPreference EMPTY_DISCOVERY_PREFERENCE = null;
    private MediaRouter2.ControllerCallback controllerCallback;
    private final Executor executor;
    private boolean isPreviousSelectedOutputSuitableForPlayback;
    private final MediaRouter2.RouteCallback routeCallback;
    private final MediaRouter2 router;

    static {
        EMPTY_DISCOVERY_PREFERENCE = new RouteDiscoveryPreference.Builder(ImmutableList.m212of(), false).build();
    }

    public DefaultSuitableOutputChecker(Context r2, final Handler r3) {
        this.router = MediaRouter2.getInstance(r2);
        this.routeCallback = new C02291(this);
        this.executor = new ExecutorC02302(this, r3);
    }

    static /* synthetic */ boolean access$000(DefaultSuitableOutputChecker r1) {
        return r1.isPreviousSelectedOutputSuitableForPlayback;
    }

    static /* synthetic */ boolean access$002(DefaultSuitableOutputChecker r0, boolean r1) {
        r0.isPreviousSelectedOutputSuitableForPlayback = r1;
        return r1;
    }

    private static boolean isRouteSuitableForMediaPlayback(MediaRoute2Info r4, int r5, boolean r6) {
        int r0 = r4.getSuitabilityStatus();
        if (r0 != 1) goto L10;
        if (r5 != 1) goto L6;
    L7:
        if (r6 == true) goto L9;
        return false;
    L9:
        return true;
    L6:
        if (r5 == 2) goto L7;
        return false;
    L10:
        if (r0 == 0) goto L12;
        return false;
    L12:
        return true;
    }

    @Override // androidx.media3.exoplayer.SuitableOutputChecker
    public void disable() {
        Assertions.checkStateNotNull(this.controllerCallback, "SuitableOutputChecker is not enabled");
        this.router.unregisterControllerCallback(this.controllerCallback);
        this.controllerCallback = null;
        this.router.unregisterRouteCallback(this.routeCallback);
    }

    @Override // androidx.media3.exoplayer.SuitableOutputChecker
    public void enable(final SuitableOutputChecker.Callback r5) {
        this.router.registerRouteCallback(this.executor, this.routeCallback, EMPTY_DISCOVERY_PREFERENCE);
        this.controllerCallback = new C02313(this, r5);
        this.router.registerControllerCallback(this.executor, this.controllerCallback);
        this.isPreviousSelectedOutputSuitableForPlayback = isSelectedOutputSuitableForPlayback();
    }

    @Override // androidx.media3.exoplayer.SuitableOutputChecker
    public boolean isSelectedOutputSuitableForPlayback() {
        Assertions.checkStateNotNull(this.controllerCallback, "SuitableOutputChecker is not enabled");
        int r0 = this.router.getSystemController().getRoutingSessionInfo().getTransferReason();
        boolean r1 = this.router.getSystemController().wasTransferInitiatedBySelf();
        Iterator<MediaRoute2Info> r2 = this.router.getSystemController().getSelectedRoutes().iterator();
    L4:
        if (r2.hasNext() == false) goto L10;
        if (isRouteSuitableForMediaPlayback(r2.next(), r0, r1) == false) goto L4;
        return true;
    L10:
        return false;
    }
}
