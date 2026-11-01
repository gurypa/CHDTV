package androidx.exifinterface.media;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.system.OsConstants;
import android.util.Log;
import android.util.Pair;
import androidx.core.view.InputDeviceCompat;
import androidx.media3.common.MimeTypes;
import androidx.media3.exoplayer.Renderer;
import androidx.media3.extractor.metadata.icy.IcyHeaders;
import com.google.common.base.Ascii;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

/* JADX INFO: loaded from: classes.dex */
public class ExifInterface {
    public static final short ALTITUDE_ABOVE_SEA_LEVEL = 0;
    public static final short ALTITUDE_BELOW_SEA_LEVEL = 1;
    static final Charset ASCII = null;
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_1 = null;
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_2 = null;
    public static final int[] BITS_PER_SAMPLE_RGB = null;
    static final short BYTE_ALIGN_II = 18761;
    static final short BYTE_ALIGN_MM = 19789;
    public static final int COLOR_SPACE_S_RGB = 1;
    public static final int COLOR_SPACE_UNCALIBRATED = 65535;
    public static final short CONTRAST_HARD = 2;
    public static final short CONTRAST_NORMAL = 0;
    public static final short CONTRAST_SOFT = 1;
    public static final int DATA_DEFLATE_ZIP = 8;
    public static final int DATA_HUFFMAN_COMPRESSED = 2;
    public static final int DATA_JPEG = 6;
    public static final int DATA_JPEG_COMPRESSED = 7;
    public static final int DATA_LOSSY_JPEG = 34892;
    public static final int DATA_PACK_BITS_COMPRESSED = 32773;
    public static final int DATA_UNCOMPRESSED = 1;
    private static final Pattern DATETIME_PRIMARY_FORMAT_PATTERN = null;
    private static final Pattern DATETIME_SECONDARY_FORMAT_PATTERN = null;
    private static final int DATETIME_VALUE_STRING_LENGTH = 19;
    private static final boolean DEBUG = false;
    static final byte[] EXIF_ASCII_PREFIX = null;
    private static final ExifTag[] EXIF_POINTER_TAGS = null;
    static final ExifTag[][] EXIF_TAGS = null;
    public static final short EXPOSURE_MODE_AUTO = 0;
    public static final short EXPOSURE_MODE_AUTO_BRACKET = 2;
    public static final short EXPOSURE_MODE_MANUAL = 1;
    public static final short EXPOSURE_PROGRAM_ACTION = 6;
    public static final short EXPOSURE_PROGRAM_APERTURE_PRIORITY = 3;
    public static final short EXPOSURE_PROGRAM_CREATIVE = 5;
    public static final short EXPOSURE_PROGRAM_LANDSCAPE_MODE = 8;
    public static final short EXPOSURE_PROGRAM_MANUAL = 1;
    public static final short EXPOSURE_PROGRAM_NORMAL = 2;
    public static final short EXPOSURE_PROGRAM_NOT_DEFINED = 0;
    public static final short EXPOSURE_PROGRAM_PORTRAIT_MODE = 7;
    public static final short EXPOSURE_PROGRAM_SHUTTER_PRIORITY = 4;
    public static final short FILE_SOURCE_DSC = 3;
    public static final short FILE_SOURCE_OTHER = 0;
    public static final short FILE_SOURCE_REFLEX_SCANNER = 2;
    public static final short FILE_SOURCE_TRANSPARENT_SCANNER = 1;
    public static final short FLAG_FLASH_FIRED = 1;
    public static final short FLAG_FLASH_MODE_AUTO = 24;
    public static final short FLAG_FLASH_MODE_COMPULSORY_FIRING = 8;
    public static final short FLAG_FLASH_MODE_COMPULSORY_SUPPRESSION = 16;
    public static final short FLAG_FLASH_NO_FLASH_FUNCTION = 32;
    public static final short FLAG_FLASH_RED_EYE_SUPPORTED = 64;
    public static final short FLAG_FLASH_RETURN_LIGHT_DETECTED = 6;
    public static final short FLAG_FLASH_RETURN_LIGHT_NOT_DETECTED = 4;
    private static final List<Integer> FLIPPED_ROTATION_ORDER = null;
    public static final short FORMAT_CHUNKY = 1;
    public static final short FORMAT_PLANAR = 2;
    public static final short GAIN_CONTROL_HIGH_GAIN_DOWN = 4;
    public static final short GAIN_CONTROL_HIGH_GAIN_UP = 2;
    public static final short GAIN_CONTROL_LOW_GAIN_DOWN = 3;
    public static final short GAIN_CONTROL_LOW_GAIN_UP = 1;
    public static final short GAIN_CONTROL_NONE = 0;
    public static final String GPS_DIRECTION_MAGNETIC = "M";
    public static final String GPS_DIRECTION_TRUE = "T";
    public static final String GPS_DISTANCE_KILOMETERS = "K";
    public static final String GPS_DISTANCE_MILES = "M";
    public static final String GPS_DISTANCE_NAUTICAL_MILES = "N";
    public static final String GPS_MEASUREMENT_2D = "2";
    public static final String GPS_MEASUREMENT_3D = "3";
    public static final short GPS_MEASUREMENT_DIFFERENTIAL_CORRECTED = 1;
    public static final String GPS_MEASUREMENT_INTERRUPTED = "V";
    public static final String GPS_MEASUREMENT_IN_PROGRESS = "A";
    public static final short GPS_MEASUREMENT_NO_DIFFERENTIAL = 0;
    public static final String GPS_SPEED_KILOMETERS_PER_HOUR = "K";
    public static final String GPS_SPEED_KNOTS = "N";
    public static final String GPS_SPEED_MILES_PER_HOUR = "M";
    private static final Pattern GPS_TIMESTAMP_PATTERN = null;
    private static final byte[] HEIF_BRAND_HEIC = null;
    private static final byte[] HEIF_BRAND_MIF1 = null;
    private static final byte[] HEIF_TYPE_FTYP = null;
    static final byte[] IDENTIFIER_EXIF_APP1 = null;
    private static final byte[] IDENTIFIER_XMP_APP1 = null;
    private static final ExifTag[] IFD_EXIF_TAGS = null;
    private static final int IFD_FORMAT_BYTE = 1;
    static final int[] IFD_FORMAT_BYTES_PER_FORMAT = null;
    private static final int IFD_FORMAT_DOUBLE = 12;
    private static final int IFD_FORMAT_IFD = 13;
    static final String[] IFD_FORMAT_NAMES = null;
    private static final int IFD_FORMAT_SBYTE = 6;
    private static final int IFD_FORMAT_SINGLE = 11;
    private static final int IFD_FORMAT_SLONG = 9;
    private static final int IFD_FORMAT_SRATIONAL = 10;
    private static final int IFD_FORMAT_SSHORT = 8;
    private static final int IFD_FORMAT_STRING = 2;
    private static final int IFD_FORMAT_ULONG = 4;
    private static final int IFD_FORMAT_UNDEFINED = 7;
    private static final int IFD_FORMAT_URATIONAL = 5;
    private static final int IFD_FORMAT_USHORT = 3;
    private static final ExifTag[] IFD_GPS_TAGS = null;
    private static final ExifTag[] IFD_INTEROPERABILITY_TAGS = null;
    private static final int IFD_OFFSET = 8;
    private static final ExifTag[] IFD_THUMBNAIL_TAGS = null;
    private static final ExifTag[] IFD_TIFF_TAGS = null;
    private static final int IFD_TYPE_EXIF = 1;
    private static final int IFD_TYPE_GPS = 2;
    private static final int IFD_TYPE_INTEROPERABILITY = 3;
    private static final int IFD_TYPE_ORF_CAMERA_SETTINGS = 7;
    private static final int IFD_TYPE_ORF_IMAGE_PROCESSING = 8;
    private static final int IFD_TYPE_ORF_MAKER_NOTE = 6;
    private static final int IFD_TYPE_PEF = 9;
    static final int IFD_TYPE_PREVIEW = 5;
    static final int IFD_TYPE_PRIMARY = 0;
    static final int IFD_TYPE_THUMBNAIL = 4;
    static final int IMAGE_TYPE_ARW = 1;
    static final int IMAGE_TYPE_CR2 = 2;
    static final int IMAGE_TYPE_DNG = 3;
    static final int IMAGE_TYPE_HEIF = 12;
    static final int IMAGE_TYPE_JPEG = 4;
    static final int IMAGE_TYPE_NEF = 5;
    static final int IMAGE_TYPE_NRW = 6;
    static final int IMAGE_TYPE_ORF = 7;
    static final int IMAGE_TYPE_PEF = 8;
    static final int IMAGE_TYPE_PNG = 13;
    static final int IMAGE_TYPE_RAF = 9;
    static final int IMAGE_TYPE_RW2 = 10;
    static final int IMAGE_TYPE_SRW = 11;
    static final int IMAGE_TYPE_UNKNOWN = 0;
    static final int IMAGE_TYPE_WEBP = 14;
    static final byte[] JPEG_SIGNATURE = null;
    public static final String LATITUDE_NORTH = "N";
    public static final String LATITUDE_SOUTH = "S";
    public static final short LIGHT_SOURCE_CLOUDY_WEATHER = 10;
    public static final short LIGHT_SOURCE_COOL_WHITE_FLUORESCENT = 14;
    public static final short LIGHT_SOURCE_D50 = 23;
    public static final short LIGHT_SOURCE_D55 = 20;
    public static final short LIGHT_SOURCE_D65 = 21;
    public static final short LIGHT_SOURCE_D75 = 22;
    public static final short LIGHT_SOURCE_DAYLIGHT = 1;
    public static final short LIGHT_SOURCE_DAYLIGHT_FLUORESCENT = 12;
    public static final short LIGHT_SOURCE_DAY_WHITE_FLUORESCENT = 13;
    public static final short LIGHT_SOURCE_FINE_WEATHER = 9;
    public static final short LIGHT_SOURCE_FLASH = 4;
    public static final short LIGHT_SOURCE_FLUORESCENT = 2;
    public static final short LIGHT_SOURCE_ISO_STUDIO_TUNGSTEN = 24;
    public static final short LIGHT_SOURCE_OTHER = 255;
    public static final short LIGHT_SOURCE_SHADE = 11;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_A = 17;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_B = 18;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_C = 19;
    public static final short LIGHT_SOURCE_TUNGSTEN = 3;
    public static final short LIGHT_SOURCE_UNKNOWN = 0;
    public static final short LIGHT_SOURCE_WARM_WHITE_FLUORESCENT = 16;
    public static final short LIGHT_SOURCE_WHITE_FLUORESCENT = 15;
    public static final String LONGITUDE_EAST = "E";
    public static final String LONGITUDE_WEST = "W";
    static final byte MARKER = -1;
    static final byte MARKER_APP1 = -31;
    private static final byte MARKER_COM = -2;
    static final byte MARKER_EOI = -39;
    private static final byte MARKER_SOF0 = -64;
    private static final byte MARKER_SOF1 = -63;
    private static final byte MARKER_SOF10 = -54;
    private static final byte MARKER_SOF11 = -53;
    private static final byte MARKER_SOF13 = -51;
    private static final byte MARKER_SOF14 = -50;
    private static final byte MARKER_SOF15 = -49;
    private static final byte MARKER_SOF2 = -62;
    private static final byte MARKER_SOF3 = -61;
    private static final byte MARKER_SOF5 = -59;
    private static final byte MARKER_SOF6 = -58;
    private static final byte MARKER_SOF7 = -57;
    private static final byte MARKER_SOF9 = -55;
    private static final byte MARKER_SOI = -40;
    private static final byte MARKER_SOS = -38;
    private static final int MAX_THUMBNAIL_SIZE = 512;
    public static final short METERING_MODE_AVERAGE = 1;
    public static final short METERING_MODE_CENTER_WEIGHT_AVERAGE = 2;
    public static final short METERING_MODE_MULTI_SPOT = 4;
    public static final short METERING_MODE_OTHER = 255;
    public static final short METERING_MODE_PARTIAL = 6;
    public static final short METERING_MODE_PATTERN = 5;
    public static final short METERING_MODE_SPOT = 3;
    public static final short METERING_MODE_UNKNOWN = 0;
    private static final Pattern NON_ZERO_TIME_PATTERN = null;
    private static final ExifTag[] ORF_CAMERA_SETTINGS_TAGS = null;
    private static final ExifTag[] ORF_IMAGE_PROCESSING_TAGS = null;
    private static final byte[] ORF_MAKER_NOTE_HEADER_1 = null;
    private static final int ORF_MAKER_NOTE_HEADER_1_SIZE = 8;
    private static final byte[] ORF_MAKER_NOTE_HEADER_2 = null;
    private static final int ORF_MAKER_NOTE_HEADER_2_SIZE = 12;
    private static final ExifTag[] ORF_MAKER_NOTE_TAGS = null;
    private static final short ORF_SIGNATURE_1 = 20306;
    private static final short ORF_SIGNATURE_2 = 21330;
    public static final int ORIENTATION_FLIP_HORIZONTAL = 2;
    public static final int ORIENTATION_FLIP_VERTICAL = 4;
    public static final int ORIENTATION_NORMAL = 1;
    public static final int ORIENTATION_ROTATE_180 = 3;
    public static final int ORIENTATION_ROTATE_270 = 8;
    public static final int ORIENTATION_ROTATE_90 = 6;
    public static final int ORIENTATION_TRANSPOSE = 5;
    public static final int ORIENTATION_TRANSVERSE = 7;
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final int ORIGINAL_RESOLUTION_IMAGE = 0;
    private static final int PEF_MAKER_NOTE_SKIP_SIZE = 6;
    private static final String PEF_SIGNATURE = "PENTAX";
    private static final ExifTag[] PEF_TAGS = null;
    public static final int PHOTOMETRIC_INTERPRETATION_BLACK_IS_ZERO = 1;
    public static final int PHOTOMETRIC_INTERPRETATION_RGB = 2;
    public static final int PHOTOMETRIC_INTERPRETATION_WHITE_IS_ZERO = 0;
    public static final int PHOTOMETRIC_INTERPRETATION_YCBCR = 6;
    private static final int PNG_CHUNK_CRC_BYTE_LENGTH = 4;
    private static final int PNG_CHUNK_TYPE_BYTE_LENGTH = 4;
    private static final byte[] PNG_CHUNK_TYPE_EXIF = null;
    private static final byte[] PNG_CHUNK_TYPE_IEND = null;
    private static final byte[] PNG_CHUNK_TYPE_IHDR = null;
    private static final byte[] PNG_SIGNATURE = null;
    private static final int RAF_OFFSET_TO_JPEG_IMAGE_OFFSET = 84;
    private static final String RAF_SIGNATURE = "FUJIFILMCCD-RAW";
    public static final int REDUCED_RESOLUTION_IMAGE = 1;
    public static final short RENDERED_PROCESS_CUSTOM = 1;
    public static final short RENDERED_PROCESS_NORMAL = 0;
    public static final short RESOLUTION_UNIT_CENTIMETERS = 3;
    public static final short RESOLUTION_UNIT_INCHES = 2;
    private static final List<Integer> ROTATION_ORDER = null;
    private static final short RW2_SIGNATURE = 85;
    public static final short SATURATION_HIGH = 0;
    public static final short SATURATION_LOW = 0;
    public static final short SATURATION_NORMAL = 0;
    public static final short SCENE_CAPTURE_TYPE_LANDSCAPE = 1;
    public static final short SCENE_CAPTURE_TYPE_NIGHT = 3;
    public static final short SCENE_CAPTURE_TYPE_PORTRAIT = 2;
    public static final short SCENE_CAPTURE_TYPE_STANDARD = 0;
    public static final short SCENE_TYPE_DIRECTLY_PHOTOGRAPHED = 1;
    public static final short SENSITIVITY_TYPE_ISO_SPEED = 3;
    public static final short SENSITIVITY_TYPE_REI = 2;
    public static final short SENSITIVITY_TYPE_REI_AND_ISO = 6;
    public static final short SENSITIVITY_TYPE_SOS = 1;
    public static final short SENSITIVITY_TYPE_SOS_AND_ISO = 5;
    public static final short SENSITIVITY_TYPE_SOS_AND_REI = 4;
    public static final short SENSITIVITY_TYPE_SOS_AND_REI_AND_ISO = 7;
    public static final short SENSITIVITY_TYPE_UNKNOWN = 0;
    public static final short SENSOR_TYPE_COLOR_SEQUENTIAL = 5;
    public static final short SENSOR_TYPE_COLOR_SEQUENTIAL_LINEAR = 8;
    public static final short SENSOR_TYPE_NOT_DEFINED = 1;
    public static final short SENSOR_TYPE_ONE_CHIP = 2;
    public static final short SENSOR_TYPE_THREE_CHIP = 4;
    public static final short SENSOR_TYPE_TRILINEAR = 7;
    public static final short SENSOR_TYPE_TWO_CHIP = 3;
    public static final short SHARPNESS_HARD = 2;
    public static final short SHARPNESS_NORMAL = 0;
    public static final short SHARPNESS_SOFT = 1;
    private static final int SIGNATURE_CHECK_SIZE = 5000;
    private static final int SKIP_BUFFER_SIZE = 8192;
    static final byte START_CODE = 42;
    public static final int STREAM_TYPE_EXIF_DATA_ONLY = 1;
    public static final int STREAM_TYPE_FULL_IMAGE_DATA = 0;
    public static final short SUBJECT_DISTANCE_RANGE_CLOSE_VIEW = 2;
    public static final short SUBJECT_DISTANCE_RANGE_DISTANT_VIEW = 3;
    public static final short SUBJECT_DISTANCE_RANGE_MACRO = 1;
    public static final short SUBJECT_DISTANCE_RANGE_UNKNOWN = 0;
    private static final String TAG = "ExifInterface";
    public static final String TAG_APERTURE_VALUE = "ApertureValue";
    public static final String TAG_ARTIST = "Artist";
    public static final String TAG_BITS_PER_SAMPLE = "BitsPerSample";
    public static final String TAG_BODY_SERIAL_NUMBER = "BodySerialNumber";
    public static final String TAG_BRIGHTNESS_VALUE = "BrightnessValue";

    @Deprecated
    public static final String TAG_CAMARA_OWNER_NAME = "CameraOwnerName";
    public static final String TAG_CAMERA_OWNER_NAME = "CameraOwnerName";
    public static final String TAG_CFA_PATTERN = "CFAPattern";
    public static final String TAG_COLOR_SPACE = "ColorSpace";
    public static final String TAG_COMPONENTS_CONFIGURATION = "ComponentsConfiguration";
    public static final String TAG_COMPRESSED_BITS_PER_PIXEL = "CompressedBitsPerPixel";
    public static final String TAG_COMPRESSION = "Compression";
    public static final String TAG_CONTRAST = "Contrast";
    public static final String TAG_COPYRIGHT = "Copyright";
    public static final String TAG_CUSTOM_RENDERED = "CustomRendered";
    public static final String TAG_DATETIME = "DateTime";
    public static final String TAG_DATETIME_DIGITIZED = "DateTimeDigitized";
    public static final String TAG_DATETIME_ORIGINAL = "DateTimeOriginal";
    public static final String TAG_DEFAULT_CROP_SIZE = "DefaultCropSize";
    public static final String TAG_DEVICE_SETTING_DESCRIPTION = "DeviceSettingDescription";
    public static final String TAG_DIGITAL_ZOOM_RATIO = "DigitalZoomRatio";
    public static final String TAG_DNG_VERSION = "DNGVersion";
    private static final String TAG_EXIF_IFD_POINTER = "ExifIFDPointer";
    public static final String TAG_EXIF_VERSION = "ExifVersion";
    public static final String TAG_EXPOSURE_BIAS_VALUE = "ExposureBiasValue";
    public static final String TAG_EXPOSURE_INDEX = "ExposureIndex";
    public static final String TAG_EXPOSURE_MODE = "ExposureMode";
    public static final String TAG_EXPOSURE_PROGRAM = "ExposureProgram";
    public static final String TAG_EXPOSURE_TIME = "ExposureTime";
    public static final String TAG_FILE_SOURCE = "FileSource";
    public static final String TAG_FLASH = "Flash";
    public static final String TAG_FLASHPIX_VERSION = "FlashpixVersion";
    public static final String TAG_FLASH_ENERGY = "FlashEnergy";
    public static final String TAG_FOCAL_LENGTH = "FocalLength";
    public static final String TAG_FOCAL_LENGTH_IN_35MM_FILM = "FocalLengthIn35mmFilm";
    public static final String TAG_FOCAL_PLANE_RESOLUTION_UNIT = "FocalPlaneResolutionUnit";
    public static final String TAG_FOCAL_PLANE_X_RESOLUTION = "FocalPlaneXResolution";
    public static final String TAG_FOCAL_PLANE_Y_RESOLUTION = "FocalPlaneYResolution";
    public static final String TAG_F_NUMBER = "FNumber";
    public static final String TAG_GAIN_CONTROL = "GainControl";
    public static final String TAG_GAMMA = "Gamma";
    public static final String TAG_GPS_ALTITUDE = "GPSAltitude";
    public static final String TAG_GPS_ALTITUDE_REF = "GPSAltitudeRef";
    public static final String TAG_GPS_AREA_INFORMATION = "GPSAreaInformation";
    public static final String TAG_GPS_DATESTAMP = "GPSDateStamp";
    public static final String TAG_GPS_DEST_BEARING = "GPSDestBearing";
    public static final String TAG_GPS_DEST_BEARING_REF = "GPSDestBearingRef";
    public static final String TAG_GPS_DEST_DISTANCE = "GPSDestDistance";
    public static final String TAG_GPS_DEST_DISTANCE_REF = "GPSDestDistanceRef";
    public static final String TAG_GPS_DEST_LATITUDE = "GPSDestLatitude";
    public static final String TAG_GPS_DEST_LATITUDE_REF = "GPSDestLatitudeRef";
    public static final String TAG_GPS_DEST_LONGITUDE = "GPSDestLongitude";
    public static final String TAG_GPS_DEST_LONGITUDE_REF = "GPSDestLongitudeRef";
    public static final String TAG_GPS_DIFFERENTIAL = "GPSDifferential";
    public static final String TAG_GPS_DOP = "GPSDOP";
    public static final String TAG_GPS_H_POSITIONING_ERROR = "GPSHPositioningError";
    public static final String TAG_GPS_IMG_DIRECTION = "GPSImgDirection";
    public static final String TAG_GPS_IMG_DIRECTION_REF = "GPSImgDirectionRef";
    private static final String TAG_GPS_INFO_IFD_POINTER = "GPSInfoIFDPointer";
    public static final String TAG_GPS_LATITUDE = "GPSLatitude";
    public static final String TAG_GPS_LATITUDE_REF = "GPSLatitudeRef";
    public static final String TAG_GPS_LONGITUDE = "GPSLongitude";
    public static final String TAG_GPS_LONGITUDE_REF = "GPSLongitudeRef";
    public static final String TAG_GPS_MAP_DATUM = "GPSMapDatum";
    public static final String TAG_GPS_MEASURE_MODE = "GPSMeasureMode";
    public static final String TAG_GPS_PROCESSING_METHOD = "GPSProcessingMethod";
    public static final String TAG_GPS_SATELLITES = "GPSSatellites";
    public static final String TAG_GPS_SPEED = "GPSSpeed";
    public static final String TAG_GPS_SPEED_REF = "GPSSpeedRef";
    public static final String TAG_GPS_STATUS = "GPSStatus";
    public static final String TAG_GPS_TIMESTAMP = "GPSTimeStamp";
    public static final String TAG_GPS_TRACK = "GPSTrack";
    public static final String TAG_GPS_TRACK_REF = "GPSTrackRef";
    public static final String TAG_GPS_VERSION_ID = "GPSVersionID";
    public static final String TAG_IMAGE_DESCRIPTION = "ImageDescription";
    public static final String TAG_IMAGE_LENGTH = "ImageLength";
    public static final String TAG_IMAGE_UNIQUE_ID = "ImageUniqueID";
    public static final String TAG_IMAGE_WIDTH = "ImageWidth";
    private static final String TAG_INTEROPERABILITY_IFD_POINTER = "InteroperabilityIFDPointer";
    public static final String TAG_INTEROPERABILITY_INDEX = "InteroperabilityIndex";
    public static final String TAG_ISO_SPEED = "ISOSpeed";
    public static final String TAG_ISO_SPEED_LATITUDE_YYY = "ISOSpeedLatitudeyyy";
    public static final String TAG_ISO_SPEED_LATITUDE_ZZZ = "ISOSpeedLatitudezzz";

    @Deprecated
    public static final String TAG_ISO_SPEED_RATINGS = "ISOSpeedRatings";
    public static final String TAG_JPEG_INTERCHANGE_FORMAT = "JPEGInterchangeFormat";
    public static final String TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = "JPEGInterchangeFormatLength";
    public static final String TAG_LENS_MAKE = "LensMake";
    public static final String TAG_LENS_MODEL = "LensModel";
    public static final String TAG_LENS_SERIAL_NUMBER = "LensSerialNumber";
    public static final String TAG_LENS_SPECIFICATION = "LensSpecification";
    public static final String TAG_LIGHT_SOURCE = "LightSource";
    public static final String TAG_MAKE = "Make";
    public static final String TAG_MAKER_NOTE = "MakerNote";
    public static final String TAG_MAX_APERTURE_VALUE = "MaxApertureValue";
    public static final String TAG_METERING_MODE = "MeteringMode";
    public static final String TAG_MODEL = "Model";
    public static final String TAG_NEW_SUBFILE_TYPE = "NewSubfileType";
    public static final String TAG_OECF = "OECF";
    public static final String TAG_OFFSET_TIME = "OffsetTime";
    public static final String TAG_OFFSET_TIME_DIGITIZED = "OffsetTimeDigitized";
    public static final String TAG_OFFSET_TIME_ORIGINAL = "OffsetTimeOriginal";
    public static final String TAG_ORF_ASPECT_FRAME = "AspectFrame";
    private static final String TAG_ORF_CAMERA_SETTINGS_IFD_POINTER = "CameraSettingsIFDPointer";
    private static final String TAG_ORF_IMAGE_PROCESSING_IFD_POINTER = "ImageProcessingIFDPointer";
    public static final String TAG_ORF_PREVIEW_IMAGE_LENGTH = "PreviewImageLength";
    public static final String TAG_ORF_PREVIEW_IMAGE_START = "PreviewImageStart";
    public static final String TAG_ORF_THUMBNAIL_IMAGE = "ThumbnailImage";
    public static final String TAG_ORIENTATION = "Orientation";
    public static final String TAG_PHOTOGRAPHIC_SENSITIVITY = "PhotographicSensitivity";
    public static final String TAG_PHOTOMETRIC_INTERPRETATION = "PhotometricInterpretation";
    public static final String TAG_PIXEL_X_DIMENSION = "PixelXDimension";
    public static final String TAG_PIXEL_Y_DIMENSION = "PixelYDimension";
    public static final String TAG_PLANAR_CONFIGURATION = "PlanarConfiguration";
    public static final String TAG_PRIMARY_CHROMATICITIES = "PrimaryChromaticities";
    private static final ExifTag TAG_RAF_IMAGE_SIZE = null;
    public static final String TAG_RECOMMENDED_EXPOSURE_INDEX = "RecommendedExposureIndex";
    public static final String TAG_REFERENCE_BLACK_WHITE = "ReferenceBlackWhite";
    public static final String TAG_RELATED_SOUND_FILE = "RelatedSoundFile";
    public static final String TAG_RESOLUTION_UNIT = "ResolutionUnit";
    public static final String TAG_ROWS_PER_STRIP = "RowsPerStrip";
    public static final String TAG_RW2_ISO = "ISO";
    public static final String TAG_RW2_JPG_FROM_RAW = "JpgFromRaw";
    public static final String TAG_RW2_SENSOR_BOTTOM_BORDER = "SensorBottomBorder";
    public static final String TAG_RW2_SENSOR_LEFT_BORDER = "SensorLeftBorder";
    public static final String TAG_RW2_SENSOR_RIGHT_BORDER = "SensorRightBorder";
    public static final String TAG_RW2_SENSOR_TOP_BORDER = "SensorTopBorder";
    public static final String TAG_SAMPLES_PER_PIXEL = "SamplesPerPixel";
    public static final String TAG_SATURATION = "Saturation";
    public static final String TAG_SCENE_CAPTURE_TYPE = "SceneCaptureType";
    public static final String TAG_SCENE_TYPE = "SceneType";
    public static final String TAG_SENSING_METHOD = "SensingMethod";
    public static final String TAG_SENSITIVITY_TYPE = "SensitivityType";
    public static final String TAG_SHARPNESS = "Sharpness";
    public static final String TAG_SHUTTER_SPEED_VALUE = "ShutterSpeedValue";
    public static final String TAG_SOFTWARE = "Software";
    public static final String TAG_SPATIAL_FREQUENCY_RESPONSE = "SpatialFrequencyResponse";
    public static final String TAG_SPECTRAL_SENSITIVITY = "SpectralSensitivity";
    public static final String TAG_STANDARD_OUTPUT_SENSITIVITY = "StandardOutputSensitivity";
    public static final String TAG_STRIP_BYTE_COUNTS = "StripByteCounts";
    public static final String TAG_STRIP_OFFSETS = "StripOffsets";
    public static final String TAG_SUBFILE_TYPE = "SubfileType";
    public static final String TAG_SUBJECT_AREA = "SubjectArea";
    public static final String TAG_SUBJECT_DISTANCE = "SubjectDistance";
    public static final String TAG_SUBJECT_DISTANCE_RANGE = "SubjectDistanceRange";
    public static final String TAG_SUBJECT_LOCATION = "SubjectLocation";
    public static final String TAG_SUBSEC_TIME = "SubSecTime";
    public static final String TAG_SUBSEC_TIME_DIGITIZED = "SubSecTimeDigitized";
    public static final String TAG_SUBSEC_TIME_ORIGINAL = "SubSecTimeOriginal";
    private static final String TAG_SUB_IFD_POINTER = "SubIFDPointer";
    public static final String TAG_THUMBNAIL_IMAGE_LENGTH = "ThumbnailImageLength";
    public static final String TAG_THUMBNAIL_IMAGE_WIDTH = "ThumbnailImageWidth";
    public static final String TAG_THUMBNAIL_ORIENTATION = "ThumbnailOrientation";
    public static final String TAG_TRANSFER_FUNCTION = "TransferFunction";
    public static final String TAG_USER_COMMENT = "UserComment";
    public static final String TAG_WHITE_BALANCE = "WhiteBalance";
    public static final String TAG_WHITE_POINT = "WhitePoint";
    public static final String TAG_XMP = "Xmp";
    public static final String TAG_X_RESOLUTION = "XResolution";
    public static final String TAG_Y_CB_CR_COEFFICIENTS = "YCbCrCoefficients";
    public static final String TAG_Y_CB_CR_POSITIONING = "YCbCrPositioning";
    public static final String TAG_Y_CB_CR_SUB_SAMPLING = "YCbCrSubSampling";
    public static final String TAG_Y_RESOLUTION = "YResolution";
    private static final int WEBP_CHUNK_SIZE_BYTE_LENGTH = 4;
    private static final byte[] WEBP_CHUNK_TYPE_ANIM = null;
    private static final byte[] WEBP_CHUNK_TYPE_ANMF = null;
    private static final int WEBP_CHUNK_TYPE_BYTE_LENGTH = 4;
    private static final byte[] WEBP_CHUNK_TYPE_EXIF = null;
    private static final byte[] WEBP_CHUNK_TYPE_VP8 = null;
    private static final byte[] WEBP_CHUNK_TYPE_VP8L = null;
    private static final byte[] WEBP_CHUNK_TYPE_VP8X = null;
    private static final int WEBP_CHUNK_TYPE_VP8X_DEFAULT_LENGTH = 10;
    private static final int WEBP_FILE_SIZE_BYTE_LENGTH = 4;
    private static final byte[] WEBP_SIGNATURE_1 = null;
    private static final byte[] WEBP_SIGNATURE_2 = null;
    private static final byte WEBP_VP8L_SIGNATURE = 47;
    private static final byte[] WEBP_VP8_SIGNATURE = null;

    @Deprecated
    public static final int WHITEBALANCE_AUTO = 0;

    @Deprecated
    public static final int WHITEBALANCE_MANUAL = 1;
    public static final short WHITE_BALANCE_AUTO = 0;
    public static final short WHITE_BALANCE_MANUAL = 1;
    public static final short Y_CB_CR_POSITIONING_CENTERED = 1;
    public static final short Y_CB_CR_POSITIONING_CO_SITED = 2;
    private static final HashMap<Integer, Integer> sExifPointerTagMap = null;
    private static final HashMap<Integer, ExifTag>[] sExifTagMapsForReading = null;
    private static final HashMap<String, ExifTag>[] sExifTagMapsForWriting = null;
    private static SimpleDateFormat sFormatterPrimary;
    private static SimpleDateFormat sFormatterSecondary;
    private static final HashSet<String> sTagSetForCompatibility = null;
    private boolean mAreThumbnailStripsConsecutive;
    private AssetManager.AssetInputStream mAssetInputStream;
    private final HashMap<String, ExifAttribute>[] mAttributes;
    private Set<Integer> mAttributesOffsets;
    private ByteOrder mExifByteOrder;
    private String mFilename;
    private boolean mHasThumbnail;
    private boolean mHasThumbnailStrips;
    private boolean mIsExifDataOnly;
    private int mMimeType;
    private boolean mModified;
    private int mOffsetToExifData;
    private int mOrfMakerNoteOffset;
    private int mOrfThumbnailLength;
    private int mOrfThumbnailOffset;
    private FileDescriptor mSeekableFileDescriptor;
    private byte[] mThumbnailBytes;
    private int mThumbnailCompression;
    private int mThumbnailLength;
    private int mThumbnailOffset;
    private boolean mXmpIsFromSeparateMarker;

    private static class ByteOrderedDataInputStream extends InputStream implements DataInput {
        private static final ByteOrder BIG_ENDIAN = null;
        private static final ByteOrder LITTLE_ENDIAN = null;
        private ByteOrder mByteOrder;
        final DataInputStream mDataInputStream;
        int mPosition;
        private byte[] mSkipBuffer;

        static {
            LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
            BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
        }

        ByteOrderedDataInputStream(InputStream r2) throws IOException {
            this(r2, ByteOrder.BIG_ENDIAN);
        }

        ByteOrderedDataInputStream(InputStream r3, ByteOrder r4) throws IOException {
            this.mByteOrder = ByteOrder.BIG_ENDIAN;
            this.mDataInputStream = new DataInputStream(r3);
            this.mDataInputStream.mark(0);
            this.mPosition = 0;
            this.mByteOrder = r4;
        }

        ByteOrderedDataInputStream(byte[] r3) throws IOException {
            this(new ByteArrayInputStream(r3), ByteOrder.BIG_ENDIAN);
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.mDataInputStream.available();
        }

        @Override // java.io.InputStream
        public void mark(int r3) {
            throw new UnsupportedOperationException("Mark is currently unsupported");
        }

        public int position() {
            return this.mPosition;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.read();
        }

        @Override // java.io.InputStream
        public int read(byte[] r3, int r4, int r5) throws IOException {
            int r0 = this.mDataInputStream.read(r3, r4, r5);
            this.mPosition += r0;
            return r0;
        }

        @Override // java.io.DataInput
        public boolean readBoolean() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readBoolean();
        }

        @Override // java.io.DataInput
        public byte readByte() throws IOException {
            this.mPosition++;
            int r0 = this.mDataInputStream.read();
            if (r0 < 0) goto L7;
            return (byte) r0;
        L7:
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public char readChar() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readChar();
        }

        @Override // java.io.DataInput
        public double readDouble() throws IOException {
            return Double.longBitsToDouble(readLong());
        }

        @Override // java.io.DataInput
        public float readFloat() throws IOException {
            return Float.intBitsToFloat(readInt());
        }

        @Override // java.io.DataInput
        public void readFully(byte[] r3) throws IOException {
            this.mPosition += r3.length;
            this.mDataInputStream.readFully(r3);
        }

        @Override // java.io.DataInput
        public void readFully(byte[] r2, int r3, int r4) throws IOException {
            this.mPosition += r4;
            this.mDataInputStream.readFully(r2, r3, r4);
        }

        @Override // java.io.DataInput
        public int readInt() throws IOException {
            this.mPosition += 4;
            int r0 = this.mDataInputStream.read();
            int r1 = this.mDataInputStream.read();
            int r2 = this.mDataInputStream.read();
            int r3 = this.mDataInputStream.read();
            if ((((r0 | r1) | r2) | r3) < 0) goto L15;
            if (this.mByteOrder != LITTLE_ENDIAN) goto L9;
            return (((r3 << 24) + (r2 << 16)) + (r1 << 8)) + r0;
        L9:
            if (this.mByteOrder != BIG_ENDIAN) goto L13;
            return (((r0 << 24) + (r1 << 16)) + (r2 << 8)) + r3;
        L13:
            throw new IOException("Invalid byte order: " + this.mByteOrder);
        L15:
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public String readLine() throws IOException {
            Log.d(ExifInterface.TAG, "Currently unsupported");
            return null;
        }

        @Override // java.io.DataInput
        public long readLong() throws IOException {
            this.mPosition += 8;
            int r1 = this.mDataInputStream.read();
            int r3 = this.mDataInputStream.read();
            int r4 = this.mDataInputStream.read();
            int r5 = this.mDataInputStream.read();
            int r6 = this.mDataInputStream.read();
            int r7 = this.mDataInputStream.read();
            int r8 = this.mDataInputStream.read();
            int r9 = this.mDataInputStream.read();
            if ((((((((r1 | r3) | r4) | r5) | r6) | r7) | r8) | r9) < 0) goto L15;
            if (this.mByteOrder != LITTLE_ENDIAN) goto L9;
            return (((((((((long) r9) << 56) + (((long) r8) << 48)) + (((long) r7) << 40)) + (((long) r6) << 32)) + (((long) r5) << 24)) + (((long) r4) << 16)) + (((long) r3) << 8)) + ((long) r1);
        L9:
            if (this.mByteOrder != BIG_ENDIAN) goto L13;
            return (((((((((long) r1) << 56) + (((long) r3) << 48)) + (((long) r4) << 40)) + (((long) r5) << 32)) + (((long) r6) << 24)) + (((long) r7) << 16)) + (((long) r8) << 8)) + ((long) r9);
        L13:
            throw new IOException("Invalid byte order: " + this.mByteOrder);
        L15:
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public short readShort() throws IOException {
            this.mPosition += 2;
            int r0 = this.mDataInputStream.read();
            int r1 = this.mDataInputStream.read();
            if ((r0 | r1) < 0) goto L15;
            if (this.mByteOrder != LITTLE_ENDIAN) goto L9;
            return (short) ((r1 << 8) + r0);
        L9:
            if (this.mByteOrder != BIG_ENDIAN) goto L13;
            return (short) ((r0 << 8) + r1);
        L13:
            throw new IOException("Invalid byte order: " + this.mByteOrder);
        L15:
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public String readUTF() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readUTF();
        }

        @Override // java.io.DataInput
        public int readUnsignedByte() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readUnsignedByte();
        }

        public long readUnsignedInt() throws IOException {
            return ((long) readInt()) & 4294967295L;
        }

        @Override // java.io.DataInput
        public int readUnsignedShort() throws IOException {
            this.mPosition += 2;
            int r0 = this.mDataInputStream.read();
            int r1 = this.mDataInputStream.read();
            if ((r0 | r1) < 0) goto L15;
            if (this.mByteOrder != LITTLE_ENDIAN) goto L9;
            return (r1 << 8) + r0;
        L9:
            if (this.mByteOrder != BIG_ENDIAN) goto L13;
            return (r0 << 8) + r1;
        L13:
            throw new IOException("Invalid byte order: " + this.mByteOrder);
        L15:
            throw new EOFException();
        }

        @Override // java.io.InputStream
        public void reset() {
            throw new UnsupportedOperationException("Reset is currently unsupported");
        }

        public void setByteOrder(ByteOrder r1) {
            this.mByteOrder = r1;
        }

        @Override // java.io.DataInput
        public int skipBytes(int r3) throws IOException {
            throw new UnsupportedOperationException("skipBytes is currently unsupported");
        }

        public void skipFully(int r7) throws IOException {
            int r0 = 0;
        L3:
            if (r0 >= r7) goto L15;
            int r1 = (int) this.mDataInputStream.skip(r7 - r0);
            if (r1 > 0) goto L14;
            if (this.mSkipBuffer != null) goto L9;
            this.mSkipBuffer = new byte[8192];
        L9:
            int r3 = this.mDataInputStream.read(this.mSkipBuffer, 0, Math.min(8192, r7 - r0));
            r1 = r3;
            if (r3 != (-1)) goto L14;
            throw new EOFException("Reached EOF while skipping " + r7 + " bytes.");
        L14:
            r0 = r0 + r1;
            goto L3
        L15:
            this.mPosition += r0;
        }
    }

    private static class ByteOrderedDataOutputStream extends FilterOutputStream {
        private ByteOrder mByteOrder;
        final OutputStream mOutputStream;

        public ByteOrderedDataOutputStream(OutputStream r1, ByteOrder r2) {
            super(r1);
            this.mOutputStream = r1;
            this.mByteOrder = r2;
        }

        public void setByteOrder(ByteOrder r1) {
            this.mByteOrder = r1;
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(byte[] r2) throws IOException {
            this.mOutputStream.write(r2);
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(byte[] r2, int r3, int r4) throws IOException {
            this.mOutputStream.write(r2, r3, r4);
        }

        public void writeByte(int r2) throws IOException {
            this.mOutputStream.write(r2);
        }

        public void writeInt(int r3) throws IOException {
            if (this.mByteOrder != ByteOrder.LITTLE_ENDIAN) goto L6;
            this.mOutputStream.write((r3 >>> 0) & 255);
            this.mOutputStream.write((r3 >>> 8) & 255);
            this.mOutputStream.write((r3 >>> 16) & 255);
            this.mOutputStream.write((r3 >>> 24) & 255);
            return;
        L6:
            if (this.mByteOrder != ByteOrder.BIG_ENDIAN) goto L10;
            this.mOutputStream.write((r3 >>> 24) & 255);
            this.mOutputStream.write((r3 >>> 16) & 255);
            this.mOutputStream.write((r3 >>> 8) & 255);
            this.mOutputStream.write((r3 >>> 0) & 255);
            return;
        }

        public void writeShort(short r3) throws IOException {
            if (this.mByteOrder != ByteOrder.LITTLE_ENDIAN) goto L6;
            this.mOutputStream.write((r3 >>> 0) & 255);
            this.mOutputStream.write((r3 >>> 8) & 255);
            return;
        L6:
            if (this.mByteOrder != ByteOrder.BIG_ENDIAN) goto L10;
            this.mOutputStream.write((r3 >>> 8) & 255);
            this.mOutputStream.write((r3 >>> 0) & 255);
            return;
        }

        public void writeUnsignedInt(long r2) throws IOException {
            writeInt((int) r2);
        }

        public void writeUnsignedShort(int r2) throws IOException {
            writeShort((short) r2);
        }
    }

    private static class ExifAttribute {
        public static final long BYTES_OFFSET_UNKNOWN = -1;
        public final byte[] bytes;
        public final long bytesOffset;
        public final int format;
        public final int numberOfComponents;

        ExifAttribute(int r1, int r2, long r3, byte[] r5) {
            this.format = r1;
            this.numberOfComponents = r2;
            this.bytesOffset = r3;
            this.bytes = r5;
        }

        ExifAttribute(int r7, int r8, byte[] r9) {
            this(r7, r8, -1, r9);
        }

        public static ExifAttribute createByte(String r5) {
            if (r5.length() == 1) goto L5;
        L10:
            byte[] r0 = r5.getBytes(ExifInterface.ASCII);
            return new ExifAttribute(1, r0.length, r0);
        L5:
            if (r5.charAt(0) < '0') goto L10;
            if (r5.charAt(0) > '1') goto L10;
            byte[] r3 = {(byte) (r5.charAt(0) - '0')};
            return new ExifAttribute(1, r3.length, r3);
        }

        public static ExifAttribute createDouble(double r2, ByteOrder r4) {
            return createDouble(new double[]{r2}, r4);
        }

        public static ExifAttribute createDouble(double[] r6, ByteOrder r7) {
            ByteBuffer r0 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[12] * r6.length]);
            r0.order(r7);
            int r2 = r6.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L6;
            r0.putDouble(r6[r3]);
            r3 = r3 + 1;
            goto L3
        L6:
            return new ExifAttribute(12, r6.length, r0.array());
        }

        public static ExifAttribute createSLong(int r1, ByteOrder r2) {
            return createSLong(new int[]{r1}, r2);
        }

        public static ExifAttribute createSLong(int[] r5, ByteOrder r6) {
            ByteBuffer r0 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[9] * r5.length]);
            r0.order(r6);
            int r2 = r5.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L6;
            r0.putInt(r5[r3]);
            r3 = r3 + 1;
            goto L3
        L6:
            return new ExifAttribute(9, r5.length, r0.array());
        }

        public static ExifAttribute createSRational(Rational r2, ByteOrder r3) {
            return createSRational(new Rational[]{r2}, r3);
        }

        public static ExifAttribute createSRational(Rational[] r7, ByteOrder r8) {
            ByteBuffer r0 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[10] * r7.length]);
            r0.order(r8);
            int r2 = r7.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L6;
            Rational r4 = r7[r3];
            r0.putInt((int) r4.numerator);
            r0.putInt((int) r4.denominator);
            r3 = r3 + 1;
            goto L3
        L6:
            return new ExifAttribute(10, r7.length, r0.array());
        }

        public static ExifAttribute createString(String r4) {
            byte[] r0 = (r4 + 0).getBytes(ExifInterface.ASCII);
            return new ExifAttribute(2, r0.length, r0);
        }

        public static ExifAttribute createULong(long r2, ByteOrder r4) {
            return createULong(new long[]{r2}, r4);
        }

        public static ExifAttribute createULong(long[] r7, ByteOrder r8) {
            ByteBuffer r0 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * r7.length]);
            r0.order(r8);
            int r2 = r7.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L6;
            r0.putInt((int) r7[r3]);
            r3 = r3 + 1;
            goto L3
        L6:
            return new ExifAttribute(4, r7.length, r0.array());
        }

        public static ExifAttribute createURational(Rational r2, ByteOrder r3) {
            return createURational(new Rational[]{r2}, r3);
        }

        public static ExifAttribute createURational(Rational[] r7, ByteOrder r8) {
            ByteBuffer r0 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * r7.length]);
            r0.order(r8);
            int r2 = r7.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L6;
            Rational r4 = r7[r3];
            r0.putInt((int) r4.numerator);
            r0.putInt((int) r4.denominator);
            r3 = r3 + 1;
            goto L3
        L6:
            return new ExifAttribute(5, r7.length, r0.array());
        }

        public static ExifAttribute createUShort(int r1, ByteOrder r2) {
            return createUShort(new int[]{r1}, r2);
        }

        public static ExifAttribute createUShort(int[] r6, ByteOrder r7) {
            ByteBuffer r0 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * r6.length]);
            r0.order(r7);
            int r2 = r6.length;
            int r3 = 0;
        L3:
            if (r3 >= r2) goto L6;
            r0.putShort((short) r6[r3]);
            r3 = r3 + 1;
            goto L3
        L6:
            return new ExifAttribute(3, r6.length, r0.array());
        }

        public double getDoubleValue(ByteOrder r7) {
            Object r0 = getValue(r7);
            if (r0 == null) goto L43;
            if ((r0 instanceof String) == false) goto L9;
            return Double.parseDouble((String) r0);
        L9:
            if ((r0 instanceof long[]) == false) goto L17;
            long[] r1 = (long[]) r0;
            if (r1.length != 1) goto L15;
            return r1[0];
        L15:
            throw new NumberFormatException("There are more than one component");
        L17:
            if ((r0 instanceof int[]) == false) goto L25;
            int[] r2 = (int[]) r0;
            if (r2.length != 1) goto L23;
            return r2[0];
        L23:
            throw new NumberFormatException("There are more than one component");
        L25:
            if ((r0 instanceof double[]) == false) goto L33;
            double[] r3 = (double[]) r0;
            if (r3.length != 1) goto L31;
            return r3[0];
        L31:
            throw new NumberFormatException("There are more than one component");
        L33:
            if ((r0 instanceof Rational[]) == false) goto L41;
            Rational[] r4 = (Rational[]) r0;
            if (r4.length != 1) goto L39;
            return r4[0].calculate();
        L39:
            throw new NumberFormatException("There are more than one component");
        L41:
            throw new NumberFormatException("Couldn't find a double value");
        L43:
            throw new NumberFormatException("NULL can't be converted to a double value");
        }

        public int getIntValue(ByteOrder r7) {
            Object r0 = getValue(r7);
            if (r0 == null) goto L27;
            if ((r0 instanceof String) == false) goto L9;
            return Integer.parseInt((String) r0);
        L9:
            if ((r0 instanceof long[]) == false) goto L17;
            long[] r1 = (long[]) r0;
            if (r1.length != 1) goto L15;
            return (int) r1[0];
        L15:
            throw new NumberFormatException("There are more than one component");
        L17:
            if ((r0 instanceof int[]) == false) goto L25;
            int[] r2 = (int[]) r0;
            if (r2.length != 1) goto L23;
            return r2[0];
        L23:
            throw new NumberFormatException("There are more than one component");
        L25:
            throw new NumberFormatException("Couldn't find a integer value");
        L27:
            throw new NumberFormatException("NULL can't be converted to a integer value");
        }

        public String getStringValue(ByteOrder r8) {
            Object r0 = getValue(r8);
            if (r0 != null) goto L6;
            return null;
        L6:
            if ((r0 instanceof String) == true) goto L8;
            StringBuilder r2 = new StringBuilder();
            if ((r0 instanceof long[]) == false) goto L21;
            long[] r1 = (long[]) r0;
            int r3 = 0;
        L13:
            if (r3 >= r1.length) goto L19;
            r2.append(r1[r3]);
            if ((r3 + 1) == r1.length) goto L17;
            r2.append(",");
        L17:
            r3 = r3 + 1;
            goto L13
        L19:
            return r2.toString();
        L21:
            if ((r0 instanceof int[]) == false) goto L32;
            int[] r4 = (int[]) r0;
            int r5 = 0;
        L24:
            if (r5 >= r4.length) goto L30;
            r2.append(r4[r5]);
            if ((r5 + 1) == r4.length) goto L28;
            r2.append(",");
        L28:
            r5 = r5 + 1;
            goto L24
        L30:
            return r2.toString();
        L32:
            if ((r0 instanceof double[]) == false) goto L43;
            double[] r6 = (double[]) r0;
            int r7 = 0;
        L35:
            if (r7 >= r6.length) goto L41;
            r2.append(r6[r7]);
            if ((r7 + 1) == r6.length) goto L39;
            r2.append(",");
        L39:
            r7 = r7 + 1;
            goto L35
        L41:
            return r2.toString();
        L43:
            if ((r0 instanceof Rational[]) == false) goto L53;
            Rational[] r9 = (Rational[]) r0;
            int r10 = 0;
        L46:
            if (r10 >= r9.length) goto L52;
            r2.append(r9[r10].numerator);
            r2.append('/');
            r2.append(r9[r10].denominator);
            if ((r10 + 1) == r9.length) goto L50;
            r2.append(",");
        L50:
            r10 = r10 + 1;
            goto L46
        L52:
            return r2.toString();
        L53:
            return null;
        L8:
            return (String) r0;
        }

        Object getValue(ByteOrder r12) {
            ByteOrderedDataInputStream r2 = null;
            ByteOrderedDataInputStream r4 = new ByteOrderedDataInputStream(this.bytes);     // Catch: Throwable -> L146 IOException -> L148
            r4.setByteOrder(r12);     // Catch: Throwable -> L146 IOException -> L148
            switch(this.format) {
                case 1: goto L124;
                case 2: goto L94;
                case 3: goto L83;
                case 4: goto L72;
                case 5: goto L61;
                case 6: goto L124;
                case 7: goto L94;
                case 8: goto L50;
                case 9: goto L39;
                case 10: goto L28;
                case 11: goto L17;
                case 12: goto L6;
                default: goto L183;
            };     // Catch: Throwable -> L146 IOException -> L148
        L6:
            double[] r5 = new double[this.numberOfComponents];     // Catch: Throwable -> L146 IOException -> L148
            int r6 = 0;
        L8:
            if (r6 >= this.numberOfComponents) goto L175;
            r5[r6] = r4.readDouble();     // Catch: Throwable -> L146 IOException -> L148
            r6 = r6 + 1;
            goto L8
        L175:
            r4.close();     // Catch: IOException -> L14
        L16:
            return r5;
        L14:
            e = move-exception;
            Log.e(ExifInterface.TAG, "IOException occurred while closing InputStream", e);
            goto L16
        L17:
            double[] r7 = new double[this.numberOfComponents];     // Catch: Throwable -> L146 IOException -> L148
            int r8 = 0;
        L19:
            if (r8 >= this.numberOfComponents) goto L179;
            r7[r8] = r4.readFloat();     // Catch: Throwable -> L146 IOException -> L148
            r8 = r8 + 1;
            goto L19
        L179:
            r4.close();     // Catch: IOException -> L25
        L27:
            return r7;
        L25:
            e = move-exception;
            Log.e(ExifInterface.TAG, "IOException occurred while closing InputStream", e);
            goto L27
        L28:
            Rational[] r9 = new Rational[this.numberOfComponents];     // Catch: Throwable -> L146 IOException -> L148
            int r10 = 0;
        L30:
            if (r10 >= this.numberOfComponents) goto L164;
            r9[r10] = new Rational(r4.readInt(), r4.readInt());     // Catch: Throwable -> L146 IOException -> L148
            r10 = r10 + 1;
            goto L30
        L164:
            r4.close();     // Catch: IOException -> L36
        L38:
            return r9;
        L36:
            e = move-exception;
            Log.e(ExifInterface.TAG, "IOException occurred while closing InputStream", e);
            goto L38
        L39:
            int[] r11 = new int[this.numberOfComponents];     // Catch: Throwable -> L146 IOException -> L148
            int r13 = 0;
        L41:
            if (r13 >= this.numberOfComponents) goto L169;
            r11[r13] = r4.readInt();     // Catch: Throwable -> L146 IOException -> L148
            r13 = r13 + 1;
            goto L41
        L169:
            r4.close();     // Catch: IOException -> L47
        L49:
            return r11;
        L47:
            e = move-exception;
            Log.e(ExifInterface.TAG, "IOException occurred while closing InputStream", e);
            goto L49
        L50:
            int[] r14 = new int[this.numberOfComponents];     // Catch: Throwable -> L146 IOException -> L148
            int r15 = 0;
        L52:
            if (r15 >= this.numberOfComponents) goto L173;
            r14[r15] = r4.readShort();     // Catch: Throwable -> L146 IOException -> L148
            r15 = r15 + 1;
            goto L52
        L173:
            r4.close();     // Catch: IOException -> L58
        L60:
            return r14;
        L58:
            e = move-exception;
            Log.e(ExifInterface.TAG, "IOException occurred while closing InputStream", e);
            goto L60
        L61:
            Rational[] r16 = new Rational[this.numberOfComponents];     // Catch: Throwable -> L146 IOException -> L148
            int r17 = 0;
        L63:
            if (r17 >= this.numberOfComponents) goto L162;
            r16[r17] = new Rational(r4.readUnsignedInt(), r4.readUnsignedInt());     // Catch: Throwable -> L146 IOException -> L148
            r17 = r17 + 1;
            goto L63
        L162:
            r4.close();     // Catch: IOException -> L69
        L71:
            return r16;
        L69:
            e = move-exception;
            Log.e(ExifInterface.TAG, "IOException occurred while closing InputStream", e);
            goto L71
        L72:
            long[] r18 = new long[this.numberOfComponents];     // Catch: Throwable -> L146 IOException -> L148
            int r19 = 0;
        L74:
            if (r19 >= this.numberOfComponents) goto L167;
            r18[r19] = r4.readUnsignedInt();     // Catch: Throwable -> L146 IOException -> L148
            r19 = r19 + 1;
            goto L74
        L167:
            r4.close();     // Catch: IOException -> L80
        L82:
            return r18;
        L80:
            e = move-exception;
            Log.e(ExifInterface.TAG, "IOException occurred while closing InputStream", e);
            goto L82
        L83:
            int[] r20 = new int[this.numberOfComponents];     // Catch: Throwable -> L146 IOException -> L148
            int r21 = 0;
        L85:
            if (r21 >= this.numberOfComponents) goto L171;
            r20[r21] = r4.readUnsignedShort();     // Catch: Throwable -> L146 IOException -> L148
            r21 = r21 + 1;
            goto L85
        L171:
            r4.close();     // Catch: IOException -> L91
        L93:
            return r20;
        L91:
            e = move-exception;
            Log.e(ExifInterface.TAG, "IOException occurred while closing InputStream", e);
            goto L93
        L94:
            int r22 = 0;
            if (this.numberOfComponents < ExifInterface.EXIF_ASCII_PREFIX.length) goto L106;
            boolean r23 = true;
            int r24 = 0;
        L99:
            if (r24 >= ExifInterface.EXIF_ASCII_PREFIX.length) goto L104;
            if (this.bytes[r24] != ExifInterface.EXIF_ASCII_PREFIX[r24]) goto L102;
            r24 = r24 + 1;     // Catch: Throwable -> L146 IOException -> L148
            goto L99
        L102:
            r23 = false;
        L104:
            if (r23 == false) goto L106;
            r22 = ExifInterface.EXIF_ASCII_PREFIX.length;     // Catch: Throwable -> L146 IOException -> L148
        L106:
            StringBuilder r25 = new StringBuilder();     // Catch: Throwable -> L146 IOException -> L148
        L108:
            if (r22 >= this.numberOfComponents) goto L117;
            byte r26 = this.bytes[r22];     // Catch: Throwable -> L146 IOException -> L148
            if (r26 == 0) goto L117;
            if (r26 < 32) goto L115;
            r25.append((char) r26);     // Catch: Throwable -> L146 IOException -> L148
        L116:
            r22 = r22 + 1;     // Catch: Throwable -> L146 IOException -> L148
            goto L108
        L115:
            r25.append('?');     // Catch: Throwable -> L146 IOException -> L148
        L117:
            String r3 = r25.toString();     // Catch: Throwable -> L146 IOException -> L148
            r4.close();     // Catch: IOException -> L120
        L122:
            return r3;
        L120:
            e = move-exception;
            Log.e(ExifInterface.TAG, "IOException occurred while closing InputStream", e);
            goto L122
        L183:
            r4.close();     // Catch: IOException -> L143
        L145:
            return null;
        L143:
            e = move-exception;
            Log.e(ExifInterface.TAG, "IOException occurred while closing InputStream", e);
            goto L145
        L124:
            if (this.bytes.length == 1) goto L126;
        L135:
            String r27 = new String(this.bytes, ExifInterface.ASCII);     // Catch: Throwable -> L146 IOException -> L148
            r4.close();     // Catch: IOException -> L138
        L140:
            return r27;
        L138:
            e = move-exception;
            Log.e(ExifInterface.TAG, "IOException occurred while closing InputStream", e);
            goto L140
        L126:
            if (this.bytes[0] < 0) goto L135;
            if (this.bytes[0] > 1) goto L135;
            String r28 = new String(new char[]{(char) (this.bytes[0] + 48)});     // Catch: Throwable -> L146 IOException -> L148
            r4.close();     // Catch: IOException -> L132
        L134:
            return r28;
        L132:
            e = move-exception;
            Log.e(ExifInterface.TAG, "IOException occurred while closing InputStream", e);
        L148:
            e = move-exception;
            Log.w(ExifInterface.TAG, "IOException occurred during reading a value", e);     // Catch: Throwable -> L146
            if (0 != 0) goto L190;
        L155:
            return null;
        L190:
            r2.close();     // Catch: IOException -> L153
        L153:
            e = move-exception;
            Log.e(ExifInterface.TAG, "IOException occurred while closing InputStream", e);
        L146:
            th = move-exception;
            if (0 != 0) goto L181;
        L161:
            throw th;
        L181:
            r2.close();     // Catch: IOException -> L159
        L159:
            e = move-exception;
            Log.e(ExifInterface.TAG, "IOException occurred while closing InputStream", e);
            goto L161
        }

        public int size() {
            return ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[this.format] * this.numberOfComponents;
        }

        public String toString() {
            return "(" + ExifInterface.IFD_FORMAT_NAMES[this.format] + ", data length:" + this.bytes.length + ")";
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ExifStreamType {
    }

    static class ExifTag {
        public final String name;
        public final int number;
        public final int primaryFormat;
        public final int secondaryFormat;

        ExifTag(String r2, int r3, int r4) {
            this.name = r2;
            this.number = r3;
            this.primaryFormat = r4;
            this.secondaryFormat = -1;
        }

        ExifTag(String r1, int r2, int r3, int r4) {
            this.name = r1;
            this.number = r2;
            this.primaryFormat = r3;
            this.secondaryFormat = r4;
        }

        boolean isFormatCompatible(int r4) {
            if (this.primaryFormat == 7) goto L35;
            if (r4 == 7) goto L35;
            if (this.primaryFormat != r4) goto L9;
        L34:
            return true;
        L9:
            if (this.secondaryFormat == r4) goto L34;
            if (this.primaryFormat == 4) goto L16;
            if (this.secondaryFormat == 4) goto L16;
        L19:
            if (this.primaryFormat == 9) goto L23;
            if (this.secondaryFormat == 9) goto L23;
        L26:
            if (this.primaryFormat == 12) goto L30;
            if (this.secondaryFormat == 12) goto L30;
            return false;
        L30:
            if (r4 != 11) goto L36;
            return true;
        L36:
            return false;
        L23:
            if (r4 != 8) goto L26;
            return true;
        L16:
            if (r4 != 3) goto L19;
            return true;
        L35:
            return true;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IfdType {
    }

    private static class Rational {
        public final long denominator;
        public final long numerator;

        Rational(double r5) {
            this((long) (10000.0d * r5), Renderer.DEFAULT_DURATION_TO_PROGRESS_US);
        }

        Rational(long r4, long r6) {
            if (r6 != 0) goto L6;
            this.numerator = 0;
            this.denominator = 1;
            return;
        L6:
            this.numerator = r4;
            this.denominator = r6;
        }

        public double calculate() {
            return this.numerator / this.denominator;
        }

        public String toString() {
            return this.numerator + "/" + this.denominator;
        }
    }

    private static class SeekableByteOrderedDataInputStream extends ByteOrderedDataInputStream {
        SeekableByteOrderedDataInputStream(InputStream r3) throws IOException {
            super(r3);
            if (r3.markSupported() == false) goto L7;
            this.mDataInputStream.mark(Integer.MAX_VALUE);
            return;
        L7:
            throw new IllegalArgumentException("Cannot create SeekableByteOrderedDataInputStream with stream that does not support mark/reset");
        }

        SeekableByteOrderedDataInputStream(byte[] r3) throws IOException {
            super(r3);
            this.mDataInputStream.mark(Integer.MAX_VALUE);
        }

        public void seek(long r3) throws IOException {
            if (this.mPosition <= r3) goto L5;
            this.mPosition = 0;
            this.mDataInputStream.reset();
        L6:
            skipFully((int) r3);
            return;
        L5:
            r3 = r3 - ((long) this.mPosition);
            goto L6
        }
    }

    static {
        char r1 = 3;
        DEBUG = Log.isLoggable(TAG, 3);
        ROTATION_ORDER = Arrays.asList(new Integer[]{1, 6, 3, 8});
        FLIPPED_ROTATION_ORDER = Arrays.asList(new Integer[]{2, 7, 4, 5});
        BITS_PER_SAMPLE_RGB = new int[]{8, 8, 8};
        BITS_PER_SAMPLE_GREYSCALE_1 = new int[]{4};
        BITS_PER_SAMPLE_GREYSCALE_2 = new int[]{8};
        JPEG_SIGNATURE = new byte[]{-1, MARKER_SOI, -1};
        HEIF_TYPE_FTYP = new byte[]{102, 116, 121, 112};
        HEIF_BRAND_MIF1 = new byte[]{109, 105, 102, 49};
        HEIF_BRAND_HEIC = new byte[]{104, 101, 105, 99};
        ORF_MAKER_NOTE_HEADER_1 = new byte[]{79, 76, 89, 77, 80, 0};
        ORF_MAKER_NOTE_HEADER_2 = new byte[]{79, 76, 89, 77, 80, 85, 83, 0, 73, 73};
        PNG_SIGNATURE = new byte[]{-119, 80, 78, 71, Ascii.f79CR, 10, Ascii.SUB, 10};
        PNG_CHUNK_TYPE_EXIF = new byte[]{101, 88, 73, 102};
        PNG_CHUNK_TYPE_IHDR = new byte[]{73, 72, 68, 82};
        PNG_CHUNK_TYPE_IEND = new byte[]{73, 69, 78, 68};
        WEBP_SIGNATURE_1 = new byte[]{82, 73, 70, 70};
        WEBP_SIGNATURE_2 = new byte[]{87, 69, 66, 80};
        WEBP_CHUNK_TYPE_EXIF = new byte[]{69, 88, 73, 70};
        WEBP_VP8_SIGNATURE = new byte[]{-99, 1, START_CODE};
        WEBP_CHUNK_TYPE_VP8X = "VP8X".getBytes(Charset.defaultCharset());
        WEBP_CHUNK_TYPE_VP8L = "VP8L".getBytes(Charset.defaultCharset());
        WEBP_CHUNK_TYPE_VP8 = "VP8 ".getBytes(Charset.defaultCharset());
        WEBP_CHUNK_TYPE_ANIM = "ANIM".getBytes(Charset.defaultCharset());
        WEBP_CHUNK_TYPE_ANMF = "ANMF".getBytes(Charset.defaultCharset());
        IFD_FORMAT_NAMES = new String[]{"", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE", "IFD"};
        IFD_FORMAT_BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};
        EXIF_ASCII_PREFIX = new byte[]{65, 83, 67, 73, 73, 0, 0, 0};
        IFD_TIFF_TAGS = new ExifTag[]{new ExifTag(TAG_NEW_SUBFILE_TYPE, 254, 4), new ExifTag(TAG_SUBFILE_TYPE, 255, 4), new ExifTag(TAG_IMAGE_WIDTH, 256, 3, 4), new ExifTag(TAG_IMAGE_LENGTH, 257, 3, 4), new ExifTag(TAG_BITS_PER_SAMPLE, 258, 3), new ExifTag(TAG_COMPRESSION, 259, 3), new ExifTag(TAG_PHOTOMETRIC_INTERPRETATION, 262, 3), new ExifTag(TAG_IMAGE_DESCRIPTION, 270, 2), new ExifTag(TAG_MAKE, 271, 2), new ExifTag(TAG_MODEL, 272, 2), new ExifTag(TAG_STRIP_OFFSETS, 273, 3, 4), new ExifTag(TAG_ORIENTATION, 274, 3), new ExifTag(TAG_SAMPLES_PER_PIXEL, 277, 3), new ExifTag(TAG_ROWS_PER_STRIP, 278, 3, 4), new ExifTag(TAG_STRIP_BYTE_COUNTS, 279, 3, 4), new ExifTag(TAG_X_RESOLUTION, 282, 5), new ExifTag(TAG_Y_RESOLUTION, 283, 5), new ExifTag(TAG_PLANAR_CONFIGURATION, 284, 3), new ExifTag(TAG_RESOLUTION_UNIT, 296, 3), new ExifTag(TAG_TRANSFER_FUNCTION, 301, 3), new ExifTag(TAG_SOFTWARE, 305, 2), new ExifTag(TAG_DATETIME, 306, 2), new ExifTag(TAG_ARTIST, 315, 2), new ExifTag(TAG_WHITE_POINT, 318, 5), new ExifTag(TAG_PRIMARY_CHROMATICITIES, 319, 5), new ExifTag(TAG_SUB_IFD_POINTER, 330, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, InputDeviceCompat.SOURCE_DPAD, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4), new ExifTag(TAG_Y_CB_CR_COEFFICIENTS, 529, 5), new ExifTag(TAG_Y_CB_CR_SUB_SAMPLING, 530, 3), new ExifTag(TAG_Y_CB_CR_POSITIONING, 531, 3), new ExifTag(TAG_REFERENCE_BLACK_WHITE, 532, 5), new ExifTag(TAG_COPYRIGHT, 33432, 2), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, 34853, 4), new ExifTag(TAG_RW2_SENSOR_TOP_BORDER, 4, 4), new ExifTag(TAG_RW2_SENSOR_LEFT_BORDER, 5, 4), new ExifTag(TAG_RW2_SENSOR_BOTTOM_BORDER, 6, 4), new ExifTag(TAG_RW2_SENSOR_RIGHT_BORDER, 7, 4), new ExifTag(TAG_RW2_ISO, 23, 3), new ExifTag(TAG_RW2_JPG_FROM_RAW, 46, 7), new ExifTag(TAG_XMP, 700, 1)};
        IFD_EXIF_TAGS = new ExifTag[]{new ExifTag(TAG_EXPOSURE_TIME, 33434, 5), new ExifTag(TAG_F_NUMBER, 33437, 5), new ExifTag(TAG_EXPOSURE_PROGRAM, 34850, 3), new ExifTag(TAG_SPECTRAL_SENSITIVITY, 34852, 2), new ExifTag(TAG_PHOTOGRAPHIC_SENSITIVITY, 34855, 3), new ExifTag(TAG_OECF, 34856, 7), new ExifTag(TAG_SENSITIVITY_TYPE, 34864, 3), new ExifTag(TAG_STANDARD_OUTPUT_SENSITIVITY, 34865, 4), new ExifTag(TAG_RECOMMENDED_EXPOSURE_INDEX, 34866, 4), new ExifTag(TAG_ISO_SPEED, 34867, 4), new ExifTag(TAG_ISO_SPEED_LATITUDE_YYY, 34868, 4), new ExifTag(TAG_ISO_SPEED_LATITUDE_ZZZ, 34869, 4), new ExifTag(TAG_EXIF_VERSION, 36864, 2), new ExifTag(TAG_DATETIME_ORIGINAL, 36867, 2), new ExifTag(TAG_DATETIME_DIGITIZED, 36868, 2), new ExifTag(TAG_OFFSET_TIME, 36880, 2), new ExifTag(TAG_OFFSET_TIME_ORIGINAL, 36881, 2), new ExifTag(TAG_OFFSET_TIME_DIGITIZED, 36882, 2), new ExifTag(TAG_COMPONENTS_CONFIGURATION, 37121, 7), new ExifTag(TAG_COMPRESSED_BITS_PER_PIXEL, 37122, 5), new ExifTag(TAG_SHUTTER_SPEED_VALUE, 37377, 10), new ExifTag(TAG_APERTURE_VALUE, 37378, 5), new ExifTag(TAG_BRIGHTNESS_VALUE, 37379, 10), new ExifTag(TAG_EXPOSURE_BIAS_VALUE, 37380, 10), new ExifTag(TAG_MAX_APERTURE_VALUE, 37381, 5), new ExifTag(TAG_SUBJECT_DISTANCE, 37382, 5), new ExifTag(TAG_METERING_MODE, 37383, 3), new ExifTag(TAG_LIGHT_SOURCE, 37384, 3), new ExifTag(TAG_FLASH, 37385, 3), new ExifTag(TAG_FOCAL_LENGTH, 37386, 5), new ExifTag(TAG_SUBJECT_AREA, 37396, 3), new ExifTag(TAG_MAKER_NOTE, 37500, 7), new ExifTag(TAG_USER_COMMENT, 37510, 7), new ExifTag(TAG_SUBSEC_TIME, 37520, 2), new ExifTag(TAG_SUBSEC_TIME_ORIGINAL, 37521, 2), new ExifTag(TAG_SUBSEC_TIME_DIGITIZED, 37522, 2), new ExifTag(TAG_FLASHPIX_VERSION, 40960, 7), new ExifTag(TAG_COLOR_SPACE, 40961, 3), new ExifTag(TAG_PIXEL_X_DIMENSION, 40962, 3, 4), new ExifTag(TAG_PIXEL_Y_DIMENSION, 40963, 3, 4), new ExifTag(TAG_RELATED_SOUND_FILE, 40964, 2), new ExifTag(TAG_INTEROPERABILITY_IFD_POINTER, 40965, 4), new ExifTag(TAG_FLASH_ENERGY, 41483, 5), new ExifTag(TAG_SPATIAL_FREQUENCY_RESPONSE, 41484, 7), new ExifTag(TAG_FOCAL_PLANE_X_RESOLUTION, 41486, 5), new ExifTag(TAG_FOCAL_PLANE_Y_RESOLUTION, 41487, 5), new ExifTag(TAG_FOCAL_PLANE_RESOLUTION_UNIT, 41488, 3), new ExifTag(TAG_SUBJECT_LOCATION, 41492, 3), new ExifTag(TAG_EXPOSURE_INDEX, 41493, 5), new ExifTag(TAG_SENSING_METHOD, 41495, 3), new ExifTag(TAG_FILE_SOURCE, 41728, 7), new ExifTag(TAG_SCENE_TYPE, 41729, 7), new ExifTag(TAG_CFA_PATTERN, 41730, 7), new ExifTag(TAG_CUSTOM_RENDERED, 41985, 3), new ExifTag(TAG_EXPOSURE_MODE, 41986, 3), new ExifTag(TAG_WHITE_BALANCE, 41987, 3), new ExifTag(TAG_DIGITAL_ZOOM_RATIO, 41988, 5), new ExifTag(TAG_FOCAL_LENGTH_IN_35MM_FILM, 41989, 3), new ExifTag(TAG_SCENE_CAPTURE_TYPE, 41990, 3), new ExifTag(TAG_GAIN_CONTROL, 41991, 3), new ExifTag(TAG_CONTRAST, 41992, 3), new ExifTag(TAG_SATURATION, 41993, 3), new ExifTag(TAG_SHARPNESS, 41994, 3), new ExifTag(TAG_DEVICE_SETTING_DESCRIPTION, 41995, 7), new ExifTag(TAG_SUBJECT_DISTANCE_RANGE, 41996, 3), new ExifTag(TAG_IMAGE_UNIQUE_ID, 42016, 2), new ExifTag("CameraOwnerName", 42032, 2), new ExifTag(TAG_BODY_SERIAL_NUMBER, 42033, 2), new ExifTag(TAG_LENS_SPECIFICATION, 42034, 5), new ExifTag(TAG_LENS_MAKE, 42035, 2), new ExifTag(TAG_LENS_MODEL, 42036, 2), new ExifTag(TAG_GAMMA, 42240, 5), new ExifTag(TAG_DNG_VERSION, 50706, 1), new ExifTag(TAG_DEFAULT_CROP_SIZE, 50720, 3, 4)};
        IFD_GPS_TAGS = new ExifTag[]{new ExifTag(TAG_GPS_VERSION_ID, 0, 1), new ExifTag(TAG_GPS_LATITUDE_REF, 1, 2), new ExifTag(TAG_GPS_LATITUDE, 2, 5, 10), new ExifTag(TAG_GPS_LONGITUDE_REF, 3, 2), new ExifTag(TAG_GPS_LONGITUDE, 4, 5, 10), new ExifTag(TAG_GPS_ALTITUDE_REF, 5, 1), new ExifTag(TAG_GPS_ALTITUDE, 6, 5), new ExifTag(TAG_GPS_TIMESTAMP, 7, 5), new ExifTag(TAG_GPS_SATELLITES, 8, 2), new ExifTag(TAG_GPS_STATUS, 9, 2), new ExifTag(TAG_GPS_MEASURE_MODE, 10, 2), new ExifTag(TAG_GPS_DOP, 11, 5), new ExifTag(TAG_GPS_SPEED_REF, 12, 2), new ExifTag(TAG_GPS_SPEED, 13, 5), new ExifTag(TAG_GPS_TRACK_REF, 14, 2), new ExifTag(TAG_GPS_TRACK, 15, 5), new ExifTag(TAG_GPS_IMG_DIRECTION_REF, 16, 2), new ExifTag(TAG_GPS_IMG_DIRECTION, 17, 5), new ExifTag(TAG_GPS_MAP_DATUM, 18, 2), new ExifTag(TAG_GPS_DEST_LATITUDE_REF, 19, 2), new ExifTag(TAG_GPS_DEST_LATITUDE, 20, 5), new ExifTag(TAG_GPS_DEST_LONGITUDE_REF, 21, 2), new ExifTag(TAG_GPS_DEST_LONGITUDE, 22, 5), new ExifTag(TAG_GPS_DEST_BEARING_REF, 23, 2), new ExifTag(TAG_GPS_DEST_BEARING, 24, 5), new ExifTag(TAG_GPS_DEST_DISTANCE_REF, 25, 2), new ExifTag(TAG_GPS_DEST_DISTANCE, 26, 5), new ExifTag(TAG_GPS_PROCESSING_METHOD, 27, 7), new ExifTag(TAG_GPS_AREA_INFORMATION, 28, 7), new ExifTag(TAG_GPS_DATESTAMP, 29, 2), new ExifTag(TAG_GPS_DIFFERENTIAL, 30, 3), new ExifTag(TAG_GPS_H_POSITIONING_ERROR, 31, 5)};
        IFD_INTEROPERABILITY_TAGS = new ExifTag[]{new ExifTag(TAG_INTEROPERABILITY_INDEX, 1, 2)};
        IFD_THUMBNAIL_TAGS = new ExifTag[]{new ExifTag(TAG_NEW_SUBFILE_TYPE, 254, 4), new ExifTag(TAG_SUBFILE_TYPE, 255, 4), new ExifTag(TAG_THUMBNAIL_IMAGE_WIDTH, 256, 3, 4), new ExifTag(TAG_THUMBNAIL_IMAGE_LENGTH, 257, 3, 4), new ExifTag(TAG_BITS_PER_SAMPLE, 258, 3), new ExifTag(TAG_COMPRESSION, 259, 3), new ExifTag(TAG_PHOTOMETRIC_INTERPRETATION, 262, 3), new ExifTag(TAG_IMAGE_DESCRIPTION, 270, 2), new ExifTag(TAG_MAKE, 271, 2), new ExifTag(TAG_MODEL, 272, 2), new ExifTag(TAG_STRIP_OFFSETS, 273, 3, 4), new ExifTag(TAG_THUMBNAIL_ORIENTATION, 274, 3), new ExifTag(TAG_SAMPLES_PER_PIXEL, 277, 3), new ExifTag(TAG_ROWS_PER_STRIP, 278, 3, 4), new ExifTag(TAG_STRIP_BYTE_COUNTS, 279, 3, 4), new ExifTag(TAG_X_RESOLUTION, 282, 5), new ExifTag(TAG_Y_RESOLUTION, 283, 5), new ExifTag(TAG_PLANAR_CONFIGURATION, 284, 3), new ExifTag(TAG_RESOLUTION_UNIT, 296, 3), new ExifTag(TAG_TRANSFER_FUNCTION, 301, 3), new ExifTag(TAG_SOFTWARE, 305, 2), new ExifTag(TAG_DATETIME, 306, 2), new ExifTag(TAG_ARTIST, 315, 2), new ExifTag(TAG_WHITE_POINT, 318, 5), new ExifTag(TAG_PRIMARY_CHROMATICITIES, 319, 5), new ExifTag(TAG_SUB_IFD_POINTER, 330, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT, InputDeviceCompat.SOURCE_DPAD, 4), new ExifTag(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, 514, 4), new ExifTag(TAG_Y_CB_CR_COEFFICIENTS, 529, 5), new ExifTag(TAG_Y_CB_CR_SUB_SAMPLING, 530, 3), new ExifTag(TAG_Y_CB_CR_POSITIONING, 531, 3), new ExifTag(TAG_REFERENCE_BLACK_WHITE, 532, 5), new ExifTag(TAG_COPYRIGHT, 33432, 2), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, 34853, 4), new ExifTag(TAG_DNG_VERSION, 50706, 1), new ExifTag(TAG_DEFAULT_CROP_SIZE, 50720, 3, 4)};
        TAG_RAF_IMAGE_SIZE = new ExifTag(TAG_STRIP_OFFSETS, 273, 3);
        ORF_MAKER_NOTE_TAGS = new ExifTag[]{new ExifTag(TAG_ORF_THUMBNAIL_IMAGE, 256, 7), new ExifTag(TAG_ORF_CAMERA_SETTINGS_IFD_POINTER, 8224, 4), new ExifTag(TAG_ORF_IMAGE_PROCESSING_IFD_POINTER, 8256, 4)};
        ORF_CAMERA_SETTINGS_TAGS = new ExifTag[]{new ExifTag(TAG_ORF_PREVIEW_IMAGE_START, 257, 4), new ExifTag(TAG_ORF_PREVIEW_IMAGE_LENGTH, 258, 4)};
        ORF_IMAGE_PROCESSING_TAGS = new ExifTag[]{new ExifTag(TAG_ORF_ASPECT_FRAME, 4371, 3)};
        PEF_TAGS = new ExifTag[]{new ExifTag(TAG_COLOR_SPACE, 55, 3)};
        EXIF_TAGS = new ExifTag[][]{IFD_TIFF_TAGS, IFD_EXIF_TAGS, IFD_GPS_TAGS, IFD_INTEROPERABILITY_TAGS, IFD_THUMBNAIL_TAGS, IFD_TIFF_TAGS, ORF_MAKER_NOTE_TAGS, ORF_CAMERA_SETTINGS_TAGS, ORF_IMAGE_PROCESSING_TAGS, PEF_TAGS};
        EXIF_POINTER_TAGS = new ExifTag[]{new ExifTag(TAG_SUB_IFD_POINTER, 330, 4), new ExifTag(TAG_EXIF_IFD_POINTER, 34665, 4), new ExifTag(TAG_GPS_INFO_IFD_POINTER, 34853, 4), new ExifTag(TAG_INTEROPERABILITY_IFD_POINTER, 40965, 4), new ExifTag(TAG_ORF_CAMERA_SETTINGS_IFD_POINTER, 8224, 1), new ExifTag(TAG_ORF_IMAGE_PROCESSING_IFD_POINTER, 8256, 1)};
        sExifTagMapsForReading = new HashMap[EXIF_TAGS.length];
        sExifTagMapsForWriting = new HashMap[EXIF_TAGS.length];
        sTagSetForCompatibility = new HashSet(Arrays.asList(new String[]{TAG_F_NUMBER, TAG_DIGITAL_ZOOM_RATIO, TAG_EXPOSURE_TIME, TAG_SUBJECT_DISTANCE, TAG_GPS_TIMESTAMP}));
        sExifPointerTagMap = new HashMap();
        ASCII = Charset.forName("US-ASCII");
        IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(ASCII);
        IDENTIFIER_XMP_APP1 = "http://ns.adobe.com/xap/1.0/\u0000".getBytes(ASCII);
        sFormatterPrimary = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
        sFormatterPrimary.setTimeZone(TimeZone.getTimeZone("UTC"));
        sFormatterSecondary = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        sFormatterSecondary.setTimeZone(TimeZone.getTimeZone("UTC"));
        int r0 = 0;
    L4:
        if (r0 >= EXIF_TAGS.length) goto L9;
        sExifTagMapsForReading[r0] = new HashMap();
        sExifTagMapsForWriting[r0] = new HashMap();
        ExifTag[] r4 = EXIF_TAGS[r0];
        int r6 = r4.length;
        int r10 = 0;
    L6:
        if (r10 >= r6) goto L8;
        ExifTag r9 = r4[r10];
        sExifTagMapsForReading[r0].put(Integer.valueOf(r9.number), r9);
        sExifTagMapsForWriting[r0].put(r9.name, r9);
        r10 = r10 + 1;
        r1 = r1;
        goto L6
    L8:
        r0 = r0 + 1;
        goto L4
    L9:
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[0].number), 5);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[1].number), 1);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[2].number), 2);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[r1].number), 3);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[4].number), 7);
        sExifPointerTagMap.put(Integer.valueOf(EXIF_POINTER_TAGS[5].number), 8);
        NON_ZERO_TIME_PATTERN = Pattern.compile(".*[1-9].*");
        GPS_TIMESTAMP_PATTERN = Pattern.compile("^(\\d{2}):(\\d{2}):(\\d{2})$");
        DATETIME_PRIMARY_FORMAT_PATTERN = Pattern.compile("^(\\d{4}):(\\d{2}):(\\d{2})\\s(\\d{2}):(\\d{2}):(\\d{2})$");
        DATETIME_SECONDARY_FORMAT_PATTERN = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})\\s(\\d{2}):(\\d{2}):(\\d{2})$");
    }

    public ExifInterface(File r3) throws IOException {
        this.mAttributes = new HashMap[EXIF_TAGS.length];
        this.mAttributesOffsets = new HashSet(EXIF_TAGS.length);
        this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
        if (r3 == null) goto L7;
        initForFilename(r3.getAbsolutePath());
        return;
    L7:
        throw new NullPointerException("file cannot be null");
    }

    public ExifInterface(FileDescriptor r5) throws IOException {
        this.mAttributes = new HashMap[EXIF_TAGS.length];
        this.mAttributesOffsets = new HashSet(EXIF_TAGS.length);
        this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
        if (r5 == null) goto L25;
        this.mAssetInputStream = null;
        this.mFilename = null;
        boolean r1 = false;
        if (isSeekableFD(r5) == false) goto L12;
        this.mSeekableFileDescriptor = r5;
        r5 = ExifInterfaceUtils.Api21Impl.dup(r5);     // Catch: Exception -> L9
        r1 = true;
    L13:
        FileInputStream r0 = null;
        r0 = new FileInputStream(r5);     // Catch: Throwable -> L19
        loadAttributes(r0);     // Catch: Throwable -> L19
        ExifInterfaceUtils.closeQuietly(r0);
        if (r1 == false) goto L30;
        ExifInterfaceUtils.closeFileDescriptor(r5);
        return;
    L30:
        return;
    L19:
        th = move-exception;
        ExifInterfaceUtils.closeQuietly(r0);
        if (r1 == false) goto L23;
        ExifInterfaceUtils.closeFileDescriptor(r5);
    L23:
        throw th;
    L9:
        e = move-exception;
        throw new IOException("Failed to duplicate file descriptor", e);
    L12:
        this.mSeekableFileDescriptor = null;
        goto L13
    L25:
        throw new NullPointerException("fileDescriptor cannot be null");
    }

    public ExifInterface(InputStream r2) throws IOException {
        this(r2, 0);
    }

    public ExifInterface(InputStream r6, int r7) throws IOException {
        this.mAttributes = new HashMap[EXIF_TAGS.length];
        this.mAttributesOffsets = new HashSet(EXIF_TAGS.length);
        this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
        if (r6 == null) goto L26;
        this.mFilename = null;
        if (r7 != 1) goto L7;
        boolean r2 = true;
    L8:
        if (r2 == false) goto L15;
        r6 = new BufferedInputStream(r6, IDENTIFIER_EXIF_APP1.length);
        if (isExifDataOnly((BufferedInputStream) r6) == true) goto L13;
        Log.w(TAG, "Given data does not follow the structure of an Exif-only data.");
        return;
    L13:
        this.mIsExifDataOnly = true;
        this.mAssetInputStream = null;
        this.mSeekableFileDescriptor = null;
    L23:
        loadAttributes(r6);
        return;
    L15:
        if ((r6 instanceof AssetManager.AssetInputStream) == false) goto L18;
        this.mAssetInputStream = (AssetManager.AssetInputStream) r6;
        this.mSeekableFileDescriptor = null;
        goto L23
    L18:
        if ((r6 instanceof FileInputStream) == true) goto L20;
    L22:
        this.mAssetInputStream = null;
        this.mSeekableFileDescriptor = null;
        goto L23
    L20:
        if (isSeekableFD(((FileInputStream) r6).getFD()) == false) goto L22;
        this.mAssetInputStream = null;
        this.mSeekableFileDescriptor = ((FileInputStream) r6).getFD();
        goto L23
    L7:
        r2 = false;
        goto L8
    L26:
        throw new NullPointerException("inputStream cannot be null");
    }

    public ExifInterface(String r3) throws IOException {
        this.mAttributes = new HashMap[EXIF_TAGS.length];
        this.mAttributesOffsets = new HashSet(EXIF_TAGS.length);
        this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
        if (r3 == null) goto L7;
        initForFilename(r3);
        return;
    L7:
        throw new NullPointerException("filename cannot be null");
    }

    private void addDefaultValuesForCompatibility() {
        String r0 = getAttribute(TAG_DATETIME_ORIGINAL);
        if (r0 == null) goto L8;
        if (getAttribute(TAG_DATETIME) != null) goto L8;
        this.mAttributes[0].put(TAG_DATETIME, ExifAttribute.createString(r0));
    L8:
        if (getAttribute(TAG_IMAGE_WIDTH) != null) goto L11;
        this.mAttributes[0].put(TAG_IMAGE_WIDTH, ExifAttribute.createULong(0, this.mExifByteOrder));
    L11:
        if (getAttribute(TAG_IMAGE_LENGTH) != null) goto L14;
        this.mAttributes[0].put(TAG_IMAGE_LENGTH, ExifAttribute.createULong(0, this.mExifByteOrder));
    L14:
        if (getAttribute(TAG_ORIENTATION) != null) goto L17;
        this.mAttributes[0].put(TAG_ORIENTATION, ExifAttribute.createULong(0, this.mExifByteOrder));
    L17:
        if (getAttribute(TAG_LIGHT_SOURCE) != null) goto L20;
        this.mAttributes[1].put(TAG_LIGHT_SOURCE, ExifAttribute.createULong(0, this.mExifByteOrder));
        return;
    }

    private String convertDecimalDegree(double r11) {
        long r0 = (long) r11;
        long r2 = (long) ((r11 - r0) * 60.0d);
        return r0 + "/1," + r2 + "/1," + Math.round((((r11 - r0) - (r2 / 60.0d)) * 3600.0d) * 1.0E7d) + "/10000000";
    }

    private static double convertRationalLatLonToDouble(String r13, String r14) {
        String[] r1 = r13.split(",", -1);     // Catch: ArrayIndexOutOfBoundsException -> L18 Throwable -> L20
        String[] r4 = r1[0].split("/", -1);     // Catch: ArrayIndexOutOfBoundsException -> L18 Throwable -> L20
        double r5 = Double.parseDouble(r4[0].trim()) / Double.parseDouble(r4[1].trim());     // Catch: ArrayIndexOutOfBoundsException -> L18 Throwable -> L20
        String[] r8 = r1[1].split("/", -1);     // Catch: ArrayIndexOutOfBoundsException -> L18 Throwable -> L20
        double r9 = Double.parseDouble(r8[0].trim()) / Double.parseDouble(r8[1].trim());     // Catch: ArrayIndexOutOfBoundsException -> L18 Throwable -> L20
        String[] r0 = r1[2].split("/", -1);     // Catch: ArrayIndexOutOfBoundsException -> L18 Throwable -> L20
        double r2 = Double.parseDouble(r0[0].trim()) / Double.parseDouble(r0[1].trim());     // Catch: ArrayIndexOutOfBoundsException -> L18 Throwable -> L20
        double r7 = ((r9 / 60.0d) + r5) + (r2 / 3600.0d);     // Catch: ArrayIndexOutOfBoundsException -> L18 Throwable -> L20
        if (r14.equals(LATITUDE_SOUTH) == true) goto L17;
        if (r14.equals(LONGITUDE_WEST) == true) goto L17;
        if (r14.equals("N") == false) goto L11;
    L15:
        return r7;
    L11:
        if (r14.equals(LONGITUDE_EAST) == true) goto L15;
        throw new IllegalArgumentException();     // Catch: ArrayIndexOutOfBoundsException -> L18 Throwable -> L20
    L17:
        return -r7;
    L22:
        throw new IllegalArgumentException();
    }

    private void copyChunksUpToGivenChunkType(ByteOrderedDataInputStream r7, ByteOrderedDataOutputStream r8, byte[] r9, byte[] r10) throws IOException {
    L2:
        byte[] r0 = new byte[4];
        if (r7.read(r0) != r0.length) goto L4;
        copyWebPChunk(r7, r8, r0);
        if (Arrays.equals(r0, r9) == true) goto L17;
        if (r10 == null) goto L2;
        if (Arrays.equals(r0, r10) == false) goto L2;
        return;
    L17:
        return;
    L4:
        StringBuilder r2 = new StringBuilder().append("Encountered invalid length while copying WebP chunks up tochunk type ").append(new String(r9, ASCII));
        if (r10 != null) goto L7;
        String r3 = "";
    L9:
        throw new IOException(r2.append(r3).toString());
    L7:
        r3 = " or " + new String(r10, ASCII);
        goto L9
    }

    private void copyWebPChunk(ByteOrderedDataInputStream r4, ByteOrderedDataOutputStream r5, byte[] r6) throws IOException {
        int r0 = r4.readInt();
        r5.write(r6);
        r5.writeInt(r0);
        if ((r0 % 2) != 1) goto L5;
        int r1 = r0 + 1;
    L6:
        ExifInterfaceUtils.copy(r4, r5, r1);
        return;
    L5:
        r1 = r0;
        goto L6
    }

    private ExifAttribute getExifAttribute(String r3) {
        if (r3 == null) goto L19;
        if (TAG_ISO_SPEED_RATINGS.equals(r3) == true) goto L6;
    L9:
        int r0 = 0;
    L11:
        if (r0 >= EXIF_TAGS.length) goto L16;
        ExifAttribute r1 = this.mAttributes[r0].get(r3);
        if (r1 != null) goto L14;
        r0 = r0 + 1;
        goto L11
    L14:
        return r1;
    L16:
        return null;
    L6:
        if (DEBUG == false) goto L8;
        Log.d(TAG, "getExifAttribute: Replacing TAG_ISO_SPEED_RATINGS with TAG_PHOTOGRAPHIC_SENSITIVITY.");
    L8:
        r3 = TAG_PHOTOGRAPHIC_SENSITIVITY;
        goto L9
    L19:
        throw new NullPointerException("tag shouldn't be null");
    }

    private void getHeifAttributes(final SeekableByteOrderedDataInputStream r18) throws IOException {
        if (Build.VERSION.SDK_INT < 28) goto L68;
        MediaMetadataRetriever r3 = new MediaMetadataRetriever();
        ExifInterfaceUtils.Api23Impl.setDataSource(r3, new C01471(this, r18));     // Catch: Throwable -> L58 RuntimeException -> L60
        String r4 = r3.extractMetadata(33);     // Catch: Throwable -> L58 RuntimeException -> L60
        String r5 = r3.extractMetadata(34);     // Catch: Throwable -> L58 RuntimeException -> L60
        String r6 = r3.extractMetadata(26);     // Catch: Throwable -> L58 RuntimeException -> L60
        String r7 = r3.extractMetadata(17);     // Catch: Throwable -> L58 RuntimeException -> L60
        String r8 = null;
        String r9 = null;
        String r10 = null;
        if ("yes".equals(r6) == false) goto L9;
        r8 = r3.extractMetadata(29);     // Catch: Throwable -> L58 RuntimeException -> L60
        r9 = r3.extractMetadata(30);     // Catch: Throwable -> L58 RuntimeException -> L60
        r10 = r3.extractMetadata(31);     // Catch: Throwable -> L58 RuntimeException -> L60
    L12:
        if (r8 == null) goto L14;
        this.mAttributes[0].put(TAG_IMAGE_WIDTH, ExifAttribute.createUShort(Integer.parseInt(r8), this.mExifByteOrder));     // Catch: Throwable -> L58 RuntimeException -> L60
    L14:
        if (r9 == null) goto L16;
        this.mAttributes[0].put(TAG_IMAGE_LENGTH, ExifAttribute.createUShort(Integer.parseInt(r9), this.mExifByteOrder));     // Catch: Throwable -> L58 RuntimeException -> L60
    L16:
        if (r10 == null) goto L69;
        int r12 = 1;
        switch(Integer.parseInt(r10)) {
            case 90: goto L22;
            case 180: goto L21;
            case 270: goto L20;
            default: goto L23;
        };     // Catch: Throwable -> L58 RuntimeException -> L60
    L20:
        r12 = 8;
        goto L23
    L21:
        r12 = 3;
        goto L23
    L22:
        r12 = 6;
    L23:
        this.mAttributes[0].put(TAG_ORIENTATION, ExifAttribute.createUShort(r12, this.mExifByteOrder));     // Catch: Throwable -> L58 RuntimeException -> L60
    L69:
        if (r4 == null) goto L52;
        if (r5 == null) goto L52;
        int r13 = Integer.parseInt(r4);     // Catch: Throwable -> L58 RuntimeException -> L60
        int r14 = Integer.parseInt(r5);     // Catch: Throwable -> L58 RuntimeException -> L60
        goto L63
    L28:
        if (r14 <= 6) goto L49;
        r18.seek(r13);     // Catch: Throwable -> L44 RuntimeException -> L46
        byte[] r0 = new byte[6];     // Catch: Throwable -> L44 RuntimeException -> L46
        if (r18.read(r0) != 6) goto L43;
        int r15 = r13 + 6;     // Catch: Throwable -> L44 RuntimeException -> L46
        int r16 = r14 - 6;     // Catch: Throwable -> L44 RuntimeException -> L46
        if (Arrays.equals(r0, IDENTIFIER_EXIF_APP1) == false) goto L41;
        byte[] r1 = new byte[r16];     // Catch: Throwable -> L44 RuntimeException -> L46
        if (r18.read(r1) != r16) goto L39;
        this.mOffsetToExifData = r15;     // Catch: RuntimeException -> L56 Throwable -> L64
        readExifSegment(r1, 0);     // Catch: RuntimeException -> L56 Throwable -> L64
        goto L52
    L39:
        throw new IOException("Can't read exif");     // Catch: RuntimeException -> L56 Throwable -> L64
        goto L63
    L41:
        throw new IOException("Invalid identifier");     // Catch: RuntimeException -> L56 Throwable -> L64
    L43:
        throw new IOException("Can't read identifier");     // Catch: RuntimeException -> L56 Throwable -> L64
    L44:
        th = th;
    L65:
        r3.release();
        throw th;
    L49:
        throw new IOException("Invalid exif length");     // Catch: RuntimeException -> L56 Throwable -> L64
    L52:
        if (DEBUG == false) goto L54;
        Log.d(TAG, "Heif meta: " + r8 + "x" + r9 + ", rotation " + r10);     // Catch: RuntimeException -> L56 Throwable -> L64
    L54:
        r3.release();
        return;
    L9:
        if ("yes".equals(r7) == false) goto L12;
        r8 = r3.extractMetadata(18);     // Catch: Throwable -> L58 RuntimeException -> L60
        r9 = r3.extractMetadata(19);     // Catch: Throwable -> L58 RuntimeException -> L60
        r10 = r3.extractMetadata(24);     // Catch: Throwable -> L58 RuntimeException -> L60
    L64:
        th = th;
    L58:
        th = th;
    L63:
        throw new UnsupportedOperationException("Failed to read EXIF from HEIF file. Given stream is either malformed or unsupported.");     // Catch: Throwable -> L64
    L68:
        throw new UnsupportedOperationException("Reading EXIF from HEIF files is supported from SDK 28 and above");
    }

    private void getJpegAttributes(ByteOrderedDataInputStream r23, int r24, int r25) throws IOException {
        if (DEBUG == false) goto L5;
        Log.d(TAG, "getJpegAttributes starting with: " + r23);
    L5:
        r23.setByteOrder(ByteOrder.BIG_ENDIAN);
        byte r5 = r23.readByte();
        byte r8 = -1;
        if (r5 != (-1)) goto L69;
        int r6 = 1;
        int r3 = 0 + 1;
        if (r23.readByte() != (-40)) goto L67;
        int r4 = r3 + 1;
    L10:
        byte r7 = r23.readByte();
        if (r7 != r8) goto L65;
        int r9 = r4 + 1;
        byte r10 = r23.readByte();
        if (DEBUG == false) goto L15;
        Log.d(TAG, "Found JPEG segment indicator: " + Integer.toHexString(r10 & 255));
    L15:
        int r11 = r9 + r6;
        if (r10 == (-39)) goto L62;
        if (r10 == (-38)) goto L62;
        int r12 = r23.readUnsignedShort() - 2;
        int r13 = r11 + 2;
        if (DEBUG == false) goto L24;
        Log.d(TAG, "JPEG segment: " + Integer.toHexString(r10 & 255) + " (length: " + (r12 + 2) + ")");
    L24:
        if (r12 < 0) goto L60;
        switch(r10) {
            case -64: goto L46;
            case -63: goto L46;
            case -62: goto L46;
            case -61: goto L46;
            case -59: goto L46;
            case -58: goto L46;
            case -57: goto L46;
            case -55: goto L46;
            case -54: goto L46;
            case -53: goto L46;
            case -51: goto L46;
            case -50: goto L46;
            case -49: goto L46;
            case -31: goto L35;
            case -2: goto L27;
            default: goto L55;
        };
    L27:
        byte[] r14 = new byte[r12];
        if (r23.read(r14) != r12) goto L34;
        r12 = 0;
        if (getAttribute(TAG_USER_COMMENT) != null) goto L55;
        this.mAttributes[r6].put(TAG_USER_COMMENT, ExifAttribute.createString(new String(r14, ASCII)));
        goto L55
    L34:
        throw new IOException("Invalid exif");
    L35:
        byte[] r15 = new byte[r12];
        r23.readFully(r15);
        r13 = r13 + r12;
        r12 = 0;
        if (ExifInterfaceUtils.startsWith(r15, IDENTIFIER_EXIF_APP1) == false) goto L39;
        byte[] r16 = Arrays.copyOfRange(r15, IDENTIFIER_EXIF_APP1.length, r15.length);
        this.mOffsetToExifData = (r24 + r13) + IDENTIFIER_EXIF_APP1.length;
        readExifSegment(r16, r25);
        setThumbnailData(new ByteOrderedDataInputStream(r16));
        goto L55
    L39:
        if (ExifInterfaceUtils.startsWith(r15, IDENTIFIER_XMP_APP1) == false) goto L55;
        int r17 = IDENTIFIER_XMP_APP1.length + r13;
        byte[] r18 = Arrays.copyOfRange(r15, IDENTIFIER_XMP_APP1.length, r15.length);
        if (getAttribute(TAG_XMP) != null) goto L55;
        this.mAttributes[0].put(TAG_XMP, new ExifAttribute(1, r18.length, r17, r18));
        r6 = 1;
        this.mXmpIsFromSeparateMarker = true;
        goto L55
    L46:
        r23.skipFully(r6);
        HashMap<String, ExifAttribute> r19 = this.mAttributes[r25];
        if (r25 == 4) goto L49;
        String r110 = TAG_IMAGE_LENGTH;
    L50:
        r19.put(r110, ExifAttribute.createULong(r23.readUnsignedShort(), this.mExifByteOrder));
        HashMap<String, ExifAttribute> r20 = this.mAttributes[r25];
        if (r25 == 4) goto L53;
        String r21 = TAG_IMAGE_WIDTH;
    L54:
        r20.put(r21, ExifAttribute.createULong(r23.readUnsignedShort(), this.mExifByteOrder));
        r12 = r12 - 5;
        goto L55
    L53:
        r21 = TAG_THUMBNAIL_IMAGE_WIDTH;
        goto L54
    L49:
        r110 = TAG_THUMBNAIL_IMAGE_LENGTH;
    L55:
        if (r12 < 0) goto L58;
        r23.skipFully(r12);
        r4 = r13 + r12;
        r8 = -1;
        goto L10
    L58:
        throw new IOException("Invalid length");
    L60:
        throw new IOException("Invalid length");
    L62:
        r23.setByteOrder(this.mExifByteOrder);
        return;
    L65:
        throw new IOException("Invalid marker:" + Integer.toHexString(r7 & 255));
    L67:
        throw new IOException("Invalid marker: " + Integer.toHexString(r5 & 255));
    L69:
        throw new IOException("Invalid marker: " + Integer.toHexString(r5 & 255));
    }

    private int getMimeType(BufferedInputStream r3) throws IOException {
        r3.mark(5000);
        byte[] r0 = new byte[5000];
        r3.read(r0);
        r3.reset();
        if (isJpegFormat(r0) == false) goto L7;
        return 4;
    L7:
        if (isRafFormat(r0) == false) goto L11;
        return 9;
    L11:
        if (isHeifFormat(r0) == false) goto L15;
        return 12;
    L15:
        if (isOrfFormat(r0) == false) goto L19;
        return 7;
    L19:
        if (isRw2Format(r0) == false) goto L23;
        return 10;
    L23:
        if (isPngFormat(r0) == false) goto L27;
        return 13;
    L27:
        if (isWebpFormat(r0) == false) goto L30;
        return 14;
    L30:
        return 0;
    }

    private void getOrfAttributes(SeekableByteOrderedDataInputStream r17) throws IOException {
        getRawAttributes(r17);
        ExifAttribute r1 = this.mAttributes[1].get(TAG_MAKER_NOTE);
        if (r1 == null) goto L31;
        SeekableByteOrderedDataInputStream r3 = new SeekableByteOrderedDataInputStream(r1.bytes);
        r3.setByteOrder(this.mExifByteOrder);
        byte[] r4 = new byte[ORF_MAKER_NOTE_HEADER_1.length];
        r3.readFully(r4);
        r3.seek(0);
        byte[] r5 = new byte[ORF_MAKER_NOTE_HEADER_2.length];
        r3.readFully(r5);
        if (Arrays.equals(r4, ORF_MAKER_NOTE_HEADER_1) == false) goto L8;
        r3.seek(8);
    L10:
        readImageFileDirectory(r3, 6);
        ExifAttribute r6 = this.mAttributes[7].get(TAG_ORF_PREVIEW_IMAGE_START);
        ExifAttribute r7 = this.mAttributes[7].get(TAG_ORF_PREVIEW_IMAGE_LENGTH);
        if (r6 == null) goto L14;
        if (r7 == null) goto L14;
        this.mAttributes[5].put(TAG_JPEG_INTERCHANGE_FORMAT, r6);
        this.mAttributes[5].put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, r7);
    L14:
        ExifAttribute r8 = this.mAttributes[8].get(TAG_ORF_ASPECT_FRAME);
        if (r8 == null) goto L32;
        int[] r9 = (int[]) r8.getValue(this.mExifByteOrder);
        if (r9 != null) goto L19;
    L29:
        Log.w(TAG, "Invalid aspect frame values. frame=" + Arrays.toString(r9));
        return;
    L19:
        if (r9.length != 4) goto L29;
        if (r9[2] > r9[0]) goto L24;
        return;
    L24:
        if (r9[3] <= r9[1]) goto L34;
        int r10 = (r9[2] - r9[0]) + 1;
        int r11 = (r9[3] - r9[1]) + 1;
        if (r10 >= r11) goto L28;
        int r12 = r10 + r11;
        r11 = r12 - r11;
        r10 = r12 - r11;
    L28:
        ExifAttribute r2 = ExifAttribute.createUShort(r10, this.mExifByteOrder);
        ExifAttribute r13 = ExifAttribute.createUShort(r11, this.mExifByteOrder);
        this.mAttributes[0].put(TAG_IMAGE_WIDTH, r2);
        this.mAttributes[0].put(TAG_IMAGE_LENGTH, r13);
        return;
    L34:
        return;
    L32:
        return;
    L8:
        if (Arrays.equals(r5, ORF_MAKER_NOTE_HEADER_2) == false) goto L10;
        r3.seek(12);
        goto L10
    }

    private void getPngAttributes(ByteOrderedDataInputStream r11) throws IOException {
        if (DEBUG == false) goto L5;
        Log.d(TAG, "getPngAttributes starting with: " + r11);
    L5:
        r11.setByteOrder(ByteOrder.BIG_ENDIAN);
        r11.skipFully(PNG_SIGNATURE.length);
        int r0 = 0 + PNG_SIGNATURE.length;
    L37:
        int r1 = r11.readInt();     // Catch: EOFException -> L34
        int r2 = r0 + 4;     // Catch: EOFException -> L34
        byte[] r3 = new byte[4];     // Catch: EOFException -> L34
        if (r11.read(r3) != r3.length) goto L33;
        int r4 = r2 + 4;     // Catch: EOFException -> L34
        if (r4 != 16) goto L16;
        if (Arrays.equals(r3, PNG_CHUNK_TYPE_IHDR) == true) goto L16;
        throw new IOException("Encountered invalid PNG file--IHDR chunk should appearas the first chunk");     // Catch: EOFException -> L34
    L16:
        if (Arrays.equals(r3, PNG_CHUNK_TYPE_IEND) == true) goto L42;
        if (Arrays.equals(r3, PNG_CHUNK_TYPE_EXIF) == true) goto L20;
        r11.skipFully(r1 + 4);     // Catch: EOFException -> L34
        r0 = r4 + (r1 + 4);     // Catch: EOFException -> L34
        goto L37
    L20:
        byte[] r5 = new byte[r1];     // Catch: EOFException -> L34
        if (r11.read(r5) != r1) goto L30;
        int r6 = r11.readInt();     // Catch: EOFException -> L34
        CRC32 r7 = new CRC32();     // Catch: EOFException -> L34
        r7.update(r3);     // Catch: EOFException -> L34
        r7.update(r5);     // Catch: EOFException -> L34
        if (((int) r7.getValue()) != r6) goto L28;
        this.mOffsetToExifData = r4;     // Catch: EOFException -> L34
        readExifSegment(r5, 0);     // Catch: EOFException -> L34
        validateImages();     // Catch: EOFException -> L34
        setThumbnailData(new ByteOrderedDataInputStream(r5));     // Catch: EOFException -> L34
        return;
    L28:
        throw new IOException("Encountered invalid CRC value for PNG-EXIF chunk.\n recorded CRC value: " + r6 + ", calculated CRC value: " + r7.getValue());     // Catch: EOFException -> L34
    L30:
        throw new IOException("Failed to read given length for given PNG chunk type: " + ExifInterfaceUtils.byteArrayToHexString(r3));     // Catch: EOFException -> L34
    L42:
        return;
    L33:
        throw new IOException("Encountered invalid length while parsing PNG chunktype");     // Catch: EOFException -> L34
    L36:
        throw new IOException("Encountered corrupt PNG file.");
    }

    private void getRafAttributes(ByteOrderedDataInputStream r23) throws IOException {
        if (DEBUG == false) goto L5;
        Log.d(TAG, "getRafAttributes starting with: " + r23);
    L5:
        r23.skipFully(RAF_OFFSET_TO_JPEG_IMAGE_OFFSET);
        byte[] r4 = new byte[4];
        byte[] r5 = new byte[4];
        byte[] r2 = new byte[4];
        r23.read(r4);
        r23.read(r5);
        r23.read(r2);
        int r6 = ByteBuffer.wrap(r4).getInt();
        int r7 = ByteBuffer.wrap(r5).getInt();
        int r8 = ByteBuffer.wrap(r2).getInt();
        byte[] r9 = new byte[r7];
        r23.skipFully(r6 - r23.position());
        r23.read(r9);
        getJpegAttributes(new ByteOrderedDataInputStream(r9), r6, 5);
        r23.skipFully(r8 - r23.position());
        r23.setByteOrder(ByteOrder.BIG_ENDIAN);
        int r11 = r23.readInt();
        if (DEBUG == false) goto L8;
        Log.d(TAG, "numberOfDirectoryEntry: " + r11);
    L8:
        int r12 = 0;
    L9:
        if (r12 >= r11) goto L17;
        int r13 = r23.readUnsignedShort();
        int r14 = r23.readUnsignedShort();
        if (r13 == TAG_RAF_IMAGE_SIZE.number) goto L12;
        r23.skipFully(r14);
        r12 = r12 + 1;
        goto L9
    L12:
        short r15 = r23.readShort();
        short r3 = r23.readShort();
        ExifAttribute r10 = ExifAttribute.createUShort(r15, this.mExifByteOrder);
        ExifAttribute r16 = ExifAttribute.createUShort(r3, this.mExifByteOrder);
        this.mAttributes[0].put(TAG_IMAGE_LENGTH, r10);
        this.mAttributes[0].put(TAG_IMAGE_WIDTH, r16);
        if (DEBUG == false) goto L20;
        Log.d(TAG, "Updated to length: " + r15 + ", width: " + r3);
        return;
    L20:
        return;
    }

    private void getRawAttributes(SeekableByteOrderedDataInputStream r7) throws IOException {
        parseTiffHeaders(r7);
        readImageFileDirectory(r7, 0);
        updateImageSizeValues(r7, 0);
        updateImageSizeValues(r7, 5);
        updateImageSizeValues(r7, 4);
        validateImages();
        if (this.mMimeType != 8) goto L10;
        ExifAttribute r0 = this.mAttributes[1].get(TAG_MAKER_NOTE);
        if (r0 == null) goto L11;
        SeekableByteOrderedDataInputStream r2 = new SeekableByteOrderedDataInputStream(r0.bytes);
        r2.setByteOrder(this.mExifByteOrder);
        r2.skipFully(6);
        readImageFileDirectory(r2, 9);
        ExifAttribute r3 = this.mAttributes[9].get(TAG_COLOR_SPACE);
        if (r3 == null) goto L12;
        this.mAttributes[1].put(TAG_COLOR_SPACE, r3);
        return;
    L12:
        return;
    L11:
        return;
    }

    private void getRw2Attributes(SeekableByteOrderedDataInputStream r7) throws IOException {
        if (DEBUG == false) goto L5;
        Log.d(TAG, "getRw2Attributes starting with: " + r7);
    L5:
        getRawAttributes(r7);
        ExifAttribute r0 = this.mAttributes[0].get(TAG_RW2_JPG_FROM_RAW);
        if (r0 == null) goto L8;
        getJpegAttributes(new ByteOrderedDataInputStream(r0.bytes), (int) r0.bytesOffset, 5);
    L8:
        ExifAttribute r1 = this.mAttributes[0].get(TAG_RW2_ISO);
        ExifAttribute r2 = this.mAttributes[1].get(TAG_PHOTOGRAPHIC_SENSITIVITY);
        if (r1 == null) goto L13;
        if (r2 != null) goto L14;
        this.mAttributes[1].put(TAG_PHOTOGRAPHIC_SENSITIVITY, r1);
        return;
    L14:
        return;
    }

    private void getStandaloneAttributes(SeekableByteOrderedDataInputStream r3) throws IOException {
        r3.skipFully(IDENTIFIER_EXIF_APP1.length);
        byte[] r0 = new byte[r3.available()];
        r3.readFully(r0);
        this.mOffsetToExifData = IDENTIFIER_EXIF_APP1.length;
        readExifSegment(r0, 0);
    }

    private void getWebpAttributes(ByteOrderedDataInputStream r9) throws IOException {
        if (DEBUG == false) goto L5;
        Log.d(TAG, "getWebpAttributes starting with: " + r9);
    L5:
        r9.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        r9.skipFully(WEBP_SIGNATURE_1.length);
        int r0 = r9.readInt() + 8;
        r9.skipFully(WEBP_SIGNATURE_2.length);
        int r1 = 8 + WEBP_SIGNATURE_2.length;
    L34:
        byte[] r2 = new byte[4];     // Catch: EOFException -> L31
        if (r9.read(r2) != r2.length) goto L30;
        int r3 = r9.readInt();     // Catch: EOFException -> L31
        int r4 = (r1 + 4) + 4;     // Catch: EOFException -> L31
        if (Arrays.equals(WEBP_CHUNK_TYPE_EXIF, r2) == true) goto L11;
        if ((r3 % 2) != 1) goto L19;
        int r5 = r3 + 1;     // Catch: EOFException -> L31
    L21:
        if ((r4 + r5) == r0) goto L39;
        if ((r4 + r5) > r0) goto L28;
        r9.skipFully(r5);     // Catch: EOFException -> L31
        r1 = r4 + r5;     // Catch: EOFException -> L31
        goto L34
    L28:
        throw new IOException("Encountered WebP file with invalid chunk size");     // Catch: EOFException -> L31
    L39:
        return;
    L19:
        r5 = r3;
        goto L21
    L11:
        byte[] r6 = new byte[r3];     // Catch: EOFException -> L31
        if (r9.read(r6) != r3) goto L15;
        this.mOffsetToExifData = r4;     // Catch: EOFException -> L31
        readExifSegment(r6, 0);     // Catch: EOFException -> L31
        setThumbnailData(new ByteOrderedDataInputStream(r6));     // Catch: EOFException -> L31
        return;
    L15:
        throw new IOException("Failed to read given length for given PNG chunk type: " + ExifInterfaceUtils.byteArrayToHexString(r2));     // Catch: EOFException -> L31
    L30:
        throw new IOException("Encountered invalid length while parsing WebP chunktype");     // Catch: EOFException -> L31
    L33:
        throw new IOException("Encountered corrupt WebP file.");
    }

    private static Pair<Integer, Integer> guessDataFormat(String r13) {
        if (r13.contains(",") == false) goto L33;
        String[] r0 = r13.split(",", -1);
        Pair<Integer, Integer> r1 = guessDataFormat(r0[0]);
        if (((Integer) r1.first).intValue() != 2) goto L7;
        return r1;
    L7:
        int r2 = 1;
    L9:
        if (r2 >= r0.length) goto L31;
        Pair<Integer, Integer> r3 = guessDataFormat(r0[r2]);
        int r7 = -1;
        int r8 = -1;
        if (((Integer) r3.first).equals(r1.first) == false) goto L13;
    L14:
        r7 = ((Integer) r1.first).intValue();
    L16:
        if (((Integer) r1.second).intValue() != (-1)) goto L18;
    L22:
        if (r7 != (-1)) goto L26;
        if (r8 != (-1)) goto L26;
        return new Pair(2, -1);
    L26:
        if (r7 != (-1)) goto L28;
        r1 = new Pair(Integer.valueOf(r8), -1);
    L30:
        r2 = r2 + 1;
        goto L9
    L28:
        if (r8 != (-1)) goto L30;
        r1 = new Pair(Integer.valueOf(r7), -1);
        goto L30
    L18:
        if (((Integer) r3.first).equals(r1.second) == false) goto L20;
    L21:
        r8 = ((Integer) r1.second).intValue();
        goto L22
    L20:
        if (((Integer) r3.second).equals(r1.second) == false) goto L22;
    L13:
        if (((Integer) r3.second).equals(r1.first) == false) goto L16;
    L31:
        return r1;
    L33:
        if (r13.contains("/") == false) goto L75;
        String[] r4 = r13.split("/", -1);
        if (r4.length != 2) goto L54;
        long r5 = (long) Double.parseDouble(r4[0]);     // Catch: NumberFormatException -> L52
        long r9 = (long) Double.parseDouble(r4[1]);     // Catch: NumberFormatException -> L52
        if (r5 < 0) goto L51;
        if (r9 < 0) goto L51;
        if (r5 > 2147483647L) goto L49;
        if (r9 > 2147483647L) goto L49;
        return new Pair(10, 5);
    L49:
        return new Pair(5, -1);
    L51:
        return new Pair(10, -1);
    L54:
        return new Pair(2, -1);
    L75:
        Long r6 = Long.valueOf(Long.parseLong(r13));     // Catch: NumberFormatException -> L67
        if (r6.longValue() < 0) goto L62;
        if (r6.longValue() > 65535) goto L62;
        return new Pair(3, 4);
    L62:
        if (r6.longValue() >= 0) goto L66;
        return new Pair(9, -1);
    L66:
        return new Pair(4, -1);
    L73:
        Double.parseDouble(r13);     // Catch: NumberFormatException -> L70
        return new Pair(12, -1);
    L72:
        return new Pair(2, -1);
    }

    private void handleThumbnailFromJfif(ByteOrderedDataInputStream r8, HashMap r9) throws IOException {
        ExifAttribute r0 = (ExifAttribute) r9.get(TAG_JPEG_INTERCHANGE_FORMAT);
        ExifAttribute r1 = (ExifAttribute) r9.get(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        if (r0 == null) goto L22;
        if (r1 == null) goto L23;
        int r2 = r0.getIntValue(this.mExifByteOrder);
        int r3 = r1.getIntValue(this.mExifByteOrder);
        if (this.mMimeType != 7) goto L8;
        r2 = r2 + this.mOrfMakerNoteOffset;
    L8:
        if (r2 <= 0) goto L19;
        if (r3 <= 0) goto L19;
        this.mHasThumbnail = true;
        if (this.mFilename == null) goto L13;
    L17:
        this.mThumbnailOffset = r2;
        this.mThumbnailLength = r3;
        goto L19
    L13:
        if (this.mAssetInputStream != null) goto L17;
        if (this.mSeekableFileDescriptor != null) goto L17;
        byte[] r4 = new byte[r3];
        r8.skip(r2);
        r8.read(r4);
        this.mThumbnailBytes = r4;
    L19:
        if (DEBUG == false) goto L24;
        Log.d(TAG, "Setting thumbnail attributes with offset: " + r2 + ", length: " + r3);
        return;
    L24:
        return;
    L23:
        return;
    }

    private void handleThumbnailFromStrips(ByteOrderedDataInputStream r22, HashMap r23) throws IOException {
        ExifAttribute r3 = (ExifAttribute) r23.get(TAG_STRIP_OFFSETS);
        ExifAttribute r4 = (ExifAttribute) r23.get(TAG_STRIP_BYTE_COUNTS);
        if (r3 == null) goto L53;
        if (r4 == null) goto L53;
        long[] r5 = ExifInterfaceUtils.convertToLongArray(r3.getValue(this.mExifByteOrder));
        long[] r6 = ExifInterfaceUtils.convertToLongArray(r4.getValue(this.mExifByteOrder));
        if (r5 == null) goto L50;
        if (r5.length == 0) goto L50;
        if (r6 == null) goto L47;
        if (r6.length == 0) goto L47;
        if (r5.length == r6.length) goto L18;
        Log.w(TAG, "stripOffsets and stripByteCounts should have same length.");
        return;
    L18:
        long r8 = 0;
        int r10 = r6.length;
        int r12 = 0;
    L19:
        if (r12 >= r10) goto L21;
        r8 = r8 + r6[r12];
        r12 = r12 + 1;
        goto L19
    L21:
        byte[] r11 = new byte[(int) r8];
        int r13 = 0;
        int r14 = 0;
        boolean r15 = true;
        this.mAreThumbnailStripsConsecutive = true;
        this.mHasThumbnailStrips = true;
        this.mHasThumbnail = true;
        int r16 = 0;
    L22:
        boolean r17 = r15;
        if (r16 >= r5.length) goto L43;
        int r18 = r13;
        int r19 = (int) r5[r16];
        int r110 = r16;
        int r111 = (int) r6[r110];
        if (r110 >= (r5.length - 1)) goto L29;
        ExifAttribute r112 = r3;
        if ((r19 + r111) == r5[r110 + 1]) goto L30;
        this.mAreThumbnailStripsConsecutive = false;
    L30:
        int r2 = r19 - r18;
        if (r2 < 0) goto L32;
        ExifAttribute r113 = r4;
        if (r22.skip(r2) != r2) goto L36;
        int r7 = r18 + r2;
        byte[] r114 = new byte[r111];
        if (r22.read(r114) != r111) goto L40;
        int r1 = r7 + r111;
        System.arraycopy(r114, 0, r11, r14, r114.length);
        r14 = r14 + r114.length;
        r16 = r110 + 1;
        r13 = r1;
        r15 = r17;
        r3 = r112;
        r4 = r113;
        goto L22
    L40:
        Log.d(TAG, "Failed to read " + r111 + " bytes.");
        return;
    L36:
        Log.d(TAG, "Failed to skip " + r2 + " bytes.");
        return;
    L32:
        Log.d(TAG, "Invalid strip offset value");
        return;
    L29:
        r112 = r3;
        goto L30
    L43:
        this.mThumbnailBytes = r11;
        if (this.mAreThumbnailStripsConsecutive == false) goto L59;
        this.mThumbnailOffset = (int) r5[0];
        this.mThumbnailLength = r11.length;
        return;
    L59:
        return;
    L47:
        Log.w(TAG, "stripByteCounts should not be null or have zero length.");
        return;
    L50:
        Log.w(TAG, "stripOffsets should not be null or have zero length.");
        return;
    }

    private void initForFilename(String r4) throws IOException {
        if (r4 == null) goto L15;
        FileInputStream r0 = null;
        this.mAssetInputStream = null;
        this.mFilename = r4;
        r0 = new FileInputStream(r4);     // Catch: Throwable -> L11
        if (isSeekableFD(r0.getFD()) == false) goto L7;
        this.mSeekableFileDescriptor = r0.getFD();     // Catch: Throwable -> L11
    L8:
        loadAttributes(r0);     // Catch: Throwable -> L11
        ExifInterfaceUtils.closeQuietly(r0);
        return;
    L7:
        this.mSeekableFileDescriptor = null;     // Catch: Throwable -> L11
    L11:
        th = move-exception;
        ExifInterfaceUtils.closeQuietly(r0);
        throw th;
    L15:
        throw new NullPointerException("filename cannot be null");
    }

    private static boolean isExifDataOnly(BufferedInputStream r4) throws IOException {
        r4.mark(IDENTIFIER_EXIF_APP1.length);
        byte[] r0 = new byte[IDENTIFIER_EXIF_APP1.length];
        r4.read(r0);
        r4.reset();
        int r1 = 0;
    L4:
        if (r1 >= IDENTIFIER_EXIF_APP1.length) goto L10;
        if (r0[r1] != IDENTIFIER_EXIF_APP1[r1]) goto L7;
        r1 = r1 + 1;
        goto L4
    L7:
        return false;
    L10:
        return true;
    }

    private boolean isHeifFormat(byte[] r21) throws IOException {
        ByteOrderedDataInputStream r2 = null;
        boolean r3 = false;
        r2 = new ByteOrderedDataInputStream(r21);     // Catch: Throwable -> L49 Exception -> L51
        long r4 = r2.readInt();     // Catch: Throwable -> L49 Exception -> L51
        byte[] r6 = new byte[4];     // Catch: Throwable -> L49 Exception -> L51
        r2.read(r6);     // Catch: Throwable -> L49 Exception -> L51
        if (Arrays.equals(r6, HEIF_TYPE_FTYP) == true) goto L7;
        r2.close();
        return false;
    L7:
        long r7 = 8;
        if (r4 != 1) goto L16;
        r4 = r2.readLong();     // Catch: Throwable -> L49 Exception -> L51
        if (r4 >= 16) goto L14;
        r2.close();
        return false;
    L14:
        r7 = 8 + 8;
    L16:
        if (r4 > r21.length) goto L18;
    L19:
        long r14 = r4 - r7;
        if (r14 >= 8) goto L23;
        r2.close();
        return false;
    L23:
        byte[] r0 = new byte[4];     // Catch: Throwable -> L49 Exception -> L51
        boolean r11 = false;
        boolean r12 = false;
        long r16 = 0;
    L24:
        if (r16 >= (r14 / 4)) goto L48;
        boolean r18 = r3;
    L46:
        e = e;
    L54:
        if (DEBUG == false) goto L56;
        Log.d(TAG, "Exception parsing HEIF file type box.", e);     // Catch: Throwable -> L49
    L56:
        if (r2 == null) goto L59;
    L57:
        r2.close();
    L59:
        return r18;
    L29:
        if (r2.read(r0) != r0.length) goto L30;
        if (r16 == 1) goto L45;
        if (Arrays.equals(r0, HEIF_BRAND_MIF1) == false) goto L38;
        r11 = true;
    L41:
        if (r11 == false) goto L45;
        if (r12 == false) goto L45;
        r2.close();
        return true;
    L38:
        if (Arrays.equals(r0, HEIF_BRAND_HEIC) == false) goto L41;
        r12 = true;
    L45:
        r16 = r16 + 1;
        r3 = r18;
        goto L24
    L30:
        r2.close();
        return r18;
    L48:
        r18 = r3;
        goto L57
    L18:
        r4 = r21.length;     // Catch: Throwable -> L49 Exception -> L51
    L49:
        th = move-exception;
        if (0 == 0) goto L62;
        r2.close();
    L62:
        throw th;
    L51:
        e = e;
        r18 = r3;
        goto L54
    }

    private static boolean isJpegFormat(byte[] r3) throws IOException {
        int r0 = 0;
    L4:
        if (r0 >= JPEG_SIGNATURE.length) goto L10;
        if (r3[r0] != JPEG_SIGNATURE[r0]) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return false;
    L10:
        return true;
    }

    private boolean isOrfFormat(byte[] r5) throws IOException {
        ByteOrderedDataInputStream r0 = null;
        boolean r1 = false;
        r0 = new ByteOrderedDataInputStream(r5);     // Catch: Throwable -> L11 Exception -> L15
        this.mExifByteOrder = readByteOrder(r0);     // Catch: Throwable -> L11 Exception -> L15
        r0.setByteOrder(this.mExifByteOrder);     // Catch: Throwable -> L11 Exception -> L15
        short r2 = r0.readShort();     // Catch: Throwable -> L11 Exception -> L15
        goto L16
    L5:
        if (r2 != 20306) goto L7;
    L8:
        r1 = true;
    L9:
        r0.close();
        return r1;
    L7:
        if (r2 != 21330) goto L9;
    L11:
        th = move-exception;
        if (r0 == null) goto L14;
        r0.close();
    L14:
        throw th;
    L16:
        if (r0 == null) goto L18;
        r0.close();
    L18:
        return false;
    }

    private boolean isPngFormat(byte[] r4) throws IOException {
        int r0 = 0;
    L4:
        if (r0 >= PNG_SIGNATURE.length) goto L10;
        if (r4[r0] != PNG_SIGNATURE[r0]) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return false;
    L10:
        return true;
    }

    private boolean isRafFormat(byte[] r5) throws IOException {
        byte[] r0 = RAF_SIGNATURE.getBytes(Charset.defaultCharset());
        int r1 = 0;
    L4:
        if (r1 >= r0.length) goto L10;
        if (r5[r1] != r0[r1]) goto L7;
        r1 = r1 + 1;
        goto L4
    L7:
        return false;
    L10:
        return true;
    }

    private boolean isRw2Format(byte[] r5) throws IOException {
        ByteOrderedDataInputStream r0 = null;
        boolean r1 = false;
        r0 = new ByteOrderedDataInputStream(r5);     // Catch: Throwable -> L9 Exception -> L13
        this.mExifByteOrder = readByteOrder(r0);     // Catch: Throwable -> L9 Exception -> L13
        r0.setByteOrder(this.mExifByteOrder);     // Catch: Throwable -> L9 Exception -> L13
        goto L14
    L5:
        if (r0.readShort() != 85) goto L7;
        r1 = true;
    L7:
        r0.close();
        return r1;
    L9:
        th = move-exception;
        if (r0 == null) goto L12;
        r0.close();
    L12:
        throw th;
    L14:
        if (r0 == null) goto L16;
        r0.close();
    L16:
        return false;
    }

    private static boolean isSeekableFD(FileDescriptor r3) {
        ExifInterfaceUtils.Api21Impl.lseek(r3, 0, OsConstants.SEEK_CUR);     // Catch: Exception -> L5
        return true;
    L7:
        if (DEBUG == false) goto L13;
        Log.d(TAG, "The file descriptor for the given input is not seekable");
        return false;
    L13:
        return false;
    }

    private boolean isSupportedDataType(HashMap r7) throws IOException {
        ExifAttribute r0 = (ExifAttribute) r7.get(TAG_BITS_PER_SAMPLE);
        if (r0 == null) goto L21;
        int[] r1 = (int[]) r0.getValue(this.mExifByteOrder);
        if (Arrays.equals(BITS_PER_SAMPLE_RGB, r1) == false) goto L8;
        return true;
    L8:
        if (this.mMimeType != 3) goto L21;
        ExifAttribute r2 = (ExifAttribute) r7.get(TAG_PHOTOMETRIC_INTERPRETATION);
        if (r2 == null) goto L21;
        int r4 = r2.getIntValue(this.mExifByteOrder);
        if (r4 != 1) goto L16;
        if (Arrays.equals(r1, BITS_PER_SAMPLE_GREYSCALE_2) == false) goto L16;
    L19:
        return true;
    L16:
        if (r4 != 6) goto L21;
        if (Arrays.equals(r1, BITS_PER_SAMPLE_RGB) == true) goto L19;
    L21:
        if (DEBUG == false) goto L25;
        Log.d(TAG, "Unsupported data type value");
        return false;
    L25:
        return false;
    }

    private static boolean isSupportedFormatForSavingAttributes(int r1) {
        if (r1 != 4) goto L5;
        return true;
    L5:
        if (r1 != 13) goto L7;
        return true;
    L7:
        if (r1 == 14) goto L14;
        return false;
    L14:
        return true;
    }

    public static boolean isSupportedMimeType(String r4) {
        if (r4 == null) goto L56;
        String r0 = r4.toLowerCase(Locale.ROOT);
        switch(r0.hashCode()) {
            case -1875291391: goto L49;
            case -1635437028: goto L46;
            case -1594371159: goto L43;
            case -1487464693: goto L40;
            case -1487464690: goto L37;
            case -1487394660: goto L34;
            case -1487018032: goto L31;
            case -1423313290: goto L28;
            case -985160897: goto L25;
            case -879258763: goto L22;
            case -332763809: goto L19;
            case 1378106698: goto L16;
            case 2099152104: goto L13;
            case 2099152524: goto L10;
            case 2111234748: goto L7;
            default: goto L51;
        };
    L51:
        byte r1 = -1;
    L52:
        switch(r1) {
            case 0: goto L54;
            case 1: goto L54;
            case 2: goto L54;
            case 3: goto L54;
            case 4: goto L54;
            case 5: goto L54;
            case 6: goto L54;
            case 7: goto L54;
            case 8: goto L54;
            case 9: goto L54;
            case 10: goto L54;
            case 11: goto L54;
            case 12: goto L54;
            case 13: goto L54;
            case 14: goto L54;
            default: goto L53;
        };
    L53:
        return false;
    L54:
        return true;
    L7:
        if (r0.equals("image/x-canon-cr2") == false) goto L51;
        r1 = 2;
        goto L52
    L10:
        if (r0.equals("image/x-nikon-nrw") == false) goto L51;
        r1 = 4;
        goto L52
    L13:
        if (r0.equals("image/x-nikon-nef") == false) goto L51;
        r1 = 3;
        goto L52
    L16:
        if (r0.equals("image/x-olympus-orf") == false) goto L51;
        r1 = 7;
        goto L52
    L19:
        if (r0.equals("image/x-pentax-pef") == false) goto L51;
        r1 = 8;
        goto L52
    L22:
        if (r0.equals(MimeTypes.IMAGE_PNG) == false) goto L51;
        r1 = Ascii.f79CR;
        goto L52
    L25:
        if (r0.equals("image/x-panasonic-rw2") == false) goto L51;
        r1 = 6;
        goto L52
    L28:
        if (r0.equals("image/x-adobe-dng") == false) goto L51;
        r1 = 1;
        goto L52
    L31:
        if (r0.equals(MimeTypes.IMAGE_WEBP) == false) goto L51;
        r1 = Ascii.f89SO;
        goto L52
    L34:
        if (r0.equals(MimeTypes.IMAGE_JPEG) == false) goto L51;
        r1 = 0;
        goto L52
    L37:
        if (r0.equals(MimeTypes.IMAGE_HEIF) == false) goto L51;
        r1 = Ascii.f81FF;
        goto L52
    L40:
        if (r0.equals(MimeTypes.IMAGE_HEIC) == false) goto L51;
        r1 = Ascii.f92VT;
        goto L52
    L43:
        if (r0.equals("image/x-sony-arw") == false) goto L51;
        r1 = 5;
        goto L52
    L46:
        if (r0.equals("image/x-samsung-srw") == false) goto L51;
        r1 = 9;
        goto L52
    L49:
        if (r0.equals("image/x-fuji-raf") == false) goto L51;
        r1 = 10;
        goto L52
    L56:
        throw new NullPointerException("mimeType shouldn't be null");
    }

    private boolean isThumbnail(HashMap r6) throws IOException {
        ExifAttribute r0 = (ExifAttribute) r6.get(TAG_IMAGE_LENGTH);
        ExifAttribute r1 = (ExifAttribute) r6.get(TAG_IMAGE_WIDTH);
        if (r0 == null) goto L10;
        if (r1 == null) goto L12;
        int r2 = r0.getIntValue(this.mExifByteOrder);
        int r3 = r1.getIntValue(this.mExifByteOrder);
        if (r2 > 512) goto L13;
        if (r3 > 512) goto L14;
        return true;
    L14:
        return false;
    L13:
        return false;
    L12:
        return false;
    L10:
        return false;
    }

    private boolean isWebpFormat(byte[] r5) throws IOException {
        int r0 = 0;
    L4:
        if (r0 >= WEBP_SIGNATURE_1.length) goto L9;
        if (r5[r0] != WEBP_SIGNATURE_1[r0]) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return false;
    L9:
        int r1 = 0;
    L11:
        if (r1 >= WEBP_SIGNATURE_2.length) goto L16;
        if (r5[(WEBP_SIGNATURE_1.length + r1) + 4] != WEBP_SIGNATURE_2[r1]) goto L14;
        r1 = r1 + 1;
        goto L11
    L14:
        return false;
    L16:
        return true;
    }

    private void loadAttributes(InputStream r4) {
        if (r4 == null) goto L58;
        int r0 = 0;
    L59:
    L41:
        th = move-exception;
        addDefaultValuesForCompatibility();
        if (DEBUG == false) goto L56;
        printAttributes();
    L56:
        throw th;
    L45:
        e = move-exception;
        if (DEBUG == false) goto L49;
        Log.w(TAG, "Invalid image: ExifInterface got an unsupported image format file(ExifInterface supports JPEG and some RAW image formats only) or a corrupted JPEG file to ExifInterface.", e);     // Catch: Throwable -> L41
    L49:
        addDefaultValuesForCompatibility();
        if (DEBUG == false) goto L62;
    L40:
        printAttributes();
        return;
    L62:
        return;
    L5:
        if (r0 >= EXIF_TAGS.length) goto L8;
        this.mAttributes[r0] = new HashMap();     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
        r0 = r0 + 1;     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
        goto L59
    L8:
        if (this.mIsExifDataOnly == true) goto L11;
        r4 = new BufferedInputStream(r4, 5000);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
        this.mMimeType = getMimeType((BufferedInputStream) r4);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
    L11:
        if (shouldSupportSeek(this.mMimeType) == false) goto L26;
        SeekableByteOrderedDataInputStream r1 = new SeekableByteOrderedDataInputStream(r4);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
        if (this.mIsExifDataOnly == false) goto L16;
        getStandaloneAttributes(r1);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
    L25:
        r1.seek(this.mOffsetToExifData);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
        setThumbnailData(r1);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
    L38:
        addDefaultValuesForCompatibility();
        if (DEBUG == true) goto L40;
        return;
    L16:
        if (this.mMimeType != 12) goto L19;
        getHeifAttributes(r1);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
        goto L25
    L19:
        if (this.mMimeType != 7) goto L22;
        getOrfAttributes(r1);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
        goto L25
    L22:
        if (this.mMimeType != 10) goto L24;
        getRw2Attributes(r1);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
        goto L25
    L24:
        getRawAttributes(r1);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
        goto L25
    L26:
        ByteOrderedDataInputStream r2 = new ByteOrderedDataInputStream(r4);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
        if (this.mMimeType != 4) goto L30;
        getJpegAttributes(r2, 0, 0);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
        goto L38
    L30:
        if (this.mMimeType != 13) goto L33;
        getPngAttributes(r2);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
        goto L38
    L33:
        if (this.mMimeType != 9) goto L36;
        getRafAttributes(r2);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
        goto L38
    L36:
        if (this.mMimeType != 14) goto L38;
        getWebpAttributes(r2);     // Catch: Throwable -> L41 UnsupportedOperationException -> L43 Throwable -> L45
        goto L38
    L58:
        throw new NullPointerException("inputstream shouldn't be null");
    }

    private static Long parseDateTime(String r13, String r14, String r15) {
        if (r13 != null) goto L5;
    L35:
        return null;
    L5:
        if (NON_ZERO_TIME_PATTERN.matcher(r13).matches() == false) goto L35;
        ParsePosition r1 = new ParsePosition(0);
        Date r3 = sFormatterPrimary.parse(r13, r1);     // Catch: IllegalArgumentException -> L33
        if (r3 != null) goto L13;
        r3 = sFormatterSecondary.parse(r13, r1);     // Catch: IllegalArgumentException -> L33
        if (r3 != null) goto L13;
        return null;
    L13:
        long r4 = r3.getTime();     // Catch: IllegalArgumentException -> L33
        if (r15 == null) goto L29;
        int r6 = 1;
        String r2 = r15.substring(0, 1);     // Catch: IllegalArgumentException -> L33
        int r8 = Integer.parseInt(r15.substring(1, 3));     // Catch: IllegalArgumentException -> L33
        int r9 = Integer.parseInt(r15.substring(4, 6));     // Catch: IllegalArgumentException -> L33
        if ("+".equals(r2) == true) goto L21;
        if ("-".equals(r2) == false) goto L29;
    L21:
        if (":".equals(r15.substring(3, 4)) == false) goto L29;
        if (r8 > 14) goto L29;
        int r7 = (((r8 * 60) + r9) * 60) * 1000;     // Catch: IllegalArgumentException -> L33
        if ("-".equals(r2) == true) goto L28;
        r6 = -1;
    L28:
        r4 = r4 + ((long) (r7 * r6));     // Catch: IllegalArgumentException -> L33
    L29:
        if (r14 == null) goto L31;
        r4 = r4 + ExifInterfaceUtils.parseSubSeconds(r14);     // Catch: IllegalArgumentException -> L33
    L31:
        return Long.valueOf(r4);
    L34:
        return null;
    }

    private void parseTiffHeaders(ByteOrderedDataInputStream r6) throws IOException {
        this.mExifByteOrder = readByteOrder(r6);
        r6.setByteOrder(this.mExifByteOrder);
        int r0 = r6.readUnsignedShort();
        if (this.mMimeType != 7) goto L5;
    L11:
        int r1 = r6.readInt();
        if (r1 < 8) goto L18;
        int r2 = r1 - 8;
        if (r2 <= 0) goto L19;
        r6.skipFully(r2);
        return;
    L19:
        return;
    L18:
        throw new IOException("Invalid first Ifd offset: " + r1);
    L5:
        if (this.mMimeType == 10) goto L11;
        if (r0 == 42) goto L11;
        throw new IOException("Invalid start code: " + Integer.toHexString(r0));
    }

    private void printAttributes() {
        int r0 = 0;
    L4:
        if (r0 >= this.mAttributes.length) goto L10;
        Log.d(TAG, "The size of tag group[" + r0 + "]: " + this.mAttributes[r0].size());
        Iterator<Map.Entry<String, ExifAttribute>> r1 = this.mAttributes[r0].entrySet().iterator();
    L7:
        if (r1.hasNext() == false) goto L9;
        Map.Entry<String, ExifAttribute> r3 = r1.next();
        ExifAttribute r4 = r3.getValue();
        Log.d(TAG, "tagName: " + r3.getKey() + ", tagType: " + r4.toString() + ", tagValue: '" + r4.getStringValue(this.mExifByteOrder) + "'");
        goto L7
    L9:
        r0 = r0 + 1;
        goto L4
    }

    private ByteOrder readByteOrder(ByteOrderedDataInputStream r5) throws IOException {
        short r0 = r5.readShort();
        switch(r0) {
            case 18761: goto L12;
            case 19789: goto L7;
            default: goto L5;
        };
    L5:
        throw new IOException("Invalid byte order: " + Integer.toHexString(r0));
    L7:
        if (DEBUG == false) goto L10;
        Log.d(TAG, "readExifSegment: Byte Align MM");
    L10:
        return ByteOrder.BIG_ENDIAN;
    L12:
        if (DEBUG == false) goto L15;
        Log.d(TAG, "readExifSegment: Byte Align II");
    L15:
        return ByteOrder.LITTLE_ENDIAN;
    }

    private void readExifSegment(byte[] r2, int r3) throws IOException {
        SeekableByteOrderedDataInputStream r0 = new SeekableByteOrderedDataInputStream(r2);
        parseTiffHeaders(r0);
        readImageFileDirectory(r0, r3);
    }

    private void readImageFileDirectory(SeekableByteOrderedDataInputStream r32, int r33) throws IOException {
        this.mAttributesOffsets.add(Integer.valueOf(r32.mPosition));
        short r3 = r32.readShort();
        if (DEBUG == false) goto L5;
        Log.d(TAG, "numberOfDirectoryEntry: " + r3);
    L5:
        if (r3 > 0) goto L7;
        return;
    L7:
        short r4 = 0;
    L9:
        if (r4 >= r3) goto L118;
        int r12 = r32.readUnsignedShort();
        int r13 = r32.readUnsignedShort();
        int r14 = r32.readInt();
        long r7 = ((long) r32.position()) + 4;
        ExifTag r9 = sExifTagMapsForReading[r33].get(Integer.valueOf(r12));
        if (DEBUG == false) goto L17;
        Integer r15 = Integer.valueOf(r33);
        Integer r23 = Integer.valueOf(r12);
        if (r9 == null) goto L15;
        int r24 = 3;
        String r11 = r9.name;
    L16:
        Integer r25 = Integer.valueOf(r13);
        Integer r26 = Integer.valueOf(r14);
        Object[] r6 = new Object[5];
        r6[0] = r15;
        r6[1] = r23;
        r6[2] = r11;
        r6[r24] = r25;
        r6[4] = r26;
        Log.d(TAG, String.format("ifdType: %d, tagNumber: %d, tagName: %s, dataFormat: %d, numberOfComponents: %d", r6));
    L18:
        boolean r8 = false;
        if (r9 == null) goto L21;
        if (r13 <= 0) goto L48;
        if (r13 >= IFD_FORMAT_BYTES_PER_FORMAT.length) goto L48;
        if (r9.isFormatCompatible(r13) == false) goto L31;
        if (r13 != 7) goto L36;
        r13 = r9.primaryFormat;
    L36:
        r12 = r12;
        r3 = r3;
        r4 = r4;
        long r27 = ((long) r14) * ((long) IFD_FORMAT_BYTES_PER_FORMAT[r13]);
        if (r27 < 0) goto L43;
        if (r27 > 2147483647L) goto L43;
        r8 = true;
        int r16 = r13;
        long r5 = r27;
    L51:
        if (r8 == true) goto L54;
        r32.seek(r7);
    L117:
        r4 = (short) (r4 + 1);
        r3 = r3;
        goto L9
    L54:
        if (r5 <= 4) goto L73;
        int r17 = r32.readInt();
        if (DEBUG == false) goto L60;
        Log.d(TAG, "seek to data offset: " + r17);
    L60:
        if (this.mMimeType != 7) goto L72;
        if (TAG_MAKER_NOTE.equals(r9.name) == false) goto L65;
        this.mOrfMakerNoteOffset = r17;
        goto L72
    L65:
        if (r33 != 6) goto L72;
        if (TAG_ORF_THUMBNAIL_IMAGE.equals(r9.name) == false) goto L72;
        this.mOrfThumbnailOffset = r17;
        this.mOrfThumbnailLength = r14;
        ExifAttribute r10 = ExifAttribute.createUShort(6, this.mExifByteOrder);
        ExifAttribute r2 = ExifAttribute.createULong(this.mOrfThumbnailOffset, this.mExifByteOrder);
        ExifAttribute r18 = ExifAttribute.createULong(this.mOrfThumbnailLength, this.mExifByteOrder);
        this.mAttributes[4].put(TAG_COMPRESSION, r10);
        this.mAttributes[4].put(TAG_JPEG_INTERCHANGE_FORMAT, r2);
        this.mAttributes[4].put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, r18);
    L72:
        r32.seek(r17);
    L74:
        Integer r19 = sExifPointerTagMap.get(Integer.valueOf(r12));
        if (DEBUG == false) goto L77;
        Log.d(TAG, "nextIfdType: " + r19 + " byteCount: " + r5);
    L77:
        if (r19 == null) goto L100;
        long r110 = -1;
        switch(r16) {
            case 3: goto L84;
            case 4: goto L83;
            case 8: goto L82;
            case 9: goto L81;
            case 13: goto L81;
            default: goto L86;
        };
    L81:
        r110 = r32.readInt();
        goto L86
    L82:
        r110 = r32.readShort();
        goto L86
    L83:
        r110 = r32.readUnsignedInt();
        goto L86
    L84:
        r110 = r32.readUnsignedShort();
    L86:
        if (DEBUG == false) goto L89;
        Log.d(TAG, String.format("Offset: %d, tagName: %s", new Object[]{Long.valueOf(r110), r9.name}));
    L89:
        if (r110 <= 0) goto L97;
        if (this.mAttributesOffsets.contains(Integer.valueOf((int) r110)) == true) goto L94;
        r32.seek(r110);
        readImageFileDirectory(r32, r19.intValue());
    L99:
        r32.seek(r7);
        goto L117
    L94:
        if (DEBUG == false) goto L99;
        Log.d(TAG, "Skip jump into the IFD since it has already been read: IfdType " + r19 + " (at " + r110 + ")");
        goto L99
    L97:
        if (DEBUG == false) goto L99;
        Log.d(TAG, "Skip jump into the IFD since its offset is invalid: " + r110);
        goto L99
    L100:
        int r20 = r32.position() + this.mOffsetToExifData;
        byte[] r111 = new byte[(int) r5];
        r32.readFully(r111);
        ExifAttribute r112 = new ExifAttribute(r16, r14, r20, r111);
        this.mAttributes[r33].put(r9.name, r112);
        if (TAG_DNG_VERSION.equals(r9.name) == false) goto L104;
        this.mMimeType = r24;
    L104:
        if (TAG_MAKE.equals(r9.name) == true) goto L108;
        if (TAG_MODEL.equals(r9.name) == true) goto L108;
    L110:
        if (TAG_COMPRESSION.equals(r9.name) == false) goto L115;
        if (r112.getIntValue(this.mExifByteOrder) != 65535) goto L115;
    L113:
        this.mMimeType = 8;
    L115:
        if (r32.position() == r7) goto L117;
        r32.seek(r7);
    L108:
        if (r112.getStringValue(this.mExifByteOrder).contains(PEF_SIGNATURE) == true) goto L113;
    L73:
        r12 = r12;
        r14 = r14;
    L43:
        if (DEBUG == false) goto L45;
        Log.d(TAG, "Skip the tag entry since the number of components is invalid: " + r14);
    L45:
        r16 = r13;
        r5 = r27;
        goto L51
    L31:
        if (DEBUG == false) goto L33;
        Log.d(TAG, "Skip the tag entry since data format (" + IFD_FORMAT_NAMES[r13] + ") is unexpected for tag: " + r9.name);
        r3 = r3;
        r4 = r4;
        r12 = r12;
    L50:
        r16 = r13;
        r5 = 0;
        goto L51
    L33:
        r3 = r3;
        r4 = r4;
        r12 = r12;
    L48:
        if (DEBUG == false) goto L50;
        Log.d(TAG, "Skip the tag entry since data format is invalid: " + r13);
        goto L50
    L21:
        if (DEBUG == false) goto L23;
        Log.d(TAG, "Skip the tag entry since tag number is not defined: " + r12);
        r3 = r3;
        r4 = r4;
        r12 = r12;
        goto L50
    L23:
        r3 = r3;
        r4 = r4;
        r12 = r12;
        goto L50
    L15:
        r24 = 3;
        r11 = null;
        goto L16
    L17:
        r24 = 3;
        goto L18
    L118:
        int r21 = r32.readInt();
        if (DEBUG == false) goto L122;
        Log.d(TAG, String.format("nextIfdOffset: %d", new Object[]{Integer.valueOf(r21)}));
    L122:
        if (r21 <= 0) goto L135;
        if (this.mAttributesOffsets.contains(Integer.valueOf(r21)) == true) goto L132;
        r32.seek(r21);
        if (this.mAttributes[4].isEmpty() == false) goto L129;
        readImageFileDirectory(r32, 4);
        return;
    L129:
        if (this.mAttributes[5].isEmpty() == false) goto L144;
        readImageFileDirectory(r32, 5);
        return;
    L144:
        return;
    L132:
        if (DEBUG == false) goto L146;
        Log.d(TAG, "Stop reading file since re-reading an IFD may cause an infinite loop: " + r21);
        return;
    L146:
        return;
    L135:
        if (DEBUG == false) goto L148;
        Log.d(TAG, "Stop reading file since a wrong offset may cause an infinite loop: " + r21);
        return;
    }

    private void removeAttribute(String r3) {
        int r0 = 0;
    L4:
        if (r0 >= EXIF_TAGS.length) goto L6;
        this.mAttributes[r0].remove(r3);
        r0 = r0 + 1;
        goto L4
    }

    private void replaceInvalidTags(int r3, String r4, String r5) {
        if (this.mAttributes[r3].isEmpty() == false) goto L5;
        return;
    L5:
        if (this.mAttributes[r3].get(r4) == null) goto L9;
        this.mAttributes[r3].put(r5, this.mAttributes[r3].get(r4));
        this.mAttributes[r3].remove(r4);
        return;
    }

    private void retrieveJpegImageSize(SeekableByteOrderedDataInputStream r9, int r10) throws IOException {
        ExifAttribute r0 = this.mAttributes[r10].get(TAG_IMAGE_LENGTH);
        ExifAttribute r1 = this.mAttributes[r10].get(TAG_IMAGE_WIDTH);
        if (r0 == null) goto L5;
        if (r1 == null) goto L5;
        return;
    L5:
        ExifAttribute r2 = this.mAttributes[r10].get(TAG_JPEG_INTERCHANGE_FORMAT);
        ExifAttribute r3 = this.mAttributes[r10].get(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        if (r2 == null) goto L11;
        if (r3 == null) goto L12;
        int r4 = r2.getIntValue(this.mExifByteOrder);
        int r5 = r2.getIntValue(this.mExifByteOrder);
        r9.seek(r4);
        byte[] r6 = new byte[r5];
        r9.read(r6);
        getJpegAttributes(new ByteOrderedDataInputStream(r6), r4, r10);
        return;
    L12:
        return;
    }

    private void saveJpegAttributes(InputStream r13, OutputStream r14) throws IOException {
        if (DEBUG == false) goto L5;
        Log.d(TAG, "saveJpegAttributes starting with (inputStream: " + r13 + ", outputStream: " + r14 + ")");
    L5:
        ByteOrderedDataInputStream r0 = new ByteOrderedDataInputStream(r13);
        ByteOrderedDataOutputStream r1 = new ByteOrderedDataOutputStream(r14, ByteOrder.BIG_ENDIAN);
        if (r0.readByte() != (-1)) goto L59;
        r1.writeByte(-1);
        if (r0.readByte() != (-40)) goto L57;
        r1.writeByte(-40);
        ExifAttribute r2 = null;
        if (getAttribute(TAG_XMP) != null) goto L12;
    L14:
        r1.writeByte(-1);
        r1.writeByte(-31);
        writeExifSegment(r1);
        if (r2 == null) goto L17;
        this.mAttributes[0].put(TAG_XMP, r2);
    L17:
        byte[] r5 = new byte[4096];
    L19:
        if (r0.readByte() != (-1)) goto L55;
        byte r6 = r0.readByte();
        switch(r6) {
            case -39: goto L45;
            case -38: goto L45;
            case -31: goto L25;
            default: goto L22;
        };
    L25:
        int r9 = r0.readUnsignedShort() - 2;
        if (r9 < 0) goto L44;
        byte[] r10 = new byte[6];
        if (r9 < 6) goto L36;
        if (r0.read(r10) != 6) goto L35;
        if (Arrays.equals(r10, IDENTIFIER_EXIF_APP1) == false) goto L36;
        r0.skipFully(r9 - 6);
        goto L19
    L35:
        throw new IOException("Invalid exif");
    L36:
        r1.writeByte(-1);
        r1.writeByte(r6);
        r1.writeUnsignedShort(r9 + 2);
        if (r9 < 6) goto L39;
        r9 = r9 - 6;
        r1.write(r10);
    L39:
        if (r9 <= 0) goto L19;
        int r8 = r0.read(r5, 0, Math.min(r9, r5.length));
        if (r8 < 0) goto L19;
        r1.write(r5, 0, r8);
        r9 = r9 - r8;
        goto L39
    L44:
        throw new IOException("Invalid length");
    L45:
        r1.writeByte(-1);
        r1.writeByte(r6);
        ExifInterfaceUtils.copy(r0, r1);
        return;
    L22:
        r1.writeByte(-1);
        r1.writeByte(r6);
        int r11 = r0.readUnsignedShort();
        r1.writeUnsignedShort(r11);
        int r12 = r11 - 2;
        if (r12 < 0) goto L53;
    L47:
        if (r12 <= 0) goto L19;
        int r15 = r0.read(r5, 0, Math.min(r12, r5.length));
        if (r15 < 0) goto L19;
        r1.write(r5, 0, r15);
        r12 = r12 - r15;
        goto L47
    L53:
        throw new IOException("Invalid length");
    L55:
        throw new IOException("Invalid marker");
    L12:
        if (this.mXmpIsFromSeparateMarker == false) goto L14;
        r2 = this.mAttributes[0].remove(TAG_XMP);
        goto L14
    L57:
        throw new IOException("Invalid marker");
    L59:
        throw new IOException("Invalid marker");
    }

    private void savePngAttributes(InputStream r10, OutputStream r11) throws IOException {
        if (DEBUG == false) goto L5;
        Log.d(TAG, "savePngAttributes starting with (inputStream: " + r10 + ", outputStream: " + r11 + ")");
    L5:
        ByteOrderedDataInputStream r0 = new ByteOrderedDataInputStream(r10);
        ByteOrderedDataOutputStream r1 = new ByteOrderedDataOutputStream(r11, ByteOrder.BIG_ENDIAN);
        ExifInterfaceUtils.copy(r0, r1, PNG_SIGNATURE.length);
        if (this.mOffsetToExifData != 0) goto L8;
        int r2 = r0.readInt();
        r1.writeInt(r2);
        ExifInterfaceUtils.copy(r0, r1, (r2 + 4) + 4);
    L9:
        ByteArrayOutputStream r3 = null;
        r3 = new ByteArrayOutputStream();     // Catch: Throwable -> L13
        ByteOrderedDataOutputStream r4 = new ByteOrderedDataOutputStream(r3, ByteOrder.BIG_ENDIAN);     // Catch: Throwable -> L13
        writeExifSegment(r4);     // Catch: Throwable -> L13
        byte[] r5 = ((ByteArrayOutputStream) r4.mOutputStream).toByteArray();     // Catch: Throwable -> L13
        r1.write(r5);     // Catch: Throwable -> L13
        CRC32 r6 = new CRC32();     // Catch: Throwable -> L13
        r6.update(r5, 4, r5.length - 4);     // Catch: Throwable -> L13
        r1.writeInt((int) r6.getValue());     // Catch: Throwable -> L13
        ExifInterfaceUtils.closeQuietly(r3);
        ExifInterfaceUtils.copy(r0, r1);
        return;
    L13:
        th = move-exception;
        ExifInterfaceUtils.closeQuietly(r3);
        throw th;
    L8:
        ExifInterfaceUtils.copy(r0, r1, ((this.mOffsetToExifData - PNG_SIGNATURE.length) - 4) - 4);
        r0.skipFully((r0.readInt() + 4) + 4);
        goto L9
    }

    private void saveWebpAttributes(InputStream r23, OutputStream r24) throws IOException {
        if (DEBUG == false) goto L5;
        Log.d(TAG, "saveWebpAttributes starting with (inputStream: " + r23 + ", outputStream: " + r24 + ")");
    L5:
        ByteOrderedDataInputStream r0 = new ByteOrderedDataInputStream(r23, ByteOrder.LITTLE_ENDIAN);
        ByteOrderedDataOutputStream r1 = new ByteOrderedDataOutputStream(r24, ByteOrder.LITTLE_ENDIAN);
        ExifInterfaceUtils.copy(r0, r1, WEBP_SIGNATURE_1.length);
        r0.skipFully(WEBP_SIGNATURE_2.length + 4);
        ByteArrayOutputStream r2 = new ByteArrayOutputStream();     // Catch: Throwable -> L75 Exception -> L77
        ByteOrderedDataOutputStream r3 = new ByteOrderedDataOutputStream(r2, ByteOrder.LITTLE_ENDIAN);     // Catch: Throwable -> L75 Exception -> L77
        if (this.mOffsetToExifData == 0) goto L12;
        ExifInterfaceUtils.copy(r0, r3, ((this.mOffsetToExifData - ((WEBP_SIGNATURE_1.length + 4) + WEBP_SIGNATURE_2.length)) - 4) - 4);     // Catch: Throwable -> L75 Exception -> L77
        r0.skipFully(4);     // Catch: Throwable -> L75 Exception -> L77
        int r6 = r0.readInt();     // Catch: Throwable -> L75 Exception -> L77
        if ((r6 % 2) == 0) goto L11;
        r6 = r6 + 1;     // Catch: Throwable -> L75 Exception -> L77
    L11:
        r0.skipFully(r6);     // Catch: Throwable -> L75 Exception -> L77
        writeExifSegment(r3);     // Catch: Throwable -> L75 Exception -> L77
    L70:
        ExifInterfaceUtils.copy(r0, r3);     // Catch: Throwable -> L75 Exception -> L77
        r1.writeInt(r2.size() + WEBP_SIGNATURE_2.length);     // Catch: Throwable -> L75 Exception -> L77
        r1.write(WEBP_SIGNATURE_2);     // Catch: Throwable -> L75 Exception -> L77
        r2.writeTo(r1);     // Catch: Throwable -> L75 Exception -> L77
        ExifInterfaceUtils.closeQuietly(r2);
        return;
    L12:
        byte[] r8 = new byte[4];     // Catch: Throwable -> L75 Exception -> L77
        if (r0.read(r8) != r8.length) goto L74;
        boolean r11 = false;
        if (Arrays.equals(r8, WEBP_CHUNK_TYPE_VP8X) == false) goto L33;
        int r9 = r0.readInt();     // Catch: Throwable -> L75 Exception -> L77
        if ((r9 % 2) != 1) goto L19;
        int r13 = r9 + 1;     // Catch: Throwable -> L75 Exception -> L77
    L20:
        byte[] r14 = new byte[r13];     // Catch: Throwable -> L75 Exception -> L77
        r0.read(r14);     // Catch: Throwable -> L75 Exception -> L77
        r14[0] = (byte) (8 | r14[0]);     // Catch: Throwable -> L75 Exception -> L77
        if (((r14[0] >> 1) & 1) != 1) goto L23;
        r11 = true;
    L23:
        r3.write(WEBP_CHUNK_TYPE_VP8X);     // Catch: Throwable -> L75 Exception -> L77
        r3.writeInt(r9);     // Catch: Throwable -> L75 Exception -> L77
        r3.write(r14);     // Catch: Throwable -> L75 Exception -> L77
        if (r11 == false) goto L30;
        copyChunksUpToGivenChunkType(r0, r3, WEBP_CHUNK_TYPE_ANIM, null);     // Catch: Throwable -> L75 Exception -> L77
    L26:
        byte[] r10 = new byte[4];     // Catch: Throwable -> L75 Exception -> L77
        r23.read(r10);     // Catch: Throwable -> L75 Exception -> L77
        if (Arrays.equals(r10, WEBP_CHUNK_TYPE_ANMF) == false) goto L28;
        copyWebPChunk(r0, r3, r10);     // Catch: Throwable -> L75 Exception -> L77
        goto L26
    L28:
        writeExifSegment(r3);     // Catch: Throwable -> L75 Exception -> L77
        goto L70
    L30:
        copyChunksUpToGivenChunkType(r0, r3, WEBP_CHUNK_TYPE_VP8, WEBP_CHUNK_TYPE_VP8L);     // Catch: Throwable -> L75 Exception -> L77
        writeExifSegment(r3);     // Catch: Throwable -> L75 Exception -> L77
        goto L70
    L19:
        r13 = r9;
        goto L20
    L33:
        if (Arrays.equals(r8, WEBP_CHUNK_TYPE_VP8) == false) goto L35;
    L36:
        int r12 = r0.readInt();     // Catch: Throwable -> L75 Exception -> L77
        int r15 = r12;
        if ((r12 % 2) != 1) goto L39;
        r15 = r15 + 1;     // Catch: Throwable -> L75 Exception -> L77
    L39:
        int r16 = 0;
        int r17 = 0;
        int r18 = 0;
        boolean r19 = false;
        byte[] r110 = new byte[3];     // Catch: Throwable -> L75 Exception -> L77
        if (Arrays.equals(r8, WEBP_CHUNK_TYPE_VP8) == false) goto L49;
        r0.read(r110);     // Catch: Throwable -> L75 Exception -> L77
        byte[] r7 = new byte[3];     // Catch: Throwable -> L75 Exception -> L77
        if (r0.read(r7) != r7.length) goto L47;
        if (Arrays.equals(WEBP_VP8_SIGNATURE, r7) == false) goto L47;
        r16 = r0.readInt();     // Catch: Throwable -> L75 Exception -> L77
        r17 = (r16 << 18) >> 18;     // Catch: Throwable -> L75 Exception -> L77
        r18 = (r16 << 2) >> 18;     // Catch: Throwable -> L75 Exception -> L77
        r15 = r15 - ((r110.length + r7.length) + 4);     // Catch: Throwable -> L75 Exception -> L77
    L60:
        r3.write(WEBP_CHUNK_TYPE_VP8X);     // Catch: Throwable -> L75 Exception -> L77
        r3.writeInt(10);     // Catch: Throwable -> L75 Exception -> L77
        byte[] r20 = new byte[10];     // Catch: Throwable -> L75 Exception -> L77
        if (r19 == false) goto L63;
        r20[0] = (byte) (r20[0] | Ascii.DLE);     // Catch: Throwable -> L75 Exception -> L77
    L63:
        r20[0] = (byte) (r20[0] | 8);     // Catch: Throwable -> L75 Exception -> L77
        int r111 = r17 - 1;     // Catch: Throwable -> L75 Exception -> L77
        int r112 = r18 - 1;     // Catch: Throwable -> L75 Exception -> L77
        r20[4] = (byte) r111;     // Catch: Throwable -> L75 Exception -> L77
        r20[5] = (byte) (r111 >> 8);     // Catch: Throwable -> L75 Exception -> L77
        r20[6] = (byte) (r111 >> 16);     // Catch: Throwable -> L75 Exception -> L77
        r20[7] = (byte) r112;     // Catch: Throwable -> L75 Exception -> L77
        r20[8] = (byte) (r112 >> 8);     // Catch: Throwable -> L75 Exception -> L77
        r20[9] = (byte) (r112 >> 16);     // Catch: Throwable -> L75 Exception -> L77
        r3.write(r20);     // Catch: Throwable -> L75 Exception -> L77
        r3.write(r8);     // Catch: Throwable -> L75 Exception -> L77
        r3.writeInt(r12);     // Catch: Throwable -> L75 Exception -> L77
        if (Arrays.equals(r8, WEBP_CHUNK_TYPE_VP8) == false) goto L67;
        r3.write(r110);     // Catch: Throwable -> L75 Exception -> L77
        r3.write(WEBP_VP8_SIGNATURE);     // Catch: Throwable -> L75 Exception -> L77
        r3.writeInt(r16);     // Catch: Throwable -> L75 Exception -> L77
    L69:
        ExifInterfaceUtils.copy(r0, r3, r15);     // Catch: Throwable -> L75 Exception -> L77
        writeExifSegment(r3);     // Catch: Throwable -> L75 Exception -> L77
        goto L70
    L67:
        if (Arrays.equals(r8, WEBP_CHUNK_TYPE_VP8L) == false) goto L69;
        r3.write(47);     // Catch: Throwable -> L75 Exception -> L77
        r3.writeInt(r16);     // Catch: Throwable -> L75 Exception -> L77
    L47:
        throw new IOException("Encountered error while checking VP8 signature");     // Catch: Throwable -> L75 Exception -> L77
    L49:
        if (Arrays.equals(r8, WEBP_CHUNK_TYPE_VP8L) == false) goto L60;
        if (r0.readByte() != 47) goto L58;
        r16 = r0.readInt();     // Catch: Throwable -> L75 Exception -> L77
        r17 = (r16 & 16383) + 1;     // Catch: Throwable -> L75 Exception -> L77
        r18 = ((268419072 & r16) >>> 14) + 1;     // Catch: Throwable -> L75 Exception -> L77
        if ((268435456 & r16) == 0) goto L55;
        boolean r113 = true;
    L56:
        r19 = r113;
        r15 = r15 - 5;     // Catch: Throwable -> L75 Exception -> L77
        goto L60
    L55:
        r113 = false;
        goto L56
    L58:
        throw new IOException("Encountered error while checking VP8L signature");     // Catch: Throwable -> L75 Exception -> L77
    L35:
        if (Arrays.equals(r8, WEBP_CHUNK_TYPE_VP8L) == false) goto L70;
    L74:
        throw new IOException("Encountered invalid length while parsing WebP chunk type");     // Catch: Throwable -> L75 Exception -> L77
    L75:
        th = move-exception;
        ExifInterfaceUtils.closeQuietly(null);
        throw th;
    L77:
        e = move-exception;
        throw new IOException("Failed to save WebP file", e);     // Catch: Throwable -> L75
    }

    private void setThumbnailData(ByteOrderedDataInputStream r4) throws IOException {
        HashMap<String, ExifAttribute> r0 = this.mAttributes[4];
        ExifAttribute r1 = r0.get(TAG_COMPRESSION);
        if (r1 == null) goto L12;
        this.mThumbnailCompression = r1.getIntValue(this.mExifByteOrder);
        switch(this.mThumbnailCompression) {
            case 1: goto L9;
            case 6: goto L7;
            case 7: goto L9;
            default: goto L17;
        };
    L7:
        handleThumbnailFromJfif(r4, r0);
        return;
    L17:
        return;
    L9:
        if (isSupportedDataType(r0) == false) goto L16;
        handleThumbnailFromStrips(r4, r0);
        return;
    L16:
        return;
    L12:
        this.mThumbnailCompression = 6;
        handleThumbnailFromJfif(r4, r0);
    }

    private static boolean shouldSupportSeek(int r1) {
        if (r1 != 4) goto L5;
        return false;
    L5:
        if (r1 != 9) goto L7;
        return false;
    L7:
        if (r1 != 13) goto L9;
        return false;
    L9:
        if (r1 == 14) goto L17;
        return true;
    L17:
        return false;
    }

    private void swapBasedOnImageSize(int r12, int r13) throws IOException {
        if (this.mAttributes[r12].isEmpty() == true) goto L26;
        if (this.mAttributes[r13].isEmpty() == true) goto L26;
        ExifAttribute r0 = this.mAttributes[r12].get(TAG_IMAGE_LENGTH);
        ExifAttribute r3 = this.mAttributes[r12].get(TAG_IMAGE_WIDTH);
        ExifAttribute r2 = this.mAttributes[r13].get(TAG_IMAGE_LENGTH);
        ExifAttribute r4 = this.mAttributes[r13].get(TAG_IMAGE_WIDTH);
        if (r0 == null) goto L22;
        if (r3 == null) goto L22;
        if (r2 == null) goto L19;
        if (r4 == null) goto L19;
        int r1 = r0.getIntValue(this.mExifByteOrder);
        int r5 = r3.getIntValue(this.mExifByteOrder);
        int r6 = r2.getIntValue(this.mExifByteOrder);
        int r7 = r4.getIntValue(this.mExifByteOrder);
        if (r1 >= r6) goto L29;
        if (r5 >= r7) goto L30;
        HashMap<String, ExifAttribute> r8 = this.mAttributes[r12];
        this.mAttributes[r12] = this.mAttributes[r13];
        this.mAttributes[r13] = r8;
        return;
    L30:
        return;
    L29:
        return;
    L19:
        if (DEBUG == false) goto L32;
        Log.d(TAG, "Second image does not contain valid size information");
        return;
    L32:
        return;
    L22:
        if (DEBUG == false) goto L34;
        Log.d(TAG, "First image does not contain valid size information");
        return;
    L34:
        return;
    L26:
        if (DEBUG == false) goto L35;
        Log.d(TAG, "Cannot perform swap since only one image data exists");
        return;
    }

    private void updateImageSizeValues(SeekableByteOrderedDataInputStream r18, int r19) throws IOException {
        ExifAttribute r1 = this.mAttributes[r19].get(TAG_DEFAULT_CROP_SIZE);
        ExifAttribute r2 = this.mAttributes[r19].get(TAG_RW2_SENSOR_TOP_BORDER);
        ExifAttribute r3 = this.mAttributes[r19].get(TAG_RW2_SENSOR_LEFT_BORDER);
        ExifAttribute r4 = this.mAttributes[r19].get(TAG_RW2_SENSOR_BOTTOM_BORDER);
        ExifAttribute r5 = this.mAttributes[r19].get(TAG_RW2_SENSOR_RIGHT_BORDER);
        if (r1 != null) goto L5;
        if (r2 == null) goto L33;
        if (r3 == null) goto L33;
        if (r4 == null) goto L33;
        if (r5 == null) goto L33;
        int r8 = r2.getIntValue(this.mExifByteOrder);
        int r9 = r4.getIntValue(this.mExifByteOrder);
        int r10 = r5.getIntValue(this.mExifByteOrder);
        int r11 = r3.getIntValue(this.mExifByteOrder);
        if (r9 <= r8) goto L36;
        if (r10 <= r11) goto L36;
        ExifAttribute r14 = ExifAttribute.createUShort(r9 - r8, this.mExifByteOrder);
        ExifAttribute r15 = ExifAttribute.createUShort(r10 - r11, this.mExifByteOrder);
        this.mAttributes[r19].put(TAG_IMAGE_LENGTH, r14);
        this.mAttributes[r19].put(TAG_IMAGE_WIDTH, r15);
        return;
    L36:
        return;
    L33:
        retrieveJpegImageSize(r18, r19);
        return;
    L5:
        if (r1.format != 5) goto L14;
        Rational[] r12 = (Rational[]) r1.getValue(this.mExifByteOrder);
        if (r12 != null) goto L9;
    L12:
        Log.w(TAG, "Invalid crop size values. cropSize=" + Arrays.toString(r12));
        return;
    L9:
        if (r12.length != 2) goto L12;
        ExifAttribute r13 = ExifAttribute.createURational(r12[0], this.mExifByteOrder);
        ExifAttribute r16 = ExifAttribute.createURational(r12[1], this.mExifByteOrder);
    L20:
        this.mAttributes[r19].put(TAG_IMAGE_WIDTH, r13);
        this.mAttributes[r19].put(TAG_IMAGE_LENGTH, r16);
        return;
    L14:
        int[] r17 = (int[]) r1.getValue(this.mExifByteOrder);
        if (r17 != null) goto L17;
    L21:
        Log.w(TAG, "Invalid crop size values. cropSize=" + Arrays.toString(r17));
        return;
    L17:
        if (r17.length != 2) goto L21;
        r13 = ExifAttribute.createUShort(r17[0], this.mExifByteOrder);
        r16 = ExifAttribute.createUShort(r17[1], this.mExifByteOrder);
        goto L20
    }

    private void validateImages() throws IOException {
        swapBasedOnImageSize(0, 5);
        swapBasedOnImageSize(0, 4);
        swapBasedOnImageSize(5, 4);
        ExifAttribute r3 = this.mAttributes[1].get(TAG_PIXEL_X_DIMENSION);
        ExifAttribute r4 = this.mAttributes[1].get(TAG_PIXEL_Y_DIMENSION);
        if (r3 == null) goto L7;
        if (r4 == null) goto L7;
        this.mAttributes[0].put(TAG_IMAGE_WIDTH, r3);
        this.mAttributes[0].put(TAG_IMAGE_LENGTH, r4);
    L7:
        if (this.mAttributes[4].isEmpty() == false) goto L12;
        if (isThumbnail(this.mAttributes[5]) == false) goto L12;
        this.mAttributes[4] = this.mAttributes[5];
        this.mAttributes[5] = new HashMap();
    L12:
        if (isThumbnail(this.mAttributes[4]) == true) goto L14;
        Log.d(TAG, "No image meets the size requirements of a thumbnail image.");
    L14:
        replaceInvalidTags(0, TAG_THUMBNAIL_ORIENTATION, TAG_ORIENTATION);
        replaceInvalidTags(0, TAG_THUMBNAIL_IMAGE_LENGTH, TAG_IMAGE_LENGTH);
        replaceInvalidTags(0, TAG_THUMBNAIL_IMAGE_WIDTH, TAG_IMAGE_WIDTH);
        replaceInvalidTags(5, TAG_THUMBNAIL_ORIENTATION, TAG_ORIENTATION);
        replaceInvalidTags(5, TAG_THUMBNAIL_IMAGE_LENGTH, TAG_IMAGE_LENGTH);
        replaceInvalidTags(5, TAG_THUMBNAIL_IMAGE_WIDTH, TAG_IMAGE_WIDTH);
        replaceInvalidTags(4, TAG_ORIENTATION, TAG_THUMBNAIL_ORIENTATION);
        replaceInvalidTags(4, TAG_IMAGE_LENGTH, TAG_THUMBNAIL_IMAGE_LENGTH);
        replaceInvalidTags(4, TAG_IMAGE_WIDTH, TAG_THUMBNAIL_IMAGE_WIDTH);
    }

    private int writeExifSegment(ByteOrderedDataOutputStream r21) throws IOException {
        int[] r2 = new int[EXIF_TAGS.length];
        int[] r3 = new int[EXIF_TAGS.length];
        ExifTag[] r4 = EXIF_POINTER_TAGS;
        int r5 = r4.length;
        int r7 = 0;
    L3:
        if (r7 >= r5) goto L6;
        removeAttribute(r4[r7].name);
        r7 = r7 + 1;
        goto L3
    L6:
        if (this.mHasThumbnail == true) goto L8;
    L11:
        int r6 = 0;
    L13:
        if (r6 >= EXIF_TAGS.length) goto L22;
        Object[] r10 = this.mAttributes[r6].entrySet().toArray();
        int r11 = r10.length;
        int r12 = 0;
    L15:
        if (r12 >= r11) goto L21;
        Map.Entry r14 = (Map.Entry) r10[r12];
        if (r14.getValue() != null) goto L20;
        this.mAttributes[r6].remove(r14.getKey());
    L20:
        r12 = r12 + 1;
        goto L15
    L21:
        r6 = r6 + 1;
        goto L13
    L22:
        int r8 = 1;
        if (this.mAttributes[1].isEmpty() == true) goto L25;
        this.mAttributes[0].put(EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong(0, this.mExifByteOrder));
    L25:
        char r13 = 2;
        if (this.mAttributes[2].isEmpty() == true) goto L29;
        this.mAttributes[0].put(EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong(0, this.mExifByteOrder));
    L29:
        if (this.mAttributes[3].isEmpty() == true) goto L31;
        this.mAttributes[1].put(EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong(0, this.mExifByteOrder));
    L31:
        int r15 = 4;
        if (this.mHasThumbnail == false) goto L38;
        if (this.mHasThumbnailStrips == false) goto L36;
        this.mAttributes[4].put(TAG_STRIP_OFFSETS, ExifAttribute.createUShort(0, this.mExifByteOrder));
        this.mAttributes[4].put(TAG_STRIP_BYTE_COUNTS, ExifAttribute.createUShort(this.mThumbnailLength, this.mExifByteOrder));
        goto L38
    L36:
        this.mAttributes[4].put(TAG_JPEG_INTERCHANGE_FORMAT, ExifAttribute.createULong(0, this.mExifByteOrder));
        this.mAttributes[4].put(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH, ExifAttribute.createULong(this.mThumbnailLength, this.mExifByteOrder));
    L38:
        int r9 = 0;
    L40:
        if (r9 >= EXIF_TAGS.length) goto L49;
        int r16 = 0;
        Iterator<Map.Entry<String, ExifAttribute>> r17 = this.mAttributes[r9].entrySet().iterator();
    L43:
        if (r17.hasNext() == false) goto L48;
        char r18 = r13;
        int r19 = r17.next().getValue().size();
        if (r19 <= 4) goto L47;
        r16 = r16 + r19;
    L47:
        r13 = r18;
        goto L43
    L48:
        r3[r9] = r3[r9] + r16;
        r9 = r9 + 1;
        goto L40
    L49:
        char r110 = r13;
        int r20 = 8;
        int r22 = 0;
    L51:
        if (r22 >= EXIF_TAGS.length) goto L57;
        if (this.mAttributes[r22].isEmpty() == true) goto L55;
        r2[r22] = r20;
        r20 = r20 + ((((this.mAttributes[r22].size() * 12) + 2) + 4) + r3[r22]);
    L55:
        r22 = r22 + 1;
        goto L51
    L57:
        if (this.mHasThumbnail == false) goto L63;
        int r23 = r20;
        if (this.mHasThumbnailStrips == false) goto L61;
        this.mAttributes[4].put(TAG_STRIP_OFFSETS, ExifAttribute.createUShort(r23, this.mExifByteOrder));
    L62:
        this.mThumbnailOffset = r23;
        r20 = r20 + this.mThumbnailLength;
        goto L63
    L61:
        this.mAttributes[4].put(TAG_JPEG_INTERCHANGE_FORMAT, ExifAttribute.createULong(r23, this.mExifByteOrder));
    L63:
        int r24 = r20;
        if (this.mMimeType != 4) goto L67;
        r24 = r24 + 8;
    L67:
        if (DEBUG == false) goto L73;
        int r25 = 0;
    L70:
        if (r25 >= EXIF_TAGS.length) goto L72;
        Integer r26 = Integer.valueOf(r25);
        Integer r27 = Integer.valueOf(r2[r25]);
        Integer r111 = Integer.valueOf(this.mAttributes[r25].size());
        Integer r112 = Integer.valueOf(r3[r25]);
        Integer r113 = Integer.valueOf(r24);
        int r114 = r8;
        Object[] r28 = new Object[5];
        r28[0] = r26;
        r28[r114] = r27;
        r28[r110] = r111;
        r28[3] = r112;
        r28[4] = r113;
        Log.d(TAG, String.format("index: %d, offsets: %d, tag count: %d, data sizes: %d, total size: %d", r28));
        r25 = r25 + 1;
        r8 = r114;
        goto L70
    L72:
        int r115 = r8;
    L75:
        if (this.mAttributes[r115].isEmpty() == true) goto L78;
        this.mAttributes[0].put(EXIF_POINTER_TAGS[r115].name, ExifAttribute.createULong(r2[r115], this.mExifByteOrder));
    L78:
        if (this.mAttributes[r110].isEmpty() == true) goto L81;
        this.mAttributes[0].put(EXIF_POINTER_TAGS[r110].name, ExifAttribute.createULong(r2[r110], this.mExifByteOrder));
    L81:
        if (this.mAttributes[r13].isEmpty() == true) goto L84;
        this.mAttributes[r115].put(EXIF_POINTER_TAGS[r13].name, ExifAttribute.createULong(r2[r13], this.mExifByteOrder));
    L84:
        switch(this.mMimeType) {
            case 4: goto L88;
            case 13: goto L87;
            case 14: goto L86;
            default: goto L90;
        };
    L86:
        r21.write(WEBP_CHUNK_TYPE_EXIF);
        r21.writeInt(r24);
        goto L90
    L87:
        r21.writeInt(r24);
        r21.write(PNG_CHUNK_TYPE_EXIF);
        goto L90
    L88:
        r21.writeUnsignedShort(r24);
        r21.write(IDENTIFIER_EXIF_APP1);
    L90:
        if (this.mExifByteOrder != ByteOrder.BIG_ENDIAN) goto L92;
        short r29 = BYTE_ALIGN_MM;
    L93:
        r21.writeShort(r29);
        r21.setByteOrder(this.mExifByteOrder);
        r21.writeUnsignedShort(42);
        r21.writeUnsignedInt(8);
        int r30 = 0;
    L95:
        if (r30 >= EXIF_TAGS.length) goto L126;
        if (this.mAttributes[r30].isEmpty() == true) goto L123;
        r21.writeUnsignedShort(this.mAttributes[r30].size());
        int r31 = ((r2[r30] + 2) + (this.mAttributes[r30].size() * 12)) + r15;
        Iterator<Map.Entry<String, ExifAttribute>> r32 = this.mAttributes[r30].entrySet().iterator();
    L100:
        if (r32.hasNext() == false) goto L110;
        Map.Entry<String, ExifAttribute> r33 = r32.next();
        int r116 = sExifTagMapsForWriting[r30].get(r33.getKey()).number;
        ExifAttribute r117 = r33.getValue();
        int r118 = r117.size();
        r21.writeUnsignedShort(r116);
        r21.writeUnsignedShort(r117.format);
        r21.writeInt(r117.numberOfComponents);
        if (r118 <= r15) goto L104;
        r21.writeUnsignedInt(r31);
        r31 = r31 + r118;
    L109:
        r15 = 4;
        goto L100
    L104:
        r21.write(r117.bytes);
        int r119 = 4;
        if (r118 >= 4) goto L109;
        int r120 = r118;
    L107:
        if (r120 >= r119) goto L109;
        r21.writeByte(0);
        r120 = r120 + 1;
        r119 = 4;
        goto L107
    L110:
        if (r30 == 0) goto L112;
    L114:
        r21.writeUnsignedInt(0);
    L115:
        Iterator<Map.Entry<String, ExifAttribute>> r121 = this.mAttributes[r30].entrySet().iterator();
    L117:
        if (r121.hasNext() == false) goto L122;
        ExifAttribute r122 = r121.next().getValue();
        if (r122.bytes.length <= 4) goto L117;
        r21.write(r122.bytes, 0, r122.bytes.length);
        goto L117
    L122:
        int r123 = 4;
    L124:
        r30 = r30 + 1;
        r15 = r123;
        goto L95
    L112:
        if (this.mAttributes[4].isEmpty() == true) goto L114;
        r21.writeUnsignedInt(r2[4]);
        goto L115
    L123:
        r123 = r15;
        goto L124
    L126:
        if (this.mHasThumbnail == false) goto L129;
        r21.write(getThumbnailBytes());
    L129:
        if (this.mMimeType == 14) goto L131;
    L133:
        r21.setByteOrder(ByteOrder.BIG_ENDIAN);
        return r24;
    L131:
        if ((r24 % 2) != r115) goto L133;
        r21.writeByte(0);
        goto L133
    L92:
        r29 = BYTE_ALIGN_II;
        goto L93
    L73:
        r115 = 1;
        goto L75
    L8:
        if (this.mHasThumbnailStrips == false) goto L10;
        removeAttribute(TAG_STRIP_OFFSETS);
        removeAttribute(TAG_STRIP_BYTE_COUNTS);
        goto L11
    L10:
        removeAttribute(TAG_JPEG_INTERCHANGE_FORMAT);
        removeAttribute(TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
        goto L11
    }

    public void flipHorizontally() {
        switch(getAttributeInt(TAG_ORIENTATION, 1)) {
            case 1: goto L12;
            case 2: goto L11;
            case 3: goto L10;
            case 4: goto L9;
            case 5: goto L8;
            case 6: goto L7;
            case 7: goto L6;
            case 8: goto L5;
            default: goto L4;
        };
    L4:
        int r2 = 0;
    L13:
        setAttribute(TAG_ORIENTATION, Integer.toString(r2));
        return;
    L5:
        r2 = 7;
        goto L13
    L6:
        r2 = 8;
        goto L13
    L7:
        r2 = 5;
        goto L13
    L8:
        r2 = 6;
        goto L13
    L9:
        r2 = 3;
        goto L13
    L10:
        r2 = 4;
        goto L13
    L11:
        r2 = 1;
        goto L13
    L12:
        r2 = 2;
        goto L13
    }

    public void flipVertically() {
        switch(getAttributeInt(TAG_ORIENTATION, 1)) {
            case 1: goto L12;
            case 2: goto L11;
            case 3: goto L10;
            case 4: goto L9;
            case 5: goto L8;
            case 6: goto L7;
            case 7: goto L6;
            case 8: goto L5;
            default: goto L4;
        };
    L4:
        int r2 = 0;
    L13:
        setAttribute(TAG_ORIENTATION, Integer.toString(r2));
        return;
    L5:
        r2 = 5;
        goto L13
    L6:
        r2 = 6;
        goto L13
    L7:
        r2 = 7;
        goto L13
    L8:
        r2 = 8;
        goto L13
    L9:
        r2 = 1;
        goto L13
    L10:
        r2 = 2;
        goto L13
    L11:
        r2 = 3;
        goto L13
    L12:
        r2 = 4;
        goto L13
    }

    public double getAltitude(double r7) {
        double r0 = getAttributeDouble(TAG_GPS_ALTITUDE, -1.0d);
        int r3 = -1;
        int r2 = getAttributeInt(TAG_GPS_ALTITUDE_REF, -1);
        if (r0 < 0.0d) goto L11;
        if (r2 < 0) goto L11;
        if (r2 == 1) goto L10;
        r3 = 1;
    L10:
        return ((double) r3) * r0;
    L11:
        return r7;
    }

    public String getAttribute(String r12) {
        if (r12 == null) goto L32;
        ExifAttribute r0 = getExifAttribute(r12);
        if (r0 != null) goto L6;
        return null;
    L6:
        if (sTagSetForCompatibility.contains(r12) == true) goto L10;
        return r0.getStringValue(this.mExifByteOrder);
    L10:
        if (r12.equals(TAG_GPS_TIMESTAMP) == true) goto L12;
        return Double.toString(r0.getDoubleValue(this.mExifByteOrder));
    L29:
        return null;
    L12:
        if (r0.format != 5) goto L14;
    L17:
        Rational[] r2 = (Rational[]) r0.getValue(this.mExifByteOrder);
        if (r2 != null) goto L20;
    L24:
        Log.w(TAG, "Invalid GPS Timestamp array. array=" + Arrays.toString(r2));
        return null;
    L20:
        if (r2.length != 3) goto L24;
        return String.format("%02d:%02d:%02d", new Object[]{Integer.valueOf((int) (r2[0].numerator / r2[0].denominator)), Integer.valueOf((int) (r2[1].numerator / r2[1].denominator)), Integer.valueOf((int) (r2[2].numerator / r2[2].denominator))});
    L14:
        if (r0.format == 10) goto L17;
        Log.w(TAG, "GPS Timestamp format is not rational. format=" + r0.format);
        return null;
    L32:
        throw new NullPointerException("tag shouldn't be null");
    }

    public byte[] getAttributeBytes(String r3) {
        if (r3 == null) goto L10;
        ExifAttribute r0 = getExifAttribute(r3);
        if (r0 != null) goto L6;
        return null;
    L6:
        return r0.bytes;
    L10:
        throw new NullPointerException("tag shouldn't be null");
    }

    public double getAttributeDouble(String r4, double r5) {
        if (r4 == null) goto L11;
        ExifAttribute r0 = getExifAttribute(r4);
        if (r0 != null) goto L12;
        return r5;
    L12:
        return r0.getDoubleValue(this.mExifByteOrder);
    L9:
        return r5;
    L11:
        throw new NullPointerException("tag shouldn't be null");
    }

    public int getAttributeInt(String r3, int r4) {
        if (r3 == null) goto L11;
        ExifAttribute r0 = getExifAttribute(r3);
        if (r0 != null) goto L12;
        return r4;
    L12:
        return r0.getIntValue(this.mExifByteOrder);
    L9:
        return r4;
    L11:
        throw new NullPointerException("tag shouldn't be null");
    }

    public long[] getAttributeRange(String r8) {
        if (r8 == null) goto L14;
        if (this.mModified == true) goto L12;
        ExifAttribute r0 = getExifAttribute(r8);
        if (r0 != null) goto L8;
        return null;
    L8:
        return new long[]{r0.bytesOffset, r0.bytes.length};
    L12:
        throw new IllegalStateException("The underlying file has been modified since being parsed");
    L14:
        throw new NullPointerException("tag shouldn't be null");
    }

    public Long getDateTime() {
        return parseDateTime(getAttribute(TAG_DATETIME), getAttribute(TAG_SUBSEC_TIME), getAttribute(TAG_OFFSET_TIME));
    }

    public Long getDateTimeDigitized() {
        return parseDateTime(getAttribute(TAG_DATETIME_DIGITIZED), getAttribute(TAG_SUBSEC_TIME_DIGITIZED), getAttribute(TAG_OFFSET_TIME_DIGITIZED));
    }

    public Long getDateTimeOriginal() {
        return parseDateTime(getAttribute(TAG_DATETIME_ORIGINAL), getAttribute(TAG_SUBSEC_TIME_ORIGINAL), getAttribute(TAG_OFFSET_TIME_ORIGINAL));
    }

    public Long getGpsDateTime() {
        String r0 = getAttribute(TAG_GPS_DATESTAMP);
        String r1 = getAttribute(TAG_GPS_TIMESTAMP);
        if (r0 == null) goto L20;
        if (r1 == null) goto L20;
        if (NON_ZERO_TIME_PATTERN.matcher(r0).matches() == false) goto L8;
    L10:
        String r3 = r0 + ' ' + r1;
        ParsePosition r4 = new ParsePosition(0);
        Date r5 = sFormatterPrimary.parse(r3, r4);     // Catch: IllegalArgumentException -> L18
        if (r5 != null) goto L16;
        r5 = sFormatterSecondary.parse(r3, r4);     // Catch: IllegalArgumentException -> L18
        if (r5 != null) goto L16;
        return null;
    L16:
        return Long.valueOf(r5.getTime());
    L19:
        return null;
    L8:
        if (NON_ZERO_TIME_PATTERN.matcher(r1).matches() == true) goto L10;
    L20:
        return null;
    }

    @Deprecated
    public boolean getLatLong(float[] r5) {
        double[] r0 = getLatLong();
        if (r0 != null) goto L5;
        return false;
    L5:
        r5[0] = (float) r0[0];
        r5[1] = (float) r0[1];
        return true;
    }

    public double[] getLatLong() {
        String r0 = getAttribute(TAG_GPS_LATITUDE);
        String r1 = getAttribute(TAG_GPS_LATITUDE_REF);
        String r2 = getAttribute(TAG_GPS_LONGITUDE);
        String r3 = getAttribute(TAG_GPS_LONGITUDE_REF);
        if (r0 == null) goto L16;
        if (r1 == null) goto L17;
        if (r2 == null) goto L18;
        if (r3 != null) goto L14;
        return null;
    L14:
        return new double[]{convertRationalLatLonToDouble(r0, r1), convertRationalLatLonToDouble(r2, r3)};
    L11:
        Log.w(TAG, "Latitude/longitude values are not parsable. " + String.format("latValue=%s, latRef=%s, lngValue=%s, lngRef=%s", new Object[]{r0, r1, r2, r3}));
        return null;
    L18:
        return null;
    L17:
        return null;
    L16:
        return null;
    }

    public int getRotationDegrees() {
        switch(getAttributeInt(TAG_ORIENTATION, 1)) {
            case 3: goto L10;
            case 4: goto L10;
            case 5: goto L8;
            case 6: goto L6;
            case 7: goto L6;
            case 8: goto L8;
            default: goto L4;
        };
    L4:
        return 0;
    L6:
        return 90;
    L8:
        return 270;
    L10:
        return 180;
    }

    public byte[] getThumbnail() {
        if (this.mThumbnailCompression == 6) goto L10;
        if (this.mThumbnailCompression == 7) goto L10;
        return null;
    L10:
        return getThumbnailBytes();
    }

    public Bitmap getThumbnailBitmap() {
        if (this.mHasThumbnail == true) goto L6;
        return null;
    L6:
        if (this.mThumbnailBytes != null) goto L9;
        this.mThumbnailBytes = getThumbnailBytes();
    L9:
        if (this.mThumbnailCompression == 6) goto L26;
        if (this.mThumbnailCompression == 7) goto L26;
        if (this.mThumbnailCompression != 1) goto L24;
        int[] r0 = new int[this.mThumbnailBytes.length / 3];
        int r4 = 0;
    L17:
        if (r4 >= r0.length) goto L19;
        r0[r4] = (((this.mThumbnailBytes[r4 * 3] << Ascii.DLE) + 0) + (this.mThumbnailBytes[(r4 * 3) + 1] << 8)) + this.mThumbnailBytes[(r4 * 3) + 2];
        r4 = r4 + 1;
        goto L17
    L19:
        ExifAttribute r2 = this.mAttributes[4].get(TAG_THUMBNAIL_IMAGE_LENGTH);
        ExifAttribute r5 = this.mAttributes[4].get(TAG_THUMBNAIL_IMAGE_WIDTH);
        if (r2 == null) goto L24;
        if (r5 == null) goto L24;
        int r1 = r2.getIntValue(this.mExifByteOrder);
        return Bitmap.createBitmap(r0, r5.getIntValue(this.mExifByteOrder), r1, Bitmap.Config.ARGB_8888);
    L24:
        return null;
    L26:
        return BitmapFactory.decodeByteArray(this.mThumbnailBytes, 0, this.mThumbnailLength);
    }

    public byte[] getThumbnailBytes() {
        if (this.mHasThumbnail == true) goto L6;
        return null;
    L6:
        if (this.mThumbnailBytes != null) goto L8;
        FileDescriptor r3 = null;
    L41:
        th = move-exception;
        ExifInterfaceUtils.closeQuietly(null);
        if (0 == 0) goto L52;
        ExifInterfaceUtils.closeFileDescriptor(null);
    L52:
        throw th;
    L43:
        e = move-exception;
        Log.d(TAG, "Encountered exception while getting thumbnail", e);     // Catch: Throwable -> L41
        ExifInterfaceUtils.closeQuietly(null);
        if (0 == 0) goto L48;
        ExifInterfaceUtils.closeFileDescriptor(null);
    L48:
        return null;
    L11:
        if (this.mAssetInputStream == null) goto L21;
        InputStream r1 = this.mAssetInputStream;     // Catch: Throwable -> L41 Exception -> L43
        if (r1.markSupported() == false) goto L15;
        r1.reset();     // Catch: Throwable -> L41 Exception -> L43
    L24:
        if (r1 == null) goto L40;
        if (r1.skip(this.mThumbnailOffset + this.mOffsetToExifData) != (this.mThumbnailOffset + this.mOffsetToExifData)) goto L38;
        byte[] r4 = new byte[this.mThumbnailLength];     // Catch: Throwable -> L41 Exception -> L43
        if (r1.read(r4) != this.mThumbnailLength) goto L36;
        this.mThumbnailBytes = r4;     // Catch: Throwable -> L41 Exception -> L43
        ExifInterfaceUtils.closeQuietly(r1);
        if (r3 == null) goto L34;
        ExifInterfaceUtils.closeFileDescriptor(r3);
    L34:
        return r4;
    L36:
        throw new IOException("Corrupted image");     // Catch: Throwable -> L41 Exception -> L43
    L38:
        throw new IOException("Corrupted image");     // Catch: Throwable -> L41 Exception -> L43
    L40:
        throw new FileNotFoundException();     // Catch: Throwable -> L41 Exception -> L43
    L15:
        Log.d(TAG, "Cannot read thumbnail from inputstream without mark/reset support");     // Catch: Throwable -> L41 Exception -> L43
        ExifInterfaceUtils.closeQuietly(r1);
        if (0 == 0) goto L19;
        ExifInterfaceUtils.closeFileDescriptor(null);
    L19:
        return null;
    L21:
        if (this.mFilename == null) goto L23;
        r1 = new FileInputStream(this.mFilename);     // Catch: Throwable -> L41 Exception -> L43
        goto L24
    L23:
        r3 = ExifInterfaceUtils.Api21Impl.dup(this.mSeekableFileDescriptor);     // Catch: Throwable -> L41 Exception -> L43
        ExifInterfaceUtils.Api21Impl.lseek(r3, 0, OsConstants.SEEK_SET);     // Catch: Throwable -> L41 Exception -> L43
        r1 = new FileInputStream(r3);     // Catch: Throwable -> L41 Exception -> L43
        goto L24
    L8:
        return this.mThumbnailBytes;
    }

    public long[] getThumbnailRange() {
        if (this.mModified == true) goto L15;
        if (this.mHasThumbnail == true) goto L7;
        return null;
    L7:
        if (this.mHasThumbnailStrips == false) goto L12;
        if (this.mAreThumbnailStripsConsecutive == true) goto L12;
        return null;
    L12:
        return new long[]{this.mThumbnailOffset + this.mOffsetToExifData, this.mThumbnailLength};
    L15:
        throw new IllegalStateException("The underlying file has been modified since being parsed");
    }

    public boolean hasAttribute(String r2) {
        if (getExifAttribute(r2) == null) goto L5;
        return true;
    L5:
        return false;
    }

    public boolean hasThumbnail() {
        return this.mHasThumbnail;
    }

    public boolean isFlipped() {
        switch(getAttributeInt(TAG_ORIENTATION, 1)) {
            case 2: goto L6;
            case 3: goto L4;
            case 4: goto L6;
            case 5: goto L6;
            case 6: goto L4;
            case 7: goto L6;
            default: goto L4;
        };
    L4:
        return false;
    L6:
        return true;
    }

    public boolean isThumbnailCompressed() {
        if (this.mHasThumbnail == true) goto L6;
        return false;
    L6:
        if (this.mThumbnailCompression != 6) goto L8;
        return true;
    L8:
        if (this.mThumbnailCompression == 7) goto L13;
        return false;
    L13:
        return true;
    }

    public void resetOrientation() {
        setAttribute(TAG_ORIENTATION, Integer.toString(1));
    }

    public void rotate(int r7) {
        if ((r7 % 90) != 0) goto L20;
        int r0 = getAttributeInt(TAG_ORIENTATION, 1);
        int r3 = 0;
        if (ROTATION_ORDER.contains(Integer.valueOf(r0)) == false) goto L11;
        int r5 = ((r7 / 90) + ROTATION_ORDER.indexOf(Integer.valueOf(r0))) % 4;
        if (r5 >= 0) goto L9;
        r3 = 4;
    L9:
        int r4 = ROTATION_ORDER.get(r5 + r3).intValue();
    L17:
        setAttribute(TAG_ORIENTATION, Integer.toString(r4));
        return;
    L11:
        if (FLIPPED_ROTATION_ORDER.contains(Integer.valueOf(r0)) == false) goto L16;
        int r6 = ((r7 / 90) + FLIPPED_ROTATION_ORDER.indexOf(Integer.valueOf(r0))) % 4;
        if (r6 >= 0) goto L15;
        r3 = 4;
    L15:
        r4 = FLIPPED_ROTATION_ORDER.get(r6 + r3).intValue();
        goto L17
    L16:
        r4 = 0;
        goto L17
    L20:
        throw new IllegalArgumentException("degree should be a multiple of 90");
    }

    public void saveAttributes() throws IOException {
        if (isSupportedFormatForSavingAttributes(this.mMimeType) == false) goto L75;
        if (this.mSeekableFileDescriptor != null) goto L12;
        if (this.mFilename != null) goto L12;
        throw new IOException("ExifInterface does not support saving attributes for the current input.");
    L12:
        if (this.mHasThumbnail == true) goto L14;
    L20:
        this.mModified = true;
        this.mThumbnailBytes = getThumbnail();
        FileInputStream r0 = null;
        FileOutputStream r1 = null;
        File r3 = File.createTempFile("temp", "tmp");     // Catch: Throwable -> L67 Exception -> L69
        if (this.mFilename == null) goto L24;
        r0 = new FileInputStream(this.mFilename);     // Catch: Throwable -> L67 Exception -> L69
    L25:
        r1 = new FileOutputStream(r3);     // Catch: Throwable -> L67 Exception -> L69
        ExifInterfaceUtils.copy(r0, r1);     // Catch: Throwable -> L67 Exception -> L69
        ExifInterfaceUtils.closeQuietly(r0);
        ExifInterfaceUtils.closeQuietly(r1);
        FileInputStream r2 = null;
        FileOutputStream r4 = null;
        boolean r7 = false;
        FileInputStream r8 = new FileInputStream(r3);     // Catch: Throwable -> L45 Exception -> L47
        if (this.mFilename == null) goto L30;
        FileOutputStream r5 = new FileOutputStream(this.mFilename);     // Catch: Throwable -> L45 Exception -> L47
    L31:
        BufferedInputStream r9 = new BufferedInputStream(r8);     // Catch: Throwable -> L45 Exception -> L47
        BufferedOutputStream r10 = new BufferedOutputStream(r5);     // Catch: Throwable -> L45 Exception -> L47
        if (this.mMimeType != 4) goto L35;
        saveJpegAttributes(r9, r10);     // Catch: Throwable -> L45 Exception -> L47
    L40:
        ExifInterfaceUtils.closeQuietly(r9);
        ExifInterfaceUtils.closeQuietly(r10);
        if (0 != 0) goto L43;
        r3.delete();
    L43:
        this.mThumbnailBytes = null;
        return;
    L35:
        if (this.mMimeType != 13) goto L38;
        savePngAttributes(r9, r10);     // Catch: Throwable -> L45 Exception -> L47
        goto L40
    L38:
        if (this.mMimeType != 14) goto L40;
        saveWebpAttributes(r9, r10);     // Catch: Throwable -> L45 Exception -> L47
        goto L40
    L30:
        ExifInterfaceUtils.Api21Impl.lseek(this.mSeekableFileDescriptor, 0, OsConstants.SEEK_SET);     // Catch: Throwable -> L45 Exception -> L47
        r5 = new FileOutputStream(this.mSeekableFileDescriptor);     // Catch: Throwable -> L45 Exception -> L47
    L45:
        th = move-exception;
        ExifInterfaceUtils.closeQuietly(null);
        ExifInterfaceUtils.closeQuietly(null);
        if (r7 == true) goto L66;
        r3.delete();
    L66:
        throw th;
    L47:
        e = move-exception;
        r2 = new FileInputStream(r3);     // Catch: Throwable -> L55 Exception -> L57
        if (this.mFilename != null) goto L51;
        ExifInterfaceUtils.Api21Impl.lseek(this.mSeekableFileDescriptor, 0, OsConstants.SEEK_SET);     // Catch: Throwable -> L55 Exception -> L57
        r4 = new FileOutputStream(this.mSeekableFileDescriptor);     // Catch: Throwable -> L55 Exception -> L57
    L52:
        ExifInterfaceUtils.copy(r2, r4);     // Catch: Throwable -> L55 Exception -> L57
        ExifInterfaceUtils.closeQuietly(r2);     // Catch: Throwable -> L45
        ExifInterfaceUtils.closeQuietly(r4);     // Catch: Throwable -> L45
        throw new IOException("Failed to save new file", e);     // Catch: Throwable -> L45
    L51:
        r4 = new FileOutputStream(this.mFilename);     // Catch: Throwable -> L55 Exception -> L57
    L57:
        e = move-exception;
        r7 = true;
        throw new IOException("Failed to save new file. Original file is stored in " + r3.getAbsolutePath(), e);     // Catch: Throwable -> L55
    L55:
        th = move-exception;
        ExifInterfaceUtils.closeQuietly(r2);     // Catch: Throwable -> L45
        ExifInterfaceUtils.closeQuietly(r4);     // Catch: Throwable -> L45
        throw th;     // Catch: Throwable -> L45
    L24:
        ExifInterfaceUtils.Api21Impl.lseek(this.mSeekableFileDescriptor, 0, OsConstants.SEEK_SET);     // Catch: Throwable -> L67 Exception -> L69
        r0 = new FileInputStream(this.mSeekableFileDescriptor);     // Catch: Throwable -> L67 Exception -> L69
    L67:
        th = move-exception;
        ExifInterfaceUtils.closeQuietly(r0);
        ExifInterfaceUtils.closeQuietly(r1);
        throw th;
    L69:
        e = move-exception;
        throw new IOException("Failed to copy original file to temp file", e);     // Catch: Throwable -> L67
    L14:
        if (this.mHasThumbnailStrips == false) goto L20;
        if (this.mAreThumbnailStripsConsecutive == true) goto L20;
        throw new IOException("ExifInterface does not support saving attributes when the image file has non-consecutive thumbnail strips");
    L75:
        throw new IOException("ExifInterface only supports saving attributes for JPEG, PNG, and WebP formats.");
    }

    public void setAltitude(double r5) {
        if (r5 < 0.0d) goto L5;
        String r0 = "0";
    L6:
        setAttribute(TAG_GPS_ALTITUDE, new Rational(Math.abs(r5)).toString());
        setAttribute(TAG_GPS_ALTITUDE_REF, r0);
        return;
    L5:
        r0 = IcyHeaders.REQUEST_HEADER_ENABLE_METADATA_VALUE;
        goto L6
    }

    public void setAttribute(String r21, String r22) {
        String r2 = r22;
        if (r21 == null) goto L129;
        if (TAG_DATETIME.equals(r21) == false) goto L7;
    L10:
        if (r2 == null) goto L21;
        boolean r3 = DATETIME_PRIMARY_FORMAT_PATTERN.matcher(r2).find();
        boolean r7 = DATETIME_SECONDARY_FORMAT_PATTERN.matcher(r2).find();
        if (r2.length() != 19) goto L18;
        if (r3 == true) goto L16;
        if (r7 == false) goto L18;
    L16:
        if (r7 == false) goto L21;
        r2 = r2.replaceAll("-", ":");
    L18:
        Log.w(TAG, "Invalid value for " + r21 + " : " + r2);
        return;
    L21:
        if (TAG_ISO_SPEED_RATINGS.equals(r21) == true) goto L23;
        String r4 = r21;
    L27:
        int r0 = 2;
        int r8 = 1;
        if (r2 != null) goto L30;
    L43:
        int r5 = 0;
    L45:
        if (r5 >= EXIF_TAGS.length) goto L127;
        if (r5 == 4) goto L49;
    L51:
        ExifTag r6 = sExifTagMapsForWriting[r5].get(r4);
        if (r6 == null) goto L125;
        if (r2 != null) goto L55;
        this.mAttributes[r5].remove(r4);
        int r18 = r5;
        int r23 = r8;
    L126:
        r5 = r18 + 1;
        r8 = r23;
        r0 = 2;
        goto L45
    L55:
        Pair<Integer, Integer> r9 = guessDataFormat(r2);
        int r11 = -1;
        if (r6.primaryFormat != ((Integer) r9.first).intValue()) goto L58;
    L87:
        int r10 = r6.primaryFormat;
    L88:
        char r12 = 0;
        switch(r10) {
            case 1: goto L124;
            case 2: goto L123;
            case 3: goto L118;
            case 4: goto L113;
            case 5: goto L108;
            case 6: goto L90;
            case 7: goto L123;
            case 8: goto L90;
            case 9: goto L103;
            case 10: goto L98;
            case 11: goto L90;
            case 12: goto L93;
            default: goto L90;
        };
    L90:
        r18 = r5;
        r23 = r8;
        if (DEBUG == false) goto L126;
        Log.d(TAG, "Data format isn't one of expected formats: " + r10);
        goto L126
    L93:
        String[] r13 = r2.split(",", -1);
        double[] r14 = new double[r13.length];
        int r15 = 0;
    L95:
        if (r15 >= r13.length) goto L97;
        r14[r15] = Double.parseDouble(r13[r15]);
        r15 = r15 + 1;
        goto L95
    L97:
        this.mAttributes[r5].put(r4, ExifAttribute.createDouble(r14, this.mExifByteOrder));
        r18 = r5;
        r23 = r8;
        goto L126
    L98:
        String[] r16 = r2.split(",", -1);
        Rational[] r17 = new Rational[r16.length];
        int r19 = 0;
    L100:
        if (r19 >= r16.length) goto L102;
        String[] r1 = r16[r19].split("/", r11);
        int r24 = r8;
        r17[r19] = new Rational((long) Double.parseDouble(r1[r12]), (long) Double.parseDouble(r1[r24]));
        r19 = r19 + 1;
        r8 = r24;
        r12 = r12;
        r5 = r5;
        r6 = r6;
        r11 = -1;
        goto L100
    L102:
        r18 = r5;
        r23 = r8;
        this.mAttributes[r18].put(r4, ExifAttribute.createSRational(r17, this.mExifByteOrder));
        goto L126
    L103:
        r18 = r5;
        r23 = r8;
        String[] r20 = r2.split(",", -1);
        int[] r25 = new int[r20.length];
        int r26 = 0;
    L105:
        if (r26 >= r20.length) goto L107;
        r25[r26] = Integer.parseInt(r20[r26]);
        r26 = r26 + 1;
        goto L105
    L107:
        this.mAttributes[r18].put(r4, ExifAttribute.createSLong(r25, this.mExifByteOrder));
        goto L126
    L108:
        r18 = r5;
        r23 = r8;
        String[] r27 = r2.split(",", -1);
        Rational[] r28 = new Rational[r27.length];
        int r29 = 0;
    L110:
        if (r29 >= r27.length) goto L112;
        String[] r30 = r27[r29].split("/", -1);
        int r110 = r29;
        r28[r110] = new Rational((long) Double.parseDouble(r30[0]), (long) Double.parseDouble(r30[r23]));
        r29 = r110 + 1;
        r27 = r27;
        goto L110
    L112:
        this.mAttributes[r18].put(r4, ExifAttribute.createURational(r28, this.mExifByteOrder));
        goto L126
    L113:
        r18 = r5;
        r23 = r8;
        String[] r31 = r2.split(",", -1);
        long[] r32 = new long[r31.length];
        int r33 = 0;
    L115:
        if (r33 >= r31.length) goto L117;
        r32[r33] = Long.parseLong(r31[r33]);
        r33 = r33 + 1;
        goto L115
    L117:
        this.mAttributes[r18].put(r4, ExifAttribute.createULong(r32, this.mExifByteOrder));
        goto L126
    L118:
        r18 = r5;
        r23 = r8;
        String[] r34 = r2.split(",", -1);
        int[] r35 = new int[r34.length];
        int r36 = 0;
    L120:
        if (r36 >= r34.length) goto L122;
        r35[r36] = Integer.parseInt(r34[r36]);
        r36 = r36 + 1;
        goto L120
    L122:
        this.mAttributes[r18].put(r4, ExifAttribute.createUShort(r35, this.mExifByteOrder));
        goto L126
    L123:
        r18 = r5;
        r23 = r8;
        this.mAttributes[r18].put(r4, ExifAttribute.createString(r2));
        goto L126
    L124:
        r18 = r5;
        r23 = r8;
        this.mAttributes[r18].put(r4, ExifAttribute.createByte(r2));
        goto L126
    L58:
        if (r6.primaryFormat == ((Integer) r9.second).intValue()) goto L87;
        if (r6.secondaryFormat == (-1)) goto L68;
        if (r6.secondaryFormat != ((Integer) r9.first).intValue()) goto L65;
    L66:
        r10 = r6.secondaryFormat;
        goto L88
    L65:
        if (r6.secondaryFormat == ((Integer) r9.second).intValue()) goto L66;
    L68:
        if (r6.primaryFormat != r8) goto L70;
    L86:
        r10 = r6.primaryFormat;
        goto L88
    L70:
        if (r6.primaryFormat == 7) goto L86;
        if (r6.primaryFormat == r0) goto L86;
        if (DEBUG == false) goto L85;
        StringBuilder r37 = new StringBuilder().append("Given tag (").append(r4).append(") value didn't match with one of expected formats: ").append(IFD_FORMAT_NAMES[r6.primaryFormat]);
        String r111 = "";
        if (r6.secondaryFormat != (-1)) goto L79;
        String r112 = "";
    L80:
        StringBuilder r38 = r37.append(r112).append(" (guess: ").append(IFD_FORMAT_NAMES[((Integer) r9.first).intValue()]);
        if (((Integer) r9.second).intValue() == (-1)) goto L84;
        r111 = ", " + IFD_FORMAT_NAMES[((Integer) r9.second).intValue()];
    L84:
        Log.d(TAG, r38.append(r111).append(")").toString());
        r18 = r5;
        r23 = r8;
        goto L126
    L79:
        r112 = ", " + IFD_FORMAT_NAMES[r6.secondaryFormat];
        goto L80
    L85:
        r18 = r5;
        r23 = r8;
        goto L126
    L125:
        r18 = r5;
        r23 = r8;
        goto L126
    L49:
        if (this.mHasThumbnail == true) goto L51;
        r18 = r5;
        r23 = r8;
        goto L126
    L127:
        return;
    L30:
        if (sTagSetForCompatibility.contains(r4) == false) goto L43;
        if (r4.equals(TAG_GPS_TIMESTAMP) == false) goto L130;
        Matcher r39 = GPS_TIMESTAMP_PATTERN.matcher(r2);
        if (r39.find() == true) goto L37;
        Log.w(TAG, "Invalid value for " + r4 + " : " + r2);
        return;
    L37:
        r2 = Integer.parseInt(r39.group(1)) + "/1," + Integer.parseInt(r39.group(2)) + "/1," + Integer.parseInt(r39.group(3)) + "/1";
        goto L43
    L130:
        r2 = new Rational(Double.parseDouble(r2)).toString();     // Catch: NumberFormatException -> L40
    L41:
        Log.w(TAG, "Invalid value for " + r4 + " : " + r2);
        return;
    L23:
        if (DEBUG == false) goto L25;
        Log.d(TAG, "setAttribute: Replacing TAG_ISO_SPEED_RATINGS with TAG_PHOTOGRAPHIC_SENSITIVITY.");
    L25:
        r4 = TAG_PHOTOGRAPHIC_SENSITIVITY;
        goto L27
    L7:
        if (TAG_DATETIME_ORIGINAL.equals(r21) == true) goto L10;
        if (TAG_DATETIME_DIGITIZED.equals(r21) == false) goto L21;
    L129:
        throw new NullPointerException("tag shouldn't be null");
    }

    public void setDateTime(Long r8) {
        if (r8 == null) goto L14;
        if (r8.longValue() < 0) goto L12;
        String r2 = Long.toString(r8.longValue() % 1000);
        int r3 = r2.length();
    L7:
        if (r3 >= 3) goto L9;
        r2 = "0" + r2;
        r3 = r3 + 1;
        goto L7
    L9:
        setAttribute(TAG_DATETIME, sFormatterPrimary.format(new Date(r8.longValue())));
        setAttribute(TAG_SUBSEC_TIME, r2);
        return;
    L12:
        throw new IllegalArgumentException("Timestamp should a positive value.");
    L14:
        throw new NullPointerException("Timestamp should not be null.");
    }

    public void setGpsInfo(Location r6) {
        if (r6 != null) goto L4;
        return;
    L4:
        setAttribute(TAG_GPS_PROCESSING_METHOD, r6.getProvider());
        setLatLong(r6.getLatitude(), r6.getLongitude());
        setAltitude(r6.getAltitude());
        setAttribute(TAG_GPS_SPEED_REF, "K");
        setAttribute(TAG_GPS_SPEED, new Rational((r6.getSpeed() * TimeUnit.HOURS.toSeconds(1)) / 1000.0f).toString());
        String[] r0 = sFormatterPrimary.format(new Date(r6.getTime())).split("\\s+", -1);
        setAttribute(TAG_GPS_DATESTAMP, r0[0]);
        setAttribute(TAG_GPS_TIMESTAMP, r0[1]);
    }

    public void setLatLong(double r5, double r7) {
        if (r5 < (-90.0d)) goto L27;
        if (r5 > 90.0d) goto L27;
        if (Double.isNaN(r5) == true) goto L27;
        if (r7 < (-180.0d)) goto L25;
        if (r7 > 180.0d) goto L25;
        if (Double.isNaN(r7) == true) goto L25;
        if (r5 < 0.0d) goto L17;
        String r2 = "N";
    L18:
        setAttribute(TAG_GPS_LATITUDE_REF, r2);
        setAttribute(TAG_GPS_LATITUDE, convertDecimalDegree(Math.abs(r5)));
        if (r7 < 0.0d) goto L21;
        String r0 = LONGITUDE_EAST;
    L22:
        setAttribute(TAG_GPS_LONGITUDE_REF, r0);
        setAttribute(TAG_GPS_LONGITUDE, convertDecimalDegree(Math.abs(r7)));
        return;
    L21:
        r0 = LONGITUDE_WEST;
        goto L22
    L17:
        r2 = LATITUDE_SOUTH;
    L25:
        throw new IllegalArgumentException("Longitude value " + r7 + " is not valid.");
    L27:
        throw new IllegalArgumentException("Latitude value " + r5 + " is not valid.");
    }
}
