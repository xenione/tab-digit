package com.xenione.digit;

/**
 * rotates middle tab upwards
 */
public final class TabAnimationUp extends AbstractTabAnimation {

    public TabAnimationUp(TabDigit.Tab mTopTab, TabDigit.Tab mBottomTab, TabDigit.Tab mMiddleTab) {
        super(mTopTab, mBottomTab, mMiddleTab);
    }

    @Override
    public void initState() {
        state = LOWER_POSITION;
    }

    @Override
    public void initMiddleTab() { /* nothing to do */ }

    @Override
    public void run() {

        if (mTime == -1) {
            return;
        }

        switch (state) {
            case LOWER_POSITION: {
                mBottomTab.next();
                state = MIDDLE_POSITION;
                break;
            }
            case MIDDLE_POSITION: {
                if (mAlpha > 90) {
                    mMiddleTab.next();
                    state = UPPER_POSITION;
                }
                break;
            }
            case UPPER_POSITION: {
                if (mAlpha >= 180) {
                    mTopTab.next();
                    state = LOWER_POSITION;
                    mTime = -1; // animation finished
                }
                break;
            }
        }

        if (mTime != -1) {
            long delta = (System.currentTimeMillis() - mTime);
            mAlpha = (int) (180 * (1 - (1 * mElapsedTime - delta) / (1 * mElapsedTime)));
            mMiddleTab.rotate(mAlpha);
        }

    }

    @Override
    protected void makeSureCycleIsClosed() {
        if (mTime == -1) {
            return;
        }
        switch (state) {
            case LOWER_POSITION: {
                mMiddleTab.next();
                state = UPPER_POSITION;
            }
            case UPPER_POSITION: {
                mTopTab.next();
                state = LOWER_POSITION;
                mTime = -1; // animation finished
            }
        }
        mMiddleTab.rotate(180);
    }
}
