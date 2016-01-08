require 'rspec'
require 'json'

describe 'CLI interface' do
  it 'should respond' do
    begin
      expect(`./ruby/bin/lang_stats`.include? 'Usage:').to eq(true)
    rescue
      expect(false).to eq(true)
    end
  end

  it 'should list supported languages' do
    response = JSON.parse `./ruby/bin/lang_stats supported_languages`

    expect(response['Java']['extensions'][0]).to eq('.java')
  end

  it 'should read repository statistics' do
    response = JSON.parse `./ruby/bin/lang_stats describe_repository . 3fe8afa350b369c6c697290f64da6aa996ede153`

    expect(response['Java']).to be_within(10).of(194928)
    expect(response['JavaScript']).to be_within(10).of(1854)
    expect(response['CSS']).to be_within(10).of(318)
    expect(response['Groovy']).to be_within(10).of(193544)
  end

  it 'should repo commit statistics' do
    response = JSON.parse `./ruby/bin/lang_stats describe_commit . 21628ec99e149f6509bfb3b3ce8faf8eb2f391c1`

    expect(response['Java']['added']).to be_within(1).of(56)
    expect(response['Java']['removed']).to be_within(1).of(4)
    expect(response['Groovy']['added']).to be_within(1).of(101)
    expect(response['Groovy']['removed']).to be_within(1).of(0)
  end

  it 'should report not found commit when describing repository' do
    response = `./ruby/bin/lang_stats describe_repository . aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa`
    expect(response).to include('hash not found')
    expect($?.exitstatus).to eq(1)
  end

  it 'should report malformed commit hash when describing repository' do
    response = `./ruby/bin/lang_stats describe_repository . not_a_hash`
    expect(response).to include('hash is malformed')
    expect($?.exitstatus).to eq(1)
  end

  it 'should report not found commit when describing commit' do
    response = `./ruby/bin/lang_stats describe_commit . aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa`
    expect(response).to include('hash not found')
    expect($?.exitstatus).to eq(1)
  end

  it 'should report malformed commit hash when describing commit' do
    response = `./ruby/bin/lang_stats describe_commit . not_a_hash`
    expect(response).to include('hash is malformed')
    expect($?.exitstatus).to eq(1)
  end
end