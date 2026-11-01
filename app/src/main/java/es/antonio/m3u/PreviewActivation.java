package es.antonio.m3u;

/* JADX INFO: loaded from: classes3.dex */
final class PreviewActivation {
    private boolean armed;
    private String selected;

    PreviewActivation() {
        this.selected = "";
    }

    boolean activate(String r4) {
        select(r4);
        if (this.armed == true) goto L6;
        this.armed = true;
        return false;
    L6:
        this.armed = false;
        return true;
    }

    void reset() {
        this.selected = "";
        this.armed = false;
    }

    void select(String r2) {
        if (this.selected.equals(r2) == true) goto L6;
        this.selected = r2;
        this.armed = false;
        return;
    }
}
