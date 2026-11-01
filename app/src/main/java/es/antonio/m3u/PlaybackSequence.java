package es.antonio.m3u;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: loaded from: classes3.dex */
public final class PlaybackSequence {
    public final List<Entry> entries;
    public final int index;

    private PlaybackSequence() {
        this.entries = Collections.emptyList();
        this.index = 0;
    }

    private PlaybackSequence(List<Entry> r1, int r2) {
        this.entries = r1;
        this.index = r2;
    }

    public PlaybackSequence(List<Entry> r2, Entry r3) {
        this(r2, r3, Collections.emptyMap());
    }

    public PlaybackSequence(List<Entry> r6, final Entry r7, Map<Entry, String> r8) {
        String r0 = providerFor(r6, r7, r8);
        this.entries = new ArrayList();
        Iterator<Entry> r1 = r6.iterator();
    L4:
        if (r1.hasNext() == false) goto L20;
        Entry r2 = r1.next();
        if (r2.kind.equals(r7.kind) == false) goto L4;
        if (r7.kind.equals("Series") == false) goto L12;
        if (Objects.equals(CatalogCounts.series(r2), CatalogCounts.series(r7)) == false) goto L4;
    L12:
        if ("Películas".equals(r7.kind) == true) goto L19;
        if (r8.containsKey(r2) == false) goto L16;
        String r3 = r8.get(r2);
    L18:
        if (r0.equals(r3) == false) goto L4;
    L16:
        r3 = Providers.name(r2, "");
    L19:
        this.entries.add(r2);
        goto L4
    L20:
        boolean r4 = false;
        Iterator<Entry> r5 = this.entries.iterator();
    L22:
        if (r5.hasNext() == false) goto L27;
        if (same(r5.next(), r7) == false) goto L22;
        r4 = true;
    L27:
        if (r4 == true) goto L30;
        this.entries.add(r7);
    L30:
        if (r7.kind.equals("Canales") == true) goto L32;
        Collections.sort(this.entries, new PlaybackSequence$$ExternalSyntheticLambda0(r7));
    L32:
        int r9 = 0;
        int r10 = 0;
    L34:
        if (r10 >= this.entries.size()) goto L39;
        if (same(this.entries.get(r10), r7) == true) goto L37;
        r10 = r10 + 1;
        goto L34
    L37:
        r9 = r10;
    L39:
        this.index = r9;
    }

    public static PlaybackSequence fromOrdered(List<Entry> r1, int r2) {
        return new PlaybackSequence(r1, r2);
    }

    static /* synthetic */ int lambda$new$0(Entry r4, Entry r5, Entry r6) {
        if (r4.kind.equals("Series") == false) goto L5;
        int r0 = 0;
    L6:
        if (r0 != 0) goto L8;
        r0 = Integer.compare(r5.season, r6.season);
    L8:
        if (r0 != 0) goto L10;
        r0 = Integer.compare(r5.episode, r6.episode);
    L10:
        if (r0 == 0) goto L12;
        return r0;
    L12:
        return String.CASE_INSENSITIVE_ORDER.compare(r5.title, r6.title);
    L5:
        r0 = String.CASE_INSENSITIVE_ORDER.compare(r5.group, r6.group);
        goto L6
    }

    public static String providerFor(List<Entry> r4, Entry r5, Map<Entry, String> r6) {
        String r0 = r6.get(r5);
        if (r0 == null) goto L5;
        return r0;
    L5:
        Iterator<Entry> r1 = r4.iterator();
    L7:
        if (r1.hasNext() == false) goto L14;
        Entry r2 = r1.next();
        if (same(r2, r5) == false) goto L7;
        String r3 = r6.get(r2);
        if (r3 == null) goto L7;
        return r3;
    L14:
        return Providers.name(r5, "");
    }

    public static boolean same(Entry r2, Entry r3) {
        if (r2.identity().equals(r3.identity()) == true) goto L5;
    L7:
        return false;
    L5:
        if (Entry.details(r2).url.equals(Entry.details(r3).url) == false) goto L7;
        return true;
    }

    public Entry neighbor(int r3) {
        int r0 = this.index + r3;
        if (r0 >= 0) goto L5;
    L8:
        return null;
    L5:
        if (r0 >= this.entries.size()) goto L8;
        return this.entries.get(r0);
    }

    PlaybackSequence selectExisting(Entry r4) {
        if ((this.entries instanceof DemandMovies.MovieEntries) == false) goto L9;
        int r5 = ((DemandMovies.MovieEntries) this.entries).find(r4);
        if (r5 >= 0) goto L8;
        return null;
    L8:
        return new PlaybackSequence(this.entries, r5);
    L9:
        int r0 = 0;
    L11:
        if (r0 >= this.entries.size()) goto L12;
        if (same(this.entries.get(r0), r4) == true) goto L16;
        r0 = r0 + 1;
        goto L11
    L16:
        return new PlaybackSequence(this.entries, r0);
    L12:
        return null;
    }
}
