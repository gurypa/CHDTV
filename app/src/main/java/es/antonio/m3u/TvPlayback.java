package es.antonio.m3u;

/* JADX INFO: loaded from: classes7.dex */
final class TvPlayback {

    static final class Scope implements Runnable {
        final PlayerActivity activity;
        final Entry selected;
        final int ticket;

        Scope(PlayerActivity r1, Entry r2, int r3) {
            this.activity = r1;
            this.selected = r2;
            this.ticket = r3;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.activity.tvTokenScope(this.selected, this.ticket);
        }
    }

    private TvPlayback() {
    }

    static boolean canPrime(boolean r0, boolean r1, boolean r2, boolean r3, boolean r4, boolean r5, boolean r6, boolean r7, long r8, long r10, boolean r12) {
        if (r0 == false) goto L15;
        if (r1 == true) goto L17;
        if (r2 == false) goto L18;
        if (r3 == true) goto L19;
        if (r4 == true) goto L20;
        if (r5 == true) goto L21;
        if (r6 == true) goto L22;
        if (r7 == false) goto L11;
        return false;
    L11:
        if (r8 > r10) goto L24;
        if (r12 == false) goto L25;
        return true;
    L25:
        return false;
    L24:
        return false;
    L22:
        return false;
    L21:
        return false;
    L20:
        return false;
    L19:
        return false;
    L18:
        return false;
    L17:
        return false;
    L15:
        return false;
    }
}
