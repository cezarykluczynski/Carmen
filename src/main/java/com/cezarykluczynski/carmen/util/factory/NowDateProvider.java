package com.cezarykluczynski.carmen.util.factory;

import com.cezarykluczynski.carmen.util.DateUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NowDateProvider {

    public Date createNowDate() {
        return DateUtil.now();
    }

}
