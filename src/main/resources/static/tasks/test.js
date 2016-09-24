module.exports = function(grunt) {
	grunt.config.merge({
		karma: {
			unit: {
				configFile: 'karma.conf.js'
			},
			watch: {
				configFile: 'karma.conf.js',
				background: true,
				singleRun: false
			}
		},
		clean: ['coverage/**', 'dist/**'],
		remapIstanbul: {
			build: {
				src: 'coverage/coverage-js.json',
				options: {
					reports: {
						'json': 'coverage/coverage-final.json',
						'html': 'coverage/html'
					}
				}
			}
		}
	});

	grunt.loadNpmTasks('grunt-karma');
	grunt.loadNpmTasks('grunt-contrib-clean');
	grunt.loadNpmTasks('remap-istanbul');

	grunt.registerTask('test', function () {
		grunt.task.run('clean');
		grunt.task.run('build');
		grunt.task.run('karma');
		grunt.task.run('remapIstanbul:build');
		grunt.task.run('watch:test');
	});

	grunt.registerTask('test-dirty', function () {
		grunt.task.run('clean');
		grunt.task.run('build-dirty');
		grunt.task.run('karma');
		grunt.task.run('remapIstanbul:build');
		grunt.task.run('watch:test');
	});

	grunt.registerTask('test-dirty-no-cov', function () {
		grunt.task.run('clean');
		grunt.task.run('build-dirty');
		grunt.task.run('karma');
		grunt.task.run('watch:test');
	});

	grunt.registerTask('test-once', function () {
		grunt.task.run('clean');
		grunt.task.run('build');
		grunt.task.run('karma');
		grunt.task.run('remapIstanbul:build');
	});

	grunt.registerTask('test-no-cov-once', function () {
		grunt.task.run('clean');
		grunt.task.run('build');
		grunt.task.run('karma');
	});

	grunt.registerTask('test-dirty-once', function () {
		grunt.task.run('clean');
		grunt.task.run('build-dirty');
		grunt.task.run('karma');
		grunt.task.run('remapIstanbul:build');
	});

	grunt.registerTask('test-dirty-no-cov-once', function () {
		grunt.task.run('clean');
		grunt.task.run('build-dirty');
		grunt.task.run('karma');
	});

	grunt.registerTask('t', 'test');
	grunt.registerTask('td', 'test-dirty');
	grunt.registerTask('tdnc', 'test-dirty-no-cov');
	grunt.registerTask('to', 'test-once');
	grunt.registerTask('tnco', 'test-no-cov-once');
	grunt.registerTask('tdo', 'test-dirty-once');
	grunt.registerTask('tdnco', 'test-dirty-no-cov-once');
};
