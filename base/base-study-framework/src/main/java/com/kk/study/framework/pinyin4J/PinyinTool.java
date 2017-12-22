package com.kk.study.framework.pinyin4J;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;  
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;  
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;  
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;  
  
import java.util.Arrays;  
  
/** 
 * 
 * Created by zengxm on 2014/12/4. 
 */  
public class PinyinTool {

    private static HanyuPinyinOutputFormat format = null;

    public PinyinTool(){
        format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    public static enum Type {
        UPPERCASE,              //全部大写  
        LOWERCASE,              //全部小写  
        FIRSTUPPER              //首字母大写  
    }

    public String toPinYin(String str)  {
        return toPinYin(str, "", Type.UPPERCASE);
    }

    public String toPinYin(String str, String spera)  {
        return toPinYin(str, spera, Type.UPPERCASE);
    }

    /**
     * 将str转换成拼音，如果不是汉字或者没有对应的拼音，则不作转换
     * 如： 明天 转换成 MINGTIAN
     *
     * @param str
     * @param spera
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public String toPinYin(String str, String spera, Type type) {
        if ("".equals(str) || str.trim().length() == 0)
            return "";
        if (type == Type.UPPERCASE) {
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        } else {
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        }

        String py = "";
        String temp = "";
        String[] t;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((int) c <= 128)
                py += c;
            else {
                /**
                 * PinyinHelper.toHanyuPinyinStringArray说明：
                 * 1、参数传中文，返回值是这个汉字的小写拼音+这个汉字是几声。比如：参数传 "汉 "，return的String[ ] 就是 [han4]
                 * 2、当传字母时，返回值是null。
                 * 注意：toHanyuPinyinStringArray接收的参数是char，意思就是说一次只能转换一个，
                 * 比如“美”是string，toHanyuPinyinStringArray不能直接接收，
                 * 每次只能传一个，返回的那个String数组里肯定只有一个元素。
                 */
                try {
                    t = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if (null == t) {
                        py += c;
                    } else {
                        temp = t[0];
                        if (type == Type.FIRSTUPPER)
                            temp = t[0].toUpperCase().charAt(0) + temp.substring(1);
                        py += temp + (i == str.length() - 1 ? "" : spera);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }

            }
        }
        return py.trim();
    }

    public static void main(String[] args) {
        PinyinTool pinyinTool = new PinyinTool();
        System.out.println(pinyinTool.toPinYin("明天ABC"));
        System.out.println(pinyinTool.toPinYin("明天ABC", "_"));
        System.out.println(pinyinTool.toPinYin("明天ABC", "_", Type.LOWERCASE));
        System.out.println(pinyinTool.toPinYin("明天ABC", "_", Type.FIRSTUPPER));
    }

}