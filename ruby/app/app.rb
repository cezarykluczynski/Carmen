require 'sinatra'
require 'json'
require 'java-properties'

require_relative 'lang_stats/detector'
require_relative 'lang_stats/supported_languages'

properties = JavaProperties.load(File.expand_path(File.dirname(__FILE__)) << "/../../src/main/resources/config.properties")
detector_port = (properties["detector.port".to_sym] or 8081).to_i

set :bind, '0.0.0.0'
set :port, detector_port
set :environment, :development

post '/detector/describe_repository' do
  if params['relative_directory'] and params['commit_hash']
    detector = LangStats::Detector.new
    begin
      return detector.describe_repository(params['relative_directory'], params['commit_hash']).to_json
    rescue LangStats::CommitHashNotFoundError => e
      status 404
      {
        :error => 'Commit hash not found.'
      }.to_json
    rescue LangStats::CommitHashMalformedException => e
      status 400
      {
        :error => 'Commit hash is malformed.'
      }.to_json
    end
  else
    status 400
    {
      :error => '"relative_directory" parameter not found.'
    }.to_json
  end
end

post '/detector/describe_commit' do
  if params['relative_directory'] and params['commit_hash']
    detector = LangStats::Detector.new
    begin
      return detector.describe_commit(params['relative_directory'], params['commit_hash']).to_json
    rescue LangStats::CommitHashNotFoundError => e
      status 404
      {
        :error => 'Commit hash not found.'
      }.to_json
    rescue LangStats::CommitHashMalformedException => e
      status 400
      {
        :error => 'Commit hash is malformed.'
      }.to_json
    end
  else
    status 400
    {
      :error => '"relative_directory" or "commit_hash" parameter not found.'
    }.to_json
  end
end

get '/supported_languages' do
  supported_languages = LangStats::SupportedLanguages.new
  supported_languages.get_supported_languages.to_json
end
