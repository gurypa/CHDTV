package androidx.core.provider;

import android.util.Base64;
import androidx.core.util.Preconditions;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class FontRequest {
    private final List<List<byte[]>> mCertificates;
    private final int mCertificatesArray;
    private final String mIdentifier;
    private final String mProviderAuthority;
    private final String mProviderPackage;
    private final String mQuery;

    public FontRequest(String r2, String r3, String r4, int r5) {
        this.mProviderAuthority = (String) Preconditions.checkNotNull(r2);
        this.mProviderPackage = (String) Preconditions.checkNotNull(r3);
        this.mQuery = (String) Preconditions.checkNotNull(r4);
        this.mCertificates = null;
        if (r5 == 0) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkArgument(r0);
        this.mCertificatesArray = r5;
        this.mIdentifier = createIdentifier(r2, r3, r4);
        return;
    L5:
        r0 = false;
        goto L6
    }

    public FontRequest(String r2, String r3, String r4, List<List<byte[]>> r5) {
        this.mProviderAuthority = (String) Preconditions.checkNotNull(r2);
        this.mProviderPackage = (String) Preconditions.checkNotNull(r3);
        this.mQuery = (String) Preconditions.checkNotNull(r4);
        this.mCertificates = (List) Preconditions.checkNotNull(r5);
        this.mCertificatesArray = 0;
        this.mIdentifier = createIdentifier(r2, r3, r4);
    }

    private String createIdentifier(String r3, String r4, String r5) {
        return r3 + "-" + r4 + "-" + r5;
    }

    public List<List<byte[]>> getCertificates() {
        return this.mCertificates;
    }

    public int getCertificatesArrayResId() {
        return this.mCertificatesArray;
    }

    String getId() {
        return this.mIdentifier;
    }

    @Deprecated
    public String getIdentifier() {
        return this.mIdentifier;
    }

    public String getProviderAuthority() {
        return this.mProviderAuthority;
    }

    public String getProviderPackage() {
        return this.mProviderPackage;
    }

    public String getQuery() {
        return this.mQuery;
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder();
        r0.append("FontRequest {mProviderAuthority: " + this.mProviderAuthority + ", mProviderPackage: " + this.mProviderPackage + ", mQuery: " + this.mQuery + ", mCertificates:");
        int r1 = 0;
    L4:
        if (r1 >= this.mCertificates.size()) goto L10;
        r0.append(" [");
        List<byte[]> r2 = this.mCertificates.get(r1);
        int r3 = 0;
    L7:
        if (r3 >= r2.size()) goto L9;
        r0.append(" \"");
        r0.append(Base64.encodeToString(r2.get(r3), 0));
        r0.append("\"");
        r3 = r3 + 1;
        goto L7
    L9:
        r0.append(" ]");
        r1 = r1 + 1;
        goto L4
    L10:
        r0.append("}");
        r0.append("mCertificatesArray: " + this.mCertificatesArray);
        return r0.toString();
    }
}
