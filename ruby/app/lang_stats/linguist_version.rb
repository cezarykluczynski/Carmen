module LangStats
  class LinguistVersion
    def initialize()
    end

    def get_linguist_version()
      response = `gem list --local | grep linguist`
      linguist_version = response[/\(.*?\)/].chomp(')')
      linguist_version[0] = ''
      response = Hash.new
      response['linguist_version'] = linguist_version
      response
    end
  end
end
