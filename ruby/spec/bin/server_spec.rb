require 'rspec'
require 'json'

was_stopped = false
pid_path = './ruby/bin/.server_pid'

describe 'server' do
  before(:all) do
    `ruby ./ruby/bin/server stop`
    was_stopped = ($?.exitstatus == 0)
  end

  it 'should start once' do
    response = `ruby ./ruby/bin/server start`

    expect(response).to include('Server started')
    expect($?.exitstatus).to eq(0)

    response = `ruby ./ruby/bin/server start`

    expect(response).to include('already running')
    expect($?.exitstatus).to eq(1)

    `ruby ./ruby/bin/server stop`
  end

  it 'should force start' do
    response = `ruby ./ruby/bin/server start`
    pid_before = File.read pid_path

    response = `ruby ./ruby/bin/server start --force`
    expect(response).to include('Server stopped')
    expect(response).to include('Server started')
    expect($?.exitstatus).to eq(0)

    pid_after = File.read pid_path
    expect(pid_before).not_to eq(pid_after)

    `ruby ./ruby/bin/server stop`
  end

  it 'should stop once when .server_pid is missing' do
    `ruby ./ruby/bin/server start`

    response = `ruby ./ruby/bin/server stop`
    expect(response).to include('Server stopped')
    expect($?.exitstatus).to eq(0)

    response = `ruby ./ruby/bin/server stop`
    expect(response).to include('Server already stopped')
    expect(response).to include('.server_pid not found')
    expect($?.exitstatus).to eq(1)
  end

  it 'should stop once when process was stopped by other means' do
    `ruby ./ruby/bin/server start`
    pid = File.read pid_path
    Process.kill 'SIGINT', pid.to_i

    response = `ruby ./ruby/bin/server stop`
    expect(response).to include('Server already stopped')
    expect(response).to include(pid << ' is not running')
    expect($?.exitstatus).to eq(1)
  end

  it 'should restart two times' do
    `ruby ./ruby/bin/server start`

    response = `ruby ./ruby/bin/server restart`
    expect(response).to include('Server stopped')
    expect(response).to include('Server started')
    expect($?.exitstatus).to eq(0)

    response = `ruby ./ruby/bin/server restart`
    expect(response).to include('Server stopped')
    expect(response).to include('Server started')
    expect($?.exitstatus).to eq(0)
  end

  it 'should restart when it was stopped' do
    `ruby ./ruby/bin/server stop`

    response = `ruby ./ruby/bin/server restart`
    expect(response).to include('Server already stopped')
    expect(response).to include('Server started')
    expect($?.exitstatus).to eq(0)
  end

  after(:all) do
    if (was_stopped) then `ruby ./ruby/bin/server start` end
  end
end