package org.android.tiffinseva.utils;

import android.os.SystemClock;
import android.view.View;

import java.util.Map;
import java.util.WeakHashMap;

public abstract class TssDebounceOnClickListener implements View.OnClickListener {
    private final long minimumInterval;
    private Map<View, Long> lastClickMap;

    /**
     * Implement this in your subclass instead of onClick
     */
    public abstract void debounceOnClick(View view);

    /**
     * The one and only constructor
     *
     * @param minimumIntervalMsec The minimum allowed time between clicks - any click sooner than
     *                            this after a previous click will be rejected
     */
    public TssDebounceOnClickListener(long minimumIntervalMsec) {
        this.minimumInterval = minimumIntervalMsec;
        this.lastClickMap = new WeakHashMap<View, Long>();
    }

    @Override
    public void onClick(View clickedView) {
        Long previousClickTimestamp = lastClickMap.get(clickedView);
        long currentTimestamp = SystemClock.uptimeMillis();

        lastClickMap.put(clickedView, currentTimestamp);
        if (previousClickTimestamp == null ||
                (currentTimestamp - previousClickTimestamp.longValue() > minimumInterval)) {
            debounceOnClick(clickedView);
        }
    }
}
