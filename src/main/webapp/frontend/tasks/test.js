module.exports = function(grunt) {
	grunt.config.merge({
		karma: {
			unit: {
				configFile: 'karma.conf.js'
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
	});
};
