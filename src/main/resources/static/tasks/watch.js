module.exports = function(grunt) {
	grunt.config.merge({
		watch: {
			sass: {
				files: 'sass/**/*.sass',
				tasks: 'sass'
			},
			ts: {
				files: 'typescript/**/*.ts',
				tasks: 'ts'
			},
			test: {
				files: [
					'karma-conf.js',
					'karma-test-shim.js',
					'typescript/**/*.ts'
				],
				tasks: [
					'ts:default',
					'karma:watch:run',
					'remapIstanbul:build'
				],
				options: {
					spawn: false
				}
			}
		}
	});

	grunt.loadNpmTasks('grunt-contrib-watch');

	grunt.event.on('watch', function(action, filepath, subtask) {
		if (subtask === 'test' && action === 'changed') {
			grunt.config.merge({
				ts: {
					'default': {
						files: [
							{
								src: [
									'typescript/admin/appAdminBootstrap.ts',
									filepath
								],
							}
						]
					}
				}
			});
		}
	});
};
