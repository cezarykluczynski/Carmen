require 'simplecov'
SimpleCov.start do
  add_filter '/spec/'
  coverage_dir 'build/simplecov/'
end

if ENV['CI'] == 'true'
  require 'codecov'
  SimpleCov.formatter = SimpleCov::Formatter::Codecov
end