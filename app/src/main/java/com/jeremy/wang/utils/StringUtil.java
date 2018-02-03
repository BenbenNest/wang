package com.jeremy.wang.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String过滤
 *
 * @author jeremy
 */

public class StringUtil {

    public static final int INDEX_NOT_FOUND = -1;

    /**
     * 检测是否有emoji表情 * @param source * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji * @param codePoint 比较的单个字符 * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

//    public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
//        if (cs1 == cs2) {
//            return true;
//        }
//        if (cs1 == null || cs2 == null) {
//            return false;
//        }
//        if (cs1.length() != cs2.length()) {
//            return false;
//        }
//        if (cs1 instanceof String && cs2 instanceof String) {
//            return cs1.equals(cs2);
//        }
//        return CharSequenceUtils.regionMatches(cs1, false, 0, cs2, 0, cs1.length());
//    }

    public static String removeStart(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.startsWith(remove)) {
            return str.substring(remove.length());
        }
        return str;
    }

    public static String replace(final String text, final String searchString, final String replacement, final int max) {
        return replace(text, searchString, replacement, max, false);
    }

    public static String replace(final String text, final String searchString, final String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    private static String replace(final String text, String searchString, final String replacement, int max, final boolean ignoreCase) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        String searchText = text;
        if (ignoreCase) {
            searchText = text.toLowerCase();
            searchString = searchString.toLowerCase();
        }
        int start = 0;
        int end = searchText.indexOf(searchString, start);
        if (end == INDEX_NOT_FOUND) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? 16 : max > 64 ? 64 : max;
        final StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != INDEX_NOT_FOUND) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = searchText.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    public static String removeEnd(final String str, final String remove) {
        if (isEmpty(str) || isEmpty(remove)) {
            return str;
        }
        if (str.endsWith(remove)) {
            return str.substring(0, str.length() - remove.length());
        }
        return str;
    }

//    public static boolean startsWith(final CharSequence str, final CharSequence prefix) {
//        return startsWith(str, prefix, false);
//    }

//    private static boolean startsWith(final CharSequence str, final CharSequence prefix, final boolean ignoreCase) {
//        if (str == null || prefix == null) {
//            return str == null && prefix == null;
//        }
//        if (prefix.length() > str.length()) {
//            return false;
//        }
//        return CharSequenceUtils.regionMatches(str, ignoreCase, 0, prefix, 0, prefix.length());
//    }

    public static String replaceCR(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * emoij表情过滤
     * @param text
     * @return
     */
    public static String filterEmoij(String text) {
        if(TextUtils.isEmpty(text)) {
            return "";
        }

        Pattern emoji = Pattern.compile(
                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = emoji.matcher(text);
        StringBuffer sb = new StringBuffer();
        // 使用 find() 方法查找第一个匹配的对象
        boolean result = matcher.find();
        // 使用循环将句子里所有的表情找出并替换再将内容加到 sb 里
        while(result) {
            matcher.appendReplacement(sb, "");
            // 继续查找下一个匹配对象
            result = matcher.find();
        }
        // 最后调用 appendTail() 方法将最后一次匹配后的剩余字符串加到 sb 里；
        matcher.appendTail(sb);
        return sb.toString();
    }
}
