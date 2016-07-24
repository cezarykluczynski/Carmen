require 'linguist'

# :nocov:
module LangStats
  class Repository < Linguist::Repository
    # copy of https://github.com/github/linguist/tree/master/lib/linguist,
    # with LOC calculated instead of blob size
    def compute_stats(old_commit_oid, cache = nil)
      return {} if current_tree.count_recursive(MAX_TREE_SIZE) >= MAX_TREE_SIZE

      old_tree = old_commit_oid && Rugged::Commit.lookup(repository, old_commit_oid).tree
      read_index
      diff = Rugged::Tree.diff(repository, old_tree, current_tree)

      # Clear file map and fetch full diff if any .gitattributes files are changed
      if cache && diff.each_delta.any? { |delta| File.basename(delta.new_file[:path]) == ".gitattributes" }
        diff = Rugged::Tree.diff(repository, old_tree = nil, current_tree)
        file_map = {}
      else
        file_map = cache ? cache.dup : {}
      end

      diff.each_delta do |delta|
        old = delta.old_file[:path]
        new = delta.new_file[:path]

        file_map.delete(old)
        next if delta.binary

        if [:added, :modified].include? delta.status
          # Skip submodules and symlinks
          mode = delta.new_file[:mode]
          mode_format = (mode & 0170000)
          next if mode_format == 0120000 || mode_format == 040000 || mode_format == 0160000

          blob = Linguist::LazyBlob.new(repository, delta.new_file[:oid], new, mode.to_s(8))

          if blob.include_in_language_stats?
            file_map[new] = [blob.language.group.name, blob.loc]
          end

          blob.cleanup!
        end
      end

      file_map
    end
  end
end
# :nocov:
