package org.covid19.live.common.data;

import org.covid19.live.R;
import org.covid19.live.common.AppConstant;

public enum MythBusterInfo {

    FACT_1(R.string.description_1, R.drawable.dis_type_1, AppConstant.MYTH_BUSTER_INFO_CARD),

    FACT_2(R.string.description_2, R.drawable.dis_type_2, AppConstant.MYTH_BUSTER_INFO_CARD),

    FACT_3(R.string.description_3, R.drawable.dis_type_3, AppConstant.MYTH_BUSTER_INFO_CARD),

    FACT_4(R.string.description_4, R.drawable.dis_type_4, AppConstant.MYTH_BUSTER_INFO_CARD),

    FACT_5(R.string.description_5, R.drawable.dis_type_5, AppConstant.MYTH_BUSTER_INFO_CARD),

    FACT_6(R.string.description_6, R.drawable.dis_type_6, AppConstant.MYTH_BUSTER_INFO_CARD),

    FACT_7(R.string.description_7, R.drawable.dis_type_7, AppConstant.MYTH_BUSTER_INFO_CARD),

    FACT_8(R.string.description_8, R.drawable.dis_type_8, AppConstant.MYTH_BUSTER_INFO_CARD),

    FACT_9(R.string.description_9, R.drawable.dis_type_9, AppConstant.MYTH_BUSTER_INFO_CARD),

    FACT_10(R.string.description_10, R.drawable.dis_type_10, AppConstant.MYTH_BUSTER_INFO_CARD),

    FACT_11(R.string.description_11, R.drawable.dis_type_11, AppConstant.MYTH_BUSTER_INFO_CARD),

    FACT_12(R.string.description_12, R.drawable.dis_type_12, AppConstant.MYTH_BUSTER_INFO_CARD),

    FACT_13(R.string.description_13, R.drawable.dis_type_13, AppConstant.MYTH_BUSTER_INFO_CARD),

    FACT_14(R.string.description_14, R.drawable.dis_type_14, AppConstant.MYTH_BUSTER_INFO_CARD),

    FACT_SOURCE(R.string.description_source, 0, AppConstant.MYTH_BUSTER_SOURCE_CARD);


    private final int mythFactIcon;

    private final int mythFactDescription;

    private final int mythFactViewType;

    MythBusterInfo(int mythFactDescription, int mythFactIcon, int mythFactViewType) {
        this.mythFactDescription = mythFactDescription;
        this.mythFactIcon = mythFactIcon;
        this.mythFactViewType = mythFactViewType;
    }

    public static MythBusterInfo[] getMythBusterFacts() {
        return new MythBusterInfo[]{
                FACT_1,
                FACT_2,
                FACT_3,
                FACT_4,
                FACT_5,
                FACT_6,
                FACT_7,
                FACT_8,
                FACT_9,
                FACT_10,
                FACT_11,
                FACT_12,
                FACT_13,
                FACT_14,
                FACT_SOURCE
        };
    }

    public int getMythFactIcon() {
        return mythFactIcon;
    }

    public int getMythFactDescription() {
        return mythFactDescription;
    }

    public int getMythFactViewType() {
        return mythFactViewType;
    }
}
