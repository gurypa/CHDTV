package es.antonio.m3u;

import android.os.Handler;
import android.os.Looper;
import java.util.List;
import java.util.concurrent.Executor;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes7.dex */
final class DemandTmdbDetails implements Runnable {
    final Entry entry;
    final Executor executor;
    final JSONObject item;
    final TmdbService.Results result;
    final TmdbService service;

    DemandTmdbDetails(TmdbService r1, Executor r2, Entry r3, JSONObject r4, TmdbService.Results r5) {
        this.service = r1;
        this.executor = r2;
        this.entry = r3;
        this.item = r4;
        this.result = r5;
    }

    /* JADX INFO: renamed from: lambda$1$es-antonio-m3u-DemandTmdbDetails, reason: not valid java name */
    /* synthetic */ void m562lambda$1$esantoniom3uDemandTmdbDetails(Handler r2, final List r3, final String r4) {
        if (this.service.detailsActive() == false) goto L6;
        r2.post(new DemandTmdbDetails$$ExternalSyntheticLambda0(this, r3, r4));
        return;
    }

    /* JADX INFO: renamed from: lambda$2$es-antonio-m3u-DemandTmdbDetails, reason: not valid java name */
    /* synthetic */ void m563lambda$2$esantoniom3uDemandTmdbDetails(List r2, String r3) {
        if (this.service.detailsActive() == false) goto L6;
        this.result.done(r2, r3);
        return;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.service.detailsActive() == true) goto L5;
        return;
    L5:
        final Handler r0 = new Handler(Looper.getMainLooper());
        this.service.detailsOriginal(new DemandTmdbDetails$$ExternalSyntheticLambda1(), this.entry, this.item, new DemandTmdbDetails$$ExternalSyntheticLambda2(this, r0));
    }
}
