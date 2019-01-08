package com.pos.priory.utils;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hft on 2017/8/10.
 */

public class LogicUtils {
    // 计算并获取CheckSum
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    /**
     * 替换特特殊字符
     *
     * @param str 待转义的字符串
     * @return 转义后的字符串
     * @@author www.niubai.net.cn
     */
    public static String htmlToText(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String findChar = "";
        while (m.find()) {
            findChar = m.group();
            break;
        }
        return m.replaceAll("\\\\\\" + findChar).trim();
    }

    public static String getSign(Map<String, String> map) {

        String result = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).compareTo(o2.getKey());
                }
            });
            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (item.getKey() != null || item.getKey() != "") {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (!(val == "" || val == null)) {
                        sb.append(key + "=" + val + "&");
                    }
                }
            }
            result = sb.toString();
            result = result.substring(0, result.length() - 1);
        } catch (Exception e) {
            Log.e("ccbPay", "e:" + e.getMessage());
            return null;
        }
        return result;
    }


    /**
     * 禁止EditText输入特殊字符和表情
     */
    public static void setProhibitEmoji(final Context context, EditText et) {
        InputFilter[] filters = {new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                StringBuffer buffer = new StringBuffer();
                for (int i = start; i < end; i++) {
                    char codePoint = source.charAt(i);
                    if (!getIsEmoji(codePoint)) {
                        buffer.append(codePoint);
                    } else {
                        i++;
                        continue;
                    }
                }
                if (source instanceof Spanned) {
                    SpannableString sp = new SpannableString(buffer);
                    TextUtils.copySpansFrom((Spanned) source, start, end, null,
                            sp, 0);
                    return sp;
                } else {
                    return buffer;
                }
            }
        }, new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                StringBuffer buffer = new StringBuffer();
                Log.e("filter", "source:" + source);
                String str = source.toString().trim();
                if (str.equals("（）") || str.equals("-") || str.equals("【】")
                        || str.equals("（") || str.equals("）") || str.equals("()") || str.equals("-") ||
                        str.equals("[]") || str.equals("(") || str.equals(")") || str.equals("_") || str.equals(".")
                        || str.equals("*")|| str.equals("/")) {
                    Log.e("filter", "buffer");
                    buffer.append(source);
                    return buffer;
                } else {
                    for (int i = start; i < end; i++) {
                        char codePoint = source.charAt(i);
                        if (Character.getType(codePoint) <= Character.LETTER_NUMBER) {
                            buffer.append(codePoint);
                        } else {
                            i++;
                            continue;
                        }
                    }
                    if (source instanceof Spanned) {
                        SpannableString sp = new SpannableString(buffer);
                        try {
                            TextUtils.copySpansFrom((Spanned) source, start, end, null,
                                    sp, 0);
                        } catch (Exception e) {

                        } finally {
                            return sp;
                        }
                    } else {
                        return buffer;
                    }
                }
            }
        }};
        et.setFilters(filters);
    }

    public static boolean getIsEmoji(char codePoint) {
        if ((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)))
            return false;
        return true;
    }


    /**
     * 保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String getKeepLastTwoNumberAfterLittlePoint(float num) {
        try {
            return new DecimalFormat("##0.00").format(num);
        } catch (Exception e) {
            return "0.00";
        }

    }

    /**
     * 保留小数点后两位
     *
     * @param num
     * @return
     */
    public static String getKeepLastTwoNumberAfterLittlePoint(double num) {
        try {
            return new DecimalFormat("##0.00").format(num);
        } catch (Exception e) {
            return "0.00";
        }
    }

    /**
     * 保留小数点后3位
     *
     * @param num
     * @return
     */
    public static String getKeepLastThreeNumberAfterLittlePoint(float num) {
        try {
            return new DecimalFormat("##0.000").format(num);
        } catch (Exception e) {
            return "0.000";
        }
    }

    /**
     * 保留小数点后3位
     *
     * @param num
     * @return
     */
    public static String getKeepLastThreeNumberAfterLittlePoint(double num) {
        try {
            return new DecimalFormat("##0.000").format(num);
        } catch (Exception e) {
            return "0.000";
        }
    }


    // 计算并获取md5值
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getSha1(String str) {
        if (null == str || 0 == str.length()) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 转两位小数
     *
     * @param d
     * @return
     */
    public static String toDouble(Double d) {
        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
//        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
//        return bg.doubleValue();
        DecimalFormat df = new DecimalFormat(".00");
        return String.format("%.2f", d);
    }

    public static boolean isEmpty(String s) {
        if (s == null || s.length() == 0)
            return true;
        else
            return false;
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186、177
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][3456789]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    /**
     * String 转float类型 且保留两位小数
     *
     * @param string
     * @return
     */
    public static String floatShiftStr(String string) {
        float a = Float.parseFloat(string);
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(a);
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    //设置百分比
    public static String getPercent(int current, int all) {

        NumberFormat nt = NumberFormat.getPercentInstance();
        //设置百分数精确度2即保留两位小数
        nt.setMinimumFractionDigits(0);
        float baifen = (float) current / all;
        return nt.format(baifen);
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(@Nullable CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 字符串 SHA 加密
     *
     * @param
     * @return
     */
    public static String SHA(final String strText) {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0) {
            try {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte byteBuffer[] = messageDigest.digest();

                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return strResult;
    }

    public static String getFilemd5(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            return "";
        }
        FileInputStream in = null;
        String result = "";
        byte buffer[] = new byte[8192];
        int len;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
            }
            byte[] bytes = md5.digest();

            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    public static double parseStringToDouble(String str){
        try{
            double reuslt = Double.parseDouble(str);
            return reuslt;
        }catch (Exception e){
            return 0;
        }
    }

    //揭示动画
    public static void animateRevealShow(View view, View rootView) {
        if (Build.VERSION.SDK_INT >= 21) {
            // 获取FloatingActionButton的中心点的坐标
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            int centerX = location[0];
            int centerY = location[1];
            // 获取扩散的半径
            float finalRadius = (float) Math.hypot((double) centerX, (double) centerY);
            // 定义揭露动画
            Animator circularRevealAnim = ViewAnimationUtils.createCircularReveal(
                    rootView, centerX, centerY, 0, finalRadius);
            circularRevealAnim.setDuration(200);
            circularRevealAnim.setInterpolator(new AccelerateInterpolator());
            circularRevealAnim.start();
        }
    }


}
