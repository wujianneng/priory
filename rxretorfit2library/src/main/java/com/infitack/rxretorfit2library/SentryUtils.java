package com.infitack.rxretorfit2library;

import io.sentry.Sentry;
import io.sentry.event.BreadcrumbBuilder;
import io.sentry.event.UserBuilder;

public class SentryUtils {
    /**
     * An example method that throws an exception.
     */
    public static void unsafeMethod(String errer) {
        throw new UnsupportedOperationException(errer);
    }

    /**
     * Note that the ``Sentry.init`` method must be called before the static API
     * is used, otherwise a ``NullPointerException`` will be thrown.
     */
    public static void logWithStaticAPI(String msg) {
        /*
        Record a breadcrumb in the current context which will be sent
        with the next event(s). By default the last 100 breadcrumbs are kept.
        */
        Sentry.getContext().recordBreadcrumb(
                new BreadcrumbBuilder().setMessage(msg).build()
        );


        // Set the user in the current context.
        Sentry.getContext().setUser(
                new UserBuilder().setEmail("1149452014@qq.com").build()
        );
//       Sentry.capture("yyyyyyyyyyyyyy");
    }


//    public static void logSetUser(String emaill){
//
//    }

    /*
      This sends a simple event to Sentry using the statically stored instance
      that was created in the ``main`` method.
      */
    public static void logCapture(String capture) {

//        if (BuildConfig.xmlrpcUrl.equals("https://portal.hft-pro.esmart365.com")){   //生产环境
        Sentry.capture(capture);
//        }else if(BuildConfig.xmlrpcUrl.equals("https://saas.odoo-stagin.esmart365.com")){ //测试环境
//            Sentry.capture("测试环境"+capture);
//        }else if(BuildConfig.xmlrpcUrl.equals("https://hft-demo.esmart365.com")){  //演示环境
//            Sentry.capture("演示环境"+capture);
//        }

    }

    public static void logErrer(String logerrer) {
        try {
            unsafeMethod(logerrer);
        } catch (Exception e) {
            // This sends an exception event to Sentry using the statically stored instance
            // that was created in the ``main`` method.
            Sentry.capture(e);

        }
    }
}
