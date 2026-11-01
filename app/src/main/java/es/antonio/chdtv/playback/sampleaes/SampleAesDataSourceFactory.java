package es.antonio.chdtv.playback.sampleaes;

import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.hls.HlsDataSourceFactory;
import es.antonio.m3u.BlackBox;
import es.antonio.m3u.HlsDiagnostics225;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/* JADX INFO: loaded from: classes5.dex */
public final class SampleAesDataSourceFactory implements HlsDataSourceFactory {
    private final Decryptor decryptor;
    private final Map<String, SampleAesManifest.Segment> diagnosticPrevious;
    private final long diagnosticRun;
    private final AtomicLong diagnosticSegments;
    private final AtomicBoolean firstTiming;
    private final SampleAesManifest manifest;
    private final DataSource.Factory network;

    public interface Decryptor {
        byte[] decrypt(byte[] r1, byte[] r2, byte[] r3) throws IOException;
    }

    private static final class SegmentBuffer extends ByteArrayOutputStream {
        SegmentBuffer(int r1) {
            super(r1);
        }

        byte[] payload() {
            if (this.count != this.buf.length) goto L6;
            return this.buf;
        L6:
            return toByteArray();
        }
    }

    private final class SegmentSource implements DataSource {
        private static final int MAX_SEGMENT = 33554432;
        private byte[] clear;
        private int cursor;
        private int end;
        private boolean opened;
        final /* synthetic */ SampleAesDataSourceFactory this$0;
        private final DataSource upstream;

        SegmentSource(SampleAesDataSourceFactory r1, DataSource r2) {
            this.this$0 = r1;
            this.upstream = r2;
        }

        @Override // androidx.media3.datasource.DataSource
        public void addTransferListener(TransferListener r2) {
            this.upstream.addTransferListener(r2);
        }

        @Override // androidx.media3.datasource.DataSource
        public void close() throws IOException {
            if (this.clear == null) goto L5;
            Arrays.fill(this.clear, (byte) 0);
        L5:
            this.clear = null;
            this.cursor = 0;
            this.end = 0;
            if (this.opened == false) goto L9;
            this.opened = false;
            this.upstream.close();
            return;
        }

        @Override // androidx.media3.datasource.DataSource
        public Map<String, List<String>> getResponseHeaders() {
            return this.upstream.getResponseHeaders();
        }

        @Override // androidx.media3.datasource.DataSource
        public Uri getUri() {
            return this.upstream.getUri();
        }

        /* JADX WARN: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: TypeSearchVarInfo not found in map for var: r1v0 ??
        	at jadx.core.dex.visitors.typeinference.TypeSearchState.getVarInfo(TypeSearchState.java:34)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.lambda$resolveIndependentVariables$1(TypeSearch.java:173)
        	at java.base/java.util.stream.MatchOps$1MatchSink.accept(MatchOps.java:90)
        	at java.base/java.util.ArrayList$ArrayListSpliterator.tryAdvance(ArrayList.java:1602)
        	at java.base/java.util.stream.ReferencePipeline$7$1.accept(ReferencePipeline.java:280)
        	at java.base/java.util.ArrayList$ArrayListSpliterator.tryAdvance(ArrayList.java:1602)
        	at java.base/java.util.stream.ReferencePipeline.forEachWithCancel(ReferencePipeline.java:129)
        	at java.base/java.util.stream.AbstractPipeline.copyIntoWithCancel(AbstractPipeline.java:527)
        	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:513)
        	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
        	at java.base/java.util.stream.MatchOps$MatchOp.evaluateSequential(MatchOps.java:230)
        	at java.base/java.util.stream.MatchOps$MatchOp.evaluateSequential(MatchOps.java:196)
        	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
        	at java.base/java.util.stream.ReferencePipeline.allMatch(ReferencePipeline.java:637)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.resolveIndependentVariables(TypeSearch.java:173)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:63)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:119)
        	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
         */
        /* JADX WARN: Type inference failed for: r1v12 */
        /* JADX WARN: Type inference failed for: r3v1 */
        /* JADX WARN: Type inference failed for: r3v11 */
        /* JADX WARN: Type inference failed for: r3v2, types: [es.antonio.chdtv.playback.sampleaes.SampleAesDataSourceFactory$SegmentSource] */
        /* JADX WARN: Type inference failed for: r3v3 */
        /* JADX WARN: Type inference failed for: r3v34 */
        /* JADX WARN: Type inference failed for: r3v36 */
        /* JADX WARN: Type inference failed for: r3v38 */
        /* JADX WARN: Type inference failed for: r3v39 */
        /* JADX WARN: Type inference failed for: r3v40 */
        /* JADX WARN: Type inference failed for: r3v41 */
        /* JADX WARN: Type inference failed for: r3v42 */
        /* JADX WARN: Type inference failed for: r3v43 */
        /* JADX WARN: Type inference failed for: r3v44 */
        /* JADX WARN: Unreachable blocks removed: 1, instructions: 1 */
        /* JADX WARN: Unreachable blocks removed: 2, instructions: 8 */
        @Override // androidx.media3.datasource.DataSource
        public long open(DataSpec r57) throws IOException {
            Object r3 = " download_ms=";
            if (this.opened == true) goto L276;
            SampleAesManifest.Segment r4 = SampleAesDataSourceFactory.access$000(this.this$0).find(r57.uri.toString(), r57.position, r57.length);
            if (r4 != null) goto L7;
        L273:
            this.opened = true;
            return this.upstream.open(r57);
        L7:
            if (r4.encrypted() == false) goto L273;
            if (r4.length > 33554432) goto L272;
            this.opened = true;
            long r40 = SampleAesDataSourceFactory.access$100(this.this$0).incrementAndGet();
            String r6 = HlsDiagnostics225.track(r4.url);
            int r15 = 0;
            if (HlsDiagnostics225.active() == false) goto L14;
            int r42 = SampleAesDataSourceFactory.access$200(this.this$0, r6, r4);
        L15:
            long r43 = SystemClock.elapsedRealtime();
            String r10 = "DOWNLOAD";
            long r47 = 0;
            DataSpec r12 = r57.buildUpon().setPosition(r4.offset).setLength(r4.length).build();     // Catch: LinkageError -> L244 Throwable -> L246 IOException -> L248
            long r13 = SystemClock.elapsedRealtime();     // Catch: LinkageError -> L244 Throwable -> L246 IOException -> L248
            long r11 = this.upstream.open(r12);     // Catch: LinkageError -> L244 Throwable -> L246 IOException -> L248
            if (r11 <= 33554432) goto L291;
            ?? r5 = this;
            String r19 = "DOWNLOAD";
            long r54 = r11;
        L242:
            e = move-exception;
            Throwable r7 = e;
        L233:
            String r21 = r19;
            long r49 = r43;
            ?? r8 = r5;
        L234:
            long r31 = r54;
            ?? r9 = r8;
        L250:
            byte[] r22 = null;
            ?? r14 = r9;
        L251:
            long r16 = SystemClock.elapsedRealtime();
            long r17 = SampleAesDataSourceFactory.access$500(r14.this$0);
            long r18 = r4.diagnosticSequence;
            long r110 = r4.diagnosticDiscontinuity;
            int r0 = r4.diagnosticIvSource;
            long r1 = r4.offset;
            long r2 = r4.length;
            long r20 = r57.position;
            long r23 = r57.length;
            if (r22 != null) goto L254;
            r49 = r16;
        L254:
            long r33 = r49 - r43;
            if (r47 != 0) goto L257;
            long r35 = 0;
        L259:
            if (r4.key != null) goto L261;
            int r37 = 0;
        L263:
            if (r4.f170iv != null) goto L265;
            int r38 = 0;
        L266:
            HlsDiagnostics225.result(r17, r40, r6, r42, r18, r110, r0, r21, r22, r1, r2, r20, r23, r31, r33, r35, r37, r38, r7);
            close();     // Catch: IOException -> L269
            throw r7;
        L270:
            throw r7;
        L265:
            r38 = r4.f170iv.length;
            goto L266
        L261:
            r37 = r4.key.length;
            goto L263
        L257:
            r35 = r16 - r47;
            goto L259
        L237:
            throw new IOException("Sample-AES: el segmento supera 32 MB");     // Catch: LinkageError -> L238 RuntimeException -> L240 Throwable -> L242
        L291:
        L229:
            e = e;
            r19 = r10;
        L231:
            r54 = r11;
            int r24 = r15;
        L232:
            r7 = e;
            r5 = r1;
            goto L233
        L20:
            if (r11 <= 0) goto L22;
            int r25 = (int) r11;     // Catch: LinkageError -> L225 RuntimeException -> L227 Throwable -> L229
        L23:
            SegmentBuffer r26 = new SegmentBuffer(r25);     // Catch: LinkageError -> L225 RuntimeException -> L227 Throwable -> L229
            byte[] r27 = new byte[16384];     // Catch: LinkageError -> L225 RuntimeException -> L227 Throwable -> L229
        L24:
            r19 = r10;
            int r28 = this.upstream.read(r27, r15, 16384);     // Catch: LinkageError -> L219 RuntimeException -> L221 Throwable -> L223
            if (r28 == (-1)) goto L48;
        L45:
            e = e;
        L46:
            r7 = e;
            r9 = this;
            r31 = r11;
            r21 = r19;
            r49 = r43;
        L41:
            e = e;
        L43:
            e = e;
            goto L46
        L30:
            if (Thread.currentThread().isInterrupted() == true) goto L40;
            if (r28 <= 0) goto L38;
            if ((r26.size() + r28) > MAX_SEGMENT) goto L36;
            r26.write(r27, 0, r28);     // Catch: LinkageError -> L41 RuntimeException -> L43 IOException -> L45
            r10 = r19;
            r15 = 0;
            goto L24
        L36:
            throw new IOException("Sample-AES: el segmento supera 32 MB");     // Catch: LinkageError -> L41 RuntimeException -> L43 IOException -> L45
        L38:
            throw new IOException("Sample-AES: descarga sin progreso");     // Catch: LinkageError -> L41 RuntimeException -> L43 IOException -> L45
        L40:
            throw new InterruptedIOException();     // Catch: LinkageError -> L41 RuntimeException -> L43 IOException -> L45
        L48:
            if (r11 != (-1)) goto L51;
        L295:
            if (r4.length != (-1)) goto L283;
            long r29 = r11;
        L299:
            r49 = SystemClock.elapsedRealtime();     // Catch: Throwable -> L206 RuntimeException -> L208 IOException -> L210
            r22 = r26.payload();     // Catch: Throwable -> L200 RuntimeException -> L202 IOException -> L204
            String r30 = "DECRYPT";
            r47 = SystemClock.elapsedRealtime();     // Catch: LinkageError -> L185 Throwable -> L187 IOException -> L189
            byte[] r32 = (byte[]) r4.key.clone();     // Catch: LinkageError -> L177 RuntimeException -> L179 Throwable -> L181
            byte[] r111 = (byte[]) r4.f170iv.clone();     // Catch: LinkageError -> L177 RuntimeException -> L179 Throwable -> L181
            this.clear = SampleAesDataSourceFactory.access$300(this.this$0).decrypt(r22, r32, r111);     // Catch: Throwable -> L168
            Arrays.fill(r32, (byte) 0);     // Catch: LinkageError -> L161 RuntimeException -> L163 IOException -> L165
            Arrays.fill(r111, (byte) 0);     // Catch: LinkageError -> L161 RuntimeException -> L163 IOException -> L165
            if (SampleAesDataSourceFactory.access$400(this.this$0).compareAndSet(false, true) == false) goto L96;
            long r112 = r49 - r13;     // Catch: LinkageError -> L89 RuntimeException -> L91 Throwable -> L93
            long r34 = SystemClock.elapsedRealtime() - r49;     // Catch: LinkageError -> L89 RuntimeException -> L91 Throwable -> L93
            Log.i("CHDTVSampleAES", "first_segment_bytes=" + r26.size() + " download_ms=" + r112 + " decrypt_ms=" + r34);     // Catch: LinkageError -> L89 RuntimeException -> L91 Throwable -> L93
            BlackBox.event("SAMPLE_AES_FIRST_SEGMENT", "bytes=" + r26.size() + " download_ms=" + r112 + " decrypt_ms=" + r34);     // Catch: LinkageError -> L89 RuntimeException -> L91 Throwable -> L93
        L93:
            e = e;
            Object r36 = this;
            r31 = r29;
            r22 = r22;
            r21 = "DECRYPT";
        L199:
            r7 = e;
            r14 = r36;
        L96:
            if (this.clear == null) goto L160;
            long r113 = SampleAesDataSourceFactory.access$500(this.this$0);     // Catch: LinkageError -> L177 RuntimeException -> L179 Throwable -> L181
            long r39 = r4.diagnosticSequence;     // Catch: LinkageError -> L177 RuntimeException -> L179 Throwable -> L181
            long r114 = r4.diagnosticDiscontinuity;     // Catch: LinkageError -> L177 RuntimeException -> L179 Throwable -> L181
            int r41 = r4.diagnosticIvSource;     // Catch: LinkageError -> L177 RuntimeException -> L179 Throwable -> L181
            long r115 = r4.offset;     // Catch: LinkageError -> L177 RuntimeException -> L179 Throwable -> L181
            long r44 = r4.length;     // Catch: Throwable -> L152 RuntimeException -> L154 IOException -> L156
            long r45 = r57.position;     // Catch: Throwable -> L152 RuntimeException -> L154 IOException -> L156
            long r46 = r57.length;     // Catch: Throwable -> L152 RuntimeException -> L154 IOException -> L156
            long r310 = r49 - r43;     // Catch: Throwable -> L152 RuntimeException -> L154 IOException -> L156
            long r311 = SystemClock.elapsedRealtime() - r47;
            String r53 = "DECRYPT";
        L146:
            e = e;
            r30 = null;
            r3 = this;
        L167:
            long r55 = r29;
        L184:
            Object r48 = r3;
        L191:
            r21 = r53;
            Object r50 = r48;
        L198:
            r31 = r55;
            r36 = r50;
            goto L199
        L102:
            if (r4.key != null) goto L104;
            int r312 = 0;
        L106:
            if (r4.f170iv != null) goto L108;
            int r313 = 0;
        L110:
            r55 = r29;
            r30 = null;
            HlsDiagnostics225.result(r113, r40, r6, r42, r39, r114, r41, "DECRYPT", r22, r115, r44, r45, r46, r55, r310, r311, r312, r313, null);     // Catch: Throwable -> L140 RuntimeException -> L142 IOException -> L144
            long r51 = r57.position - r4.offset;
            if (r51 < 0) goto L126;
            SegmentSource r52 = this;
        L131:
            e = e;
        L139:
            r21 = "VALIDATE";
            r22 = r22;
            r50 = r52;
        L127:
            e = e;
        L129:
            e = e;
            goto L139
        L117:
            if (r51 > r52.clear.length) goto L126;
            r52.cursor = (int) r51;     // Catch: LinkageError -> L127 RuntimeException -> L129 IOException -> L131
            long r116 = ((long) r52.clear.length) - r51;     // Catch: LinkageError -> L127 RuntimeException -> L129 IOException -> L131
            int r56 = r52.cursor;     // Catch: LinkageError -> L127 RuntimeException -> L129 IOException -> L131
            if (r57.length == (-1)) goto L122;
            r116 = Math.min(r116, r57.length);     // Catch: LinkageError -> L127 RuntimeException -> L129 IOException -> L131
        L122:
            r52.end = r56 + ((int) r116);     // Catch: LinkageError -> L127 RuntimeException -> L129 IOException -> L131
            return r52.end - r52.cursor;
        L126:
            throw new IOException("Sample-AES: posición fuera de segmento");     // Catch: LinkageError -> L127 RuntimeException -> L129 IOException -> L131
        L133:
            e = e;
            r52 = this;
        L140:
            e = e;
            r3 = this;
            goto L184
        L108:
            r313 = r4.f170iv.length;     // Catch: Throwable -> L146 RuntimeException -> L148 IOException -> L150
            goto L110
        L104:
            r312 = r4.key.length;     // Catch: Throwable -> L146 RuntimeException -> L148 IOException -> L150
        L152:
            e = e;
            r3 = this;
        L183:
            r53 = "DECRYPT";
            r55 = r29;
            r30 = null;
            goto L184
        L160:
            throw new IOException("Sample-AES: segmento descifrado vacío");     // Catch: LinkageError -> L171 RuntimeException -> L173 IOException -> L175
        L165:
            e = e;
        L166:
            r3 = this;
            r53 = "DECRYPT";
            r30 = null;
        L161:
            e = e;
        L163:
            e = e;
        L168:
            th = move-exception;
            Arrays.fill(r32, (byte) 0);     // Catch: LinkageError -> L171 RuntimeException -> L173 IOException -> L175
            Arrays.fill(r111, (byte) 0);     // Catch: LinkageError -> L171 RuntimeException -> L173 IOException -> L175
            throw th;     // Catch: LinkageError -> L171 RuntimeException -> L173 IOException -> L175
        L181:
            e = e;
            r3 = this;
        L175:
            e = e;
        L171:
            e = e;
        L173:
            e = e;
        L187:
            e = e;
            r48 = this;
            r53 = "DECRYPT";
            r55 = r29;
            r47 = 0;
        L200:
            e = move-exception;
            r8 = this;
            r54 = r29;
            r7 = e;
            r21 = r19;
        L206:
            e = e;
            r54 = r29;
        L218:
            r24 = 0;
            goto L232
        L283:
            r29 = r11;
        L68:
            e = e;
        L76:
            r7 = e;
            r14 = this;
            r31 = r29;
            r49 = r43;
            r47 = 0;
            r22 = null;
            r21 = r19;
        L64:
            e = e;
        L66:
            e = e;
            goto L76
        L60:
            if (r26.size() == r4.length) goto L299;
        L63:
            throw new IOException("Sample-AES: segmento incompleto");     // Catch: LinkageError -> L64 RuntimeException -> L66 IOException -> L68
        L70:
            e = e;
            r29 = r11;
        L212:
            e = e;
            r54 = r11;
            goto L218
        L51:
            if (r26.size() == r11) goto L295;
            r29 = r11;
        L223:
            e = e;
            goto L231
        L22:
            r25 = 65536;
        L246:
            e = move-exception;
            r9 = this;
            r7 = e;
            r21 = "DOWNLOAD";
            r49 = r43;
            r31 = -1;
            goto L250
        L14:
            r42 = 0;
            goto L15
        L272:
            throw new IOException("Sample-AES: el segmento supera 32 MB");
        L276:
            throw new IOException("Sample-AES: fuente ya abierta");
        }

        @Override // androidx.media3.common.DataReader
        public int read(byte[] r3, int r4, int r5) throws IOException {
            if (r5 != 0) goto L6;
            return 0;
        L6:
            if (this.clear != null) goto L10;
            return this.upstream.read(r3, r4, r5);
        L10:
            if (this.cursor != this.end) goto L13;
            return -1;
        L13:
            int r6 = Math.min(r5, this.end - this.cursor);
            System.arraycopy(this.clear, this.cursor, r3, r4, r6);
            this.cursor += r6;
            return r6;
        }
    }

    public SampleAesDataSourceFactory(DataSource.Factory r2, SampleAesManifest r3) {
        this(r2, r3, new SampleAesDataSourceFactory$$ExternalSyntheticLambda0());
    }

    public SampleAesDataSourceFactory(DataSource.Factory r3, SampleAesManifest r4, Decryptor r5) {
        this.diagnosticRun = HlsDiagnostics225.nextRun();
        this.diagnosticSegments = new AtomicLong();
        this.diagnosticPrevious = new LinkedHashMap();
        this.firstTiming = new AtomicBoolean();
        this.network = r3;
        this.manifest = r4;
        this.decryptor = r5;
    }

    static /* synthetic */ SampleAesManifest access$000(SampleAesDataSourceFactory r0) {
        return r0.manifest;
    }

    static /* synthetic */ AtomicLong access$100(SampleAesDataSourceFactory r0) {
        return r0.diagnosticSegments;
    }

    static /* synthetic */ int access$200(SampleAesDataSourceFactory r0, String r1, SampleAesManifest.Segment r2) {
        return r0.diagnosticCrypto(r1, r2);
    }

    static /* synthetic */ Decryptor access$300(SampleAesDataSourceFactory r0) {
        return r0.decryptor;
    }

    static /* synthetic */ AtomicBoolean access$400(SampleAesDataSourceFactory r0) {
        return r0.firstTiming;
    }

    static /* synthetic */ long access$500(SampleAesDataSourceFactory r2) {
        return r2.diagnosticRun;
    }

    private synchronized int diagnosticCrypto(String r3, SampleAesManifest.Segment r4) {
        monitor-enter(this);
    L19:
        th = move-exception;
        throw th;
    L4:
        if (this.diagnosticPrevious.size() <= 16) goto L6;
        this.diagnosticPrevious.clear();     // Catch: Throwable -> L19
    L6:
        SampleAesManifest.Segment r5 = this.diagnosticPrevious.put(r3, r4);     // Catch: Throwable -> L19
        if (r5 != null) goto L9;
        int r6 = 1;
    L17:
        monitor-exit(this);
        return r6;
    L9:
        int r1 = 0;
        if (Arrays.equals(r5.key, r4.key) == true) goto L12;
        int r0 = 2;
    L13:
        if (Arrays.equals(r5.f170iv, r4.f170iv) == true) goto L16;
        r1 = 4;
    L16:
        r6 = r0 | r1;
        goto L17
    L12:
        r0 = 0;
        goto L13
    }

    @Override // androidx.media3.exoplayer.hls.HlsDataSourceFactory
    public DataSource createDataSource(int r3) {
        DataSource r0 = this.network.createDataSource();
        if (r3 == 1) goto L5;
        return r0;
    L5:
        return new SegmentSource(this, r0);
    }
}
