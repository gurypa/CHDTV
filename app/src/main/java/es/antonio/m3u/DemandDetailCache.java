package es.antonio.m3u;

import android.os.Handler;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes7.dex */
final class DemandDetailCache {

    static final class CachedItems extends AbstractList<JSONObject> {
        final JSONObject saved;

        CachedItems(JSONObject r1) {
            this.saved = r1;
        }

        @Override // java.util.AbstractList, java.util.List
        public /* bridge */ /* synthetic */ Object get(int r1) {
            return get(r1);
        }

        @Override // java.util.AbstractList, java.util.List
        public JSONObject get(int r1) {
            if (r1 != 0) goto L6;
            return this.saved;
        L6:
            throw new IndexOutOfBoundsException();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return 1;
        }
    }

    static final class Dispatch implements TmdbService.Results {
        final Handler main;
        final TmdbService.Results result;
        final TmdbService service;

        Dispatch(Handler r1, TmdbService r2, TmdbService.Results r3) {
            this.main = r1;
            this.service = r2;
            this.result = r3;
        }

        @Override // es.antonio.m3u.TmdbService.Results
        public void done(List<JSONObject> r5, String r6) {
            this.main.post(new Result(this.service, this.result, r5, r6));
        }
    }

    static final class Load implements Runnable {
        final Entry entry;
        final Handler main;
        final TmdbService.Results result;
        final TmdbService service;

        Load(Handler r1, TmdbService r2, Entry r3, TmdbService.Results r4) {
            this.main = r1;
            this.service = r2;
            this.entry = r3;
            this.result = r4;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.service.detailsActive() == true) goto L5;
            return;
        L5:
            JSONObject r0 = this.service.cached(this.entry);
            if (this.service.detailsActive() == true) goto L8;
            return;
        L8:
            if (r0 != null) goto L11;
            this.service.find(this.entry, new Dispatch(this.main, this.service, this.result));
            return;
        L11:
            this.main.post(new Ready(this.service, this.result, r0));
        }
    }

    static final class Ready implements Runnable {
        final TmdbService.Results result;
        final JSONObject saved;
        final TmdbService service;

        Ready(TmdbService r1, TmdbService.Results r2, JSONObject r3) {
            this.service = r1;
            this.result = r2;
            this.saved = r3;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.service.detailsActive() == false) goto L6;
            this.result.done(new CachedItems(this.saved), null);
            return;
        }
    }

    static final class Result implements Runnable {
        final String error;
        final List<JSONObject> items;
        final TmdbService.Results result;
        final TmdbService service;

        Result(TmdbService r1, TmdbService.Results r2, List<JSONObject> r3, String r4) {
            this.service = r1;
            this.result = r2;
            this.items = r3;
            this.error = r4;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.service.detailsActive() == false) goto L6;
            this.result.done(this.items, this.error);
            return;
        }
    }

    DemandDetailCache() {
    }

    static JSONObject exact(Entry r1, List<JSONObject> r2) {
        if ((r2 instanceof CachedItems) == false) goto L6;
        return r2.get(0);
    L6:
        return TmdbService.exact(r1, r2);
    }

    static void request(ExecutorService r1, Handler r2, TmdbService r3, Entry r4, TmdbService.Results r5) {
        r1.execute(new Load(r2, r3, r4, r5));     // Catch: RejectedExecutionException -> L4
        return;
    L5:
        r2.post(new Result(r3, r5, Collections.emptyList(), "TMDB está ocupado. Vuelve a intentarlo"));
    }
}
