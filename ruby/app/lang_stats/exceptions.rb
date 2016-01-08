module LangStats
  class CommitHashNotFoundError < StandardError
    def initialize
    end
  end

  class CommitHashMalformedException < StandardError
    def initialize
    end
  end
end