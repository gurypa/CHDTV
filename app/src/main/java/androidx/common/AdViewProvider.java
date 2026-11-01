package androidx.media3.common;

import android.view.ViewGroup;
import com.google.common.collect.ImmutableList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface AdViewProvider {

    /* JADX INFO: renamed from: androidx.media3.common.AdViewProvider$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static List $default$getAdOverlayInfos(AdViewProvider r1) {
            return ImmutableList.m212of();
        }
    }

    List<AdOverlayInfo> getAdOverlayInfos();

    ViewGroup getAdViewGroup();
}
