package com.cezarykluczynski.carmen.lang.stats.mapper

import com.cezarykluczynski.carmen.lang.stats.domain.Language
import com.cezarykluczynski.carmen.lang.stats.domain.LanguageType
import org.json.JSONObject
import spock.lang.Specification

class LinguistLanguageMapperTest extends Specification {

    private static final String RESPONSE = '''
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

    private LanguageMapper languageMapper

    def setup() {
        languageMapper = new LinguistLanguageMapper()
    }

    def "maps list"() {
        when:
        List<Language> languageList = languageMapper.mapLanguageList(new JSONObject(RESPONSE))

        then:
        languageList.size() == 3

        languageList.get(0).getName() == "C2hs Haskell"
        languageList.get(0).getParent() == languageList.get(1)
        languageList.get(0).getColor() == "#29b544"
        languageList.get(0).getType() == LanguageType.PROGRAMMING


        languageList.get(1).getName() == "Haskell"
        languageList.get(1).getParent() == null
        languageList.get(1).getColor() == "#29b544"
        languageList.get(1).getType() == LanguageType.PROGRAMMING

        languageList.get(2).getName() == "HTML"
        languageList.get(2).getParent() == null
        languageList.get(2).getColor() == "#e44b23"
        languageList.get(2).getType() == LanguageType.MARKUP
    }

}
