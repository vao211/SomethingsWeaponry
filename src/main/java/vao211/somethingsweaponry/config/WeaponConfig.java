package vao211.somethingsweaponry.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class WeaponConfig extends MidnightConfig {
    public static final String MOD_ID = "somethingsweaponry";
    @Entry(category = "weapons")
    public static float oblivionSwordDamage = 8.0f;
    @Entry(category = "weapons")
    public static float oblivionSwordAttackSpeed = 1.3f;
    @Entry(category = "weapons")
    public static float oblivionDaggerDamage = 6.0f;
    @Entry(category = "weapons")
    public static float oblivionDaggerAttackSpeed = 2.0f;
    @Entry(category = "weapons")
    public static float oblivionAxeDamage = 10.0f;
    @Entry(category = "weapons")
    public static float oblivionAxeAttackSpeed = 1.0f;

}