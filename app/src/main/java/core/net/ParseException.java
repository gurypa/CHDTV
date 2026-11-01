package androidx.core.net;

/* JADX INFO: loaded from: classes.dex */
public class ParseException extends RuntimeException {
    public final String response;

    ParseException(String r1) {
        super(r1);
        this.response = r1;
    }
}
