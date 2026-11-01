package androidx.media3.extractor.metadata.emsg;

import androidx.media3.common.Metadata;
import androidx.media3.common.util.Assertions;
import androidx.media3.common.util.ParsableByteArray;
import androidx.media3.extractor.metadata.MetadataInputBuffer;
import androidx.media3.extractor.metadata.SimpleMetadataDecoder;
import java.nio.ByteBuffer;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class EventMessageDecoder extends SimpleMetadataDecoder {
    public EventMessageDecoder() {
    }

    @Override // androidx.media3.extractor.metadata.SimpleMetadataDecoder
    protected Metadata decode(MetadataInputBuffer r6, ByteBuffer r7) {
        return new Metadata(new Metadata.Entry[]{decode(new ParsableByteArray(r7.array(), r7.limit()))});
    }

    public EventMessage decode(ParsableByteArray r10) {
        return new EventMessage((String) Assertions.checkNotNull(r10.readNullTerminatedString()), (String) Assertions.checkNotNull(r10.readNullTerminatedString()), r10.readLong(), r10.readLong(), Arrays.copyOfRange(r10.getData(), r10.getPosition(), r10.limit()));
    }
}
