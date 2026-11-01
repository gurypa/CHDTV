package androidx.media3.datasource.cache;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultContentMetadata implements ContentMetadata {
    public static final DefaultContentMetadata EMPTY = null;
    private int hashCode;
    private final Map<String, byte[]> metadata;

    static {
        EMPTY = new DefaultContentMetadata(Collections.emptyMap());
    }

    public DefaultContentMetadata() {
        this(Collections.emptyMap());
    }

    public DefaultContentMetadata(Map<String, byte[]> r2) {
        this.metadata = Collections.unmodifiableMap(r2);
    }

    private static void addValues(HashMap<String, byte[]> r4, Map<String, Object> r5) {
        Iterator<Map.Entry<String, Object>> r0 = r5.entrySet().iterator();
    L4:
        if (r0.hasNext() == false) goto L6;
        Map.Entry<String, Object> r1 = r0.next();
        r4.put(r1.getKey(), getBytes(r1.getValue()));
        goto L4
    }

    private static Map<String, byte[]> applyMutations(Map<String, byte[]> r2, ContentMetadataMutations r3) {
        HashMap r0 = new HashMap(r2);
        removeValues(r0, r3.getRemovedValues());
        addValues(r0, r3.getEditedValues());
        return r0;
    }

    private static byte[] getBytes(Object r3) {
        if ((r3 instanceof Long) == false) goto L7;
        return ByteBuffer.allocate(8).putLong(((Long) r3).longValue()).array();
    L7:
        if ((r3 instanceof String) == false) goto L11;
        return ((String) r3).getBytes(StandardCharsets.UTF_8);
    L11:
        if ((r3 instanceof byte[]) == false) goto L15;
        return (byte[]) r3;
    L15:
        throw new IllegalArgumentException();
    }

    private static boolean isMetadataEqual(Map<String, byte[]> r6, Map<String, byte[]> r7) {
        if (r6.size() == r7.size()) goto L5;
        return false;
    L5:
        Iterator<Map.Entry<String, byte[]>> r0 = r6.entrySet().iterator();
    L7:
        if (r0.hasNext() == false) goto L12;
        Map.Entry<String, byte[]> r1 = r0.next();
        if (Arrays.equals(r1.getValue(), r7.get(r1.getKey())) == true) goto L7;
        return false;
    L12:
        return true;
    }

    private static void removeValues(HashMap<String, byte[]> r2, List<String> r3) {
        int r0 = 0;
    L4:
        if (r0 >= r3.size()) goto L6;
        r2.remove(r3.get(r0));
        r0 = r0 + 1;
        goto L4
    }

    @Override // androidx.media3.datasource.cache.ContentMetadata
    public final boolean contains(String r2) {
        return this.metadata.containsKey(r2);
    }

    public DefaultContentMetadata copyWithMutationsApplied(ContentMetadataMutations r3) {
        Map<String, byte[]> r0 = applyMutations(this.metadata, r3);
        if (isMetadataEqual(this.metadata, r0) == false) goto L6;
        return this;
    L6:
        return new DefaultContentMetadata(r0);
    }

    public Set<Map.Entry<String, byte[]>> entrySet() {
        return this.metadata.entrySet();
    }

    public boolean equals(Object r3) {
        if (this != r3) goto L5;
        return true;
    L5:
        if (r3 != null) goto L7;
        return false;
    L7:
        if (getClass() == r3.getClass()) goto L10;
        return false;
    L10:
        return isMetadataEqual(this.metadata, ((DefaultContentMetadata) r3).metadata);
    }

    @Override // androidx.media3.datasource.cache.ContentMetadata
    public final long get(String r4, long r5) {
        byte[] r0 = this.metadata.get(r4);
        if (r0 != null) goto L5;
        return r5;
    L5:
        return ByteBuffer.wrap(r0).getLong();
    }

    @Override // androidx.media3.datasource.cache.ContentMetadata
    public final String get(String r4, String r5) {
        byte[] r0 = this.metadata.get(r4);
        if (r0 != null) goto L5;
        return r5;
    L5:
        return new String(r0, StandardCharsets.UTF_8);
    }

    @Override // androidx.media3.datasource.cache.ContentMetadata
    public final byte[] get(String r3, byte[] r4) {
        byte[] r0 = this.metadata.get(r3);
        if (r0 != null) goto L5;
        return r4;
    L5:
        return Arrays.copyOf(r0, r0.length);
    }

    public int hashCode() {
        if (this.hashCode != 0) goto L10;
        int r0 = 0;
        Iterator<Map.Entry<String, byte[]>> r1 = this.metadata.entrySet().iterator();
    L6:
        if (r1.hasNext() == false) goto L8;
        Map.Entry<String, byte[]> r2 = r1.next();
        r0 = r0 + (r2.getKey().hashCode() ^ Arrays.hashCode(r2.getValue()));
        goto L6
    L8:
        this.hashCode = r0;
    L10:
        return this.hashCode;
    }
}
