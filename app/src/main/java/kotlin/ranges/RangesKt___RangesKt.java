package kotlin.ranges;

import androidx.exifinterface.media.ExifInterface;
import java.util.NoSuchElementException;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.random.RandomKt;

/* JADX INFO: compiled from: _Ranges.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000t\n\u0002\b\u0002\n\u0002\u0010\u000f\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\u0010\n\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\f\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u001d\u001a'\u0010\u0000\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\u0003\u001a\u0002H\u0001¢\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u0000\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005\u001a\u0012\u0010\u0000\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0006\u001a\u0012\u0010\u0000\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0007\u001a\u0012\u0010\u0000\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b\u001a\u0012\u0010\u0000\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t\u001a\u0012\u0010\u0000\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n\u001a'\u0010\u000b\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\u0006\u0010\f\u001a\u0002H\u0001¢\u0006\u0002\u0010\u0004\u001a\u0012\u0010\u000b\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u0012\u0010\u000b\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u0012\u0010\u000b\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u0012\u0010\u000b\u001a\u00020\b*\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0012\u0010\u000b\u001a\u00020\t*\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0012\u0010\u000b\u001a\u00020\n*\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a3\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\b\u0010\u0003\u001a\u0004\u0018\u0001H\u00012\b\u0010\f\u001a\u0004\u0018\u0001H\u0001¢\u0006\u0002\u0010\u000e\u001a/\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0010H\u0007¢\u0006\u0002\u0010\u0011\u001a-\u0010\r\u001a\u0002H\u0001\"\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u0002*\u0002H\u00012\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0012¢\u0006\u0002\u0010\u0013\u001a\u001a\u0010\r\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u0005\u001a\u001a\u0010\r\u001a\u00020\u0006*\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006\u001a\u001a\u0010\r\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u0007\u001a\u001a\u0010\r\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b\u001a\u0018\u0010\r\u001a\u00020\b*\u00020\b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u0012\u001a\u001a\u0010\r\u001a\u00020\t*\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\t\u001a\u0018\u0010\r\u001a\u00020\t*\u00020\t2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\u0012\u001a\u001a\u0010\r\u001a\u00020\n*\u00020\n2\u0006\u0010\u0003\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0087\n¢\u0006\u0002\u0010\u0019\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00070\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001d\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0\u00122\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0006H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0\u00122\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b \u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020!2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\n\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020!2\b\u0010\u0017\u001a\u0004\u0018\u00010\bH\u0087\n¢\u0006\u0002\u0010\"\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020!2\u0006\u0010\u001a\u001a\u00020\tH\u0087\n\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020!2\u0006\u0010\u001a\u001a\u00020\nH\u0087\n\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020#2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\n\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020#2\u0006\u0010\u001a\u001a\u00020\bH\u0087\n\u001a\u001c\u0010\u0014\u001a\u00020\u0015*\u00020#2\b\u0010\u0017\u001a\u0004\u0018\u00010\tH\u0087\n¢\u0006\u0002\u0010$\u001a\u0015\u0010\u0014\u001a\u00020\u0015*\u00020#2\u0006\u0010\u001a\u001a\u00020\nH\u0087\n\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050%2\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050%2\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00050%2\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001b\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\u00060%2\u0006\u0010\u001a\u001a\u00020\u0007H\u0087\u0002¢\u0006\u0002\b\u001c\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0%2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0%2\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\b0%2\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001e\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0%2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0%2\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\t0%2\u0006\u0010\u001a\u001a\u00020\nH\u0087\u0002¢\u0006\u0002\b\u001f\u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0%2\u0006\u0010\u001a\u001a\u00020\u0005H\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0%2\u0006\u0010\u001a\u001a\u00020\bH\u0087\u0002¢\u0006\u0002\b \u001a \u0010\u0014\u001a\u00020\u0015*\b\u0012\u0004\u0012\u00020\n0%2\u0006\u0010\u001a\u001a\u00020\tH\u0087\u0002¢\u0006\u0002\b \u001a\u0015\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\u00052\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\u00052\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020**\u00020\u00182\u0006\u0010(\u001a\u00020\u0018H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\b2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\b2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\b2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\b2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\t2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\n2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\n2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020)*\u00020\n2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010&\u001a\u00020'*\u00020\n2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\f\u0010+\u001a\u00020\u0018*\u00020*H\u0007\u001a\f\u0010+\u001a\u00020\b*\u00020'H\u0007\u001a\f\u0010+\u001a\u00020\t*\u00020)H\u0007\u001a\u0013\u0010,\u001a\u0004\u0018\u00010\u0018*\u00020*H\u0007¢\u0006\u0002\u0010-\u001a\u0013\u0010,\u001a\u0004\u0018\u00010\b*\u00020'H\u0007¢\u0006\u0002\u0010.\u001a\u0013\u0010,\u001a\u0004\u0018\u00010\t*\u00020)H\u0007¢\u0006\u0002\u0010/\u001a\f\u00100\u001a\u00020\u0018*\u00020*H\u0007\u001a\f\u00100\u001a\u00020\b*\u00020'H\u0007\u001a\f\u00100\u001a\u00020\t*\u00020)H\u0007\u001a\u0013\u00101\u001a\u0004\u0018\u00010\u0018*\u00020*H\u0007¢\u0006\u0002\u0010-\u001a\u0013\u00101\u001a\u0004\u0018\u00010\b*\u00020'H\u0007¢\u0006\u0002\u0010.\u001a\u0013\u00101\u001a\u0004\u0018\u00010\t*\u00020)H\u0007¢\u0006\u0002\u0010/\u001a\r\u00102\u001a\u00020\u0018*\u00020\u0016H\u0087\b\u001a\u0014\u00102\u001a\u00020\u0018*\u00020\u00162\u0006\u00102\u001a\u000203H\u0007\u001a\r\u00102\u001a\u00020\b*\u00020!H\u0087\b\u001a\u0014\u00102\u001a\u00020\b*\u00020!2\u0006\u00102\u001a\u000203H\u0007\u001a\r\u00102\u001a\u00020\t*\u00020#H\u0087\b\u001a\u0014\u00102\u001a\u00020\t*\u00020#2\u0006\u00102\u001a\u000203H\u0007\u001a\u0014\u00104\u001a\u0004\u0018\u00010\u0018*\u00020\u0016H\u0087\b¢\u0006\u0002\u00105\u001a\u001b\u00104\u001a\u0004\u0018\u00010\u0018*\u00020\u00162\u0006\u00102\u001a\u000203H\u0007¢\u0006\u0002\u00106\u001a\u0014\u00104\u001a\u0004\u0018\u00010\b*\u00020!H\u0087\b¢\u0006\u0002\u00107\u001a\u001b\u00104\u001a\u0004\u0018\u00010\b*\u00020!2\u0006\u00102\u001a\u000203H\u0007¢\u0006\u0002\u00108\u001a\u0014\u00104\u001a\u0004\u0018\u00010\t*\u00020#H\u0087\b¢\u0006\u0002\u00109\u001a\u001b\u00104\u001a\u0004\u0018\u00010\t*\u00020#2\u0006\u00102\u001a\u000203H\u0007¢\u0006\u0002\u0010:\u001a\n\u0010;\u001a\u00020**\u00020*\u001a\n\u0010;\u001a\u00020'*\u00020'\u001a\n\u0010;\u001a\u00020)*\u00020)\u001a\u0015\u0010<\u001a\u00020**\u00020*2\u0006\u0010<\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010<\u001a\u00020'*\u00020'2\u0006\u0010<\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010<\u001a\u00020)*\u00020)2\u0006\u0010<\u001a\u00020\tH\u0086\u0004\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\u0006H\u0000¢\u0006\u0002\u0010>\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\u0007H\u0000¢\u0006\u0002\u0010?\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\bH\u0000¢\u0006\u0002\u0010@\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\tH\u0000¢\u0006\u0002\u0010A\u001a\u0013\u0010=\u001a\u0004\u0018\u00010\u0005*\u00020\nH\u0000¢\u0006\u0002\u0010B\u001a\u0013\u0010C\u001a\u0004\u0018\u00010\b*\u00020\u0006H\u0000¢\u0006\u0002\u0010D\u001a\u0013\u0010C\u001a\u0004\u0018\u00010\b*\u00020\u0007H\u0000¢\u0006\u0002\u0010E\u001a\u0013\u0010C\u001a\u0004\u0018\u00010\b*\u00020\tH\u0000¢\u0006\u0002\u0010F\u001a\u0013\u0010G\u001a\u0004\u0018\u00010\t*\u00020\u0006H\u0000¢\u0006\u0002\u0010H\u001a\u0013\u0010G\u001a\u0004\u0018\u00010\t*\u00020\u0007H\u0000¢\u0006\u0002\u0010I\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\u0006H\u0000¢\u0006\u0002\u0010K\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\u0007H\u0000¢\u0006\u0002\u0010L\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\bH\u0000¢\u0006\u0002\u0010M\u001a\u0013\u0010J\u001a\u0004\u0018\u00010\n*\u00020\tH\u0000¢\u0006\u0002\u0010N\u001a\u0015\u0010O\u001a\u00020!*\u00020\u00052\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\u00052\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\u00052\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\u00052\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020\u0016*\u00020\u00182\u0006\u0010(\u001a\u00020\u0018H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\b2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\b2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\b2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\b2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\t2\u0006\u0010(\u001a\u00020\nH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\n2\u0006\u0010(\u001a\u00020\u0005H\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\n2\u0006\u0010(\u001a\u00020\bH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020#*\u00020\n2\u0006\u0010(\u001a\u00020\tH\u0086\u0004\u001a\u0015\u0010O\u001a\u00020!*\u00020\n2\u0006\u0010(\u001a\u00020\nH\u0086\u0004¨\u0006P"}, m421d2 = {"coerceAtLeast", ExifInterface.GPS_DIRECTION_TRUE, "", "minimumValue", "(Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "", "", "", "", "", "", "coerceAtMost", "maximumValue", "coerceIn", "(Ljava/lang/Comparable;Ljava/lang/Comparable;Ljava/lang/Comparable;)Ljava/lang/Comparable;", "range", "Lkotlin/ranges/ClosedFloatingPointRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedFloatingPointRange;)Ljava/lang/Comparable;", "Lkotlin/ranges/ClosedRange;", "(Ljava/lang/Comparable;Lkotlin/ranges/ClosedRange;)Ljava/lang/Comparable;", "contains", "", "Lkotlin/ranges/CharRange;", "element", "", "(Lkotlin/ranges/CharRange;Ljava/lang/Character;)Z", "value", "byteRangeContains", "doubleRangeContains", "floatRangeContains", "intRangeContains", "longRangeContains", "shortRangeContains", "Lkotlin/ranges/IntRange;", "(Lkotlin/ranges/IntRange;Ljava/lang/Integer;)Z", "Lkotlin/ranges/LongRange;", "(Lkotlin/ranges/LongRange;Ljava/lang/Long;)Z", "Lkotlin/ranges/OpenEndRange;", "downTo", "Lkotlin/ranges/IntProgression;", "to", "Lkotlin/ranges/LongProgression;", "Lkotlin/ranges/CharProgression;", "first", "firstOrNull", "(Lkotlin/ranges/CharProgression;)Ljava/lang/Character;", "(Lkotlin/ranges/IntProgression;)Ljava/lang/Integer;", "(Lkotlin/ranges/LongProgression;)Ljava/lang/Long;", "last", "lastOrNull", "random", "Lkotlin/random/Random;", "randomOrNull", "(Lkotlin/ranges/CharRange;)Ljava/lang/Character;", "(Lkotlin/ranges/CharRange;Lkotlin/random/Random;)Ljava/lang/Character;", "(Lkotlin/ranges/IntRange;)Ljava/lang/Integer;", "(Lkotlin/ranges/IntRange;Lkotlin/random/Random;)Ljava/lang/Integer;", "(Lkotlin/ranges/LongRange;)Ljava/lang/Long;", "(Lkotlin/ranges/LongRange;Lkotlin/random/Random;)Ljava/lang/Long;", "reversed", "step", "toByteExactOrNull", "(D)Ljava/lang/Byte;", "(F)Ljava/lang/Byte;", "(I)Ljava/lang/Byte;", "(J)Ljava/lang/Byte;", "(S)Ljava/lang/Byte;", "toIntExactOrNull", "(D)Ljava/lang/Integer;", "(F)Ljava/lang/Integer;", "(J)Ljava/lang/Integer;", "toLongExactOrNull", "(D)Ljava/lang/Long;", "(F)Ljava/lang/Long;", "toShortExactOrNull", "(D)Ljava/lang/Short;", "(F)Ljava/lang/Short;", "(I)Ljava/lang/Short;", "(J)Ljava/lang/Short;", "until", "kotlin-stdlib"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/ranges/RangesKt")
class RangesKt___RangesKt extends RangesKt__RangesKt {
    public RangesKt___RangesKt() {
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean byteRangeContains(ClosedRange r3, double r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Byte r0 = RangesKt.toByteExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean byteRangeContains(ClosedRange r3, float r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Byte r0 = RangesKt.toByteExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final boolean byteRangeContains(ClosedRange<Byte> r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Byte r0 = RangesKt.toByteExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final boolean byteRangeContains(ClosedRange<Byte> r3, long r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Byte r0 = RangesKt.toByteExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final boolean byteRangeContains(ClosedRange<Byte> r3, short r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Byte r0 = RangesKt.toByteExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final boolean byteRangeContains(OpenEndRange<Byte> r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Byte r0 = RangesKt.toByteExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final boolean byteRangeContains(OpenEndRange<Byte> r3, long r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Byte r0 = RangesKt.toByteExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final boolean byteRangeContains(OpenEndRange<Byte> r3, short r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Byte r0 = RangesKt.toByteExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final byte coerceAtLeast(byte r1, byte r2) {
        if (r1 >= r2) goto L5;
        return r2;
    L5:
        return r1;
    }

    public static final double coerceAtLeast(double r2, double r4) {
        if (r2 >= r4) goto L6;
        return r4;
    L6:
        return r2;
    }

    public static final float coerceAtLeast(float r1, float r2) {
        if (r1 >= r2) goto L6;
        return r2;
    L6:
        return r1;
    }

    public static final int coerceAtLeast(int r1, int r2) {
        if (r1 >= r2) goto L5;
        return r2;
    L5:
        return r1;
    }

    public static final long coerceAtLeast(long r2, long r4) {
        if (r2 >= r4) goto L6;
        return r4;
    L6:
        return r2;
    }

    public static final <T extends Comparable<? super T>> T coerceAtLeast(T r1, T r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "minimumValue");
        if (r1.compareTo(r2) >= 0) goto L6;
        return r2;
    L6:
        return r1;
    }

    public static final short coerceAtLeast(short r1, short r2) {
        if (r1 >= r2) goto L5;
        return r2;
    L5:
        return r1;
    }

    public static final byte coerceAtMost(byte r1, byte r2) {
        if (r1 <= r2) goto L5;
        return r2;
    L5:
        return r1;
    }

    public static final double coerceAtMost(double r2, double r4) {
        if (r2 <= r4) goto L6;
        return r4;
    L6:
        return r2;
    }

    public static final float coerceAtMost(float r1, float r2) {
        if (r1 <= r2) goto L6;
        return r2;
    L6:
        return r1;
    }

    public static final int coerceAtMost(int r1, int r2) {
        if (r1 <= r2) goto L5;
        return r2;
    L5:
        return r1;
    }

    public static final long coerceAtMost(long r2, long r4) {
        if (r2 <= r4) goto L6;
        return r4;
    L6:
        return r2;
    }

    public static final <T extends Comparable<? super T>> T coerceAtMost(T r1, T r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "maximumValue");
        if (r1.compareTo(r2) <= 0) goto L6;
        return r2;
    L6:
        return r1;
    }

    public static final short coerceAtMost(short r1, short r2) {
        if (r1 <= r2) goto L5;
        return r2;
    L5:
        return r1;
    }

    public static final byte coerceIn(byte r3, byte r4, byte r5) {
        if (r4 > r5) goto L9;
        if (r3 >= r4) goto L5;
        return r4;
    L5:
        if (r3 <= r5) goto L7;
        return r5;
    L7:
        return r3;
    L9:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + r5 + " is less than minimum " + r4 + '.');
    }

    public static final double coerceIn(double r3, double r5, double r7) {
        if (r5 > r7) goto L12;
        if (r3 >= r5) goto L8;
        return r5;
    L8:
        if (r3 <= r7) goto L10;
        return r7;
    L10:
        return r3;
    L12:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + r7 + " is less than minimum " + r5 + '.');
    }

    public static final float coerceIn(float r3, float r4, float r5) {
        if (r4 > r5) goto L12;
        if (r3 >= r4) goto L8;
        return r4;
    L8:
        if (r3 <= r5) goto L10;
        return r5;
    L10:
        return r3;
    L12:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + r5 + " is less than minimum " + r4 + '.');
    }

    public static final int coerceIn(int r3, int r4, int r5) {
        if (r4 > r5) goto L9;
        if (r3 >= r4) goto L5;
        return r4;
    L5:
        if (r3 <= r5) goto L7;
        return r5;
    L7:
        return r3;
    L9:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + r5 + " is less than minimum " + r4 + '.');
    }

    public static final int coerceIn(int r3, ClosedRange<Integer> r4) {
        Intrinsics.checkNotNullParameter(r4, "range");
        if ((r4 instanceof ClosedFloatingPointRange) == false) goto L7;
        return ((Number) RangesKt.coerceIn(Integer.valueOf(r3), (ClosedFloatingPointRange) r4)).intValue();
    L7:
        if (r4.isEmpty() == true) goto L17;
        if (r3 >= ((Number) r4.getStart()).intValue()) goto L12;
        return ((Number) r4.getStart()).intValue();
    L12:
        if (r3 <= ((Number) r4.getEndInclusive()).intValue()) goto L15;
        return ((Number) r4.getEndInclusive()).intValue();
    L15:
        return r3;
    L17:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: " + r4 + '.');
    }

    public static final long coerceIn(long r3, long r5, long r7) {
        if (r5 > r7) goto L12;
        if (r3 >= r5) goto L8;
        return r5;
    L8:
        if (r3 <= r7) goto L10;
        return r7;
    L10:
        return r3;
    L12:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + r7 + " is less than minimum " + r5 + '.');
    }

    public static final long coerceIn(long r3, ClosedRange<Long> r5) {
        Intrinsics.checkNotNullParameter(r5, "range");
        if ((r5 instanceof ClosedFloatingPointRange) == false) goto L7;
        return ((Number) RangesKt.coerceIn(Long.valueOf(r3), (ClosedFloatingPointRange) r5)).longValue();
    L7:
        if (r5.isEmpty() == true) goto L17;
        if (r3 >= ((Number) r5.getStart()).longValue()) goto L12;
        return ((Number) r5.getStart()).longValue();
    L12:
        if (r3 <= ((Number) r5.getEndInclusive()).longValue()) goto L15;
        return ((Number) r5.getEndInclusive()).longValue();
    L15:
        return r3;
    L17:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: " + r5 + '.');
    }

    public static final <T extends Comparable<? super T>> T coerceIn(T r3, T r4, T r5) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r4 == null) goto L15;
        if (r5 == null) goto L15;
        if (r4.compareTo(r5) > 0) goto L14;
        if (r3.compareTo(r4) >= 0) goto L11;
        return r4;
    L11:
        if (r3.compareTo(r5) <= 0) goto L23;
        return r5;
    L23:
        return r3;
    L14:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + r5 + " is less than minimum " + r4 + '.');
    L15:
        if (r4 != null) goto L17;
    L19:
        if (r5 == null) goto L23;
        if (r3.compareTo(r5) <= 0) goto L23;
        return r5;
    L17:
        if (r3.compareTo(r4) >= 0) goto L19;
        return r4;
    }

    public static final <T extends Comparable<? super T>> T coerceIn(T r3, ClosedFloatingPointRange<T> r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "range");
        if (r4.isEmpty() == true) goto L17;
        if (r4.lessThanOrEquals(r3, r4.getStart()) == false) goto L10;
        if (r4.lessThanOrEquals(r4.getStart(), r3) == true) goto L10;
        return r4.getStart();
    L10:
        if (r4.lessThanOrEquals(r4.getEndInclusive(), r3) == false) goto L15;
        if (r4.lessThanOrEquals(r3, r4.getEndInclusive()) == true) goto L15;
        return r4.getEndInclusive();
    L15:
        return r3;
    L17:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: " + r4 + '.');
    }

    public static final <T extends Comparable<? super T>> T coerceIn(T r3, ClosedRange<T> r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "range");
        if ((r4 instanceof ClosedFloatingPointRange) == false) goto L7;
        return (T) RangesKt.coerceIn(r3, (ClosedFloatingPointRange) r4);
    L7:
        if (r4.isEmpty() == true) goto L17;
        if (r3.compareTo(r4.getStart()) >= 0) goto L12;
        return (T) r4.getStart();
    L12:
        if (r3.compareTo(r4.getEndInclusive()) <= 0) goto L15;
        return (T) r4.getEndInclusive();
    L15:
        return r3;
    L17:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: " + r4 + '.');
    }

    public static final short coerceIn(short r3, short r4, short r5) {
        if (r4 > r5) goto L9;
        if (r3 >= r4) goto L5;
        return r4;
    L5:
        if (r3 <= r5) goto L7;
        return r5;
    L7:
        return r3;
    L9:
        throw new IllegalArgumentException("Cannot coerce value to an empty range: maximum " + r5 + " is less than minimum " + r4 + '.');
    }

    private static final boolean contains(CharRange r1, Character r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if (r2 != null) goto L5;
    L7:
        return false;
    L5:
        if (r1.contains(r2.charValue()) == false) goto L7;
        return true;
    }

    private static final boolean contains(IntRange r1, byte r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return RangesKt.intRangeContains(r1, r2);
    }

    private static final boolean contains(IntRange r1, long r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return RangesKt.intRangeContains(r1, r2);
    }

    private static final boolean contains(IntRange r1, Integer r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if (r2 != null) goto L5;
    L7:
        return false;
    L5:
        if (r1.contains(r2.intValue()) == false) goto L7;
        return true;
    }

    private static final boolean contains(IntRange r1, short r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return RangesKt.intRangeContains(r1, r2);
    }

    private static final boolean contains(LongRange r1, byte r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return RangesKt.longRangeContains(r1, r2);
    }

    private static final boolean contains(LongRange r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return RangesKt.longRangeContains(r1, r2);
    }

    private static final boolean contains(LongRange r2, Long r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r3 != null) goto L5;
    L7:
        return false;
    L5:
        if (r2.contains(r3.longValue()) == false) goto L7;
        return true;
    }

    private static final boolean contains(LongRange r1, short r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return RangesKt.longRangeContains(r1, r2);
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean doubleRangeContains(ClosedRange r2, byte r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.contains(Double.valueOf(r3));
    }

    public static final boolean doubleRangeContains(ClosedRange<Double> r2, float r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.contains(Double.valueOf(r3));
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean doubleRangeContains(ClosedRange r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.contains(Double.valueOf(r3));
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean doubleRangeContains(ClosedRange r2, long r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.contains(Double.valueOf(r3));
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean doubleRangeContains(ClosedRange r2, short r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.contains(Double.valueOf(r3));
    }

    public static final boolean doubleRangeContains(OpenEndRange<Double> r2, float r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.contains(Double.valueOf(r3));
    }

    public static final CharProgression downTo(char r2, char r3) {
        return CharProgression.Companion.fromClosedRange(r2, r3, -1);
    }

    public static final IntProgression downTo(byte r2, byte r3) {
        return IntProgression.Companion.fromClosedRange(r2, r3, -1);
    }

    public static final IntProgression downTo(byte r2, int r3) {
        return IntProgression.Companion.fromClosedRange(r2, r3, -1);
    }

    public static final IntProgression downTo(byte r2, short r3) {
        return IntProgression.Companion.fromClosedRange(r2, r3, -1);
    }

    public static final IntProgression downTo(int r2, byte r3) {
        return IntProgression.Companion.fromClosedRange(r2, r3, -1);
    }

    public static final IntProgression downTo(int r2, int r3) {
        return IntProgression.Companion.fromClosedRange(r2, r3, -1);
    }

    public static final IntProgression downTo(int r2, short r3) {
        return IntProgression.Companion.fromClosedRange(r2, r3, -1);
    }

    public static final IntProgression downTo(short r2, byte r3) {
        return IntProgression.Companion.fromClosedRange(r2, r3, -1);
    }

    public static final IntProgression downTo(short r2, int r3) {
        return IntProgression.Companion.fromClosedRange(r2, r3, -1);
    }

    public static final IntProgression downTo(short r2, short r3) {
        return IntProgression.Companion.fromClosedRange(r2, r3, -1);
    }

    public static final LongProgression downTo(byte r7, long r8) {
        return LongProgression.Companion.fromClosedRange(r7, r8, -1);
    }

    public static final LongProgression downTo(int r7, long r8) {
        return LongProgression.Companion.fromClosedRange(r7, r8, -1);
    }

    public static final LongProgression downTo(long r7, byte r9) {
        return LongProgression.Companion.fromClosedRange(r7, r9, -1);
    }

    public static final LongProgression downTo(long r7, int r9) {
        return LongProgression.Companion.fromClosedRange(r7, r9, -1);
    }

    public static final LongProgression downTo(long r7, long r9) {
        return LongProgression.Companion.fromClosedRange(r7, r9, -1);
    }

    public static final LongProgression downTo(long r7, short r9) {
        return LongProgression.Companion.fromClosedRange(r7, r9, -1);
    }

    public static final LongProgression downTo(short r7, long r8) {
        return LongProgression.Companion.fromClosedRange(r7, r8, -1);
    }

    public static final char first(CharProgression r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r3.isEmpty() == true) goto L7;
        return r3.getFirst();
    L7:
        throw new NoSuchElementException("Progression " + r3 + " is empty.");
    }

    public static final int first(IntProgression r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r3.isEmpty() == true) goto L7;
        return r3.getFirst();
    L7:
        throw new NoSuchElementException("Progression " + r3 + " is empty.");
    }

    public static final long first(LongProgression r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r3.isEmpty() == true) goto L7;
        return r3.getFirst();
    L7:
        throw new NoSuchElementException("Progression " + r3 + " is empty.");
    }

    public static final Character firstOrNull(CharProgression r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if (r1.isEmpty() == false) goto L6;
        return null;
    L6:
        return Character.valueOf(r1.getFirst());
    }

    public static final Integer firstOrNull(IntProgression r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if (r1.isEmpty() == false) goto L6;
        return null;
    L6:
        return Integer.valueOf(r1.getFirst());
    }

    public static final Long firstOrNull(LongProgression r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.isEmpty() == false) goto L6;
        return null;
    L6:
        return Long.valueOf(r2.getFirst());
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean floatRangeContains(ClosedRange r1, byte r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.contains(Float.valueOf(r2));
    }

    public static final boolean floatRangeContains(ClosedRange<Float> r1, double r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.contains(Float.valueOf((float) r2));
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean floatRangeContains(ClosedRange r1, int r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.contains(Float.valueOf(r2));
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean floatRangeContains(ClosedRange r1, long r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.contains(Float.valueOf(r2));
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean floatRangeContains(ClosedRange r1, short r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.contains(Float.valueOf(r2));
    }

    public static final boolean intRangeContains(ClosedRange<Integer> r1, byte r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.contains(Integer.valueOf(r2));
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean intRangeContains(ClosedRange r3, double r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Integer r0 = RangesKt.toIntExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean intRangeContains(ClosedRange r3, float r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Integer r0 = RangesKt.toIntExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final boolean intRangeContains(ClosedRange<Integer> r3, long r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Integer r0 = RangesKt.toIntExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final boolean intRangeContains(ClosedRange<Integer> r1, short r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.contains(Integer.valueOf(r2));
    }

    public static final boolean intRangeContains(OpenEndRange<Integer> r1, byte r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.contains(Integer.valueOf(r2));
    }

    public static final boolean intRangeContains(OpenEndRange<Integer> r3, long r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Integer r0 = RangesKt.toIntExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final boolean intRangeContains(OpenEndRange<Integer> r1, short r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.contains(Integer.valueOf(r2));
    }

    public static final char last(CharProgression r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r3.isEmpty() == true) goto L7;
        return r3.getLast();
    L7:
        throw new NoSuchElementException("Progression " + r3 + " is empty.");
    }

    public static final int last(IntProgression r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r3.isEmpty() == true) goto L7;
        return r3.getLast();
    L7:
        throw new NoSuchElementException("Progression " + r3 + " is empty.");
    }

    public static final long last(LongProgression r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        if (r3.isEmpty() == true) goto L7;
        return r3.getLast();
    L7:
        throw new NoSuchElementException("Progression " + r3 + " is empty.");
    }

    public static final Character lastOrNull(CharProgression r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if (r1.isEmpty() == false) goto L6;
        return null;
    L6:
        return Character.valueOf(r1.getLast());
    }

    public static final Integer lastOrNull(IntProgression r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        if (r1.isEmpty() == false) goto L6;
        return null;
    L6:
        return Integer.valueOf(r1.getLast());
    }

    public static final Long lastOrNull(LongProgression r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        if (r2.isEmpty() == false) goto L6;
        return null;
    L6:
        return Long.valueOf(r2.getLast());
    }

    public static final boolean longRangeContains(ClosedRange<Long> r2, byte r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.contains(Long.valueOf(r3));
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean longRangeContains(ClosedRange r3, double r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Long r0 = RangesKt.toLongExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean longRangeContains(ClosedRange r3, float r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Long r0 = RangesKt.toLongExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final boolean longRangeContains(ClosedRange<Long> r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.contains(Long.valueOf(r3));
    }

    public static final boolean longRangeContains(ClosedRange<Long> r2, short r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.contains(Long.valueOf(r3));
    }

    public static final boolean longRangeContains(OpenEndRange<Long> r2, byte r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.contains(Long.valueOf(r3));
    }

    public static final boolean longRangeContains(OpenEndRange<Long> r2, int r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.contains(Long.valueOf(r3));
    }

    public static final boolean longRangeContains(OpenEndRange<Long> r2, short r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.contains(Long.valueOf(r3));
    }

    private static final char random(CharRange r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return RangesKt.random(r1, Random.Default);
    }

    public static final char random(CharRange r3, Random r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "random");
        return (char) r4.nextInt(r3.getFirst(), r3.getLast() + 1);
    L6:
        e = move-exception;
        throw new NoSuchElementException(e.getMessage());
    }

    private static final int random(IntRange r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return RangesKt.random(r1, Random.Default);
    }

    public static final int random(IntRange r3, Random r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "random");
        return RandomKt.nextInt(r4, r3);
    L5:
        e = move-exception;
        throw new NoSuchElementException(e.getMessage());
    }

    private static final long random(LongRange r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return RangesKt.random(r2, Random.Default);
    }

    public static final long random(LongRange r3, Random r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "random");
        return RandomKt.nextLong(r4, r3);
    L5:
        e = move-exception;
        throw new NoSuchElementException(e.getMessage());
    }

    private static final Character randomOrNull(CharRange r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return RangesKt.randomOrNull(r1, Random.Default);
    }

    public static final Character randomOrNull(CharRange r2, Random r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "random");
        if (r2.isEmpty() == false) goto L7;
        return null;
    L7:
        return Character.valueOf((char) r3.nextInt(r2.getFirst(), r2.getLast() + 1));
    }

    private static final Integer randomOrNull(IntRange r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return RangesKt.randomOrNull(r1, Random.Default);
    }

    public static final Integer randomOrNull(IntRange r1, Random r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "random");
        if (r1.isEmpty() == false) goto L7;
        return null;
    L7:
        return Integer.valueOf(RandomKt.nextInt(r2, r1));
    }

    private static final Long randomOrNull(LongRange r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return RangesKt.randomOrNull(r1, Random.Default);
    }

    public static final Long randomOrNull(LongRange r2, Random r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "random");
        if (r2.isEmpty() == false) goto L7;
        return null;
    L7:
        return Long.valueOf(RandomKt.nextLong(r3, r2));
    }

    public static final CharProgression reversed(CharProgression r4) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        return CharProgression.Companion.fromClosedRange(r4.getLast(), r4.getFirst(), -r4.getStep());
    }

    public static final IntProgression reversed(IntProgression r4) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        return IntProgression.Companion.fromClosedRange(r4.getLast(), r4.getFirst(), -r4.getStep());
    }

    public static final LongProgression reversed(LongProgression r8) {
        Intrinsics.checkNotNullParameter(r8, "<this>");
        return LongProgression.Companion.fromClosedRange(r8.getLast(), r8.getFirst(), -r8.getStep());
    }

    public static final boolean shortRangeContains(ClosedRange<Short> r1, byte r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.contains(Short.valueOf(r2));
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean shortRangeContains(ClosedRange r3, double r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Short r0 = RangesKt.toShortExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    @Deprecated(message = "This `contains` operation mixing integer and floating point arguments has ambiguous semantics and is going to be removed.")
    @DeprecatedSinceKotlin(errorSince = "1.4", hiddenSince = "1.5", warningSince = "1.3")
    public static final /* synthetic */ boolean shortRangeContains(ClosedRange r3, float r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Short r0 = RangesKt.toShortExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final boolean shortRangeContains(ClosedRange<Short> r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Short r0 = RangesKt.toShortExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final boolean shortRangeContains(ClosedRange<Short> r3, long r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Short r0 = RangesKt.toShortExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final boolean shortRangeContains(OpenEndRange<Short> r1, byte r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.contains(Short.valueOf(r2));
    }

    public static final boolean shortRangeContains(OpenEndRange<Short> r3, int r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Short r0 = RangesKt.toShortExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final boolean shortRangeContains(OpenEndRange<Short> r3, long r4) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Short r0 = RangesKt.toShortExactOrNull(r4);
        if (r0 != null) goto L7;
        return false;
    L7:
        return r3.contains(r0);
    }

    public static final CharProgression step(CharProgression r4, int r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        if (r5 <= 0) goto L5;
        boolean r0 = true;
    L6:
        RangesKt.checkStepIsPositive(r0, Integer.valueOf(r5));
        CharProgression.Companion r1 = CharProgression.Companion;
        char r2 = r4.getFirst();
        char r3 = r4.getLast();
        if (r4.getStep() <= 0) goto L9;
        int r6 = r5;
    L11:
        return r1.fromClosedRange(r2, r3, r6);
    L9:
        r6 = -r5;
        goto L11
    L5:
        r0 = false;
        goto L6
    }

    public static final IntProgression step(IntProgression r4, int r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        if (r5 <= 0) goto L5;
        boolean r0 = true;
    L6:
        RangesKt.checkStepIsPositive(r0, Integer.valueOf(r5));
        IntProgression.Companion r1 = IntProgression.Companion;
        int r2 = r4.getFirst();
        int r3 = r4.getLast();
        if (r4.getStep() <= 0) goto L9;
        int r6 = r5;
    L11:
        return r1.fromClosedRange(r2, r3, r6);
    L9:
        r6 = -r5;
        goto L11
    L5:
        r0 = false;
        goto L6
    }

    public static final LongProgression step(LongProgression r11, long r12) {
        Intrinsics.checkNotNullParameter(r11, "<this>");
        if (r12 <= 0) goto L5;
        boolean r2 = true;
    L6:
        RangesKt.checkStepIsPositive(r2, Long.valueOf(r12));
        LongProgression.Companion r4 = LongProgression.Companion;
        long r5 = r11.getFirst();
        long r7 = r11.getLast();
        if (r11.getStep() <= 0) goto L9;
        long r9 = r12;
    L11:
        return r4.fromClosedRange(r5, r7, r9);
    L9:
        r9 = -r12;
        goto L11
    L5:
        r2 = false;
        goto L6
    }

    public static final Byte toByteExactOrNull(double r4) {
        boolean r1 = false;
        if ((-128.0d) <= r4) goto L5;
    L7:
        if (r1 == true) goto L11;
        return null;
    L11:
        return Byte.valueOf((byte) r4);
    L5:
        if (r4 > 127.0d) goto L7;
        r1 = true;
        goto L7
    }

    public static final Byte toByteExactOrNull(float r2) {
        boolean r1 = false;
        if ((-128.0f) <= r2) goto L5;
    L7:
        if (r1 == true) goto L11;
        return null;
    L11:
        return Byte.valueOf((byte) r2);
    L5:
        if (r2 > 127.0f) goto L7;
        r1 = true;
        goto L7
    }

    public static final Byte toByteExactOrNull(int r3) {
        if (new IntRange(-128, 127).contains(r3) == true) goto L7;
        return null;
    L7:
        return Byte.valueOf((byte) r3);
    }

    public static final Byte toByteExactOrNull(long r5) {
        if (new LongRange(-128, 127).contains(r5) == true) goto L7;
        return null;
    L7:
        return Byte.valueOf((byte) r5);
    }

    public static final Byte toByteExactOrNull(short r3) {
        if (RangesKt.intRangeContains(new IntRange(-128, 127), r3) == true) goto L7;
        return null;
    L7:
        return Byte.valueOf((byte) r3);
    }

    public static final Integer toIntExactOrNull(double r4) {
        boolean r1 = false;
        if ((-2.147483648E9d) <= r4) goto L5;
    L7:
        if (r1 == true) goto L11;
        return null;
    L11:
        return Integer.valueOf((int) r4);
    L5:
        if (r4 > 2.147483647E9d) goto L7;
        r1 = true;
        goto L7
    }

    public static final Integer toIntExactOrNull(float r2) {
        boolean r1 = false;
        if ((-2.14748365E9f) <= r2) goto L5;
    L7:
        if (r1 == true) goto L11;
        return null;
    L11:
        return Integer.valueOf((int) r2);
    L5:
        if (r2 > 2.14748365E9f) goto L7;
        r1 = true;
        goto L7
    }

    public static final Integer toIntExactOrNull(long r5) {
        if (new LongRange(-2147483648L, 2147483647L).contains(r5) == true) goto L7;
        return null;
    L7:
        return Integer.valueOf((int) r5);
    }

    public static final Long toLongExactOrNull(double r4) {
        boolean r1 = false;
        if ((-9.223372036854776E18d) <= r4) goto L5;
    L7:
        if (r1 == true) goto L11;
        return null;
    L11:
        return Long.valueOf((long) r4);
    L5:
        if (r4 > 9.223372036854776E18d) goto L7;
        r1 = true;
        goto L7
    }

    public static final Long toLongExactOrNull(float r2) {
        boolean r1 = false;
        if ((-9.223372E18f) <= r2) goto L5;
    L7:
        if (r1 == true) goto L11;
        return null;
    L11:
        return Long.valueOf((long) r2);
    L5:
        if (r2 > 9.223372E18f) goto L7;
        r1 = true;
        goto L7
    }

    public static final Short toShortExactOrNull(double r4) {
        boolean r1 = false;
        if ((-32768.0d) <= r4) goto L5;
    L7:
        if (r1 == true) goto L11;
        return null;
    L11:
        return Short.valueOf((short) r4);
    L5:
        if (r4 > 32767.0d) goto L7;
        r1 = true;
        goto L7
    }

    public static final Short toShortExactOrNull(float r2) {
        boolean r1 = false;
        if ((-32768.0f) <= r2) goto L5;
    L7:
        if (r1 == true) goto L11;
        return null;
    L11:
        return Short.valueOf((short) r2);
    L5:
        if (r2 > 32767.0f) goto L7;
        r1 = true;
        goto L7
    }

    public static final Short toShortExactOrNull(int r3) {
        if (new IntRange(-32768, 32767).contains(r3) == true) goto L7;
        return null;
    L7:
        return Short.valueOf((short) r3);
    }

    public static final Short toShortExactOrNull(long r5) {
        if (new LongRange(-32768, 32767).contains(r5) == true) goto L7;
        return null;
    L7:
        return Short.valueOf((short) r5);
    }

    public static final CharRange until(char r2, char r3) {
        if (Intrinsics.compare(r3, 0) > 0) goto L7;
        return CharRange.Companion.getEMPTY();
    L7:
        return new CharRange(r2, (char) (r3 - 1));
    }

    public static final IntRange until(byte r2, byte r3) {
        return new IntRange(r2, r3 - 1);
    }

    public static final IntRange until(byte r2, int r3) {
        if (r3 > Integer.MIN_VALUE) goto L7;
        return IntRange.Companion.getEMPTY();
    L7:
        return new IntRange(r2, r3 - 1);
    }

    public static final IntRange until(byte r2, short r3) {
        return new IntRange(r2, r3 - 1);
    }

    public static final IntRange until(int r2, byte r3) {
        return new IntRange(r2, r3 - 1);
    }

    public static final IntRange until(int r2, int r3) {
        if (r3 > Integer.MIN_VALUE) goto L7;
        return IntRange.Companion.getEMPTY();
    L7:
        return new IntRange(r2, r3 - 1);
    }

    public static final IntRange until(int r2, short r3) {
        return new IntRange(r2, r3 - 1);
    }

    public static final IntRange until(short r2, byte r3) {
        return new IntRange(r2, r3 - 1);
    }

    public static final IntRange until(short r2, int r3) {
        if (r3 > Integer.MIN_VALUE) goto L7;
        return IntRange.Companion.getEMPTY();
    L7:
        return new IntRange(r2, r3 - 1);
    }

    public static final IntRange until(short r2, short r3) {
        return new IntRange(r2, r3 - 1);
    }

    public static final LongRange until(byte r5, long r6) {
        if (r6 > Long.MIN_VALUE) goto L7;
        return LongRange.Companion.getEMPTY();
    L7:
        return new LongRange(r5, r6 - 1);
    }

    public static final LongRange until(int r5, long r6) {
        if (r6 > Long.MIN_VALUE) goto L7;
        return LongRange.Companion.getEMPTY();
    L7:
        return new LongRange(r5, r6 - 1);
    }

    public static final LongRange until(long r5, byte r7) {
        return new LongRange(r5, ((long) r7) - 1);
    }

    public static final LongRange until(long r5, int r7) {
        return new LongRange(r5, ((long) r7) - 1);
    }

    public static final LongRange until(long r3, long r5) {
        if (r5 > Long.MIN_VALUE) goto L7;
        return LongRange.Companion.getEMPTY();
    L7:
        return new LongRange(r3, r5 - 1);
    }

    public static final LongRange until(long r5, short r7) {
        return new LongRange(r5, ((long) r7) - 1);
    }

    public static final LongRange until(short r5, long r6) {
        if (r6 > Long.MIN_VALUE) goto L7;
        return LongRange.Companion.getEMPTY();
    L7:
        return new LongRange(r5, r6 - 1);
    }
}
