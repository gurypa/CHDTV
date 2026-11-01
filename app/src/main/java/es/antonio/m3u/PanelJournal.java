package es.antonio.m3u;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes7.dex */
final class PanelJournal {
    static final long CHUNK = 2097152;
    static final int CHUNKS = 16;
    private File active;
    private final int chunkCount;
    private final long chunkSize;
    private long cursorChunk;
    private long cursorOffset;
    final File folder;
    private long lostBytes;

    static final class Batch {
        long chunk;
        long offset;
        final List<String> rows;

        Batch() {
            this.rows = new ArrayList();
        }
    }

    PanelJournal(File r4) throws IOException {
        this(r4, 2097152, 16);
    }

    PanelJournal(File r1, long r2, int r4) throws IOException {
        this.folder = r1;
        this.chunkSize = r2;
        this.chunkCount = r4;
        if (r1.isDirectory() == false) goto L5;
    L9:
        readState();
        List<File> r3 = files();
        if (r3.isEmpty() == true) goto L12;
        this.active = r3.get(r3.size() - 1);
    L12:
        repairTail();
        return;
    L5:
        if (r1.mkdirs() == true) goto L9;
        throw new IOException("Outbox unavailable");
    }

    static boolean covers(Batch r4, Batch r5) {
        if (r4.chunk <= r5.chunk) goto L5;
        return true;
    L5:
        if (r4.chunk == r5.chunk) goto L7;
        return false;
    L7:
        if (r4.offset >= r5.offset) goto L13;
        return false;
    L13:
        return true;
    }

    private List<File> files() {
        File[] r0 = this.folder.listFiles(new PanelJournal$$ExternalSyntheticLambda0());
        if (r0 != null) goto L5;
        ArrayList r1 = new ArrayList();
    L6:
        Collections.sort(r1, Comparator.comparing(new PanelJournal$$ExternalSyntheticLambda1()));
        return r1;
    L5:
        r1 = new ArrayList(Arrays.asList(r0));
        goto L6
    }

    static /* synthetic */ boolean lambda$0(File r0, String r1) {
        return r1.matches("chunk-[0-9]{20}\\.jsonl");
    }

    private static long number(File r2) {
        return Long.parseLong(r2.getName().substring(6, 26));
    }

    private void readState() {
        String[] r0 = {"cursor.bin", "cursor.previous"};
        int r1 = 0;
    L4:
        if (r1 >= 2) goto L5;
        Throwable r5 = null;
        DataInputStream r6 = new DataInputStream(new FileInputStream(new File(this.folder, r0[r1])));     // Catch: Throwable -> L29
    L25:
        th = move-exception;
        r6.close();     // Catch: Throwable -> L29
        throw th;     // Catch: Throwable -> L29
    L11:
        if (r6.readInt() == 20501) goto L14;
        r6.close();     // Catch: Throwable -> L29
    L36:
        r1 = r1 + 1;
        goto L4
    L14:
        this.cursorChunk = r6.readLong();     // Catch: Throwable -> L25
        this.cursorOffset = r6.readLong();     // Catch: Throwable -> L25
        this.lostBytes = r6.readLong();     // Catch: Throwable -> L25
        if (this.cursorChunk < 0) goto L24;
        if (this.cursorOffset < 0) goto L24;
        if (this.lostBytes < 0) goto L24;
        r6.close();     // Catch: Throwable -> L29
        return;
    L24:
        throw new IOException();     // Catch: Throwable -> L25
    L29:
        th = th;
        if (0 == 0) goto L34;
        if (null != th) goto L39;
    L33:
        th = null;
        goto L34
    L39:
        r5.addSuppressed(th);     // Catch: IOException -> L35
    L34:
        throw th;     // Catch: IOException -> L35
    L5:
        this.lostBytes = 0;
        this.cursorOffset = 0;
        this.cursorChunk = 0;
    }

    private void repairTail() throws IOException {
        if (this.active != null) goto L5;
        return;
    L5:
        if (this.active.length() == 0) goto L41;
        Throwable r0 = null;
        RandomAccessFile r1 = new RandomAccessFile(this.active, "rw");     // Catch: Throwable -> L27
        long r4 = r1.length();     // Catch: Throwable -> L24
        long r8 = r4 - 1;     // Catch: Throwable -> L24
        r1.seek(r8);     // Catch: Throwable -> L24
        if (r1.read() != 10) goto L15;
        r1.close();     // Catch: Throwable -> L27
        return;
    L15:
        if (r8 < 0) goto L20;
        r1.seek(r8);     // Catch: Throwable -> L24
        if (r1.read() == 10) goto L20;
        r8 = r8 - 1;     // Catch: Throwable -> L27
    L20:
        this.lostBytes += (r4 - r8) - 1;     // Catch: Throwable -> L24
        r1.setLength(r8 + 1);     // Catch: Throwable -> L24
        saveState();     // Catch: Throwable -> L24
        r1.close();     // Catch: Throwable -> L27
        return;
    L24:
        th = move-exception;
        r1.close();     // Catch: Throwable -> L27
        throw th;     // Catch: Throwable -> L27
    L27:
        th = move-exception;
        if (0 == 0) goto L32;
        if (null == th) goto L39;
        r0.addSuppressed(th);
        throw null;
    L39:
        throw null;
    L32:
        throw th;
    }

    private void saveState() throws IOException {
        File r0 = new File(this.folder, "cursor.tmp");
        File r1 = new File(this.folder, "cursor.bin");
        File r2 = new File(this.folder, "cursor.previous");
        Throwable r3 = null;
        FileOutputStream r4 = new FileOutputStream(r0);     // Catch: Throwable -> L39
        DataOutputStream r5 = new DataOutputStream(r4);     // Catch: Throwable -> L32
        r5.writeInt(20501);     // Catch: Throwable -> L28
        r5.writeLong(this.cursorChunk);     // Catch: Throwable -> L28
        r5.writeLong(this.cursorOffset);     // Catch: Throwable -> L28
        r5.writeLong(this.lostBytes);     // Catch: Throwable -> L28
        r5.flush();     // Catch: Throwable -> L28
        r4.getFD().sync();     // Catch: Throwable -> L28
        r5.close();     // Catch: Throwable -> L32
        r4.close();     // Catch: Throwable -> L39
        if (r1.exists() == false) goto L24;
        if (r2.exists() == false) goto L19;
        if (r2.delete() == true) goto L19;
        throw new IOException();
    L19:
        if (r1.renameTo(r2) == true) goto L24;
        throw new IOException();
    L24:
        if (r0.renameTo(r1) == false) goto L27;
        return;
    L27:
        throw new IOException();
    L28:
        th = move-exception;
        r5.close();     // Catch: Throwable -> L32
        throw th;     // Catch: Throwable -> L32
    L32:
        th = move-exception;
        if (0 == 0) goto L36;
        if (null == th) goto L37;
        r3.addSuppressed(th);     // Catch: Throwable -> L39
    L37:
        r4.close();     // Catch: Throwable -> L39
        throw r3;     // Catch: Throwable -> L39
    L36:
        r3 = th;
    L39:
        th = move-exception;
        if (0 == 0) goto L49;
        if (null == th) goto L44;
        r3.addSuppressed(th);
    L44:
        throw null;
    }

    synchronized void acknowledge(Batch r3) throws IOException {
        monitor-enter(this);
        this.cursorChunk = r3.chunk;     // Catch: Throwable -> L6
        this.cursorOffset = r3.offset;     // Catch: Throwable -> L6
        saveState();     // Catch: Throwable -> L6
        monitor-exit(this);
        return;
    L6:
        th = move-exception;
        throw th;
    }

    synchronized void append(String r11) throws IOException {
        monitor-enter(this);
        byte[] r12 = (String.valueOf(r11) + "\n").getBytes(StandardCharsets.UTF_8);     // Catch: Throwable -> L49
        if (r12.length > 32768) goto L60;
        if (this.active != null) goto L8;
    L9:
        long r3 = 1;
        if (this.active == null) goto L13;
        r3 = 1 + number(this.active);     // Catch: Throwable -> L49
    L13:
        this.active = new File(this.folder, String.format(Locale.ROOT, "chunk-%020d.jsonl", new Object[]{Long.valueOf(r3)}));     // Catch: Throwable -> L49
    L14:
        Throwable r0 = null;
        FileOutputStream r4 = new FileOutputStream(this.active, true);     // Catch: Throwable -> L42
        r4.write(r12);     // Catch: Throwable -> L39
        r4.close();     // Catch: Throwable -> L42
        List<File> r13 = files();     // Catch: Throwable -> L49
    L20:
        if (r13.size() <= this.chunkCount) goto L21;
        File r1 = r13.remove(0);     // Catch: Throwable -> L49
        long r2 = number(r1);     // Catch: Throwable -> L49
        long r5 = r1.length();     // Catch: Throwable -> L49
        if (r2 <= this.cursorChunk) goto L26;
    L29:
        long r6 = this.lostBytes;     // Catch: Throwable -> L49
        if (r2 != this.cursorChunk) goto L32;
        long r7 = this.cursorOffset;     // Catch: Throwable -> L49
    L33:
        this.lostBytes = r6 + Math.max(0, r5 - r7);     // Catch: Throwable -> L49
    L35:
        if (r1.delete() == false) goto L38;
        saveState();     // Catch: Throwable -> L49
        goto L20
    L38:
        throw new IOException("Outbox rotation failed");     // Catch: Throwable -> L49
    L32:
        r7 = 0;
        goto L33
    L26:
        if (r2 != this.cursorChunk) goto L35;
        if (r5 <= this.cursorOffset) goto L35;
    L21:
        monitor-exit(this);
        return;
    L39:
        th = move-exception;
        r4.close();     // Catch: Throwable -> L42
        throw th;     // Catch: Throwable -> L42
    L42:
        th = move-exception;
        if (0 == 0) goto L58;
        if (null == th) goto L59;
        r0.addSuppressed(th);     // Catch: Throwable -> L49
    L59:
        throw null;     // Catch: Throwable -> L49
    L58:
        throw th;     // Catch: Throwable -> L49
    L8:
        if ((this.active.length() + ((long) r12.length)) <= this.chunkSize) goto L14;
    L60:
        throw new IOException("Outbox record too large");     // Catch: Throwable -> L49
    L49:
        th = move-exception;
        throw th;
    }

    synchronized long lostBytes() {
        monitor-enter(this);
        long r0 = this.lostBytes;     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    synchronized Batch mark() {
        monitor-enter(this);
        Batch r0 = new Batch();     // Catch: Throwable -> L14
        long r2 = 0;
        if (this.active != null) goto L6;
        long r4 = 0;
    L7:
        r0.chunk = r4;     // Catch: Throwable -> L14
        if (this.active == null) goto L11;
        r2 = this.active.length();     // Catch: Throwable -> L14
    L11:
        r0.offset = r2;     // Catch: Throwable -> L14
        monitor-exit(this);
        return r0;
    L6:
        r4 = number(this.active);     // Catch: Throwable -> L14
    L14:
        th = move-exception;
        throw th;
    }

    synchronized long pendingBytes() {
        monitor-enter(this);
        Iterator<File> r0 = files().iterator();     // Catch: Throwable -> L16
        long r3 = 0;
    L4:
        if (r0.hasNext() == false) goto L6;
        File r5 = r0.next();     // Catch: Throwable -> L16
        long r6 = number(r5);     // Catch: Throwable -> L16
        if (r6 < this.cursorChunk) goto L4;
        long r8 = r5.length();     // Catch: Throwable -> L16
        if (r6 != this.cursorChunk) goto L13;
        long r7 = this.cursorOffset;     // Catch: Throwable -> L16
    L15:
        r3 = r3 + Math.max(0, r8 - r7);
        goto L4
    L13:
        r7 = 0;
        goto L15
    L6:
        monitor-exit(this);
        return r3;
    L16:
        th = move-exception;
        throw th;
    }

    synchronized Batch read(int r13) throws IOException {
        monitor-enter(this);
        Batch r0 = new Batch();     // Catch: Throwable -> L42
        r0.chunk = this.cursorChunk;     // Catch: Throwable -> L42
        r0.offset = this.cursorOffset;     // Catch: Throwable -> L42
        Iterator<File> r1 = files().iterator();     // Catch: Throwable -> L42
        int r2 = 0;
    L5:
        if (r1.hasNext() == false) goto L6;
        File r3 = r1.next();     // Catch: Throwable -> L42
        long r4 = number(r3);     // Catch: Throwable -> L42
        if (r4 < this.cursorChunk) goto L5;
        Throwable r6 = null;
        RandomAccessFile r7 = new RandomAccessFile(r3, "r");     // Catch: Throwable -> L36
        if (r4 != this.cursorChunk) goto L17;
        long r8 = Math.min(this.cursorOffset, r7.length());     // Catch: Throwable -> L33
    L18:
        r7.seek(r8);     // Catch: Throwable -> L33
    L19:
        String r5 = r7.readLine();     // Catch: Throwable -> L33
        if (r5 == null) goto L21;
        String r9 = new String(r5.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);     // Catch: Throwable -> L33
        int r10 = r9.getBytes(StandardCharsets.UTF_8).length;     // Catch: Throwable -> L33
        if (r0.rows.isEmpty() == false) goto L26;
    L29:
        r2 = r2 + r10;
        r0.chunk = r4;     // Catch: Throwable -> L33
        r0.offset = r7.getFilePointer();     // Catch: Throwable -> L33
        r0.rows.add(r9);     // Catch: Throwable -> L33
        if (r0.rows.size() < r13) goto L19;
        r7.close();     // Catch: Throwable -> L36
        goto L6
    L26:
        if ((r2 + r10) <= 184320) goto L29;
        r7.close();     // Catch: Throwable -> L36
        goto L6
    L21:
        r7.close();     // Catch: Throwable -> L36
        goto L5
    L17:
        r8 = 0;
        goto L18
    L33:
        th = move-exception;
        r7.close();     // Catch: Throwable -> L36
        throw th;     // Catch: Throwable -> L36
    L36:
        th = move-exception;
        if (0 == 0) goto L58;
        if (null == th) goto L41;
        r6.addSuppressed(th);     // Catch: Throwable -> L42
    L41:
        throw null;     // Catch: Throwable -> L42
    L58:
        throw th;     // Catch: Throwable -> L42
    L6:
        monitor-exit(this);
        return r0;
    L42:
        th = move-exception;
        throw th;
    }

    synchronized void rewind() throws IOException {
        monitor-enter(this);
        this.cursorChunk = 0;     // Catch: Throwable -> L7
        this.cursorOffset = 0;     // Catch: Throwable -> L7
        saveState();     // Catch: Throwable -> L7
        monitor-exit(this);
        return;
    L7:
        th = move-exception;
        throw th;
    }
}
