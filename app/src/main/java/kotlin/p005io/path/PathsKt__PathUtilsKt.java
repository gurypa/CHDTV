package kotlin.p005io.path;

import androidx.exifinterface.media.ExifInterface;
import androidx.media3.extractor.text.ttml.TtmlNode;
import java.io.IOException;
import java.net.URI;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileStore;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.p005io.CloseableKt;
import kotlin.sequences.Sequence;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: PathUtils.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000Ì\u0001\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0001\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0011\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u0017\u001a\u00020\u0001H\u0087\b\u001a*\u0010\u0016\u001a\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u00012\u0012\u0010\u0019\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00010\u001a\"\u00020\u0001H\u0087\b¢\u0006\u0002\u0010\u001b\u001a?\u0010\u001c\u001a\u00020\u00022\b\u0010\u001d\u001a\u0004\u0018\u00010\u00022\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u00012\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0007¢\u0006\u0002\u0010!\u001a6\u0010\u001c\u001a\u00020\u00022\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u00012\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0087\b¢\u0006\u0002\u0010\"\u001aK\u0010#\u001a\u00020\u00022\b\u0010\u001d\u001a\u0004\u0018\u00010\u00022\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010$\u001a\u0004\u0018\u00010\u00012\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0007¢\u0006\u0002\u0010%\u001aB\u0010#\u001a\u00020\u00022\n\b\u0002\u0010\u001e\u001a\u0004\u0018\u00010\u00012\n\b\u0002\u0010$\u001a\u0004\u0018\u00010\u00012\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0087\b¢\u0006\u0002\u0010&\u001a\u001c\u0010'\u001a\u00020(2\u0006\u0010\u0017\u001a\u00020\u00022\n\u0010)\u001a\u0006\u0012\u0002\b\u00030*H\u0001\u001a4\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00020,2\u0017\u0010-\u001a\u0013\u0012\u0004\u0012\u00020/\u0012\u0004\u0012\u0002000.¢\u0006\u0002\b1H\u0007\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a\r\u00102\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u00103\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a.\u00104\u001a\u00020\u0002*\u00020\u00022\u0006\u00105\u001a\u00020\u00022\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u0002070\u001a\"\u000207H\u0087\b¢\u0006\u0002\u00108\u001a\u001f\u00104\u001a\u00020\u0002*\u00020\u00022\u0006\u00105\u001a\u00020\u00022\b\b\u0002\u00109\u001a\u00020:H\u0087\b\u001a.\u0010;\u001a\u00020\u0002*\u00020\u00022\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0087\b¢\u0006\u0002\u0010<\u001a.\u0010=\u001a\u00020\u0002*\u00020\u00022\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0087\b¢\u0006\u0002\u0010<\u001a.\u0010>\u001a\u00020\u0002*\u00020\u00022\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0087\b¢\u0006\u0002\u0010<\u001a\u0015\u0010?\u001a\u00020\u0002*\u00020\u00022\u0006\u00105\u001a\u00020\u0002H\u0087\b\u001a-\u0010@\u001a\u00020\u0002*\u00020\u00022\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0007¢\u0006\u0002\u0010<\u001a6\u0010A\u001a\u00020\u0002*\u00020\u00022\u0006\u00105\u001a\u00020\u00022\u001a\u0010\u001f\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030 0\u001a\"\u0006\u0012\u0002\b\u00030 H\u0087\b¢\u0006\u0002\u0010B\u001a\r\u0010C\u001a\u000200*\u00020\u0002H\u0087\b\u001a\r\u0010D\u001a\u00020:*\u00020\u0002H\u0087\b\u001a\u0015\u0010E\u001a\u00020\u0002*\u00020\u00022\u0006\u0010F\u001a\u00020\u0002H\u0087\n\u001a\u0015\u0010E\u001a\u00020\u0002*\u00020\u00022\u0006\u0010F\u001a\u00020\u0001H\u0087\n\u001a&\u0010G\u001a\u00020:*\u00020\u00022\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u00020H0\u001a\"\u00020HH\u0087\b¢\u0006\u0002\u0010I\u001a2\u0010J\u001a\u0002HK\"\n\b\u0000\u0010K\u0018\u0001*\u00020L*\u00020\u00022\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u00020H0\u001a\"\u00020HH\u0087\b¢\u0006\u0002\u0010M\u001a4\u0010N\u001a\u0004\u0018\u0001HK\"\n\b\u0000\u0010K\u0018\u0001*\u00020L*\u00020\u00022\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u00020H0\u001a\"\u00020HH\u0087\b¢\u0006\u0002\u0010M\u001a\r\u0010O\u001a\u00020P*\u00020\u0002H\u0087\b\u001a\r\u0010Q\u001a\u00020R*\u00020\u0002H\u0087\b\u001a.\u0010S\u001a\u000200*\u00020\u00022\b\b\u0002\u0010T\u001a\u00020\u00012\u0012\u0010U\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u0002000.H\u0087\bø\u0001\u0000\u001a0\u0010V\u001a\u0004\u0018\u00010W*\u00020\u00022\u0006\u0010X\u001a\u00020\u00012\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u00020H0\u001a\"\u00020HH\u0087\b¢\u0006\u0002\u0010Y\u001a&\u0010Z\u001a\u00020[*\u00020\u00022\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u00020H0\u001a\"\u00020HH\u0087\b¢\u0006\u0002\u0010\\\u001a(\u0010]\u001a\u0004\u0018\u00010^*\u00020\u00022\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u00020H0\u001a\"\u00020HH\u0087\b¢\u0006\u0002\u0010_\u001a,\u0010`\u001a\b\u0012\u0004\u0012\u00020b0a*\u00020\u00022\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u00020H0\u001a\"\u00020HH\u0087\b¢\u0006\u0002\u0010c\u001a&\u0010d\u001a\u00020:*\u00020\u00022\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u00020H0\u001a\"\u00020HH\u0087\b¢\u0006\u0002\u0010I\u001a\r\u0010e\u001a\u00020:*\u00020\u0002H\u0087\b\u001a\r\u0010f\u001a\u00020:*\u00020\u0002H\u0087\b\u001a\r\u0010g\u001a\u00020:*\u00020\u0002H\u0087\b\u001a&\u0010h\u001a\u00020:*\u00020\u00022\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u00020H0\u001a\"\u00020HH\u0087\b¢\u0006\u0002\u0010I\u001a\u0015\u0010i\u001a\u00020:*\u00020\u00022\u0006\u0010F\u001a\u00020\u0002H\u0087\b\u001a\r\u0010j\u001a\u00020:*\u00020\u0002H\u0087\b\u001a\r\u0010k\u001a\u00020:*\u00020\u0002H\u0087\b\u001a\u001c\u0010l\u001a\b\u0012\u0004\u0012\u00020\u00020m*\u00020\u00022\b\b\u0002\u0010T\u001a\u00020\u0001H\u0007\u001a.\u0010n\u001a\u00020\u0002*\u00020\u00022\u0006\u00105\u001a\u00020\u00022\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u0002070\u001a\"\u000207H\u0087\b¢\u0006\u0002\u00108\u001a\u001f\u0010n\u001a\u00020\u0002*\u00020\u00022\u0006\u00105\u001a\u00020\u00022\b\b\u0002\u00109\u001a\u00020:H\u0087\b\u001a&\u0010o\u001a\u00020:*\u00020\u00022\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u00020H0\u001a\"\u00020HH\u0087\b¢\u0006\u0002\u0010I\u001a2\u0010p\u001a\u0002Hq\"\n\b\u0000\u0010q\u0018\u0001*\u00020r*\u00020\u00022\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u00020H0\u001a\"\u00020HH\u0087\b¢\u0006\u0002\u0010s\u001a<\u0010p\u001a\u0010\u0012\u0004\u0012\u00020\u0001\u0012\u0006\u0012\u0004\u0018\u00010W0t*\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u00012\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u00020H0\u001a\"\u00020HH\u0087\b¢\u0006\u0002\u0010u\u001a\r\u0010v\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\u0014\u0010w\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0002H\u0007\u001a\u0016\u0010x\u001a\u0004\u0018\u00010\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0002H\u0007\u001a\u0014\u0010y\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0018\u001a\u00020\u0002H\u0007\u001a8\u0010z\u001a\u00020\u0002*\u00020\u00022\u0006\u0010X\u001a\u00020\u00012\b\u0010{\u001a\u0004\u0018\u00010W2\u0012\u00106\u001a\n\u0012\u0006\b\u0001\u0012\u00020H0\u001a\"\u00020HH\u0087\b¢\u0006\u0002\u0010|\u001a\u0015\u0010}\u001a\u00020\u0002*\u00020\u00022\u0006\u0010{\u001a\u00020[H\u0087\b\u001a\u0015\u0010~\u001a\u00020\u0002*\u00020\u00022\u0006\u0010{\u001a\u00020^H\u0087\b\u001a\u001b\u0010\u007f\u001a\u00020\u0002*\u00020\u00022\f\u0010{\u001a\b\u0012\u0004\u0012\u00020b0aH\u0087\b\u001a\u000f\u0010\u0080\u0001\u001a\u00020\u0002*\u00030\u0081\u0001H\u0087\b\u001aF\u0010\u0082\u0001\u001a\u0003H\u0083\u0001\"\u0005\b\u0000\u0010\u0083\u0001*\u00020\u00022\b\b\u0002\u0010T\u001a\u00020\u00012\u001b\u0010\u0084\u0001\u001a\u0016\u0012\u000b\u0012\t\u0012\u0004\u0012\u00020\u00020\u0085\u0001\u0012\u0005\u0012\u0003H\u0083\u00010.H\u0087\bø\u0001\u0000¢\u0006\u0003\u0010\u0086\u0001\u001a3\u0010\u0087\u0001\u001a\u000200*\u00020\u00022\r\u0010\u0088\u0001\u001a\b\u0012\u0004\u0012\u00020\u00020,2\n\b\u0002\u0010\u0089\u0001\u001a\u00030\u008a\u00012\t\b\u0002\u0010\u008b\u0001\u001a\u00020:H\u0007\u001aJ\u0010\u0087\u0001\u001a\u000200*\u00020\u00022\n\b\u0002\u0010\u0089\u0001\u001a\u00030\u008a\u00012\t\b\u0002\u0010\u008b\u0001\u001a\u00020:2\u0017\u0010-\u001a\u0013\u0012\u0004\u0012\u00020/\u0012\u0004\u0012\u0002000.¢\u0006\u0002\b1H\u0007\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0003 \u0001\u001a0\u0010\u008c\u0001\u001a\t\u0012\u0004\u0012\u00020\u00020\u0085\u0001*\u00020\u00022\u0014\u00106\u001a\u000b\u0012\u0007\b\u0001\u0012\u00030\u008d\u00010\u001a\"\u00030\u008d\u0001H\u0007¢\u0006\u0003\u0010\u008e\u0001\"\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00028FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u001f\u0010\u0007\u001a\u00020\u0001*\u00020\u00028Æ\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\b\u0010\u0004\u001a\u0004\b\t\u0010\u0006\"\u001e\u0010\n\u001a\u00020\u0001*\u00020\u00028FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u000b\u0010\u0004\u001a\u0004\b\f\u0010\u0006\"\u001e\u0010\r\u001a\u00020\u0001*\u00020\u00028FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u000e\u0010\u0004\u001a\u0004\b\u000f\u0010\u0006\"\u001e\u0010\u0010\u001a\u00020\u0001*\u00020\u00028FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0011\u0010\u0004\u001a\u0004\b\u0012\u0010\u0006\"\u001f\u0010\u0013\u001a\u00020\u0001*\u00020\u00028Æ\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0014\u0010\u0004\u001a\u0004\b\u0015\u0010\u0006\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u008f\u0001"}, m421d2 = {"extension", "", "Ljava/nio/file/Path;", "getExtension$annotations", "(Ljava/nio/file/Path;)V", "getExtension", "(Ljava/nio/file/Path;)Ljava/lang/String;", "invariantSeparatorsPath", "getInvariantSeparatorsPath$annotations", "getInvariantSeparatorsPath", "invariantSeparatorsPathString", "getInvariantSeparatorsPathString$annotations", "getInvariantSeparatorsPathString", "name", "getName$annotations", "getName", "nameWithoutExtension", "getNameWithoutExtension$annotations", "getNameWithoutExtension", "pathString", "getPathString$annotations", "getPathString", "Path", "path", TtmlNode.RUBY_BASE, "subpaths", "", "(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path;", "createTempDirectory", "directory", "prefix", "attributes", "Ljava/nio/file/attribute/FileAttribute;", "(Ljava/nio/file/Path;Ljava/lang/String;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;", "(Ljava/lang/String;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;", "createTempFile", "suffix", "(Ljava/nio/file/Path;Ljava/lang/String;Ljava/lang/String;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;", "(Ljava/lang/String;Ljava/lang/String;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;", "fileAttributeViewNotAvailable", "", "attributeViewClass", "Ljava/lang/Class;", "fileVisitor", "Ljava/nio/file/FileVisitor;", "builderAction", "Lkotlin/Function1;", "Lkotlin/io/path/FileVisitorBuilder;", "", "Lkotlin/ExtensionFunctionType;", "absolute", "absolutePathString", "copyTo", "target", "options", "Ljava/nio/file/CopyOption;", "(Ljava/nio/file/Path;Ljava/nio/file/Path;[Ljava/nio/file/CopyOption;)Ljava/nio/file/Path;", "overwrite", "", "createDirectories", "(Ljava/nio/file/Path;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;", "createDirectory", "createFile", "createLinkPointingTo", "createParentDirectories", "createSymbolicLinkPointingTo", "(Ljava/nio/file/Path;Ljava/nio/file/Path;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;", "deleteExisting", "deleteIfExists", TtmlNode.TAG_DIV, "other", "exists", "Ljava/nio/file/LinkOption;", "(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Z", "fileAttributesView", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, "Ljava/nio/file/attribute/FileAttributeView;", "(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Ljava/nio/file/attribute/FileAttributeView;", "fileAttributesViewOrNull", "fileSize", "", "fileStore", "Ljava/nio/file/FileStore;", "forEachDirectoryEntry", "glob", "action", "getAttribute", "", "attribute", "(Ljava/nio/file/Path;Ljava/lang/String;[Ljava/nio/file/LinkOption;)Ljava/lang/Object;", "getLastModifiedTime", "Ljava/nio/file/attribute/FileTime;", "(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Ljava/nio/file/attribute/FileTime;", "getOwner", "Ljava/nio/file/attribute/UserPrincipal;", "(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Ljava/nio/file/attribute/UserPrincipal;", "getPosixFilePermissions", "", "Ljava/nio/file/attribute/PosixFilePermission;", "(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Ljava/util/Set;", "isDirectory", "isExecutable", "isHidden", "isReadable", "isRegularFile", "isSameFileAs", "isSymbolicLink", "isWritable", "listDirectoryEntries", "", "moveTo", "notExists", "readAttributes", ExifInterface.GPS_MEASUREMENT_IN_PROGRESS, "Ljava/nio/file/attribute/BasicFileAttributes;", "(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Ljava/nio/file/attribute/BasicFileAttributes;", "", "(Ljava/nio/file/Path;Ljava/lang/String;[Ljava/nio/file/LinkOption;)Ljava/util/Map;", "readSymbolicLink", "relativeTo", "relativeToOrNull", "relativeToOrSelf", "setAttribute", "value", "(Ljava/nio/file/Path;Ljava/lang/String;Ljava/lang/Object;[Ljava/nio/file/LinkOption;)Ljava/nio/file/Path;", "setLastModifiedTime", "setOwner", "setPosixFilePermissions", "toPath", "Ljava/net/URI;", "useDirectoryEntries", ExifInterface.GPS_DIRECTION_TRUE, "block", "Lkotlin/sequences/Sequence;", "(Ljava/nio/file/Path;Ljava/lang/String;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "visitFileTree", "visitor", "maxDepth", "", "followLinks", "walk", "Lkotlin/io/path/PathWalkOption;", "(Ljava/nio/file/Path;[Lkotlin/io/path/PathWalkOption;)Lkotlin/sequences/Sequence;", "kotlin-stdlib-jdk7"}, m422k = 5, m423mv = {1, 9, 0}, m425xi = 49, m426xs = "kotlin/io/path/PathsKt")
class PathsKt__PathUtilsKt extends PathsKt__PathRecursiveFunctionsKt {
    public PathsKt__PathUtilsKt() {
    }

    private static final Path Path(String r2) {
        Intrinsics.checkNotNullParameter(r2, "path");
        Path r0 = Paths.get(r2, new String[0]);
        Intrinsics.checkNotNullExpressionValue(r0, "get(...)");
        return r0;
    }

    private static final Path Path(String r2, String... r3) {
        Intrinsics.checkNotNullParameter(r2, TtmlNode.RUBY_BASE);
        Intrinsics.checkNotNullParameter(r3, "subpaths");
        Path r0 = Paths.get(r2, (String[]) Arrays.copyOf(r3, r3.length));
        Intrinsics.checkNotNullExpressionValue(r0, "get(...)");
        return r0;
    }

    private static final Path absolute(Path r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Path r0 = r2.toAbsolutePath();
        Intrinsics.checkNotNullExpressionValue(r0, "toAbsolutePath(...)");
        return r0;
    }

    private static final String absolutePathString(Path r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.toAbsolutePath().toString();
    }

    private static final Path copyTo(Path r3, Path r4, boolean r5) throws IOException {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "target");
        if (r5 == false) goto L5;
        CopyOption[] r1 = {StandardCopyOption.REPLACE_EXISTING};
    L6:
        Path r0 = Files.copy(r3, r4, (CopyOption[]) Arrays.copyOf(r1, r1.length));
        Intrinsics.checkNotNullExpressionValue(r0, "copy(...)");
        return r0;
    L5:
        r1 = new CopyOption[0];
        goto L6
    }

    private static final Path copyTo(Path r2, Path r3, CopyOption... r4) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "target");
        Intrinsics.checkNotNullParameter(r4, "options");
        Path r0 = Files.copy(r2, r3, (CopyOption[]) Arrays.copyOf(r4, r4.length));
        Intrinsics.checkNotNullExpressionValue(r0, "copy(...)");
        return r0;
    }

    static /* synthetic */ Path copyTo$default(Path r1, Path r2, boolean r3, int r4, Object r5) throws IOException {
        if ((r4 & 2) == 0) goto L5;
        r3 = false;
    L5:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "target");
        if (r3 == false) goto L8;
        CopyOption[] r6 = {StandardCopyOption.REPLACE_EXISTING};
    L9:
        Path r7 = Files.copy(r1, r2, (CopyOption[]) Arrays.copyOf(r6, r6.length));
        Intrinsics.checkNotNullExpressionValue(r7, "copy(...)");
        return r7;
    L8:
        r6 = new CopyOption[0];
        goto L9
    }

    private static final Path createDirectories(Path r2, FileAttribute<?>... r3) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "attributes");
        Path r0 = Files.createDirectories(r2, (FileAttribute[]) Arrays.copyOf(r3, r3.length));
        Intrinsics.checkNotNullExpressionValue(r0, "createDirectories(...)");
        return r0;
    }

    private static final Path createDirectory(Path r2, FileAttribute<?>... r3) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "attributes");
        Path r0 = Files.createDirectory(r2, (FileAttribute[]) Arrays.copyOf(r3, r3.length));
        Intrinsics.checkNotNullExpressionValue(r0, "createDirectory(...)");
        return r0;
    }

    private static final Path createFile(Path r2, FileAttribute<?>... r3) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "attributes");
        Path r0 = Files.createFile(r2, (FileAttribute[]) Arrays.copyOf(r3, r3.length));
        Intrinsics.checkNotNullExpressionValue(r0, "createFile(...)");
        return r0;
    }

    private static final Path createLinkPointingTo(Path r2, Path r3) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "target");
        Path r0 = Files.createLink(r2, r3);
        Intrinsics.checkNotNullExpressionValue(r0, "createLink(...)");
        return r0;
    }

    public static final Path createParentDirectories(Path r6, FileAttribute<?>... r7) throws IOException {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "attributes");
        Path r2 = r6.getParent();
        if (r2 != null) goto L5;
    L14:
        return r6;
    L5:
        if (Files.isDirectory(r2, (LinkOption[]) Arrays.copyOf(new LinkOption[0], 0)) == true) goto L14;
        FileAttribute[] r4 = (FileAttribute[]) Arrays.copyOf(r7, r7.length);     // Catch: FileAlreadyExistsException -> L8
        Intrinsics.checkNotNullExpressionValue(Files.createDirectories(r2, (FileAttribute[]) Arrays.copyOf(r4, r4.length)), "createDirectories(...)");     // Catch: FileAlreadyExistsException -> L8
    L8:
        e = move-exception;
        if (Files.isDirectory(r2, (LinkOption[]) Arrays.copyOf(new LinkOption[0], 0)) == true) goto L14;
        throw e;
    }

    private static final Path createSymbolicLinkPointingTo(Path r2, Path r3, FileAttribute<?>... r4) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "target");
        Intrinsics.checkNotNullParameter(r4, "attributes");
        Path r0 = Files.createSymbolicLink(r2, r3, (FileAttribute[]) Arrays.copyOf(r4, r4.length));
        Intrinsics.checkNotNullExpressionValue(r0, "createSymbolicLink(...)");
        return r0;
    }

    private static final Path createTempDirectory(String r2, FileAttribute<?>... r3) throws IOException {
        Intrinsics.checkNotNullParameter(r3, "attributes");
        Path r0 = Files.createTempDirectory(r2, (FileAttribute[]) Arrays.copyOf(r3, r3.length));
        Intrinsics.checkNotNullExpressionValue(r0, "createTempDirectory(...)");
        return r0;
    }

    public static final Path createTempDirectory(Path r2, String r3, FileAttribute<?>... r4) throws IOException {
        Intrinsics.checkNotNullParameter(r4, "attributes");
        if (r2 == null) goto L5;
        Path r1 = Files.createTempDirectory(r2, r3, (FileAttribute[]) Arrays.copyOf(r4, r4.length));
        Intrinsics.checkNotNullExpressionValue(r1, "createTempDirectory(...)");
        return r1;
    L5:
        Path r5 = Files.createTempDirectory(r3, (FileAttribute[]) Arrays.copyOf(r4, r4.length));
        Intrinsics.checkNotNullExpressionValue(r5, "createTempDirectory(...)");
        return r5;
    }

    static /* synthetic */ Path createTempDirectory$default(String r0, FileAttribute[] r1, int r2, Object r3) throws IOException {
        if ((r2 & 1) == 0) goto L5;
        r0 = null;
    L5:
        Intrinsics.checkNotNullParameter(r1, "attributes");
        Path r4 = Files.createTempDirectory(r0, (FileAttribute[]) Arrays.copyOf(r1, r1.length));
        Intrinsics.checkNotNullExpressionValue(r4, "createTempDirectory(...)");
        return r4;
    }

    public static /* synthetic */ Path createTempDirectory$default(Path r0, String r1, FileAttribute[] r2, int r3, Object r4) throws IOException {
        if ((r3 & 2) == 0) goto L6;
        r1 = null;
    L6:
        return PathsKt.createTempDirectory(r0, r1, r2);
    }

    private static final Path createTempFile(String r2, String r3, FileAttribute<?>... r4) throws IOException {
        Intrinsics.checkNotNullParameter(r4, "attributes");
        Path r0 = Files.createTempFile(r2, r3, (FileAttribute[]) Arrays.copyOf(r4, r4.length));
        Intrinsics.checkNotNullExpressionValue(r0, "createTempFile(...)");
        return r0;
    }

    public static final Path createTempFile(Path r2, String r3, String r4, FileAttribute<?>... r5) throws IOException {
        Intrinsics.checkNotNullParameter(r5, "attributes");
        if (r2 == null) goto L5;
        Path r1 = Files.createTempFile(r2, r3, r4, (FileAttribute[]) Arrays.copyOf(r5, r5.length));
        Intrinsics.checkNotNullExpressionValue(r1, "createTempFile(...)");
        return r1;
    L5:
        Path r6 = Files.createTempFile(r3, r4, (FileAttribute[]) Arrays.copyOf(r5, r5.length));
        Intrinsics.checkNotNullExpressionValue(r6, "createTempFile(...)");
        return r6;
    }

    static /* synthetic */ Path createTempFile$default(String r0, String r1, FileAttribute[] r2, int r3, Object r4) throws IOException {
        if ((r3 & 1) == 0) goto L6;
        r0 = null;
    L6:
        if ((r3 & 2) == 0) goto L8;
        r1 = null;
    L8:
        Intrinsics.checkNotNullParameter(r2, "attributes");
        Path r5 = Files.createTempFile(r0, r1, (FileAttribute[]) Arrays.copyOf(r2, r2.length));
        Intrinsics.checkNotNullExpressionValue(r5, "createTempFile(...)");
        return r5;
    }

    public static /* synthetic */ Path createTempFile$default(Path r1, String r2, String r3, FileAttribute[] r4, int r5, Object r6) throws IOException {
        if ((r5 & 2) == 0) goto L6;
        r2 = null;
    L6:
        if ((r5 & 4) == 0) goto L9;
        r3 = null;
    L9:
        return PathsKt.createTempFile(r1, r2, r3, r4);
    }

    private static final void deleteExisting(Path r1) throws IOException {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Files.delete(r1);
    }

    private static final boolean deleteIfExists(Path r1) throws IOException {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return Files.deleteIfExists(r1);
    }

    private static final Path div(Path r2, String r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        Path r0 = r2.resolve(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "resolve(...)");
        return r0;
    }

    private static final Path div(Path r2, Path r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "other");
        Path r0 = r2.resolve(r3);
        Intrinsics.checkNotNullExpressionValue(r0, "resolve(...)");
        return r0;
    }

    private static final boolean exists(Path r1, LinkOption... r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "options");
        return Files.exists(r1, (LinkOption[]) Arrays.copyOf(r2, r2.length));
    }

    public static final Void fileAttributeViewNotAvailable(Path r3, Class<?> r4) {
        Intrinsics.checkNotNullParameter(r3, "path");
        Intrinsics.checkNotNullParameter(r4, "attributeViewClass");
        throw new UnsupportedOperationException("The desired attribute view type " + r4 + " is not available for the file " + r3 + '.');
    }

    private static final /* synthetic */ <V extends FileAttributeView> V fileAttributesView(Path r4, LinkOption... r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "options");
        Intrinsics.reifiedOperationMarker(4, ExifInterface.GPS_MEASUREMENT_INTERRUPTED);
        V r2 = (V) Files.getFileAttributeView(r4, FileAttributeView.class, (LinkOption[]) Arrays.copyOf(r5, r5.length));
        if (r2 != null) goto L5;
        Intrinsics.reifiedOperationMarker(4, ExifInterface.GPS_MEASUREMENT_INTERRUPTED);
        PathsKt.fileAttributeViewNotAvailable(r4, FileAttributeView.class);
        throw new KotlinNothingValueException();
    L5:
        return r2;
    }

    private static final /* synthetic */ <V extends FileAttributeView> V fileAttributesViewOrNull(Path r2, LinkOption... r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "options");
        Intrinsics.reifiedOperationMarker(4, ExifInterface.GPS_MEASUREMENT_INTERRUPTED);
        return (V) Files.getFileAttributeView(r2, FileAttributeView.class, (LinkOption[]) Arrays.copyOf(r3, r3.length));
    }

    private static final long fileSize(Path r2) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return Files.size(r2);
    }

    private static final FileStore fileStore(Path r2) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        FileStore r0 = Files.getFileStore(r2);
        Intrinsics.checkNotNullExpressionValue(r0, "getFileStore(...)");
        return r0;
    }

    public static final FileVisitor<Path> fileVisitor(Function1<? super FileVisitorBuilder, Unit> r1) {
        Intrinsics.checkNotNullParameter(r1, "builderAction");
        FileVisitorBuilderImpl r0 = new FileVisitorBuilderImpl();
        r1.invoke(r0);
        return r0.build();
    }

    private static final void forEachDirectoryEntry(Path r9, String r10, Function1<? super Path, Unit> r11) throws IOException {
        Intrinsics.checkNotNullParameter(r9, "<this>");
        Intrinsics.checkNotNullParameter(r10, "glob");
        Intrinsics.checkNotNullParameter(r11, "action");
        DirectoryStream<Path> r0 = Files.newDirectoryStream(r9, r10);
        DirectoryStream<Path> r3 = r0;     // Catch: Throwable -> L14
        Intrinsics.checkNotNull(r3);     // Catch: Throwable -> L14
        Iterator<Path> r7 = r3.iterator();     // Catch: Throwable -> L14
    L5:
        if (r7.hasNext() == false) goto L7;
        r11.invoke(r7.next());     // Catch: Throwable -> L14
        goto L5
    L7:
        Unit r4 = Unit.INSTANCE;     // Catch: Throwable -> L14
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L11;
        CloseableKt.closeFinally(r0, null);
        return;
    L11:
        if (r0 == null) goto L34;
        r0.close();
        return;
    L34:
        return;
    L14:
        th = move-exception;
        throw th;     // Catch: Throwable -> L16
    L16:
        th = move-exception;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == true) goto L24;
        if (r0 != null) goto L26;
    L25:
        throw th;
    L26:
        r0.close();     // Catch: Throwable -> L22
        goto L25
    L24:
        CloseableKt.closeFinally(r0, th);
        goto L25
    }

    static /* synthetic */ void forEachDirectoryEntry$default(Path r7, String r8, Function1 r9, int r10, Object r11) throws IOException {
        if ((r10 & 1) == 0) goto L5;
        r8 = "*";
    L5:
        Intrinsics.checkNotNullParameter(r7, "<this>");
        Intrinsics.checkNotNullParameter(r8, "glob");
        Intrinsics.checkNotNullParameter(r9, "action");
        DirectoryStream<Path> r12 = Files.newDirectoryStream(r7, r8);
        DirectoryStream<Path> r1 = r12;     // Catch: Throwable -> L17
        Intrinsics.checkNotNull(r1);     // Catch: Throwable -> L17
        Iterator<Path> r5 = r1.iterator();     // Catch: Throwable -> L17
    L8:
        if (r5.hasNext() == false) goto L10;
        r9.invoke(r5.next());     // Catch: Throwable -> L17
        goto L8
    L10:
        Unit r2 = Unit.INSTANCE;     // Catch: Throwable -> L17
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L14;
        CloseableKt.closeFinally(r12, null);
        return;
    L14:
        if (r12 == null) goto L37;
        r12.close();
        return;
    L37:
        return;
    L17:
        th = move-exception;
        throw th;     // Catch: Throwable -> L19
    L19:
        th = move-exception;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == true) goto L27;
        if (r12 != null) goto L31;
    L28:
        throw th;
    L31:
        r12.close();     // Catch: Throwable -> L25
        goto L28
    L27:
        CloseableKt.closeFinally(r12, th);
        goto L28
    }

    private static final Object getAttribute(Path r1, String r2, LinkOption... r3) throws IOException {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "attribute");
        Intrinsics.checkNotNullParameter(r3, "options");
        return Files.getAttribute(r1, r2, (LinkOption[]) Arrays.copyOf(r3, r3.length));
    }

    public static final String getExtension(Path r3) {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Path r0 = r3.getFileName();
        if (r0 == null) goto L11;
        String r1 = r0.toString();
        if (r1 == null) goto L12;
        String r2 = StringsKt.substringAfterLast(r1, '.', "");
        if (r2 != null) goto L10;
        return "";
    L10:
        return r2;
    L12:
        return "";
    L11:
        return "";
    }

    public static /* synthetic */ void getExtension$annotations(Path r0) {
    }

    private static final String getInvariantSeparatorsPath(Path r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return PathsKt.getInvariantSeparatorsPathString(r1);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Use invariantSeparatorsPathString property instead.", replaceWith = @ReplaceWith(expression = "invariantSeparatorsPathString", imports = {}))
    public static /* synthetic */ void getInvariantSeparatorsPath$annotations(Path r0) {
    }

    public static final String getInvariantSeparatorsPathString(Path r7) {
        Intrinsics.checkNotNullParameter(r7, "<this>");
        String r2 = r7.getFileSystem().getSeparator();
        if (Intrinsics.areEqual(r2, "/") == true) goto L6;
        String r1 = r7.toString();
        Intrinsics.checkNotNull(r2);
        return StringsKt.replace$default(r1, r2, "/", false, 4, null);
    L6:
        return r7.toString();
    }

    public static /* synthetic */ void getInvariantSeparatorsPathString$annotations(Path r0) {
    }

    private static final FileTime getLastModifiedTime(Path r2, LinkOption... r3) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "options");
        FileTime r0 = Files.getLastModifiedTime(r2, (LinkOption[]) Arrays.copyOf(r3, r3.length));
        Intrinsics.checkNotNullExpressionValue(r0, "getLastModifiedTime(...)");
        return r0;
    }

    public static final String getName(Path r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Path r0 = r1.getFileName();
        if (r0 == null) goto L5;
        String r2 = r0.toString();
    L6:
        if (r2 != null) goto L9;
        return "";
    L9:
        return r2;
    L5:
        r2 = null;
        goto L6
    }

    public static /* synthetic */ void getName$annotations(Path r0) {
    }

    public static final String getNameWithoutExtension(Path r4) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Path r0 = r4.getFileName();
        if (r0 == null) goto L8;
        String r1 = r0.toString();
        if (r1 == null) goto L8;
        String r2 = StringsKt.substringBeforeLast$default(r1, ".", null, 2, null);
        if (r2 == null) goto L8;
        return r2;
    L8:
        return "";
    }

    public static /* synthetic */ void getNameWithoutExtension$annotations(Path r0) {
    }

    private static final UserPrincipal getOwner(Path r1, LinkOption... r2) throws IOException {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "options");
        return Files.getOwner(r1, (LinkOption[]) Arrays.copyOf(r2, r2.length));
    }

    private static final String getPathString(Path r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return r1.toString();
    }

    public static /* synthetic */ void getPathString$annotations(Path r0) {
    }

    private static final Set<PosixFilePermission> getPosixFilePermissions(Path r2, LinkOption... r3) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "options");
        Set<PosixFilePermission> r0 = Files.getPosixFilePermissions(r2, (LinkOption[]) Arrays.copyOf(r3, r3.length));
        Intrinsics.checkNotNullExpressionValue(r0, "getPosixFilePermissions(...)");
        return r0;
    }

    private static final boolean isDirectory(Path r1, LinkOption... r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "options");
        return Files.isDirectory(r1, (LinkOption[]) Arrays.copyOf(r2, r2.length));
    }

    private static final boolean isExecutable(Path r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return Files.isExecutable(r1);
    }

    private static final boolean isHidden(Path r1) throws IOException {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return Files.isHidden(r1);
    }

    private static final boolean isReadable(Path r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return Files.isReadable(r1);
    }

    private static final boolean isRegularFile(Path r1, LinkOption... r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "options");
        return Files.isRegularFile(r1, (LinkOption[]) Arrays.copyOf(r2, r2.length));
    }

    private static final boolean isSameFileAs(Path r1, Path r2) throws IOException {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "other");
        return Files.isSameFile(r1, r2);
    }

    private static final boolean isSymbolicLink(Path r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return Files.isSymbolicLink(r1);
    }

    private static final boolean isWritable(Path r1) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        return Files.isWritable(r1);
    }

    public static final List<Path> listDirectoryEntries(Path r4, String r5) throws IOException {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "glob");
        DirectoryStream<Path> r0 = Files.newDirectoryStream(r4, r5);
        DirectoryStream<Path> r1 = r0;     // Catch: Throwable -> L6
        Intrinsics.checkNotNull(r1);     // Catch: Throwable -> L6
        List<Path> r3 = CollectionsKt.toList(r1);     // Catch: Throwable -> L6
        CloseableKt.closeFinally(r0, null);
        return r3;
    L6:
        th = move-exception;
        throw th;     // Catch: Throwable -> L8
    L8:
        th = move-exception;
        CloseableKt.closeFinally(r0, th);
        throw th;
    }

    public static /* synthetic */ List listDirectoryEntries$default(Path r0, String r1, int r2, Object r3) throws IOException {
        if ((r2 & 1) == 0) goto L6;
        r1 = "*";
    L6:
        return PathsKt.listDirectoryEntries(r0, r1);
    }

    private static final Path moveTo(Path r3, Path r4, boolean r5) throws IOException {
        Intrinsics.checkNotNullParameter(r3, "<this>");
        Intrinsics.checkNotNullParameter(r4, "target");
        if (r5 == false) goto L5;
        CopyOption[] r1 = {StandardCopyOption.REPLACE_EXISTING};
    L6:
        Path r0 = Files.move(r3, r4, (CopyOption[]) Arrays.copyOf(r1, r1.length));
        Intrinsics.checkNotNullExpressionValue(r0, "move(...)");
        return r0;
    L5:
        r1 = new CopyOption[0];
        goto L6
    }

    private static final Path moveTo(Path r2, Path r3, CopyOption... r4) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "target");
        Intrinsics.checkNotNullParameter(r4, "options");
        Path r0 = Files.move(r2, r3, (CopyOption[]) Arrays.copyOf(r4, r4.length));
        Intrinsics.checkNotNullExpressionValue(r0, "move(...)");
        return r0;
    }

    static /* synthetic */ Path moveTo$default(Path r1, Path r2, boolean r3, int r4, Object r5) throws IOException {
        if ((r4 & 2) == 0) goto L5;
        r3 = false;
    L5:
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "target");
        if (r3 == false) goto L8;
        CopyOption[] r6 = {StandardCopyOption.REPLACE_EXISTING};
    L9:
        Path r7 = Files.move(r1, r2, (CopyOption[]) Arrays.copyOf(r6, r6.length));
        Intrinsics.checkNotNullExpressionValue(r7, "move(...)");
        return r7;
    L8:
        r6 = new CopyOption[0];
        goto L9
    }

    private static final boolean notExists(Path r1, LinkOption... r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "options");
        return Files.notExists(r1, (LinkOption[]) Arrays.copyOf(r2, r2.length));
    }

    private static final /* synthetic */ <A extends BasicFileAttributes> A readAttributes(Path r2, LinkOption... r3) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "options");
        Intrinsics.reifiedOperationMarker(4, ExifInterface.GPS_MEASUREMENT_IN_PROGRESS);
        A r0 = (A) Files.readAttributes(r2, BasicFileAttributes.class, (LinkOption[]) Arrays.copyOf(r3, r3.length));
        Intrinsics.checkNotNullExpressionValue(r0, "readAttributes(...)");
        return r0;
    }

    private static final Map<String, Object> readAttributes(Path r2, String r3, LinkOption... r4) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "attributes");
        Intrinsics.checkNotNullParameter(r4, "options");
        Map<String, Object> r0 = Files.readAttributes(r2, r3, (LinkOption[]) Arrays.copyOf(r4, r4.length));
        Intrinsics.checkNotNullExpressionValue(r0, "readAttributes(...)");
        return r0;
    }

    private static final Path readSymbolicLink(Path r2) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Path r0 = Files.readSymbolicLink(r2);
        Intrinsics.checkNotNullExpressionValue(r0, "readSymbolicLink(...)");
        return r0;
    }

    public static final Path relativeTo(Path r4, Path r5) {
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, TtmlNode.RUBY_BASE);
        return PathRelativizer.INSTANCE.tryRelativeTo(r4, r5);
    L5:
        e = move-exception;
        throw new IllegalArgumentException(e.getMessage() + "\nthis path: " + r4 + "\nbase path: " + r5, e);
    }

    public static final Path relativeToOrNull(Path r2, Path r3) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, TtmlNode.RUBY_BASE);
        return PathRelativizer.INSTANCE.tryRelativeTo(r2, r3);
    L7:
        return null;
    }

    public static final Path relativeToOrSelf(Path r1, Path r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, TtmlNode.RUBY_BASE);
        Path r0 = PathsKt.relativeToOrNull(r1, r2);
        if (r0 == null) goto L5;
        return r0;
    L5:
        return r1;
    }

    private static final Path setAttribute(Path r2, String r3, Object r4, LinkOption... r5) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "attribute");
        Intrinsics.checkNotNullParameter(r5, "options");
        Path r0 = Files.setAttribute(r2, r3, r4, (LinkOption[]) Arrays.copyOf(r5, r5.length));
        Intrinsics.checkNotNullExpressionValue(r0, "setAttribute(...)");
        return r0;
    }

    private static final Path setLastModifiedTime(Path r2, FileTime r3) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "value");
        Path r0 = Files.setLastModifiedTime(r2, r3);
        Intrinsics.checkNotNullExpressionValue(r0, "setLastModifiedTime(...)");
        return r0;
    }

    private static final Path setOwner(Path r2, UserPrincipal r3) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "value");
        Path r0 = Files.setOwner(r2, r3);
        Intrinsics.checkNotNullExpressionValue(r0, "setOwner(...)");
        return r0;
    }

    private static final Path setPosixFilePermissions(Path r2, Set<? extends PosixFilePermission> r3) throws IOException {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Intrinsics.checkNotNullParameter(r3, "value");
        Path r0 = Files.setPosixFilePermissions(r2, r3);
        Intrinsics.checkNotNullExpressionValue(r0, "setPosixFilePermissions(...)");
        return r0;
    }

    private static final Path toPath(URI r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        Path r0 = Paths.get(r2);
        Intrinsics.checkNotNullExpressionValue(r0, "get(...)");
        return r0;
    }

    private static final <T> T useDirectoryEntries(Path r6, String r7, Function1<? super Sequence<? extends Path>, ? extends T> r8) throws IOException {
        Intrinsics.checkNotNullParameter(r6, "<this>");
        Intrinsics.checkNotNullParameter(r7, "glob");
        Intrinsics.checkNotNullParameter(r8, "block");
        DirectoryStream<Path> r0 = Files.newDirectoryStream(r6, r7);
        DirectoryStream<Path> r3 = r0;     // Catch: Throwable -> L10
        Intrinsics.checkNotNull(r3);     // Catch: Throwable -> L10
        T r5 = r8.invoke(CollectionsKt.asSequence(r3));     // Catch: Throwable -> L10
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L7;
        CloseableKt.closeFinally(r0, null);
    L9:
        return r5;
    L7:
        if (r0 == null) goto L9;
        r0.close();
    L10:
        th = move-exception;
        throw th;     // Catch: Throwable -> L12
    L12:
        th = move-exception;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == true) goto L20;
        if (r0 != null) goto L22;
    L21:
        throw th;
    L22:
        r0.close();     // Catch: Throwable -> L18
        goto L21
    L20:
        CloseableKt.closeFinally(r0, th);
        goto L21
    }

    static /* synthetic */ Object useDirectoryEntries$default(Path r4, String r5, Function1 r6, int r7, Object r8) throws IOException {
        if ((r7 & 1) == 0) goto L5;
        r5 = "*";
    L5:
        Intrinsics.checkNotNullParameter(r4, "<this>");
        Intrinsics.checkNotNullParameter(r5, "glob");
        Intrinsics.checkNotNullParameter(r6, "block");
        DirectoryStream<Path> r9 = Files.newDirectoryStream(r4, r5);
        DirectoryStream<Path> r1 = r9;     // Catch: Throwable -> L13
        Intrinsics.checkNotNull(r1);     // Catch: Throwable -> L13
        Object r3 = r6.invoke(CollectionsKt.asSequence(r1));     // Catch: Throwable -> L13
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == false) goto L10;
        CloseableKt.closeFinally(r9, null);
    L12:
        return r3;
    L10:
        if (r9 == null) goto L12;
        r9.close();
    L13:
        th = move-exception;
        throw th;     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        if (PlatformImplementationsKt.apiVersionIsAtLeast(1, 1, 0) == true) goto L23;
        if (r9 != null) goto L27;
    L24:
        throw th;
    L27:
        r9.close();     // Catch: Throwable -> L21
        goto L24
    L23:
        CloseableKt.closeFinally(r9, th);
        goto L24
    }

    public static final void visitFileTree(Path r1, int r2, boolean r3, Function1<? super FileVisitorBuilder, Unit> r4) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r4, "builderAction");
        PathsKt.visitFileTree(r1, PathsKt.fileVisitor(r4), r2, r3);
    }

    public static final void visitFileTree(Path r1, FileVisitor<Path> r2, int r3, boolean r4) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "visitor");
        if (r4 == false) goto L5;
        Set r0 = SetsKt.setOf(FileVisitOption.FOLLOW_LINKS);
    L6:
        Files.walkFileTree(r1, r0, r3, r2);
        return;
    L5:
        r0 = SetsKt.emptySet();
        goto L6
    }

    public static /* synthetic */ void visitFileTree$default(Path r0, int r1, boolean r2, Function1 r3, int r4, Object r5) {
        if ((r4 & 1) == 0) goto L6;
        r1 = Integer.MAX_VALUE;
    L6:
        if ((r4 & 2) == 0) goto L8;
        r2 = false;
    L8:
        PathsKt.visitFileTree(r0, r1, r2, r3);
    }

    public static /* synthetic */ void visitFileTree$default(Path r0, FileVisitor r1, int r2, boolean r3, int r4, Object r5) {
        if ((r4 & 2) == 0) goto L6;
        r2 = Integer.MAX_VALUE;
    L6:
        if ((r4 & 4) == 0) goto L8;
        r3 = false;
    L8:
        PathsKt.visitFileTree(r0, r1, r2, r3);
    }

    public static final Sequence<Path> walk(Path r1, PathWalkOption... r2) {
        Intrinsics.checkNotNullParameter(r1, "<this>");
        Intrinsics.checkNotNullParameter(r2, "options");
        return new PathTreeWalk(r1, r2);
    }
}
