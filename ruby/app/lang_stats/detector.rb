require 'rugged'
require_relative 'exceptions'
require_relative 'linguist/repository'

module LangStats
  class Detector

    def initialize()
    end

    def describe_repository(path_to_repository, commit_hash)
      project = get_repository(path_to_repository, commit_hash)
      return get_statistics_or_translate_vendor_exceptions project, "languages"
    end

    def describe_commit(path_to_repository, commit_hash)
      project = get_repository(path_to_repository, commit_hash)
      cache = get_statistics_or_translate_vendor_exceptions(project, "cache")
      affected_files = get_files_affected_by_commit path_to_repository, commit_hash
      return get_commit_stats cache, affected_files
    end

    private

    def get_repository(path_to_repository, commit_hash)
      repository = Rugged::Repository.new(path_to_repository)
      LangStats::Repository.new(repository, commit_hash)
    end

    def get_files_affected_by_commit(path_to_repository, commit_hash)
      numstat = get_raw_files_affected_by_commit path_to_repository, commit_hash

      raise RuntimeError unless numstat.is_a?(String)

      parse_files_affected_by_commit_into_stats numstat
    end

    def get_statistics_or_translate_vendor_exceptions(project, accessor_method_name)
      begin
        return project.send(accessor_method_name)
      rescue Rugged::OdbError => e
        raise CommitHashNotFoundError
      rescue Rugged::InvalidError => e
        raise CommitHashMalformedException
      end
    end

    def get_raw_files_affected_by_commit(path_to_repository, commit_hash)
      numstat = nil

      begin
        numstat = `cd #{path_to_repository} && git show --numstat --format= #{commit_hash}`
      ensure
        system 'cd -'
      end

      numstat
    end

    def parse_files_affected_by_commit_into_stats(numstat)
      affected_files = Hash.new

      numstat.each_line { |line|
        parsed_line = line.sub(/\n$/, '').split(/\t/, 3)
        added_lines = parsed_line[0] == '-' ? nil : parsed_line[0].to_i
        removed_lines = parsed_line[1] == '-' ? nil : parsed_line[1].to_i
        affected_files[parsed_line[2]] = [added_lines, removed_lines]
      }

      affected_files
    end

    def get_commit_stats(files_languages, relevant_files)
      languages_stats = Hash.new

      relevant_files.each{ |file_name, file_line_stats|
        file_metrics = files_languages[file_name]

        next if file_metrics.nil?

        language = file_metrics[0]

        languages_stats[language] = {
          :added => 0,
          :removed => 0
        } if languages_stats[language].nil?

        languages_stats[language][:added] += file_line_stats[0]
        languages_stats[language][:removed] += file_line_stats[1]
      }

      languages_stats
    end
  end
end
