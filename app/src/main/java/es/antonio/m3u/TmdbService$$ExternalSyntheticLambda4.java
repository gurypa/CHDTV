package es.antonio.m3u;

import java.util.List;
import org.json.JSONArray;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes3.dex */
public final /* synthetic */ class TmdbService$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ TmdbService f$0;
    public final /* synthetic */ List f$1;
    public final /* synthetic */ Entry f$2;
    public final /* synthetic */ JSONArray f$3;
    public final /* synthetic */ TmdbService.RelatedResults f$4;

    public /* synthetic */ TmdbService$$ExternalSyntheticLambda4(TmdbService r1, List r2, Entry r3, JSONArray r4, TmdbService.RelatedResults r5) {
        this.f$0 = r1;
        this.f$1 = r2;
        this.f$2 = r3;
        this.f$3 = r4;
        this.f$4 = r5;
    }

    @Override // java.lang.Runnable
    public final void run() {
        TmdbService.$r8$lambda$zPPpwsA0chlSOBY8TRkX7HwRyXs(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
