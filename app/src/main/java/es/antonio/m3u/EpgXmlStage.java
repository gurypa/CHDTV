package es.antonio.m3u;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* JADX INFO: loaded from: classes7.dex */
final class EpgXmlStage {

    static final class Meter extends FilterInputStream {
        long bytes;
        long nanos;

        Meter(InputStream r1) {
            super(r1);
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            long r0 = System.nanoTime();
            int r2 = this.in.read();     // Catch: Throwable -> L8
            if (r2 < 0) goto L6;
            this.bytes++;     // Catch: Throwable -> L8
        L6:
            this.nanos += System.nanoTime() - r0;
            return r2;
        L8:
            th = move-exception;
            this.nanos += System.nanoTime() - r0;
            throw th;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] r5, int r6, int r7) throws IOException {
            long r0 = System.nanoTime();
            int r8 = this.in.read(r5, r6, r7);     // Catch: Throwable -> L8
            if (r8 <= 0) goto L6;
            this.bytes += (long) r8;     // Catch: Throwable -> L8
        L6:
            this.nanos += System.nanoTime() - r0;
            return r8;
        L8:
            th = move-exception;
            this.nanos += System.nanoTime() - r0;
            throw th;
        }
    }

    private EpgXmlStage() {
    }

    static EpgData parse(InputStream r8, long r9) throws Exception {
        long r1 = System.nanoTime();
        Meter r3 = new Meter(r8);
        BlackBox.event("EPG_XML_START", "");
        EpgData r10 = EpgStreamingParser.parse(r3, r9);     // Catch: Exception -> L5
        BlackBox.event("EPG_XML_READY", "duration_ms=" + ((System.nanoTime() - r1) / 1000000) + " input_read_ms=" + (r3.nanos / 1000000) + " xml_bytes=" + r3.bytes + " programmes=" + r10.count());     // Catch: Exception -> L5
        return r10;
    L5:
        e = move-exception;
        BlackBox.event("EPG_XML_ERROR", "duration_ms=" + ((System.nanoTime() - r1) / 1000000) + " type=" + e.getClass().getSimpleName());
        throw e;
    }
}
