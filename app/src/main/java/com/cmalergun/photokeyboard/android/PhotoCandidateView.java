package com.cmalergun.photokeyboard.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.cmalergun.photokeyboard.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoCandidateView extends View {
    private SoftKeyboard pService;
    private static final int OUT_OF_BOUNDS = -1;
    private List<String> pSuggestions;
    private int pTouchX = OUT_OF_BOUNDS;
    private boolean pTypedWordValid;
    private Drawable pSelectionHighlight;

    private Rect pBgPadding;
    private static final int SCROLL_PIXELS = 20;
    private static final int MAX_SUGGESTIONS = 10;
    private int[] pWordX = new int[MAX_SUGGESTIONS];
    private int[] pWordWidth = new int[MAX_SUGGESTIONS];

    private static final int X_GAP = 10;

    private int pColorRecommended;
    private int pColorNormal;
    private int pColorOther;
    private Paint pPaint;
    private int pVerticalPadding;
    private int pTargetScrollX;
    private boolean pScrolled;
    private static final List<String> EMPTY_LIST = new ArrayList<String>();
    private final int extraHeight = 25;
    private int pTotalWidth;
    private int pSelectedIndex;
    private GestureDetector pGestureDetector;

    /**
     * Construct a CandidateView for showing suggested words for completion.
     *
     * @param context
     */
    public PhotoCandidateView(Context context) {
        super(context);

        pSelectionHighlight = context.getResources().getDrawable(
                android.R.drawable.list_selector_background);
        pSelectionHighlight.setState(new int[]{
                android.R.attr.state_enabled,
                android.R.attr.state_focused,
                android.R.attr.state_window_focused,
                android.R.attr.state_pressed
        });
        Resources r = context.getResources();

        setBackgroundColor(r.getColor(R.color.candidate_background));

        pColorNormal = r.getColor(R.color.candidate_normal);
        pColorRecommended = r.getColor(R.color.candidate_recommended);
        pColorOther = r.getColor(R.color.candidate_other);
        pVerticalPadding = r.getDimensionPixelSize(R.dimen.candidate_vertical_padding);

        pPaint = new Paint();
        pPaint.setColor(pColorNormal);
        pPaint.setAntiAlias(true);
        pPaint.setTextSize(r.getDimensionPixelSize(R.dimen.candidate_font_height));
        pPaint.setStrokeWidth(0);

        pGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                pScrolled = true;
                int sx = getScrollX();
                sx += distanceX;
                if (sx < 0) {
                    sx = 0;
                }
                if (sx + getWidth() > pTotalWidth) {
                    sx -= distanceX;
                }
                pTargetScrollX = sx;
                scrollTo(sx, getScrollY());
                invalidate();
                return true;
            }
        });
        setHorizontalFadingEdgeEnabled(true);
        setWillNotDraw(false);
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);
    }

    /**
     * A connection back to the service to communicate with the text field
     *
     * @param listener
     */
    public void setService(SoftKeyboard listener) {
        pService = listener;
    }

    @Override
    public int computeHorizontalScrollRange() {
        return pTotalWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = resolveSize(50, widthMeasureSpec);

        // Get the desired height of the icon menu view (last row of items does
        // not have a divider below)
        Rect padding = new Rect();
        pSelectionHighlight.getPadding(padding);
        final int desiredHeight = ((int) pPaint.getTextSize()) + pVerticalPadding
                + padding.top + padding.bottom + extraHeight;

        // Maximum possible width and desired height
        setMeasuredDimension(measuredWidth,
                resolveSize(desiredHeight, heightMeasureSpec));
    }

    /**
     * If the canvas is null, then only touch calculations are performed to pick the target
     * candidate.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas != null) {
            super.onDraw(canvas);
        }
        pTotalWidth = 0;
        if (pSuggestions == null) return;

        if (pBgPadding == null) {
            pBgPadding = new Rect(0, 0, 0, 0);
            if (getBackground() != null) {
                getBackground().getPadding(pBgPadding);
            }
        }
        int x = 0;
        final int count = pSuggestions.size();
        final int height = getHeight();
        final Rect bgPadding = pBgPadding;
        final Paint paint = pPaint;
        final int touchX = pTouchX;
        final int scrollX = getScrollX();
        final boolean scrolled = pScrolled;
        final boolean typedWordValid = pTypedWordValid;
        final int y = (int) (((height - pPaint.getTextSize()) / 2) - pPaint.ascent());
        for (int i = 0; i < count; i++) {
            // Break the loop. This fix the app from crashing.
            if(i >= MAX_SUGGESTIONS){
                break;
            }
            String suggestion = pSuggestions.get(i);
            float textWidth = paint.measureText(suggestion);
            final int wordWidth = (int) textWidth + X_GAP * 2;
            pWordX[i] = x;
            pWordWidth[i] = wordWidth;
            paint.setColor(pColorNormal);
            if (touchX + scrollX >= x && touchX + scrollX < x + wordWidth && !scrolled) {
                if (canvas != null) {
                    canvas.translate(x, 0);
                    pSelectionHighlight.setBounds(0, bgPadding.top, wordWidth, height);
                    pSelectionHighlight.draw(canvas);
                    canvas.translate(-x, 0);
                }
                pSelectedIndex = i;
            }
            if (canvas != null) {
                if ((i == 1 && !typedWordValid) || (i == 0 && typedWordValid)) {
                    paint.setFakeBoldText(true);
                    paint.setColor(pColorRecommended);
                } else if (i != 0) {
                    paint.setColor(pColorOther);
                }

                canvas.drawText(suggestion, x + X_GAP, y, paint);
                paint.setColor(pColorOther);
                canvas.drawLine(x + wordWidth + 0.5f, bgPadding.top,
                        x + wordWidth + 0.5f, height + 1, paint);
                paint.setFakeBoldText(false);
            }
            x += wordWidth;
        }
        pTotalWidth = x;
        if (pTargetScrollX != getScrollX()) {
            scrolToTarget();
        }
    }

    private void scrolToTarget() {
        int sx = getScrollX();
        if (pTargetScrollX > sx) {
            sx += SCROLL_PIXELS;
            if (sx >= pTargetScrollX) {
                sx = pTargetScrollX;
                requestLayout();
            }
        } else {
            sx -= SCROLL_PIXELS;
            if (sx <= pTargetScrollX) {
                sx = pTargetScrollX;
                requestLayout();
            }
        }
        scrollTo(sx, getScrollY());
        invalidate();
    }

    @SuppressLint("WrongCall")
    public void setSuggestions(List<String> suggestions, boolean completions,
                               boolean typedWordValid) {
        clear();
        if (suggestions != null) {
            pSuggestions = new ArrayList<String>(suggestions);
        }
        pTypedWordValid = typedWordValid;
        scrollTo(0, 0);
        pTargetScrollX = 0;
        // Compute the total width
        onDraw(null);
        invalidate();
        requestLayout();
    }


    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (pGestureDetector.onTouchEvent(me)) {
            return true;
        }
        int action = me.getAction();
        int x = (int) me.getX();
        int y = (int) me.getY();
        pTouchX = x;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pScrolled = false;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (y <= 0) {
                    // Fling up!?
                    if (pSelectedIndex >= 0) {
                        pService.pickSuggestionManually(pSelectedIndex);
                        pSelectedIndex = -1;
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (!pScrolled) {
                    if (pSelectedIndex >= 0) {
                        pService.pickSuggestionManually(pSelectedIndex);
                    }
                }
                pSelectedIndex = -1;
                removeHighlights();
                requestLayout();
                break;
        }
        return true;
    }

    /**
     * For flick through from keyboard, call this method with the x coordinate of the flick
     * gesture.
     *
     * @param x
     */
    @SuppressLint("WrongCall")
    public void takeSuggestionAt(float x) {
        pTouchX = (int) x;
        // To detect candidate
        onDraw(null);
        if (pSelectedIndex >= 0) {
            pService.pickSuggestionManually(pSelectedIndex);
        }
        invalidate();
    }

    public void clear() {
        pSuggestions = EMPTY_LIST;
        pTouchX = OUT_OF_BOUNDS;
        pSelectedIndex = -1;
        invalidate();
    }

    private void removeHighlights() {
        pTouchX = OUT_OF_BOUNDS;
        invalidate();
    }
}