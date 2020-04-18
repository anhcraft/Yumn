package dev.anhcraft.yumn.utils;

public class RelativePos {
    public static final int[] ALL = new int[]{
            1, // 0001; 0 1
            3, // 0011; 0 -1
            4, // 0100; 1 0
            5, // 0101; 1 1
            7, // 0111; 1 -1
            12, // 1100; -1 0
            13, // 1101; -1 1
            15, // 1111; -1 -1
    };

    private static final int NX_OP = 8;
    private static final int X_OP = 4;
    private static final int NZ_OP = 2;
    private static final int Z_OP = 1;
    private static final int[] SIGN = new int[]{1, -1};

    public static int getX(int v){
        return SIGN[(v & NX_OP) >> 3] * ((v & X_OP) >> 2);
    }

    public static int getZ(int v){
        return SIGN[(v & NZ_OP) >> 1] * (v & Z_OP);
    }
}
