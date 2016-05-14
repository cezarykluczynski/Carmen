package com.cezarykluczynski.carmen.model.cassandra.repositories;

import com.cezarykluczynski.carmen.cron.languages.annotations.Keyspace;
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesDiffStatistics;
import com.cezarykluczynski.carmen.cron.languages.annotations.LanguagesStatistics;
import com.cezarykluczynski.carmen.model.CarmenNoSQLEntity;
import com.cezarykluczynski.carmen.model.cassandra.GitDescription;
import java.util.UUID;
import javax.annotation.Generated;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

@Generated("com.cezarykluczynski.carmen.cron.languages.builder.CassandraJavaPoetEntityBuilder")
@LanguagesDiffStatistics
@LanguagesStatistics
@Keyspace("repositories")
@Table("commits")
public class Commit extends CarmenNoSQLEntity implements GitDescription {
    @Column
    public String hash;

    @PrimaryKey
    public UUID id;

    @Column
    public Integer language_1;

    @Column
    public Integer language_1_added;

    @Column
    public Integer language_1_removed;

    @Column
    public Integer language_2;

    @Column
    public Integer language_2_added;

    @Column
    public Integer language_2_removed;

    @Column
    public Integer language_3;

    @Column
    public Integer language_3_added;

    @Column
    public Integer language_3_removed;

    @Column
    public Integer language_4;

    @Column
    public Integer language_4_added;

    @Column
    public Integer language_4_removed;

    @Column
    public Integer language_5;

    @Column
    public Integer language_5_added;

    @Column
    public Integer language_5_removed;

    @Column
    public Integer language_6;

    @Column
    public Integer language_6_added;

    @Column
    public Integer language_6_removed;

    @Column
    public Integer language_7;

    @Column
    public Integer language_7_added;

    @Column
    public Integer language_7_removed;

    @Column
    public Integer language_8;

    @Column
    public Integer language_8_added;

    @Column
    public Integer language_8_removed;

    @Column
    public Integer language_9;

    @Column
    public Integer language_9_added;

    @Column
    public Integer language_9_removed;

    @Column
    public Integer language_10;

    @Column
    public Integer language_10_added;

    @Column
    public Integer language_10_removed;

    @Column
    public Integer language_11;

    @Column
    public Integer language_11_added;

    @Column
    public Integer language_11_removed;

    @Column
    public Integer language_12;

    @Column
    public Integer language_12_added;

    @Column
    public Integer language_12_removed;

    @Column
    public Integer language_13;

    @Column
    public Integer language_13_added;

    @Column
    public Integer language_13_removed;

    @Column
    public Integer language_14;

    @Column
    public Integer language_14_added;

    @Column
    public Integer language_14_removed;

    @Column
    public Integer language_15;

    @Column
    public Integer language_15_added;

    @Column
    public Integer language_15_removed;

    @Column
    public Integer language_16;

    @Column
    public Integer language_16_added;

    @Column
    public Integer language_16_removed;

    @Column
    public Integer language_17;

    @Column
    public Integer language_17_added;

    @Column
    public Integer language_17_removed;

    @Column
    public Integer language_18;

    @Column
    public Integer language_18_added;

    @Column
    public Integer language_18_removed;

    @Column
    public Integer language_19;

    @Column
    public Integer language_19_added;

    @Column
    public Integer language_19_removed;

    @Column
    public Integer language_20;

    @Column
    public Integer language_20_added;

    @Column
    public Integer language_20_removed;

    @Column
    public Integer language_21;

    @Column
    public Integer language_21_added;

    @Column
    public Integer language_21_removed;

    @Column
    public Integer language_22;

    @Column
    public Integer language_22_added;

    @Column
    public Integer language_22_removed;

    @Column
    public Integer language_23;

    @Column
    public Integer language_23_added;

    @Column
    public Integer language_23_removed;

    @Column
    public Integer language_24;

    @Column
    public Integer language_24_added;

    @Column
    public Integer language_24_removed;

    @Column
    public Integer language_25;

    @Column
    public Integer language_25_added;

    @Column
    public Integer language_25_removed;

    @Column
    public Integer language_26;

    @Column
    public Integer language_26_added;

    @Column
    public Integer language_26_removed;

    @Column
    public Integer language_27;

    @Column
    public Integer language_27_added;

    @Column
    public Integer language_27_removed;

    @Column
    public Integer language_28;

    @Column
    public Integer language_28_added;

    @Column
    public Integer language_28_removed;

    @Column
    public Integer language_29;

    @Column
    public Integer language_29_added;

    @Column
    public Integer language_29_removed;

    @Column
    public Integer language_30;

    @Column
    public Integer language_30_added;

    @Column
    public Integer language_30_removed;

    @Column
    public Integer language_31;

    @Column
    public Integer language_31_added;

    @Column
    public Integer language_31_removed;

    @Column
    public Integer language_32;

    @Column
    public Integer language_32_added;

    @Column
    public Integer language_32_removed;

    @Column
    public Integer language_33;

    @Column
    public Integer language_33_added;

    @Column
    public Integer language_33_removed;

    @Column
    public Integer language_34;

    @Column
    public Integer language_34_added;

    @Column
    public Integer language_34_removed;

    @Column
    public Integer language_35;

    @Column
    public Integer language_35_added;

    @Column
    public Integer language_35_removed;

    @Column
    public Integer language_36;

    @Column
    public Integer language_36_added;

    @Column
    public Integer language_36_removed;

    @Column
    public Integer language_37;

    @Column
    public Integer language_37_added;

    @Column
    public Integer language_37_removed;

    @Column
    public Integer language_38;

    @Column
    public Integer language_38_added;

    @Column
    public Integer language_38_removed;

    @Column
    public Integer language_39;

    @Column
    public Integer language_39_added;

    @Column
    public Integer language_39_removed;

    @Column
    public Integer language_40;

    @Column
    public Integer language_40_added;

    @Column
    public Integer language_40_removed;

    @Column
    public Integer language_41;

    @Column
    public Integer language_41_added;

    @Column
    public Integer language_41_removed;

    @Column
    public Integer language_42;

    @Column
    public Integer language_42_added;

    @Column
    public Integer language_42_removed;

    @Column
    public Integer language_43;

    @Column
    public Integer language_43_added;

    @Column
    public Integer language_43_removed;

    @Column
    public Integer language_44;

    @Column
    public Integer language_44_added;

    @Column
    public Integer language_44_removed;

    @Column
    public Integer language_45;

    @Column
    public Integer language_45_added;

    @Column
    public Integer language_45_removed;

    @Column
    public Integer language_46;

    @Column
    public Integer language_46_added;

    @Column
    public Integer language_46_removed;

    @Column
    public Integer language_47;

    @Column
    public Integer language_47_added;

    @Column
    public Integer language_47_removed;

    @Column
    public Integer language_48;

    @Column
    public Integer language_48_added;

    @Column
    public Integer language_48_removed;

    @Column
    public Integer language_49;

    @Column
    public Integer language_49_added;

    @Column
    public Integer language_49_removed;

    @Column
    public Integer language_50;

    @Column
    public Integer language_50_added;

    @Column
    public Integer language_50_removed;

    @Column
    public Integer language_51;

    @Column
    public Integer language_51_added;

    @Column
    public Integer language_51_removed;

    @Column
    public Integer language_52;

    @Column
    public Integer language_52_added;

    @Column
    public Integer language_52_removed;

    @Column
    public Integer language_53;

    @Column
    public Integer language_53_added;

    @Column
    public Integer language_53_removed;

    @Column
    public Integer language_54;

    @Column
    public Integer language_54_added;

    @Column
    public Integer language_54_removed;

    @Column
    public Integer language_55;

    @Column
    public Integer language_55_added;

    @Column
    public Integer language_55_removed;

    @Column
    public Integer language_56;

    @Column
    public Integer language_56_added;

    @Column
    public Integer language_56_removed;

    @Column
    public Integer language_57;

    @Column
    public Integer language_57_added;

    @Column
    public Integer language_57_removed;

    @Column
    public Integer language_58;

    @Column
    public Integer language_58_added;

    @Column
    public Integer language_58_removed;

    @Column
    public Integer language_59;

    @Column
    public Integer language_59_added;

    @Column
    public Integer language_59_removed;

    @Column
    public Integer language_60;

    @Column
    public Integer language_60_added;

    @Column
    public Integer language_60_removed;

    @Column
    public Integer language_61;

    @Column
    public Integer language_61_added;

    @Column
    public Integer language_61_removed;

    @Column
    public Integer language_62;

    @Column
    public Integer language_62_added;

    @Column
    public Integer language_62_removed;

    @Column
    public Integer language_63;

    @Column
    public Integer language_63_added;

    @Column
    public Integer language_63_removed;

    @Column
    public Integer language_64;

    @Column
    public Integer language_64_added;

    @Column
    public Integer language_64_removed;

    @Column
    public Integer language_65;

    @Column
    public Integer language_65_added;

    @Column
    public Integer language_65_removed;

    @Column
    public Integer language_66;

    @Column
    public Integer language_66_added;

    @Column
    public Integer language_66_removed;

    @Column
    public Integer language_67;

    @Column
    public Integer language_67_added;

    @Column
    public Integer language_67_removed;

    @Column
    public Integer language_68;

    @Column
    public Integer language_68_added;

    @Column
    public Integer language_68_removed;

    @Column
    public Integer language_69;

    @Column
    public Integer language_69_added;

    @Column
    public Integer language_69_removed;

    @Column
    public Integer language_70;

    @Column
    public Integer language_70_added;

    @Column
    public Integer language_70_removed;

    @Column
    public Integer language_71;

    @Column
    public Integer language_71_added;

    @Column
    public Integer language_71_removed;

    @Column
    public Integer language_72;

    @Column
    public Integer language_72_added;

    @Column
    public Integer language_72_removed;

    @Column
    public Integer language_73;

    @Column
    public Integer language_73_added;

    @Column
    public Integer language_73_removed;

    @Column
    public Integer language_74;

    @Column
    public Integer language_74_added;

    @Column
    public Integer language_74_removed;

    @Column
    public Integer language_75;

    @Column
    public Integer language_75_added;

    @Column
    public Integer language_75_removed;

    @Column
    public Integer language_76;

    @Column
    public Integer language_76_added;

    @Column
    public Integer language_76_removed;

    @Column
    public Integer language_77;

    @Column
    public Integer language_77_added;

    @Column
    public Integer language_77_removed;

    @Column
    public Integer language_78;

    @Column
    public Integer language_78_added;

    @Column
    public Integer language_78_removed;

    @Column
    public Integer language_79;

    @Column
    public Integer language_79_added;

    @Column
    public Integer language_79_removed;

    @Column
    public Integer language_80;

    @Column
    public Integer language_80_added;

    @Column
    public Integer language_80_removed;

    @Column
    public Integer language_81;

    @Column
    public Integer language_81_added;

    @Column
    public Integer language_81_removed;

    @Column
    public Integer language_82;

    @Column
    public Integer language_82_added;

    @Column
    public Integer language_82_removed;

    @Column
    public Integer language_83;

    @Column
    public Integer language_83_added;

    @Column
    public Integer language_83_removed;

    @Column
    public Integer language_84;

    @Column
    public Integer language_84_added;

    @Column
    public Integer language_84_removed;

    @Column
    public Integer language_85;

    @Column
    public Integer language_85_added;

    @Column
    public Integer language_85_removed;

    @Column
    public Integer language_86;

    @Column
    public Integer language_86_added;

    @Column
    public Integer language_86_removed;

    @Column
    public Integer language_87;

    @Column
    public Integer language_87_added;

    @Column
    public Integer language_87_removed;

    @Column
    public Integer language_88;

    @Column
    public Integer language_88_added;

    @Column
    public Integer language_88_removed;

    @Column
    public Integer language_89;

    @Column
    public Integer language_89_added;

    @Column
    public Integer language_89_removed;

    @Column
    public Integer language_90;

    @Column
    public Integer language_90_added;

    @Column
    public Integer language_90_removed;

    @Column
    public Integer language_91;

    @Column
    public Integer language_91_added;

    @Column
    public Integer language_91_removed;

    @Column
    public Integer language_92;

    @Column
    public Integer language_92_added;

    @Column
    public Integer language_92_removed;

    @Column
    public Integer language_93;

    @Column
    public Integer language_93_added;

    @Column
    public Integer language_93_removed;

    @Column
    public Integer language_94;

    @Column
    public Integer language_94_added;

    @Column
    public Integer language_94_removed;

    @Column
    public Integer language_95;

    @Column
    public Integer language_95_added;

    @Column
    public Integer language_95_removed;

    @Column
    public Integer language_96;

    @Column
    public Integer language_96_added;

    @Column
    public Integer language_96_removed;

    @Column
    public Integer language_97;

    @Column
    public Integer language_97_added;

    @Column
    public Integer language_97_removed;

    @Column
    public Integer language_98;

    @Column
    public Integer language_98_added;

    @Column
    public Integer language_98_removed;

    @Column
    public Integer language_99;

    @Column
    public Integer language_99_added;

    @Column
    public Integer language_99_removed;

    @Column
    public Integer language_100;

    @Column
    public Integer language_100_added;

    @Column
    public Integer language_100_removed;

    @Column
    public Integer language_101;

    @Column
    public Integer language_101_added;

    @Column
    public Integer language_101_removed;

    @Column
    public Integer language_102;

    @Column
    public Integer language_102_added;

    @Column
    public Integer language_102_removed;

    @Column
    public Integer language_103;

    @Column
    public Integer language_103_added;

    @Column
    public Integer language_103_removed;

    @Column
    public Integer language_104;

    @Column
    public Integer language_104_added;

    @Column
    public Integer language_104_removed;

    @Column
    public Integer language_105;

    @Column
    public Integer language_105_added;

    @Column
    public Integer language_105_removed;

    @Column
    public Integer language_106;

    @Column
    public Integer language_106_added;

    @Column
    public Integer language_106_removed;

    @Column
    public Integer language_107;

    @Column
    public Integer language_107_added;

    @Column
    public Integer language_107_removed;

    @Column
    public Integer language_108;

    @Column
    public Integer language_108_added;

    @Column
    public Integer language_108_removed;

    @Column
    public Integer language_109;

    @Column
    public Integer language_109_added;

    @Column
    public Integer language_109_removed;

    @Column
    public Integer language_110;

    @Column
    public Integer language_110_added;

    @Column
    public Integer language_110_removed;

    @Column
    public Integer language_111;

    @Column
    public Integer language_111_added;

    @Column
    public Integer language_111_removed;

    @Column
    public Integer language_112;

    @Column
    public Integer language_112_added;

    @Column
    public Integer language_112_removed;

    @Column
    public Integer language_113;

    @Column
    public Integer language_113_added;

    @Column
    public Integer language_113_removed;

    @Column
    public Integer language_114;

    @Column
    public Integer language_114_added;

    @Column
    public Integer language_114_removed;

    @Column
    public Integer language_115;

    @Column
    public Integer language_115_added;

    @Column
    public Integer language_115_removed;

    @Column
    public Integer language_116;

    @Column
    public Integer language_116_added;

    @Column
    public Integer language_116_removed;

    @Column
    public Integer language_117;

    @Column
    public Integer language_117_added;

    @Column
    public Integer language_117_removed;

    @Column
    public Integer language_118;

    @Column
    public Integer language_118_added;

    @Column
    public Integer language_118_removed;

    @Column
    public Integer language_119;

    @Column
    public Integer language_119_added;

    @Column
    public Integer language_119_removed;

    @Column
    public Integer language_120;

    @Column
    public Integer language_120_added;

    @Column
    public Integer language_120_removed;

    @Column
    public Integer language_121;

    @Column
    public Integer language_121_added;

    @Column
    public Integer language_121_removed;

    @Column
    public Integer language_122;

    @Column
    public Integer language_122_added;

    @Column
    public Integer language_122_removed;

    @Column
    public Integer language_123;

    @Column
    public Integer language_123_added;

    @Column
    public Integer language_123_removed;

    @Column
    public Integer language_124;

    @Column
    public Integer language_124_added;

    @Column
    public Integer language_124_removed;

    @Column
    public Integer language_125;

    @Column
    public Integer language_125_added;

    @Column
    public Integer language_125_removed;

    @Column
    public Integer language_126;

    @Column
    public Integer language_126_added;

    @Column
    public Integer language_126_removed;

    @Column
    public Integer language_127;

    @Column
    public Integer language_127_added;

    @Column
    public Integer language_127_removed;

    @Column
    public Integer language_128;

    @Column
    public Integer language_128_added;

    @Column
    public Integer language_128_removed;

    @Column
    public Integer language_129;

    @Column
    public Integer language_129_added;

    @Column
    public Integer language_129_removed;

    @Column
    public Integer language_130;

    @Column
    public Integer language_130_added;

    @Column
    public Integer language_130_removed;

    @Column
    public Integer language_131;

    @Column
    public Integer language_131_added;

    @Column
    public Integer language_131_removed;

    @Column
    public Integer language_132;

    @Column
    public Integer language_132_added;

    @Column
    public Integer language_132_removed;

    @Column
    public Integer language_133;

    @Column
    public Integer language_133_added;

    @Column
    public Integer language_133_removed;

    @Column
    public Integer language_134;

    @Column
    public Integer language_134_added;

    @Column
    public Integer language_134_removed;

    @Column
    public Integer language_135;

    @Column
    public Integer language_135_added;

    @Column
    public Integer language_135_removed;

    @Column
    public Integer language_136;

    @Column
    public Integer language_136_added;

    @Column
    public Integer language_136_removed;

    @Column
    public Integer language_137;

    @Column
    public Integer language_137_added;

    @Column
    public Integer language_137_removed;

    @Column
    public Integer language_138;

    @Column
    public Integer language_138_added;

    @Column
    public Integer language_138_removed;

    @Column
    public Integer language_139;

    @Column
    public Integer language_139_added;

    @Column
    public Integer language_139_removed;

    @Column
    public Integer language_140;

    @Column
    public Integer language_140_added;

    @Column
    public Integer language_140_removed;

    @Column
    public Integer language_141;

    @Column
    public Integer language_141_added;

    @Column
    public Integer language_141_removed;

    @Column
    public Integer language_142;

    @Column
    public Integer language_142_added;

    @Column
    public Integer language_142_removed;

    @Column
    public Integer language_143;

    @Column
    public Integer language_143_added;

    @Column
    public Integer language_143_removed;

    @Column
    public Integer language_144;

    @Column
    public Integer language_144_added;

    @Column
    public Integer language_144_removed;

    @Column
    public Integer language_145;

    @Column
    public Integer language_145_added;

    @Column
    public Integer language_145_removed;

    @Column
    public Integer language_146;

    @Column
    public Integer language_146_added;

    @Column
    public Integer language_146_removed;

    @Column
    public Integer language_147;

    @Column
    public Integer language_147_added;

    @Column
    public Integer language_147_removed;

    @Column
    public Integer language_148;

    @Column
    public Integer language_148_added;

    @Column
    public Integer language_148_removed;

    @Column
    public Integer language_149;

    @Column
    public Integer language_149_added;

    @Column
    public Integer language_149_removed;

    @Column
    public Integer language_150;

    @Column
    public Integer language_150_added;

    @Column
    public Integer language_150_removed;

    @Column
    public Integer language_151;

    @Column
    public Integer language_151_added;

    @Column
    public Integer language_151_removed;

    @Column
    public Integer language_152;

    @Column
    public Integer language_152_added;

    @Column
    public Integer language_152_removed;

    @Column
    public Integer language_153;

    @Column
    public Integer language_153_added;

    @Column
    public Integer language_153_removed;

    @Column
    public Integer language_154;

    @Column
    public Integer language_154_added;

    @Column
    public Integer language_154_removed;

    @Column
    public Integer language_155;

    @Column
    public Integer language_155_added;

    @Column
    public Integer language_155_removed;

    @Column
    public Integer language_156;

    @Column
    public Integer language_156_added;

    @Column
    public Integer language_156_removed;

    @Column
    public Integer language_157;

    @Column
    public Integer language_157_added;

    @Column
    public Integer language_157_removed;

    @Column
    public Integer language_158;

    @Column
    public Integer language_158_added;

    @Column
    public Integer language_158_removed;

    @Column
    public Integer language_159;

    @Column
    public Integer language_159_added;

    @Column
    public Integer language_159_removed;

    @Column
    public Integer language_160;

    @Column
    public Integer language_160_added;

    @Column
    public Integer language_160_removed;

    @Column
    public Integer language_161;

    @Column
    public Integer language_161_added;

    @Column
    public Integer language_161_removed;

    @Column
    public Integer language_162;

    @Column
    public Integer language_162_added;

    @Column
    public Integer language_162_removed;

    @Column
    public Integer language_163;

    @Column
    public Integer language_163_added;

    @Column
    public Integer language_163_removed;

    @Column
    public Integer language_164;

    @Column
    public Integer language_164_added;

    @Column
    public Integer language_164_removed;

    @Column
    public Integer language_165;

    @Column
    public Integer language_165_added;

    @Column
    public Integer language_165_removed;

    @Column
    public Integer language_166;

    @Column
    public Integer language_166_added;

    @Column
    public Integer language_166_removed;

    @Column
    public Integer language_167;

    @Column
    public Integer language_167_added;

    @Column
    public Integer language_167_removed;

    @Column
    public Integer language_168;

    @Column
    public Integer language_168_added;

    @Column
    public Integer language_168_removed;

    @Column
    public Integer language_169;

    @Column
    public Integer language_169_added;

    @Column
    public Integer language_169_removed;

    @Column
    public Integer language_170;

    @Column
    public Integer language_170_added;

    @Column
    public Integer language_170_removed;

    @Column
    public Integer language_171;

    @Column
    public Integer language_171_added;

    @Column
    public Integer language_171_removed;

    @Column
    public Integer language_172;

    @Column
    public Integer language_172_added;

    @Column
    public Integer language_172_removed;

    @Column
    public Integer language_173;

    @Column
    public Integer language_173_added;

    @Column
    public Integer language_173_removed;

    @Column
    public Integer language_174;

    @Column
    public Integer language_174_added;

    @Column
    public Integer language_174_removed;

    @Column
    public Integer language_175;

    @Column
    public Integer language_175_added;

    @Column
    public Integer language_175_removed;

    @Column
    public Integer language_176;

    @Column
    public Integer language_176_added;

    @Column
    public Integer language_176_removed;

    @Column
    public Integer language_177;

    @Column
    public Integer language_177_added;

    @Column
    public Integer language_177_removed;

    @Column
    public Integer language_178;

    @Column
    public Integer language_178_added;

    @Column
    public Integer language_178_removed;

    @Column
    public Integer language_179;

    @Column
    public Integer language_179_added;

    @Column
    public Integer language_179_removed;

    @Column
    public Integer language_180;

    @Column
    public Integer language_180_added;

    @Column
    public Integer language_180_removed;

    @Column
    public Integer language_181;

    @Column
    public Integer language_181_added;

    @Column
    public Integer language_181_removed;

    @Column
    public Integer language_182;

    @Column
    public Integer language_182_added;

    @Column
    public Integer language_182_removed;

    @Column
    public Integer language_183;

    @Column
    public Integer language_183_added;

    @Column
    public Integer language_183_removed;

    @Column
    public Integer language_184;

    @Column
    public Integer language_184_added;

    @Column
    public Integer language_184_removed;

    @Column
    public Integer language_185;

    @Column
    public Integer language_185_added;

    @Column
    public Integer language_185_removed;

    @Column
    public Integer language_186;

    @Column
    public Integer language_186_added;

    @Column
    public Integer language_186_removed;

    @Column
    public Integer language_187;

    @Column
    public Integer language_187_added;

    @Column
    public Integer language_187_removed;

    @Column
    public Integer language_188;

    @Column
    public Integer language_188_added;

    @Column
    public Integer language_188_removed;

    @Column
    public Integer language_189;

    @Column
    public Integer language_189_added;

    @Column
    public Integer language_189_removed;

    @Column
    public Integer language_190;

    @Column
    public Integer language_190_added;

    @Column
    public Integer language_190_removed;

    @Column
    public Integer language_191;

    @Column
    public Integer language_191_added;

    @Column
    public Integer language_191_removed;

    @Column
    public Integer language_192;

    @Column
    public Integer language_192_added;

    @Column
    public Integer language_192_removed;

    @Column
    public Integer language_193;

    @Column
    public Integer language_193_added;

    @Column
    public Integer language_193_removed;

    @Column
    public Integer language_194;

    @Column
    public Integer language_194_added;

    @Column
    public Integer language_194_removed;

    @Column
    public Integer language_195;

    @Column
    public Integer language_195_added;

    @Column
    public Integer language_195_removed;

    @Column
    public Integer language_196;

    @Column
    public Integer language_196_added;

    @Column
    public Integer language_196_removed;

    @Column
    public Integer language_197;

    @Column
    public Integer language_197_added;

    @Column
    public Integer language_197_removed;

    @Column
    public Integer language_198;

    @Column
    public Integer language_198_added;

    @Column
    public Integer language_198_removed;

    @Column
    public Integer language_199;

    @Column
    public Integer language_199_added;

    @Column
    public Integer language_199_removed;

    @Column
    public Integer language_200;

    @Column
    public Integer language_200_added;

    @Column
    public Integer language_200_removed;

    @Column
    public Integer language_201;

    @Column
    public Integer language_201_added;

    @Column
    public Integer language_201_removed;

    @Column
    public Integer language_202;

    @Column
    public Integer language_202_added;

    @Column
    public Integer language_202_removed;

    @Column
    public Integer language_203;

    @Column
    public Integer language_203_added;

    @Column
    public Integer language_203_removed;

    @Column
    public Integer language_204;

    @Column
    public Integer language_204_added;

    @Column
    public Integer language_204_removed;

    @Column
    public Integer language_205;

    @Column
    public Integer language_205_added;

    @Column
    public Integer language_205_removed;

    @Column
    public Integer language_206;

    @Column
    public Integer language_206_added;

    @Column
    public Integer language_206_removed;

    @Column
    public Integer language_207;

    @Column
    public Integer language_207_added;

    @Column
    public Integer language_207_removed;

    @Column
    public Integer language_208;

    @Column
    public Integer language_208_added;

    @Column
    public Integer language_208_removed;

    @Column
    public Integer language_209;

    @Column
    public Integer language_209_added;

    @Column
    public Integer language_209_removed;

    @Column
    public Integer language_210;

    @Column
    public Integer language_210_added;

    @Column
    public Integer language_210_removed;

    @Column
    public Integer language_211;

    @Column
    public Integer language_211_added;

    @Column
    public Integer language_211_removed;

    @Column
    public Integer language_212;

    @Column
    public Integer language_212_added;

    @Column
    public Integer language_212_removed;

    @Column
    public Integer language_213;

    @Column
    public Integer language_213_added;

    @Column
    public Integer language_213_removed;

    @Column
    public Integer language_214;

    @Column
    public Integer language_214_added;

    @Column
    public Integer language_214_removed;

    @Column
    public Integer language_215;

    @Column
    public Integer language_215_added;

    @Column
    public Integer language_215_removed;

    @Column
    public Integer language_216;

    @Column
    public Integer language_216_added;

    @Column
    public Integer language_216_removed;

    @Column
    public Integer language_217;

    @Column
    public Integer language_217_added;

    @Column
    public Integer language_217_removed;

    @Column
    public Integer language_218;

    @Column
    public Integer language_218_added;

    @Column
    public Integer language_218_removed;

    @Column
    public Integer language_219;

    @Column
    public Integer language_219_added;

    @Column
    public Integer language_219_removed;

    @Column
    public Integer language_220;

    @Column
    public Integer language_220_added;

    @Column
    public Integer language_220_removed;

    @Column
    public Integer language_221;

    @Column
    public Integer language_221_added;

    @Column
    public Integer language_221_removed;

    @Column
    public Integer language_222;

    @Column
    public Integer language_222_added;

    @Column
    public Integer language_222_removed;

    @Column
    public Integer language_223;

    @Column
    public Integer language_223_added;

    @Column
    public Integer language_223_removed;

    @Column
    public Integer language_224;

    @Column
    public Integer language_224_added;

    @Column
    public Integer language_224_removed;

    @Column
    public Integer language_225;

    @Column
    public Integer language_225_added;

    @Column
    public Integer language_225_removed;

    @Column
    public Integer language_226;

    @Column
    public Integer language_226_added;

    @Column
    public Integer language_226_removed;

    @Column
    public Integer language_227;

    @Column
    public Integer language_227_added;

    @Column
    public Integer language_227_removed;

    @Column
    public Integer language_228;

    @Column
    public Integer language_228_added;

    @Column
    public Integer language_228_removed;

    @Column
    public Integer language_229;

    @Column
    public Integer language_229_added;

    @Column
    public Integer language_229_removed;

    @Column
    public Integer language_230;

    @Column
    public Integer language_230_added;

    @Column
    public Integer language_230_removed;

    @Column
    public Integer language_231;

    @Column
    public Integer language_231_added;

    @Column
    public Integer language_231_removed;

    @Column
    public Integer language_232;

    @Column
    public Integer language_232_added;

    @Column
    public Integer language_232_removed;

    @Column
    public Integer language_233;

    @Column
    public Integer language_233_added;

    @Column
    public Integer language_233_removed;

    @Column
    public Integer language_234;

    @Column
    public Integer language_234_added;

    @Column
    public Integer language_234_removed;

    @Column
    public Integer language_235;

    @Column
    public Integer language_235_added;

    @Column
    public Integer language_235_removed;

    @Column
    public Integer language_236;

    @Column
    public Integer language_236_added;

    @Column
    public Integer language_236_removed;

    @Column
    public Integer language_237;

    @Column
    public Integer language_237_added;

    @Column
    public Integer language_237_removed;

    @Column
    public Integer language_238;

    @Column
    public Integer language_238_added;

    @Column
    public Integer language_238_removed;

    @Column
    public Integer language_239;

    @Column
    public Integer language_239_added;

    @Column
    public Integer language_239_removed;

    @Column
    public Integer language_240;

    @Column
    public Integer language_240_added;

    @Column
    public Integer language_240_removed;

    @Column
    public Integer language_241;

    @Column
    public Integer language_241_added;

    @Column
    public Integer language_241_removed;

    @Column
    public Integer language_242;

    @Column
    public Integer language_242_added;

    @Column
    public Integer language_242_removed;

    @Column
    public Integer language_243;

    @Column
    public Integer language_243_added;

    @Column
    public Integer language_243_removed;

    @Column
    public Integer language_244;

    @Column
    public Integer language_244_added;

    @Column
    public Integer language_244_removed;

    @Column
    public Integer language_245;

    @Column
    public Integer language_245_added;

    @Column
    public Integer language_245_removed;

    @Column
    public Integer language_246;

    @Column
    public Integer language_246_added;

    @Column
    public Integer language_246_removed;

    @Column
    public Integer language_247;

    @Column
    public Integer language_247_added;

    @Column
    public Integer language_247_removed;

    @Column
    public Integer language_248;

    @Column
    public Integer language_248_added;

    @Column
    public Integer language_248_removed;

    @Column
    public Integer language_249;

    @Column
    public Integer language_249_added;

    @Column
    public Integer language_249_removed;

    @Column
    public Integer language_250;

    @Column
    public Integer language_250_added;

    @Column
    public Integer language_250_removed;

    @Column
    public Integer language_251;

    @Column
    public Integer language_251_added;

    @Column
    public Integer language_251_removed;

    @Column
    public Integer language_252;

    @Column
    public Integer language_252_added;

    @Column
    public Integer language_252_removed;

    @Column
    public Integer language_253;

    @Column
    public Integer language_253_added;

    @Column
    public Integer language_253_removed;

    @Column
    public Integer language_254;

    @Column
    public Integer language_254_added;

    @Column
    public Integer language_254_removed;

    @Column
    public Integer language_255;

    @Column
    public Integer language_255_added;

    @Column
    public Integer language_255_removed;

    @Column
    public Integer language_256;

    @Column
    public Integer language_256_added;

    @Column
    public Integer language_256_removed;

    @Column
    public Integer language_257;

    @Column
    public Integer language_257_added;

    @Column
    public Integer language_257_removed;

    @Column
    public Integer language_258;

    @Column
    public Integer language_258_added;

    @Column
    public Integer language_258_removed;

    @Column
    public Integer language_259;

    @Column
    public Integer language_259_added;

    @Column
    public Integer language_259_removed;

    @Column
    public Integer language_260;

    @Column
    public Integer language_260_added;

    @Column
    public Integer language_260_removed;

    @Column
    public Integer language_261;

    @Column
    public Integer language_261_added;

    @Column
    public Integer language_261_removed;

    @Column
    public Integer language_262;

    @Column
    public Integer language_262_added;

    @Column
    public Integer language_262_removed;

    @Column
    public Integer language_263;

    @Column
    public Integer language_263_added;

    @Column
    public Integer language_263_removed;

    @Column
    public Integer language_264;

    @Column
    public Integer language_264_added;

    @Column
    public Integer language_264_removed;

    @Column
    public Integer language_265;

    @Column
    public Integer language_265_added;

    @Column
    public Integer language_265_removed;

    @Column
    public Integer language_266;

    @Column
    public Integer language_266_added;

    @Column
    public Integer language_266_removed;

    @Column
    public Integer language_267;

    @Column
    public Integer language_267_added;

    @Column
    public Integer language_267_removed;

    @Column
    public Integer language_268;

    @Column
    public Integer language_268_added;

    @Column
    public Integer language_268_removed;

    @Column
    public Integer language_269;

    @Column
    public Integer language_269_added;

    @Column
    public Integer language_269_removed;

    @Column
    public Integer language_270;

    @Column
    public Integer language_270_added;

    @Column
    public Integer language_270_removed;

    @Column
    public Integer language_271;

    @Column
    public Integer language_271_added;

    @Column
    public Integer language_271_removed;

    @Column
    public Integer language_272;

    @Column
    public Integer language_272_added;

    @Column
    public Integer language_272_removed;

    @Column
    public Integer language_273;

    @Column
    public Integer language_273_added;

    @Column
    public Integer language_273_removed;

    @Column
    public Integer language_274;

    @Column
    public Integer language_274_added;

    @Column
    public Integer language_274_removed;

    @Column
    public Integer language_275;

    @Column
    public Integer language_275_added;

    @Column
    public Integer language_275_removed;

    @Column
    public Integer language_276;

    @Column
    public Integer language_276_added;

    @Column
    public Integer language_276_removed;

    @Column
    public Integer language_277;

    @Column
    public Integer language_277_added;

    @Column
    public Integer language_277_removed;

    @Column
    public Integer language_278;

    @Column
    public Integer language_278_added;

    @Column
    public Integer language_278_removed;

    @Column
    public Integer language_279;

    @Column
    public Integer language_279_added;

    @Column
    public Integer language_279_removed;

    @Column
    public Integer language_280;

    @Column
    public Integer language_280_added;

    @Column
    public Integer language_280_removed;

    @Column
    public Integer language_281;

    @Column
    public Integer language_281_added;

    @Column
    public Integer language_281_removed;

    @Column
    public Integer language_282;

    @Column
    public Integer language_282_added;

    @Column
    public Integer language_282_removed;

    @Column
    public Integer language_283;

    @Column
    public Integer language_283_added;

    @Column
    public Integer language_283_removed;

    @Column
    public Integer language_284;

    @Column
    public Integer language_284_added;

    @Column
    public Integer language_284_removed;

    @Column
    public Integer language_285;

    @Column
    public Integer language_285_added;

    @Column
    public Integer language_285_removed;

    @Column
    public Integer language_286;

    @Column
    public Integer language_286_added;

    @Column
    public Integer language_286_removed;

    @Column
    public Integer language_287;

    @Column
    public Integer language_287_added;

    @Column
    public Integer language_287_removed;

    @Column
    public Integer language_288;

    @Column
    public Integer language_288_added;

    @Column
    public Integer language_288_removed;

    @Column
    public Integer language_289;

    @Column
    public Integer language_289_added;

    @Column
    public Integer language_289_removed;

    @Column
    public Integer language_290;

    @Column
    public Integer language_290_added;

    @Column
    public Integer language_290_removed;

    @Column
    public Integer language_291;

    @Column
    public Integer language_291_added;

    @Column
    public Integer language_291_removed;

    @Column
    public Integer language_292;

    @Column
    public Integer language_292_added;

    @Column
    public Integer language_292_removed;

    @Column
    public Integer language_293;

    @Column
    public Integer language_293_added;

    @Column
    public Integer language_293_removed;

    @Column
    public Integer language_294;

    @Column
    public Integer language_294_added;

    @Column
    public Integer language_294_removed;

    @Column
    public Integer language_295;

    @Column
    public Integer language_295_added;

    @Column
    public Integer language_295_removed;

    @Column
    public Integer language_296;

    @Column
    public Integer language_296_added;

    @Column
    public Integer language_296_removed;

    @Column
    public Integer language_297;

    @Column
    public Integer language_297_added;

    @Column
    public Integer language_297_removed;

    @Column
    public Integer language_298;

    @Column
    public Integer language_298_added;

    @Column
    public Integer language_298_removed;

    @Column
    public Integer language_299;

    @Column
    public Integer language_299_added;

    @Column
    public Integer language_299_removed;

    @Column
    public Integer language_300;

    @Column
    public Integer language_300_added;

    @Column
    public Integer language_300_removed;

    @Column
    public Integer language_301;

    @Column
    public Integer language_301_added;

    @Column
    public Integer language_301_removed;

    @Column
    public Integer language_302;

    @Column
    public Integer language_302_added;

    @Column
    public Integer language_302_removed;

    @Column
    public Integer language_303;

    @Column
    public Integer language_303_added;

    @Column
    public Integer language_303_removed;

    @Column
    public Integer language_304;

    @Column
    public Integer language_304_added;

    @Column
    public Integer language_304_removed;

    @Column
    public Integer language_305;

    @Column
    public Integer language_305_added;

    @Column
    public Integer language_305_removed;

    @Column
    public Integer language_306;

    @Column
    public Integer language_306_added;

    @Column
    public Integer language_306_removed;

    @Column
    public Integer language_307;

    @Column
    public Integer language_307_added;

    @Column
    public Integer language_307_removed;

    @Column
    public Integer language_308;

    @Column
    public Integer language_308_added;

    @Column
    public Integer language_308_removed;

    @Column
    public Integer language_309;

    @Column
    public Integer language_309_added;

    @Column
    public Integer language_309_removed;

    @Column
    public Integer language_310;

    @Column
    public Integer language_310_added;

    @Column
    public Integer language_310_removed;

    @Column
    public Integer language_311;

    @Column
    public Integer language_311_added;

    @Column
    public Integer language_311_removed;

    @Column
    public Integer language_312;

    @Column
    public Integer language_312_added;

    @Column
    public Integer language_312_removed;

    @Column
    public Integer language_313;

    @Column
    public Integer language_313_added;

    @Column
    public Integer language_313_removed;

    @Column
    public Integer language_314;

    @Column
    public Integer language_314_added;

    @Column
    public Integer language_314_removed;

    @Column
    public Integer language_315;

    @Column
    public Integer language_315_added;

    @Column
    public Integer language_315_removed;

    @Column
    public Integer language_316;

    @Column
    public Integer language_316_added;

    @Column
    public Integer language_316_removed;

    @Column
    public Integer language_317;

    @Column
    public Integer language_317_added;

    @Column
    public Integer language_317_removed;

    @Column
    public Integer language_318;

    @Column
    public Integer language_318_added;

    @Column
    public Integer language_318_removed;

    @Column
    public Integer language_319;

    @Column
    public Integer language_319_added;

    @Column
    public Integer language_319_removed;

    @Column
    public Integer language_320;

    @Column
    public Integer language_320_added;

    @Column
    public Integer language_320_removed;

    @Column
    public Integer language_321;

    @Column
    public Integer language_321_added;

    @Column
    public Integer language_321_removed;

    @Column
    public Integer language_322;

    @Column
    public Integer language_322_added;

    @Column
    public Integer language_322_removed;

    @Column
    public Integer language_323;

    @Column
    public Integer language_323_added;

    @Column
    public Integer language_323_removed;

    @Column
    public Integer language_324;

    @Column
    public Integer language_324_added;

    @Column
    public Integer language_324_removed;

    @Column
    public Integer language_325;

    @Column
    public Integer language_325_added;

    @Column
    public Integer language_325_removed;

    @Column
    public Integer language_326;

    @Column
    public Integer language_326_added;

    @Column
    public Integer language_326_removed;

    @Column
    public Integer language_327;

    @Column
    public Integer language_327_added;

    @Column
    public Integer language_327_removed;

    @Column
    public Integer language_328;

    @Column
    public Integer language_328_added;

    @Column
    public Integer language_328_removed;

    @Column
    public Integer language_329;

    @Column
    public Integer language_329_added;

    @Column
    public Integer language_329_removed;

    @Column
    public Integer language_330;

    @Column
    public Integer language_330_added;

    @Column
    public Integer language_330_removed;

    @Column
    public Integer language_331;

    @Column
    public Integer language_331_added;

    @Column
    public Integer language_331_removed;

    @Column
    public Integer language_332;

    @Column
    public Integer language_332_added;

    @Column
    public Integer language_332_removed;

    @Column
    public Integer language_333;

    @Column
    public Integer language_333_added;

    @Column
    public Integer language_333_removed;

    @Column
    public Integer language_334;

    @Column
    public Integer language_334_added;

    @Column
    public Integer language_334_removed;

    @Column
    public Integer language_335;

    @Column
    public Integer language_335_added;

    @Column
    public Integer language_335_removed;

    @Column
    public Integer language_336;

    @Column
    public Integer language_336_added;

    @Column
    public Integer language_336_removed;

    @Column
    public Integer language_337;

    @Column
    public Integer language_337_added;

    @Column
    public Integer language_337_removed;

    @Column
    public Integer language_338;

    @Column
    public Integer language_338_added;

    @Column
    public Integer language_338_removed;

    @Column
    public Integer language_339;

    @Column
    public Integer language_339_added;

    @Column
    public Integer language_339_removed;

    @Column
    public Integer language_340;

    @Column
    public Integer language_340_added;

    @Column
    public Integer language_340_removed;

    @Column
    public Integer language_341;

    @Column
    public Integer language_341_added;

    @Column
    public Integer language_341_removed;

    @Column
    public Integer language_342;

    @Column
    public Integer language_342_added;

    @Column
    public Integer language_342_removed;

    @Column
    public Integer language_343;

    @Column
    public Integer language_343_added;

    @Column
    public Integer language_343_removed;

    @Column
    public Integer language_344;

    @Column
    public Integer language_344_added;

    @Column
    public Integer language_344_removed;

    @Column
    public Integer language_345;

    @Column
    public Integer language_345_added;

    @Column
    public Integer language_345_removed;

    @Column
    public Integer language_346;

    @Column
    public Integer language_346_added;

    @Column
    public Integer language_346_removed;

    @Column
    public Integer language_347;

    @Column
    public Integer language_347_added;

    @Column
    public Integer language_347_removed;

    @Column
    public Integer language_348;

    @Column
    public Integer language_348_added;

    @Column
    public Integer language_348_removed;

    @Column
    public Integer language_349;

    @Column
    public Integer language_349_added;

    @Column
    public Integer language_349_removed;

    @Column
    public Integer language_350;

    @Column
    public Integer language_350_added;

    @Column
    public Integer language_350_removed;

    @Column
    public Integer language_351;

    @Column
    public Integer language_351_added;

    @Column
    public Integer language_351_removed;

    @Column
    public Integer language_352;

    @Column
    public Integer language_352_added;

    @Column
    public Integer language_352_removed;

    @Column
    public Integer language_353;

    @Column
    public Integer language_353_added;

    @Column
    public Integer language_353_removed;

    @Column
    public Integer language_354;

    @Column
    public Integer language_354_added;

    @Column
    public Integer language_354_removed;

    @Column
    public Integer language_355;

    @Column
    public Integer language_355_added;

    @Column
    public Integer language_355_removed;

    @Column
    public Integer language_356;

    @Column
    public Integer language_356_added;

    @Column
    public Integer language_356_removed;

    @Column
    public Integer language_357;

    @Column
    public Integer language_357_added;

    @Column
    public Integer language_357_removed;

    @Column
    public Integer language_358;

    @Column
    public Integer language_358_added;

    @Column
    public Integer language_358_removed;

    @Column
    public Integer language_359;

    @Column
    public Integer language_359_added;

    @Column
    public Integer language_359_removed;

    @Column
    public Integer language_360;

    @Column
    public Integer language_360_added;

    @Column
    public Integer language_360_removed;

    @Column
    public Integer language_361;

    @Column
    public Integer language_361_added;

    @Column
    public Integer language_361_removed;

    @Column
    public Integer language_362;

    @Column
    public Integer language_362_added;

    @Column
    public Integer language_362_removed;

    @Column
    public Integer language_363;

    @Column
    public Integer language_363_added;

    @Column
    public Integer language_363_removed;

    @Column
    public Integer language_364;

    @Column
    public Integer language_364_added;

    @Column
    public Integer language_364_removed;

    @Column
    public Integer language_365;

    @Column
    public Integer language_365_added;

    @Column
    public Integer language_365_removed;

    @Column
    public Integer language_366;

    @Column
    public Integer language_366_added;

    @Column
    public Integer language_366_removed;

    @Column
    public Integer language_367;

    @Column
    public Integer language_367_added;

    @Column
    public Integer language_367_removed;

    @Column
    public Integer language_368;

    @Column
    public Integer language_368_added;

    @Column
    public Integer language_368_removed;

    @Column
    public Integer language_369;

    @Column
    public Integer language_369_added;

    @Column
    public Integer language_369_removed;

    @Column
    public Integer language_370;

    @Column
    public Integer language_370_added;

    @Column
    public Integer language_370_removed;

    @Column
    public Integer language_371;

    @Column
    public Integer language_371_added;

    @Column
    public Integer language_371_removed;

    @Column
    public Integer language_372;

    @Column
    public Integer language_372_added;

    @Column
    public Integer language_372_removed;

    @Column
    public Integer language_373;

    @Column
    public Integer language_373_added;

    @Column
    public Integer language_373_removed;

    @Column
    public Integer language_374;

    @Column
    public Integer language_374_added;

    @Column
    public Integer language_374_removed;

    @Column
    public Integer language_375;

    @Column
    public Integer language_375_added;

    @Column
    public Integer language_375_removed;

    @Column
    public Integer language_376;

    @Column
    public Integer language_376_added;

    @Column
    public Integer language_376_removed;

    @Column
    public Integer language_377;

    @Column
    public Integer language_377_added;

    @Column
    public Integer language_377_removed;

    @Column
    public Integer language_378;

    @Column
    public Integer language_378_added;

    @Column
    public Integer language_378_removed;

    @Column
    public Integer language_379;

    @Column
    public Integer language_379_added;

    @Column
    public Integer language_379_removed;

    @Column
    public Integer language_380;

    @Column
    public Integer language_380_added;

    @Column
    public Integer language_380_removed;

    @Column
    public Integer language_381;

    @Column
    public Integer language_381_added;

    @Column
    public Integer language_381_removed;

    @Column
    public Integer language_382;

    @Column
    public Integer language_382_added;

    @Column
    public Integer language_382_removed;

    @Column
    public Integer language_383;

    @Column
    public Integer language_383_added;

    @Column
    public Integer language_383_removed;

    @Column
    public Integer language_384;

    @Column
    public Integer language_384_added;

    @Column
    public Integer language_384_removed;

    @Column
    public Integer language_385;

    @Column
    public Integer language_385_added;

    @Column
    public Integer language_385_removed;

    public UUID getId() {
        return id;
    }
}
