package es.antonio.m3u;

import android.os.Handler;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import org.json.JSONArray;

/* JADX INFO: loaded from: classes7.dex */
final class DemandRelated {

    static final class Job implements Runnable {
        final TmdbService.RelatedResults callback;
        final List<Entry> catalog;
        final Entry current;
        final Handler main;
        final JSONArray recommendations;
        final TmdbService service;

        Job(Handler r1, TmdbService r2, List<Entry> r3, Entry r4, JSONArray r5, TmdbService.RelatedResults r6) {
            this.main = r1;
            this.service = r2;
            this.catalog = r3;
            this.current = r4;
            this.recommendations = r5;
            this.callback = r6;
        }

        @Override // java.lang.Runnable
        public void run() {
            List<RelatedTitles.Result> r0 = new RelatedTitles(SectionStore.related(this.catalog, this.current), this.current, this.service).resolve(this.recommendations);     // Catch: Exception -> L4
            String r1 = null;
        L6:
            this.main.post(new Ready(this.callback, r0, r1));
            return;
        L5:
            r0 = Collections.emptyList();
            r1 = "No se pudieron cargar los relacionados";
            goto L6
        }
    }

    static final class Ready implements Runnable {
        final TmdbService.RelatedResults callback;
        final String error;
        final List<RelatedTitles.Result> result;

        Ready(TmdbService.RelatedResults r1, List<RelatedTitles.Result> r2, String r3) {
            this.callback = r1;
            this.result = r2;
            this.error = r3;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.callback.done(this.result, this.error);
        }
    }

    DemandRelated() {
    }

    static void request(ExecutorService r8, Handler r9, TmdbService r10, List<Entry> r11, Entry r12, JSONArray r13, TmdbService.RelatedResults r14) {
        r8.execute(new Job(r9, r10, r11, r12, r13, r14));     // Catch: RejectedExecutionException -> L4
        return;
    L5:
        r14.done(Collections.emptyList(), "Relacionados ocupados. Abre la ficha de nuevo");
    }
}
