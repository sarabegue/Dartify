package com.mentit.dartify.HelperClasses;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

public class PermissionCallback {

    public interface IPermissionResponse {
        void onCompleted(GraphResponse response);
    }

    private GraphRequest.Callback mCallback;

    public PermissionCallback(final IPermissionResponse caller) {
        // Handled by PermissionsActivity
        mCallback = response -> caller.onCompleted(response);
    }

    public GraphRequest.Callback getCallback() {
        return mCallback;
    }
}
