package com.kehua.wisetools.utils;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.kehua.wisetools.common.APP;
import com.kehua.wisetools.model.prefs.SPProvider;
import java.util.Locale;

/**
* 多语言切换工具类（尝试版）
* 2017-12
*/
public class LanUtils {

    public final static String Chinese = "zh";
    public final static String English = "en";
    public final static String DefaultLanguage = "default";

    private static String systemLanguage;
    //    private static String localLanguage;
//    private static String userSelectLanguage;
    private static Locale targetLable;

    /**
     * @des:App初始化时候进行语言设置
     */
    public static void init(Application application) {
        systemLanguage = getLocalLanguage(application);

        String spSelectedLanguage = SPProvider.getLanguageSelect();
        //如果没有设置,设置SP即可，否则进行语言切换
        if (StringUtils.isTrimEmpty(spSelectedLanguage)) {
            SPProvider.saveLanguageSelect(Chinese);
        } else {
            languageSelect(application, spSelectedLanguage, true);
        }
    }

    /**
     * 查看本地配置
     */
    private static String getLocalLanguage() {
        return getLocalLanguage(APP.getInstance());
    }

    private static String getLocalLanguage(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        Locale locale = android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                ? configuration.getLocales().get(0)
                : configuration.locale;

        String language = locale.getLanguage();
        //判断语言(默认返回英文)
        if (language.endsWith(Chinese)) {
            return Chinese;
        }
        return English;
    }

    /**
     * 语言选择,返回值代表是否真的进行语言切换
     */
    public static boolean languageSelect(String targetLanguage, boolean forceUpdate) {
        return languageSelect(APP.getInstance(), targetLanguage, forceUpdate);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean languageSelect(Context context, String targetLanguage, boolean forceUpdate) {
        if (StringUtils.isTrimEmpty(targetLanguage)) {
            return false;
        }
        //先存储到SP用于标记用户选择
        SPProvider.saveLanguageSelect(targetLanguage);

        //因为有跟随系统选项，所以需要进一步转化（当处于跟随系统选项，则进行获取系统语言标志）
        targetLanguage = getTargetLanguage(targetLanguage);
        String localLanguage = getLocalLanguage(context);

        //如果目标语言与本地语言一致，则不继续操作，否则进行语言切换
        if ((!forceUpdate) && targetLanguage.equals(localLanguage)) {
            return false;
        }

        //切换本地语言库配置
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        targetLable = Locale.getDefault();

        switch (targetLanguage) {
            case Chinese:
                targetLable = Locale.CHINESE;
                break;
            default:
                targetLable = Locale.ENGLISH;
                break;
        }
        //配置，根据sdk 版本进行设置（7.0以上设置方式不同）
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wrap(context, targetLable);
        } else {
            configuration.locale = targetLable;
            configuration.setLayoutDirection(targetLable);
            resources.updateConfiguration(configuration, dm);
        }

        if (ActivityUtils.getTopActivity() != null) {
            //需要重建才能生效配置
            ActivityUtils.getTopActivity().recreate();
        }
        return true;
    }

    /**
     * 语言更新配置
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static ContextWrapper wrap(Context context, Locale newLocale) {
        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale);
            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context = context.createConfigurationContext(configuration);
        } else {
            configuration.setLocale(newLocale);
            context = context.createConfigurationContext(configuration);
        }
        res.updateConfiguration(configuration, res.getDisplayMetrics());

        return new ContextWrapper(context);
    }

    /**
     * 因为还有跟随系统选项，所以需要对选择的语言进行进一步识别
     */
    public static String getTargetLanguage() {
        return getTargetLanguage(SPProvider.getLanguageSelect());
    }

    public static String getTargetLanguage(String targetLanguage) {
        String result = English;
        switch (targetLanguage) {
            case DefaultLanguage:
                result = systemLanguage;
                break;
            case Chinese:
                result = Chinese;
                break;
        }
        return result;
    }

    public static Locale getTargetLable() {
        targetLable = targetLable == null ? Locale.getDefault() : targetLable;
        return targetLable;
    }

    public static void clear() {
        SPProvider.saveLanguageSelect("");
        targetLable = Locale.getDefault();
    }
}
