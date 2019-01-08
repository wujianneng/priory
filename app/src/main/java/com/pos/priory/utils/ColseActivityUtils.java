package com.pos.priory.utils;

import android.app.Activity;

import com.pos.priory.activitys.AddNewOrderActivity;
import com.pos.priory.activitys.ChangeGoodsActivity;
import com.pos.priory.activitys.MemberActivity;
import com.pos.priory.activitys.MemberInfoActivity;
import com.pos.priory.activitys.OrderDetialActivity;
import com.pos.priory.activitys.ReturnBalanceActivity;
import com.pos.priory.activitys.ReturnGoodsActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by hft on 2017/8/27.
 */

public class ColseActivityUtils {
    public static List<Activity> activityList = new LinkedList<Activity>();

    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void finishWholeFuntionActivitys() {
        try {
            if (activityList.size() > 0) {
                for (Activity activity : activityList) {
                    if (activity instanceof AddNewOrderActivity || activity instanceof MemberActivity
                            || activity instanceof OrderDetialActivity || activity instanceof ChangeGoodsActivity
                            || activity instanceof MemberInfoActivity || activity instanceof ReturnGoodsActivity
                            || activity instanceof ReturnBalanceActivity) {
                        activity.finish();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeActivity(Activity activity) {
        if (activityList != null) {
            boolean bResult = activityList.remove(activity);
            while (bResult) {
                bResult = activityList.remove(activity);
            }
        }
    }


    public static void exic() {
        try {
            if (activityList.size() > 0) {
                for (Activity activitys : activityList) {
                    try {
                        activitys.finish();
                    } catch (Exception e) {
                    }
                }
            }
            android.os.Process.killProcess(android.os.Process.myPid());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeAllAcitivty() {
        try {
            if (activityList.size() > 0) {
                for (Activity activity : activityList) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
