package com.ningkangyuan.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ningkangyuan.MyApplication;
import com.ningkangyuan.R;
/**
 * Created by xuchun on 2016/8/22.
 */
public class VerifyUtil {

    public static String IDCardValidate(String IDStr) {
        String tipInfo = "";//  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_1)
        String Ai = "";
        //  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_2) 15 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_3)18 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_4)
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            tipInfo = " " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_5) + "15 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_3) + "18 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_7) + " ";
            return tipInfo;
        }


        // 18 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_8)17 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_9)15 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_10)
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0,  17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0,  6) + "19" + IDStr.substring(6,  15);
        }
        if (isNumeric(Ai) == false) {
            tipInfo = " " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_14) + "15 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_15) + " ; 18 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_16) + " ";
            return tipInfo;
        }


        //  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_17)
        String strYear = Ai.substring(6,  10);//  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_19)
        String strMonth = Ai.substring(10,  12);//  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_21)
        String strDay = Ai.substring(12,  14);//  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_23)
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            tipInfo = MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_24);
            return tipInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                tipInfo = MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_25);
                return tipInfo;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            tipInfo = MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_26);
            return tipInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            tipInfo = MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_27);
            return tipInfo;
        }


        //  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_28)
        Hashtable areacode = GetAreaCode();
        // " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_29)Hashtable " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_30)
        if (areacode.get(Ai.substring(0,  2)) == null) {
            tipInfo = MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_32);
            return tipInfo;
        }

        if(isVarifyCode(Ai, IDStr)==false){
            tipInfo = MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_34);
            return tipInfo;
        }


        return tipInfo;
    }


    /*
     *  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_35)18 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_36)
    *  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_37)18 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_38)：
       　　1.  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_39)17 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_40)
       　　 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_41)：S = Sum(Ai * Wi),  i = 0,  ... ,  16
       　　 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_45)Ai " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_46)i " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_47)Wi " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_46)i " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_49)： 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
       　　2.  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_50)11 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_51)
       　　Y = mod(S,  11)
       　　3.  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_53)
       　　 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_54)：
       　　 Y " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_55)：     0  1  2  3  4  5  6  7  8  9  10
       　　 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_56)： 1  0  X  9  8  7  6  5  4  3   2
    */
    private static boolean isVarifyCode(String Ai, String IDStr) {
        String[] VarifyCode = { "1",  "0",  "X",  "9",  "8",  "7",  "6",  "5",  "4", "3",  "2" };
        String[] Wi = { "7",  "9",  "10",  "5",  "8",  "4",  "2",  "1",  "6",  "3",  "7", "9",  "10",  "5",  "8",  "4",  "2" };
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum = sum + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = sum % 11;
        String strVerifyCode = VarifyCode[modValue];
        Ai = Ai + strVerifyCode;
        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                return false;

            }
        }
        return true;
    }


    /**
     *  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_84)Hashtable " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_85)
     * @return Hashtable  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_86)
     */

    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_88));
        hashtable.put("12",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_90));
        hashtable.put("13",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_92));
        hashtable.put("14",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_94));
        hashtable.put("15",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_96));
        hashtable.put("21",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_98));
        hashtable.put("22",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_100));
        hashtable.put("23",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_102));
        hashtable.put("31",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_104));
        hashtable.put("32",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_106));
        hashtable.put("33",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_108));
        hashtable.put("34",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_110));
        hashtable.put("35",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_112));
        hashtable.put("36",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_114));
        hashtable.put("37",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_116));
        hashtable.put("41",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_118));
        hashtable.put("42",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_120));
        hashtable.put("43",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_122));
        hashtable.put("44",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_124));
        hashtable.put("45",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_126));
        hashtable.put("46",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_128));
        hashtable.put("50",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_130));
        hashtable.put("51",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_132));
        hashtable.put("52",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_134));
        hashtable.put("53",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_136));
        hashtable.put("54",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_138));
        hashtable.put("61",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_140));
        hashtable.put("62",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_142));
        hashtable.put("63",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_144));
        hashtable.put("64",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_146));
        hashtable.put("65",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_148));
        hashtable.put("71",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_150));
        hashtable.put("81",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_152));
        hashtable.put("82",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_154));
        hashtable.put("91",  MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_156));
        return hashtable;
    }

    /**
     *  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_157)0-9 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_158)0 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_159)
     * @param strnum
     * @return
     */
    private static boolean isNumeric(String strnum) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(strnum);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_160)： " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_161)： " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_162)、 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_163)31 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_164)、30 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_165)28 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_166)29 " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_164)
     *
     * @param strDate
     * @return
     */
    private static boolean isDate(String strDate) {
        Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     *  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_168)
     *
     * @param  str
     * @return  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_169)true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3, 4, 5, 8][0-9]{9}$"); //  " + MyApplication.mContext.getResources().getString(R.string.VerifyUtil_java_173)
        m = p.matcher(str);
        b = m.matches();
        return b;
    }
}