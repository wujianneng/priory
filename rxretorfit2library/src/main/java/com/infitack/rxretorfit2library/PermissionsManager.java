package com.infitack.rxretorfit2library;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/3/3.
 */

public class PermissionsManager {
    private static final String TAG = PermissionsManager.class.getSimpleName();
    private final Set<String> mPendingRequests = new HashSet(1);
    private final Set<String> mPermissions = new HashSet(1);
    private final List<WeakReference<PermissionsResultAction>> mPendingActions = new ArrayList(1);
    private static PermissionsManager mInstance = null;

    public static PermissionsManager getInstance() {
        if(mInstance == null) {
            mInstance = new PermissionsManager();
        }

        return mInstance;
    }

    private PermissionsManager() {
        this.initializePermissionsMap();
    }

    private synchronized void initializePermissionsMap() {
        Field[] fields = Manifest.permission.class.getFields();
        Field[] var5 = fields;
        int var4 = fields.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            Field field = var5[var3];
            String name = null;

            try {
                name = (String)field.get("");
            } catch (IllegalAccessException var8) {
                Log.e(TAG, "Could not access field", var8);
            }

            this.mPermissions.add(name);
        }

    }

    @NonNull
    private synchronized String[] getManifestPermissions(@NonNull Activity activity) {
        PackageInfo packageInfo = null;
        ArrayList list = new ArrayList(1);

        try {
            Log.d(TAG, activity.getPackageName());
            packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 4096);
        } catch (PackageManager.NameNotFoundException var9) {
            Log.e(TAG, "A problem occurred when retrieving permissions", var9);
        }

        if(packageInfo != null) {
            String[] permissions = packageInfo.requestedPermissions;
            if(permissions != null) {
                String[] var8 = permissions;
                int var7 = permissions.length;

                for(int var6 = 0; var6 < var7; ++var6) {
                    String perm = var8[var6];
                    Log.d(TAG, "Manifest contained permission: " + perm);
                    list.add(perm);
                }
            }
        }

        return (String[])list.toArray(new String[list.size()]);
    }

    private synchronized void addPendingAction(@NonNull String[] permissions, @Nullable PermissionsResultAction action) {
        if(action != null) {
            action.registerPermissions(permissions);
            this.mPendingActions.add(new WeakReference(action));
        }
    }

    private synchronized void removePendingAction(@Nullable PermissionsResultAction action) {
        Iterator iterator = this.mPendingActions.iterator();

        while(true) {
            WeakReference weakRef;
            do {
                if(!iterator.hasNext()) {
                    return;
                }

                weakRef = (WeakReference)iterator.next();
            } while(weakRef.get() != action && weakRef.get() != null);

            iterator.remove();
        }
    }

    public synchronized boolean hasPermission(@Nullable Context context, @NonNull String permission) {
        return context != null && (ActivityCompat.checkSelfPermission(context, permission) == 0 || !this.mPermissions.contains(permission));
    }

    public synchronized boolean hasAllPermissions(@Nullable Context context, @NonNull String[] permissions) {
        if(context == null) {
            return false;
        } else {
            boolean hasAllPermissions = true;
            String[] var7 = permissions;
            int var6 = permissions.length;

            for(int var5 = 0; var5 < var6; ++var5) {
                String perm = var7[var5];
                hasAllPermissions &= this.hasPermission(context, perm);
            }

            return hasAllPermissions;
        }
    }

    public synchronized void requestAllManifestPermissionsIfNecessary(@Nullable Activity activity, @Nullable PermissionsResultAction action) {
        if(activity != null) {
            String[] perms = this.getManifestPermissions(activity);
            this.requestPermissionsIfNecessaryForResult(activity, perms, action);
        }
    }

    public synchronized void requestPermissionsIfNecessaryForResult(@Nullable Activity activity, @NonNull String[] permissions, @Nullable PermissionsResultAction action) {
        if(activity != null) {
            this.addPendingAction(permissions, action);
            if(Build.VERSION.SDK_INT < 23) {
                this.doPermissionWorkBeforeAndroidM(activity, permissions, action);
            } else {
                List permList = this.getPermissionsListToRequest(activity, permissions, action);
                if(permList.isEmpty()) {
                    this.removePendingAction(action);
                } else {
                    String[] permsToRequest = (String[])permList.toArray(new String[permList.size()]);
                    this.mPendingRequests.addAll(permList);
                    ActivityCompat.requestPermissions(activity, permsToRequest, 1);
                }
            }

        }
    }

    public synchronized void requestPermissionsIfNecessaryForResult(@NonNull Fragment fragment, @NonNull String[] permissions, @Nullable PermissionsResultAction action) {
        FragmentActivity activity = fragment.getActivity();
        if(activity != null) {
            this.addPendingAction(permissions, action);
            if(Build.VERSION.SDK_INT < 23) {
                this.doPermissionWorkBeforeAndroidM(activity, permissions, action);
            } else {
                List permList = this.getPermissionsListToRequest(activity, permissions, action);
                if(permList.isEmpty()) {
                    this.removePendingAction(action);
                } else {
                    String[] permsToRequest = (String[])permList.toArray(new String[permList.size()]);
                    this.mPendingRequests.addAll(permList);
                    fragment.requestPermissions(permsToRequest, 1);
                }
            }

        }
    }

    public synchronized void notifyPermissionsChange(@NonNull String[] permissions, @NonNull int[] results) {
        int size = permissions.length;
        if(results.length < size) {
            size = results.length;
        }

        Iterator iterator = this.mPendingActions.iterator();

        while(true) {
            while(iterator.hasNext()) {
                PermissionsResultAction n = (PermissionsResultAction)((WeakReference)iterator.next()).get();

                for(int n1 = 0; n1 < size; ++n1) {
                    if(n == null || n.onResult(permissions[n1], results[n1])) {
                        iterator.remove();
                        break;
                    }
                }
            }

            for(int var7 = 0; var7 < size; ++var7) {
                this.mPendingRequests.remove(permissions[var7]);
            }

            return;
        }
    }

    private void doPermissionWorkBeforeAndroidM(@NonNull Activity activity, @NonNull String[] permissions, @Nullable PermissionsResultAction action) {
        String[] var7 = permissions;
        int var6 = permissions.length;

        for(int var5 = 0; var5 < var6; ++var5) {
            String perm = var7[var5];
            if(action != null) {
                if(!this.mPermissions.contains(perm)) {
                    action.onResult(perm, PermissionsResultAction.Permissions.NOT_FOUND);
                } else if(ActivityCompat.checkSelfPermission(activity, perm) != 0) {
                    action.onResult(perm, PermissionsResultAction.Permissions.DENIED);
                } else {
                    action.onResult(perm, PermissionsResultAction.Permissions.GRANTED);
                }
            }
        }

    }

    @NonNull
    private List<String> getPermissionsListToRequest(@NonNull Activity activity, @NonNull String[] permissions, @Nullable PermissionsResultAction action) {
        ArrayList permList = new ArrayList(permissions.length);
        String[] var8 = permissions;
        int var7 = permissions.length;

        for(int var6 = 0; var6 < var7; ++var6) {
            String perm = var8[var6];
            if(!this.mPermissions.contains(perm)) {
                if(action != null) {
                    action.onResult(perm, PermissionsResultAction.Permissions.NOT_FOUND);
                }
            } else if(ActivityCompat.checkSelfPermission(activity, perm) != 0) {
                if(!this.mPendingRequests.contains(perm)) {
                    permList.add(perm);
                }
            } else if(action != null) {
                action.onResult(perm, PermissionsResultAction.Permissions.GRANTED);
            }
        }

        return permList;
    }
}