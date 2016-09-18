package org.nativescript.widgets;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by hhristov on 8/23/16.
 */

public class ViewHelper {
        private ViewHelper() {

        }

        static final int version = android.os.Build.VERSION.SDK_INT;

        public static int getMinWidth(android.view.View view) {
            return view.getMinimumWidth();
        }
        public static void setMinWidth(android.view.View view, int value) {
            view.setMinimumWidth(value);
        }

        public static int getMinHeight(android.view.View view) {
            return view.getMinimumHeight();
        }
        public static void setMinHeight(android.view.View view, int value) {
            view.setMinimumHeight(value);
        }

        public static int getWidth(android.view.View view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params != null) {
                return params.width;
            }

            return ViewGroup.LayoutParams.MATCH_PARENT;
        }
        public static void setWidth(android.view.View view, int value) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params == null) {
                params = new CommonLayoutParams();
                params.width = value;
            }

            view.setLayoutParams(params);
        }

        public static int getHeight(android.view.View view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params != null) {
                return params.height;
            }

            return ViewGroup.LayoutParams.MATCH_PARENT;
        }
        public static void setHeight(android.view.View view, int value) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params == null) {
                params = new CommonLayoutParams();
                params.height = value;
            }

            view.setLayoutParams(params);
        }

        public static Rect getMargin(android.view.View view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) params;
                return new Rect(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin);
            }

            return new Rect();
        }
        public static void setMargin(android.view.View view, int left, int top, int right, int bottom) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            // Initialize if empty.
            if (params == null) {
                params = new CommonLayoutParams();
            }

            // Set margins only if params are of the correct type.
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) params;
                lp.leftMargin = left;
                lp.topMargin = top;
                lp.rightMargin = right;
                lp.bottomMargin = bottom;
                view.setLayoutParams(params);
            }
        }

        public static int getMarginLeft(android.view.View view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) params;
                return lp.leftMargin;
            }

            return 0;
        }
        public static void setMarginLeft(android.view.View view, int value) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            // Initialize if empty.
            if (params == null) {
                params = new CommonLayoutParams();
            }

            // Set margins only if params are of the correct type.
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) params;
                lp.leftMargin = value;
                view.setLayoutParams(params);
            }
        }

        public static int getMarginTop(android.view.View view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) params;
                return lp.topMargin;
            }

            return 0;
        }
        public static void setMarginTop(android.view.View view, int value) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            // Initialize if empty.
            if (params == null) {
                params = new CommonLayoutParams();
            }

            // Set margins only if params are of the correct type.
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) params;
                lp.topMargin = value;
                view.setLayoutParams(params);
            }
        }

        public static int getMarginRight(android.view.View view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) params;
                return lp.rightMargin;
            }

            return 0;
        }
        public static void setMarginRight(android.view.View view, int value) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            // Initialize if empty.
            if (params == null) {
                params = new CommonLayoutParams();
            }

            // Set margins only if params are of the correct type.
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) params;
                lp.rightMargin = value;
                view.setLayoutParams(params);
            }
        }

        public static int getMarginBottom(android.view.View view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) params;
                return lp.bottomMargin;
            }

            return 0;
        }
        public static void setMarginBottom(android.view.View view, int value) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            // Initialize if empty.
            if (params == null) {
                params = new CommonLayoutParams();
            }

            // Set margins only if params are of the correct type.
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) params;
                lp.bottomMargin = value;
                view.setLayoutParams(params);
            }
        }

        public static String getHorizontalAlighment(android.view.View view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) params;
                if (Gravity.isHorizontal(lp.gravity)) {
                    switch (lp.gravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                        case Gravity.LEFT:
                            return "left";
                        case Gravity.CENTER:
                            return "center";
                        case Gravity.RIGHT:
                            return "right";
                        case Gravity.FILL_HORIZONTAL:
                            return "stretch";

                    }
                }
            }

            return "stretch";
        }
        public static void setHorizontalAlighment(android.view.View view, String value) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            // Initialize if empty.
            if (params == null) {
                params = new CommonLayoutParams();
            }

            // Set margins only if params are of the correct type.
            if (params instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) params;
                switch (value) {
                    case "left":
                        lp.gravity = Gravity.LEFT | (lp.gravity & Gravity.VERTICAL_GRAVITY_MASK);
                        break;
                    case "center":
                        lp.gravity = Gravity.CENTER_HORIZONTAL | (lp.gravity & Gravity.VERTICAL_GRAVITY_MASK);
                        break;
                    case "right":
                        lp.gravity = Gravity.RIGHT | (lp.gravity & Gravity.VERTICAL_GRAVITY_MASK);
                        break;
                    case "stretch":
                        lp.gravity = Gravity.FILL_HORIZONTAL | (lp.gravity & Gravity.VERTICAL_GRAVITY_MASK);
                        break;
                }
                view.setLayoutParams(params);
            }
        }

        public static String getVerticalAlignment(android.view.View view) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) params;
                if (Gravity.isHorizontal(lp.gravity)) {
                    switch (lp.gravity & Gravity.VERTICAL_GRAVITY_MASK) {
                        case Gravity.TOP:
                            return "top";
                        case Gravity.CENTER:
                            return "center";
                        case Gravity.BOTTOM:
                            return "bottom";
                        case Gravity.FILL_VERTICAL:
                            return "stretch";

                    }
                }
            }

            return "stretch";
        }
        public static void setVerticalAlignment(android.view.View view, String value){
            ViewGroup.LayoutParams params = view.getLayoutParams();
            // Initialize if empty.
            if (params == null) {
                params = new CommonLayoutParams();
            }

            // Set margins only if params are of the correct type.
            if (params instanceof FrameLayout.LayoutParams) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) params;
                switch (value) {
                    case "top":
                        lp.gravity = Gravity.TOP | (lp.gravity & Gravity.HORIZONTAL_GRAVITY_MASK);
                        break;
                    case "center":
                        lp.gravity = Gravity.CENTER_VERTICAL | (lp.gravity & Gravity.HORIZONTAL_GRAVITY_MASK);
                        break;
                    case "bottom":
                        lp.gravity = Gravity.BOTTOM| (lp.gravity & Gravity.HORIZONTAL_GRAVITY_MASK);
                        break;
                    case "stretch":
                        lp.gravity = Gravity.FILL_VERTICAL | (lp.gravity & Gravity.HORIZONTAL_GRAVITY_MASK);
                        break;
                }
                view.setLayoutParams(params);
            }
        }

        public static Rect getPadding(android.view.View view) {
            return new Rect(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        }
        public static void setPadding(android.view.View view, int left, int top, int right, int bottom) {
            view.setPadding(left, top, right, bottom);
        }

        public static int getPaddingLeft(android.view.View view) {
            return view.getPaddingLeft();
        }
        public static void setPaddingLeft(android.view.View view, int value) {
            view.setPadding(value, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        }

        public static int getPaddingTop(android.view.View view) {
            return view.getPaddingTop();
        }
        public static void setPaddingTop(android.view.View view, int value) {
            view.setPadding(view.getPaddingLeft(), value, view.getPaddingRight(), view.getPaddingBottom());
        }

        public static int getPaddingRight(android.view.View view) {
            return view.getPaddingRight();
        }
        public static void setPaddingRight(android.view.View view, int value) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), value, view.getPaddingBottom());
        }

        public static int getPaddingBottom(android.view.View view) {
            return view.getPaddingBottom();
        }
        public static void setPaddingBottom(android.view.View view, int value) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), value);
        }

        public static float getRotate(android.view.View view) {
            return view.getRotation();
        }
        public static void setRotate(android.view.View view, float value) {
            view.setRotation(value);
        }

        public static float getScaleX(android.view.View view) {
            return view.getScaleX();
        }
        public static void setScaleX(android.view.View view, float value) {
            view.setScaleX(value);
        }

        public static float getScaleY(android.view.View view) {
            return view.getScaleY();
        }
        public static void setScaleY(android.view.View view, float value) {
            view.setScaleY(value);
        }

        public static float getTranslateX(android.view.View view) {
            return view.getTranslationX();
        }
        public static void setTranslateX(android.view.View view, float value) {
            view.setTranslationX(value);
        }

        public static float getTranslateY(android.view.View view) {
            return view.getTranslationY();
        }
        public static void setTranslateY(android.view.View view, float value) {
            view.setTranslationY(value);
        }

        @TargetApi(21)
        public static float getZIndex(android.view.View view) {
            if (ViewHelper.version >= 21) {
                return view.getZ();
            }

            return 0;
        }

        @TargetApi(21)
        public static void setZIndex(android.view.View view, float value) {
            if (ViewHelper.version >= 21) {
                view.setZ(value);
            }
        }
}
    