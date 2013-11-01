package com.ijsbrandslob.appirater;

import java.util.ArrayList;

import android.app.Application;

public class AppiraterApplication extends Application {
    private boolean                                  mInBackground = true;
    private ArrayList<ApplicationLifecycleCallbacks> mListeners;

    
    // Anyone listening for Application level pause resume should implement this.
    public interface ApplicationLifecycleCallbacks {
        public void onApplicationPause();
        public void onApplicationResume(AppiraterActivity activity);
        public void onApplicationLaunch(AppiraterActivity activity);
    }
 
    public void addApplicationLifecycleCallbacks(ApplicationLifecycleCallbacks callback) {
        if (mListeners == null)
            mListeners = new ArrayList<ApplicationLifecycleCallbacks>();
 
        mListeners.add(callback);
    }
 
    public void removeApplicationLifecycleCallbacks(ApplicationLifecycleCallbacks callback) {
        if (mListeners == null)
            return;
 
        mListeners.remove(callback);
    }
 
    // When any activity pauses with the new activity not being ours.
    public void onActivityPause(AppiraterActivity activity) {
        if (activity.wasAppiraterActivityOpened() || mListeners == null)
            return;
 
        mInBackground = true;
 
        for (ApplicationLifecycleCallbacks listener: mListeners)
            listener.onApplicationPause();
    }
 
    // When any activity resumes with a previous activity not being ours.
    public void onActivityResume(AppiraterActivity activity) {
        // This is a misnomer. Should have been "wasGeckoActivityOpened".
        if (activity.wasAppiraterActivityOpened() || mListeners == null)
            return;
 
        for (ApplicationLifecycleCallbacks listener: mListeners)
            listener.onApplicationResume(activity);
 
        mInBackground = false;
    }
    
    public void onApplicationLaunch(AppiraterActivity activity) {
        if (mInBackground && mListeners != null) {
            for (ApplicationLifecycleCallbacks listener: mListeners)
                listener.onApplicationLaunch(activity);
        }
        mInBackground = false;
    }
 
    // Helper to find if the Application is in background from any activity.
    public boolean isApplicationInBackground() {
        return mInBackground;
    }
}
