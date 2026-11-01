package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.AnnotationTarget;

/* JADX INFO: compiled from: RequiresOptIn.kt */
/* JADX INFO: loaded from: classes.dex */
@Target({ElementType.ANNOTATION_TYPE})
@Metadata(m420d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0087\u0002\u0018\u00002\u00020\u0001:\u0001\u0005B\n\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003R\u000f\u0010\u0002\u001a\u00020\u0003¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0004¨\u0006\u0006"}, m421d2 = {"Landroidx/annotation/RequiresOptIn;", "", "level", "Landroidx/annotation/RequiresOptIn$Level;", "()Landroidx/annotation/RequiresOptIn$Level;", "Level", "annotation-experimental_release"}, m422k = 1, m423mv = {1, 7, 1}, m425xi = 48)
@kotlin.annotation.Target(allowedTargets = {AnnotationTarget.ANNOTATION_CLASS})
@Retention(RetentionPolicy.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.BINARY)
public @interface RequiresOptIn {

    /* JADX INFO: compiled from: RequiresOptIn.kt */
    @Metadata(m420d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004¨\u0006\u0005"}, m421d2 = {"Landroidx/annotation/RequiresOptIn$Level;", "", "(Ljava/lang/String;I)V", "WARNING", "ERROR", "annotation-experimental_release"}, m422k = 1, m423mv = {1, 7, 1}, m425xi = 48)
    public static final enum Level extends Enum<Level> {
        private static final /* synthetic */ Level[] $VALUES = null;
        public static final Level ERROR = null;
        public static final Level WARNING = null;

        private static final /* synthetic */ Level[] $values() {
            return new Level[]{WARNING, ERROR};
        }

        static {
            WARNING = new Level("WARNING", 0);
            ERROR = new Level("ERROR", 1);
            $VALUES = $values();
        }

        private Level(String r1, int r2) {
            super(r1, r2);
        }

        public static Level valueOf(String r1) {
            return (Level) Enum.valueOf(Level.class, r1);
        }

        public static Level[] values() {
            return (Level[]) $VALUES.clone();
        }
    }

    Level level() default Level.ERROR;
}
