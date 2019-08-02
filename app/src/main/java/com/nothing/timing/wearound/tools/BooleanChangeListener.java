package com.nothing.timing.wearound.tools;

public class BooleanChangeListener {

        private boolean bool;
        private ChangeListener listener;

        public boolean isBool() {
            return bool;
        }

        public void setBool(boolean bool) {

            this.bool = bool;
            if (listener != null) listener.onBooleanChange();
        }

        public interface ChangeListener {
            void onBooleanChange();
        }
}
