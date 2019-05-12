package com.xenione.digit;

public abstract class AbstractTabAnimation {

    protected final static int LOWER_POSITION = 0;
    protected final static int MIDDLE_POSITION = 1;
    protected final static int UPPER_POSITION = 2;

    protected final TabDigit.Tab mTopTab;
    protected final TabDigit.Tab mBottomTab;
    protected final TabDigit.Tab mMiddleTab;

    protected int state;
    protected int mAlpha = 0;
    protected long mTime = -1;
    protected float mElapsedTime = 1000.0f;

    public AbstractTabAnimation(TabDigit.Tab mTopTab, TabDigit.Tab mBottomTab, TabDigit.Tab mMiddleTab) {
        this.mTopTab = mTopTab;
        this.mBottomTab = mBottomTab;
        this.mMiddleTab = mMiddleTab;
        initState();
    }

    public void start() {
        makeSureCycleIsClosed();
        mTime = System.currentTimeMillis();
    }

    public void sync() {
        makeSureCycleIsClosed();
    }

    public abstract void initState();
    public abstract void initMiddleTab();
    public abstract void run();
    protected abstract void makeSureCycleIsClosed();

}
