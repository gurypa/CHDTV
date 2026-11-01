package androidx.media3.extractor.text;

import androidx.media3.common.C0180C;
import androidx.media3.common.Format;
import androidx.media3.common.util.Consumer;
import com.google.common.collect.ImmutableList;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public interface SubtitleParser {

    /* JADX INFO: renamed from: androidx.media3.extractor.text.SubtitleParser$-CC, reason: invalid class name */
    public final /* synthetic */ class CC {
        public static void $default$parse(SubtitleParser r6, byte[] r7, OutputOptions r8, Consumer r9) {
            r6.parse(r7, 0, r7.length, r8, r9);
        }

        public static Subtitle $default$parseToLegacySubtitle(SubtitleParser r7, byte[] r8, int r9, int r10) {
            final ImmutableList.Builder r0 = ImmutableList.builder();
            OutputOptions r5 = OutputOptions.access$000();
            Objects.requireNonNull(r0);
            r7.parse(r8, r9, r10, r5, new SubtitleParser$$ExternalSyntheticLambda0(r0));
            return new CuesWithTimingSubtitle(r0.build());
        }

        public static void $default$reset(SubtitleParser r0) {
        }
    }

    public interface Factory {
        public static final Factory UNSUPPORTED = null;

        static {
            UNSUPPORTED = new C04441();
        }

        SubtitleParser create(Format r1);

        int getCueReplacementBehavior(Format r1);

        boolean supportsFormat(Format r1);
    }

    public static class OutputOptions {
        private static final OutputOptions ALL = null;
        public final boolean outputAllCues;
        public final long startTimeUs;

        static {
            ALL = new OutputOptions(C0180C.TIME_UNSET, false);
        }

        private OutputOptions(long r1, boolean r3) {
            this.startTimeUs = r1;
            this.outputAllCues = r3;
        }

        static /* synthetic */ OutputOptions access$000() {
            return ALL;
        }

        public static OutputOptions allCues() {
            return ALL;
        }

        public static OutputOptions cuesAfterThenRemainingCuesBefore(long r2) {
            return new OutputOptions(r2, true);
        }

        public static OutputOptions onlyCuesAfter(long r2) {
            return new OutputOptions(r2, false);
        }
    }

    int getCueReplacementBehavior();

    void parse(byte[] r1, int r2, int r3, OutputOptions r4, Consumer<CuesWithTiming> r5);

    void parse(byte[] r1, OutputOptions r2, Consumer<CuesWithTiming> r3);

    Subtitle parseToLegacySubtitle(byte[] r1, int r2, int r3);

    void reset();
}
