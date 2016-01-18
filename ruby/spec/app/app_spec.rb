require 'rspec'
require 'rack/test'
require 'json'

require File.expand_path '../../../app/app.rb', __FILE__

module RSpecMixin
  include Rack::Test::Methods
  def app() Sinatra::Application end
end

RSpec.configure { |configuration|
  configuration.include RSpecMixin
}

describe 'HTTP interface' do
  it 'should list supported languages' do
    get '/supported_languages'

    expect(last_response).to be_ok
    response = JSON.parse last_response.body

    expect(response['Java']['extensions'][0]).to eq('.java')
  end

  it 'should read repository statistics' do
    post '/detector/describe_repository', {
      :relative_directory => '.',
      :commit_hash => '3fe8afa350b369c6c697290f64da6aa996ede153'
    }

    expect(last_response).to be_ok
    response = JSON.parse last_response.body

    expect(response['Java']).to be_within(10).of(194928)
    expect(response['JavaScript']).to be_within(10).of(1854)
    expect(response['CSS']).to be_within(10).of(318)
    expect(response['Groovy']).to be_within(10).of(193544)
  end

  it 'should read commit statistics' do
    post '/detector/describe_commit', {
      :relative_directory => '.',
      :commit_hash => '21628ec99e149f6509bfb3b3ce8faf8eb2f391c1'
    }

    expect(last_response).to be_ok
    response = JSON.parse last_response.body

    expect(response['Java']['added']).to be_within(1).of(56)
    expect(response['Java']['removed']).to be_within(1).of(4)
    expect(response['Groovy']['added']).to be_within(1).of(101)
    expect(response['Groovy']['removed']).to be_within(1).of(0)
  end

  it 'should report not found commit when describing repository' do
    post '/detector/describe_repository', {
      :relative_directory => '.',
      :commit_hash => 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'
    }

    expect(last_response.status).to eq(404)
    expect(last_response.body).to include('hash not found')
  end

  it 'should report malformed commit hash when describing repository' do
    post '/detector/describe_repository', {
      :relative_directory => '.',
      :commit_hash => 'not_a_hash'
    }

    expect(last_response.status).to eq(400)
    expect(last_response.body).to include('hash is malformed')
  end

  it 'should report not found commit when describing commit' do
    post '/detector/describe_commit', {
      :relative_directory => '.',
      :commit_hash => 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa'
    }

    expect(last_response.status).to eq(404)
    expect(last_response.body).to include('hash not found')
  end

  it 'should report malformed commit hash when describing commit' do
    post '/detector/describe_commit', {
      :relative_directory => '.',
      :commit_hash => 'not_a_hash'
    }

    expect(last_response.status).to eq(400)
    expect(last_response.body).to include('hash is malformed')
  end

  it 'should report errors when parameters are missing' do
    request_without_relative_directory = {
      :commit_hash => '',
    }
    request_without_commit_hash = {
      :relative_directory => '',
    }

    post '/detector/describe_repository', request_without_relative_directory
    expect(last_response.status).to eq(400)
    expect(last_response.body).to include('parameter not found')

    post '/detector/describe_repository', request_without_commit_hash
    expect(last_response.status).to eq(400)
    expect(last_response.body).to include('parameter not found')

    post '/detector/describe_commit', request_without_relative_directory
    expect(last_response.status).to eq(400)
    expect(last_response.body).to include('parameter not found')

    post '/detector/describe_commit', request_without_commit_hash
    expect(last_response.status).to eq(400)
    expect(last_response.body).to include('parameter not found')
  end
end