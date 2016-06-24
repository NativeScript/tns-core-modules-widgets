package org.nativescript.widgets;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by hristov on 6/15/2016.
 */
public class BorderDrawable extends ColorDrawable {
    private final float density;
    private Rect boundsCache;
    private float borderWidth;
    private int borderColor;
    private float borderRadius;
    private String clipPath;
    private int backgroundColor;
    private Paint backgroundColorPaint;
    private Bitmap backgroundImage;
    private String backgroundRepeat;
    private String backgroundPosition;
    private CSSValue[] backgroundPositionParsedCSSValues;
    private String backgroundSize;
    private CSSValue[] backgroundSizeParsedCSSValues;
    private BackgroundDrawParams bkgParams;

    public float getBorderWidth() {
        return borderWidth;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public float getBorderRadius() {
        return borderRadius;
    }

    public String getClipPath() {
        return clipPath;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public Bitmap getBackgroundImage() {
        return backgroundImage;
    }

    public String getBackgroundRepeat() {
        return backgroundRepeat;
    }

    public String getBackgroundPosition() {
        return backgroundPosition;
    }

    public String getBackgroundSize() {
        return backgroundSize;
    }

    public BorderDrawable(float density){
        super();
        this.density = density;
    }

    public void refresh(float borderWidth,
                        int borderColor,
                        float borderRadius,
                        String clipPath,
                        int backgroundColor,
                        Bitmap backgroundImage,
                        String backgroundRepeat,
                        String backgroundPosition,
                        CSSValue[] backgroundPositionParsedCSSValues,
                        String backgroundSize,
                        CSSValue[] backgroundSizeParsedCSSValues){
        boolean invalidate = false;

        if (!Objects.equals(this.borderWidth, borderWidth)){
            this.borderWidth = borderWidth;
            invalidate = true;
        }
        if (!Objects.equals(this.borderColor, borderColor)){
            this.borderColor = borderColor;
            invalidate = true;
        }
        if (!Objects.equals(this.borderRadius, borderRadius)){
            this.borderRadius = borderRadius;
            invalidate = true;
        }
        if (!Objects.equals(this.clipPath, clipPath)){
            this.clipPath = clipPath;
            this.clipPathFunctionCache = null;
            this.clipPathValuesCache = null;
            this.clipPathPolygonPointsCache = null;
            invalidate = true;
        }
        if (!Objects.equals(this.backgroundColor, backgroundColor)){
            this.backgroundColor = backgroundColor;
            if (this.backgroundColor != Color.TRANSPARENT){
                this.backgroundColorPaint = new Paint();
                this.backgroundColorPaint.setStyle(Paint.Style.FILL);
                this.backgroundColorPaint.setColor(this.backgroundColor);
                this.backgroundColorPaint.setAntiAlias(true);
            }
            else {
                this.backgroundColorPaint = null;
            }
            invalidate = true;
        }
        if (!Objects.equals(this.backgroundImage, backgroundImage)){
            this.backgroundImage = backgroundImage;
            this.bkgParams = null;
            invalidate = true;
        }
        if (!Objects.equals(this.backgroundRepeat, backgroundRepeat)){
            this.backgroundRepeat = backgroundRepeat;
            this.bkgParams = null;
            invalidate = true;
        }
        if (!Objects.equals(this.backgroundPosition, backgroundPosition)){
            this.backgroundPosition = backgroundPosition;
            this.backgroundPositionParsedCSSValues = backgroundPositionParsedCSSValues;
            this.bkgParams = null;
            invalidate = true;
        }
        if (!Objects.equals(this.backgroundSize, backgroundSize)){
            this.backgroundSize = backgroundSize;
            this.backgroundSizeParsedCSSValues = backgroundSizeParsedCSSValues;
            this.bkgParams = null;
            invalidate = true;
        }
        if (invalidate){
            this.invalidateSelf();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = this.copyBounds();
        if (!Objects.equals(this.boundsCache, bounds)){
            this.bkgParams = null;
        }
        this.boundsCache = bounds;

        float borderWidth = this.borderWidth * this.density;
        float halfBorderWidth = borderWidth / 2f;
        // We will inset background colors and images so antialiasing will not color pixels outside the border.
        // If the border is transparent we will backoff less, and we will not backoff more than half a pixel or half the border width.
        float normalizedBorderAlpha = ((float)Color.alpha(this.borderColor)) / 255f;
        float backoffAntialias = Math.min(0.5f, halfBorderWidth) * normalizedBorderAlpha;
        RectF backgroundBoundsF = new RectF(bounds.left + backoffAntialias, bounds.top + backoffAntialias, bounds.right - backoffAntialias, bounds.bottom - backoffAntialias);

        float outerRadius = this.borderRadius * this.density;

        // draw background
        if (this.backgroundColorPaint != null) {
            if (!Objects.isNullOrEmpty(this.clipPath)) {
                this.drawClipPath(canvas, this.backgroundColorPaint, backgroundBoundsF);
            }
            else {
                canvas.drawRoundRect(backgroundBoundsF, outerRadius, outerRadius, this.backgroundColorPaint);
            }
        }

        if (this.backgroundImage != null) {
            if (this.bkgParams == null) {
                this.bkgParams = getBackgroundDrawParams(
                        bounds.width(),
                        bounds.height(),
                        this.backgroundImage,
                        this.backgroundRepeat,
                        this.backgroundPosition,
                        this.backgroundPositionParsedCSSValues,
                        this.backgroundSize,
                        this.backgroundSizeParsedCSSValues
                );
            }

            Matrix transform = new Matrix();
            if (this.bkgParams.sizeX > 0f && this.bkgParams.sizeY > 0f) {
                float scaleX = this.bkgParams.sizeX / this.backgroundImage.getWidth();
                float scaleY = this.bkgParams.sizeY / this.backgroundImage.getHeight();
                transform.setScale(scaleX, scaleY, 0f, 0f);
            } else {
                this.bkgParams.sizeX = this.backgroundImage.getWidth();
                this.bkgParams.sizeY = this.backgroundImage.getHeight();
            }
            transform.postTranslate(this.bkgParams.posX - backoffAntialias, this.bkgParams.posY - backoffAntialias);

            BitmapShader shader = new BitmapShader(this.backgroundImage, android.graphics.Shader.TileMode.REPEAT, android.graphics.Shader.TileMode.REPEAT);
            shader.setLocalMatrix(transform);

            Paint backgroundImagePaint = new Paint();
            backgroundImagePaint.setShader(shader);

            float imageWidth = this.bkgParams.repeatX ? bounds.width() : this.bkgParams.sizeX;
            float imageHeight = this.bkgParams.repeatY ? bounds.height() : this.bkgParams.sizeY;
            this.bkgParams.posX = this.bkgParams.repeatX ? 0f : this.bkgParams.posX;
            this.bkgParams.posY = this.bkgParams.repeatY ? 0f: this.bkgParams.posY;

            if (!Objects.isNullOrEmpty(this.clipPath)) {
                this.drawClipPath(canvas, backgroundImagePaint, backgroundBoundsF);
            }
            else {
                boolean supportsPathOp = android.os.Build.VERSION.SDK_INT >= 19;
                if (supportsPathOp) {
                    // Path.Op can be used in API level 19+ to achieve the perfect geometry.
                    Path backgroundPath = new Path();
                    backgroundPath.addRoundRect(backgroundBoundsF, outerRadius, outerRadius, android.graphics.Path.Direction.CCW);
                    Path backgroundNoRepeatPath = new Path();
                    backgroundNoRepeatPath.addRect(this.bkgParams.posX, this.bkgParams.posY, this.bkgParams.posX + imageWidth, this.bkgParams.posY + imageHeight, android.graphics.Path.Direction.CCW);
                    intersect(backgroundPath, backgroundNoRepeatPath);
                    canvas.drawPath(backgroundPath, backgroundImagePaint);
                } else {
                    // Clipping here will not be anti-aliased but at least it won't shine through the rounded corners.
                    canvas.save();
                    canvas.clipRect(this.bkgParams.posX, this.bkgParams.posY, this.bkgParams.posX + imageWidth, this.bkgParams.posY + imageHeight);
                    canvas.drawRoundRect(backgroundBoundsF, outerRadius, outerRadius, backgroundImagePaint);
                    canvas.restore();
                }
            }
        }

        // draw border
        if (borderWidth > 0f && this.borderColor != Color.TRANSPARENT) {
            RectF middleBoundsF = new RectF(bounds.left + halfBorderWidth, bounds.top + halfBorderWidth, bounds.right - halfBorderWidth, bounds.bottom - halfBorderWidth);
            Paint borderPaint = new Paint();
            borderPaint.setColor(this.borderColor);
            borderPaint.setAntiAlias(true);

            if (!Objects.isNullOrEmpty(this.clipPath)) {
                borderPaint.setStyle(android.graphics.Paint.Style.STROKE);
                borderPaint.setStrokeWidth(borderWidth);
                this.drawClipPath(canvas, borderPaint, backgroundBoundsF);
            } else {
                if (outerRadius <= 0f) {
                    borderPaint.setStyle(android.graphics.Paint.Style.STROKE);
                    borderPaint.setStrokeWidth(borderWidth);
                    canvas.drawRect(middleBoundsF, borderPaint);
                } else if (outerRadius >= borderWidth) {
                    borderPaint.setStyle(android.graphics.Paint.Style.STROKE);
                    borderPaint.setStrokeWidth(borderWidth);
                    float middleRadius = Math.max(0f, outerRadius - halfBorderWidth);
                    canvas.drawRoundRect(middleBoundsF, middleRadius, middleRadius, borderPaint);
                } else {
                    Path borderPath = new Path();
                    RectF borderOuterBoundsF = new RectF(bounds.left, bounds.top, bounds.right, bounds.bottom);
                    borderPath.addRoundRect(borderOuterBoundsF, outerRadius, outerRadius, android.graphics.Path.Direction.CCW);
                    RectF borderInnerBoundsF = new RectF(bounds.left + borderWidth, bounds.top + borderWidth, bounds.right - borderWidth, bounds.bottom - borderWidth);
                    borderPath.addRect(borderInnerBoundsF, android.graphics.Path.Direction.CW);
                    borderPaint.setStyle(android.graphics.Paint.Style.FILL);
                    canvas.drawPath(borderPath, borderPaint);
                }
            }
        }
    }

    @TargetApi(19)
    private static void intersect(Path path1, Path path2){
        path1.op(path2, Path.Op.INTERSECT);
    }

    private static Pattern whitespaceAndCommaPattern = Pattern.compile("[\\s,]+");
    private static Pattern whitespacePattern = Pattern.compile("\\s+");
    private String clipPathFunctionCache;
    private String[] clipPathValuesCache;
    private String[][] clipPathPolygonPointsCache;
    private static final String RECT = "rect";
    private static final String CIRCLE = "circle";
    private static final String ELLIPSE = "ellipse";
    private static final String POLYGON = "polygon";
    private void drawClipPath(Canvas canvas, Paint paint, RectF bounds) {
        if (this.clipPathFunctionCache == null){
            this.clipPathFunctionCache = this.clipPath.substring(0, this.clipPath.indexOf("("));
        }

        switch (this.clipPathFunctionCache){
            case RECT: //rect(0, 0, 100%, 100%)
                if (this.clipPathValuesCache == null){
                    String valueBetweenParentheses = this.clipPath.substring(this.clipPath.indexOf("(") + 1, this.clipPath.indexOf(")"));
                    this.clipPathValuesCache = whitespaceAndCommaPattern.split(valueBetweenParentheses);
                }
                float top = cssValueToDevicePixels(this.clipPathValuesCache[0], bounds.top, this.density);
                float left = cssValueToDevicePixels(this.clipPathValuesCache[1], bounds.left, this.density);
                float bottom = cssValueToDevicePixels(this.clipPathValuesCache[2], bounds.bottom, this.density);
                float right = cssValueToDevicePixels(this.clipPathValuesCache[3], bounds.right, this.density);
                canvas.drawRect(left, top, right, bottom, paint);
                break;
            case CIRCLE://circle(100% at 50% 50%)
                if (this.clipPathValuesCache == null){
                    String valueBetweenParentheses = this.clipPath.substring(this.clipPath.indexOf("(") + 1, this.clipPath.indexOf(")"));
                    this.clipPathValuesCache = whitespacePattern.split(valueBetweenParentheses);
                }
                float radius = cssValueToDevicePixels(this.clipPathValuesCache[0], (bounds.width() > bounds.height() ? bounds.height() : bounds.width()) / 2, this.density);
                float y = cssValueToDevicePixels(this.clipPathValuesCache[2], bounds.height(), this.density);
                float x = cssValueToDevicePixels(this.clipPathValuesCache[3], bounds.width(), this.density);
                canvas.drawCircle(x, y, radius, paint);
                break;
            case ELLIPSE://ellipse(50% 50% at 50% 50%)
                if (this.clipPathValuesCache == null){
                    String valueBetweenParentheses = this.clipPath.substring(this.clipPath.indexOf("(") + 1, this.clipPath.indexOf(")"));
                    this.clipPathValuesCache = whitespacePattern.split(valueBetweenParentheses);
                }
                float rX = cssValueToDevicePixels(this.clipPathValuesCache[0], bounds.right, this.density);
                float rY = cssValueToDevicePixels(this.clipPathValuesCache[1], bounds.bottom, this.density);
                float cX = cssValueToDevicePixels(this.clipPathValuesCache[3], bounds.right, this.density);
                float cY = cssValueToDevicePixels(this.clipPathValuesCache[4], bounds.bottom, this.density);
                left = cX - rX;
                top = cY - rY;
                right = (rX * 2) + left;
                bottom = (rY * 2) + top;
                canvas.drawOval(new android.graphics.RectF(left, top, right, bottom), paint);
                break;
            case POLYGON://polygon(20% 0%, 0% 20%, 30% 50%, 0% 80%, 20% 100%, 50% 70%, 80% 100%, 100% 80%, 70% 50%, 100% 20%, 80% 0%, 50% 30%)
                if (this.clipPathValuesCache == null) {
                    String valueBetweenParentheses = this.clipPath.substring(this.clipPath.indexOf("(") + 1, this.clipPath.indexOf(")"));
                    //20% 0%, 0% 20%, 30% 50%, 0% 80%, 20% 100%, 50% 70%, 80% 100%, 100% 80%, 70% 50%, 100% 20%, 80% 0%, 50% 30%
                    this.clipPathValuesCache = valueBetweenParentheses.split(",");
                    //[" 0% 20%"][" 30% 50%"][...]
                    this.clipPathPolygonPointsCache = new String[this.clipPathValuesCache.length][2];
                    for (int i = 0; i < this.clipPathValuesCache.length; i++) {
                        this.clipPathPolygonPointsCache[i] = whitespacePattern.split(this.clipPathValuesCache[i].trim());
                    }
                    //["0%"]["20%"]
                    //["30%"]["50%"]
                }

                Path path = new Path();
                PointF firstPoint = null;
                for (String[] xy : this.clipPathPolygonPointsCache) {

                    PointF point = new PointF(
                            cssValueToDevicePixels(xy[0], bounds.width(), this.density),
                            cssValueToDevicePixels(xy[1], bounds.height(), this.density)
                    );

                    if (firstPoint == null) {
                        firstPoint = point;
                        // Start the path
                        path.moveTo(point.x, point.y);
                    }

                    path.lineTo(point.x, point.y);
                }
                // Close the path
                if (firstPoint != null){
                    path.lineTo(firstPoint.x, firstPoint.y);
                }
                canvas.drawPath(path, paint);
                break;
        }
    }

    private static final String EMPTY = "";
    private static final String NO_REPEAT = "no-repeat";
    private static final String REPEAT_X = "repeat-x";
    private static final String REPEAT_Y = "repeat-y";
    private static final String PERCENT = "%";
    private static final String PX = "px";
    private static final String NUMBER = "number";
    private static final String IDENT = "ident";
    private static final String COVER = "cover";
    private static final String CONTAIN = "contain";
    private static final String LEFT = "left";
    private static final String TOP = "top";
    private static final String CENTER = "center";
    private static final String RIGHT = "right";
    private static final String BOTTOM = "bottom";
    private static BackgroundDrawParams getBackgroundDrawParams(
            float width,
            float height,
            Bitmap backgroundImage,
            String backgroundRepeat,
            String backgroundPosition,
            CSSValue[] backgroundPositionParsedCSSValues,
            String backgroundSize,
            CSSValue[] backgroundSizeParsedCSSValues
    ) {
        BackgroundDrawParams result = new BackgroundDrawParams();

        // repeat
        if (!Objects.isNullOrEmpty(backgroundRepeat)) {
            switch (backgroundRepeat.toLowerCase(Locale.ENGLISH)) {
                case NO_REPEAT:
                    result.repeatX = false;
                    result.repeatY = false;
                    break;

                case REPEAT_X:
                    result.repeatY = false;
                    break;

                case REPEAT_Y:
                    result.repeatX = false;
                    break;
            }
        }

        float imageWidth = backgroundImage.getWidth();
        float imageHeight = backgroundImage.getHeight();

        // size
        if (!Objects.isNullOrEmpty(backgroundSize)) {
            if (backgroundSizeParsedCSSValues.length == 2) {
                CSSValue vx = backgroundSizeParsedCSSValues[0];
                CSSValue vy = backgroundSizeParsedCSSValues[1];
                if (PERCENT.equals(vx.getUnit()) && PERCENT.equals(vy.getUnit())) {
                    imageWidth = width * vx.getValue() / 100f;
                    imageHeight = height * vy.getValue() / 100f;

                    result.sizeX = imageWidth;
                    result.sizeY = imageHeight;
                }
                else if (NUMBER.equals(vx.getType()) && NUMBER.equals(vy.getType()) &&
                        ((PX.equals(vx.getUnit()) && PX.equals(vy.getUnit())) || ((vx.getUnit() == null || vx.getUnit().isEmpty()) && (vy.getUnit() == null || vy.getUnit().isEmpty())))) {
                    imageWidth = vx.getValue();
                    imageHeight = vy.getValue();

                    result.sizeX = imageWidth;
                    result.sizeY = imageHeight;
                }
            }
            else if (backgroundSizeParsedCSSValues.length == 1 && IDENT.equals(backgroundSizeParsedCSSValues[0].getType())) {
                float scale = 0f;

                if (COVER.equals(backgroundSizeParsedCSSValues[0].getString())) {
                    scale = Math.max(width / imageWidth, height / imageHeight);
                }
                else if (CONTAIN.equals(backgroundSizeParsedCSSValues[0].getString())) {
                    scale = Math.min(width / imageWidth, height / imageHeight);
                }

                if (scale > 0f) {
                    imageWidth *= scale;
                    imageHeight *= scale;

                    result.sizeX = imageWidth;
                    result.sizeY = imageHeight;
                }
            }
        }

        // position
        if (!Objects.isNullOrEmpty(backgroundPosition)) {
            CSSValue[] xy = parsePosition(backgroundPositionParsedCSSValues);
            if (xy != null) {
                CSSValue vx = xy[0];
                CSSValue vy = xy[1];
                float spaceX = width - imageWidth;
                float spaceY = height - imageHeight;

                if (PERCENT.equals(vx.getUnit()) && PERCENT.equals(vy.getUnit())) {
                    result.posX = spaceX * vx.getValue() / 100f;
                    result.posY = spaceY * vy.getValue() / 100f;
                }
                else if (NUMBER.equals(vx.getType()) && NUMBER.equals(vy.getType()) &&
                        ((PX.equals(vx.getUnit()) && PX.equals(vy.getUnit())) || ((vx.getUnit() == null || vx.getUnit().isEmpty()) && (vy.getUnit() == null || vy.getUnit().isEmpty())))) {
                    result.posX = vx.getValue();
                    result.posY = vy.getValue();
                }
                else if (IDENT.equals(vx.getType()) && IDENT.equals(vy.getType())) {
                    if (CENTER.equals(vx.getString().toLowerCase(Locale.ENGLISH))) {
                        result.posX = spaceX / 2f;
                    }
                    else if (RIGHT.equals(vx.getString().toLowerCase(Locale.ENGLISH))) {
                        result.posX = spaceX;
                    }

                    if (CENTER.equals(vy.getString().toLowerCase(Locale.ENGLISH))) {
                        result.posY = spaceY / 2f;
                    }
                    else if (BOTTOM.equals(vy.getString().toLowerCase(Locale.ENGLISH))) {
                        result.posY = spaceY;
                    }
                }
            }
        }

        return result;
    }

    private static CSSValue[] parsePosition(CSSValue[] values) {
        if (values.length == 2) {
            return values;
        }

        CSSValue[] result = null;
        if (values.length == 1 && IDENT.equals(values[0].getType())) {
            String val = values[0].getString().toLowerCase(Locale.ENGLISH);
            CSSValue center = new CSSValue(IDENT, CENTER, null, 0);

            // If you only one keyword is specified, the other value is "center"
            if (LEFT.equals(val) || RIGHT.equals(val)) {
                result  = new CSSValue[] {values[0], center};
            }
            else if (TOP.equals(val) || BOTTOM.equals(val)) {
                result  = new CSSValue[] {center, values[0]};
            }
            else if (CENTER.equals(val)) {
                result  = new CSSValue[] {center, center};
            }
        }

        return result;
    }

    private static float cssValueToDevicePixels(String source, float total, float density) {
        float result;
        source = source.trim();

        if (source.indexOf(PX) > -1) {
            result = Float.parseFloat(source.replace(PX, EMPTY));
        }
        else if (source.indexOf(PERCENT) > -1 && total > 0f) {
            result = (Float.parseFloat(source.replace(PERCENT, EMPTY)) / 100f) * (total / density);
        }
        else {
            result = Float.parseFloat(source);
        }
        return result * density;
    }
}

class BackgroundDrawParams {
    public boolean repeatX = true;
    public boolean repeatY = true;
    public float posX;
    public float posY;
    public float sizeX;
    public float sizeY;
}