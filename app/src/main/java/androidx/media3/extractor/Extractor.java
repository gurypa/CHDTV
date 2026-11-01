package androidx.media3.extractor;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import org.checkerframework.dataflow.qual.SideEffectFree;

/* JADX INFO: loaded from: classes.dex */
public interface Extractor {
    public static final int RESULT_CONTINUE = 0;
    public static final int RESULT_END_OF_INPUT = -1;
    public static final int RESULT_SEEK = 1;

    /* JADX INFO: renamed from: androidx.media3.extractor.Extractor$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static List $default$getSniffFailureDetails(Extractor r1) {
            return ImmutableList.m212of();
        }

        @SideEffectFree
        public static Extractor $default$getUnderlyingImplementation(Extractor r0) {
            return r0;
        }
    }

    @Target({ElementType.TYPE_USE})
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface ReadResult {
    }

    List<SniffFailure> getSniffFailureDetails();

    @SideEffectFree
    Extractor getUnderlyingImplementation();

    void init(ExtractorOutput r1);

    int read(ExtractorInput r1, PositionHolder r2) throws IOException;

    void release();

    void seek(long r1, long r3);

    boolean sniff(ExtractorInput r1) throws IOException;
}
