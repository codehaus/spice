package org.componenthaus.tests;

import org.prevayler.PrevalentSystem;
import org.prevayler.AlarmClock;

public class MockPrevalentSystem implements PrevalentSystem {
    public void clock(AlarmClock alarmClock) {
    }

    public AlarmClock clock() {
        return null;
    }
}
