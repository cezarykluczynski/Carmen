package com.cezarykluczynski.carmen.lang.stats.mapper

import com.cezarykluczynski.carmen.lang.stats.domain.Language
import com.cezarykluczynski.carmen.lang.stats.domain.LanguageType
import org.json.JSONObject
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class LinguistLanguageMapperTest {

    LanguageMapper languageMapper

    @BeforeMethod
    void setUp() {
        languageMapper = new LinguistLanguageMapper()
    }

    @Test
    void mapList() {
        String response = '''
            {
                "C2hs Haskell":{
                    "type":"programming",
                    "group":"Haskell",
                    "aliases":["c2hs"],
                    "extensions":[".chs"],
                    "tm_scope":"source.haskell",
                    "ace_mode":"haskell"
                },
                "Haskell":{
                    "type":"programming",
                    "color":"#29b544",
                    "extensions":[".hs",".hsc"],
                    "ace_mode":"haskell"
                },
                "HTML":{
                    "type":"markup",
                    "tm_scope":"text.html.basic",
                    "ace_mode":"html",
                    "color":"#e44b23",
                    "aliases":["xhtml"],
                    "extensions":[".html",".htm",".html.hl",".inc",".st",".xht",".xhtml"]
                }
            }
         '''

        List<Language> languageList = languageMapper.mapList(new JSONObject(response))

        Assert.assertEquals languageList.size(), 3

        Assert.assertEquals languageList.get(0).getName(), "C2hs Haskell"
        Assert.assertEquals languageList.get(0).getParent(), languageList.get(1)
        Assert.assertEquals languageList.get(0).getColor(), "#29b544"
        Assert.assertEquals languageList.get(0).getType(), LanguageType.PROGRAMMING


        Assert.assertEquals languageList.get(1).getName(), "Haskell"
        Assert.assertNull languageList.get(1).getParent()
        Assert.assertEquals languageList.get(1).getColor(), "#29b544"
        Assert.assertEquals languageList.get(1).getType(), LanguageType.PROGRAMMING

        Assert.assertEquals languageList.get(2).getName(), "HTML"
        Assert.assertNull languageList.get(2).getParent()
        Assert.assertEquals languageList.get(2).getColor(), "#e44b23"
        Assert.assertEquals languageList.get(2).getType(), LanguageType.MARKUP
    }

}
