package vao211.somethingsweaponry.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class WeaponConfig extends MidnightConfig {
    public static final String MOD_ID = "somethingsweaponry";
    @Entry(category = "weapons")
    public static float oblivionSwordDamage = 9.0f;
    @Entry(category = "weapons")
    public static float oblivionSwordAttackSpeed = 1.3f;
    @Entry
    public static float oblivionDaggerDamage = 6.0f;
    @Entry
    public static float oblivionDaggerAttackSpeed = 2.0f;
}