package com.example.LockGuardPro.widget.patternlockview.listener;

/**
 * Created by aritraroy on 19/03/17.
 */


import com.example.LockGuardPro.widget.patternlockview.PatternLockView;

import java.util.List;

/**
 * The callback interface for detecting patterns entered by the user
 */
public interface PatternLockViewListenerqqq {

    /**
     * Fired when the pattern drawing has just started
     */
    void onStarted();

    /**
     * Fired when the pattern is still being drawn and progressed to
     * one more {@link PatternLockView.Dot}
     */
    void onProgress(List<PatternLockView.Dot> progressPattern);

    /**
     * Fired when the user has completed drawing the pattern and has moved their finger away
     * from the view
     */
    void onComplete(List<PatternLockView.Dot> pattern);

    /**
     * Fired when the patten has been cleared from the view
     */
    void onCleared();
}