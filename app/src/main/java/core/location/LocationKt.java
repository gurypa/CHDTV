package androidx.core.location;

import android.location.Location;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Location.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0086\n\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0002H\u0086\n¨\u0006\u0004"}, m421d2 = {"component1", "", "Landroid/location/Location;", "component2", "core-ktx_release"}, m422k = 2, m423mv = {1, 7, 1}, m425xi = 48)
public final class LocationKt {
    public static final double component1(Location r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        return r3.getLatitude();
    }

    public static final double component2(Location r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        return r3.getLongitude();
    }
}
