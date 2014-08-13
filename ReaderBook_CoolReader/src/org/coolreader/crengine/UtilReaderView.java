package org.coolreader.crengine;

/**
 * User: XuanTrung
 * Date: 6/25/2014
 * Time: 11:04 AM
 */
public class UtilReaderView implements Settings {
    final static private String[] styleCodes = {
            "def",
            "title",
            "subtitle",
            "pre",
            "link",
            "cite",
            "epigraph",
            "poem",
            "text-author",
            "footnote",
            "footnote-link",
            "footnote-title",
            "annotation",
    };

    static public void saveColor(Properties mProperties, boolean night) {
        if (night) {
            mProperties.setProperty(PROP_PAGE_BACKGROUND_IMAGE_NIGHT, mProperties.getProperty(PROP_PAGE_BACKGROUND_IMAGE, "(NONE)"));
            mProperties.setColor(PROP_BACKGROUND_COLOR_NIGHT, mProperties.getColor(PROP_BACKGROUND_COLOR, 0x000000));
            mProperties.setColor(PROP_FONT_COLOR_NIGHT, mProperties.getColor(PROP_FONT_COLOR, 0xFFFFFF));
            mProperties.setColor(PROP_STATUS_FONT_COLOR_NIGHT, mProperties.getColor(PROP_STATUS_FONT_COLOR, 0xFFFFFF));
            mProperties.setInt(PROP_APP_SCREEN_BACKLIGHT_NIGHT, mProperties.getInt(PROP_APP_SCREEN_BACKLIGHT, -1));
            mProperties.setProperty(PROP_FONT_GAMMA_NIGHT, mProperties.getProperty(PROP_FONT_GAMMA, "1.0"));
            mProperties.setProperty(PROP_APP_THEME_NIGHT, mProperties.getProperty(PROP_APP_THEME, "BLACK"));
            mProperties.setInt(PROP_APP_HIGHLIGHT_BOOKMARKS_NIGHT, mProperties.getInt(PROP_APP_HIGHLIGHT_BOOKMARKS, 1));
            mProperties.setColor(PROP_HIGHLIGHT_SELECTION_COLOR_NIGHT, mProperties.getColor(PROP_HIGHLIGHT_SELECTION_COLOR, 0xCCCCCC));
            mProperties.setColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_COMMENT_NIGHT, mProperties.getColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_COMMENT, 0xFFFF40));
            mProperties.setColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_CORRECTION_NIGHT, mProperties.getColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_CORRECTION, 0xFF8000));
        } else {
            mProperties.setProperty(PROP_PAGE_BACKGROUND_IMAGE_DAY, mProperties.getProperty(PROP_PAGE_BACKGROUND_IMAGE, "(NONE)"));
            mProperties.setColor(PROP_BACKGROUND_COLOR_DAY, mProperties.getColor(PROP_BACKGROUND_COLOR, 0xFFFFFF));
            mProperties.setColor(PROP_FONT_COLOR_DAY, mProperties.getColor(PROP_FONT_COLOR, 0x000000));
            mProperties.setColor(PROP_STATUS_FONT_COLOR_DAY, mProperties.getColor(PROP_STATUS_FONT_COLOR, 0x000000));
            mProperties.setInt(PROP_APP_SCREEN_BACKLIGHT_DAY, mProperties.getInt(PROP_APP_SCREEN_BACKLIGHT, -1));
            mProperties.setProperty(PROP_FONT_GAMMA_DAY, mProperties.getProperty(PROP_FONT_GAMMA, "1.0"));
            mProperties.setProperty(PROP_APP_THEME_DAY, mProperties.getProperty(PROP_APP_THEME, "WHITE"));
            mProperties.setInt(PROP_APP_HIGHLIGHT_BOOKMARKS_DAY, mProperties.getInt(PROP_APP_HIGHLIGHT_BOOKMARKS, 1));
            mProperties.setColor(PROP_HIGHLIGHT_SELECTION_COLOR_DAY, mProperties.getColor(PROP_HIGHLIGHT_SELECTION_COLOR, 0xCCCCCC));
            mProperties.setColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_COMMENT_DAY, mProperties.getColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_COMMENT, 0xFFFF40));
            mProperties.setColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_CORRECTION_DAY, mProperties.getColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_CORRECTION, 0xFF8000));
        }
        for (String code : styleCodes) {
            String styleName = "styles." + code + ".color";
            String v = mProperties.getProperty(styleName);
            if (v != null) {
                if (night)
                    mProperties.setProperty(styleName + ".night", v);
                else
                    mProperties.setProperty(styleName + ".day", v);
            }
        }
    }

    static public void restoreColor(Properties mProperties, boolean night) {
        if (night) {
            mProperties.setProperty(PROP_PAGE_BACKGROUND_IMAGE, mProperties.getProperty(PROP_PAGE_BACKGROUND_IMAGE_NIGHT, "(NONE)"));
            mProperties.setColor(PROP_BACKGROUND_COLOR, mProperties.getColor(PROP_BACKGROUND_COLOR_NIGHT, 0x000000));
            mProperties.setColor(PROP_FONT_COLOR, mProperties.getColor(PROP_FONT_COLOR_NIGHT, 0xFFFFFF));
            mProperties.setColor(PROP_STATUS_FONT_COLOR, mProperties.getColor(PROP_STATUS_FONT_COLOR_NIGHT, 0xFFFFFF));
            mProperties.setInt(PROP_APP_SCREEN_BACKLIGHT, mProperties.getInt(PROP_APP_SCREEN_BACKLIGHT_NIGHT, 70));
            mProperties.setProperty(PROP_FONT_GAMMA, mProperties.getProperty(PROP_FONT_GAMMA_NIGHT, "1.0"));
            mProperties.setProperty(PROP_APP_THEME, mProperties.getProperty(PROP_APP_THEME_NIGHT, "BLACK"));
            mProperties.setInt(PROP_APP_HIGHLIGHT_BOOKMARKS, mProperties.getInt(PROP_APP_HIGHLIGHT_BOOKMARKS_NIGHT, 1));
            mProperties.setColor(PROP_HIGHLIGHT_SELECTION_COLOR, mProperties.getColor(PROP_HIGHLIGHT_SELECTION_COLOR_NIGHT, 0xCCCCCC));
            mProperties.setColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_COMMENT, mProperties.getColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_COMMENT_NIGHT, 0xFFFF40));
            mProperties.setColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_CORRECTION, mProperties.getColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_CORRECTION_NIGHT, 0xFF8000));
        } else {
            mProperties.setProperty(PROP_PAGE_BACKGROUND_IMAGE, mProperties.getProperty(PROP_PAGE_BACKGROUND_IMAGE_DAY, "(NONE)"));
            mProperties.setColor(PROP_BACKGROUND_COLOR, mProperties.getColor(PROP_BACKGROUND_COLOR_DAY, 0xFFFFFF));
            mProperties.setColor(PROP_FONT_COLOR, mProperties.getColor(PROP_FONT_COLOR_DAY, 0x000000));
            mProperties.setColor(PROP_STATUS_FONT_COLOR, mProperties.getColor(PROP_STATUS_FONT_COLOR_DAY, 0x000000));
            mProperties.setInt(PROP_APP_SCREEN_BACKLIGHT, mProperties.getInt(PROP_APP_SCREEN_BACKLIGHT_DAY, 80));
            mProperties.setProperty(PROP_FONT_GAMMA, mProperties.getProperty(PROP_FONT_GAMMA_DAY, "1.0"));
            mProperties.setProperty(PROP_APP_THEME, mProperties.getProperty(PROP_APP_THEME_DAY, "WHITE"));
            mProperties.setInt(PROP_APP_HIGHLIGHT_BOOKMARKS, mProperties.getInt(PROP_APP_HIGHLIGHT_BOOKMARKS_DAY, 1));
            mProperties.setColor(PROP_HIGHLIGHT_SELECTION_COLOR, mProperties.getColor(PROP_HIGHLIGHT_SELECTION_COLOR_DAY, 0xCCCCCC));
            mProperties.setColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_COMMENT, mProperties.getColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_COMMENT_DAY, 0xFFFF40));
            mProperties.setColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_CORRECTION, mProperties.getColor(PROP_HIGHLIGHT_BOOKMARK_COLOR_CORRECTION_DAY, 0xFF8000));
        }
        for (String code : styleCodes) {
            String styleName = "styles." + code + ".color";
            String pname = night ? styleName + ".night" : styleName + ".day";
            String v = mProperties.getProperty(pname);
            if (v != null)
                mProperties.setProperty(styleName, mProperties.getProperty(pname));
        }
    }

    static public void toggleDayNightMode(Properties mProperties) {
        boolean oldMode = mProperties.getBool(PROP_NIGHT_MODE, false);
        saveColor(mProperties, oldMode);
        boolean newMode = !oldMode;
        restoreColor(mProperties, newMode);
        mProperties.setBool(PROP_NIGHT_MODE, newMode);
    }


}
