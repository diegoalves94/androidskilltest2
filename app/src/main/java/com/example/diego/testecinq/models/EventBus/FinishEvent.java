package com.example.diego.testecinq.models.EventBus;

public class FinishEvent {
    private boolean isFinished;

    public FinishEvent() {
    }

    public FinishEvent(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
