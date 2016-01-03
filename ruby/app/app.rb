require 'sinatra'
require 'json'
require 'java-properties'

require_relative 'lang_stats/detector'
require_relative 'lang_stats/supported_languages'

properties = JavaProperties.load("src/main/resources/config.properties")
detector_ip = (properties["detector.ip".to_sym] or 8081).to_i

set :bind, '0.0.0.0'
set :port, detector_ip
set :environment, :production

post '/detector/describe_repository' do
  if params['path_to_directory'] and params['commit_hash']
    detector = LangStats::Detector.new
    return detector.describe_repository(params['path_to_directory'], params['commit_hash']).to_json
  else
    status 400
    {
      :error => '"path_to_directory" parameter not found.'
    }.to_json
  end
end

post '/detector/describe_commit' do
  if params['path_to_directory'] and params['commit_hash']
    detector = LangStats::Detector.new
    return detector.describe_commit(params['path_to_directory'], params['commit_hash']).to_json
  else
    status 400
    {
      :error => '"path_to_directory" or "commit_hash" parameter not found.'
    }.to_json
  end
end

get '/supported_languages' do
  supported_languages = LangStats::SupportedLanguages.new
  supported_languages.get_supported_languages.to_json
end
