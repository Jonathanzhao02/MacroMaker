package main.java;

import main.java.impls.enums.*;

import java.util.EnumMap;
import java.util.Objects;
import java.util.prefs.*;

public class UserPreferences {
    private final Preferences preferences;
    private final EnumMap<KeySetting, Integer> keySettings = new EnumMap<>(KeySetting.class);
    private final EnumMap<OptionSetting, Boolean> optionSettings = new EnumMap<>(OptionSetting.class);
    private final EnumMap<DoubleSetting, Double> doubleSettings = new EnumMap<>(DoubleSetting.class);

    public UserPreferences() {
        preferences = Preferences.userRoot().node(UserPreferences.class.getName());
        setToDefaultSettings();
    }

    public void addListener(PreferenceChangeListener listener) {
        preferences.addPreferenceChangeListener(listener);
    }

    public void removeListener(PreferenceChangeListener listener) {
        preferences.removePreferenceChangeListener(listener);
    }

    private void setToDefaultSettings() {
        for (KeySetting keySetting : KeySetting.values()) {
            keySettings.put(keySetting, preferences.getInt(keySetting.toString(), keySetting.getVal()));
        }

        for (OptionSetting optionSetting : OptionSetting.values()) {
            optionSettings.put(optionSetting, preferences.getBoolean(optionSetting.toString(), optionSetting.getVal()));
        }

        for (DoubleSetting doubleSetting : DoubleSetting.values()) {
            doubleSettings.put(doubleSetting, preferences.getDouble(doubleSetting.toString(), doubleSetting.getVal()));
        }
    }

    public void resetToDefaultSettings() {
        for (KeySetting keySetting : KeySetting.values()) {
            setKeySetting(keySetting, keySetting.getVal());
        }

        for (OptionSetting optionSetting : OptionSetting.values()) {
            setOptionSetting(optionSetting, optionSetting.getVal());
        }

        for (DoubleSetting doubleSetting : DoubleSetting.values()) {
            setDoubleSetting(doubleSetting, doubleSetting.getVal());
        }
    }

    public int setKeySetting(KeySetting keySetting, int value) {
        preferences.putInt(keySetting.toString(), value);
        return Objects.requireNonNullElse(keySettings.put(keySetting, value), 0);
    }

    public int getKeySetting(KeySetting keySetting) {
        return keySettings.get(keySetting);
    }

    public boolean setOptionSetting(OptionSetting optionSetting, boolean value) {
        preferences.putBoolean(optionSetting.toString(), value);
        return Objects.requireNonNullElse(optionSettings.put(optionSetting, value), false);
    }

    public boolean getOptionSetting(OptionSetting optionSetting) {
        return optionSettings.get(optionSetting);
    }

    public double setDoubleSetting(DoubleSetting doubleSetting, double value) {
        preferences.putDouble(doubleSetting.toString(), value);
        return Objects.requireNonNullElse(doubleSettings.put(doubleSetting, value), 0.0);
    }

    public double getDoubleSetting(DoubleSetting doubleSetting) {
        return doubleSettings.get(doubleSetting);
    }
}
