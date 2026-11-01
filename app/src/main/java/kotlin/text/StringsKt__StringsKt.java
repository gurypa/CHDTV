package kotlin.text;

import androidx.media3.extractor.text.ttml.TtmlNode;
import com.arthenica.ffmpegkit.StreamInformation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ReplaceWith;
import kotlin.TuplesKt;
import kotlin.collections.ArraysKt;
import kotlin.collections.CharIterator;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

/* JADX INFO: compiled from: Strings.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u0084\u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\f\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0019\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010\u0011\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b!\u001a\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0006H\u0000\u001a\u001c\u0010\f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010\u0011\u001a\u00020\r*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001f\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0086\u0002\u001a\u001f\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0086\u0002\u001a\u0015\u0010\u0012\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0016H\u0087\n\u001a\u0018\u0010\u0017\u001a\u00020\u0010*\u0004\u0018\u00010\u00022\b\u0010\u000e\u001a\u0004\u0018\u00010\u0002H\u0000\u001a\u0018\u0010\u0018\u001a\u00020\u0010*\u0004\u0018\u00010\u00022\b\u0010\u000e\u001a\u0004\u0018\u00010\u0002H\u0000\u001a\u001c\u0010\u0019\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010\u0019\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a:\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001aE\u0010\u001b\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010 \u001a\u00020\u0010H\u0002¢\u0006\u0002\b!\u001a:\u0010\"\u001a\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\r\u0018\u00010\u001c*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0012\u0010#\u001a\u00020\u0010*\u00020\u00022\u0006\u0010$\u001a\u00020\u0006\u001a7\u0010%\u001a\u0002H&\"\f\b\u0000\u0010'*\u00020\u0002*\u0002H&\"\u0004\b\u0001\u0010&*\u0002H'2\f\u0010(\u001a\b\u0012\u0004\u0012\u0002H&0)H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010*\u001a7\u0010+\u001a\u0002H&\"\f\b\u0000\u0010'*\u00020\u0002*\u0002H&\"\u0004\b\u0001\u0010&*\u0002H'2\f\u0010(\u001a\b\u0012\u0004\u0012\u0002H&0)H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010*\u001a&\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a;\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010 \u001a\u00020\u0010H\u0002¢\u0006\u0002\b.\u001a&\u0010,\u001a\u00020\u0006*\u00020\u00022\u0006\u0010/\u001a\u00020\r2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u00100\u001a\u00020\u0006*\u00020\u00022\u0006\u00101\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a,\u00100\u001a\u00020\u0006*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\r\u00103\u001a\u00020\u0010*\u00020\u0002H\u0087\b\u001a\r\u00104\u001a\u00020\u0010*\u00020\u0002H\u0087\b\u001a\r\u00105\u001a\u00020\u0010*\u00020\u0002H\u0087\b\u001a \u00106\u001a\u00020\u0010*\u0004\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a \u00107\u001a\u00020\u0010*\u0004\u0018\u00010\u0002H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u001a\r\u00108\u001a\u000209*\u00020\u0002H\u0086\u0002\u001a&\u0010:\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u0010:\u001a\u00020\u0006*\u00020\u00022\u0006\u0010/\u001a\u00020\r2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a&\u0010;\u001a\u00020\u0006*\u00020\u00022\u0006\u00101\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a,\u0010;\u001a\u00020\u0006*\u00020\u00022\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\r0\u001e2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0010\u0010<\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u0002\u001a\u0010\u0010>\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u0002\u001a\u0015\u0010@\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u0016H\u0087\f\u001a\u000f\u0010A\u001a\u00020\r*\u0004\u0018\u00010\rH\u0087\b\u001a\u001c\u0010B\u001a\u00020\u0002*\u00020\u00022\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010B\u001a\u00020\r*\u00020\r2\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010E\u001a\u00020\u0002*\u00020\u00022\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001a\u001c\u0010E\u001a\u00020\r*\u00020\r2\u0006\u0010C\u001a\u00020\u00062\b\b\u0002\u0010D\u001a\u00020\u0014\u001aG\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00010=*\u00020\u00022\u000e\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H2\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0002¢\u0006\u0004\bI\u0010J\u001a=\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00010=*\u00020\u00022\u0006\u0010G\u001a\u0002022\b\b\u0002\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0002¢\u0006\u0002\bI\u001a4\u0010K\u001a\u00020\u0010*\u00020\u00022\u0006\u0010L\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010M\u001a\u00020\u00062\u0006\u0010C\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0010H\u0000\u001a\u0012\u0010N\u001a\u00020\u0002*\u00020\u00022\u0006\u0010O\u001a\u00020\u0002\u001a\u0012\u0010N\u001a\u00020\r*\u00020\r2\u0006\u0010O\u001a\u00020\u0002\u001a\u001a\u0010P\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u0006\u001a\u0012\u0010P\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u001d\u0010P\u001a\u00020\r*\u00020\r2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u0006H\u0087\b\u001a\u0015\u0010P\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u0001H\u0087\b\u001a\u0012\u0010R\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010R\u001a\u00020\r*\u00020\r2\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010S\u001a\u00020\u0002*\u00020\u00022\u0006\u0010T\u001a\u00020\u0002\u001a\u001a\u0010S\u001a\u00020\u0002*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a\u0012\u0010S\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u0002\u001a\u001a\u0010S\u001a\u00020\r*\u00020\r2\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u0002\u001a.\u0010U\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0014\b\b\u0010V\u001a\u000e\u0012\u0004\u0012\u00020X\u0012\u0004\u0012\u00020\u00020WH\u0087\bø\u0001\u0000\u001a\u001d\u0010U\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010Y\u001a\u00020\rH\u0087\b\u001a$\u0010Z\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010Z\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010\\\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010\\\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010]\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010]\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010^\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a$\u0010^\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\u0006\u0010Y\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001d\u0010_\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010Y\u001a\u00020\rH\u0087\b\u001a)\u0010`\u001a\u00020\r*\u00020\r2\u0012\u0010V\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00140WH\u0087\bø\u0001\u0000¢\u0006\u0002\ba\u001a)\u0010`\u001a\u00020\r*\u00020\r2\u0012\u0010V\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00020WH\u0087\bø\u0001\u0000¢\u0006\u0002\bb\u001a\"\u0010c\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010Y\u001a\u00020\u0002\u001a\u001a\u0010c\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u00012\u0006\u0010Y\u001a\u00020\u0002\u001a%\u0010c\u001a\u00020\r*\u00020\r2\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010-\u001a\u00020\u00062\u0006\u0010Y\u001a\u00020\u0002H\u0087\b\u001a\u001d\u0010c\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u00012\u0006\u0010Y\u001a\u00020\u0002H\u0087\b\u001a=\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0012\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H\"\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006¢\u0006\u0002\u0010e\u001a0\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\n\u0010G\u001a\u000202\"\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u001a/\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0006\u0010T\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u000b\u001a\u00020\u0006H\u0002¢\u0006\u0002\bf\u001a%\u0010d\u001a\b\u0012\u0004\u0012\u00020\r0?*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0087\b\u001a=\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\u0012\u0010G\u001a\n\u0012\u0006\b\u0001\u0012\u00020\r0H\"\u00020\r2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006¢\u0006\u0002\u0010h\u001a0\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\n\u0010G\u001a\u000202\"\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u000b\u001a\u00020\u0006\u001a%\u0010g\u001a\b\u0012\u0004\u0012\u00020\r0=*\u00020\u00022\u0006\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u000b\u001a\u00020\u0006H\u0087\b\u001a\u001c\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u001c\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a$\u0010i\u001a\u00020\u0010*\u00020\u00022\u0006\u0010O\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u001a\u0012\u0010j\u001a\u00020\u0002*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u001d\u0010j\u001a\u00020\u0002*\u00020\r2\u0006\u0010k\u001a\u00020\u00062\u0006\u0010l\u001a\u00020\u0006H\u0087\b\u001a\u001f\u0010m\u001a\u00020\r*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00062\b\b\u0002\u0010-\u001a\u00020\u0006H\u0087\b\u001a\u0012\u0010m\u001a\u00020\r*\u00020\u00022\u0006\u0010Q\u001a\u00020\u0001\u001a\u0012\u0010m\u001a\u00020\r*\u00020\r2\u0006\u0010Q\u001a\u00020\u0001\u001a\u001c\u0010n\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010n\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010o\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010o\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010p\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010p\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010q\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\u00142\b\b\u0002\u0010[\u001a\u00020\r\u001a\u001c\u0010q\u001a\u00020\r*\u00020\r2\u0006\u0010T\u001a\u00020\r2\b\b\u0002\u0010[\u001a\u00020\r\u001a\f\u0010r\u001a\u00020\u0010*\u00020\rH\u0007\u001a\u0013\u0010s\u001a\u0004\u0018\u00010\u0010*\u00020\rH\u0007¢\u0006\u0002\u0010t\u001a\n\u0010u\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010u\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\bø\u0001\u0000\u001a\u0016\u0010u\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010u\u001a\u00020\r*\u00020\rH\u0087\b\u001a$\u0010u\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\bø\u0001\u0000\u001a\u0016\u0010u\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\u001a\n\u0010w\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010w\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\bø\u0001\u0000\u001a\u0016\u0010w\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010w\u001a\u00020\r*\u00020\rH\u0087\b\u001a$\u0010w\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\bø\u0001\u0000\u001a\u0016\u0010w\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\u001a\n\u0010x\u001a\u00020\u0002*\u00020\u0002\u001a$\u0010x\u001a\u00020\u0002*\u00020\u00022\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\bø\u0001\u0000\u001a\u0016\u0010x\u001a\u00020\u0002*\u00020\u00022\n\u00101\u001a\u000202\"\u00020\u0014\u001a\r\u0010x\u001a\u00020\r*\u00020\rH\u0087\b\u001a$\u0010x\u001a\u00020\r*\u00020\r2\u0012\u0010v\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00100WH\u0086\bø\u0001\u0000\u001a\u0016\u0010x\u001a\u00020\r*\u00020\r2\n\u00101\u001a\u000202\"\u00020\u0014\"\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00028F¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006y"}, m421d2 = {"indices", "Lkotlin/ranges/IntRange;", "", "getIndices", "(Ljava/lang/CharSequence;)Lkotlin/ranges/IntRange;", "lastIndex", "", "getLastIndex", "(Ljava/lang/CharSequence;)I", "requireNonNegativeLimit", "", "limit", "commonPrefixWith", "", "other", "ignoreCase", "", "commonSuffixWith", "contains", "char", "", "regex", "Lkotlin/text/Regex;", "contentEqualsIgnoreCaseImpl", "contentEqualsImpl", "endsWith", "suffix", "findAnyOf", "Lkotlin/Pair;", "strings", "", "startIndex", "last", "findAnyOf$StringsKt__StringsKt", "findLastAnyOf", "hasSurrogatePairAt", StreamInformation.KEY_INDEX, "ifBlank", "R", "C", "defaultValue", "Lkotlin/Function0;", "(Ljava/lang/CharSequence;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "ifEmpty", "indexOf", "endIndex", "indexOf$StringsKt__StringsKt", "string", "indexOfAny", "chars", "", "isEmpty", "isNotBlank", "isNotEmpty", "isNullOrBlank", "isNullOrEmpty", "iterator", "Lkotlin/collections/CharIterator;", "lastIndexOf", "lastIndexOfAny", "lineSequence", "Lkotlin/sequences/Sequence;", "lines", "", "matches", "orEmpty", "padEnd", "length", "padChar", "padStart", "rangesDelimitedBy", "delimiters", "", "rangesDelimitedBy$StringsKt__StringsKt", "(Ljava/lang/CharSequence;[Ljava/lang/String;IZI)Lkotlin/sequences/Sequence;", "regionMatchesImpl", "thisOffset", "otherOffset", "removePrefix", "prefix", "removeRange", "range", "removeSuffix", "removeSurrounding", TtmlNode.RUBY_DELIMITER, "replace", "transform", "Lkotlin/Function1;", "Lkotlin/text/MatchResult;", "replacement", "replaceAfter", "missingDelimiterValue", "replaceAfterLast", "replaceBefore", "replaceBeforeLast", "replaceFirst", "replaceFirstChar", "replaceFirstCharWithChar", "replaceFirstCharWithCharSequence", "replaceRange", "split", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Ljava/util/List;", "split$StringsKt__StringsKt", "splitToSequence", "(Ljava/lang/CharSequence;[Ljava/lang/String;ZI)Lkotlin/sequences/Sequence;", "startsWith", "subSequence", "start", "end", "substring", "substringAfter", "substringAfterLast", "substringBefore", "substringBeforeLast", "toBooleanStrict", "toBooleanStrictOrNull", "(Ljava/lang/String;)Ljava/lang/Boolean;", "trim", "predicate", "trimEnd", "trimStart", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/text/StringsKt")
class StringsKt__StringsKt extends StringsKt__StringsJVMKt {
    public StringsKt__StringsKt() {
    }

    public static final /* synthetic */ Pair access$findAnyOf(CharSequence r1, Collection r2, int r3, boolean r4, boolean r5) {
        return findAnyOf$StringsKt__StringsKt(r1, r2, r3, r4, r5);
    }

    public static final String commonPrefixWith(CharSequence r4, CharSequence r5, boolean r6) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "other");
        int r0 = Math.min(r4.length(), r5.length());
        int r1 = 0;
    L3:
        if (r1 >= r0) goto L8;
        if (CharsKt.equals(r4.charAt(r1), r5.charAt(r1), r6) == false) goto L8;
        r1 = r1 + 1;
    L8:
        if (StringsKt.hasSurrogatePairAt(r4, r1 - 1) == false) goto L10;
    L11:
        r1 = r1 - 1;
    L13:
        return r4.subSequence(0, r1).toString();
    L10:
        if (StringsKt.hasSurrogatePairAt(r5, r1 - 1) == false) goto L13;
        goto L11
    }

    public static /* synthetic */ String commonPrefixWith$default(CharSequence r0, CharSequence r1, boolean r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = false;
    L6:
        return StringsKt.commonPrefixWith(r0, r1, r2);
    }

    public static final String commonSuffixWith(CharSequence r6, CharSequence r7, boolean r8) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "other");
        int r0 = r6.length();
        int r1 = r7.length();
        int r2 = Math.min(r0, r1);
        int r3 = 0;
    L3:
        if (r3 >= r2) goto L8;
        if (CharsKt.equals(r6.charAt((r0 - r3) - 1), r7.charAt((r1 - r3) - 1), r8) == false) goto L8;
        r3 = r3 + 1;
    L8:
        if (StringsKt.hasSurrogatePairAt(r6, (r0 - r3) - 1) == false) goto L10;
    L11:
        r3 = r3 - 1;
    L13:
        return r6.subSequence(r0 - r3, r0).toString();
    L10:
        if (StringsKt.hasSurrogatePairAt(r7, (r1 - r3) - 1) == false) goto L13;
        goto L11
    }

    public static /* synthetic */ String commonSuffixWith$default(CharSequence r0, CharSequence r1, boolean r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = false;
    L6:
        return StringsKt.commonSuffixWith(r0, r1, r2);
    }

    public static final boolean contains(CharSequence r7, char r8, boolean r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        if (StringsKt.indexOf$default(r7, r8, 0, r9, 2, null) < 0) goto L5;
        return true;
    L5:
        return false;
    }

    public static final boolean contains(CharSequence r11, CharSequence r12, boolean r13) {
        Intrinsics.checkNotNullParameter(r11, "<this>");
        Intrinsics.checkNotNullParameter(r12, "other");
        if ((r12 instanceof String) == false) goto L9;
        if (StringsKt.indexOf$default(r11, (String) r12, 0, r13, 2, null) < 0) goto L13;
        return true;
    L13:
        return false;
    L9:
        if (indexOf$StringsKt__StringsKt$default(r11, r12, 0, r11.length(), r13, false, 16, null) < 0) goto L12;
        return true;
    L12:
        return false;
    }

    private static final boolean contains(CharSequence r1, Regex r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "regex");
        return r2.containsMatchIn(r1);
    }

    public static /* synthetic */ boolean contains$default(CharSequence r0, char r1, boolean r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = false;
    L6:
        return StringsKt.contains(r0, r1, r2);
    }

    public static /* synthetic */ boolean contains$default(CharSequence r0, CharSequence r1, boolean r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = false;
    L6:
        return StringsKt.contains(r0, r1, r2);
    }

    public static final boolean contentEqualsIgnoreCaseImpl(CharSequence r6, CharSequence r7) {
        if ((r6 instanceof String) == true) goto L5;
    L8:
        if (r6 != r7) goto L11;
        return true;
    L11:
        if (r6 == null) goto L23;
        if (r7 == null) goto L23;
        if (r6.length() != r7.length()) goto L23;
        int r2 = 0;
        int r3 = r6.length();
    L17:
        if (r2 >= r3) goto L22;
        if (CharsKt.equals(r6.charAt(r2), r7.charAt(r2), true) == false) goto L20;
        r2 = r2 + 1;
        goto L17
    L20:
        return false;
    L22:
        return true;
    L23:
        return false;
    L5:
        if ((r7 instanceof String) == false) goto L8;
        return StringsKt.equals((String) r6, (String) r7, true);
    }

    public static final boolean contentEqualsImpl(CharSequence r6, CharSequence r7) {
        if ((r6 instanceof String) == false) goto L9;
        if ((r7 instanceof String) == false) goto L9;
        return Intrinsics.areEqual(r6, r7);
    L9:
        if (r6 != r7) goto L12;
        return true;
    L12:
        if (r6 == null) goto L24;
        if (r7 == null) goto L24;
        if (r6.length() != r7.length()) goto L24;
        int r2 = 0;
        int r3 = r6.length();
    L18:
        if (r2 >= r3) goto L23;
        if (r6.charAt(r2) != r7.charAt(r2)) goto L21;
        r2 = r2 + 1;
        goto L18
    L21:
        return false;
    L23:
        return true;
    L24:
        return false;
    }

    public static final boolean endsWith(CharSequence r1, char r2, boolean r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if (r1.length() > 0) goto L5;
    L7:
        return false;
    L5:
        if (CharsKt.equals(r1.charAt(StringsKt.getLastIndex(r1)), r2, r3) == false) goto L7;
        return true;
    }

    public static final boolean endsWith(CharSequence r8, CharSequence r9, boolean r10) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        Intrinsics.checkNotNullParameter(r9, "suffix");
        if (r10 == true) goto L11;
        if ((r8 instanceof String) == false) goto L11;
        if ((r9 instanceof String) == false) goto L11;
        return StringsKt.endsWith$default((String) r8, (String) r9, false, 2, null);
    L11:
        return StringsKt.regionMatchesImpl(r8, r8.length() - r9.length(), r9, 0, r9.length(), r10);
    }

    public static /* synthetic */ boolean endsWith$default(CharSequence r0, char r1, boolean r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = false;
    L6:
        return StringsKt.endsWith(r0, r1, r2);
    }

    public static /* synthetic */ boolean endsWith$default(CharSequence r0, CharSequence r1, boolean r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = false;
    L6:
        return StringsKt.endsWith(r0, r1, r2);
    }

    public static final Pair<Integer, String> findAnyOf(CharSequence r1, Collection<String> r2, int r3, boolean r4) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "strings");
        return findAnyOf$StringsKt__StringsKt(r1, r2, r3, r4, false);
    }

    private static final Pair<Integer, String> findAnyOf$StringsKt__StringsKt(CharSequence r17, Collection<String> r18, int r19, boolean r20, boolean r21) {
        if (r20 == true) goto L15;
        if (r18.size() != 1) goto L15;
        String r1 = (String) CollectionsKt.single(r18);
        if (r21 == true) goto L9;
        int r3 = StringsKt.indexOf$default(r17, r1, r19, false, 4, null);
    L10:
        if (r3 >= 0) goto L13;
        return null;
    L13:
        return TuplesKt.m428to(Integer.valueOf(r3), r1);
    L9:
        r3 = StringsKt.lastIndexOf$default(r17, r1, r19, false, 4, null);
    L15:
        if (r21 == true) goto L17;
        IntRange r2 = new IntRange(RangesKt.coerceAtLeast(r19, 0), r17.length());
    L18:
        IntProgression r8 = r2;
        if ((r17 instanceof String) == false) goto L39;
        int r4 = r8.getFirst();
        int r5 = r8.getLast();
        int r6 = r8.getStep();
        if (r6 <= 0) goto L23;
        if (r4 > r5) goto L23;
    L25:
        int r12 = r4;
    L26:
        Iterator<T> r7 = r18.iterator();
    L28:
        if (r7.hasNext() == false) goto L32;
        Object r15 = r7.next();
        String r9 = (String) r15;
        if (StringsKt.regionMatches(r9, 0, (String) r17, r12, r9.length(), r20) == false) goto L28;
    L33:
        String r10 = (String) r15;
        if (r10 != null) goto L36;
        if (r12 == r5) goto L59;
        r12 = r12 + r6;
    L59:
        return null;
    L36:
        return TuplesKt.m428to(Integer.valueOf(r12), r10);
    L32:
        r15 = null;
    L23:
        if (r6 >= 0) goto L59;
        if (r5 > r4) goto L59;
    L39:
        int r11 = r8.getFirst();
        int r13 = r8.getLast();
        int r14 = r8.getStep();
        if (r14 <= 0) goto L42;
        if (r11 > r13) goto L42;
    L44:
        int r16 = r11;
    L45:
        Iterator<T> r110 = r18.iterator();
    L47:
        if (r110.hasNext() == false) goto L52;
        Object r111 = r110.next();
        String r112 = (String) r111;
        if (StringsKt.regionMatchesImpl(r112, 0, r17, r16, r112.length(), r20) == false) goto L47;
    L53:
        String r0 = (String) r111;
        if (r0 != null) goto L56;
        if (r16 == r13) goto L59;
        r16 = r16 + r14;
        goto L45
    L56:
        return TuplesKt.m428to(Integer.valueOf(r16), r0);
    L52:
        r111 = null;
    L42:
        if (r14 >= 0) goto L59;
        if (r13 > r11) goto L59;
    L17:
        r2 = RangesKt.downTo(RangesKt.coerceAtMost(r19, StringsKt.getLastIndex(r17)), 0);
        goto L18
    }

    public static /* synthetic */ Pair findAnyOf$default(CharSequence r1, Collection r2, int r3, boolean r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L6;
        r3 = 0;
    L6:
        if ((r5 & 4) == 0) goto L9;
        r4 = false;
    L9:
        return StringsKt.findAnyOf(r1, r2, r3, r4);
    }

    public static final Pair<Integer, String> findLastAnyOf(CharSequence r1, Collection<String> r2, int r3, boolean r4) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "strings");
        return findAnyOf$StringsKt__StringsKt(r1, r2, r3, r4, true);
    }

    public static /* synthetic */ Pair findLastAnyOf$default(CharSequence r0, Collection r1, int r2, boolean r3, int r4, Object r5) {
        if ((r4 & 2) == 0) goto L6;
        r2 = StringsKt.getLastIndex(r0);
    L6:
        if ((r4 & 4) == 0) goto L9;
        r3 = false;
    L9:
        return StringsKt.findLastAnyOf(r0, r1, r2, r3);
    }

    public static final IntRange getIndices(CharSequence r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        return new IntRange(0, r3.length() - 1);
    }

    public static final int getLastIndex(CharSequence r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.length() - 1;
    }

    public static final boolean hasSurrogatePairAt(CharSequence r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (new IntRange(0, r3.length() - 2).contains(r4) == true) goto L5;
        return false;
    L5:
        if (Character.isHighSurrogate(r3.charAt(r4)) == true) goto L7;
        return false;
    L7:
        if (Character.isLowSurrogate(r3.charAt(r4 + 1)) == false) goto L13;
        return true;
    L13:
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <C extends CharSequence & R, R> R ifBlank(C r1, Function0<? extends R> r2) {
        Intrinsics.checkNotNullParameter(r2, "defaultValue");
        if (StringsKt.isBlank(r1) == false) goto L6;
        return r2.invoke();
    L6:
        return r1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <C extends CharSequence & R, R> R ifEmpty(C r1, Function0<? extends R> r2) {
        Intrinsics.checkNotNullParameter(r2, "defaultValue");
        if (r1.length() != 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L9;
        return r2.invoke();
    L9:
        return r1;
    L5:
        r0 = false;
        goto L6
    }

    public static final int indexOf(CharSequence r2, char r3, int r4, boolean r5) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r5 == true) goto L9;
        if ((r2 instanceof String) == false) goto L9;
        return ((String) r2).indexOf(r3, r4);
    L9:
        return StringsKt.indexOfAny(r2, new char[]{r3}, r4, r5);
    }

    public static final int indexOf(CharSequence r9, String r10, int r11, boolean r12) {
        Intrinsics.checkNotNullParameter(r9, "<this>");
        Intrinsics.checkNotNullParameter(r10, "string");
        if (r12 == true) goto L9;
        if ((r9 instanceof String) == false) goto L9;
        return ((String) r9).indexOf(r10, r11);
    L9:
        return indexOf$StringsKt__StringsKt$default(r9, r10, r11, r9.length(), r12, false, 16, null);
    }

    private static final int indexOf$StringsKt__StringsKt(CharSequence r15, CharSequence r16, int r17, int r18, boolean r19, boolean r20) {
        CharSequence r0 = r16;
        if (r20 == true) goto L5;
        IntRange r2 = new IntRange(RangesKt.coerceAtLeast(r17, 0), RangesKt.coerceAtMost(r18, r15.length()));
    L6:
        IntProgression r8 = r2;
        if ((r15 instanceof String) == true) goto L9;
    L21:
        int r1 = r8.getFirst();
        int r9 = r8.getLast();
        int r10 = r8.getStep();
        if (r10 <= 0) goto L24;
        if (r1 > r9) goto L24;
    L26:
        int r3 = r1;
    L28:
        if (StringsKt.regionMatchesImpl(r0, 0, r15, r3, r0.length(), r19) == true) goto L29;
        if (r3 == r9) goto L42;
        r3 = r3 + r10;
        r0 = r16;
        goto L28
    L42:
        return -1;
    L29:
        return r3;
    L24:
        if (r10 >= 0) goto L39;
        if (r9 <= r1) goto L26;
        return -1;
    L39:
        return -1;
    L9:
        if ((r0 instanceof String) == false) goto L21;
        int r4 = r8.getFirst();
        int r5 = r8.getLast();
        int r6 = r8.getStep();
        if (r6 <= 0) goto L13;
        if (r4 > r5) goto L13;
    L15:
        int r12 = r4;
    L17:
        if (StringsKt.regionMatches((String) r0, 0, (String) r15, r12, r0.length(), r19) == true) goto L18;
        if (r12 == r5) goto L41;
        r12 = r12 + r6;
        goto L17
    L41:
        return -1;
    L18:
        return r12;
    L13:
        if (r6 >= 0) goto L32;
        if (r5 <= r4) goto L15;
        return -1;
    L32:
        return -1;
    L5:
        r2 = RangesKt.downTo(RangesKt.coerceAtMost(r17, StringsKt.getLastIndex(r15)), RangesKt.coerceAtLeast(r18, 0));
        goto L6
    }

    static /* synthetic */ int indexOf$StringsKt__StringsKt$default(CharSequence r6, CharSequence r7, int r8, int r9, boolean r10, boolean r11, int r12, Object r13) {
        if ((r12 & 16) == 0) goto L6;
        r11 = false;
    L6:
        return indexOf$StringsKt__StringsKt(r6, r7, r8, r9, r10, r11);
    }

    public static /* synthetic */ int indexOf$default(CharSequence r1, char r2, int r3, boolean r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L6;
        r3 = 0;
    L6:
        if ((r5 & 4) == 0) goto L9;
        r4 = false;
    L9:
        return StringsKt.indexOf(r1, r2, r3, r4);
    }

    public static /* synthetic */ int indexOf$default(CharSequence r1, String r2, int r3, boolean r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L6;
        r3 = 0;
    L6:
        if ((r5 & 4) == 0) goto L9;
        r4 = false;
    L9:
        return StringsKt.indexOf(r1, r2, r3, r4);
    }

    public static final int indexOfAny(CharSequence r1, Collection<String> r2, int r3, boolean r4) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "strings");
        Pair<Integer, String> r0 = findAnyOf$StringsKt__StringsKt(r1, r2, r3, r4, false);
        if (r0 != null) goto L7;
        return -1;
    L7:
        return r0.getFirst().intValue();
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    public static final int indexOfAny(CharSequence r12, char[] r13, int r14, boolean r15) {
        Intrinsics.checkNotNullParameter(r12, "<this>");
        Intrinsics.checkNotNullParameter(r13, "chars");
        if (r15 == false) goto L5;
    L10:
        ?? r1 = new IntRange(RangesKt.coerceAtLeast(r14, 0), StringsKt.getLastIndex(r12)).iterator();
    L12:
        if (r1.hasNext() == false) goto L22;
        int r3 = r1.nextInt();
        char r4 = r12.charAt(r3);
        int r7 = r13.length;
        int r8 = 0;
    L14:
        if (r8 >= r7) goto L19;
        if (CharsKt.equals(r13[r8], r4, r15) == true) goto L17;
        r8 = r8 + 1;
        goto L14
    L17:
        boolean r5 = true;
    L20:
        if (r5 == false) goto L12;
        return r3;
    L19:
        r5 = false;
        goto L20
    L22:
        return -1;
    L5:
        if (r13.length != 1) goto L10;
        if ((r12 instanceof String) == false) goto L10;
        return ((String) r12).indexOf(ArraysKt.single(r13), r14);
    }

    public static /* synthetic */ int indexOfAny$default(CharSequence r1, Collection r2, int r3, boolean r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L6;
        r3 = 0;
    L6:
        if ((r5 & 4) == 0) goto L9;
        r4 = false;
    L9:
        return StringsKt.indexOfAny(r1, r2, r3, r4);
    }

    public static /* synthetic */ int indexOfAny$default(CharSequence r1, char[] r2, int r3, boolean r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L6;
        r3 = 0;
    L6:
        if ((r5 & 4) == 0) goto L9;
        r4 = false;
    L9:
        return StringsKt.indexOfAny(r1, r2, r3, r4);
    }

    private static final boolean isEmpty(CharSequence r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if (r1.length() != 0) goto L5;
        return true;
    L5:
        return false;
    }

    private static final boolean isNotBlank(CharSequence r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return !StringsKt.isBlank(r1);
    }

    private static final boolean isNotEmpty(CharSequence r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if (r1.length() <= 0) goto L5;
        return true;
    L5:
        return false;
    }

    private static final boolean isNullOrBlank(CharSequence r1) {
        if (r1 != null) goto L4;
    L7:
        return true;
    L4:
        if (StringsKt.isBlank(r1) == true) goto L7;
        return false;
    }

    private static final boolean isNullOrEmpty(CharSequence r1) {
        if (r1 != null) goto L4;
    L7:
        return true;
    L4:
        if (r1.length() == 0) goto L7;
        return false;
    }

    public static final CharIterator iterator(final CharSequence r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return new C11601(r1);
    }

    public static final int lastIndexOf(CharSequence r2, char r3, int r4, boolean r5) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r5 == true) goto L9;
        if ((r2 instanceof String) == false) goto L9;
        return ((String) r2).lastIndexOf(r3, r4);
    L9:
        return StringsKt.lastIndexOfAny(r2, new char[]{r3}, r4, r5);
    }

    public static final int lastIndexOf(CharSequence r7, String r8, int r9, boolean r10) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "string");
        if (r10 == true) goto L9;
        if ((r7 instanceof String) == false) goto L9;
        return ((String) r7).lastIndexOf(r8, r9);
    L9:
        return indexOf$StringsKt__StringsKt(r7, r8, r9, 0, r10, true);
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence r0, char r1, int r2, boolean r3, int r4, Object r5) {
        if ((r4 & 2) == 0) goto L6;
        r2 = StringsKt.getLastIndex(r0);
    L6:
        if ((r4 & 4) == 0) goto L9;
        r3 = false;
    L9:
        return StringsKt.lastIndexOf(r0, r1, r2, r3);
    }

    public static /* synthetic */ int lastIndexOf$default(CharSequence r0, String r1, int r2, boolean r3, int r4, Object r5) {
        if ((r4 & 2) == 0) goto L6;
        r2 = StringsKt.getLastIndex(r0);
    L6:
        if ((r4 & 4) == 0) goto L9;
        r3 = false;
    L9:
        return StringsKt.lastIndexOf(r0, r1, r2, r3);
    }

    public static final int lastIndexOfAny(CharSequence r1, Collection<String> r2, int r3, boolean r4) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "strings");
        Pair<Integer, String> r0 = findAnyOf$StringsKt__StringsKt(r1, r2, r3, r4, true);
        if (r0 != null) goto L7;
        return -1;
    L7:
        return r0.getFirst().intValue();
    }

    public static final int lastIndexOfAny(CharSequence r11, char[] r12, int r13, boolean r14) {
        Intrinsics.checkNotNullParameter(r11, "<this>");
        Intrinsics.checkNotNullParameter(r12, "chars");
        if (r14 == false) goto L5;
    L10:
        int r1 = RangesKt.coerceAtMost(r13, StringsKt.getLastIndex(r11));
    L12:
        if ((-1) >= r1) goto L23;
        char r2 = r11.charAt(r1);
        int r5 = r12.length;
        boolean r6 = false;
        int r7 = 0;
    L14:
        if (r7 >= r5) goto L20;
        if (CharsKt.equals(r12[r7], r2, r14) == true) goto L17;
        r7 = r7 + 1;
        goto L14
    L17:
        r6 = true;
    L20:
        if (r6 == true) goto L21;
        r1 = r1 - 1;
        goto L12
    L21:
        return r1;
    L23:
        return -1;
    L5:
        if (r12.length != 1) goto L10;
        if ((r11 instanceof String) == false) goto L10;
        return ((String) r11).lastIndexOf(ArraysKt.single(r12), r13);
    }

    public static /* synthetic */ int lastIndexOfAny$default(CharSequence r0, Collection r1, int r2, boolean r3, int r4, Object r5) {
        if ((r4 & 2) == 0) goto L6;
        r2 = StringsKt.getLastIndex(r0);
    L6:
        if ((r4 & 4) == 0) goto L9;
        r3 = false;
    L9:
        return StringsKt.lastIndexOfAny(r0, r1, r2, r3);
    }

    public static /* synthetic */ int lastIndexOfAny$default(CharSequence r0, char[] r1, int r2, boolean r3, int r4, Object r5) {
        if ((r4 & 2) == 0) goto L6;
        r2 = StringsKt.getLastIndex(r0);
    L6:
        if ((r4 & 4) == 0) goto L9;
        r3 = false;
    L9:
        return StringsKt.lastIndexOfAny(r0, r1, r2, r3);
    }

    public static final Sequence<String> lineSequence(CharSequence r9) {
        Intrinsics.checkNotNullParameter(r9, "<this>");
        return StringsKt.splitToSequence$default(r9, new String[]{"\r\n", "\n", "\r"}, false, 0, 6, null);
    }

    public static final List<String> lines(CharSequence r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return SequencesKt.toList(StringsKt.lineSequence(r1));
    }

    private static final boolean matches(CharSequence r1, Regex r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "regex");
        return r2.matches(r1);
    }

    private static final String orEmpty(String r1) {
        if (r1 != null) goto L5;
        return "";
    L5:
        return r1;
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [kotlin.collections.IntIterator] */
    public static final CharSequence padEnd(CharSequence r4, int r5, char r6) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        if (r5 < 0) goto L15;
        if (r5 <= r4.length()) goto L7;
        StringBuilder r0 = new StringBuilder(r5);
        r0.append(r4);
        ?? r1 = new IntRange(1, r5 - r4.length()).iterator();
    L10:
        if (r1.hasNext() == false) goto L13;
        r1.nextInt();
        r0.append(r6);
        goto L10
    L13:
        return r0;
    L7:
        return r4.subSequence(0, r4.length());
    L15:
        throw new IllegalArgumentException("Desired length " + r5 + " is less than zero.");
    }

    public static final String padEnd(String r1, int r2, char r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return StringsKt.padEnd(r1, r2, r3).toString();
    }

    public static /* synthetic */ CharSequence padEnd$default(CharSequence r0, int r1, char r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = ' ';
    L6:
        return StringsKt.padEnd(r0, r1, r2);
    }

    public static /* synthetic */ String padEnd$default(String r0, int r1, char r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = ' ';
    L6:
        return StringsKt.padEnd(r0, r1, r2);
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [kotlin.collections.IntIterator] */
    public static final CharSequence padStart(CharSequence r4, int r5, char r6) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        if (r5 < 0) goto L15;
        if (r5 <= r4.length()) goto L7;
        StringBuilder r0 = new StringBuilder(r5);
        ?? r1 = new IntRange(1, r5 - r4.length()).iterator();
    L10:
        if (r1.hasNext() == false) goto L12;
        r1.nextInt();
        r0.append(r6);
        goto L10
    L12:
        r0.append(r4);
        return r0;
    L7:
        return r4.subSequence(0, r4.length());
    L15:
        throw new IllegalArgumentException("Desired length " + r5 + " is less than zero.");
    }

    public static final String padStart(String r1, int r2, char r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return StringsKt.padStart(r1, r2, r3).toString();
    }

    public static /* synthetic */ CharSequence padStart$default(CharSequence r0, int r1, char r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = ' ';
    L6:
        return StringsKt.padStart(r0, r1, r2);
    }

    public static /* synthetic */ String padStart$default(String r0, int r1, char r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = ' ';
    L6:
        return StringsKt.padStart(r0, r1, r2);
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(CharSequence r2, final char[] r3, int r4, final boolean r5, int r6) {
        StringsKt.requireNonNegativeLimit(r6);
        return new DelimitedRangesSequence(r2, r4, r6, new StringsKt__StringsKt$rangesDelimitedBy$1(r3, r5));
    }

    private static final Sequence<IntRange> rangesDelimitedBy$StringsKt__StringsKt(CharSequence r3, String[] r4, int r5, final boolean r6, int r7) {
        StringsKt.requireNonNegativeLimit(r7);
        final List r0 = ArraysKt.asList(r4);
        return new DelimitedRangesSequence(r3, r5, r7, new StringsKt__StringsKt$rangesDelimitedBy$2(r0, r6));
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence r1, char[] r2, int r3, boolean r4, int r5, int r6, Object r7) {
        if ((r6 & 2) == 0) goto L6;
        r3 = 0;
    L6:
        if ((r6 & 4) == 0) goto L9;
        r4 = false;
    L9:
        if ((r6 & 8) == 0) goto L12;
        r5 = 0;
    L12:
        return rangesDelimitedBy$StringsKt__StringsKt(r1, r2, r3, r4, r5);
    }

    static /* synthetic */ Sequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence r1, String[] r2, int r3, boolean r4, int r5, int r6, Object r7) {
        if ((r6 & 2) == 0) goto L6;
        r3 = 0;
    L6:
        if ((r6 & 4) == 0) goto L9;
        r4 = false;
    L9:
        if ((r6 & 8) == 0) goto L12;
        r5 = 0;
    L12:
        return rangesDelimitedBy$StringsKt__StringsKt(r1, r2, r3, r4, r5);
    }

    public static final boolean regionMatchesImpl(CharSequence r4, int r5, CharSequence r6, int r7, int r8, boolean r9) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r6, "other");
        if (r7 < 0) goto L18;
        if (r5 < 0) goto L18;
        if (r5 > (r4.length() - r8)) goto L18;
        if (r7 > (r6.length() - r8)) goto L18;
        int r1 = 0;
    L11:
        if (r1 >= r8) goto L16;
        if (CharsKt.equals(r4.charAt(r5 + r1), r6.charAt(r7 + r1), r9) == false) goto L14;
        r1 = r1 + 1;
        goto L11
    L14:
        return false;
    L16:
        return true;
    L18:
        return false;
    }

    public static final CharSequence removePrefix(CharSequence r3, CharSequence r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "prefix");
        if (StringsKt.startsWith$default(r3, r4, false, 2, null) == false) goto L7;
        return r3.subSequence(r4.length(), r3.length());
    L7:
        return r3.subSequence(0, r3.length());
    }

    public static final String removePrefix(String r4, CharSequence r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "prefix");
        if (StringsKt.startsWith$default(r4, r5, false, 2, null) == false) goto L6;
        String r0 = r4.substring(r5.length());
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        return r0;
    L6:
        return r4;
    }

    public static final CharSequence removeRange(CharSequence r4, int r5, int r6) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        if (r6 < r5) goto L11;
        if (r6 == r5) goto L7;
        StringBuilder r1 = new StringBuilder(r4.length() - (r6 - r5));
        Intrinsics.checkNotNullExpressionValue(r1.append(r4, 0, r5), "append(...)");
        Intrinsics.checkNotNullExpressionValue(r1.append(r4, r6, r4.length()), "append(...)");
        return r1;
    L7:
        return r4.subSequence(0, r4.length());
    L11:
        throw new IndexOutOfBoundsException("End index (" + r6 + ") is less than start index (" + r5 + ").");
    }

    public static final CharSequence removeRange(CharSequence r2, IntRange r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "range");
        return StringsKt.removeRange(r2, r3.getStart().intValue(), r3.getEndInclusive().intValue() + 1);
    }

    private static final String removeRange(String r1, int r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return StringsKt.removeRange(r1, r2, r3).toString();
    }

    private static final String removeRange(String r1, IntRange r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "range");
        return StringsKt.removeRange(r1, r2).toString();
    }

    public static final CharSequence removeSuffix(CharSequence r3, CharSequence r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "suffix");
        if (StringsKt.endsWith$default(r3, r4, false, 2, null) == false) goto L7;
        return r3.subSequence(0, r3.length() - r4.length());
    L7:
        return r3.subSequence(0, r3.length());
    }

    public static final String removeSuffix(String r4, CharSequence r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "suffix");
        if (StringsKt.endsWith$default(r4, r5, false, 2, null) == false) goto L6;
        String r0 = r4.substring(0, r4.length() - r5.length());
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        return r0;
    L6:
        return r4;
    }

    public static final CharSequence removeSurrounding(CharSequence r1, CharSequence r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, TtmlNode.RUBY_DELIMITER);
        return StringsKt.removeSurrounding(r1, r2, r2);
    }

    public static final CharSequence removeSurrounding(CharSequence r4, CharSequence r5, CharSequence r6) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "prefix");
        Intrinsics.checkNotNullParameter(r6, "suffix");
        if (r4.length() < (r5.length() + r6.length())) goto L11;
        if (StringsKt.startsWith$default(r4, r5, false, 2, null) == false) goto L11;
        if (StringsKt.endsWith$default(r4, r6, false, 2, null) == false) goto L11;
        return r4.subSequence(r5.length(), r4.length() - r6.length());
    L11:
        return r4.subSequence(0, r4.length());
    }

    public static final String removeSurrounding(String r1, CharSequence r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, TtmlNode.RUBY_DELIMITER);
        return StringsKt.removeSurrounding(r1, r2, r2);
    }

    public static final String removeSurrounding(String r4, CharSequence r5, CharSequence r6) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "prefix");
        Intrinsics.checkNotNullParameter(r6, "suffix");
        if (r4.length() >= (r5.length() + r6.length())) goto L5;
    L10:
        return r4;
    L5:
        if (StringsKt.startsWith$default(r4, r5, false, 2, null) == false) goto L10;
        if (StringsKt.endsWith$default(r4, r6, false, 2, null) == false) goto L10;
        String r0 = r4.substring(r5.length(), r4.length() - r6.length());
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        return r0;
    }

    private static final String replace(CharSequence r1, Regex r2, String r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "regex");
        Intrinsics.checkNotNullParameter(r3, "replacement");
        return r2.replace(r1, r3);
    }

    private static final String replace(CharSequence r1, Regex r2, Function1<? super MatchResult, ? extends CharSequence> r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "regex");
        Intrinsics.checkNotNullParameter(r3, "transform");
        return r2.replace(r1, r3);
    }

    public static final String replaceAfter(String r7, char r8, String r9, String r10) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r9, "replacement");
        Intrinsics.checkNotNullParameter(r10, "missingDelimiterValue");
        int r11 = StringsKt.indexOf$default(r7, r8, 0, false, 6, null);
        if (r11 == (-1)) goto L7;
        String r3 = r7;
        return StringsKt.replaceRange(r3, r11 + 1, r7.length(), r9).toString();
    L7:
        return r10;
    }

    public static final String replaceAfter(String r7, String r8, String r9, String r10) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, TtmlNode.RUBY_DELIMITER);
        Intrinsics.checkNotNullParameter(r9, "replacement");
        Intrinsics.checkNotNullParameter(r10, "missingDelimiterValue");
        int r11 = StringsKt.indexOf$default(r7, r8, 0, false, 6, null);
        if (r11 == (-1)) goto L7;
        String r3 = r7;
        return StringsKt.replaceRange(r3, r8.length() + r11, r7.length(), r9).toString();
    L7:
        return r10;
    }

    public static /* synthetic */ String replaceAfter$default(String r0, char r1, String r2, String r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L6;
        r3 = r0;
    L6:
        return StringsKt.replaceAfter(r0, r1, r2, r3);
    }

    public static /* synthetic */ String replaceAfter$default(String r0, String r1, String r2, String r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L6;
        r3 = r0;
    L6:
        return StringsKt.replaceAfter(r0, r1, r2, r3);
    }

    public static final String replaceAfterLast(String r7, char r8, String r9, String r10) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r9, "replacement");
        Intrinsics.checkNotNullParameter(r10, "missingDelimiterValue");
        int r11 = StringsKt.lastIndexOf$default(r7, r8, 0, false, 6, null);
        if (r11 == (-1)) goto L7;
        String r3 = r7;
        return StringsKt.replaceRange(r3, r11 + 1, r7.length(), r9).toString();
    L7:
        return r10;
    }

    public static final String replaceAfterLast(String r7, String r8, String r9, String r10) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, TtmlNode.RUBY_DELIMITER);
        Intrinsics.checkNotNullParameter(r9, "replacement");
        Intrinsics.checkNotNullParameter(r10, "missingDelimiterValue");
        int r11 = StringsKt.lastIndexOf$default(r7, r8, 0, false, 6, null);
        if (r11 == (-1)) goto L7;
        String r3 = r7;
        return StringsKt.replaceRange(r3, r8.length() + r11, r7.length(), r9).toString();
    L7:
        return r10;
    }

    public static /* synthetic */ String replaceAfterLast$default(String r0, char r1, String r2, String r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L6;
        r3 = r0;
    L6:
        return StringsKt.replaceAfterLast(r0, r1, r2, r3);
    }

    public static /* synthetic */ String replaceAfterLast$default(String r0, String r1, String r2, String r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L6;
        r3 = r0;
    L6:
        return StringsKt.replaceAfterLast(r0, r1, r2, r3);
    }

    public static final String replaceBefore(String r7, char r8, String r9, String r10) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r9, "replacement");
        Intrinsics.checkNotNullParameter(r10, "missingDelimiterValue");
        int r11 = StringsKt.indexOf$default(r7, r8, 0, false, 6, null);
        if (r11 != (-1)) goto L6;
        return r10;
    L6:
        return StringsKt.replaceRange(r7, 0, r11, r9).toString();
    }

    public static final String replaceBefore(String r7, String r8, String r9, String r10) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, TtmlNode.RUBY_DELIMITER);
        Intrinsics.checkNotNullParameter(r9, "replacement");
        Intrinsics.checkNotNullParameter(r10, "missingDelimiterValue");
        int r11 = StringsKt.indexOf$default(r7, r8, 0, false, 6, null);
        if (r11 != (-1)) goto L6;
        return r10;
    L6:
        return StringsKt.replaceRange(r7, 0, r11, r9).toString();
    }

    public static /* synthetic */ String replaceBefore$default(String r0, char r1, String r2, String r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L6;
        r3 = r0;
    L6:
        return StringsKt.replaceBefore(r0, r1, r2, r3);
    }

    public static /* synthetic */ String replaceBefore$default(String r0, String r1, String r2, String r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L6;
        r3 = r0;
    L6:
        return StringsKt.replaceBefore(r0, r1, r2, r3);
    }

    public static final String replaceBeforeLast(String r7, char r8, String r9, String r10) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r9, "replacement");
        Intrinsics.checkNotNullParameter(r10, "missingDelimiterValue");
        int r11 = StringsKt.lastIndexOf$default(r7, r8, 0, false, 6, null);
        if (r11 != (-1)) goto L6;
        return r10;
    L6:
        return StringsKt.replaceRange(r7, 0, r11, r9).toString();
    }

    public static final String replaceBeforeLast(String r7, String r8, String r9, String r10) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, TtmlNode.RUBY_DELIMITER);
        Intrinsics.checkNotNullParameter(r9, "replacement");
        Intrinsics.checkNotNullParameter(r10, "missingDelimiterValue");
        int r11 = StringsKt.lastIndexOf$default(r7, r8, 0, false, 6, null);
        if (r11 != (-1)) goto L6;
        return r10;
    L6:
        return StringsKt.replaceRange(r7, 0, r11, r9).toString();
    }

    public static /* synthetic */ String replaceBeforeLast$default(String r0, char r1, String r2, String r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L6;
        r3 = r0;
    L6:
        return StringsKt.replaceBeforeLast(r0, r1, r2, r3);
    }

    public static /* synthetic */ String replaceBeforeLast$default(String r0, String r1, String r2, String r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L6;
        r3 = r0;
    L6:
        return StringsKt.replaceBeforeLast(r0, r1, r2, r3);
    }

    private static final String replaceFirst(CharSequence r1, Regex r2, String r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "regex");
        Intrinsics.checkNotNullParameter(r3, "replacement");
        return r2.replaceFirst(r1, r3);
    }

    private static final String replaceFirstCharWithChar(String r3, Function1<? super Character, Character> r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "transform");
        if (r3.length() <= 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L9;
        char r1 = r4.invoke(Character.valueOf(r3.charAt(0))).charValue();
        String r2 = r3.substring(1);
        Intrinsics.checkNotNullExpressionValue(r2, "substring(...)");
        return r1 + r2;
    L9:
        return r3;
    L5:
        r0 = false;
        goto L6
    }

    private static final String replaceFirstCharWithCharSequence(String r3, Function1<? super Character, ? extends CharSequence> r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "transform");
        if (r3.length() <= 0) goto L5;
        boolean r0 = true;
    L6:
        if (r0 == false) goto L9;
        StringBuilder r1 = new StringBuilder().append(r4.invoke(Character.valueOf(r3.charAt(0))));
        String r2 = r3.substring(1);
        Intrinsics.checkNotNullExpressionValue(r2, "substring(...)");
        return r1.append(r2).toString();
    L9:
        return r3;
    L5:
        r0 = false;
        goto L6
    }

    public static final CharSequence replaceRange(CharSequence r3, int r4, int r5, CharSequence r6) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r6, "replacement");
        if (r5 < r4) goto L7;
        StringBuilder r0 = new StringBuilder();
        Intrinsics.checkNotNullExpressionValue(r0.append(r3, 0, r4), "append(...)");
        r0.append(r6);
        Intrinsics.checkNotNullExpressionValue(r0.append(r3, r5, r3.length()), "append(...)");
        return r0;
    L7:
        throw new IndexOutOfBoundsException("End index (" + r5 + ") is less than start index (" + r4 + ").");
    }

    public static final CharSequence replaceRange(CharSequence r2, IntRange r3, CharSequence r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "range");
        Intrinsics.checkNotNullParameter(r4, "replacement");
        return StringsKt.replaceRange(r2, r3.getStart().intValue(), r3.getEndInclusive().intValue() + 1, r4);
    }

    private static final String replaceRange(String r1, int r2, int r3, CharSequence r4) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r4, "replacement");
        return StringsKt.replaceRange(r1, r2, r3, r4).toString();
    }

    private static final String replaceRange(String r1, IntRange r2, CharSequence r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "range");
        Intrinsics.checkNotNullParameter(r3, "replacement");
        return StringsKt.replaceRange(r1, r2, r3).toString();
    }

    public static final void requireNonNegativeLimit(int r3) {
        if (r3 < 0) goto L4;
        boolean r0 = true;
    L5:
        if (r0 == false) goto L8;
        return;
    L8:
        throw new IllegalArgumentException(("Limit must be non-negative, but was " + r3).toString());
    L4:
        r0 = false;
        goto L5
    }

    private static final List<String> split(CharSequence r1, Regex r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "regex");
        return r2.split(r1, r3);
    }

    public static final List<String> split(CharSequence r9, char[] r10, boolean r11, int r12) {
        Intrinsics.checkNotNullParameter(r9, "<this>");
        Intrinsics.checkNotNullParameter(r10, "delimiters");
        if (r10.length == 1) goto L5;
        Iterable r13 = SequencesKt.asIterable(rangesDelimitedBy$StringsKt__StringsKt$default(r9, r10, 0, r11, r12, 2, null));
        ArrayList r14 = new ArrayList(CollectionsKt.collectionSizeOrDefault(r13, 10));
        Iterator r3 = r13.iterator();
    L8:
        if (r3.hasNext() == false) goto L11;
        r14.add(StringsKt.substring(r9, (IntRange) r3.next()));
        goto L8
    L11:
        return r14;
    L5:
        return split$StringsKt__StringsKt(r9, String.valueOf(r10[0]), r11, r12);
    }

    public static final List<String> split(CharSequence r10, String[] r11, boolean r12, int r13) {
        Intrinsics.checkNotNullParameter(r10, "<this>");
        Intrinsics.checkNotNullParameter(r11, "delimiters");
        boolean r1 = true;
        if (r11.length != 1) goto L11;
        String r2 = r11[0];
        if (r2.length() == 0) goto L8;
        r1 = false;
    L8:
        if (r1 == true) goto L11;
        return split$StringsKt__StringsKt(r10, r2, r12, r13);
    L11:
        Iterable r14 = SequencesKt.asIterable(rangesDelimitedBy$StringsKt__StringsKt$default(r10, r11, 0, r12, r13, 2, null));
        ArrayList r15 = new ArrayList(CollectionsKt.collectionSizeOrDefault(r14, 10));
        Iterator r3 = r14.iterator();
    L13:
        if (r3.hasNext() == false) goto L16;
        r15.add(StringsKt.substring(r10, (IntRange) r3.next()));
        goto L13
    L16:
        return r15;
    }

    private static final List<String> split$StringsKt__StringsKt(CharSequence r7, String r8, boolean r9, int r10) {
        StringsKt.requireNonNegativeLimit(r10);
        int r0 = 0;
        int r1 = StringsKt.indexOf(r7, r8, 0, r9);
        if (r1 == (-1)) goto L23;
        boolean r3 = true;
        if (r10 == 1) goto L23;
        if (r10 > 0) goto L10;
        r3 = false;
    L10:
        int r5 = 10;
        if (r3 == false) goto L13;
        r5 = RangesKt.coerceAtMost(r10, 10);
    L13:
        ArrayList r4 = new ArrayList(r5);
    L14:
        r4.add(r7.subSequence(r0, r1).toString());
        r0 = r1 + r8.length();
        if (r3 == true) goto L17;
    L18:
        r1 = StringsKt.indexOf(r7, r8, r0, r9);
        if (r1 != (-1)) goto L14;
    L20:
        r4.add(r7.subSequence(r0, r7.length()).toString());
        return r4;
    L17:
        if (r4.size() == (r10 - 1)) goto L20;
    L23:
        return CollectionsKt.listOf(r7.toString());
    }

    static /* synthetic */ List split$default(CharSequence r0, Regex r1, int r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L5;
        r2 = 0;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "regex");
        return r1.split(r0, r2);
    }

    public static /* synthetic */ List split$default(CharSequence r1, char[] r2, boolean r3, int r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L6;
        r3 = false;
    L6:
        if ((r5 & 4) == 0) goto L9;
        r4 = 0;
    L9:
        return StringsKt.split(r1, r2, r3, r4);
    }

    public static /* synthetic */ List split$default(CharSequence r1, String[] r2, boolean r3, int r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L6;
        r3 = false;
    L6:
        if ((r5 & 4) == 0) goto L9;
        r4 = 0;
    L9:
        return StringsKt.split(r1, r2, r3, r4);
    }

    private static final Sequence<String> splitToSequence(CharSequence r1, Regex r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "regex");
        return r2.splitToSequence(r1, r3);
    }

    public static final Sequence<String> splitToSequence(final CharSequence r8, char[] r9, boolean r10, int r11) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        Intrinsics.checkNotNullParameter(r9, "delimiters");
        return SequencesKt.map(rangesDelimitedBy$StringsKt__StringsKt$default(r8, r9, 0, r10, r11, 2, null), new C11622(r8));
    }

    public static final Sequence<String> splitToSequence(final CharSequence r8, String[] r9, boolean r10, int r11) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        Intrinsics.checkNotNullParameter(r9, "delimiters");
        return SequencesKt.map(rangesDelimitedBy$StringsKt__StringsKt$default(r8, r9, 0, r10, r11, 2, null), new C11611(r8));
    }

    static /* synthetic */ Sequence splitToSequence$default(CharSequence r0, Regex r1, int r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L5;
        r2 = 0;
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        Intrinsics.checkNotNullParameter(r1, "regex");
        return r1.splitToSequence(r0, r2);
    }

    public static /* synthetic */ Sequence splitToSequence$default(CharSequence r1, char[] r2, boolean r3, int r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L6;
        r3 = false;
    L6:
        if ((r5 & 4) == 0) goto L9;
        r4 = 0;
    L9:
        return StringsKt.splitToSequence(r1, r2, r3, r4);
    }

    public static /* synthetic */ Sequence splitToSequence$default(CharSequence r1, String[] r2, boolean r3, int r4, int r5, Object r6) {
        if ((r5 & 2) == 0) goto L6;
        r3 = false;
    L6:
        if ((r5 & 4) == 0) goto L9;
        r4 = 0;
    L9:
        return StringsKt.splitToSequence(r1, r2, r3, r4);
    }

    public static final boolean startsWith(CharSequence r2, char r3, boolean r4) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.length() > 0) goto L5;
        return false;
    L5:
        if (CharsKt.equals(r2.charAt(0), r3, r4) == false) goto L9;
        return true;
    L9:
        return false;
    }

    public static final boolean startsWith(CharSequence r7, CharSequence r8, int r9, boolean r10) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "prefix");
        if (r10 == true) goto L11;
        if ((r7 instanceof String) == false) goto L11;
        if ((r8 instanceof String) == false) goto L11;
        return StringsKt.startsWith$default((String) r7, (String) r8, r9, false, 4, null);
    L11:
        return StringsKt.regionMatchesImpl(r7, r9, r8, 0, r8.length(), r10);
    }

    public static final boolean startsWith(CharSequence r7, CharSequence r8, boolean r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "prefix");
        if (r9 == true) goto L11;
        if ((r7 instanceof String) == false) goto L11;
        if ((r8 instanceof String) == false) goto L11;
        return StringsKt.startsWith$default((String) r7, (String) r8, false, 2, null);
    L11:
        return StringsKt.regionMatchesImpl(r7, 0, r8, 0, r8.length(), r9);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence r0, char r1, boolean r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = false;
    L6:
        return StringsKt.startsWith(r0, r1, r2);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence r0, CharSequence r1, int r2, boolean r3, int r4, Object r5) {
        if ((r4 & 4) == 0) goto L6;
        r3 = false;
    L6:
        return StringsKt.startsWith(r0, r1, r2, r3);
    }

    public static /* synthetic */ boolean startsWith$default(CharSequence r0, CharSequence r1, boolean r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = false;
    L6:
        return StringsKt.startsWith(r0, r1, r2);
    }

    public static final CharSequence subSequence(CharSequence r2, IntRange r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "range");
        return r2.subSequence(r3.getStart().intValue(), r3.getEndInclusive().intValue() + 1);
    }

    @Deprecated(message = "Use parameters named startIndex and endIndex.", replaceWith = @ReplaceWith(expression = "subSequence(startIndex = start, endIndex = end)", imports = {}))
    private static final CharSequence subSequence(String r1, int r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.subSequence(r2, r3);
    }

    private static final String substring(CharSequence r1, int r2, int r3) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.subSequence(r2, r3).toString();
    }

    public static final String substring(CharSequence r2, IntRange r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "range");
        return r2.subSequence(r3.getStart().intValue(), r3.getEndInclusive().intValue() + 1).toString();
    }

    public static final String substring(String r2, IntRange r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "range");
        String r0 = r2.substring(r3.getStart().intValue(), r3.getEndInclusive().intValue() + 1);
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        return r0;
    }

    static /* synthetic */ String substring$default(CharSequence r0, int r1, int r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L5;
        r2 = r0.length();
    L5:
        Intrinsics.checkNotNullParameter(r0, "<this>");
        return r0.subSequence(r1, r2).toString();
    }

    public static final String substringAfter(String r7, char r8, String r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r9, "missingDelimiterValue");
        int r10 = StringsKt.indexOf$default(r7, r8, 0, false, 6, null);
        if (r10 == (-1)) goto L7;
        String r0 = r7.substring(r10 + 1, r7.length());
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        return r0;
    L7:
        return r9;
    }

    public static final String substringAfter(String r7, String r8, String r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, TtmlNode.RUBY_DELIMITER);
        Intrinsics.checkNotNullParameter(r9, "missingDelimiterValue");
        int r10 = StringsKt.indexOf$default(r7, r8, 0, false, 6, null);
        if (r10 == (-1)) goto L7;
        String r0 = r7.substring(r8.length() + r10, r7.length());
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        return r0;
    L7:
        return r9;
    }

    public static /* synthetic */ String substringAfter$default(String r0, char r1, String r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = r0;
    L6:
        return StringsKt.substringAfter(r0, r1, r2);
    }

    public static /* synthetic */ String substringAfter$default(String r0, String r1, String r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = r0;
    L6:
        return StringsKt.substringAfter(r0, r1, r2);
    }

    public static final String substringAfterLast(String r7, char r8, String r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r9, "missingDelimiterValue");
        int r10 = StringsKt.lastIndexOf$default(r7, r8, 0, false, 6, null);
        if (r10 == (-1)) goto L7;
        String r0 = r7.substring(r10 + 1, r7.length());
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        return r0;
    L7:
        return r9;
    }

    public static final String substringAfterLast(String r7, String r8, String r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, TtmlNode.RUBY_DELIMITER);
        Intrinsics.checkNotNullParameter(r9, "missingDelimiterValue");
        int r10 = StringsKt.lastIndexOf$default(r7, r8, 0, false, 6, null);
        if (r10 == (-1)) goto L7;
        String r0 = r7.substring(r8.length() + r10, r7.length());
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        return r0;
    L7:
        return r9;
    }

    public static /* synthetic */ String substringAfterLast$default(String r0, char r1, String r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = r0;
    L6:
        return StringsKt.substringAfterLast(r0, r1, r2);
    }

    public static /* synthetic */ String substringAfterLast$default(String r0, String r1, String r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = r0;
    L6:
        return StringsKt.substringAfterLast(r0, r1, r2);
    }

    public static final String substringBefore(String r7, char r8, String r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r9, "missingDelimiterValue");
        int r10 = StringsKt.indexOf$default(r7, r8, 0, false, 6, null);
        if (r10 == (-1)) goto L7;
        String r0 = r7.substring(0, r10);
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        return r0;
    L7:
        return r9;
    }

    public static final String substringBefore(String r7, String r8, String r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, TtmlNode.RUBY_DELIMITER);
        Intrinsics.checkNotNullParameter(r9, "missingDelimiterValue");
        int r10 = StringsKt.indexOf$default(r7, r8, 0, false, 6, null);
        if (r10 == (-1)) goto L7;
        String r0 = r7.substring(0, r10);
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        return r0;
    L7:
        return r9;
    }

    public static /* synthetic */ String substringBefore$default(String r0, char r1, String r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = r0;
    L6:
        return StringsKt.substringBefore(r0, r1, r2);
    }

    public static /* synthetic */ String substringBefore$default(String r0, String r1, String r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = r0;
    L6:
        return StringsKt.substringBefore(r0, r1, r2);
    }

    public static final String substringBeforeLast(String r7, char r8, String r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r9, "missingDelimiterValue");
        int r10 = StringsKt.lastIndexOf$default(r7, r8, 0, false, 6, null);
        if (r10 == (-1)) goto L7;
        String r0 = r7.substring(0, r10);
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        return r0;
    L7:
        return r9;
    }

    public static final String substringBeforeLast(String r7, String r8, String r9) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, TtmlNode.RUBY_DELIMITER);
        Intrinsics.checkNotNullParameter(r9, "missingDelimiterValue");
        int r10 = StringsKt.lastIndexOf$default(r7, r8, 0, false, 6, null);
        if (r10 == (-1)) goto L7;
        String r0 = r7.substring(0, r10);
        Intrinsics.checkNotNullExpressionValue(r0, "substring(...)");
        return r0;
    L7:
        return r9;
    }

    public static /* synthetic */ String substringBeforeLast$default(String r0, char r1, String r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = r0;
    L6:
        return StringsKt.substringBeforeLast(r0, r1, r2);
    }

    public static /* synthetic */ String substringBeforeLast$default(String r0, String r1, String r2, int r3, Object r4) {
        if ((r3 & 2) == 0) goto L6;
        r2 = r0;
    L6:
        return StringsKt.substringBeforeLast(r0, r1, r2);
    }

    public static final boolean toBooleanStrict(String r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (Intrinsics.areEqual(r3, "true") == false) goto L6;
        return true;
    L6:
        if (Intrinsics.areEqual(r3, "false") == false) goto L10;
        return false;
    L10:
        throw new IllegalArgumentException("The string doesn't represent a boolean value: " + r3);
    }

    public static final Boolean toBooleanStrictOrNull(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if (Intrinsics.areEqual(r1, "true") == false) goto L6;
        return true;
    L6:
        if (Intrinsics.areEqual(r1, "false") == true) goto L11;
        return null;
    L11:
        return false;
    }

    public static final CharSequence trim(CharSequence r8) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        int r2 = 0;
        int r3 = r8.length() - 1;
        boolean r4 = false;
    L3:
        if (r2 > r3) goto L16;
        if (r4 == true) goto L6;
        int r5 = r2;
    L7:
        boolean r6 = CharsKt.isWhitespace(r8.charAt(r5));
        if (r4 == false) goto L9;
        if (r6 == false) goto L16;
        r3 = r3 - 1;
        goto L3
    L9:
        if (r6 == false) goto L10;
        r2 = r2 + 1;
        goto L3
    L10:
        r4 = true;
        goto L3
    L6:
        r5 = r3;
    L16:
        return r8.subSequence(r2, r3 + 1);
    }

    public static final CharSequence trim(CharSequence r6, Function1<? super Character, Boolean> r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "predicate");
        int r1 = 0;
        int r2 = r6.length() - 1;
        boolean r3 = false;
    L3:
        if (r1 > r2) goto L16;
        if (r3 == true) goto L6;
        int r4 = r1;
    L7:
        boolean r5 = r7.invoke(Character.valueOf(r6.charAt(r4))).booleanValue();
        if (r3 == false) goto L9;
        if (r5 == false) goto L16;
        r2 = r2 - 1;
        goto L3
    L9:
        if (r5 == false) goto L10;
        r1 = r1 + 1;
        goto L3
    L10:
        r3 = true;
        goto L3
    L6:
        r4 = r2;
    L16:
        return r6.subSequence(r1, r2 + 1);
    }

    public static final CharSequence trim(CharSequence r8, char... r9) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        Intrinsics.checkNotNullParameter(r9, "chars");
        int r2 = 0;
        int r3 = r8.length() - 1;
        boolean r4 = false;
    L3:
        if (r2 > r3) goto L16;
        if (r4 == true) goto L6;
        int r5 = r2;
    L7:
        boolean r6 = ArraysKt.contains(r9, r8.charAt(r5));
        if (r4 == false) goto L9;
        if (r6 == false) goto L16;
        r3 = r3 - 1;
        goto L3
    L9:
        if (r6 == false) goto L10;
        r2 = r2 + 1;
        goto L3
    L10:
        r4 = true;
        goto L3
    L6:
        r5 = r3;
    L16:
        return r8.subSequence(r2, r3 + 1);
    }

    private static final String trim(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return StringsKt.trim(r1).toString();
    }

    public static final String trim(String r8, Function1<? super Character, Boolean> r9) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        Intrinsics.checkNotNullParameter(r9, "predicate");
        String r1 = r8;
        int r3 = 0;
        int r4 = r1.length() - 1;
        boolean r5 = false;
    L3:
        if (r3 > r4) goto L16;
        if (r5 == true) goto L6;
        int r6 = r3;
    L7:
        boolean r7 = r9.invoke(Character.valueOf(r1.charAt(r6))).booleanValue();
        if (r5 == false) goto L9;
        if (r7 == false) goto L16;
        r4 = r4 - 1;
        goto L3
    L9:
        if (r7 == false) goto L10;
        r3 = r3 + 1;
        goto L3
    L10:
        r5 = true;
        goto L3
    L6:
        r6 = r4;
    L16:
        return r1.subSequence(r3, r4 + 1).toString();
    }

    public static final String trim(String r10, char... r11) {
        Intrinsics.checkNotNullParameter(r10, "<this>");
        Intrinsics.checkNotNullParameter(r11, "chars");
        String r2 = r10;
        int r4 = 0;
        int r5 = r2.length() - 1;
        boolean r6 = false;
    L3:
        if (r4 > r5) goto L16;
        if (r6 == true) goto L6;
        int r7 = r4;
    L7:
        boolean r8 = ArraysKt.contains(r11, r2.charAt(r7));
        if (r6 == false) goto L9;
        if (r8 == false) goto L16;
        r5 = r5 - 1;
        goto L3
    L9:
        if (r8 == false) goto L10;
        r4 = r4 + 1;
        goto L3
    L10:
        r6 = true;
        goto L3
    L6:
        r7 = r5;
    L16:
        return r2.subSequence(r4, r5 + 1).toString();
    }

    public static final CharSequence trimEnd(CharSequence r6) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        int r2 = r6.length() - 1;
        if (r2 < 0) goto L9;
    L4:
        int r3 = r2;
        r2 = r2 - 1;
        if (CharsKt.isWhitespace(r6.charAt(r3)) == false) goto L13;
        if (r2 >= 0) goto L4;
    L13:
        return r6.subSequence(0, r3 + 1);
    L9:
        return "";
    }

    public static final CharSequence trimEnd(CharSequence r4, Function1<? super Character, Boolean> r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "predicate");
        int r1 = r4.length() - 1;
        if (r1 < 0) goto L10;
    L4:
        int r2 = r1;
        r1 = r1 - 1;
        if (r5.invoke(Character.valueOf(r4.charAt(r2))).booleanValue() == false) goto L7;
        if (r1 >= 0) goto L4;
    L7:
        return r4.subSequence(0, r2 + 1);
    L10:
        return "";
    }

    public static final CharSequence trimEnd(CharSequence r6, char... r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "chars");
        int r2 = r6.length() - 1;
        if (r2 < 0) goto L9;
    L4:
        int r3 = r2;
        r2 = r2 - 1;
        if (ArraysKt.contains(r7, r6.charAt(r3)) == false) goto L13;
        if (r2 >= 0) goto L4;
    L13:
        return r6.subSequence(0, r3 + 1);
    L9:
        return "";
    }

    private static final String trimEnd(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return StringsKt.trimEnd(r1).toString();
    }

    public static final String trimEnd(String r6, Function1<? super Character, Boolean> r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "predicate");
        String r1 = r6;
        int r3 = r1.length() - 1;
        if (r3 < 0) goto L8;
    L4:
        int r4 = r3;
        r3 = r3 - 1;
        if (r7.invoke(Character.valueOf(r1.charAt(r4))).booleanValue() == false) goto L6;
        if (r3 >= 0) goto L4;
    L6:
        String r5 = r1.subSequence(0, r4 + 1);
    L10:
        return r5.toString();
    L8:
        goto L10
    }

    public static final String trimEnd(String r8, char... r9) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        Intrinsics.checkNotNullParameter(r9, "chars");
        String r2 = r8;
        int r4 = r2.length() - 1;
        if (r4 < 0) goto L8;
    L4:
        int r5 = r4;
        r4 = r4 - 1;
        if (ArraysKt.contains(r9, r2.charAt(r5)) == false) goto L6;
        if (r4 >= 0) goto L4;
    L6:
        String r6 = r2.subSequence(0, r5 + 1);
    L10:
        return r6.toString();
    L8:
        goto L10
    }

    public static final CharSequence trimStart(CharSequence r6) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        int r2 = 0;
        int r3 = r6.length();
    L3:
        if (r2 >= r3) goto L9;
        if (CharsKt.isWhitespace(r6.charAt(r2)) == false) goto L12;
        r2 = r2 + 1;
        goto L3
    L12:
        return r6.subSequence(r2, r6.length());
    L9:
        return "";
    }

    public static final CharSequence trimStart(CharSequence r4, Function1<? super Character, Boolean> r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "predicate");
        int r1 = 0;
        int r2 = r4.length();
    L3:
        if (r1 >= r2) goto L10;
        if (r5.invoke(Character.valueOf(r4.charAt(r1))).booleanValue() == false) goto L7;
        r1 = r1 + 1;
        goto L3
    L7:
        return r4.subSequence(r1, r4.length());
    L10:
        return "";
    }

    public static final CharSequence trimStart(CharSequence r6, char... r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "chars");
        int r2 = 0;
        int r3 = r6.length();
    L3:
        if (r2 >= r3) goto L9;
        if (ArraysKt.contains(r7, r6.charAt(r2)) == false) goto L12;
        r2 = r2 + 1;
        goto L3
    L12:
        return r6.subSequence(r2, r6.length());
    L9:
        return "";
    }

    private static final String trimStart(String r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return StringsKt.trimStart(r1).toString();
    }

    public static final String trimStart(String r6, Function1<? super Character, Boolean> r7) {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "predicate");
        String r1 = r6;
        int r3 = 0;
        int r4 = r1.length();
    L3:
        if (r3 >= r4) goto L8;
        if (r7.invoke(Character.valueOf(r1.charAt(r3))).booleanValue() == false) goto L6;
        r3 = r3 + 1;
        goto L3
    L6:
        String r5 = r1.subSequence(r3, r1.length());
    L10:
        return r5.toString();
    L8:
        goto L10
    }

    public static final String trimStart(String r8, char... r9) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        Intrinsics.checkNotNullParameter(r9, "chars");
        String r2 = r8;
        int r4 = 0;
        int r5 = r2.length();
    L3:
        if (r4 >= r5) goto L8;
        if (ArraysKt.contains(r9, r2.charAt(r4)) == false) goto L6;
        r4 = r4 + 1;
        goto L3
    L6:
        String r6 = r2.subSequence(r4, r2.length());
    L10:
        return r6.toString();
    L8:
        goto L10
    }
}
