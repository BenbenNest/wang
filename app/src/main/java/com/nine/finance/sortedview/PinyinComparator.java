package com.nine.finance.sortedview;

import com.nine.finance.model.BankInfo;

import java.util.Comparator;

/**
 * @author xiaanming
 */
public class PinyinComparator implements Comparator<BankInfo> {

    public int compare(BankInfo o1, BankInfo o2) {
        if (getSortLetters(o1.getBankName()).equals("@")
                || getSortLetters(o2.getBankName()).equals("#")) {
            return -1;
        } else if (getSortLetters(o1.getBankName()).equals("#")
                || getSortLetters(o2.getBankName()).equals("@")) {
            return 1;
        } else {
            return getSortLetters(o1.getBankName()).compareTo(getSortLetters(o2.getBankName()));
        }
    }

    public String getSortLetters(String name) {
        String pinyin = CharacterParser.getInstance().getSelling(name);
        String sortString = pinyin.substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            return sortString.toUpperCase();
        } else {
            return "#";
        }
    }

}
