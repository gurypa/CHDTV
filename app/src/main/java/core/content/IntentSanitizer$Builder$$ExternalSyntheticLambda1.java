package androidx.core.content;

import androidx.core.util.Predicate;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class IntentSanitizer$Builder$$ExternalSyntheticLambda1 implements Predicate {
    public final /* synthetic */ String f$0;

    public /* synthetic */ IntentSanitizer$Builder$$ExternalSyntheticLambda1(String r1) {
        this.f$0 = r1;
    }

    @Override // androidx.core.util.Predicate
    public /* synthetic */ Predicate and(Predicate r1) {
        return Predicate.CC.$default$and(this, r1);
    }

    @Override // androidx.core.util.Predicate
    public /* synthetic */ Predicate negate() {
        return Predicate.CC.$default$negate(this);
    }

    @Override // androidx.core.util.Predicate
    /* JADX INFO: renamed from: or */
    public /* synthetic */ Predicate mo5or(Predicate r1) {
        return Predicate.CC.$default$or(this, r1);
    }

    @Override // androidx.core.util.Predicate
    public final boolean test(Object r2) {
        return this.f$0.equals((String) r2);
    }
}
