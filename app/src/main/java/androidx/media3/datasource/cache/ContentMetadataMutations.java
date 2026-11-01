package androidx.media3.datasource.cache;

import android.net.Uri;
import androidx.media3.common.util.Assertions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ContentMetadataMutations {
    private final Map<String, Object> editedValues;
    private final List<String> removedValues;

    public ContentMetadataMutations() {
        this.editedValues = new HashMap();
        this.removedValues = new ArrayList();
    }

    private ContentMetadataMutations checkAndSet(String r4, Object r5) {
        this.editedValues.put((String) Assertions.checkNotNull(r4), Assertions.checkNotNull(r5));
        this.removedValues.remove(r4);
        return this;
    }

    public static ContentMetadataMutations setContentLength(ContentMetadataMutations r1, long r2) {
        return r1.set(ContentMetadata.KEY_CONTENT_LENGTH, r2);
    }

    public static ContentMetadataMutations setRedirectedUri(ContentMetadataMutations r2, Uri r3) {
        if (r3 != null) goto L7;
        return r2.remove(ContentMetadata.KEY_REDIRECTED_URI);
    L7:
        return r2.set(ContentMetadata.KEY_REDIRECTED_URI, r3.toString());
    }

    public Map<String, Object> getEditedValues() {
        HashMap r0 = new HashMap(this.editedValues);
        Iterator r1 = r0.entrySet().iterator();
    L4:
        if (r1.hasNext() == false) goto L10;
        Map.Entry r2 = (Map.Entry) r1.next();
        Object r3 = r2.getValue();
        if ((r3 instanceof byte[]) == false) goto L4;
        byte[] r4 = (byte[]) r3;
        r2.setValue(Arrays.copyOf(r4, r4.length));
        goto L4
    L10:
        return Collections.unmodifiableMap(r0);
    }

    public List<String> getRemovedValues() {
        return Collections.unmodifiableList(new ArrayList(this.removedValues));
    }

    public ContentMetadataMutations remove(String r2) {
        this.removedValues.add(r2);
        this.editedValues.remove(r2);
        return this;
    }

    public ContentMetadataMutations set(String r2, long r3) {
        return checkAndSet(r2, Long.valueOf(r3));
    }

    public ContentMetadataMutations set(String r2, String r3) {
        return checkAndSet(r2, r3);
    }

    public ContentMetadataMutations set(String r2, byte[] r3) {
        return checkAndSet(r2, Arrays.copyOf(r3, r3.length));
    }
}
