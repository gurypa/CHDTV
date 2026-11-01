package androidx.core.widget;

import android.text.Editable;
import android.text.TextWatcher;
import androidx.media3.exoplayer.upstream.CmcdData;
import androidx.media3.extractor.text.ttml.TtmlNode;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function4;

/* JADX INFO: compiled from: TextView.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J*\u0010\u0006\u001a\u00020\u00032\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\nH\u0016J*\u0010\r\u001a\u00020\u00032\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0016¨\u0006\u000f"}, m421d2 = {"androidx/core/widget/TextViewKt$addTextChangedListener$textWatcher$1", "Landroid/text/TextWatcher;", "afterTextChanged", "", CmcdData.Factory.STREAMING_FORMAT_SS, "Landroid/text/Editable;", "beforeTextChanged", "text", "", "start", "", "count", TtmlNode.ANNOTATION_POSITION_AFTER, "onTextChanged", TtmlNode.ANNOTATION_POSITION_BEFORE, "core-ktx_release"}, m422k = 1, m423mv = {1, 7, 1}, m425xi = 176)
public final class TextViewKt$addTextChangedListener$textWatcher$1 implements TextWatcher {
    final /* synthetic */ Function1<Editable, Unit> $afterTextChanged;
    final /* synthetic */ Function4<CharSequence, Integer, Integer, Integer, Unit> $beforeTextChanged;
    final /* synthetic */ Function4<CharSequence, Integer, Integer, Integer, Unit> $onTextChanged;

    /* JADX WARN: Multi-variable type inference failed */
    public TextViewKt$addTextChangedListener$textWatcher$1(Function1<? super Editable, Unit> r1, Function4<? super CharSequence, ? super Integer, ? super Integer, ? super Integer, Unit> r2, Function4<? super CharSequence, ? super Integer, ? super Integer, ? super Integer, Unit> r3) {
        this.$afterTextChanged = r1;
        this.$beforeTextChanged = r2;
        this.$onTextChanged = r3;
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable r2) {
        this.$afterTextChanged.invoke(r2);
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence r5, int r6, int r7, int r8) {
        this.$beforeTextChanged.invoke(r5, Integer.valueOf(r6), Integer.valueOf(r7), Integer.valueOf(r8));
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence r5, int r6, int r7, int r8) {
        this.$onTextChanged.invoke(r5, Integer.valueOf(r6), Integer.valueOf(r7), Integer.valueOf(r8));
    }
}
