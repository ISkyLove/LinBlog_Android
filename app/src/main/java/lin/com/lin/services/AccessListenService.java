package lin.com.lin.services;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class AccessListenService extends AccessibilityService {
    public AccessListenService() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int eventtype=accessibilityEvent.getEventType();
        switch (eventtype){
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                AccessibilityNodeInfo nodeInfo1 = accessibilityEvent.getSource();

                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                AccessibilityNodeInfo nodeInfo2 = accessibilityEvent.getSource();
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                AccessibilityNodeInfo nodeInfo3 = accessibilityEvent.getSource();
                break;
        }
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }
}
