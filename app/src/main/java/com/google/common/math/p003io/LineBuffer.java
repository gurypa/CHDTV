package com.google.common.p003io;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
abstract class LineBuffer {
    private StringBuilder line;
    private boolean sawReturn;

    LineBuffer() {
        this.line = new StringBuilder();
    }

    private boolean finishLine(boolean r3) throws IOException {
        if (this.sawReturn == false) goto L7;
        if (r3 == false) goto L6;
        String r0 = "\r\n";
    L10:
        handleLine(this.line.toString(), r0);
        this.line = new StringBuilder();
        this.sawReturn = false;
        return r3;
    L6:
        r0 = "\r";
        goto L10
    L7:
        if (r3 == false) goto L9;
        r0 = "\n";
        goto L10
    L9:
        r0 = "";
        goto L10
    }

    protected void add(char[] r9, int r10, int r11) throws IOException {
        int r0 = r10;
        if (this.sawReturn == false) goto L12;
        if (r11 <= 0) goto L12;
        if (r9[r0] != '\n') goto L8;
        boolean r1 = true;
    L10:
        if (finishLine(r1) == false) goto L12;
        r0 = r0 + 1;
        goto L12
    L8:
        r1 = false;
    L12:
        int r2 = r0;
        int r5 = r10 + r11;
    L13:
        if (r0 >= r5) goto L29;
        switch(r9[r0]) {
            case 10: goto L27;
            case 13: goto L17;
            default: goto L28;
        };
    L17:
        this.line.append(r9, r2, r0 - r2);
        this.sawReturn = true;
        if ((r0 + 1) < r5) goto L20;
    L26:
        r2 = r0 + 1;
        goto L28
    L20:
        if (r9[r0 + 1] != '\n') goto L22;
        boolean r6 = true;
    L24:
        if (finishLine(r6) == false) goto L26;
        r0 = r0 + 1;
        goto L26
    L22:
        r6 = false;
        goto L24
    L27:
        this.line.append(r9, r2, r0 - r2);
        finishLine(true);
        r2 = r0 + 1;
    L28:
        r0 = r0 + 1;
        goto L13
    L29:
        this.line.append(r9, r2, (r10 + r11) - r2);
    }

    protected void finish() throws IOException {
        if (this.sawReturn == false) goto L5;
    L6:
        finishLine(false);
        return;
    L5:
        if (this.line.length() > 0) goto L6;
    }

    protected abstract void handleLine(String r1, String r2) throws IOException;
}
