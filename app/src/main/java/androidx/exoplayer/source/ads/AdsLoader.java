package androidx.media3.exoplayer.source.ads;

import androidx.media3.common.AdPlaybackState;
import androidx.media3.common.AdViewProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.datasource.DataSpec;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public interface AdsLoader {

    public interface EventListener {

        /* JADX INFO: renamed from: androidx.media3.exoplayer.source.ads.AdsLoader$EventListener$-CC, reason: invalid class name */
        public final /* synthetic */ class CC {
            public static void $default$onAdClicked(EventListener r0) {
            }

            public static void $default$onAdLoadError(EventListener r0, AdsMediaSource.AdLoadException r1, DataSpec r2) {
            }

            public static void $default$onAdPlaybackState(EventListener r0, AdPlaybackState r1) {
            }

            public static void $default$onAdTapped(EventListener r0) {
            }
        }

        void onAdClicked();

        void onAdLoadError(AdsMediaSource.AdLoadException r1, DataSpec r2);

        void onAdPlaybackState(AdPlaybackState r1);

        void onAdTapped();
    }

    public interface Provider {
        AdsLoader getAdsLoader(MediaItem.AdsConfiguration r1);
    }

    void handlePrepareComplete(AdsMediaSource r1, int r2, int r3);

    void handlePrepareError(AdsMediaSource r1, int r2, int r3, IOException r4);

    void release();

    void setPlayer(Player r1);

    void setSupportedContentTypes(int... r1);

    void start(AdsMediaSource r1, DataSpec r2, Object r3, AdViewProvider r4, EventListener r5);

    void stop(AdsMediaSource r1, EventListener r2);
}
