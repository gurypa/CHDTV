package androidx.media3.extractor.metadata.emsg;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public final class EventMessageEncoder {
    private final ByteArrayOutputStream byteArrayOutputStream;
    private final DataOutputStream dataOutputStream;

    public EventMessageEncoder() {
        this.byteArrayOutputStream = new ByteArrayOutputStream(512);
        this.dataOutputStream = new DataOutputStream(this.byteArrayOutputStream);
    }

    private static void writeNullTerminatedString(DataOutputStream r1, String r2) throws IOException {
        r1.writeBytes(r2);
        r1.writeByte(0);
    }

    public byte[] encode(EventMessage r5) {
        this.byteArrayOutputStream.reset();
        writeNullTerminatedString(this.dataOutputStream, r5.schemeIdUri);     // Catch: IOException -> L9
        if (r5.value == null) goto L6;
        String r0 = r5.value;     // Catch: IOException -> L9
    L7:
        writeNullTerminatedString(this.dataOutputStream, r0);     // Catch: IOException -> L9
        this.dataOutputStream.writeLong(r5.durationMs);     // Catch: IOException -> L9
        this.dataOutputStream.writeLong(r5.f49id);     // Catch: IOException -> L9
        this.dataOutputStream.write(r5.messageData);     // Catch: IOException -> L9
        this.dataOutputStream.flush();     // Catch: IOException -> L9
        return this.byteArrayOutputStream.toByteArray();
    L6:
        r0 = "";
    L9:
        e = move-exception;
        throw new RuntimeException(e);
    }
}
