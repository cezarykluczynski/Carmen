require 'linguist'

module LangStats
  class SupportedLanguages
    def initialize()
    end

    def get_supported_languages()
      linguist_path = $:.grep(/linguist/).first
      languages_path = '' << linguist_path << '/linguist/languages.yml'
      YAML.load File.read(languages_path)
    end
  end
end
