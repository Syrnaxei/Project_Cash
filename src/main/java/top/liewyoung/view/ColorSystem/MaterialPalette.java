package top.liewyoung.view.ColorSystem;

import java.awt.Color;

/**
 * 保存 Material Design 3 颜色角色的记录类
 * @author LiewYoung
 * @since 2025/12/13
 */
public record MaterialPalette(
        // --- Primary (主要强调色) ---
        // 用于主要按钮、激活状态
        Color primary,
        // 用于位于 Primary 之上的内容（文字/图标），通常是白色
        Color onPrimary,

        // --- Primary Container (主要容器) ---
        // 用于卡片背景、选中项背景等（颜色较柔和）
        Color primaryContainer,
        // 用于位于 Container 之上的内容（文字），通常是深色
        Color onPrimaryContainer,

        // --- Surface (表面/背景) ---
        // 整个应用的底色，或者是卡片的底色
        Color surface,
        // 位于 Surface 之上的文字颜色
        Color onSurface,


        Color surfaceVariant,


        Color outline,

        Color error
) {

    // ==========================================
    // 预设主题 1: Moss Green (苔藓绿 )
    // ==========================================
    public static final MaterialPalette MOSS = new MaterialPalette(
            new Color(56, 106, 32),      // primary (深绿)
            new Color(255, 255, 255),    // onPrimary (白)
            new Color(216, 232, 203),    // primaryContainer (浅绿)
            new Color(16, 32, 5),        // onPrimaryContainer (极深绿)
            new Color(253, 253, 245),    // surface (暖白)
            new Color(26, 28, 24),       // onSurface (黑灰)
            new Color(226, 227, 219),    // surfaceVariant (灰绿)
            new Color(116, 121, 109),    // outline (中性灰)
            new Color(186, 26, 26)       // error (深红)
    );

    // ==========================================
    // 预设主题 2: Deep Purple (Google 默认风格)
    // ==========================================
    public static final MaterialPalette PURPLE = new MaterialPalette(
            new Color(103, 80, 164),     // primary (紫色)
            new Color(255, 255, 255),    // onPrimary
            new Color(234, 221, 255),    // primaryContainer (浅紫)
            new Color(33, 0, 93),        // onPrimaryContainer (深紫)
            new Color(255, 251, 254),    // surface (几乎纯白)
            new Color(28, 27, 31),       // onSurface
            new Color(231, 224, 236),    // surfaceVariant
            new Color(121, 116, 126),    // outline
            new Color(179, 38, 30)       // error
    );

}