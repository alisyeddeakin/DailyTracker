package deakin.com.example.healthbot.Common;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

import deakin.com.example.healthbot.R;


public class ATProgressBar extends ProgressBar {

    Timer timer;
    int progress = 0;
    int totalTime = 10000; // millis
    TimerTask timerTask;
    OnProgressCompletionListener onProgressCompletionListener;
    Context context;
    boolean isRunning = false;

    public ATProgressBar(Context context) {
        super(context);
        init(context);
    }

    public ATProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ATProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public ATProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    private void init(Context context) {
        this.context = context;
        timer = new Timer();
        setProgressDrawable(context.getResources().getDrawable(R.drawable.bg_progress));
    }

    public void startProgress() {
        isRunning = true;
        if (timerTask != null) {
            timerTask.cancel();
        }
        createNewTask();
        progress = 0;
        timer.scheduleAtFixedRate(timerTask, 0, totalTime / getMax());
    }

    public void createNewTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                progress += 1;
                if (progress < getMax() && isRunning) {
                    setProgress(progress);
                } else {

                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //progress is completed here
                            if (onProgressCompletionListener != null) {
                                onProgressCompletionListener.progressCompleted();
                            }
                        }
                    });
                    timerTask.cancel();
                }
            }
        };
    }

    public void stopProgress() {
        isRunning = true;
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    public void resumeProgress() {
        isRunning = true;
        if (timerTask != null) {
            timerTask.cancel();
        }
        createNewTask();
        timer.scheduleAtFixedRate(timerTask, 0, totalTime / getMax());
    }

    public void resetProgress() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        progress = 0;
        setProgress(0);
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public void setOnProgressCompletionListener(OnProgressCompletionListener onProgressCompletionListener) {
        this.onProgressCompletionListener = onProgressCompletionListener;
    }

    public interface OnProgressCompletionListener {
        void progressCompleted();
    }
}
